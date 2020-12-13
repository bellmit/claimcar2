package ins.sino.claimcar.claimjy.vo.price;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("BODY") 
public class JyPriceReqBody implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ReqInfo") 
	private JyPriceReqInfo info;
	
	@XStreamAlias("EvalLossInfo") 
	private JyPriceReqEvalLossInfo lossInfo;

	public JyPriceReqInfo getInfo() {
		return info;
	}

	public void setInfo(JyPriceReqInfo info) {
		this.info = info;
	}

	public JyPriceReqEvalLossInfo getLossInfo() {
		return lossInfo;
	}

	public void setLossInfo(JyPriceReqEvalLossInfo lossInfo) {
		this.lossInfo = lossInfo;
	}
	
	

}
