package exam.dao;

import exam.model.role.Manager;

public interface ManagerDao {
	
	/**
	 * 管理员登录
	 * 只提供此接口，管理员帐号的修改只限于DB
	 */
	public Manager login(String name, String password);

}
