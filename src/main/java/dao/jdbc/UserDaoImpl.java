package dao.jdbc;

import dao.interfaces.UserDao;
import model.User;

import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface UserDaoImpl extends UserDao {

    String SQL_GET_BY_ID =
            "SELECT id, email, first_name, last_name, birth_date, sex, CONCAT_WS(' ', first_name, last_name) AS name FROM users WHERE id = ?";
    String SQL_GET_BY_EMAIL =
            "SELECT id, email, first_name, last_name, birth_date, sex, CONCAT_WS(' ', first_name, last_name) AS name FROM users WHERE email = ?";
    String SQL_GET_BY_EMAIL_AND_PASSWORD =
            "SELECT id, email, first_name, last_name, birth_date, sex, CONCAT_WS(' ', first_name, last_name) AS name FROM users WHERE email = ? AND password = MD5(?)";
    String SQL_GET_ALL =
            "SELECT id, email, first_name, last_name, birth_date, sex, CONCAT_WS(' ', first_name, last_name) AS name FROM users";
    String SQL_ADD_NEW =
            "INSERT INTO users (email, password, first_name, last_name) VALUES (?, MD5(?), ?, ?, ?)";

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
    default List<User> getAll(String searchParam) { // TODO: 19.07.2016 Исправить на Set
        return (List<User>) (selectCollection(User.class, SQL_GET_ALL));
    }

    @Override
    default Optional<User> addNew(String email, String password, String first_name, String last_name) {
        update(SQL_ADD_NEW, email, password, first_name, last_name);
        return getByEmail(email);
    }

}