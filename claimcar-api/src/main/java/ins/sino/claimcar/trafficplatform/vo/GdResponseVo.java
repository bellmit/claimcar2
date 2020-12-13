/******************************************************************************
* CREATETIME : 2018年9月10日 上午11:25:38
******************************************************************************/
package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * <pre></pre>
 * @author ★zhousm
 */
@XStreamAlias("Packet")
public class GdResponseVo {

	@XStreamAlias("Head")
	private ResponseHeadVo headVo;

	public ResponseHeadVo getHeadVo() {
		return headVo;
	}

	public void setHeadVo(ResponseHeadVo headVo) {
		this.headVo = headVo;
	}
}
