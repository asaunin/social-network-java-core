package filter;

import dao.interfaces.UserDao;
import lombok.extern.log4j.Log4j;
import model.User;
import service.DBInitializer;
import servlets.HttpFilter;
import servlets.HttpSessionWrapper;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

@Log4j
@WebFilter("/*")
public class SecurityFilter implements HttpFilter {

    private UserDao userDao;

    private final String[] paths = {"/localisation",
                                    "/login",
                                    "/logout",
                                    "/registration"};

    private final String[] extensions = {".ico"};

    private boolean isAllowedUri(HttpServletRequest request) {
        String context = request.getContextPath();
        String path = request.getRequestURI().substring(context.length());
        for (String item : paths) {
            if (path.startsWith(item) || path.equals("/")) {
                return true;
            }
        }
        for (String item : extensions) {
            if (path.endsWith(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userDao = (UserDao) filterConfig.getServletContext().getAttribute(DBInitializer.USER_DAO);
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
            IOException, ServletException {

        final HttpSessionWrapper session = HttpSessionWrapper.from(request.getSession());

        request.setCharacterEncoding("UTF-8");

        //Set default locale
        if (!session.hasLocale())
            session.setLocale(Locale.getDefault());

        //Skip authorisation if assertion mode
        boolean assertsEnabled = false;
        assert assertsEnabled = true;
        if (assertsEnabled && !session.hasUser()) {
            Optional<User> user = userDao.getByEmail("vasia@mail.ru"); // TODO: 19.07.2016 Переписать на параметры 
            if (user.isPresent())
                session.setUser(user.get());
        }

        //Security check
        if (session.hasUser() || isAllowedUri(request))
            chain.doFilter(request, response);
        else
            response.sendRedirect("login.jsp");

    }

}
