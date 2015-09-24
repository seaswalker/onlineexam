package exam.service;

import java.util.List;

import exam.dto.ERView;
import exam.dto.StatisticsData;
import exam.dto.StudentReport;
import exam.model.ExaminationResult;
import exam.service.base.BaseService;

public interface ExaminationResultService extends BaseService<ExaminationResult> {

	/**
	 * 返回一个ERView视图，用于在客户端展现考试结果(含具体错误信息)
	 * @param id ExaminationResult id
	 * @return
	 */
	public ERView getViewById(int id);
	
	/**
	 * 获取给定试卷的统计数据
	 * @param eid 试卷id
	 * @return
	 */
	public StatisticsData getStatisticsData(int eid);
	
	/**
	 * 查询指定试卷的成绩报告数据
	 * @param eid 试卷id
	 * @return
	 */
	public List<StudentReport> getReportData(int eid);
	
}
