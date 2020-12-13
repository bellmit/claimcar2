package ins.sino.claimcar.genilex.dlossReqVo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Entities")
public class EntitiesVo implements Serializable{
	private static final long serialVersionUID = 1L;

	@XStreamAlias("Evals")
	private List<EvalVo> evalVos;//定核损信息列表
	
	@XStreamAlias("ClaimMains")
	private List<ClaimMainVo> claimMains;//赔案主档信息

	public List<EvalVo> getEvalVos() {
		return evalVos;
	}

	public void setEvalVos(List<EvalVo> evalVos) {
		this.evalVos = evalVos;
	}

	public List<ClaimMainVo> getClaimMains() {
		return claimMains;
	}

	public void setClaimMains(List<ClaimMainVo> claimMains) {
		this.claimMains = claimMains;
	}
	
	
  
}
