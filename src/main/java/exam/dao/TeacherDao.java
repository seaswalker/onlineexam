package exam.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import exam.dao.base.BaseDao;
import exam.model.role.Teacher;

public interface TeacherDao extends BaseDao<Teacher> {

	/**
	 * 随意查
	 * @param sql
	 * @param rowMapper
	 * @return
	 */
	public <T> List<T> query(String sql, RowMapper<T> rowMapper);
	
}
