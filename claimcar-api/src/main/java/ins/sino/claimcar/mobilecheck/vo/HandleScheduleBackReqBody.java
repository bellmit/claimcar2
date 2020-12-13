package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 手动调度返回查勘定损员信息接口 - body（快赔请求理赔）
 * @author zy
 *
 */
@XStreamAlias("BODY")
public class HandleScheduleBackReqBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/*** 数据部分 */
	@XStreamAlias("SCHEDULEWF")
	private HandleScheduleBackReqScheduleWF scheduleWF;
	
	/*** 调度对象 */
	@XStreamAlias("SCHEDULEITEMLIST")
	private List<HandleScheduleBackReqScheduleItem> scheduleItemList;
	
	

	public HandleScheduleBackReqScheduleWF getScheduleWF() {
		return scheduleWF;
	}

	public void setScheduleWF(HandleScheduleBackReqScheduleWF scheduleWF) {
		this.scheduleWF = scheduleWF;
	}

	public List<HandleScheduleBackReqScheduleItem> getScheduleItemList() {
		return scheduleItemList;
	}

	public void setScheduleItemList(
			List<HandleScheduleBackReqScheduleItem> scheduleItemList) {
		this.scheduleItemList = scheduleItemList;
	}
	
}
