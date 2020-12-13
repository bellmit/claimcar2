package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;
import java.sql.Date;

public class PolicyLinkQueryVo implements Serializable {

	/**
	 * niuqiang  
	 *   */
	private static final long serialVersionUID = 1L;
	
	private String respUserName;
	private String respUserPhone;
	private String driverName;
	private String hphm;
	private Date accidentTimeStart;
	private Date accidentTimeEnd;
	
	// 分页
	private Integer start;// 记录起始位置
	private Integer length;// 记录数

	
	
	public String getRespUserName() {
		return respUserName;
	}
	public void setRespUserName(String respUserName) {
		this.respUserName = respUserName;
	}
	public String getRespUserPhone() {
		return respUserPhone;
	}
	public void setRespUserPhone(String respUserPhone) {
		this.respUserPhone = respUserPhone;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getHphm() {
		return hphm;
	}
	public void setHphm(String hphm) {
		this.hphm = hphm;
	}
	public Date getAccidentTimeStart() {
		return accidentTimeStart;
	}
	public void setAccidentTimeStart(Date accidentTimeStart) {
		this.accidentTimeStart = accidentTimeStart;
	}
	public Date getAccidentTimeEnd() {
		return accidentTimeEnd;
	}
	public void setAccidentTimeEnd(Date accidentTimeEnd) {
		this.accidentTimeEnd = accidentTimeEnd;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}

	

}
