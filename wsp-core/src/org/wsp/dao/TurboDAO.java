package org.wsp.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.models.TradingSession;
import org.wsp.models.Turbo;

@Repository
@Transactional
public class TurboDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<Turbo> ListAll() {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"from Turbo where state <> 0 order by tradingSessionIdTradingSession , type, barDes, priority")
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<Turbo> ListActive() {
		return sessionFactory.getCurrentSession()
				.createQuery("from Turbo where state = 1").list();
	}

	@SuppressWarnings("unchecked")
	public List<Turbo> ListWorking() {
		return sessionFactory.getCurrentSession()
				.createQuery("from Turbo where state = 2").list();
	}

	public Turbo getById(Integer Id) {
		return (Turbo) sessionFactory.getCurrentSession().get(Turbo.class, Id);
	}

	@SuppressWarnings("unchecked")
	public List<Turbo> getByTradingSession(TradingSession session) {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"from Turbo where state <> 0 and tradingSessionIdTradingSession=:id")
				.setInteger("id", session.getIdTradingSession()).list();
	}

	@SuppressWarnings("unchecked")
	public List<Turbo> getByType(String Type) {
		return sessionFactory.getCurrentSession()
				.createQuery("from Turbo where state <> 0 and typeTurbo=:type")
				.setString("type", Type).list();
	}

	public void add(Turbo turbo) {
		sessionFactory.getCurrentSession().save(turbo);
	}

	public void update(Turbo turbo) {
		sessionFactory.getCurrentSession().update(turbo);
	}

	public void del(Turbo turbo) {
		sessionFactory.getCurrentSession().delete(turbo);
	}

	public void desactivate(Turbo turbo) {
		turbo.setState(0);
		sessionFactory.getCurrentSession().update(turbo);
	}

	public void init(Turbo turbo) {
		turbo.setState(1);
		sessionFactory.getCurrentSession().update(turbo);
	}

	@SuppressWarnings("unchecked")
	public Boolean exists(Turbo turbo) {
		List<Turbo> li = sessionFactory.getCurrentSession()
				.createQuery("from Turbo where name=:p")
				.setString("p", turbo.getName()).list();
		if (li.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public Turbo getActiveTurbo(String Type, TradingSession tradingSession) {
		String Query = "from Turbo where state = 2 and typeTurbo=:type and tradingSessionIdTradingSession=:id order by priority ASC";
		List<Turbo> Turbos = sessionFactory.getCurrentSession()
				.createQuery(Query).setString("type", Type)
				.setInteger("id", tradingSession.getIdTradingSession()).list();
		if (Turbos.isEmpty()) {
			String query = "from Turbo where state <> 0 and state <> 99 and typeTurbo=:type and tradingSessionIdTradingSession=:id order by priority ASC";
			List<Turbo> turbos = sessionFactory.getCurrentSession()
					.createQuery(query).setString("type", Type)
					.setInteger("id", tradingSession.getIdTradingSession())
					.list();
			if (turbos.isEmpty()) {
				return null;
			} else {
				return turbos.get(0);
			}
		} else {
			return Turbos.get(0);
		}

	}
}
