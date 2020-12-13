/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

//结案信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiSubrogationClaimCloseDataVo {
	@XmlElement(name = "AccidentType", required = true)
	private String accidentType;//保险事故分类；参见代码

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "ClaimCloseTime", required = true)
	private Date claimCloseTime;//结案时间；精确到分

	@XmlElement(name = "ClaimAmount", required = true)
	private Double claimAmount;//赔款总金额

	@XmlElement(name = "OtherFee")
	private Double otherFee;//其他费用

	@XmlElement(name = "DirectClaimAmount")
	private Double directClaimAmount;//直接理赔费用总金额

	/**	损失赔偿情况信息列表 */
	@XmlElement(name = "ClaimCloseCoverData")
	private List<BiClaimCloseOverDataVo> claimCloseOverDataList;
	
	/**	追偿/清付信息列表 */
	@XmlElement(name = "CloseRecoveryOrPayData")
	private  List<BiClaimCloseRecoveryOrPayDataVo> closeRecoveryOrPayDataList;

	/** 
	 * @return 返回 accidentType  保险事故分类；参见代码
	 */ 
	public String getAccidentType(){ 
	    return accidentType;
	}

	/** 
	 * @param accidentType 要设置的 保险事故分类；参见代码
	 */ 
	public void setAccidentType(String accidentType){ 
	    this.accidentType=accidentType;
	}

	/** 
	 * @return 返回 claimCloseTime  结案时间；精确到分
	 */ 
	public Date getClaimCloseTime(){ 
	    return claimCloseTime;
	}

	/** 
	 * @param claimCloseTime 要设置的 结案时间；精确到分
	 */ 
	public void setClaimCloseTime(Date claimCloseTime){ 
	    this.claimCloseTime=claimCloseTime;
	}

	/** 
	 * @return 返回 claimAmount  赔款总金额
	 */ 
	public Double getClaimAmount(){ 
	    return claimAmount;
	}

	/** 
	 * @param claimAmount 要设置的 赔款总金额
	 */ 
	public void setClaimAmount(Double claimAmount){ 
	    this.claimAmount=claimAmount;
	}

	/** 
	 * @return 返回 otherFee  其他费用
	 */ 
	public Double getOtherFee(){ 
	    return otherFee;
	}

	/** 
	 * @param otherFee 要设置的 其他费用
	 */ 
	public void setOtherFee(Double otherFee){ 
	    this.otherFee=otherFee;
	}

	/** 
	 * @return 返回 directClaimAmount  直接理赔费用总金额
	 */ 
	public Double getDirectClaimAmount(){ 
	    return directClaimAmount;
	}

	/** 
	 * @param directClaimAmount 要设置的 直接理赔费用总金额
	 */ 
	public void setDirectClaimAmount(Double directClaimAmount){ 
	    this.directClaimAmount=directClaimAmount;
	}



	public List<BiClaimCloseOverDataVo> getClaimCloseOverDataList() {
		return claimCloseOverDataList;
	}

	public void setClaimCloseOverDataList(
			List<BiClaimCloseOverDataVo> claimCloseOverDataList) {
		this.claimCloseOverDataList = claimCloseOverDataList;
	}

	public List<BiClaimCloseRecoveryOrPayDataVo> getCloseRecoveryOrPayDataList() {
		return closeRecoveryOrPayDataList;
	}

	public void setCloseRecoveryOrPayDataList(
			List<BiClaimCloseRecoveryOrPayDataVo> closeRecoveryOrPayDataList) {
		this.closeRecoveryOrPayDataList = closeRecoveryOrPayDataList;
	}


}
