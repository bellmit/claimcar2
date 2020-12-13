package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 获取查勘定损员信息接口body（理赔请求快赔）
 * @author zy
 *
 */
@XStreamAlias("BODY")
public class PersonnelInformationReqBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/*** 数据部分 */
	@XStreamAlias("SCHEDULEWF")
	private PersonnelInformationReqScheduleWF scheduleWF;
	
	/*** 调度对象 */
	@XStreamAlias("SCHEDULEITEMLIST")
	private List<PersonnelInformationReqScheduleItem> scheduleItemList;

	
	public PersonnelInformationReqScheduleWF getScheduleWF() {
		return scheduleWF;
	}

	public void setScheduleWF(PersonnelInformationReqScheduleWF scheduleWF) {
		this.scheduleWF = scheduleWF;
	}

	public List<PersonnelInformationReqScheduleItem> getScheduleItemList() {
		return scheduleItemList;
	}

	public void setScheduleItemList(List<PersonnelInformationReqScheduleItem> scheduleItemList) {
		this.scheduleItemList = scheduleItemList;
	}
	
}
