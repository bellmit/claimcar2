package ins.sino.claimcar.rule.vo;

import java.io.Serializable;
/**
 * 财产定损提交核损规则
 * @author CHENMQ
 *
 */
public class LossPropToVerifyRuleVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** 规则描述 */
	private String description;
	/** 总定损金额*/
	private Double sumLossFee = 0d;
	/** 客户等级 */
	private String customLevel;
	/** 核损通过级别 */
	private Integer backLevel;
	/** 分公司最高级别**/
	private Integer maxLevel;
	/** 返回消息 */
	private String backMessage;
	/** 总公司标识 */
	private String topComp;
	/** 机构代码 */
	private String comCode;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCustomLevel() {
		return customLevel;
	}
	public void setCustomLevel(String customLevel) {
		this.customLevel = customLevel;
	}
	public Integer getBackLevel() {
		return backLevel;
	}
	public void setBackLevel(Integer backLevel) {
		this.backLevel = backLevel;
	}
	public Integer getMaxLevel() {
		return maxLevel;
	}
	public void setMaxLevel(Integer maxLevel) {
		this.maxLevel = maxLevel;
	}
	public String getBackMessage() {
		return backMessage;
	}
	public void setBackMessage(String backMessage) {
		this.backMessage = backMessage;
	}
	public Double getSumLossFee() {
		return sumLossFee;
	}
	public void setSumLossFee(Double sumLossFee) {
		this.sumLossFee = sumLossFee;
	}
	public String getTopComp() {
		return topComp;
	}
	public void setTopComp(String topComp) {
		this.topComp = topComp;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

}
