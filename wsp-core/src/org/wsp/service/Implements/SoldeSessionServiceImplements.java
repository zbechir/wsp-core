package org.wsp.service.Implements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wsp.dao.SoldeSessionDAO;
import org.wsp.models.SoldeSession;
import org.wsp.models.TradingSession;
import org.wsp.service.Interfaces.SoldeSessionServiceInterface;
@Service(value = "SoldeSession")
public class SoldeSessionServiceImplements implements
		SoldeSessionServiceInterface {

	@Autowired
	SoldeSessionDAO dao;
	@Override
	public void add(SoldeSession soldeSession) {
		dao.add(soldeSession);
	}

	@Override
	public SoldeSession getLast(TradingSession tradingSession) {
		return dao.getLast(tradingSession);
	}

	@Override
	public SoldeSession getById(Integer Id) {
		return dao.getById(Id);
	}

}
