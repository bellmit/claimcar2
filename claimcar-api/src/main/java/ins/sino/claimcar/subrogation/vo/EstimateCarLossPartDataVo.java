/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;


//车辆损失部位列表（隶属于车辆损失情况）
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
public class EstimateCarLossPartDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 车辆损失部位 **/ 
	private String lossPart;

	public String getLossPart() {
		return lossPart;
	}

	public void setLossPart(String lossPart) {
		this.lossPart = lossPart;
	}


}
