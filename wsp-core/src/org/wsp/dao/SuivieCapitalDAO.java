package org.wsp.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.models.SuivieCapital;
import org.wsp.models.TradingSession;

@Repository
@Transactional
public class SuivieCapitalDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void add(SuivieCapital suivieCapital) {
		sessionFactory.getCurrentSession().save(suivieCapital);
	}

	@SuppressWarnings("unchecked")
	public SuivieCapital getLast(TradingSession tradingSession) {
		List<SuivieCapital> sc = sessionFactory
				.getCurrentSession()
				.createQuery(
						"from SuivieCapital where tradingSessionIdTradingSession = :id order by idSuivieCapital desc")
				.setInteger("id", tradingSession.getIdTradingSession())
				.setMaxResults(1).list();
		if (sc.isEmpty()) {
			return null;
		} else {
			return sc.get(0);
		}
	}

	public SuivieCapital getbyId(Integer Id){
		return (SuivieCapital) sessionFactory.getCurrentSession().get(SuivieCapital.class, Id);
	}

}
