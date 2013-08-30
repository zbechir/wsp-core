package org.wsp.service.Implements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wsp.dao.SuivieCapitalDAO;
import org.wsp.models.SuivieCapital;
import org.wsp.models.TradingSession;
import org.wsp.service.Interfaces.SuivieCapitalServiceInterface;

@Service(value = "SuivieCapital")
public class SuivieCapitalServiceImplements implements
		SuivieCapitalServiceInterface {

	@Autowired
	SuivieCapitalDAO dao;
	@Override
	public void add(SuivieCapital suivieCapital) {
		dao.add(suivieCapital);
	}

	@Override
	public SuivieCapital getLast(TradingSession tradingSession) {
		return dao.getLast(tradingSession);
	}

	@Override
	public SuivieCapital getById(Integer Id) {
		return dao.getbyId(Id);
	}

}
