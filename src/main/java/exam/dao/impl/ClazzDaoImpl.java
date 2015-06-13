package exam.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import exam.dao.ClazzDao;
import exam.dao.base.BaseDaoImpl;
import exam.model.Clazz;
import exam.model.Grade;
import exam.model.Major;

@Repository("clazzDao")
public class ClazzDaoImpl extends BaseDaoImpl<Clazz> implements ClazzDao {
	
	//字段匹配
	private static RowMapper<Clazz> rowMapper;
	//统一的查询语句，需要查出年级和专业
	private static String sql = "select c.id as c_id, c.cno as c_cno, g.id as g_id, g.grade as g_grade,m.id as m_id, m.name as m_name from class c join grade g on c.gid = g.id join major m on m.id = c.mid";
	
	static {
		rowMapper = new RowMapper<Clazz>() {
			public Clazz mapRow(ResultSet rs, int rowNum) throws SQLException {
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
				return clazz;
			}
		};
	}

	@Override
	public void save(Clazz entity) {
		String sql = "insert into class values(null, ?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] {entity.getCno(), entity.getGrade().getId(), entity.getMajor().getId()});
	}
	
	@Override
	public void update(Clazz entity) {
		String sql = "update class set cno = ?, gid = ?, mid = ? where id = ?";
		jdbcTemplate.update(sql, 
				new Object[] {entity.getCno(), entity.getGrade().getId(), entity.getMajor().getId(), entity.getId()});
	}
	
	@Override
	public void delete(Object id) {
		jdbcTemplate.update("delete from class where id = " + id);
	}
	
	@Override
	public Clazz getById(Object id) {
		return jdbcTemplate.queryForObject("select * from class where id = " + id, Clazz.class);
	}
	
	@Override
	public List<Clazz> getAll() {
		return find(null);
	}
	
	@Override
	public List<Clazz> find(Clazz entity) {
		StringBuilder sqlBuilder = new StringBuilder(sql).append(" where 1 = 1");
		if(entity != null) {
			if(entity.getId() > 0) {
				sqlBuilder.append(" and c.id = ").append(entity.getId());
			}
			if(entity.getCno() > 0) {
				sqlBuilder.append(" and c.cno = ").append(entity.getCno());
			}
			if(entity.getGrade() != null) {
				sqlBuilder.append(" and c.gid = ").append(entity.getGrade().getId());
			}
			if(entity.getMajor() != null) {
				sqlBuilder.append(" and c.mid = ").append(entity.getMajor().getId());
			}
		}
		return jdbcTemplate.query(sqlBuilder.toString(), rowMapper);
	}

	@Override
	protected RowMapper<Clazz> getRowMapper() {
		return rowMapper;
	}

	@Override
	protected String getSql() {
		return sql;
	}

	@Override
	protected String getCountSql() {
		return "select count(*) from class";
	}
	
}
