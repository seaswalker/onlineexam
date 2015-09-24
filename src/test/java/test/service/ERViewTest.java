package test.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import exam.dto.ERView;
import exam.dto.StatisticsData;
import exam.dto.StudentReport;
import exam.service.ExaminationResultService;
import exam.util.ExcelUtil;
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
	
	@Test
	public void getReportData() {
		List<StudentReport> result = examinationResultService.getReportData(4);
		System.out.println(result);
	}
	
	@Test
	public void saveExcel() throws IOException {
		List<StudentReport> result = examinationResultService.getReportData(4);
		ExcelUtil.generateExcel(result, "");
	}
	
}
