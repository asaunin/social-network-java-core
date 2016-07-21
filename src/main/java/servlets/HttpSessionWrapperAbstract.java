package servlets;

import model.User;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@Deprecated
public abstract class HttpSessionWrapperAbstract {

    private static final String LOCALE_NAME = "locale";
    private static final String USER_NAME = "user";

    private static HttpSession session;

    public static HttpSessionWrapperAbstract getInstance(HttpSession httpSession) {
        session = httpSession;
        try {
            return HttpSessionWrapperAbstract.class.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static Locale getLocale() {
        return (Locale) session.getAttribute(LOCALE_NAME);
    }

    public static void setLocale(Locale locale) {
        session.setAttribute(LOCALE_NAME, locale);
    }

    public static void setUser(User user) {
        session.setAttribute(USER_NAME, user);
    }

    public static boolean hasLocale() {
        return !(session.getAttribute(LOCALE_NAME) == null);
    }

    public static boolean hasUser() {
        return !(session.getAttribute(USER_NAME) == null);
    }

}
