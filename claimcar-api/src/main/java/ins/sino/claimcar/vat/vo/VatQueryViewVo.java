package ins.sino.claimcar.vat.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class VatQueryViewVo implements Serializable{
	
 private static final long serialVersionUID = 1L;
 private Long billContId;//发票计算书关系表Id
 private Long billId;//发票Id
 private int indexNo;//序号
 private String registNo;//报案号
 private String compensateNo;//计算书号
 private String policyNo;//保单号
 private String comCode;//归属机构
 private String comName;//归属机构名称
 private String payName;//收款人
 private String payId;//收款人id
 private Date underwriteStartDate;//核赔开始区间
 private Date underwriteEndDate;//核赔结束区间
 private Date underwriteDate;//核损通过时间
 private String accountNo;//收款人账号
 private String workFlag;//待办状态
 private String workName;//待办状态名称
 private String saleName;//销方名称
 private String saleNo;//销方纳税人识别号
 private String billNo;//发票号码
 private String billCode;//发票代码
 private BigDecimal billNnum;//发票不含税金额
 private BigDecimal billSnum;//发票税额
 private BigDecimal billSl;//发票税率
 private BigDecimal billNum;//发票价税合计金额
 private String billSlName;//发票税率百分比
 private Date billDate;//开票日期
 private Date billStartDate;//开票日期开始期间
 private Date billEndDate;//开票日期结束期间
 private BigDecimal registerNum;//已登记金额
 private BigDecimal registerNum1;//已登记金额1
 private BigDecimal sumAmt;//费用总金额
 private String bussType;//业务类型
 private String bussName;//业务类型名称
 private String feeCode;//费用类型
 private String feeName;//费用类型名称
 private String payeeType;//实付/预付
 private String sendStatus;//推送状态
 private String sendStatusName;//推送状态名称
 private String registerStatus;//登记状态
 private String registerStatusName;//登记状态名称
 private String backFlag;//打回状态
 private String backFlagName;//打回状态名称
 private String backCauseInfo;//打回原因
 private String vaildFlag;//有效状态
 private String vaildFlagName;//有效名称
 private String payType;//赔付类型
 private String policyName;//被保险人
 private String licenseNo;//标的车牌号
 private Date reportStartTime;//报案开始区间
 private Date reportEndTime;//报案结束区间
 private Date damageStartTime;//出险开始区间
 private Date damageEndTime;//出险结束区间
 private Date endcaseStartTime;//结案开始区间
 private Date endcaseEndTime;//结案开始区间
 private String bussId;//业务表Id
 private String busstableType;//业务表Id表类型
 private String vidflag;//验真状态
 private String vidflagName;//验真状态名称
 private String billUrl;
 //分页
 private Integer start;// 记录起始位置
 private Integer length;// 记录数
 
 
public String getRegistNo() {
	return registNo;
}
public void setRegistNo(String registNo) {
	this.registNo = registNo;
}
public String getCompensateNo() {
	return compensateNo;
}
public void setCompensateNo(String compensateNo) {
	this.compensateNo = compensateNo;
}
public String getComCode() {
	return comCode;
}
public void setComCode(String comCode) {
	this.comCode = comCode;
}
public String getComName() {
	return comName;
}
public void setComName(String comName) {
	this.comName = comName;
}
public String getPayName() {
	return payName;
}
public void setPayName(String payName) {
	this.payName = payName;
}
public String getPayId() {
	return payId;
}
public void setPayId(String payId) {
	this.payId = payId;
}
public Date getUnderwriteStartDate() {
	return underwriteStartDate;
}
public void setUnderwriteStartDate(Date underwriteStartDate) {
	this.underwriteStartDate = underwriteStartDate;
}
public Date getUnderwriteEndDate() {
	return underwriteEndDate;
}
public void setUnderwriteEndDate(Date underwriteEndDate) {
	this.underwriteEndDate = underwriteEndDate;
}
public String getAccountNo() {
	return accountNo;
}
public void setAccountNo(String accountNo) {
	this.accountNo = accountNo;
}
public String getWorkFlag() {
	return workFlag;
}
public void setWorkFlag(String workFlag) {
	this.workFlag = workFlag;
}
public String getSaleName() {
	return saleName;
}
public void setSaleName(String saleName) {
	this.saleName = saleName;
}
public String getSaleNo() {
	return saleNo;
}
public void setSaleNo(String saleNo) {
	this.saleNo = saleNo;
}
public String getBillNo() {
	return billNo;
}
public void setBillNo(String billNo) {
	this.billNo = billNo;
}
public String getBillCode() {
	return billCode;
}
public void setBillCode(String billCode) {
	this.billCode = billCode;
}
public BigDecimal getBillNnum() {
	return billNnum;
}
public void setBillNnum(BigDecimal billNnum) {
	this.billNnum = billNnum;
}
public BigDecimal getBillSnum() {
	return billSnum;
}
public void setBillSnum(BigDecimal billSnum) {
	this.billSnum = billSnum;
}
public BigDecimal getBillSl() {
	return billSl;
}
public void setBillSl(BigDecimal billSl) {
	this.billSl = billSl;
}
public BigDecimal getBillNum() {
	return billNum;
}
public void setBillNum(BigDecimal billNum) {
	this.billNum = billNum;
}
public Date getBillDate() {
	return billDate;
}
public void setBillDate(Date billDate) {
	this.billDate = billDate;
}
public Date getBillStartDate() {
	return billStartDate;
}
public void setBillStartDate(Date billStartDate) {
	this.billStartDate = billStartDate;
}
public Date getBillEndDate() {
	return billEndDate;
}
public void setBillEndDate(Date billEndDate) {
	this.billEndDate = billEndDate;
}
public BigDecimal getRegisterNum() {
	return registerNum;
}
public void setRegisterNum(BigDecimal registerNum) {
	this.registerNum = registerNum;
}
public BigDecimal getSumAmt() {
	return sumAmt;
}
public void setSumAmt(BigDecimal sumAmt) {
	this.sumAmt = sumAmt;
}
public String getBussType() {
	return bussType;
}
public void setBussType(String bussType) {
	this.bussType = bussType;
}
public String getBussName() {
	return bussName;
}
public void setBussName(String bussName) {
	this.bussName = bussName;
}
public String getFeeCode() {
	return feeCode;
}
public void setFeeCode(String feeCode) {
	this.feeCode = feeCode;
}
public String getFeeName() {
	return feeName;
}
public void setFeeName(String feeName) {
	this.feeName = feeName;
}
public String getSendStatus() {
	return sendStatus;
}
public void setSendStatus(String sendStatus) {
	this.sendStatus = sendStatus;
}
public String getSendStatusName() {
	return sendStatusName;
}
public void setSendStatusName(String sendStatusName) {
	this.sendStatusName = sendStatusName;
}
public String getRegisterStatus() {
	return registerStatus;
}
public void setRegisterStatus(String registerStatus) {
	this.registerStatus = registerStatus;
}
public String getRegisterStatusName() {
	return registerStatusName;
}
public void setRegisterStatusName(String registerStatusName) {
	this.registerStatusName = registerStatusName;
}
public String getBackFlag() {
	return backFlag;
}
public void setBackFlag(String backFlag) {
	this.backFlag = backFlag;
}
public String getBackCauseInfo() {
	return backCauseInfo;
}
public void setBackCauseInfo(String backCauseInfo) {
	this.backCauseInfo = backCauseInfo;
}
public String getVaildFlag() {
	return vaildFlag;
}
public void setVaildFlag(String vaildFlag) {
	this.vaildFlag = vaildFlag;
}
public String getVaildFlagName() {
	return vaildFlagName;
}
public void setVaildFlagName(String vaildFlagName) {
	this.vaildFlagName = vaildFlagName;
}
public String getPayType() {
	return payType;
}
public void setPayType(String payType) {
	this.payType = payType;
}
public static long getSerialversionuid() {
	return serialVersionUID;
}
public Date getUnderwriteDate() {
	return underwriteDate;
}
public void setUnderwriteDate(Date underwriteDate) {
	this.underwriteDate = underwriteDate;
}
public String getPolicyName() {
	return policyName;
}
public void setPolicyName(String policyName) {
	this.policyName = policyName;
}
public String getLicenseNo() {
	return licenseNo;
}
public void setLicenseNo(String licenseNo) {
	this.licenseNo = licenseNo;
}
public Date getReportStartTime() {
	return reportStartTime;
}
public void setReportStartTime(Date reportStartTime) {
	this.reportStartTime = reportStartTime;
}
public Date getReportEndTime() {
	return reportEndTime;
}
public void setReportEndTime(Date reportEndTime) {
	this.reportEndTime = reportEndTime;
}
public Date getDamageStartTime() {
	return damageStartTime;
}
public void setDamageStartTime(Date damageStartTime) {
	this.damageStartTime = damageStartTime;
}
public Date getDamageEndTime() {
	return damageEndTime;
}
public void setDamageEndTime(Date damageEndTime) {
	this.damageEndTime = damageEndTime;
}
public Date getEndcaseStartTime() {
	return endcaseStartTime;
}
public void setEndcaseStartTime(Date endcaseStartTime) {
	this.endcaseStartTime = endcaseStartTime;
}
public Date getEndcaseEndTime() {
	return endcaseEndTime;
}
public void setEndcaseEndTime(Date endcaseEndTime) {
	this.endcaseEndTime = endcaseEndTime;
}
public String getPolicyNo() {
	return policyNo;
}
public void setPolicyNo(String policyNo) {
	this.policyNo = policyNo;
}
public Integer getStart() {
	return start;
}
public void setStart(Integer start) {
	this.start = start;
}
public Integer getLength() {
	return length;
}
public void setLength(Integer length) {
	this.length = length;
}
public int getIndexNo() {
	return indexNo;
}
public void setIndexNo(int indexNo) {
	this.indexNo = indexNo;
}
public String getWorkName() {
	return workName;
}
public void setWorkName(String workName) {
	this.workName = workName;
}
public String getBussId() {
	return bussId;
}
public void setBussId(String bussId) {
	this.bussId = bussId;
}
public BigDecimal getRegisterNum1() {
	return registerNum1;
}
public void setRegisterNum1(BigDecimal registerNum1) {
	this.registerNum1 = registerNum1;
}
public String getBillSlName() {
	return billSlName;
}
public void setBillSlName(String billSlName) {
	this.billSlName = billSlName;
}
public String getVidflag() {
	return vidflag;
}
public void setVidflag(String vidflag) {
	this.vidflag = vidflag;
}
public String getVidflagName() {
	return vidflagName;
}
public void setVidflagName(String vidflagName) {
	this.vidflagName = vidflagName;
}
public String getPayeeType() {
	return payeeType;
}
public void setPayeeType(String payeeType) {
	this.payeeType = payeeType;
}
public String getBackFlagName() {
	return backFlagName;
}
public void setBackFlagName(String backFlagName) {
	this.backFlagName = backFlagName;
}
public Long getBillContId() {
	return billContId;
}
public void setBillContId(Long billContId) {
	this.billContId = billContId;
}
public Long getBillId() {
	return billId;
}
public void setBillId(Long billId) {
	this.billId = billId;
}
public String getBusstableType() {
	return busstableType;
}
public void setBusstableType(String busstableType) {
	this.busstableType = busstableType;
}
public String getBillUrl() {
	return billUrl;
}
public void setBillUrl(String billUrl) {
	this.billUrl = billUrl;
}

 

}
