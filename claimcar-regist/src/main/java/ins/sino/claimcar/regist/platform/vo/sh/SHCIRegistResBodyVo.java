/******************************************************************************
 * CREATETIME : 2016年5月24日 下午5:09:29
 ******************************************************************************/
package ins.sino.claimcar.regist.platform.vo.sh;

import ins.sino.claimcar.platform.vo.common.RiskInfo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 上海-交强报案平台返回信息BodyVo类
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHCIRegistResBodyVo {

	/**
	 * 基本信息
	 */
	@XmlElement(name = "BASE_PART")
	private SHCIRegistResBasePartVo response_BasePartVo;

	/**
	 * 出险车辆历史理赔案件列表（多条）：
	 */
	@XmlElementWrapper(name = "CLAIM_LIST")
	@XmlElement(name = "CLAIM_DATA")
	private List<SHCIRegistResClaimDataVo> response_ClaimDataVo;

	/**
	 * 代位求偿码信息（多条）
	 */
	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHCIRegistResSubrogationDataVo> Response_SubrogationDataVo;

	
	@XmlElement(name = "RiskInfo")
	private RiskInfo riskInfo; //增加风险信息
	
	
	public SHCIRegistResBasePartVo getResponse_BasePartVo() {
		return response_BasePartVo;
	}

	public void setResponse_BasePartVo(SHCIRegistResBasePartVo response_BasePartVo) {
		this.response_BasePartVo = response_BasePartVo;
	}

	public List<SHCIRegistResClaimDataVo> getResponse_ClaimDataVo() {
		return response_ClaimDataVo;
	}

	public void setResponse_ClaimDataVo(List<SHCIRegistResClaimDataVo> response_ClaimDataVo) {
		this.response_ClaimDataVo = response_ClaimDataVo;
	}

	public List<SHCIRegistResSubrogationDataVo> getResponse_SubrogationDataVo() {
		return Response_SubrogationDataVo;
	}

	public void setResponse_SubrogationDataVo(List<SHCIRegistResSubrogationDataVo> response_SubrogationDataVo) {
		Response_SubrogationDataVo = response_SubrogationDataVo;
	}

	public RiskInfo getRiskInfo() {
		return riskInfo;
	}

	public void setRiskInfo(RiskInfo riskInfo) {
		this.riskInfo = riskInfo;
	}

}
