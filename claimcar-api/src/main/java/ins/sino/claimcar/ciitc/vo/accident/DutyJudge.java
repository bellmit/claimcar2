package ins.sino.claimcar.ciitc.vo.accident;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("DutyJudge") 
public class DutyJudge implements Serializable{
	 private static final long serialVersionUID = 1L;

		@XStreamAlias("protocolAuditUnit")
		private String protocolAuditUnit;//自行协议书审核单位
		@XStreamAlias("protocolElectPic")
		private String protocolElectPic;//协议书电子图片地址
		@XStreamAlias("identificationOrgan")
		private String identificationOrgan;//认定机关名称
		@XStreamAlias("verificationElectPic")
		private String verificationElectPic;//认定书电子图片地址
		public String getProtocolAuditUnit() {
			return protocolAuditUnit;
		}
		public void setProtocolAuditUnit(String protocolAuditUnit) {
			this.protocolAuditUnit = protocolAuditUnit;
		}
		public String getProtocolElectPic() {
			return protocolElectPic;
		}
		public void setProtocolElectPic(String protocolElectPic) {
			this.protocolElectPic = protocolElectPic;
		}
		public String getIdentificationOrgan() {
			return identificationOrgan;
		}
		public void setIdentificationOrgan(String identificationOrgan) {
			this.identificationOrgan = identificationOrgan;
		}
		public String getVerificationElectPic() {
			return verificationElectPic;
		}
		public void setVerificationElectPic(String verificationElectPic) {
			this.verificationElectPic = verificationElectPic;
		}
		
		
}
