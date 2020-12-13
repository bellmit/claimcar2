/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.Date;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**开始追偿确认
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiRecoveryConfirmDataVo {
	/** 开始追偿时间 **/ 
	@XmlElement(name="RecoveryStartTime", required = true)
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date recoveryStartTime;

	public Date getRecoveryStartTime() {
		return recoveryStartTime;
	}

	public void setRecoveryStartTime(Date recoveryStartTime) {
		this.recoveryStartTime = recoveryStartTime;
	} 



}
