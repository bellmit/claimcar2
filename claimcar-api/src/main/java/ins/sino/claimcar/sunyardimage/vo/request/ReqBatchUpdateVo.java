package ins.sino.claimcar.sunyardimage.vo.request;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import ins.sino.claimcar.sunyardimage.vo.common.BatchVo;
@XStreamAlias("BATCH")
public class ReqBatchUpdateVo extends BatchVo implements Serializable{
	@XStreamAlias("ONLY_SELF_ALERT")
	private String onlySelfAlert;
	@XStreamAlias("CLASSIFY_LIMIT")
	private String classifyLimit;
	@XStreamAlias("VTREE")
	private ReqVtreeVo vtreeVo;
	public String getOnlySelfAlert() {
		return onlySelfAlert;
	}
	public void setOnlySelfAlert(String onlySelfAlert) {
		this.onlySelfAlert = onlySelfAlert;
	}
	public String getClassifyLimit() {
		return classifyLimit;
	}
	public void setClassifyLimit(String classifyLimit) {
		this.classifyLimit = classifyLimit;
	}
	public ReqVtreeVo getVtreeVo() {
		return vtreeVo;
	}
	public void setVtreeVo(ReqVtreeVo vtreeVo) {
		this.vtreeVo = vtreeVo;
	}
	

}
