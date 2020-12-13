package ins.sino.claimcar.sunyardimage.vo.response;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("PAGES")
public class ResPagesVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias(impl=RespageVo.class,value="PAGE")
	@XStreamImplicit
	private List<RespageVo> resPageVos;


	public List<RespageVo> getResPageVos() {
		return resPageVos;
	}


	public void setResPageVos(List<RespageVo> resPageVos) {
		this.resPageVos = resPageVos;
	}

}
