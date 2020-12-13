package ins.sino.claimcar.lossperson.vo;

import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeDetailVo;

import java.util.List;

/**
 * Custom VO class of PO PrpLDlossPersTraceFee
 */ 
public class PrpLDlossPersTraceFeeVo extends PrpLDlossPersTraceFeeVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private List<PrpLLossPersonFeeDetailVo> PrpLLossPersonFeeDetails;

	public List<PrpLLossPersonFeeDetailVo> getPrpLLossPersonFeeDetails() {
		return PrpLLossPersonFeeDetails;
	}

	public void setPrpLLossPersonFeeDetails(
			List<PrpLLossPersonFeeDetailVo> prpLLossPersonFeeDetails) {
		PrpLLossPersonFeeDetails = prpLLossPersonFeeDetails;
	}
}
