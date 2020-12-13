package ins.sino.claimcar.sunyardimage.vo.response;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("PAGE_EXT")
public class ResPageExtVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamImplicit(itemFieldName="EXT_ATTR")
	private List<ResExtAttrVo> extAttrVo;

	public List<ResExtAttrVo> getExtAttrVo() {
		return extAttrVo;
	}

	public void setExtAttrVo(List<ResExtAttrVo> extAttrVo) {
		this.extAttrVo = extAttrVo;
	}

	

}
