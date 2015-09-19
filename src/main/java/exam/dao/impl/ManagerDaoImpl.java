package exam.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import exam.dao.base.BaseDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import exam.dao.ManagerDao;
import exam.model.role.Manager;
import exam.util.DataUtil;

@Repository("managerDao")
public class ManagerDaoImpl extends BaseDaoImpl<Manager> implements ManagerDao {

    private static RowMapper<Manager> rowMapper;

    {
        rowMapper = new RowMapper<Manager>() {
            @Override
            public Manager mapRow(ResultSet rs, int rowNum) throws SQLException {
                Manager manager = new Manager();
                manager.setId(rs.getInt("id"));
                manager.setName(rs.getString("name"));
                manager.setPassword(rs.getString("password"));
                return manager;
            }
        };
    }

    @Override
    public String getCountSql() {
        return "select count(id) from manager";
    }

    @Override
    public String getSql() {
        return "select * from manager";
    }

    @Override
    public RowMapper<Manager> getRowMapper() {
        return rowMapper;
    }
}
