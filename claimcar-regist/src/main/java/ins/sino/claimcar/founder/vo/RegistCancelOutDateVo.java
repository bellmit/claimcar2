package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 报案注销接口vo（理赔请求客服系统）
 * @author Luwei
 */
@XStreamAlias("OUTDATE")
public class RegistCancelOutDateVo {

	/**
	 * 报案号
	 */
	@XStreamAlias("ClmNo")
	private String clmNo;

	public String getClmNo() {
		return clmNo;
	}

	public void setClmNo(String clmNo) {
		this.clmNo = clmNo;
	}

}
