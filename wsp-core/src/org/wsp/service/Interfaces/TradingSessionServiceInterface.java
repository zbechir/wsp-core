package org.wsp.service.Interfaces;

import java.util.List;

import org.wsp.models.TradingSession;

public interface TradingSessionServiceInterface {

	public void init(TradingSession tradingSession);

	public void work(TradingSession tradingSession);

	public List<TradingSession> getActive();
	
	public List<TradingSession> getworking();
	public TradingSession getById(Integer Id);
}
