package ins.sino.claimcar.base.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.vo.PrpsmsMessageVo;
import ins.sino.claimcar.claim.vo.SmsmainhisVo;
import ins.sino.claimcar.manager.vo.PrpdAddresseeVo;
import ins.sino.claimcar.other.service.MsgModelService;
import ins.sino.claimcar.other.service.MsgParamService;
import ins.sino.claimcar.other.vo.SysMsgModelVo;
import ins.sino.claimcar.other.vo.SysMsgParamVo;
import org.springframework.http.HttpRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
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
@RequestMapping("/msgModel")
public class MsgModelAction {
	@Autowired
	MsgModelService msgModelService;
	
	@Autowired
	MsgParamService msgParamService;
	
	private static Logger logger = LoggerFactory.getLogger(MsgModelAction.class);
	
	@RequestMapping("/msgModelList.do")
	@ResponseBody
	public ModelAndView msgParamList() {
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName("base/msgModel/MsgModelList");
		return modelAndView;
	}
	
	//短信查询
	@RequestMapping("/msgModelQueryList.do")
	public ModelAndView msgQueryList(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		 //SimpleDateFormat format=new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String registNo=request.getParameter("registNo");
		String isRepair=request.getParameter("isRepair");
		Date endDate=new Date();//结束时间为当前时间
		Date startDate = DateUtils.addDays(endDate,-15);
		
		modelAndView.addObject("startDate",startDate);
		modelAndView.addObject("endDate",endDate);
		modelAndView.addObject("registNo",registNo);
		modelAndView.addObject("isRepair",isRepair);
		modelAndView.setViewName("base/msgModel/MsgModelQueryList");
		return modelAndView;
	}
	
	//短信查询方法
	@RequestMapping(value="/msgModelFind.do",method = RequestMethod.POST )
	@ResponseBody
	public String search(@FormModel(value="PrpsmsMessageVo")PrpsmsMessageVo prpsmsMessageVo,
			@RequestParam(value="start",defaultValue="0")Integer start,
			@RequestParam(value="length",defaultValue="10")Integer length)throws Exception{
		
		logger.info("==============="+prpsmsMessageVo.getTruesendTimeStart()+prpsmsMessageVo.getTruesendTimeEnd());
		ResultPage<PrpsmsMessageVo> list =msgModelService.findAllSmsMessageByHql(prpsmsMessageVo, start, length);
				
		String jsonData = ResponseUtils.toDataTableJson(list, "phoneCode", "sendNodecode", "truesendTime","sendText","status");
		return jsonData;
		
	}
	
	
	//领导人信息管理
	@RequestMapping("/leaderInfoManageList.do")
	@ResponseBody
	public ModelAndView leaderInfoManageList() {
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName("base/msgModel/LeaderInfoManageList");
		return modelAndView;
	}
	
	@RequestMapping("/msgModelEdit.do")
	@ResponseBody
	public ModelAndView msgModelEdit(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String Index ="";
		String mid = request.getParameter("mid");
	     Index =request.getParameter("index");
		if(mid!=null){
			Long id = Long.parseLong(mid);
			SysMsgModelVo sysMsgModelVo = msgModelService.findSysMsgModelVoByPK(id);
			modelAndView.addObject("SysMsgModel", sysMsgModelVo);	
		}
		
			List<SysMsgParamVo> sysMsgParamVo = msgParamService.findSysMsgParamVo();
					
			modelAndView.addObject("msgParam", sysMsgParamVo);
			modelAndView.addObject("Index", Index);
		modelAndView.setViewName("base/msgModel/MsgModelEdit");

		return modelAndView;
	}
	
	@RequestMapping("/saveMsgModel.do")
	@ResponseBody
	public AjaxResult saveMsgModel(
			@FormModel(value = "SysMsgModelVo") SysMsgModelVo sysMsgModelVo) {
		AjaxResult ajaxResult = new AjaxResult();
		//System.out.println(sysMsgModelVo.getModelName());
		Date date = new Date();
		sysMsgModelVo.setCreateTime(date);
		sysMsgModelVo.setUpdateTime(date);
		sysMsgModelVo.setCreateUser(WebUserUtils.getUserCode());
		sysMsgModelVo.setUpdateUser(WebUserUtils.getUserCode());
		msgModelService.saveOrUpdateSysMsgModel(sysMsgModelVo);
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ajaxResult;
	}

