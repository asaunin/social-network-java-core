package servlets;

import dao.interfaces.MessageDao;
import dao.interfaces.UserDao;
import lombok.extern.log4j.Log4j;
import model.User;
import service.DBInitializer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Log4j
@WebServlet(name = "MyProfile",
        urlPatterns = {"/"})
//urlPatterns = {"/myprofile", "/profile", "/friends", "/groups", "/followers", "/messages"})
public class MyProfile extends HttpServlet {

    private UserDao userDao;
    private MessageDao messageDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDao = (UserDao) config.getServletContext().getAttribute(DBInitializer.USER_DAO);
        messageDao = (MessageDao) config.getServletContext().getAttribute(DBInitializer.MESSAGE_DAO);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final HttpSessionWrapper session = HttpSessionWrapper.from(request.getSession());
        final String path = request.getServletPath();
        final String action = path.substring(1);
        final String profileId = request.getParameter("id");

        switch (action) {
            case "profile":
                if (profileId!=null)
                    setProfile(session, profileId); //Set foreign profile
                else
                    setProfile(session, String.valueOf(session.getUser().getId()));  //Set own profile
                break;
            case "friends":
                session.setProfilesList(userDao.getAll(""));
                break;
            case "conversation":
                if (profileId!=null) {
                    setProfile(session, profileId);
                    session.setMessagesList(messageDao.getAll(session.getUser(), session.getProfile()));
                }
                break;
            case "messages":
                break;
            case "followers":
                break;
            default:
                return;
        }

        session.setCurrentTab(action);
        getServletContext().getRequestDispatcher("/myprofile.jsp").forward(request, response);
        //response.sendRedirect("myprofile.jsp#" + action);
        //request.getRequestDispatcher("/myprofile.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final HttpSessionWrapper session = HttpSessionWrapper.from(request.getSession());
        final String path = request.getServletPath();
        final String action = path.substring(1);

        switch (action) {
            case "sendMessage":
                sendMessage(session, request.getParameter("body"));
                break;
            default:
                return;
        }

        response.sendRedirect(request.getHeader("Referer"));
//        getServletContext().getRequestDispatcher(request.getHeader("Referer")).forward(request, response);

    }

    private void setProfile(HttpSessionWrapper session, String userId) {

        long id;
        try {
            id = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e); // TODO: 19.07.2016 Реализовать редирект на страницу ошибки
        }

        Optional<User> user = userDao.getById(id);
        if (user.isPresent()) {
            session.setProfile(user.get());
        } else {
            log.error("Wrong user");
            throw new RuntimeException("Wrong user"); // TODO: 19.07.2016 Реализовать редирект на страницу ошибки
        }
    }

    private void sendMessage(HttpSessionWrapper session, String body) {

        // TODO: 20.07.2016 Обработать else
        if (session.hasProfile())
            messageDao.addNew(session.getUser(), session.getProfile(), body.replaceAll("\r\n", "<br>"));
    }

}
