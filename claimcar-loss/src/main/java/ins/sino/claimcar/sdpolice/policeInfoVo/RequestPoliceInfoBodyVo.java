package ins.sino.claimcar.sdpolice.policeInfoVo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Body")
public class RequestPoliceInfoBodyVo {
	@XStreamAlias("BaseInfo")
	private RequestPoliceInfoBaseBodyVo baseBodyVo;

	public RequestPoliceInfoBaseBodyVo getBaseBodyVo() {
		return baseBodyVo;
	}

	public void setBaseBodyVo(RequestPoliceInfoBaseBodyVo baseBodyVo) {
		this.baseBodyVo = baseBodyVo;
	}
	

}
