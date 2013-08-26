package org.wsp.TradingLogic;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.wsp.models.Turbo;
import org.wsp.models.TurboPosition;
import org.wsp.service.Interfaces.TurboPositionServiceInterface;


public class Trading {

	private static final Logger log = Logger.getLogger(Trading.class);
	private ApplicationContext context;
	private Turbo Call, Put;
	private TurboPosition CallTp, PutTp;
	private TurboPositionServiceInterface turboPositionServiceInterface;
	
	public Trading(ApplicationContext context, Turbo call, Turbo put,
			TurboPosition callTp, TurboPosition putTp) {
		super();
		this.context = context;
		turboPositionServiceInterface=(TurboPositionServiceInterface) context.getBean("TurboPosition");
		Call = call;
		Put = put;
		CallTp = callTp;
		PutTp = putTp;
	}
	
	public void Trade(){
		turboPositionServiceInterface.add(CallTp);
		log.info("Turbo CALL : "+CallTp);
		turboPositionServiceInterface.add(PutTp);
		log.info("Turbo PUT : "+PutTp);
		
	}
	
	
	

}
