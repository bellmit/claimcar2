package ins.sino.claimcar.manager.vo;

import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLFxqFavoreeVo;
import ins.sino.claimcar.claim.vo.PrpLPayFxqCustomVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom VO class of PO PrpLPayCustom
 */
public class PrpLPayCustomVo extends PrpLPayCustomVoBase implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Double sumAmt;
	private String otherFlag;
	private String payRefReason;
	private String serialNo;
	private String publicAndPrivate;//公私标志
	private String summary;//摘要
	private String frostFlag;//是否冻结标识
	private String reason;//冻结原因
	private String authorityPhone;//手机
	private String claimNo;//立案号
	
	private List<PrpLPrePayVo> prpLPrePayVos = new ArrayList<PrpLPrePayVo>();
	private List<PrpLChargeVo> prpLChargeVos = new ArrayList<PrpLChargeVo>();
	private List<PrpLPayFxqCustomVo> prpLPayFxqCustomVos = new ArrayList<PrpLPayFxqCustomVo>();
	private List<PrpLFxqFavoreeVo> prpLFxqFavoreeVos = new ArrayList<PrpLFxqFavoreeVo>();

	public Double getSumAmt() {
		return sumAmt;
	}

	public void setSumAmt(Double sumAmt) {
		this.sumAmt = sumAmt;
	}

	public String getOtherFlag() {
		return otherFlag;
	}

	public void setOtherFlag(String otherFlag) {
		this.otherFlag = otherFlag;
	}

	public String getPayRefReason() {
		return payRefReason;
	}

	public void setPayRefReason(String payRefReason) {
		this.payRefReason = payRefReason;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public List<PrpLPrePayVo> getPrpLPrePayVos() {
		return prpLPrePayVos;
	}

	public void setPrpLPrePayVos(List<PrpLPrePayVo> prpLPrePayVos) {
		this.prpLPrePayVos = prpLPrePayVos;
	}

	public List<PrpLChargeVo> getPrpLChargeVos() {
		return prpLChargeVos;
	}

	public void setPrpLChargeVos(List<PrpLChargeVo> prpLChargeVos) {
		this.prpLChargeVos = prpLChargeVos;
	}
	

	
    public List<PrpLPayFxqCustomVo> getPrpLPayFxqCustomVos() {
        return prpLPayFxqCustomVos;
    }

    
    public void setPrpLPayFxqCustomVos(List<PrpLPayFxqCustomVo> prpLPayFxqCustomVos) {
        this.prpLPayFxqCustomVos = prpLPayFxqCustomVos;
    }

    public String getPublicAndPrivate() {
		return publicAndPrivate;
	}

	public void setPublicAndPrivate(String publicAndPrivate) {
		this.publicAndPrivate = publicAndPrivate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

    
    public String getFrostFlag() {
        return frostFlag;
    }

    
    public void setFrostFlag(String frostFlag) {
        this.frostFlag = frostFlag;
    }

    
    public String getReason() {
        return reason;
    }

    
    public void setReason(String reason) {
        this.reason = reason;
    }

    
    public String getAuthorityPhone() {
        return authorityPhone;
    }

    
    public void setAuthorityPhone(String authorityPhone) {
        this.authorityPhone = authorityPhone;
    }

    
    public List<PrpLFxqFavoreeVo> getPrpLFxqFavoreeVos() {
        return prpLFxqFavoreeVos;
    }

    
    public void setPrpLFxqFavoreeVos(List<PrpLFxqFavoreeVo> prpLFxqFavoreeVos) {
        this.prpLFxqFavoreeVos = prpLFxqFavoreeVos;
    }

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

    



}
