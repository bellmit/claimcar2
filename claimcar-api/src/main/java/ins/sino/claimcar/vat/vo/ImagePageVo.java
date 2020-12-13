package ins.sino.claimcar.vat.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("PAGE")
public class ImagePageVo implements Serializable{
 private static final long serialVersionUID = 1L;

@XStreamAlias("PAGEID")
@XStreamAsAttribute
private String pageId;

@XStreamAlias("FILE_NAME")
@XStreamAsAttribute
private String fileName;

@XStreamAlias("PAGE_URL")
@XStreamAsAttribute
private String pageUrl;

@XStreamAlias("THUM_URL")
@XStreamAsAttribute
private String thumUrl;

@XStreamAlias("PAGE_LONGURL")
@XStreamAsAttribute
private String pageLongurl;

@XStreamAlias("THUM_LONGURL")
@XStreamAsAttribute
private String thumLongurl;

@XStreamAlias("FILE_NO")
@XStreamAsAttribute
private String fileNo;

public String getPageId() {
	return pageId;
}

public void setPageId(String pageId) {
	this.pageId = pageId;
}

public String getFileName() {
	return fileName;
}

public void setFileName(String fileName) {
	this.fileName = fileName;
}

public String getPageUrl() {
	return pageUrl;
}

public void setPageUrl(String pageUrl) {
	this.pageUrl = pageUrl;
}

public String getThumUrl() {
	return thumUrl;
}

public void setThumUrl(String thumUrl) {
	this.thumUrl = thumUrl;
}

public String getPageLongurl() {
	return pageLongurl;
}

public void setPageLongurl(String pageLongurl) {
	this.pageLongurl = pageLongurl;
}

public String getThumLongurl() {
	return thumLongurl;
}

public void setThumLongurl(String thumLongurl) {
	this.thumLongurl = thumLongurl;
}

public String getFileNo() {
	return fileNo;
}

public void setFileNo(String fileNo) {
	this.fileNo = fileNo;
}



}
