package test.service;

import javax.annotation.Resource;

import org.junit.Test;

import exam.model.page.PageBean;
import exam.model.role.Student;
import exam.service.StudentService;
import test.base.Base;

public class StudentServiceTest extends Base{

	@Resource
	private StudentService studentService;
	
	@Test
	public void pageSearch() {
		PageBean<Student> result = studentService.pageSearch(1, 10, 10, null, null, null);
		System.out.println(result.getRecords().size());
	}
	
	@Test
	public void isExisted() {
		System.out.println(studentService.isExisted("201201050536"));
	}
	
}
