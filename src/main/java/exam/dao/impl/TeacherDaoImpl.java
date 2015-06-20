package exam.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import exam.dao.TeacherDao;
import exam.dao.base.BaseDaoImpl;
import exam.model.role.Teacher;
import exam.util.DataUtil;

@Repository("teacherDao")
public class TeacherDaoImpl extends BaseDaoImpl<Teacher> implements TeacherDao {
	
	private static RowMapper<Teacher> rowMapper;
	private static String sql = "select * from teacher";
	
	static {
		rowMapper = new RowMapper<Teacher>() {
			public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
				Teacher teacher = new Teacher();
				teacher.setId(rs.getString("id"));
				teacher.setName(rs.getString("name"));
				teacher.setPassword(rs.getString("password"));
				return teacher;
			}
		};
	}

	@Override
	public void save(Teacher entity) {
		jdbcTemplate.update("insert into teacher values(?, ?, ?)", new Object[] {entity.getId(), entity.getName(), entity.getPassword()});
	}
	
	@Override
	public void delete(Object id) {
		jdbcTemplate.update("delete from teacher where id = " + id);
	}
	
	@Override
	public Teacher getById(Object id) {
		return jdbcTemplate.queryForObject("select * from teacher where id = '" + id + "'", Teacher.class);
	}
	
	@Override
	public List<Teacher> getAll() {
		return find(null);
	}
	
	@Override
	public List<Teacher> find(Teacher entity) {
		StringBuilder sqlBuilder = new StringBuilder(sql).append(" where 1 = 1");
		if(entity != null) {
			if(DataUtil.isValid(entity.getId())) {
				sqlBuilder.append(" and id = '").append(entity.getId()).append("'");
			}
			if(DataUtil.isValid(entity.getName())) {
				sqlBuilder.append(" and name = '").append(entity.getName()).append("'");
			}
			if(DataUtil.isValid(entity.getPassword())) {
				sqlBuilder.append(" and password = '").append(entity.getPassword()).append("'");
			}
		}
		return jdbcTemplate.query(sqlBuilder.toString(), rowMapper);
	}
	
	public RowMapper<Teacher> getRowMapper() {
		return rowMapper;
	}

	public String getSql() {
		return sql;
	}

	public String getCountSql() {
		return "select count(id) from teacher";
	}
	
}
