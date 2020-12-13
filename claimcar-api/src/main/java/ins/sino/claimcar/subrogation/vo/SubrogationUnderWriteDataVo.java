/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;
import java.util.List;

//理算核赔信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
public class SubrogationUnderWriteDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<UndwrtWriteAdjustmentDataVo> adjustmentDataList;
	/*
	*//**	损失赔偿情况信息列表 *//*
	private List<UndwrtWriteCoverDataVo> coverDataList;
	
	*//**	追偿/清付信息列表  *//*
	private List<UndwrtWriteRecoveryOrPayDataVo> recoveryOrPayDataList;*/

	public List<UndwrtWriteAdjustmentDataVo> getAdjustmentDataList() {
		return adjustmentDataList;
	}

	public void setAdjustmentDataList(
			List<UndwrtWriteAdjustmentDataVo> adjustmentDataList) {
		this.adjustmentDataList = adjustmentDataList;
	}

	

	
}
