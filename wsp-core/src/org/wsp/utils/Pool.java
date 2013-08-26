package org.wsp.utils;

import org.springframework.context.ApplicationContext;
import org.wsp.models.TradingSession;
import org.wsp.models.Turbo;
import org.wsp.models.TurboPosition;
import org.wsp.service.Interfaces.TurboServiceInterface;

public class Pool {
	private Turbo Call;
	private Turbo Put;
	private TradingSession tradingSession;
	private ApplicationContext context;
	private TurboServiceInterface turboServiceInterface;
	
	

	public Pool(TradingSession tradingSession, ApplicationContext context) {
		super();
		this.tradingSession = tradingSession;
		this.context = context;
		turboServiceInterface=(TurboServiceInterface) context.getBean("Turbo");
		Call=turboServiceInterface.getActiveCall(tradingSession);
		if(Call.getState()==1){
			turboServiceInterface.toWorkMode(Call);
		}
		Put=turboServiceInterface.getActivePut(tradingSession);
		if(Put.getState()==1){
			turboServiceInterface.toWorkMode(Put);
		}
	}

	public Turbo getCall() {
		return Call;
	}

	public void setCall(Turbo call) {
		Call = call;
	}

	public Turbo getPut() {
		return Put;
	}

	public void setPut(Turbo put) {
		Put = put;
	}
	
	public void update(){
		Call=turboServiceInterface.getActiveCall(tradingSession);
		if(Call.getState()==1){
			turboServiceInterface.toWorkMode(Call);
		}
		Put=turboServiceInterface.getActivePut(tradingSession);
		if(Put.getState()==1){
			turboServiceInterface.toWorkMode(Put);
		}
	}
	
	

}
