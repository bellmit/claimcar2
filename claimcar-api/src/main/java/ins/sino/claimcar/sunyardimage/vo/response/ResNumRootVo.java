package ins.sino.claimcar.sunyardimage.vo.response;

import ins.sino.claimcar.sunyardimage.vo.common.RootVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("root")
public class ResNumRootVo extends RootVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("RETURN_DATA")
	private ResReturnDataVo resReturnDataVo;

	public ResReturnDataVo getResReturnDataVo() {
		return resReturnDataVo;
	}

	public void setResReturnDataVo(ResReturnDataVo resReturnDataVo) {
		this.resReturnDataVo = resReturnDataVo;
	}
	
	
}
