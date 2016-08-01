package servlets;

import dao.interfaces.UserDao;
import listeners.Initializer;
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
        log.warn(errorMessage);
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/registration.jsp").forward(request, response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDao = (UserDao) config.getServletContext().getAttribute(Initializer.USER_DAO);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final HttpSessionWrapper session = request::getSession;
        final Locale locale = session.getLocale();
        final String email = request.getParameter("email");
        final String password = request.getParameter("password");
        final String password_confirmation = request.getParameter("password_confirmation");
        final String first_name = request.getParameter("first_name");
        final String last_name = request.getParameter("last_name");
        Optional<User> user = null;

        //Validate input parameters
        String errorMessage = Validator.validateRegistration(email, password, password_confirmation, first_name, last_name, locale);
        if (!errorMessage.isEmpty()) {
            error(errorMessage, request, response);
            return;
        }

        //Check if user is already registered
        user = userDao.getByEmailPassword(email, password);
        if (user.isPresent()) {
            errorMessage = Validator.getMessage(Validator.ErrorMessage.DUPLICATED_REGISTRATION, locale);
            error(errorMessage, request, response);
            return;
        }

        //Add new user
        User sessionUser = userDao.addUser(email, password, first_name, last_name).get();
        log.info(String.format("User \"%s\" successfully registered", email));
        session.setUser(sessionUser);
        response.sendRedirect("/profile?id=" + sessionUser.getId());

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


}