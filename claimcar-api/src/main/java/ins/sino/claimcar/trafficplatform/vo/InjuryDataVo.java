package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("InjuryData")
public class InjuryDataVo implements Serializable{
	
private static final long serialVersionUID = 1L;
/**
 *  受伤部位
 */
@XStreamAlias("InjuryPart")
private String injuryPart;
	
/**
 *  伤残程度代码
 */
@XStreamAlias("InjuryLevelCode")
private String injuryLevelCode;

public String getInjuryPart() {
	return injuryPart;
}

public void setInjuryPart(String injuryPart) {
	this.injuryPart = injuryPart;
}

public String getInjuryLevelCode() {
	return injuryLevelCode;
}

public void setInjuryLevelCode(String injuryLevelCode) {
	this.injuryLevelCode = injuryLevelCode;
}



}
