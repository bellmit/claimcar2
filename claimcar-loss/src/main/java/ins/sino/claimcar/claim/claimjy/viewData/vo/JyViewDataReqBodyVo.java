package ins.sino.claimcar.claim.claimjy.viewData.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class JyViewDataReqBodyVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@XStreamAlias("EvalLossInfo") 
    private EvalLossInfo evalLossInfo;
	public EvalLossInfo getEvalLossInfo() {
		return evalLossInfo;
	}
	public void setEvalLossInfo(EvalLossInfo evalLossInfo) {
		this.evalLossInfo = evalLossInfo;
	}
	
	
	 
}
