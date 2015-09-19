package test.dao;

import javax.annotation.Resource;

import org.junit.Test;

import exam.dao.GradeDao;
import exam.model.Grade;

/**
 * 测试年级操作
 * @author skywalker
 *
 */
public class GradeDaoTest {
	
	@Resource
	private GradeDao gradeDao;

	@Test
	public void save() {
		Grade grade = new Grade();
		grade.setGrade(2012);
		gradeDao.save(grade);
	}
	
	@Test
	public void update() {
		//Grade grade = new Grade();
		//gradeDao.executeSql(grade);
	}
	
	@Test
	public void delete() {
		gradeDao.delete(1);
	}
	
}
