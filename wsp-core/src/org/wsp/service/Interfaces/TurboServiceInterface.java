package org.wsp.service.Interfaces;

import java.util.List;

import org.wsp.models.TradingSession;
import org.wsp.models.Turbo;

public interface TurboServiceInterface {
	
	public void refreshTurbos();
	public List<Turbo> listAllturbo();
	public Turbo getActiveCall(TradingSession tradingSession);
	public Turbo getActivePut(TradingSession tradingSession);
	public void toWorkMode(Turbo turbo);
	public void init(Turbo turbo);
	public void desactivate(Turbo turbo);
	public Turbo getById(Integer Id);
}
