package com.tr.service;

import com.tr.entity.SysEmployee;
import com.tr.entity.SysPosition;

/**
 * Ա��ҵ��ӿ�
 *
 */
public interface SysEmployeeService {
	
	// ��½
	public SysEmployee login(SysEmployee sysEmployee);
	
	// ����Ա����Ż�ò���������Ա��
	public SysEmployee getEmployeeBySn(String sn);
	
	// ����ְ����Ա��
	public SysEmployee getEmployeeBySn(SysPosition sysPosition);
}
