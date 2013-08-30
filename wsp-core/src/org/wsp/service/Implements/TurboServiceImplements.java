package org.wsp.service.Implements;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wsp.dao.TurboDAO;
import org.wsp.models.TradingSession;
import org.wsp.models.Turbo;
import org.wsp.parser.HtmlParser;
import org.wsp.service.Interfaces.TurboServiceInterface;

@Service(value = "Turbo")
public class TurboServiceImplements implements TurboServiceInterface {

	@Autowired
	TurboDAO dao;

	@Override
	public void refreshTurbos() {
		HtmlParser html = new HtmlParser();
		List<Turbo> turbos = html.getAllTurbo("Turbo%2520Call", "var%20DESC");
		for (int i = 0; i < turbos.size(); i++) {
			if (turbos.get(i) != null) {
				if (!dao.exists(turbos.get(i))) {
					dao.add(turbos.get(i));
				}
			}
		}
		turbos = html.getAllTurbo("Turbo%2520Call", "var%20ASC");
		for (int i = 0; i < turbos.size(); i++) {
			if (turbos.get(i) != null) {
				if (!dao.exists(turbos.get(i))) {
					dao.add(turbos.get(i));
				}
			}
		}
		turbos = html.getAllTurbo( "Turbo%2520Put", "var%20DESC");
		for (int i = 0; i < turbos.size(); i++) {
			if (turbos.get(i) != null) {
				if (!dao.exists(turbos.get(i))) {
					dao.add(turbos.get(i));
				}
			}
		}
		turbos = html.getAllTurbo("Turbo%2520Put", "var%20ASC");
		for (int i = 0; i < turbos.size(); i++) {
			if (turbos.get(i) != null) {
				if (!dao.exists(turbos.get(i))) {
					dao.add(turbos.get(i));
				}
			}
		}

	}

	@Override
	public List<Turbo> listAllturbo() {
		return dao.ListAll();
	}

	@Override
	public Turbo getActiveCall(TradingSession tradingSession) {
		return dao.getActiveTurbo("Turbo Call", tradingSession);
	}

	@Override
	public Turbo getActivePut(TradingSession tradingSession) {
		return dao.getActiveTurbo("Turbo Put", tradingSession);
	}

	@Override
	public void toWorkMode(Turbo turbo) {
		turbo.setState(2);
		dao.update(turbo);		
	}

	@Override
	public void init(Turbo turbo) {
		dao.init(turbo);		
	}

	@Override
	public void desactivate(Turbo turbo) {
		dao.desactivate(turbo);		
	}

	@Override
	public Turbo getById(Integer Id) {
		return dao.getById(Id);
	}

}
