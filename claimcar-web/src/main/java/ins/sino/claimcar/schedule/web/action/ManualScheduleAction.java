package ins.sino.claimcar.schedule.web.action;

import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.hnbxrest.service.QuickUserService;
import ins.sino.claimcar.hnbxrest.vo.PrplQuickUserVo;
import ins.sino.claimcar.mobilecheck.service.MobileCheckService;
import ins.sino.claimcar.mobilecheck.vo.FixedPositionBackReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleBackReqScheduleItemSD;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleBackReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqBody;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqSDBody;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleItem;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleItemSD;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleSD;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleWF;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleSDBackReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleSDReqVo;
import ins.sino.claimcar.mobilecheck.vo.HeadReq;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/manualSchedule")
public class ManualScheduleAction {
	
	private static Logger logger = LoggerFactory.getLogger(ManualScheduleAction.class);
	
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	ScheduleTaskService scheduleTaskService;
	@Autowired
	private MobileCheckService mobileCheckService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	AreaDictService areaDictService;
	@Autowired
	QuickUserService quickUserService;
	
	public static final String AUTOSCHEDULE_URL_METHOD = "prplschedule/autoSchedule.do";
	public static final String HANDLSCHEDULE_URL_METHOD = "prplschedule/handlSchedule.do";
	public static final String HANDLSCHEDULE_BACKURL_METHOD = "/manualSchedule/backMapValue.do";
	
	
	/**
	 * 调度查勘
	 * @param registNo
	 * @param ids
	 * @param checkAreaCode
	 * @param checkAddress
	 * @param lngXlatY
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/checkScheduleOpenMap.ajax")
	@ResponseBody
	public AjaxResult checkScheduleOpenMap(String registNo, String ids,
			String checkAreaCode, String checkAddress, String lngXlatY,String selfDefinareaCode,HttpServletRequest request) throws Exception {
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		List<Long> IdList = new ArrayList<Long>();
		if(StringUtils.isNotBlank(ids)){
			String[] arrIds = ids.split(",");
			for (String str:arrIds) {
				IdList.add(Long.valueOf(str));
			}
		}
		String[] code = areaDictService.findAreaByAreaCode(checkAreaCode,"");
		String provinceCode = code[0];
		String cityCode = code[1];
		String regionCode = checkAreaCode;
		
		HandleScheduleReqVo reqVo = new HandleScheduleReqVo();
		
		HeadReq head = setHeadReq();//设置头部信息
		
		HandleScheduleReqBody body = new HandleScheduleReqBody();
		
		HandleScheduleReqScheduleWF scheduleWf = this.setHandleScheduleReqScheduleWF(registVo);
		
		List<HandleScheduleReqScheduleItem> scheduleItemList = new ArrayList<HandleScheduleReqScheduleItem>();
		HandleScheduleReqScheduleItem personScheduleItemListResult = new HandleScheduleReqScheduleItem();
		HandleScheduleReqScheduleItem scheduleItemResult = new HandleScheduleReqScheduleItem();
		
		List<HandleScheduleReqScheduleItem> scheduleItemListResult = new ArrayList<HandleScheduleReqScheduleItem>();
		List<PrpLScheduleItemsVo> vos = new ArrayList<PrpLScheduleItemsVo>();
		if(IdList.size() > 0){
			vos = scheduleTaskService.getPrpLScheduleItemsVoByIds(IdList);
		}else{//后面加的调度人伤跟踪任务。且每次只能增加一个
			PrpLScheduleItemsVo prpLScheduleItemsVo = new PrpLScheduleItemsVo();
			prpLScheduleItemsVo.setId(new Long(1));//虚拟ID
			prpLScheduleItemsVo.setSerialNo("-1");
			prpLScheduleItemsVo.setItemsName("人伤");
			prpLScheduleItemsVo.setItemType(CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PERSON);
			vos.add(prpLScheduleItemsVo);
		}
		for (PrpLScheduleItemsVo vo : vos) {
			HandleScheduleReqScheduleItem item = new HandleScheduleReqScheduleItem();
			item.setTaskId(vo.getId().toString());
			item.setNodeType(StringUtils.equals(vo.getItemType(), CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PERSON)
					? FlowNode.PLoss.toString() : FlowNode.Check.toString());
			if(item.getNodeType().equals( FlowNode.PLoss.name())){
				scheduleWf.setCaseType("5");//人伤案件
			}
			item.setItemNo(vo.getSerialNo());
			item.setItemName(vo.getItemsName());
			item.setProvinceCode(provinceCode);
			item.setCityCode(cityCode);
			item.setRegionCode(regionCode);
			item.setDamageAddress(checkAddress);
			item.setLngXlatY(lngXlatY != null ? lngXlatY : "");
			item.setSelfDefinareaCode(selfDefinareaCode);
			scheduleItemList.add(item);
		}
		
		
		//排除
		for(HandleScheduleReqScheduleItem Listvo : scheduleItemList){
			if(Listvo.getNodeType().equals("PLoss")){
				personScheduleItemListResult = Listvo;
			}
			if(Listvo.getNodeType().equals("Check")){
				scheduleItemResult = Listvo;
			}
		}
		if(personScheduleItemListResult !=null && personScheduleItemListResult.getNodeType() != null){
			scheduleItemListResult.add(personScheduleItemListResult);
		}
		if(scheduleItemResult !=null && scheduleItemResult.getNodeType() != null){
			scheduleItemListResult.add(scheduleItemResult);
		}
		body.setScheduleWF(scheduleWf);
		body.setScheduleItemList(scheduleItemListResult);
		
		reqVo.setHead(head);
		reqVo.setBody(body);
		String serverPort = request.getServerPort()+"";
		String serverName = request.getServerName();
		String contextPath = request.getContextPath();
		String net_protocol = SpringProperties.getProperty("net_protocol");
		String backUrl = net_protocol + serverName + ":"+ serverPort + contextPath;
		logger.info("调度/新增查勘-电子地图部分请求参数：serverName=" + serverName + ", serverPort= " + serverPort + "contextPath=" + contextPath + ", backUrl="+backUrl);
		String url1 = SpringProperties.getProperty("MClaimPlatform_URL")+HANDLSCHEDULE_URL_METHOD;
		String url = mobileCheckService.getHandelScheduleUrl(reqVo,backUrl+HANDLSCHEDULE_BACKURL_METHOD,url1);
		url = url.replaceAll("\"", "\'");
		
		AjaxResult result = new AjaxResult();
		result.setData(url);
		result.setStatus(HttpStatus.SC_OK);
		
		return result;
	}
	
	/**
	 * 调度定损
	 * @param registNo
	 * @param typeFlag
	 * @param checkAreaCode
	 * @param checkAddress
	 * @param lngXlatY
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/defLossScheduleOpenMap.ajax")
	@ResponseBody
	public AjaxResult defLossScheduleOpenMap(String registNo, String typeFlag,
			String checkAreaCode, String checkAddress, String lngXlatY,String selfDefinareaCode,HttpServletRequest request) throws Exception {
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		String[] code = areaDictService.findAreaByAreaCode(checkAreaCode,"");
		String provinceCode = code[0];
		String cityCode = code[1];
		String regionCode = checkAreaCode;
		
		HandleScheduleReqVo reqVo = new HandleScheduleReqVo();
		
		HeadReq head = setHeadReq();//设置头部信息
		
		HandleScheduleReqBody body = new HandleScheduleReqBody();
		
		HandleScheduleReqScheduleWF scheduleWf = this.setHandleScheduleReqScheduleWF(registVo);
		
		List<HandleScheduleReqScheduleItem> scheduleItemList = new ArrayList<HandleScheduleReqScheduleItem>();
		
		//后面增加定损任务可以增加多个任务，只能车损或者物损其中一一种,但是只能指派给同一个人，所以只需构建一个车损或者物损项
		
		HandleScheduleReqScheduleItem item = new HandleScheduleReqScheduleItem();
		item.setTaskId("1");//虚拟ID
		item.setNodeType(StringUtils.equals(typeFlag, CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PROP)
				? FlowNode.DLProp.toString() : FlowNode.DLCar.toString());
		item.setItemNo(StringUtils.equals(typeFlag, CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PROP)
				? "0" : "1");
		item.setItemName(StringUtils.equals(typeFlag, CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PROP)
				? "物损" : "车损");
		item.setProvinceCode(provinceCode);
		item.setCityCode(cityCode);
		item.setRegionCode(regionCode);
		item.setDamageAddress(checkAddress);
		item.setLngXlatY(lngXlatY != null ? lngXlatY : "");
		item.setSelfDefinareaCode(selfDefinareaCode);
		scheduleItemList.add(item);
		
		body.setScheduleWF(scheduleWf);
		body.setScheduleItemList(scheduleItemList);
		
		reqVo.setHead(head);
		reqVo.setBody(body);
		//获取url
		String serverPort = request.getServerPort()+"";
		String serverName = request.getServerName();
		String contextPath = request.getContextPath();
		String net_protocol = SpringProperties.getProperty("net_protocol");
		String backUrl = net_protocol + serverName + ":"+ serverPort + contextPath;
		logger.info("调度定损-电子地图部分请求参数：serverName=" + serverName + ", serverPort= " + serverPort + "contextPath=" + contextPath + ", backUrl="+backUrl);
		String url1 = SpringProperties.getProperty("MClaimPlatform_URL")+HANDLSCHEDULE_URL_METHOD;
		String url = mobileCheckService.getHandelScheduleUrl(reqVo,backUrl+HANDLSCHEDULE_BACKURL_METHOD,url1);
		url = url.replaceAll("\"", "\'");
		
		logger.debug(url);
		
		AjaxResult result = new AjaxResult();
		result.setData(url);
		result.setStatus(HttpStatus.SC_OK);
		
		return result;
	}
	
	/**
	 * 改派
	 * @param registNo
	 * @param typeFlag
	 * @param checkAreaCode
	 * @param checkAddress
	 * @param lngXlatY
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/reassignmentScheduleOpenMap.ajax")
	@ResponseBody
	public AjaxResult reassignmentScheduleOpenMap(String registNo, String typeFlag,
			String checkAreaCode, String checkAddress, String lngXlatY,String selfDefinareaCode
			,HttpServletRequest request) throws Exception {
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		String[] code = areaDictService.findAreaByAreaCode(checkAreaCode,"");
		String provinceCode = code[0];
		String cityCode = code[1];
		String regionCode = checkAreaCode;
		
		HandleScheduleReqVo reqVo = new HandleScheduleReqVo();
		
		HeadReq head = setHeadReq();//设置头部信息
		
		HandleScheduleReqBody body = new HandleScheduleReqBody();
		
		HandleScheduleReqScheduleWF scheduleWf = this.setHandleScheduleReqScheduleWF(registVo);
		
		List<HandleScheduleReqScheduleItem> scheduleItemList = new ArrayList<HandleScheduleReqScheduleItem>();
		
		//改派每次只能改派一个任务，查勘，人伤跟踪，车损，物损
		PrpLScheduleItemsVo prpLScheduleItemsVo = new PrpLScheduleItemsVo();
		prpLScheduleItemsVo.setId(new Long(1));//虚拟ID
		if(typeFlag.equals(FlowNode.PLoss.name())){
			prpLScheduleItemsVo.setSerialNo("-1");
			prpLScheduleItemsVo.setItemsName("人伤");
			prpLScheduleItemsVo.setItemType(CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PERSON);
		}else if(typeFlag.equals(FlowNode.DLCar.name())){
			prpLScheduleItemsVo.setSerialNo("2");
			prpLScheduleItemsVo.setItemsName("车损");
			prpLScheduleItemsVo.setItemType(CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_THIRDCAR);
		}else if(typeFlag.equals(FlowNode.DLProp.name())){
			prpLScheduleItemsVo.setSerialNo("0");
			prpLScheduleItemsVo.setItemsName("物损");
			prpLScheduleItemsVo.setItemType(CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PROP);
		}else if(typeFlag.equals(FlowNode.Check.name())){
			prpLScheduleItemsVo.setSerialNo("1");
			prpLScheduleItemsVo.setItemsName("查勘");
			prpLScheduleItemsVo.setItemType(CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_MAINCAR);
		}
		
		HandleScheduleReqScheduleItem item = new HandleScheduleReqScheduleItem();
		item.setTaskId(prpLScheduleItemsVo.getId().toString());
		item.setNodeType(typeFlag);
		if(item.getNodeType().equals( FlowNode.PLoss.name())){
			scheduleWf.setCaseType("5");//人伤案件
		}
		item.setItemNo(prpLScheduleItemsVo.getSerialNo());
		item.setItemName(prpLScheduleItemsVo.getItemsName());
		item.setProvinceCode(provinceCode);
		item.setCityCode(cityCode);
		item.setRegionCode(regionCode);
		item.setDamageAddress(checkAddress);
		item.setLngXlatY(lngXlatY != null ? lngXlatY : "");
		item.setSelfDefinareaCode(selfDefinareaCode);
		scheduleItemList.add(item);
		
		body.setScheduleWF(scheduleWf);
		body.setScheduleItemList(scheduleItemList);
		
		reqVo.setHead(head);
		reqVo.setBody(body);
		//获取url
		String serverPort = request.getServerPort()+"";
		String serverName = request.getServerName();
		String contextPath = request.getContextPath();
		String net_protocol = SpringProperties.getProperty("net_protocol");
		String backUrl = net_protocol + serverName + ":"+ serverPort + contextPath;
		logger.info("调度改派-电子地图部分请求参数：serverName=" + serverName + ", serverPort= " + serverPort + "contextPath=" + contextPath + ", backUrl="+backUrl);
		String url1 = SpringProperties.getProperty("MClaimPlatform_URL")+HANDLSCHEDULE_URL_METHOD;
		String url = mobileCheckService.getHandelScheduleUrl(reqVo,backUrl+HANDLSCHEDULE_BACKURL_METHOD,url1);
		url = url.replaceAll("\"", "\'");
		
		AjaxResult result = new AjaxResult();
		result.setData(url);
		result.setStatus(HttpStatus.SC_OK);
		
		return result;
	}



	@RequestMapping(value = "/backMapValue.do")
	@ResponseBody
	public ModelAndView backMapValue(String xml) throws UnsupportedEncodingException {
		//logger.info(new String(xml.getBytes(), "GBK"));
		URLDecoder urlDecoder = new URLDecoder();
		
		xml = urlDecoder.decode(xml, "UTF-8");
        logger.info("调度地图返回==========="+xml);
        HandleScheduleBackReqVo scheduleBackReqVo = ClaimBaseCoder.xmlToObj(xml, HandleScheduleBackReqVo.class);
		ModelAndView mav = new ModelAndView();
		mav.addObject("xml", xml);
		mav.addObject("res", scheduleBackReqVo.getBody().getScheduleItemList());
		mav.setViewName("schedule/scheduleEdit/SchOpenQuickClaimMap");
		return mav;
	}
	
	@RequestMapping(value = "/backPositionMapValue.do")
	@ResponseBody
	public ModelAndView backPositionMapValue(String xml) throws UnsupportedEncodingException {
		URLDecoder urlDecoder = new URLDecoder();
		String returnXml[] = xml.split(";");
		logger.info(returnXml[0]+":"+returnXml[1]);
        xml = urlDecoder.decode(returnXml[1], "GBK");
        FixedPositionBackReqVo fixedPositionBackReqVo = ClaimBaseCoder.xmlToObj(xml, FixedPositionBackReqVo.class);
		ModelAndView mav = new ModelAndView();
		mav.addObject("res", fixedPositionBackReqVo.getBody());
		mav.addObject("item", returnXml[0]);
		mav.setViewName("schedule/scheduleEdit/OpenPosituinMap");
		return mav;
	}
	
	
	/**
	 * 设置头部信息
	 * @return
	 */
	private HeadReq setHeadReq(){
		HeadReq head = new HeadReq();
		head.setRequestType("PersonnelInformation");
		head.setPassword("mclaim_psd");
		head.setUser("mclaim_user");
		return head;
	}
	
	
	private HandleScheduleReqScheduleWF setHandleScheduleReqScheduleWF(PrpLRegistVo registVo){
		HandleScheduleReqScheduleWF scheduleWf = new HandleScheduleReqScheduleWF();
		scheduleWf.setRegistNo(registVo.getRegistNo());
		scheduleWf.setPolicyNo(registVo.getPolicyNo());
		scheduleWf.setDamageTime(registVo.getDamageTime());
		scheduleWf.setReportTime(registVo.getReportTime());
		scheduleWf.setLicenseNo(registVo.getPrpLRegistExt().getLicenseNo());
		scheduleWf.setInsuredName(registVo.getPrpLRegistExt().getInsuredName());
		
		this.setScheduleWf(scheduleWf, registVo);
		return scheduleWf;
	}
	
