package ins.sino.claimcar.subrogation.sh.vo;

import java.io.Serializable;

/**
 * 上海代位查询页面Vo
 * 
 * @author ★Luwei
 */
public class SubrogationSHQueryVo implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	private String comCode;// 机构代码

	private String registNo;// 报案号

	private String claimNo;// 立案号

	private String claimSeqNo;// 理赔编码

	
	// setters and getters
	
	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getRegistNo() {
		return registNo;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getClaimSeqNo() {
		return claimSeqNo;
	}

	public void setClaimSeqNo(String claimSeqNo) {
		this.claimSeqNo = claimSeqNo;
	}

}
