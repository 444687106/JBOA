package com.tr.service.impl;

import java.util.List;

import com.tr.dao.Dao;
import com.tr.entity.SysPosition;
import com.tr.service.SysPositionService;

/**
 * 职务业务实现类
 */
public class SysPositionServiceImpl implements SysPositionService {
	
	// dao 接口
	private Dao dao;

	@Override
	public List<SysPosition> getAll() {
		return dao.find("from SysPosition");
	}

	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

}
