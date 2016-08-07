package dao;

import dao.interfaces.Dao;
import dao.jdbc.UserDaoImpl;
import model.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserDaoImplTest {

    private static final String RESOURCES_FILE_PATH = "src/test/resources/";
    private static final String DB_PROPERTIES_FILE_NAME = "db.properties";
    private static final String DB_SCHEMA_FILE_NAME = "db.sql";

    private static final String USER_EMAIL = "john@mail.ru";
    private static String USER_PASSWORD = "qwerty";
    private static String USER_FIRST_NAME = "John";
    private static String USER_LAST_NAME = "Doe";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static UserDaoImpl userDao;
    private User testUser;

    /**
     * Initializes connection pool for future tests
     * Clears all data in test database
     */
    @BeforeClass
    public static void initialiseDb() throws Exception{

        PoolProperties poolProp = new PoolProperties();
        Properties prop = new Properties();
//        try {
            InputStream resourceAsStream =
                    Files.newInputStream(
                            Paths.get(RESOURCES_FILE_PATH, DB_PROPERTIES_FILE_NAME));
            prop.load(resourceAsStream);
            poolProp.setDriverClassName(prop.getProperty("driverClassName"));
            poolProp.setUrl(prop.getProperty("url"));
            poolProp.setUsername(prop.getProperty("user"));
            poolProp.setPassword(prop.getProperty("password"));
            DataSource ds = new DataSource(poolProp);
            ds.setPoolProperties(poolProp);
            String[] sqls =
                    Files.lines(
                            Paths.get(RESOURCES_FILE_PATH, DB_SCHEMA_FILE_NAME))
                            .collect(Collectors.joining())
                            .split(";");
            Dao conn = ds::getConnection;
            userDao = (UserDaoImpl) ds::getConnection;
            conn.batch(sqls);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Fills DB with initial data for user tests:
     * Adds test user
     */
    @Before
    public void addUserTest() {
        assertThat(userDao.getNumberOfUsers(0L), is(0L));
        final Optional<User> user = userDao.addUser(USER_EMAIL, USER_PASSWORD, USER_FIRST_NAME, USER_LAST_NAME);
        assertThat(user.isPresent(), is(true));
        testUser = user.get();
        assertThat(userDao.getNumberOfUsers(0L), is(1L));
    }

    /**
     * Clears initial data:
     * Removes test user
     */
    @After
    public void removeUserTest() {
        assertThat(userDao.getNumberOfUsers(0L), is(1L));
        userDao.removeUser(testUser);
        assertThat(userDao.getNumberOfUsers(0L), is(0L));
    }

    @Test
    public void getNumberOfUsersTest() {
        assertThat(userDao.getNumberOfUsers(0L), is(1L));
    }

    @Test
    public void getByIdTest() {
        final Optional<User> userById = userDao.getUserById(testUser.getId());
        assertThat(userById.isPresent(), is(true));
        assertThat(userById.get().equals(testUser), is(true));
        assertThat(userById.get().getName(), is("John Doe"));
    }

    @Test
    public void getByEmailTest() {
        final Optional<User> userById = userDao.getUserByEmail(USER_EMAIL);
        assertThat(userById.isPresent(), is(true));
        assertThat(userById.get().equals(testUser), is(true));
    }

    @Test
    public void getByEmailPasswordTest() {
        final Optional<User> userById = userDao.getUserByEmailPassword(USER_EMAIL, USER_PASSWORD);
        assertThat(userById.isPresent(), is(true));
        assertThat(userById.get().equals(testUser), is(true));
    }

    @Test
    public void getListTest() {
        final List<User> list = userDao.getUserList(testUser, 10, 0, "");
        assertThat(list.size(), is(1));
        assertThat(list.get(0).equals(testUser), is(true));
    }

    @Test(expected = RuntimeException.class)
    public void addExistingUserTest() {
        final Optional<User> user = userDao.addUser(USER_EMAIL, USER_PASSWORD, USER_FIRST_NAME, USER_LAST_NAME);
    }

}
