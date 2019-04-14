package com.servlet.webservlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.servlet.domain.User;
import com.servlet.service.UserService;

public class LoginServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 0. 设置编码（显示中文）
		response.setContentType("text/html;charset=utf-8");
		// 1.接收用户名和密码
		String username = request.getParameter("username");//name属性中的string值
		String password = request.getParameter("password");
		
		// 2.调用userservice 里的login(username,password) 返回值：User user
		User user = null;
		try {
			user = new UserService().login(username,password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 3.判断user是否为空
		if(user == null) {
			// 3.1 若为空，写提示信息
			response.getWriter().print("用户名和密码不匹配");
		}else {
			// 3.2 若不为空，写提示信息
			response.getWriter().print(user.getUsername()+":欢迎回来");
		}
	}
}
