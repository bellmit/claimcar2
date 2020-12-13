/******************************************************************************
 * CREATETIME : 2016年5月23日 下午3:13:20
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiCertifyDocDetailDataVo {

	@XmlElement(name = "DOC_NAME", required = true)
	private String docName;// 单证名称

	@XmlElement(name = "DOC_TYPE", required = true)
	private String docType;// 单证分类；参见代码

	@XmlElement(name = "REMARK")
	private String remark;// 备注

	/**
	 * @return 返回 docName 单证名称
	 */
	public String getDocName() {
		return docName;
	}

	/**
	 * @param docName 要设置的 单证名称
	 */
	public void setDocName(String docName) {
		this.docName = docName;
	}

	/**
	 * @return 返回 docType 单证分类；参见代码
	 */
	public String getDocType() {
		return docType;
	}

	/**
	 * @param docType 要设置的 单证分类；参见代码
	 */
	public void setDocType(String docType) {
		this.docType = docType;
	}

	/**
	 * @return 返回 remark 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark 要设置的 备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
