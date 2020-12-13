package ins.sino.claimcar.sunyardimage.vo.request;

import java.io.Serializable;

import ins.sino.claimcar.sunyardimage.vo.common.ImageBaseRootVo;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("root")
public class ReqImageBaseRootVo extends ImageBaseRootVo implements Serializable{
	@XStreamAlias("META_DATA")
	private ReqMetaDataVo metaDataVo;

	public ReqMetaDataVo getMetaDataVo() {
		return metaDataVo;
	}

	public void setMetaDataVo(ReqMetaDataVo metaDataVo) {
		this.metaDataVo = metaDataVo;
	}
	
	
}
