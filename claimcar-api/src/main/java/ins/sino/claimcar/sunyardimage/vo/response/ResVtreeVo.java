package ins.sino.claimcar.sunyardimage.vo.response;

import ins.sino.claimcar.sunyardimage.vo.common.VtreeVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqGetFatherNodeVo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ResVtreeVo extends VtreeVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XStreamImplicit(itemFieldName="NODE")
	private List<ReqGetFatherNodeVo> fatherNodeVo;

	public List<ReqGetFatherNodeVo> getFatherNodeVo() {
		return fatherNodeVo;
	}
	public void setFatherNodeVo(List<ReqGetFatherNodeVo> fatherNodeVo) {
		this.fatherNodeVo = fatherNodeVo;
	}
	
	
}
