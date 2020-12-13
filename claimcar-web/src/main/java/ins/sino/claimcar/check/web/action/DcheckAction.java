package ins.sino.claimcar.check.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.support.ObjectToMapCallback;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaFactorPowerVo;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.DateUtils;
import ins.platform.utils.ExportExcelUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.DcheckService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.util.TaskQueryUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;











import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/dcheck")
public class DcheckAction {
	private Logger logger = LoggerFactory.getLogger(DcheckAction.class);
	@Autowired
	private WfTaskQueryService wfTaskQueryService;
	@Autowired
	private DcheckService dcheckService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	private SaaUserPowerService saaUserPowerService;
	
	
	@RequestMapping(value="/dcheckList.do")
	public ModelAndView dcheckQuery(){
		ModelAndView mv =new ModelAndView();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		Date reportTimeEnd=new Date();
		Date reportTimeStart=DateUtils.addDays(reportTimeEnd, -15);

		mv.addObject("damageTimeStart",startDate);
		mv.addObject("damageTimeEnd",endDate);
		mv.addObject("reportTimeStart",reportTimeStart);
		mv.addObject("reportTimeEnd",reportTimeEnd);
		mv.setViewName("flowQuery/dcheckQueryList");
		return mv;
	}
	
	/**
	 * 双代岗查询方法
	 * @param taskQueryVo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/dcheckSearch.do", method = RequestMethod.POST)
	@ResponseBody
	public String dcheckSearch(PrpLWfTaskQueryVo taskQueryVo,@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
			@RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception{
		SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(WebUserUtils.getUserCode());
		Map<String,List<SaaFactorPowerVo>> factorPowerMap = new HashMap<String,List<SaaFactorPowerVo>>();
		String comCode=WebUserUtils.getComCode();
		if(userPowerVo != null){
			factorPowerMap = userPowerVo.getPermitFactorMap();
		}
		if(userPowerVo==null){
			throw new RuntimeException("未找到用户【"+WebUserUtils.getUserCode()+"】在系统的登录机构，查询无结果！");
		}
		List<WfTaskQueryResultVo> resultLists = new ArrayList<WfTaskQueryResultVo>();
		long totalCount = 0;
		if(factorPowerMap!=null && factorPowerMap.size()>0){
			List<SaaFactorPowerVo> factorPowerVoList = factorPowerMap.get("FF_COMCODE");
			if(factorPowerVoList != null && factorPowerVoList.size()>0){
				for(int i = 0; i < factorPowerVoList.size(); i++){
					comCode=factorPowerVoList.get(i).getDataValue();
					if(StringUtils.isNotBlank(comCode) && comCode.contains("%")){
						int comIndex=comCode.indexOf("%");
						String comStart=comCode.substring(0,comIndex);
						if(comIndex==1){
							comCode=comStart+"0000000";
						}else if(comIndex==2){
							comCode=comStart+"000000";
						}else if(comIndex==3){
							comCode=comStart+"00000";
						}else if(comIndex==4){
							comCode=comStart+"0000";
						}else if(comIndex==5){
							comCode=comStart+"000";
						}else if(comIndex==6){
							comCode=comStart+"00";
						}else if(comIndex==7){
							comCode=comStart+"0";
						}
					}
					
//					ResultPage<WfTaskQueryResultVo> resultVoLists=dcheckService.search(taskQueryVo,comCode, start, length);
//				
//					for(WfTaskQueryResultVo queryResultVo : resultVoLists.getData()){
//						resultList.add(queryResultVo);
//					}
//					totalCount = totalCount + resultVoLists.getTotalCount();
//					long as = resultVoLists.getLength();
					
					List<WfTaskQueryResultVo> resultVoList=dcheckService.search(taskQueryVo,comCode, start, length);
					for(WfTaskQueryResultVo queryResultVo : resultVoList){
						resultLists.add(queryResultVo);
					}
					//查询结果，总共的数目
					totalCount += resultVoList.size();
				}
				
				
			}
			
		}
		//组装分页数据
		List<WfTaskQueryResultVo> resultList = new ArrayList<WfTaskQueryResultVo>();
		//当查询结果为空时，跳过组装分页，直接返回
		if(resultLists != null && resultLists.size() > 0){
			if(totalCount % 10 > 0 && totalCount - start < 10 ){
				for(int i = start; i < start + (totalCount % 10); i++){
					resultList.add(resultLists.get(i));
				}
			}else{
				for(int i = start; i < start + 10; i++){
					resultList.add(resultLists.get(i));
				}
			}
		}
		
		ResultPage<WfTaskQueryResultVo> resultVoList = new ResultPage<WfTaskQueryResultVo>(start, length, totalCount, resultList);
		String jsonData = null;
		jsonData = ResponseUtils.toDataTableJson(resultVoList,searchCallBack(),"registNo","policyNo","policyNoLink","licenseNo","insuredName","damageTime",
				"reportTime","comCodePly:ComCode","damageAddress","flowId");
		logger.info("FlowQuery.jsonData="+jsonData);

		return jsonData;
		
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
				return dataMap;
			}
		};
		return callBack;
	}
	/**
	 * 展示代查勘相应的处理机构
	 * @param flowId
	 * @return
	 */
	@RequestMapping(value="/dcheckHandlerList.do")
	public ModelAndView dcheckHandlerList(String flowId){
		ModelAndView mv=new ModelAndView();
		List<String> nodeCodes=new ArrayList<String>();
		nodeCodes.add(FlowNode.Check.name());
		nodeCodes.add(FlowNode.DLoss.name());
		nodeCodes.add(FlowNode.PLoss.name());
		List<PrpLWfTaskQueryVo> taskQueryList=new ArrayList<PrpLWfTaskQueryVo>();
		//查询in表
		List<PrpLWfTaskVo> taskInVos=wfTaskHandleService.findPrpLWfTaskByFlowIdAndnodeCode(flowId,"1",nodeCodes);
		//查询out表
		List<PrpLWfTaskVo> taskOutVos=wfTaskHandleService.findPrpLWfTaskByFlowIdAndnodeCode(flowId,"2",nodeCodes);
		if(taskInVos!=null && taskInVos.size()>0){
			for(PrpLWfTaskVo vo:taskInVos){
				PrpLWfTaskQueryVo taskVo=new PrpLWfTaskQueryVo();
				if(FlowNode.Check.name().equals(vo.getNodeCode())){
					taskVo.setDcheckTaskType(vo.getTaskName());
					taskVo.setDcheckHandlerCom(codeTranService.transCode("ComCode",StringUtils.isNotBlank(vo.getHandlerCom())? vo.getHandlerCom().substring(0,6)+"00":""));
					taskVo.setDcheckHandlerName(codeTranService.transCode("UserCode",vo.getHandlerUser()));
					taskQueryList.add(taskVo);
				}else if(FlowNode.DLoss.name().equals(vo.getNodeCode())){
				    taskVo.setDcheckTaskType(vo.getTaskName()+ "("+vo.getItemName()+")");
					taskVo.setDcheckHandlerCom(codeTranService.transCode("ComCode",StringUtils.isNotBlank(vo.getHandlerCom())? vo.getHandlerCom().substring(0,6)+"00":""));
					taskVo.setDcheckHandlerName(codeTranService.transCode("UserCode",vo.getHandlerUser()));
					taskQueryList.add(taskVo);
					
				}else if(FlowNode.PLoss.name().equals(vo.getNodeCode())){
					if(FlowNode.PLFirst.name().equals(vo.getSubNodeCode())){
						taskVo.setDcheckTaskType("人伤首次跟踪");
						taskVo.setDcheckHandlerCom(codeTranService.transCode("ComCode",StringUtils.isNotBlank(vo.getHandlerCom())? vo.getHandlerCom().substring(0,6)+"00":""));
						taskVo.setDcheckHandlerName(codeTranService.transCode("UserCode",vo.getHandlerUser()));
						taskQueryList.add(taskVo);
					}else if(FlowNode.PLNext.name().equals(vo.getSubNodeCode())){
						taskVo.setDcheckTaskType("人伤后续跟踪");
						taskVo.setDcheckHandlerCom(codeTranService.transCode("ComCode",StringUtils.isNotBlank(vo.getHandlerCom())? vo.getHandlerCom().substring(0,6)+"00":""));
						taskVo.setDcheckHandlerName(codeTranService.transCode("UserCode",vo.getHandlerUser()));
						taskQueryList.add(taskVo);
					}
				}
			}
		}
		
		if(taskOutVos!=null && taskOutVos.size()>0){
			for(PrpLWfTaskVo vo:taskOutVos){
				PrpLWfTaskQueryVo taskVo=new PrpLWfTaskQueryVo();
				if(FlowNode.Check.name().equals(vo.getNodeCode()) && !"7".equals(vo.getWorkStatus()) && !"8".equals(vo.getWorkStatus())){
					taskVo.setDcheckTaskType(vo.getTaskName());
					taskVo.setDcheckHandlerCom(codeTranService.transCode("ComCode",StringUtils.isNotBlank(vo.getHandlerCom())? vo.getHandlerCom().substring(0,6)+"00":""));
					taskVo.setDcheckHandlerName(codeTranService.transCode("UserCode",vo.getHandlerUser()));
					taskQueryList.add(taskVo);
				}else if(FlowNode.DLoss.name().equals(vo.getNodeCode()) && !"7".equals(vo.getWorkStatus()) && !"8".equals(vo.getWorkStatus())){
				    taskVo.setDcheckTaskType(vo.getTaskName()+ "("+vo.getItemName()+")");
					taskVo.setDcheckHandlerCom(codeTranService.transCode("ComCode",StringUtils.isNotBlank(vo.getHandlerCom())? vo.getHandlerCom().substring(0,6)+"00":""));
					taskVo.setDcheckHandlerName(codeTranService.transCode("UserCode",vo.getHandlerUser()));
					taskQueryList.add(taskVo);
					
				}else if(FlowNode.PLoss.name().equals(vo.getNodeCode()) && !"7".equals(vo.getWorkStatus()) && !"8".equals(vo.getWorkStatus())){
					if(FlowNode.PLFirst.name().equals(vo.getSubNodeCode())){
						taskVo.setDcheckTaskType("人伤首次跟踪");
						taskVo.setDcheckHandlerCom(codeTranService.transCode("ComCode",StringUtils.isNotBlank(vo.getHandlerCom())? vo.getHandlerCom().substring(0,6)+"00":""));
						taskVo.setDcheckHandlerName(codeTranService.transCode("UserCode",vo.getHandlerUser()));
						taskQueryList.add(taskVo);
					}else if(FlowNode.PLNext.name().equals(vo.getSubNodeCode())){
						taskVo.setDcheckTaskType("人伤后续跟踪");
						taskVo.setDcheckHandlerCom(codeTranService.transCode("ComCode",StringUtils.isNotBlank(vo.getHandlerCom())? vo.getHandlerCom().substring(0,6)+"00":""));
						taskVo.setDcheckHandlerName(codeTranService.transCode("UserCode",vo.getHandlerUser()));
						taskQueryList.add(taskVo);
					}
				}
			}
		}
		List<PrpLWfTaskQueryVo> taskQueryList1=new ArrayList<PrpLWfTaskQueryVo>();
		if(taskQueryList!=null && taskQueryList.size()>0){
			for(PrpLWfTaskQueryVo vo1:taskQueryList){
				if(vo1.getDcheckTaskType().contains("查勘")){
					taskQueryList1.add(vo1);
				}
			}
			for(PrpLWfTaskQueryVo vo1:taskQueryList){
				if(vo1.getDcheckTaskType().contains("定损")){
					taskQueryList1.add(vo1);
				}
			}
			for(PrpLWfTaskQueryVo vo1:taskQueryList){
				if(vo1.getDcheckTaskType().contains("人伤")){
					taskQueryList1.add(vo1);
				}
			}
		}
		mv.addObject("taskQueryList",taskQueryList1);
		mv.setViewName("flowQuery/dcheckHandlerShow");
		return mv;
	}
	
