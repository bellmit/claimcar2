package ins.sino.claimcar.mtainterface.check.vo;

import ins.sino.claimcar.carchild.check.vo.CTCertifyItem;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class MTACheckInfoSubmitReqBodyVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CHECKTASKINFO")
	private MTACheckInfoSubmitVo checkInfoSubmitVo;
	
	@XStreamAlias("PRPLCERTIFYITEMLIST")
    private List<CTCertifyItem>  certifyItemlist; //单证目录

	public MTACheckInfoSubmitVo getCheckInfoSubmitVo() {
		return checkInfoSubmitVo;
	}

	public void setCheckInfoSubmitVo(MTACheckInfoSubmitVo checkInfoSubmitVo) {
		this.checkInfoSubmitVo = checkInfoSubmitVo;
	}

	public List<CTCertifyItem> getCertifyItemlist() {
		return certifyItemlist;
	}

	public void setCertifyItemlist(List<CTCertifyItem> certifyItemlist) {
		this.certifyItemlist = certifyItemlist;
	}
	
}
