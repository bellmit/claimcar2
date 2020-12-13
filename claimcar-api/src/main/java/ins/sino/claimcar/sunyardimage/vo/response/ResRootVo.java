package ins.sino.claimcar.sunyardimage.vo.response;

import ins.sino.claimcar.sunyardimage.vo.common.RootVo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("root")
public class ResRootVo extends RootVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("PAGES")
	private ResPagesVo pagesVo;
	//private List<RespageVo> resPageVos;
	@XStreamAlias("SYD")
	private ResSydVo sydVo;

	public ResPagesVo getPagesVo() {
		return pagesVo;
	}

	public void setPagesVo(ResPagesVo pagesVo) {
		this.pagesVo = pagesVo;
	}

	public ResSydVo getSydVo() {
		return sydVo;
	}

	
	public void setSydVo(ResSydVo sydVo) {
		this.sydVo = sydVo;
	}
	
	
}
