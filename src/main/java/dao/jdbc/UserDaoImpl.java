package dao.jdbc;

import dao.beans.FriendBean;
import dao.beans.LongBean;
import dao.interfaces.UserDao;
import model.User;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface UserDaoImpl extends UserDao {

    Logger log = Logger.getLogger(UserDaoImpl.class); // TODO: 21.07.2016 Разобраться с логгером интерфейса

    String SQL_GET_NUMBER_OF_USERS =
            "SELECT count(id) AS id FROM users WHERE id != ?;";
    String SQL_ADD_NEW =
            "INSERT INTO users (email, password, first_name, last_name) VALUES (?, MD5(?), ?, ?);";
    String SQL_ADD_FRIEND =
            "INSERT INTO friends (userid, friendid) VALUES (?, ?);";
    String SQL_REMOVE_FRIEND =
            "DELETE FROM friends WHERE userid = ? AND friendid = ?;";

    String SQL_GET_BY_EMAIL =
            "SELECT id, email, first_name, last_name, birth_date, reg_date, sex, CONCAT_WS(' ', first_name, last_name) AS name FROM users WHERE email = ?;";
    String SQL_GET_BY_EMAIL_AND_PASSWORD =
            "SELECT id, email, first_name, last_name, birth_date, reg_date, sex, CONCAT_WS(' ', first_name, last_name) AS name FROM users WHERE email = ? AND password = MD5(?);";
    String SQL_GET_BY_ID =
            "SELECT id, email, first_name, last_name, birth_date, reg_date, sex, CONCAT_WS(' ', first_name, last_name) AS name FROM users WHERE id = ?;";
    String SQL_GET_BY_ID_AND_FRIEND =
        "SELECT id, email, first_name, last_name, birth_date, reg_date, sex, " +
                "CONCAT_WS(' ', first_name, last_name) AS name, " +
                "CASE WHEN userfriends.friendid ISNULL THEN FALSE ELSE TRUE END AS isuserfriend, " +
                "CASE WHEN friendofuser.userid ISNULL THEN FALSE ELSE TRUE END AS isfriendofuser " +
                "FROM users " +
                "LEFT JOIN friends AS userfriends ON users.id = userfriends.friendid AND userfriends.userid = ? " +
                "LEFT JOIN friends AS friendofuser ON users.id = friendofuser.userid AND friendofuser.friendid = ? " +
                "WHERE id = ?;";
    String SQL_GET_ALL =
            "SELECT id, email, first_name, last_name, birth_date, reg_date, sex, " +
                    "CONCAT_WS(' ', first_name, last_name) AS name, " +
                    "CASE WHEN userfriends.friendid ISNULL THEN FALSE ELSE TRUE END AS isuserfriend, " +
                    "CASE WHEN friendofuser.userid ISNULL THEN FALSE ELSE TRUE END AS isfriendofuser " +
                    "FROM users " +
                    "LEFT JOIN friends AS userfriends ON users.id = userfriends.friendid AND userfriends.userid = ? " +
                    "LEFT JOIN friends AS friendofuser ON users.id = friendofuser.userid AND friendofuser.friendid = ? " +
                    "ORDER BY name " +
                    "limit ? offset ?;";

    @Override
    default long getNumberOfUsers(User currentUser) {

        Optional<LongBean> bean = select(LongBean.class, SQL_GET_NUMBER_OF_USERS, currentUser.getId());
        if (bean.isPresent())
            return bean.get().getId();
        else
            return 0L;

    }

    @Override
    default Optional<User> getById(long id) {
        return select(User.class, SQL_GET_BY_ID, id);
    }

    @Override
    default Optional<User> getById(long id, long profileid) {
        return select(User.class, SQL_GET_BY_ID_AND_FRIEND, id, profileid, id);
    }

    @Override
    default Optional<User> getByEmailPassword(String email, String password) {
        return select(User.class, SQL_GET_BY_EMAIL_AND_PASSWORD, email, password);
    }

    @Override
    default Optional<User> getByEmail(String email) {
        return select(User.class, SQL_GET_BY_EMAIL, email);
    }

    @Override
    default List<User> getAll(User user, long recordsPerPage, long offset, String searchParam) {
        return (List<User>) selectCollection(User.class, SQL_GET_ALL, user.getId(), user.getId(), recordsPerPage, offset);
    }

    @Override
    default Optional<User> addNew(String email, String password, String first_name, String last_name) {
        return insert(User.class, SQL_ADD_NEW, email, password, first_name, last_name);
    }

    @Override
    default void addFriend(User user, User friend) {
        insert(FriendBean.class, SQL_ADD_FRIEND, user.getId(), friend.getId());
    }

    @Override
    default void removeFriend(User user, User friend) {
        delete(SQL_REMOVE_FRIEND, user.getId(), friend.getId());
    }
}