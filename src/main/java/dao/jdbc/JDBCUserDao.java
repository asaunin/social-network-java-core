package dao.jdbc;

import dao.interfaces.UserDao;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static dao.service.Util.prepareStatement;

@FunctionalInterface
public interface JDBCUserDao extends UserDao {

    String SQL_GET_BY_ID =
            "SELECT id, email, first_name, last_name, birth_date FROM users WHERE id = ?";
    String SQL_GET_BY_EMAIL_AND_PASSWORD =
            "SELECT id, email, first_name, last_name, birth_date FROM users WHERE email = ? AND password = ?";
//    "SELECT id, email, first_name, last_name, birth_date FROM User WHERE email = ? AND password = MD5(?)"; // TODO: 29.06.2016 Заменить на MD5

    @Override
    default Optional<User> get(long id) {
        return find(SQL_GET_BY_ID, id);
    }

    @Override
    default Optional<User> get(String email, String password) {
        return find(SQL_GET_BY_EMAIL_AND_PASSWORD, email, password);
    }

    default Optional<User> find(String sql, Object... values) {//// TODO: 29.06.2016 Уточнить можно ли делать такое 
        User user = null;

        try {
            PreparedStatement statement = prepareStatement(getConnection(), sql, false, values);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = map(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(user);
    }

    default User map(ResultSet resultSet) throws SQLException { //// TODO: 29.06.2016 Как автоматиирваь Mappng? 
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setFirst_name(resultSet.getString("first_name"));
        user.setLast_name(resultSet.getString("last_name"));
        user.setBirth_date(resultSet.getDate("birth_date"));
        return user;
    }

}