package org.wsp.threads;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.wsp.TradingLogic.Trading;
import org.wsp.models.TradingSession;
import org.wsp.models.Turbo;
import org.wsp.models.TurboPosition;
import org.wsp.parser.HtmlParser;
import org.wsp.service.Interfaces.TurboServiceInterface;
import org.wsp.utils.Pool;

public class TradingSessionThreads implements Runnable {

	private static final Logger log = Logger
			.getLogger(TradingSessionThreads.class);
	private ApplicationContext context;
	private Boolean stop = false;
	private Pool pool;
	private TurboServiceInterface turboServiceInterface;
	private Trading trading;

	public TradingSessionThreads(ApplicationContext context,
			TradingSession tradingSession) {
		super();
		this.context = context;
		pool = new Pool(tradingSession, context);
		turboServiceInterface = (TurboServiceInterface) context
				.getBean("Turbo");
		trading = new Trading(context);
	}

	@Override
	public void run() {
		Turbo Call = null;
		Turbo Put = null;
		try {
			while (!stop) {
				if (pool != null) {
					Call = pool.getCall();
					Put = pool.getPut();
					if (Call != null && Put != null) {
						Calendar Now = Calendar.getInstance();
						Calendar Time = Calendar.getInstance();
						Time.clear();
						Time.set(Calendar.HOUR_OF_DAY,
								Now.get(Calendar.HOUR_OF_DAY));
						Time.set(Calendar.MINUTE, Now.get(Calendar.MINUTE));
						Date date = Time.getTime();
						// log.info(date);
						if (Now.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
								|| Now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
							log.info("It is week-end!!!!!! STOP WORKING");
							Thread.sleep(1000 * 60 * 15);
						} else {
							if (date.after(Call.getBeginNeg())
									&& date.before(Call.getEndNed())
									&& date.after(Put.getBeginNeg())
									&& date.before(Put.getEndNed())) {
								HtmlParser CallParser = new HtmlParser(Call,
										context);
								HtmlParser PutParser = new HtmlParser(Put,
										context);
								TurboPosition CallTp = CallParser
										.parsePosition();
								TurboPosition PutTp = PutParser.parsePosition();
								if (CallTp != null && PutTp != null) {
									trading.init(Call, Put, CallTp, PutTp);
									trading.Trade();
								} else {
									log.error("Problem in parsing Turbos, Updating the pool");
									// pool.update();
								}
								// Thread.sleep(1000 * 15 * 1);
							} else {
								SimpleDateFormat Sdf = new SimpleDateFormat(
										"HH:mm:ss");
								log.info("It is night " + Sdf.format(date)
										+ " !!! pls go sleep.");
								log.info("Will begin working at "
										+ Sdf.format(Put.getBeginNeg()));
								Thread.sleep(1000 * 60 * 1);
							}
						}
					} else {
						log.info("Call or Put Turbo is null trying to update them.");
						// pool.update();
					}
					pool.update();
				}

			}
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (Call != null && Put != null) {
				log.info("Initialize the Turbos...");
				turboServiceInterface.init(Call);
				turboServiceInterface.init(Put);
				log.info("Turbos Intialized...");
			}
		}
	}

	public Boolean getStop() {
		return stop;
	}

	public void setStop(Boolean stop) {
		this.stop = stop;
	}

}
