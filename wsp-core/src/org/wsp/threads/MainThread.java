package org.wsp.threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.wsp.models.TradingSession;
import org.wsp.service.Interfaces.TradingSessionServiceInterface;

public class MainThread implements Runnable {

	private static final Logger log = Logger.getLogger(MainThread.class);
	private ApplicationContext context;
	private Boolean stop = false;
	private TradingSessionServiceInterface tradingSessionServiceInterface;
	private ArrayList<Thread> Th = new ArrayList<Thread>();

	public MainThread(ApplicationContext context) {
		super();
		this.context = context;
		tradingSessionServiceInterface = (TradingSessionServiceInterface) context
				.getBean("TradingSession");
	}

	@Override
	public void run() {
		ArrayList<TradingSessionThreads> Threads = new ArrayList<TradingSessionThreads>();
		try {
			while (!stop) {
				List<TradingSession> Tss = tradingSessionServiceInterface
						.getActive();
				if (!Tss.isEmpty()) {
					for (int i = 0; i < Tss.size(); i++) {
						TradingSession Ts = Tss.get(i);
						TradingSessionThreads tradingSessionThreads = new TradingSessionThreads(
								context, Ts);
						Thread thread = new Thread(tradingSessionThreads);
						thread.setName(Ts.getName());
						thread.start();
						Threads.add(tradingSessionThreads);
						Th.add(thread);
						tradingSessionServiceInterface.work(Ts);
					}
				}
				Thread.sleep(1000*60*1);
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage(),e);
		} finally {
			if (!Threads.isEmpty()) {
				for (int i = 0; i < Threads.size(); i++) {
					Threads.get(i).setStop(true);
				}
			}
			List<TradingSession> Tss = tradingSessionServiceInterface
					.getworking();
			if (!Tss.isEmpty()) {
				for (int i = 0; i < Tss.size(); i++) {
					tradingSessionServiceInterface.init(Tss.get(i));
				}
			}

		}
	}

	public Boolean getStop() {
		return stop;
	}

	public void setStop(Boolean stop) {
		this.stop = stop;
	}

	public String getStatus() {
		String Status = "";
		if (getStop()) {
			Status = "KO-Application Stopped";
		} else {
			Status = "OK-";
			if (Th != null || !Th.isEmpty()) {
				for (int i = 0; i < Th.size(); i++) {
					Thread th = Th.get(i);
					if (th.isAlive()) {
						Status = Status + "Thread " + th.getName()
								+ " is Running";
					} else {
						Status = Status + "Thread " + th.getName()
								+ " is Stopped";
					}
				}
			}else{
				Status = Status + "No thread created";
			}
		}
		return Status;
	}

}
