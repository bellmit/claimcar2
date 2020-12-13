/******************************************************************************
 * CREATETIME : 2016年6月1日 下午2:38:59
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 单证明细（多条）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIEndCaseAddReqDocDataVo {

	/**
	 * 单证名称
	 */
	@XmlElement(name = "DOC_NAME")
	private String docName;// 单证名称

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

}
