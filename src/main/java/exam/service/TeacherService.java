package exam.service;

import exam.model.role.Teacher;
import exam.service.base.BaseService;

public interface TeacherService extends BaseService<Teacher> {

	/**
	 * 更新老师姓名
	 */
	public void updateName(String id, String name);
	
	/**
	 * 更改密码
	 * @param password 未经过MD5加密的密码
	 */
	public void updatePassword(String id, String password);
	
	/**
	 * 检查教师是否存在
	 * @param id 教职工号
	 * @return 存在返回true
	 */
	public boolean isExist(String id);
	
	/**
	 * 保存所教的班级
	 */
	public void updateTeachClazzs(String ids, String tid);
	
	/**
	 * 教师登录
	 * @param password 未经过md5加密的密码
	 */
	public Teacher login(String name, String password);
	
}
