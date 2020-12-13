package ins.sino.claimcar.genilex.dlossReqVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PersonLossFee")
public class PersonLossFeeVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("EvalID")
	private String  evalID;//定核损ID
	@XStreamAlias("PersonID")
	private String personID;//伤亡人员编号
	@XStreamAlias("FeeType")
	private String feeType;//损失赔偿类型明细代码	
	@XStreamAlias("UnderDefLoss")
	private String underDefLoss;//核损金额		
	@XStreamAlias("ExpenseSort")
	private String expenseSort;//费用类别	
	@XStreamAlias("CurrencyCode")
	private String currencyCode;//币种
	@XStreamAlias("ItemBillAmt")
	private String itemBillAmt;//单据金额	
	@XStreamAlias("Remark")
	private String remark;//备注	
	@XStreamAlias("CreateBy")
	private String createBy;//创建者	
	@XStreamAlias("CreateTime")
	private String createTime;//创建日期		
	@XStreamAlias("UpdateBy")
	private String updateBy;//更新者	
	@XStreamAlias("UpdateTime")
	private String updateTime;//更新日期
	public String getEvalID() {
		return evalID;
	}
	public void setEvalID(String evalID) {
		this.evalID = evalID;
	}
	public String getPersonID() {
		return personID;
	}
	public void setPersonID(String personID) {
		this.personID = personID;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getUnderDefLoss() {
		return underDefLoss;
	}
	public void setUnderDefLoss(String underDefLoss) {
		this.underDefLoss = underDefLoss;
	}
	public String getExpenseSort() {
		return expenseSort;
	}
	public void setExpenseSort(String expenseSort) {
		this.expenseSort = expenseSort;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getItemBillAmt() {
		return itemBillAmt;
	}
	public void setItemBillAmt(String itemBillAmt) {
		this.itemBillAmt = itemBillAmt;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
		

}
