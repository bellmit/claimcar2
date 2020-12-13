package ins.sino.claimcar.pinganUnion.enums;
/**
 * 平安联盟--影像代码
 * @author Think
 *
 */
public enum PingAnImageCodeEnum {
   CheckImage("000","CheckImage","查勘照片","claim-picture","车险理赔-损失图片"),
   CommonCertify("001","CommonCertify","公用单证","claim-certify","车险理赔-索赔清单"),
   RobberyNo("002","RobberyNo","盗抢未寻回","claim-certify","车险理赔-索赔清单"),
   RobberyYES("003","RobberyYES","盗抢寻回","claim-certify","车险理赔-索赔清单"),
   Subjectcar("004","Subjectcar","标的车损","claim-picture","车险理赔-损失图片"),
   Subjectprop("005","Subjectprop","标的物损","claim-picture","车险理赔-损失图片"),
   PerSonHurt("006","PerSonHurt","人伤","claim-picture","车险理赔-损失图片"),
   ThreeCar("007","ThreeCar","三者车损","claim-picture","车险理赔-损失图片"),
   Threeprop("008","Threeprop","三者物损","claim-picture","车险理赔-损失图片"),
   ManyPay("009","ManyPay","多次赔付","claim-certify","车险理赔-索赔清单"),
   padpay("010","padpay","垫付","claim-certify","车险理赔-索赔清单"),
   prepay("011","prepay","预付","claim-certify","车险理赔-索赔清单"),
   survey("012","survey","调查","claim-certify","车险理赔-索赔清单"),
   EsEndCase("013","EsEndCase","特殊结案类型","claim-certify","车险理赔-索赔清单"),
   Recovery("014","Recovery","追偿","claim-certify","车险理赔-索赔清单"),
   Residualvalue("015","Residualvalue","拍卖残值","claim-certify","车险理赔-索赔清单"),
   Compvalue("016","Compvalue","高价值配件残值","claim-certify","车险理赔-索赔清单"),
   RiskRefuse("024","RiskRefuse","风险排查","claim-certify","车险理赔-索赔清单"),
   AIPay("025","AIPay","AI生物支付","claim-certify","车险理赔-索赔清单"),
   surveyManage("026","surveyManage","调查信息管理","claim-certify","车险理赔-索赔清单"),
   LawyerCertificate("027","LawyerCertificate","律师认证证件","claim-certify","车险理赔-索赔清单"),
   surveyFee("030","surveyFee","调查费用","claim-certify","车险理赔-索赔清单"),
   paycertify("100","paycertify","支付类单证","claim-certify","车险理赔-索赔清单"),
   otherType("99","otherType","其它","claim-picture","车险理赔-损失图片");
   
   private String pingAnCode;
   private String selfCode;
   private String imageType;
   private String rootNode;
   private String rootName;
   
  /**
   * 私有构造函数
   * @param pingAnCode
   * @param selfCode
   * @param imageType
   */
  PingAnImageCodeEnum(String pingAnCode,String selfCode,String imageType,String rootNode,String rootName){
	   this.pingAnCode=pingAnCode;
	   this.selfCode=selfCode;
	   this.imageType=imageType;
	   this.rootNode=rootNode;
	   this.rootName=rootName;
   }

public String getPingAnCode() {
	return pingAnCode;
}

public void setPingAnCode(String pingAnCode) {
	this.pingAnCode = pingAnCode;
}

public String getSelfCode() {
	return selfCode;
}

public void setSelfCode(String selfCode) {
	this.selfCode = selfCode;
}

public String getImageType() {
	return imageType;
}

public void setImageType(String imageType) {
	this.imageType = imageType;
}

public String getRootNode() {
	return rootNode;
}

public void setRootNode(String rootNode) {
	this.rootNode = rootNode;
}

public String getRootName() {
	return rootName;
}

public void setRootName(String rootName) {
	this.rootName = rootName;
}

 public static PingAnImageCodeEnum getEnumType(String pingAnCode){
	 PingAnImageCodeEnum[] pingAnImageCodeEnums=PingAnImageCodeEnum.values();
	 for(PingAnImageCodeEnum codeEnum:pingAnImageCodeEnums){
		 if(codeEnum.getPingAnCode().equals(pingAnCode)){
			 return codeEnum;
		 }
	 }
	 return PingAnImageCodeEnum.otherType;
 }
}
