package com.ray.controller.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ray.service.UserService;
import com.ray.utilities.CryptoUtil;


@WebServlet("/admin/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;
       
    
    public LoginServlet() {
        super();
        userService = new UserService();
    }

    /*
	 * manage LOGOUT
	 * */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// remove session attribute
		request.getSession().removeAttribute("userEmail");
		response.sendRedirect(request.getContextPath() + "/admin/login.jsp");
	}

	/*
	 * manage LOGIN
	 * */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		// do login. if fail return error massage
		String errorMessage = userService.checkLogin(
					email, 
					CryptoUtil.hashPassword(password, getServletContext().getInitParameter("salt"))
				);

		// if login fail, forward to login page
		if (errorMessage != null) {
			request.setAttribute("message", errorMessage);

			/// http://localhost:8080/ebook/admin/login.jsp
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("login.jsp");
			requestDispatcher.forward(request, response);
			return;
		}

		// if success, pass data {"userEmail", email} to admin page
		request.getSession().setAttribute("userEmail", email);

		/// http://localhost:8080/ebook/admin/
		response.sendRedirect(request.getContextPath() + "/admin/");
	}

}
