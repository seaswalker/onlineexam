package exam.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.MajorDao;
import exam.dao.base.BaseDao;
import exam.model.Major;
import exam.service.MajorService;
import exam.service.base.BaseServiceImpl;
import exam.util.DataUtil;

@Service("majorService")
public class MajorServiceImpl extends BaseServiceImpl<Major> implements MajorService {
	
	private MajorDao majorDao;

	@Resource(name = "majorDao")
	@Override
	protected void setBaseDao(BaseDao<Major> baseDao) {
		super.baseDao = baseDao;
		this.majorDao = (MajorDao) baseDao;
	}
	
	@Override
	public Major findByName(String name) {
		Major major = new Major();
		major.setName(name);
		List<Major> majors = majorDao.find(major);
		return DataUtil.isValid(majors) ? majors.get(0) : null;
	}
	
	@Override
	public List<Major> findAll() {
		return majorDao.find(null);
	}
	
	@Override
	public void batchDelete(String ids) {
		String sql = "delete from major where id in (" + ids + ")";
		majorDao.executeSql(sql);
	}
	
	@Override
	public List<Major> findByGrade(int grade) {
		String sql = "select * from major where id in "
				+ "(select mid from class where gid = " + grade + ")";
		return majorDao.queryBySQL(sql);
	}
	
	@Override
	public void update(int id, String name) {
		String sql = "executeSql major set name = ? where id = ?";
		majorDao.executeSql(sql, new Object[]{name, id});
	}
	
	@Override
	public void saveOrUpdate(Major entity) {
		if (entity.getId() <= 0) {
			majorDao.executeSql("insert into major values(null, ?)", new Object[] {entity.getName()});
		}
	}

	@Override
	public void delete(Object id) {
		majorDao.executeSql("delete from major where id = " + id);
	}
}
