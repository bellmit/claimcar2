package ins.sino.claimcar.platform.vo.sh;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("InjuryIdentifyInfoData")
public class SHCILossReqInjuryIdentifyInfoDataVo {

	@XStreamAlias("InjuryIdentifyName")
	private String injuryIdentifyName;  //伤残鉴定机构名称
	
	@XStreamAlias("InjuryIdentifyCertiCode")
	private String injuryIdentifyCertiCode;//伤残鉴定机构组织机构代码

	public String getInjuryIdentifyName() {
		return injuryIdentifyName;
	}

	public void setInjuryIdentifyName(String injuryIdentifyName) {
		this.injuryIdentifyName = injuryIdentifyName;
	}

	public String getInjuryIdentifyCertiCode() {
		return injuryIdentifyCertiCode;
	}

	public void setInjuryIdentifyCertiCode(String injuryIdentifyCertiCode) {
		this.injuryIdentifyCertiCode = injuryIdentifyCertiCode;
	}
	
	
}
