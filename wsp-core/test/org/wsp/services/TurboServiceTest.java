package org.wsp.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wsp.service.Implements.TurboServiceImplements;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/wsp-context.xml"})
public class TurboServiceTest {

	@Autowired
	private TurboServiceImplements turboServiceImplements;
	@Test
	public void testAddTurbos() {
		turboServiceImplements.refreshTurbos();
		
	}

}
