package exam.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.base.BaseDao;
import exam.model.role.Teacher;
import exam.service.TeacherService;
import exam.service.base.BaseServiceImpl;

@Service("teacherService")
public class TeacherServiceImpl extends BaseServiceImpl<Teacher> implements TeacherService {

	@Resource(name = "teacherDao")
	@Override
	protected void setBaseDao(BaseDao<Teacher> baseDao) {
		super.baseDao = baseDao;
	}

}
