package test.service;

import exam.service.ExamService;
import org.junit.Test;
import test.base.Base;

import javax.annotation.Resource;

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

}
