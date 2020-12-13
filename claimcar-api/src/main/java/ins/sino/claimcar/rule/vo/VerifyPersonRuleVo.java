/******************************************************************************
* CREATETIME : 2016年2月22日 上午11:11:13
******************************************************************************/
package ins.sino.claimcar.rule.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 核价核损规则vo
 * @author ★LiuPing
 * @CreateTime 2016年2月22日
 */
public class VerifyPersonRuleVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 机构代码 */
	private String comCode;
	/** 险类代码 */
	private String classCode;
	/** 险种代码 */
	private String riskCode;
	/** 当前时间，用于判断规则有效期 */
	private Date now = new Date();

	/** 审核金额 */
	private Double sumLossFee = 0d;

	/** 客户等级 */
	private String customLevel;

	/** 返回相应的，需要的等级代码，0-表示可自动核损/核价通过 */
	private Integer backLevel;
	/** 最大审核级别**/
	private Integer maxLevel;
	/** 返回消息 */
	private String backMessage;

	/** 总公司标识 */
	private String topComp;


	public Double getSumLossFee() {
		return sumLossFee;
	}

	public void setSumLossFee(Double sumLossFee) {
		this.sumLossFee = sumLossFee;
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

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public Date getNow() {
		return now;
	}

	public void setNow(Date now) {
		this.now = now;
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

	public String getTopComp() {
		return topComp;
	}

	public void setTopComp(String topComp) {
		this.topComp = topComp;
	}

}
