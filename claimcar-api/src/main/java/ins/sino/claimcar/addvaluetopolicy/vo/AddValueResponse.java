package ins.sino.claimcar.addvaluetopolicy.vo;

public class AddValueResponse {
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

    public static AddValueResponse success(){
        AddValueResponse responseVo = new AddValueResponse();
        responseVo.setCode("200");
        responseVo.setMsg("响应成功");
        return responseVo;
    }

    public AddValueResponse fail(String code, String msg){
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
