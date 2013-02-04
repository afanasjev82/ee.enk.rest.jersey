package ee.enk.rest.jersey.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
public class TradableErrorBean {
	private String errorMsg;
    private int errorCode;
    
	public TradableErrorBean() {}
    public TradableErrorBean(String errorMsg, int errorCode) {
		this.errorMsg = errorMsg;
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}