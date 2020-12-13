/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//单证信息明细列表(隶属于单证信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiSubrogationDocDetailDataVo {
	@XmlElement(name = "DocName", required = true)
	private String docName;//单证名称


	/** 
	 * @return 返回 docName  单证名称
	 */ 
	public String getDocName(){ 
	    return docName;
	}

	/** 
	 * @param docName 要设置的 单证名称
	 */ 
	public void setDocName(String docName){ 
	    this.docName=docName;
	}





	
	
}
