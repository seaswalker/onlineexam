package exam.service.impl;

import javax.annotation.Resource;

import exam.dao.ExamDao;
import exam.dao.base.GenerateKeyCallback;
import exam.model.Clazz;
import exam.model.Question;
import exam.model.QuestionType;
import exam.util.DataUtil;

import org.springframework.stereotype.Service;

import exam.dao.base.BaseDao;
import exam.model.Exam;
import exam.service.ExamService;
import exam.service.QuestionService;
import exam.service.base.BaseServiceImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service("examService")
public class ExamServiceImpl extends BaseServiceImpl<Exam> implements ExamService {

    private ExamDao examDao;
    @Resource
    private QuestionService questionService;

	@Resource(name = "examDao")
	@Override
	protected void setBaseDao(BaseDao<Exam> baseDao) {
		super.baseDao = baseDao;
        this.examDao = (ExamDao) baseDao;
	}

    /**
     * 需要在保存exam后，建立试卷和题目、试卷和班级的关联关系
     * 由于此方法在一个事物之内，由此导致了外键的问题:
     * 设置试卷和试题以及试卷和班级的关联关系时，由于事物的原因，此时试卷实际上尚未存入数据库，试卷的主键当然不存在，
     * 所以导致了两个关联关系无法建立，所以删除了外键。。。
     */
    @Override
    public void saveOrUpdate(Exam entity) {
    	if (entity.getId() <= 0) {
	        //首先保存exam,需要获取新插入的记录的主键
	        int examId = saveExam(entity);
	        List<Integer> questionIds = saveQuestions(entity);
	        //设置试卷和班级的关联关系
	        saveExamClassRelationships(entity.getClazzs(), examId);
	        //设置题目和试卷的关联关系
	        saveExamQuestionRelationships(questionIds, examId);
    	}
    }

    /**
     * 辅助保存试卷
     * @param entity 试卷
     * @return 试卷id
     */
    private int saveExam(Exam entity) {
        String sql = "insert into exam values(null,?,?,?,?,?,?,?,?,?)";
        return examDao.getKeyHelper(sql, (ps, param) -> {
            Exam exam = (Exam) param;
            ps.setString(1, exam.getTitle());
            ps.setInt(2, exam.getLimit());
            ps.setTimestamp(3, exam.getEndTime() != null ? new Timestamp(exam.getEndTime().getTime()) : null);
            ps.setString(4, exam.getStatus().name());
            ps.setInt(5, exam.getPoints());
            ps.setInt(6, exam.getSinglePoints());
            ps.setInt(7, exam.getMultiPoints());
            ps.setInt(8, exam.getJudgePoints());
            ps.setString(9, exam.getTeacher().getId());
        }, entity);
    }

    /**
     * 辅助保存题目，并且返回自动生成的主键
     * @param entity 试卷
     * @return 题目id集合
     */
    private List<Integer> saveQuestions(Exam entity) {
        //保存所有新插入的问题的id
        List<Integer> questionIds = new ArrayList<>();
        //保存题目，同时返回题目的id
        String sql = "insert into question values(null,?,?,?,?,?,?,?,?,?)";
        QuestionGenerateKeyCallback questionGenerateKeyCallback = new QuestionGenerateKeyCallback();
        for (final Question question : entity.getSingleQuestions()) {
            questionIds.add(question.getId() > 0 ? question.getId() : examDao.getKeyHelper(sql, questionGenerateKeyCallback, question));
        }
        for (final Question question : entity.getMultiQuestions()) {
        	questionIds.add(question.getId() > 0 ? question.getId() : examDao.getKeyHelper(sql, questionGenerateKeyCallback, question));
        }
        for (final Question question : entity.getJudgeQuestions()) {
            questionIds.add(question.getId() > 0 ? question.getId() : examDao.getKeyHelper(sql, questionGenerateKeyCallback, question));
        }
        return questionIds;
    }

    /**
     * 辅助建立班级和试卷的关系
     * @param classes 此套试卷适用的班级
     * @param examId 试卷id
     */
    private void saveExamClassRelationships(List<Clazz> classes, int examId) {
        StringBuilder sb = new StringBuilder("insert into exam_class values");
        for (Clazz clazz : classes) {
            sb.append("(null,").append(examId).append(",").append(clazz.getId()).append("),");
        }
        sb.deleteCharAt(sb.length() - 1);
        examDao.executeSql(sb.toString());
    }

    /**
     * 辅助建立试卷和题目的关联关系
     * @param questionIds 问题id的集合
     * @param examId 试卷id
     */
    private void saveExamQuestionRelationships(List<Integer> questionIds, int examId) {
        StringBuilder sb = new StringBuilder("insert into exam_question values");
        for (Integer qid : questionIds) {
            sb.append("(null,").append(examId).append(",").append(qid).append("),");
        }
        System.out.println(sb.deleteCharAt(sb.length() - 1));
        examDao.executeSql(sb.toString());
    }

    @Override
    public void switchStatus(int examId, String status, Integer days) {
        String sql = "update exam set status = ?";
        List<Object> params = new ArrayList<>(3);
        params.add(status);
        if (DataUtil.isValid(days)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, days);
            sql += ",endtime = ?";
            params.add(new Timestamp(calendar.getTime().getTime()));
        }
        sql += " where id = ?";
        params.add(examId);
        examDao.executeSql(sql, params.toArray());
    }

    @Override
    public void delete(Object id) {
        int examId = (Integer) id;
        //删除试卷和班级的关联关系
        String sql = "delete from exam_class where eid = " + examId;
        examDao.executeSql(sql);
        //删除试卷和题目的关联关系
        sql = "delete from exam_question where eid = " + examId;
        examDao.executeSql(sql);
        //删除试卷
        sql = "delete from exam where id = " + examId;
        examDao.executeSql(sql);
        //TODO 删除此试卷的成绩
    }
    
    @Override
    public Exam findWithQuestions(Exam exam) {
    	//先查出试题
    	Exam result = examDao.find(exam).get(0);
    	List<Question> questions = questionService.findByExam(result.getId());
    	return filterQuestions(result, questions);
    }
    
    /**
     * 从全部题目中筛选出单选题、多选题以及判断题并且设置到exam中去
     * @param exam
     * @param questions
     */
    private Exam filterQuestions(Exam exam, List<Question> questions) {
    	questions.stream().forEach(question -> {
    		if (question.getType() == QuestionType.SINGLE) {
    			exam.addSingleQuestion(question);
    		} else if (question.getType() == QuestionType.MULTI) {
    			exam.addMultiQuestion(question);
    		} else {
    			exam.addJudgeQuestion(question);
    		}
    	});
    	return exam;
    }
    
    private static class QuestionGenerateKeyCallback implements GenerateKeyCallback {

        @Override
        public void setParameters(PreparedStatement ps, Object param) throws SQLException {
            Question question = (Question) param;
            ps.setString(1, question.getTitle());
            ps.setString(2, question.getOptionA());
            ps.setString(3, question.getOptionB());
            ps.setString(4, question.getOptionC());
            ps.setString(5, question.getOptionD());
            ps.setInt(6, question.getPoint());
            ps.setString(7, question.getType().name());
            ps.setString(8, question.getAnswer());
            ps.setString(9, question.getTeacher().getId());
        }

    }
}
