package org.wsp.service.Implements;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wsp.dao.TradingsessionDAO;
import org.wsp.models.TradingSession;
import org.wsp.service.Interfaces.TradingSessionServiceInterface;

@Service(value = "TradingSession")
public class TradingSessionServiceImplements implements
		TradingSessionServiceInterface {
	@Autowired
	TradingsessionDAO dao;

	@Override
	public void init(TradingSession tradingSession) {
		dao.init(tradingSession);
	}

	@Override
	public void work(TradingSession tradingSession) {
		dao.working(tradingSession);
	}

	@Override
	public List<TradingSession> getActive() {
		return dao.ListActive();
	}

	@Override
	public List<TradingSession> getworking() {
		return dao.ListWorking();
	}

}
