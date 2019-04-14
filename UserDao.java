package com.servlet.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.servlet.domain.User;
import com.servlet.utils.DataSourceUtils;

public class UserDao {
	
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return 用户
	 * @throws SQLException 
	 */
	public User getUserByUsernameAndPwd(String username, String password) throws SQLException {
		// 创建queryrunner
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		
		//编写sql
		String sql = "select * from user where username = ? and password = ?";
		//执行sql语句
		User user = qr.query(sql, new BeanHandler<>(User.class),username,password);
		return user;
	}

}
