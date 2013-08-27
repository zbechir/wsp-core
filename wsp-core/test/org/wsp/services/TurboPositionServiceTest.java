package org.wsp.services;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wsp.models.TurboPosition;
import org.wsp.service.Implements.TurboPositionServiceImplements;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/wsp-context.xml"})
public class TurboPositionServiceTest {

	@Autowired
	TurboPositionServiceImplements turboPositionServiceImplements;
	@Test
	public void testAdd() {
		TurboPosition turboPosition=new TurboPosition();
		turboPosition.setAchat(0);
		turboPosition.setCreationDate(new Date());
		turboPosition.setTurboIdTurbo(10);
		turboPosition.setPrixSousJacent(0);
		turboPosition.setQte(0);
		turboPosition.setTradingSessionIdTradingSession(1);
		turboPosition.setVente(0);
		turboPositionServiceImplements.add(turboPosition);
		assertNotNull(turboPosition);
		assertNotNull(turboPosition.getIdTurboPosition());
	}

	@Test
	public void testGetById() {
		fail("Not yet implemented");
	}

}
