package servlets;

import common.Wrapper;
import model.User;

import javax.servlet.http.HttpSession;
import java.util.Locale;

public interface HttpSessionWrapper extends Wrapper<HttpSession> {//, HttpSession {

    String LOCALE_NAME = "locale";
    String USER_NAME = "user";

    static HttpSessionWrapper from(HttpSession httpSession) {
        return () -> httpSession;
    }

    default Locale getLocale() {
        return (Locale) toSrc().getAttribute(LOCALE_NAME);
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

    default boolean hasLocale() {
        return !(toSrc().getAttribute(LOCALE_NAME) == null);
    }

    default boolean hasUser() {
        return !(toSrc().getAttribute(USER_NAME) == null);
    }

}


