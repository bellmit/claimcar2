/******************************************************************************
 * CREATETIME : 2016年6月1日 上午11:05:08
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * 追加损失赔偿情况（多条）
 * 
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIEndCaseAddReqClaimCoverListVo {

	/**
	 * 损失赔偿情况明细（多条）DETAIL_LIST（隶属于损失赔偿情况）
	 */
	@XmlElementWrapper(name = "DETAIL_LIST")
	@XmlElement(name = "DETAIL_DATA")
	private List<SHCIEndCaseAddReqDetailListVo> detailListVo;

	@XmlElement(name = "POLICY_NO")
	private String policyNo;// 保单号

	@XmlElement(name = "LIABILITY_RATE")
	private int liabilityRate;// 赔偿责任比例

	@XmlElement(name = "CLAIM_FEE_TYPE", required = true)
	private String claimFeeType;// 损失赔偿类型

	@XmlElement(name = "COVERAGE_TYPE")
	private String coverageType;// 赔偿险种类型

	@XmlElement(name = "COVERAGE_CODE", required = true)
	private String coverageCode;// 赔偿平台险种代码

	@XmlElement(name = "COM_COVERAGE_CODE")
	private String comCoverageCode;// 赔偿公司险种代码

	@XmlElement(name = "LOSS_AMOUNT", required = true)
	private Double lossAmount;// 追加估损金额未决估计赔款

	@XmlElement(name = "CLAIM_AMOUNT", required = true)
	private Double claimAmount;// 赔款金额

	@XmlElement(name = "SalvageFee", required = true)
	private Double SalvageFee;// 追加施救费
	
	@XmlElement(name = "IsDeviceItem")
	private String IsDeviceItem;//是否新增设备

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public int getLiabilityRate() {
		return liabilityRate;
	}

	public void setLiabilityRate(int liabilityRate) {
		this.liabilityRate = liabilityRate;
	}

	public String getClaimFeeType() {
		return claimFeeType;
	}

	public void setClaimFeeType(String claimFeeType) {
		this.claimFeeType = claimFeeType;
	}

	public String getCoverageType() {
		return coverageType;
	}

	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}

	public String getCoverageCode() {
		return coverageCode;
	}

	public void setCoverageCode(String coverageCode) {
		this.coverageCode = coverageCode;
	}

	public String getComCoverageCode() {
		return comCoverageCode;
	}

	public void setComCoverageCode(String comCoverageCode) {
		this.comCoverageCode = comCoverageCode;
	}
	
	public Double getLossAmount() {
		return lossAmount;
	}

	public void setLossAmount(Double lossAmount) {
		this.lossAmount = lossAmount;
	}

	public Double getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	public Double getSalvageFee() {
		return SalvageFee;
	}

	public void setSalvageFee(Double salvageFee) {
		SalvageFee = salvageFee;
	}

	public List<SHCIEndCaseAddReqDetailListVo> getDetailListVo() {
		return detailListVo;
	}

	public void setDetailListVo(List<SHCIEndCaseAddReqDetailListVo> detailListVo) {
		this.detailListVo = detailListVo;
	}

	public String getIsDeviceItem() {
		return IsDeviceItem;
	}

	public void setIsDeviceItem(String isDeviceItem) {
		IsDeviceItem = isDeviceItem;
	}
	
	

}
