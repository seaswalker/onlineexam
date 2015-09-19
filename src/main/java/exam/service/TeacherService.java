package exam.service;

import exam.model.role.Teacher;
import exam.service.base.BaseService;

public interface TeacherService extends BaseService<Teacher> {

	/**
	 * 更新老师姓名
	 */
	void updateName(String id, String name);
	
	/**
	 * 更改密码
	 * @param password 未经过MD5加密的密码
	 */
	void updatePassword(String id, String password);
	
	/**
	 * 检查教师是否存在
	 * @param id 教职工号
	 * @return 存在返回true
	 */
	boolean isExist(String id);
	
	/**
	 * 保存所教的班级
	 */
	void updateTeachClazzs(String ids, String tid);
	
	/**
	 * 教师登录
	 * @param password 未经过md5加密的密码
	 */
	Teacher login(String name, String password);

    /**
     * 修改教师的密码
     * @param id 教师id
     * @param newPassword 新密码
     */
    void modifyPassword(String id, String newPassword);
}
