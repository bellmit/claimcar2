package ins.sino.claimcar.claimcarYJ.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class DLhkReqBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("LDLHKMAIN")
	private PrpLDlhkMainVo prpLDlhkMainVo;  //复勘主要内容

	public PrpLDlhkMainVo getPrpLDlhkMainVo() {
		return prpLDlhkMainVo;
	}

	public void setPrpLDlhkMainVo(PrpLDlhkMainVo prpLDlhkMainVo) {
		this.prpLDlhkMainVo = prpLDlhkMainVo;
	}

	
}
