package dao;

import dao.interfaces.Dao;
import dao.jdbc.FriendsDaoImpl;
import dao.jdbc.UserDaoImpl;
import model.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.*;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FriendsDaoImplTest {

    private static final String RESOURCES_FILE_PATH = "src/test/resources/";
    private static final String DB_PROPERTIES_FILE_NAME = "db.properties";
    private static final String DB_SCHEMA_FILE_NAME = "db.sql";

    private static final String FIRST_USER_EMAIL = "doe@mail.ru";
    private static final String SECOND_USER_EMAIL = "snow@mail.ru";
    private static final String USER_PASSWORD = "qwerty";
    private static final String USER_FIRST_NAME = "John";
    private static final String FIRST_USER_LAST_NAME = "Doe";
    private static final String SECOND_USER_LAST_NAME = "Snow";

    private static DataSource ds;
    private static UserDaoImpl userDao;
    private static FriendsDaoImpl friendsDao;
    private User firstUser;
    private User secondUser;

    /**
     * Initializes connection pool for future tests
     * Clears all data in test database
     */
    @BeforeClass
    public static void initialiseDb() throws Exception {

        PoolProperties poolProp = new PoolProperties();
        Properties prop = new Properties();

        InputStream resourceAsStream =
                Files.newInputStream(
                        Paths.get(RESOURCES_FILE_PATH, DB_PROPERTIES_FILE_NAME));
        prop.load(resourceAsStream);
        poolProp.setDriverClassName(prop.getProperty("driverClassName"));
        poolProp.setUrl(prop.getProperty("url"));
        poolProp.setUsername(prop.getProperty("user"));
        poolProp.setPassword(prop.getProperty("password"));
        ds = new DataSource(poolProp);
        ds.setPoolProperties(poolProp);
        String[] sqls =
                Files.lines(
                        Paths.get(RESOURCES_FILE_PATH, DB_SCHEMA_FILE_NAME))
                        .collect(Collectors.joining())
                        .split(";");
        Dao conn = ds::getConnection;
        conn.batch(sqls);

        userDao = ds::getConnection;
        friendsDao = ds::getConnection;

    }

    @AfterClass
    public static void finalizeDb() throws Exception {
        ds.close();
    }

    /**
     * Fills DB with initial data for user tests:
     * Add test users
     */
    @Before
    public void beforeTest() {
        assertThat(userDao.getNumberOfUsers(0L), is(0L));
        firstUser = userDao.addUser(FIRST_USER_EMAIL, USER_PASSWORD, USER_FIRST_NAME, FIRST_USER_LAST_NAME).get();
        secondUser = userDao.addUser(SECOND_USER_EMAIL, USER_PASSWORD, USER_FIRST_NAME, SECOND_USER_LAST_NAME).get();
        assertThat(userDao.getNumberOfUsers(0L), is(2L));
    }

    /**
     * Clears initial data:
     * Remove test users
     */
    @After
    public void afterTest() {
        assertThat(userDao.getNumberOfUsers(0L), is(2L));
        friendsDao.removeFriend(firstUser, secondUser);
        friendsDao.removeFriend(secondUser, firstUser);
        userDao.removeUser(firstUser);
        userDao.removeUser(secondUser);
        assertThat(userDao.getNumberOfUsers(0L), is(0L));
    }

    @Test
    public void friendsTest() {

        List<User> friends;

        //Add friend & check
        friendsDao.addFriend(firstUser, secondUser);
        assertThat(friendsDao.getNumberOfFriends(firstUser, ""), is(1L));
        assertThat(friendsDao.getNumberOfFriends(firstUser, FIRST_USER_LAST_NAME), is(0L));
        assertThat(friendsDao.getNumberOfFriends(firstUser, SECOND_USER_LAST_NAME), is(1L));
        assertThat(friendsDao.getNumberOfFriends(secondUser, ""), is(0L));

        friends = friendsDao.getFriendList(firstUser, 10, 0, "");
        assertThat(friends.size(), is(1));
        assertThat(friends.contains(secondUser), is(true));

        friends = friendsDao.getFriendList(firstUser, 10, 0, SECOND_USER_LAST_NAME);
        assertThat(friends.size(), is(1));
        assertThat(friends.contains(secondUser), is(true));

        friends = friendsDao.getFriendList(firstUser, 10, 0, FIRST_USER_LAST_NAME);
        assertThat(friends.size(), is(0));

        firstUser = userDao.getUserById(firstUser.getId(), secondUser.getId()).get();
        assertThat(firstUser.isIsfriendofuser(), is(true));
        assertThat(firstUser.isIsuserfriend(), is(false));

        //Remove friend & check
        friendsDao.removeFriend(firstUser, secondUser);
        assertThat(friendsDao.getNumberOfFriends(firstUser, ""), is(0L));
        assertThat(friendsDao.getNumberOfFriends(firstUser, FIRST_USER_LAST_NAME), is(0L));
        assertThat(friendsDao.getNumberOfFriends(firstUser, SECOND_USER_LAST_NAME), is(0L));
        assertThat(friendsDao.getNumberOfFriends(secondUser, ""), is(0L));

        friends = friendsDao.getFriendList(firstUser, 10, 0, "");
        assertThat(friends.size(), is(0));

    }


}
