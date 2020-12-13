package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 调度信息更新接口vo（理赔请求客服系统）
 * @author Luwei
 */
@XStreamAlias("BODY")
public class ScheduleInfoReqBodyVo {

	@XStreamAlias("INPUTDATA")
	private ScheduleInfoReqInputDataVo inputData;

	public ScheduleInfoReqInputDataVo getInputData() {
		return inputData;
	}

	public void setInputData(ScheduleInfoReqInputDataVo inputData) {
		this.inputData = inputData;
	}

}
