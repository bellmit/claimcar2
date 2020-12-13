package ins.sino.claimcar.moblie.loss.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class SubmitDeflossRequestBody implements Serializable{

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("CERTAINSTASKINFO")
	private CertainsTaskRequestVo certainsTaskRequestVo;

	public CertainsTaskRequestVo getCertainsTaskRequestVo() {
		return certainsTaskRequestVo;
	}

	public void setCertainsTaskRequestVo(CertainsTaskRequestVo certainsTaskRequestVo) {
		this.certainsTaskRequestVo = certainsTaskRequestVo;
	}
}
