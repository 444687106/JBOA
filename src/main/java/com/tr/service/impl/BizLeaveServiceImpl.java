package com.tr.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tr.dao.Dao;
import com.tr.entity.BizLeave;
import com.tr.service.BizLeaveService;

/**
 * 请假业务实现类
 * 
 */
public class BizLeaveServiceImpl implements BizLeaveService {

	// dao 接口
	private Dao dao;

	@Override
	public void addNewLeave(BizLeave leave) {
		dao.save(leave);
	}

	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	@Override
	public List<BizLeave> getLeaveList(Map<String, Object> conditions,
			int pageNo, int pageSize) {
		List<BizLeave> leaveList = new ArrayList<BizLeave>();
		StringBuffer buffer = new StringBuffer("from BizLeave where 1=1");
		if (conditions.get("position") == null) {
			return null;
		} else {
			if (conditions.get("position").equals("员工") || conditions.get("position").equals("财务")) {
				buffer.append(" and sysEmployeeByEmployeeSn.sn = :empNo");
			} else {
				if (conditions.get("position").equals("部门经理")) {
					buffer.append(" and sysEmployeeByNextDealSn.sn = :empNo");
					buffer.append(" and sysEmployeeByEmployeeSn.sysDepartment.id = :deptNo ");
				} else {
					buffer.append(" and sysEmployeeByNextDealSn.sn = :empNo");
				}
			}
			if (conditions.get("type") != null && !conditions.get("type").toString().equals("全部")) {
				buffer.append(" and leavetype  = :type");
			}
			if (conditions.get("startDate") != null) {
				buffer.append(" and createtime >= :startDate");
			}
			if (conditions.get("endDate") != null) {
				buffer.append(" and createtime <= :endDate");
			}
			buffer.append(" order by createtime desc");
		}
		leaveList = dao.find(buffer.toString(), conditions, pageNo, pageSize);
		return leaveList;
	}

	@Override
	public Long getTotal(Map<String, Object> conditions) {
		StringBuffer buffer = new StringBuffer("select count(id) from BizLeave where 1=1");
		if (conditions.get("position") == null) {
			return null;
		} else {
			if (conditions.get("position").equals("员工") || conditions.get("position").equals("财务")) {
				buffer.append(" and sysEmployeeByEmployeeSn.sn = :empNo");
			} else {
				if (conditions.get("position").equals("部门经理")) {
					buffer.append(" and sysEmployeeByNextDealSn.sn = :empNo");
					buffer.append(" and sysEmployeeByEmployeeSn.sysDepartment.id = :deptNo ");
				} else {
					buffer.append(" and sysEmployeeByNextDealSn.sn = :empNo");
				}
			}
			if (conditions.get("type") != null && !conditions.get("type").toString().equals("全部")) {
				buffer.append(" and leavetype  = :type");
			}
			if (conditions.get("startDate") != null) {
				buffer.append(" and createtime >= :startDate");
			}
			if (conditions.get("endDate") != null) {
				buffer.append(" and createtime <= :endDate");
			}
		}
		return (Long) dao.total(buffer.toString(), conditions);
	}

	@Override
	public BizLeave getLeave(Long id) {
		return (BizLeave) dao.get(BizLeave.class, id);
	}

	@Override
	public void updateLeave(BizLeave leave) {
		this.dao.update(leave);	
	}

}
