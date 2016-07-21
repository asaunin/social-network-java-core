package listeners;

import lombok.extern.log4j.Log4j;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Log4j
@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent e) {
        // Possibly create a stack trace here, and dump it
        log.info("Session created: " + e.getSession().getId() + ", timeout "
                + e.getSession().getMaxInactiveInterval());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent e) {
        log.info("Session destroyed: " + e.getSession().getId());
    }

}