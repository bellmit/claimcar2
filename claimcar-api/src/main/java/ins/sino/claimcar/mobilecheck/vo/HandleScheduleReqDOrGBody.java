package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 手动调度请求接口-body（理赔请求快赔）
 * @author zy
 *
 */
@XStreamAlias("BODY")
public class HandleScheduleReqDOrGBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/*** 数据部分 */
	@XStreamAlias("SCHEDULEINFO")
	private HandleScheduleReqScheduleDOrG scheduleDOrG;

	public HandleScheduleReqScheduleDOrG getScheduleDOrG() {
		return scheduleDOrG;
	}

	public void setScheduleDOrG(HandleScheduleReqScheduleDOrG scheduleDOrG) {
		this.scheduleDOrG = scheduleDOrG;
	}






	
	
}
