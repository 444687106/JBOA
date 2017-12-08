package com.tr.service;

import com.tr.entity.SysEmployee;
import com.tr.entity.SysPosition;

/**
 * 员工业务接口
 *
 */
public interface SysEmployeeService {
	
	// 登陆
	public SysEmployee login(SysEmployee sysEmployee);
	
	// 根据员工编号获得部门下所有员工
	public SysEmployee getEmployeeBySn(String sn);
	
	// 根据职务获得员工
	public SysEmployee getEmployeeBySn(SysPosition sysPosition);
}
