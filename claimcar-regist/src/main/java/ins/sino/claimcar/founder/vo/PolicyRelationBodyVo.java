package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 保单关联接口vo（理赔请求客服系统）
 * @author Luwei
 */
@XStreamAlias("BODY")
public class PolicyRelationBodyVo {

	@XStreamAlias("OUTDATE")
	private PolicyRelationDateVo outDate;

	public PolicyRelationDateVo getOutDate() {
		return outDate;
	}

	public void setOutDate(PolicyRelationDateVo outDate) {
		this.outDate = outDate;
	}

}
