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
public class HandleScheduleSDBackReqBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/*** 数据部分 */
	@XStreamAlias("SCHEDULEWF")
	private HandleScheduleBackReqScheduleSD scheduleSD;
	
	/*** 调度对象 */
	@XStreamAlias("SCHEDULEITEMLIST")
	private List<HandleScheduleBackReqScheduleItemSD> scheduleItemList;

	public HandleScheduleBackReqScheduleSD getScheduleSD() {
		return scheduleSD;
	}

	public void setScheduleSD(HandleScheduleBackReqScheduleSD scheduleSD) {
		this.scheduleSD = scheduleSD;
	}

	public List<HandleScheduleBackReqScheduleItemSD> getScheduleItemList() {
		return scheduleItemList;
	}

	public void setScheduleItemList(
			List<HandleScheduleBackReqScheduleItemSD> scheduleItemList) {
		this.scheduleItemList = scheduleItemList;
	}
	
	

	
}
