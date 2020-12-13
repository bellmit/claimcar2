package ins.sino.claimcar.realtimequery.vo;

import java.io.Serializable;
import java.util.Date;

public class PrpLRealTimeQueryVoBase implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String usAge;  //查询剩余次数
	private String reportNo;
	private String disType;
	private Date changeTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsAge() {
		return usAge;
	}
	public void setUsAge(String usAge) {
		this.usAge = usAge;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getDisType() {
		return disType;
	}
	public void setDisType(String disType) {
		this.disType = disType;
	}
	public Date getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}
	
	
}
