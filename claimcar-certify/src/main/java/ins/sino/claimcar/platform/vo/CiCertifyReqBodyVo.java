/******************************************************************************
 * CREATETIME : 2016年5月23日 下午3:05:52
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiCertifyReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private CiCertifyBasePartVo basePart;

	@XmlElementWrapper(name = "DOC_DETAIL_LIST")
	@XmlElement(name = "DOC_DETAIL_DATA")
	private List<CiCertifyDocDetailDataVo> docDetailDatas;

	/**
	 * @return 返回 basePart。
	 */
	public CiCertifyBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(CiCertifyBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 docDetailDatas。
	 */
	public List<CiCertifyDocDetailDataVo> getDocDetailDatas() {
		return docDetailDatas;
	}

	/**
	 * @param docDetailDatas 要设置的 docDetailDatas。
	 */
	public void setDocDetailDatas(List<CiCertifyDocDetailDataVo> docDetailDatas) {
		this.docDetailDatas = docDetailDatas;
	}

}
