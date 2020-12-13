/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import ins.sino.claimcar.subrogation.vo.CheckInfoDataVo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//互审基本信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SubrogationCheckDataVo {
	/** 结算码 **/ 
	@XmlElement(name="AccountsNo", required = true)
	private String recoveryCode; 

	/** 追偿起始时间 **/ 
	@XmlElement(name="RecoverDStart", required = true)
	private String recoverDStart; 

	/** 追偿方保险公司；参见代码 **/ 
	@XmlElement(name="RecoverComCode", required = true)
	private String recoverComCode; 

	/** 追偿方承保地区；参见代码 **/ 
	@XmlElement(name="RecoverAreaCode", required = true)
	private String recoverAreaCode; 

	/** 责任对方保险公司；参见代码 **/ 
	@XmlElement(name="CompensateComCode", required = true)
	private String compensateComCode; 

	/** 责任对承保地区；参见代码 **/ 
	@XmlElement(name="CompensateAreaCode", required = true)
	private String compensateAreaCode; 

	/** 追偿方保险公司报案号 **/ 
	@XmlElement(name="RecoverReportNo", required = true)
	private String recoverReportNo; 

	/** 责任对方报案号 **/ 
	@XmlElement(name="CompensateReportNo", required = true)
	private String compensateReportNo; 

	/** 追偿/清付险种；参见代码 **/ 
	@XmlElement(name="CoverageCode", required = true)
	private String coverageCode; 

	/** 互审状态；参见代码 **/ 
	@XmlElement(name="CheckStats", required = true)
	private String checkStats;

	/*<!--互审信息-->*/
	@XmlElement(name = "CheckInfo")
	//private List<CheckInfoDataVo> checkInfoDataVoList;
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
