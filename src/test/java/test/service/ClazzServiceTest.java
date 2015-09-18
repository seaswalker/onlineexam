package test.service;

import exam.model.Clazz;
import exam.service.ClazzService;
import org.junit.Test;
import test.base.Base;

import javax.annotation.Resource;
import java.util.List;

/**测试ClazzService
 * Created by skywalker on 2015/9/18.
 */
public class ClazzServiceTest extends Base {

    @Resource
    private ClazzService clazzService;

    @Test
    public void testFindByExam() {
        List<Clazz> clazzs = clazzService.findByExam(4);
        System.out.println(clazzs);
    }

}
