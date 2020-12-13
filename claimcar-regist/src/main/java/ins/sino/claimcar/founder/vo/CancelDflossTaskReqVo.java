/******************************************************************************
 * CREATETIME : 2016年8月4日 下午7:03:23
 ******************************************************************************/
package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * <pre>定损任务注销</pre>
 * @author ★Luwei
 */
@XStreamAlias("PACKET")
public class CancelDflossTaskReqVo {

	@XStreamAsAttribute
	private String type = "REQUEST";

	@XStreamAsAttribute
	private String version = "1.0";

	@XStreamAlias("HEAD")
	private CommonReqHeadVo head;

	@XStreamAlias("BODY")
	private CancelDflossTaskBodyVo bodyVo;

	/**
	 * @return 返回 type。
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type 要设置的 type。
	 */
	public void setType(String type) {
		this.type = type;
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

	/**
	 * @return 返回 head。
	 */
	public CommonReqHeadVo getHead() {
		return head;
	}

	/**
	 * @param head 要设置的 head。
	 */
	public void setHead(CommonReqHeadVo head) {
		this.head = head;
	}

	/**
	 * @return 返回 bodyVo。
	 */
	public CancelDflossTaskBodyVo getBodyVo() {
		return bodyVo;
	}

	/**
	 * @param bodyVo 要设置的 bodyVo。
	 */
	public void setBodyVo(CancelDflossTaskBodyVo bodyVo) {
		this.bodyVo = bodyVo;
	}

}
