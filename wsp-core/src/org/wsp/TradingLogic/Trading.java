package org.wsp.TradingLogic;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.wsp.models.TradingSession;
import org.wsp.models.Turbo;
import org.wsp.models.TurboPosition;
import org.wsp.models.TurboPositionLogging;
import org.wsp.models.TurboPositionSimulation;
import org.wsp.service.Interfaces.AskBidServiceInterface;
import org.wsp.service.Interfaces.ParamsServiceInterface;
import org.wsp.service.Interfaces.SoldeSessionServiceInterface;
import org.wsp.service.Interfaces.TradingSessionServiceInterface;
import org.wsp.service.Interfaces.TurboPositionServiceInterface;

public class Trading {

	private static final Logger log = Logger.getLogger(Trading.class);
	private ApplicationContext context;
	private TradingSession tradingSession;
	private Turbo Call, Put;
	private TurboPosition CallTp, PutTp, TpCallLast, TpPutLast, TpCallAchat,
			TpPutAchat;
	private TurboPositionServiceInterface turboPositionServiceInterface;
	private AskBidServiceInterface askBidServiceInterface;
	private ParamsServiceInterface paramsServiceInterface;
	private SoldeSessionServiceInterface soldeSessionServiceInterface;
	private TradingSessionServiceInterface tradingSessionServiceInterface;
	private Float NormalBuythreshold, NormalSellThreshold, DiffSellThreshold,
			OverlayBuyThersold;

	public Trading(ApplicationContext Context) {
		super();
		context = Context;
		turboPositionServiceInterface = (TurboPositionServiceInterface) context
				.getBean("TurboPosition");
		askBidServiceInterface = (AskBidServiceInterface) context
				.getBean("AskBid");
		paramsServiceInterface = (ParamsServiceInterface) context
				.getBean("Params");
		soldeSessionServiceInterface = (SoldeSessionServiceInterface) context
				.getBean("SoldeSession");
		tradingSessionServiceInterface = (TradingSessionServiceInterface) context
				.getBean("TradingSession");

	}

	public void init(Turbo call, Turbo put, TurboPosition TpCall,
			TurboPosition TpPut) {
		log.info("Initialisation of the trading...");
		Call = call;
		Put = put;
		CallTp = TpCall;
		PutTp = TpPut;
		tradingSession = tradingSessionServiceInterface.getById(Call
				.getTradingSessionIdTradingSession());
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
			TpCallAchat = turboPositionServiceInterface
					.getById(askBidServiceInterface.getLstByTurbo(Call)
							.getTurboPositionIdTurboPosition());
		}
		if (Put.getStock() != 0 && TpPutAchat == null) {
			TpPutAchat = turboPositionServiceInterface
					.getById(askBidServiceInterface.getLstByTurbo(Put)
							.getTurboPositionIdTurboPosition());
		}
		NormalBuythreshold = new Float(
				paramsServiceInterface.getByName("NormalBuyThreshold"));
		NormalSellThreshold = new Float(
				paramsServiceInterface.getByName("NormalSellThreshold"));
		DiffSellThreshold = new Float(
				paramsServiceInterface.getByName("DiffSellThreshold"));
		OverlayBuyThersold = new Float(
				paramsServiceInterface.getByName("OverlayBuyThreshold"));
		log.info("Trading Initialized...");
		savingTurboPositions();
	}

	public void Trade() {
		Integer Val = Integer.valueOf(paramsServiceInterface
				.getByName("AppStatus"));
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

	private Integer CalculateNominalStock(TurboPosition position) {
		Integer Res = new Integer(0);
		float Liq;
		if (soldeSessionServiceInterface.getLast(tradingSession) == null) {
			Liq = tradingSession.getSoldeInitial();
		} else {
			Liq = soldeSessionServiceInterface.getLast(tradingSession)
					.getSoldeLiquid();
		}
		if (Call.getStock() != 0 || Put.getStock() != 0) {
			Res = (int) Math.round(Liq * 1 / position.getVente());
		} else {
			Res = (int) Math.round(Liq * 0.5 / position.getVente());
		}
		return Res;
	}

	private void noTrading() {
		log.info("No Trading");
	}

	private void noTradingWithSold() {
		if (Call.getStock() != 0) {
			//askBidService.AddBid(CallTp);
			TpCallLast = PutTp;
			TpCallAchat = null;
			log.info("");
		}

		if (Put.getStock() != 0) {
			//askBidService.AddBid(PutTp);
			TpPutLast = PutTp;
			TpPutAchat = null;
			log.info("");
		}

		log.info("No Trading");
	}

	private void normalTrading() {
		// TODO Auto-generated method stub

	}

	private void savingTurboPositions() {
		log.info("Saving Turbo Positions");
		turboPositionServiceInterface.add(CallTp);
		turboPositionServiceInterface.add(PutTp);
		// LOGGING SAVE
		TurboPositionLogging turboPositionLogging = new TurboPositionLogging();
		if (TpCallAchat == null) {
			turboPositionLogging.setAchatCall((float) 0);
		} else {
			turboPositionLogging.setAchatCall(TpCallAchat.getAchat());
		}
		if (TpPutAchat == null) {
			turboPositionLogging.setAchatPut((float) 0);
		} else {
			turboPositionLogging.setAchatPut(TpPutAchat.getAchat());
		}
		turboPositionLogging.setCallIdTurbo(CallTp.getTurboIdTurbo());
		turboPositionLogging.setCallPrice(CallTp.getAchat());
		turboPositionLogging.setCreationDate(new Date());
		turboPositionLogging.setLastCall(TpCallLast.getAchat());
		turboPositionLogging.setLastPut(TpPutLast.getAchat());
		turboPositionLogging.setPrixSousJacent(CallTp.getPrixSousJacent());
		turboPositionLogging.setPutIdTurbo(PutTp.getTurboIdTurbo());
		turboPositionLogging.setPutPrice(PutTp.getAchat());
		turboPositionLogging.setTradingSessionIdTradingSession(CallTp
				.getTradingSessionIdTradingSession());
		turboPositionServiceInterface.add(turboPositionLogging);

		// SIMULATION SAVE

		TurboPositionSimulation turboPositionSimulation = new TurboPositionSimulation();
		turboPositionSimulation.setAchatCall(CallTp.getAchat());
		turboPositionSimulation.setAchatPut(PutTp.getAchat());
		turboPositionSimulation.setCallIdTurbo(CallTp.getTurboIdTurbo());
		turboPositionSimulation.setCreationDate(new Date());
		turboPositionSimulation.setPutIdTurbo(PutTp.getTurboIdTurbo());
		turboPositionSimulation.setTradingSessionIdTradingSession(CallTp
				.getTradingSessionIdTradingSession());
		turboPositionSimulation.setVenteCall(CallTp.getVente());
		turboPositionSimulation.setVentePut(PutTp.getVente());
		turboPositionServiceInterface.add(turboPositionSimulation);
		log.info("Positions saved...");
	}

}
