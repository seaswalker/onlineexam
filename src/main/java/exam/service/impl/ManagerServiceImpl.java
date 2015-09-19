package exam.service.impl;

import javax.annotation.Resource;

import exam.util.DataUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import exam.dao.ManagerDao;
import exam.model.role.Manager;
import exam.service.ManagerService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service("managerService")
public class ManagerServiceImpl implements ManagerService {
	
	@Resource
	private ManagerDao managerDao;

	public Manager login(String name, String password) {
        String sql = "select * from manager where name = ? and password = ?";
        List<Manager> managers = managerDao.queryBySQL(sql, new Object[] {name, password});
        return DataUtil.isValid(managers) ? managers.get(0) : null;
	}
	
}
