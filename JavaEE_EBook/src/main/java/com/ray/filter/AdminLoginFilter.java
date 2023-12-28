package com.ray.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/// http://localhost:8080/ebook/admin/... (this filter work if access to url match /admin/*)
@WebFilter("/admin/*")
public class AdminLoginFilter extends HttpFilter implements Filter {
       
   
    public AdminLoginFilter() {
        super();
    }

	
	public void destroy() {
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// convert ServletRequest request to HttpServletRequest httpRequest to get session attribute
		// because session belong to HttpServletRequest
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		/**
		 * getSession(parameter) -> default parameter = true
		 * parameter = true: if session not exist then create session
		 * parameter = false: if session not exist return null, else get session 
		 */
		HttpSession session = httpRequest.getSession(false);
		
		// if logged in. session.getAttribute("userEmail"), userEmail was created in LoginServlet
		boolean isLoggedIn = session != null && session.getAttribute("userEmail") != null;
		
		/**
		 * Exception cases: if not logged in
		 */
		// http://localhost:8080/ebook/admin/login
		String loginURI = httpRequest.getContextPath() + "/admin/login";
		// httpRequest.getRequestURI() = http://localhost:8080/ebook/admin/login
		boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
		// httpRequest.getRequestURI() = http://localhost:8080/ebook/admin/login.jsp
		boolean isLoginPage = httpRequest.getRequestURI().endsWith("login.jsp");

		// if not logged in
		if (isLoggedIn || isLoginRequest || isLoginPage) {
			chain.doFilter(request, response); // allow to access current page.
		} else { // if not logged in, allow access to login page only.
			RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.forward(httpRequest, response);
		}
	}

	
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
