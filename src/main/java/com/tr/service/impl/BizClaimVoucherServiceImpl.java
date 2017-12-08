package com.tr.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.tr.dao.Dao;
import com.tr.entity.BizClaimVoucher;
import com.tr.service.BizClaimVoucherService;

/**
 * 报销单业务实现类
 *
 */
public class BizClaimVoucherServiceImpl implements BizClaimVoucherService {
	
	// dao 接口
	private Dao dao;

	@Override
	public Long addNewClaimVoucher(BizClaimVoucher bizClaimVoucher) {
		return (Long) getDao().save(bizClaimVoucher);
	}

	@Override
	public List<BizClaimVoucher> getClaimVoucherByConditions(Map<String, Object> conditions, int pageNo, int pageSize) {
		StringBuffer buffer = new StringBuffer("from BizClaimVoucher where 1=1");
		conditions.put("deptMgrStatus", "新创建");
		if (conditions.get("position") == null) {
			return null;
		} else {
			if (conditions.get("position").toString().equals("员工")) {
				buffer.append(" and sysEmployeeByCreateSn.sn = :empNo");
			} else if (conditions.get("position").toString().equals("部门经理")){
				buffer.append(" and sysEmployeeByCreateSn.sysDepartment.id = :deptNo");
				buffer.append(" and status <> :deptMgrStatus");
			} else {
				buffer.append(" and sysEmployeeByNextDealSn.sn = :empNo");
				buffer.append(" and status <> :deptMgrStatus");
			}
			if (conditions.get("status") != null && !conditions.get("status").toString().equals("全部")) {
				buffer.append(" and status = :status");
			}
			if (conditions.get("startDate") != null) {
				buffer.append(" and createTime >= :startDate");
			}
			if (conditions.get("endDate") != null) {
				buffer.append(" and createTime <= :endDate");
			}
			buffer.append(" order by createTime desc, status");
		}
		return dao.find(buffer.toString(), conditions, pageNo, pageSize);
	}

	@Override
	public BizClaimVoucher getBizClaimVoucher(Long id) {
		return (BizClaimVoucher) dao.get(BizClaimVoucher.class,id);
	}

	@Override
	public void editBizClaimVoucher(BizClaimVoucher claimVoucher) {
		dao.update(claimVoucher);
	}

	@Override
	public Long getTotal(Map<String, Object> conditions) {
		StringBuffer buffer = new StringBuffer("select count(id) from BizClaimVoucher b where 1=1");
		conditions.put("deptMgrStatus", "新创建");
		if (conditions.get("position") == null) {
			return 0L;
		} else {
			if (conditions.get("position").toString().equals("员工")) {
				buffer.append(" and sysEmployeeByCreateSn.sn = :empNo");
			} else if (conditions.get("position").toString().equals("部门经理")){
				buffer.append(" and sysEmployeeByCreateSn.sysDepartment.id = :deptNo");
				buffer.append(" and status <> :deptMgrStatus");
			} else {
				buffer.append(" and sysEmployeeByNextDealSn.sn = :empNo");
				buffer.append(" and status <> :deptMgrStatus");
			}
			if (conditions.get("status") != null && !conditions.get("status").toString().equals("全部")) {
				buffer.append(" and status = :status");
			}
			if (conditions.get("startDate") != null) {
				buffer.append(" and createTime >= :startDate");
			}
			if (conditions.get("endDate") != null) {
				buffer.append(" and createTime <= :endDate");
			}
		}
		return (Long) dao.total(buffer.toString(), conditions);
	}

	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

}
