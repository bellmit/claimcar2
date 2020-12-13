/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.Date;
import java.util.List;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

//结案信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiSubrogationClaimCloseDataVo {
	/** 保险事故分类；参见代码 **/ 
	@XmlElement(name="ACCIDENT_TYPE", required = true)
	private String accidentType; 

	/** 结案时间；格式YYYYMMDDHHMM **/ 
	@XmlElement(name="ENDCASE_DATE", required = true)
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date endcaseDate; 

	/** 赔款总金额 **/ 
	@XmlElement(name="CLAIM_AMOUNT", required = true)
	private Double claimAmount; 

	/** 其他费用 **/ 
	@XmlElement(name="OTHER_FEE")
	private Double otherFee;
	/**	损失赔偿情况信息列表 */
	@XmlElementWrapper(name = "CLAIM_CLOSE_COVER_LIST")
	@XmlElement(name = "CLAIM_CLOSE_COVER_DATA")
	private List<CiClaimCloseOverDataVo> claimCloseOverDataList;
	
	/**	追偿/清付信息列表 */
	@XmlElementWrapper(name = "CLOSE_RECOVERY_OR_PAY_LIST")
	@XmlElement(name = "CLOSE_RECOVERY_OR_PAY_DATA")
	private  List<CiClaimCloseRecoveryOrPayDataVo> closeRecoveryOrPayDataList;


	
	public String getAccidentType() {
		return accidentType;
	}

	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}

	public Date getEndcaseDate() {
		return endcaseDate;
	}

	public void setEndcaseDate(Date endcaseDate) {
		this.endcaseDate = endcaseDate;
	}

	public Double getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	public Double getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}

	
	public List<CiClaimCloseOverDataVo> getClaimCloseOverDataList() {
		return claimCloseOverDataList;
	}

	
	public void setClaimCloseOverDataList(List<CiClaimCloseOverDataVo> claimCloseOverDataList) {
		this.claimCloseOverDataList = claimCloseOverDataList;
	}

	
	public List<CiClaimCloseRecoveryOrPayDataVo> getCloseRecoveryOrPayDataList() {
		return closeRecoveryOrPayDataList;
	}

	
	public void setCloseRecoveryOrPayDataList(List<CiClaimCloseRecoveryOrPayDataVo> closeRecoveryOrPayDataList) {
		this.closeRecoveryOrPayDataList = closeRecoveryOrPayDataList;
	}

	

}
