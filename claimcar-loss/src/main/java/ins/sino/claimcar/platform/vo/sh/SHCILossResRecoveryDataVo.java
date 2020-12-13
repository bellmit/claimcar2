/******************************************************************************
 * CREATETIME : 2016年5月26日 下午6:14:16
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 代位求偿信息列表（多条）RECOVERY_LIST属于代位求偿码信息
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCILossResRecoveryDataVo {

	/**
	 * 追偿保险公司代码
	 */
	@XmlElement(name = "RECOVERY_COM")
	private String recoveryCom;

	/**
	 * 追偿保险公司案件
	 */
	@XmlElement(name = "RECOVERY_REPORT_NO")
	private String recoveryReportNo;

	/**
	 * 追偿保险公司案件状态
	 */
	@XmlElement(name = "STATUS")
	private Date status;

	public String getRecoveryCom() {
		return recoveryCom;
	}

	public void setRecoveryCom(String recoveryCom) {
		this.recoveryCom = recoveryCom;
	}

	public String getRecoveryReportNo() {
		return recoveryReportNo;
	}

	public void setRecoveryReportNo(String recoveryReportNo) {
		this.recoveryReportNo = recoveryReportNo;
	}

	public Date getStatus() {
		return status;
	}

	public void setStatus(Date status) {
		this.status = status;
	}

}
