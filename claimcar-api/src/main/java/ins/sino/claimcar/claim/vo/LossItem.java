package ins.sino.claimcar.claim.vo;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 
 * 立案所需的损失项来自不同的表，金额的来源和损失类型的来源也不同，
 * 为了避免复杂的数据来源判断，所以有了这个类
 *
 */
public class LossItem implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 损失类型，八类
	 */
	private String	lossItemType;
	/**
	 * 损失项的名称
	 */
	private String 	lossItemTypeName;
	/**
	 * 损失项的分类，对于财产为财产分类，对于人员为行业分类，对于其他损失为费用名称
	 */
	private String	lossType;
	/**
	 * 行业分类名称
	 */
	private String	lossTypeName;
	/**
	 * 损失项的名称，车辆和其他为车牌号，人员为名称
	 */
	private String	lossName;
	/**
	 * 估损金额
	 */
	private BigDecimal sumClaim;
	/**
	 * 施救费
	 */
	private BigDecimal rescueFee;
	/**
	 * 残值
	 */
	private BigDecimal rejectFee;
	/**
	 * 1.车辆 2.财产 3.人员 4.其他
	 */
	private int flag;		
	/**
	 * 数据来源:查勘、定损、医疗跟踪
	 */
	private String dataSource;
	/**
	 * 损失项在对应表中的主键
	 */
	private Map itemKey;
	
	/**
	 * 涉案损失项对应的主键
	 */
	private Long keyId;
	/**
	 * 损失类别对应的险别
	 */
	//private List<String> kindCode;
	
	/**
	 * 估损金额——医疗费：医药费、续医费、伙食补助费
	 */
	private BigDecimal sumClaimForMedical;
	/**
	 * 损失类型——医疗费：医药费、续医费、伙食补助费(单独为医疗费增加一个lossitemtype为021)
	 */
	private String	lossItemTypeMedical;
	/**
	 *商业险责任比例 
	 */
	private BigDecimal indemnityDutyRate;
	
	/**
	 *商业险责任类型 
	 */
	private String indemnityDuty;
	
	/** 交强险责任比例 */
	private BigDecimal ciIndemDuty;

	
	public String getLossItemType() {
		return lossItemType;
	}

	
	public void setLossItemType(String lossItemType) {
		this.lossItemType = lossItemType;
	}

	
	public String getLossItemTypeName() {
		return lossItemTypeName;
	}

	
	public void setLossItemTypeName(String lossItemTypeName) {
		this.lossItemTypeName = lossItemTypeName;
	}

	
	public String getLossType() {
		return lossType;
	}

	
	public void setLossType(String lossType) {
		this.lossType = lossType;
	}

	
	public String getLossTypeName() {
		return lossTypeName;
	}

	
	public void setLossTypeName(String lossTypeName) {
		this.lossTypeName = lossTypeName;
	}

	
	public String getLossName() {
		return lossName;
	}

	
	public void setLossName(String lossName) {
		this.lossName = lossName;
	}

	
	public BigDecimal getSumClaim() {
		return sumClaim;
	}

	
	public void setSumClaim(BigDecimal sumClaim) {
		this.sumClaim = sumClaim;
	}

	
	public BigDecimal getRescueFee() {
		return rescueFee;
	}

	
	public void setRescueFee(BigDecimal rescueFee) {
		this.rescueFee = rescueFee;
	}

	
	public BigDecimal getRejectFee() {
		return rejectFee;
	}

	
	public void setRejectFee(BigDecimal rejectFee) {
		this.rejectFee = rejectFee;
	}

	
	public int getFlag() {
		return flag;
	}

	
	public void setFlag(int flag) {
		this.flag = flag;
	}

	
	public String getDataSource() {
		return dataSource;
	}

	
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	
	public Map getItemKey() {
		return itemKey;
	}

	
	public void setItemKey(Map itemKey) {
		this.itemKey = itemKey;
	}

	
	public Long getKeyId() {
		return keyId;
	}

	
	public void setKeyId(Long keyId) {
		this.keyId = keyId;
	}

	
	public BigDecimal getSumClaimForMedical() {
		return sumClaimForMedical;
	}

	
	public void setSumClaimForMedical(BigDecimal sumClaimForMedical) {
		this.sumClaimForMedical = sumClaimForMedical;
	}

	
	public String getLossItemTypeMedical() {
		return lossItemTypeMedical;
	}

	
	public void setLossItemTypeMedical(String lossItemTypeMedical) {
		this.lossItemTypeMedical = lossItemTypeMedical;
	}

	
	public BigDecimal getIndemnityDutyRate() {
		return indemnityDutyRate;
	}

	
	public void setIndemnityDutyRate(BigDecimal indemnityDutyRate) {
		this.indemnityDutyRate = indemnityDutyRate;
	}

	
	public String getIndemnityDuty() {
		return indemnityDuty;
	}

	
	public void setIndemnityDuty(String indemnityDuty) {
		this.indemnityDuty = indemnityDuty;
	}

	
	public BigDecimal getCiIndemDuty() {
		return ciIndemDuty;
	}

	
	public void setCiIndemDuty(BigDecimal ciIndemDuty) {
		this.ciIndemDuty = ciIndemDuty;
	}
	
}
