package ins.sino.claimcar.court.dlclaim.xyvo;



import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
@XStreamAlias("doc")
public class DocVo implements Serializable{
private static final long serialVersionUID = 1L;
@XStreamAlias("version")
@XStreamAsAttribute
private String version;
@XStreamAlias("DocInfo")
private DocInfoVo docInfoVo;
@XStreamAlias("PageInfo")
private PageInfoVo pageInfoVo;
@XStreamAlias("VTREE")
private VtreeImageVo vtreeImageVo;

public String getVersion() {
	return version;
}
public void setVersion(String version) {
	this.version = version;
}
public DocInfoVo getDocInfoVo() {
	return docInfoVo;
}
public void setDocInfoVo(DocInfoVo docInfoVo) {
	this.docInfoVo = docInfoVo;
}
public PageInfoVo getPageInfoVo() {
	return pageInfoVo;
}
public void setPageInfoVo(PageInfoVo pageInfoVo) {
	this.pageInfoVo = pageInfoVo;
}
public VtreeImageVo getVtreeImageVo() {
	return vtreeImageVo;
}
public void setVtreeImageVo(VtreeImageVo vtreeImageVo) {
	this.vtreeImageVo = vtreeImageVo;
}


}
