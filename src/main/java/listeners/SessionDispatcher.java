package listeners;

import dao.interfaces.FriendsDao;
import dao.interfaces.MessageDao;
import dao.interfaces.UserDao;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import model.Message;
import model.User;
import service.Validator;
import servlets.HttpSessionWrapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import static service.Validator.*;
import static servlets.HttpSessionWrapper.RECORDS_PER_PAGE;
import static servlets.HttpSessionWrapper.from;

@Log4j
@WebServlet("/")
public class SessionDispatcher extends HttpServlet {

    private UserDao userDao;
    private MessageDao messageDao;
    private FriendsDao friendsDao;

    private enum Action {

        ACTION_VIEW_CONTACT("contact"),
        ACTION_VIEW_CONVERSATION("conversation"),
        ACTION_VIEW_MESSAGES("messages"),
        ACTION_VIEW_PROFILE("profile"),
        ACTION_VIEW_USERLIST("users"),
        ACTION_VIEW_FRIENDLIST("friends"),
        ACTION_ADD_FRIEND("addFriend"),
        ACTION_SEND_MESSAGE("sendMessage"),
        ACTION_REMOVE_FRIEND("removeFriend"),
        ACTION_CHANGE_CONTACT("changeContact"),
        ACTION_CHANGE_PASSWORD("changePassword"),
        ACTION_NOT_DEFINED("");

        @Getter
        private final String text;

        Action(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.getText();
        }

        public static Action of(String value) {
            for (Action v : values())
                if (v.getText().equalsIgnoreCase(value)) return v;
            return ACTION_NOT_DEFINED;
        }

        private static boolean profileNeeded(Action action) {
            return action == ACTION_VIEW_PROFILE
                    || action == ACTION_VIEW_CONVERSATION
                    || action == ACTION_SEND_MESSAGE
                    || action == ACTION_ADD_FRIEND
                    || action == ACTION_REMOVE_FRIEND;
        }

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDao = (UserDao) config.getServletContext().getAttribute(Initializer.USER_DAO);
        messageDao = (MessageDao) config.getServletContext().getAttribute(Initializer.MESSAGE_DAO);
        friendsDao = (FriendsDao) config.getServletContext().getAttribute(Initializer.FRIENDS_DAO);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final HttpSessionWrapper session = from(request.getSession(false));
        final String path = request.getServletPath();
        final Action action = Action.of(path.substring(1));

        //Check and set session profile if needed. It might be used later
        final int responseCode = setProfileIfNeeded(session, action, request.getParameter("id"));
        if (responseCode != HttpServletResponse.SC_OK) {
            response.sendError(responseCode);
            return;
        }

        switch (action) {

            case ACTION_VIEW_CONTACT:
                break;

            case ACTION_VIEW_CONVERSATION:
                viewConversation(session);
                break;

            case ACTION_VIEW_MESSAGES:
                viewMessages(session);
                break;

            case ACTION_VIEW_PROFILE:
                break;

            case ACTION_VIEW_USERLIST:
                viewUserList(session, false, request.getParameter("userPage"), request.getParameter("searchText"));
                break;

            case ACTION_VIEW_FRIENDLIST:
                viewUserList(session, true, request.getParameter("userPage"), request.getParameter("searchText"));
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;

        }

        session.setCurrentTab(action.getText());
        getServletContext().getRequestDispatcher("/main.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final HttpSessionWrapper session = from(request.getSession(false));
        final Action action = Action.of(request.getParameter("action"));

        //Check and set session profile if needed. It might be used later
        final int responseCode = setProfileIfNeeded(session, action, request.getParameter("id"));
        if (responseCode != HttpServletResponse.SC_OK) {
            response.sendError(responseCode);
            return;
        }

        switch (action) {

            case ACTION_ADD_FRIEND:
                addFriend(session);
                break;

            case ACTION_CHANGE_CONTACT:
                changeContact(session, request, response);
                return;

            case ACTION_CHANGE_PASSWORD:
                changePassword(session, request, response);
                return;

            case ACTION_REMOVE_FRIEND:
                removeFriend(session);
                break;

            case ACTION_SEND_MESSAGE:
                sendMessage(session, request.getParameter("messageBody"));
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;

        }

        response.sendRedirect(request.getHeader("Referer"));

    }

    private int setProfileIfNeeded(HttpSessionWrapper session, Action action, String profileId) {

        final Optional<User> profile;

        if (Action.profileNeeded(action)) {

            final long id = Optional.ofNullable(profileId)
                    .map(Long::parseLong)
                    .orElse(0L);

            if (action == Action.ACTION_VIEW_PROFILE)
                profile = userDao.getUserById(id, session.getUser().getId());
            else
                profile = userDao.getUserById(id);

            if (profile.isPresent()) {
                session.setProfile(profile.get());
            } else {
                log.warn(String.format("For action \"\"%s\" user \"%s\" not found", action.getText(), profileId));
                return HttpServletResponse.SC_NOT_FOUND;
            }

        }

        return HttpServletResponse.SC_OK;

    }

