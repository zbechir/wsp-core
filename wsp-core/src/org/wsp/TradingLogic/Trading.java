package org.wsp.TradingLogic;

import java.util.Date;

import javax.swing.text.Position;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.wsp.models.AskBid;
import org.wsp.models.SoldeSession;
import org.wsp.models.SuivieCapital;
import org.wsp.models.TradingSession;
import org.wsp.models.Turbo;
import org.wsp.models.TurboPosition;
import org.wsp.models.TurboPositionLogging;
import org.wsp.models.TurboPositionSimulation;
import org.wsp.service.Interfaces.AskBidServiceInterface;
import org.wsp.service.Interfaces.ParamsServiceInterface;
import org.wsp.service.Interfaces.SoldeSessionServiceInterface;
import org.wsp.service.Interfaces.SuivieCapitalServiceInterface;
import org.wsp.service.Interfaces.TradingSessionServiceInterface;
import org.wsp.service.Interfaces.TurboPositionServiceInterface;
import org.wsp.service.Interfaces.TurboServiceInterface;

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
	private SuivieCapitalServiceInterface suivieCapitalServiceInterface;
	private TurboServiceInterface turboServiceInterface;
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
		suivieCapitalServiceInterface = (SuivieCapitalServiceInterface) context
				.getBean("SuivieCapital");
		turboServiceInterface = (TurboServiceInterface) context
				.getBean("Turbo");

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
		TsUpdate();
		SuivieCapitalUpdate();
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

	private void TsUpdate() {
		tradingSession.setGlobalBalance(getCapital()
				- tradingSession.getSoldeInitial());
		tradingSessionServiceInterface.update(tradingSession);

	}

	private void SuivieCapitalUpdate() {
		SuivieCapital sc = new SuivieCapital();
		sc.setMontant(getCapital());
		sc.setSnapshotTime(new Date());
		sc.setTradingSessionIdTradingSession(tradingSession
				.getIdTradingSession());
		sc.setTurboPositionIdTurboPosition(CallTp.getIdTurboPosition());
		suivieCapitalServiceInterface.add(sc);
	}

	private Float getCapital() {
		Float Cap = new Float(0);
		Cap = getLiq()
				+ ((Call.getStock() * CallTp.getAchat()) + (Put.getStock() * PutTp
						.getAchat()));
		log.debug("Liquiditée : "+getLiq());
		log.debug("Call Value : "+Call.getStock() * CallTp.getAchat());
		log.debug("Put Value : "+Put.getStock() * PutTp.getAchat());
		log.info("Gloabal Balance ==>" + Cap + "€");
		return Cap;
	}

	private Float getLiq() {
		Float Liq;
		if (soldeSessionServiceInterface.getLast(tradingSession) == null) {
			Liq = tradingSession.getSoldeInitial();
		} else {
			Liq = soldeSessionServiceInterface.getLast(tradingSession)
					.getSoldeLiquid();
		}
		return Liq;
	}

	private Integer CalculateNominalStock(TurboPosition position) {
		Integer Res = new Integer(0);
		Float Liq = getLiq();
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
			Vente(CallTp);
			TpCallLast = PutTp;
			TpCallAchat = null;
			log.info("");
		}

		if (Put.getStock() != 0) {
			// askBidService.AddBid(PutTp);
			Vente(PutTp);
			TpPutLast = PutTp;
			TpPutAchat = null;
			log.info("");
		}

		log.info("No Trading");
	}

	private void normalTrading() {
		log.info("Begin Trading...");
		log.info("Call Turbo Position is : " + CallTp);
		log.info("Put Turbo Position is : " + PutTp);
		// WORKIN ON THE CALL
		log.info("Trading the call turbo position");
		Float Diff = new Float(0);
		Diff = Math.abs(CallTp.getAchat() - TpCallLast.getAchat());
		log.info("L ancienne Turbo position est ==> " + TpCallLast.getAchat()
				+ " €");
		log.info("La nouvelle Turbo position est ==> " + CallTp.getAchat()
				+ " €");
		log.info("Differance between last and new is ==>" + Diff + " €");
		if ((CallTp.getAchat() > 0.02) || (PutTp.getAchat() > 0.02)) {
			if (Diff != 0) {

				if (Call.getStock() == 0) {
					log.info("Turbo STOCK NULL");
					if (CallTp.getAchat() < TpCallLast.getAchat()) {
						log.info("TURBO A LA BAISSE");
						TpCallLast = CallTp;
					} else {
						log.info("TURBO A LA HAUSSE");
						if (Put.getStock() == 0) {
							if (Diff >= NormalBuythreshold) {
								log.info("Commencement Achat du Turbo ==>"
										+ Call);
								Integer Qte = CalculateNominalStock(CallTp);
								Achat(CallTp, Qte);
								TpCallAchat = CallTp;
								TpCallLast = CallTp;
								log.info("Achat Executer de " + Qte
										+ " du Turbo " + Call);
							}
						} else {
							if (Diff >= OverlayBuyThersold) {
								log.info("Commencement de achat du Turbo "
										+ Call);
								Integer Qte = CalculateNominalStock(CallTp);
								Achat(CallTp, Qte);
								TpCallAchat = CallTp;
								TpCallLast = CallTp;
								log.info("Achat Executer de " + Qte
										+ " du Turbo " + Call);
							}
						}

					}
				} else {
					log.info("Turbo STOCK " + Call.getStock());
					if ((CallTp.getAchat() < TpCallLast.getAchat())) {
						log.info("TURBO A LA BAISSE");
						log.info("La derniere Turbo position Acheté est ==> "
								+ TpCallAchat);
						Float DiffCallAchat = TpCallAchat.getVente()
								- CallTp.getVente();
						if (((Diff >= NormalSellThreshold) || (DiffCallAchat >= DiffSellThreshold))) {
							log.info("TURBO A LA BAISSE et commencement de procedure de vente du turbo "
									+ Call);
							Vente(CallTp);
							TpCallLast = CallTp;
							TpCallAchat = null;
							log.info("Vente Executer de " + Call.getStock()
									+ " du Turbo " + Call);
						}
					} else {
						TpCallLast = CallTp;
						log.info("TURBO A LA Hausse");
					}

				}
			}
		}

		// WORKIN ON THE PUT
		log.info("Trading the Put turbo position");
		Float DiffPut = new Float(0);
		DiffPut = Math.abs(PutTp.getAchat() - TpPutLast.getAchat());
		log.info("Lancienne Turbo position est ==> " + TpPutLast.getAchat());
		log.info("La nouvelle Turbo position est ==> " + PutTp.getAchat());
		log.info("DiffPuterance between last and new is ==>" + DiffPut);

		if ((PutTp.getAchat() > 0.02) || (PutTp.getAchat() > 0.02)) {
			if (DiffPut != 0) {

				if (Put.getStock() == 0) {
					log.info("Turbo STOCK NULL");
					if (PutTp.getAchat() < TpPutLast.getAchat()) {
						log.info("TURBO A LA BAISSE");
						TpPutLast = PutTp;
					} else {
						log.info("TURBO A LA HAUSSE");
						if (Call.getStock() == 0) {
							if (DiffPut >= NormalBuythreshold) {
								log.info("Commencement de l achat du Turbo "
										+ Put);
								Integer Qte = CalculateNominalStock(PutTp);
								Achat(PutTp, Qte);
								TpPutAchat = PutTp;
								TpPutLast = PutTp;
								log.info("Achat Executer de " + Qte
										+ " du Turbo " + Put);
							}
						} else {
							if (DiffPut >= OverlayBuyThersold) {
								log.info("Commencement de l achat du Turbo "
										+ Put);
								Integer Qte = CalculateNominalStock(PutTp);
								Achat(PutTp, Qte);
								TpPutAchat = PutTp;
								TpPutLast = PutTp;
								log.info("Achat Executer de " + Qte
										+ " du Turbo " + Put);
							}
						}

					}
				} else {
					log.info("Turbo STOCK " + Put.getStock());
					if ((PutTp.getAchat() < TpPutLast.getAchat())) {
						log.info("TURBO A LA BAISSE");
						log.info("La derniere Turbo position Acheté est ==> "
								+ TpPutAchat);
						Float DiffPutPutAchat = TpPutAchat.getVente()
								- PutTp.getVente();
						if (((DiffPut >= NormalSellThreshold) || (DiffPutPutAchat >= DiffSellThreshold))) {
							log.info("TURBO A LA BAISSE et commencement de procedure de vente du turbo "
									+ Put);
							Vente(PutTp);
							TpPutLast = PutTp;
							TpPutAchat = null;
							log.info("Vente Executer de " + Put.getStock()
									+ " du Turbo " + Put);
						}
					} else {
						TpPutLast = PutTp;
						log.info("TURBO A LA Hausse");
					}

				}
			}
		}

	}

	private void Achat(TurboPosition turboPosition, Integer Qte) {
		Turbo turbo=turboServiceInterface.getById(turboPosition.getTurboIdTurbo());
		turbo.setStock(Qte);
		turboServiceInterface.update(turbo);
		AskBid ab = new AskBid();
		ab.setAskOrBid("Achat");
		ab.setMontantGlobal(turboPosition.getVente() * Qte);
		ab.setPrixUnitaire(turboPosition.getVente());
		ab.setQte(Qte);
		ab.setTradingSessionIdTradingSession(tradingSession
				.getIdTradingSession());
		ab.setTransDate(new Date());
		ab.setTurboIdTurbo(turboPosition.getTurboIdTurbo());
		ab.setTurboPositionIdTurboPosition(turboPosition.getIdTurboPosition());
		askBidServiceInterface.add(ab);
		SoldeSession ss = new SoldeSession();
		ss.setAskBidIdAskBid(ab.getIdAskBid());
		ss.setTimeSnapshot(new Date());
		ss.setTradingSessionIdTradingSession(tradingSession
				.getIdTradingSession());
		Float ll = getLiq() - (Qte * turboPosition.getVente());
		ss.setSoldeLiquid(ll);
		soldeSessionServiceInterface.add(ss);
	}

	private void Vente(TurboPosition turboPosition) {
		Turbo turbo=turboServiceInterface.getById(turboPosition.getTurboIdTurbo());
		AskBid ab = new AskBid();
		ab.setAskOrBid("Vente");
		ab.setMontantGlobal(turboPosition.getAchat() * turbo.getStock());
		ab.setPrixUnitaire(turboPosition.getAchat());
		ab.setQte(turbo.getStock());
		ab.setTradingSessionIdTradingSession(tradingSession
				.getIdTradingSession());
		ab.setTransDate(new Date());
		ab.setTurboIdTurbo(turboPosition.getTurboIdTurbo());
		ab.setTurboPositionIdTurboPosition(turboPosition.getIdTurboPosition());
		askBidServiceInterface.add(ab);
		SoldeSession ss = new SoldeSession();
		ss.setAskBidIdAskBid(ab.getIdAskBid());
		ss.setTimeSnapshot(new Date());
		ss.setTradingSessionIdTradingSession(tradingSession
				.getIdTradingSession());
		Float ll = getLiq() + (turbo.getStock() * turboPosition.getAchat());
		ss.setSoldeLiquid(ll);
		soldeSessionServiceInterface.add(ss);
		turbo.setStock(0);
		turboServiceInterface.update(turbo);
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
