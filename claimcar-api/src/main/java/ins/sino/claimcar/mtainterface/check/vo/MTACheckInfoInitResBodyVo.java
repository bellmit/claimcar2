package ins.sino.claimcar.mtainterface.check.vo;


import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class MTACheckInfoInitResBodyVo  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CHECKTASKINFO")
	private MTACheckTaskInfoVo checkTaskInfo;

	public MTACheckTaskInfoVo getCheckTaskInfo() {
		return checkTaskInfo;
	}

	public void setCheckTaskInfo(MTACheckTaskInfoVo checkTaskInfo) {
		this.checkTaskInfo = checkTaskInfo;
	}

	
	
}
