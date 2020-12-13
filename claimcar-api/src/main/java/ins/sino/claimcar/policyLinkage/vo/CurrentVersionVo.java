/******************************************************************************
 * CREATETIME : 2016年10月18日 下午8:17:10
 ******************************************************************************/
package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author niuqiqang
 */
@XStreamAlias("result")
public class CurrentVersionVo implements Serializable{

	private static final long serialVersionUID = 1L;

	@XStreamAlias("code")
	private String code;

	@XStreamAlias("info")
	private CurrentVersionInfoVo resultInfo;

	@XStreamAlias("msg")
	private String resultMsg;

	/**
	 * @return 返回 code。
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code 要设置的 code。
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return 返回 resultInfo。
	 */
	public CurrentVersionInfoVo getResultInfo() {
		return resultInfo;
	}

	/**
	 * @param resultInfo 要设置的 resultInfo。
	 */
	public void setResultInfo(CurrentVersionInfoVo resultInfo) {
		this.resultInfo = resultInfo;
	}

	/**
	 * @return 返回 resultMsg。
	 */
	public String getResultMsg() {
		return resultMsg;
	}

	/**
	 * @param resultMsg 要设置的 resultMsg。
	 */
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

}
