package exam.service;

import exam.model.Exam;
import exam.model.page.PageBean;
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
    
    /**
     * 判断该学会是否已经参加过此考试
     * @param eid 试卷id
     * @param sid 学生id
     * @return 参加过返回true
     */
    public boolean hasJoined(int eid, String sid);
    
    /**
     * 检查给定的试卷是否任然可用(没有被删除或停止运行)
     * @param eid 试卷id
     * @return 可用返回true
     */
    public boolean isUseful(int eid);
    
    /**
	 * 你懂的
	 * @param eid 试卷id
	 * @return
	 */
	public Exam getById(int eid);
	
	/**
	 * 分页查询某个教师的所有试卷
	 * @param pageCode 页码
	 * @param pageSize 每页的数量
	 * @param pageNumber 共显示多少个页码
	 * @param tid 教师id
	 * @return
	 */
	public PageBean<Exam> pageSearchByTeacher(int pageCode, int pageSize, int pageNumber, String tid);
	
	/**
	 * 分页查询一个学生可以参加的所有考试
	 * @param sid 学生id
	 * @return
	 */
	public PageBean<Exam> pageSearchByStudent(int pageCode, int pageSize, int pageNumber, String sid);
    
}
