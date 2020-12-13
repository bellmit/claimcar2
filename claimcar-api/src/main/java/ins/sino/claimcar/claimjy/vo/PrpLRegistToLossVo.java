package ins.sino.claimcar.claimjy.vo;



import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author ★LiYi
 */
@XStreamAlias("PACKET")
public class PrpLRegistToLossVo implements Serializable {

	@XStreamAlias("HEAD")
	private JyReqHeadVo jyReqHeadVo ;
	@XStreamAlias("BODY")
	private PrpLRegistToLossBodyVo prpLRegistToLossBodyVo;
	
	public JyReqHeadVo getJyReqHeadVo() {
		return jyReqHeadVo;
	}

	public void setJyReqHeadVo(JyReqHeadVo jyReqHeadVo) {
		this.jyReqHeadVo = jyReqHeadVo;
	}

	public PrpLRegistToLossBodyVo getPrpLRegistToLossBodyVo() {
		return prpLRegistToLossBodyVo;
	}

	public void setPrpLRegistToLossBodyVo(PrpLRegistToLossBodyVo prpLRegistToLossBodyVo) {
		this.prpLRegistToLossBodyVo = prpLRegistToLossBodyVo;
	}

	public PrpLRegistToLossVo(){
		super();
	}
}

