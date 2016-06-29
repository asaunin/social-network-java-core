package dao.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface Dao {

    Connection getConnection() throws SQLException;

}
