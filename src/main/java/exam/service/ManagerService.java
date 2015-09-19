package exam.service;

import exam.model.role.Manager;

public interface ManagerService {

	/**
	 * 用户登录
	 */
	Manager login(String name, String password);
	
}
