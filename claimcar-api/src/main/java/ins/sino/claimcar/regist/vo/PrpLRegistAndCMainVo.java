package ins.sino.claimcar.regist.vo;

import java.util.List;


/**
 * Custom VO class of PO PrpLRegist
 */ 
public class PrpLRegistAndCMainVo implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private List<PrpLCMainVo> prpLCMainVo;
	private List<PrpLRegistVo> prpLRegistVo;
	public List<PrpLCMainVo> getPrpLCMainVo() {
		return prpLCMainVo;
	}
	public void setPrpLCMainVo(List<PrpLCMainVo> prpLCMainVo) {
		this.prpLCMainVo = prpLCMainVo;
	}
	public List<PrpLRegistVo> getPrpLRegistVo() {
		return prpLRegistVo;
	}
	public void setPrpLRegistVo(List<PrpLRegistVo> prpLRegistVo) {
		this.prpLRegistVo = prpLRegistVo;
	}
}
