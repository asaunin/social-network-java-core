package servlets;

import common.Wrapper;
import model.Message;
import model.User;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Locale;

@FunctionalInterface
public interface HttpSessionWrapper extends Wrapper<HttpSession> {//, HttpSession {

    String LOCALE_NAME = "locale";
    String USER_NAME = "user";
    String USER_LIST = "userList";
    String MESSAGE_LIST = "messageList";
    String PROFILE_NAME = "profile";
    String CURRENT_USER_PAGE = "currentUserPage";
    String CURRENT_TAB = "currentTab";
    String NUMBER_OF_USER_PAGES = "numberOfUserPages";
    String SEARCH_TEXT = "searchText";

    int RECORDS_PER_PAGE = 10;

    static HttpSessionWrapper from(HttpSession httpSession) {
        return (httpSession == null) ? null : () -> httpSession;
    }

    default void invalidate() {
        toSrc().invalidate();
    }

    default Locale getLocale() {
        if (hasLocale())
            return (Locale) toSrc().getAttribute(LOCALE_NAME);
        else
            return Locale.getDefault();
    }

    default boolean hasLocale() {
        return !(toSrc().getAttribute(LOCALE_NAME) == null);
    }

    default void setLocale(Locale locale) {
        toSrc().setAttribute(LOCALE_NAME, locale);
    }

    default User getUser() {
        return (User) toSrc().getAttribute(USER_NAME);
    }

    default void setUser(User user) {
        toSrc().setAttribute(USER_NAME, user);
    }

    default int getCurrentUserPage() { return (int) toSrc().getAttribute(CURRENT_USER_PAGE); }

    default void setCurrentUserPage(int currentUserPage) { toSrc().setAttribute(CURRENT_USER_PAGE, currentUserPage); }

    default void setNumberOfUserPages(int numberOfProfilePages) { toSrc().setAttribute(NUMBER_OF_USER_PAGES, numberOfProfilePages); }

    default boolean hasUser() {
        return !(toSrc().getAttribute(USER_NAME) == null);
    }

    default User getProfile() {
        return (User) toSrc().getAttribute(PROFILE_NAME);
    }

    default void setProfile(User profile) {
        toSrc().setAttribute(PROFILE_NAME, profile);
    }

    default boolean hasProfile() {
        return !(toSrc().getAttribute(PROFILE_NAME) == null);
    }

    default User getCurrentTab() {
        return (User) toSrc().getAttribute(CURRENT_TAB);
    }

    default void setCurrentTab(String currentTab) {
        toSrc().setAttribute(CURRENT_TAB, currentTab);
    }

    default void setUserList(Collection<User> userList) {
        toSrc().setAttribute(USER_LIST, userList);
    }

    default void setMessageList(Collection<Message> messageList) {
        toSrc().setAttribute(MESSAGE_LIST, messageList);
    }

    default String getSearchText() {
        final Object searchText = toSrc().getAttribute(SEARCH_TEXT);
        if (searchText==null)
            return "";
        else
            return (String) toSrc().getAttribute(SEARCH_TEXT);
    }

    default void setSearchText(String searchText) { toSrc().setAttribute(SEARCH_TEXT, searchText); }

}

