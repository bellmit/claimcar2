/******************************************************************************
* CREATETIME : 2016年3月16日 上午11:55:40
* FILE       : ins.sino.claimcar.carplatform.vo.PolicyRiskWarnBodyVo
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 被代位查询接口 交强请求body
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiBeSubrogationReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private CiBeSubrogationBaseVo beSubrogationBaseVo;

	
	public CiBeSubrogationBaseVo getBeSubrogationBaseVo() {
		return beSubrogationBaseVo;
	}

	
	public void setBeSubrogationBaseVo(CiBeSubrogationBaseVo beSubrogationBaseVo) {
		this.beSubrogationBaseVo = beSubrogationBaseVo;
	}

	

	
	
}
