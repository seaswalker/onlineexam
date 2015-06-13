package exam.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.ManagerDao;
import exam.model.role.Manager;
import exam.service.ManagerService;

@Service("managerService")
public class ManagerServiceImpl implements ManagerService {
	
	@Resource
	private ManagerDao managerDao;

	public Manager login(String name, String password) {
		return managerDao.login(name, password);
	}
	
}
