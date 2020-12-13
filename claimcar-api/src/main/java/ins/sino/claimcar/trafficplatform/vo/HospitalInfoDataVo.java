package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HospitalInfoData")
public class HospitalInfoDataVo {
	/**
	 * 治疗机构名称
	 */
	@XStreamAlias("HospitalName")
	private String hospitalName;
	
	/**
	 * 治疗机构组织机构代码
	 */
	@XStreamAlias("HospitalFactoryCertiCode")
	private String hospitalFactoryCertiCode;

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
