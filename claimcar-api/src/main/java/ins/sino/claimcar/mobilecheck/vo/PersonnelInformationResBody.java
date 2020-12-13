package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 获取查勘定损员信息接口-返回body（快赔请求理赔）
 * @author zy
 *
 */
@XStreamAlias("BODY")
public class PersonnelInformationResBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/*** 数据部分 */
	@XStreamAlias("SCHEDULEWF")
	private PersonnelInformationResScheduleWF scheduleWF;
	
	/*** 调度对象 */
	@XStreamAlias("SCHEDULEITEMLIST")
	private List<PersonnelInformationResScheduleItem> scheduleItemList;

	public PersonnelInformationResScheduleWF getScheduleWF() {
		return scheduleWF;
	}

	public void setScheduleWF(PersonnelInformationResScheduleWF scheduleWF) {
		this.scheduleWF = scheduleWF;
	}

	public List<PersonnelInformationResScheduleItem> getScheduleItemList() {
		return scheduleItemList;
	}

	public void setScheduleItemList(
			List<PersonnelInformationResScheduleItem> scheduleItemList) {
		this.scheduleItemList = scheduleItemList;
	}
	
	
}