	@RequestMapping("/findMsgModel.do")
	@ResponseBody
	public String search(
			@FormModel(value = "SysMsgModelVo") SysMsgModelVo sysMsgModelVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) {
		ResultPage<SysMsgModelVo> resultPage = msgModelService.findAllSysMsgModelByHql(sysMsgModelVo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(resultPage, "id",
				"modelName", "comCode:ComCode", "modelType:MsgModelType",
				"systemNode:SystemNode","validFlag");
		logger.debug(jsonData);
		return jsonData;
	}
	
	@RequestMapping("/searchLeaderInfo.do")
	@ResponseBody
	public String searchLeaderInfo(
			@FormModel(value = "prpdAddresseeVo") PrpdAddresseeVo prpdAddresseeVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length){
		ResultPage<PrpdAddresseeVo> page=msgModelService.searchLeaderInfo(prpdAddresseeVo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(page, "id",
				"name", "comCode:ComCode", "mobileNo");
		logger.debug(jsonData);
		return jsonData;
	}

	@RequestMapping("/initLeaderInfoEdit.do")
	@ResponseBody
	public ModelAndView initLeaderInfoEdit(Long id){
		ModelAndView modelAndView = new ModelAndView();
		List<PrpdAddresseeVo> prpdAddresseeVoList=msgModelService.findLeaderInfoById(id);
		modelAndView.addObject("prpdAddresseeVoList",prpdAddresseeVoList);
		modelAndView.setViewName("base/msgModel/LeaderInfoEdit");
		return modelAndView;
	}	/**
	 * 打开增加领导页面
	 * @return
	 */
	@RequestMapping("/openAddLeaderInfoPage.do")
	public ModelAndView addLeaderInfo(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("base/msgModel/LeaderInfoAdd");
		return modelAndView;
	}



	/**
	 * 自助理赔短信发送人维护页面
	 */
	@RequestMapping("/autoclaimmsgsender.do")
	@ResponseBody
	public ModelAndView modifyAutoClaimMsgSender(){
		ModelAndView modelAndView=new ModelAndView();
		List<PrpdAddresseeVo> prpdAddresseeVoList = msgModelService.searchAutoClaimMsgSender(CodeConstants.ModelType.autocheck);
		modelAndView.addObject("prpdAddresseeVoList",prpdAddresseeVoList);
		modelAndView.setViewName("base/msgModel/AutoClaimMsgSender");
		return modelAndView;
	}

	/**
	 * 更新自助理赔短信发送人名字或电话
	 * @param addressid 主键
	 * @param name 名字
	 * @param mobile 电话
	 * @return
	 */
	@RequestMapping("/updateMsgSenderInfo.do")
	@ResponseBody
	public AjaxResult updateAutoClaimMsgSender(String addressid, String name, String mobile) {
		AjaxResult ajaxResult = new AjaxResult();
		if (StringUtils.isBlank(addressid)) {
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setData("主键为空，无法完成修改！");

			return ajaxResult;
		}
		if (StringUtils.isBlank(name)) {
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setData("名字为空，无法完成修改！");

			return ajaxResult;
		}
		PrpdAddresseeVo prpdAddresseeVo = msgModelService.findAutoClaimMsgSenderById(new Long(addressid));
		if (prpdAddresseeVo == null) {
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setData("未找到相关人员信息，无法完成修改！");
		} else {
			prpdAddresseeVo.setName(name);
			prpdAddresseeVo.setMobileNo(mobile);
			try {
				msgModelService.updateAutoClaimMsgSender(prpdAddresseeVo);
				ajaxResult.setStatus(HttpStatus.SC_OK);
				ajaxResult.setData("修改成功！");
			} catch (Exception e) {
				logger.info("自助理赔短信接收人信息修改失败！prpdAddressee.id = " + addressid + " name=" + name, e);
				ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				ajaxResult.setData("修改失败！");
			}
		}
		return ajaxResult;
	}
	
	@RequestMapping("/deleteMsgModel.do")
	@ResponseBody
	public AjaxResult deleteMsgModel(HttpServletRequest request) {
		AjaxResult ar = new AjaxResult();
		String mid = request.getParameter("mid");
		if (mid != null) {
			Long id = Long.parseLong(mid);
			msgModelService.deleteSysMsgModelByPK(id);
			ar.setStatus(org.apache.http.HttpStatus.SC_OK);
		}
		return ar;
	}

	@RequestMapping("/activOrCancel.do")
	@ResponseBody
	public AjaxResult activOrCancel(String id,String validFlag){
		AjaxResult ar = new AjaxResult();
		String resuleData = msgModelService.activOrCancel(id, validFlag);
		ar.setData(resuleData);
		ar.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ar;
	}
	
	//添加信息行的ajax请求
	@RequestMapping("/addInfor.ajax")
	@ResponseBody
	public ModelAndView addInfor(int size) {
		ModelAndView modelAndView = new ModelAndView();
		List<PrpdAddresseeVo> prpdAddresseeVoList = new ArrayList<PrpdAddresseeVo>();
		PrpdAddresseeVo prpdAddresseeVo = new PrpdAddresseeVo();
		prpdAddresseeVoList.add(prpdAddresseeVo);
		modelAndView.addObject("size", size);
		modelAndView.addObject("prpdAddresseeVoList",prpdAddresseeVoList);
		modelAndView.setViewName("base/msgModel/LeaderInfoEdit_TBody");
		return modelAndView;
	}

	/**
	 * 新增领导
	 * @param list
	 * @return
	 */
	@RequestMapping("/addLeaderInfo.do")
	@ResponseBody
	public AjaxResult addLeaderInfo(
			@FormModel(value = "prpdAddresseeVo") List<PrpdAddresseeVo> list){
		AjaxResult aj = new AjaxResult();
		if(list.size()==0 && list.isEmpty()){
			aj.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			aj.setStatusText("请先填写新增的信息");
			return aj;
		}
		for (PrpdAddresseeVo prpdAddresseeVo:list) {
			if ("请选择标志".equals(prpdAddresseeVo.getFlag())){
				aj.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				aj.setStatusText("标志未选择，请选择");
				return aj;
			}
		}
		//检查是否重复添加
		boolean flag = msgModelService.checkIfAddAgain(list);
		if (flag){
			aj.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			aj.setStatusText("不能新增相同领导人信息或该领导人信息已存在");
			return aj;
		}
		msgModelService.addLeaderInfo(list,WebUserUtils.getUserCode());
		aj.setStatus(HttpStatus.SC_OK);
		return aj;
	}

	/**
	 * 修改领导信息
	 * @param list
	 * @return
	 */
	@RequestMapping("/updateLeaderInfo.do")
	@ResponseBody
	public AjaxResult updateLeaderInfo(
			@FormModel(value = "prpdAddresseeVo") List<PrpdAddresseeVo> list){
		AjaxResult ar = new AjaxResult();
		//检查所要修改的领导人信息，数据库中是否已经存在
		boolean flag = msgModelService.checkIfExist(list);
		if (flag){
			ar.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ar.setStatusText("信息未改动或所要修改的信息已存在，请重新编辑");
			return ar;
		}
		msgModelService.updateLeaderInfo2(list,WebUserUtils.getUserCode());
		ar.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ar;
	}

	/**
	 *删除领导信息
	 * @param
	 * @return
	 */
	@RequestMapping("/deleteLeaderInfo.do")
	@ResponseBody
	public AjaxResult deleteLeaderInfo(Long id){
		msgModelService.deleteLeaderInfoById(id);
		AjaxResult aj = new AjaxResult();
		aj.setStatus(org.apache.http.HttpStatus.SC_OK);
		return aj;
	}
}
