package org.wsp.threads;

import org.springframework.context.ApplicationContext;
import org.wsp.service.Interfaces.TurboServiceInterface;

public class TurboRefrech implements Runnable {

	private ApplicationContext context;
	
	public TurboRefrech(ApplicationContext Context) {
		super();
		context=Context;
	}

	@Override
	public void run() {
		TurboServiceInterface turboServiceInterface =(TurboServiceInterface) context.getBean("Turbo");
		turboServiceInterface.refreshTurbos();
	}

}
