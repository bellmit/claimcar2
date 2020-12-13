/******************************************************************************
 * CREATETIME : 2016年4月27日 上午10:15:55
 ******************************************************************************/
package platform.test;

import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.mobilecheck.service.MobileCheckService;
import ins.sino.claimcar.platform.service.CheckToPlatformService;
import ins.sino.claimcar.schedule.service.ScheduleService;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestTrueRollback;

/**
 * @author ★Luwei
 */
public class PlatformTest extends BaseTestTrueRollback {

	private static final Logger logger = LoggerFactory
			.getLogger(PlatformTest.class);

	@Autowired
	CheckToPlatformService checkToPlatformSevice;

	@Autowired
	CheckHandleService checkService;

	@Autowired
	CheckHandleService checkHandleService;
	

	@Autowired
	MobileCheckService mobileCheckService;
	
	@Autowired
	ScheduleService scheduleService;

	
	@Test
	public void test() {

		String registNo = "4000000201612060000910";
		String userName = "000000";
		try {
//			checkToPlatformSevice.sendCheckToPlatformCI(registNo);
			checkToPlatformSevice.sendCheckToPlatformBI(registNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
//	public void test2() throws Exception{
//		String registNo = "4000000201612060000099";
//		
////		List<PrpLScheduleDefLossVo> prpLScheduleDefLossList = scheduleService.findPrpLScheduleDefLossList("4000000201612060000101");
//		
//		logger.debug("");
//		
//		List<PrpLScheduleDefLossVo> prpLScheduleDefLossList = new ArrayList<PrpLScheduleDefLossVo>();
//		PrpLScheduleDefLossVo s = new PrpLScheduleDefLossVo();
//		s.setSerialNo(1);
//		prpLScheduleDefLossList.add(s);
//		
//		PersonnelInformationReqVo reqVo = new PersonnelInformationReqVo();
//
//		HeadReq head = new HeadReq();
//
//		head.setRequestType("PersonnelInformation");
//
//		PersonnelInformationReqBody reqBody = new PersonnelInformationReqBody();
//
//		List<PersonnelInformationReqScheduleItem> scheduleItemList = new ArrayList<PersonnelInformationReqScheduleItem>();
//
//		PersonnelInformationReqScheduleWF reqScheduleWf = new PersonnelInformationReqScheduleWF();
//		reqScheduleWf.setRegistNo(registNo);
//		reqScheduleWf.setPolicyNo("");
//		reqScheduleWf.setType("1");// 0-报案环节提交， 1-查勘提交调度
//		reqScheduleWf.setBusinessType("all");
//		reqScheduleWf.setCustomType("all");
//		reqScheduleWf.setCaseType("all");
//
//		for (PrpLScheduleDefLossVo vo : prpLScheduleDefLossList) {
//			if (vo == null)
//				continue;
//			PersonnelInformationReqScheduleItem reqItem = new PersonnelInformationReqScheduleItem();
//			String serialNo = vo.getSerialNo().toString();
//			reqItem.setTaskId(serialNo);
//			if (StringUtils.equals(vo.getDeflossType(),
//					CodeConstants.DeflossType.PropLoss)) {
//				reqItem.setNodeType(FlowNode.DLProp.toString());
//				reqItem.setItemNo(serialNo);
//			} else {
//				reqItem.setNodeType(FlowNode.DLCar.toString());
//				reqItem.setItemNo(serialNo);
//			}
//			String regionCode = "440111";
//			reqItem.setItemName(vo.getItemsName());
//			String provinceCode = StringUtils.substring(regionCode, 0, 2)
//					+ "0000";
//			reqItem.setProvinceCode(provinceCode);
//			String cityCode = StringUtils.substring(regionCode, 0, 4) + "00";
//			reqItem.setCityCode(cityCode);
//			reqItem.setRegionCode(regionCode);
//			reqItem.setDamageAddress("1234");
//			reqItem.setLngXlatY("113.286936,23.209802");
//			scheduleItemList.add(reqItem);
//		}
//
//		reqBody.setScheduleWF(reqScheduleWf);
//		reqBody.setScheduleItemList(scheduleItemList);
//
//		reqVo.setHead(head);
//		reqVo.setBody(reqBody);
//
//		PersonnelInformationResVo resVo = mobileCheckService
//				.getPersonnelInformation(reqVo);
//		List<PersonnelInformationResScheduleItem> list = resVo.getBody()
//				.getScheduleItemList();
//		// 默认设置推荐的人
//		for (PrpLScheduleDefLossVo vo : prpLScheduleDefLossList) {
//			if (vo == null)
//				continue;
//			Map<String, String> showMap = new HashMap<String, String>();
//			if ("0".equals(vo.getTaskFlag())) {
//				for (PersonnelInformationResScheduleItem item : list) {
//					if (StringUtils.equals(item.getTaskId(), vo.getSerialNo()
//							.toString())) {
//						vo.setScheduledUsercode(item.getNextHandlerCode());
//						vo.setScheduledComcode(item.getScheduleObjectId());
//						showMap.put(
//								item.getNextHandlerCode() + ","
//										+ item.getScheduleObjectId(),
//								item.getNextHandlerName());
//					}
//				}
//				vo.setShowMap(showMap);
//			}
//		}
//	}
//	@Test
//	public void test() throws ParseException{
//		
//		String registNo = "4000000201611010000449";
//		CiClaimDemandVo CiClaimDemandVo = carPlatHandleService.findCiClaimDemandVoByRegistNo(registNo);
//		
//		PlatformController controller = PlatformFactory.getInstance(CiClaimDemandVo.getComCode(),RequestType.CheckCI);
//		CICheckReqBodyVo requestBodyVo =null;// = checkToPlatformSevice.setRequestBodyCI(registNo,CiClaimDemandVo);
//		//BI_Request_BodyVo requestBodyVo = checkToPlatformSevice.setRequestBodyBI(registNo,CiClaimDemandVo);
//		logger.info("requestVo:",requestBodyVo);
//		try{
//			controller.callPlatform(requestBodyVo);
//			
//			CICheckResHeadVo resHeadVo=controller.getHeadVo(CICheckResHeadVo.class);
//			if(!"0000".equals(resHeadVo.getErrorCode())){
//				//logger.debug("错误代码："+resHeadVo.getErrorCode());
//				System.out.println("返回代码："+resHeadVo.getResponseCode());
//				System.out.println("错误代码："+resHeadVo.getErrorCode());
//				System.out.println("错误信息："+resHeadVo.getErrorMessage());
//			}
//			CICheckResBodyVo resBodyVo = controller.getBodyVo(CICheckResBodyVo.class);
//			System.out.println("000000--"+resBodyVo);
//			logger.debug("返回body："+resBodyVo);
//			resBodyVo.getRiskInfoVo();
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//	}


}
