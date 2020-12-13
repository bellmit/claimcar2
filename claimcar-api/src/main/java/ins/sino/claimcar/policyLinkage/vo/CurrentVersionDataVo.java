/******************************************************************************
 * CREATETIME : 2016年10月18日 下午8:23:18
 ******************************************************************************/
package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★Luwei
 */
@XStreamAlias("data")
public class CurrentVersionDataVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@XStreamAlias("lastupdatetime")
	private String lastupdatetime;

	@XStreamAlias("version")
	private String version;

	/**
	 * @return 返回 lastupdatetime。
	 */
	public String getLastupdatetime() {
		return lastupdatetime;
	}

	/**
	 * @param lastupdatetime 要设置的 lastupdatetime。
	 */
	public void setLastupdatetime(String lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}

	/**
	 * @return 返回 version。
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version 要设置的 version。
	 */
	public void setVersion(String version) {
		this.version = version;
	}

}
