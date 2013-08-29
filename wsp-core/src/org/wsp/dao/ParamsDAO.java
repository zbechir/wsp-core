package org.wsp.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.models.Params;

@Repository
@Transactional
public class ParamsDAO {
	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	public List<Params> listAll(){
		return sessionFactory.getCurrentSession().createQuery("from Params").list();
	}
	
	public String getByName(String name) {
		return (String) sessionFactory.getCurrentSession().createQuery("select value from Params where param= :param").setString("param",name).list().get(0);
	}
	
	public void update(Params params){
		sessionFactory.getCurrentSession().update(params);
	}
	
}
