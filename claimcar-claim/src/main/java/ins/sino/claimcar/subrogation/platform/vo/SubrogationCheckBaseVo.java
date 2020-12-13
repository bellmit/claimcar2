/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:19:11
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.Date;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

//互审信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SubrogationCheckBaseVo {
	/** 结算码 **/ 
	@XmlElement(name="AccountsNo")
	private String accountsNo; 

	/** 互审状态；参见代码 **/ 
	@XmlElement(name="CheckStats")
	private String checkStats; 

	/** 审核方类型；参见代码 **/ 
	@XmlElement(name="CheckOwnType", required = true)
	private String checkOwnType; 

	/** 追偿起始时间起 **/ 
	@XmlElement(name="RecoverDStart")
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date recoverDStart; 

	/** 追偿起始时间至 **/ 
	@XmlElement(name="RecoverDEnd")
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date recoverDEnd;

	public String getAccountsNo() {
		return accountsNo;
	}

	public void setAccountsNo(String accountsNo) {
		this.accountsNo = accountsNo;
	}

	public String getCheckStats() {
		return checkStats;
	}

	public void setCheckStats(String checkStats) {
		this.checkStats = checkStats;
	}

	public String getCheckOwnType() {
		return checkOwnType;
	}

	public void setCheckOwnType(String checkOwnType) {
		this.checkOwnType = checkOwnType;
	}

	public Date getRecoverDStart() {
		return recoverDStart;
	}

	public void setRecoverDStart(Date recoverDStart) {
		this.recoverDStart = recoverDStart;
	}

	public Date getRecoverDEnd() {
		return recoverDEnd;
	}

	public void setRecoverDEnd(Date recoverDEnd) {
		this.recoverDEnd = recoverDEnd;
	} 



	
}
