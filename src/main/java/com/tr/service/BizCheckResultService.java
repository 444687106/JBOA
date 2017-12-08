package com.tr.service;

import java.util.List;

import com.tr.entity.BizCheckResult;
import com.tr.entity.BizClaimVoucher;

/**
 * 审核结果接口
 *
 */
public interface BizCheckResultService {
	
	// 根据报销单编号获得审核结果集合
	public List<BizCheckResult> getCheckResult(BizClaimVoucher bizClaimVoucher);
	
	// 新增审批
	public Long addNewResult(BizCheckResult checkResult);
	
	// 删除
	public void deleteResult(List<BizCheckResult> resultList);
}
