package ins.sino.claimcar.pinganVo;
public class CarPersonDlossQueryVo {

    private String insuranceCompanyNo;
    private String operatorUm;
    private ParamCarDlossObjVo paramObj;
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

    public void setParamObj(ParamCarDlossObjVo paramObj) {
         this.paramObj = paramObj;
     }
     public ParamCarDlossObjVo getParamObj() {
         return paramObj;
     }

}