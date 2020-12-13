package ins.sino.claimcar.court.dlclaim.xyvo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("VTREE")
public class VtreeImageVo implements Serializable{

	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	@XStreamAlias("APP_CODE")
	private String app_code;
	@XStreamAsAttribute
	@XStreamAlias("APP_NAME")
	private String app_name;
	@XStreamImplicit(itemFieldName="NODE")
	private List<NodeImageVo> nodeImageVos;
	public String getApp_code() {
		return app_code;
	}
	public void setApp_code(String app_code) {
		this.app_code = app_code;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public List<NodeImageVo> getNodeImageVos() {
		return nodeImageVos;
	}
	public void setNodeImageVos(List<NodeImageVo> nodeImageVos) {
		this.nodeImageVos = nodeImageVos;
	}
	
}
