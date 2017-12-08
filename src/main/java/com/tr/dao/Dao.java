package com.tr.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * dao 接口
 * @param <T>
 *
 */
public interface Dao {
	
	// 新增
	public Serializable save(Object object);
	
	// 分页查询
	public List find(String hql, Map<String, Object> conditions, int pageNo, int pageSize);
	
	// 查询全部
	public List find(String hql);
	
	// 查询详细
	public Object get(Class clazz,Serializable id);
	
	// 修改
	public void update(Object object);
	
	// 统计总记录数
	public Serializable total(String hql, Map<String, Object> conditions);
	
	// 删除
	public void delete(Object object);
	
	// 按报销单编号删除明细
	public Integer deleteDetail(Serializable mainId);
	
	// 按属性查找
	public List find(String hql,Object object);
}
