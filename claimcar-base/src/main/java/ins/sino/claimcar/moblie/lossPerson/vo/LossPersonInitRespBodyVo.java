package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class LossPersonInitRespBodyVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CERTAINSTASKINFO")
	private CertainsLossPerTaskRequestVo taskInfoVo; 
	
	@XStreamAlias("PERSIONINFOLIST")
	private List<PersonInfoVos> personInfoVo;
	
	@XStreamAlias("FEELIST")
	private List<FeeInfoVos> feeInfoVo;
	

	public CertainsLossPerTaskRequestVo getTaskInfoVo() {
		return taskInfoVo;
	}

	public void setTaskInfoVo(CertainsLossPerTaskRequestVo taskInfoVo) {
		this.taskInfoVo = taskInfoVo;
	}

	public List<PersonInfoVos> getPersonInfoVo() {
		return personInfoVo;
	}

	public void setPersonInfoVo(List<PersonInfoVos> personInfoVo) {
		this.personInfoVo = personInfoVo;
	}

	public List<FeeInfoVos> getFeeInfoVo() {
		return feeInfoVo;
	}

	public void setFeeInfoVo(List<FeeInfoVos> feeInfoVoList) {
		this.feeInfoVo = feeInfoVoList;
	}
	
	
	

}
