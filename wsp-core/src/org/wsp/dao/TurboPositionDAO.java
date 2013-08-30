package org.wsp.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.models.TurboPosition;
import org.wsp.models.TurboPositionLogging;
import org.wsp.models.TurboPositionSimulation;

@Repository
@Transactional
public class TurboPositionDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(TurboPosition turboPosition) {
		sessionFactory.getCurrentSession().save(turboPosition);
		sessionFactory.getCurrentSession().flush();
	}

	public TurboPosition getById(Integer Id) {
		return (TurboPosition) sessionFactory.getCurrentSession().get(
				TurboPosition.class, Id);
	}
	
	public void save(TurboPositionLogging turboPositionLogging){
		sessionFactory.getCurrentSession().save(turboPositionLogging);
	}
	
	public void save (TurboPositionSimulation turboPositionSimulation){
		sessionFactory.getCurrentSession().save(turboPositionSimulation);
	}
}
