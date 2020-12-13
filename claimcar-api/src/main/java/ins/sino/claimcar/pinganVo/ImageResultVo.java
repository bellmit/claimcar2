/**
  * Copyright 2020 bejson.com 
  */
package ins.sino.claimcar.pinganVo;
import java.util.List;

/**
 * Auto-generated: 2020-07-20 17:38:16
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ImageResultVo {

    private String code;
    private String msg;
    private List<RespImageDataVo> respData;
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

    public void setRespData(List<RespImageDataVo> respData) {
         this.respData = respData;
     }
     public List<RespImageDataVo> getRespData() {
         return respData;
     }

}