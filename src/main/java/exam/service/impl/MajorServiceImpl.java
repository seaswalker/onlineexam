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
	public List<Major> findByGrade(int grade) {
		String sql = "select * from major where id in "
				+ "(select mid from class where gid = " + grade + ")";
		return majorDao.queryBySQL(sql);
	}
	
	@Override
	public void saveOrUpdate(Major entity) {
		if (entity.getId() <= 0) {
			majorDao.executeSql("insert into major values(null, ?)", new Object[] {entity.getName()});
		} else {
			majorDao.executeSql("update major set name = ? where id = ?", new Object[] {entity.getName(), entity.getId()});
		}
	}

	@Override
	public void delete(Object id) {
		//删除专业需要: 删除专业 -> 班级、教师和班级的关联 -> 学生 -> 考试记录(examinationresult) -> 做的题目(examinationresult_question)
		String[] sqls = {
			//首先删除做过的题
			"delete from examinationresult_question where erid in (select id from examinationresult where eid in (select eid from exam_class where cid in (select id from class where mid = " + id + ")))",
			//删除考试记录
			"delete from examinationresult where eid in (select eid from exam_class where cid in (select cid from class where mid = " + id + "))",
			//删除学生
			"delete from student where cid in (select id from class where mid = " + id + ")",
			//删除班级和教师的关联关系
			"delete from teacher_class where cid in (select id from class where mid = " + id + ")",
			//删除班级
			"delete from class where mid = " + id,
			//删除专业
			"delete from major where id = " + id
		};
		majorDao.batchUpdate(sqls);
	}
	
}
