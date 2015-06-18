package exam.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.ClazzDao;
import exam.dao.base.BaseDao;
import exam.model.Clazz;
import exam.model.Grade;
import exam.model.Major;
import exam.service.ClazzService;
import exam.service.base.BaseServiceImpl;

@Service("clazzService")
public class ClazzServiceImpl extends BaseServiceImpl<Clazz> implements ClazzService {
	
	private ClazzDao clazzDao;
	
	@Resource(name = "clazzDao")
	@Override
	protected void setBaseDao(BaseDao<Clazz> baseDao) {
		super.baseDao = baseDao;
		this.clazzDao = (ClazzDao) baseDao;
	}

	public List<Clazz> findByMajor(int majorId) {
		Clazz clazz = new Clazz();
		clazz.setMajor(new Major(majorId));
		return clazzDao.find(clazz);
	}

	public List<Clazz> findByGrade(int grade) {
		Clazz clazz = new Clazz();
		clazz.setGrade(new Grade(grade));
		return clazzDao.find(clazz);
	}

	public List<Clazz> findByCNO(int cno) {
		Clazz clazz = new Clazz();
		clazz.setCno(cno);
		return clazzDao.find(clazz);
	}
	
	public List<Clazz> findClazzOnly(Clazz clazz) {
		return clazzDao.findClazzOnly(clazz);
	}

	public boolean isExist(int grade, int major, int cno) {
		StringBuilder sqlBuilder = new StringBuilder("select count(id) from class where gid = ");
		sqlBuilder.append(grade).append(" and mid = ").append(major).append(" and cno = ").append(cno);
		BigInteger result = (BigInteger) clazzDao.queryForObject(sqlBuilder.toString(), BigInteger.class);
		return result.intValue() > 0;
	}

}
