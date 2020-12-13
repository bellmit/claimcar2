package ins.sino.claimcar.sunyardimage.vo.request;

import java.io.Serializable;

import ins.sino.claimcar.sunyardimage.vo.common.BaseDataVo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("root")
public class ReqImageBaseQueryRootVo implements Serializable{
	@XStreamAlias("BASE_DATA")
	private BaseDataVo baseDataVo;
	@XStreamAlias("META_DATA")
	private ReqMetaDataQueryVo metaDataQueryVo;
	public BaseDataVo getBaseDataVo() {
		return baseDataVo;
	}
	public void setBaseDataVo(BaseDataVo baseDataVo) {
		this.baseDataVo = baseDataVo;
	}
	public ReqMetaDataQueryVo getMetaDataQueryVo() {
		return metaDataQueryVo;
	}
	public void setMetaDataQueryVo(ReqMetaDataQueryVo metaDataQueryVo) {
		this.metaDataQueryVo = metaDataQueryVo;
	}
	
}
