package exam.service.base;

import java.util.HashMap;
import java.util.List;

import exam.dao.base.BaseDao;
import exam.model.page.PageBean;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

	protected BaseDao<T> baseDao;
	
	/**
	 * 设置具体的BaseDao
	 */
	protected abstract void setBaseDao(BaseDao<T> baseDao);
	
	public T getById(Object id) {
		return baseDao.getById(id);
	}
	
	public List<T> getAll() {
		return baseDao.getAll();
	}
	
	public void update(T entity) {
		baseDao.update(entity);
	}
	
	public void delete(Object id) {
		baseDao.delete(id);
	}
	
	public void save(T entity) {
		baseDao.save(entity);
	}
	
	public List<T> find(T entity) {
		return baseDao.find(entity);
	}
	
	public void batchDelete(String ids) {
		throw new UnsupportedOperationException();
	}
	
	public PageBean<T> pageSearch(int pageCode, int pageSize, int pageNumber,
			String where, List<Object> params, HashMap<String, String> orderbys) {
		return baseDao.pageSearch(pageCode, pageSize, pageNumber, where, params, orderbys);
	}
	
}
