/******************************************************************************
* CREATETIME : 2019年7月30日 上午9:42:33
******************************************************************************/
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
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.manager.vo.PrpdcheckBankVo;
import ins.sino.claimcar.manager.vo.PrplOldaccbankCodeVo;
import ins.sino.claimcar.other.service.AccountInfoService;
import ins.sino.claimcar.other.service.AccountQueryService;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.vo.CheckQueryResultVo;
import ins.sino.claimcar.other.vo.CheckQueryVo;
import ins.sino.claimcar.other.vo.PrpDAccRollBackAccountVo;
import ins.sino.claimcar.other.vo.PrpLAcheckMainVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckmAuditVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;


/**
 * <pre></pre>
 * @author ★XHY
 */
@Controller
@RequestMapping("/checkfee")
public class CheckFeeManageAction {
	private static Logger logger = LoggerFactory.getLogger(CheckFeeManageAction.class);
	// 常用格式定义
	private static final String FM_DATE_mm = "#yyyy-MM-dd HH:mm";
	@Autowired
	private AcheckService acheckService;
	@Autowired
	private ManagerService managerService;

	
	@Autowired
	private AccountQueryService accountQueryService;
	
	@Autowired
	private CodeTranService codeTranService;
	
	
	@Autowired
	private PayCustomService payCustomService; 
	
	
	@Autowired
	private AccountInfoService accountInfoService;
	@Autowired
	private WfTaskQueryService wfTaskQueryService;
	
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	ClaimTextService claimTextSerVice;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	
	@Autowired 
	private SysUserService sysUserService;
	@Autowired
	private BilllclaimService billlclaimService;
	

	/**
	 * 进入查勘费补录页面
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月7日 上午11:01:45): <br>
	 */
     @RequestMapping("/checkFeeSupplementList")
	 public ModelAndView  checkFeeSupplement(){
    	 ModelAndView modelAndView = new ModelAndView();
    	 Date endDate = new Date();
 		 Date startDate = DateUtils.addDays(endDate, -15);
 		 modelAndView.addObject("taskInTimeStart",startDate);
 		 modelAndView.addObject("taskInTimeEnd",endDate);
    	 modelAndView.setViewName("base/checkagency/CheckFeeSupplyList");
    	 return modelAndView;
    }
     
    /**
      *	查询查勘费补录列表 
      * <pre></pre>
      * @param queryVo
      * @param start
      * @param length
      * @return
      * @throws ParseException
      * @modified:
      * ☆XiaoHuYao(2019年8月7日 上午11:20:37): <br>
      */
    @RequestMapping("/chFSupplymentSerach")
 	@ResponseBody
 	public String chFSupplymentSerach(@FormModel("queryVo") CheckQueryVo queryVo,// 页面组装VO
 							@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
 							@RequestParam(value = "length", defaultValue = "10") Integer length) throws ParseException {
 		queryVo.setHandlerCode(queryVo.getHandlerCode().replaceAll("\\s*",""));// 去掉所有空格符
 		queryVo.setStart(start);
 		queryVo.setLength(length);
 		ResultPage<CheckQueryResultVo> resultPage;
 		String jsonData = null;
 		//先判断是否是公估人员再做查询
		if(managerService.findCheckByUserCode(queryVo.getHandlerCode()) == null){
			throw new IllegalArgumentException("此工号不是查勘人员");
		}
		try{
			resultPage = acheckService.findSupplymentPageForCheckFee(queryVo);
			jsonData = ResponseUtils.toDataTableJson(resultPage,"registNo","policyNo","insureName"
					,"lossDate"+FM_DATE_mm,"taskDetail","userName","mainId","taskType");
			logger.info("chFSupplymentSerach.jsonData="+jsonData);
		}catch(Exception e){
			logger.error("查询公估费补录列表失败",e);
		}
 		return jsonData;

 	}
    /**
     * 
     * 查勘费补录保存
     * @param taskType
     * @param mainId
     * @return
     * @modified:
     * ☆XiaoHuYao(2019年8月13日 下午6:28:59): <br>
     */
	@RequestMapping("/supplementCheckFee")
	@ResponseBody
	public AjaxResult supplementCheckFee(String taskType,Long mainId,String registNo,String userCode){
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo userVo = sysUserService.findByUserCode(userCode);
		try{
			//财产追加和车辆追加考虑之前是否生成了查勘费
			if(managerService.findExistACheck(taskType,mainId,registNo,userCode)){
				throw new IllegalArgumentException("该查勘费任务已经生成，不能重复生成");
			}
			managerService.saveCheckFee(taskType,mainId,registNo,userVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setStatusText("操作成功！");
		}catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			logger.error("查勘费补录失败",e);
		}
		return ajaxResult;
	}
	/**
	 * 进入查勘费查询界面
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年7月30日 上午9:46:44): <br>
	 */
	@RequestMapping("/checkFeeQueryList.do")
	public ModelAndView checkFeeCheck() {
		ModelAndView mav = new ModelAndView();
		String comCode = WebUserUtils.getComCode();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		List<PrpdCheckBankMainVo> prpdChecklist=  managerService.findCheckListByHql(comCode);
		List<SysCodeDictVo> dictVos = new ArrayList<SysCodeDictVo>();

		if(prpdChecklist!=null && prpdChecklist.size()>0){
			for(PrpdCheckBankMainVo checkKind:prpdChecklist){
				SysCodeDictVo dict1Vo = new SysCodeDictVo();
				dict1Vo.setCodeCode(checkKind.getCheckCode()+"");
				dict1Vo.setCodeName(checkKind.getCheckCode()+"-"+checkKind.getCheckName());
				dictVos.add(dict1Vo);
			}
		}else{
			SysCodeDictVo dict1Vo = new SysCodeDictVo();
			dict1Vo.setCodeCode("");
			dict1Vo.setCodeName(" ");
			dictVos.add(dict1Vo);
		}

		mav.addObject("taskInTimeStart",startDate);
		mav.addObject("taskInTimeEnd",endDate);
		mav.addObject("dictVos",dictVos);
		mav.setViewName("base/checkagency/checkFeeQueryList");
		return mav;
	}
	
	/**
	 * 进入查勘费任务查询界面
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆yzy(2019年7月30日 上午9:46:44): <br>
	 */
	@RequestMapping(value="/checkFeeTaskQueryList.do", method = RequestMethod.GET)
	public ModelAndView checkFeeTaskCheckQuery(@RequestParam(value = "node") String nodeCode,String subNode) {
		String comCode = WebUserUtils .getComCode();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		ModelAndView mav = new ModelAndView();
		mav.addObject("nodeCode", nodeCode);
		mav.addObject("taskInTimeStart", startDate);
		mav.addObject("taskInTimeEnd", endDate);
		mav.addObject("reportTimeStart", startDate);
		mav.addObject("reportTimeEnd", endDate);
		List<PrpdCheckBankMainVo> prpdChecklist=  managerService.findCheckListByHql(comCode);
		List<SysCodeDictVo> dictVos = new ArrayList<SysCodeDictVo>();

		if(prpdChecklist!=null && prpdChecklist.size()>0){
			for(PrpdCheckBankMainVo checkKind:prpdChecklist){
				SysCodeDictVo dict1Vo = new SysCodeDictVo();
				dict1Vo.setCodeCode(checkKind.getCheckCode()+"");
				dict1Vo.setCodeName(checkKind.getCheckCode()+"-"+checkKind.getCheckName());
				dictVos.add(dict1Vo);
			}
		}else{
			SysCodeDictVo dict1Vo = new SysCodeDictVo();
			dict1Vo.setCodeCode("");
			dict1Vo.setCodeName(" ");
			dictVos.add(dict1Vo);
		}
		mav.addObject("subNodeCode", subNode);
		mav.addObject("dictVos",dictVos);
		mav.setViewName("base/checkagency/CheckFeeTaskQueryList");
		return mav;
	}
	
