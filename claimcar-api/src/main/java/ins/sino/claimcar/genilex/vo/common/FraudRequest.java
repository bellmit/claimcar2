package ins.sino.claimcar.genilex.vo.common;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("FraudRequest")
public class FraudRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ProductRequestId") 
	private String  productRequestId;
	
	@XStreamAlias("ReportNo") 
	private String  reportNo;

	@XStreamAlias("ScoreType") 
	private String  scoredType;

	@XStreamAlias("ServiceType") 
	private String  serviceType;

	public String getProductRequestId() {
		return productRequestId;
	}

	public void setProductRequestId(String productRequestId) {
		this.productRequestId = productRequestId;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getScoredType() {
		return scoredType;
	}

	public void setScoredType(String scoredType) {
		this.scoredType = scoredType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

}
