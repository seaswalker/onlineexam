package exam.controller.teacher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.dto.StatisticsData;
import exam.dto.StudentReport;
import exam.model.Exam;
import exam.model.page.PageBean;
import exam.model.role.Teacher;
import exam.service.ExamService;
import exam.service.ExaminationResultService;
import exam.util.DataUtil;
import exam.util.ExcelUtil;
import exam.util.JFreechartUtil;
import exam.util.json.JSON;
import exam.util.json.JSONArray;
import exam.util.json.JSONObject;

/**
 * 教师角色-试卷相关控制
 * @author skywalker
 *
 */
@Controller("exam.controller.teacher.ExamController")
@RequestMapping("/teacher/exam")
public class ExamController {
	
	@Resource
	private ExamService examService;
	@Resource
	private ExaminationResultService examinationResultService;
	@Value("#{properties['exam.pageSize']}")
	private int pageSize;
	@Value("#{properties['exam.pageNumber']}")
	private int pageNumber;

	/**
	 * 返回试卷列表
	 * @param pn 页码，输入输入非法，那么为1
	 * @param search 搜索内容，按照标题搜索
	 */
	@RequestMapping("/list")
	public String list(String pn, String search, Model model) {
		int pageCode = DataUtil.getPageCode(pn);
		String where = "where 1 = 1 ";
		if (DataUtil.isValid(search)) {
			where += " and title like '%" + search + "%'"; 
		}
		PageBean<Exam> pageBean = examService.pageSearch(pageCode, pageSize, pageNumber, where, null, null);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("search", search);
		return "teacher/exam_list";
	}
	
	/**
	 * 转向试卷添加页面
	 */
	@RequestMapping("/add")
	public String addUI() {
		return "teacher/exam_add";
	}
	
	/**
	 * 添加一套试卷
	 * @param exam 包含所有题目以及设置信息的json字符串
	 */
	@RequestMapping("/save")
	@ResponseBody
	public void add(String exam, HttpServletRequest request, HttpServletResponse response) {
		Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
		JSON json = new JSONObject();
		Exam result = DataUtil.parseExam(exam, teacher);
		//总分为零，说明试卷为空，不允许
		if (result.getPoints() == 0) {
			json.addElement("result", "0").addElement("message", "请不要提交空试卷!");
		} else {
			examService.saveOrUpdate(result);
			json.addElement("result", "1");
		}
		DataUtil.writeJSON(json, response);
	}

    /**
     * 删除一套试题
     * @param examId 试卷id
     * @param response
     */
    @RequestMapping("/remove")
    @ResponseBody
    public void delete(Integer examId, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        if (!DataUtil.isValid(examId)) {
            json.addElement("result", "0");
        } else {
            examService.delete(examId);
            json.addElement("result", "1");
        }
        DataUtil.writeJSON(json, response);
    }

    /**
     * 切换试卷的状态
     * @param examId 试卷id
     * @param days 运行的天数，此参数仅在切换至正正在运行(RUNNING)状态时才有效
     * @param status 要切换到的状态
     */
    @RequestMapping("/status")
    @ResponseBody
    public void switchStatus(Integer examId, String status, Integer days, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        if (!DataUtil.isValid(examId) || !DataUtil.isValid(status)) {
            json.addElement("result", "0");
        } else {
            examService.switchStatus(examId, status, days);
            json.addElement("result", "1");
        }
        DataUtil.writeJSON(json, response);
    }
    
    /**
     * 转向统计信息页面
     * 这里不直接统计，转而先返回到一个页面，再用ajax请求的原因是防止长时间后才会给客户端相应
     * @param eid 试卷id
     */
    @RequestMapping("/statistics/{eid}")
    public String toStatistics(@PathVariable Integer eid, Model model) {
    	model.addAttribute("eid", eid);
    	return "teacher/statistics";
    }
    
    /**
     * 处理ajax请求，真正实现统计功能
     * 客户端需要展现:
     * 1.一个饼图，包含分数低于总分60%的百分比，60%-80%的百分比，80%-90%，90%-100%四个区间
     * 2.最高分、最低分及考生姓名
     * 3.试卷题目、参加考试的总人数
     * @param eid 试卷id
     * @throws IOException 
     */
    @RequestMapping("/statistics/do/{eid}")
    @ResponseBody
    public void statistics(@PathVariable Integer eid, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	//获得统计信息
    	StatisticsData data = examinationResultService.getStatisticsData(eid);
    	//生成统计图
    	String realPath = request.getServletContext().getRealPath("/") + "/";
    	String imagePath = "charts/pie_" + eid + ".png";
 		JFreechartUtil.generateChart(data, realPath + imagePath);
 		JSONObject json = new JSONObject();
 		json.addElement("result", "1").addElement("url", imagePath).addElement("highestPoint", String.valueOf(data.getHighestPoint()))
 			.addElement("lowestPoint", String.valueOf(data.getLowestPoint())).addElement("title", data.getTitle()).addElement("count", String.valueOf(data.getPersonCount()));
 		//最高分学生名单
 		JSONArray highestNames = new JSONArray();
 		for (String name : data.getHighestNames()) {
 			highestNames.addElement("name", name);
 		}
 		json.addElement("highestNames", highestNames);
 		JSONArray lowestNames = new JSONArray();
 		for (String name : data.getLowestNames()) {
 			lowestNames.addElement("name", name);
 		}
 		json.addElement("lowestNames", lowestNames);
 		DataUtil.writeJSON(json, response);
    }
    
    /**
     * 转向文件下载页面，这么做(而不是直接下载)的原因是生成xls文件可能给客户端造成明显的卡顿感
     * @param eid 试卷id
     * @return
     */
    @RequestMapping("/download/{eid}")
    public String download(@PathVariable Integer eid, Model model) {
    	model.addAttribute("eid", eid);
    	return "teacher/download";
    }
    
    /**
     * 生成excel成绩报告单
     * @param eid 试卷id
     * @return
     * @throws IOException 
     */
    @RequestMapping("/report/{eid}")
    @ResponseBody
    public void report(@PathVariable("eid") Integer eid, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	List<StudentReport> reportData = examinationResultService.getReportData(eid);
    	String path = request.getServletContext().getRealPath("/") + "/reports/report_" + eid + ".xls";
    	InputStream is = ExcelUtil.generateExcel(reportData, path); 
    	//设置文件下载响应头
    	response.setContentType("application/zip");
    	//生成下载的文件名,解决中文文件名不显示的问题
    	String fileName = URLEncoder.encode(reportData.get(0).getTitle() + "成绩单.xls", "UTF-8");
    	response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
    	OutputStream os = response.getOutputStream();
    	byte[] b = new byte[1024];
    	int i = 0;
    	while ((i = is.read(b)) > 0) {
    		os.write(b, 0, i);
    	}
    	os.flush();
    	is.close();
    	os.close();
    }

}
