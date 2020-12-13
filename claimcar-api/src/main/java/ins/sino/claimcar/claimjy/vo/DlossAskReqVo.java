package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class DlossAskReqVo implements Serializable{	
	private static final long serialVersionUID = 1L;
	@XStreamAlias("HEAD")
	private JyReqHeadVo jyReqHeadVo;
	@XStreamAlias("BODY")
	private DlossAskReqBodyVo dlossAskReqBodyVo;
	
	public JyReqHeadVo getJyReqHeadVo() {
		return jyReqHeadVo;
	}
	public void setJyReqHeadVo(JyReqHeadVo jyReqHeadVo) {
		this.jyReqHeadVo = jyReqHeadVo;
	}
	public DlossAskReqBodyVo getDlossAskReqBodyVo() {
		return dlossAskReqBodyVo;
	}
	public void setDlossAskReqBodyVo(DlossAskReqBodyVo dlossAskReqBodyVo) {
		this.dlossAskReqBodyVo = dlossAskReqBodyVo;
	}
}
