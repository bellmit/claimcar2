package ins.sino.claimcar.manager.vo;

import java.io.Serializable;

public class PrpdcheckServerVo extends PrpdcheckServerVoBase implements Serializable{

	private static final long serialVersionUID = 1L; 
	private String serviceName;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
