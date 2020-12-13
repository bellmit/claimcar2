package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 住院信息
 * @author j2eel
 *
 */
@XStreamAlias("INPANTIENTINFO")
public class InPantientInfoVo implements Serializable{

	private static final long serialVersionUID = 1L;
	@XStreamAlias("ID")
	private String id; 	
	@XStreamAlias("INTIME")
	private String inTime; 			//入院时间
	@XStreamAlias("OUTTIME")		
	private String outTime;			//出院时间
	@XStreamAlias("PROVINCE")		
	private String province;		//省
	@XStreamAlias("CITY")
	private String city;			//市
	@XStreamAlias("HOSPITALNAME")
	private String hospitalName;    //医院名称
	@XStreamAlias("HOSPITALCODE")
	private String hospitalCode;    //医院编码
	@XStreamAlias("REMARK")
	private String remark;			//备注
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInTime() {
		return inTime;
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}
	public String getOutTime() {
		return outTime;
	}
	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	

}
