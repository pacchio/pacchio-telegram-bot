package org.telegram.telegrambots.coinmarketcap;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Coin {
	private String symbol;
	private BigDecimal price;
	private BigDecimal circulatingSupply;

	public BigDecimal getRapporto1() {
		return rapporto1;
	}

	public void setRapporto1(BigDecimal rapporto1) {
		this.rapporto1 = rapporto1;
	}

	public BigDecimal getRapporto3() {
		return rapporto3;
	}

	public void setRapporto3(BigDecimal rapporto3) {
		this.rapporto3 = rapporto3;
	}

	public BigDecimal getRapporto2() {
		return rapporto2;
	}

	public void setRapporto2(BigDecimal rapporto2) {
		this.rapporto2 = rapporto2;
	}

	private BigDecimal rapporto1;
	private BigDecimal rapporto2;
	private BigDecimal rapporto3;
	private BigDecimal marketCap;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getCirculatingSupply() {
		return circulatingSupply;
	}

	public void setCirculatingSupply(BigDecimal circulatingSupply) {
		this.circulatingSupply = circulatingSupply;
	}

	public BigDecimal getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(BigDecimal marketCap) {
		this.marketCap = marketCap;
	}
}