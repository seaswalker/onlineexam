package exam.service;

import exam.dto.ERView;
import exam.model.ExaminationResult;
import exam.service.base.BaseService;

public interface ExaminationResultService extends BaseService<ExaminationResult> {

	/**
	 * 返回一个ERView视图，用于在客户端展现考试结果(含具体错误信息)
	 * @param id ExaminationResult id
	 * @return
	 */
	public ERView getViewById(int id);
	
}
