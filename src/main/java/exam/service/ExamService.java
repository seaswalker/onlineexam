package exam.service;

import exam.model.Exam;
import exam.service.base.BaseService;

public interface ExamService extends BaseService<Exam> {

    /**
     * 切换试卷的运行状态
     * @param examId
     * @param status
     * @param days
     */
    void switchStatus(int examId, String status, Integer days);

}
