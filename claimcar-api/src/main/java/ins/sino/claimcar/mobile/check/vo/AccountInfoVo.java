package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("ACCOUNTINFO")
public class AccountInfoVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ACCOUNTID")
	private String accountId; //理赔收款人ID
	
	@XStreamAlias("ACCOUNTSERIALNO")
	private String accountSerialNo; //移动终端收款人序号
	
	@XStreamAlias("PAYEENATURE")
    private String payeeNature; //收款人性质
	
	@XStreamAlias("IDENTIFYTYPE")
    private String identifyType; //证件类型
	
	@XStreamAlias("PAYEETYPE")
	private String payeeType; //收款人类型
	
	@XStreamAlias("NAME")
	private String name; //姓名
	
	@XStreamAlias("PUBANDPRILOGO")
    private String pubandPrilogo; //公私标志
	
	@XStreamAlias("IDENTIFYNUMBER")
    private String identifyNumber; //证件号码
	
	@XStreamAlias("ACCOUNTNO")
    private String accountNo; //收款人账号
	
	@XStreamAlias("PROVINCECODE")
    private String provinceCode; //收款人开户行归属地省代码
	
	@XStreamAlias("CITYCODE")
    private String cityCode; //收款人开户行归属地市代码
	
	@XStreamAlias("BANKNAME")
	private String bankName; //开户银行  收款方开户行
	
	@XStreamAlias("BRANCHNAME")
    private String branchName; //收款人开户行分行名称
	
	@XStreamAlias("BANKCODE")
    private String bankCode; //收款人银行行号
	
	@XStreamAlias("TRANSFERMODE")
    private String transferMode; //转账汇款模式

	
	@XStreamAlias("PHONE")
	private String phone; //收款人手机号码

	@XStreamAlias("DIGEST")
    private String digest; //摘要
	
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountSerialNo() {
		return accountSerialNo;
	}

	public void setAccountSerialNo(String accountSerialNo) {
		this.accountSerialNo = accountSerialNo;
	}

	public String getPayeeType() {
		return payeeType;
	}

	public void setPayeeType(String payeeType) {
		this.payeeType = payeeType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifyNumber() {
		return identifyNumber;
	}

	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

    
    public String getPayeeNature() {
        return payeeNature;
    }

    
    public void setPayeeNature(String payeeNature) {
        this.payeeNature = payeeNature;
    }

    
    public String getIdentifyType() {
        return identifyType;
    }

    
    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    
    public String getPubandPrilogo() {
        return pubandPrilogo;
    }

    
    public void setPubandPrilogo(String pubandPrilogo) {
        this.pubandPrilogo = pubandPrilogo;
    }

    
    public String getProvinceCode() {
        return provinceCode;
    }

    
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    
    public String getCityCode() {
        return cityCode;
    }

    
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    
    public String getBranchName() {
        return branchName;
    }

    
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    
    public String getBankCode() {
        return bankCode;
    }

    
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    
    public String getTransferMode() {
        return transferMode;
    }

    
    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

    
    public String getDigest() {
        return digest;
    }

    
    public void setDigest(String digest) {
        this.digest = digest;
    }
	
	
	
}
