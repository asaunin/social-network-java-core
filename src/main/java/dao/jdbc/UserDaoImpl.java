package dao.jdbc;

import common.Private;
import dao.beans.LongBean;
import dao.interfaces.UserDao;
import model.User;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface UserDaoImpl extends UserDao {

    Logger log = Logger.getLogger(UserDaoImpl.class);

    String SQL_GET_NUMBER_OF_USERS =
            "SELECT count(id) AS id FROM users WHERE id != ?;";
    String SQL_GET_NUMBER_OF_USERS_WITH_SEARCH_PARAMETER =
            "SELECT count(id) AS id FROM users "
                    .concat("WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE LOWER(?) AND id != ?");
    String SQL_ADD_USER =
            "INSERT INTO users (email, password, first_name, last_name) VALUES (?, MD5(?), ?, ?);";
    String SQL_UPDATE_USER =
            "UPDATE users SET email = ?, first_name = ?, last_name = ?, phone = ?, birth_date = ?, sex = ? WHERE id = ?;";
    String SQL_REMOVE_USER =
            "DELETE FROM users WHERE id = ?;";
    String SQL_CHANGE_PASSWORD =
            "UPDATE users SET password = MD5(?) WHERE id = ?;";

    String SQL_GET_BY_EMAIL =
            "SELECT id, email, first_name, last_name, birth_date, phone, reg_date, sex, CONCAT_WS(' ', first_name, last_name) AS name FROM users WHERE email = ?;";
    String SQL_GET_BY_EMAIL_AND_PASSWORD =
            "SELECT id, email, first_name, last_name, birth_date, phone, reg_date, sex, CONCAT_WS(' ', first_name, last_name) AS name FROM users WHERE email = ? AND password = MD5(?);";
    String SQL_GET_BY_ID =
            "SELECT id, email, first_name, last_name, birth_date, phone, reg_date, sex, CONCAT_WS(' ', first_name, last_name) AS name FROM users WHERE id = ?;";
    String SQL_GET_BY_ID_AND_CHECK_FRIENDSHIP =
            "SELECT id, email, first_name, last_name, birth_date, phone, reg_date, sex, "
                    .concat("CONCAT_WS(' ', first_name, last_name) AS name, ")
                    .concat("CASE WHEN userfriends.friendid ISNULL THEN FALSE ELSE TRUE END AS isuserfriend, ")
                    .concat("CASE WHEN friendofuser.userid ISNULL THEN FALSE ELSE TRUE END AS isfriendofuser ")
                    .concat("FROM users ")
                    .concat("LEFT JOIN friends AS userfriends ON users.id = userfriends.friendid AND userfriends.userid = ? ")
                    .concat("LEFT JOIN friends AS friendofuser ON users.id = friendofuser.userid AND friendofuser.friendid = ? ")
                    .concat("WHERE id = ?;");
    String SQL_GET_USER_LIST =
            "SELECT id, email, first_name, last_name, birth_date, phone, reg_date, sex, "
                    .concat("CONCAT_WS(' ', first_name, last_name) AS name, ")
                    .concat("CASE WHEN userfriends.friendid ISNULL THEN FALSE ELSE TRUE END AS isuserfriend, ")
                    .concat("CASE WHEN friendofuser.userid ISNULL THEN FALSE ELSE TRUE END AS isfriendofuser ")
                    .concat("FROM users ")
                    .concat("LEFT JOIN friends AS userfriends ON users.id = userfriends.friendid AND userfriends.userid = ? ")
                    .concat("LEFT JOIN friends AS friendofuser ON users.id = friendofuser.userid AND friendofuser.friendid = ? ")
                    .concat("WHERE id != ? ")
                    .concat("ORDER BY name ")
                    .concat("limit ? offset ?;");
    String SQL_GET_USER_LIST_WITH_SEARCH_PARAMETER =
            "SELECT id, email, first_name, last_name, birth_date, phone, reg_date, sex, "
                    .concat("CONCAT_WS(' ', first_name, last_name) AS name, ")
                    .concat("CASE WHEN userfriends.friendid ISNULL THEN FALSE ELSE TRUE END AS isuserfriend, ")
                    .concat("CASE WHEN friendofuser.userid ISNULL THEN FALSE ELSE TRUE END AS isfriendofuser ")
                    .concat("FROM users ")
                    .concat("LEFT JOIN friends AS userfriends ON users.id = userfriends.friendid AND userfriends.userid = ? ")
                    .concat("LEFT JOIN friends AS friendofuser ON users.id = friendofuser.userid AND friendofuser.friendid = ? ")
                    .concat("WHERE LOWER(CONCAT_WS(' ', first_name, last_name)) LIKE LOWER(?) AND id != ? ")
                    .concat("ORDER BY name ")
                    .concat("limit ? offset ?;");

    @Override
    default long getNumberOfUsers(long currentUserId) {
        return getNumberOfUsers(currentUserId, "");
    }

    @Override
    default long getNumberOfUsers(long currentUserId, String searchText) {

        final Optional<LongBean> bean;
        if (searchText==null || searchText.isEmpty())
            bean = select(LongBean.class, SQL_GET_NUMBER_OF_USERS, currentUserId);
        else
            bean = select(LongBean.class, SQL_GET_NUMBER_OF_USERS_WITH_SEARCH_PARAMETER,  "%" + searchText + "%", currentUserId);
        return bean.get().getId();

    }

    @Override
    default Optional<User> getUserById(long id) {
        return select(User.class, SQL_GET_BY_ID, id);
    }

    @Override
    default Optional<User> getUserById(long id, long isFriendId) {
        return select(User.class, SQL_GET_BY_ID_AND_CHECK_FRIENDSHIP, id, isFriendId, id);
    }

    @Override
    default Optional<User> getUserByEmailPassword(String email, String password) {
        return select(User.class, SQL_GET_BY_EMAIL_AND_PASSWORD, email, password);
    }

    @Override
    default Optional<User> getUserByEmail(String email) {
        return select(User.class, SQL_GET_BY_EMAIL, email);
    }

    @Override
    default List<User> getUserList(User currentUser, int recordsPerPage, int offset, String searchText) {

        if (searchText==null || searchText.isEmpty())
            return (List<User>) selectCollection(User.class,
                    SQL_GET_USER_LIST,
                    currentUser.getId(),
                    currentUser.getId(),
                    currentUser.getId(),
                    recordsPerPage,
                    offset);
        else
            return (List<User>) selectCollection(User.class,
                    SQL_GET_USER_LIST_WITH_SEARCH_PARAMETER,
                    currentUser.getId(),
                    currentUser.getId(),
                    "%" + searchText + "%",
                    currentUser.getId(),
                    recordsPerPage,
                    offset);

    }

    @Override
    default Optional<User> addUser(String email, String password, String first_name, String last_name) {
        return insert(User.class, SQL_ADD_USER, email, password, first_name, last_name);
    }

    @Private //Only for tests
    @Override
    default void removeUser(User user) {
        /* FIXME: 07.08.2016    Add removeFriends & removeMessages procedures, otherwise remove may fail is user has friends or messages
                                Temporary removeUser is marked as Private
        */
        delete(SQL_REMOVE_USER, user.getId());
    }

    @Override
    default void updateUser(User user) {
        update(SQL_UPDATE_USER,
                user.getEmail(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getPhone(),
                user.getBirth_date(),
                user.getSex(),
                user.getId());
    }

    @Override
    default void changePassword(User user, String password) {
        update(SQL_CHANGE_PASSWORD, password, user.getId());
    }

}