/******************************************************************************
 * CREATETIME : 2016年5月31日 下午6:45:19
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 人员治疗机构
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIVClaimHospitalInfoDataVo {

	@XmlElement(name = "HospitalName")
	private String hospitalname;// 治疗机构名称

	/*牛强  2017-03-15 改*/
	@XmlElement(name="HospitalFactoryCertiCode")
	private String hospitalFactoryCertiCode;  //治疗机构组织机构代码
	
	/**
	 * @return 返回 hospitalname 治疗机构名称
	 */
	public String getHospitalname() {
		return hospitalname;
	}

	/**
	 * @param hospitalname 要设置的 治疗机构名称
	 */
	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}

	public String getHospitalFactoryCertiCode() {
		return hospitalFactoryCertiCode;
	}

	public void setHospitalFactoryCertiCode(String hospitalFactoryCertiCode) {
		this.hospitalFactoryCertiCode = hospitalFactoryCertiCode;
	}

}
