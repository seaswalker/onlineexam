package exam.service;

import exam.model.role.Student;
import exam.service.base.BaseService;

public interface StudentService extends BaseService<Student> {

	/**
	 * 检测一个学号是否存在
	 */
	public boolean isExisted(String id);
	
}
