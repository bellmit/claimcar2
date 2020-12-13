package ins.sino.claimcar.sunyardimage.vo.response;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("DOC_EXT")
public class ResDocExtVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("EXT_ATTR")
	private ResExtAttrVo extAttrVo;

	
	public ResExtAttrVo getExtAttrVo() {
		return extAttrVo;
	}

	
	public void setExtAttrVo(ResExtAttrVo extAttrVo) {
		this.extAttrVo = extAttrVo;
	}
	
}
