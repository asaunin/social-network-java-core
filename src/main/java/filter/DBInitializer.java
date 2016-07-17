package filter;

import dao.jdbc.JDBCUserDao;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.File;

@WebListener
public class DBInitializer implements ServletContextListener {

    @Resource(name="jdbc/ProdDB")
    private static DataSource ds;

    public static final String USER_DAO = "userDao";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        final ServletContext servletContext = servletContextEvent.getServletContext();

        //Initialize log4j
        String log4jConfig = servletContext.getInitParameter("log4j-config");
        if(log4jConfig == null) {
            System.err.println("No log4j-config init param, initializing log4j with BasicConfigurator");
            BasicConfigurator.configure();
        } else {
            String webAppPath = servletContext.getRealPath("/");
            String log4jProp = webAppPath + log4jConfig;
            File log4jConfigFile = new File(log4jProp);
            if (log4jConfigFile.exists()) {
                System.out.println("Initializing log4j with: " + log4jProp);
                DOMConfigurator.configure(log4jProp);
            } else {
                System.err.println(log4jProp + " file not found, initializing log4j with BasicConfigurator");
                BasicConfigurator.configure();
            }
        }

        //Initialize DB Connection
        servletContext.setAttribute(USER_DAO, (JDBCUserDao) ds::getConnection);
//        servletContext.setAttribute(GUN_DAO, (MySqlGunDao) ds::getConnection);
//        servletContext.setAttribute(INSTANCE_DAO, (MySqlInstanceDao) ds::getConnection);


    }
}
