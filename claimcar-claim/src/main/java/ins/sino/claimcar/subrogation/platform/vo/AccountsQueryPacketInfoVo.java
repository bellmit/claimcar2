/******************************************************************************
* CREATETIME : 2016年4月1日 上午9:24:12
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年4月1日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountsQueryPacketInfoVo {
	
	@XmlElement(name = "PacketNo")
	private String packetNo;//数据包序号

	@XmlElement(name = "TotalPacketCount")
	private String totalPacketCount;//总包数

	@XmlElement(name = "TotalDataCount")
	private String totalDataCount;//总条数


	/** 
	 * @return 返回 packetNo  数据包序号
	 */ 
	public String getPacketNo(){ 
	    return packetNo;
	}

	/** 
	 * @param packetNo 要设置的 数据包序号
	 */ 
	public void setPacketNo(String packetNo){ 
	    this.packetNo=packetNo;
	}

	/** 
	 * @return 返回 totalPacketCount  总包数
	 */ 
	public String getTotalPacketCount(){ 
	    return totalPacketCount;
	}

	/** 
	 * @param totalPacketCount 要设置的 总包数
	 */ 
	public void setTotalPacketCount(String totalPacketCount){ 
	    this.totalPacketCount=totalPacketCount;
	}

	/** 
	 * @return 返回 totalDataCount  总条数
	 */ 
	public String getTotalDataCount(){ 
	    return totalDataCount;
	}

	/** 
	 * @param totalDataCount 要设置的 总条数
	 */ 
	public void setTotalDataCount(String totalDataCount){ 
	    this.totalDataCount=totalDataCount;
	}



}
