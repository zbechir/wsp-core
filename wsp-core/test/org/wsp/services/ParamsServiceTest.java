package org.wsp.services;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wsp.service.Implements.ParamsServiceImplements;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/wsp-context.xml"})
public class ParamsServiceTest {

	@Autowired
	ParamsServiceImplements paramsServiceImplements;
	@Test
	public void testGetByName() {
		String test=paramsServiceImplements.getByName("DiffSellThreshold");
		assertNotNull(test);
		System.out.println(test);
	}

}
