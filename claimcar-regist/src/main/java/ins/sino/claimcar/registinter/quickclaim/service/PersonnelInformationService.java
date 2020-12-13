package ins.sino.claimcar.registinter.quickclaim.service;

import ins.sino.claimcar.mobilecheck.service.MobileCheckService;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationReqVo;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationResScheduleItem;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationResVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.registinter.quickclaim.util.PersonnelInformationCoder;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 获取查勘定损员信息接口
 * @author zy
 *
 */
@Service(value = "personnelInformationService")
public class PersonnelInformationService {

	private static Logger logger = LoggerFactory.getLogger(PersonnelInformationService.class);

	@Autowired
	PersonnelInformationCoder personnelInformationCoder;
	
	@Autowired
	private MobileCheckService mobileCheckService;
	
	public void getPersonnelInformation(PrpLRegistVo registVo, List<PrpLScheduleItemsVo> itemsVoList,PrpLScheduleTaskVo scheduleTaskVo,String url) throws Exception {
		PersonnelInformationReqVo reqVo = personnelInformationCoder.enCode(registVo, itemsVoList);
		
		PersonnelInformationResVo resVo = mobileCheckService.getPersonnelInformation(reqVo,url);
		/*String xmlToSend = ClaimBaseCoder.objToXml(reqVo);
		
		//String url = "http://10.236.0.163:9001/MClaimPlatform/prplschedule/autoSchedule.do?xml=";
		String url = PropertiesUtils.getProperties("AUTOSCHEDULE_URL");
		url = url+"?xml=";
		
		logger.debug("send---------------------------"+xmlToSend);
		
		String xmlReturn = QuickClaimUtil.requestPlatform(xmlToSend, url, 50);
		
		logger.debug("return---------------------------"+xmlReturn);
		
		PersonnelInformationResVo resVo = personnelInformationCoder.xmlToObj(xmlReturn, PersonnelInformationResVo.class);
	    */
		List<PersonnelInformationResScheduleItem> list = resVo.getBody().getScheduleItemList();
		//默认设置推荐的人
		for(PersonnelInformationResScheduleItem item:list){
			for(PrpLScheduleItemsVo sceduleItem:itemsVoList){
				/*if(StringUtils.equals(item.getNodeType(), FlowNode.PLoss.name()) && StringUtils.equals(sceduleItem.getItemType(), CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PERSON)){
					logger.debug("PLoss 人伤跟踪人员"+item.getNextHandlerCode());
					sceduleItem.setScheduledUsercode(item.getNextHandlerCode());
					sceduleItem.setScheduledComcode(item.getScheduleObjectId());
				}else if(StringUtils.equals(item.getNodeType(), FlowNode.Check.name()) && !StringUtils.equals(sceduleItem.getItemType(), CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PERSON)){
					logger.debug("Check 查勘人员"+item.getNextHandlerCode());
					sceduleItem.setScheduledUsercode(item.getNextHandlerCode());
					sceduleItem.setScheduledComcode(item.getScheduleObjectId());
				}*/
				if(StringUtils.equals(item.getTaskId(), sceduleItem.getSerialNo())){
					logger.info("默认推荐人员"+item.getNodeType()+":"+item.getNextHandlerCode());
					sceduleItem.setScheduledUsercode(item.getNextHandlerCode());
					sceduleItem.setScheduledComcode(item.getScheduleObjectId());
					if("Check".equals(item.getNodeType())){
						scheduleTaskVo.setRelateHandlerMobile(item.getRelateHandlerMobile());
						scheduleTaskVo.setRelateHandlerName(item.getRelateHandlerName());
						scheduleTaskVo.setIsComuserCode(item.getIsComuserCode());
						if(StringUtils.isNotBlank(item.getCallNumber())){
							scheduleTaskVo.setCallNumber(item.getCallNumber());
						}
						
					}else{
						scheduleTaskVo.setPersonRelateHandlerMobile(item.getRelateHandlerMobile());
						scheduleTaskVo.setPersonRelateHandlerName(item.getRelateHandlerName());
						scheduleTaskVo.setPersonIsComuserCode(item.getIsComuserCode());
					}
					/*sceduleItem.setRelateHandlerName(item.getRelateHandlerName());
					sceduleItem.setRelateHandlerMobile(item.getRelateHandlerMobile());
					sceduleItem.setIsComuserCode(item.getIsComuserCode());*/
				}
				
			}
		}
	}
}
