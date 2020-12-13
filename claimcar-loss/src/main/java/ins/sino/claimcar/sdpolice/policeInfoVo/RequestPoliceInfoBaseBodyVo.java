package ins.sino.claimcar.sdpolice.policeInfoVo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("BaseInfo")
public class RequestPoliceInfoBaseBodyVo {
	@XStreamAlias("ClaimSequenceNo")
	private String claimSequenceNo;//理赔编号
	@XStreamAlias("WarnStage")
	private String warnStage;//理赔阶段
	@XStreamImplicit(itemFieldName="WarnInfo")
	private List<WarnInfoVo> warnInfoList;//预警信息
	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}
	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
	}
	public String getWarnStage() {
		return warnStage;
	}
	public void setWarnStage(String warnStage) {
		this.warnStage = warnStage;
	}
	public List<WarnInfoVo> getWarnInfoList() {
		return warnInfoList;
	}
	public void setWarnInfoList(List<WarnInfoVo> warnInfoList) {
		this.warnInfoList = warnInfoList;
	}
	

}
