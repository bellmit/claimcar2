package ins.sino.claimcar.genilex.dlossReqVo;

import java.io.Serializable;
import java.util.List;

import ins.sino.claimcar.genilex.vo.common.FraudRequest;
import ins.sino.claimcar.genilex.vo.common.Requestor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("DataPacket")
public class DataPacketVo implements Serializable {
	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	@XStreamAlias("xmlns")
	private String xmlns="http://soap.fraud.message.genilex.com";
	@XStreamAlias("PacketType")
	private String packetType;
	@XStreamAlias("Requestor")
	private Requestor requestorVo;
	@XStreamAlias("ProductRequests")
	private List<FraudRequest> fraudRequests;
	@XStreamAlias("Entities")
	private EntitiesVo entitiesVo;
	public String getXmlns() {
		return xmlns;
	}
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	public String getPacketType() {
		return packetType;
	}
	public void setPacketType(String packetType) {
		this.packetType = packetType;
	}
	public Requestor getRequestorVo() {
		return requestorVo;
	}
	public void setRequestorVo(Requestor requestorVo) {
		this.requestorVo = requestorVo;
	}
	public EntitiesVo getEntitiesVo() {
		return entitiesVo;
	}
	public void setEntitiesVo(EntitiesVo entitiesVo) {
		this.entitiesVo = entitiesVo;
	}
	public List<FraudRequest> getFraudRequests() {
		return fraudRequests;
	}
	public void setFraudRequests(List<FraudRequest> fraudRequests) {
		this.fraudRequests = fraudRequests;
	}
	
	
	
	

}
