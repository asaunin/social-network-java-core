package service;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;

import javax.servlet.ServletException;
import java.io.File;

public class EmbeddedServer implements Runnable {

    private static final String RESOURCES_PATH = "/src/test/resources/context.xml";

    private Tomcat tomcat;
    private Thread serverThread;

    public EmbeddedServer(int port, String contextPath) throws ServletException {

        //Tomcat initialisation
        tomcat = new Tomcat();
        tomcat.enableNaming();
        tomcat.setPort(port);
        tomcat.setBaseDir("target/tomcat");
//        tomcat.addWebapp(contextPath, new File("src/main/webapp").getAbsolutePath());
        Context context = tomcat.addContext("", "target/tomcat");
        context.getNamingResources().addResource(setDBContextResourse());
        serverThread = new Thread(this);

    }

    public void start() {
        serverThread.start();
    }

    public void run() {

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
        tomcat.getServer().await();

    }

    public void stop() {

        try {
            tomcat.stop();
            tomcat.destroy();
            deleteDirectory(new File("target/tomcat/"));
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }

    }

    private void deleteDirectory(File path) {

        if (path == null) return;
        if (path.exists()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory()) {
                    deleteDirectory(f);
                    f.delete();
                } else {
                    f.delete();
                }
            }
            path.delete();
        }

    }

    public static ContextResource setDBContextResourse() {

        ContextResource resource = new ContextResource(); // TODO: 26.07.2016 Переписать на проперти
        resource.setName("jdbc/TestBD");
        resource.setType("javax.sql.DataSource");
        resource.setAuth("Container");
        resource.setScope("Sharable");
        resource.setProperty("driverClassName", "org.postgresql.Driver");
        resource.setProperty("url", "jdbc:postgresql://localhost:5432/test");
        resource.setProperty("username", "postgres");
        resource.setProperty("password", "postgres");
        resource.setProperty("maxTotal", "100");
        resource.setProperty("maxIdle", "30");
        resource.setProperty("maxWaitMillis", "10000");
        return resource;
    }

}