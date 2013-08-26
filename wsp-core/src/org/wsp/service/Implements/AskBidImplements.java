package org.wsp.service.Implements;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wsp.dao.AskBidDAO;
import org.wsp.models.AskBid;
import org.wsp.service.Interfaces.AskBidServiceInterface;

@Service(value = "AskBid")
public class AskBidImplements implements AskBidServiceInterface {
	
	@Autowired
	AskBidDAO dao;

	@Override
	public List<AskBid> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AskBid> listByDate(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AskBid> listType(String Type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AskBid> listLast(int number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AskBid> listToDay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AskBid> listAfterDate(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AskBid getById(Integer Id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(AskBid askBid) {
		dao.Add(askBid);

	}

	@Override
	public void update(AskBid askBid) {
		dao.Update(askBid);

	}

	@Override
	public void remove(AskBid askBid) {
		dao.Remove(askBid);

	}

}
