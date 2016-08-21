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
import java.sql.Date;
import java.util.Calendar;
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
    private static final String USER_PASSWORD = "qwerty";
    private static final String USER_FIRST_NAME = "John";
    private static final String USER_LAST_NAME = "Doe";
    private static final String USER_WRONG_NAME = "Ben";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static DataSource ds;
    private static UserDaoImpl userDao;
    private User testUser;

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
        userDao = ds::getConnection;

        Dao conn = ds::getConnection;
        conn.batch(sqls);

    }

    @AfterClass
    public static void finalizeDb() throws Exception {
        ds.close();
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
        assertThat(userDao.getNumberOfUsers(0L, USER_FIRST_NAME), is(1L));
        assertThat(userDao.getNumberOfUsers(0L, USER_WRONG_NAME), is(0L));
    }

    @Test
    public void getByIdTest() {
        final Optional<User> userById = userDao.getUserById(testUser.getId());
        assertThat(userById.isPresent(), is(true));
        assertThat(userById.get().equals(testUser), is(true));
        assertThat(userById.get().getName(), is(USER_FIRST_NAME + " " + USER_LAST_NAME));
    }

    @Test
    public void getByEmailTest() {
        final Optional<User> userById = userDao.getUserByEmail(USER_EMAIL);
        assertThat(userById.isPresent(), is(true));
        assertThat(userById.get().equals(testUser), is(true));
    }

    @Test
    public void getByEmailPasswordTest() {
        final Optional<User> userByEmailPassword = userDao.getUserByEmailPassword(USER_EMAIL, USER_PASSWORD);
        assertThat(userByEmailPassword.isPresent(), is(true));
        assertThat(userByEmailPassword.get().equals(testUser), is(true));
    }

    @Test
    public void getListTest() {

        List<User> list = userDao.getUserList(testUser, 10, 0, "");
        assertThat(list.size(), is(0));

        list = userDao.getUserList(new User(), 10, 0, "");
        assertThat(list.size(), is(1));
        assertThat(list.get(0).equals(testUser), is(true)); // TODO: 18.08.2016 Переделать на hamcrest matcher по работе с коллекциями hasItem  

        list = userDao.getUserList(new User(), 10, 0, "John");
        assertThat(list.size(), is(1));

        list = userDao.getUserList(new User(), 10, 0, USER_WRONG_NAME);
        assertThat(list.size(), is(0));

    }

    @Test
    public void changePasswordTest() {

        userDao.changePassword(testUser, USER_PASSWORD.toUpperCase());
        assertThat(userDao.getUserByEmailPassword(USER_EMAIL, USER_PASSWORD.toUpperCase()).get().getId(), is(testUser.getId()));

        userDao.changePassword(testUser, USER_PASSWORD);
        assertThat(userDao.getUserByEmailPassword(USER_EMAIL, USER_PASSWORD).get().getId(), is(testUser.getId()));

    }

    @Test
    public void updateUserTest() {

        final Date birth_date = new Date(Calendar.getInstance().getTime().getTime());
        final String phone = "+7 (911) 094-04-57";
        final String sex = "2";

        testUser.setPhone(phone);
        testUser.setBirth_date(birth_date);
        testUser.setSex(sex);
        userDao.updateUser(testUser);

        final User userById = userDao.getUserById(testUser.getId()).get();
        assertThat(userById.equals(testUser), is(true));


    }

    @Test
    public void newUserTest() {
        final User user = new User(testUser);
        assertThat(user.equals(testUser), is(true));
    }

    @Test(expected = RuntimeException.class)
    public void addExistingUserTest() {
        final Optional<User> user = userDao.addUser(USER_EMAIL, USER_PASSWORD, USER_FIRST_NAME, USER_LAST_NAME);
    }

}
