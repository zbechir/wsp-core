package org.wsp.threads;

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
	private TradingSession tradingSession;
	private Pool pool;
	private TurboServiceInterface turboServiceInterface;

	public TradingSessionThreads(ApplicationContext context,
			TradingSession tradingSession) {
		super();
		this.context = context;
		this.tradingSession = tradingSession;
		pool = new Pool(tradingSession, context);
		turboServiceInterface = (TurboServiceInterface) context
				.getBean("Turbo");
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
						HtmlParser CallParser = new HtmlParser(Call);
						HtmlParser PutParser = new HtmlParser(Put);
						TurboPosition CallTp = CallParser.parsePosition();
						TurboPosition PutTp = PutParser.parsePosition();
						Trading trading=new Trading(context, Call, Put, CallTp, PutTp);
						trading.Trade();
					} else {
						pool.update();
					}
				}

			}
		} finally {
			if (Call != null && Put != null) {
				turboServiceInterface.init(Call);
				turboServiceInterface.init(Put);
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
