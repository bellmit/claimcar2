/******************************************************************************
 * CREATETIME : 2016年10月18日 下午8:20:31
 ******************************************************************************/
package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★Luwei
 */
@XStreamAlias("info")
public class CurrentVersionInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@XStreamAlias("data")
	private CurrentVersionDataVo CurrentVersionDataVo;

	/**
	 * @return 返回 currentVersionDataVo。
	 */
	public CurrentVersionDataVo getCurrentVersionDataVo() {
		return CurrentVersionDataVo;
	}

	/**
	 * @param currentVersionDataVo 要设置的 currentVersionDataVo。
	 */
	public void setCurrentVersionDataVo(CurrentVersionDataVo currentVersionDataVo) {
		CurrentVersionDataVo = currentVersionDataVo;
	}

}
