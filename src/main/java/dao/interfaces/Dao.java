package dao.interfaces;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;
import servlets.Login;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface Dao {

    static Logger logger = Logger.getLogger(Login.class);

    Connection getConnection() throws SQLException;

    default <T> Optional<T> query(Class<T> type, String sql, Object... values) {

        T bean = null;
        QueryRunner run = new QueryRunner();
        ResultSetHandler<T> h = new BeanHandler<T>(type);

        try {
            bean = run.query(getConnection(), sql, h, values);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return Optional.ofNullable(bean);
    }

    default <T> Collection<T> listQuery(Class<T> type, String sql, Object... values) {

        List<T> bean = null;
        QueryRunner run = new QueryRunner();
        ResultSetHandler<List<T>> h = new BeanListHandler<T>(type);

        try {
            bean = run.query(getConnection(), sql, h, values);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return bean;
    }

    default int update(String sql, Object... values) {

        int count = 0;
        QueryRunner run = new QueryRunner();

        try {
            count = run.update(getConnection(), sql, values);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return count;
    }

}
