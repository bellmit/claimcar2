/******************************************************************************
 * CREATETIME : 2016年5月23日 下午4:13:12
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiCertifyReqBodyVo {

	@XmlElement(name = "BasePart")
	private BiCertifyBasePartVo basePart;

	@XmlElement(name = "DocDetailData")
	private List<BiCertifyDocDetailDataVo> docDetailDataList;

	@XmlElement(name = "SubrogationData")
	private List<BiCertifySubrogationDataVo> subrogationDataList;

	/**
	 * @return 返回 basePart。
	 */
	public BiCertifyBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(BiCertifyBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 docDetailDataList。
	 */
	public List<BiCertifyDocDetailDataVo> getDocDetailDataList() {
		return docDetailDataList;
	}

	/**
	 * @param docDetailDataList 要设置的 docDetailDataList。
	 */
	public void setDocDetailDataList(List<BiCertifyDocDetailDataVo> docDetailDataList) {
		this.docDetailDataList = docDetailDataList;
	}

	/**
	 * @return 返回 subrogationDataList。
	 */
	public List<BiCertifySubrogationDataVo> getSubrogationDataList() {
		return subrogationDataList;
	}

	/**
	 * @param subrogationDataList 要设置的 subrogationDataList。
	 */
	public void setSubrogationDataList(List<BiCertifySubrogationDataVo> subrogationDataList) {
		this.subrogationDataList = subrogationDataList;
	}

}
