package com.tr.service;

import java.util.List;

import com.tr.entity.BizClaimVoucherDetail;

/**
 *  报销单业务接口
 *
 */
public interface BizClaimVoucherDetailService {
	
	// 添加报销单明细
	public void addNewClaimVoucherDetail(List<BizClaimVoucherDetail> bizClaimVoucherDetails);
	
	// 删除报销单明细
	public boolean deleteDetail(Long id);
	
}
