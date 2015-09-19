package exam.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import exam.model.ExamStatus;
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
				exam.setStatus(ExamStatus.valueOf(rs.getString("status")));
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
	 * 执行插入操作，并且返回此条记录的id
	 * @param sql 执行的sql语句
	 * @param callback 为PreparedStatement设置参数的回调函数
     * @param object 传递给回调函数的参数
	 * @return 生成的自增id
	 */
    @Override
	public int getKeyHelper(final String sql, final GenerateKeyCallback callback, final Object object) {
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

	public String getCountSql() {
		return countSql;
	}

	public String getSql() {
		return sql;
	}

	public RowMapper<Exam> getRowMapper() {
		return rowMapper;
	}
	
}
