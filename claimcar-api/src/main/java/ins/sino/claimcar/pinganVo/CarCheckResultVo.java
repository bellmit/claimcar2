/**
  * Copyright 2020 bejson.com 
  */
package ins.sino.claimcar.pinganVo;

/**
 * Auto-generated: 2020-07-20 11:53:53
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class CarCheckResultVo {

    private String code;
    private String msg;
    private RespCarCheckDataVo respData;
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

    public void setRespData(RespCarCheckDataVo respData) {
         this.respData = respData;
     }
     public RespCarCheckDataVo getRespData() {
         return respData;
     }

}