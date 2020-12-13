package ins.sino.claimcar.court.dlclaim.xyvo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("PAGE_EXT")
public class PageExtVo {
	@XStreamImplicit(itemFieldName="EXT_ATTR")
	private List<ExtAttrSonVo> extAttrSonVos;

	public List<ExtAttrSonVo> getExtAttrSonVos() {
		return extAttrSonVos;
	}

	public void setExtAttrSonVos(List<ExtAttrSonVo> extAttrSonVos) {
		this.extAttrSonVos = extAttrSonVos;
	}
	

}
