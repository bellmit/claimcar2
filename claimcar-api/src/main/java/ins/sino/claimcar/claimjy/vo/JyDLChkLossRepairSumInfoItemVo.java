package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("Item")
public class JyDLChkLossRepairSumInfoItemVo implements Serializable {

	@XStreamAlias("WorkTypeCode")
	private String workTypeCode;

	@XStreamAlias("ItemCount")
	private String itemCount;

	@XStreamAlias("ApprRepairSum")
	private String apprRepairSum;

	public String getWorkTypeCode() {
		return workTypeCode;
	}

	public void setWorkTypeCode(String workTypeCode) {
		this.workTypeCode = workTypeCode;
	}

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public String getApprRepairSum() {
		return apprRepairSum;
	}

	public void setApprRepairSum(String apprRepairSum) {
		this.apprRepairSum = apprRepairSum;
	}

	public JyDLChkLossRepairSumInfoItemVo(){
		super();
	}

}
