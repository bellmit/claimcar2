/******************************************************************************
 * CREATETIME : 2016年6月1日 上午10:35:13
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 人员治疗机构（多条）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIEndCaseAddReqHospitalInfoDataVo {

	/**
	 * 人员治疗机构（多条）
	 */
	@XmlElement(name = "HospitalName")
	private String hospitalName;// 人员治疗机构（多条）

	/*牛强  2017-03-15 改*/
	@XmlElement(name="HospitalFactoryCertiCode")
	private String hospitalFactoryCertiCode;  //治疗机构组织机构代码
	
	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalFactoryCertiCode() {
		return hospitalFactoryCertiCode;
	}

	public void setHospitalFactoryCertiCode(String hospitalFactoryCertiCode) {
		this.hospitalFactoryCertiCode = hospitalFactoryCertiCode;
	}
}
