package ins.sino.claimcar.lossperson.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("personnelInfo")
public class ILPersonnelInfoVo implements Serializable {

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("lossType")
	private String lossType;  //损失类型
	
	@XStreamAlias("lossItemType")
	private String lossItemType;  //人员属性
	
	@XStreamAlias("lossPartyName")
	private String lossPartyName;  //车牌号码 

	public String getLossType() {
		return lossType;
	}

	public void setLossType(String lossType) {
		this.lossType = lossType;
	}

	public String getLossItemType() {
		return lossItemType;
	}

	public void setLossItemType(String lossItemType) {
		this.lossItemType = lossItemType;
	}

	public String getLossPartyName() {
		return lossPartyName;
	}

	public void setLossPartyName(String lossPartyName) {
		this.lossPartyName = lossPartyName;
	}
	
	
}
