package exam.service;

import java.util.List;

import exam.model.Clazz;
import exam.service.base.BaseService;

public interface ClazzService extends BaseService<Clazz> {

	/**
	 * 根据专业id查找
	 */
	public List<Clazz> findByMajor(int majorId);
	
	/**
	 * 根据年级查找
	 */
	public List<Clazz> findByGrade(int grade);
	
	/**
	 * 根据班号查找
	 */
	public List<Clazz> findByCNO(int cno);
	
}
