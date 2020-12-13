package ins.sino.claimcar.sunyardimage.vo.request;

import ins.sino.claimcar.sunyardimage.vo.common.ImageBaseRootVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("root")
public class ReqImageNumBaseRootVo extends ImageBaseRootVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("META_DATA")
	private ReqGetMetaDataNumVo metaDataVo;
	public ReqGetMetaDataNumVo getMetaDataVo() {
		return metaDataVo;
	}
	public void setMetaDataVo(ReqGetMetaDataNumVo metaDataVo) {
		this.metaDataVo = metaDataVo;
	}
	
}
