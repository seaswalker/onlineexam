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
	
	@Override
	public void delete(Object id) {
		//需要删除: 班级、教师和班级的关联 -> 学生 -> 学生的考试记录(examinationresult) -> 做题记录(examinationresult_question)
		String[] sqls = {
			//删除做题记录
			"delete from examinationresult_question where erid in (select id from examinationresult where eid in (select eid from exam_class where cid = " + id + "))",
			//删除考试记录
			"delete from examinationresult where eid in (select eid from exam_class where cid = " + id + ")",
			//删除学生
			"delete from student where cid = " + id,
			//删除和教师的关联
			"delete from teacher_class where cid = " + id,
			//删除班级
			"delete from class where id = " + id
		};
		clazzDao.batchUpdate(sqls);
	}
	
	/**
	 * 班级没有修改功能，直接删了重新添加就好
	 */
	@Override
	public void saveOrUpdate(Clazz entity) {
		if (entity.getId() <= 0) {
			String sql = "insert into class values(null, ?, ?, ?)";
			clazzDao.executeSql(sql, new Object[] {entity.getCno(), entity.getGrade().getId(), entity.getMajor().getId()});
		}
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
	
	@Override
	public List<Clazz> findAll() {
		return clazzDao.find(null);
	}

    /**
     * 由于班级对象并没有试卷的信息，而是通过第三张表建立关联关系，所以没有借助find方法实现
     * @param examId 试卷id
     * @return 班级列表
     */
    @Override
    public List<Clazz> findByExam(Integer examId) {
        String sql = clazzDao.getSql() + " where c.id in (select cid from exam_class where eid = " + examId + ")";
        return clazzDao.queryBySQL(sql);
    }

    public List<Clazz> findClazzOnly(Clazz clazz) {
		return clazzDao.findClazzOnly(clazz);
	}
	
	public List<Clazz> findByTeacher(String tid) {
		StringBuilder sqlBuilder = new StringBuilder(clazzDao.getSql());
		sqlBuilder.append(" where c.id in (select cid from teacher_class where tid = '")
			.append(tid).append("')");
		return clazzDao.queryBySQL(sqlBuilder.toString());
	}

	public boolean isExist(int grade, int major, int cno) {
		StringBuilder sqlBuilder = new StringBuilder("select count(id) from class where gid = ");
		sqlBuilder.append(grade).append(" and mid = ").append(major).append(" and cno = ").append(cno);
		BigInteger result = (BigInteger) clazzDao.queryForObject(sqlBuilder.toString(), BigInteger.class);
		return result.intValue() > 0;
	}

    @Override
    public void resetExamClass(int examId, String clazzIds) {
        //首先删除此试卷的已有的关联关系
        String sql = "delete from exam_class where eid = " + examId;
        clazzDao.executeSql(sql);
        //重建
        StringBuilder sb = new StringBuilder("insert into exam_class values");
        for (String id : clazzIds.split(",")) {
            sb.append("(null,").append(examId).append(",").append(id).append("),");
        }
        clazzDao.executeSql(sb.deleteCharAt(sb.length() - 1).toString());
    }
}
