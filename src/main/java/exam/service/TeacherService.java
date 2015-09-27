package exam.service;

import java.util.List;

import exam.dto.ClassDTO;
import exam.model.role.Teacher;
import exam.service.base.BaseService;

public interface TeacherService extends BaseService<Teacher> {

	/**
	 * 更新老师姓名
	 */
	void updateName(String id, String name);
	
	/**
	 * 更改密码
	 * 此方法会把初始密码标志位改为true(已经修改密码)
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
     * 不能用saveOrUpdate()!!!!!
     * @param id
     * @param name
     * @param password
     */
    void saveTeacher(String id, String name, String password);
    
    /**
     * 获取特定教师所教的班级及其专业、年级信息
     * @param tid 教师id
     * @return
     */
    List<ClassDTO> getClassesWithMajorAndGrade(String tid);
}
