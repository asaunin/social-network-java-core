package dao;

import dao.interfaces.Dao;
import dao.jdbc.MessageDaoImpl;
import dao.jdbc.UserDaoImpl;
import model.Message;
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

public class MessageDaoImplTest {

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
    private static MessageDaoImpl messageDao;
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
        messageDao = ds::getConnection;

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
        messageDao.removeUser(firstUser);
        messageDao.removeUser(secondUser);
        assertThat(messageDao.getAll(firstUser, secondUser).size(), is(0));
        userDao.removeUser(firstUser);
        userDao.removeUser(secondUser);
        assertThat(userDao.getNumberOfUsers(0L), is(0L));
    }

    @Test
    public void messagesTest() {

        List<Message> conversation;

        //Add friend & check
        messageDao.addNew(firstUser, secondUser, "Hallo, " + SECOND_USER_LAST_NAME);
        assertThat(messageDao.getAll(firstUser, secondUser).size(), is(1));
        assertThat(messageDao.getAll(secondUser, firstUser).size(), is(1));

        final Message message = messageDao.getLast(firstUser).get(0);
        assertThat(message.getBody().equals("Hallo, " + SECOND_USER_LAST_NAME), is(true));
        assertThat(message.getSender().equals(firstUser), is(true));
        assertThat(message.getRecipient().equals(secondUser), is(true));

        messageDao.addNew(secondUser, firstUser, "Hallo, " + FIRST_USER_LAST_NAME);
        assertThat(messageDao.getAll(firstUser, secondUser).size(), is(2));
        assertThat(messageDao.getAll(secondUser, firstUser).size(), is(2));

    }


}
