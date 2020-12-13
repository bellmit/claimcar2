package ins.sino.claimcar.manager.vo;

/**
 * Custom VO class of PO PrpdIntermServer
 */ 
public class PrpdIntermServerVo extends PrpdIntermServerVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private String serviceName;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
