package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 查勘员号码更新请求vo
 * 
 * @author zjd
 *
 */
@XStreamAlias("OUTDATE")
public class CallPhoneOutdateReq implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	//报案号
	@XStreamAlias("ClmNo")
	private String clmNo = "";
	//更新的查勘员电话
	@XStreamAlias("ExaminePhone")
	private String examinePhone = "";
	public String getClmNo() {
		return clmNo;
	}
	public void setClmNo(String clmNo) {
		this.clmNo = clmNo;
	}
	public String getExaminePhone() {
		return examinePhone;
	}
	public void setExaminePhone(String examinePhone) {
		this.examinePhone = examinePhone;
	}

	
}
