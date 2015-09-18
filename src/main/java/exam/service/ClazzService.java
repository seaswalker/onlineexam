package exam.service;

import java.util.List;

import exam.model.Clazz;
import exam.service.base.BaseService;

public interface ClazzService extends BaseService<Clazz> {

	/**
	 * 根据专业id查找
	 */
	List<Clazz> findByMajor(int majorId);
	
	/**
	 * 根据年级查找
	 */
	List<Clazz> findByGrade(int grade);
	
	/**
	 * 根据班号查找
	 */
	List<Clazz> findByCNO(int cno);
	
	/**
	 * 不查出关联的年级和专业
	 */
	List<Clazz> findClazzOnly(Clazz clazz);
	
	/**
	 * 根据教师id查出所教的班级
	 */
	List<Clazz> findByTeacher(String tid);
	
	/**
	 * 检查班级是否存在
	 * @param grade 年级id
	 * @param major 专业id
	 * @param cno 班级号
	 */
	boolean isExist(int grade, int major, int cno);

    /**
     * 根据试卷id查找适用的班级
     * @param examId 试卷id
     * @return 班级列表
     */
    List<Clazz> findByExam(Integer examId);

    /**
     * 重建试卷和班级的关联
     * @param examId
     * @param clazzIds 格式: "1, 2, 3"
     */
    void resetExamClass(int examId, String clazzIds);
}
