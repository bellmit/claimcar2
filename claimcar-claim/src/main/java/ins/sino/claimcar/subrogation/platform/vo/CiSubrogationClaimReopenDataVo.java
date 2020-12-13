/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

//重开赔案信息列表
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiSubrogationClaimReopenDataVo {
	
	/** 重开原因 **/ 
	@XmlElement(name="REOPEN_CAUSE")
	private String reopenCause; 

	/** 重开时间 格式:YYYYMMDDHHMM **/ 
	@XmlElement(name="REOPEN_DATE", required = true)
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date reopenDate;

	public String getReopenCause() {
		return reopenCause;
	}

	public void setReopenCause(String reopenCause) {
		this.reopenCause = reopenCause;
	}

	public Date getReopenDate() {
		return reopenDate;
	}

	public void setReopenDate(Date reopenDate) {
		this.reopenDate = reopenDate;
	} 


	
	
}
