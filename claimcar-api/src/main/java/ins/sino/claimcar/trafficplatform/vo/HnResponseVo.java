/******************************************************************************
* CREATETIME : 2018年9月12日 下午5:02:08
******************************************************************************/
package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * <pre></pre>
 * @author ★zhousm
 */
@XStreamAlias("Packet")
public class HnResponseVo {

	@XStreamAlias("Head")
	private ResponseHeadVo headVo;

	public ResponseHeadVo getHeadVo() {
		return headVo;
	}

	public void setHeadVo(ResponseHeadVo headVo) {
		this.headVo = headVo;
	}
}
