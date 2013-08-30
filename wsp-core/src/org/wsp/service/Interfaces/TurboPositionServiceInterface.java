package org.wsp.service.Interfaces;

import org.wsp.models.TurboPosition;
import org.wsp.models.TurboPositionLogging;
import org.wsp.models.TurboPositionSimulation;

public interface TurboPositionServiceInterface {

	public void add(TurboPosition turboPosition);

	public TurboPosition getById(Integer Id);

	public void add(TurboPositionLogging turboPosition);

	public void add(TurboPositionSimulation turboPosition);

}
