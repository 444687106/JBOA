package com.tr.service;

import java.util.List;
import java.util.Map;

import com.tr.entity.BizClaimVoucher;

/**
 * ������ҵ��ӿ�
 *
 */
public interface BizClaimVoucherService {
	
	// ����µı�����
	public Long addNewClaimVoucher(BizClaimVoucher bizClaimVoucher);
	
	// �鿴������
	public List<BizClaimVoucher> getClaimVoucherByConditions(Map<String, Object> conditions, int pageNo, int pageSize);
	
	// ���� id ��ñ���������
	public BizClaimVoucher getBizClaimVoucher(Long id);
	
	// �޸ı�����
	public void editBizClaimVoucher(BizClaimVoucher claimVoucher);
	
	// ��ȡ�ܼ�¼��
	public Long getTotal(Map<String, Object> conditions);
	
}
