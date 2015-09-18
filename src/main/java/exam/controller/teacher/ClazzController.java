package exam.controller.teacher;

import exam.model.Clazz;
import exam.service.ClazzService;
import exam.util.DataUtil;
import exam.util.json.JSONArray;
import exam.util.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**教师角色下的班级操作
 * Created by skywalker on 2015/9/18.
 */
@Controller("exam.controller.teacher.ClazzController")
@RequestMapping("/teacher/clazz")
public class ClazzController {

    @Resource
    private ClazzService clazzService;

    /**
     * 根据试卷id返回此试卷适用的班级
     * @param examId 试卷id
     */
    @RequestMapping("/list")
    @ResponseBody
    public void list(Integer examId, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        if (!DataUtil.isValid(examId)) {
            json.addElement("result", "0").addElement("message", "参数无效");
        } else {
            JSONArray array = new JSONArray();
            List<Clazz> clazzs = clazzService.findByExam(examId);
            for (Clazz clazz : clazzs) {
                array.addObject(clazz.getJSON());
            }
            json.addElement("result", "1").addElement("data", array);
        }
        DataUtil.writeJSON(json, response);
    }

    /**
     * 重新建立试卷和班级的关联关系
     * @param examId 试卷id
     * @param clazzIds 班级id，格式:"1, 2, 3"
     * @param response
     */
    @RequestMapping("/reset")
    @ResponseBody
    public void reset(Integer examId, String clazzIds, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        if (!DataUtil.isValid(examId) || !DataUtil.isValid(clazzIds)) {
            json.addElement("result", "0");
        } else {
            clazzService.resetExamClass(examId, clazzIds);
            json.addElement("result", "1");
        }
        DataUtil.writeJSON(json, response);
    }

}
