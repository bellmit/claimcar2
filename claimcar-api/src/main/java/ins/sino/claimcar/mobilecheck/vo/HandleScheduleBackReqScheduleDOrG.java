package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 手动调度返回查勘定损员信息接口 - 数据部分（快赔请求理赔）
 * @author zy
 *
 */
public class HandleScheduleBackReqScheduleDOrG implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 报案号 */
	@XStreamAlias("REGISTNO")
	private String registNo;
	
	/** 类型 */
	@XStreamAlias("SCHEDULETYPE")
	private String type;
	/** 类型 */
	@XStreamAlias("USERCODE")
	private String userCode;
	/** 订单号 */
	@XStreamAlias("ORDERNO")
	private String orderNo;
	/*** 调度对象 */
	@XStreamAlias("TASKLIST")
	private List<HandleScheduleBackReqScheduleItemDOrG> scheduleItemList;
	
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

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public List<HandleScheduleBackReqScheduleItemDOrG> getScheduleItemList() {
		return scheduleItemList;
	}

	public void setScheduleItemList(
			List<HandleScheduleBackReqScheduleItemDOrG> scheduleItemList) {
		this.scheduleItemList = scheduleItemList;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	
}
