package ins.sino.claimcar.court.dlclaim.xyvo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("root")
public class ImageUrlbaseVo implements Serializable{
private static final long serialVersionUID = 1L;

@XStreamAlias("RESPONSE_CODE")
private String response_code;//返回结果代码
@XStreamAlias("RESPONSE_MSG")
private String response_msg;//返回结果信息
@XStreamAlias("PAGES")
private List<PageVo> pagesVo;
@XStreamAlias("SYD")
private SydVo sydVo;
public String getResponse_code() {
	return response_code;
}
public void setResponse_code(String response_code) {
	this.response_code = response_code;
}
public String getResponse_msg() {
	return response_msg;
}
public void setResponse_msg(String response_msg) {
	this.response_msg = response_msg;
}

public List<PageVo> getPagesVo() {
	return pagesVo;
}
public void setPagesVo(List<PageVo> pagesVo) {
	this.pagesVo = pagesVo;
}
public SydVo getSydVo() {
	return sydVo;
}
public void setSydVo(SydVo sydVo) {
	this.sydVo = sydVo;
}


	
}
