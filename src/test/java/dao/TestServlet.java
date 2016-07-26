package dao;

import org.apache.catalina.startup.Tomcat;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestServlet {

//    private static DataSource ds;
//
//    private static UserDaoImpl userDao;
//    private static MessageDaoImpl messageDao;
//
//    private static final String RESOURCES_FILE_PATH = "src/test/resources/";
//    private static final String INIT_DB_SCRIPT_PATH = "db.sql";
//
//    private static Tomcat tomcat;
//    public static final Optional<String> PORT = Optional.ofNullable(System.getenv("PORT"));
//    public static final Optional<String> HOSTNAME = Optional.ofNullable(System.getenv("HOSTNAME"));

    @BeforeClass
    public static void init() throws Exception {

        String webappDirLocation = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();

        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        tomcat.setPort(Integer.valueOf(webPort));

//        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
//        System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());
//
//        // Declare an alternative location for your "WEB-INF/classes" dir
//        // Servlet 3.0 annotation will work
//        File additionWebInfClasses = new File("target/classes");
//        WebResourceRoot resources = new StandardRoot(ctx);
//        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
//                additionWebInfClasses.getAbsolutePath(), "/"));
//        ctx.setResources(resources);
//
//        tomcat.start();
//        tomcat.getServer().await();

        String baseDir = ".";
        tomcat.setBaseDir(baseDir);
        tomcat.getHost().setAppBase(baseDir);
        tomcat.getHost().setDeployOnStartup(true);
        tomcat.getHost().setAutoDeploy(true);
        tomcat.start();
//        tomcat.getServer().await();
    }

    @Test
    public void connectionTest() {

        System.out.println("dsfsdgs");
        //Optional<User> user = userDao.getById(1L);

    }

    private static void executeInitDbScript() {

//        try {
//            Connection conn = ds.getConnection();
//            Statement statement = conn.createStatement();
//            final String[] sqlExpressions = Files.lines(
//                    Paths.get(RESOURCES_FILE_PATH + INIT_DB_SCRIPT_PATH))
//                    .collect(Collectors.joining()).split(";");
//            Arrays.stream(sqlExpressions)
//                    .forEach(s -> {
//                        try {
//                            statement.addBatch(s);
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                    });
//            statement.executeBatch();
//        } catch (SQLException | IOException e) {
//            throw new RuntimeException();
//        }

    }

}
