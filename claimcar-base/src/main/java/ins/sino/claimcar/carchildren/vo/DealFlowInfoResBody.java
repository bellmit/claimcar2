package ins.sino.claimcar.carchildren.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("BODY")
public class DealFlowInfoResBody implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("TASKLIST")
	private List<TaskinfoVo> tasklist;
	public List<TaskinfoVo> getTasklist() {
		return tasklist;
	}
	public void setTasklist(List<TaskinfoVo> tasklist) {
		this.tasklist = tasklist;
	}
	

}
