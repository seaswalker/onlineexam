package exam.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import exam.dao.StudentDao;
import exam.dao.base.BaseDaoImpl;
import exam.model.Clazz;
import exam.model.Grade;
import exam.model.Major;
import exam.model.role.Student;
import exam.util.DataUtil;

@Repository("studentDao")
public class StudentDaoImpl extends BaseDaoImpl<Student> implements StudentDao {
	
	private static RowMapper<Student> rowMapper;
	private static String sql = "select s.id as s_id, s.name as s_name, s.password as s_password, "
			+ "c.id as c_id, c.cno as c_cno, g.id as g_id, g.grade as g_grade, m.id as m_id, m.name as m_name from student s join class c on c.id = s.cid"
			+ " join grade g on g.id = c.gid join major m on m.id = c.mid";
	
	static {
		rowMapper = new RowMapper<Student>() {
			public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
				Student student = new Student();
				student.setId(rs.getString("s_id"));
				student.setName(rs.getString("s_name"));
				student.setPassword(rs.getString("s_password"));
				Clazz clazz = new Clazz();
				clazz.setId(rs.getInt("c_id"));
				clazz.setCno(rs.getInt("c_cno"));
				Major major = new Major();
				major.setId(rs.getInt("m_id"));
				major.setName(rs.getString("m_name"));
				Grade grade = new Grade();
				grade.setId(rs.getInt("g_id"));
				grade.setGrade(rs.getInt("g_grade"));
				clazz.setMajor(major);
				clazz.setGrade(grade);
				student.setClazz(clazz);
				return student;
			}
		};
	}
	
	@Override
	public Student getById(Object id) {
		return jdbcTemplate.queryForObject("select * from student where id = ?", Student.class, new Object[] {id});
	}

	@Override
	public void save(Student entity) {
		jdbcTemplate.update("insert into student values(?, ?, ?, ?)",
				new Object[] {entity.getId(), entity.getName(), entity.getPassword(), entity.getClazz().getId()});
	}

	@Override
	public void delete(Object id) {
		jdbcTemplate.update("delete from student where id = " + id);
	}

	@Override
	public void update(String sql, Object[] params) {
		jdbcTemplate.update(sql, params);
	}
	
	@Override
	public List<Student> find(Student entity) {
		StringBuilder sqlBuilder = new StringBuilder(sql).append(" where 1 = 1");
		if(entity != null) {
			if(DataUtil.isValid(entity.getId())) {
				sqlBuilder.append(" and id = ").append(entity.getId());
			}
			if(DataUtil.isValid(entity.getName())) {
				sqlBuilder.append(" and name = ").append(entity.getName());
			}
			if(DataUtil.isValid(entity.getPassword())) {
				sqlBuilder.append(" and password = ").append(entity.getPassword());
			}
		}
		return jdbcTemplate.query(sqlBuilder.toString(), rowMapper);
	}
	
	@Override
	public Object queryForObject(String sql, Class<?> clazz) {
		return jdbcTemplate.queryForObject(sql, clazz);
	}
	
	@Override
	public List<Student> getAll() {
		return find(null);
	}

	@Override
	protected RowMapper<Student> getRowMapper() {
		return rowMapper;
	}

	@Override
	protected String getSql() {
		return sql;
	}

	/**
	 * 这个地方必须手动制定一个别名s，否则controller中where条件没法用，这是个bug?
	 */
	@Override
	protected String getCountSql() {
		return "select count(id) from student s";
	}

}
