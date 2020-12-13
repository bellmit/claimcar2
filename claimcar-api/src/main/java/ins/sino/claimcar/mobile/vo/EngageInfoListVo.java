package ins.sino.claimcar.mobile.vo;


import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ENGAGEINFO")
public class EngageInfoListVo  implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/*
	 * 特别约定名称
	 */
	@XStreamAlias("CLAUSESNAME")
	private String clausesName;
	
	/*
	 * 特别约定内容
	 */
	@XStreamAlias("CLAUSES")
	private String clauses;

	public String getClausesName() {
		return clausesName;
	}

	public void setClausesName(String clausesName) {
		this.clausesName = clausesName;
	}

	public String getClauses() {
		return clauses;
	}

	public void setClauses(String clauses) {
		this.clauses = clauses;
	}
	
	
}
