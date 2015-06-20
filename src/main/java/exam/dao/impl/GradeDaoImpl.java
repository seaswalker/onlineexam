package exam.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import exam.dao.GradeDao;
import exam.dao.base.BaseDaoImpl;
import exam.model.Grade;

@Repository("gradeDao")
public class GradeDaoImpl extends BaseDaoImpl<Grade> implements GradeDao {
	
	private static RowMapper<Grade> rowMapper;
	private static String sql = "select * from grade";
	
	static {
		rowMapper = new RowMapper<Grade>() {
			public Grade mapRow(ResultSet rs, int rowNum) throws SQLException {
				Grade grade = new Grade();
				grade.setId(rs.getInt("id"));
				grade.setGrade(rs.getInt("grade"));
				return grade;
			}
		};
	}
	
	@Override
	public Grade getById(Object id) {
		return jdbcTemplate.queryForObject("select * from grade where id = " + id, Grade.class);
	}

	@Override
	public void save(Grade entity) {
		jdbcTemplate.update("insert into grade values(null, ?)",
				entity.getGrade());
	}

	@Override
	public void delete(Object id) {
		jdbcTemplate.update("delete from grade where id = " + id);
	}

	@Override
	public List<Grade> find(Grade entity) {
		StringBuilder sqlBuilder = new StringBuilder(sql).append(" where 1 = 1");
		if(entity != null) {
			if(entity.getId() > 0) {
				sqlBuilder.append(" and id = ").append(entity.getId());
			}
			if(entity.getGrade() > 0) {
				sqlBuilder.append(" and grade = ").append(entity.getGrade());
			}
		}
		return jdbcTemplate.query(sqlBuilder.toString(), rowMapper);
	}

	public List<Grade> getAll() {
		return jdbcTemplate.query(sql, rowMapper);
	}

	public RowMapper<Grade> getRowMapper() {
		return rowMapper;
	}

	public String getSql() {
		return sql;
	}

	public String getCountSql() {
		return "select count(id) from grade";
	}

}
