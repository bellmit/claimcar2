package ins.sino.claimcar.sunyardimage.vo.request;

import ins.sino.claimcar.sunyardimage.vo.common.BaseDataVo;
import ins.sino.claimcar.sunyardimage.vo.common.BatchVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("root")
public class ReqImageBaseUpdateRootVo extends BatchVo implements Serializable {
	
	@XStreamAlias("BASE_DATA")
	private BaseDataVo baseDataVo;
	@XStreamAlias("META_DATA")
	private ReqMetaDataUpdateVo metaDataUpdateVo;
	public BaseDataVo getBaseDataVo() {
		return baseDataVo;
	}
	public void setBaseDataVo(BaseDataVo baseDataVo) {
		this.baseDataVo = baseDataVo;
	}
	public ReqMetaDataUpdateVo getMetaDataUpdateVo() {
		return metaDataUpdateVo;
	}
	public void setMetaDataUpdateVo(ReqMetaDataUpdateVo metaDataUpdateVo) {
		this.metaDataUpdateVo = metaDataUpdateVo;
	}
	
}
