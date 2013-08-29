package org.wsp.TradingLogic;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.wsp.models.Turbo;
import org.wsp.models.TurboPosition;
import org.wsp.service.Interfaces.AskBidServiceInterface;
import org.wsp.service.Interfaces.ParamsServiceInterface;
import org.wsp.service.Interfaces.TurboPositionServiceInterface;


public class Trading {

	private static final Logger log = Logger.getLogger(Trading.class);
	private ApplicationContext context;
	private Turbo Call, Put;
	private TurboPosition CallTp, PutTp,TpCallLast,TpPutLast,TpCallAchat,TpPutAchat;
	private TurboPositionServiceInterface turboPositionServiceInterface;
	private AskBidServiceInterface askBidServiceInterface;
	private ParamsServiceInterface paramsServiceInterface;
	private Float NormalBuythreshold,NormalSellThreshold,DiffSellThreshold,OverlayBuyThersold;
	
	
	public Trading(ApplicationContext Context) {
		super();
		context = Context;
		turboPositionServiceInterface=(TurboPositionServiceInterface) context.getBean("TurboPosition");		
		askBidServiceInterface =(AskBidServiceInterface) context.getBean("AskBid");
		paramsServiceInterface=(ParamsServiceInterface) context.getBean("Params");
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
		NormalBuythreshold = new Float(paramsServiceInterface.getByName("NormalBuyThreshold"));
		NormalSellThreshold = new Float(paramsServiceInterface.getByName("NormalSellThreshold"));
		DiffSellThreshold = new Float(paramsServiceInterface.getByName("DiffSellThreshold"));
		OverlayBuyThersold = new Float(paramsServiceInterface.getByName("OverlayBuyThreshold"));				
		log.info("Trading Initialized...");
		savingTurboPositions();
	}
	
	public void Trade(){
		Integer Val = Integer.valueOf(paramsServiceInterface.getByName("AppStatus"));
		switch (Val) {
		case 2:
			normalTrading();
			break;
		case 3:
			noTradingWithSold();
			break;
		case 4:
			noTrading();
			break;

		default:
			normalTrading();
			break;
		}

	}
	
	private void noTrading() {
		// TODO Auto-generated method stub
		
	}

	private void noTradingWithSold() {
		// TODO Auto-generated method stub
		
	}

	private void normalTrading() {
		// TODO Auto-generated method stub
		
	}

	private void savingTurboPositions(){
		log.info("Saving Turbo Positions");
		turboPositionServiceInterface.add(CallTp);
		turboPositionServiceInterface.add(PutTp);
		
	}
	
	

}
