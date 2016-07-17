package dao.jdbc;

import dao.interfaces.UserDao;
import model.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface JDBCUserDao extends UserDao {

    String SQL_GET_BY_ID =
            "SELECT id, email, first_name, last_name, birth_date FROM users WHERE id = ?";
    String SQL_GET_BY_EMAIL =
            "SELECT id, email, first_name, last_name, birth_date FROM users WHERE email = ?";
    String SQL_GET_BY_EMAIL_AND_PASSWORD =
            "SELECT id, email, first_name, last_name, birth_date FROM users WHERE email = ? AND password = MD5(?)";
    String SQL_GET_ALL =
            "SELECT id, email, first_name, last_name, birth_date FROM users";
    String SQL_SET =
            "INSERT INTO users (email, password, first_name, last_name, reg_date) VALUES (?, MD5(?), ?, ?, ?)";

    @Override
    default Optional<User> getById(long id) {
        return query(User.class, SQL_GET_BY_ID, id);
    }

    @Override
    default Optional<User> getByEmailPassword(String email, String password) {
        return query(User.class, SQL_GET_BY_EMAIL_AND_PASSWORD, email, password);
    }

    @Override
    default Optional<User> getByEmail(String email) {
        return query(User.class, SQL_GET_BY_EMAIL, email);
    }

    @Override
    default List<User> getAll(String searchParam) {
        return new ArrayList(listQuery(User.class, SQL_GET_ALL)); // TODO: 11.07.2016 Возможно стоит поменять коллекцию на List
    }

    @Override
    default Optional<User> addNew(String email, String password, String first_name, String last_name) {
        Timestamp now = new Timestamp(new Date().getTime());
        update(SQL_SET, email, password, first_name, last_name, now);
        return getByEmail(email);
    }

}