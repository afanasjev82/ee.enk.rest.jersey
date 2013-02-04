package ee.enk.rest.jersey.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class Tradable {
	private long id;
	private String bestBid;
	private String bestAsk;
	private String contracts;
	private String highestTradedPrice;
	private String lowestTradedPrice;
	private String closingPrice;
	private String startTradingDate; 
	private String stopTradingDate;
	private String startDeliveryDate;
	private String stopDeliveryDate;
	private String mac;
	
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Tradable() {
	}
	
	public Tradable(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getBestBid() {
		return bestBid;
	}

	public String getBestAsk() {
		return bestAsk;
	}

	public String getContracts() {
		return contracts;
	}

	public String getHighestTradedPrice() {
		return highestTradedPrice;
	}

	public String getLowestTradedPrice() {
		return lowestTradedPrice;
	}

	public String getClosingPrice() {
		return closingPrice;
	}

	public String getStartTradingDate() {
		return startTradingDate;
	}

	public String getStopTradingDate() {
		return stopTradingDate;
	}

	public String getStartDeliveryDate() {
		return startDeliveryDate;
	}

	public String getStopDeliveryDate() {
		return stopDeliveryDate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setBestBid(String bestBid) {
		this.bestBid = bestBid;
	}

	public void setBestAsk(String bestAsk) {
		this.bestAsk = bestAsk;
	}

	public void setContracts(String contracts) {
		this.contracts = contracts;
	}

	public void setHighestTradedPrice(String highestTradedPrice) {
		this.highestTradedPrice = highestTradedPrice;
	}

	public void setLowestTradedPrice(String lowestTradedPrice) {
		this.lowestTradedPrice = lowestTradedPrice;
	}

	public void setClosingPrice(String closingPrice) {
		this.closingPrice = closingPrice;
	}

	public void setStartTradingDate(String startTradingDate) {
		this.startTradingDate = startTradingDate;
	}

	public void setStopTradingDate(String stopTradingDate) {
		this.stopTradingDate = stopTradingDate;
	}

	public void setStartDeliveryDate(String startDeliveryDate) {
		this.startDeliveryDate = startDeliveryDate;
	}

	public void setStopDeliveryDate(String stopDeliveryDate) {
		this.stopDeliveryDate = stopDeliveryDate;
	}

	@Override
	public String toString() {
		return "Tradable id=" + id + "\n bestBid=" + bestBid + "\n bestAsk="
				+ bestAsk + "\n contracts=" + contracts
				+ "\n highestTradedPrice=" + highestTradedPrice
				+ "\n lowestTradedPrice=" + lowestTradedPrice
				+ "\n closingPrice=" + closingPrice + ", startTradingDate="
				+ startTradingDate + ", stopTradingDate=" + stopTradingDate
				+ "\n startDeliveryDate=" + startDeliveryDate
				+ "\n stopDeliveryDate=" + stopDeliveryDate;
	}
	
	@XmlTransient
	public String getValuesForMac(){
		return id+" "+bestBid+" "+bestAsk+" "+contracts+" "+highestTradedPrice+" "+lowestTradedPrice+" "+closingPrice+" "+
		 startTradingDate+" "+stopTradingDate+" "+startDeliveryDate+" "+stopDeliveryDate;
	}
	
}
