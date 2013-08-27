package org.wsp.services;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wsp.models.AskBid;
import org.wsp.service.Implements.AskBidImplements;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/wsp-context.xml"})
public class AskBidtest {

	@Autowired
	private AskBidImplements askBidImplements;
	@Test
	public void testinsert() {
		AskBid askBid=new AskBid();
		askBid.setAskOrBid("Achat");
		askBid.setMontantGlobal((float) (0.25 * 256));
		askBid.setPrixUnitaire((float) 0.25);
		askBid.setQte(256);
		askBid.setTradingSessionIdTradingSession(0);
		askBid.setTransDate(new Date());
		askBid.setTurboIdTurbo(0);
		askBid.setTurboPositionIdTurboPosition(0);
		askBidImplements.add(askBid);
		assertNotNull("AskBis not null",askBid);
		assertNotNull("AskBid Inserted",askBid.getIdAskBid());
		
	}

}
