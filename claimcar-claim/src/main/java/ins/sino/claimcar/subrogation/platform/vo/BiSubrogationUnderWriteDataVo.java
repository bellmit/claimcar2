/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//理算核赔信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiSubrogationUnderWriteDataVo {
	/*理算信息列表(隶属于理算核赔信息)*/
	@XmlElement(name = "AdjustmentData")
	private List<BiUndwrtWriteAdjustmentDataVo> adjustmentDataList;

	public List<BiUndwrtWriteAdjustmentDataVo> getAdjustmentDataList() {
		return adjustmentDataList;
	}

	public void setAdjustmentDataList(
			List<BiUndwrtWriteAdjustmentDataVo> adjustmentDataList) {
		this.adjustmentDataList = adjustmentDataList;
	}
	
	

}
