package com.kindustry.mybatis.test;

public interface UserMapper {
	public void insertUser(User user);

	public User getUser(String name);
}
