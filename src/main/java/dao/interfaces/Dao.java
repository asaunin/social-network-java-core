package dao.interfaces;

import common.Private;
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

    @Private
    default <T> Optional<T> select(Class<T> type, String sql, Object... values) {

        T bean = null;
        QueryRunner run = new QueryRunner();
        ResultSetHandler<T> rsh = new BeanHandler<T>(type);

        try {
            bean = run.query(getConnection(), sql, rsh, values);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return Optional.ofNullable(bean);
    }

    @Private
    default <T> Collection<T> selectCollection(Class<T> type, String sql, Object... values) {

        Collection<T> bean = null;
        QueryRunner run = new QueryRunner();
        ResultSetHandler<List<T>> rsh = new BeanListHandler<T>(type);

        try {
            bean = run.query(getConnection(), sql, rsh, values);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return bean;
    }

    @Private
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

    @Private
    default <T> Optional<T> insert(Class<T> type, String sql, Object... values) {

        T bean = null;
        QueryRunner run = new QueryRunner();
        ResultSetHandler<T> rsh = new BeanHandler<T>(type);

        try {
            bean = run.insert(getConnection(), sql, rsh, values);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return Optional.ofNullable(bean);
    }

}
