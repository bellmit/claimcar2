package ins.sino.claimcar.payment.detail.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("JFee")
public class JFeeVo {

	@XStreamAlias("JplanFee")
	private JplanFeeVo jplanFeeVo;// 收付主信息明细
	@XStreamAlias("JlinkAccount")
	private JlinkAccountVo jlinkAccountVo;// 收款直付账号明细
	public JplanFeeVo getJplanFeeVo() {
		return jplanFeeVo;
	}
	public void setJplanFeeVo(JplanFeeVo jplanFeeVo) {
		this.jplanFeeVo = jplanFeeVo;
	}
	public JlinkAccountVo getJlinkAccountVo() {
		return jlinkAccountVo;
	}
	public void setJlinkAccountVo(JlinkAccountVo jlinkAccountVo) {
		this.jlinkAccountVo = jlinkAccountVo;
	}
}
