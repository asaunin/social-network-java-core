package listeners;

import dao.jdbc.JDBCUserDao;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener
public class DbInitializer implements ServletContextListener {

    @Resource(name="jdbc/ProdDB")
    private static DataSource ds;

    public static final String USER_DAO = "UserDao";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        final ServletContext servletContext = servletContextEvent.getServletContext();

        //initialize DB Connection
        servletContext.setAttribute(USER_DAO, (JDBCUserDao) ds::getConnection);

    }
}
