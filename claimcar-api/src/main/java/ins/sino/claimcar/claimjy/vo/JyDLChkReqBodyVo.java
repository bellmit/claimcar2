package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("BODY")
public class JyDLChkReqBodyVo implements Serializable {

	@XStreamAlias("ReqInfo")
	private JyDLChkReqBodyReqInfoVo reqInfoVo;
	@XStreamAlias("EvalLossInfo")
	private JyDLChkReqBodyEvalLossInfoVo evalLossInfo;

	public JyDLChkReqBodyReqInfoVo getReqInfoVo() {
		return reqInfoVo;
	}

	public void setReqInfoVo(JyDLChkReqBodyReqInfoVo reqInfoVo) {
		this.reqInfoVo = reqInfoVo;
	}

	public JyDLChkReqBodyEvalLossInfoVo getEvalLossInfo() {
		return evalLossInfo;
	}

	public void setEvalLossInfo(JyDLChkReqBodyEvalLossInfoVo evalLossInfo) {
		this.evalLossInfo = evalLossInfo;
	}

	public JyDLChkReqBodyVo(){
		super();
	}


}
