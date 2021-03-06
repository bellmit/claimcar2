package ins.sino.claimcar.subrogation.web.action;

import ins.framework.common.ResultPage;
import ins.framework.service.CodeTranService;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.platform.service.PlatformReUploadService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.sh.service.SubrogationSHHandleService;
import ins.sino.claimcar.subrogation.sh.vo.CopyInformationCheckDetailListVo;
import ins.sino.claimcar.subrogation.sh.vo.CopyInformationCheckListVo;
import ins.sino.claimcar.subrogation.sh.vo.CopyInformationResultVo;
import ins.sino.claimcar.subrogation.sh.vo.CopyInformationSubrogationViewVo;
import ins.sino.claimcar.subrogation.sh.vo.SubrogationSHQueryVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/subrogationSH")
public class SubrogationSHAction {

	private Logger logger = LoggerFactory.getLogger(SubrogationQueryAction.class);
	
	@Autowired
	private PlatformReUploadService platformLogService;
	
	@Autowired
	private RegistQueryService registQueryService;
	
	@Autowired
	private ClaimTaskService claimTaskService;
	
	@Autowired
	private PolicyViewService policyViewService;
	
	@Autowired
	private SubrogationSHHandleService handleService;
	
	
	@Autowired
	private SubrogationSHHandleService subrogationSHHandleService;
	@Autowired
	SubrogationService subrogationService;
	
	@Autowired
	private CodeTranService codeTranService;
	
	
	
