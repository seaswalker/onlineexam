package exam.dao.base;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface GenerateKeyCallback {

	/**
	 * 为PreparedStatement设置参数
	 * @param ps
	 * @param param 传递给此方法的执行环境(?)，其实可选
	 * @throws SQLException 
	 */
	public <T> void setParameters(PreparedStatement ps, T param) throws SQLException;
	
}
