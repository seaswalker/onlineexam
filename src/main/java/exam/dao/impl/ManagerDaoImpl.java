package exam.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import exam.dao.ManagerDao;
import exam.model.role.Manager;
import exam.util.DataUtil;

@Repository("managerDao")
public class ManagerDaoImpl implements ManagerDao {
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	public Manager login(String name, String password) {
		String sql = "select * from manager where name = ? and password = ?";
		List<Manager> managers =  jdbcTemplate.query(sql, new Object[] {name, password},  new RowMapper<Manager>() {
			public Manager mapRow(ResultSet rs, int rowNum) throws SQLException {
				Manager manager = new Manager();
				manager.setId(rs.getInt("id"));
				manager.setName(rs.getString("name"));
				manager.setPassword(rs.getString("password"));
				return manager;
			}
		});
		return DataUtil.isValid(managers) ? managers.get(0) : null;
	}
}
