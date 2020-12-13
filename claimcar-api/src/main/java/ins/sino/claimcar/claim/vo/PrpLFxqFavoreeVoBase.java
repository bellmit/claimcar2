package ins.sino.claimcar.claim.vo;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Vo Base Class of PO PrpLFxqFavoree
 */ 
public class PrpLFxqFavoreeVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
    private PrpLPayCustomVo prpLPayCustomVo;
	private String favoreeName;
	private String favoreelInsureRelation;
	private String favoreeAdress;
	private String favoreelBusinessArea;
	private String favoreelRevenueRegistNo;
	private String favoreelBusinessCode;
	private Date favoreeCertifyStartDate;
	private Date favoreeCertifyEndDate;
	private String favoreenSex;
	private String favoreenNatioNality;
	private String favoreeCertifyType;
	private String favoreeIdentifyCode;
	private String favoreenProfession;
	private String favoreenPhone;
	private String favoreenAdressType;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	private String customerType;
	private String compensateNo;
	private String claimNo;
	private String flag;
	private String seeFlag;

    protected PrpLFxqFavoreeVoBase() {
	
    }
    
	
	
    public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public PrpLPayCustomVo getPrpLPayCustomVo() {
        return prpLPayCustomVo;
    }
    
    public void setPrpLPayCustomVo(PrpLPayCustomVo prpLPayCustomVo) {
        this.prpLPayCustomVo = prpLPayCustomVo;
    }
    
    public String getFavoreeName() {
        return favoreeName;
    }

    public void setFavoreeName(String favoreeName) {
        this.favoreeName = favoreeName;
    }

    public String getFavoreelInsureRelation() {
        return favoreelInsureRelation;
    }
    
    public void setFavoreelInsureRelation(String favoreelInsureRelation) {
        this.favoreelInsureRelation = favoreelInsureRelation;
    }
	
    public String getFavoreeAdress() {
        return favoreeAdress;
    }
    
    public void setFavoreeAdress(String favoreeAdress) {
        this.favoreeAdress = favoreeAdress;
    }


    public String getFavoreelBusinessArea() {
		return this.favoreelBusinessArea;
	}

	public void setFavoreelBusinessArea(String favoreelBusinessArea) {
		this.favoreelBusinessArea = favoreelBusinessArea;
	}

	public String getFavoreelRevenueRegistNo() {
		return this.favoreelRevenueRegistNo;
	}

	public void setFavoreelRevenueRegistNo(String favoreelRevenueRegistNo) {
		this.favoreelRevenueRegistNo = favoreelRevenueRegistNo;
	}

	public String getFavoreelBusinessCode() {
		return this.favoreelBusinessCode;
	}

	public void setFavoreelBusinessCode(String favoreelBusinessCode) {
		this.favoreelBusinessCode = favoreelBusinessCode;
	}
	
    public Date getFavoreeCertifyStartDate() {
        return favoreeCertifyStartDate;
    }
 
    public void setFavoreeCertifyStartDate(Date favoreeCertifyStartDate) {
        this.favoreeCertifyStartDate = favoreeCertifyStartDate;
    }
   
    public Date getFavoreeCertifyEndDate() {
        return favoreeCertifyEndDate;
    }
  
    public void setFavoreeCertifyEndDate(Date favoreeCertifyEndDate) {
        this.favoreeCertifyEndDate = favoreeCertifyEndDate;
    }


	public String getFavoreenSex() {
		return this.favoreenSex;
	}

	public void setFavoreenSex(String favoreenSex) {
		this.favoreenSex = favoreenSex;
	}

	public String getFavoreenNatioNality() {
		return this.favoreenNatioNality;
	}

	public void setFavoreenNatioNality(String favoreenNatioNality) {
		this.favoreenNatioNality = favoreenNatioNality;
	}

	public String getFavoreeCertifyType() {
		return this.favoreeCertifyType;
	}

	public void setFavoreeCertifyType(String favoreeCertifyType) {
		this.favoreeCertifyType = favoreeCertifyType;
	}

	public String getFavoreeIdentifyCode() {
		return this.favoreeIdentifyCode;
	}

	public void setFavoreeIdentifyCode(String favoreeIdentifyCode) {
		this.favoreeIdentifyCode = favoreeIdentifyCode;
	}

	public String getFavoreenProfession() {
		return this.favoreenProfession;
	}

	public void setFavoreenProfession(String favoreenProfession) {
		this.favoreenProfession = favoreenProfession;
	}

	public String getFavoreenPhone() {
		return this.favoreenPhone;
	}

	public void setFavoreenPhone(String favoreenPhone) {
		this.favoreenPhone = favoreenPhone;
	}

	public String getFavoreenAdressType() {
		return this.favoreenAdressType;
	}

	public void setFavoreenAdressType(String favoreenAdressType) {
		this.favoreenAdressType = favoreenAdressType;
	}

    public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
  
    public String getCustomerType() {
        return customerType;
    }
    
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
    
    public String getCompensateNo() {
        return compensateNo;
    }  
    
    public void setCompensateNo(String compensateNo) {
        this.compensateNo = compensateNo;
    }

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSeeFlag() {
		return seeFlag;
	}

	public void setSeeFlag(String seeFlag) {
		this.seeFlag = seeFlag;
	}
    
    
    
}