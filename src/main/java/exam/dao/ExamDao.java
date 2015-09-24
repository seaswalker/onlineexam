package exam.dao;

import exam.dao.base.BaseDao;
import exam.model.Exam;

public interface ExamDao extends BaseDao<Exam> {

	/**
	 * 批量更新，其实直使用存储过程貌似是一个better的方式...
	 * @param sqls sql数组
	 * @return 我也不知道是什么，看文档去...
	 */
	public int[] batchUpdate(String...sqls);
	
}