	private HandleScheduleReqScheduleSD setHandleScheduleReqScheduleSD(PrpLRegistVo registVo){
		HandleScheduleReqScheduleSD scheduleWf = new HandleScheduleReqScheduleSD();
		scheduleWf.setRegistNo(registVo.getRegistNo());
		scheduleWf.setPolicyNo(registVo.getPolicyNo());
		scheduleWf.setType("2");
		this.setScheduleWf(scheduleWf, registVo);
		return scheduleWf;
	}
	
	/**
	 * 自定义区域人员获取 改派
	 * @param registNo
	 * @param ids
	 * @param checkAreaCode
	 * @param checkAddress
	 * @param lngXlatY
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/selfDefineOpenMap.ajax")
	@ResponseBody
	public AjaxResult selfDefineOpenMap(@RequestParam("id")String id,String registNo, String typeFlag,
			String checkAreaCode, String checkAddress, String lngXlatY,@FormModel(value = "prpLScheduleDefLosses") List<PrpLScheduleDefLossVo> prpLScheduleDefLoss) throws Exception {
		//根据typeFlag来判断人伤查勘定损财产或者车辆
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		String[] code = areaDictService.findAreaByAreaCode(checkAreaCode,"");
		String provinceCode = code[0];
		String cityCode = code[1];
		String regionCode = checkAreaCode;
		
		HandleScheduleSDReqVo reqVo = new HandleScheduleSDReqVo();
		
		HeadReq head = setHeadReq();//设置头部信息
		
		HandleScheduleReqSDBody body = new HandleScheduleReqSDBody();
		
		HandleScheduleReqScheduleSD scheduleSD = this.setHandleScheduleReqScheduleSD(registVo);
		
		List<HandleScheduleReqScheduleItemSD> scheduleItemList = new ArrayList<HandleScheduleReqScheduleItemSD>();
		
		//后面增加定损任务可以增加多个任务，只能车损或者物损其中一一种,但是只能指派给同一个人，所以只需构建一个车损或者物损项
		
		
		List<Long> ids = new ArrayList<Long>();
		ids.add(Long.valueOf(id));
		HandleScheduleReqScheduleItemSD item = new HandleScheduleReqScheduleItemSD();
		item.setTaskId("1");//虚拟ID
		if (FlowNode.PLoss.equals(typeFlag)) {
			item.setNodeType("Check");
		}else{
			item.setNodeType(typeFlag);
		}
		
		if (FlowNode.DLoss.equals(typeFlag)) {
			PrpLScheduleDefLossVo scheduleDefLossVo = scheduleTaskService.findPrpLScheduleDefLossVoById(new Long(id));
			List<PrpLScheduleDefLossVo> prpLScheduleDefLosses = new ArrayList<PrpLScheduleDefLossVo>();
			prpLScheduleDefLosses.add(scheduleDefLossVo);
			for(PrpLScheduleDefLossVo ItemsVo :prpLScheduleDefLosses){
				
				if(ItemsVo.getDeflossType().equals("1")){
					if(ItemsVo.getLossitemType().equals("0")){
						item.setItemNo("0");
						item.setItemName("地面");
					}else if(ItemsVo.getLossitemType().equals("1")){
						item.setItemNo("1-1");
						item.setItemName(ItemsVo.getItemsContent());
					}else{
						item.setItemNo("1-2");
						item.setItemName(ItemsVo.getItemsContent());
					}
				}else if(ItemsVo.getDeflossType().equals("2")){
					if(ItemsVo.getLossitemType().equals("0")){
						item.setItemNo("0");
						item.setItemName("地面");
					}else if(ItemsVo.getLossitemType().equals("1")){
						item.setItemNo("2-1");
						item.setItemName(ItemsVo.getItemsContent());
					}else{
						item.setItemNo("2-2");
						item.setItemName(ItemsVo.getItemsContent());
					}
				}
			}
		} else if (FlowNode.PLoss.equals(typeFlag)) {
			List<PrpLScheduleItemsVo> scheduleItemVos = scheduleTaskService.getPrpLScheduleItemsVoByIds(ids);
			if(scheduleItemVos != null && scheduleItemVos.size() > 0){
				for(PrpLScheduleItemsVo ItemsVo :scheduleItemVos){
					if(ItemsVo.getItemType().equals("1")){
						item.setItemNo("1-1");
						item.setItemName(ItemsVo.getItemsContent());
					}else if(ItemsVo.getItemType().equals("2")){
						item.setItemNo("1-2");
						item.setItemName(ItemsVo.getItemsContent());
					}else if(ItemsVo.getItemType().equals("4")){
						item.setItemName("人伤");
						item.setItemNo("-1");
					}else{//1-N	财产（同车的方式）
						if(ItemsVo.getSerialNo().equals("0")){//地面
							item.setItemNo("0");
							item.setItemName(ItemsVo.getLicenseNo());
						}else if(ItemsVo.getSerialNo().equals("1")){
							item.setItemNo("2-1");
							item.setItemName(ItemsVo.getLicenseNo());
						}else{//三者
							item.setItemNo("2-2");
							item.setItemName(ItemsVo.getLicenseNo());
						}
					}
				}
			}
		} else {
			List<PrpLScheduleTaskVo> scheduleTaskVos = scheduleTaskService.getPrpLScheduleTaskVoByIds(ids);
			if(scheduleTaskVos != null && scheduleTaskVos.size() > 0){
				//this.setReassignments(prpLScheduleTaskVo, checkAreaCode, lngXlatY, "Check", scheduleTaskVos.get(0).getPrpLScheduleItemses());
				for(PrpLScheduleItemsVo ItemsVo :scheduleTaskVos.get(0).getPrpLScheduleItemses()){
					if(ItemsVo.getItemType().equals("1")){
						item.setItemNo("1-1");
						item.setItemName(ItemsVo.getItemsContent());
					}else if(ItemsVo.getItemType().equals("2")){
						item.setItemNo("1-2");
						item.setItemName(ItemsVo.getItemsContent());
					}else if(ItemsVo.getItemType().equals("4")){
						item.setItemName("人伤");
						item.setItemNo("-1");
					}else{//1-N	财产（同车的方式）
						if(ItemsVo.getSerialNo().equals("0")){//地面
							item.setItemNo("0");
							item.setItemName(ItemsVo.getLicenseNo());
						}else if(ItemsVo.getSerialNo().equals("1")){
							item.setItemNo("2-1");
							item.setItemName(ItemsVo.getLicenseNo());
						}else{//三者
							item.setItemNo("2-2");
							item.setItemName(ItemsVo.getLicenseNo());
						}
					}
				}
			}
		}
		item.setProvinceCode(provinceCode);
		item.setCityCode(cityCode);
		item.setRegionCode(regionCode);
		item.setDamageAddress(checkAddress);
		item.setLngXlatY(lngXlatY != null ? lngXlatY : "");
		scheduleItemList.add(item);
		
		body.setScheduleSD(scheduleSD);
		body.setScheduleItemList(scheduleItemList);
		
		reqVo.setHead(head);
		reqVo.setBody(body);
		//获取url
		String url = SpringProperties.getProperty("MClaimPlatform_URL")+AUTOSCHEDULE_URL_METHOD;
		HandleScheduleSDBackReqVo vo = mobileCheckService.getHandelScheduleSDUrl(reqVo,url);
		AjaxResult result = new AjaxResult();
		result.setData(vo.getBody().getScheduleItemList());
		result.setStatus(HttpStatus.SC_OK);
		
		return result;
	}

	/**
	 * 自定义区域人员获取定损 查勘
	 * @param prpLScheduleTaskVo
	 * @param prpLScheduleItemses
	 * @param prpLScheduleDefLosses
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/selfDefineOpenMaps.ajax")
	@ResponseBody
	public AjaxResult selfDefineOpenMaps(@FormModel(value = "prpLScheduleTaskVo") PrpLScheduleTaskVo prpLScheduleTaskVo,
			@FormModel(value = "prpLScheduleItemses") List<PrpLScheduleItemsVo> prpLScheduleItemses,
			@FormModel(value = "prpLScheduleDefLosses") List<PrpLScheduleDefLossVo> prpLScheduleDefLosses) throws Exception {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
		String checkAreaCode = prpLScheduleTaskVo.getRegionCode();
		String checkAddress = prpLScheduleTaskVo.getCheckAddress();
		String lngXlatY = prpLScheduleTaskVo.getCheckAddressMapCode();
		String lossFlag = "";
		String[] code = areaDictService.findAreaByAreaCode(checkAreaCode,"");
		String provinceCode="";
		String cityCode="";
		if(code!=null && code.length>0){
			provinceCode = code[0];
			cityCode = code[1];
		}
		String regionCode = checkAreaCode;
		
		HandleScheduleSDReqVo reqVo = new HandleScheduleSDReqVo();
		
		HeadReq head = setHeadReq();//设置头部信息
		
		HandleScheduleReqSDBody body = new HandleScheduleReqSDBody();
		
		HandleScheduleReqScheduleSD scheduleSD = this.setHandleScheduleReqScheduleSD(registVo);
		
		List<HandleScheduleReqScheduleItemSD> scheduleItemList = new ArrayList<HandleScheduleReqScheduleItemSD>();
		
		//后面增加定损任务可以增加多个任务，只能车损或者物损其中一一种,但是只能指派给同一个人，所以只需构建一个车损或者物损项
		
		
		int id = 1;
		HandleScheduleSDBackReqVo vo = new HandleScheduleSDBackReqVo();
		AjaxResult result = new AjaxResult();
		//新
		//查勘
		List<HandleScheduleReqScheduleItemSD> scheduleItemListResult = new ArrayList<HandleScheduleReqScheduleItemSD>();
		if( prpLScheduleItemses!=null && prpLScheduleItemses.size() > 0){
			for (PrpLScheduleItemsVo ItemsVo : prpLScheduleItemses) {
				HandleScheduleReqScheduleItemSD item1 = new HandleScheduleReqScheduleItemSD();
				item1.setTaskId(String.valueOf(id++));//虚拟ID
				if (ItemsVo.getItemType().equals("4")) {//人伤
					item1.setNodeType("PLoss");
				} else {//查勘
					item1.setNodeType("Check");
				}

				item1.setProvinceCode(provinceCode);
				item1.setCityCode(cityCode);
				item1.setRegionCode(regionCode);
				item1.setDamageAddress(checkAddress);
				item1.setLngXlatY(lngXlatY != null ? lngXlatY : "");
				//设置自定义区域编码
				if (StringUtils.isNotEmpty(prpLScheduleTaskVo.getSelfDefinareaCode())) {
					item1.setSelfDefinAreaCode(prpLScheduleTaskVo.getSelfDefinareaCode());
				}
				scheduleItemList.add(item1);

			}
			HandleScheduleReqScheduleItemSD scheduleItemResult = new HandleScheduleReqScheduleItemSD();
			HandleScheduleReqScheduleItemSD personScheduleItemResult = new HandleScheduleReqScheduleItemSD();
			//只传查勘或者人伤
			for(HandleScheduleReqScheduleItemSD vos : scheduleItemList){
				if(vos.getNodeType().equals("PLoss")){
					personScheduleItemResult = vos;
				}
				if(vos.getNodeType().equals("Check")){
					scheduleItemResult = vos;
				}
			}
			if(personScheduleItemResult !=null && personScheduleItemResult.getNodeType() != null){
				scheduleItemListResult.add(personScheduleItemResult);
			}
			if(scheduleItemResult !=null && scheduleItemResult.getNodeType() != null){
				scheduleItemListResult.add(scheduleItemResult);
			}
			
			body.setScheduleSD(scheduleSD);
			body.setScheduleItemList(scheduleItemListResult);
			
			reqVo.setHead(head);
			reqVo.setBody(body);
			String url = SpringProperties.getProperty("MClaimPlatform_URL")+AUTOSCHEDULE_URL_METHOD;
			boolean checkCar = true;
			boolean checkPloss =true;
			List<HandleScheduleBackReqScheduleItemSD>  handleScheduleBackReqScheduleItemSDs = new ArrayList<HandleScheduleBackReqScheduleItemSD>();
			if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
				for(PrpLScheduleItemsVo itemsVo:prpLScheduleItemses){
					List<PrplQuickUserVo> prplQuickUserVos = new ArrayList<PrplQuickUserVo>();
					if("4".equals(itemsVo.getItemType())){
						if(checkPloss){
							prplQuickUserVos = quickUserService.findQuickUserVosByGBFlag("2",registVo.getComCode());
							checkPloss=false;
						}
					}else{
						if(checkCar){
							prplQuickUserVos = quickUserService.findQuickUserVosByGBFlag("1",registVo.getComCode());
							checkCar = false;
						}
					}
					if(prplQuickUserVos!=null&& !prplQuickUserVos.isEmpty()){
						for(int i = 0; i<prplQuickUserVos.size(); i++ ){
							HandleScheduleBackReqScheduleItemSD handleScheduleBackReqScheduleItemSD = new HandleScheduleBackReqScheduleItemSD();
							if("4".equals(itemsVo.getItemType()) ){
								handleScheduleBackReqScheduleItemSD.setNodeType("PLoss");
							}else {
								handleScheduleBackReqScheduleItemSD.setNodeType("Check");
							}
							handleScheduleBackReqScheduleItemSD.setNextHandlerCode(prplQuickUserVos.get(i).getUserCode());
							handleScheduleBackReqScheduleItemSD.setNextHandlerName(prplQuickUserVos.get(i).getUserName());
							handleScheduleBackReqScheduleItemSD.setScheduleObjectId(prplQuickUserVos.get(i).getComCode());
							handleScheduleBackReqScheduleItemSD.setScheduleObjectName(prplQuickUserVos.get(i).getComName());
							handleScheduleBackReqScheduleItemSD.setIsComuserCode("0");
							handleScheduleBackReqScheduleItemSD.setRelateHandlerName("");
							handleScheduleBackReqScheduleItemSD.setRelateHandlerMobile("");
							if(StringUtils.isNotBlank(prplQuickUserVos.get(i).getPhone())){
								handleScheduleBackReqScheduleItemSD.setRelateHandlerMobile(prplQuickUserVos.get(i).getPhone());
							}
							handleScheduleBackReqScheduleItemSDs.add(handleScheduleBackReqScheduleItemSD);
						}
					}
				}
			}else{
				vo = mobileCheckService.getHandelScheduleSDUrl(reqVo,url);
				if("1".equals(registVo.getIsQuickCase())){
					List<PrplQuickUserVo> prplQuickUserVos = quickUserService.findAll();
					for(int i = 0; i<prplQuickUserVos.size(); i++ ){
						HandleScheduleBackReqScheduleItemSD handleScheduleBackReqScheduleItemSD = new HandleScheduleBackReqScheduleItemSD();
						handleScheduleBackReqScheduleItemSD.setNodeType("Check");
						handleScheduleBackReqScheduleItemSD.setNextHandlerCode(prplQuickUserVos.get(i).getUserCode());
						handleScheduleBackReqScheduleItemSD.setNextHandlerName(prplQuickUserVos.get(i).getUserName());
						handleScheduleBackReqScheduleItemSD.setScheduleObjectId(prplQuickUserVos.get(i).getComCode());
						handleScheduleBackReqScheduleItemSD.setScheduleObjectName(prplQuickUserVos.get(i).getComName());
						handleScheduleBackReqScheduleItemSD.setIsComuserCode("0");
						handleScheduleBackReqScheduleItemSD.setRelateHandlerName("");
						handleScheduleBackReqScheduleItemSD.setRelateHandlerMobile("");
						handleScheduleBackReqScheduleItemSD.setCallNumber("");
						handleScheduleBackReqScheduleItemSDs.add(handleScheduleBackReqScheduleItemSD);
					}
				}else{
					handleScheduleBackReqScheduleItemSDs = vo.getBody().getScheduleItemList();
				}
			}
			result.setData(handleScheduleBackReqScheduleItemSDs);
		}
		
		
		//定损
		if( prpLScheduleDefLosses!=null && prpLScheduleDefLosses.size() > 0){
			for(PrpLScheduleDefLossVo ItemsVo :prpLScheduleDefLosses){
				HandleScheduleReqScheduleItemSD item = new HandleScheduleReqScheduleItemSD();
				item.setTaskId(String.valueOf(id++));//虚拟ID
				if(ItemsVo.getDeflossType().equals("1")){
					item.setNodeType("DLCar");
					if(ItemsVo.getLossitemType().equals("0")){
						item.setItemNo("0");
						item.setItemName("地面");
					}else if(ItemsVo.getLossitemType().equals("1")){
						item.setItemNo("1");
						item.setItemName(ItemsVo.getItemsContent());
					}else{
						item.setItemNo("2");
						item.setItemName(ItemsVo.getItemsContent());
					}
				}else if(ItemsVo.getDeflossType().equals("2")){
					item.setNodeType("DLProp");
					if(ItemsVo.getLossitemType().equals("0")){
						item.setItemNo("0");
						item.setItemName("地面");
					}else if(ItemsVo.getLossitemType().equals("1")){
						item.setItemNo("1");
						item.setItemName(ItemsVo.getItemsContent());
					}else{
						item.setItemNo("2");
						item.setItemName(ItemsVo.getItemsContent());
					}
				}
				item.setProvinceCode(provinceCode);
				item.setCityCode(cityCode);
				item.setRegionCode(regionCode);
				item.setDamageAddress(checkAddress);
				item.setLngXlatY(lngXlatY != null ? lngXlatY : "");
				//设置自定义区域编码
				if(StringUtils.isNotEmpty(prpLScheduleTaskVo.getSelfDefinareaCode())){
					item.setSelfDefinAreaCode(prpLScheduleTaskVo.getSelfDefinareaCode());
				}
				scheduleItemListResult.add(item);
			}
			//获取第一list集合
			List<HandleScheduleBackReqScheduleItemSD> listVo = new ArrayList<HandleScheduleBackReqScheduleItemSD>();
			body.setScheduleSD(scheduleSD);
			body.setScheduleItemList(scheduleItemListResult);
			reqVo.setHead(head);
			reqVo.setBody(body);
			String url = SpringProperties.getProperty("MClaimPlatform_URL")+AUTOSCHEDULE_URL_METHOD;
			vo = mobileCheckService.getHandelScheduleSDUrl(reqVo,url);
			//排除
			if(vo.getBody().getScheduleItemList() != null){
				List<HandleScheduleBackReqScheduleItemSD> list = vo.getBody().getScheduleItemList();
				for(HandleScheduleBackReqScheduleItemSD vos : list){
					if("1".equals(vos.getTaskId())){
						listVo.add(vos);
					}
				}
			}
			result.setData(listVo);
		}
		
		result.setStatus(HttpStatus.SC_OK);
		
		return result;
	}


	/**
	 * 自定义区域人员获取 改派
	 * @param id
	 * @param registNo
	 * @param typeFlag
	 * @param selfDefinareaCode
	 * @param checkAreaCode
	 * @param checkAddress
	 * @param lngXlatY
	 * @param prpLScheduleDefLoss
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/selfReassignMentOpenMap.ajax")
	@ResponseBody
	public AjaxResult selfDefineOpenMap1(@RequestParam("id")String id,String registNo, String typeFlag,String selfDefinareaCode,
			String checkAreaCode, String checkAddress, String lngXlatY,@FormModel(value = "prpLScheduleDefLosses") List<PrpLScheduleDefLossVo> prpLScheduleDefLoss) throws Exception {
		//根据typeFlag来判断人伤查勘定损财产或者车辆
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		String[] code = areaDictService.findAreaByAreaCode(checkAreaCode,"");
		String provinceCode = code[0];
		String cityCode = code[1];
		String regionCode = checkAreaCode;
		
		HandleScheduleSDReqVo reqVo = new HandleScheduleSDReqVo();
		
		HeadReq head = setHeadReq();//设置头部信息
		
		HandleScheduleReqSDBody body = new HandleScheduleReqSDBody();
		
		HandleScheduleReqScheduleSD scheduleSD = this.setHandleScheduleReqScheduleSD(registVo);
		
		List<HandleScheduleReqScheduleItemSD> scheduleItemList = new ArrayList<HandleScheduleReqScheduleItemSD>();
		List<HandleScheduleReqScheduleItemSD> scheduleItemListResult = new ArrayList<HandleScheduleReqScheduleItemSD>();
		//后面增加定损任务可以增加多个任务，只能车损或者物损其中一一种,但是只能指派给同一个人，所以只需构建一个车损或者物损项
		
		
		List<Long> ids = new ArrayList<Long>();
		ids.add(Long.valueOf(id));
		int taskid = 1;
		if (FlowNode.DLoss.equals(typeFlag)) {
			PrpLScheduleDefLossVo scheduleDefLossVo = scheduleTaskService.findPrpLScheduleDefLossVoById(new Long(id));
			List<PrpLScheduleDefLossVo> prpLScheduleDefLosses = new ArrayList<PrpLScheduleDefLossVo>();
			prpLScheduleDefLosses.add(scheduleDefLossVo);
			
			//定损
			if( prpLScheduleDefLosses!=null && prpLScheduleDefLosses.size() > 0){
				for(PrpLScheduleDefLossVo ItemsVo :prpLScheduleDefLosses){
					HandleScheduleReqScheduleItemSD item = new HandleScheduleReqScheduleItemSD();
					item.setTaskId(String.valueOf(taskid++));//虚拟ID
					if(ItemsVo.getDeflossType().equals("1")){
						item.setNodeType("DLCar");
						if(ItemsVo.getLossitemType().equals("0")){
							item.setItemNo("0");
							item.setItemName("地面");
						}else if(ItemsVo.getLossitemType().equals("1")){
							item.setItemNo("1");
							item.setItemName(ItemsVo.getItemsContent());
						}else{
							item.setItemNo("2");
							item.setItemName(ItemsVo.getItemsContent());
						}
					}else if(ItemsVo.getDeflossType().equals("2")){
						item.setNodeType("DLProp");
						if(ItemsVo.getLossitemType().equals("0")){
							item.setItemNo("0");
							item.setItemName("地面");
						}else if(ItemsVo.getLossitemType().equals("1")){
							item.setItemNo("1");
							item.setItemName(ItemsVo.getItemsContent());
						}else{
							item.setItemNo("2");
							item.setItemName(ItemsVo.getItemsContent());
						}
					}
					item.setProvinceCode(provinceCode);
					item.setCityCode(cityCode);
					item.setRegionCode(regionCode);
					item.setDamageAddress(checkAddress);
					item.setLngXlatY(lngXlatY != null ? lngXlatY : "");
					//设置自定义区域编码
					if(StringUtils.isNotEmpty(selfDefinareaCode)){
						item.setSelfDefinAreaCode(selfDefinareaCode);
					}
					scheduleItemListResult.add(item);
				}
			}
		} else if (FlowNode.PLoss.equals(typeFlag)) {
			List<PrpLScheduleItemsVo> prpLScheduleItemses = scheduleTaskService.getPrpLScheduleItemsVoByIds(ids);
			if( prpLScheduleItemses!=null && prpLScheduleItemses.size() > 0){
				for(PrpLScheduleItemsVo ItemsVo :prpLScheduleItemses){
					HandleScheduleReqScheduleItemSD item1 = new HandleScheduleReqScheduleItemSD();
					item1.setTaskId(String.valueOf(taskid++));//虚拟ID
					if(ItemsVo.getItemType().equals("4")){//人伤
						item1.setNodeType("PLoss");
					}else{//查勘
						item1.setNodeType("Check");
					}

					item1.setProvinceCode(provinceCode);
					item1.setCityCode(cityCode);
					item1.setRegionCode(regionCode);
					item1.setDamageAddress(checkAddress);
					item1.setLngXlatY(lngXlatY != null ? lngXlatY : "");
					//设置自定义区域编码
					if(StringUtils.isNotEmpty(selfDefinareaCode)){
						item1.setSelfDefinAreaCode(selfDefinareaCode);
					}
					scheduleItemList.add(item1);
				
				}
				HandleScheduleReqScheduleItemSD scheduleItemResult = new HandleScheduleReqScheduleItemSD();
				HandleScheduleReqScheduleItemSD personScheduleItemResult = new HandleScheduleReqScheduleItemSD();
				//只传查勘或者人伤
				for(HandleScheduleReqScheduleItemSD vos : scheduleItemList){
					if(vos.getNodeType().equals("PLoss")){
						personScheduleItemResult = vos;
					}
					if(vos.getNodeType().equals("Check")){
						scheduleItemResult = vos;
					}
				}
				if(personScheduleItemResult !=null && personScheduleItemResult.getNodeType() != null){
					scheduleItemListResult.add(personScheduleItemResult);
				}
				if(scheduleItemResult !=null && scheduleItemResult.getNodeType() != null){
					scheduleItemListResult.add(scheduleItemResult);
				}
			}
		} else {
			List<PrpLScheduleTaskVo> scheduleTaskVos = scheduleTaskService.getPrpLScheduleTaskVoByIds(ids);
			if(scheduleTaskVos != null && scheduleTaskVos.size() > 0){
				for(PrpLScheduleItemsVo ItemsVo :scheduleTaskVos.get(0).getPrpLScheduleItemses()){
					HandleScheduleReqScheduleItemSD item1 = new HandleScheduleReqScheduleItemSD();
					item1.setTaskId(String.valueOf(taskid++));//虚拟ID
							if(ItemsVo.getItemType().equals("4")){//人伤
								item1.setNodeType("PLoss");
							}else{//查勘
								item1.setNodeType("Check");
							}
						
							item1.setProvinceCode(provinceCode);
							item1.setCityCode(cityCode);
							item1.setRegionCode(regionCode);
							item1.setDamageAddress(checkAddress);
							item1.setLngXlatY(lngXlatY != null ? lngXlatY : "");
							//设置自定义区域编码
							if(StringUtils.isNotEmpty(selfDefinareaCode)){
								item1.setSelfDefinAreaCode(selfDefinareaCode);
							}
							scheduleItemList.add(item1);
				
				}
				HandleScheduleReqScheduleItemSD scheduleItemResult = new HandleScheduleReqScheduleItemSD();
				HandleScheduleReqScheduleItemSD personScheduleItemResult = new HandleScheduleReqScheduleItemSD();
				//只传查勘或者人伤
				for(HandleScheduleReqScheduleItemSD vos : scheduleItemList){
					if(vos.getNodeType().equals("PLoss")){
						personScheduleItemResult = vos;
					}
					if(vos.getNodeType().equals("Check")){
						scheduleItemResult = vos;
					}
				}
				if(personScheduleItemResult !=null && personScheduleItemResult.getNodeType() != null){
					scheduleItemListResult.add(personScheduleItemResult);
				}
				if(scheduleItemResult !=null && scheduleItemResult.getNodeType() != null){
					scheduleItemListResult.add(scheduleItemResult);
				}
			}
		}

		
		body.setScheduleSD(scheduleSD);
		body.setScheduleItemList(scheduleItemListResult);
		
		reqVo.setHead(head);
		reqVo.setBody(body);
		//获取url
		String url = SpringProperties.getProperty("MClaimPlatform_URL")+AUTOSCHEDULE_URL_METHOD;
		AjaxResult result = new AjaxResult();
		HandleScheduleSDBackReqVo vo = mobileCheckService.getHandelScheduleSDUrl(reqVo,url);
		result.setData(vo.getBody().getScheduleItemList());
		result.setStatus(HttpStatus.SC_OK);
		return result;
	}
	
	
	
	/**
	 * 自定义区域人员获取返回的数据接收
	 */
	@RequestMapping(value = "/backMapValueSelf.do")
	@ResponseBody
	public ModelAndView backMapValueSelf(String xml) throws UnsupportedEncodingException {
		URLDecoder urlDecoder = new URLDecoder();
        xml = urlDecoder.decode(xml, "UTF-8");
        logger.info(xml);

        HandleScheduleSDBackReqVo scheduleBackReqVo = ClaimBaseCoder.xmlToObj(xml, HandleScheduleSDBackReqVo.class);
		ModelAndView mav = new ModelAndView();
		mav.addObject("xml", xml);
		mav.addObject("res", scheduleBackReqVo.getBody().getScheduleItemList());
		mav.setViewName("schedule/scheduleEdit/SchOpenSelfList1");
		return mav;
	}
	
	
	