	/**
	 * 进入查勘费任务审核查询界面
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆yzy(2019年7月30日 上午9:46:44): <br>
	 */
	@RequestMapping(value="/checkFeeTaskAuditQueryList.do", method = RequestMethod.GET)
	public ModelAndView checkFeeTaskAuditQuery(@RequestParam(value = "node") String nodeCode,String subNode) {
		String comCode = WebUserUtils .getComCode();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		ModelAndView mav = new ModelAndView();
		mav.addObject("nodeCode", nodeCode);
		mav.addObject("taskInTimeStart", startDate);
		mav.addObject("taskInTimeEnd", endDate);
		mav.addObject("reportTimeStart", startDate);
		mav.addObject("reportTimeEnd", endDate);
		List<PrpdCheckBankMainVo> prpdChecklist=  managerService.findCheckListByHql(comCode);
		List<SysCodeDictVo> dictVos = new ArrayList<SysCodeDictVo>();

		if(prpdChecklist!=null && prpdChecklist.size()>0){
			for(PrpdCheckBankMainVo checkKind:prpdChecklist){
				SysCodeDictVo dict1Vo = new SysCodeDictVo();
				dict1Vo.setCodeCode(checkKind.getCheckCode()+"");
				dict1Vo.setCodeName(checkKind.getCheckCode()+"-"+checkKind.getCheckName());
				dictVos.add(dict1Vo);
			}
		}else{
			SysCodeDictVo dict1Vo = new SysCodeDictVo();
			dict1Vo.setCodeCode("");
			dict1Vo.setCodeName(" ");
			dictVos.add(dict1Vo);
		}
		mav.addObject("subNodeCode", subNode);
		mav.addObject("dictVos",dictVos);
		mav.setViewName("base/checkagency/CheckFeeTaskAuditQueryList");
		return mav;
	}
	
	
	/**
	 * 查勘费查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified: ☆yezy(2019年8月5日 上午11:34:40): <br>
	 */
	@RequestMapping("/checkFeeQuery.do")
	@ResponseBody
	public String checkFeeQuery(CheckQueryVo queryVo) throws Exception {
		if(queryVo.getEndCaseTimeEnd()!=null){
			queryVo.setEndCaseTimeEnd(DateUtils.addDays(queryVo.getEndCaseTimeEnd(),1));
		}
		String comCode = WebUserUtils.getComCode();
		queryVo.setComCode(comCode);
		ResultPage<CheckQueryResultVo> resultPage=acheckService.findPageForACheck(queryVo);
		String jsonData = ResponseUtils.toDataTableJson(resultPage,"registNo","checkCode:CheckPayCode","checkName","insureName","insureCode",
				"policyNo","lossDate"+FM_DATE_mm,"taskDetail","endCaseTime","checkFee","veriLoss","userName","photoCount");
		logger.debug("queryVo.jsonData="+jsonData); 

		return jsonData;
	}
    
