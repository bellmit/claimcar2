package ins.sino.claimcar.sunyardimage.vo.request;

import java.io.Serializable;
import java.util.List;

import ins.sino.claimcar.sunyardimage.vo.common.FatherNodeVo;
import ins.sino.claimcar.sunyardimage.vo.common.VtreeVo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("VTREE")
public class ReqVtreeVo extends VtreeVo implements Serializable{
	@XStreamImplicit(itemFieldName="NODE")
	private List<ReqFatherNodeVo> fatherNodeVos;

	public List<ReqFatherNodeVo> getFatherNodeVos() {
		return fatherNodeVos;
	}

	public void setFatherNodeVos(List<ReqFatherNodeVo> fatherNodeVos) {
		this.fatherNodeVos = fatherNodeVos;
	}
	

}
