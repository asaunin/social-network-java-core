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
@WebServlet("/login")
public class Login extends HttpServlet {

    // TODO: 06.07.2016 Подключить логгер
    // TODO: 14.07.2016 Развертка приложения
    // TODO: 14.07.2016 Тестирование под нагрузкой Curla

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

        //Validate input parameters
        ValidationCode validationCode = validateLogin(email, password);
        if (!isValidCode(validationCode)) {
            showError(getMessage(validationCode, session.getLocale()), request, response);
            return;
        }

        //Check if login is correct
        Optional<User> user = userDao.getUserByEmailPassword(email, password);
        if (!user.isPresent()) {
            user = userDao.getUserByEmail(email);
            if (!user.isPresent())
                validationCode = ValidationCode.USER_NOT_FOUND;
            else
                validationCode = ValidationCode.PASS_INCORRECT;
            showError(getMessage(validationCode, session.getLocale()), request, response);
        } else {
            User sessionUser = user.get();
            log.info(String.format("Login \"%s\" successful", email));
            session.setUser(sessionUser);
            response.sendRedirect("/profile?id=" + sessionUser.getId());
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void showError(String errorMessage, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.warn(errorMessage);
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

}