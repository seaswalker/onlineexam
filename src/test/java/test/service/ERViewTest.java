package test.service;

import javax.annotation.Resource;

import org.junit.Test;

import exam.dto.ERView;
import exam.dto.StatisticsData;
import exam.service.ExaminationResultService;
import test.base.Base;

public class ERViewTest extends Base {
	
	@Resource
	private ExaminationResultService examinationResultService;

	@Test
	public void getViewById() {
		ERView view = examinationResultService.getViewById(3);
		System.out.println(view);
	}
	
	@Test
	public void getStatistics() {
		StatisticsData data = examinationResultService.getStatisticsData(4);
		System.out.println(data);
	}
	
}
