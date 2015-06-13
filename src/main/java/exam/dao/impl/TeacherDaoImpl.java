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
				teacher.setId(rs.getInt("id"));
				teacher.setName(rs.getString("name"));
				teacher.setPassword(rs.getString("password"));
				return teacher;
			}
		};
	}

	@Override
	public void save(Teacher entity) {
		jdbcTemplate.update("insert into teacher values(null, ?, ?)", new Object[] {entity.getName(), entity.getPassword()});
	}
	
	@Override
	public void update(Teacher entity) {
		jdbcTemplate.update("update teacher set name = ?, password = ? where id = ?", 
				new Object[] {entity.getName(), entity.getPassword(), entity.getId()});
	}
	
	@Override
	public void delete(Object id) {
		jdbcTemplate.update("delete from teacher where id = " + id);
	}
	
	@Override
	public Teacher getById(Object id) {
		return jdbcTemplate.queryForObject("select * from teacher where id = " + id, Teacher.class);
	}
	
	@Override
	public List<Teacher> getAll() {
		return find(null);
	}
	
	@Override
	public List<Teacher> find(Teacher entity) {
		StringBuilder sqlBuilder = new StringBuilder(sql).append(" where 1 = 1");
		if(entity != null) {
			if(entity.getId() > 0) {
				sqlBuilder.append(" and id = ").append(entity.getId());
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

	@Override
	protected RowMapper<Teacher> getRowMapper() {
		return rowMapper;
	}

	@Override
	protected String getSql() {
		return sql;
	}

	@Override
	protected String getCountSql() {
		return "select count(id) from teacher";
	}
	
}
