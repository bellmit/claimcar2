package ins.sino.claimcar.interf.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("BasePart")
public class SettleBasePartVo  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("SettleNo")
	private String settleNo;//结算单号
	@XStreamAlias("CertiType")
	private String certiType;//结算单类型
	@XStreamAlias("Accountcode")
	private String accountcode;//账号
	@XStreamAlias("OperateType")
	private String operateType;//操作类型，1-生成结算单，0-注销结算单
	@XStreamAlias("PayRefReason")
	private String payRefReason;//收付原因
	
	@XStreamImplicit
	private List<ItemVo> itemVoList;//对应的计算书号及序列号

	public String getSettleNo() {
		return settleNo;
	}

	public void setSettleNo(String settleNo) {
		this.settleNo = settleNo;
	}

	public String getCertiType() {
		return certiType;
	}

	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	public String getAccountcode() {
		return accountcode;
	}

	public void setAccountcode(String accountcode) {
		this.accountcode = accountcode;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getPayRefReason() {
		return payRefReason;
	}

	public void setPayRefReason(String payRefReason) {
		this.payRefReason = payRefReason;
	}

	public List<ItemVo> getItemVoList() {
		return itemVoList;
	}

	public void setItemVoList(List<ItemVo> itemVoList) {
		this.itemVoList = itemVoList;
	}
	
}
