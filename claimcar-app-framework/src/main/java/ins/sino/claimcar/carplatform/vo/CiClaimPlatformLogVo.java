package ins.sino.claimcar.carplatform.vo;

import java.util.Date;


/**
 * Custom VO class of PO CiClaimPlatformLog
 */
public class CiClaimPlatformLogVo extends CiClaimPlatformLogVoBase implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// 分页
	private Integer start;// 记录起始位置
	private Integer length;// 记录数

	private String validNo;// 投保确认码

	private Date requestTimeStart;// 操作开始日期
	private Date requestTimeEnd;// 操作日期
	private String userCode;

	public String getValidNo() {
		return this.validNo;
	}

	public void setValidNo(String validNo) {
		this.validNo = validNo;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	/**
	 * @return 返回 requestTimeStart。
	 */
	public Date getRequestTimeStart() {
		return requestTimeStart;
	}

	/**
	 * @param requestTimeStart 要设置的 requestTimeStart。
	 */
	public void setRequestTimeStart(Date requestTimeStart) {
		this.requestTimeStart = requestTimeStart;
	}

	/**
	 * @return 返回 requestTimeEnd。
	 */
	public Date getRequestTimeEnd() {
		return requestTimeEnd;
	}

	/**
	 * @param requestTimeEnd 要设置的 requestTimeEnd。
	 */
	public void setRequestTimeEnd(Date requestTimeEnd) {
		this.requestTimeEnd = requestTimeEnd;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
}
