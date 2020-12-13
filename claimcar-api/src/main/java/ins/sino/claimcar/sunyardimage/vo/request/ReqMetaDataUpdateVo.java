package ins.sino.claimcar.sunyardimage.vo.request;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("META_DATA")
public class ReqMetaDataUpdateVo implements Serializable{
	 @XStreamImplicit(itemFieldName="BATCH")	
	 private List<ReqBatchUpdateVo> batchUpdateVos;

	public List<ReqBatchUpdateVo> getBatchUpdateVos() {
		return batchUpdateVos;
	}

	public void setBatchUpdateVos(List<ReqBatchUpdateVo> batchUpdateVos) {
		this.batchUpdateVos = batchUpdateVos;
	}
	 

}
