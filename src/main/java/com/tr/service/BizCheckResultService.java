package com.tr.service;

import java.util.List;

import com.tr.entity.BizCheckResult;
import com.tr.entity.BizClaimVoucher;

/**
 * ��˽���ӿ�
 *
 */
public interface BizCheckResultService {
	
	// ���ݱ�������Ż����˽������
	public List<BizCheckResult> getCheckResult(BizClaimVoucher bizClaimVoucher);
	
	// ��������
	public Long addNewResult(BizCheckResult checkResult);
	
	// ɾ��
	public void deleteResult(List<BizCheckResult> resultList);
}
