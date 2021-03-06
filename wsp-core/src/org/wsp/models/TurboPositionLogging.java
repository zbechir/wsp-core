package org.wsp.models;

// Generated 12 août 2013 22:46:28 by Hibernate Tools 4.0.0

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TurboPositionLogging generated by hbm2java
 */
@Entity
@Table(name = "TurboPositionLogging", catalog = "wsp")
public class TurboPositionLogging implements java.io.Serializable {

	private Integer idTurboPositionLogging;
	private int tradingSessionIdTradingSession;
	private int callIdTurbo;
	private int putIdTurbo;
	private float callPrice;
	private float putPrice;
	private float lastCall;
	private float lastPut;
	private Float achatCall;
	private Float achatPut;
	private Float prixSousJacent;
	private Date creationDate;

	public TurboPositionLogging() {
	}

	public TurboPositionLogging(int tradingSessionIdTradingSession,
			int callIdTurbo, int putIdTurbo, float callPrice, float putPrice,
			float lastCall, float lastPut, Date creationDate) {
		this.tradingSessionIdTradingSession = tradingSessionIdTradingSession;
		this.callIdTurbo = callIdTurbo;
		this.putIdTurbo = putIdTurbo;
		this.callPrice = callPrice;
		this.putPrice = putPrice;
		this.lastCall = lastCall;
		this.lastPut = lastPut;
		this.creationDate = creationDate;
	}

	public TurboPositionLogging(int tradingSessionIdTradingSession,
			int callIdTurbo, int putIdTurbo, float callPrice, float putPrice,
			float lastCall, float lastPut, Float achatCall, Float achatPut,
			Float prixSousJacent, Date creationDate) {
		this.tradingSessionIdTradingSession = tradingSessionIdTradingSession;
		this.callIdTurbo = callIdTurbo;
		this.putIdTurbo = putIdTurbo;
		this.callPrice = callPrice;
		this.putPrice = putPrice;
		this.lastCall = lastCall;
		this.lastPut = lastPut;
		this.achatCall = achatCall;
		this.achatPut = achatPut;
		this.prixSousJacent = prixSousJacent;
		this.creationDate = creationDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idTurboPositionLogging", unique = true, nullable = false)
	public Integer getIdTurboPositionLogging() {
		return this.idTurboPositionLogging;
	}

	public void setIdTurboPositionLogging(Integer idTurboPositionLogging) {
		this.idTurboPositionLogging = idTurboPositionLogging;
	}

	@Column(name = "TradingSession_idTradingSession", nullable = false)
	public int getTradingSessionIdTradingSession() {
		return this.tradingSessionIdTradingSession;
	}

	public void setTradingSessionIdTradingSession(
			int tradingSessionIdTradingSession) {
		this.tradingSessionIdTradingSession = tradingSessionIdTradingSession;
	}

	@Column(name = "Call_idTurbo", nullable = false)
	public int getCallIdTurbo() {
		return this.callIdTurbo;
	}

	public void setCallIdTurbo(int callIdTurbo) {
		this.callIdTurbo = callIdTurbo;
	}

	@Column(name = "Put_idTurbo", nullable = false)
	public int getPutIdTurbo() {
		return this.putIdTurbo;
	}

	public void setPutIdTurbo(int putIdTurbo) {
		this.putIdTurbo = putIdTurbo;
	}

	@Column(name = "CallPrice", nullable = false, precision = 12, scale = 0)
	public float getCallPrice() {
		return this.callPrice;
	}

	public void setCallPrice(float callPrice) {
		this.callPrice = callPrice;
	}

	@Column(name = "PutPrice", nullable = false, precision = 12, scale = 0)
	public float getPutPrice() {
		return this.putPrice;
	}

	public void setPutPrice(float putPrice) {
		this.putPrice = putPrice;
	}

	@Column(name = "LastCall", nullable = false, precision = 12, scale = 0)
	public float getLastCall() {
		return this.lastCall;
	}

	public void setLastCall(float lastCall) {
		this.lastCall = lastCall;
	}

	@Column(name = "LastPut", nullable = false, precision = 12, scale = 0)
	public float getLastPut() {
		return this.lastPut;
	}

	public void setLastPut(float lastPut) {
		this.lastPut = lastPut;
	}

	@Column(name = "AchatCall", precision = 12, scale = 0)
	public Float getAchatCall() {
		return this.achatCall;
	}

	public void setAchatCall(Float achatCall) {
		this.achatCall = achatCall;
	}

	@Column(name = "AchatPut", precision = 12, scale = 0)
	public Float getAchatPut() {
		return this.achatPut;
	}

	public void setAchatPut(Float achatPut) {
		this.achatPut = achatPut;
	}

	@Column(name = "PrixSousJacent", precision = 12, scale = 0)
	public Float getPrixSousJacent() {
		return this.prixSousJacent;
	}

	public void setPrixSousJacent(Float prixSousJacent) {
		this.prixSousJacent = prixSousJacent;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreationDate", nullable = false, length = 19)
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
