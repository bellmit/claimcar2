package ins.sino.claimcar.claim.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Body")
public class RequestSdBodyVo implements Serializable{
    @XStreamAlias("BaseInfo")
	private BaseInfoSdVo baseInfoSdVo;

	public BaseInfoSdVo getBaseInfoSdVo() {
		return baseInfoSdVo;
	}

	public void setBaseInfoSdVo(BaseInfoSdVo baseInfoSdVo) {
		this.baseInfoSdVo = baseInfoSdVo;
	}
    
	
}
