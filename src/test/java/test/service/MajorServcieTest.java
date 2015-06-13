package test.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import exam.model.Major;
import exam.model.page.PageBean;
import exam.service.MajorService;
import test.base.Base;

public class MajorServcieTest extends Base {

	@Resource
	private MajorService majorService;
	
	@Test
	public void search() {
		List<Object> params = new ArrayList<Object>(1);
		params.add("自动化");
		PageBean<Major> pageBean = majorService.pageSearch(1, 8, 10, " where name like '%自动化%' ", null, null);
		System.out.println(pageBean.getRecords().size());
	}
	
}
