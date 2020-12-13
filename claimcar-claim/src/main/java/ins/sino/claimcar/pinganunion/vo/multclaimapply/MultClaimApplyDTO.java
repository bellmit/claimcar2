package ins.sino.claimcar.pinganunion.vo.multclaimapply;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
/**
 * 
 * @Description: 隶属于multClaimApplyDTO重开信息
 * @author: zhubin
 * @date: 2020年8月3日 下午3:54:52
 */
public class MultClaimApplyDTO implements Serializable{
	private static final long serialVersionUID = 7009309931851939545L;
	private String reportNo;//报案号：
	private String caseTimes;//赔付次数：
	private String applyType;//申请类型：0-多次赔付
	private String applyReason;//申请原因：
	private String addAssess;//追加定损：Y-是 N-否
	private String addAccept;//票据加收：Y-是 N-否
	private String applyTimes;//申请次数：
	private String applyUm;//申请人：
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="applyDate", format="yyyy-MM-dd hh:mm:ss")
	private Date applyDate;//申请时间：
	private String verifyUm;//审批人：
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="verifyDate", format="yyyy-MM-dd hh:mm:ss")
	private Date verifyDate;//审批时间：
	private String verifyOptions;//审批意见：1-同意2-退回
	private String verifyRemark;//审批意见说明：
	private String addFee;//追加直接理赔费用：Y-是 N-否
	private String addOther;//追加其他：Y-是 N-否
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getCaseTimes() {
		return caseTimes;
	}
	public void setCaseTimes(String caseTimes) {
		this.caseTimes = caseTimes;
	}
	public String getApplyType() {
		return applyType;
	}
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	public String getApplyReason() {
		return applyReason;
	}
	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
	public String getAddAssess() {
		return addAssess;
	}
	public void setAddAssess(String addAssess) {
		this.addAssess = addAssess;
	}
	public String getAddAccept() {
		return addAccept;
	}
	public void setAddAccept(String addAccept) {
		this.addAccept = addAccept;
	}
	public String getApplyTimes() {
		return applyTimes;
	}
	public void setApplyTimes(String applyTimes) {
		this.applyTimes = applyTimes;
	}
	public String getApplyUm() {
		return applyUm;
	}
	public void setApplyUm(String applyUm) {
		this.applyUm = applyUm;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getVerifyUm() {
		return verifyUm;
	}
	public void setVerifyUm(String verifyUm) {
		this.verifyUm = verifyUm;
	}
	public Date getVerifyDate() {
		return verifyDate;
	}
	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}
	public String getVerifyOptions() {
		return verifyOptions;
	}
	public void setVerifyOptions(String verifyOptions) {
		this.verifyOptions = verifyOptions;
	}
	public String getVerifyRemark() {
		return verifyRemark;
	}
	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}
	public String getAddFee() {
		return addFee;
	}
	public void setAddFee(String addFee) {
		this.addFee = addFee;
	}
	public String getAddOther() {
		return addOther;
	}
	public void setAddOther(String addOther) {
		this.addOther = addOther;
	}

	

}
