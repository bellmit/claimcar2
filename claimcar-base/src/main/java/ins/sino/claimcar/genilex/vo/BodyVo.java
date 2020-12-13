package ins.sino.claimcar.genilex.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("soapenv:Body")
public class BodyVo {
        
        @XStreamAlias("DataPacket")
        private DataPacketReqVo dataPacketVo;
        
        public DataPacketReqVo getDataPacketVo() {
            return dataPacketVo;
        }
        
        public void setDataPacketVo(DataPacketReqVo dataPacketVo) {
            this.dataPacketVo = dataPacketVo;
        }
}
