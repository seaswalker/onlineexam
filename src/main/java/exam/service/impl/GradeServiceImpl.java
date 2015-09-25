package exam.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.GradeDao;
import exam.dao.base.BaseDao;
import exam.model.Grade;
import exam.service.GradeService;
import exam.service.base.BaseServiceImpl;
import exam.util.DataUtil;

@Service("gradeService")
public class GradeServiceImpl extends BaseServiceImpl<Grade> implements GradeService {
	
	private GradeDao gradeDao;

	@Resource(name = "gradeDao")
	@Override
	protected void setBaseDao(BaseDao<Grade> baseDao) {
		super.baseDao = baseDao;
		this.gradeDao = (GradeDao) baseDao;
	}
	
	@Override
	public Grade findByGrade(String grade) {
		Grade _grade = new Grade();
		_grade.setGrade(Integer.parseInt(grade));
		List<Grade> grades = gradeDao.find(_grade);
		return DataUtil.isValid(grades) ? grades.get(0) : null;
	}
	
	@Override
	public List<Grade> findAll() {
		return gradeDao.find(null);
	}
	
	@Override
	public void saveOrUpdate(Grade entity) {
		if (entity.getId() <= 0) {
			gradeDao.executeSql("insert into grade values(null, " + entity.getGrade() + ")");
		}
	}
	
	@Override
	public void delete(Object id) {
		//需要删除的有年级 -> 班级 -> 学生 -> 学生的考试记录(examinationresult) -> 做过的试题记录(examinationresult_question)
		String[] sqls = {
			//删除做过的试题记录
			"delete from examinationresult_question where erid in (select er.id from examinationresult er where er.eid in (select ec.eid from exam_class ec where ec.cid in(select c.id from class c where c.gid = " + id + ")))",
			//删除所有的考试记录(examinationresult)
			"delete from examinationresult where eid in (select ec.eid from exam_class ec where ec.cid in(select c.id from class c where c.gid = " + id + "))",
			//删除学生
			"delete from student where cid in (select id from class where gid = " + id + ")",
			//删除班级和试卷的关联关系
			"delete from exam_class where cid in (select id from class where gid = " + id + ")",
			//删除教师和班级的关联关系
			"delete from teacher_class where cid in (select id from class where gid = " + id + ")",
			//删除班级
			"delete from class where gid = " + id,
			//删除年级
			"delete from grade where id = " + id
		};
		gradeDao.batchUpdate(sqls);
	}
	
}
