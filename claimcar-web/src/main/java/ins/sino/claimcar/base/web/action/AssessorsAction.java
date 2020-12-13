package ins.sino.claimcar.base.web.action;

import ins.framework.common.ResultPage;
import ins.framework.service.CodeTranService;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.DateUtils;
import ins.platform.utils.ExportExcelUtils;
import ins.platform.utils.ImportExcelUtil;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.ClaimToReinsuranceService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdIntermBankVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.manager.vo.PrplOldaccbankCodeVo;
import ins.sino.claimcar.other.service.AccountInfoService;
import ins.sino.claimcar.other.service.AccountQueryService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.AssessorQueryResultVo;
import ins.sino.claimcar.other.vo.AssessorQueryVo;
import ins.sino.claimcar.other.vo.PrpDAccRollBackAccountVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;
import ins.sino.claimcar.other.vo.PrplInterrmAuditVo;
import ins.sino.claimcar.payment.service.ClaimToPaymentService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.vat.service.BilllclaimService;
import ins.sino.claimcar.vat.vo.PrpLbillinfoVo;
import ins.sino.claimcar.vat.vo.VatQueryViewVo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.sinosoft.image.action.ImgRemoteDataAction;

@Controller
@RequestMapping("/assessors")
public class AssessorsAction {

	private static Logger logger = LoggerFactory.getLogger(AssessorsAction.class);
	// 常用格式定义
	private static final String FM_DATE_dd = "#yyyy-MM-dd";
	private static final String FM_DATE_mm = "#yyyy-MM-dd HH:mm";
	private static final String FM_NUMB_00 = "$0.00";

	@Autowired
	private ManagerService managerService;
	@Autowired
	private AssessorService assessorService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	ClaimTextService claimTextSerVice;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	ClaimToPaymentService claimToPaymentService;
	@Autowired
	ClaimToReinsuranceService claimToReinsuranceService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;

	@Autowired
	private AccountQueryService accountQueryService;

	@Autowired
	private PayCustomService payCustomService;
	@Autowired
	private AccountInfoService accountInfoService;
	
	@Autowired
	private SysUserService sysService;
	
	@Autowired
	private BilllclaimService billlclaimService;

	/**
	 * 进入公估费补录页面
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月7日 上午11:01:45): <br>
	 */
     @RequestMapping("/assessorFeeSupplementList")
	 public ModelAndView  assessorFeeSupplement(){
    	 ModelAndView modelAndView = new ModelAndView();
    	 Date endDate = new Date();
 		 Date startDate = DateUtils.addDays(endDate, -15);
 		 modelAndView.addObject("taskInTimeStart",startDate);
 		 modelAndView.addObject("taskInTimeEnd",endDate);
    	 modelAndView.setViewName("base/assessors/AssessorsSupplyList");
    	 return modelAndView;
    }
     
    /**
      *	查询公估费补录列表 
      * <pre></pre>
      * @param queryVo
      * @param start
      * @param length
      * @return
      * @throws ParseException
      * @modified:
      * ☆XiaoHuYao(2019年8月7日 上午11:20:37): <br>
      */
    @RequestMapping("/assSupplymentSerach")
 	@ResponseBody
 	public String assSupplymentSerach(@FormModel("queryVo") AssessorQueryVo queryVo,// 页面组装VO
 							@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
 							@RequestParam(value = "length", defaultValue = "10") Integer length) throws ParseException {
 		queryVo.setHandlerCode(queryVo.getHandlerCode().replaceAll("\\s*",""));// 去掉所有空格符
 		queryVo.setStart(start);
 		queryVo.setLength(length);
 		ResultPage<AssessorQueryResultVo> resultPage;
 		//先判断是否是公估人员再做查询
		if(managerService.findIntermByUserCode(queryVo.getHandlerCode()) == null){
			throw new IllegalArgumentException("此工号不是公估人员");
		}
 		String jsonData = null;
		try{
			resultPage = assessorService.findSupplymentPageForAssessor(queryVo);
			jsonData = ResponseUtils.toDataTableJson(resultPage,"registNo","policyNo","insureName"
					,"lossDate"+FM_DATE_mm,"taskDetail","userName","taskType","bussId");
			logger.info("assSupplymentSerach.jsonData="+jsonData);
		}catch(Exception e){
			logger.error("查询公估费补录列表失败",e);
		}
 		return jsonData;

 	}
    
    /**
     * 公估费保存
     * @param taskType
     * @param mainId
     * @return
     * @modified:
     * ☆XiaoHuYao(2019年8月13日 下午6:28:59): <br>
     */
	@RequestMapping("/supplementAssessors")
	@ResponseBody
	public AjaxResult supplementAssessors(String taskType,Long mainId,String registNo,String userCode){
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo userVo = sysService.findByUserCode(userCode);
		try{
			//财产追加和定损追加需要考虑是否已经生成了公估费任务
			if(managerService.findExistAsessor(taskType,mainId,registNo,userCode)){
				throw new IllegalArgumentException("该公估费任务已经生成，不能重复生成");
			}
			managerService.saveAssessors(taskType,mainId,registNo,userVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setStatusText("操作成功！");
		}catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			logger.error("公估费补录失败",e);
		}
		return ajaxResult;
	}
	/**
	 * 公估费查询界面
	 * @return
	 * @modified: ☆XMSH(2016年8月17日 上午11:15:53): <br>
	 */
	@RequestMapping("/assessorsFeeQueryList.do")
	public ModelAndView assessorsFeeCheck() {
		String comCode = WebUserUtils.getComCode();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		ModelAndView mav = new ModelAndView();
		List<PrpdIntermMainVo> prpdIntermlist = managerService.findIntermListByHql(comCode);
		List<PrpdIntermMainVo> prpdIntermlist_2 = managerService.findIntermListByCaseType(comCode,"2");
		List<SysCodeDictVo> dictVos = new ArrayList<SysCodeDictVo>();
		String dictStr_0 = "";
		String dictStr_2 = "";

		if(prpdIntermlist!=null&&prpdIntermlist.size()>0){
			for(PrpdIntermMainVo itemKind:prpdIntermlist){
				SysCodeDictVo dict1Vo = new SysCodeDictVo();
				dict1Vo.setCodeCode(itemKind.getId()+"");
				dict1Vo.setCodeName(itemKind.getIntermCode()+"-"+itemKind.getIntermName());
				dictVos.add(dict1Vo);
			}
		}else{
			SysCodeDictVo dict1Vo = new SysCodeDictVo();
			dict1Vo.setCodeCode("");
			dict1Vo.setCodeName(" ");
			dictVos.add(dict1Vo);
		}

		for(PrpdIntermMainVo itemKind:prpdIntermlist){
			String key = itemKind.getId().toString();
			String value = itemKind.getIntermCode()+"-"+itemKind.getIntermName();
			dictStr_0 = dictStr_0+key+"+"+value+",";
		}
		for(PrpdIntermMainVo itemKind:prpdIntermlist_2){
			String key = itemKind.getId().toString();
			String value = itemKind.getIntermCode()+"-"+itemKind.getIntermName();
			dictStr_2 = dictStr_2+key+"+"+value+",";
		}
		if( !"".equals(dictStr_0)){
			dictStr_0 = dictStr_0.substring(0,dictStr_0.length()-1);
		}else{
			dictStr_0 = "+ ";
		}
		if( !"".equals(dictStr_2)){
			dictStr_2 = dictStr_2.substring(0,dictStr_2.length()-1);
		}else{
			dictStr_2 = "+ ";
		}

		mav.addObject("taskInTimeStart",startDate);
		mav.addObject("taskInTimeEnd",endDate);
		mav.addObject("dictVos",dictVos);
		mav.addObject("dictStr_0",dictStr_0);
		mav.addObject("dictStr_2",dictStr_2);
		mav.setViewName("base/assessors/AssessorsFeeQueryList");

		return mav;
	}

