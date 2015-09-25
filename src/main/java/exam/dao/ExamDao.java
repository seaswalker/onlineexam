package exam.dao;

import java.util.List;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;

import exam.dao.base.BaseDao;
import exam.model.Exam;

public interface ExamDao extends BaseDao<Exam> {

	/**
	 * 批量更新，其实直使用存储过程貌似是一个better的方式...
	 * @param sqls sql数组
	 * @return 我也不知道是什么，看文档去...
	 */
	public int[] batchUpdate(String...sqls);
	
	/**
	 * 调用存储过程查询
	 * @param creator 为存储过程设置参数
	 * @param callback 从返回的ResultSet设置java对象
	 * @return 试卷列表
	 */
	public List<Exam> execute(CallableStatementCreator creator, CallableStatementCallback<List<Exam>> callback);
	
}
