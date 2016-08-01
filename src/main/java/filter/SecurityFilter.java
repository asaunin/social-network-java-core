package filter;

import dao.interfaces.UserDao;
import listeners.Initializer;
import lombok.extern.log4j.Log4j;
import model.User;
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
        userDao = (UserDao) filterConfig.getServletContext().getAttribute(Initializer.USER_DAO);
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
            IOException, ServletException {

        final HttpSessionWrapper session = request::getSession;
        final long TEST_USER_ID = 1L;

        request.setCharacterEncoding("UTF-8");

        //Set default locale
        if (!session.hasLocale())
            session.setLocale(Locale.getDefault());

        //Skip authorisation if assertion mode
        boolean assertsEnabled = false;
        assert assertsEnabled = true;
        if (assertsEnabled && !session.hasUser()) {
            Optional<User> user = userDao.getById(TEST_USER_ID);
            if (user.isPresent()) {
                User sessionUser = user.get();
                log.info(String.format("Login skipped in test mode. User \"%s\" logged without authorisation", sessionUser.getEmail()));
                session.setUser(sessionUser);
//                response.sendRedirect("/profile?id=" + sessionUser.getId()); // TODO: 21.07.2016 Doesn't work because of multiple session initialisation
//                return;
            }
        }

        //Security check
        if (session.hasUser() || isAllowedUri(request))
            chain.doFilter(request, response);
        else
            response.sendRedirect("login.jsp");

    }

}
