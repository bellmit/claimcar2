/******************************************************************************
 * CREATETIME : 2016年5月30日 下午2:36:35
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 代位求偿信息列表（多条）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIClaimResRecoveryDataVo {

	@XmlElement(name = "RECOVERY_COM")
	private String recoveryCom;// 追偿保险公司代码

	@XmlElement(name = "RECOVERY_REPORT_NO")
	private String recoveryReportNo;// 追偿保险公司案件

	@XmlElement(name = "STATUS")
	private String status;// 追偿保险公司案件状态

	/**
	 * @return 返回 recoveryCom 追偿保险公司代码
	 */
	public String getRecoveryCom() {
		return recoveryCom;
	}

	/**
	 * @param recoveryCom 要设置的 追偿保险公司代码
	 */
	public void setRecoveryCom(String recoveryCom) {
		this.recoveryCom = recoveryCom;
	}

	/**
	 * @return 返回 recoveryReportNo 追偿保险公司案件
	 */
	public String getRecoveryReportNo() {
		return recoveryReportNo;
	}

	/**
	 * @param recoveryReportNo 要设置的 追偿保险公司案件
	 */
	public void setRecoveryReportNo(String recoveryReportNo) {
		this.recoveryReportNo = recoveryReportNo;
	}

	/**
	 * @return 返回 status 追偿保险公司案件状态
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status 要设置的 追偿保险公司案件状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
