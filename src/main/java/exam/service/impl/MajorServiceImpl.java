package exam.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.base.BaseDao;
import exam.model.Major;
import exam.service.MajorService;
import exam.service.base.BaseServiceImpl;

@Service("majorService")
public class MajorServiceImpl extends BaseServiceImpl<Major> implements MajorService {

	@Resource(name = "majorDao")
	@Override
	protected void setBaseDao(BaseDao<Major> baseDao) {
		super.baseDao = baseDao;
	}

}
