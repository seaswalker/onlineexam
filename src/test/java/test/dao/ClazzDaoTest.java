package test.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import exam.dao.ClazzDao;
import exam.model.Clazz;
import exam.model.Grade;
import exam.model.Major;
import test.base.Base;

public class ClazzDaoTest extends Base {

	@Resource
	private ClazzDao clazzDao;
	
	@Test
	public void save() {
		Clazz clazz = new Clazz();
		clazz.setCno(2);
		clazz.setGrade(new Grade(1));
		clazz.setMajor(new Major(1));
		clazzDao.save(clazz);
	}
	
	@Test
	public void find() {
		Clazz clazz = new Clazz();
		clazz.setMajor(new Major(1));
		List<Clazz> list = clazzDao.find(clazz);
		System.out.println(list.size());
	}
	
}
