package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * 案件交互查询页面VO
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class SubrogationCheckVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 结算码 **/ 
	private String recoveryCode; 

	/** 追偿起始时间 **/ 
	private String recoverDStart; 

	/** 追偿方保险公司；参见代码 **/ 
	private String recoverComCode; 

	/** 追偿方承保地区；参见代码 **/ 
	private String recoverAreaCode; 

	/** 责任对方保险公司；参见代码 **/ 
	private String compensateComCode; 

	/** 责任对承保地区；参见代码 **/ 
	private String compensateAreaCode; 

	/** 追偿方保险公司报案号 **/ 
	private String recoverReportNo; 

	/** 责任对方报案号 **/ 
	private String compensateReportNo; 

	/** 追偿/清付险种；参见代码 **/ 
	private String coverageCode; 

	/** 互审状态；参见代码 **/ 
	private String checkStats;

	/*<!--互审信息-->*/
	//private String CheckInfo;
	//private List<CheckInfoDataVo> checkInfoDataVoList;
	@XmlElement(name = "CheckInfo")
	private List<CheckInfoDataVo> prpLPlatCheckSubs;
	 

	public String getRecoveryCode() {
		return recoveryCode;
	}

	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}

	public String getRecoverDStart() {
		return recoverDStart;
	}

	public void setRecoverDStart(String recoverDStart) {
		this.recoverDStart = recoverDStart;
	}

	public String getRecoverComCode() {
		return recoverComCode;
	}

	public void setRecoverComCode(String recoverComCode) {
		this.recoverComCode = recoverComCode;
	}

	public String getRecoverAreaCode() {
		return recoverAreaCode;
	}

	public void setRecoverAreaCode(String recoverAreaCode) {
		this.recoverAreaCode = recoverAreaCode;
	}

	public String getCompensateComCode() {
		return compensateComCode;
	}

	public void setCompensateComCode(String compensateComCode) {
		this.compensateComCode = compensateComCode;
	}

	public String getCompensateAreaCode() {
		return compensateAreaCode;
	}

	public void setCompensateAreaCode(String compensateAreaCode) {
		this.compensateAreaCode = compensateAreaCode;
	}

	public String getRecoverReportNo() {
		return recoverReportNo;
	}

	public void setRecoverReportNo(String recoverReportNo) {
		this.recoverReportNo = recoverReportNo;
	}

	public String getCompensateReportNo() {
		return compensateReportNo;
	}

	public void setCompensateReportNo(String compensateReportNo) {
		this.compensateReportNo = compensateReportNo;
	}

	public String getCoverageCode() {
		return coverageCode;
	}

	public void setCoverageCode(String coverageCode) {
		this.coverageCode = coverageCode;
	}

	public String getCheckStats() {
		return checkStats;
	}

	public void setCheckStats(String checkStats) {
		this.checkStats = checkStats;
	}

	public List<CheckInfoDataVo> getPrpLPlatCheckSubs() {
		return prpLPlatCheckSubs;
	}

	public void setPrpLPlatCheckSubs(List<CheckInfoDataVo> prpLPlatCheckSubs) {
		this.prpLPlatCheckSubs = prpLPlatCheckSubs;
	}




	
	

}
