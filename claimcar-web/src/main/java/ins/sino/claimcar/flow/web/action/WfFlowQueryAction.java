/******************************************************************************
* CREATETIME : 2016年1月10日 下午10:32:56
******************************************************************************/
package ins.sino.claimcar.flow.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.support.ObjectToMapCallback;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaFactorPowerVo;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.DateUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.YN01;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.RepairFactoryService;
import ins.sino.claimcar.flow.service.WfFlowService;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfNodeService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.util.TaskQueryUtils;
import ins.sino.claimcar.flow.util.WorkFlowUtils;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.WfFlowNodeShowVo;
import ins.sino.claimcar.flow.vo.WfFlowNodeVo;
import ins.sino.claimcar.flow.vo.WfFlowShowVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.regist.service.PolicyQueryService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.utils.SaaPowerUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;


/**
 * 流程查询
 * @author ★LiuPing
 * @CreateTime 2016年1月10日
 */
@Controller
@RequestMapping("/flowQuery")
public class WfFlowQueryAction {
	private static Logger logger = LoggerFactory.getLogger(WfFlowQueryAction.class);
	
	@Autowired
	private WfFlowService wfFlowService;	
	@Autowired
	private WfNodeService wfNodeService;
	@Autowired
	private WfMainService wfMainService;
	@Autowired
	private SaaUserPowerService saaUserPowerService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private WfTaskQueryService wfTaskQueryService;
	@Autowired
	private RegistService registService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	PolicyQueryService policyQueryService;
	@Autowired
	RepairFactoryService repairFactoryService;
	
	@RequestMapping(value = "/initWorkbench.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initWorkbench(PrpLWfTaskQueryVo taskQueryVo) throws Exception {
		//PrpLWfTaskQueryVo taskQueryVo = new PrpLWfTaskQueryVo();
		taskQueryVo.setLength(10);
		taskQueryVo.setHandleStatus("0");
		//ResultPage<WfTaskQueryResultVo> resultPage = wfFlowService.findFlowForPage(taskQueryVo);
		ResultPage<WfTaskQueryResultVo> resultPage = wfFlowService.findFlowForPage(taskQueryVo,null);
		List<WfTaskQueryResultVo> reslutList = resultPage.getData();
		taskQueryVo.setHandleStatus("1");
		ResultPage<WfTaskQueryResultVo> resultPage2 = wfFlowService.findFlowForPage(taskQueryVo,null);
		List<WfTaskQueryResultVo> reslutList2 = resultPage2.getData();
		taskQueryVo.setHandleStatus("2");
		ResultPage<WfTaskQueryResultVo> resultPage3 = wfFlowService.findFlowForPage(taskQueryVo,null);
		List<WfTaskQueryResultVo> reslutList3 = resultPage3.getData();
		taskQueryVo.setHandleStatus("3");
		ResultPage<WfTaskQueryResultVo> resultPage4 = wfFlowService.findFlowForPage(taskQueryVo,null);
		List<WfTaskQueryResultVo> reslutList4 = resultPage4.getData();
		ModelAndView mav = new ModelAndView();
		mav.addObject("reslutList",reslutList);
		mav.addObject("reslutList2",reslutList2);
		mav.addObject("reslutList3",reslutList3);
		mav.addObject("reslutList4",reslutList4);

		mav.setViewName("flowQuery/FlowWorkbench");
		logger.debug(mav.getViewName());
		return mav;
	}
	
