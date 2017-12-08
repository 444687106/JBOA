package com.tr.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.tr.dao.Dao;

/**
 * dao  µœ÷¿‡
 *
 */
public class DaoImpl extends HibernateDaoSupport implements Dao {

	@Override
	public Serializable save(Object object) {
		return this.getHibernateTemplate().save(object);
	}

	@Override
	public List find(final String hql, final Map<String, Object> conditions, final int pageNo,
			final int pageSize) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List>() {

			@Override
			public List doInHibernate(Session session) throws HibernateException,
					SQLException {
				return session.createQuery(hql).setProperties(conditions)
						.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
			}
		});
	}

	@Override
	public Object get(Class clazz, Serializable id) {
		return this.getHibernateTemplate().get(clazz, id);
	}

	@Override
	public void update(Object object) {
		this.getHibernateTemplate().update(object);
	}

	@Override
	public Serializable total(final String hql, final Map<String, Object> conditions) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Serializable>() {
			@Override
			public Serializable doInHibernate(Session session)
					throws HibernateException, SQLException {
				return (Serializable) session.createQuery(hql).setProperties(conditions).uniqueResult();
			}
		});
	}

	@Override
	public Integer deleteDetail(final Serializable mainId) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException,
					SQLException {
				return session.createSQLQuery("delete biz_claim_voucher_detail where main_id = ?")
						.setParameter(0, mainId)
						.executeUpdate();
			}
		});
	}

	@Override
	public List find(String hql,Object object) {
		return this.getHibernateTemplate().findByValueBean(hql,object);
	}

	@Override
	public void delete(Object object) {
		this.getHibernateTemplate().delete(object);
	}

	@Override
	public List find(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

}
