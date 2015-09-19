package exam.dao;

import java.util.List;

import exam.dao.base.BaseDao;
import exam.model.Clazz;

public interface ClazzDao extends BaseDao<Clazz> {

	/**
	 * 不查出关联的年级和专业
	 */
	List<Clazz> findClazzOnly(Clazz clazz);

}
