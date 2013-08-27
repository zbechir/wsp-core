package org.wsp.TradingLogic;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.wsp.models.Turbo;
import org.wsp.models.TurboPosition;
import org.wsp.service.Interfaces.AskBidServiceInterface;
import org.wsp.service.Interfaces.TurboPositionServiceInterface;


public class Trading {

	private static final Logger log = Logger.getLogger(Trading.class);
	private ApplicationContext context;
	private Turbo Call, Put;
	private TurboPosition CallTp, PutTp,TpCallLast,TpPutLast,TpCallAchat,TpPutAchat;
	private TurboPositionServiceInterface turboPositionServiceInterface;
	private AskBidServiceInterface askBidServiceInterface;
	
	public Trading(ApplicationContext context) {
		super();
		this.context = context;
		turboPositionServiceInterface=(TurboPositionServiceInterface) context.getBean("TurboPosition");		
		askBidServiceInterface =(AskBidServiceInterface) context.getBean("AskBid");
	}
	
	public void init(Turbo call, Turbo put,TurboPosition TpCall, TurboPosition TpPut){
		log.info("Initialisation of the trading...");
		Call=call;
		Put=put;
		CallTp=TpCall;
		PutTp=TpPut;
		log.info("Call Turbo Position is : " + CallTp);
		log.info("Put Turbo Position is : " + PutTp);
		if (TpCallLast == null
				|| TpCallLast.getTurboIdTurbo() != TpCall.getTurboIdTurbo()) {
			TpCallLast = CallTp;
		}
		if (TpPutLast == null
				|| TpPutLast.getTurboIdTurbo() != TpPut.getTurboIdTurbo()) {
			TpPutLast = TpPut;
		}
		if (TpCallAchat != null
				&& TpCallAchat.getIdTurboPosition() != TpCall.getTurboIdTurbo()) {
			TpCallAchat = null;
		}
		if (TpPutAchat != null
				&& TpPutAchat.getIdTurboPosition() != TpPut.getTurboIdTurbo()) {
			TpPutAchat = null;
		}

		if (Call.getStock() != 0 && TpCallAchat == null) {
			TpCallAchat = turboPositionServiceInterface.getById(askBidServiceInterface.getLstByTurbo(Call).getTurboPositionIdTurboPosition());
		}
		if (Put.getStock() != 0 && TpPutAchat == null) {
			TpPutAchat = turboPositionServiceInterface.getById(askBidServiceInterface.getLstByTurbo(Put).getTurboPositionIdTurboPosition());
		}
		log.info("Trading Initialized...");
		savingTurboPositions();
	}
	
	public void Trade(){
				
	}
	
	private void savingTurboPositions(){
		log.info("Saving Turbo Positions");
		turboPositionServiceInterface.add(CallTp);
		turboPositionServiceInterface.add(PutTp);
		
	}
	
	

}
