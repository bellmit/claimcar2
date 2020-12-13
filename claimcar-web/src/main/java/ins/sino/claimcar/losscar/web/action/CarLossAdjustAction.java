/******************************************************************************
* CREATETIME : 2016年2月3日 上午10:18:22
******************************************************************************/
package ins.sino.claimcar.losscar.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.support.ObjectToMapCallback;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants.YN01;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.CarQueryReslutVo;
import ins.sino.claimcar.losscar.vo.CarQueryVo;
import ins.sino.claimcar.losscar.vo.DefCommonVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 车辆定损查询Action，查询可追加定损，可修改定损的任务
 * @CreateTime 2016年2月3日
 */
@Controller
@RequestMapping(value = "/carLossAdjust")
public class CarLossAdjustAction {

	private static Logger logger = LoggerFactory.getLogger(CarLossAdjustAction.class);

	
	@Autowired
	private LossCarService lossCarService;
	
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	
	@Autowired
	private RegistQueryService registQueryService;
	/**
	 * 查询可以修改定损的任务
	 */
	@RequestMapping(value = "/modifyList")
	public ModelAndView ModifyList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("carQuery/ModifyList");
		return mv;
	}
	
	/**
	 * 查询可以追加定损的任务
	 */
	@RequestMapping(value = "/additionList")
	public ModelAndView AdditionList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("carQuery/AdditionList");
		return mv;
	}
	
	/**
	 * 修改定损或者追加定损 查询方法
	 * carQueryVo.deflossFlag add 追加定损 ，modify修改定损
	 */
	@RequestMapping(value = "/forModifySeach")
	@ResponseBody
	public String forModifySeach(CarQueryVo carQueryVo) throws Exception {
		carQueryVo.setComCode(WebUserUtils.getComCode());
		ResultPage<CarQueryReslutVo> resultPage = lossCarService.findPageForAdjust(carQueryVo);
		String jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(),"mercyFlag","customerLevel","registNo","licenseNo","modelName","insuredName");
		logger.info("forModifySeach.jsonData="+jsonData);
		
		return jsonData;
	}
	
	/**
	 * 进入修改定损或者追加定损页面
	 */
	@RequestMapping(value = "/initCarAdjustLoss.do")
	public ModelAndView initCarAdjustLoss(Long lossId,String deflossFlag){
		ModelAndView mv = new ModelAndView();
		String registNo="";
		PrpLRegistVo prpLregistVo=null;
		PrpLDlossCarMainVo lossCarMainVo = lossCarService.findLossCarMainById(lossId);
		if(lossCarMainVo != null ){
			   registNo = lossCarMainVo.getRegistNo();
			  prpLregistVo = registQueryService.findByRegistNo(registNo);
			 
			
			}
		PrpLDlossCarInfoVo carInfoVo = lossCarService.findPrpLDlossCarInfoVoById(lossCarMainVo.getCarId());
		if(lossCarMainVo.getDeflossCarType().equals("1")){
			PrpLCItemCarVo prpLCItemCarVo = registQueryService.findCItemCarByRegistNo(lossCarMainVo.getRegistNo());
			mv.addObject("prpLCItemCarVo",prpLCItemCarVo);
		}
		DefCommonVo commonVo = new DefCommonVo();
		commonVo.setClaimType("0");
		FlowNode currentNode;
		if("add".equals(deflossFlag)){//追加定损
			mv.setViewName("lossCar/AdditionDefloss");
			currentNode = FlowNode.DLCarAdd;
		}else{//修改定损
			mv.setViewName("lossCar/ModifyDefloss");
			currentNode = FlowNode.DLCarMod;
		}
		//判断是否已发起了修改或者追加任务
		if(wfTaskHandleService.existTaskByNodeCode(lossCarMainVo.getRegistNo(),
				currentNode,lossCarMainVo.getId().toString(),"0")){
			commonVo.setHandleStatus("1");
		}
		
		mv.addObject("lossCarMainVo",lossCarMainVo);
		mv.addObject("carInfoVo",carInfoVo);
		mv.addObject("commonVo",commonVo);
		mv.addObject("deflossFlag",deflossFlag);
		mv.addObject("",deflossFlag);
		mv.addObject("prpLregistVo",prpLregistVo);
		
		return mv;
	}

	/**
	 * 修改定损发起任务
	 * ☆yangkun(2016年2月4日 下午12:15:37): <br>
	 */
	@RequestMapping(value = "/carModifyLaunch.do")
	@ResponseBody
	public AjaxResult carModifyLaunch(Long lossId){
		SysUserVo userVo = WebUserUtils.getUser();
		String retStr = lossCarService.carModifyLaunch(lossId,userVo);
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(retStr);
		
		return ajaxResult;
	}
	
	/**
	 * 追加定损发起任务
	 * ☆yangkun(2016年2月4日 下午12:15:37): <br>
	 */
	@RequestMapping(value = "/carAdditionLaunch.do")
	@ResponseBody
	public AjaxResult carAdditionLaunch(Long lossId){
		SysUserVo userVo = WebUserUtils.getUser();
		String retStr = lossCarService.carAdditionLaunch(lossId,userVo);
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(retStr);
		
		return ajaxResult;
	}
	
	private ObjectToMapCallback searchCallBack() {
		ObjectToMapCallback callBack = new ObjectToMapCallback() {

			public Map<String,Object> toMap(Object object) {
				Map<String,Object> dataMap = new HashMap<String,Object>();
				CarQueryReslutVo resultVo = (CarQueryReslutVo)object;
				String registNo = resultVo.getRegistNo();
				String regNoUrl = "/claimcar/carLossAdjust/initCarAdjustLoss.do?lossId="+resultVo.getLossId()+"&deflossFlag="+resultVo.getDeflossFlag();

				StringBuffer regHtml = new StringBuffer();
				regHtml.append("<a href='"+regNoUrl+"' >");
				regHtml.append(registNo);
				regHtml.append("</a>");
				dataMap.put("registNoHtml",regHtml.toString());
				
				StringBuffer bussTagHtml = new StringBuffer();
//				bussTagHtml.append("<span class='badge label-danger radius '>vip</span>");
				if(YN01.Y.equals(resultVo.getMercyFlag())){
					bussTagHtml.append("<span class='badge label-danger radius' title='紧急'>急</span>");
				}
				//参考TaskExtMapUtils 
				if(YN01.Y.equals(resultVo.getIsAlarm())){
					bussTagHtml.append("<span class='badge label-warning radius' title='已报警'>警</span>");
				}
				if(YN01.Y.equals(resultVo.getIsClaimSelf())){
					bussTagHtml.append("<span class='badge label-primary radius' title='互碰自赔'>互</span>");
				}
				if(YN01.Y.equals(resultVo.getIsSubRogation())){
					bussTagHtml.append("<span class='badge label-secondary radius' title='代位求偿'>代</span>");
				}
				if(YN01.Y.equals(resultVo.getTpFlag())){
					bussTagHtml.append("<span class='badge label-secondary radius' title='通赔'>通</span>");
				}
				if(YN01.Y.equals(resultVo.getIsMajorCase())){
					bussTagHtml.append("<span class='badge label-secondary radius' title='重大案件'>重</span>");
				}

				dataMap.put("bussTag",bussTagHtml.toString());
				
				return dataMap;
			}
		};
		return callBack;
	}
	
}
