package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 手动调度返回查勘定损员信息接口 - body（快赔请求理赔）
 * @author zy
 *
 */
@XStreamAlias("BODY")
public class HandleScheduleDOrgBackReqBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/*** 数据部分 */
	@XStreamAlias("SCHEDULEINFO")
	private HandleScheduleBackReqScheduleDOrG scheduleSD;

	public HandleScheduleBackReqScheduleDOrG getScheduleSD() {
		return scheduleSD;
	}

	public void setScheduleSD(HandleScheduleBackReqScheduleDOrG scheduleSD) {
		this.scheduleSD = scheduleSD;
	}
	


	
	
	

	
}
