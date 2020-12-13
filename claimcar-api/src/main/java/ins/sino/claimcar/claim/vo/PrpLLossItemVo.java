package ins.sino.claimcar.claim.vo;

import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Custom VO class of PO PrpLLossItem
 */ 
public class PrpLLossItemVo extends PrpLLossItemVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	//损失类型
	private String lossFeeType;
	
	//无责代赔备份金额
	private BigDecimal absolvePayAmts;
	private List<PrpLPlatLockVo> platLockList;
	private List<PrpLSubrogationPersonVo> subrogationPersonVoList;//非机动车代位信息

    private BigDecimal originLossFee;  //保存初始化的 定损金额 发票金额 小值
	public String getLossFeeType() {
		return lossFeeType;
	}

	public void setLossFeeType(String lossFeeType) {
		this.lossFeeType = lossFeeType;
	}

	public List<PrpLPlatLockVo> getPlatLockList() {
		return platLockList;
	}

	public void setPlatLockList(List<PrpLPlatLockVo> platLockList) {
		this.platLockList = platLockList;
	}

	public BigDecimal getAbsolvePayAmts() {
		return absolvePayAmts;
	}

	public void setAbsolvePayAmts(BigDecimal absolvePayAmts) {
		this.absolvePayAmts = absolvePayAmts;
	}

	public BigDecimal getOriginLossFee() {
		return originLossFee;
	}

	public void setOriginLossFee(BigDecimal originLossFee) {
		this.originLossFee = originLossFee;
	}

	public List<PrpLSubrogationPersonVo> getSubrogationPersonVoList() {
		return subrogationPersonVoList;
	}

	public void setSubrogationPersonVoList(List<PrpLSubrogationPersonVo> subrogationPersonVoList) {
		this.subrogationPersonVoList = subrogationPersonVoList;
	}
	
	
	
}
