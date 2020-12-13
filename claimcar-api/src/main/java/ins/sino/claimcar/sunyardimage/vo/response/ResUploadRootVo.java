package ins.sino.claimcar.sunyardimage.vo.response;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("root")
public class ResUploadRootVo implements Serializable{

	@XStreamAlias("PAGES")
	private List<RespageVo> resPageVos;
	
	public List<RespageVo> getResPageVos() {
		return resPageVos;
	}
	public void setResPageVos(List<RespageVo> resPageVos) {
		this.resPageVos = resPageVos;
	}
	
	
}
