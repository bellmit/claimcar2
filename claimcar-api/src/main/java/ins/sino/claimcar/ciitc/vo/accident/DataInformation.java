package ins.sino.claimcar.ciitc.vo.accident;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("DataInformation")
public class DataInformation implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("acciNo")
	private String acciNo;
	
	@XStreamAlias("acciAreaCode")
	private String acciAreaCode;
	
	@XStreamAlias("reportStartDate")
	private String reportStartDate;
	
	@XStreamAlias("reportEndDate")
	private String reportEndDate;

	public String getAcciNo() {
		return acciNo;
	}

	public void setAcciNo(String acciNo) {
		this.acciNo = acciNo;
	}

	public String getAcciAreaCode() {
		return acciAreaCode;
	}

	public void setAcciAreaCode(String acciAreaCode) {
		this.acciAreaCode = acciAreaCode;
	}

	public String getReportStartDate() {
		return reportStartDate;
	}

	public void setReportStartDate(String reportStartDate) {
		this.reportStartDate = reportStartDate;
	}

	public String getReportEndDate() {
		return reportEndDate;
	}

	public void setReportEndDate(String reportEndDate) {
		this.reportEndDate = reportEndDate;
	}
	
	
}
