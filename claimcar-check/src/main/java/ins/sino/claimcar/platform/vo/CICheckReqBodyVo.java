/******************************************************************************
 * CREATETIME : 2016年4月13日 下午3:44:15
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.*;

/**
 * 车险信息平台交强险V6.0.0接口 -->查勘登记vo -->交强-->请求报文body
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CICheckReqBodyVo {

	/** 基本信息 **/
	@XmlElement(name = "BASE_PART")
	private CICheckReqBasePartVo basePartVo;

	/** 车辆损失情况列表 **/
	@XmlElement(name = "CAR_LIST")
	private CICheckReqCarListVo carListVo;

	/** 财产损失情况列表 **/
	@XmlElement(name = "PROTECT_LIST")
	private CICheckReqProtectListVo protectListVo;

	/** 人员损失情况列表 **/
	@XmlElement(name = "PERSON_LIST")
	private CICheckReqPersonListVo personListVo;

	
	
	public CICheckReqBasePartVo getBasePartVo() {
		return basePartVo;
	}

	public void setBasePartVo(CICheckReqBasePartVo basePartVo) {
		this.basePartVo = basePartVo;
	}

	public CICheckReqCarListVo getCarListVo() {
		return carListVo;
	}

	public void setCarListVo(CICheckReqCarListVo carListVo) {
		this.carListVo = carListVo;
	}

	public CICheckReqProtectListVo getProtectListVo() {
		return protectListVo;
	}

	public void setProtectListVo(CICheckReqProtectListVo protectListVo) {
		this.protectListVo = protectListVo;
	}

	public CICheckReqPersonListVo getPersonListVo() {
		return personListVo;
	}

	public void setPersonListVo(CICheckReqPersonListVo personListVo) {
		this.personListVo = personListVo;
	}

}
