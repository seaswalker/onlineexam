package test.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import exam.dto.ClassDTO;
import exam.service.TeacherService;
import test.base.Base;

public class TeacherServiceTest extends Base {

	@Resource
	private TeacherService teacherService;
	
	@Test
	public void getClassesWithMajorAndGrade() {
		List<ClassDTO> result = teacherService.getClassesWithMajorAndGrade("1111");
		System.out.println(result.size());
	}
	
}
