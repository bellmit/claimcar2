package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 手动调度返回查勘定损员信息接口 - 数据部分（快赔请求理赔）
 * @author zy
 *
 */
@XStreamAlias("SCHEDULEWF")
public class HandleScheduleBackReqScheduleWF implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 报案号 */
	@XStreamAlias("REGISTNO")
	private String registNo;
	
	/** 是否异地案件 */
	@XStreamAlias("ISELSEWHERE")
	private String isElseWhere;
	
	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getIsElseWhere() {
		return isElseWhere;
	}

	public void setIsElseWhere(String isElseWhere) {
		this.isElseWhere = isElseWhere;
	}
	
	
	
}