	/**
	 * ????????????????????????????????????
	 * ???Luwei(2017???3???6??? ??????5:42:30): <br>
	 */
	@RequestMapping("/copyInformationList.do")
	public ModelAndView copyInformationQuery() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("comCode",WebUserUtils.getComCode());
		modelAndView.setViewName("subrogationsh/CopyInformationList");
		return modelAndView;
	}
	
	/**
	 * ?????????????????????????????????
	 * ???Luwei(2017???3???7??? ??????3:51:08): <br>
	 */
	@RequestMapping("/claimRecoveryQueryList.do")
	public ModelAndView claimRecoveryQuery() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("subrogation/ClaimRecoveryList");
		return modelAndView;
	}
	
	/**
	 * ??????????????????????????????
	 * ???Luwei(2017???3???7??? ??????3:51:08): <br>
	 */
	@RequestMapping("/recoveryBackQueryList.do")
	public ModelAndView recoveryBackQuery() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("subrogationsh/RecoveryBackList");
		return modelAndView;
	}
	
	/**
	 * ????????????????????????????????????????????????
	 * @return
	 * @modified:
	 * ???Luwei(2017???3???6??? ??????6:28:48): <br>
	 */
	@RequestMapping("/copyInformationSearch.do")
	@ResponseBody
	public String copyInformationSearch(@FormModel("subrogationSHQueryVo") SubrogationSHQueryVo queryVo) throws Exception{
		
		List<CopyInformationSubrogationViewVo> subrogationList = new ArrayList<CopyInformationSubrogationViewVo>();
		
		String comCode = WebUserUtils.getComCode();
		logger.info("=====registNo=="+queryVo.getRegistNo());
		logger.info("=====claimNo=="+queryVo.getClaimNo());
		List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(queryVo.getRegistNo());
		if (cMainVoList != null && !cMainVoList.isEmpty()) {
			comCode = cMainVoList.get(0).getComCode();
		}
		queryVo.setComCode(comCode);
		//???????????????,?????????
		PrpLClaimVo claimVo=claimTaskService.findClaimVoByClaimNo(queryVo.getClaimNo());
		PrpLRegistVo registVo=registQueryService.findByRegistNo(queryVo.getRegistNo());
		if(claimVo!=null){
			queryVo.setRegistNo(claimVo.getRegistNo());
		}else if(registVo!=null){
			queryVo.setRegistNo(registVo.getRegistNo());
		}else{
			throw new IllegalArgumentException(" --?????????????????????????????????--  ");
		}
		
		//??????????????????
		//??????
		CiClaimPlatformLogVo ciLog = platformLogService.findLogByBussNo(RequestType.RegistInfoCI_SH.getCode(),queryVo.getRegistNo(),comCode);
		//??????
		CiClaimPlatformLogVo biLog = platformLogService.findLogByBussNo(RequestType.RegistInfoBI_SH.getCode(),queryVo.getRegistNo(),comCode);
		
		
		//??????_??????????????????????????????????????????_??????
		List<CopyInformationResultVo> resultVoList = new ArrayList<CopyInformationResultVo>();
		if (StringUtils.isNotEmpty(queryVo.getClaimNo())) { //
			
			
			if (claimVo != null && Risk.DQZ.equals(claimVo.getRiskCode()) && ciLog != null) {
				queryVo.setClaimSeqNo(ciLog.getClaimSeqNo());
				resultVoList = handleService.sendCopyInformationToSubrogationSH(queryVo);
			}
			
			if (claimVo != null && !Risk.DQZ.equals(claimVo.getRiskCode()) && biLog != null) {
				queryVo.setClaimSeqNo(ciLog.getClaimSeqNo());
				resultVoList = handleService.sendCopyInformationToSubrogationSH(queryVo);
			}
			
		} else {
			//
			for (PrpLCMainVo cMainVo : cMainVoList) {
				List<CopyInformationResultVo> result = null;
				if (Risk.DQZ.equals(cMainVo.getRiskCode()) && ciLog != null) {
					queryVo.setClaimSeqNo(ciLog.getClaimSeqNo());
					result = handleService.sendCopyInformationToSubrogationSH(queryVo);
				} else if (!Risk.DQZ.equals(cMainVo.getRiskCode()) && biLog != null) {//
					queryVo.setClaimSeqNo(biLog.getClaimSeqNo());
					result = handleService.sendCopyInformationToSubrogationSH(queryVo);
				}
				
				if (result != null && !result.isEmpty()) {
					for (CopyInformationResultVo r : result) {
						resultVoList.add(r);
					}
				}
			}
		}
		
		//???????????????????????????
		if (resultVoList != null && !resultVoList.isEmpty()) {
			for (CopyInformationResultVo resultVo : resultVoList) {
				if (resultVo.getSubrogationViewVo() != null) {
					
					if(StringUtils.isNotBlank(resultVo.getSubrogationViewVo().getBerecoveryInsurerCode()) && resultVo.getSubrogationViewVo().getBerecoveryInsurerCode().endsWith("01")){
						 
						resultVo.getSubrogationViewVo().setBerecoveryInsurerCode(resultVo.getSubrogationViewVo().getBerecoveryInsurerCode().substring(0,resultVo.getSubrogationViewVo().getBerecoveryInsurerCode().length()-2 ));
						
					 }else if(StringUtils.isNotBlank(resultVo.getSubrogationViewVo().getBerecoveryInsurerCode())){
						 resultVo.getSubrogationViewVo().setBerecoveryInsurerCode(resultVo.getSubrogationViewVo().getBerecoveryInsurerCode());
					 }else{
						 
					 }
					if(StringUtils.isNotBlank(resultVo.getSubrogationViewVo().getInsurerCode()) && resultVo.getSubrogationViewVo().getInsurerCode().endsWith("01")){
						 
						resultVo.getSubrogationViewVo().setInsurerCode(resultVo.getSubrogationViewVo().getInsurerCode().substring(0,resultVo.getSubrogationViewVo().getInsurerCode().length()-2));
						
					 }else if(StringUtils.isNotBlank(resultVo.getSubrogationViewVo().getInsurerCode())){
						 resultVo.getSubrogationViewVo().setInsurerCode(resultVo.getSubrogationViewVo().getInsurerCode());
					 }else{
						 
					 }
					subrogationList.add(resultVo.getSubrogationViewVo());
				}
			}
		}
		
		String msg = "";
		if (resultVoList.size() == 0 || !"1".equals(resultVoList.get(0).getResponseCode())) {
			msg = resultVoList.size() == 0 ? "?????????????????????????????????" : resultVoList.get(0).getErrorMessage();
			throw new IllegalArgumentException("==????????????????????????==???" + msg);
		}
		
		//????????????
		ResultPage<CopyInformationSubrogationViewVo> resultPage2 = new ResultPage<CopyInformationSubrogationViewVo>(0,100,subrogationList.size(),subrogationList);
		
		String jsonData = ResponseUtils.toDataTableJson(resultPage2,"recoveryCode",
				"recoveryCodeStatus:RecoveryCodeStatus","lockedTime","coverageCode:DWCoverageType",
				"insurerCode:DWInsurerCode","coverageType:DWCoverageType",
				"berecoveryInsurerCode:DWInsurerCode");
		return jsonData;
	}
	
	/**
	 * ????????????????????????????????????(???????????????????????????  ?????????????????????)
	 * @param claimNo
	 * @return AjaxResult
	 * ???Luwei(2017???3???20??? ??????10:33:23): <br>
	 */
	@RequestMapping("/isInvalidQS.ajax")
	@ResponseBody
	public AjaxResult isInvalidQS_SH(String claimNo) {
		AjaxResult ajaxResult = new AjaxResult();
		boolean result = true;//?????????????????????
		try{
			PrpLRegistVo registVo=new PrpLRegistVo();
			PrpLClaimVo claimVo=new PrpLClaimVo();
			if (StringUtils.isNotEmpty(claimNo)) {
				 claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
				if(claimVo!=null){
					 registVo=registQueryService.findByRegistNo(claimVo.getRegistNo());
				}
					
			}
		  result=isNotBackCompensate(registVo.getComCode(),registVo.getRegistNo(),claimVo.getClaimNo());
			  
			
	}catch(Exception e){
			// TODO Auto-generated catch block
			logger.info("???????????????????????????????????????????????????????????????");
//			e.printStackTrace();
			result = true;
		}
		ajaxResult.setData(result);
		return ajaxResult;
	}
	
	@RequestMapping("/DWMoreMessage.do")
	public ModelAndView DWMoreMessage(String recoveryCode)throws Exception{
		ModelAndView mv=new ModelAndView();
		CiClaimPlatformLogVo biciLog=null;
		PrpLPlatLockVo prpLPlatLockVo =handleService.findPlatLockByRecoveryCode(recoveryCode);
		List<CopyInformationResultVo> resultVoList = new ArrayList<CopyInformationResultVo>();
		CopyInformationSubrogationViewVo copyInformationSubrogationViewVo=new CopyInformationSubrogationViewVo();
		SubrogationSHQueryVo queryVo = new SubrogationSHQueryVo();
		String comCode = WebUserUtils.getComCode();
		if(prpLPlatLockVo!=null){
			List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(prpLPlatLockVo.getRegistNo());
			if (cMainVoList != null && !cMainVoList.isEmpty()) {
				comCode = cMainVoList.get(0).getComCode();
			}
		}
		
		if(prpLPlatLockVo!=null){
			if("1101".equals(prpLPlatLockVo.getRiskCode())){
				//??????????????????
				//??????
				biciLog = platformLogService.findLogByBussNo(RequestType.RegistInfoCI_SH.getCode(),prpLPlatLockVo.getRegistNo(),comCode);
			}else{
				//??????
				biciLog = platformLogService.findLogByBussNo(RequestType.RegistInfoBI_SH.getCode(),prpLPlatLockVo.getRegistNo(),comCode);
			}
			if(biciLog!=null){
				queryVo.setRegistNo(prpLPlatLockVo.getRegistNo());
				queryVo.setClaimSeqNo(biciLog.getClaimSeqNo());
				queryVo.setComCode(comCode);
				resultVoList = handleService.sendCopyInformationToSubrogationSH(queryVo);
				if(resultVoList!=null && resultVoList.size()>0){
					for(CopyInformationResultVo vo:resultVoList){
						if(StringUtils.isNotBlank(vo.getSubrogationViewVo().getRecoveryCode())){
							if(vo.getSubrogationViewVo().getRecoveryCode().equals(prpLPlatLockVo.getRecoveryCode())){
								copyInformationSubrogationViewVo=vo.getSubrogationViewVo();
								
								
							}
						}
					}
				}
			}
			
		}
		/*List<CopyInformationCheckListVo> vos=new ArrayList<CopyInformationCheckListVo>();
		CopyInformationCheckListVo vo =new CopyInformationCheckListVo();
		CopyInformationCheckListVo vo1 =new CopyInformationCheckListVo();
		
		List<CopyInformationCheckDetailListVo> bos=new ArrayList<CopyInformationCheckDetailListVo>();
		CopyInformationCheckDetailListVo bo=new CopyInformationCheckDetailListVo();
		bo.setCheckOpinion("0");
		CopyInformationCheckDetailListVo bo1=new CopyInformationCheckDetailListVo();
		bo1.setCheckOpinion("1");
		bos.add(bo);
		bos.add(bo1);
		vo.setCheckDetailListVo(bos);
		vo1.setCheckDetailListVo(bos);
		vo.setCheckStats("1");
		vo1.setCheckStats("2");
		vos.add(vo);
		vos.add(vo1);
		copyInformationSubrogationViewVo.setCheckListVo(vos);*/
		mv.addObject("copyInformationSubrogationViewVo",copyInformationSubrogationViewVo);
		
		mv.setViewName("subrogationsh/DWMoreMessage");
		
		return mv;
	}
	
	/* ?????????????????????????????????????????????????????????????????????????????????????????????????????????
	 ?????????????????????????????????????????????????????????*/
