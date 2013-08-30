package org.wsp.service.Interfaces;

import org.wsp.models.SoldeSession;
import org.wsp.models.TradingSession;

public interface SoldeSessionServiceInterface {
	public void add(SoldeSession soldeSession);
	public SoldeSession getLast(TradingSession tradingSession);
	public SoldeSession getById(Integer Id);
}
