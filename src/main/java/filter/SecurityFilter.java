package filter;

import lombok.extern.log4j.Log4j;
import servlets.HttpFilter;
import servlets.HttpSessionWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
@Log4j
public class SecurityFilter implements HttpFilter {

    private String[] paths = {
            "/settings",
            "/edit",
            "/delete"
    };

    private boolean isAuthorizedAccess(HttpServletRequest request) {
        String context = request.getContextPath();
        String path = request.getRequestURI().substring(context.length());
        for (String item : paths) {
            if (item.equals(path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
            IOException, ServletException {

        final HttpSessionWrapper session = HttpSessionWrapper.from(request.getSession());

        request.setCharacterEncoding("UTF-8");

        //Set default locale
        if (!session.hasLocale())
            session.setLocale(Locale.getDefault());

        if (!session.hasUser() && isAuthorizedAccess(request)) {
            response.sendRedirect("login.jsp");
            return;
        }
        chain.doFilter(request, response);

    }

}
