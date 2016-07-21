package servlets;

import dao.interfaces.UserDao;
import lombok.extern.log4j.Log4j;
import model.User;
import service.DBInitializer;
import service.Validator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Log4j
@WebServlet("/login")
public class Login extends HttpServlet {

    // TODO: 06.07.2016 Подключить логгер
    // TODO: 14.07.2016 Прописать пути в фильтр
    // TODO: 11.07.2016 Реализовать logout
    // TODO: 14.07.2016 Вместо forward лучше использовать sendRedirect
    // TODO: 14.07.2016 Развертка приложения
    // TODO: 14.07.2016 Тестирование под нагрузкой Curla

    private UserDao userDao;

    private void error(String errorMessage, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.error(errorMessage);
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/login.jsp").forward(request, response); // TODO: 14.07.2016 http://stackoverflow.com/questions/17001185/pass-hidden-parameters-using-response-sendredirect
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDao = (UserDao) config.getServletContext().getAttribute(DBInitializer.USER_DAO);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final HttpSessionWrapper session = HttpSessionWrapper.from(request.getSession());
        final String email = request.getParameter("email");
        final String password = request.getParameter("password");

        //Validate input parameters
        String errorMessage = Validator.validateLogin(email, password, session.getLocale());
        if (!errorMessage.isEmpty()) {
            error(errorMessage, request, response);
            return;
        }

        //Check if login is correct
        Optional<User> user = userDao.getByEmailPassword(email, password);
        if (!user.isPresent()) {
            user = userDao.getByEmail(email);
            if (!user.isPresent())
                errorMessage = Validator.getMessage(Validator.ErrorMessage.USER_NOT_FOUND, session.getLocale());
            else
                errorMessage = Validator.getMessage(Validator.ErrorMessage.PASS_INCORRECT, session.getLocale());
            error(errorMessage, request, response);
            return;
        } else {
            log.info(String.format("Login \"%s\" successful", email));
            session.setUser(user.get());
            response.sendRedirect("myprofile.jsp");
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}