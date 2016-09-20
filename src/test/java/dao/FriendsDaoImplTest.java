package dao;

import dao.jdbc.FriendsDaoImpl;
import dao.jdbc.UserDaoImpl;
import model.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.*;

import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FriendsDaoImplTest {

    private static final String FIRST_USER_EMAIL = "doe@mail.ru";
    private static final String SECOND_USER_EMAIL = "snow@mail.ru";
    private static final String USER_PASSWORD = "qwerty";
    private static final String USER_FIRST_NAME = "John";
    private static final String FIRST_USER_LAST_NAME = "Doe";
    private static final String SECOND_USER_LAST_NAME = "Snow";

    private static UserDaoImpl userDao;
    private static FriendsDaoImpl friendsDao;
    private User firstUser;
    private User secondUser;

    @BeforeClass
    public static void initialiseDb() throws Exception {
        DataSource ds = TestSuite.getDataSource();
        userDao = ds::getConnection;
        friendsDao = ds::getConnection;
    }

    @AfterClass
    public static void finalizeDb() throws Exception {
        TestSuite.closeDataSource();
    }

    @Before
    public void beforeTest() {
        assertThat(userDao.getNumberOfUsers(0L), is(0L));
        firstUser = userDao.addUser(FIRST_USER_EMAIL, USER_PASSWORD, USER_FIRST_NAME, FIRST_USER_LAST_NAME).get();
        secondUser = userDao.addUser(SECOND_USER_EMAIL, USER_PASSWORD, USER_FIRST_NAME, SECOND_USER_LAST_NAME).get();
        assertThat(userDao.getNumberOfUsers(0L), is(2L));
    }

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

        Collection<User> friends;

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
