package dao.interfaces;

import common.Private;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface Dao {

    Logger log = Logger.getLogger(Dao.class);
    String errorMessage = "SQL query error";

    Connection getConnection() throws SQLException;

    //SELECT * FROM pg_stat_activity; for active connections monitoring

    @Private
    default int[] batch(String[] sqls) {

        int[] rows;

        Connection conn = null;
        try {
            conn = getConnection();
            Statement statement = conn.createStatement();
            Arrays.stream(sqls).
                    forEach(s -> {
                        try {
                            statement.addBatch(s);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }

                    });
           rows = statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(errorMessage, e);
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return rows;

    }

    @Private
    default <T> Optional<T> select(Class<T> type, String sql, Object... values) {

        T bean = null;
        QueryRunner run = new QueryRunner();
        ResultSetHandler<T> rsh = new BeanHandler<>(type);

        Connection conn = null;
        try {
            conn = getConnection();
            bean = run.query(conn, sql, rsh, values);
        } catch (SQLException e) {
            log.error(errorMessage, e);
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return Optional.ofNullable(bean);

    }

    @Private
    default <T> Collection<T> selectCollection(Class<T> type, String sql, Object... values) {

        Collection<T> bean = null;
        QueryRunner run = new QueryRunner();
        ResultSetHandler<List<T>> rsh = new BeanListHandler<>(type);

        Connection conn = null;
        try {
            conn = getConnection();
            bean = run.query(conn, sql, rsh, values);
        } catch (SQLException e) {
            log.error(errorMessage, e);
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return bean;

    }

    @Private
    default int update(String sql, Object... values) {

        int count = 0;
        QueryRunner run = new QueryRunner();

        Connection conn = null;
        try {
            conn = getConnection();
            count = run.update(conn, sql, values);
        } catch (SQLException e) {
            log.error(errorMessage, e);
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return count;

    }

    @Private
    default int delete(String sql, Object... values) {

        int count = 0;
        QueryRunner run = new QueryRunner();

        Connection conn = null;
        try {
            conn = getConnection();
            count = run.update(conn, sql, values);
        } catch (SQLException e) {
            log.error(errorMessage, e);
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return count;

    }

    @Private
    default <T> Optional<T> insert(Class<T> type, String sql, Object... values) {

        T bean = null;
        QueryRunner run = new QueryRunner();
        ResultSetHandler<T> rsh = new BeanHandler<>(type);

        Connection conn = null;
        try {
            conn = getConnection();
            bean = run.insert(conn, sql, rsh, values);
        } catch (SQLException e) {
            log.error(errorMessage, e);
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return Optional.ofNullable(bean);

    }

}
