package servlets;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@FunctionalInterface
public interface HttpFilter extends Filter {

    void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;

    @Override
    default void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }
}
