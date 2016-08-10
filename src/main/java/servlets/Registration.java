package servlets;

import dao.interfaces.UserDao;
import listeners.Initializer;
import lombok.extern.log4j.Log4j;
import model.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static service.Validator.*;

@Log4j
@WebServlet("/registration")
public class Registration extends HttpServlet {

    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDao = (UserDao) config.getServletContext().getAttribute(Initializer.USER_DAO);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final HttpSessionWrapper session = request::getSession;
        final String email = request.getParameter("email");
        final String password = request.getParameter("password");
        final String confirm_password = request.getParameter("confirm_password");
        final String first_name = request.getParameter("first_name");
        final String last_name = request.getParameter("last_name");
        Optional<User> user = null;

        //Validate input parameters
        ValidationCode validationCode = validateRegistration(email, password, confirm_password, first_name, last_name);
        if (!isValidCode(validationCode)) {
            showError(getMessage(validationCode, session.getLocale()), request, response);
            return;
        }

        //Check if user is already registered
        user = userDao.getUserByEmail(email);
        if (user.isPresent()) {
            showError(getMessage(ValidationCode.DUPLICATED_REGISTRATION, session.getLocale()), request, response);
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

    private void showError(String errorMessage, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.warn(errorMessage);
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/registration.jsp").forward(request, response);
    }


}