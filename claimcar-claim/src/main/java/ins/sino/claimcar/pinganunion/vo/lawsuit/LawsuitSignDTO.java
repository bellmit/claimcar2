package ins.sino.claimcar.pinganunion.vo.lawsuit;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class LawsuitSignDTO implements Serializable{
	private static final long serialVersionUID = -5459000094465095630L;
	private String lawsuitNo;//诉讼号
	private String plaintiff;//原告
	private String defendant;//被告
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="signDate", format="yyyy-MM-dd hh:mm:ss")
	private Date signDate;//传票签收日期
	private String courtCaseNo;//法院案号
	private String courtName;//法院
	private String signer;//签收人
	private String lawHandlName;//应诉处理人
	private String judge;//经办法官
	private String lawsuitType;//案件诉讼性质：1-侵权纠纷、2-合同纠纷
	private String dutyCodeList;//涉案险别：Eg：02,46,45等逗号拼接数据代码定义3.3
	private String appearOpinion;//诉讼意见：1-拒赔、2-非拒赔、3-注销诉讼
	private String remark;//应诉方案
	private String lawsuitStage;//诉讼阶段：1-一审、2-二审
	public String getLawsuitNo() {
		return lawsuitNo;
	}
	public void setLawsuitNo(String lawsuitNo) {
		this.lawsuitNo = lawsuitNo;
	}
	public String getPlaintiff() {
		return plaintiff;
	}
	public void setPlaintiff(String plaintiff) {
		this.plaintiff = plaintiff;
	}
	public String getDefendant() {
		return defendant;
	}
	public void setDefendant(String defendant) {
		this.defendant = defendant;
	}
	
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getCourtCaseNo() {
		return courtCaseNo;
	}
	public void setCourtCaseNo(String courtCaseNo) {
		this.courtCaseNo = courtCaseNo;
	}
	public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public String getSigner() {
		return signer;
	}
	public void setSigner(String signer) {
		this.signer = signer;
	}
	public String getLawHandlName() {
		return lawHandlName;
	}
	public void setLawHandlName(String lawHandlName) {
		this.lawHandlName = lawHandlName;
	}
	public String getJudge() {
		return judge;
	}
	public void setJudge(String judge) {
		this.judge = judge;
	}
	public String getLawsuitType() {
		return lawsuitType;
	}
	public void setLawsuitType(String lawsuitType) {
		this.lawsuitType = lawsuitType;
	}
	public String getDutyCodeList() {
		return dutyCodeList;
	}
	public void setDutyCodeList(String dutyCodeList) {
		this.dutyCodeList = dutyCodeList;
	}
	public String getAppearOpinion() {
		return appearOpinion;
	}
	public void setAppearOpinion(String appearOpinion) {
		this.appearOpinion = appearOpinion;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLawsuitStage() {
		return lawsuitStage;
	}
	public void setLawsuitStage(String lawsuitStage) {
		this.lawsuitStage = lawsuitStage;
	}
	
	

}
