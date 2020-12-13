package ins.sino.claimcar.claim.vo;


/**
 * M险不计免赔组织数据,免赔率与免赔金额组织.
 * 
 * @author zhouwen
 * 
 */
public class MItemKindVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/** M险拆分后对应的险别 */
	private String kindCode;

	/** 免赔率 */
	private double exceptKindDeductibleRate;

	/** 该险别不计免赔额 */
	private double exceptKindDeductible;

	
	public String getKindCode() {
		return kindCode;
	}

	
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	
	public double getExceptKindDeductibleRate() {
		return exceptKindDeductibleRate;
	}

	
	public void setExceptKindDeductibleRate(double exceptKindDeductibleRate) {
		this.exceptKindDeductibleRate = exceptKindDeductibleRate;
	}

	
	public double getExceptKindDeductible() {
		return exceptKindDeductible;
	}

	
	public void setExceptKindDeductible(double exceptKindDeductible) {
		this.exceptKindDeductible = exceptKindDeductible;
	}
	
}
