package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class DlossReceiveResVo implements Serializable{	
	private static final long serialVersionUID = 1L;
	@XStreamAlias("HEAD")
	
	private JyResHeadVo jyResHeadVo;
	public JyResHeadVo getJyResHeadVo() {
		return jyResHeadVo;
	}
	public void setJyResHeadVo(JyResHeadVo jyResHeadVo) {
		this.jyResHeadVo = jyResHeadVo;
	}
}
