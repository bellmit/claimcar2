package ins.sino.claimcar.regist.vo;

/**
 * Custom VO class of PO PrpLCMain
 */ 
public class PrpLCMainVo extends PrpLCMainVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private Double sumDisCountPremium;// 折扣保费合计

	private Double sumItemAmount;// 条款保额之和

	private Double sumBenchmarkPremium;// 条款基准保费之和

	private Double sumDisCountPremiumBI;// 商业险折扣保费合计
	
	private Double sumItemPremium;// 条款实际保费只和 
	
	private String pingendorseNo;//拼接批单号
	
	private String memberFlag = "0";  //是否为需要识别的会员  0-不是，1-是
	
	private Integer startMinute;//起保分钟
	
	private Integer endMinute;//终保分钟
	
	private String claimSequenceNo;//理赔编号
	
    private String claimType;//案件类型
    
    private String remarks;//备注
    
    private String serviceMobile;//修理厂手机号码
    
	public String getMemberFlag() {
		return memberFlag;
	}

	public void setMemberFlag(String memberFlag) {
		this.memberFlag = memberFlag;
	}

	public Double getSumDisCountPremium() {
		return sumDisCountPremium;
	}

	public void setSumDisCountPremium(Double sumDisCountPremium) {
		this.sumDisCountPremium = sumDisCountPremium;
	}

	public Double getSumItemAmount() {
		return sumItemAmount;
	}

	public void setSumItemAmount(Double sumItemAmount) {
		this.sumItemAmount = sumItemAmount;
	}

	public Double getSumBenchmarkPremium() {
		return sumBenchmarkPremium;
	}

	public void setSumBenchmarkPremium(Double sumBenchmarkPremium) {
		this.sumBenchmarkPremium = sumBenchmarkPremium;
	}

	public Double getSumDisCountPremiumBI() {
		return sumDisCountPremiumBI;
	}

	public void setSumDisCountPremiumBI(Double sumDisCountPremiumBI) {
		this.sumDisCountPremiumBI = sumDisCountPremiumBI;
	}

	public Double getSumItemPremium() {
		return sumItemPremium;
	}

	public void setSumItemPremium(Double sumItemPremium) {
		this.sumItemPremium = sumItemPremium;
	}

	public String getPingendorseNo() {
		return pingendorseNo;
	}

	public void setPingendorseNo(String pingendorseNo) {
		this.pingendorseNo = pingendorseNo;
	}

    
    public Integer getStartMinute() {
        return startMinute;
    }

    
    public void setStartMinute(Integer startMinute) {
        this.startMinute = startMinute;
    }

    
    public Integer getEndMinute() {
        return endMinute;
    }

    
    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }

    
    public String getClaimSequenceNo() {
        return claimSequenceNo;
    }

    
    public void setClaimSequenceNo(String claimSequenceNo) {
        this.claimSequenceNo = claimSequenceNo;
    }

    
    public String getClaimType() {
        return claimType;
    }

    
    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    
    public String getRemarks() {
        return remarks;
    }

    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

	public String getServiceMobile() {
		return serviceMobile;
	}

	public void setServiceMobile(String serviceMobile) {
		this.serviceMobile = serviceMobile;
	}
	
	

}
