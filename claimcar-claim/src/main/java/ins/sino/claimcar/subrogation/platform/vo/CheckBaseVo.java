/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:19:11
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckBaseVo {

	/** 结算码 **/ 
	@XmlElement(name="AccountsNo", required = true)
	private String accountsNo; 

	/** 互审意见 **/ 
	@XmlElement(name="CheckOpinion")
	private String checkOpinion; 

	/** 互审状态 **/ 
	@XmlElement(name="CheckStats", required = true)
	private String checkStats;

	public String getAccountsNo() {
		return accountsNo;
	}

	public void setAccountsNo(String accountsNo) {
		this.accountsNo = accountsNo;
	}

	public String getCheckOpinion() {
		return checkOpinion;
	}

	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}

	public String getCheckStats() {
		return checkStats;
	}

	public void setCheckStats(String checkStats) {
		this.checkStats = checkStats;
	} 


}
