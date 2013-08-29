package org.wsp.service.Implements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wsp.dao.ParamsDAO;
import org.wsp.service.Interfaces.ParamsServiceInterface;

@Service(value = "Params")
public class ParamsServiceImplements implements ParamsServiceInterface {
	
	@Autowired
	ParamsDAO dao;
	
	@Override
	public String getByName(String name) {
		return dao.getByName(name);
	}

}
