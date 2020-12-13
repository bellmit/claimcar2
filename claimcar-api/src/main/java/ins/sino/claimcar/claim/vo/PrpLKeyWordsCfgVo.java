package ins.sino.claimcar.claim.vo;

import java.io.Serializable;

/**
 * PrpLKeyWordsCfg 对应的VO
 * 
 * @author ffsz
 *
 */
public class PrpLKeyWordsCfgVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 关键字
	 */
	private String keyword;
	/**
	 * 是否有效标识 1-有效 0-无效
	 */
	private String status;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
