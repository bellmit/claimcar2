/**
  * Copyright 2020 bejson.com 
  */
package ins.sino.claimcar.pinganVo;


public class CheckQueryVo {

    private String insuranceCompanyNo;
    private String operatorUm;
    private ParamCheckObjVo paramObj;
    public void setInsuranceCompanyNo(String insuranceCompanyNo) {
         this.insuranceCompanyNo = insuranceCompanyNo;
     }
     public String getInsuranceCompanyNo() {
         return insuranceCompanyNo;
     }

    public void setOperatorUm(String operatorUm) {
         this.operatorUm = operatorUm;
     }
     public String getOperatorUm() {
         return operatorUm;
     }

    public void setParamObj(ParamCheckObjVo paramObj) {
         this.paramObj = paramObj;
     }
     public ParamCheckObjVo getParamObj() {
         return paramObj;
     }

}