	@RequestMapping(value = "/initFlowQuery.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initPage(Model model) {
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		ModelAndView mav = new ModelAndView();

		mav.addObject("damageTimeStart",startDate);
		mav.addObject("damageTimeEnd",endDate);

		//获取机构
		SysUserVo userVo = WebUserUtils.getUser();
		if("0001".equals(userVo.getComCode().substring(0, 4)) || "00000000".equals(userVo.getComCode())){
			//mav.addObject("comCodeFlags", "1");
			List<SysCodeDictVo> codeDictVoList= wfTaskQueryService.findPrpDcompanyByUserCode(null,"2");
			Map<String,String> carNoMap = new HashMap<String,String>();
			for(SysCodeDictVo vo : codeDictVoList){
				carNoMap.put(vo.getCodeCode(),vo.getCodeName());
			}
			mav.addObject("carNoMap", carNoMap);
		}else{
			List<SysCodeDictVo> codeDictVoList = new ArrayList<SysCodeDictVo>();
			List<SysCodeDictVo> codeDictTempVoList = new ArrayList<SysCodeDictVo>();
			List<SysCodeDictVo> permitcompanycodeDictVoList = new ArrayList<SysCodeDictVo>();
			permitcompanycodeDictVoList = wfTaskQueryService.findPermitcompanyByUserCode(userVo.getUserCode());
			for(SysCodeDictVo vo : permitcompanycodeDictVoList){
				if(vo.getCodeCode().endsWith("000000")){
					codeDictTempVoList = wfTaskQueryService.findPrpDcompanyByUserCode(vo.getCodeCode().substring(0, 2),"3");
					if(codeDictTempVoList != null && codeDictTempVoList.size()>0){
						codeDictVoList.addAll(codeDictTempVoList);
					}
				}else{
					codeDictTempVoList = wfTaskQueryService.findPrpDcompanyByUserCode(vo.getCodeCode().substring(0, 4),"3");
					if(codeDictTempVoList != null && codeDictTempVoList.size()>0){
						codeDictVoList.addAll(codeDictTempVoList);
					}
				}
			}
			Map<String,String> carNoMap = new HashMap<String,String>();
			for(SysCodeDictVo vo : codeDictVoList){
				carNoMap.put(vo.getCodeCode(),vo.getCodeName());
			}
			mav.addObject("carNoMap", carNoMap);
		}
		mav.setViewName("flowQuery/workFlowQueryList");
		logger.debug(mav.getViewName());
		return mav;
	}
	
	@RequestMapping(value = "/showCaseList.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView searchCaseList(@RequestParam(value = "flowId")String flowId) throws Exception {
		ModelAndView mav = new ModelAndView();
		//查找流程
		List<WfFlowNodeVo> wfFlowNodeVoList = wfFlowService.findAllWfTaskByFlowId(flowId);
		//按taskId排序
		Collections.sort(wfFlowNodeVoList,new Comparator<WfFlowNodeVo>(){
            public int compare(WfFlowNodeVo arg0, WfFlowNodeVo arg1) {
                return arg0.getTaskId().compareTo(arg1.getTaskId());
            }
        });
		mav.addObject("resultList",wfFlowNodeVoList);
		mav.setViewName("flowQuery/caseList");
		logger.debug(mav.getViewName());
		return mav;
	}
	
	@RequestMapping(value = "/search.do", method = RequestMethod.POST)
	@ResponseBody
	public String search(PrpLWfTaskQueryVo taskQueryVo,HttpServletRequest request) throws Exception {
		ResultPage<WfTaskQueryResultVo> resultPage = null;
		String querySystem = taskQueryVo.getHandleStatus();//0代表新系统，1代表旧系统
		String userCode = WebUserUtils.getUserCode();
		if("1".equals(querySystem)){
			SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(WebUserUtils.getUserCode());
			Map<String,List<SaaFactorPowerVo>> factorPowerMap = new HashMap<String,List<SaaFactorPowerVo>>();
			if(userPowerVo != null){
				factorPowerMap = userPowerVo.getPermitFactorMap();
			}
			taskQueryVo.setUserCode(userCode);
			resultPage = wfFlowService.findOldFlowForPage(taskQueryVo,factorPowerMap);
		}else{
			SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(WebUserUtils.getUserCode());
			Map<String,List<SaaFactorPowerVo>> factorPowerMap = new HashMap<String,List<SaaFactorPowerVo>>();
			if(userPowerVo != null){
				factorPowerMap = userPowerVo.getPermitFactorMap();
			}
			taskQueryVo.setUserCode(userCode);
			if(managerService.findIntermByUserCode(userCode) != null){
				taskQueryVo.setIntermFlag("2");//当前登录工号为公估工号
			}
			resultPage = wfFlowService.findFlowForPage(taskQueryVo,factorPowerMap);
		}
		String jsonData = null;
		jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(),"registNo","policyNo","policyNoLink","licenseNo","insuredName","damageTime",
				"reportTime","comCodePly:ComCode","riskCode:RiskCode","damageAddress","flowId","handlerUser","serialNo");
		logger.debug("FlowQuery.jsonData="+jsonData);

