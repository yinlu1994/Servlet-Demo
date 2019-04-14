package com.servlet.service;

import java.sql.SQLException;

import com.servlet.dao.UserDao;
import com.servlet.domain.User;

public class UserService {
	
	/**
	 * 用户登录
	 * @param 用户名
	 * @param 密码
	 * @return User用户
	 * @throws SQLException 
	 */
	public User login(String username, String password) throws SQLException {
		UserDao dao = new UserDao();
		return dao.getUserByUsernameAndPwd(username,password);
	}

}
