/******************************************************************************
* CREATETIME : 2016年6月13日 下午3:57:56
******************************************************************************/
package ins.sino.claimcar.platform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.EstimatedLossAmountInfoVo;
import ins.sino.claimcar.claim.vo.EstimatedLossAmountReqBodyVo;


/**
 * 未决案件最新估损金额上传
 * @author ★XMSH
 */
public class SendEstimatedLossAmountToPlatformService {
	
	@Autowired
	private ClaimTaskService claimTaskService;
	
	public void sendEstimatedLossAmountToPlatform(){
		
		String rule = SpringProperties.getProperty("dhic.newclaim.comcode");//获取上线机构
		String[] comCodes = rule.split(",");
		
		for(String comCode : comCodes){
			String code = "";
			if(comCode.length()==4){
				code = comCode + "0000";
			}else{
				code = comCode + "000000";
			}
			
			PlatformController controller = PlatformFactory.getInstance(code,RequestType.EstimatedLossAmount);
			List<EstimatedLossAmountInfoVo> infoList = claimTaskService.getClaimVoByEstimatedLossAmount(comCode);
			
			EstimatedLossAmountReqBodyVo bodyVo = new EstimatedLossAmountReqBodyVo();
			bodyVo.setEstimatedLossAmountInfo(infoList);
			
			controller.callPlatform(bodyVo);
		}
	}

}
