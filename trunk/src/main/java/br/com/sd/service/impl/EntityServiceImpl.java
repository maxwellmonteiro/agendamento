package br.com.sd.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.sd.HibernateUtil;
import br.com.sd.service.EntityService;

public class EntityServiceImpl<T extends Serializable> implements EntityService<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7583346647282079092L;
	
	private transient Session session;
	private transient Transaction tx;
	
	protected Session openSession() {
		try {
			if (session == null || !session.isOpen()) {
				session = HibernateUtil.getSessionFactory().openSession();
			}
		} catch (HibernateException e) {			
			e.printStackTrace();
		}
		return session;
	}
	
	protected void closeSession() {
		if (session != null && session.isOpen()) {
			try {
				session.close();
			} catch (HibernateException e) {
				throw e;
			}
		}
	}

	private String getClassName() {
		Type genericSuperclass = this.getClass().getGenericSuperclass();
	    if (genericSuperclass instanceof ParameterizedType) {
	      ParameterizedType pt = (ParameterizedType) genericSuperclass;
	      Type type = pt.getActualTypeArguments()[0];
	      return type.getTypeName();
	    }
	    return null;
	}
	
	@Override
	public T salvar(T entity) {	
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(entity);
			tx.commit();			
		} catch (HibernateException e) {
			throw e;
		}
		finally {
			closeSession();
		}
		
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> buscarTodos() {
		List<T> entities;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			entities = session.createQuery("from " + getClassName()).list();
		} catch (HibernateException e) {
			throw e;
		}
		finally {
			closeSession();
		}
		return entities;
	}

}
