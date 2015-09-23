package exam.dao;

import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import exam.dao.base.BaseDao;
import exam.model.ExaminationResult;

public interface ExaminationResultDao extends BaseDao<ExaminationResult> {

	/**
	 * 此方法的参数可以高度定制，是为了在一次查询中在两个表中拿到
	 * singlePoints、multiPoints、judgePoints、point、time
	 * @param sql 要执行的查询语句
	 * @param resultSetExtractor 用来设置返回值
	 * @return
	 */
	public <T> T query(String sql, ResultSetExtractor<T> resultSetExtractor);
	
	/**
	 * 查出所有的ERViewQuestion
	 * @param sql
	 * @param rowMapper
	 * @return
	 */
	public <T> List<T> query(String sql, RowMapper<T> rowMapper);
	
}
