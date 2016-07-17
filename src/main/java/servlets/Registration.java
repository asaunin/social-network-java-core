package servlets;

import dao.interfaces.UserDao;
import service.DBInitializer;
import lombok.extern.log4j.Log4j;
import model.User;
import service.Validator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

@Log4j
@WebServlet("/register")
public class Registration extends HttpServlet {

    private UserDao userDao;

    private void error(String errorMessage, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.error(errorMessage);
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        userDao = (UserDao) config.getServletContext().getAttribute(DBInitializer.USER_DAO);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final HttpSessionWrapper session = HttpSessionWrapper.from(request.getSession());
        final Locale locale = session.getLocale();
        final String email = request.getParameter("email");
        final String password = request.getParameter("password");
        final String password_confirmation = request.getParameter("password_confirmation");
        final String first_name = request.getParameter("first_name");
        final String last_name = request.getParameter("last_name");

        //Validate input parameters
        String errorMessage = Validator.validateRegistration(email, password, password_confirmation, first_name, last_name, locale);
        if (!errorMessage.isEmpty()) {
            error(errorMessage, request, response);
            return;
        }

        //Check if user is already registered
        Optional<User> user = userDao.getByEmailPassword(email, password);
        if (user.isPresent()) {
            errorMessage = Validator.getMessage(Validator.ErrorMessage.DUPLICATED_REGISTRATION, locale);
            error(errorMessage, request, response);
            return;
        }

        //Add new user
        user = userDao.addNew(email, password, first_name, last_name);
        if (!user.isPresent()) {
            errorMessage = Validator.getMessage(Validator.ErrorMessage.REGISTRATION, locale);
            error(errorMessage, request, response);
            return;
        } else {
            log.info(String.format("User \"%s\" successfully registered", email));
            session.setUser(user.get());
            response.sendRedirect("myprofile.html");
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


}