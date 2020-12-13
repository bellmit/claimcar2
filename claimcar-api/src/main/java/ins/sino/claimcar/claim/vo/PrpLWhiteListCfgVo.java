package ins.sino.claimcar.claim.vo;

import java.io.Serializable;

/**
 * PrpLWhiteListCfg 对应VO
 * 
 * @author ffsz
 *
 */
public class PrpLWhiteListCfgVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	主键ID
	 */
	private Long id;

	/**
	 * 	白名单URL
	 */
	private String url;

	/**
	 * 	拦截的关键字
	 */
	private String keywords;

	/**
	 * 	是否有效标志，1:有效，0:无效
	 */
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
