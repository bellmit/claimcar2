package ins.sino.claimcar.genilex.vo.check;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("soapenv:Body")
public class CheckBody implements Serializable  {

	@XStreamAlias("DataPacket")
	private CheckPacket dataPacket;

	public CheckPacket getDataPacket() {
		return dataPacket;
	}

	public void setDataPacket(CheckPacket dataPacket) {
		this.dataPacket = dataPacket;
	}
	
	
}
