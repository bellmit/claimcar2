package ins.sino.claimcar.pinganUnion.vo;

import java.io.Serializable;

/**
 * @Description 返回对象
 * @Author liuys
 * @Date 2020/7/20 11:57
 */
public class ResponseVo implements Serializable {
    private String code;
    private String msg;
    private Object respData;
    private String signature;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static ResponseVo success(){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setCode("200");
        responseVo.setMsg("响应成功");
        return responseVo;
    }

    public ResponseVo fail(String code, String msg){
        this.code = code;
        this.msg = msg;
        return this;
    }

    public Object getRespData() {
        return respData;
    }

    public void setRespData(Object respData) {
        this.respData = respData;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
