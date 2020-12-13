package ins.sino.claimcar.pinganunion.vo.lawsuit;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 
 * @Description: 诉讼基本信息
 * @author: zhubin
 * @date: 2020年7月31日 下午6:18:12
 */
public class LawsuitBaseInfoDTO implements Serializable {
	private static final long serialVersionUID = 7161945014596089834L;
	private String reportNo;//报案号
	private int caseTimes;//赔付次数
	private String lawsuitNo;//诉讼号
	private String payResult;//赔付结论:1赔付、2整案拒赔、3商业险拒赔、4注销
	private String firstTrialPersonAmount;//一审判决/调解金额（人伤）
	private String firstTrialCarAmount;//一审判决/调解金额（车物损）
	private String secondTrialPersonAmount;//二审判决/调解金额（人伤）
	private String secondTrialCarAmount;//二审判决/调解金额（车物损）
	private String appealPersonAmount;//诉请金额（人伤）
	private String appealCarAmount;//诉请金额（车物损）
	private String lawEndCaseBy;//诉讼结案人
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="lawEndCaseDate", format="yyyy-MM-dd hh:mm:ss")
	private Date lawEndCaseDate;//诉讼结案时间
	private String shouldLawBeforeTrail;//一审庭前是否该诉：0:不该诉,1:该诉
	private String shouldLawAfterTrail;//一审庭后是否该诉：0:不该诉,1:该诉
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public int getCaseTimes() {
		return caseTimes;
	}
	public void setCaseTimes(int caseTimes) {
		this.caseTimes = caseTimes;
	}
	public String getLawsuitNo() {
		return lawsuitNo;
	}
	public void setLawsuitNo(String lawsuitNo) {
		this.lawsuitNo = lawsuitNo;
	}
	public String getPayResult() {
		return payResult;
	}
	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}
	public String getFirstTrialPersonAmount() {
		return firstTrialPersonAmount;
	}
	public void setFirstTrialPersonAmount(String firstTrialPersonAmount) {
		this.firstTrialPersonAmount = firstTrialPersonAmount;
	}
	public String getFirstTrialCarAmount() {
		return firstTrialCarAmount;
	}
	public void setFirstTrialCarAmount(String firstTrialCarAmount) {
		this.firstTrialCarAmount = firstTrialCarAmount;
	}
	public String getSecondTrialPersonAmount() {
		return secondTrialPersonAmount;
	}
	public void setSecondTrialPersonAmount(String secondTrialPersonAmount) {
		this.secondTrialPersonAmount = secondTrialPersonAmount;
	}
	public String getSecondTrialCarAmount() {
		return secondTrialCarAmount;
	}
	public void setSecondTrialCarAmount(String secondTrialCarAmount) {
		this.secondTrialCarAmount = secondTrialCarAmount;
	}
	public String getAppealPersonAmount() {
		return appealPersonAmount;
	}
	public void setAppealPersonAmount(String appealPersonAmount) {
		this.appealPersonAmount = appealPersonAmount;
	}
	public String getAppealCarAmount() {
		return appealCarAmount;
	}
	public void setAppealCarAmount(String appealCarAmount) {
		this.appealCarAmount = appealCarAmount;
	}
	public String getLawEndCaseBy() {
		return lawEndCaseBy;
	}
	public void setLawEndCaseBy(String lawEndCaseBy) {
		this.lawEndCaseBy = lawEndCaseBy;
	}
	public Date getLawEndCaseDate() {
		return lawEndCaseDate;
	}
	public void setLawEndCaseDate(Date lawEndCaseDate) {
		this.lawEndCaseDate = lawEndCaseDate;
	}
	public String getShouldLawBeforeTrail() {
		return shouldLawBeforeTrail;
	}
	public void setShouldLawBeforeTrail(String shouldLawBeforeTrail) {
		this.shouldLawBeforeTrail = shouldLawBeforeTrail;
	}
	public String getShouldLawAfterTrail() {
		return shouldLawAfterTrail;
	}
	public void setShouldLawAfterTrail(String shouldLawAfterTrail) {
		this.shouldLawAfterTrail = shouldLawAfterTrail;
	}
	
	
	

}
