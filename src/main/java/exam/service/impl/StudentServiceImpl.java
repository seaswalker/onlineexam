package exam.service.impl;

import java.math.BigInteger;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.StudentDao;
import exam.dao.base.BaseDao;
import exam.model.role.Student;
import exam.service.StudentService;
import exam.service.base.BaseServiceImpl;

@Service("studentService")
public class StudentServiceImpl extends BaseServiceImpl<Student> implements StudentService {
	
	private StudentDao studentDao;

	@Resource(name = "studentDao")
	@Override
	protected void setBaseDao(BaseDao<Student> baseDao) {
		super.baseDao = baseDao;
		this.studentDao = (StudentDao) baseDao;
	}
	
	public boolean isExisted(String id) {
		BigInteger result = (BigInteger) studentDao.queryForObject("select count(id) from student where id = " + id, BigInteger.class);
		return result.intValue() > 0;
	}

}
