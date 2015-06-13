package exam.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.base.BaseDao;
import exam.model.role.Student;
import exam.service.StudentService;
import exam.service.base.BaseServiceImpl;

@Service("studentService")
public class StudentServiceImpl extends BaseServiceImpl<Student> implements StudentService {

	@Resource(name = "studentDao")
	@Override
	protected void setBaseDao(BaseDao<Student> baseDao) {
		super.baseDao = baseDao;
	}

}
