package dao;

import dao.interfaces.Dao;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.junit.runners.Suite.*;

@RunWith(Suite.class)
@SuiteClasses({UserDaoImplTest.class, FriendsDaoImplTest.class, MessageDaoImplTest.class})
public class TestSuite {

    private static final String DB_SCHEMA_FILE_NAME = "db-schema.sql";

    private static DataSource ds;
    private static PostgresProcess process;
    private static boolean isTestSuite;

    static DataSource getDataSource() throws Exception {

        if (ds == null) {

            // starting Postgres
            PostgresStarter<PostgresExecutable, PostgresProcess> runtime = PostgresStarter.getDefaultInstance();
            final PostgresConfig config = PostgresConfig.defaultWithDbName("test");
            PostgresExecutable exec = runtime.prepare(config);
            process = exec.start();

            // connecting to a running Postgres
            String url = format("jdbc:postgresql://%s:%s/%s",
                    config.net().host(),
                    config.net().port(),
                    config.storage().dbName()
            );
            PoolProperties poolProp = new PoolProperties();
            poolProp.setDriverClassName("org.postgresql.Driver");
            poolProp.setUrl(url);
            ds = new DataSource(poolProp);

            // feeding up the database
            Dao conn = ds::getConnection;

            String[] sqls =
                    Files.lines(
                            Paths.get(ClassLoader.getSystemResource(DB_SCHEMA_FILE_NAME).toURI()))
                            .collect(Collectors.joining())
                            .split(";");
            conn.batch(sqls);

        }

        return ds;

    }

    static void closeDataSource() throws Exception {
        if (!isTestSuite)
            destroy();
    }

    @BeforeClass
    public static void create() throws Exception {
        isTestSuite = true;
        getDataSource();
    }

    @AfterClass
    public static void destroy() throws Exception {
        ds.close();
        process.stop();
    }

}