	@RequestMapping(value="/exportExcel.do")
	@ResponseBody
	public String exportExcel(HttpServletResponse response,PrpLWfTaskQueryVo taskQueryVo) throws Exception {
		String comCode=WebUserUtils.getComCode();
		SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(WebUserUtils.getUserCode());
		Map<String,List<SaaFactorPowerVo>> factorPowerMap = new HashMap<String,List<SaaFactorPowerVo>>();
		if(userPowerVo != null){
			factorPowerMap = userPowerVo.getPermitFactorMap();
		}
		if(userPowerVo==null){
			throw new RuntimeException("未找到用户【"+WebUserUtils.getUserCode()+"】在系统的登录机构，查询无结果！");
		}
		if(factorPowerMap!=null && factorPowerMap.size()>0){
			List<SaaFactorPowerVo> factorPowerVoList = factorPowerMap.get("FF_COMCODE");
			if(factorPowerVoList != null && factorPowerVoList.size()>0){
				comCode=factorPowerVoList.get(0).getDataValue();
				if(StringUtils.isNotBlank(comCode) && comCode.contains("%")){
					int comIndex=comCode.indexOf("%");
					String comStart=comCode.substring(0,comIndex);
					if(comIndex==1){
						comCode=comStart+"0000000";
					}else if(comIndex==2){
						comCode=comStart+"000000";
					}else if(comIndex==3){
						comCode=comStart+"00000";
					}else if(comIndex==4){
						comCode=comStart+"0000";
					}else if(comIndex==5){
						comCode=comStart+"000";
					}else if(comIndex==6){
						comCode=comStart+"00";
					}else if(comIndex==7){
						comCode=comStart+"0";
					}
				}
				
				
				
			}
			
		}
		List<WfTaskQueryResultVo>  resultList=dcheckService.dcheckDatas(taskQueryVo,comCode);
		// 填充projects数据
		List<Map<String,Object>> list=dcheckService.createExcelRecord(resultList);
		String fileDir = "ins/sino/claimcar/other/files/dcheckHandlerCom.xlsx";
		// map中对应的key
		String keys[] = {"registNo","policyNo","insuredName","licenseNo","damageTime","reportTime","damageAddress","comCodePly","handlerCom"};	
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try{
			File file = ExportExcelUtils.getExcelDemoFile(fileDir);
			ExportExcelUtils.writeNewExcel(file,"Sheet1",list,keys,CodeConstants.IsSingleAccident.NOT).write(os);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		response.reset();
        String fileName="代查勘案件信息.xlsx";
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition","attachment;filename="+URLEncoder.encode(fileName,"utf-8"));
        
		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try{
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			// Simple read/write loop.
			while( -1!=( bytesRead = bis.read(buff,0,buff.length) )){
				bos.write(buff,0,bytesRead);
			}
		}
		catch(final IOException e){
			throw e;
		}
		finally{
			if(bis!=null) bis.close();
			if(bos!=null) bos.close();
		}

		return null;
	}
}
