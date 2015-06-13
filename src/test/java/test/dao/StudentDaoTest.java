package test.dao;

import javax.annotation.Resource;

import org.junit.Test;

import test.base.Base;
import exam.dao.StudentDao;
import exam.model.Clazz;
import exam.model.role.Student;
import exam.util.StringUtil;

/**
 * 测试StudentDao
 * @author skywalker
 *
 */
public class StudentDaoTest extends Base {

	@Resource
	private StudentDao studentDao;
	
	@Test
	public void testSave() {
		Student student = new Student();
		student.setName("习近平").setPassword(StringUtil.md5("1234"));
		Clazz clazz = new Clazz();
		clazz.setId(1);
		student.setClazz(clazz);
		studentDao.save(student);
	}
	
}
