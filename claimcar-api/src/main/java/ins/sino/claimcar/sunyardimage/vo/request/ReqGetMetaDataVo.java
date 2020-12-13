package ins.sino.claimcar.sunyardimage.vo.request;

import ins.sino.claimcar.sunyardimage.vo.common.BatchVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ReqGetMetaDataVo implements Serializable{
	@XStreamAlias("BATCH")
	private BatchVo batchVo;

	public BatchVo getBatchVo() {
		return batchVo;
	}

	public void setBatchVo(BatchVo batchVo) {
		this.batchVo = batchVo;
	}

}
