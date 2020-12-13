package ins.sino.claimcar.sunyardimage.vo.request;

import ins.sino.claimcar.sunyardimage.vo.common.ImageBaseRootVo;
import ins.sino.claimcar.sunyardimage.vo.response.ResMetaDataVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("root")
public class ReqGetImageBaseRootVo extends ImageBaseRootVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("META_DATA")
	private ReqGetMetaDataVo metaDataVo;
	public ReqGetMetaDataVo getMetaDataVo() {
		return metaDataVo;
	}
	public void setMetaDataVo(ReqGetMetaDataVo metaDataVo) {
		this.metaDataVo = metaDataVo;
	}
	
}
