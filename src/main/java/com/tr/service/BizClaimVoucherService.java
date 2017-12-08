package com.tr.service;

import java.util.List;
import java.util.Map;

import com.tr.entity.BizClaimVoucher;

/**
 * 报销单业务接口
 *
 */
public interface BizClaimVoucherService {
	
	// 添加新的报销单
	public Long addNewClaimVoucher(BizClaimVoucher bizClaimVoucher);
	
	// 查看报销单
	public List<BizClaimVoucher> getClaimVoucherByConditions(Map<String, Object> conditions, int pageNo, int pageSize);
	
	// 根据 id 获得报销单对象
	public BizClaimVoucher getBizClaimVoucher(Long id);
	
	// 修改报销单
	public void editBizClaimVoucher(BizClaimVoucher claimVoucher);
	
	// 获取总记录数
	public Long getTotal(Map<String, Object> conditions);
	
}
