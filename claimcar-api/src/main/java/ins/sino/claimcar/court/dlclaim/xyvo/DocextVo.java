package ins.sino.claimcar.court.dlclaim.xyvo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("DOC_EXT")
public class DocextVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("EXT_ATTR")
	private ExtAttrVo extAttrVo;
	public ExtAttrVo getExtAttrVo() {
		return extAttrVo;
	}
	public void setExtAttrVo(ExtAttrVo extAttrVo) {
		this.extAttrVo = extAttrVo;
	}
    
}