    private void viewConversation(HttpSessionWrapper session) {
        Collection<Message> messageList = messageDao.getAll(session.getUser(), session.getProfile());
        session.setMessageList(messageList);
    }

    private void viewMessages(HttpSessionWrapper session) {
        Collection<Message> messageList = messageDao.getLast(session.getUser());
        session.setMessageList(messageList);
    }

    private void viewUserList(HttpSessionWrapper session, boolean onlyFriends, String userPage, String searchText) {

        final User user = session.getUser();
        final int numberOfPages;
        final Collection<User> userList;
        final long numberOfUsers;
        if (onlyFriends)
            numberOfUsers = friendsDao.getNumberOfFriends(user, searchText);
        else
            numberOfUsers = userDao.getNumberOfUsers(user.getId(), searchText);

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
            numberOfPages = (int) Math.ceil((double) numberOfUsers / RECORDS_PER_PAGE);
        session.setNumberOfUserPages(numberOfPages);

        if (page > numberOfPages || page < 1)
            session.setCurrentUserPage(1);
        else
            session.setCurrentUserPage(page);

        if (onlyFriends)
            userList = friendsDao.getFriendList(user, RECORDS_PER_PAGE, (page - 1) * RECORDS_PER_PAGE, searchText);
        else
            userList = userDao.getUserList(user, RECORDS_PER_PAGE, (page - 1) * RECORDS_PER_PAGE, searchText);

        session.setSearchText(searchText);
        session.setUserList(userList);

    }

    private void addFriend(HttpSessionWrapper session) {
        friendsDao.addFriend(session.getUser(), session.getProfile());
    }

    private void changeContact(HttpSessionWrapper session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String email = request.getParameter("email");
        final String first_name = request.getParameter("first_name");
        final String last_name = request.getParameter("last_name");
        final String phone = request.getParameter("phone");
        final String str_birth_date = request.getParameter("birth_date");
        final String sex = request.getParameter("sex");
        final User user = new User(session.getUser());

        //Create user with new contact information
        user.setEmail(email);
        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        user.setPhone(phone);
        user.setSex(sex);
        try {
            final DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            final java.util.Date date = format.parse(str_birth_date);
            final Date birth_date = new Date(date.getTime());
            user.setBirth_date(birth_date);
        } catch (IllegalArgumentException | ParseException e) {
            log.warn(String.format("Update contact. User \"%s\" date \"%s\" is invalid", session.getUser().getId(), str_birth_date));
        }

        //Check if data not changed
        if (user.equals(session.getUser())) {
            request.getRequestDispatcher("/main.jsp").forward(request, response);
            return;
        }

        //Validate input parameters
        ValidationCode validationCode = validateContact(email, first_name, last_name);

        //Check if email changed
        if (Validator.isValidCode(validationCode))
            if (!email.equals(session.getUser().getEmail())) {
                final Optional<User> findUser = userDao.getUserByEmail(email);
                if (findUser.isPresent())
                    validationCode = ValidationCode.DUPLICATED_REGISTRATION;
            }

        //Update user information
        if (Validator.isValidCode(validationCode)) {
            userDao.updateUser(user);
            session.setUser(user);
            if (session.hasProfile() && session.getProfile().getId() == user.getId())
                session.setProfile(user);
        }

        showValidationMessage(validationCode, session.getLocale(), "changeContactMessage", request, response);

    }

    private void changePassword(HttpSessionWrapper session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String old_password = request.getParameter("old_password");
        final String password = request.getParameter("password");
        final String confirm_password = request.getParameter("confirm_password");

        //Validate input parameters
        ValidationCode validationCode = validatePasswordChange(old_password, password, confirm_password);

        //Check if old password is correct
        if (Validator.isValidCode(validationCode)) {
            final Optional<User> user = userDao.getUserByEmailPassword(session.getUser().getEmail(), old_password);
            if (!user.isPresent())
                validationCode = ValidationCode.PASS_INCORRECT;
            else
                userDao.changePassword(user.get(), password);
        }

        showValidationMessage(validationCode, session.getLocale(), "changePasswordMessage", request, response);

    }

    private void removeFriend(HttpSessionWrapper session) {
        friendsDao.removeFriend(session.getUser(), session.getProfile());
    }

    private void sendMessage(HttpSessionWrapper session, String messageBody) {
        messageDao.addNew(session.getUser(), session.getProfile(), messageBody.replaceAll("\r\n", "<br>"));
    }

    private void showValidationMessage(ValidationCode validationCode, Locale locale, String fieldName, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String message = Validator.getMessage(validationCode, locale);

        request.setAttribute(fieldName, message);
        if (Validator.isValidCode(validationCode)) {
            log.info(message);
            request.setAttribute("alertType", "alert-success");
        } else {
            log.warn(message);
            request.setAttribute("alertType", "alert-danger");
        }
        request.getRequestDispatcher("/main.jsp").forward(request, response);

    }

}
