package com.tr.service;

import java.util.List;

import com.tr.entity.BizClaimVoucherDetail;

/**
 *  ������ҵ��ӿ�
 *
 */
public interface BizClaimVoucherDetailService {
	
	// ��ӱ�������ϸ
	public void addNewClaimVoucherDetail(List<BizClaimVoucherDetail> bizClaimVoucherDetails);
	
	// ɾ����������ϸ
	public boolean deleteDetail(Long id);
	
}
