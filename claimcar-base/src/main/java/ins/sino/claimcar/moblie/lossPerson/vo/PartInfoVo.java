package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 受伤部位信息
 * @author j2eel
 *
 */
@XStreamAlias("PARTINFO")
public class PartInfoVo implements Serializable{

	private static final long serialVersionUID = 1L;
	@XStreamAlias("ID")
	private String id;              //主键
	@XStreamAlias("PART")
	private String part;       		//受伤部位
	@XStreamAlias("DIAGNOSIS")
	private String diagnosis;  		//伤情诊断
	@XStreamAlias("TREATMENTTYPE")
	private String treatmentType;  //治疗方式
	@XStreamAlias("TREATMENTWAY")  
	private String treatmentWay;   //治疗途径
	@XStreamAlias("PROGNOSIS")
	private String prognosis;      //预后情况
	@XStreamAlias("DISABILITYDEGREE")
	private String disabilityDegree; //伤残程度
	@XStreamAlias("DETAIL")			
	private String detail; 			//具体诊断
	@XStreamAlias("REMARK")
	private String remark;          //跟踪信息备注
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPart() {
		return part;
	}
	public void setPart(String part) {
		this.part = part;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getTreatmentType() {
		return treatmentType;
	}
	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}
	public String getTreatmentWay() {
		return treatmentWay;
	}
	public void setTreatmentWay(String treatmentWay) {
		this.treatmentWay = treatmentWay;
	}
	public String getPrognosis() {
		return prognosis;
	}
	public void setPrognosis(String prognosis) {
		this.prognosis = prognosis;
	}
	public String getDisabilityDegree() {
		return disabilityDegree;
	}
	public void setDisabilityDegree(String disabilityDegree) {
		this.disabilityDegree = disabilityDegree;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

    
}