public boolean isNotBackCompensate(String comCode,String registNo,String claimNo){
		
	
	boolean backCompensate=true;//?????????????????????????????????--true-????????????false--??????
	if(StringUtils.isNotBlank(comCode)){
		if(comCode.startsWith("22")){
			   PrpLClaimVo prpLclaimVo=null;
			    if(StringUtils.isNotBlank(claimNo) && StringUtils.isNotBlank(registNo)){
				   prpLclaimVo=claimTaskService.findClaimVoByClaimNo(claimNo);
				   String requestType="";
				   if(prpLclaimVo!=null){
					   if("1101".equals(prpLclaimVo.getRiskCode())){
						   requestType=RequestType.RegistInfoCI_SH.getCode();
					   }else{
						   requestType=RequestType.RegistInfoBI_SH.getCode();
					   }
				   }
				   //??????????????????
				   CiClaimPlatformLogVo formLogVo = platformLogService.findLogByBussNo(requestType,registNo,comCode);
				   
				   if (formLogVo == null || StringUtils.isEmpty(formLogVo.getClaimSeqNo())) {
						throw new IllegalArgumentException("?????????????????????????????????,?????????");
					}
				   SubrogationSHQueryVo queryVo = new SubrogationSHQueryVo();
				    queryVo.setComCode(comCode);
					queryVo.setRegistNo(registNo);
					queryVo.setClaimSeqNo(formLogVo.getClaimSeqNo());
					
					//????????????????????? -- ??????????????????????????????????????????
					List<CopyInformationResultVo> resultVo;
					try{
						resultVo =subrogationSHHandleService.sendCopyInformationToSubrogationSH(queryVo);
						List<PrpLPlatLockVo> platLockVos=subrogationService.findPlatLockVoByPayFlag(registNo);
						if(resultVo!=null && resultVo.size()>0){
							for(CopyInformationResultVo vo:resultVo){
								if(vo.getSubrogationViewVo()!=null){
									if(platLockVos!=null && platLockVos.size()>0){
										for(PrpLPlatLockVo platLockVo:platLockVos){
											if(prpLclaimVo!=null){//?????????
												if("1101".equals(prpLclaimVo.getRiskCode()) && "1101".equals(platLockVo.getRiskCode())){
													if(StringUtils.isNotBlank(vo.getSubrogationViewVo().getRecoveryCode()) && StringUtils.isNotBlank(platLockVo.getRecoveryCode())){
														if(vo.getSubrogationViewVo().getRecoveryCode().equals(platLockVo.getRecoveryCode())){
															if(!(vo.getSubrogationViewVo().getRecoveryCodeStatus().equals(platLockVo.getRecoveryCodeStatus()))){
																backCompensate=false;
															}
														}
													}
													
												}else{//?????????
													if(StringUtils.isNotBlank(vo.getSubrogationViewVo().getRecoveryCode()) && StringUtils.isNotBlank(platLockVo.getRecoveryCode())){
														if(vo.getSubrogationViewVo().getRecoveryCode().equals(platLockVo.getRecoveryCode())){
															if(!(vo.getSubrogationViewVo().getRecoveryCodeStatus().equals(platLockVo.getRecoveryCodeStatus()))){
																backCompensate=false;
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
						
					}catch(Exception e){
						logger.info("???????????????????????????????????????????????????????????????");
						backCompensate=true;
					}
					
				   
			   }
		    }
		}
	
		return backCompensate;
	}
}
