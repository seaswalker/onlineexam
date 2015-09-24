package test.service;

import javax.annotation.Resource;

import org.junit.Test;

import test.base.Base;
import exam.model.Exam;
import exam.service.ExamService;

/**
 * Created by skywalker on 2015/9/19.
 */
public class ExamServiceTest extends Base {

    @Resource
    private ExamService examService;

    @Test
    public void testSwitchStatus() {
        examService.switchStatus(4, "RUNNING", 12);
    }
    
    @Test
    public void findWithQuestion() {
    	Exam exam = new Exam();
    	exam.setId(8);
    	Exam result = examService.findWithQuestions(exam);
    	System.out.println(result);
    }
    
    @Test
    public void hasJoined() {
    	System.out.println(examService.hasJoined(4, "201201050538"));
    }
    
    /**
     * 预计产生的效果:
     * exam_class 8, 9, 10
     * exam_question 1,2,3
     * examinationresult_question 7-12
     * examinationresult 3, 4
     * exam 4
     */
    @Test
    public void delete() {
    	examService.delete(4);
    }

}
