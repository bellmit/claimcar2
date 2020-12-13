package ins.sino.claimcar.lossprop.web.action;

import ins.framework.web.AjaxResult;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.lossprop.service.PropLossAdjustService;
import ins.sino.claimcar.lossprop.service.PropLossHandleService;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/propQueryOrLaunch")
public class PropQueryLaunchAction {
	@Autowired
	private PropLossService propLossService;
	@Autowired
	private PropTaskService propTaskService;
	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private PropLossHandleService propLossHandleService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	private PropLossAdjustService propLossAdjustService;
	
	/**
	 * 进入 财产定修改和追加发起 页面
	 */
	@RequestMapping(value="/propModifyLaunchEdit")
	public ModelAndView propModifyLaunchEdit(String businessId,String deflossFlag) {
		Long bId=Long.valueOf(businessId);
		String registNo="";
		PrpLRegistVo prpLregistVo=null;
		ModelAndView mv=new ModelAndView();
		PrpLdlossPropMainVo	lossPropMainVo=propLossService.findPropMainVoById(bId);
		if(lossPropMainVo != null ){
		   registNo = lossPropMainVo.getRegistNo();
		  prpLregistVo = registQueryService.findByRegistNo(registNo);
		  
		
		}
		
		FlowNode currentNode;
		if("add".equals(deflossFlag)){//追加定损
			mv.setViewName("propLoss/addPropDefloss/AddLaunchEdit");
			currentNode = FlowNode.DLPropAdd;
		}else{//修改定损
			mv.setViewName("propLoss/modifyPropDefloss/ModifyLaunchEdit");
			currentNode = FlowNode.DLPropMod;
		}
		String handleStatus = "0";
		//判断是否已发起了修改或者追加任务
		if(wfTaskHandleService.existTaskByNodeCode(lossPropMainVo.getRegistNo(),
				currentNode,lossPropMainVo.getId().toString(),"0")){
			handleStatus = "1";
		}
		
		mv.addObject("lossPropMainVo", lossPropMainVo);
		mv.addObject("deflossFlag",deflossFlag);
		mv.addObject("handleStatus",handleStatus);
		mv.addObject("prpLregistVo",prpLregistVo);
		return mv;
	}

	
	/**
	 * 财产定损修改 发起 方法
	 */
	@RequestMapping(value="/propModifyLaunch")
	@ResponseBody
	public AjaxResult propModifyLaunch(Long businessId){
		AjaxResult ajaxResult = new AjaxResult();
		try{
			/*DeflossActionVo deflossVo=new DeflossActionVo();
			this.setSecurity(deflossVo);*/
			SysUserVo sysUserVo = WebUserUtils.getUser();
			String retStr = propLossAdjustService.propModifyLaunch(businessId,sysUserVo);
			
			
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(retStr);
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
	
		return ajaxResult;
	}
	
	/**
	 * 财产追加定损 发起
	 */
	@RequestMapping(value="/propAddLaunch")
	@ResponseBody
	public AjaxResult propAddLaunch(Long businessId,String itemContent, String remark){
		//财产追加定损 传递过来的数据要操作 scheduleTask 和
		AjaxResult ajaxResult=new AjaxResult();
		try{
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
			/*DeflossActionVo deflossVo=new DeflossActionVo();
			this.setSecurity(deflossVo);
			deflossVo.setBusinessId(businessId);*/
			SysUserVo userVo = WebUserUtils.getUser();
			String retStr = propLossAdjustService.propAdditionLaunch(businessId,userVo,itemContent,remark);
			
			//提交工作流成功后 给前台页面返回一个业务号数据
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(retStr);
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		
		return ajaxResult;
	}
	
/*	private void setSecurity(DeflossActionVo deflossVo) {
		deflossVo.setUserCode(SecurityUtils.getUserCode());
		deflossVo.setUserName("管理员");
		deflossVo.setComCode(SecurityUtils.getComCode());
		deflossVo.setComName("深圳分公司");
	}*/
	
	@RequestMapping("/dLPropModVlaid.do")
	@ResponseBody
	public AjaxResult dLPropModVlaid(String registNo,String lossId){
		AjaxResult ajaxResult = new AjaxResult();
		try{
			String retData = propLossService.dLPropModVlaid(registNo, lossId);
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(retData);
		}
		catch(Exception e){
			e.printStackTrace();
			ajaxResult.setStatus(HttpStatus.SC_EXPECTATION_FAILED);
		}
		return ajaxResult;
	}
}
