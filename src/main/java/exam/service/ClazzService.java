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
	
	/**
	 * 不查出关联的年级和专业
	 */
	public List<Clazz> findClazzOnly(Clazz clazz);
	
	/**
	 * 检查班级是否存在
	 * @param grade 年级id
	 * @param major 专业id
	 * @param cno 班级号
	 */
	public boolean isExist(int grade, int major, int cno);
	
}
