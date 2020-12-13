package ins.sino.claimcar.sunyardimage.vo.request;

import ins.sino.claimcar.sunyardimage.vo.common.BatchVo;
import ins.sino.claimcar.sunyardimage.vo.common.VtreeVo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BATCH")
public class ReqBatchVo extends BatchVo implements Serializable{
	@XStreamAlias("COM_CODE")
	private String comCode;
	@XStreamAlias("VTREE")
	private ReqVtreeVo vtreeVo;
	@XStreamAlias("PAGES")
	private ReqPagesVo reqPagesVo;
	
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	
	public ReqVtreeVo getVtreeVo() {
		return vtreeVo;
	}
	public void setVtreeVo(ReqVtreeVo vtreeVo) {
		this.vtreeVo = vtreeVo;
	}
	public ReqPagesVo getReqPagesVo() {
		return reqPagesVo;
	}
	public void setReqPagesVo(ReqPagesVo reqPagesVo) {
		this.reqPagesVo = reqPagesVo;
	}
	
	

}
