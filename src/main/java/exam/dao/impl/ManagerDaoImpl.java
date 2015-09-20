package exam.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import exam.dao.base.BaseDaoImpl;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import exam.dao.ManagerDao;
import exam.model.role.Manager;

@Repository("managerDao")
public class ManagerDaoImpl extends BaseDaoImpl<Manager> implements ManagerDao {

    private static RowMapper<Manager> rowMapper;

    {
        rowMapper = new RowMapper<Manager>() {
            public Manager mapRow(ResultSet rs, int rowNum) throws SQLException {
                Manager manager = new Manager();
                manager.setId(rs.getInt("id"));
                manager.setName(rs.getString("name"));
                manager.setPassword(rs.getString("password"));
                return manager;
            }
        };
    }

    public String getCountSql() {
        return "select count(id) from manager";
    }

    public String getSql() {
        return "select * from manager";
    }

    public RowMapper<Manager> getRowMapper() {
        return rowMapper;
    }
}
