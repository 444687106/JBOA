package com.tr.service.impl;

import java.util.List;

import com.tr.dao.Dao;
import com.tr.entity.BizCheckResult;
import com.tr.entity.BizClaimVoucher;
import com.tr.service.BizCheckResultService;

/**
 * 审核结果业务实现类
 *
 */
public class BizCheckResultServiceImpl implements BizCheckResultService {
	
	// dao 接口
	private Dao dao;

	@Override
	public List<BizCheckResult> getCheckResult(BizClaimVoucher bizClaimVoucher) {
		String hql = "from BizCheckResult where bizClaimVoucher.id = :id order by sysEmployee.sysPosition.id";
		return getDao().find(hql, bizClaimVoucher);
	}

	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	@Override
	public Long addNewResult(BizCheckResult checkResult) {
		return (Long) dao.save(checkResult);
	}

	@Override
	public void deleteResult(List<BizCheckResult> resultList) {
		for (BizCheckResult result : resultList) {
			dao.delete(result);
		}
	}

}
