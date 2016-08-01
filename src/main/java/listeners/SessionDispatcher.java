package listeners;

import dao.interfaces.FriendsDao;
import dao.interfaces.MessageDao;
import dao.interfaces.UserDao;
import lombok.extern.log4j.Log4j;
import model.Message;
import model.User;
import servlets.HttpSessionWrapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Log4j
@WebServlet("/")
public class SessionDispatcher extends HttpServlet {

    private UserDao userDao;
    private MessageDao messageDao;
    private FriendsDao friendsDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDao = (UserDao) config.getServletContext().getAttribute(Initializer.USER_DAO);
        messageDao = (MessageDao) config.getServletContext().getAttribute(Initializer.MESSAGE_DAO);
        friendsDao = (FriendsDao) config.getServletContext().getAttribute(Initializer.FRIENDS_DAO);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final HttpSessionWrapper session = request::getSession;
        final String path = request.getServletPath();
        final String action = path.substring(1);

        final String profileId = request.getParameter("id");
        final String userPage = request.getParameter("userPage");
        final int responseCode;

        switch (action) {

            case "profile":
                responseCode = setProfile(session, profileId);
                break;

            case "users":
                responseCode = viewUserList(session, userPage);
                break;

            case "conversation":
                responseCode = viewConversation(session, profileId);
                break;

            case "messages":
                responseCode = viewMessages(session); // TODO: 26.07.2016 Профиль не требуется
                break;

//            case "messages":
//                break;

            default:
                return;

        }

        if (responseCode != HttpServletResponse.SC_OK) {
            response.sendError(responseCode);
        } else {
            session.setCurrentTab(action);
            getServletContext().getRequestDispatcher("/main.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final HttpSessionWrapper session = request::getSession;
        final String path = request.getServletPath();
        final String action = path.substring(1);

        final String profileId = request.getParameter("id");
        final String messageBody = request.getParameter("messageBody");

        //Check if profile id is not correct
        final Optional<User> profile = getProfile(profileId);
        if (!profile.isPresent()) {
            log.warn(String.format("For action \"%s\" user \"%s\" not found", action, profileId));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        switch (action) {

            case "sendMessage":
                messageDao.addNew(session.getUser(), profile.get(), messageBody.replaceAll("\r\n", "<br>"));
                break;

            case "addFriend":
                friendsDao.addFriend(session.getUser(), profile.get());
                break;

            case "removeFriend":
                friendsDao.removeFriend(session.getUser(), profile.get());
                break;

            default:
                return;

        }

        response.sendRedirect(request.getHeader("Referer"));

    }

    private Optional<User> getProfile(String profileId) {

        final long id = Optional.ofNullable(profileId)
                .map(Long::parseLong)
                .orElse(0L);

        return userDao.getById(id);

    }

    private Optional<User> getProfile(String profileId, long userId) {

        final long id = Optional.ofNullable(profileId)
                .map(Long::parseLong)
                .orElse(0L);

        return friendsDao.getById(id, userId);

    }

    private int viewUserList(HttpSessionWrapper session, String userPage) {

        final User user = session.getUser();
        final int numberOfPages;
        final List<User> profilesList;
        final long numberOfUsers = userDao.getNumberOfUsers() - 1; //Exclude current user

        int page;
        try {
            page = Integer.parseInt(userPage);
        } catch (NumberFormatException e) {
            page = 1;
            log.warn(String.format("Scroll user list. User \"%s\" page \"%s\" not found", session.getUser().getId(), userPage));
        }

        if (numberOfUsers > Integer.MAX_VALUE)
            numberOfPages = 10;
        else
            numberOfPages = (int) Math.ceil((double) numberOfUsers / HttpSessionWrapper.USERS_PER_PAGE);
        session.setNumberOfUserPages(numberOfPages);

        if (page > numberOfPages || page < 1)
            session.setCurrentUserPage(1);
        else
            session.setCurrentUserPage(page);

        profilesList = userDao.getList(user, HttpSessionWrapper.USERS_PER_PAGE, (page-1) * HttpSessionWrapper.USERS_PER_PAGE, "");
        session.setProfilesList(profilesList);
        return HttpServletResponse.SC_OK;

    }

    private int viewConversation(HttpSessionWrapper session, String profileId) {

        final Optional<User> profile = getProfile(profileId);
        if (profile.isPresent()) {
            session.setProfile(profile.get());
            List<Message> messageList = messageDao.getAll(session.getUser(), profile.get());
            session.setMessagesList(messageList);
            return HttpServletResponse.SC_OK;
        } else {
            log.warn(String.format("For action \"conversation\" user \"%s\" not found", profileId));
            return HttpServletResponse.SC_BAD_REQUEST;
        }

    }

    private int viewMessages(HttpSessionWrapper session) {

        List<Message> messageList = messageDao.getLast(session.getUser());
        session.setMessagesList(messageList);
        return HttpServletResponse.SC_OK;

    }

    private int setProfile(HttpSessionWrapper session, String profileId) {

        final Optional<User> profile = getProfile(profileId, session.getUser().getId());
        if (profile.isPresent()) {
            session.setProfile(profile.get());
            return HttpServletResponse.SC_OK;
        } else {
            log.warn(String.format("For action \"profile\" user \"%s\" not found", profileId));
            return HttpServletResponse.SC_BAD_REQUEST;
        }

    }

}
