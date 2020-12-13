package ins.sino.claimcar.sunyardimage.vo.request;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("META_DATA")
public class ReqMetaDataQueryVo implements Serializable{
 @XStreamImplicit(itemFieldName="BATCH")	
 private List<ReqBatchQueryVo> batchQueryVos;

public List<ReqBatchQueryVo> getBatchQueryVos() {
	return batchQueryVos;
}

public void setBatchQueryVos(List<ReqBatchQueryVo> batchQueryVos) {
	this.batchQueryVos = batchQueryVos;
}
 

}
