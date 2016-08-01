package dao.jdbc;

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
            "SELECT count(id) AS id FROM users;";
    String SQL_ADD_USER =
            "INSERT INTO users (email, password, first_name, last_name) VALUES (?, MD5(?), ?, ?);";
    String SQL_REMOVE_USER =
            "DELETE FROM users WHERE id = ?;";

    String SQL_GET_BY_EMAIL =
            "SELECT id, email, first_name, last_name, birth_date, reg_date, sex, CONCAT_WS(' ', first_name, last_name) AS name FROM users WHERE email = ?;";
    String SQL_GET_BY_EMAIL_AND_PASSWORD =
            "SELECT id, email, first_name, last_name, birth_date, reg_date, sex, CONCAT_WS(' ', first_name, last_name) AS name FROM users WHERE email = ? AND password = MD5(?);";
    String SQL_GET_BY_ID =
            "SELECT id, email, first_name, last_name, birth_date, reg_date, sex, CONCAT_WS(' ', first_name, last_name) AS name FROM users WHERE id = ?;";
    String SQL_GET_ALL =
            "SELECT id, email, first_name, last_name, birth_date, reg_date, sex, "
                    .concat("CONCAT_WS(' ', first_name, last_name) AS name, ")
                    .concat("CASE WHEN userfriends.friendid ISNULL THEN FALSE ELSE TRUE END AS isuserfriend, ")
                    .concat("CASE WHEN friendofuser.userid ISNULL THEN FALSE ELSE TRUE END AS isfriendofuser ")
                    .concat("FROM users ")
                    .concat("LEFT JOIN friends AS userfriends ON users.id = userfriends.friendid AND userfriends.userid = ? ")
                    .concat("LEFT JOIN friends AS friendofuser ON users.id = friendofuser.userid AND friendofuser.friendid = ? ")
                    .concat("ORDER BY name ")
                    .concat("limit ? offset ?;");

    @Override
    default long getNumberOfUsers() {

        Optional<LongBean> bean = select(LongBean.class, SQL_GET_NUMBER_OF_USERS);
        return bean.get().getId();

    }

    @Override
    default Optional<User> getById(long id) {
        return select(User.class, SQL_GET_BY_ID, id);
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
    default List<User> getList(User currentUser, long recordsPerPage, long offset, String searchParam) {
        return (List<User>) selectCollection(User.class, SQL_GET_ALL, currentUser.getId(), currentUser.getId(), recordsPerPage, offset);
    }

    @Override
    default Optional<User> addUser(String email, String password, String first_name, String last_name) {
        return insert(User.class, SQL_ADD_USER, email, password, first_name, last_name);
    }

    @Override
    default void removeUser(User user) {
        delete(SQL_REMOVE_USER, user.getId());
    }

}