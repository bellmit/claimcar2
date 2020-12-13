/******************************************************************************
* CREATETIME : 2016年4月1日 上午9:22:03
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 结算查询接口body
 * @author ★YangKun
 * @CreateTime 2016年4月1日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class AccountsQueryReqBodyVo {

	@XmlElement(name = "PacketInfo")
	private AccountsQueryPacketInfoVo packetInfo;
	
	@XmlElement(name = "BasePart")
	private AccountsQueryBaseVo accountsQueryBaseVo;

	
	public AccountsQueryPacketInfoVo getPacketInfo() {
		return packetInfo;
	}

	
	public void setPacketInfo(AccountsQueryPacketInfoVo packetInfo) {
		this.packetInfo = packetInfo;
	}

	
	public AccountsQueryBaseVo getAccountsQueryBaseVo() {
		return accountsQueryBaseVo;
	}

	
	public void setAccountsQueryBaseVo(AccountsQueryBaseVo accountsQueryBaseVo) {
		this.accountsQueryBaseVo = accountsQueryBaseVo;
	}
	
	
	
	
}
