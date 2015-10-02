package exam.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import exam.dto.ExaminationAnswer;
import exam.dto.MarkedQuestion;
import exam.model.Clazz;
import exam.model.Exam;
import exam.model.ExamStatus;
import exam.model.ExaminationResult;
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
public abstract class DataUtil {
	
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
			Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, setting.getInt("runTime"));
            exam.setEndTime(calendar.getTime());
            exam.setStatus(ExamStatus.RUNNING);
        } else {
        	exam.setStatus(ExamStatus.NOTRUN);
        }
        //解析适用的班级,如果有多个适用的班级，那么cid的格式为1,2,3
        String cids = setting.getString("cid");
        Grade grade = new Grade(setting.getInt("gid"));
        Major major = new Major(setting.getInt("mid"));
        for (String cid : cids.split(",")) {
        	Clazz clazz = new Clazz();
        	clazz.setGrade(grade);
        	clazz.setMajor(major);
        	clazz.setId(Integer.parseInt(cid));
        	exam.addClazz(clazz);
        }
		exam.setTitle(StringUtil.htmlEncode(setting.getString("title")));
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
		String idStr = null;
		for (Object o : nodes) {
			question = new Question();
			jsonQuestion = JSONObject.fromObject(o);
			question.setTitle(jsonQuestion.getString("title"));
			if (isValid((idStr = jsonQuestion.getString("id")))) {
				question.setId(Integer.parseInt(idStr));
			}
			if (type != QuestionType.JUDGE) {
				question.setOptionA(jsonQuestion.getString("optionA"));
				question.setOptionB(jsonQuestion.getString("optionB"));
				question.setOptionC(jsonQuestion.getString("optionC"));
				question.setOptionD(jsonQuestion.getString("optionD"));
			}
			point = jsonQuestion.getInt("point");
			question.setPoint(point);
			points += point;
			question.setType(type);
			question.setAnswer(jsonQuestion.getString("answer"));
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

	/**
	 * 把从客户端传来的json格式的考试结果封装为ExaminationResult对象
	 * 格式示例:{eid:1,questions:[{id:1,answer:2,3}]}
	 * @param result
	 * @return ExaminationResult
	 */
	public static ExaminationAnswer parseAnswers(String result) {
		JSONObject json = JSONObject.fromObject(result);
		ExaminationAnswer er = new ExaminationAnswer();
		er.setExamId(json.getInt("eid"));
		JSONArray questions = json.getJSONArray("questions");
		JSONObject question = null;
		for (Object o : questions) {
			question = JSONObject.fromObject(o);
			er.addQuestion(question.getInt("id"), question.getString("answer"));
		}
		return er;
	}

	/**
	 * 批卷
	 * @param er 考生的答案
	 * @param exam 所考的试卷，包含各个题目
	 */
	public static ExaminationResult markExam(ExaminationAnswer ea, Exam exam, String sid) {
		ExaminationResult er = new ExaminationResult();
		er.setExamId(exam.getId());
		er.setStudentId(sid);
		er.setExamTitle(exam.getTitle());
		er.setTime(new Date());
		//计算总分
		int sum = 0;
		Map<Integer, String> answers = ea.getAnswers();
		sum += markHelper(exam.getSingleQuestions(), answers, er);
		sum += markHelper(exam.getMultiQuestions(), answers, er);
		sum += markHelper(exam.getJudgeQuestions(), answers, er);
		er.setPoint(sum);
		return er;
	}
	
	/**
	 * 辅助批卷-批阅一道题
	 * @return questions的总分数
	 */
	private static int markHelper(List<Question> questions, Map<Integer, String> answers, ExaminationResult er) {
		int point = 0;
		MarkedQuestion mq = null;
		String wa = null;
		for (Question q : questions) {
			mq = new MarkedQuestion();
			mq.setQuestionId(q.getId());
			wa = answers.get(q.getId());
			if (q.getAnswer().equals(wa)) {
				mq.setRight(true);
				point += q.getPoint();
			} else {
				mq.setRight(false);
			}
 			mq.setRight(q.getAnswer().equals(wa));
			mq.setWrongAnswer(wa);
		}
		er.addMarkedQuestion(mq);
		return point;
	}
	
}
