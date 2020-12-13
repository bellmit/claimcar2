package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class SubmitLossPerRequestBody implements Serializable{

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("CERTAINSTASKINFO")
	private CertainsLossPerTaskRequestVo certainsTaskRequestVo;
	
	@XStreamAlias("PERSIONINFOLIST")
	private List<PersonInfoVos> personInfoVo;
	
	@XStreamAlias("FEELISTS")
	private List<FeeInfoVo> feeInfos;//费用信息

	public CertainsLossPerTaskRequestVo getCertainsTaskRequestVo() {
		return certainsTaskRequestVo;
	}

	public void setCertainsTaskRequestVo(
			CertainsLossPerTaskRequestVo certainsTaskRequestVo) {
		this.certainsTaskRequestVo = certainsTaskRequestVo;
	}
	
	public List<PersonInfoVos> getPersonInfoVo() {
		return personInfoVo;
	}

	public void setPersonInfoVo(List<PersonInfoVos> personInfoVo) {
		this.personInfoVo = personInfoVo;
	}

	public List<FeeInfoVo> getFeeInfos() {
		return feeInfos;
	}

	public void setFeeInfos(List<FeeInfoVo> feeInfos) {
		this.feeInfos = feeInfos;
	}

	
	
	
}


