package ee.enk.rest.jersey.auth;

import java.io.Serializable;
import java.util.Date;

public class Session implements Serializable {

	private static final long serialVersionUID = -361227042214862545L;

	private String sessionId;   
	private String userId;      
	private boolean active;     
	private boolean secure;    

	private Date createTime;    
	private Date lastAccessedTime;  
	
	public Session(String sessionId, String userId){
		this.sessionId = sessionId;
		this.userId = userId;
		this.active = true;
		this.secure = true;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isSecure() {
		return secure;
	}
	public void setSecure(boolean secure) {
		this.secure = secure;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastAccessedTime() {
		return lastAccessedTime;
	}
	public void setLastAccessedTime(Date lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}


