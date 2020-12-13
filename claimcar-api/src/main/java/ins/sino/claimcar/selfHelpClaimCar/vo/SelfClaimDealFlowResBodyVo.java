package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class SelfClaimDealFlowResBodyVo {
	@XStreamAlias("TASKLIST")
	private List<TaskInfoVo> taskInfoVoList;

	public List<TaskInfoVo> getTaskInfoVoList() {
		return taskInfoVoList;
	}

	public void setTaskInfoVoList(List<TaskInfoVo> taskInfoVoList) {
		this.taskInfoVoList = taskInfoVoList;
	}

}
