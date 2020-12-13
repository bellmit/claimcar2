package ins.sino.claimcar.trafficplatform.vo;

import java.math.BigDecimal;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 山东预警-理算核赔登记-AdjustmentData
 * <pre></pre>
 * @author ★WeiLanlei
 */
@XStreamAlias("AdjustmentData")
public class EWVClaimAdjustmentDataVo {
	
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("AdjustmentCode")
	private String adjustmentCode;// 理算编码

	@XStreamAlias("UnderWriteDesc")
	private String underWriteDesc;// 核赔意见
	
	@XStreamAlias("ClaimAmount")
	private BigDecimal claimAmount;// 赔偿金额(含施救费)

	@XStreamAlias("UnderWriteEndTime")
	private String underWriteEndTime;// 理算核赔通过时间；精确到分

	@XStreamImplicit(itemFieldName="ClaimCoverData")
	private List<EWVClaimClaimCoverDataVo> claimCoverData;// 损失赔偿情况列表

	
	public String getAdjustmentCode() {
		return adjustmentCode;
	}

	
	public void setAdjustmentCode(String adjustmentCode) {
		this.adjustmentCode = adjustmentCode;
	}

	
	public String getUnderWriteDesc() {
		return underWriteDesc;
	}

	
	public void setUnderWriteDesc(String underWriteDesc) {
		this.underWriteDesc = underWriteDesc;
	}

	
	public BigDecimal getClaimAmount() {
		return claimAmount;
	}

	
	public void setClaimAmount(BigDecimal claimAmount) {
		this.claimAmount = claimAmount;
	}

	
	public String getUnderWriteEndTime() {
		return underWriteEndTime;
	}

	
	public void setUnderWriteEndTime(String underWriteEndTime) {
		this.underWriteEndTime = underWriteEndTime;
	}

	
	public List<EWVClaimClaimCoverDataVo> getClaimCoverData() {
		return claimCoverData;
	}

	
	public void setClaimCoverData(List<EWVClaimClaimCoverDataVo> claimCoverData) {
		this.claimCoverData = claimCoverData;
	}

	
	
	

	
}
