package org.wsp.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.models.SoldeSession;
import org.wsp.models.TradingSession;

@Repository
@Transactional
public class SoldeSessionDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void add(SoldeSession soldeSession){
		sessionFactory.getCurrentSession().save(soldeSession);
	}
	
	@SuppressWarnings("unchecked")
	public SoldeSession getLast(TradingSession tradingSession){
		List<SoldeSession> ss=sessionFactory.getCurrentSession().createQuery("from SoldeSession where tradingSessionIdTradingSession = :id order by idSoldeSession desc").setInteger("id", tradingSession.getIdTradingSession()).setMaxResults(1).list();
		if (ss.isEmpty()){
			return null;
		}else{
			return ss.get(0);
		}
	}
	
	public SoldeSession getById(Integer Id){
		return (SoldeSession) sessionFactory.getCurrentSession().get(SoldeSession.class, Id);
	}
	
		
}
