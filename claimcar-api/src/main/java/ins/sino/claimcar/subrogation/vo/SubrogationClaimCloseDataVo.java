/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

//结案信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
public class SubrogationClaimCloseDataVo implements Serializable{
	private static final long serialVersionUID = 1L;


	private Double directClaimAmount;//直接理赔费用总金额
	/** 保险事故分类；参见代码 **/ 
	private String accidentType; 

	/** 结案时间；格式YYYYMMDDHHMM **/ 
	private Date endcaseDate; 

	/** 赔款总金额 **/ 
	private Double claimAmount; 

	/** 其他费用 **/ 
	private Double otherFee;
	/**	损失赔信息列表偿情况 */
	private List<ClaimCloseOverDataVo> claimCloseOverDataList;
	
	/**	追偿/清付信息列表 */
	private  List<ClaimCloseRecoveryOrPayDataVo> closeRecoveryOrPayDataList;

	private Date claimCloseTime;//结案时间；精确到分

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

	public List<ClaimCloseOverDataVo> getClaimCloseOverDataList() {
		return claimCloseOverDataList;
	}

	public void setClaimCloseOverDataList(
			List<ClaimCloseOverDataVo> claimCloseOverDataList) {
		this.claimCloseOverDataList = claimCloseOverDataList;
	}

	public List<ClaimCloseRecoveryOrPayDataVo> getCloseRecoveryOrPayDataList() {
		return closeRecoveryOrPayDataList;
	}

	public void setCloseRecoveryOrPayDataList(
			List<ClaimCloseRecoveryOrPayDataVo> closeRecoveryOrPayDataList) {
		this.closeRecoveryOrPayDataList = closeRecoveryOrPayDataList;
	}

	public Double getDirectClaimAmount() {
		return directClaimAmount;
	}

	public void setDirectClaimAmount(Double directClaimAmount) {
		this.directClaimAmount = directClaimAmount;
	}

	public Date getClaimCloseTime() {
		return claimCloseTime;
	}

	public void setClaimCloseTime(Date claimCloseTime) {
		this.claimCloseTime = claimCloseTime;
	}

	
}
