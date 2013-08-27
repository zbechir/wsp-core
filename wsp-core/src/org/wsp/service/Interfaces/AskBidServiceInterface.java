package org.wsp.service.Interfaces;

import java.util.Date;
import java.util.List;

import org.wsp.models.AskBid;
import org.wsp.models.Turbo;

public interface AskBidServiceInterface {

	public List<AskBid> listAll();
	public List<AskBid> listByDate(Date date);
	public List<AskBid> listType(String Type);
	public List<AskBid> listLast(int number);
	public List<AskBid> listToDay();
	public List<AskBid> listAfterDate(Date date);
	public AskBid getById(Integer Id);
	public AskBid getLstByTurbo(Turbo turbo);
	public void add(AskBid askBid);
	public void update(AskBid askBid);
	public void remove(AskBid askBid);
}
