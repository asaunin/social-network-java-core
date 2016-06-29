package servlets;

import org.apache.log4j.Logger;
import service.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "login", urlPatterns = { "/Login" })
public class Registration extends HttpServlet {

    static Logger logger = Logger.getLogger(Login.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String errorMessage = "";

        if (email == null || email.equals(""))
            errorMessage = "Email is empty";
        if (password == null || password.equals(""))
            errorMessage = "Password is empty";
        if (!Validator.isValidEmail(email))
            errorMessage = "Email is not valid";

//        errorMsg = "<div class=\"alert alert-danger\">\n<strong>Danger!</strong> Indicates a dangerous or potentially negative action.\n</div>";




       /* else{

            Connection con = (Connection) getServletContext().getAttribute("DBConnection");
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = con.prepareStatement("select id, name, email,country from Users where email=? and password=? limit 1");
                ps.setString(1, email);
                ps.setString(2, password);
                rs = ps.executeQuery();

                if(rs != null && rs.next()){

                    //User user = new User(rs.getString("name"), rs.getString("email"), rs.getString("country"), rs.getInt("id"));
                    User user = new User();
                    //logger.info("User found with details="+user);
                    HttpSession session = request.getSession();
                    session.setAttribute("User", user);
                    response.sendRedirect("home.jsp");;
                }else{
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
                    PrintWriter out= response.getWriter();
                    //logger.error("User not found with email="+email);
                    out.println("<font color=red>No user found with given email id, please register first.</font>");
                    rd.include(request, response);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                //logger.error("Database connection problem");
                throw new ServletException("DB Connection problem.");
            }finally{
                try {
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                    //logger.error("SQLException in closing PreparedStatement or ResultSet");;
                }

            }
        }*/
    }

}