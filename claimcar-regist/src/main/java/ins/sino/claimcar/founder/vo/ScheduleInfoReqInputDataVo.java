package ins.sino.claimcar.founder.vo;

import ins.platform.utils.xstreamconverters.SinosoftDateConverter;

import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * 调度信息更新接口vo（理赔请求客服系统）
 * @author Luwei
 */
@XStreamAlias("INPUTDATA")
public class ScheduleInfoReqInputDataVo {

	/**
	 * 报案号
	 */
	@XStreamAlias("ClmNo")
	private String clmNo;

	/**
	 * 调度时间yyyyMMddHHmmss
	 */
	@XStreamAlias("AttemperTime")
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyyMMddHHmmss"})
	private Date attemperTime;

	/**
	 * 调度信息列表（多条）
	 */
	@XStreamAlias("ExamineList")
	private List<ScheduleInfoReqExamineListVo> examineList;

	/**
	 * 是否自助理赔案件 1-是，0-否
	 */
	@XStreamAlias("SelfClaimFlag")
	private String selfClaimFlag;
	
	public String getClmNo() {
		return clmNo;
	}

	public void setClmNo(String clmNo) {
		this.clmNo = clmNo;
	}

	public Date getAttemperTime() {
		return attemperTime;
	}

	public void setAttemperTime(Date attemperTime) {
		this.attemperTime = attemperTime;
	}

	public List<ScheduleInfoReqExamineListVo> getExamineList() {
		return examineList;
	}

	public void setExamineList(List<ScheduleInfoReqExamineListVo> examineList) {
		this.examineList = examineList;
	}

	public String getSelfClaimFlag() {
		return selfClaimFlag;
	}

	public void setSelfClaimFlag(String selfClaimFlag) {
		this.selfClaimFlag = selfClaimFlag;
	}

	
}
