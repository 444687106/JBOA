package com.tr.service.impl;

import java.util.List;

import com.tr.dao.Dao;
import com.tr.entity.SysEmployee;
import com.tr.entity.SysPosition;
import com.tr.service.SysEmployeeService;

/**
 * 员工业务实现类
 */
public class SysEmployeeServiceImpl implements SysEmployeeService {
	
	// dao 接口
	private Dao dao;

	@Override
	public SysEmployee login(SysEmployee sysEmployee) {
		String hql = "from SysEmployee where sn = :sn and password = :password";
		List<SysEmployee> empList = dao.find(hql.toString(), sysEmployee);
		if (empList.size() > 0) {
			return empList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public SysEmployee getEmployeeBySn(String sn) {
		return (SysEmployee) dao.get(SysEmployee.class,sn);
	}

	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	@Override
	public SysEmployee getEmployeeBySn(SysPosition sysPosition) {
		String hql = "from SysEmployee where sysPosition.nameCn = :sysPosition.nameCn";
		List<SysEmployee> empList = dao.find(hql, sysPosition);
		if (empList.size() > 0 )
			return empList.get(0);
		else
			return null;
	}

}
