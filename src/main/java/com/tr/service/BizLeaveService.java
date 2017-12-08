package com.tr.service;

import java.util.List;
import java.util.Map;

import com.tr.entity.BizLeave;

/**
 * 请假业务接口
 *
 */
public interface BizLeaveService {
	
	// 新增请假
	public void addNewLeave(BizLeave leave);
	
	// 分页查询
	public List<BizLeave> getLeaveList(Map<String, Object> conditions,int pageNo,int pageSize);
	
	// 获得总记录数
	public Long getTotal(Map<String, Object> conditions);
	
	// 根据主键获得请假对象
	public BizLeave getLeave(Long id);
	
	// 修改请假
	public void updateLeave(BizLeave leave);
	
}
