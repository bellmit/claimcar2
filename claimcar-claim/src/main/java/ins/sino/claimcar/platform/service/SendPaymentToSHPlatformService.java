/******************************************************************************
 * CREATETIME : 2016年6月12日 下午7:19:58
 ******************************************************************************/
package ins.sino.claimcar.platform.service;

import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.platform.vo.sh.SHBIPaymentBasePartVo;
import ins.sino.claimcar.platform.vo.sh.SHBIPaymentPayDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBIPaymentReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHCIPaymentBasePartVo;
import ins.sino.claimcar.platform.vo.sh.SHCIPaymentPayDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIPaymentReqBodyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ins.sino.claimcar.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**	赔款支付
 * @author ★XMSH
 */
@Service("sendPaymentToSHPlatformService")
public class SendPaymentToSHPlatformService {

	private Logger logger = LoggerFactory.getLogger(SendPaymentToSHPlatformService.class);

	@Autowired
	private CompensateService compensateService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	CiClaimPlatformLogService platformLogService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	RegistQueryService registQueryService;
	
	/**
	 * --赔款支付
	 */
	public CiClaimPlatformLogVo sendPaymentToSH(String compensateNo) throws Exception {
		PrpLCompensateVo compensateVo = compensateService.findCompByPK(compensateNo);
		if(Risk.DQZ.equals(compensateVo.getRiskCode())){//交强
			return sendPaymentCIToSHPlatform(compensateVo);
		}else{
			return sendPaymentBIToSHPlatform(compensateVo);
		}
	}
	/**
	 * 发送--赔款支付，调用方法
	 * @param bussNo
	 * @param reqType
	 * @param comCode
	 */
	public void sendPaymentToSHPlatform(CiClaimPlatformLogVo logVo){
		PrpLCompensateVo compensateVo = compensateService.findCompByPK(logVo.getBussNo());
		if(compensateVo != null){
			if(RequestType.PaymentBI_SH.getCode().equals(logVo.getRequestType())){
				sendPaymentBIToSHPlatform(compensateVo);
			}
			if(RequestType.PaymentCI_SH.getCode().equals(logVo.getRequestType())){
				sendPaymentCIToSHPlatform(compensateVo);
			}
		}
	}

	public CiClaimPlatformLogVo sendPaymentCIToSHPlatform(PrpLCompensateVo compensateVo) {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return null;
		}
		logger.info("");
//		PrpLCompensateVo compensateVo = compensateService.findCompByPK(compensateNo);
		String reqType = Risk.DQZ.equals(compensateVo.getRiskCode())?"11":"12";
		String comCode = policyViewService.findPolicyComCode(compensateVo.getRegistNo(),reqType);
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo
				(RequestType.RegistInfoCI_SH.getCode(), compensateVo.getRegistNo(), comCode);

		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.PaymentCI_SH);

		SHCIPaymentReqBodyVo bodyVo = new SHCIPaymentReqBodyVo();
		SHCIPaymentBasePartVo basePart = new SHCIPaymentBasePartVo();
		basePart.setClaimSeqNo(logVo == null ? "" : logVo.getClaimSeqNo());
		basePart.setCompensateNo(compensateVo.getCompensateNo());
		basePart.setClaimAmount(DataUtils.NullToZero(compensateVo.getSumAmt()).doubleValue());
		basePart.setPayMethod("04");
		String account = "0";
		String accountName = "无";
//		String bankName = "无";
		Date payTime = new Date();
		List<PrpLPaymentVo> paymentVoList = compensateVo.getPrpLPayments();
		if(paymentVoList != null && paymentVoList.size() > 0){
			Long payeeId = compensateVo.getPrpLPayments().get(0).getPayeeId();
			PrpLPayCustomVo customVo = managerService.findPayCustomVoById(payeeId);
			account = customVo.getAccountNo();
//			bankName = customVo.getPayeeName();
			accountName = customVo.getPayeeName();
			payTime = compensateVo.getPrpLPayments().get(0).getPayTime();
		}
