package service;

import org.apache.catalina.startup.Tomcat;

public class Loader { // TODO: 26.07.2016 Пока не ясно зачем нужен

//    public static final Optional<String> PORT = Optional.ofNullable(System.getenv("PORT"));
//    public static final Optional<String> HOSTNAME = Optional.ofNullable(System.getenv("HOSTNAME"));

    public static void main(String[] args) {

//        String contextPath = "/" ;
//        String appBase = ".";
//        Tomcat tomcat = new Tomcat();
//        tomcat.setPort(Integer.valueOf(PORT.orElse("8080") ));
//        tomcat.setHostname(HOSTNAME.orElse("localhost"));
//        tomcat.getHost().setAppBase(appBase);
//        tomcat.addWebapp(contextPath, appBase);
//        tomcat.start();
//        tomcat.getServer().await();

        System.out.println("варыра5");

        Tomcat tomcat = new Tomcat();
        tomcat.enableNaming();
        tomcat.setPort(8080);
//        tomcat.setBaseDir("target/tomcat");
//        Context context = tomcat.addContext("", "target/tomcat");
//        context.getNamingResources().addResource(setDBContextResourse());
//        try {
//            tomcat.start();
//            tomcat.getServer().await();
//            tomcat.stop();
//            tomcat.destroy();
//        } catch (LifecycleException e) {
//            e.printStackTrace();
//        }
    }

//    public static ContextResource setDBContextResourse() {
//
//        ContextResource resource = new ContextResource(); // TODO: 26.07.2016 Переписать на проперти
//        resource.setName("jdbc/TestBD");
//        resource.setType("javax.sql.DataSource");
//        resource.setAuth("Container");
//        resource.setScope("Sharable");
//        resource.setProperty("driverClassName", "org.postgresql.Driver");
//        resource.setProperty("url", "jdbc:postgresql://localhost:5432/test");
//        resource.setProperty("username", "postgres");
//        resource.setProperty("password", "postgres");
//        resource.setProperty("maxTotal", "100");
//        resource.setProperty("maxIdle", "30");
//        resource.setProperty("maxWaitMillis", "10000");
//        return resource;
//    }

}