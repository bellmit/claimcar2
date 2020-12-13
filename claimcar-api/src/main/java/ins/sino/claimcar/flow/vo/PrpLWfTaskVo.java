package ins.sino.claimcar.flow.vo;

import ins.sino.claimcar.recloss.vo.PrpLRecLossVo;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Custom VO class of PO PrpLWfTaskOut
 */ 
public class PrpLWfTaskVo extends PrpLWfTaskVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private Map<String,String> bussTagMap = null;
	
	private String quickFlag;
	
	private String lossItemName;
	
	private PrpLRecLossVo prpLRecLossVo;
	
	private String mobileNo;  //移动查勘 标的序号
	private String mobileName;   // 移动查勘 标的名称
	private String mobileOperateType; //移动查勘操作类型
	private BigDecimal originalTaskId; //退回任务原ID
	
	//金额
	private BigDecimal money;
	private String subCheckFlag;//代查勘标识，0-正常案件，1-司内代查勘，2-公估代查勘，3-公估案件

	private String backFlags;//是否能退回单证，定损标识，2-能退，1-不能退
	public Map<String,String> getBussTagMap() {
		return bussTagMap;
	}

	public void setBussTagMap(Map<String,String> bussTagMap) {
		this.bussTagMap = bussTagMap;
	}

	public String getQuickFlag() {
		return quickFlag;
	}

	public void setQuickFlag(String quickFlag) {
		this.quickFlag = quickFlag;
	}

	public String getLossItemName() {
		return lossItemName;
	}

	public void setLossItemName(String lossItemName) {
		this.lossItemName = lossItemName;
	}

	public PrpLRecLossVo getPrpLRecLossVo() {
		return prpLRecLossVo;
	}

	public void setPrpLRecLossVo(PrpLRecLossVo prpLRecLossVo) {
		this.prpLRecLossVo = prpLRecLossVo;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

    
    public String getMobileNo() {
        return mobileNo;
    }

    
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    
    public String getMobileName() {
        return mobileName;
    }

    
    public void setMobileName(String mobileName) {
        this.mobileName = mobileName;
    }

    
    public String getMobileOperateType() {
        return mobileOperateType;
    }

    
    public void setMobileOperateType(String mobileOperateType) {
        this.mobileOperateType = mobileOperateType;
    }

	public BigDecimal getOriginalTaskId() {
		return originalTaskId;
	}

	public void setOriginalTaskId(BigDecimal originalTaskId) {
		this.originalTaskId = originalTaskId;
	}

	public String getSubCheckFlag() {
		return subCheckFlag;
	}

	public void setSubCheckFlag(String subCheckFlag) {
		this.subCheckFlag = subCheckFlag;
	}

    
    public String getBackFlags() {
        return backFlags;
    }

    
    public void setBackFlags(String backFlags) {
        this.backFlags = backFlags;
    }

	
}
