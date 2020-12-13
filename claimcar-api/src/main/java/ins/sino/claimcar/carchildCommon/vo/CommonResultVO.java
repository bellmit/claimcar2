package ins.sino.claimcar.carchildCommon.vo;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

public class CommonResultVO<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	public String resultCode = null;
	public String resultMsg = null;
	public String requestCode = null;
	public T resultObject = null;

	public CommonResultVO() {
	}

	public CommonResultVO(String resultCode, String resultMsg) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	public CommonResultVO(T obj) {
		super();
		this.resultObject = obj;
	}

	@XmlTransient
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	@XmlTransient
	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	@XmlTransient
	public T getResultObject() {
		return resultObject;
	}

	public void setResultObject(T resultObject) {
		this.resultObject = resultObject;
	}
}

