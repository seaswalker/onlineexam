package exam.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	
	public Major findByName(String name) {
		Major major = new Major();
		major.setName(name);
		List<Major> majors = majorDao.find(major);
		return DataUtil.isValid(majors) ? majors.get(0) : null;
	}
	
	public List<Major> findAll() {
		return majorDao.getAll();
	}
	
	@Override
	public void batchDelete(String ids) {
		String sql = "delete from major where id in (" + ids + ")";
		majorDao.executeSql(sql);
	}
	
	public List<Major> findByGrade(int grade) {
		String sql = "select * from major where id in "
				+ "(select mid from class where gid = " + grade + ")";
		return majorDao.queryBySQL(sql);
	}
	
	public void update(int id, String name) {
		String sql = "update major set name = ? where id = ?";
		majorDao.update(sql, new Object[] {name, id});
	}
	
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse("1990-04-15");
		System.out.println(sdf2.format(date));
	}

}
