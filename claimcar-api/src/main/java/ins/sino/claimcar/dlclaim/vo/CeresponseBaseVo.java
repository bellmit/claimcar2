package ins.sino.claimcar.dlclaim.vo;


import java.io.Serializable;

public class CeresponseBaseVo implements Serializable{

private static final long serialVersionUID = 1L;

private String code;//状态码
private String message;//返回信息

public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}

public static CeresponseBaseVo SUCCESS(String code,String message){
	CeresponseBaseVo ceresponseBaseVo = new CeresponseBaseVo();
	ceresponseBaseVo.setCode(code);
	ceresponseBaseVo.setMessage(message);
    
    return ceresponseBaseVo;
}

}
