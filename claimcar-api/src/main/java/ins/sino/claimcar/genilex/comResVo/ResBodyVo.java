package ins.sino.claimcar.genilex.comResVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("soap:Body")
public class ResBodyVo  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("DataPacket")
	private DataPacketVo dataPacketVo;
	
	public DataPacketVo getDataPacketVo() {
		return dataPacketVo;
	}
	
	public void setDataPacketVo(DataPacketVo dataPacketVo) {
		this.dataPacketVo = dataPacketVo;
	}


}
