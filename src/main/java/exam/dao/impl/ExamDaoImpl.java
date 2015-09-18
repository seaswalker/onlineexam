package exam.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import exam.dao.ExamDao;
import exam.dao.base.BaseDaoImpl;
import exam.dao.base.GenerateKeyCallback;
import exam.model.Clazz;
import exam.model.Exam;
import exam.model.Question;
import exam.model.role.Teacher;
import exam.util.DataUtil;

@Repository("examDao")
public class ExamDaoImpl extends BaseDaoImpl<Exam> implements ExamDao {
	
	private static String sql = "select * from exam";
	private static String countSql = "select count(id) from exam";
	private static RowMapper<Exam> rowMapper;
	
	static {
		rowMapper = new RowMapper<Exam>() {
			public Exam mapRow(ResultSet rs, int rowNum) throws SQLException {
				Exam exam = new Exam();
				exam.setId(rs.getInt("id"));
				exam.setTitle(rs.getString("title"));
				exam.setStatus(rs.getBoolean("status"));
				exam.setEndTime(rs.getTimestamp("endtime"));
				exam.setJudgePoints(rs.getInt("judgepoints"));
				exam.setLimit(rs.getInt("limit"));
				exam.setMultiPoints(rs.getInt("multipoints"));
				exam.setPoints(rs.getInt("points"));
				exam.setSinglePoints(rs.getInt("singlepoints"));
				return exam;
			}
		};
	}

    /**
     * 由于此方法在一个事物之内，由此导致了外键的问题:
     * 设置试卷和试题以及试卷和班级的关联关系时，由于事物的原因，此时试卷实际上尚未存入数据库，试卷的主键当然不存在，
     * 所以导致了两个关联关系无法建立，所以删除了外键。。。
     */
	@Override
	public void save(final Exam entity) {
		//首先保存exam,需要获取新插入的记录的主键
		int examId = saveExam(entity);
        List<Integer> questionIds = saveQuestions(entity);
		//设置试卷和班级的关联关系
		saveExamClassRelationships(entity.getClazzs(), examId);
		//设置题目和试卷的关联关系
        saveExamQuestionRelationships(questionIds, examId);
    }

    /**
     * 辅助保存试卷
     * @param entity 试卷
     * @return 试卷id
     */
    private int saveExam(Exam entity) {
        String sql = "insert into exam values(null,?,?,?,?,?,?,?,?,?)";
        return getKeyHelper(sql, (ps, param) -> {
            Exam exam = (Exam) param;
            ps.setString(1, exam.getTitle());
            ps.setInt(2, exam.getLimit());
            ps.setTimestamp(3, exam.isStatus() ? new Timestamp(exam.getEndTime().getTime()) : null);
            ps.setBoolean(4, exam.isStatus());
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
        sql = "insert into question values(null,?,?,?,?,?,?,?,?,?)";
        QuestionGenerateKeyCallback questionGenerateKeyCallback = new QuestionGenerateKeyCallback();
        for (final Question question : entity.getSingleQuestions()) {
            questionIds.add(getKeyHelper(sql, questionGenerateKeyCallback, question));
        }
        for (final Question question : entity.getMultiQuestions()) {
            questionIds.add(getKeyHelper(sql, questionGenerateKeyCallback, question));
        }
        for (final Question question : entity.getJudgeQuestions()) {
            questionIds.add(getKeyHelper(sql, questionGenerateKeyCallback, question));
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
        jdbcTemplate.update(sb.toString());
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
        sb.deleteCharAt(sb.length() - 1);
        jdbcTemplate.update(sb.toString());
    }
	
	/**
	 * 执行插入操作，并且返回此条记录的id
	 * @param sql 执行的sql语句
	 * @param callback 为PreparedStatement设置参数的回调函数
     * @param object 传递给回调函数的参数
	 * @return 生成的自增id
	 */
	private int getKeyHelper(final String sql, final GenerateKeyCallback callback, final Object object) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                callback.setParameters(ps, object);
                return ps;
            }
        }, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public static void main(String[] args) {
		String json = "{'singles':[{'title':'阿森纳是第几?','point':'10','optionA':'3','optionB':'2','optionC':'4','optionD':'1','answer':'1'}],'multis':[{'title':'阿森纳是第几?','point':'10','optionA':'3','optionB':'2','optionC':'4','optionD':'1','answer':'1,2'}],'judges':[{'title':'娜娜有输球了吗','point':'100','answer':'1'}],'setting':{'title':'试卷题目','timeLimit':'60','grade':'1','major':'1','clazz':'1','status':1,'runTime':'7'}}";
		Teacher teacher = new Teacher();
		teacher.setId("1111");
		new ExamDaoImpl().save(DataUtil.parseExam(json, teacher));
	}
	
	public String getCountSql() {
		return countSql;
	}

	public String getSql() {
		return sql;
	}

	public RowMapper<Exam> getRowMapper() {
		return rowMapper;
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
			ps.setInt(7, question.getType().type());
			ps.setString(8, question.getAnswer());
			ps.setString(9, question.getTeacher().getId());
		}
		
	}

}
