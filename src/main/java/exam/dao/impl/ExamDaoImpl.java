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
	
	@Override
	public void save(final Exam entity) {
		//首先保存exam,需要获取新插入的记录的主键
		String sql = "intsert into exam values(null,?,?,?,?,?,?,?,?,?)";
		int examId = getKeyHelper(sql, new GenerateKeyCallback() {
			public void setParameters(PreparedStatement ps, Exam param) throws SQLException {
				ps.setString(1, entity.getTitle());
				ps.setInt(2, entity.getLimit());
				ps.setTimestamp(3, new Timestamp(entity.getEndTime().getTime()));
				ps.setBoolean(4, entity.isStatus());
				ps.setInt(5, entity.getPoints());
				ps.setInt(6, entity.getSinglePoints());
				ps.setInt(7, entity.getMultiPoints());
				ps.setInt(8, entity.getJudgePoints());
				ps.setString(9, entity.getTeacher().getId());
			}

			public <T> void setParameters(PreparedStatement ps, T param)
					throws SQLException {
				// TODO Auto-generated method stub
				
			}
		});
		//保存所有新插入的问题的id
		List<Integer> questionIds = new ArrayList<Integer>();
		//保存题目，同时返回题目的id
		sql = "intsert into question values(null,?,?,?,?,?,?,?,?,?)";
		for (final Question question : entity.getSingleQuestions()) {
			questionIds.add(getKeyHelper(sql, new GenerateKeyCallback() {
				public void setParameters(PreparedStatement ps) throws SQLException {
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
			}));
		}
		for (Question question : entity.getMultiQuestions()) {
		}
		for (Question question : entity.getJudgeQuestions()) {
		}
		//jdbcTemplate.update(sb.toString());
		//设置试卷和班级的关联关系
		//sb = new StringBuilder("insert into exam_class values");
		//设置题目和试卷的关联关系
		//String sql = "insert into exam_question values";
	}
	
	/**
	 * 执行插入操作，并且返回此条记录的id
	 * @param sql 执行的sql语句
	 * @param callback 为PreparedStatement设置参数的回调函数
	 * @return 生成的自增id
	 */
	private int getKeyHelper(final String sql, final GenerateKeyCallback callback) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				callback.setParameters(ps, null);
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
			
		}
		
	}

}
