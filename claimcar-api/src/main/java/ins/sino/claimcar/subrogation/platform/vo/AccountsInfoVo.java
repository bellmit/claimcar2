/******************************************************************************
* CREATETIME : 2016年4月1日 上午9:34:30
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.io.Serializable;
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
public class AccountsInfoVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "AccountsNo", required = true)
	private String accountsNo;//结算码

	@XmlElement(name = "AccountsNoStatus", required = true)
	private String accountsNoStatus;//结算码状态；参见代码

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "AccountsStartDate", required = true)
	private Date accountsStartDate;//结算起始时间

	@XmlElement(name = "RecoverCompanyCode", required = true)
	private String recoverCompanyCode;//追偿方保险公司；参见代码

	@XmlElement(name = "RecoverAreaCode", required = true)
	private String recoverAreaCode;//追偿方承保地区；参见代码

	@XmlElement(name = "CompensateComCode", required = true)
	private String compensateComCode;//责任对方保险公司；参见代码

	@XmlElement(name = "CompensationAreaCode", required = true)
	private String compensationAreaCode;//责任对方承保地区；参见代码

	@XmlElement(name = "CoverageCode", required = true)
	private String coverageCode;//追偿/清付险种；参见代码

	@XmlElement(name = "RecoverAmount", required = true)
	private Double recoverAmount;//追偿金额

	@XmlElement(name = "CompensateAmount", required = true)
	private Double compensateAmount;//原始清付金额

	@XmlElement(name = "LastCompensateAmount", required = true)
	private Double lastCompensateAmount;//最新清付金额

	@XmlElement(name = "AccountAmount", required = true)
	private Double accountAmount;//结算金额


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
	 * @return 返回 accountsStartDate  结算起始时间
	 */ 
	public Date getAccountsStartDate(){ 
	    return accountsStartDate;
	}

	/** 
	 * @param accountsStartDate 要设置的 结算起始时间
	 */ 
	public void setAccountsStartDate(Date accountsStartDate){ 
	    this.accountsStartDate=accountsStartDate;
	}

	/** 
	 * @return 返回 recoverCompanyCode  追偿方保险公司；参见代码
	 */ 
	public String getRecoverCompanyCode(){ 
	    return recoverCompanyCode;
	}

	/** 
	 * @param recoverCompanyCode 要设置的 追偿方保险公司；参见代码
	 */ 
	public void setRecoverCompanyCode(String recoverCompanyCode){ 
	    this.recoverCompanyCode=recoverCompanyCode;
	}

	/** 
	 * @return 返回 recoverAreaCode  追偿方承保地区；参见代码
	 */ 
	public String getRecoverAreaCode(){ 
	    return recoverAreaCode;
	}

	/** 
	 * @param recoverAreaCode 要设置的 追偿方承保地区；参见代码
	 */ 
	public void setRecoverAreaCode(String recoverAreaCode){ 
	    this.recoverAreaCode=recoverAreaCode;
	}

	/** 
	 * @return 返回 compensateComCode  责任对方保险公司；参见代码
	 */ 
	public String getCompensateComCode(){ 
	    return compensateComCode;
	}

	/** 
	 * @param compensateComCode 要设置的 责任对方保险公司；参见代码
	 */ 
	public void setCompensateComCode(String compensateComCode){ 
	    this.compensateComCode=compensateComCode;
	}

	/** 
	 * @return 返回 compensationAreaCode  责任对方承保地区；参见代码
	 */ 
	public String getCompensationAreaCode(){ 
	    return compensationAreaCode;
	}

	/** 
	 * @param compensationAreaCode 要设置的 责任对方承保地区；参见代码
	 */ 
	public void setCompensationAreaCode(String compensationAreaCode){ 
	    this.compensationAreaCode=compensationAreaCode;
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
	 * @return 返回 recoverAmount  追偿金额
	 */ 
	public Double getRecoverAmount(){ 
	    return recoverAmount;
	}

	/** 
	 * @param recoverAmount 要设置的 追偿金额
	 */ 
	public void setRecoverAmount(Double recoverAmount){ 
	    this.recoverAmount=recoverAmount;
	}

	/** 
	 * @return 返回 compensateAmount  原始清付金额
	 */ 
	public Double getCompensateAmount(){ 
	    return compensateAmount;
	}

	/** 
	 * @param compensateAmount 要设置的 原始清付金额
	 */ 
	public void setCompensateAmount(Double compensateAmount){ 
	    this.compensateAmount=compensateAmount;
	}

	/** 
	 * @return 返回 lastCompensateAmount  最新清付金额
	 */ 
	public Double getLastCompensateAmount(){ 
	    return lastCompensateAmount;
	}

	/** 
	 * @param lastCompensateAmount 要设置的 最新清付金额
	 */ 
	public void setLastCompensateAmount(Double lastCompensateAmount){ 
	    this.lastCompensateAmount=lastCompensateAmount;
	}

	/** 
	 * @return 返回 accountAmount  结算金额
	 */ 
	public Double getAccountAmount(){ 
	    return accountAmount;
	}

	/** 
	 * @param accountAmount 要设置的 结算金额
	 */ 
	public void setAccountAmount(Double accountAmount){ 
	    this.accountAmount=accountAmount;
	}



}