//		basePart.setBankName(bankName);
		basePart.setBankAccount(account);
		basePart.setBankAccountName(accountName);
		basePart.setPayTime(payTime == null ? new Date() : payTime);

		/*************************标准地址库版本号、坐标数据、坐标系代码**************************/
		// 获取报案上传平台开关
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.REGISTTOPALTFORMFLAG, "22000000");
		// 如果开关打开，需要组织坐标数据及坐标系代码，坐标数据为空时，传(-1, -1)
		if ("1".equals(configValueVo.getConfigValue())) {
			// 经纬度
			basePart.setCoordinate(HttpUtils.getCoordinate(registVo.getDamageMapCode()));
			// 01-GPS坐标系  02-百度坐标系  03-火星坐标系，如高德、腾讯  04-2000 国家大地坐标系
			basePart.setCoordinateSystem("02");
		}
		/*************************标准地址库版本号、坐标数据、坐标系代码**************************/

		// 收款账户明细
		List<SHCIPaymentPayDataVo> payList = new ArrayList<SHCIPaymentPayDataVo>();
		for(PrpLPaymentVo paymentVo:compensateVo.getPrpLPayments()){
			SHCIPaymentPayDataVo payData = new SHCIPaymentPayDataVo();
			PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(paymentVo.getPayeeId());

			payData.setRBankAccount(payCustomVo.getAccountNo());
			payData.setRBankName(payCustomVo.getBankName());
			payData.setRBankAccountName(payCustomVo.getPayeeName());
			payData.setRBankCenticode(payCustomVo.getCertifyNo());
			payData.setClaimCode(logVo.getClaimSeqNo());
			// payData.setClaimAddCode(claimAddCode);
			payData.setClaimAmount(DataUtils.NullToZero(paymentVo.getSumRealPay()).doubleValue());
			payData.setClaimType("01");

			payList.add(payData);
		}
		bodyVo.setBasePart(basePart);
		bodyVo.setPayList(payList);

		return controller.callPlatform(bodyVo);
	}

	public CiClaimPlatformLogVo sendPaymentBIToSHPlatform(PrpLCompensateVo compensateVo) {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return null;
		}
		String reqType = Risk.DQZ.equals(compensateVo.getRiskCode())?"11":"12";
		String comCode = policyViewService.findPolicyComCode(compensateVo.getRegistNo(),reqType);
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo
				(RequestType.RegistInfoBI_SH.getCode(), compensateVo.getRegistNo(), comCode);
		
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.PaymentBI_SH);

		SHBIPaymentReqBodyVo bodyVo = new SHBIPaymentReqBodyVo();
		SHBIPaymentBasePartVo basePart = new SHBIPaymentBasePartVo();
		basePart.setClaimSeqNo(logVo == null ? "" : logVo.getClaimSeqNo());
		basePart.setCompensateNo(compensateVo.getCompensateNo());
		basePart.setClaimAmount(DataUtils.NullToZero(compensateVo.getSumAmt()).doubleValue());
		basePart.setPayMethod("01");
		String account = "0";
		String accountName = "无";
//		String bankName = "无";
		Date payTime = null;
		List<PrpLPaymentVo> paymentVoList = compensateVo.getPrpLPayments();
		if(paymentVoList != null && paymentVoList.size() > 0){
//			account = compensateVo.getPrpLPayments().get(0).getAccountNo();
			Long payeeId = compensateVo.getPrpLPayments().get(0).getPayeeId();
			PrpLPayCustomVo customVo = managerService.findPayCustomVoById(payeeId);
			account = customVo.getAccountNo();
//			bankName = customVo.getPayeeName();
			accountName = customVo.getPayeeName();
			payTime = compensateVo.getPrpLPayments().get(0).getPayTime();
		}
//		basePart.setBankName(bankName);
		basePart.setBankAccount(account);
		basePart.setBankAccountName(accountName);
		basePart.setPayTime(payTime == null ? new Date() : payTime);

		/*************************标准地址库版本号、坐标数据、坐标系代码**************************/
		// 获取报案上传平台开关
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.REGISTTOPALTFORMFLAG, "22000000");
		// 如果开关打开，需要组织坐标数据及坐标系代码，坐标数据为空时，传(-1, -1)
		if ("1".equals(configValueVo.getConfigValue())) {
			// 经纬度
			basePart.setCoordinate(HttpUtils.getCoordinate(registVo.getDamageMapCode()));
			// 01-GPS坐标系  02-百度坐标系  03-火星坐标系，如高德、腾讯  04-2000 国家大地坐标系
			basePart.setCoordinateSystem("02");
		}
		/*************************标准地址库版本号、坐标数据、坐标系代码**************************/

		// 收款账户明细
		List<SHBIPaymentPayDataVo> payList = new ArrayList<SHBIPaymentPayDataVo>();
		for(PrpLPaymentVo paymentVo:compensateVo.getPrpLPayments()){
			SHBIPaymentPayDataVo payData = new SHBIPaymentPayDataVo();
			PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(paymentVo.getPayeeId());

			payData.setRBankAccount(payCustomVo.getAccountNo());
			payData.setRBankName(payCustomVo.getBankName());
			payData.setRBankAccountName(payCustomVo.getPayeeName());
			payData.setRBankCenticode(payCustomVo.getCertifyNo());
			payData.setClaimCode(logVo.getClaimSeqNo());
			// payData.setClaimAddCode(claimAddCode);
			payData.setClaimAmount(DataUtils.NullToZero(paymentVo.getSumRealPay()).doubleValue());
			payData.setClaimType("01");

			payList.add(payData);
		}
		bodyVo.setBasePart(basePart);
		bodyVo.setPayList(payList);

		return controller.callPlatform(bodyVo);
	}

}
