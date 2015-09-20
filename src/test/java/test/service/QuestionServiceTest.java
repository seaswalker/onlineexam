package test.service;

import javax.annotation.Resource;

import org.junit.Test;

import exam.model.Question;
import exam.model.page.PageBean;
import exam.service.QuestionService;
import test.base.Base;

public class QuestionServiceTest extends Base {
	
	@Resource
	private QuestionService questionService;

	@Test
	public void pageSearch() {
		String sql = "where tid = '1000' and type = 'SINGLE'";
		PageBean<Question> pageBean = questionService.pageSearch(1, 8, 10, sql, null, null);
		System.out.println(pageBean);
	}
	
	@Test
	public void isUsedByExam() {
		System.out.println(questionService.isUsedByExam(6));
	}
	
}
