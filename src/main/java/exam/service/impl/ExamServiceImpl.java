package exam.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.base.BaseDao;
import exam.model.Exam;
import exam.service.ExamService;
import exam.service.base.BaseServiceImpl;

@Service("examService")
public class ExamServiceImpl extends BaseServiceImpl<Exam> implements ExamService {

	@Resource(name = "examDao")
	@Override
	protected void setBaseDao(BaseDao<Exam> baseDao) {
		super.baseDao = baseDao;
	}

}
