package ins.sino.claimcar.moblie.lossPerson.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.moblie.loss.vo.InitDeflossReqBody;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 移动查勘人伤初始化,请求报文
 * @author j2eel
 *
 */
@XStreamAlias("PACKET")
public class LossPersonInitReqVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD")
	private MobileCheckHead reqHeadVo;
	
	@XStreamAlias("BODY")
	private InitDeflossReqBody reqBodyVo;

	public MobileCheckHead getReqHeadVo() {
		return reqHeadVo;
	}

	public void setReqHeadVo(MobileCheckHead reqHeadVo) {
		this.reqHeadVo = reqHeadVo;
	}

	public InitDeflossReqBody getReqBodyVo() {
		return reqBodyVo;
	}

	public void setReqBodyVo(InitDeflossReqBody reqBodyVo) {
		this.reqBodyVo = reqBodyVo;
	}
	
	

}
