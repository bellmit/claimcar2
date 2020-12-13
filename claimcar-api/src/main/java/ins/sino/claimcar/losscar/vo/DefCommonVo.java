package ins.sino.claimcar.losscar.vo;

import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefCommonVo implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	/**车损险保额*/
	private BigDecimal carAmount;
	
	/** 盗抢险保额 */
	private BigDecimal theftAmount;
	
	/** 内修和外修的总金额*/
	private BigDecimal otherAmount;
	
	/**赔案类别  prplcheck取值*/
	private String claimType;
	
	/** 出险时间 **/
	private Date damageDate;
	/** 出险地区 **/
	private String damageAreaCode;
	/** 出险地点 **/
	private String damageAddress;
	
	/**是否第一现场查勘 **/
	private String firstAddressFlag;
	
	/** 查勘人 **/
	private String checker;
	
	/** 查勘人身份证号码 **/
	private String checkerIdfNo;
	
	/** 查勘日期 **/
	private Date checkTime;
	
	/** 查勘地点 **/
	private String checkAddress;
	
	/** 偏差金额 **/
	private BigDecimal offsetVeri;
	
	/** 偏差比例 **/
	private BigDecimal offsetVeriRate;
	
	/** 已经核损通过的定损金额  追加定损使用 **/
	private BigDecimal sumpaidDef = new BigDecimal("0");
	
	/** 是否接受 **/
	private String acceptFlag = "1";
	
	/** 是否处理 页面置灰**/
	private String handleStatus;
	
	/** 公估标准 **/
	private String intermStanders; 
	/** 三者车车牌号码**/
	private Map<String,String> thirdCarMap = new HashMap<String,String>();
	
	/**是否可以发起复勘**/
	private Boolean recheckExist = false;
	
	/**复检 标识  0初始化 1 已发起复检 2核价不同意**/
	private String reLossFlag = "0";
	
	private int currencyLevel = -1;
	/**是否存在退回下级按钮 **/
	private Boolean lowerButton = false;
	/** 是否审核通过  **/
	private Boolean verifyPassFlag = false ;
	
	/** 损失信息 车辆损失 **/
	private List<PrpLDlossCarMainVo> lossCarMainList;
	
	/** 损失信息 财产损失  **/
	private List<PrpLdlossPropMainVo> lossPropMainList;
	
	/** 损失信息  人伤损失 **/
	private List<PrpLDlossPersTraceVo> lossPersTraceList;
	/** 旧条款  新增设备 */
	private Map<String,String> deviceMap;
	/** 配件**/
	private String compKindName;
	
	public BigDecimal getCarAmount() {
		return carAmount;
	}

	public void setCarAmount(BigDecimal carAmount) {
		this.carAmount = carAmount;
	}
	
	public String getIntermStanders() {
		return intermStanders;
	}
	
	public void setIntermStanders(String intermStanders) {
		this.intermStanders = intermStanders;
	}


	public BigDecimal getTheftAmount() {
		return theftAmount;
	}

	
	public void setTheftAmount(BigDecimal theftAmount) {
		this.theftAmount = theftAmount;
	}

	
	public String getClaimType() {
		return claimType;
	}
	
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	
	public Date getDamageDate() {
		return damageDate;
	}
	
	public void setDamageDate(Date damageDate) {
		this.damageDate = damageDate;
	}
	
	public String getDamageAddress() {
		return damageAddress;
	}

	
	public void setDamageAddress(String damageAddress) {
		this.damageAddress = damageAddress;
	}

	public String getFirstAddressFlag() {
		return firstAddressFlag;
	}

	
	public void setFirstAddressFlag(String firstAddressFlag) {
		this.firstAddressFlag = firstAddressFlag;
	}

	
	public String getChecker() {
		return checker;
	}

	
	public void setChecker(String checker) {
		this.checker = checker;
	}

	
	public String getCheckerIdfNo() {
		return checkerIdfNo;
	}

	
	public void setCheckerIdfNo(String checkerIdfNo) {
		this.checkerIdfNo = checkerIdfNo;
	}

	
	public Date getCheckTime() {
		return checkTime;
	}

	
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	
	public String getCheckAddress() {
		return checkAddress;
	}

	
	public void setCheckAddress(String checkAddress) {
		this.checkAddress = checkAddress;
	}

	
	public BigDecimal getOffsetVeri() {
		return offsetVeri;
	}

	
	public void setOffsetVeri(BigDecimal offsetVeri) {
		this.offsetVeri = offsetVeri;
	}

	
	public BigDecimal getOffsetVeriRate() {
		return offsetVeriRate;
	}

	
	public void setOffsetVeriRate(BigDecimal offsetVeriRate) {
		this.offsetVeriRate = offsetVeriRate;
	}
	
	public String getAcceptFlag() {
		return acceptFlag;
	}
	
	public void setAcceptFlag(String acceptFlag) {
		this.acceptFlag = acceptFlag;
	}
	
	public String getHandleStatus() {
		return handleStatus;
	}
	
	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}
	
	public Boolean getRecheckExist() {
		return recheckExist;
	}

	public void setRecheckExist(Boolean recheckExist) {
		this.recheckExist = recheckExist;
	}
	
	public List<PrpLDlossCarMainVo> getLossCarMainList() {
		return lossCarMainList;
	}

	public void setLossCarMainList(List<PrpLDlossCarMainVo> lossCarMainList) {
		this.lossCarMainList = lossCarMainList;
	}

	public List<PrpLdlossPropMainVo> getLossPropMainList() {
		return lossPropMainList;
	}

	public void setLossPropMainList(List<PrpLdlossPropMainVo> lossPropMainList) {
		this.lossPropMainList = lossPropMainList;
	}

	public List<PrpLDlossPersTraceVo> getLossPersTraceList() {
		return lossPersTraceList;
	}
	
	public void setLossPersTraceList(List<PrpLDlossPersTraceVo> lossPersTraceList) {
		this.lossPersTraceList = lossPersTraceList;
	}

	public Boolean getLowerButton() {
		return lowerButton;
	}

	public void setLowerButton(Boolean lowerButton) {
		this.lowerButton = lowerButton;
	}

	public BigDecimal getSumpaidDef() {
		return sumpaidDef;
	}

	public void setSumpaidDef(BigDecimal sumpaidDef) {
		this.sumpaidDef = sumpaidDef;
	}

	public String getDamageAreaCode() {
		return damageAreaCode;
	}

	public void setDamageAreaCode(String damageAreaCode) {
		this.damageAreaCode = damageAreaCode;
	}

	public Map<String,String> getThirdCarMap() {
		return thirdCarMap;
	}

	public void setThirdCarMap(Map<String,String> thirdCarMap) {
		this.thirdCarMap = thirdCarMap;
	}

	public BigDecimal getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(BigDecimal otherAmount) {
		this.otherAmount = otherAmount;
	}

	public int getCurrencyLevel() {
		return currencyLevel;
	}

	public void setCurrencyLevel(int currencyLevel) {
		this.currencyLevel = currencyLevel;
	}

	public Boolean getVerifyPassFlag() {
		return verifyPassFlag;
	}

	public void setVerifyPassFlag(Boolean verifyPassFlag) {
		this.verifyPassFlag = verifyPassFlag;
	}

	public String getReLossFlag() {
		return reLossFlag;
	}

	public void setReLossFlag(String reLossFlag) {
		this.reLossFlag = reLossFlag;
	}

	public Map<String,String> getDeviceMap() {
		return deviceMap;
	}

	public void setDeviceMap(Map<String,String> deviceMap) {
		this.deviceMap = deviceMap;
	}

	public String getCompKindName() {
		return compKindName;
	}

	public void setCompKindName(String compKindName) {
		this.compKindName = compKindName;
	}
	
	
}
