/******************************************************************************
 * CREATETIME : 2016年5月26日 下午5:31:24
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * --
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCILossResClaimCoverDataVo {

	/**
	 * 
	 */
	@XmlElementWrapper(name = "DETAIL_LIST")
	@XmlElement(name = "DETAIL_DATA")
	private List<SHCILossResDetailDataVo> detailDataVo;

	@XmlElement(name = "POLICY_NO")
	private String policyNo;

	@XmlElement(name = "LIABILITY_RATE")
	private String liabilityRate;

	@XmlElement(name = "CLAIM_FEE_TYPE")
	private String claimFeeType;

	@XmlElement(name = "COVERAGE_TYPE")
	private String coverageType;

	@XmlElement(name = "COVERAGE_CODE")
	private String coverageCode;

	@XmlElement(name = "LOSS_AMOUNT")
	private Double lossAmount;

	@XmlElement(name = "CLAIM_AMOUNT")
	private Double claimAmount;
	

	public List<SHCILossResDetailDataVo> getDetailDataVo() {
		return detailDataVo;
	}

	public void setDetailDataVo(List<SHCILossResDetailDataVo> detailDataVo) {
		this.detailDataVo = detailDataVo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getLiabilityRate() {
		return liabilityRate;
	}

	public void setLiabilityRate(String liabilityRate) {
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

}
