package ins.sino.claimcar.genilex.dlossReqVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("soapenv:Body")
public class BodyVo implements Serializable{
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
