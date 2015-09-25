package exam.dao;

import java.util.List;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;

import exam.dao.base.BaseDao;
import exam.model.Exam;

public interface ExamDao extends BaseDao<Exam> {

	/**
	 * 调用存储过程查询
	 * @param creator 为存储过程设置参数
	 * @param callback 从返回的ResultSet设置java对象
	 * @return 试卷列表
	 */
	public List<Exam> execute(CallableStatementCreator creator, CallableStatementCallback<List<Exam>> callback);
	
}
