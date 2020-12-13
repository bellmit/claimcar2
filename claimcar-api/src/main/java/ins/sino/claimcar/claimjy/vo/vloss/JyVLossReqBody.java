package ins.sino.claimcar.claimjy.vo.vloss;

import ins.sino.claimcar.claimjy.vo.price.JyPriceReqInfo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY") 
public class JyVLossReqBody implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ReqInfo") 
	private JyPriceReqInfo info;
	
	@XStreamAlias("EvalLossInfo") 
	private JyVLossReqEvalLossInfo lossInfo;

	public JyPriceReqInfo getInfo() {
		return info;
	}

	public void setInfo(JyPriceReqInfo info) {
		this.info = info;
	}

	public JyVLossReqEvalLossInfo getLossInfo() {
		return lossInfo;
	}

	public void setLossInfo(JyVLossReqEvalLossInfo lossInfo) {
		this.lossInfo = lossInfo;
	}
	
	

}
