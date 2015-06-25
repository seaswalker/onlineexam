package exam.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.TeacherDao;
import exam.dao.base.BaseDao;
import exam.model.role.Teacher;
import exam.service.TeacherService;
import exam.service.base.BaseServiceImpl;
import exam.util.DataUtil;
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
	
	public void updateTeachClazzs(String ids, String tid) {
		//首先删除记录
		String sql = "delete from teacher_class where tid = '" + tid + "'";
		teacherDao.executeSql(sql);
		//批量插入
		StringBuilder sqlBuilder = new StringBuilder("insert into teacher_class values");
		String[] notes = ids.split(",");
		for(String note : notes) {
			sqlBuilder.append("(null, '").append(tid).append("', '").append(note).append("'),");
		}
		sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
		teacherDao.executeSql(sqlBuilder.toString());
	}
	
	public Teacher login(String name, String password) {
		String sql = "select * from teacher where name = ? and password = ?";
		List<Teacher> result = teacherDao.queryBySQL(sql, name, StringUtil.md5(password));
		return DataUtil.isValid(result) ? result.get(0) : null;
	}
	
}