		return jsonData;
		
	}
	
	@RequestMapping(value = "/showFlow.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView search(@RequestParam(value = "flowId") String flowId,String showType,SysUserVo loginUser) throws Exception {
		SaaUserPowerVo saaUserPowerVo = saaUserPowerService.findUserPower(loginUser.getUserCode());
		ModelAndView mv = new ModelAndView();
		WfFlowShowVo wfFlowShowVo = new WfFlowShowVo();
		PrpLWfMainVo prpLWfMainVo = wfMainService.findPrpLWfMainVoByFlowId(flowId);
		SysUserVo userVo = WebUserUtils.getUser();
		//查找流程
		List<WfFlowNodeVo> wfFlowNodeVoList = wfFlowService.findAllWfTaskByFlowId(flowId);
		Collections.sort(wfFlowNodeVoList,new Comparator<WfFlowNodeVo>(){
            public int compare(WfFlowNodeVo arg0, WfFlowNodeVo arg1) {
                return arg0.getTaskId().compareTo(arg1.getTaskId());
            }
        });
		//构造根节点
		WfFlowNodeVo rootNode = new WfFlowNodeVo();
		for(WfFlowNodeVo wfFlowNodeVo:wfFlowNodeVoList){
			if(wfFlowNodeVo.getUpperTaskId().equals(new BigDecimal(0))){
				//设置子节点，初始为空
				List<WfFlowNodeVo> childList = new ArrayList<WfFlowNodeVo>();
				wfFlowNodeVo.setChildNode(childList);
				rootNode = wfFlowNodeVo;
				break;
			}
		}
		WorkFlowUtils workFlowUtils = new WorkFlowUtils();
		//构建树结构
		workFlowUtils.buildTree(rootNode.getTaskId(), rootNode, wfFlowNodeVoList, prpLWfMainVo,saaUserPowerVo);
		
		if("node".equals(showType)){//节点图
			List<WfFlowNodeShowVo> wfFlowNodeShowList = wfNodeService.findWfFlowNodeShowList(flowId);
			workFlowUtils.recursionNodeShow(rootNode,wfFlowNodeShowList);
			int num = 0;
			int rowNum = 0;
			int maxColum = 1;
			//设置节点位置
			for(int i = 0;i < wfFlowNodeShowList.size(); i++){
				WfFlowNodeShowVo wfFlowNodeShowVo = wfFlowNodeShowList.get(i);
				List<WfFlowNodeVo> childList = wfFlowNodeShowVo.getChildList();
				int row = i+1;
				int rootNum = 0;
				for(int j = 0; j < childList.size();j ++){
					WfFlowNodeVo wfFlowNodeVo = childList.get(j);
					WfFlowNodeVo parentNode = wfFlowNodeVo.getParentNode();
					if(parentNode == null){//根节点
						wfFlowNodeVo.setRow(1);
						wfFlowNodeVo.setColum(1);
						rootNum++;
						rowNum++;
					}else{
						//父节点的nodeCode和wfFlowNodeShowVo的一致
						if(wfFlowNodeShowVo.getNodeCode().equals(parentNode.getNodeCode())){
							wfFlowNodeVo.setRow(parentNode.getRow());
							wfFlowNodeVo.setColum(parentNode.getColum() + 1);
							if(parentNode.getColum() + 1 > maxColum){
								maxColum = parentNode.getColum() + 1;
							}
						}else{
							rootNum++;
							if(rootNum > 1){
								num++;
							}
							wfFlowNodeVo.setRow(row+num);
							rowNum = row+num;
						}
					}
				}
				wfFlowNodeShowVo.setRootNodeNum(rootNum);
				wfFlowNodeShowVo.setRowNum(rowNum);
			}
			wfFlowShowVo.setMaxRow(rowNum);
			wfFlowShowVo.setMaxColum(maxColum);
			wfFlowShowVo.setWfFlowNodeShowList(wfFlowNodeShowList);
			mv.setViewName("flowQuery/workFlowNodeShow");
		}else{//流程图
			//迭代 设置wfFlowNodeVoList中节点位置
			workFlowUtils.recursion(rootNode, 1, wfFlowNodeVoList);
			wfFlowShowVo.setMaxRow(workFlowUtils.getDeepNum());
			wfFlowShowVo.setMaxColum(workFlowUtils.getLeafNodeNum());
			wfFlowShowVo.setWfFlowNodeList(wfFlowNodeVoList);
			mv.setViewName("flowQuery/workFlowShow");
		}
		String bussTag = getBussTag(wfFlowNodeVoList);
		
		wfFlowShowVo.setBussTag(TaskExtMapUtils.jsonTagToHtmlInFlow(bussTag).toString());
		mv.addObject("wfFlowShowVo", wfFlowShowVo);
		
		//关联工单和新建工单
	    Boolean hasRegistTask = SaaPowerUtils.hasTask(userVo.getUserCode(),"claim.regist.query");
  		String URL = SpringProperties.getProperty("FOCUS_URL");
  		String recording_URL = SpringProperties.getProperty("FOCUS_RECORDING");
  		mv.addObject("hasRegistTask",hasRegistTask);
  		mv.addObject("recording_URL",recording_URL);
  		mv.addObject("registNo", prpLWfMainVo.getRegistNo());
		mv.addObject("addCode_URLS",URL);
		mv.addObject("queryCode_URLS",URL);
		
		return mv;
	}
	
	
	private ObjectToMapCallback searchCallBack() {
		ObjectToMapCallback callBack = new ObjectToMapCallback() {

			public Map<String, Object> toMap(Object object) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				WfTaskQueryResultVo resultVo = (WfTaskQueryResultVo) object;
				String registNo = resultVo.getRegistNo();
				String policyNo = resultVo.getPolicyNo();
				String policyNoLink = resultVo.getPolicyNoLink();
				String plyNoUrl = TaskQueryUtils.getTaskHandUrl("PolicyView",
						null);
				plyNoUrl += "?registNo=" + registNo;

				StringBuffer plyNoHtml = new StringBuffer();
				plyNoHtml.append("<a href='").append(plyNoUrl)
						.append("'   target='_blank' >");
				plyNoHtml.append(policyNo);
				if (StringUtils.isNotBlank(policyNoLink)) {
					plyNoHtml.append("<br>");
					plyNoHtml.append(policyNoLink);
				}
				plyNoHtml.append("</a>");
				dataMap.put("policyNoHtml", plyNoHtml.toString());
				// 将旧理赔
				dataMap.put("oldClaimUrl",SpringProperties.getProperty("dhic.oldclaim.url"));
				return dataMap;
			}
		};
		return callBack;
	}
	
	public String getBussTag(List<WfFlowNodeVo> wfFlowNodeVoList){
		String registNo = wfFlowNodeVoList.get(wfFlowNodeVoList.size()-1).getRegistNo();
		String bussTag = wfFlowNodeVoList.get(wfFlowNodeVoList.size()-1).getBussTag();
		
		Map<String, Object> map = JSONObject.parseObject(bussTag);
		if(map==null){
			map = new HashMap<String, Object>();
		}
		
		for(WfFlowNodeVo nodeVo:wfFlowNodeVoList){
			if((FlowNode.Chk.name().equals(nodeVo.getSubNodeCode())||FlowNode.DLCar.name().equals(nodeVo.getSubNodeCode()))&&
					("1".equals(nodeVo.getIsMobileAccept())||"2".equals(nodeVo.getIsMobileAccept())||"3".equals(nodeVo.getIsMobileAccept()))&&
					("0".equals(nodeVo.getWorkStatus())||"2".equals(nodeVo.getWorkStatus())||"3".equals(nodeVo.getWorkStatus()))){

				map.put("isMobileCase", nodeVo.getIsMobileAccept());
				
				break;
			}
		}
		
		PrpLRegistVo prplRegistVo = registService.findRegistByRegistNo(registNo);
		if("oldClaim".equals(prplRegistVo.getFlag())){
			map.put("isOldClaim", "1");
		}
		if(CodeConstants.GBFlag.MAJORCOMMON.equals(prplRegistVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORCOMMON.equals(prplRegistVo.getIsGBFlag()) || CodeConstants.GBFlag.MAJORRELATION.equals(prplRegistVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(prplRegistVo.getIsGBFlag())){
			map.put("isGBFlag", "1");
		}
		if(YN01.Y.equals(prplRegistVo.getSelfClaimFlag())){
			map.put("selfClaimFlag", "1");
		}
		
		/**
		 * 承保推修手机号与修理厂推修手机号匹配
		 */
		List<PrpLCMainVo> prpLCMains=prpLCMainService.findPrpLCMainsByRegistNo(registNo);
	    String comcode="";
	    if(prpLCMains!=null && prpLCMains.size()>0){
	    	for(PrpLCMainVo mainVo:prpLCMains){
	    	   if(!"1101".equals(mainVo.getRiskCode())){
	    		   comcode=mainVo.getComCode();
	    		   break;
	    	   }else{
	    		   comcode=mainVo.getComCode();
	    	   }
	    	}
	    }
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.REPAIRPHONE,comcode);
		if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){
		    String serviceMobile="";//推修手机号，有商业取商业，没商业取交强
			Long repairId=null;//修理厂主表Id
			String BIPolicyNo="";
			String CIPolicyNo="";
				if(prpLCMains!=null && prpLCMains.size()>0){
					for(PrpLCMainVo mainVo:prpLCMains){
						if("1101".equals(mainVo.getRiskCode())){
							CIPolicyNo=mainVo.getPolicyNo();
						}else{
							BIPolicyNo=mainVo.getPolicyNo();
						}
					}
				}
				
			    if(StringUtils.isNotBlank(BIPolicyNo)){
			      serviceMobile=policyQueryService.findPrpCMian(BIPolicyNo);
			    }
			    if(StringUtils.isNotBlank(CIPolicyNo) && StringUtils.isBlank(serviceMobile)){
			      serviceMobile=policyQueryService.findPrpCMian(CIPolicyNo);
			    }
			    if(StringUtils.isNotBlank(serviceMobile)){
			      repairId=repairFactoryService.findRepairFactoryBy(serviceMobile);
			    }
			    if(repairId!=null){
			    	map.put("isRePair", "1");
			    }
		  }
		bussTag = JSONObject.toJSONString(map);
		
		return bussTag; 
	}
	
	@RequestMapping(value = "/showOldTaskFlow.ajax", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult searchOldCaseList(@RequestParam(value = "registNo")String registNo) throws Exception {
		AjaxResult ar = new AjaxResult();
		PrpLWfMainVo wfMainVo = wfMainService.findPrpLWfMainVoByRegistNo(registNo);
		if(wfMainVo==null){
			///生成旧理赔流程节点
			wfTaskHandleService.generateFlow(registNo);
			wfTaskHandleService.generateFlowLevel(registNo);
			wfMainVo = wfMainService.findPrpLWfMainVoByRegistNo(registNo);
		}
		if(wfMainVo==null){
			ar.setData("");
		}else{
			ar.setData(wfMainVo.getFlowId());
		}
		
		ar.setStatus(HttpStatus.SC_OK);

		return ar;
	}
}
