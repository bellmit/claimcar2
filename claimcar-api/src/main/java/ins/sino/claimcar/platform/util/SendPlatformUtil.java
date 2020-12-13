/******************************************************************************
* CREATETIME : 2016年11月1日 下午8:37:21
******************************************************************************/
package ins.sino.claimcar.platform.util;

import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

/**
 * <pre>平台是否上传校验</pre>
 * @author ★Luwei
 */
public class SendPlatformUtil {
	
	private static Logger logger = LoggerFactory.getLogger(SendPlatformUtil.class);
	

	
	/**
	 * <pre>发送平台入口</pre>
	 * @param requestType-交易请求类型
	 * @param comCode-保单机构
	 * @param bodyVo-
	 * @param registNo-报案号
	 * @return
	 * @modified:
	 * ☆Luwei(2016年11月2日 上午10:01:08): <br>
	 */
	public CiClaimPlatformLogVo sendPlatform(RequestType requestType,String comCode,Object bodyVo,String registNo){
		CiClaimPlatformLogVo platformLogVo = null;
		//平台类型
//		PlatfromType platformType = requestType.getPlatformType();
//		PlatformController controller = PlatformFactory.getInstance(comCode,requestType);
		//平台发送校验
//		if(isMor(platformType,comCode,registNo)){
//			platformLogVo = controller.callPlatform(bodyVo);
//		}else{
//			logger.debug("============无需上传平台的数据，业务号："+registNo+"=========请求类型："+platformType);
//		}
		return platformLogVo;
	}
	
	/**
	 * <pre>判断承保车辆是否是摩托车、拖拉机（交强不上传平台，商业（1203,1208,1209不上传平台））</pre>
	 * @param bussNo,上传平台的参数,报案号或立案号
	 * @param PlatfromType,上传平台类型
	 * @param comCode,上传平台的机构
	 * @return-false(不需上传平台),true(需上传平台)
	 * @modified:
	 * ☆Luwei(2016年11月1日 下午8:39:35): <br>
	 */
	public static boolean isMor(PrpLCMainVo cMainVo){
		logger.info("========报案号：===========");
		boolean isMor = true;
	
		//排除粤Z
		String comCode =  SpringProperties.getProperty("NAMEVERIFIED_SMS_COM");
		if(StringUtils.isNotBlank(cMainVo.getComCode()) 
				&& StringUtils.isNotBlank(comCode)
				&& comCode.contains(cMainVo.getComCode().substring(0,2))
				&&StringUtils.isBlank(cMainVo.getValidNo())){
			PrpLCItemCarVo cItemCarVo = cMainVo.getPrpCItemCars().get(0);
			if(cItemCarVo.getLicenseNo() != null && cItemCarVo.getLicenseNo().startsWith(CodeConstants.licenseNoStart)){
				return false;
			}
		}
		//排除上海平台
		if(!cMainVo.getComCode().startsWith("22")){
			String riskCode = cMainVo.getRiskCode();
			if(isMor2(cMainVo)){//保险车辆是拖拉机、摩托车车型
				if(Risk.DQZ.equals(riskCode)){//交强平台
					isMor = false;
				}else{//商业平台
					if(Risk.isDAC(riskCode)){
						isMor = false;
					}
				}
			}
			if (Risk.isDBC(riskCode) || Risk.isDBT(riskCode) || Risk.isDBD(riskCode) || Risk.isDBG(riskCode)) {//全国的1208、1209,1232,1233不上平台
				isMor = false;
			}
		}
		return isMor;
	}
	
	/**
	 * <pre>判断保险车辆是否为拖拉机、摩托车车型,</pre>
	 * @param registNo
	 * @modified:
	 * ☆Luwei(2016年11月2日 上午10:24:40): <br>
	 */
	private static boolean isMor2(PrpLCMainVo cMainVo) {
		boolean mor = false;
		if(cMainVo !=null){
			PrpLCItemCarVo cItemCar = cMainVo.getPrpCItemCars().get(0);
			String carKindCode = cItemCar.getCarKindCode();
			if(StringUtils.isNotBlank(carKindCode)&&"J,M".indexOf(carKindCode.substring(0,1)) > -1){
				mor = true;// 保险车辆为拖拉机、摩托车
			}
			if(Risk.isDAC(cMainVo.getRiskCode())){
				mor = true;
			}
		}
		return mor;
	}
	
}
