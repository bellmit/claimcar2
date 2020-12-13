package ins.sino.claimcar.genilex.dlossReqVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EvalPersonInjury")
public class EvalPersonInjuryVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("EvalID")
	private String evalID;//定核损ID
	@XStreamAlias("PersonID")
	private String personID;//伤亡人员编号	
	@XStreamAlias("SerialNo")
	private String serialNo;//顺序号	
	@XStreamAlias("InjuryPart")
	private String injuryPart;//受伤部位
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
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getInjuryPart() {
		return injuryPart;
	}
	public void setInjuryPart(String injuryPart) {
		this.injuryPart = injuryPart;
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
