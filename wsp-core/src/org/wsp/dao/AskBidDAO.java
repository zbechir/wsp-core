package org.wsp.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.models.AskBid;
import org.wsp.models.Turbo;

@Repository
@Transactional
public class AskBidDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<AskBid> ListAll() {
		return sessionFactory.getCurrentSession().createQuery("from AskBid")
				.list();
	}

	public AskBid getById(Integer Id) {
		return (AskBid) sessionFactory.getCurrentSession()
				.get(AskBid.class, Id);
	}

	@SuppressWarnings("unchecked")
	public AskBid getLastByTurbo(Turbo turbo) {
		List<AskBid> asks = sessionFactory
				.getCurrentSession()
				.createQuery(
						"from AskBid where turboIdTurbo = :id order by idAskBid desc")
				.setInteger("id", turbo.getIdTurbo()).setMaxResults(1).list();
		if (asks.isEmpty()) {
			return null;
		} else {
			return asks.get(0);
		}
	}

	public void Add(AskBid askBid) {
		sessionFactory.getCurrentSession().save(askBid);
	}

	public void Update(AskBid askBid) {
		sessionFactory.getCurrentSession().update(askBid);
	}

	public void Remove(AskBid askBid) {
		sessionFactory.getCurrentSession().delete(askBid);
	}

}
