package exam.service;

import exam.model.Grade;
import exam.service.base.BaseService;

public interface GradeService extends BaseService<Grade> {

	/**
	 * 根据年级搜索
	 */
	public Grade findByGrade(String grade);
	
}
