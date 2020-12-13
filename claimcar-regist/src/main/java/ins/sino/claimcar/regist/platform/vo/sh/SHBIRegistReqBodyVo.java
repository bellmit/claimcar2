/******************************************************************************
 * CREATETIME : 2016年5月25日 上午9:13:35
 ******************************************************************************/
package ins.sino.claimcar.regist.platform.vo.sh;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 商业报案请求平台信息BodyVo类-上海平台
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHBIRegistReqBodyVo {

	/**
	 * 基本信息
	 */
	@XmlElement(name = "BASE_PART")
	private SHBIRegistReqBasePartVo request_basePartVo;

	/**
	 * 第三方车辆情况（多条）
	 */
	@XmlElementWrapper(name = "THIRD_VEHICLE_LIST")
	@XmlElement(name = "THIRD_VEHICLE_DATA")
	private List<SHBIRegistReqThirdVehicleDataVo> request_thirdVehicleDataVo;

	/**
	 * 损失情况列表（多条）[就传车辆]
	 */
	@XmlElementWrapper(name = "OBJ_LIST")
	@XmlElement(name = "OBJ_DATA")
	private List<SHBIRegistReqObjDataVo> request_objDataVo;

	/**
	 * 被追偿案件信息列表（多条）[不送]
	 */
	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHCIRegistReqSubrogationDataVo> request_subrogationDataVo;

	/**
	 * 争议信息列表（多条）[不送]
	 */
	@XmlElementWrapper(name = "DISPUTE_LIST")
	@XmlElement(name = "DISPUTE_DATA")
	private List<SHCIRegistReqDisputeDataVo> request_disputeDataVo;

	
	
	public SHBIRegistReqBasePartVo getRequest_basePartVo() {
		return request_basePartVo;
	}

	public void setRequest_basePartVo(SHBIRegistReqBasePartVo request_basePartVo) {
		this.request_basePartVo = request_basePartVo;
	}

	public List<SHBIRegistReqThirdVehicleDataVo> getRequest_thirdVehicleDataVo() {
		return request_thirdVehicleDataVo;
	}

	public void setRequest_thirdVehicleDataVo(List<SHBIRegistReqThirdVehicleDataVo> request_thirdVehicleDataVo) {
		this.request_thirdVehicleDataVo = request_thirdVehicleDataVo;
	}

	public List<SHBIRegistReqObjDataVo> getRequest_objDataVo() {
		return request_objDataVo;
	}

	public void setRequest_objDataVo(List<SHBIRegistReqObjDataVo> request_objDataVo) {
		this.request_objDataVo = request_objDataVo;
	}

	public List<SHCIRegistReqSubrogationDataVo> getRequest_subrogationDataVo() {
		return request_subrogationDataVo;
	}

	public void setRequest_subrogationDataVo(List<SHCIRegistReqSubrogationDataVo> request_subrogationDataVo) {
		this.request_subrogationDataVo = request_subrogationDataVo;
	}

	public List<SHCIRegistReqDisputeDataVo> getRequest_disputeDataVo() {
		return request_disputeDataVo;
	}

	public void setRequest_disputeDataVo(List<SHCIRegistReqDisputeDataVo> request_disputeDataVo) {
		this.request_disputeDataVo = request_disputeDataVo;
	}

}
