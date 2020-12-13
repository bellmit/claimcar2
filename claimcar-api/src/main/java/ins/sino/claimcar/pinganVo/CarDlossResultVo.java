package ins.sino.claimcar.pinganVo;



public class CarDlossResultVo {

    private String code;
    private String msg;
    private RespCarDlossDataVo respData;
    public void setCode(String code) {
         this.code = code;
     }
     public String getCode() {
         return code;
     }

    public void setMsg(String msg) {
         this.msg = msg;
     }
     public String getMsg() {
         return msg;
     }

    public void setRespData(RespCarDlossDataVo respData) {
         this.respData = respData;
     }
     public RespCarDlossDataVo getRespData() {
         return respData;
     }

}