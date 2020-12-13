package ins.sino.claimcar.carchild.check.vo;


import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class CTCheckInfoSubmitReqBodyVo  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CHECKTASKINFO")
	private CTCheckInfoSubmitVo checkInfoSubmitVo;
	
	@XStreamAlias("PRPLCERTIFYITEMLIST")
    private List<CTCertifyItem>  certifyItemlist; //单证目录

	public CTCheckInfoSubmitVo getCheckInfoSubmitVo() {
		return checkInfoSubmitVo;
	}

	public void setCheckInfoSubmitVo(CTCheckInfoSubmitVo checkInfoSubmitVo) {
		this.checkInfoSubmitVo = checkInfoSubmitVo;
	}

	public List<CTCertifyItem> getCertifyItemlist() {
		return certifyItemlist;
	}

	public void setCertifyItemlist(List<CTCertifyItem> certifyItemlist) {
		this.certifyItemlist = certifyItemlist;
	}
	
	
}
