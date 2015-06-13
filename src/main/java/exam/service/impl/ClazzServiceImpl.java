package exam.service.impl;

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

	@Resource(name = "clazzDao")
	@Override
	protected void setBaseDao(BaseDao<Clazz> baseDao) {
		super.baseDao = baseDao;
		this.clazzDao = (ClazzDao) baseDao;
	}

}
