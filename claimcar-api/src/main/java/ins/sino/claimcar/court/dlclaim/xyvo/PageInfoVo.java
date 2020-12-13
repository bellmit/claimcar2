package ins.sino.claimcar.court.dlclaim.xyvo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("PageInfo")
public class PageInfoVo {
@XStreamImplicit(itemFieldName = "PAGE")	
private List<PageSonNodeVo> pageSonVos;

public List<PageSonNodeVo> getPageSonVos() {
	return pageSonVos;
}

public void setPageSonVos(List<PageSonNodeVo> pageSonVos) {
	this.pageSonVos = pageSonVos;
}




	
}
