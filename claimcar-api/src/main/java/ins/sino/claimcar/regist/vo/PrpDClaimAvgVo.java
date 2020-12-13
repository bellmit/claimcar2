package ins.sino.claimcar.regist.vo;

import ins.sino.claimcar.regist.vo.PrpDClaimAvgVoBase;

/**
 * Custom VO class of PO PrpDClaimAvg
 */ 
public class PrpDClaimAvgVo extends PrpDClaimAvgVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	// 分页
	private Integer start;// 记录起始位置
	private Integer length;// 记录数
	
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	
	
}
