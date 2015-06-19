package exam.service.impl;

import java.math.BigInteger;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.TeacherDao;
import exam.dao.base.BaseDao;
import exam.model.role.Teacher;
import exam.service.TeacherService;
import exam.service.base.BaseServiceImpl;
import exam.util.StringUtil;

@Service("teacherService")
public class TeacherServiceImpl extends BaseServiceImpl<Teacher> implements TeacherService {
	
	private TeacherDao teacherDao;

	@Resource(name = "teacherDao")
	@Override
	protected void setBaseDao(BaseDao<Teacher> baseDao) {
		super.baseDao = baseDao;
		this.teacherDao = (TeacherDao) baseDao;
	}
	
	public void updateName(String id, String name) {
		String sql = "update teacher set name = ? where id = ?";
		teacherDao.update(sql, new Object[] {name, id});
	}

	public void updatePassword(String id, String password) {
		String sql = "update teacher set password = ? where id = ?";
		teacherDao.update(sql, new Object[] {StringUtil.md5(password), id});
	}
	
	public boolean isExist(String id) {
		String sql = "select count(id) from teacher where id = '" + id + "'";
		BigInteger result = (BigInteger) teacherDao.queryForObject(sql, BigInteger.class);
		return result.intValue() > 0;
	}
	
}
