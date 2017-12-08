package com.tr.service;

import java.util.List;

import com.tr.entity.SysPosition;


/**
 * 职务业务接口
 *
 */
public interface SysPositionService {
	
	// 获得所有职务
	public List<SysPosition> getAll();
}
