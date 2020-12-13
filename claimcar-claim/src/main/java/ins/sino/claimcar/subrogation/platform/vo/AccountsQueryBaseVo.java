/******************************************************************************
* CREATETIME : 2016年4月1日 上午9:27:06
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * @author ★YangKun
 * @CreateTime 2016年4月1日
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class AccountsQueryBaseVo {

	@XmlElement(name = "AccountsNo")
	private String accountsNo;//结算码

	@XmlElement(name = "AccountsNoStatus", required = true)
	private String accountsNoStatus;//结算码状态；参见代码

	@XmlElement(name = "AcrossProvinceFlag", required = true)
	private String acrossProvinceFlag;//跨省标志代码；参见代码

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "AccountDateStart")
	private Date accountDateStart;//结算起始时间起

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "AccountDateEnd")
	private Date accountDateEnd;//结算起始时间止

	@XmlElement(name = "OppoentCompanyCode")
	private String oppoentCompanyCode;//对方保险公司；参见代码

	@XmlElement(name = "OppoentAreaCode")
	private String oppoentAreaCode;//对方承保地区；参见代码

	@XmlElement(name = "CoverageCode")
	private String coverageCode;//追偿/清付险种；参见代码

	@XmlElement(name = "RecoverStatus")
	private String recoverStatus;//本方追偿状态；参见代码

	@XmlElement(name = "RecoverAmountStart")
	private String recoverAmountStart;//追偿金额区间起

	@XmlElement(name = "RecoverAmountEnd")
	private String recoverAmountEnd;//追偿金额区间止

	@XmlElement(name = "CompAmountStart")
	private String compAmountStart;//清付金额区间起

	@XmlElement(name = "CompAmountEnd")
	private String compAmountEnd;//清付金额区间止


	/** 
	 * @return 返回 accountsNo  结算码
	 */ 
	public String getAccountsNo(){ 
	    return accountsNo;
	}

	/** 
	 * @param accountsNo 要设置的 结算码
	 */ 
	public void setAccountsNo(String accountsNo){ 
	    this.accountsNo=accountsNo;
	}

	/** 
	 * @return 返回 accountsNoStatus  结算码状态；参见代码
	 */ 
	public String getAccountsNoStatus(){ 
	    return accountsNoStatus;
	}

	/** 
	 * @param accountsNoStatus 要设置的 结算码状态；参见代码
	 */ 
	public void setAccountsNoStatus(String accountsNoStatus){ 
	    this.accountsNoStatus=accountsNoStatus;
	}

	/** 
	 * @return 返回 acrossProvinceFlag  跨省标志代码；参见代码
	 */ 
	public String getAcrossProvinceFlag(){ 
	    return acrossProvinceFlag;
	}

	/** 
	 * @param acrossProvinceFlag 要设置的 跨省标志代码；参见代码
	 */ 
	public void setAcrossProvinceFlag(String acrossProvinceFlag){ 
	    this.acrossProvinceFlag=acrossProvinceFlag;
	}

	/** 
	 * @return 返回 accountDateStart  结算起始时间起
	 */ 
	public Date getAccountDateStart(){ 
	    return accountDateStart;
	}

	/** 
	 * @param accountDateStart 要设置的 结算起始时间起
	 */ 
	public void setAccountDateStart(Date accountDateStart){ 
	    this.accountDateStart=accountDateStart;
	}

	/** 
	 * @return 返回 accountDateEnd  结算起始时间止
	 */ 
	public Date getAccountDateEnd(){ 
	    return accountDateEnd;
	}

	/** 
	 * @param accountDateEnd 要设置的 结算起始时间止
	 */ 
	public void setAccountDateEnd(Date accountDateEnd){ 
	    this.accountDateEnd=accountDateEnd;
	}

	/** 
	 * @return 返回 oppoentCompanyCode  对方保险公司；参见代码
	 */ 
	public String getOppoentCompanyCode(){ 
	    return oppoentCompanyCode;
	}

	/** 
	 * @param oppoentCompanyCode 要设置的 对方保险公司；参见代码
	 */ 
	public void setOppoentCompanyCode(String oppoentCompanyCode){ 
	    this.oppoentCompanyCode=oppoentCompanyCode;
	}

	/** 
	 * @return 返回 oppoentAreaCode  对方承保地区；参见代码
	 */ 
	public String getOppoentAreaCode(){ 
	    return oppoentAreaCode;
	}

	/** 
	 * @param oppoentAreaCode 要设置的 对方承保地区；参见代码
	 */ 
	public void setOppoentAreaCode(String oppoentAreaCode){ 
	    this.oppoentAreaCode=oppoentAreaCode;
	}

	/** 
	 * @return 返回 coverageCode  追偿/清付险种；参见代码
	 */ 
	public String getCoverageCode(){ 
	    return coverageCode;
	}

	/** 
	 * @param coverageCode 要设置的 追偿/清付险种；参见代码
	 */ 
	public void setCoverageCode(String coverageCode){ 
	    this.coverageCode=coverageCode;
	}

	/** 
	 * @return 返回 recoverStatus  本方追偿状态；参见代码
	 */ 
	public String getRecoverStatus(){ 
	    return recoverStatus;
	}

	/** 
	 * @param recoverStatus 要设置的 本方追偿状态；参见代码
	 */ 
	public void setRecoverStatus(String recoverStatus){ 
	    this.recoverStatus=recoverStatus;
	}

	/** 
	 * @return 返回 recoverAmountStart  追偿金额区间起
	 */ 
	public String getRecoverAmountStart(){ 
	    return recoverAmountStart;
	}

	/** 
	 * @param recoverAmountStart 要设置的 追偿金额区间起
	 */ 
	public void setRecoverAmountStart(String recoverAmountStart){ 
	    this.recoverAmountStart=recoverAmountStart;
	}

	/** 
	 * @return 返回 recoverAmountEnd  追偿金额区间止
	 */ 
	public String getRecoverAmountEnd(){ 
	    return recoverAmountEnd;
	}

	/** 
	 * @param recoverAmountEnd 要设置的 追偿金额区间止
	 */ 
	public void setRecoverAmountEnd(String recoverAmountEnd){ 
	    this.recoverAmountEnd=recoverAmountEnd;
	}

	/** 
	 * @return 返回 compAmountStart  清付金额区间起
	 */ 
	public String getCompAmountStart(){ 
	    return compAmountStart;
	}

	/** 
	 * @param compAmountStart 要设置的 清付金额区间起
	 */ 
	public void setCompAmountStart(String compAmountStart){ 
	    this.compAmountStart=compAmountStart;
	}

	/** 
	 * @return 返回 compAmountEnd  清付金额区间止
	 */ 
	public String getCompAmountEnd(){ 
	    return compAmountEnd;
	}

	/** 
	 * @param compAmountEnd 要设置的 清付金额区间止
	 */ 
	public void setCompAmountEnd(String compAmountEnd){ 
	    this.compAmountEnd=compAmountEnd;
	}



}
