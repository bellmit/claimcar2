package ins.sino.claimcar.sunyardimage.vo.request;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ReqGetMetaDataNumVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("BATCH")
	private ReqBatchNumVo reqBatchNumVo;
	public ReqBatchNumVo getReqBatchNumVo() {
		return reqBatchNumVo;
	}
	public void setReqBatchNumVo(ReqBatchNumVo reqBatchNumVo) {
		this.reqBatchNumVo = reqBatchNumVo;
	}

	
}