	/**
	 * 公估费查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified: ☆XMSH(2016年8月19日 上午11:34:40): <br>
	 */
	@RequestMapping("/assessorsQuery.do")
	@ResponseBody
	public String assessorsQuery(AssessorQueryVo queryVo) throws Exception {
		if(queryVo.getEndCaseTimeEnd()!=null){
			queryVo.setEndCaseTimeEnd(DateUtils.addDays(queryVo.getEndCaseTimeEnd(),1));
		}
		String comCode = WebUserUtils.getComCode();
		queryVo.setComCode(comCode);
		ResultPage<AssessorQueryResultVo> resultPage = assessorService.findPageForAssessor(queryVo);
		String jsonData = ResponseUtils.toDataTableJson(resultPage,"registNo","intermCode:GongGuPayCode","intermName","insureName","insureCode",
				"policyNo","lossDate"+FM_DATE_mm,"taskDetail","endCaseTime","assessorFee","veriLoss","userName","photoCount");
		logger.debug("assessorsQuery.jsonData="+jsonData);

		return jsonData;
	}

	/**
	 * 导出excel
	 * @param response
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified: ☆XMSH(2016年8月22日 下午2:24:42): <br>
	 */
	@RequestMapping("/exportExcel.do")
	@ResponseBody
	public String exportExcel(HttpServletResponse response,AssessorQueryVo queryVo) throws Exception {

		String comCode = WebUserUtils.getComCode();
		queryVo.setComCode(comCode);
		if(StringUtils.isBlank(queryVo.getIntermCode())){
			queryVo.setIntermCode("00000"); // 查询不到
		}
		List<AssessorQueryResultVo> results = assessorService.getDatas(queryVo);
		// 填充projects数据
		List<Map<String,Object>> list = assessorService.createExcelRecord(results);
		String fileDir = "ins/sino/claimcar/other/files/assessorFeeTemplate.xlsx";
		String keys[] = {"registNo","policyNo","insureName","intermCode","intermName","lossDate","payment","taskStatus","taskDetail","userName","photoCount","amount","id","taskType","assessorFee","veriLoss","isSurvey"};// map中的key
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try{
			File file = ExportExcelUtils.getExcelDemoFile(fileDir);
			ExportExcelUtils.writeNewExcel(file,"Sheet1",list,keys,CodeConstants.IsSingleAccident.YES).write(os);
		}catch(IOException e){
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		response.reset();

		String fileName = "公估费.xlsx";
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
		}catch(final IOException e){
			throw e;
		}
		finally{
			if(bis!=null) bis.close();
			if(bos!=null) bos.close();
		}

		return null;
	}

