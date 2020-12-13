package ins.sino.claimcar.losscar.vo;

import java.io.Serializable;

public class SendVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String serverName;
	private String serverPort;
	private String contextPath;
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
	
	
}
