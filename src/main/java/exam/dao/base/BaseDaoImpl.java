package exam.dao.base;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import exam.model.page.PageBean;
import exam.util.DataUtil;

/**
 * 注入JdbcTemplate对象
 * 提供方法的空实现
 * @author skywalker
 *
 */
public abstract class BaseDaoImpl<T> implements BaseDao<T> {
	
	@Resource
	protected JdbcTemplate jdbcTemplate;
	//rowMapper，由子类提供
	protected RowMapper<T> rowMapper = getRowMapper();
	//查询sql语句
	protected String sql = getSql();
	//数量查询语句
	protected String countSql = getCountSql();
	
	public List<T> find(T entity) {
		throw new UnsupportedOperationException();
	}

    /**
     * 此方法使用update实现，因为从源码得知，execute()一般用于DDL，而不是DML
     */
	public void executeSql(String sql) {
		jdbcTemplate.update(sql);
	}

    public void executeSql(String sql, Object[] params) {
        jdbcTemplate.update(sql, params);
    }

	public List<T> queryBySQL(String sql, Object... params) {
		return jdbcTemplate.query(sql, params, rowMapper);
	}

	public List<T> queryBySQL(String sql) {
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	public Object queryForObject(String sql, Class<?> clazz) {
		return jdbcTemplate.queryForObject(sql, clazz);
	}
	
	@Override
	public int[] batchUpdate(String... sqls) {
		return jdbcTemplate.batchUpdate(sqls);
	}
	
	/**
	 * 执行插入操作，并且返回此条记录的id
	 * @param sql 执行的sql语句
	 * @param callback 为PreparedStatement设置参数的回调函数
     * @param object 传递给回调函数的参数
	 * @return 生成的自增id
	 */
	@Override
	public int getKeyHelper(final String sql, final GenerateKeyCallback callback, final Object object) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                callback.setParameters(ps, object);
                return ps;
            }
        }, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public PageBean<T> pageSearch(int pageCode, int pageSize, int pageNumber,
		String where, List<Object> params, HashMap<String, String> orderbys) {
		
		String whereSql = (DataUtil.isValid(where)) ? where : "";
		StringBuilder sqlBuilder = new StringBuilder(sql).append(" ").append(whereSql);
		StringBuilder countSqlBuilder = new StringBuilder(countSql).append(" ").append(whereSql);
		PageBean<T> pageBean = null;
		if(DataUtil.isValid(orderbys)) {
			//设置排序
			sqlBuilder.append(" order by ");
			for(String key : orderbys.keySet()) {
				sqlBuilder.append(key).append(" ").append(orderbys.get(key)).append(",");
			}
			sqlBuilder.deleteCharAt(sql.length() - 1);
		}
		//设置分页
		int begin = (pageCode - 1) * pageSize;
		sqlBuilder.append(" limit ").append(begin).append(", ").append(pageSize);
		//设置参数
		if(DataUtil.isValid(params)) {
			Object[] paramsArray = params.toArray();
			pageBean = new PageBean<T>(jdbcTemplate.query(sqlBuilder.toString(), paramsArray, rowMapper), pageSize, pageCode,
					((BigInteger) jdbcTemplate.queryForObject(countSqlBuilder.toString(), paramsArray, BigInteger.class)).intValue(), pageNumber);
		}else {
			pageBean = new PageBean<T>(jdbcTemplate.query(sqlBuilder.toString(), rowMapper), pageSize, pageCode,
					((BigInteger) jdbcTemplate.queryForObject(countSqlBuilder.toString(), BigInteger.class)).intValue(), pageNumber);
		}
		return pageBean;
	}

}
