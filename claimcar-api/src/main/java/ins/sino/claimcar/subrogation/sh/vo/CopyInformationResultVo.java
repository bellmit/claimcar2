package ins.sino.claimcar.subrogation.sh.vo;

import java.io.Serializable;

/**
 * 代位求偿信息抄回返回的信息(页面vo)
 * @author ★Luwei
 */
public class CopyInformationResultVo implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	private String responseCode;// 返回类型代码

	private String errorMessage;// 错误信息

	private CopyInformationSubrogationViewVo subrogationViewVo;// 代位求偿信息列表（多条）

	//setters and getters
	
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public CopyInformationSubrogationViewVo getSubrogationViewVo() {
		return subrogationViewVo;
	}

	public void setSubrogationViewVo(CopyInformationSubrogationViewVo subrogationViewVo) {
		this.subrogationViewVo = subrogationViewVo;
	}

}
