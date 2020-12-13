package ins.sino.claimcar.moblie.loss.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class InitDeflossResBody implements Serializable {

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("CERTAINSTASKINFO")
	private CertainsTaskInfoVo certainsTaskInfoVo;

	public CertainsTaskInfoVo getCertainsTaskInfoVo() {
		return certainsTaskInfoVo;
	}

	public void setCertainsTaskInfoVo(CertainsTaskInfoVo certainsTaskInfoVo) {
		this.certainsTaskInfoVo = certainsTaskInfoVo;
	}
	
}
