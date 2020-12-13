/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

//理算核赔信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiSubrogationUnderWriteDataVo {
	
	@XmlElementWrapper(name = "ADJUSTMENT_LIST")
	@XmlElement(name = "ADJUSTMENT_DATA")
	private List<CiUndwrtWriteAdjustmentDataVo> adjustmentDataList;
	
/*	*//**	损失赔偿情况信息列表 *//*
	@XmlElementWrapper(name = "CLAIM_COVER_LIST")
	@XmlElement(name = "CLAIM_COVER_DATA")
	private List<CiUndwrtWriteCoverDataVo> coverDataList;
	
	*//**	追偿/清付信息列表  *//*
	@XmlElementWrapper(name = "RECOVERY_OR_PAY_LIST")
	@XmlElement(name = "RECOVERY_OR_PAY_DATA")
	private List<CiUndwrtWriteRecoveryOrPayDataVo> recoveryOrPayDataList;*/

	
	public List<CiUndwrtWriteAdjustmentDataVo> getAdjustmentDataList() {
		return adjustmentDataList;
	}

	
	public void setAdjustmentDataList(List<CiUndwrtWriteAdjustmentDataVo> adjustmentDataList) {
		this.adjustmentDataList = adjustmentDataList;
	}

	
	



}
