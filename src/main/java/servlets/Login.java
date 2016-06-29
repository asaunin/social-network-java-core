package servlets;

import dao.interfaces.UserDao;
import listeners.DbInitializer;
import model.User;
import org.apache.log4j.Logger;
import service.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "login", urlPatterns = { "/Login" })
public class Login extends HttpServlet {

    public static final String USER = "user";
    static Logger logger = Logger.getLogger(Login.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String errorMessage = "";

        if (email == null || email.equals("")) { // TODO: 29.06.2016 Как реализовать конструкцию через Лямбды 
            errorMessage = "Email is empty";
        } else
        if (password == null || password.equals("")) {
            errorMessage = "Password is empty";
        } else
        if (!Validator.isValidEmail(email))
            errorMessage = "Email is not valid";

        HttpSession session = request.getSession();
        if (!errorMessage.isEmpty()) {

            logger.error(errorMessage);
            String bsErrorMassage = "<div class=\"alert alert-danger\"><strong>" + errorMessage + "</strong></div>";
            session.setAttribute("LoginError", bsErrorMassage);
            response.sendRedirect(request.getHeader("Referer")); // TODO: 28.06.2016 Не перегружать страницу

        } else {

            session.removeAttribute("LoginError");
            UserDao userDao = (UserDao) getServletContext().getAttribute(DbInitializer.USER_DAO);
            final Optional<User> user = userDao.get(email, password);
            if (user.isPresent()) {
                session.setAttribute(USER, user.get());
                System.out.println("Ура пользователь найден!!!!!!");
            } else {
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        }

    }

}