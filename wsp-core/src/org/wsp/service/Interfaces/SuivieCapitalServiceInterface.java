package org.wsp.service.Interfaces;

import org.wsp.models.SuivieCapital;
import org.wsp.models.TradingSession;

public interface SuivieCapitalServiceInterface {

	public void add(SuivieCapital suivieCapital);
	public SuivieCapital getLast(TradingSession tradingSession);
	public SuivieCapital getById(Integer Id);
}
