package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 获取查勘定损员信息接口-返回数据部分（快赔请求理赔）
 * @author zy
 *
 */
@XStreamAlias("SCHEDULEWF")
public class PersonnelInformationResScheduleWF implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 报案号 */
	@XStreamAlias("REGISTNO")
	private String registNo;
	
	/** 类型 */
	@XStreamAlias("TYPE")
	private String type;
	
	/** 类型 */
	@XStreamAlias("ISELSEWHERE")
	private String isElseWhere;
	

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsElseWhere() {
		return isElseWhere;
	}

	public void setIsElseWhere(String isElseWhere) {
		this.isElseWhere = isElseWhere;
	}
	
	
	
}
