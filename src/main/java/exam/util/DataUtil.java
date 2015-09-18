package exam.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import exam.model.Clazz;
import exam.model.Exam;
import exam.model.Grade;
import exam.model.Major;
import exam.model.Question;
import exam.model.QuestionType;
import exam.model.role.Teacher;
import exam.util.json.JSON;

/**
 * 数据工具
 * @author skywalker
 *
 */
public class DataUtil {
	
	/**
	 * 支持的图片扩展名
	 * jpg jpeg png gif bmp
	 */
	private static List<String> extensions;
	/**
	 * 支持的图片content-type
	 * image/jpeg image/png image/gif image/bmp
	 */
	private static List<String> contentTypes;
	/**
	 * 分页使用，判断是否是大于0的数字
	 */
	private static Pattern pattern = Pattern.compile("^[1-9][0-9]*$");
	
	public static boolean isValid(String str) {
		return str != null && !str.trim().equals("");
	}

	public static boolean isValid(String...strs) {
		for(String str : strs) {
			if(!isValid(str)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断List的有效性
	 */
	public static boolean isValid(List<?> list) {
		return ! (list == null || list.size() == 0);
	}
	
	/**
	 * 判断Map有效性
	 */
	public static boolean isValid(Map<?, ?> map) {
		return ! (map == null || map.size() == 0);
	}
	
	
	/**
	 * 判断Integer类型有效性
	 * 此处小于1即认为无效
	 */
	public static boolean isValid(Integer ...nums) {
		for(Integer i : nums) {
			if(i == null || i < 1) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 获取文件的扩展名
	 */
	public static String getExtend(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
	}
	
	/**
	 * 初始化扩展名、content-type
	 */
	private static void init() {
		//TODO 硬编码?
		extensions = new ArrayList<String>(Arrays.asList("jpg", "jpeg", "png", "gif", "bmp")) ;
		contentTypes = new ArrayList<String>(Arrays.asList("image/jpeg", "image/png", "image/gif", "image/bmp"));
	}
	
	/**
	 * 验证是否是图片类型
	 */
	public static boolean isImage(String extension, String contentType) {
		if(extensions == null || contentTypes == null) {
			init();
		}
		return extension.contains(extension) && contentTypes.contains(contentType);
	}
	
	/**
	 * 向客户端发送json格式数据
	 * @param useJSONStyle true --返回类型为application/json;charset=utf-8
	 * 否则 text/html
	 * 为了解决json格式跨域无法传送的问题
	 */
	public static void writeJSON(JSON json, HttpServletResponse response, boolean useJSONStyle) {
		if(response == null) {
			throw new NullPointerException("response为空");
		}
		PrintWriter out = null;
		try {
			response.setContentType(useJSONStyle ? "application/json;charset=utf-8" : "text/html;charset=utf-8");
			out = response.getWriter();
			out.write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(out != null) {
				out.close();
			}
		}
	}
	
	public static void writeJSON(JSON json, HttpServletResponse response) {
		writeJSON(json, response, true);
	}
	
	/**
	 * 校验验证码
	 */
	public static boolean checkVerify(String verify, HttpSession session) {
		String rand = (String) session.getAttribute("rand");
		return rand.equals(verify);
	}
	
	/**
	 * 是不是数字
	 * @param num
	 */
	public static boolean isNumber(String num) {
		if(!isValid(num)) {
			return false;
		}
		Matcher matcher = pattern.matcher(num);
		return matcher.matches();
	}
	
	/**
	 * 一堆字符串是不是数字
	 * @return 当且仅当全部是数字时返回true
	 */
	public static boolean isNumber(String...nums) {
		for(String num : nums) {
			if(!isNumber(num)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 获得页码
	 * @return 返回>=1的数字
	 * 如果给定的字符串不合法，返回1
	 */
	public static int getPageCode(String str) {
		if(isNumber(str)) {
			return Integer.parseInt(str);
		}
		return 1;
	}
	
	/**
	 * 根据给定的json串解析为一套试卷
	 * @param json 
	 * @param teacher 设置为和试卷的关联关系
	 * @return 解析完的试题
	 */
	public static Exam parseExam(String json, Teacher teacher) {
		if (!isValid(json) || teacher == null) {
			return null;
		}
		JSONObject node = JSONObject.fromObject(json);
		Exam exam = new Exam();
		//解析单选题
		int singlePoints = 0,
			multiPoints = 0,
			judgePoints = 0,
			sumPoints = 0;
		sumPoints += (singlePoints = parseQuestion(exam, JSONArray.fromObject(node.get("singles")), QuestionType.SINGLE, teacher));
		sumPoints += (multiPoints = parseQuestion(exam, JSONArray.fromObject(node.get("multis")), QuestionType.MULTI, teacher));
		sumPoints += (judgePoints = parseQuestion(exam, JSONArray.fromObject(node.get("judges")), QuestionType.JUDGE, teacher));
		exam.setSinglePoints(singlePoints);
		exam.setMultiPoints(multiPoints);
		exam.setJudgePoints(judgePoints);
		exam.setPoints(sumPoints);
		//解析设置选项
		JSONObject setting = JSONObject.fromObject(node.get("setting"));
		exam.setLimit(setting.getInt("timeLimit"));
		int status = setting.getInt("status");
		if (status == 1) {
			exam.setStatus(true);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, setting.getInt("runTime"));
			exam.setEndTime(calendar.getTime());
		}
		//解析适用的班级
		//TODO 暂且只实现添加一个班级，未来可能改进为添加整个年级、专业
		Clazz clazz = new Clazz();
		clazz.setCno(setting.getInt("clazz"));
		clazz.setGrade(new Grade(setting.getInt("grade")));
		clazz.setMajor(new Major(setting.getInt("major")));
		exam.addClazz(clazz);
		exam.setTitle(setting.getString("title"));
        exam.setTeacher(teacher);
		return exam;
	}
	
	/**
	 * 解析出一道题目
	 * @param exam 试卷对象，解析完成后直接加入试卷
	 * @param nodes 题目节点
	 * @param type 题目类型
	 * @return 返回此组题目的总分值
	 */
	private static int parseQuestion(Exam exam, JSONArray nodes, QuestionType type, Teacher teacher) {
		Question question = new Question();
		JSONObject jsonQuestion = null;
		//计算此组题目的分值
		int points = 0, point = 0;
		for (Object o : nodes) {
			question = new Question();
			jsonQuestion = JSONObject.fromObject(o);
			question.setTitle(jsonQuestion.getString("title"));
			if (type != QuestionType.JUDGE) {
				question.setOptionA(jsonQuestion.getString("optionA"));
				question.setOptionB(jsonQuestion.getString("optionB"));
				question.setOptionC(jsonQuestion.getString("optionC"));
				question.setOptionD(jsonQuestion.getString("optionD"));
			}
			point = jsonQuestion.getInt("point");
			question.setPoint(point);
			points += point;
			question.setAnswer(jsonQuestion.getString("answer"));
			question.setType(type);
			question.setTeacher(teacher);
			if (type == QuestionType.SINGLE) {
				exam.addSingleQuestion(question);
			} else if (type == QuestionType.MULTI) {
				exam.addMultiQuestion(question);
			} else {
				exam.addJudgeQuestion(question);
			}
		}
		return points;
	}
	
}
