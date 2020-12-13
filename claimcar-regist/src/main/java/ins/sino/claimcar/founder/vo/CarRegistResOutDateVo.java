package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 车险报案vo（理赔请求客服系统-返回body,体信息）
 * @author Luwei
 *
 */
@XStreamAlias("OUTDATE")
public class CarRegistResOutDateVo {

	/** 报案号 */
	@XStreamAlias("ClmNo")
	private String clmNo;

	public String getClmNo() {
		return clmNo;
	}

	public void setClmNo(String clmNo) {
		this.clmNo = clmNo;
	}

}
