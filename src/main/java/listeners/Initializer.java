package listeners;

import dao.jdbc.FriendsDaoImpl;
import dao.jdbc.MessageDaoImpl;
import dao.jdbc.UserDaoImpl;
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
public class Initializer implements ServletContextListener {

    @Resource(name="jdbc/ProdDB")
    private static DataSource ds;

    public static final String USER_DAO = "userDao";
    public static final String MESSAGE_DAO = "messageDao";
    public static final String FRIENDS_DAO = "friendsDao";

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
        servletContext.setAttribute(USER_DAO, (UserDaoImpl) ds::getConnection);
        servletContext.setAttribute(MESSAGE_DAO, (MessageDaoImpl) ds::getConnection);
        servletContext.setAttribute(FRIENDS_DAO, (FriendsDaoImpl) ds::getConnection);

    }

}
