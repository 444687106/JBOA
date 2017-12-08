package com.tr.service;

import java.util.List;
import java.util.Map;

import com.tr.entity.BizLeave;

/**
 * ���ҵ��ӿ�
 *
 */
public interface BizLeaveService {
	
	// �������
	public void addNewLeave(BizLeave leave);
	
	// ��ҳ��ѯ
	public List<BizLeave> getLeaveList(Map<String, Object> conditions,int pageNo,int pageSize);
	
	// ����ܼ�¼��
	public Long getTotal(Map<String, Object> conditions);
	
	// �������������ٶ���
	public BizLeave getLeave(Long id);
	
	// �޸����
	public void updateLeave(BizLeave leave);
	
}
