package exam.service.impl;

import javax.annotation.Resource;

import exam.dao.ExamDao;
import exam.dao.base.GenerateKeyCallback;
import exam.model.Clazz;
import exam.model.ExamStatus;
import exam.model.Question;
import exam.model.QuestionType;
import exam.util.DataUtil;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.stereotype.Service;

import exam.dao.base.BaseDao;
import exam.model.Exam;
import exam.model.page.PageBean;
import exam.service.ExamService;
import exam.service.QuestionService;
import exam.service.base.BaseServiceImpl;

import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    	} else {
    		String sql = "update exam set title = ?, timelimit = ? where id = ?";
    		examDao.executeSql(sql, new Object[] {entity.getTitle(), entity.getLimit(), entity.getId()});
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
        //注意一个问题:delete语句里面不能用别名
        String[] sqls = {
        		//删除试卷和班级的关联关系
        		"delete from exam_class where eid = " + examId,
        		//删除试卷和题目的关联关系
        		"delete from exam_question where eid = " + examId,
        		//删除examinationresult_question表的内容
        		"delete from examinationresult_question where erid in (select er.id from examinationresult er where er.eid = " + examId + ")",
        		//删除examinationresult表的记录
        		"delete from examinationresult where eid = " + examId,
        		//删除试卷
        		"delete from exam where id = " + examId
        };
        examDao.batchUpdate(sqls);
    }
    
    @Override
    public Exam findWithQuestions(Exam exam) {
    	//先查出试题
    	Exam result = getById(exam.getId());
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
    
    @Override
    public boolean hasJoined(int eid, String sid) {
    	String sql = "select count(id) from examinationresult where eid = " + eid + " and sid = '" + 
    			sid + "'";
    	BigInteger count = (BigInteger) examDao.queryForObject(sql, BigInteger.class);
    	return count.intValue() > 0;
    }
    
    @Override
    public boolean isUseful(int eid) {
    	String sql = "select count(id) from exam where id = " + eid +" and status = 'RUNNING'";
    	BigInteger result = (BigInteger) examDao.queryForObject(sql, BigInteger.class);
    	return result.intValue() > 0;
    }
    
    @Override
	public Exam getById(int eid) {
		List<Exam> list = examDao.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String sql = "{call getExamById(?)}";
				CallableStatement cs = con.prepareCall(sql);
				//设置存储过程的参数
				cs.setInt(1, eid);
				return cs;
			}
		}, ExamCallableStatementCallback.instance);
		return list.get(0);
	}

	@Override
	public PageBean<Exam> pageSearchByStudent(int pageCode, int pageSize, int pageNumber, String sid) {
		//查询出所有的试卷
		List<Exam> list = examDao.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String sql = "{call getExamByStudent(?, ?, ?)}";
				CallableStatement cs = con.prepareCall(sql);
				cs.setString(1, sid);
				cs.setInt(2, pageCode);
				cs.setInt(3, pageSize);
				return cs;
			}
		}, ExamCallableStatementCallback.instance);
		//查出所有的记录数
		String sql = "select count(e.id) from exam e where e.id in (select eid from exam_class where cid = (select cid from student where id = '" +
		 sid + "'))";
		BigInteger count = (BigInteger) examDao.queryForObject(sql, BigInteger.class);
		return new PageBean<>(list, pageSize, pageCode, count.intValue(), pageNumber);
	}
	
	@Override
	public PageBean<Exam> pageSearchByTeacher(int pageCode, int pageSize, int pageNumber, String tid) {
		//查询出所有的试卷
		List<Exam> list = examDao.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String sql = "{call getExamByTeacher(?, ?, ?)}";
				CallableStatement cs = con.prepareCall(sql);
				cs.setInt(1, pageCode);
				cs.setInt(2, pageSize);
				cs.setString(3, tid);
				return cs;
			}
		}, ExamCallableStatementCallback.instance);
		//查出所有的记录数
		String sql = "select count(e.id) from exam e where e.tid = '" + tid + "'";
		BigInteger count = (BigInteger) examDao.queryForObject(sql, BigInteger.class);
		return new PageBean<>(list, pageSize, pageCode, count.intValue(), pageNumber);
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
    
    /**
     * 回调函数封装试卷对象，共getById()和pageSearch()使用
     * @author skywalker
     *
     */
    private static class ExamCallableStatementCallback implements CallableStatementCallback<List<Exam>> {
    	
    	static ExamCallableStatementCallback instance = new ExamCallableStatementCallback();
    	
    	private ExamCallableStatementCallback() {}

		@Override
		public List<Exam> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
			List<Exam> list = new ArrayList<>();
			ResultSet rs = cs.executeQuery();
			Exam exam = null;
			while (rs.next()) {
				exam = new Exam();
				exam.setId(rs.getInt("id"));
				exam.setTitle(rs.getString("title"));
				exam.setStatus(ExamStatus.valueOf(rs.getString("status")));
				exam.setEndTime(rs.getTimestamp("endtime"));
				exam.setJudgePoints(rs.getInt("judgepoints"));
				exam.setLimit(rs.getInt("timelimit"));
				exam.setMultiPoints(rs.getInt("multipoints"));
				exam.setPoints(rs.getInt("points"));
				exam.setSinglePoints(rs.getInt("singlepoints"));
				list.add(exam);
			}
			return list;
		}
    	
    }

}
