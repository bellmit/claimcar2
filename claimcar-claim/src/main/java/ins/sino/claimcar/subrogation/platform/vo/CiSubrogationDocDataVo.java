/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

//单证信息明细列表(隶属于单证信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiSubrogationDocDataVo {
	

	@XmlElementWrapper(name = "DOC_DETAIL_LIST")
	@XmlElement(name = "DOC_DETAIL_DATA")
	private List<CiSubrogationDocDetailDataVo> docDetailDataList;

	public List<CiSubrogationDocDetailDataVo> getDocDetailDataList() {
		return docDetailDataList;
	}

	public void setDocDetailDataList(
			List<CiSubrogationDocDetailDataVo> docDetailDataList) {
		this.docDetailDataList = docDetailDataList;
	}

	
	
	
	
	
}
