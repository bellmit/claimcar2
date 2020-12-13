package ins.sino.claimcar.claim.web.action;

import ins.framework.common.ResultPage;
import ins.framework.utils.Beans;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrplReplevyDetailVo;
import ins.sino.claimcar.claim.vo.PrplReplevyMainVo;
import ins.sino.claimcar.endcase.service.EndCaseService;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.other.vo.RecPayLaunchResultVo;
import ins.sino.claimcar.other.vo.RecPayLaunchVo;
import ins.sino.claimcar.pay.service.RecPayService;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/recPay")
public class RecPayAction {
	private static Logger logger = LoggerFactory.getLogger(RecPayAction.class);
	
	@Autowired
	RecPayService recPayService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	private SubrogationService subrogationService;
	
	@Autowired
	EndCaseService endCaseService;
	@Autowired
	WfTaskQueryService wfTaskQueryService;
	
	
	/**
	 * 追偿初始化
	 * @return
	 */
	@RequestMapping(value = "/recPayList.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initPage() {
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -1);
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("taskQuery/RecPayTaskQueryList");

		return mav;
	}
	
	/** /claimcar/recPay/recPayEdit.do
	 * 追偿处理初始化
	 */
	@RequestMapping(value="/recPayEdit.do")
	@ResponseBody
	public ModelAndView recPayEdit(Double flowTaskId){
		ModelAndView mv = new ModelAndView();
		Date date=new Date();
		PrpLCompensateVo compensateVo=new PrpLCompensateVo();
		PrplReplevyMainVo prplReplevyMainVo=new PrplReplevyMainVo();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		String riskCode=wfTaskVo.getRiskCode();
		if(wfTaskVo.getHandlerStatus().equals("0")){
			mv.addObject("userCode", WebUserUtils.getUserCode());
			compensateVo=compensateTaskService.findPrpLCompensateVoByPK(wfTaskVo.getCompensateNo());
			mv.addObject("policyNo",compensateVo.getPolicyNo());
			mv.addObject("claimNo",compensateVo.getClaimNo());
			mv.addObject("riskCode", compensateVo.getRiskCode());
		}else{
			prplReplevyMainVo = recPayService.findPrplReplevyMainVoByComPensateNo(wfTaskVo.getHandlerIdKey());
			mv.addObject("userCode", prplReplevyMainVo.getHandlerCode());
			mv.addObject("prplReplevyMain", prplReplevyMainVo);
			mv.addObject("prplReplevyDetailVos", prplReplevyMainVo.getPrplReplevyDetails());
			mv.addObject("replevyType", prplReplevyMainVo.getReplevyType());
			mv.addObject("compensateNo", prplReplevyMainVo.getCompensateNo());
			
			mv.addObject("policyNo",prplReplevyMainVo.getPolicyNo());
			mv.addObject("claimNo",prplReplevyMainVo.getClaimNo());
			mv.addObject("riskCode", prplReplevyMainVo.getRiskCode());
		}
		mv.addObject("endDate", date);
		mv.addObject("handlerStatus", wfTaskVo.getHandlerStatus());
		mv.addObject("registNo",wfTaskVo.getRegistNo());
		mv.addObject("comCode",wfTaskVo.getComCode());
		mv.addObject("flowTaskId", flowTaskId);
		mv.addObject("taskVo", wfTaskVo);
		mv.setViewName("recPay/RecPayEdit");
		return mv;
	}
	

	/**
	 * 添加信息行的ajax请求
	 * 
	 * @return
	 */
	@RequestMapping("/addReplevy.ajax")
	@ResponseBody
	public ModelAndView addRecRow(int size,String riskCode) {
		//int size=0;
		//size=recPayTSize.intValue();
		//PrplReplevyMainVo prplReplevyMainVo=new PrplReplevyMainVo();
		List<PrplReplevyDetailVo> prplReplevyDetailVos=new ArrayList<PrplReplevyDetailVo>();
		PrplReplevyDetailVo prplReplevyDetailVo=new PrplReplevyDetailVo();
		prplReplevyDetailVos.add(prplReplevyDetailVo);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("size", size);
		modelAndView.addObject("riskCode", riskCode);
		modelAndView.addObject("prplReplevyDetailVos",prplReplevyDetailVos);
		modelAndView.setViewName("recPay/RecPayEdit_RecPayTbody");
		return modelAndView;
	}
	
	/**
	 * 提交追偿处理
	 */
	@RequestMapping("/commitRecPay.do")
	@ResponseBody
	public AjaxResult commitRecPay(Double flowTaskId,
			@FormModel(value="prplReplevyMainVo")PrplReplevyMainVo prplReplevyMainVo){
		//Long flowTaskId=Long.parseLong(flowTaskid);
		AjaxResult ajaxResult = new AjaxResult();
		Date date=new Date();
		try{
			SysUserVo userVo = WebUserUtils.getUser();
			if(prplReplevyMainVo.getId() == null){
				prplReplevyMainVo.setCreateTime(date);
				prplReplevyMainVo.setCreateUser(userVo.getUserCode());
			}else{
				PrplReplevyMainVo prplRemain=recPayService.findPrplReplevyMainVoByPK(prplReplevyMainVo.getId());
				prplReplevyMainVo.setCreateTime(prplRemain.getCreateTime());
				prplReplevyMainVo.setCreateUser(prplRemain.getCreateUser());
				prplReplevyMainVo.setCompensateNo(prplRemain.getCompensateNo());
			}
			
			
			prplReplevyMainVo.setUpdateTime(date);
			prplReplevyMainVo.setUpdateUser(userVo.getUserCode());
			prplReplevyMainVo.setUnderWriteFlag("1");
			recPayService.commitRecPay(flowTaskId,prplReplevyMainVo,userVo);
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		}catch (Exception e) {
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}
	
	/**
	 * 暂存追偿处理
	 */
	@RequestMapping("/saveRecPay.do")
	@ResponseBody
	public AjaxResult saveRecPay(Double flowTaskId,
			@FormModel(value="prplReplevyMainVo")PrplReplevyMainVo prplReplevyMainVo){
		AjaxResult ajaxResult = new AjaxResult();
		Date date=new Date();
		if(prplReplevyMainVo.getId() == null){
			prplReplevyMainVo.setCreateTime(date);
			prplReplevyMainVo.setCreateUser(WebUserUtils.getUserCode());
		}
		prplReplevyMainVo.setUpdateTime(date);
		prplReplevyMainVo.setUpdateUser(WebUserUtils.getUserCode());
		prplReplevyMainVo.setUnderWriteFlag("0");
		PrplReplevyMainVo replevyMainVo=recPayService.saveOrUpdatePrplReplevyMain(prplReplevyMainVo,flowTaskId);
		ajaxResult.setData(replevyMainVo.getId());
		if(replevyMainVo.getSumRealReplevy()!=null){
		   ajaxResult.setStatusText(replevyMainVo.getSumRealReplevy().toString());
		}
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ajaxResult;
	}
	/**
	 * 非机动车代位信息带出
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆WLL(2017年5月3日 下午8:39:27): <br>
	 */
	@RequestMapping("/getSubPers.ajax")
	@ResponseBody
	public AjaxResult getSubPers(String registNo){
		AjaxResult ajaxResult = new AjaxResult();
		PrpLSubrogationMainVo PrpLSubrogationMainVo = subrogationService.find(registNo);
		List<PrpLSubrogationPersonVo> subrogationPersonVoList = PrpLSubrogationMainVo.getPrpLSubrogationPersons();
		PrpLSubrogationPersonVo persVo = new PrpLSubrogationPersonVo();
		//TODO 此处由于追偿的设计问题 只能展示第一个被追偿方 后续修改追偿需求后  再修改此处的展示 现在默认取第一个被追偿方
		if(CodeConstants.CommonConst.TRUE.equals(PrpLSubrogationMainVo.getSubrogationFlag()) &&
				subrogationPersonVoList!=null && subrogationPersonVoList.size()>0){
			Beans.copy().from(subrogationPersonVoList.get(0)).to(persVo);
		}
		ajaxResult.setData(persVo);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	
	/**
	 * 初始化手动追偿发起页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/recPayLaunchTaskQueryList.do")
	public ModelAndView recPayLaunchTaskQueryList() {
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		ModelAndView mav = new ModelAndView();
		mav.addObject("taskInTimeStart",startDate);
		mav.addObject("taskInTimeEnd",endDate);
		mav.setViewName("taskQuery/RecPayLaunchTaskQueryList");
		return mav;
	}
	
	/**
	 * 查询可以发起手动发起追偿的数据
	 * @param model
	 * @param recPayLaunchVo
	 * @param start
	 * @param length
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/recPayLaunchFind.do")
	@ResponseBody
	public String recPayLaunchFind(
			Model model,
			@FormModel("recPayLaunchVo") RecPayLaunchVo recLaunchVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception {
		String userCode = WebUserUtils.getUserCode();
		List<SysCodeDictVo> codes = wfTaskQueryService.findPermitcompanyByUserCode(userCode);
		String comCode = "";
		if(codes != null && codes.size() > 0){
			comCode = codes.get(0).getCodeCode();
		}
		ResultPage<RecPayLaunchResultVo> page = endCaseService.find(recLaunchVo, start, length,comCode);
		String jsonData = ResponseUtils.toDataTableJson(page,"mercyFlagName","registNoHtml","policyNoHtml","claimNo","policyType","endTime");
		logger.debug(jsonData);
		return jsonData;
	}
	
	
	/**
	 * 发起手动追偿：
	 * 1.判断立案号下是否有未完成追偿
	 *   没有：发起追偿
	 *   有   ： 提示存在
	 * @return
	 */
	@RequestMapping(value = "/validateExist.do",method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult validateExist(String claimNo){
		AjaxResult ajaxResult = new AjaxResult();
		String msg = "";
		List<PrpLWfTaskVo> prpLWfTaskVo = wfTaskHandleService.findCanCelTask(claimNo,FlowNode.RecPay);
		if(prpLWfTaskVo != null && prpLWfTaskVo.size() > 0){
			//该立案号有未完成的追偿
			msg = "yes";
		}else{
			msg = "no";
		}
		ajaxResult.setData(msg);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	/**
	 * 发起手动追偿：存数据到表，跳转到追偿页面
	 * @param claimNo
	 * @return
	 */
	@RequestMapping(value = "/recPayLaunch.do",method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult recPayLaunch(String claimNo,String registNo){
		AjaxResult ajaxResult = new AjaxResult();
		try {
			BigDecimal taskid = recPayService.startRecPay(claimNo,registNo);
			ajaxResult.setData(taskid.doubleValue());
		} catch (Exception e) {
			ajaxResult.setData("fail");
		}
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	
}
