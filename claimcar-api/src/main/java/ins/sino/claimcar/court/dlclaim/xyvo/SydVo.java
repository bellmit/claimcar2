package ins.sino.claimcar.court.dlclaim.xyvo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("SYD")
public class SydVo implements Serializable{
private static final long serialVersionUID = 1L;
@XStreamAlias("doc")
 private DocVo docVo;

public DocVo getDocVo() {
	return docVo;
}

public void setDocVo(DocVo docVo) {
	this.docVo = docVo;
}
 
}
