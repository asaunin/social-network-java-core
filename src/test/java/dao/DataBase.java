package dao;

import dao.interfaces.Dao;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Collectors;

class DataBase {

    private static final String RESOURCES_FILE_PATH = "src/test/resources/";
    private static final String DB_PROPERTIES_FILE_NAME = "db.properties";
    private static final String DB_SCHEMA_FILE_NAME = "db.sql";

    static DataSource init() throws Exception {

        PoolProperties poolProp = new PoolProperties();
        Properties prop = new Properties();

        InputStream resourceAsStream =
                Files.newInputStream(
                        Paths.get(RESOURCES_FILE_PATH, DB_PROPERTIES_FILE_NAME));
        prop.load(resourceAsStream);
        poolProp.setDriverClassName(prop.getProperty("driverClassName"));
        poolProp.setUrl(prop.getProperty("url"));
        poolProp.setUsername(prop.getProperty("user"));
        poolProp.setPassword(prop.getProperty("password"));
        DataSource ds = new DataSource(poolProp);
        ds.setPoolProperties(poolProp);
        String[] sqls =
                Files.lines(
                        Paths.get(RESOURCES_FILE_PATH, DB_SCHEMA_FILE_NAME))
                        .collect(Collectors.joining())
                        .split(";");
        Dao conn = ds::getConnection;
        conn.batch(sqls);

        return ds;

    }


}
