package ins.sino.claimcar.carchild.check.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.List;

@XStreamAlias("BODY")
public class CTCheckInfoInitResBodyVo  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CHECKTASKINFO")
	private CTCheckTaskInfoVo checkTaskInfo;
	
	

	public CTCheckTaskInfoVo getCheckTaskInfo() {
		return checkTaskInfo;
	}

	public void setCheckTaskInfo(CTCheckTaskInfoVo checkTaskInfo) {
		this.checkTaskInfo = checkTaskInfo;
	}

}
