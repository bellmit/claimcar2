package ins.sino.claimcar.pinganVo;

public class PersonCheckResultVo {

    private String code;
    private String msg;
    private RespPersonCheckDataVo respData;
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

    public void setRespData(RespPersonCheckDataVo respData) {
         this.respData = respData;
     }
     public RespPersonCheckDataVo getRespData() {
         return respData;
     }

}