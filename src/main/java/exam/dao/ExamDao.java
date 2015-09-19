package exam.dao;

import exam.dao.base.BaseDao;
import exam.dao.base.GenerateKeyCallback;
import exam.model.Exam;

public interface ExamDao extends BaseDao<Exam> {

    /**
     * 执行sql语句并且返回生成的主键id
     * @param sql
     * @param callback createPreparedStatement方法需要返回一个PreparedStatement，而PreparedStatement需要设置参数
     * @param param sql的?由param内的属性替换
     * @return 主键id
     */
    int getKeyHelper(final String sql, final GenerateKeyCallback callback, final Object param);

}
