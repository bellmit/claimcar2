package ins.sino.claimcar.carchildren.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class CaseCancleNopassReqBodyVo {
   @XStreamAlias("REGISTNO")
   private String registNo;
   @XStreamAlias("USERCODE")
   private String usercode;
   @XStreamAlias("REVIEWDATE")
   private String reviewDate;
   @XStreamAlias("REMARK")
   private String remark;
   @XStreamAlias("TIMESTAMP")
   private String timestamp;
   
   public String getRegistNo() {
	  return registNo;
  }
  public void setRegistNo(String registNo) {
	  this.registNo = registNo;
  }
  public String getUsercode() {
	  return usercode;
  }
  public void setUsercode(String usercode) {
	  this.usercode = usercode;
  }
  public String getReviewDate() {
	  return reviewDate;
  }
  public void setReviewDate(String reviewDate) {
	  this.reviewDate = reviewDate;
  }
  public String getRemark() {
	return remark;
  }
  public void setRemark(String remark) {
	  this.remark = remark;
  }
  public String getTimestamp() {
	return timestamp;
  }
  public void setTimestamp(String timestamp) {
	 this.timestamp = timestamp;
  }
   
  
}