	/**
	 * 导入Excel
	 * @param file
	 * @param request
	 * @throws IOException
	 * @modified: ☆XMSH(2016年8月23日 下午4:22:29): <br>
	 */
	@RequestMapping(value = "importExcel.ajax", method = RequestMethod.POST)
	@ResponseBody
	public void importExcel(@RequestParam MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		try{
			List<List<Object>> objects = ImportExcelUtil.getBankListByExcel(file.getInputStream(),file.getOriginalFilename());

			SysUserVo userVo = WebUserUtils.getUser();
			Double flowTaskId = assessorService.applyAssessorTask(objects,userVo);

			json.put("data",flowTaskId);
			json.put("status",HttpStatus.SC_OK);
		}catch(Exception e){
			json.put("status",HttpStatus.SC_INTERNAL_SERVER_ERROR);
			json.put("msg",e.getMessage());
			e.printStackTrace();
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(json.toJSONString());
	}

	/**
	 * 公估费录入
	 * @return
	 * @modified: ☆XMSH(2016年8月17日 上午11:16:14): <br>
	 */
	@RequestMapping("/assessorsFeeEntry.do")
	public ModelAndView assessorsFeeEntry(Double taskId) {
		ModelAndView modelAndView = new ModelAndView();

		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(taskId);// 公估任务

		String taskNo = taskVo.getRegistNo();// 公估费工作流报案号存的是任务号

		PrpLAssessorMainVo assessorMainVo = assessorService.findAssessorMainVoByTaskNo(taskNo);
		// 查找关联保单号
		if(assessorMainVo!=null&&assessorMainVo.getPrpLAssessorFees()!=null&&assessorMainVo.getPrpLAssessorFees().size()>0){
			for(PrpLAssessorFeeVo fee:assessorMainVo.getPrpLAssessorFees()){
				if(StringUtils.isBlank(fee.getRemark())){
					fee.setRemark("公估费");
				}
				List<PrpLCMainVo> cmain = prpLCMainService.findPrpLCMainsByRegistNo(fee.getRegistNo());
				if(cmain!=null&&cmain.size()==2){
					if(cmain.get(0).getPolicyNo().equals(fee.getPolicyNo())){
						fee.setPolicyNoLink(cmain.get(1).getPolicyNo());
						fee.setRiskCode(cmain.get(1).getRiskCode());
					}else{
						fee.setPolicyNoLink(cmain.get(0).getPolicyNo());
						fee.setRiskCode(cmain.get(0).getRiskCode());
					}
				}
			}
		}

		PrpLClaimTextVo prpLClaimTextVo = claimTextSerVice.findClaimTextByNode(assessorMainVo.getId(),taskVo.getSubNodeCode(),"0");// 暂存时flag=0，提交flag=1
		List<PrpLClaimTextVo> prpLClaimTextVos = claimTextSerVice.findClaimTextList(assessorMainVo.getId(),assessorMainVo.getTaskId(),
				FlowNode.Interm.name());// 意见列表
		assessorMainVo.setComCode(WebUserUtils.getComCode());
		PrpdIntermMainVo intermMainVo = null;
		if(assessorMainVo.getIntermId()!=null){
			intermMainVo = managerService.findIntermById(assessorMainVo.getIntermId().toString());
		}else{
			intermMainVo = managerService.findIntermByCode(assessorMainVo.getIntermcode(),assessorMainVo.getComCode());
		}
		List<VatQueryViewVo> vatQueryViewVos= billlclaimService.findPrpLbillinfoVoByTaskNo(taskNo);
		modelAndView.addObject("vatQueryViewVos",vatQueryViewVos);
		modelAndView.addObject("taskVo",taskVo);
		modelAndView.addObject("handlerStatus",taskVo.getHandlerStatus());
		modelAndView.addObject("assessorMainVo",assessorMainVo);
		modelAndView.addObject("prpLClaimTextVo",prpLClaimTextVo);
		modelAndView.addObject("prpLClaimTextVos",prpLClaimTextVos);
		modelAndView.addObject("intermMainVo",intermMainVo);
		modelAndView.setViewName("base/assessors/AssessorsFeeEntry");
		return modelAndView;

	}

	@RequestMapping(value = "/saveAssessorFee.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveAssessorFee(@FormModel("assessorMainVo") PrpLAssessorMainVo prpLAssessorMainVo,
										@FormModel("assessorFeeVo") List<PrpLAssessorFeeVo> feeVoList,
										@FormModel("prpLClaimTextVo") PrpLClaimTextVo prpLClaimTextVo) {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			SysUserVo userVo = WebUserUtils.getUser();

			// 更新费用金额
			assessorService.updateAssessorFee(feeVoList);
			// 跟新总金额
			assessorService.updateSumAmountFee(prpLAssessorMainVo);

			// 跟踪意见
			prpLClaimTextVo.setComCode(WebUserUtils.getComCode());
			String description = codeTranService.transCode("AuditStatus","save");
			prpLClaimTextVo.setDescription(description);
			prpLClaimTextVo.setStatus("save");
			prpLClaimTextVo.setFlag("0");
			// prpLClaimTextVo.setComName(WebUserUtils.getComName());
			prpLClaimTextVo.setBigNode(FlowNode.Interm.name());
			prpLClaimTextVo.setNodeCode(FlowNode.IntermQuery.name());
			if(prpLClaimTextVo.getId()==null){// 新增
				prpLClaimTextVo.setBussTaskId(prpLAssessorMainVo.getId());
				prpLClaimTextVo.setBussNo(prpLAssessorMainVo.getTaskId());
				prpLClaimTextVo.setRegistNo(prpLAssessorMainVo.getTaskId());
				prpLClaimTextVo.setCreateUser(userVo.getUserCode());
				prpLClaimTextVo.setCreateTime(new Date());
				prpLClaimTextVo.setOperatorCode(userVo.getUserCode());
				prpLClaimTextVo.setOperatorName(userVo.getUserName());
				prpLClaimTextVo.setInputTime(new Date());
			}else{
				prpLClaimTextVo.setUpdateUser(WebUserUtils.getComCode());
				prpLClaimTextVo.setUpdateTime(new Date());
			}

			claimTextSerVice.saveOrUpdte(prpLClaimTextVo);
			ajaxResult.setData(prpLClaimTextVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}

	@RequestMapping(value = "/submitAssessorFee.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult submitAssessorFee(@FormModel("assessorMainVo") PrpLAssessorMainVo prpLAssessorMainVo,
										@FormModel("assessorFeeVo") List<PrpLAssessorFeeVo> feeVoList,
										@FormModel("prpLClaimTextVo") PrpLClaimTextVo prpLClaimTextVo,Double flowTaskId) {
		AjaxResult ajaxResult = new AjaxResult();
		try{

			PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);// 公估任务
			SysUserVo userVo = WebUserUtils.getUser();

			// 更新费用金额
			assessorService.updateAssessorFee(feeVoList);
			// 跟新总金额
			assessorService.updateSumAmountFee(prpLAssessorMainVo);
			// 跟踪意见
			prpLClaimTextVo.setComCode(WebUserUtils.getComCode());
			String description = codeTranService.transCode("AuditStatus","submit");
			prpLClaimTextVo.setDescription(description);
			prpLClaimTextVo.setStatus("submit");
			prpLClaimTextVo.setFlag("1");
			if(prpLClaimTextVo.getId()==null){// 新增
				prpLClaimTextVo.setBussTaskId(prpLAssessorMainVo.getId());
				prpLClaimTextVo.setBussNo(prpLAssessorMainVo.getTaskId());
				prpLClaimTextVo.setRegistNo(prpLAssessorMainVo.getTaskId());
				prpLClaimTextVo.setCreateUser(userVo.getUserCode());
				prpLClaimTextVo.setCreateTime(new Date());
				prpLClaimTextVo.setOperatorCode(userVo.getUserCode());
				prpLClaimTextVo.setOperatorName(userVo.getUserName());
				// prpLClaimTextVo.setComName(WebUserUtils.getComName());
				prpLClaimTextVo.setBigNode(FlowNode.Interm.name());
				prpLClaimTextVo.setNodeCode(FlowNode.IntermQuery.name());
				prpLClaimTextVo.setInputTime(new Date());
			}else{
				prpLClaimTextVo.setUpdateUser(WebUserUtils.getComCode());
				prpLClaimTextVo.setUpdateTime(new Date());
			}

			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			submitVo.setFlowId(taskVo.getFlowId());
			submitVo.setFlowTaskId(taskVo.getTaskId());
			submitVo.setComCode(userVo.getComCode());
			submitVo.setNextNode(FlowNode.IntermCheckQuery);
			submitVo.setCurrentNode(FlowNode.IntermQuery);
			submitVo.setAssignCom(WebUserUtils.getComCode());
			submitVo.setAssignUser(WebUserUtils.getUserCode());
			submitVo.setTaskInUser(userVo.getUserCode());
			submitVo.setTaskInKey(taskVo.getRegistNo());

			wfTaskHandleService.submitAssessorTask(taskVo,submitVo);
			claimTextSerVice.saveOrUpdte(prpLClaimTextVo);
			ajaxResult.setData(prpLClaimTextVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}

	@RequestMapping(value = "/submitAssessorFeeAudit.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult submitAssessorFeeAudit(@FormModel("assessorMainVo") PrpLAssessorMainVo prpLAssessorMainVo,
												@FormModel("prpLClaimTextVo") PrpLClaimTextVo prpLClaimTextVo,Double flowTaskId,String audit) {
		AjaxResult ajaxResult = new AjaxResult();
		try{

			PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);// 公估任务
			SysUserVo userVo = WebUserUtils.getUser();

			// 跟踪意见
			prpLClaimTextVo.setComCode(WebUserUtils.getComCode());
			String description = codeTranService.transCode("AuditStatus","submit");
			prpLClaimTextVo.setDescription(description);

			prpLClaimTextVo.setFlag("1");
			if(prpLClaimTextVo.getId()==null){// 新增
				prpLClaimTextVo.setBussTaskId(prpLAssessorMainVo.getId());
				prpLClaimTextVo.setBussNo(prpLAssessorMainVo.getTaskId());
				prpLClaimTextVo.setRegistNo(prpLAssessorMainVo.getTaskId());
				prpLClaimTextVo.setCreateUser(userVo.getUserCode());
				prpLClaimTextVo.setCreateTime(new Date());
				prpLClaimTextVo.setOperatorCode(userVo.getUserCode());
				prpLClaimTextVo.setOperatorName(userVo.getUserName());
				// prpLClaimTextVo.setComName(WebUserUtils.getComName());
				prpLClaimTextVo.setBigNode(FlowNode.Interm.name());
				prpLClaimTextVo.setNodeCode(FlowNode.IntermCheckQuery.name());
				prpLClaimTextVo.setInputTime(new Date());
			}else{
				prpLClaimTextVo.setUpdateUser(WebUserUtils.getComCode());
				prpLClaimTextVo.setUpdateTime(new Date());
			}

			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();

			if("submit".equals(audit)){
				submitVo.setNextNode(FlowNode.END);
				prpLClaimTextVo.setStatus("submitAudit");// 审核通过
				prpLAssessorMainVo.setUnderwriteuser(WebUserUtils.getUserCode());
				assessorService.updateUnderWriteFlag(prpLAssessorMainVo); // 写会审核通过标志
			}else if("back".equals(audit)){
				submitVo.setSubmitType(SubmitType.B);
				submitVo.setNextNode(FlowNode.IntermTaskQuery);
				prpLClaimTextVo.setStatus("backIntermFee");// 退回申请
			}

			submitVo.setHandleIdKey(prpLAssessorMainVo.getId().toString());
			submitVo.setFlowId(taskVo.getFlowId());
			submitVo.setFlowTaskId(taskVo.getTaskId());
			submitVo.setComCode(userVo.getComCode());
			submitVo.setCurrentNode(FlowNode.IntermCheckQuery);
			submitVo.setTaskInUser(userVo.getUserCode());
			submitVo.setTaskInKey(taskVo.getRegistNo());

			wfTaskHandleService.submitAssessorTask(taskVo,submitVo);
			claimTextSerVice.saveOrUpdte(prpLClaimTextVo);
			if("submit".equals(audit)){ // 接口调用//总勘费为0，不送收付，不送VAT
				if(prpLAssessorMainVo.getSumAmount().compareTo(new BigDecimal(0))!=0){
				PrpLAssessorMainVo assessorMainVo = assessorService.findAssessorMainVoByTaskNo(prpLAssessorMainVo.getTaskId());
				interfaceAsyncService.assessorToPayment(assessorMainVo);// 送收付
				//interfaceAsyncService.assessorToInvoice(assessorMainVo,userVo);
				// interfaceAsyncService.assessorToReins(assessorMainVo);
				}
			}

			ajaxResult.setData(prpLClaimTextVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}

	/**
	 * 公估任务注销
	 * @param taskId
	 * @return
	 * @modified: ☆XMSH(2016年9月28日 下午8:07:45): <br>
	 */
	@RequestMapping("/submitCancel.do")
	@ResponseBody
	public AjaxResult submitCancel(Double flowTaskId) {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			SysUserVo userVo = WebUserUtils.getUser();
			assessorService.submitCancel(flowTaskId,userVo);

			ajaxResult.setData("2");
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}

	/**
	 * 公估费审核
	 * @return
	 * @modified: ☆XMSH(2016年8月17日 上午11:16:55): <br>
	 */
	@RequestMapping("/assessorsFeeAudit.do")
	public ModelAndView assessorsFeeAudit(Double taskId) {
		ModelAndView modelAndView = new ModelAndView();

		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(taskId);// 公估任务

		String taskNo = taskVo.getRegistNo();// 公估费工作流报案号存的是任务号

		PrpLAssessorMainVo assessorMainVo = assessorService.findAssessorMainVoByTaskNo(taskNo);
		
		if(assessorMainVo!=null&&assessorMainVo.getPrpLAssessorFees()!=null&&assessorMainVo.getPrpLAssessorFees().size()>0){
			for(PrpLAssessorFeeVo fee:assessorMainVo.getPrpLAssessorFees()){
				List<PrpLCMainVo> cmain = prpLCMainService.findPrpLCMainsByRegistNo(fee.getRegistNo());
				if(cmain!=null&&cmain.size()==2){
					if(cmain.get(0).getPolicyNo().equals(fee.getPolicyNo())){
						fee.setPolicyNoLink(cmain.get(1).getPolicyNo());
						fee.setRiskCode(cmain.get(1).getRiskCode());
					}else{
						fee.setPolicyNoLink(cmain.get(0).getPolicyNo());
						fee.setRiskCode(cmain.get(0).getRiskCode());
					}
				}
			}
		}

		PrpLClaimTextVo prpLClaimTextVo = claimTextSerVice.findClaimTextByNode(assessorMainVo.getId(),taskVo.getSubNodeCode(),"0");// 暂存时flag=0，提交flag=1
		List<PrpLClaimTextVo> prpLClaimTextVos = claimTextSerVice.findClaimTextList(assessorMainVo.getId(),assessorMainVo.getTaskId(),
				FlowNode.Interm.name());// 意见列表
		assessorMainVo.setComCode(WebUserUtils.getComCode());
		// PrpdIntermMainVo intermMainVo = managerService.findIntermByCode(assessorMainVo.getIntermcode(),assessorMainVo.getComCode());
		PrpdIntermMainVo intermMainVo = null;
		if(assessorMainVo.getIntermId()!=null){
			intermMainVo = managerService.findIntermById(assessorMainVo.getIntermId().toString());
		}else{
			intermMainVo = managerService.findIntermByCode(assessorMainVo.getIntermcode(),assessorMainVo.getComCode());
		}

		modelAndView.addObject("taskVo",taskVo);
		modelAndView.addObject("handlerStatus",taskVo.getHandlerStatus());
		modelAndView.addObject("assessorMainVo",assessorMainVo);
		modelAndView.addObject("prpLClaimTextVo",prpLClaimTextVo);
		modelAndView.addObject("prpLClaimTextVos",prpLClaimTextVos);
		modelAndView.addObject("intermMainVo",intermMainVo);

		modelAndView.setViewName("base/assessors/AssessorsFeeAudit");
		return modelAndView;
	}

	@RequestMapping("/auditTakskExportExcel.do")
	@ResponseBody
	public String auditTakskExportExcel(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String taskId = request.getParameter("taskId");
		String workStatus = request.getParameter("workStatus");
		List<AssessorQueryResultVo> results = assessorService.findAssessorMainVo(taskId);
		for(AssessorQueryResultVo result:results){
			result.setWorkStatus(workStatus); // 设置案件状态
		}
		List<Map<String,Object>> list = assessorService.createExcelRecord(results);
		String fileDir = "ins/sino/claimcar/other/files/assessorFeeTask.xlsx";
		String keys[] = {"registNo","policyNo","claimNo","compensateNo","kindCode","insureName","payment","taskDetail","amount","intermName"};// map中的key
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try{
			File file = ExportExcelUtils.getExcelDemoFile(fileDir);
			ExportExcelUtils.writeNewExcel(file,"Sheet1",list,keys,CodeConstants.IsSingleAccident.NOT).write(os);
		}catch(IOException e){
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		response.reset();

		String fileName = "公估费.xlsx";
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
		}catch(final IOException e){
			throw e;
		}
		finally{
			if(bis!=null) bis.close();
			if(bos!=null) bos.close();
		}

		return null;
	}

	/**
	 * 本案件下公估费费用查看
	 * @param registNo
	 * @return
	 */
	@RequestMapping("/assessorShowView.do")
	public ModelAndView assessorShowView(String registNo) {
		ModelAndView mv = new ModelAndView();
		List<PrpLAssessorFeeVo> feeVos = new ArrayList<PrpLAssessorFeeVo>();
		List<PrpLAssessorFeeVo> listVo = assessorService.findPrpLAssessorFeeVoByRegistNo(registNo);
		if(listVo!=null&&listVo.size()>0){
			for(PrpLAssessorFeeVo vo:listVo){
				PrpLAssessorMainVo assessMainVo = assessorService.findAssessorMainVoById(vo.getAssessMainId());
				if(assessMainVo!=null){
					PrpLWfTaskVo prplwfTaskVo = wfTaskHandleService.findprplwftaskVoByNodeCodeAndflowId("Interm",assessMainVo.getTaskId());
					if("3".equals(assessMainVo.getUnderWriteFlag())){
						if(prplwfTaskVo!=null){
							vo.setTaskId(prplwfTaskVo.getTaskId());
						}
						feeVos.add(vo);
					}
				}

			}
		}
		mv.addObject("prpLAssessorFeeVos",feeVos);
		mv.setViewName("base/assessors/AssessorsFeeShowView");

		return mv;
	}

	/**
	 * 公估费退票查询
	 * @return
	 */
	@RequestMapping("/assessorFeeBackTicketList.do")
	public ModelAndView assessorFeeBackTicketQuery() {
		ModelAndView mv = new ModelAndView();
		String comCode = WebUserUtils.getComCode();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		List<PrpdIntermMainVo> prpdIntermlist = managerService.findIntermListByHql(comCode);
		List<SysCodeDictVo> dictVos = new ArrayList<SysCodeDictVo>();
		for(PrpdIntermMainVo itemKind:prpdIntermlist){
			SysCodeDictVo dict1Vo = new SysCodeDictVo();
			dict1Vo.setCodeCode(itemKind.getId()+"");
			dict1Vo.setCodeName(itemKind.getIntermCode()+"-"+itemKind.getIntermName());
			dictVos.add(dict1Vo);
		}
		mv.addObject("dictVos",dictVos);
		mv.addObject("taskInTimeStart",startDate);
		mv.addObject("taskInTimeEnd",endDate);
		mv.setViewName("base/assessors/AssessorFeeBackTicketList");
		return mv;
	}

	/**
	 * 公估费退票查询方法
	 * @param handleStatus
	 * @param queryVo
	 * @param start
	 * @param length
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/assessorFeeAccountSearch.do", method = RequestMethod.POST)
	@ResponseBody
	public String search(@RequestParam("handleStatus") String handleStatus,@FormModel("prpLWfTaskQueryVo") PrpLWfTaskQueryVo queryVo,// 页面组装VO
							@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
							@RequestParam(value = "length", defaultValue = "10") Integer length) throws ParseException {
		queryVo.setCompensateNo(queryVo.getBussNo().replaceAll("\\s*",""));// 去掉所有空格符
		queryVo.setClaimNo(queryVo.getClaimNo().replaceAll("\\s*",""));// 去掉所有空格符
		queryVo.setComCode(WebUserUtils.getComCode());
		queryVo.setUserCode(WebUserUtils.getUserCode());

		ResultPage<PrpLPayBankVo> page = assessorService.search(queryVo,start,length,handleStatus);
		String jsonData = ResponseUtils.toDataTableJson(page,"bussNo","bankName","accountNo","remark","appTime","payType:PayRefReason","accountId",
				"backaccountId","intermCode","registNo","accountName");
		return jsonData;

	}

	@RequestMapping("/assessorMoreMessageList.do")
	public ModelAndView intermMoreMessage(String bussNo,String accountId,String backaccountId,String intermCode,String registNo) {
		ModelAndView mav = new ModelAndView();
		List<PrpLAssessorFeeVo> feeLists = assessorService.findPrpLAssessorFeeVoByCompensateNoOrEndNo(bussNo);
		if(feeLists!=null&&feeLists.size()>0){
			for(PrpLAssessorFeeVo vo:feeLists){
				vo.setCreateUser("公估费");
			}
		}
		PrpDAccRollBackAccountVo accountVo = accountQueryService.findRollBackAccountById(Long.valueOf(backaccountId));
		if(accountVo!=null&&StringUtils.isNotBlank(accountVo.getRollbackCode())){
			String userName = codeTranService.transCode("UserCode",accountVo.getRollbackCode());
			accountVo.setRollbackCode(userName);
		}
		String requstime = "";
		if(accountVo!=null&&accountVo.getRollBackTime()!=null){
			Date rollBackTime = accountVo.getRollBackTime();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			requstime = format.format(rollBackTime);
		}
		String auditOpinionSign = "0";// 审核意见是否显示，如果是在退票审核中退回的，就显示，否则就不显示
		// 如果是退票审核退回的，则页面展示退票审核意见
		PrplInterrmAuditVo auditVo = new PrplInterrmAuditVo();
		if(accountVo!=null&&"3".equals(accountVo.getIsHaveAudit())){
			auditVo = assessorService.findPrplInterrmAuditVoById(accountVo.getAuditId());
			auditOpinionSign = "1";
		}
		mav.addObject("auditOpinion",auditVo.getAuditOpinion());// 审核意见
		mav.addObject("updateUser",auditVo.getUpdateUser());// 审核人员
		mav.addObject("updateTime",auditVo.getUpdateTime());// 审核时间
		mav.addObject("auditOpinionSign",auditOpinionSign);
		mav.addObject("registNo",registNo);
		mav.addObject("intermCode",intermCode);
		mav.addObject("requstime",requstime);
		mav.addObject("prpDAccRollBackAccountVo",accountVo);
		mav.addObject("accountId",accountId);// 银行账户信息表Id
		mav.addObject("assessorFeeList",feeLists);
		mav.setViewName("base/assessors/AssessorInfoMain");
		return mav;
	}

	@RequestMapping("/assessorBankInfoQuery.do")
	public ModelAndView assessorBankInfoList(String accountId,String errorType,String certiNo,String backaccountId,String intermCode,String registNo) {
		ModelAndView mav = new ModelAndView();
		PrpdIntermBankVo interBankVo = managerService.findPrpdIntermBankVoById(Long.valueOf(accountId));
		String oldAccountName = interBankVo.getAccountName();// 旧的收款人户名，传入页面与修改的收款人户名进行比对，如果一样，则不走退票审核，否则要走退票审核；
		PrpdIntermBankVo bankVo = new PrpdIntermBankVo();

		PrplInterrmAuditVo interrmAuditVo = assessorService.findPrplInterrmAuditVoByBackAccountId(Long.valueOf(backaccountId));
		PrpDAccRollBackAccountVo backAccountVo = accountQueryService.findRollBackAccountById(Long.valueOf(backaccountId));
		// InfoFlag==o则取审核表的数据，否则就取银行表的信息
		if("0".equals(backAccountVo.getInfoFlag())){
			setParamsForPrpdIntermBank(bankVo,interrmAuditVo);
			bankVo.setId(interrmAuditVo.getOldBankId());
		}else{
			PrpdIntermMainVo intermMainVo = managerService.findIntermById(interBankVo.getIntermMianId());
			bankVo = intermMainVo.getPrpdIntermBank();
			List<PrpLAssessorFeeVo> feeLists = assessorService.findPrpLAssessorFeeVoByCompensateNoOrEndNo(certiNo);
			if(feeLists!=null&&feeLists.size()>0){
				for(PrpLAssessorFeeVo vo:feeLists){
					if(StringUtils.isNotBlank(vo.getRemark())){
						bankVo.setRemark(vo.getRemark());
						break;
					}
				}
			}
		}

		// 将查出的数据放入Map中去
		Map<String,String> bankCodeMap = new HashMap<String,String>();
		List<PrplOldaccbankCodeVo> listVo = payCustomService.findPrplOldaccbankCodeByFlag("1");
		if(listVo!=null&&listVo.size()>0){
			for(PrplOldaccbankCodeVo vo:listVo){
				bankCodeMap.put(vo.getBankCode(),vo.getBankName());
			}
		}

		mav.addObject("prpDAccRollBackAccountVo",backAccountVo);
		mav.addObject("registNo",registNo);
		mav.addObject("backaccontId",backaccountId);
		mav.addObject("errorType",errorType);
		mav.addObject("bankCodeMap",bankCodeMap);
		mav.addObject("intermCode",intermCode);
		if("88".equals(errorType)){// 88此标志区分展示哪个页面
			mav.setViewName("base/assessors/AssessorBankInfoView");
			mav.addObject("prpdIntermBank",bankVo);
		}else{
			// 如果是在退票审核中被退回的，则银行信息修改页面展示被退回时的银行信息--AuditFlag=0为被公估费退票审核退回时还没有被送收付，
			if(backAccountVo!=null&&"3".equals(backAccountVo.getIsHaveAudit())&&"0".equals(backAccountVo.getAuditFlag())){
				PrpdIntermBankVo bankVo1 = new PrpdIntermBankVo();
				PrplInterrmAuditVo auditVo = assessorService.findPrplInterrmAuditVoById(backAccountVo.getAuditId());
				setParamsForPrpdIntermBank(bankVo1,auditVo);
				bankVo1.setId(auditVo.getOldBankId());// 旧的银行表数据Id
				mav.addObject("prpdIntermBank",bankVo1);
				mav.addObject("oldAccountName",oldAccountName);
			}else{
				// 不是退票审核退回的就显示原来的银行信息
				mav.addObject("prpdIntermBank",bankVo);
				mav.addObject("oldAccountName",oldAccountName);
			}
			mav.setViewName("base/assessors/AssessorBankInfoEdit");
		}
		return mav;
	}

	/**
	 * 银行信息检索
	 * @return
	 */
	@RequestMapping(value = "/findBankNoQueryList.do")
	public ModelAndView findBankNoInit() {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("manager/IntermediaryEdit_BankNumQueryList");
		return mv;
	}

	/**
	 * 公估费退票送收付 isNeedAudit 是否需要走退票审核0-不需要，1-需要
	 * @return
	 */
	@RequestMapping("/sendAssAccountToPayment.do")
	@ResponseBody
	public AjaxResult sendAssAccountToPayment(@RequestParam("registNo") String registNo,@FormModel("prpdIntermBankVo") PrpdIntermBankVo intermbankVo,
												@RequestParam("backaccontId") String backaccontId,@RequestParam("bankId") String bankId,
												@RequestParam("intermCode") String intermCode,@RequestParam("isSendMoney") String isSendMoney,
												@RequestParam("isNeedAudit") String isNeedAudit) {
		AjaxResult ajax = new AjaxResult();
		SysUserVo userVo = WebUserUtils.getUser();
		String index = "0";
		PrpDAccRollBackAccountVo backAccountVo = accountQueryService.findRollBackAccountById(Long.valueOf(backaccontId));
		try{
			if("1".equals(isNeedAudit)){
				PrplInterrmAuditVo auditVo = new PrplInterrmAuditVo();
				auditVo.setBackAccountId(Long.valueOf(backaccontId));
				auditVo.setOldBankId(Long.valueOf(bankId));
				auditVo.setCreateTime(new Date());
				auditVo.setCreateUser(userVo.getUserCode());
				auditVo.setComCode(userVo.getComCode());
				auditVo.setRegistNo(registNo);
				auditVo.setIntermCode(intermCode);
				auditVo.setStatus("0");
				auditVo.setBussNo(backAccountVo.getCertiNo());// 可能为结算单号，也可能为计算书号
				auditVo.setIsautoAudit("1");
				auditVo.setIsAutoPay(isSendMoney);// 是否送资金或送收付标志
				setParamsForPrplInterrmAudit(intermbankVo,auditVo);

				assessorService.updateOrSaveOfPrplInterrmAudit(auditVo);
				// 数据提交的审核，则原来的退票表状态变为已处理
				assessorService.updateStatusOfPrpdaccrollbackaccount("0",Long.valueOf(backaccontId));
				assessorService.updateInfoFlagOfPrpdaccrollbackaccount("0",Long.valueOf(backaccontId));
				ajax.setStatus(HttpStatus.SC_OK);
				ajax.setStatusText("操作成功！已提交至退票审核，请通知审核岗处理！");
			}else{

				backAccountVo.setOldAccountId(isSendMoney);
				if(StringUtils.isBlank(registNo)&&backAccountVo!=null){
					registNo = backAccountVo.getCertiNo();
				}
				PrplInterrmAuditVo auditVo = new PrplInterrmAuditVo();
				auditVo.setBackAccountId(Long.valueOf(backaccontId));
				auditVo.setOldBankId(Long.valueOf(bankId));
				auditVo.setCreateTime(new Date());
				auditVo.setCreateUser(userVo.getUserCode());
				auditVo.setComCode(userVo.getComCode());
				auditVo.setRegistNo(registNo);
				auditVo.setIntermCode(intermCode);
				auditVo.setStatus("1");
				auditVo.setIsautoAudit("0");
				auditVo.setBussNo(backAccountVo.getCertiNo());// 可能为结算单号，也可能为计算书号
				auditVo.setIsAutoPay(isSendMoney);
				setParamsForPrplInterrmAudit(intermbankVo,auditVo);
				String str = accountInfoService.sendAssessFeeAccountToNewPayment(intermbankVo,userVo,registNo,backAccountVo,bankId,intermCode,auditVo);
				index = "1";
				ajax.setStatus(HttpStatus.SC_OK);
				ajax.setData(index);
				ajax.setStatusText("操作成功！且自动审核完成！");
			}

		}catch(Exception e){
			ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajax.setStatusText("操作失败！"+e.getMessage());
			e.printStackTrace();

		}
		return ajax;
	}

	/**
	 * 公估费退票审核
	 * @return
	 */
	@RequestMapping("/assessorFeeBackTicketAuditList.do")
	@ResponseBody
	public ModelAndView assessorFeeBackTicketAuditList() {
		ModelAndView mav = new ModelAndView();
		String comCode = WebUserUtils.getComCode();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		List<PrpdIntermMainVo> prpdIntermlist = managerService.findIntermListByHql(comCode);
		List<SysCodeDictVo> dictVos = new ArrayList<SysCodeDictVo>();
		for(PrpdIntermMainVo itemKind:prpdIntermlist){
			SysCodeDictVo dict1Vo = new SysCodeDictVo();
			dict1Vo.setCodeCode(itemKind.getId()+"");
			dict1Vo.setCodeName(itemKind.getIntermCode()+"-"+itemKind.getIntermName());
			dictVos.add(dict1Vo);
		}
		mav.addObject("dictVos",dictVos);
		mav.addObject("taskInTimeStart",startDate);
		mav.addObject("taskInTimeEnd",endDate);
		mav.setViewName("base/assessors/AssessorFeeBackTicketAuditList");

		return mav;
	}

	/**
	 * 公估费退票审核查询方法
	 * @param handleStatus
	 * @param queryVo
	 * @param start
	 * @param length
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/assessorFeeAccountAuditSearch.do")
	@ResponseBody
	public String assessorFeeAccountAuditSearch(@RequestParam("handleStatus") String handleStatus,
												@FormModel("prpLWfTaskQueryVo") PrpLWfTaskQueryVo queryVo,// 页面组装VO
												@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
												@RequestParam(value = "length", defaultValue = "10") Integer length) throws ParseException {
		queryVo.setCompensateNo(queryVo.getBussNo().replaceAll("\\s*",""));// 去掉所有空格符
		queryVo.setClaimNo(queryVo.getClaimNo().replaceAll("\\s*",""));// 去掉所有空格符
		queryVo.setComCode(WebUserUtils.getComCode());
		queryVo.setUserCode(WebUserUtils.getUserCode());

		ResultPage<PrpLPayBankVo> page = assessorService.assessorAuditSearch(queryVo,start,length,handleStatus);
		String jsonData = ResponseUtils.toDataTableJson(page,"bussNo","bankName","accountNo","remark","appTime","payType:PayRefReason","accountId",
				"backaccountId","intermCode","registNo","accountName","auditId");
		return jsonData;

	}

	/**
	 * 公估费退票审核更多信息
	 * @param bussNo
	 * @param accountId
	 * @param backaccountId
	 * @param intermCode
	 * @param registNo
	 * @return
	 */
	@RequestMapping("/assessorAuditMoreMessageList.do")
	public ModelAndView intermAuditMoreMessage(String bussNo,String accountId,String backaccountId,String intermCode,String registNo,String auditId) {
		ModelAndView mav = new ModelAndView();

		List<PrpLAssessorFeeVo> feeLists = assessorService.findPrpLAssessorFeeVoByCompensateNoOrEndNo(bussNo);
		if(feeLists!=null&&feeLists.size()>0){
			for(PrpLAssessorFeeVo vo:feeLists){
				vo.setCreateUser("公估费");
			}
		}
		PrpDAccRollBackAccountVo accountVo = accountQueryService.findRollBackAccountById(Long.valueOf(backaccountId));
		if(accountVo!=null&&StringUtils.isNotBlank(accountVo.getRollbackCode())){
			String userName = codeTranService.transCode("UserCode",accountVo.getRollbackCode());
			accountVo.setRollbackCode(userName);
		}
		String requstime = "";
		if(accountVo!=null&&accountVo.getRollBackTime()!=null){
			Date rollBackTime = accountVo.getRollBackTime();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			requstime = format.format(rollBackTime);
		}
		PrplInterrmAuditVo auditVo = assessorService.findPrplInterrmAuditVoById(Long.valueOf(auditId));
		String intermname = "";// 公估机构三级名称
		if(StringUtils.isNotBlank(intermCode)&& !intermCode.startsWith("0")){
			PrpdIntermMainVo itermMainVo = managerService.findIntermById(intermCode);
			if(itermMainVo!=null&&itermMainVo.getId()!=null){
				intermname = itermMainVo.getIntermName();
			}
		}else{
			intermname = intermCode;
		}

		mav.addObject("auditId",auditId);
		mav.addObject("prplInterrmAuditVo",auditVo);
		mav.addObject("registNo",registNo);
		mav.addObject("intermname",intermname);
		mav.addObject("requstime",requstime);
		mav.addObject("prpDAccRollBackAccountVo",accountVo);
		mav.addObject("accountId",accountId);// 银行账户信息表Id
		mav.addObject("assessorFeeList",feeLists);
		mav.setViewName("base/assessors/AssessorAuditInfoMain");
		return mav;
	}

	/**
	 * 公估费退票审核查询
	 * @param auditId
	 * @return
	 */
	@RequestMapping("/assessorAuditBankInfoQuery.do")
	public ModelAndView assessorAuditBankInfoList(String auditId) {
		ModelAndView mav = new ModelAndView();
		PrpdIntermBankVo bankVo = new PrpdIntermBankVo();
		PrplInterrmAuditVo auditVo = assessorService.findPrplInterrmAuditVoById(Long.valueOf(auditId));

		// 将查出的数据放入Map中去
		Map<String,String> bankCodeMap = new HashMap<String,String>();
		List<PrplOldaccbankCodeVo> listVo = payCustomService.findPrplOldaccbankCodeByFlag("1");
		if(listVo!=null&&listVo.size()>0){
			for(PrplOldaccbankCodeVo vo:listVo){
				bankCodeMap.put(vo.getBankCode(),vo.getBankName());
			}
		}
		if(auditVo!=null){
			setParamsForPrpdIntermBank(bankVo,auditVo);
		}

		mav.addObject("bankCodeMap",bankCodeMap);
		mav.addObject("prpdIntermBank",bankVo);
		mav.setViewName("base/assessors/AssessorBankInfoView");

		return mav;
	}

	/**
	 * 公估费退票审核的0-暂存，1-审核通过，2-退回
	 * @param auditId
	 * @param auditOpinion
	 * @return
	 */
	@RequestMapping("/auditWays.do")
	@ResponseBody
	public AjaxResult auditWays(@RequestParam("auditId") String auditId,@RequestParam("auditOpioion") String contexts,
								@RequestParam("sign") String sign) {
		AjaxResult ajax = new AjaxResult();
		try{
			SysUserVo userVo = WebUserUtils.getUser();
			if(StringUtils.isBlank(auditId)){
				throw new IllegalArgumentException("传入的公估费退票审核表的Id为空！");
			}
			PrplInterrmAuditVo auditVo = assessorService.findPrplInterrmAuditVoById(Long.valueOf(auditId));

			if("0".equals(sign)){
				// 退票审核暂存时，保存审核意见
				auditVo.setAuditOpinion(contexts);
				auditVo.setUpdateUser(userVo.getUserCode());
				auditVo.setUpdateTime(new Date());
				assessorService.updateOrSaveOfPrplInterrmAudit(auditVo);
				ajax.setStatus(HttpStatus.SC_OK);
				ajax.setStatusText("暂存成功！");
			}else if("1".equals(sign)){
				// 退票审核通过直接送收付
				PrpDAccRollBackAccountVo backAccountVo = accountQueryService.findRollBackAccountById(auditVo.getBackAccountId());
				backAccountVo.setOldAccountId(auditVo.getIsAutoPay());
				String registNo = auditVo.getRegistNo();
				PrpdIntermBankVo intermbankVo = new PrpdIntermBankVo();
				setParamsForPrpdIntermBank(intermbankVo,auditVo);
				// 回写公估费退票审核表
				auditVo.setAuditOpinion(contexts);
				auditVo.setStatus("1");
				auditVo.setUpdateUser(userVo.getUserCode());
				auditVo.setUpdateTime(new Date());
				auditVo.setIsautoAudit("1");
				String str = accountInfoService.sendAssessFeeAccountToNewPayment(intermbankVo,userVo,registNo,backAccountVo,
						String.valueOf(auditVo.getOldBankId()),auditVo.getIntermCode(),auditVo);
				ajax.setStatus(HttpStatus.SC_OK);
				ajax.setStatusText("操作成功！");
			}else if("2".equals(sign)){
				auditVo.setStatus("2");
				auditVo.setAuditOpinion(contexts);
				auditVo.setUpdateUser(userVo.getUserCode());
				auditVo.setUpdateTime(new Date());
				assessorService.updateOrSaveOfPrplInterrmAudit(auditVo);
				assessorService.updateAuditIdAndIsHaveAuditAndAuditFlagOfPrpdaccrollbackaccount(auditVo.getBackAccountId(),Long.valueOf(auditId),"3",
						"0");
				assessorService.updateStatusOfPrpdaccrollbackaccount("2",auditVo.getBackAccountId());
				ajax.setStatus(HttpStatus.SC_OK);
				ajax.setStatusText("已退回到公估费退票修改");
			}
		}catch(Exception e){

			ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajax.setStatusText("操作失败！"+e.getMessage());
			e.printStackTrace();
		}
		return ajax;

	}

	/**
	 * 给公估费银行表Vo赋值
	 * @param intermbankVo
	 * @param auditVo
	 */
	public void setParamsForPrpdIntermBank(PrpdIntermBankVo intermbankVo,PrplInterrmAuditVo auditVo) {
		intermbankVo.setAccountName(auditVo.getAccountName());
		intermbankVo.setAccountNo(auditVo.getAccountCode());
		intermbankVo.setCertifyNo(auditVo.getCertifyNo());
		intermbankVo.setMobile(auditVo.getMobile());
		intermbankVo.setPublicAndPrivate(auditVo.getPublicAndprivate());
		intermbankVo.setCity(auditVo.getCity());
		intermbankVo.setBankName(auditVo.getBankName());
		intermbankVo.setBankOutlets(auditVo.getBankoutLets());
		intermbankVo.setBankNumber(auditVo.getBankNumber());
		intermbankVo.setRemark(auditVo.getRemark());

	}

	/**
	 * 给公估费审核表赋值
	 * @param intermbankVo
	 * @param auditVo
	 */
	public void setParamsForPrplInterrmAudit(PrpdIntermBankVo intermbankVo,PrplInterrmAuditVo auditVo) {
		auditVo.setAccountName(intermbankVo.getAccountName());
		auditVo.setAccountCode(intermbankVo.getAccountNo());
		auditVo.setCertifyNo(intermbankVo.getCertifyNo());
		auditVo.setMobile(intermbankVo.getMobile());
		auditVo.setPublicAndprivate(intermbankVo.getPublicAndPrivate());
		auditVo.setCity(intermbankVo.getCity());
		auditVo.setBankName(intermbankVo.getBankName());
		auditVo.setBankoutLets(intermbankVo.getBankOutlets());
		auditVo.setBankNumber(intermbankVo.getBankNumber());
		auditVo.setRemark(intermbankVo.getRemark());
	}	

     public Integer findPhotoCount(String bussNo){
         String imgDataUrl = SpringProperties.getProperty("IMG_MANAGER_URL");
         ImgRemoteDataAction imgAction = new ImgRemoteDataAction(imgDataUrl);
         int photoCount = 0;
        try{
            Map<String,Integer> imgMap = imgAction.getUploadCount(bussNo);
            if(imgMap != null && !imgMap.isEmpty()){
                for(Map.Entry<String,Integer> mapValue :imgMap.entrySet() ){
                    photoCount += mapValue.getValue();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            return photoCount;
        }
         return photoCount;
     }
     
     /**
      * 公估费发票登记
      * @return
      */
     @RequestMapping("/assFeeRegister.do")
     public ModelAndView assFeeRegister(String taskNo,String billId,String interCodeId){
    	 ModelAndView mvc=new ModelAndView();
    	 PrpLAssessorMainVo assessorMainVo = assessorService.findAssessorMainVoByTaskNo(taskNo);
    	 PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoById(Long.valueOf(billId));//发票信息表
 		// 查找关联保单号
 		if(assessorMainVo!=null&&assessorMainVo.getPrpLAssessorFees()!=null && assessorMainVo.getPrpLAssessorFees().size()>0){
 			for(PrpLAssessorFeeVo fee:assessorMainVo.getPrpLAssessorFees()){
 				if(StringUtils.isBlank(fee.getRemark())){
 					fee.setRemark("公估费");
 				}
 				List<PrpLCMainVo> cmain = prpLCMainService.findPrpLCMainsByRegistNo(fee.getRegistNo());
 				if(cmain!=null&&cmain.size()==2){
 					if(cmain.get(0).getPolicyNo().equals(fee.getPolicyNo())){
 						fee.setPolicyNoLink(cmain.get(1).getPolicyNo());
 					}else{
 						fee.setPolicyNoLink(cmain.get(0).getPolicyNo());
 					}
 				}
 			}
 		}
 		List<PrpLAssessorFeeVo> assFeeVos=new ArrayList<PrpLAssessorFeeVo>();
 		PrpLConfigValueVo configValuesingVo = ConfigUtil.findConfigByCode(CodeConstants.newBillDate);
 		//以上线时间为分界点，后面审核通过的数据，才允许绑定发票
 		if(assessorMainVo!=null && assessorMainVo.getUnderWriteDate()!=null && DateBillString(configValuesingVo.getConfigValue())!=null && DateBillString(configValuesingVo.getConfigValue()).getTime()<=assessorMainVo.getUnderWriteDate().getTime()) {
 			if(assessorMainVo!=null&&assessorMainVo.getPrpLAssessorFees()!=null && assessorMainVo.getPrpLAssessorFees().size()>0){
 	 			for(PrpLAssessorFeeVo fee:assessorMainVo.getPrpLAssessorFees()){
 	 				if(StringUtils.isBlank(fee.getLinkBillNo())){
 	 					assFeeVos.add(fee);
 	 				}
 	 			}
 	 		}
 		}
 		VatQueryViewVo vatQueryViewVo=new VatQueryViewVo();
 		vatQueryViewVo.setBillNo(prpLbillinfoVo.getBillNo());
 		vatQueryViewVo.setBillCode(prpLbillinfoVo.getBillCode());
 		vatQueryViewVo.setBillId(prpLbillinfoVo.getId());
 		vatQueryViewVo.setBillDate(prpLbillinfoVo.getBillDate());
 		vatQueryViewVo.setBillSnum(prpLbillinfoVo.getBillSnum());
 		vatQueryViewVo.setBillSlName(percentChage(prpLbillinfoVo.getBillSl()));
 		vatQueryViewVo.setBillNum(prpLbillinfoVo.getBillNum());
 		vatQueryViewVo.setBillNnum(prpLbillinfoVo.getBillNnum());
 		vatQueryViewVo.setSaleName(prpLbillinfoVo.getSaleName());
 		vatQueryViewVo.setSaleNo(prpLbillinfoVo.getSaleNo());
 		vatQueryViewVo.setRegisterStatus(prpLbillinfoVo.getRegisterStatus());
 		if("1".equals(prpLbillinfoVo.getRegisterStatus())){
 			vatQueryViewVo.setRegisterNum(prpLbillinfoVo.getBillNum());
 		}
 		List<PrpLAssessorFeeVo> assfeeList=billlclaimService.findPrpLAssessorFeeBylinkBillNo(prpLbillinfoVo.getBillNo()+"_"+prpLbillinfoVo.getBillCode());
 	    // 查找关联保单号
		if(assfeeList!=null && assfeeList.size()>0){
			for(PrpLAssessorFeeVo fee:assfeeList){
				if(StringUtils.isBlank(fee.getRemark())){
					fee.setRemark("公估费");
				}
				List<PrpLCMainVo> cmain = prpLCMainService.findPrpLCMainsByRegistNo(fee.getRegistNo());
				if(cmain!=null&&cmain.size()==2){
					if(cmain.get(0).getPolicyNo().equals(fee.getPolicyNo())){
						fee.setPolicyNoLink(cmain.get(1).getPolicyNo());
					}else{
						fee.setPolicyNoLink(cmain.get(0).getPolicyNo());
					}
				}
			}
		}
 		mvc.addObject("vatQueryViewVo",vatQueryViewVo);
 		mvc.addObject("assFeeVos",assFeeVos);
 		mvc.addObject("assfeeList",assfeeList);//发票绑定的计算书集合
 		mvc.addObject("interCodeId",interCodeId);
 		mvc.addObject("taskNo",taskNo);//任务号
 		mvc.setViewName("base/assessors/AssfeeRegister");
    	return mvc;
     }
     
     /**
      * 公估费发票信息展示
      * @return
      */
     @RequestMapping("/assFeebillView.do")
     public ModelAndView assFeebillView(String billId){
    	 ModelAndView mvc=new ModelAndView();
    	 PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoById(Long.valueOf(billId));//发票信息表
 		VatQueryViewVo vatQueryViewVo=new VatQueryViewVo();
 		vatQueryViewVo.setBillNo(prpLbillinfoVo.getBillNo());
 		vatQueryViewVo.setBillCode(prpLbillinfoVo.getBillCode());
 		vatQueryViewVo.setBillId(prpLbillinfoVo.getId());
 		vatQueryViewVo.setBillDate(prpLbillinfoVo.getBillDate());
 		vatQueryViewVo.setBillSnum(prpLbillinfoVo.getBillSnum());
 		vatQueryViewVo.setBillSlName(percentChage(prpLbillinfoVo.getBillSl()));
 		vatQueryViewVo.setBillNum(prpLbillinfoVo.getBillNum());
 		vatQueryViewVo.setBillNnum(prpLbillinfoVo.getBillNnum());
 		vatQueryViewVo.setSaleName(prpLbillinfoVo.getSaleName());
 		vatQueryViewVo.setSaleNo(prpLbillinfoVo.getSaleNo());
 		vatQueryViewVo.setRegisterStatus(prpLbillinfoVo.getRegisterStatus());
 		vatQueryViewVo.setSendStatus(prpLbillinfoVo.getSendstatus());
 		if("1".equals(prpLbillinfoVo.getSendstatus())){
 			vatQueryViewVo.setSendStatusName("已推送");
 		}else{
 			vatQueryViewVo.setSendStatusName("未推送");
 		}
 		vatQueryViewVo.setBackFlag(prpLbillinfoVo.getBackFlag());//打回标志
 		if("1".equals(prpLbillinfoVo.getBackFlag())){
 			vatQueryViewVo.setBackCauseInfo(prpLbillinfoVo.getBackCauseinfo());
 		}
 		vatQueryViewVo.setVidflag("1");
 		vatQueryViewVo.setVidflagName("成功");
 		List<PrpLAssessorFeeVo> assfeeList=billlclaimService.findPrpLAssessorFeeBylinkBillNo(prpLbillinfoVo.getBillNo()+"_"+prpLbillinfoVo.getBillCode());
 	    // 查找关联保单号
		if(assfeeList!=null && assfeeList.size()>0){
			for(PrpLAssessorFeeVo fee:assfeeList){
				if(StringUtils.isBlank(fee.getRemark())){
					fee.setRemark("公估费");
				}
				List<PrpLCMainVo> cmain = prpLCMainService.findPrpLCMainsByRegistNo(fee.getRegistNo());
				if(cmain!=null&&cmain.size()==2){
					if(cmain.get(0).getPolicyNo().equals(fee.getPolicyNo())){
						fee.setPolicyNoLink(cmain.get(1).getPolicyNo());
					}else{
						fee.setPolicyNoLink(cmain.get(0).getPolicyNo());
					}
				}
			}
		}
 		mvc.addObject("vatQueryViewVo",vatQueryViewVo);
 		mvc.addObject("assfeeList",assfeeList);//发票绑定的计算书集合
 		mvc.setViewName("base/assessors/assFeebillView");
    	return mvc;
     }
     
     
     /**
 	 * 转换为百分比
 	 * @param snum
 	 * @return
 	 */
 	private String percentChage(BigDecimal snum) {
 		String strPercent="";
 		if(snum==null){
 			return strPercent;
 		}else{
 			NumberFormat percent = NumberFormat.getPercentInstance();
 			percent.setMaximumFractionDigits(2);
 			strPercent=percent.format(snum.doubleValue());
 		}
 		
 		return strPercent;
 	}
 	
 	 /**
 		 * 时间转换方法
 		 * Date 类型转换 String类型
 		 * @param strDate
 		 * @return
 		 * @throws ParseException2020-05-27 14:45:02
 		 */
 		private static Date DateBillString(String strDate){
 			Date date=null;
 			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 			  if(strDate!=null){
 				  try {
 					date=format.parse(strDate);
 				} catch (Exception e) {
 					e.printStackTrace();
 				}
 			}
 			return date;
 		}
}

