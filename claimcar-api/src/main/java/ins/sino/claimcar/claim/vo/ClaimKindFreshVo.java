package ins.sino.claimcar.claim.vo;

import java.math.BigDecimal;


/**
 * <pre></pre>
 * @author ★weilanlei
 * 用于理算核赔结束之后刷新立案数据的临时Vo
 * @CreateTime 2016年6月22日
 */
public class ClaimKindFreshVo  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	// 类别名称 险别或者是车财损失 死亡伤 医疗费用
	private String ItemName;
	// 估损金额
	private BigDecimal defLoss = BigDecimal.ZERO;
	// 施救费
	private BigDecimal rescueFee = BigDecimal.ZERO;
	// 估计赔款
	private BigDecimal claimLoss = BigDecimal.ZERO;

	public String getItemName() {
		return ItemName;
	}

	public void setItemName(String itemName) {
		ItemName = itemName;
	}

	public BigDecimal getDefLoss() {
		return defLoss;
	}

	public void setDefLoss(BigDecimal defLoss) {
		this.defLoss = defLoss;
	}

	public BigDecimal getRescueFee() {
		return rescueFee;
	}

	public void setRescueFee(BigDecimal rescueFee) {
		this.rescueFee = rescueFee;
	}

	public BigDecimal getClaimLoss() {
		return claimLoss;
	}

	public void setClaimLoss(BigDecimal claimLoss) {
		this.claimLoss = claimLoss;
	}
	
	
}
