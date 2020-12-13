package ins.sino.claimcar.claim.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("BaseInfo")
public class BaseInfoSdVo implements Serializable{

  @XStreamAlias("ClaimSequenceNo")
  private String claimSequenceNo;
  @XStreamAlias("WarnStage")
  private String warnStage;
  @XStreamImplicit(itemFieldName = "WarnInfo")
  private List<WarnInfoSdVo> warnInfoSdVos;
public String getClaimSequenceNo() {
	return claimSequenceNo;
}
public void setClaimSequenceNo(String claimSequenceNo) {
	this.claimSequenceNo = claimSequenceNo;
}
public String getWarnStage() {
	return warnStage;
}
public void setWarnStage(String warnStage) {
	this.warnStage = warnStage;
}
public List<WarnInfoSdVo> getWarnInfoSdVos() {
	return warnInfoSdVos;
}
public void setWarnInfoSdVos(List<WarnInfoSdVo> warnInfoSdVos) {
	this.warnInfoSdVos = warnInfoSdVos;
}
  
	
}