	private void setScheduleWf(HandleScheduleReqScheduleWF scheduleWf,PrpLRegistVo registVo){
		//设置
		List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
		prpLCMainVoList = policyViewService.getPolicyAllInfo(registVo.getRegistNo());
		String businessPlate ="";
		
		if(prpLCMainVoList != null && prpLCMainVoList.size() > 0){
			businessPlate = CodeTranUtil.transCode("businessPlate", prpLCMainVoList.get(0).getBusinessPlate());
			//代理人编码
			if(prpLCMainVoList.get(0).getAgentCode()!=null){
				scheduleWf.setAgentCode(prpLCMainVoList.get(0).getAgentCode());
			}
			//保单归属地编码
			if(prpLCMainVoList.get(0).getComCode() != null){
				String code = areaDictService.findAreaList("areaCode",registVo.getPrpLRegistExt().getCheckAddressCode());
				//承保地区
				String comCode = "";
				if(prpLCMainVoList.size()==2){
					for(PrpLCMainVo vo:prpLCMainVoList){
						if(("12").equals(vo.getRiskCode().substring(0, 2))){
							comCode = vo.getComCode();
						}
					}
				}else{
					comCode = prpLCMainVoList.get(0).getComCode();
				}
				//保单归属地编码
				scheduleWf.setComCode(comCode);
				//是否异地案件
				if(code != null && comCode!=""){
					if("0002".equals(code.substring(0, 4))){//深圳
						if(!code.substring(0, 4).equals(comCode.substring(0, 4))){
							scheduleWf.setIsElseWhere("1");//是
						}else{
							scheduleWf.setIsElseWhere("0");//否
						}
					}else{
						if("0002".equals(comCode.substring(0, 4))){//添加深圳的单这种情况
							if(!code.substring(0, 4).equals(comCode.substring(0, 4))){
								scheduleWf.setIsElseWhere("1");//是
							}else{
								scheduleWf.setIsElseWhere("0");//否
							}
						}else{
							if(!code.substring(0, 2).equals(comCode.substring(0, 2))){
								scheduleWf.setIsElseWhere("1");//是
							}else{
								scheduleWf.setIsElseWhere("0");//否
							}
						}
					}
				}else{
					scheduleWf.setIsElseWhere("1");//是
				}
			}
		}else{
			scheduleWf.setAgentCode("all");
			scheduleWf.setIsElseWhere("all");
		}
		
		if(businessPlate !="" && businessPlate != null){
			scheduleWf.setBusinessType(businessPlate);//业务类型
		}else{
			scheduleWf.setBusinessType("all");//业务类型
		}
		//TODO 到时取大客户的值
		if(prpLCMainVoList != null && prpLCMainVoList.size() > 0){
			if(StringUtils.isNotBlank(prpLCMainVoList.get(0).getAgentName()) ||
					StringUtils.isNotBlank(prpLCMainVoList.get(0).getAgentCode())){
				scheduleWf.setCustomType("2");//客户分类
			}else{
				scheduleWf.setCustomType("3");//客户分类
			}
		
		}else{
			scheduleWf.setCustomType("all");//客户分类
		}
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registVo.getRegistNo());
		if("DM04".equals(prpLRegistVo.getDamageCode())){//全车盗抢
			scheduleWf.setCaseType("3");
		}else if("DM02".equals(prpLRegistVo.getDamageCode())){//玻璃案件
			scheduleWf.setCaseType("2");
		}else if(prpLRegistVo.getPrpLRegistPersonLosses() != null && prpLRegistVo.getPrpLRegistPersonLosses().size() > 0){
			//人伤
			scheduleWf.setCaseType("5");
		}else if("2".equals(prpLRegistVo.getPrpLRegistExt().getCheckType())){//快处快赔
			scheduleWf.setCaseType("4");
		}else{
			scheduleWf.setCaseType("1");//案件类型
		}
	}
	private void setScheduleWf(HandleScheduleReqScheduleSD scheduleWf,PrpLRegistVo registVo){
		//无保单报案获取不到值所以设置all
		//设置
		List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
		prpLCMainVoList = policyViewService.getPolicyAllInfo(registVo.getRegistNo());
		String businessPlate ="";
		
		if(prpLCMainVoList != null && prpLCMainVoList.size() > 0){
			businessPlate = CodeTranUtil.transCode("businessPlate", prpLCMainVoList.get(0).getBusinessPlate());
			//代理人编码
			if(prpLCMainVoList.get(0).getAgentCode()!=null){
				scheduleWf.setAgentCode(prpLCMainVoList.get(0).getAgentCode());
			}
			//保单归属地编码
			if(prpLCMainVoList.get(0).getComCode() != null){
				String code = areaDictService.findAreaList("areaCode",registVo.getPrpLRegistExt().getCheckAddressCode());
				//承保地区
				String comCode = "";
				if(prpLCMainVoList.size()==2){
					for(PrpLCMainVo vo:prpLCMainVoList){
						if(("12").equals(vo.getRiskCode().substring(0, 2))){
							comCode = vo.getComCode();
						}
					}
				}else{
					comCode = prpLCMainVoList.get(0).getComCode();
				}
				//保单归属地编码
				scheduleWf.setComCode(comCode);
				//是否异地案件
				if(code != null && comCode!=""){
					if("0002".equals(code.substring(0, 4))){//深圳出险
						if(!code.substring(0, 4).equals(comCode.substring(0, 4))){
							scheduleWf.setIsElseWhere("1");//是
						}else{
							scheduleWf.setIsElseWhere("0");//否
						}
					}else{//非深圳出险
						if("0002".equals(comCode.substring(0, 4))){//添加深圳的单这种情况
							if(!code.substring(0, 4).equals(comCode.substring(0, 4))){
								scheduleWf.setIsElseWhere("1");//是
							}else{
								scheduleWf.setIsElseWhere("0");//否
							}
						}else{
							if(!code.substring(0, 2).equals(comCode.substring(0, 2))){
								scheduleWf.setIsElseWhere("1");//是
							}else{
								scheduleWf.setIsElseWhere("0");//否
							}
						}
						
					}
				}else{
					scheduleWf.setIsElseWhere("1");//是
				}
			}
		}else{
			scheduleWf.setAgentCode("all");
			scheduleWf.setIsElseWhere("all");
		}
		if(businessPlate !="" && businessPlate != null){
			scheduleWf.setBusinessType(businessPlate);//业务类型
		}else{
			scheduleWf.setBusinessType("all");//业务类型
		}
		//TODO 到时取大客户的值
		if(prpLCMainVoList != null && prpLCMainVoList.size() > 0){
			if(StringUtils.isNotBlank(prpLCMainVoList.get(0).getAgentName()) ||
					StringUtils.isNotBlank(prpLCMainVoList.get(0).getAgentCode())){
				scheduleWf.setCustomType("2");//客户分类
			}else{
				scheduleWf.setCustomType("3");//客户分类
			}
		
		}else{
			scheduleWf.setCustomType("all");//客户分类
		}
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registVo.getRegistNo());
		if("DM04".equals(prpLRegistVo.getDamageCode())){//全车盗抢
			scheduleWf.setCaseType("3");
		}else if("DM02".equals(prpLRegistVo.getDamageCode())){//玻璃案件
			scheduleWf.setCaseType("2");
		}else if(prpLRegistVo.getPrpLRegistPersonLosses() != null && prpLRegistVo.getPrpLRegistPersonLosses().size() > 0){
			//人伤
			scheduleWf.setCaseType("5");
		}else if("2".equals(prpLRegistVo.getPrpLRegistExt().getCheckType())){//快处快赔
			scheduleWf.setCaseType("4");
		}else{
			scheduleWf.setCaseType("1");//案件类型
		}
	}
	
}
