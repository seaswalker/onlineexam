package exam.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.GradeDao;
import exam.dao.base.BaseDao;
import exam.model.Grade;
import exam.service.GradeService;
import exam.service.base.BaseServiceImpl;
import exam.util.DataUtil;

@Service("gradeService")
public class GradeServiceImpl extends BaseServiceImpl<Grade> implements GradeService {
	
	private GradeDao gradeDao;

	@Resource(name = "gradeDao")
	@Override
	protected void setBaseDao(BaseDao<Grade> baseDao) {
		super.baseDao = baseDao;
		this.gradeDao = (GradeDao) baseDao;
	}
	
	public Grade findByGrade(String grade) {
		Grade _grade = new Grade();
		_grade.setGrade(Integer.parseInt(grade));
		List<Grade> grades = gradeDao.find(_grade);
		return DataUtil.isValid(grades) ? grades.get(0) : null;
	}
	
	@Override
	public void batchDelete(String ids) {
		//TODO
	}

}
