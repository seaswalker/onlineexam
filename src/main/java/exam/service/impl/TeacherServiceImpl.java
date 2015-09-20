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
	
	@Override
	public void updateName(String id, String name) {
		String sql = "executeSql teacher set name = ? where id = ?";
		teacherDao.executeSql(sql, new Object[]{name, id});
	}

	@Override
	public void updatePassword(String id, String password) {
		String sql = "executeSql teacher set password = ? where id = ?";
		teacherDao.executeSql(sql, new Object[]{StringUtil.md5(password), id});
	}
	
	@Override
	public boolean isExist(String id) {
		String sql = "select count(id) from teacher where id = '" + id + "'";
		BigInteger result = (BigInteger) teacherDao.queryForObject(sql, BigInteger.class);
		return result.intValue() > 0;
	}
	
	@Override
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
	
	@Override
	public Teacher login(String name, String password) {
		String sql = "select * from teacher where name = ? and password = ?";
		List<Teacher> result = teacherDao.queryBySQL(sql, name, StringUtil.md5(password));
		return DataUtil.isValid(result) ? result.get(0) : null;
	}

    @Override
    public void modifyPassword(String id, String newPassword) {
        String sql = "update teacher set password = '" + StringUtil.md5(newPassword) + "' where id = '"
                + id + "'";
        teacherDao.executeSql(sql);
    }
    
    @Override
	public void save(Teacher entity) {
    	teacherDao.executeSql("insert into teacher values(?, ?, ?)", new Object[] {entity.getId(), entity.getName(), entity.getPassword()});
	}
	
	@Override
	public void delete(Object id) {
		teacherDao.executeSql("delete from teacher where id = " + id);
	}
}
