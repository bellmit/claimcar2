package ins.sino.claimcar.genilex.vo.endCase;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("soapenv:Body")
public class EndCaseBody implements Serializable{

	@XStreamAlias("DataPacket")
	private EndCasePacket dataPacket;

	public EndCasePacket getDataPacket() {
		return dataPacket;
	}

	public void setDataPacket(EndCasePacket dataPacket) {
		this.dataPacket = dataPacket;
	}
	
	
	
}
