package ins.sino.claimcar.sunyardimage.vo.response;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("SYD")
public class ResSydVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("doc")
	private ResDocVo docVo;
	
	public ResDocVo getDocVo() {
		return docVo;
	}
	
	public void setDocVo(ResDocVo docVo) {
		this.docVo = docVo;
	}

}
