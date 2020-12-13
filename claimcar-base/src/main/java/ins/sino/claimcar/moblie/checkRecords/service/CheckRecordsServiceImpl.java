package ins.sino.claimcar.moblie.checkRecords.service;

import ins.framework.lang.Springs;
import ins.platform.utils.ClaimBaseCoder;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckBody;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckRequest;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.checkRecords.vo.MC_Body;
import ins.sino.claimcar.moblie.checkRecords.vo.MC_CheckLossInfo;
import ins.sino.claimcar.moblie.checkRecords.vo.MC_Packet;
import ins.sino.claimcar.moblie.logUtil.QuickClaimLogUtil;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;

public class CheckRecordsServiceImpl implements ServiceInterface {
private static Logger logger = LoggerFactory.getLogger(CheckRecordsServiceImpl.class);
	
	@Autowired
	private CheckTaskService checkTaskService;
	@Override
	public Object service(String arg0, Object arg1) {
		
		MC_Packet packet = new MC_Packet();
		 MobileCheckResponseHead head = new MobileCheckResponseHead();
		 head.setResponseType("006");
		 head.setResponseCode("YES");
		 head.setResponseMessage("Success");
		 String registNo ="";
		try {
			String reqXml = ClaimBaseCoder.objToXmlUtf(arg1);
			logger.info("=============查勘记录信息查询接口请求报文："+reqXml);
			registNo = this.validHeadInfo((MobileCheckRequest)arg1);
			
			PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
			if(checkVo == null ){
				throw new IllegalArgumentException(" 报案号错误，请检查报案号   ");
			}
			PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
			PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo, 1);
			Assert.notNull(checkVo, " 报案号异常 ");
			MC_Body body = new MC_Body();
			String isRobbery = "0";
			 List<MC_CheckLossInfo> checkLossList = new ArrayList<MC_CheckLossInfo>();
			 List<PrpLCheckCarVo> checkCarList = checkVo.getPrpLCheckTask().getPrpLCheckCars();
			 if(checkCarList != null && checkCarList.size() > 0){  // 车
				 for(PrpLCheckCarVo checkCarVo : checkCarList){
					 if(1==checkCarVo.getSerialNo()){
				    	 body.setDrivingCarType(checkCarVo.getPrpLCheckCarInfo().getBrandName());
				    	 if("G".equals(checkCarVo.getKindCode())){
				    		 isRobbery = "1";
				    	 }
					 }
					 MC_CheckLossInfo checkLossVo = new MC_CheckLossInfo();
					 checkLossVo.setLossType("1");  //-1	人伤       1	车辆（包含标的和三者）      0	物损     2	其他
					 checkLossVo.setLicenseNO(checkCarVo.getPrpLCheckCarInfo().getLicenseNo());
					 checkLossVo.setHandlerCode(checkCarVo.getCreateUser());
					 checkLossVo.setCerAmount(checkCarVo.getLossFee().toString());
					 checkLossVo.setPrieAmount(checkCarVo.getLossFee().toString());
					 checkLossVo.setVeriAmount(checkCarVo.getLossFee().toString());
					 checkLossList.add(checkLossVo);
				 }
			 }
			 List<PrpLCheckPropVo> checkPorpList =  checkVo.getPrpLCheckTask().getPrpLCheckProps();
			 if(checkPorpList != null && checkPorpList.size() > 0){  //财
				 for(PrpLCheckPropVo checkPropVo : checkPorpList){
					 MC_CheckLossInfo checkLossVo = new MC_CheckLossInfo();
					 checkLossVo.setLossType("0");
					 checkLossVo.setLicenseNO("");
					 checkLossVo.setHandlerCode(checkPropVo.getCreateUser());
					 checkLossVo.setCerAmount(checkPropVo.getLossFee().toString());
					 checkLossVo.setPrieAmount(checkPropVo.getLossFee().toString());
					 checkLossVo.setVeriAmount(checkPropVo.getLossFee().toString());
					 checkLossList.add(checkLossVo);
				 }
			 }
			 
			 List<PrpLCheckPersonVo> checkPersonList =  checkVo.getPrpLCheckTask().getPrpLCheckPersons();
			 if(checkPersonList != null && checkPersonList.size() > 0){  //人
				 for(PrpLCheckPersonVo checkPersonVo : checkPersonList){
					 MC_CheckLossInfo checkLossVo = new MC_CheckLossInfo();
					 checkLossVo.setLossType("-1");
					 checkLossVo.setLicenseNO("");
					 checkLossVo.setHandlerCode(checkPersonVo.getCreateUser());
					 checkLossVo.setCerAmount(checkPersonVo.getLossFee().toString());
					 checkLossVo.setPrieAmount(checkPersonVo.getLossFee().toString());
					 checkLossVo.setVeriAmount(checkPersonVo.getLossFee().toString());
					 checkLossList.add(checkLossVo);
				 }
			 }
			
	    	 body.setRegistNo(checkVo.getRegistNo());
	    	 body.setFirstSiteFlag(checkTaskVo.getFirstAddressFlag());
	    	 body.setDamageCode(checkVo.getDamageCode());
	    	 body.setIndemnityDuty(checkDutyVo.getIndemnityDuty());
	    	 body.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate().toString());
	    	 body.setIsRobbery(isRobbery);
	    	 body.setIsMajorCase(checkVo.getMajorCaseFlag());
	    	 body.setExcessType(checkVo.getIsClaimSelf());
	    	 body.setIsSubrogateType(checkVo.getIsSubRogation());
	    	 body.setBizLossAmount("");  
	    	 body.setLossAmount("");
	    	// body.setIsRejected("0"); //
	    	 body.setDamageType(checkVo.getDamageTypeCode());
	    	 body.setTextType(checkTaskVo.getContexts());
	    	// body.setDrivingCarType(drivingCarType);
	    	 body.setCheckLossList(checkLossList);
	    	 packet.setHead(head);
	    	 packet.setBody(body);
	    	 
			
		} catch (Exception e) {
			 head.setResponseType("006");
			 head.setResponseCode("NO");
			 head.setResponseMessage(e.getMessage());
			 packet.setHead(head);
			 logger.info("========查勘记录信息查询接口（快赔请求理赔异常，异常信息："+e.getMessage());
			 e.printStackTrace();
		}
		
		String resXml = ClaimBaseCoder.objToXmlUtf(packet);
		logger.info("=============查勘记录信息查询接口返回报文："+resXml);
		return  packet;
	}

	private String validHeadInfo(MobileCheckRequest packet){
		if(checkTaskService == null){
			checkTaskService = (CheckTaskService)Springs.getBean(CheckTaskService.class);
		}
		Assert.notNull(packet, " 请求信息为空  ");
		MobileCheckHead head = packet.getHead();
		if (!"006".equals(head.getRequestType())|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())) {
			throw new IllegalArgumentException(" 请求头参数错误  ");
		}
		MobileCheckBody body = packet.getBody();
		Assert.notNull(body.getRegistNo(), " 报案号为空  ");
		return body.getRegistNo();
	}
}
