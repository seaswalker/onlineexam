package exam.service;

import java.util.List;

import exam.model.Major;
import exam.service.base.BaseService;

public interface MajorService extends BaseService<Major> {

	/**
	 * 根据专业名称搜索
	 * @param name 专业名称
	 * @return 找不到返回null
	 */
	public Major findByName(String name);
	
	/**
	 * 根据年级查找
	 * @param grade 年级id
	 */
	public List<Major> findByGrade(int grade);
	
	/**
	 * 获取所有专业
	 */
	public List<Major> findAll();
	
	/**
	 * 专业修改
	 */
	public void update(int id, String name);
	
}
