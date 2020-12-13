package ins.sino.claimcar.sunyardimage.vo.request;

import java.io.Serializable;

import ins.sino.claimcar.sunyardimage.vo.common.BatchVo;
import ins.sino.claimcar.sunyardimage.vo.common.VtreeVo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BATCH")
public class ReqBatchQueryVo extends BatchVo implements Serializable {

	@XStreamAlias("COM_CODE")
	private String comCode;
	@XStreamAlias("VTREE")
	private ReqVtreeVo vtreeVo;
	
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
	
	
	
}
