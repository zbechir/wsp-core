package org.wsp.service.Implements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wsp.dao.TurboPositionDAO;
import org.wsp.models.TurboPosition;
import org.wsp.models.TurboPositionLogging;
import org.wsp.models.TurboPositionSimulation;
import org.wsp.service.Interfaces.TurboPositionServiceInterface;

@Service(value = "TurboPosition")
public class TurboPositionServiceImplements implements
		TurboPositionServiceInterface {

	@Autowired
	TurboPositionDAO dao;
	@Override
	public void add(TurboPosition turboPosition) {
		dao.save(turboPosition);
	}

	@Override
	public TurboPosition getById(Integer Id) {
		return dao.getById(Id);
	}

	@Override
	public void add(TurboPositionLogging turboPosition) {
		dao.save(turboPosition);		
	}

	@Override
	public void add(TurboPositionSimulation turboPosition) {
		dao.save(turboPosition);		
	}

}