	/**
	 * 导出excel
	 * @param response
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified: ☆yzy(2019年8月5日 下午2:24:42): <br>
	 */
	@RequestMapping("/exportExcel.do")
	@ResponseBody
	public String exportExcel(HttpServletResponse response,CheckQueryVo queryVo) throws Exception {

		String comCode = WebUserUtils.getComCode();
		queryVo.setComCode(comCode);
		if(StringUtils.isBlank(queryVo.getCheckCode())){
			queryVo.setCheckCode("00000"); // 查询不到
		}
		List<CheckQueryResultVo> results = acheckService.getDatas(queryVo);
		if(results!=null && results.size()>5000){
			throw new IllegalArgumentException("EXCEL导出条目不允许超过5000条!");
		}
		// 填充projects数据
		List<Map<String,Object>> list = acheckService.createExcelRecord(results);
		String fileDir = "ins/sino/claimcar/other/files/checkFeeTemplate.xlsx";
		String keys[] = {"registNo","policyNo","insureName","checkCode","checkName","lossDate","payment","taskStatus","taskDetail","userName","photoCount","amount","id","taskType","checkFee","veriLoss","isSurvey"};// map中的key
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

		String fileName = "查勘费.xlsx";
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
	 * @modified: ☆yzy(2019年8月5日 下午4:22:29): <br>
	 */
	@RequestMapping(value = "importExcel.ajax", method = RequestMethod.POST)
	@ResponseBody
	public void importExcel(@RequestParam MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		try{
			List<List<Object>> objects = ImportExcelUtil.getBankListByExcel(file.getInputStream(),file.getOriginalFilename());

			SysUserVo userVo = WebUserUtils.getUser();
			Double flowTaskId =acheckService.applyAcheckTask(objects, userVo);

			json.put("data",flowTaskId);
			json.put("status",HttpStatus.SC_OK);
		}catch(Exception e){
			json.put("status",HttpStatus.SC_INTERNAL_SERVER_ERROR);
			json.put("msg",e.getMessage());
			logger.info("查勘费导入报错：---》",e);
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(json.toJSONString());
	}
	
	    //查勘费任务查询
		@RequestMapping(value = "/checkFeeTaskQuery.do", method = RequestMethod.POST)
		@ResponseBody
		public String checkFeeTaskQuery( PrpLWfTaskQueryVo taskQueryVo// 页面组装VO
				)throws Exception {
			
			//ComCode用saa_permitcompany.comcode
			List<SysCodeDictVo> sysCodeList=wfTaskQueryService.findAllPermitcompanyByUserCode(WebUserUtils.getUserCode());
			if(sysCodeList != null && sysCodeList.size() >0){
				taskQueryVo.setComCode(sysCodeList.get(0).getCodeCode());
			}else{
				taskQueryVo.setComCode(WebUserUtils.getComCode());
			}
			ResultPage<WfTaskQueryResultVo> page = wfTaskQueryService.findCheckFeeTaskQuery(taskQueryVo);
			String jsonData = ResponseUtils.toDataTableJson(page,"registNo","taskId","checkCode:CheckPayCode",
					"taskInUser:UserCode","taskInTime"+FM_DATE_mm,"checkCode");
			logger.debug("jsonData="+jsonData);
			return jsonData;

		
		}
		
		//查勘费审核查询
		@RequestMapping(value = "/checkFeeVeriTaskQuery.do", method = RequestMethod.POST)
		@ResponseBody
		public String checkFeeVeriTaskQuery( PrpLWfTaskQueryVo taskQueryVo)throws Exception {// 页面组装VO
			String userCode=WebUserUtils.getUserCode();
			List<SysCodeDictVo> sysCodeList=wfTaskQueryService.findPermitcompanyByUserCode(userCode);
			String comCode=WebUserUtils.getComCode();
			if(sysCodeList!=null && sysCodeList.size()>0){
				comCode=sysCodeList.get(0).getCodeCode();
			}
			taskQueryVo.setComCode(comCode);	
			ResultPage<WfTaskQueryResultVo> page = wfTaskQueryService.findCheckFeeVeriTaskQuery(taskQueryVo);
			String jsonData = ResponseUtils.toDataTableJson(page,"taskId","checkCode:CheckPayCode",
					"registNo",	"taskInUser:UserCode","taskInTime"+FM_DATE_mm);
			return jsonData;

		
		}
		
		/**
		 * 查勘费录入
		 * @return
		 * @modified: ☆yzy(2019年8月6日 上午11:16:14): <br>
		 */
		@RequestMapping("/checksFeeEntry.do")
		public ModelAndView checksFeeFeeEntry(Double taskId) {
			ModelAndView modelAndView = new ModelAndView();

			PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(taskId);// 查勘任务

			String taskNo = taskVo.getRegistNo();// 查勘费工作流报案号存的是任务号

			PrpLAcheckMainVo prpLAcheckMainVo=acheckService.findAcheckMainVoByTaskNo(taskNo);
			// 查找关联保单号
			if(prpLAcheckMainVo!=null && prpLAcheckMainVo.getPrpLCheckFees()!=null && prpLAcheckMainVo.getPrpLCheckFees().size()>0){
				for(PrpLCheckFeeVo fee:prpLAcheckMainVo.getPrpLCheckFees()){
					if(StringUtils.isBlank(fee.getRemark())){
						fee.setRemark("查勘费");
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

			PrpLClaimTextVo prpLClaimTextVo = claimTextSerVice.findClaimTextByNode(prpLAcheckMainVo.getId(),taskVo.getSubNodeCode(),"0");// 暂存时flag=0，提交flag=1
			List<PrpLClaimTextVo> prpLClaimTextVos = claimTextSerVice.findClaimTextList(prpLAcheckMainVo.getId(),prpLAcheckMainVo.getTaskId(),
					FlowNode.CheckFee.name());// 意见列表
			prpLAcheckMainVo.setComCode(WebUserUtils.getComCode());
			PrpdCheckBankMainVo checkBankMainVo = null;
			if(prpLAcheckMainVo.getCheckmId()!=null){
				checkBankMainVo = managerService.findCheckById(prpLAcheckMainVo.getCheckmId().toString());
			}else{
				checkBankMainVo = managerService.findCheckByCode(prpLAcheckMainVo.getCheckcode(),prpLAcheckMainVo.getComCode());
			}
			List<VatQueryViewVo> vatQueryViewVos= billlclaimService.findPrpLbillinfoVoByTaskNo(taskNo);
			modelAndView.addObject("vatQueryViewVos",vatQueryViewVos);
			modelAndView.addObject("taskVo",taskVo);
			modelAndView.addObject("handlerStatus",taskVo.getHandlerStatus());
			modelAndView.addObject("prpLAcheckMainVo",prpLAcheckMainVo);
			modelAndView.addObject("prpLClaimTextVo",prpLClaimTextVo);
			modelAndView.addObject("prpLClaimTextVos",prpLClaimTextVos);
			modelAndView.addObject("checkBankMainVo",checkBankMainVo);
			modelAndView.setViewName("base/checkagency/CheckFeeEntry");
			return modelAndView;

		}
		
		@RequestMapping(value = "/saveCheckFee.do", method = RequestMethod.POST)
		@ResponseBody
		public AjaxResult saveCheckFee(@FormModel("prpLAcheckMainVo") PrpLAcheckMainVo prpLAcheckMainVo,
									   @FormModel("checkFeeVo") List<PrpLCheckFeeVo> feeVoList,
									   @FormModel("prpLClaimTextVo") PrpLClaimTextVo prpLClaimTextVo) {
			AjaxResult ajaxResult = new AjaxResult();
			try{
				SysUserVo userVo = WebUserUtils.getUser();

				// 更新费用金额
				acheckService.updateCheckFee(feeVoList);
				// 跟新总金额
				acheckService.updateSumAmountFee(prpLAcheckMainVo);

				// 跟踪意见
				prpLClaimTextVo.setComCode(WebUserUtils.getComCode());
				String description = codeTranService.transCode("AuditStatus","save");
				prpLClaimTextVo.setDescription(description);
				prpLClaimTextVo.setStatus("save");
				prpLClaimTextVo.setFlag("0");
				// prpLClaimTextVo.setComName(WebUserUtils.getComName());
				prpLClaimTextVo.setBigNode(FlowNode.CheckFee.name());
				prpLClaimTextVo.setNodeCode(FlowNode.CheckFeeQuery.name());
				if(prpLClaimTextVo.getId()==null){// 新增
					prpLClaimTextVo.setBussTaskId(prpLAcheckMainVo.getId());
					prpLClaimTextVo.setBussNo(prpLAcheckMainVo.getTaskId());
					prpLClaimTextVo.setRegistNo(prpLAcheckMainVo.getTaskId());
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

		@RequestMapping(value = "/submitCheckFee.do", method = RequestMethod.POST)
		@ResponseBody
		public AjaxResult submitCheckFee(@FormModel("prpLAcheckMainVo") PrpLAcheckMainVo prpLAcheckMainVo,
									     @FormModel("checkFeeVo") List<PrpLCheckFeeVo> feeVoList,
											@FormModel("prpLClaimTextVo") PrpLClaimTextVo prpLClaimTextVo,Double flowTaskId) {
			AjaxResult ajaxResult = new AjaxResult();
			try{

				PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);// 查勘任务
				SysUserVo userVo = WebUserUtils.getUser();

				// 更新费用金额
				acheckService.updateCheckFee(feeVoList);
				// 跟新总金额
				acheckService.updateSumAmountFee(prpLAcheckMainVo);
				// 跟踪意见
				prpLClaimTextVo.setComCode(WebUserUtils.getComCode());
				String description = codeTranService.transCode("AuditStatus","submit");
				prpLClaimTextVo.setDescription(description);
				prpLClaimTextVo.setStatus("submit");
				prpLClaimTextVo.setFlag("1");
				if(prpLClaimTextVo.getId()==null){// 新增
					prpLClaimTextVo.setBussTaskId(prpLAcheckMainVo.getId());
					prpLClaimTextVo.setBussNo(prpLAcheckMainVo.getTaskId());
					prpLClaimTextVo.setRegistNo(prpLAcheckMainVo.getTaskId());
					prpLClaimTextVo.setCreateUser(userVo.getUserCode());
					prpLClaimTextVo.setCreateTime(new Date());
					prpLClaimTextVo.setOperatorCode(userVo.getUserCode());
					prpLClaimTextVo.setOperatorName(userVo.getUserName());
					// prpLClaimTextVo.setComName(WebUserUtils.getComName());
					prpLClaimTextVo.setBigNode(FlowNode.CheckFee.name());
					prpLClaimTextVo.setNodeCode(FlowNode.CheckFeeQuery.name());
					prpLClaimTextVo.setInputTime(new Date());
				}else{
					prpLClaimTextVo.setUpdateUser(WebUserUtils.getComCode());
					prpLClaimTextVo.setUpdateTime(new Date());
				}

				WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
				submitVo.setFlowId(taskVo.getFlowId());
				submitVo.setFlowTaskId(taskVo.getTaskId());
				submitVo.setComCode(userVo.getComCode());
				submitVo.setNextNode(FlowNode.CheckFeeCheckQuery);
				submitVo.setCurrentNode(FlowNode.CheckFeeQuery);
				submitVo.setAssignCom(WebUserUtils.getComCode());
				submitVo.setAssignUser(WebUserUtils.getUserCode());
				submitVo.setTaskInUser(userVo.getUserCode());
				submitVo.setTaskInKey(taskVo.getRegistNo());

				wfTaskHandleService.submitAcheckTask(taskVo, submitVo);
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
		
		/**
		 * 查勘费审核
		 * @return
		 * @modified: ☆yzy(2019年8月6日 上午11:16:55): <br>
		 */
		@RequestMapping("/checksFeeAudit.do")
		public ModelAndView checksFeeAudit(Double taskId) {
			ModelAndView modelAndView = new ModelAndView();

			PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(taskId);// 查勘任务

			String taskNo = taskVo.getRegistNo();// 查勘费工作流报案号存的是任务号

			PrpLAcheckMainVo prpLAcheckMainVo=acheckService.findAcheckMainVoByTaskNo(taskNo);
			
			if(prpLAcheckMainVo!=null && prpLAcheckMainVo.getPrpLCheckFees()!=null && prpLAcheckMainVo.getPrpLCheckFees().size()>0){
				for(PrpLCheckFeeVo fee:prpLAcheckMainVo.getPrpLCheckFees()){
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
			
			PrpLClaimTextVo prpLClaimTextVo = claimTextSerVice.findClaimTextByNode(prpLAcheckMainVo.getId(),taskVo.getSubNodeCode(),"0");// 暂存时flag=0，提交flag=1
			List<PrpLClaimTextVo> prpLClaimTextVos = claimTextSerVice.findClaimTextList(prpLAcheckMainVo.getId(),prpLAcheckMainVo.getTaskId(),
					FlowNode.CheckFee.name());// 意见列表
			prpLAcheckMainVo.setComCode(WebUserUtils.getComCode());
			// PrpdIntermMainVo intermMainVo = managerService.findIntermByCode(assessorMainVo.getIntermcode(),assessorMainVo.getComCode());
			PrpdCheckBankMainVo checkBankMainVo = null;
			if(prpLAcheckMainVo.getCheckmId()!=null){
				checkBankMainVo = managerService.findCheckById(prpLAcheckMainVo.getCheckmId().toString());
			}else{
				checkBankMainVo = managerService.findCheckByCode(prpLAcheckMainVo.getCheckcode(),prpLAcheckMainVo.getComCode());
			}

			modelAndView.addObject("taskVo",taskVo);
			modelAndView.addObject("handlerStatus",taskVo.getHandlerStatus());
			modelAndView.addObject("prpLAcheckMainVo",prpLAcheckMainVo);
			modelAndView.addObject("prpLClaimTextVo",prpLClaimTextVo);
			modelAndView.addObject("prpLClaimTextVos",prpLClaimTextVos);
			modelAndView.addObject("checkBankMainVo",checkBankMainVo);

			modelAndView.setViewName("base/checkagency/CheckFeeAudit");
			return modelAndView;
		}
		
		@RequestMapping(value = "/submitCheckFeeAudit.do", method = RequestMethod.POST)
		@ResponseBody
		public AjaxResult submitCheckFeeAudit(@FormModel("prpLAcheckMainVo") PrpLAcheckMainVo prpLAcheckMainVo,
											  @FormModel("prpLClaimTextVo") PrpLClaimTextVo prpLClaimTextVo,Double flowTaskId,String audit) {
			AjaxResult ajaxResult = new AjaxResult();
			try{

				PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);// 查勘任务
				SysUserVo userVo = WebUserUtils.getUser();

				// 跟踪意见
				prpLClaimTextVo.setComCode(WebUserUtils.getComCode());
				String description = codeTranService.transCode("AuditStatus","submit");
				prpLClaimTextVo.setDescription(description);

				prpLClaimTextVo.setFlag("1");
				if(prpLClaimTextVo.getId()==null){// 新增
					prpLClaimTextVo.setBussTaskId(prpLAcheckMainVo.getId());
					prpLClaimTextVo.setBussNo(prpLAcheckMainVo.getTaskId());
					prpLClaimTextVo.setRegistNo(prpLAcheckMainVo.getTaskId());
					prpLClaimTextVo.setCreateUser(userVo.getUserCode());
					prpLClaimTextVo.setCreateTime(new Date());
					prpLClaimTextVo.setOperatorCode(userVo.getUserCode());
					prpLClaimTextVo.setOperatorName(userVo.getUserName());
					// prpLClaimTextVo.setComName(WebUserUtils.getComName());
					prpLClaimTextVo.setBigNode(FlowNode.CheckFee.name());
					prpLClaimTextVo.setNodeCode(FlowNode.CheckFeeCheckQuery.name());
					prpLClaimTextVo.setInputTime(new Date());
				}else{
					prpLClaimTextVo.setUpdateUser(WebUserUtils.getComCode());
					prpLClaimTextVo.setUpdateTime(new Date());
				}

				WfTaskSubmitVo submitVo = new WfTaskSubmitVo();

				if("submit".equals(audit)){
					submitVo.setNextNode(FlowNode.END);
					prpLClaimTextVo.setStatus("submitAudit");// 审核通过
					prpLAcheckMainVo.setUnderwriteuser(WebUserUtils.getUserCode());
					acheckService.updateUnderWriteFlag(prpLAcheckMainVo);//回写审核通过标志
				}else if("back".equals(audit)){
					submitVo.setSubmitType(SubmitType.B);
					submitVo.setNextNode(FlowNode.CheckFeeTaskQuery);
					prpLClaimTextVo.setStatus("backCheckFee");// 退回申请
				}

				submitVo.setHandleIdKey(prpLAcheckMainVo.getId().toString());
				submitVo.setFlowId(taskVo.getFlowId());
				submitVo.setFlowTaskId(taskVo.getTaskId());
				submitVo.setComCode(userVo.getComCode());
				submitVo.setCurrentNode(FlowNode.CheckFeeCheckQuery);
				submitVo.setTaskInUser(userVo.getUserCode());
				submitVo.setTaskInKey(taskVo.getRegistNo());

				wfTaskHandleService.submitAcheckTask(taskVo, submitVo);
				claimTextSerVice.saveOrUpdte(prpLClaimTextVo);
				if("submit".equals(audit)){ // 接口调用//总费用为0时，不送收付与Vat
					if(prpLAcheckMainVo.getSumAmount().compareTo(new BigDecimal(0))!=0){
						PrpLAcheckMainVo acheckMainVo=acheckService.findAcheckMainVoByTaskNo(prpLAcheckMainVo.getTaskId());
						interfaceAsyncService.checkFeeToPayment(acheckMainVo);// 送收付
						//interfaceAsyncService.checkFeeToInvoice(acheckMainVo,userVo);
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

		@RequestMapping("/auditTakskExportExcel.do")
		@ResponseBody
		public String auditTakskExportExcel(HttpServletRequest request,HttpServletResponse response) throws Exception {
			String taskId = request.getParameter("taskId");
			String workStatus = request.getParameter("workStatus");
			List<CheckQueryResultVo> results=acheckService.findAcheckMainVo(taskId);
			if(results!=null && results.size()>5000){
				throw new IllegalArgumentException("EXCEL导出条目不允许超过5000条!");
			}
			if(results!=null && results.size()>0){
				for(CheckQueryResultVo result:results){
					result.setWorkStatus(workStatus); // 设置案件状态
				}
			}
			
			List<Map<String,Object>> list = acheckService.createExcelRecord(results);
			String fileDir = "ins/sino/claimcar/other/files/checkFeeTask.xlsx";
			String keys[] = {"registNo","policyNo","claimNo","compensateNo","kindCode","insureName","payment","taskDetail","amount","checkName"};// map中的key
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

			String fileName = "查勘费.xlsx";
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
		 * 本案件下查勘费费用查看
		 * @param registNo
		 * @return
		 */
		@RequestMapping("/checkFeeShowView.do")
		public ModelAndView checkFeeShowView(String registNo) {
			ModelAndView mv = new ModelAndView();
			List<PrpLCheckFeeVo> feeVos = new ArrayList<PrpLCheckFeeVo>();
			List<PrpLCheckFeeVo> listVo=acheckService.findPrpLCheckFeeVoByRegistNo(registNo);
			
			if(listVo!=null && listVo.size()>0){
				for(PrpLCheckFeeVo vo:listVo){
					PrpLAcheckMainVo prpLAcheckMainVo=acheckService.findAcheckMainVoById(vo.getAcheckMainId());
					if(prpLAcheckMainVo!=null){
						PrpLWfTaskVo prplwfTaskVo = wfTaskHandleService.findprplwftaskVoByNodeCodeAndflowId("CheckFee",prpLAcheckMainVo.getTaskId());
						if("3".equals(prpLAcheckMainVo.getUnderWriteFlag())){
							if(prplwfTaskVo!=null){
								vo.setTaskId(prplwfTaskVo.getTaskId());
							}
							feeVos.add(vo);
						}
					}

				}
			}
			mv.addObject("prpLCheckFeeVos",feeVos);
			mv.setViewName("base/checkagency/CheckFeeShowView");

			return mv;
		}

		/**
		 * 查勘任务注销
		 * @param taskId
		 * @return
		 * @modified: ☆yzy(2019年8月6日 下午8:07:45): <br>
		 */
		@RequestMapping("/submitCancel.do")
		@ResponseBody
		public AjaxResult submitCancel(Double flowTaskId) {
			AjaxResult ajaxResult = new AjaxResult();
			try{
				SysUserVo userVo = WebUserUtils.getUser();
				acheckService.submitCancel(flowTaskId, userVo);

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
	 * 查勘费退票查询
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年7月30日 下午2:22:32): <br>
	 */
	@RequestMapping("/checkFeeBackTicketList")
	public ModelAndView checkFeeBackTicketQuery() {
		ModelAndView mv = new ModelAndView();
		String comCode = WebUserUtils.getComCode();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		List<PrpdCheckBankMainVo> prpdCheckBankMainlist = managerService.findCheckListByHql(comCode);
		List<SysCodeDictVo> dictVos = new ArrayList<SysCodeDictVo>();
		for(PrpdCheckBankMainVo itemKind:prpdCheckBankMainlist){
			SysCodeDictVo dict1Vo = new SysCodeDictVo();
			dict1Vo.setCodeCode(itemKind.getId()+"");
			dict1Vo.setCodeName(itemKind.getCheckCode()+"-"+itemKind.getCheckName());
			dictVos.add(dict1Vo);
		}
		mv.addObject("dictVos",dictVos);
		mv.addObject("taskInTimeStart",startDate);
		mv.addObject("taskInTimeEnd",endDate);
		mv.setViewName("base/checkagency/CheckFeeBackTicketList");
		return mv;
	}
	
	
	/**
	 * 查勘费退票审核
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年7月30日 下午2:22:40): <br>
	 */
	@RequestMapping("/checkFeeBackTicketAuditList")
	@ResponseBody
	public ModelAndView checkFeeBackTicketAuditList() {
		ModelAndView mav = new ModelAndView();
		String comCode = WebUserUtils.getComCode();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		List<PrpdCheckBankMainVo> prpdChecklist = managerService.findCheckListByHql(comCode);
		List<SysCodeDictVo> dictVos = new ArrayList<SysCodeDictVo>();
		for(PrpdCheckBankMainVo itemKind:prpdChecklist){
			SysCodeDictVo dict1Vo = new SysCodeDictVo();
			dict1Vo.setCodeCode(itemKind.getId()+"");
			dict1Vo.setCodeName(itemKind.getCheckCode()+"-"+itemKind.getCheckName());
			dictVos.add(dict1Vo);
		}
		mav.addObject("dictVos",dictVos);
		mav.addObject("taskInTimeStart",startDate);
		mav.addObject("taskInTimeEnd",endDate);
		mav.setViewName("base/checkagency/CheckFeeBackTicketAuditList");
		return mav;
	}
	/**
	 * 查勘费退票审核查询方法
	 * <pre></pre>
	 * @param handleStatus
	 * @param queryVo
	 * @param start
	 * @param length
	 * @return
	 * @throws ParseException
	 * @modified:
	 * ☆XiaoHuYao(2019年7月30日 下午3:53:41): <br>
	 */
	@RequestMapping("/checkFeeAccountAuditSearch")
	@ResponseBody
	public String checkFeeAccountAuditSearch(@RequestParam("handleStatus") String handleStatus,
												@FormModel("prpLWfTaskQueryVo") PrpLWfTaskQueryVo queryVo,// 页面组装VO
												@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
												@RequestParam(value = "length", defaultValue = "10") Integer length) {
		queryVo.setCompensateNo(queryVo.getBussNo().replaceAll("\\s*",""));// 去掉所有空格符
		queryVo.setClaimNo(queryVo.getClaimNo().replaceAll("\\s*",""));// 去掉所有空格符
		queryVo.setComCode(WebUserUtils.getComCode());
		queryVo.setUserCode(WebUserUtils.getUserCode());

		ResultPage<PrpLPayBankVo> page = null;
		String jsonData = null;
		try{
			page = acheckService.checkFeeAuditSearch(queryVo,start,length,handleStatus);
			jsonData = ResponseUtils.toDataTableJson(page,"bussNo","bankName","accountNo","remark","appTime","payType:PayRefReason","accountId",
					"backaccountId","checkCode","registNo","accountName","auditId");
		}catch(ParseException e){
			logger.error("查勘费退票审核列表查询失败",e);
		}
		return jsonData;

	}
	

	/**
	 * 查勘费退票查询方法
	 * <pre></pre>
	 * @param handleStatus
	 * @param queryVo
	 * @param start
	 * @param length
	 * @return
	 * @throws ParseException
	 * @modified:
	 * ☆XiaoHuYao(2019年7月30日 下午3:54:38): <br>
	 */
	@RequestMapping(value = "/checkFeeAccountSearch", method = RequestMethod.POST)
	@ResponseBody
	public String search(@RequestParam("handleStatus") String handleStatus,@FormModel("prpLWfTaskQueryVo") PrpLWfTaskQueryVo queryVo,// 页面组装VO
							@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
							@RequestParam(value = "length", defaultValue = "10") Integer length) {
		queryVo.setCompensateNo(queryVo.getBussNo().replaceAll("\\s*",""));// 去掉所有空格符
		queryVo.setClaimNo(queryVo.getClaimNo().replaceAll("\\s*",""));// 去掉所有空格符
		queryVo.setComCode(WebUserUtils.getComCode());
		queryVo.setUserCode(WebUserUtils.getUserCode());
		String jsonData = null;
		ResultPage<PrpLPayBankVo> page;
		try{
			page = acheckService.search(queryVo,start,length,handleStatus);
			jsonData = ResponseUtils.toDataTableJson(page,"bussNo","bankName","accountNo","remark","appTime","payType:PayRefReason","accountId",
					"backaccountId","checkCode","registNo","accountName");
		}catch(ParseException e){
			logger.error("查询查勘费退票信息失败",e);
		}
		return jsonData;

	}
	
	@RequestMapping("/checkFeeMoreMessageList")
	public ModelAndView intermMoreMessage(String bussNo,String accountId,String backaccountId,String checkCode,String registNo) {
		ModelAndView mav = new ModelAndView();
		List<PrpLCheckFeeVo> feeLists = acheckService.findPrpLCheckFeeVoByBussNo(bussNo);
		if(feeLists!=null&&feeLists.size()>0){
			for(PrpLCheckFeeVo vo:feeLists){
				vo.setCreateUser("查勘费");
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
		String auditOpinionSign = CodeConstants.CommonConst.FALSE;// 审核意见是否显示，如果是在退票审核中退回的，就显示，否则就不显示
		// 如果是退票审核退回的，则页面展示退票审核意见
		PrpLCheckmAuditVo auditVo = new PrpLCheckmAuditVo();
		if(accountVo!=null&&"3".equals(accountVo.getIsHaveAudit())){
			auditVo = acheckService.findPrpLCheckmAuditVoById(accountVo.getAuditId());
			auditOpinionSign = CodeConstants.CommonConst.TRUE;
		}
		mav.addObject("auditOpinion",auditVo.getAuditOpinion());// 审核意见
		mav.addObject("updateUser",auditVo.getUpdateUser());// 审核人员
		mav.addObject("updateTime",auditVo.getUpdateTime());// 审核时间
		mav.addObject("auditOpinionSign",auditOpinionSign);
		mav.addObject("registNo",registNo);
		mav.addObject("checkCode",checkCode);
		mav.addObject("requstime",requstime);
		mav.addObject("prpDAccRollBackAccountVo",accountVo);
		mav.addObject("accountId",accountId);// 银行账户信息表Id
		mav.addObject("checkFeeList",feeLists);
		mav.setViewName("base/checkagency/CheckFeeInfoMain");
		return mav;
	}
	
	/**
	 * 查勘费退票审核更多信息
	 * <pre></pre>
	 * @param bussNo
	 * @param accountId
	 * @param backaccountId
	 * @param intermCode
	 * @param registNo
	 * @param auditId
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年7月30日 下午3:57:09): <br>
	 */
	@RequestMapping("/checkfeeAuditMoreMessageList")
	public ModelAndView checkFeeAuditMoreMessage(String bussNo,String accountId,String backaccountId,String checkCode,String registNo,String auditId) {
		ModelAndView mav = new ModelAndView();

		List<PrpLCheckFeeVo> feeLists = acheckService.findPrpLCheckFeeVoByBussNo(bussNo);
		if(feeLists!=null&&feeLists.size()>0){
			for(PrpLCheckFeeVo vo:feeLists){
				vo.setCreateUser("查勘费");
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
		PrpLCheckmAuditVo auditVo = acheckService.findPrpLCheckmAuditVoById(Long.valueOf(auditId));
		String checkname = "";// 查勘机构三级名称
		if(StringUtils.isNotBlank(checkCode)&& !checkCode.startsWith(CodeConstants.CommonConst.FALSE)){
			PrpdCheckBankMainVo checkBankMainVo = managerService.findCheckById(checkCode);
			if(checkBankMainVo!=null&&checkBankMainVo.getId()!=null){
				checkname = checkBankMainVo.getCheckName();
			}
		}else{
			checkname = checkCode;
		}

		mav.addObject("auditId",auditId);
		mav.addObject("prpLCheckmAuditVo",auditVo);
		mav.addObject("registNo",registNo);
		mav.addObject("checkname",checkname);
		mav.addObject("requstime",requstime);
		mav.addObject("prpDAccRollBackAccountVo",accountVo);
		mav.addObject("accountId",accountId);// 银行账户信息表Id
		mav.addObject("checkFeeList",feeLists);
		mav.setViewName("base/checkagency/CheckFeeAuditInfoMain");
		return mav;
	}
	
	/**
	 * 查勘费退票审核查询
	 * @param auditId
	 * @return
	 */
	@RequestMapping("/checkFeeAuditBankInfoQuery")
	public ModelAndView checkFeeAuditBankInfoList(String auditId) {
		ModelAndView mav = new ModelAndView();
		PrpdcheckBankVo bankVo = new PrpdcheckBankVo();
		PrpLCheckmAuditVo auditVo = acheckService.findPrpLCheckmAuditVoById(Long.valueOf(auditId));

		// 将查出的数据放入Map中去
		Map<String,String> bankCodeMap = new HashMap<String,String>();
		List<PrplOldaccbankCodeVo> listVo = payCustomService.findPrplOldaccbankCodeByFlag("1");
		if(listVo!=null&&listVo.size()>0){
			for(PrplOldaccbankCodeVo vo:listVo){
				bankCodeMap.put(vo.getBankCode(),vo.getBankName());
			}
		}
		if(auditVo!=null){
			setParamsForPrpdCheckBank(bankVo,auditVo);
		}

		mav.addObject("bankCodeMap",bankCodeMap);
		mav.addObject("prpdcheckBank",bankVo);
		mav.setViewName("base/checkagency/CheckFeeBankInfoView");

		return mav;
	}
	
	/**
	 * 查勘费退票审核的0-暂存，1-审核通过，2-退回
	 * @param auditId
	 * @param auditOpinion
	 * @return
	 */
	@RequestMapping("/auditWays")
	@ResponseBody
	public AjaxResult auditWays(@RequestParam("auditId") String auditId,@RequestParam("auditOpioion") String contexts,
								@RequestParam("sign") String sign) {
		AjaxResult ajax = new AjaxResult();
		try{
			SysUserVo userVo = WebUserUtils.getUser();
			if(StringUtils.isBlank(auditId)){
				throw new IllegalArgumentException("传入的查勘费退票审核表的Id为空！");
			}
			PrpLCheckmAuditVo auditVo = acheckService.findPrpLCheckmAuditVoById(Long.valueOf(auditId));

			if(CodeConstants.CommonConst.FALSE.equals(sign)){
				// 退票审核暂存时，保存审核意见
				auditVo.setAuditOpinion(contexts);
				auditVo.setUpdateUser(userVo.getUserCode());
				auditVo.setUpdateTime(new Date());
				acheckService.updateOrSaveOfPrpLCheckmAudit(auditVo);
				ajax.setStatus(HttpStatus.SC_OK);
				ajax.setStatusText("暂存成功！");
			}else if(CodeConstants.CommonConst.TRUE.equals(sign)){
				// 退票审核通过直接送收付
				PrpDAccRollBackAccountVo backAccountVo = accountQueryService.findRollBackAccountById(auditVo.getBackAccountId());
				backAccountVo.setOldAccountId(auditVo.getIsAutoPay());
				String registNo = auditVo.getRegistNo();
				PrpdcheckBankVo checkbankVo = new PrpdcheckBankVo();
				setParamsForPrpdCheckBank(checkbankVo,auditVo);
				// 回写查勘费退票审核表
				auditVo.setAuditOpinion(contexts);
				auditVo.setStatus(CodeConstants.CommonConst.TRUE);
				auditVo.setUpdateUser(userVo.getUserCode());
				auditVo.setUpdateTime(new Date());
				auditVo.setIsautoAudit(CodeConstants.CommonConst.TRUE);
				String str = accountInfoService.sendCheckFeeAccountToPayment(checkbankVo,userVo,registNo,backAccountVo,
						String.valueOf(auditVo.getOldBankId()),auditVo.getCheckCode(),auditVo);
				ajax.setStatus(HttpStatus.SC_OK);
				ajax.setStatusText("操作成功！");
			}else if("2".equals(sign)){
				auditVo.setStatus("2");
				auditVo.setAuditOpinion(contexts);
				auditVo.setUpdateUser(userVo.getUserCode());
				auditVo.setUpdateTime(new Date());
				acheckService.updateOrSaveOfPrpLCheckmAudit(auditVo);
				acheckService.updatAccrollbackaccount(auditVo.getBackAccountId(),Long.valueOf(auditId),"3",
						"0");
				acheckService.updateStatusOfPrpdaccrollbackaccount("2",auditVo.getBackAccountId());
				ajax.setStatus(HttpStatus.SC_OK);
				ajax.setStatusText("已退回到查勘费退票修改");
			}
		}catch(Exception e){
			ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajax.setStatusText("操作失败！"+e.getMessage());
			logger.error("查勘费退票审核失败",e);
		}
		return ajax;
	}
	
	/**
	 * 查勘费退票送收付 isNeedAudit 是否需要走退票审核0-不需要，1-需要
	 * <pre></pre>
	 * @param registNo
	 * @param intermbankVo
	 * @param backaccontId
	 * @param bankId
	 * @param intermCode
	 * @param isSendMoney
	 * @param isNeedAudit
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年7月30日 下午4:26:56): <br>
	 */
	@RequestMapping("/sendCheckFeeAccountToPayment")
	@ResponseBody
	public AjaxResult sendCheckFeeAccountToPayment(@RequestParam("registNo") String registNo,@FormModel("prpdcheckBankVo") PrpdcheckBankVo checkbankVo,
												@RequestParam("backaccontId") String backaccontId,@RequestParam("bankId") String bankId,
												@RequestParam("checkCode") String checkCode,@RequestParam("isSendMoney") String isSendMoney,
												@RequestParam("isNeedAudit") String isNeedAudit) {
		AjaxResult ajax = new AjaxResult();
		SysUserVo userVo = WebUserUtils.getUser();
		String index = "0";
		PrpDAccRollBackAccountVo backAccountVo = accountQueryService.findRollBackAccountById(Long.valueOf(backaccontId));
		try{
			if(CodeConstants.CommonConst.TRUE.equals(isNeedAudit)){
				PrpLCheckmAuditVo auditVo = new PrpLCheckmAuditVo();
				auditVo.setBackAccountId(Long.valueOf(backaccontId));
				auditVo.setOldBankId(Long.valueOf(bankId));
				auditVo.setCreateTime(new Date());
				auditVo.setCreateUser(userVo.getUserCode());
				auditVo.setComCode(userVo.getComCode());
				auditVo.setRegistNo(registNo);
				auditVo.setCheckCode(checkCode);
				auditVo.setStatus(CodeConstants.CommonConst.FALSE);
				auditVo.setBussNo(backAccountVo.getCertiNo());// 可能为结算单号，也可能为计算书号
				auditVo.setIsautoAudit(CodeConstants.CommonConst.TRUE);
				auditVo.setIsAutoPay(isSendMoney);// 是否送资金或送收付标志
				setParamsForPrplCheckAudit(checkbankVo,auditVo);

				acheckService.updateOrSaveOfPrpLCheckmAudit(auditVo);
				// 数据提交的审核，则原来的退票表状态变为已处理
				acheckService.updateStatusOfPrpdaccrollbackaccount(CodeConstants.CommonConst.FALSE,Long.valueOf(backaccontId));
				acheckService.updateInfoFlagOfPrpdaccrollbackaccount(CodeConstants.CommonConst.FALSE,Long.valueOf(backaccontId));
				ajax.setStatus(HttpStatus.SC_OK);
				ajax.setStatusText("操作成功！已提交至退票审核，请通知审核岗处理！");
			}else{

				backAccountVo.setOldAccountId(isSendMoney);
				if(StringUtils.isBlank(registNo)&&backAccountVo!=null){
					registNo = backAccountVo.getCertiNo();
				}
				PrpLCheckmAuditVo auditVo = new PrpLCheckmAuditVo();
				auditVo.setBackAccountId(Long.valueOf(backaccontId));
				auditVo.setOldBankId(Long.valueOf(bankId));
				auditVo.setCreateTime(new Date());
				auditVo.setCreateUser(userVo.getUserCode());
				auditVo.setComCode(userVo.getComCode());
				auditVo.setRegistNo(registNo);
				auditVo.setCheckCode(checkCode);
				auditVo.setStatus(CodeConstants.CommonConst.TRUE);
				auditVo.setIsautoAudit(CodeConstants.CommonConst.FALSE);
				auditVo.setBussNo(backAccountVo.getCertiNo());// 可能为结算单号，也可能为计算书号
				auditVo.setIsAutoPay(isSendMoney);
				setParamsForPrplCheckAudit(checkbankVo,auditVo);
				// String str = accountInfoService.sendCheckFeeAccountToPayment(checkbankVo,userVo,registNo,backAccountVo,bankId,checkCode,auditVo);
				String str = accountInfoService.sendCheckFeeAccountToNewPayment(checkbankVo,userVo,registNo,backAccountVo,bankId,checkCode,auditVo);
				index = CodeConstants.CommonConst.TRUE;
				ajax.setStatus(HttpStatus.SC_OK);
				ajax.setData(index);
				ajax.setStatusText("操作成功！且自动审核完成！");
			}

		}catch(Exception e){
			ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajax.setStatusText("操作失败！"+e.getMessage());
			logger.error("查勘费退票审核失败",e);

		}
		return ajax;
	}
	
	/**
	 * 给查勘费审核表赋值
	 * @param intermbankVo
	 * @param auditVo
	 */
	public void setParamsForPrplCheckAudit(PrpdcheckBankVo checkbankVo,PrpLCheckmAuditVo auditVo) {
		auditVo.setAccountName(checkbankVo.getAccountName());
		auditVo.setAccountCode(checkbankVo.getAccountNo());
		auditVo.setCertifyNo(checkbankVo.getCertifyNo());
		auditVo.setMobile(checkbankVo.getMobile());
		auditVo.setPublicAndprivate(checkbankVo.getPublicAndPrivate());
		auditVo.setCity(checkbankVo.getCity());
		auditVo.setBankName(checkbankVo.getBankName());
		auditVo.setBankoutLets(checkbankVo.getBankOutlets());
		auditVo.setBankNumber(checkbankVo.getBankNumber());
		auditVo.setRemark(checkbankVo.getRemark());
	}
	
	@RequestMapping("/checkFeeBankInfoQuery")
	public ModelAndView checkFeeBankInfoList(String accountId,String errorType,String certiNo,String backaccountId,String checkCode,String registNo) {
		ModelAndView mav = new ModelAndView();
		PrpdcheckBankVo checkBankVo = managerService.findPrpdcheckBankVoById(Long.valueOf(accountId));
		String oldAccountName = checkBankVo.getAccountName();// 旧的收款人户名，传入页面与修改的收款人户名进行比对，如果一样，则不走退票审核，否则要走退票审核；
		PrpdcheckBankVo bankVo = new PrpdcheckBankVo();

		PrpLCheckmAuditVo checkFeeAuditVo = acheckService.findCheckFeeAuditByAccountId(Long.valueOf(backaccountId));
		PrpDAccRollBackAccountVo backAccountVo = accountQueryService.findRollBackAccountById(Long.valueOf(backaccountId));
		// InfoFlag==o则取审核表的数据，否则就取银行表的信息
		if(CodeConstants.CommonConst.FALSE.equals(backAccountVo.getInfoFlag())){
			setParamsForPrpdCheckBank(bankVo,checkFeeAuditVo);
			bankVo.setId(checkFeeAuditVo.getOldBankId());
		}else{
			PrpdCheckBankMainVo checkMainVo = managerService.findCheckById(checkBankVo.getCheckMianId());
			bankVo = checkMainVo.getPrpdcheckBank();
			List<PrpLCheckFeeVo> feeLists = acheckService.findPrpLCheckFeeVoByBussNo(certiNo);
			if(feeLists!=null&&feeLists.size()>0){
				for(PrpLCheckFeeVo vo:feeLists){
					if(StringUtils.isNotBlank(vo.getRemark())){
						bankVo.setRemark(vo.getRemark());
						break;
					}
				}
			}
		}

		// 将查出的数据放入Map中去
		Map<String,String> bankCodeMap = new HashMap<String,String>();
		List<PrplOldaccbankCodeVo> listVo = payCustomService.findPrplOldaccbankCodeByFlag(CodeConstants.CommonConst.TRUE);
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
		mav.addObject("checkCode",checkCode);
		if("88".equals(errorType)){// 88此标志区分展示哪个页面
			mav.setViewName("base/checkfee/CheckFeeBankInfoView");
			mav.addObject("prpdcheckBank",bankVo);
		}else{
			// 如果是在退票审核中被退回的，则银行信息修改页面展示被退回时的银行信息--AuditFlag=0为被查勘费退票审核退回时还没有被送收付，
			if(backAccountVo!=null&&"3".equals(backAccountVo.getIsHaveAudit())&&CodeConstants.CommonConst.FALSE.equals(backAccountVo.getAuditFlag())){
				PrpdcheckBankVo bankVo1 = new PrpdcheckBankVo();
				PrpLCheckmAuditVo auditVo = acheckService.findPrpLCheckmAuditVoById(backAccountVo.getAuditId());
				setParamsForPrpdCheckBank(bankVo1,auditVo);
				bankVo1.setId(auditVo.getOldBankId());// 旧的银行表数据Id
				mav.addObject("prpdcheckBank",bankVo1);
				mav.addObject("oldAccountName",oldAccountName);
			}else{
				// 不是退票审核退回的就显示原来的银行信息
				mav.addObject("prpdcheckBank",bankVo);
				mav.addObject("oldAccountName",oldAccountName);
			}
			mav.setViewName("base/checkagency/CheckFeeBankInfoEdit");
		}
		return mav;
	}
	
	/**
	 * 给查勘费银行表Vo赋值
	 * @param 
	 * @param auditVo
	 */
	public void setParamsForPrpdCheckBank(PrpdcheckBankVo checkbankVo,PrpLCheckmAuditVo auditVo) {
		checkbankVo.setAccountName(auditVo.getAccountName());
		checkbankVo.setAccountNo(auditVo.getAccountCode());
		checkbankVo.setCertifyNo(auditVo.getCertifyNo());
		checkbankVo.setMobile(auditVo.getMobile());
		checkbankVo.setPublicAndPrivate(auditVo.getPublicAndprivate());
		checkbankVo.setCity(auditVo.getCity());
		checkbankVo.setBankName(auditVo.getBankName());
		checkbankVo.setBankOutlets(auditVo.getBankoutLets());
		checkbankVo.setBankNumber(auditVo.getBankNumber());
		checkbankVo.setRemark(auditVo.getRemark());

	}
	
	@RequestMapping("/checkBankInfoQuery")
	public ModelAndView checkBankInfoList(String accountId,String errorType,String certiNo,String backaccountId,String checkCode,String registNo) {
		ModelAndView mav = new ModelAndView();
		PrpdcheckBankVo checkBankVo = managerService.findPrpdcheckBankVoById(Long.valueOf(accountId));
		String oldAccountName = checkBankVo.getAccountName();// 旧的收款人户名，传入页面与修改的收款人户名进行比对，如果一样，则不走退票审核，否则要走退票审核；
		PrpdcheckBankVo bankVo = new PrpdcheckBankVo();

		PrpLCheckmAuditVo interrmAuditVo = acheckService.findCheckFeeAuditByAccountId(Long.valueOf(backaccountId));
		PrpDAccRollBackAccountVo backAccountVo = accountQueryService.findRollBackAccountById(Long.valueOf(backaccountId));
		// InfoFlag==o则取审核表的数据，否则就取银行表的信息
		if("0".equals(backAccountVo.getInfoFlag())){
			setParamsForPrpdCheckBank(bankVo,interrmAuditVo);
			bankVo.setId(interrmAuditVo.getOldBankId());
		}else{
			PrpdCheckBankMainVo checkBankMainVo = managerService.findCheckById(checkBankVo.getCheckMianId());
			bankVo = checkBankMainVo.getPrpdcheckBank();
			List<PrpLCheckFeeVo> feeLists = acheckService.findPrpLCheckFeeVoByBussNo(certiNo);
			if(feeLists!=null&&feeLists.size()>0){
				for(PrpLCheckFeeVo vo:feeLists){
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
		mav.addObject("checkCode",checkCode);
		if("88".equals(errorType)){// 88此标志区分展示哪个页面
			mav.setViewName("base/checkagency/CheckFeeBankInfoView");
			mav.addObject("prpdcheckBank",bankVo);
		}else{
			// 如果是在退票审核中被退回的，则银行信息修改页面展示被退回时的银行信息--AuditFlag=0为被公估费退票审核退回时还没有被送收付，
			if(backAccountVo!=null&&"3".equals(backAccountVo.getIsHaveAudit())&&"0".equals(backAccountVo.getAuditFlag())){
				PrpdcheckBankVo bankVo1 = new PrpdcheckBankVo();
				PrpLCheckmAuditVo auditVo = acheckService.findPrpLCheckmAuditVoById(backAccountVo.getAuditId());
				setParamsForPrpdCheckBank(bankVo1,auditVo);
				bankVo1.setId(auditVo.getOldBankId());// 旧的银行表数据Id
				mav.addObject("prpdcheckBank",bankVo1);
				mav.addObject("oldAccountName",oldAccountName);
			}else{
				// 不是退票审核退回的就显示原来的银行信息
				mav.addObject("prpdcheckBank",bankVo);
				mav.addObject("oldAccountName",oldAccountName);
			}
			mav.setViewName("base/checkagency/CheckFeeBankInfoEdit");
		}
		return mav;
	}
	
	/**
     * 查勘费发票登记
     * @return
     */
    @RequestMapping("/cheFeeRegister.do")
    public ModelAndView cheFeeRegister(String taskNo,String billId,String interCodeId){
   	 ModelAndView mvc=new ModelAndView();
   	 PrpLAcheckMainVo acheckMainVo = acheckService.findAcheckMainVoByTaskNo(taskNo);
   	 PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoById(Long.valueOf(billId));//发票信息表
		// 查找关联保单号
		if(acheckMainVo!=null && acheckMainVo.getPrpLCheckFees()!=null && acheckMainVo.getPrpLCheckFees().size()>0){
			for(PrpLCheckFeeVo fee:acheckMainVo.getPrpLCheckFees()){
				if(StringUtils.isBlank(fee.getRemark())){
					fee.setRemark("查勘费");
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
		List<PrpLCheckFeeVo> cheFeeVos=new ArrayList<PrpLCheckFeeVo>();
		PrpLConfigValueVo configValuesingVo = ConfigUtil.findConfigByCode(CodeConstants.newBillDate);
		//以上线时间为分界点，后面审核通过的数据，才允许绑定发票
 		if(acheckMainVo!=null && acheckMainVo.getUnderWriteDate()!=null && DateBillString(configValuesingVo.getConfigValue())!=null && DateBillString(configValuesingVo.getConfigValue()).getTime()<=acheckMainVo.getUnderWriteDate().getTime()) {
 			if(acheckMainVo!=null && acheckMainVo.getPrpLCheckFees()!=null && acheckMainVo.getPrpLCheckFees().size()>0){
 				for(PrpLCheckFeeVo fee:acheckMainVo.getPrpLCheckFees()){
 					if(StringUtils.isBlank(fee.getLinkBillNo())){
 						cheFeeVos.add(fee);
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
		List<PrpLCheckFeeVo> chefeeList=billlclaimService.findPrpLCheckFeeBylinkBillNo(prpLbillinfoVo.getBillNo()+"_"+prpLbillinfoVo.getBillCode());
	    // 查找关联保单号
		if(chefeeList!=null && chefeeList.size()>0){
			for(PrpLCheckFeeVo fee:chefeeList){
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
		mvc.addObject("cheFeeVos",cheFeeVos);
		mvc.addObject("chefeeList",chefeeList);//发票绑定的计算书集合
		mvc.addObject("interCodeId",interCodeId);
		mvc.addObject("taskNo",taskNo);//任务号
		mvc.setViewName("base/checkagency/ChefeeRegister");
   	return mvc;
    }
    
    /**
     * 查勘费发票信息展示
     * @return
     */
    @RequestMapping("/cheFeebillView.do")
    public ModelAndView cheFeebillView(String billId){
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
		List<PrpLCheckFeeVo> chefeeList=billlclaimService.findPrpLCheckFeeBylinkBillNo(prpLbillinfoVo.getBillNo()+"_"+prpLbillinfoVo.getBillCode());
	    // 查找关联保单号
		if(chefeeList!=null && chefeeList.size()>0){
			for(PrpLCheckFeeVo fee:chefeeList){
				if(StringUtils.isBlank(fee.getRemark())){
					fee.setRemark("查勘费");
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
		mvc.addObject("chefeeList",chefeeList);//发票绑定的计算书集合
		mvc.setViewName("base/checkagency/cheFeebillView");
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
