package exam.service.impl;

import javax.annotation.Resource;

import exam.util.DataUtil;
import exam.util.StringUtil;

import org.springframework.stereotype.Service;

import exam.dao.ManagerDao;
import exam.model.role.Manager;
import exam.service.ManagerService;

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
	
	@Override
	public void updatePassword(int id, String password) {
		String sql = "update manager set password = ?, modified = 1 where id = ?";
		managerDao.executeSql(sql, new Object[] {StringUtil.md5(password), id});
	}
	
}
