package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 被抚养人员信息
 * @author j2eel
 *
 */
@XStreamAlias("RAISEINFO")
public class RaiseInfoVo implements Serializable{

	private static final long serialVersionUID = 1L;
	@XStreamAlias("ID")
	private String id;
	@XStreamAlias("NAME")
	private String name;
	@XStreamAlias("SEX")
	private String sex;
	@XStreamAlias("AGE")
	private String age;
	@XStreamAlias("HUKOUTYPE")
	private String hukouType;		//户口性质
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
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getHukouType() {
		return hukouType;
	}
	public void setHukouType(String hukouType) {
		this.hukouType = hukouType;
	}
	public String getRelationShip() {
		return relationShip;
	}
	public void setRelationShip(String relationShip) {
		this.relationShip = relationShip;
	}
	
	

}
