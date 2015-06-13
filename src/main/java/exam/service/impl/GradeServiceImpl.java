package exam.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.base.BaseDao;
import exam.model.Grade;
import exam.service.GradeService;
import exam.service.base.BaseServiceImpl;

@Service("gradeService")
public class GradeServiceImpl extends BaseServiceImpl<Grade> implements GradeService {

	@Resource(name = "gradeDao")
	@Override
	protected void setBaseDao(BaseDao<Grade> baseDao) {
		super.baseDao = baseDao;
	}

}
