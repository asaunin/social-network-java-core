package servlets;

import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@Log4j
@WebServlet("/logout")
public class Logout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final HttpSessionWrapper session = HttpSessionWrapper.from(request.getSession(false));

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    log.info("JSESSIONID=" + cookie.getValue());
                    break;
                }
            }
        }

        //Invalidate the session if exists
        if (session != null) {
            log.info("User \"" + session.getUser() + "\" logout");
            session.setUser(null);
            session.invalidate();
            response.sendRedirect("login.jsp");
        }

    }

}
