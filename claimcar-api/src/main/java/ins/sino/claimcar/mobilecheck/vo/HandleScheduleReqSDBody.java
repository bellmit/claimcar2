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
public class HandleScheduleReqSDBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/*** 数据部分 */
	@XStreamAlias("SCHEDULEWF")
	private HandleScheduleReqScheduleSD scheduleSD;
	
	/*** 调度对象 */
	@XStreamAlias("SCHEDULEITEMLIST")
	private List<HandleScheduleReqScheduleItemSD> scheduleItemList;

	public HandleScheduleReqScheduleSD getScheduleSD() {
		return scheduleSD;
	}

	public void setScheduleSD(HandleScheduleReqScheduleSD scheduleSD) {
		this.scheduleSD = scheduleSD;
	}

	public List<HandleScheduleReqScheduleItemSD> getScheduleItemList() {
		return scheduleItemList;
	}

	public void setScheduleItemList(
			List<HandleScheduleReqScheduleItemSD> scheduleItemList) {
		this.scheduleItemList = scheduleItemList;
	}




	
	
}
