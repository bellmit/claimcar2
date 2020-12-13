package ins.sino.claimcar.base.web.action;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.vo.PrpsmsEmailVo;
import ins.sino.claimcar.mail.service.MailModelService;
import ins.sino.claimcar.mail.vo.PrpLMailModelVo;
import ins.sino.claimcar.manager.vo.PrpdEmailVo;

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
@RequestMapping("/mailModel")
public class MailModelAction {
	
	private static Logger logger = LoggerFactory.getLogger(MailModelAction.class);
	@Autowired
	MailModelService mailModelService;
	
	@RequestMapping("/mailModelList.do")
	public ModelAndView msgParamList() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("base/mailModel/MailModelList");
		return modelAndView;
	}
	
	@RequestMapping("/findMailModel.do")
	@ResponseBody
	public String search(
			@FormModel(value = "PrpLMailModelVo") PrpLMailModelVo mailModelVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) {
		mailModelVo.setComCode(WebUserUtils.getComCode());
		ResultPage<PrpLMailModelVo> resultPage = mailModelService.findAllSysMsgModelByHql(mailModelVo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(resultPage, "id",
				"modelName", "comCode:ComCode", "modelType:MailModelType",
				"systemNode:MailNode","validFlag","comCode","modelType");
		
		return jsonData;
	}
	
	@RequestMapping("/isVaildMailModel.ajax")
	@ResponseBody
	public AjaxResult isVaildMailModel(Long id,String modelType,String comCode) {
		
		AjaxResult ajaxResult = new AjaxResult();
		int  index = 0;
		if(mailModelService.existsMailModel(id,modelType,comCode)){
			index = 1;
		}
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		ajaxResult.setData(index);
		return ajaxResult;
	}
	@RequestMapping("/mailModelEdit.do")
	public ModelAndView msgModelEdit(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String mid = request.getParameter("mid");
		String Index =request.getParameter("index");
		String isParentCom = CodeConstants.CommonConst.FALSE;
		if(WebUserUtils.getComCode().startsWith("0000") || WebUserUtils.getComCode().startsWith("0001") || WebUserUtils.getComCode().startsWith("0002")){
			isParentCom = CodeConstants.CommonConst.TRUE;
		} else {
			SysUserVo user = WebUserUtils.getUser();
			String userComCode = user.getComCode().substring(0,2) + "000000";
			modelAndView.addObject("userComCode", userComCode);
		}
		if(mid!=null){
			Long id = Long.parseLong(mid);
			PrpLMailModelVo mailModelVo = mailModelService.findMailModelByPk(id);
			modelAndView.addObject("prpLMailModel", mailModelVo);	
		}
		modelAndView.addObject("Index", Index);
		modelAndView.addObject("isParentCom", isParentCom);
		modelAndView.setViewName("base/mailModel/MailModelEdit");

		return modelAndView;
	}
	
	@RequestMapping("/deleteMailModel.do")
	@ResponseBody
	public AjaxResult deleteMailModel(HttpServletRequest request) {
		AjaxResult ar = new AjaxResult();
		String mid = request.getParameter("mid");
		if (mid != null) {
			Long id = Long.parseLong(mid);
			mailModelService.deleteMailModelByPk(id);
			ar.setStatus(org.apache.http.HttpStatus.SC_OK);
		}
		return ar;
	}
	
	@RequestMapping("/activOrCancel.do")
	@ResponseBody
	public AjaxResult activOrCancel(String id,String validFlag){
		AjaxResult ar = new AjaxResult();
		String resuleData = mailModelService.activOrCancel(Long.valueOf(id), validFlag);
		ar.setData(resuleData);
		ar.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ar;
	}
	
	@RequestMapping("/saveMailModel.do")
	@ResponseBody
	public AjaxResult saveMailModel(
			@FormModel(value = "PrpLMailModelVo") PrpLMailModelVo prpLMailModelVo) {
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo  user = WebUserUtils.getUser();
		if(StringUtils.isBlank(prpLMailModelVo.getComCode())){
			if(user != null){
				String userComCode = null;
				if(user.getComCode().startsWith("0002")){
					userComCode = user.getComCode().substring(0,4) + "0000";
				} else{
					userComCode = user.getComCode().substring(0,2) + "000000";
				}
				prpLMailModelVo.setComCode(userComCode);
			}
		}
		mailModelService.saveOrUpdateMailModel(prpLMailModelVo, WebUserUtils.getUser());
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	//邮件发送人列表查询
	@RequestMapping("/searchSendInfo.do")
	@ResponseBody
	public String searchLeaderInfo(
			@FormModel(value = "prpdEmailVo") PrpdEmailVo prpdEmailVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length){
		ResultPage<PrpdEmailVo> page=mailModelService.searchSendInfo(prpdEmailVo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(page, "id",
				"name", "email","caseType:Mold","comCode:ComCode");
		logger.info(jsonData);
		return jsonData;
	}
	
	/**
	 * 进入邮件发送人信息管理
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年3月14日 下午10:45:40): <br>
	 */
	@RequestMapping("/senderInfoManageList.do")
	public ModelAndView leaderInfoManageList() {
		ModelAndView modelAndView = new ModelAndView();
		SysUserVo user = WebUserUtils.getUser();
		if(user != null){
			String userComCode = null; 
			if(user.getComCode().startsWith("0000") || user.getComCode().startsWith("0001") || user.getComCode().startsWith("0002")){
				userComCode = user.getComCode().substring(0,4) + "0000";
			} else {
				userComCode = user.getComCode().substring(0,2) + "000000";
			}
			modelAndView.addObject("userComCode",userComCode);
		}
		modelAndView.setViewName("base/mailModel/senderInfoManageList");
		return modelAndView;
	}
	/**
	 * 初始化邮件发送人页面
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年3月14日 下午10:45:21): <br>
	 */
	@RequestMapping("/initSenderInfoEdit.do")
	public ModelAndView initLeaderInfoEdit(){
		ModelAndView modelAndView = new ModelAndView();
		SysUserVo user = WebUserUtils.getUser();
		String comCode = null;
		if(user != null){
			String userComCode = null; 
			if(user.getComCode().startsWith("0000") || user.getComCode().startsWith("0001") || user.getComCode().startsWith("0002")){
				userComCode = user.getComCode().substring(0,4) + "0000";
			} else {
				userComCode = user.getComCode().substring(0,2) + "000000";
			}
			modelAndView.addObject("userComCode",userComCode);
			if(userComCode.startsWith("0000") || userComCode.startsWith("0001")){
				comCode = null;
			} else {
				comCode = userComCode;
			}
		}
		List<PrpdEmailVo> prpdEmailVoList = mailModelService.findAllSenderInfo(comCode);
		modelAndView.addObject("prpdEmailVoList",prpdEmailVoList);
		modelAndView.setViewName("base/mailModel/SenderInfoEdit");
		return modelAndView;
	}
	/**
	 * 新增、更新email信息
	 * <pre></pre>
	 * @param list
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年3月14日 下午10:45:00): <br>
	 */
	@RequestMapping("/updateSenderInfo.do")
	@ResponseBody
	public AjaxResult updateLeaderInfo(
			@FormModel(value = "prpdEmailVo") List<PrpdEmailVo> list){
		AjaxResult ar = new AjaxResult();
		
		
		SysUserVo user = WebUserUtils.getUser();
		String comCode = null;
		if(user != null){
			comCode = user.getComCode();
		}
		mailModelService.updateSenderInfo(list, comCode);
		ar.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ar;
	}
	//添加信息行的ajax请求
	@RequestMapping(value="/addInfor.ajax",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult addInfor(int size) {
		AjaxResult ar = new AjaxResult();
		List<PrpdEmailVo> prpdEmailVoList = new ArrayList<PrpdEmailVo>();
		PrpdEmailVo prpdEmailVo = new PrpdEmailVo();
		prpdEmailVoList.add(prpdEmailVo);
		ar.setData(prpdEmailVoList);
		ar.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ar;
	}
	//邮件查询
	@RequestMapping("/mailModelQueryList.do")
	public ModelAndView msgQueryList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		Date endDate=new Date();//结束时间为当前时间
		Date startDate = DateUtils.addDays(endDate,-15);

		SysUserVo user = WebUserUtils.getUser();
		if(user != null){
			String userComCode = null; 
			if(user.getComCode().startsWith("0000") || user.getComCode().startsWith("0001") || user.getComCode().startsWith("0002")){
				userComCode = user.getComCode().substring(0,4) + "0000";
			} else {
				userComCode = user.getComCode().substring(0,2) + "000000";
			}
			modelAndView.addObject("userComCode",userComCode);
		}
		modelAndView.addObject("startDate",startDate);
		modelAndView.addObject("endDate",endDate);
		modelAndView.setViewName("base/mailModel/MailModelQueryList");
		return modelAndView;
	}
	//邮件查询方法
	@RequestMapping(value="/mailModelFind.do",method = RequestMethod.POST )
	@ResponseBody
	public String search(@FormModel(value="prpsmsEmailVo")PrpsmsEmailVo prpsmsEmailVo,
			@RequestParam(value="start",defaultValue="0")Integer start,
			@RequestParam(value="length",defaultValue="10")Integer length)throws Exception{
		ResultPage<PrpsmsEmailVo> list =mailModelService.findAllSmsEmailByHql(prpsmsEmailVo, start, length);				
		String jsonData = ResponseUtils.toDataTableJson(list, "email","createTime","sendText","status");
		return jsonData;
	}	
}
