package ins.sino.claimcar.sunyardimage.vo.response;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("PageInfo")
public class ResPageInfoVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	

	@XStreamImplicit(itemFieldName="PAGE")
	private List<ResPonsePageVo> pageVos;

	public List<ResPonsePageVo> getPageVos() {
		return pageVos;
	}

	public void setPageVos(List<ResPonsePageVo> pageVos) {
		this.pageVos = pageVos;
	}

	


}
