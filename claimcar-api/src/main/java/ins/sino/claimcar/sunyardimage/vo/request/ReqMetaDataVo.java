package ins.sino.claimcar.sunyardimage.vo.request;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("META_DATA")
public class ReqMetaDataVo implements Serializable{
@XStreamAlias("BATCH")
private ReqBatchVo batchVo;

public ReqBatchVo getBatchVo() {
	return batchVo;
}

public void setBatchVo(ReqBatchVo batchVo) {
	this.batchVo = batchVo;
}

}
