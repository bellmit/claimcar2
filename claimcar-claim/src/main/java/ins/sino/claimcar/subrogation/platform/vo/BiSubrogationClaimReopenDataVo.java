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
public class BiSubrogationClaimReopenDataVo {
	
	@XmlElement(name = "ReopenCause")
	private String reopenCause;//重开原因

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "ReopenDate", required = true)
	private Date reopenDate;//重开时间；精确到分


	/** 
	 * @return 返回 reopenCause  重开原因
	 */ 
	public String getReopenCause(){ 
	    return reopenCause;
	}

	/** 
	 * @param reopenCause 要设置的 重开原因
	 */ 
	public void setReopenCause(String reopenCause){ 
	    this.reopenCause=reopenCause;
	}

	/** 
	 * @return 返回 reopenDate  重开时间；精确到分
	 */ 
	public Date getReopenDate(){ 
	    return reopenDate;
	}

	/** 
	 * @param reopenDate 要设置的 重开时间；精确到分
	 */ 
	public void setReopenDate(Date reopenDate){ 
	    this.reopenDate=reopenDate;
	}



}
