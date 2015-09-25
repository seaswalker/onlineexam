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
	 * 学生登录
	 * @param username 用户名
	 * @param password 密码(未经过MD5加密)
	 * @return 登录成功返回学生对象，否则null
	 */
	public Student login(String username, String password);

	/**
	 * 修改密码
	 * @param id 学生id
	 * @param newPassword 未经过MD5加密的密码
	 */
	public void modifyPassword(String id, String newPassword);
	
	/**
	 * 更新学生，这个不能使用saveOrUpdate，因为这个的id是自己输入的
	 * @param cid 班号
	 * @param name 姓名
	 * @param id id
	 */
	public void updateStudent(int cid, String name, String id);
	
	/**
	 * 添加学生
	 * @param id
	 * @param name
	 * @param password
	 * @param cid
	 */
	public void saveStudent(String id, String name, String password, int cid);
	
}
