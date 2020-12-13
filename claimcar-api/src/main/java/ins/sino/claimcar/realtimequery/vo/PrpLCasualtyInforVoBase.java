package ins.sino.claimcar.realtimequery.vo;


import java.io.Serializable;
import java.util.List;

public class PrpLCasualtyInforVoBase implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String personProperty;  //人员属性	
	private String personName;  //伤亡人员姓名	
	private String csCertiType;  //伤亡证件类型	
	private String csCertiCode;  //伤亡人员证件号码	
	private String csMedicalType;  //伤亡人员医疗类型	
	private String medicalType;  //伤情类别代码	
	private List<PrpLInjuredDetailsVo> prpLInjuredDetailsVo;  //伤亡详情	
	private Long upperId;  //维护一个父级关系表id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPersonProperty() {
		return personProperty;
	}
	public void setPersonProperty(String personProperty) {
		this.personProperty = personProperty;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getCsCertiType() {
		return csCertiType;
	}
	public void setCsCertiType(String csCertiType) {
		this.csCertiType = csCertiType;
	}
	public String getCsCertiCode() {
		return csCertiCode;
	}
	public void setCsCertiCode(String csCertiCode) {
		this.csCertiCode = csCertiCode;
	}
	public String getCsMedicalType() {
		return csMedicalType;
	}
	public void setCsMedicalType(String csMedicalType) {
		this.csMedicalType = csMedicalType;
	}
	public String getMedicalType() {
		return medicalType;
	}
	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}
	public List<PrpLInjuredDetailsVo> getPrpLInjuredDetailsVo() {
		return prpLInjuredDetailsVo;
	}
	public void setPrpLInjuredDetailsVo(
			List<PrpLInjuredDetailsVo> prpLInjuredDetailsVo) {
		this.prpLInjuredDetailsVo = prpLInjuredDetailsVo;
	}
	public Long getUpperId() {
		return upperId;
	}
	public void setUpperId(Long upperId) {
		this.upperId = upperId;
	}
	
	
}
