package dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.EmbeddedServer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyAppDeployedOnLocalTest {

    private EmbeddedServer embeddedServer;

    @Before
    public void startServer() throws ServletException, NamingException, SQLException {
        embeddedServer = new EmbeddedServer(9090, "/myservice");
        embeddedServer.start();

        Context initContext = new InitialContext();
        Context envContext  = (Context)initContext.lookup("java:/comp/env");
        DataSource ds = (DataSource)envContext.lookup("jdbc/eswastha");

        Connection conn = ds.getConnection();
        StringBuilder msg = new StringBuilder();

        try (Statement stm = conn.createStatement()) {
            String query = "show tables;";
            ResultSet rs = stm.executeQuery(query);
            // Store and return result of the query
            while (rs.next()) {
                msg.append(rs.getString("Tables_in_JCGExampleDB"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            // Release connection back to the pool
            if (conn != null) {
                conn.close();
            }
            conn = null; // prevent any future access
        }


    }

    @After
    public void stopServer() {
        embeddedServer.stop();
    }

    @Test
    public void test_send_request_without_parameters(){
//        Client client = ...
//        Reply reply = client.sendRequest();
//        assertTrue (reply.isOk());
    }
}