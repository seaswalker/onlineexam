package exam.service;

import exam.model.role.Student;
import exam.service.base.BaseService;

public interface StudentService extends BaseService<Student> {

	/**
	 * 检测一个学号是否存在
	 */
	public boolean isExisted(String id);
	
	/**
	 * 修改密码
	 * @param password 没有经过MD5加密的密码
	 */
	public void updatePassword(String id, String password);
	
	/**
	 * 管理员更新学生
	 * @param cid 班级id
	 */
	public void update(String id, String name, int cid);
	
}