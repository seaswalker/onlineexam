package test.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import exam.dao.MajorDao;
import exam.model.Major;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MajorDaoTest {
	
	@Resource
	private MajorDao majorDao;

	@Test
	public void save() {
		Major major = new Major(1, "电子信息科学与技术");
		majorDao.save(major);
		System.out.println("保存成功");
	}
	
	@Test
	public void getById() {
		System.out.println(majorDao.getById(3).getName());
	}
	
	@Test
	public void update() {
		/*Major major = new Major(3, "计算机科学与技术");
		majorDao.executeSql(major);*/
	}
	
	@Test
	public void find() {
		System.out.println(majorDao.find(new Major(0, "计算机科学与技术")).size());
	}
	
	@Test
	public void delete() {
		majorDao.delete(3);
	}
	
}
