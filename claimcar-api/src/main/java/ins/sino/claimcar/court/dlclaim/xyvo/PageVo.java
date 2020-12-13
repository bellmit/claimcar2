package ins.sino.claimcar.court.dlclaim.xyvo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("UAGE")
public class PageVo implements Serializable{
private static final long serialVersionUID = 1L;
@XStreamAlias("PAGEID")
@XStreamAsAttribute
private String pageId;

@XStreamAlias("PAGE_URL")
@XStreamAsAttribute
private String PageUrl;//图片原图地址

@XStreamAlias("THUM_URL")
@XStreamAsAttribute
private String ThumUrl;//图片缩略图地址

@XStreamAlias("FILE_NO")
@XStreamAsAttribute
private String FileNo;//图片编号

@XStreamAlias("PAGE_VER")
@XStreamAsAttribute
private String pagever;


public String getPageId() {
	return pageId;
}

public void setPageId(String pageId) {
	this.pageId = pageId;
}

public String getPageUrl() {
	return PageUrl;
}

public void setPageUrl(String pageUrl) {
	PageUrl = pageUrl;
}

public String getThumUrl() {
	return ThumUrl;
}

public void setThumUrl(String thumUrl) {
	ThumUrl = thumUrl;
}

public String getFileNo() {
	return FileNo;
}

public void setFileNo(String fileNo) {
	FileNo = fileNo;
}

public String getPagever() {
	return pagever;
}

public void setPagever(String pagever) {
	this.pagever = pagever;
}



}
