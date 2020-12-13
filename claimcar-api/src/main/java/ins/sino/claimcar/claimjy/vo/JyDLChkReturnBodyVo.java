package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("BODY")
public class JyDLChkReturnBodyVo implements Serializable {

	@XStreamAlias("EvalLossInfo")
	private JyDLChkEvalLossInfoVo evalLossInfoVo;

	@XStreamAlias("LossFitInfo")
	private JyDLChkLossFitInfoVo lossFitInfoVo;

	@XStreamAlias("LossRepairInfo")
	private JyDLChkLossRepairInfoVo lossRepairInfoVo;

	@XStreamAlias("LossRepairSumInfo")
	private JyDLChkLossRepairSumInfoVo lossRepairSumInfoVo;

	@XStreamAlias("LossOuterRepairInfo")
	private JyDLChkLossOuterRepairInfoVo lossOuterRepairInfoVo;

	@XStreamAlias("LossAssistInfo")
	private JyDLChkLossAssistInfoVo lossAssistInfoVo;

	public JyDLChkEvalLossInfoVo getEvalLossInfoVo() {
		return evalLossInfoVo;
	}

	public void setEvalLossInfoVo(JyDLChkEvalLossInfoVo evalLossInfoVo) {
		this.evalLossInfoVo = evalLossInfoVo;
	}

	public JyDLChkLossFitInfoVo getLossFitInfoVo() {
		return lossFitInfoVo;
	}

	public void setLossFitInfoVo(JyDLChkLossFitInfoVo lossFitInfoVo) {
		this.lossFitInfoVo = lossFitInfoVo;
	}

	public JyDLChkLossRepairInfoVo getLossRepairInfoVo() {
		return lossRepairInfoVo;
	}

	public void setLossRepairInfoVo(JyDLChkLossRepairInfoVo lossRepairInfoVo) {
		this.lossRepairInfoVo = lossRepairInfoVo;
	}

	public JyDLChkLossRepairSumInfoVo getLossRepairSumInfoVo() {
		return lossRepairSumInfoVo;
	}

	public void setLossRepairSumInfoVo(JyDLChkLossRepairSumInfoVo lossRepairSumInfoVo) {
		this.lossRepairSumInfoVo = lossRepairSumInfoVo;
	}

	public JyDLChkLossOuterRepairInfoVo getLossOuterRepairInfoVo() {
		return lossOuterRepairInfoVo;
	}

	public void setLossOuterRepairInfoVo(JyDLChkLossOuterRepairInfoVo lossOuterRepairInfoVo) {
		this.lossOuterRepairInfoVo = lossOuterRepairInfoVo;
	}

	public JyDLChkLossAssistInfoVo getLossAssistInfoVo() {
		return lossAssistInfoVo;
	}

	public void setLossAssistInfoVo(JyDLChkLossAssistInfoVo lossAssistInfoVo) {
		this.lossAssistInfoVo = lossAssistInfoVo;
	}

	public JyDLChkReturnBodyVo(){
		super();
	}

}
