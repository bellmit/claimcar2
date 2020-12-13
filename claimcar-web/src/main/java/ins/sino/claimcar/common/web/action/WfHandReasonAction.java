/******************************************************************************
 * CREATETIME : 2016年1月19日 上午11:37:25
 ******************************************************************************/
package ins.sino.claimcar.common.web.action;

import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.recloss.service.WfHandReasonServices;
import ins.sino.claimcar.recloss.vo.PrpLHandReasonVo;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/common")
public class WfHandReasonAction {

	// 服务装载
	@Autowired
	private WfHandReasonServices wfHandReasonServices;
	@Autowired
	private SysUserService sysUserService;
	
	@RequestMapping(value = "/handReasonEdit.do")
	public ModelAndView initReportMsg(Model model,
			@FormModel("prpLHandReasonVo") PrpLHandReasonVo prpLHandReasonVo) {
		prpLHandReasonVo.setComCode(WebUserUtils.getComCode());
		prpLHandReasonVo.setCreateUser(WebUserUtils.getUserCode());
		prpLHandReasonVo.setUpdateUser(WebUserUtils.getUserCode());
		wfHandReasonServices.saveSysMsg(prpLHandReasonVo);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("sysmessage/RegistMessageList");
		return mav;
	}

	@RequestMapping(value = "/init.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView init(Model model,HttpServletRequest request,HttpSession session) {
		
		BigDecimal taskId=new BigDecimal(request.getParameter("taskId"));
		String flowId=request.getParameter("flowId");
		String registNo=request.getParameter("registNo");
		String nodeCode=request.getParameter("nodeCode");
		String subNodeCode=request.getParameter("subNodeCode");
		PrpLHandReasonVo prpLHandReasonVo = new PrpLHandReasonVo();
		// 查看id返回数据
		prpLHandReasonVo = wfHandReasonServices.findHandReasonById(taskId);
		prpLHandReasonVo.setFlowId(flowId);
		prpLHandReasonVo.setTaskId(taskId);
		prpLHandReasonVo.setRegistNo(registNo);
		model.addAttribute("prpLHandReasonVo", prpLHandReasonVo);
		if(FlowNode.Sched.equals(nodeCode)||FlowNode.Check.equals(nodeCode)){
			model.addAttribute("nodeCode", "A");
		}
		if(FlowNode.DLoss.equals(nodeCode)||FlowNode.VLoss.equals(nodeCode)
				||FlowNode.VPrice.equals(nodeCode)){
			model.addAttribute("nodeCode", "B");
		}
		if(FlowNode.Certi.equals(nodeCode)){
			model.addAttribute("nodeCode", "C");
		}
		if(FlowNode.Compe.equals(nodeCode)||FlowNode.VClaim.equals(nodeCode)){
			model.addAttribute("nodeCode", "D");
		}
		if(FlowNode.ReOpen.equals(nodeCode)){
			model.addAttribute("nodeCode", "E");
		}
		ModelAndView mav = new ModelAndView();
		mav.setViewName("manager/showTaskQueryList");
		return mav;
	}
	
	
	
	
	@RequestMapping(value = "/findByMList.do")
	@ResponseBody
	public JSONObject findByMList(HttpServletRequest request) {
		
	String q= request.getParameter("q");
	System.out.println("q=="+q);
/*	JSONObject json = new JSONObject();
	json.put("id", 10);*/
	
	

	List<SysUserVo> sysUserVoList= sysUserService.findByUserOrName(q);
	 JSONArray json = new JSONArray();
	 JSONObject jos = new JSONObject();
     for(SysUserVo a : sysUserVoList){
         JSONObject jo = new JSONObject();
         jo.put("text", a.getUserName());
         jo.put("id", a.getUserCode());
         json.add(jo);
     }
     jos.put("results", json);
     jos.put("total", sysUserVoList.size());
	System.out.println(sysUserVoList.size()+"==json==="+jos);
	return jos;
	//return ：“{'result':[{'id':'4048','text':'4808','name':'CHINA169-BJ'},{'id':'4048','text':'4808','name':'CHINA169-BJ'}],'total':'1'}”
	}
}
