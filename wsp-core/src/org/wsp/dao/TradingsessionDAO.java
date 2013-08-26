package org.wsp.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.models.TradingSession;

@Repository
@Transactional
public class TradingsessionDAO {
	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	public List<TradingSession> ListAll(){
		return sessionFactory.getCurrentSession().createQuery("from TradingSession where state <> 0").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<TradingSession> ListActive(){
		return sessionFactory.getCurrentSession().createQuery("from TradingSession where state = 1").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<TradingSession> ListWorking(){
		return sessionFactory.getCurrentSession().createQuery("from TradingSession where state = 2").list();
	}
	
	public TradingSession getById(Integer Id){
		return (TradingSession) sessionFactory.getCurrentSession().get(TradingSession.class, Id);
	}
	
	public void add(TradingSession session){
		sessionFactory.getCurrentSession().save(session);
	}
	
	public void update(TradingSession session){
		sessionFactory.getCurrentSession().update(session);
	}
	
	public void del(TradingSession session){
		sessionFactory.getCurrentSession().delete(session);
	}
	
	public void desactivate(TradingSession session){
		session.setState(0);
		sessionFactory.getCurrentSession().update(session);
	}
	
	public void init(TradingSession session){
		session.setState(1);
		sessionFactory.getCurrentSession().update(session);
	}
	
	public void working(TradingSession session){
		session.setState(2);
		sessionFactory.getCurrentSession().update(session);
	}
}
