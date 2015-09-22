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
    public void switchStatus(int examId, String status, Integer days);
    
    /**
     * 查找试卷，还要查出关联的题目
     * @param exam
     * @return 没有返回list的必要，应该不存在需要查出多套试题且带题目的场景
     */
    public Exam findWithQuestions(Exam exam);
    
}
