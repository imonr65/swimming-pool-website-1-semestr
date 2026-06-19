package filters;

import models.Role;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/admin/*")
public class RolesFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        Role role = (Role) session.getAttribute("userRole");
        String servletPath = request.getServletPath();
        if (role == null) {
            response.sendRedirect("/signIn");
            return;
        }

        if (servletPath.contains("/admin")) {
            if (role.getName().equals("ADMIN")) {
                filterChain.doFilter(request, response);
            } else {
                response.sendRedirect("/");
            }
        } else if (servletPath.contains("/coach")) {
            if (role.getName().equals("COACH") || role.getName().equals("ADMIN")) {
                filterChain.doFilter(request, response);
            } else {
                response.sendRedirect("/");
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
