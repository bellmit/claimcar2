package ins.sino.claimcar.moblie.certify.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class CertifyBody implements Serializable{
	/**  */
	private static final long serialVersionUID = -4623864062836013585L;
	
	@XStreamAlias("PRPLCERTIFYITEMLIST")
    private List<CertifyItem>  certifyItemlist; //单证目录

    
    public List<CertifyItem> getCertifyItemlist() {
        return certifyItemlist;
    }

    
    public void setCertifyItemlist(List<CertifyItem> certifyItemlist) {
        this.certifyItemlist = certifyItemlist;
    }
    

	
	
}
