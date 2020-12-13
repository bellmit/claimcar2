package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 手动调度请求接口-body（理赔请求快赔）
 * @author zy
 *
 */
@XStreamAlias("BODY")
public class HandleScheduleReqBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/*** 数据部分 */
	@XStreamAlias("SCHEDULEWF")
	private HandleScheduleReqScheduleWF scheduleWF;
	
	/*** 调度对象 */
	@XStreamAlias("SCHEDULEITEMLIST")
	private List<HandleScheduleReqScheduleItem> scheduleItemList;

	public HandleScheduleReqScheduleWF getScheduleWF() {
		return scheduleWF;
	}

	public void setScheduleWF(HandleScheduleReqScheduleWF scheduleWF) {
		this.scheduleWF = scheduleWF;
	}

	public List<HandleScheduleReqScheduleItem> getScheduleItemList() {
		return scheduleItemList;
	}

	public void setScheduleItemList(List<HandleScheduleReqScheduleItem> scheduleItemList) {
		this.scheduleItemList = scheduleItemList;
	}
	
	
}
