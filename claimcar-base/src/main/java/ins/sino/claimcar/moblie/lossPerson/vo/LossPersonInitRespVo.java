package ins.sino.claimcar.moblie.lossPerson.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 移动查勘人伤跟踪初始化，返回报文
 * @author j2eel
 *
 */
@XStreamAlias("PACKET")
public class LossPersonInitRespVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("HEAD")
	private MobileCheckResponseHead respHeadVo;

	@XStreamAlias("BODY")
	private LossPersonInitRespBodyVo respBodyVo;

	public MobileCheckResponseHead getRespHeadVo() {
		return respHeadVo;
	}

	public void setRespHeadVo(MobileCheckResponseHead respHeadVo) {
		this.respHeadVo = respHeadVo;
	}

	public LossPersonInitRespBodyVo getRespBodyVo() {
		return respBodyVo;
	}

	public void setRespBodyVo(LossPersonInitRespBodyVo respBodyVo) {
		this.respBodyVo = respBodyVo;
	}
	
	
}
