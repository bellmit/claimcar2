package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 护理人员信息
 * @author j2eel
 *
 */
@XStreamAlias("NURSEINFO")
public class NurseInfoVo implements Serializable{

	private static final long serialVersionUID = 1L;
	@XStreamAlias("ID")
	private String id;
	@XStreamAlias("NAME")
	private String name;		
	@XStreamAlias("SEX")
	private String sex;
	@XStreamAlias("CAREERCODE")
	private String careerCode;	
	@XStreamAlias("CAREERNAME")
	private String careerName;	
	@XStreamAlias("INCOMETYPE")
	private String incomeType;		//收入
	@XStreamAlias("RELATIONSHIP")
	private String relationShip;	//与被害人关系
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCareerCode() {
		return careerCode;
	}
	public void setCareerCode(String careerCode) {
		this.careerCode = careerCode;
	}
	public String getCareerName() {
		return careerName;
	}
	public void setCareerName(String careerName) {
		this.careerName = careerName;
	}
	public String getIncomeType() {
		return incomeType;
	}
	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
	}
	public String getRelationShip() {
		return relationShip;
	}
	public void setRelationShip(String relationShip) {
		this.relationShip = relationShip;
	}

	

}
