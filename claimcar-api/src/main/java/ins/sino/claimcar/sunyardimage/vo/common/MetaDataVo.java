package ins.sino.claimcar.sunyardimage.vo.common;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class MetaDataVo implements Serializable{
@XStreamAlias("BATCH")
private BatchVo batchVo;

public BatchVo getBatchVo() {
	return batchVo;
}

public void setBatchVo(BatchVo batchVo) {
	this.batchVo = batchVo;
}

}
