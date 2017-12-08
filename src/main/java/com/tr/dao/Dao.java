package com.tr.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * dao �ӿ�
 * @param <T>
 *
 */
public interface Dao {
	
	// ����
	public Serializable save(Object object);
	
	// ��ҳ��ѯ
	public List find(String hql, Map<String, Object> conditions, int pageNo, int pageSize);
	
	// ��ѯȫ��
	public List find(String hql);
	
	// ��ѯ��ϸ
	public Object get(Class clazz,Serializable id);
	
	// �޸�
	public void update(Object object);
	
	// ͳ���ܼ�¼��
	public Serializable total(String hql, Map<String, Object> conditions);
	
	// ɾ��
	public void delete(Object object);
	
	// �����������ɾ����ϸ
	public Integer deleteDetail(Serializable mainId);
	
	// �����Բ���
	public List find(String hql,Object object);
}
