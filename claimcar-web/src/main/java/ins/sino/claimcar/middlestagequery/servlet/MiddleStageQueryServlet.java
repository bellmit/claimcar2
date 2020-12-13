package ins.sino.claimcar.middlestagequery.servlet;

import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.DataUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.LossParty;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyItemVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.endcase.service.EndCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.jiangxicourt.web.action.BaseServlet;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropFeeVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.middlestagequery.vo.CarDefloss;
import ins.sino.claimcar.middlestagequery.vo.Compensates;
import ins.sino.claimcar.middlestagequery.vo.DocCollectGuides;
import ins.sino.claimcar.middlestagequery.vo.Payments;
import ins.sino.claimcar.middlestagequery.vo.ReqBodyVo;
import ins.sino.claimcar.middlestagequery.vo.RespBodyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

/**
 * 中台案件详情查询
 */
public class MiddleStageQueryServlet extends BaseServlet {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(MiddleStageQueryServlet.class);
    
	@Autowired
	RegistService registService;
	@Autowired
	PrpLCMainService prplCMainService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	ClaimService claimService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private PropTaskService propTaskService;
	@Autowired
	private PersTraceService persTraceService;
    @Autowired
	private ClaimTaskService claimTaskService;
    @Autowired
    private EndCaseService endCaseService;
    @Autowired
    private CertifyService certifyService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	/** 
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ReqBodyVo reqBodyVo = new ReqBodyVo();
		JSONObject jsonResp = new JSONObject();
		
		try {
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
			StringBuilder responseBuilder = new StringBuilder();
			String inputStr = null;
			while((inputStr = streamReader.readLine()) != null){
				responseBuilder.append(inputStr);
			}
			JSONObject jsonObj = JSONObject.parseObject(responseBuilder.toString());
			JSONObject jsonMessage = jsonObj.getJSONObject("message");
			JSONObject jsonBody = jsonMessage.getJSONObject("body");
			String registNo = jsonBody.get("registNo").toString();
			
//			reqBodyVo = jsonBody.toJavaObject(ReqBodyVo.class);
//			if(reqBodyVo != null){
//				System.out.println(reqBodyVo.getRegistNo());
//			}
			if(registNo != null){
				reqBodyVo.setRegistNo(registNo);
			}else{
				jsonResp.put("Status", "500");
				jsonResp.put("Message", "报案号为空！");
				logger.info("返回信息： " + jsonResp.toString());
			}
			try {
				jsonResp = this.middleStageQuery(reqBodyVo);
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PrintWriter out = null;
		try {
			out = resp.getWriter();
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json");
			out.print(jsonResp.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
	
	public JSONObject middleStageQuery(ReqBodyVo reqBodyVos) throws IOException, TimeoutException{
		
		JSONObject json = new JSONObject();
		RespBodyVo respBodyVo = new RespBodyVo();
		ReqBodyVo reqBodyVo = new ReqBodyVo();
		reqBodyVo.setRegistNo(reqBodyVos.getRegistNo());
//		reqBodyVo.setClaimNo(reqBodyVos.getClaimNo());
		List<CarDefloss> carDefloss = new ArrayList<CarDefloss>();
		List<Compensates> compensates = new ArrayList<Compensates>();
		List<DocCollectGuides> docCollectGuidesList = new ArrayList<DocCollectGuides>();
		List<Payments> payments = new ArrayList<Payments>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		PrpLRegistVo registVo = registService.findRegistByRegistNo(reqBodyVo.getRegistNo());
		if(registVo != null){
			List<PrpLCMainVo> cmainVo = prplCMainService.findPrpLCMainsByRegistNo(reqBodyVo.getRegistNo());
			if(cmainVo != null && cmainVo.size() > 0){
				for(PrpLCMainVo vo : cmainVo){
					if("1101".equals(vo.getRiskCode())){
						respBodyVo.setCiPolicyNo(vo.getPolicyNo());
						respBodyVo.setCiRiskCode(vo.getRiskCode());
					}else{
						respBodyVo.setPolicyNo(vo.getPolicyNo());
						respBodyVo.setRiskCode(vo.getRiskCode());
					}
				}
			}
			if(registVo != null){
				respBodyVo.setDamageTime(registVo.getDamageTime()!= null ? format.format(registVo.getDamageTime()):"");
				if(StringUtils.isNotBlank(registVo.getDamageCode())){
					respBodyVo.setDamageDetail(codeTranService.findCodeName("DamageCode", registVo.getDamageCode()));
				}
				if(StringUtils.isNotBlank(registVo.getDamageAddress())){
					respBodyVo.setDamageAddress(registVo.getDamageAddress());
				}
				respBodyVo.setReportTime(registVo.getReportTime() != null ? format.format(registVo.getReportTime()):"");
				//报案方式
				if(StringUtils.isNotBlank(registVo.getSelfClaimFlag()) && "1".equals(registVo.getSelfClaimFlag())){
					respBodyVo.setReportMode("1"); //自助理赔（鼎e赔）
				}else if(StringUtils.isNotBlank(registVo.getIsQuickCase()) && "1".equals(registVo.getIsQuickCase())){
					respBodyVo.setReportMode("2"); //河南快赔
				}else{
					respBodyVo.setReportMode("3"); //电话中心
				}
				
				if(StringUtils.isNotBlank(registVo.getReportorName())){
					respBodyVo.setReportName(registVo.getReportorName());
				}
				if(StringUtils.isNotBlank(registVo.getLinkerName())){
					respBodyVo.setLinkerName(registVo.getLinkerName());
				}
				if(StringUtils.isNotBlank(registVo.getLinkerPhone())){
					respBodyVo.setLinkerPhone(registVo.getLinkerPhone());
				}	
				if(StringUtils.isNotBlank(registVo.getDriverName())){
					respBodyVo.setDriverName(registVo.getDriverName());
				}
			}
			PrpLRegistExtVo registExtVo = registQueryService.getPrpLRegistExtInfo(reqBodyVo.getRegistNo());
			if(registExtVo != null){
				if(StringUtils.isNotBlank(registExtVo.getDangerRemark())){
					respBodyVo.setDamageRemark(registExtVo.getDangerRemark());
				}
			}
			
			//案件状态（报案：Regis；查勘：Check；理算：Compe；结案：EndCas；注销/拒赔：cance）、处理人、处理时间
			List<PrpLClaimVo> claimVoList = claimService.findClaimListByRegistNo(reqBodyVo.getRegistNo());
			boolean canceFlag = false;
			if(claimVoList != null && claimVoList.size() > 0){
				for(PrpLClaimVo vo : claimVoList){
					//0:注销，2：拒赔
					if(StringUtils.isNotBlank(vo.getValidFlag()) && ("0".equals(vo.getValidFlag()) ||"2".equals(vo.getValidFlag()))){
						respBodyVo.setCaseStatus("90");
						List<PrpLWfTaskVo> wftaskoutList = wfTaskHandleService.findPrpLWfTaskOutTimeDescByRegistNo(reqBodyVo.getRegistNo());
						if(wftaskoutList != null && wftaskoutList.size() > 0){
							if(wftaskoutList.get(0).getHandlerTime() != null){
								respBodyVo.setHandleTime(format.format(wftaskoutList.get(0).getHandlerTime()));
							}else{
								if(wftaskoutList.get(0).getTaskInTime() != null){
									respBodyVo.setHandleTime(format.format(wftaskoutList.get(0).getTaskInTime()));
								}
							}
							if(StringUtils.isNotBlank(wftaskoutList.get(0).getHandlerUser())){
								respBodyVo.setHandleUser(wftaskoutList.get(0).getHandlerUser());
							}else{
								if(StringUtils.isNotBlank(wftaskoutList.get(0).getAssignUser())){
									respBodyVo.setHandleUser(wftaskoutList.get(0).getAssignUser());
								}
							}
						}
						canceFlag = true;
					}
				}
			}
			if(canceFlag){
				logger.info("报案号： " + reqBodyVo.getRegistNo() + " 已注销或拒赔！");
			}else{
				List<PrpLWfTaskVo> wftaskoutLists = wfTaskHandleService.findPrpLWfTaskOutTimeDescByRegistNo(reqBodyVo.getRegistNo());
				if(wftaskoutLists != null && wftaskoutLists.size() > 0){
					for(PrpLWfTaskVo vo : wftaskoutLists){
						if(StringUtils.isNotBlank(vo.getHandlerStatus()) && "3".equals(vo.getHandlerStatus())){
							if("EndCas".equals(vo.getNodeCode())){
								respBodyVo.setCaseStatus("7");
								if(StringUtils.isNotBlank(vo.getHandlerUser())){
									respBodyVo.setHandleUser(vo.getHandlerUser());
								}else{
									if(StringUtils.isNotBlank(vo.getAssignUser())){
										respBodyVo.setHandleUser(vo.getAssignUser());
									}
								}
								if(vo.getHandlerTime() != null){
									respBodyVo.setHandleTime(format.format(vo.getHandlerTime()));
								}else{
									if(vo.getTaskInTime() != null){
										respBodyVo.setHandleTime(format.format(vo.getTaskInTime()));
									}
								}
								break;
							}else if("Compe".equals(vo.getNodeCode())){
								respBodyVo.setCaseStatus("5");
								if(StringUtils.isNotBlank(vo.getHandlerUser())){
									respBodyVo.setHandleUser(vo.getHandlerUser());
								}else{
									if(StringUtils.isNotBlank(vo.getAssignUser())){
										respBodyVo.setHandleUser(vo.getAssignUser());
									}
								}
								if(vo.getHandlerTime() != null){
									respBodyVo.setHandleTime(format.format(vo.getHandlerTime()));
								}else{
									if(vo.getTaskInTime() != null){
										respBodyVo.setHandleTime(format.format(vo.getTaskInTime()));
									}
								}
								break;
							}else if("Check".equals(vo.getNodeCode())){
								respBodyVo.setCaseStatus("3");
								if(StringUtils.isNotBlank(vo.getHandlerUser())){
									respBodyVo.setHandleUser(vo.getHandlerUser());
								}else{
									if(StringUtils.isNotBlank(vo.getAssignUser())){
										respBodyVo.setHandleUser(vo.getAssignUser());
									}
								}
								if(vo.getHandlerTime() != null){
									respBodyVo.setHandleTime(format.format(vo.getHandlerTime()));
								}else{
									if(vo.getTaskInTime() != null){
										respBodyVo.setHandleTime(format.format(vo.getTaskInTime()));
									}
								}
								break;
							}else if("Regis".equals(vo.getNodeCode())){
								respBodyVo.setCaseStatus("1");
								if(StringUtils.isNotBlank(vo.getHandlerUser())){
									respBodyVo.setHandleUser(vo.getHandlerUser());
								}else{
									if(StringUtils.isNotBlank(vo.getAssignUser())){
										respBodyVo.setHandleUser(vo.getAssignUser());
									}
								}
								if(vo.getHandlerTime() != null){
									respBodyVo.setHandleTime(format.format(vo.getHandlerTime()));
								}else{
									if(vo.getTaskInTime() != null){
										respBodyVo.setHandleTime(format.format(vo.getTaskInTime()));
									}
								}
								break;
							}
						}
					}
				}else{
					List<PrpLWfTaskVo> wfTaskinList = wfTaskHandleService.findPrpLWfTaskInTimeDescByRegistNo(reqBodyVo.getRegistNo());
					if(wfTaskinList!= null && wfTaskinList.size() > 0){
						respBodyVo.setCaseStatus("1");
						if(StringUtils.isNotBlank(wfTaskinList.get(0).getHandlerUser())){
							respBodyVo.setHandleUser(wfTaskinList.get(0).getHandlerUser());
						}else{
							if(StringUtils.isNotBlank(wfTaskinList.get(0).getAssignUser())){
								respBodyVo.setHandleUser(wfTaskinList.get(0).getAssignUser());
							}
						}
						if(wfTaskinList.get(0).getHandlerTime() != null){
							respBodyVo.setHandleTime(format.format(wfTaskinList.get(0).getHandlerTime()));
						}else{
							if(wfTaskinList.get(0).getTaskInTime() != null){
								respBodyVo.setHandleTime(format.format(wfTaskinList.get(0).getTaskInTime()));
							}
						}
					}
				}
			}
			
			//从报案时保单车辆信息表获取 车牌号、车架号、Vin码
			List<PrpLCItemCarVo> itemCarVo = policyViewService.findPrpcItemcarByRegistNo(reqBodyVo.getRegistNo());
			if(itemCarVo != null && itemCarVo.size() > 0){
				if(StringUtils.isNotBlank(itemCarVo.get(0).getLicenseNo())){
					respBodyVo.setLicenseNo(itemCarVo.get(0).getLicenseNo());
				}
				if(StringUtils.isNotBlank(itemCarVo.get(0).getFrameNo())){
					respBodyVo.setFrameNo(itemCarVo.get(0).getFrameNo());
				}
				if(StringUtils.isNotBlank(itemCarVo.get(0).getVinNo())){
					respBodyVo.setVinNo(itemCarVo.get(0).getVinNo());
				}else{
					if(StringUtils.isNotBlank(itemCarVo.get(0).getFrameNo())){
						respBodyVo.setVinNo(itemCarVo.get(0).getFrameNo());
					}
				}
				
			}
			//总施救费
			BigDecimal sumRescueFee = BigDecimal.ZERO;
			//总估计赔款（总核损金额）
			BigDecimal claimAmount = BigDecimal.ZERO;
			//车
			List<PrpLDlossCarMainVo> lossCarMainVo = lossCarService.findLossCarMainByRegistNo(reqBodyVo.getRegistNo());
			if(lossCarMainVo != null && lossCarMainVo.size() > 0){
				for(PrpLDlossCarMainVo vo : lossCarMainVo){
					if(StringUtils.isNotBlank(vo.getUnderWriteFlag()) && "1".equals(vo.getUnderWriteFlag())){
						//三者车定损方式为无损失的,定损注销不上传平台
						if(LossParty.THIRD.equals(vo.getDeflossCarType()) && 
								(CodeConstants.CetainLossType.DEFLOSS_NULL.equals(vo.getCetainLossType()) ||
										CodeConstants.VeriFlag.CANCEL.equals(vo.getUnderWriteFlag()))){
							continue; 
						}
						sumRescueFee.add(DataUtils.NullToZero(vo.getSumRescueFee()));
						claimAmount.add(DataUtils.NullToZero(vo.getSumVeriLossFee()));
					}
				}
			}
			
			//财
			List<PrpLdlossPropMainVo> dlossPropMainVoList = propTaskService.findPropMainListByRegistNo(reqBodyVo.getRegistNo());
			if(dlossPropMainVoList != null && dlossPropMainVoList.size() > 0){
				for(PrpLdlossPropMainVo vo : dlossPropMainVoList){
					//注销的不添加
					if(CodeConstants.VeriFlag.CANCEL.equals(vo.getUnderWriteFlag())){
						continue;
					}
					claimAmount.add(DataUtils.NullToZero(vo.getSumVeriLoss()));
					sumRescueFee.add(DataUtils.NullToZero(vo.getVerirescueFee()));
				}
			}
			
			//人
			List<PrpLDlossPersTraceMainVo> dlossPersTraceMainVoList = persTraceService.findPersTraceMainVo(reqBodyVo.getRegistNo());
			if(dlossPersTraceMainVoList != null && dlossPersTraceMainVoList.size() > 0){
				for(PrpLDlossPersTraceMainVo vo : dlossPersTraceMainVoList){
					if(StringUtils.isNotBlank(vo.getCaseProcessType()) && !"10".equals(vo.getCaseProcessType())){
						if(StringUtils.isNotBlank(vo.getUnderwriteFlag()) && 
								("1".equals(vo.getUnderwriteFlag()) || "2".equals(vo.getUnderwriteFlag()))){
							List<PrpLDlossPersTraceVo> persTraceVoList = vo.getPrpLDlossPersTraces();
							if(persTraceVoList != null && persTraceVoList.size() > 0){
								for(PrpLDlossPersTraceVo voo : persTraceVoList){
									if("1".equals(voo.getValidFlag())){
										claimAmount.add(new BigDecimal(vo.getSumVeriDefloss() != null ? vo.getSumVeriDefloss() : "0.0"));
									}
								}
							}
						}
					}
				}
			}
			//总估计赔款（总核损金额）
			respBodyVo.setSumClaim(DataUtils.NullToZero(claimAmount).toString());
			//总施救费
			respBodyVo.setSumRescueFee(DataUtils.NullToZero(sumRescueFee).toString());
			
			//结案时间、注销时间、拒赔时间
			List<PrpLClaimVo> claimVo = claimTaskService.findClaimListByRegistNo(reqBodyVo.getRegistNo(), "1");
			List<PrpLEndCaseVo> endCaseVo = endCaseService.findEndCaseVo(reqBodyVo.getRegistNo());
			if(endCaseVo != null && endCaseVo.size() > 0){
				for(PrpLEndCaseVo vo : endCaseVo){
					if(vo.getEndCaseDate() != null){
						respBodyVo.setEndCaseTime(format.format(vo.getEndCaseDate()));
						break;
					}else{
						if(claimVo != null && claimVo.size() > 0){
							for(PrpLClaimVo voo : claimVo){
								if(voo.getEndCaseTime() != null){
									respBodyVo.setEndCaseTime(format.format(voo.getEndCaseTime()));
									break;
								}
							}
						}
					}
				}
			}
					
			if(claimVo != null && claimVo.size() > 0){
				for(PrpLClaimVo vo : claimVo){
					if(vo.getCancelTime() != null){
						respBodyVo.setLogOffTime(format.format(vo.getCancelTime()));
						respBodyVo.setCancelTime(format.format(vo.getCancelTime()));
					}
				}
			}
			
			//单证收集
			List<PrpLCertifyItemVo> itemVoList = new ArrayList<PrpLCertifyItemVo>();
			List<PrpLCertifyDirectVo> directVoList = new ArrayList<PrpLCertifyDirectVo>();
			PrpLCertifyMainVo certifyMainVo = certifyService.findPrpLCertifyMainVo(reqBodyVo.getRegistNo());
			if(certifyMainVo != null){
				itemVoList = certifyMainVo.getPrpLCertifyItems();
				if(itemVoList != null && itemVoList.size() > 0){
					for(PrpLCertifyItemVo vo : itemVoList){
						directVoList = vo.getPrpLCertifyDirects();
						if(directVoList != null && directVoList.size() > 0){
							for(PrpLCertifyDirectVo voo : directVoList){
								DocCollectGuides guides = new DocCollectGuides();
								if(StringUtils.isNotBlank(voo.getLossItemCode())){
									guides.setDocCode(voo.getLossItemCode());
								}
								if(StringUtils.isNotBlank(voo.getLossItemName())){
									guides.setDocName(voo.getLossItemName());
								}
								docCollectGuidesList.add(guides);
							}
						}
					}
				}
				
				if(StringUtils.isNotBlank(certifyMainVo.getPolicyNo()) && cmainVo != null && cmainVo.size() > 0){
					for(PrpLCMainVo vo : cmainVo){
						if(vo.getPolicyNo().equals(certifyMainVo.getPolicyNo())){
							if(docCollectGuidesList != null && docCollectGuidesList.size() > 0){
								for(int i = 0; i < docCollectGuidesList.size(); i++){
									docCollectGuidesList.get(i).setPolicyNo(vo.getPolicyNo());
									docCollectGuidesList.get(i).setRiskCode(vo.getRiskCode());
									docCollectGuidesList.get(i).setRegistNo(vo.getRegistNo());
									docCollectGuidesList.get(i).setSerialNo(String.valueOf(i));
								}
							}
						}
					}
				}
				respBodyVo.setDocCollectGuides(docCollectGuidesList);
			}
			//赔款信息
			List<PrpLCompensateVo> compensateVoList = compensateTaskService.queryCompensate(reqBodyVo.getRegistNo(), "N");
			if(compensateVoList != null && compensateVoList.size() > 0){
				for(PrpLCompensateVo vo : compensateVoList){
					if(StringUtils.isNotBlank(vo.getUnderwriteFlag()) && "1".equals(vo.getUnderwriteFlag())){
						Compensates compensate = new Compensates();
						compensate.setRegistNo(vo.getRegistNo());
						if(StringUtils.isNoneBlank(vo.getClaimNo())){
							compensate.setClaimNo(vo.getClaimNo());
						}
						if(StringUtils.isNoneBlank(vo.getPolicyNo())){
							compensate.setPolicyNo(vo.getPolicyNo());
						}
						if(StringUtils.isNoneBlank(vo.getCurrency())){
							compensate.setCurrency(vo.getCurrency());
						}
						if(StringUtils.isNotBlank(vo.getLcText())){
							compensate.setCompensateText(vo.getLcText());
						}
						compensate.setSumAmt(DataUtils.NullToZero(vo.getSumAmt()).toString());
						
						compensates.add(compensate);
						
						//赔款支付信息
						Payments payment = new Payments();
						List<PrpLPaymentVo> paymentList = vo.getPrpLPayments();
						if(paymentList != null && paymentList.size() > 0){
							for(PrpLPaymentVo payMent : paymentList){
								PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(payMent.getPayeeId());
								if(payCustomVo != null){
									if(StringUtils.isNotBlank(payCustomVo.getAccountNo())){
										payment.setAccountNo(payCustomVo.getAccountNo());
									}
									if(StringUtils.isNotBlank(payCustomVo.getPayeeName())){
										payment.setAccountName(payCustomVo.getPayeeName());
									}
								}
								if(StringUtils.isNoneBlank(vo.getCurrency())){
									payment.setCurrency(vo.getCurrency());
								}
								payment.setSumRealPay(DataUtils.NullToZero(payMent.getSumRealPay()).toString());
								if(payMent.getPayTime() != null && !"".equals(payMent.getPayTime())){
									payment.setPayTime(format.format(payMent.getPayTime()));
								}
								payments.add(payment);
							}
						}
					}
					
				}
				respBodyVo.setCompensates(compensates);
				respBodyVo.setPayments(payments);
			}
			//车辆核定损失情况
			if(lossCarMainVo != null && lossCarMainVo.size() > 0){
				for(PrpLDlossCarMainVo vo : lossCarMainVo){
					CarDefloss defloss = new CarDefloss();
					if(StringUtils.isNotBlank(vo.getUnderWriteFlag()) && "1".equals(vo.getUnderWriteFlag()) && 
							vo.getDeflossCarType() != null && "1".equals(vo.getDeflossCarType())){
						defloss.setDeflossType("本车");
						if(vo.getPrpLDlossCarComps() != null && vo.getPrpLDlossCarComps().size() > 0){
							for(PrpLDlossCarCompVo compVo : vo.getPrpLDlossCarComps()){
								if(StringUtils.isNotBlank(compVo.getCompName())){
									defloss.setDeflossName(compVo.getCompName());
								}
								
								defloss.setDeflossNum(DataUtils.NullToZeroInt(compVo.getVeriQuantity()).toString());
								defloss.setDeflossSumAmt(DataUtils.NullToZero(compVo.getSumVeriLoss()).toString());
								//使用修理厂价格
								defloss.setDeflossPrice(DataUtils.NullToZero(compVo.getRepairFactoryFee()).toString());
							}
						}
					}else if(StringUtils.isNotBlank(vo.getUnderWriteFlag()) && "1".equals(vo.getUnderWriteFlag()) && 
							vo.getDeflossCarType() != null && "3".equals(vo.getDeflossCarType())){
						defloss.setDeflossType("三者车");
						if(vo.getPrpLDlossCarComps() != null && vo.getPrpLDlossCarComps().size() > 0){
							for(PrpLDlossCarCompVo compVo : vo.getPrpLDlossCarComps()){
								if(StringUtils.isNotBlank(compVo.getCompName())){
									defloss.setDeflossName(compVo.getCompName());
								}
								
								defloss.setDeflossNum(DataUtils.NullToZeroInt(compVo.getVeriQuantity()).toString());
								defloss.setDeflossSumAmt(DataUtils.NullToZero(compVo.getSumVeriLoss()).toString());
								//使用修理厂价格
								defloss.setDeflossPrice(DataUtils.NullToZero(compVo.getRepairFactoryFee()).toString());
							}
						}
					}
					carDefloss.add(defloss);
				}
				respBodyVo.setCarDefloss(carDefloss);
			}
			//财产
			if(dlossPropMainVoList != null && dlossPropMainVoList.size() > 0){
				for(PrpLdlossPropMainVo propMain : dlossPropMainVoList){
					if(StringUtils.isNotBlank(propMain.getUnderWriteFlag()) && "1".equals(propMain.getUnderWriteFlag())){
						List<PrpLdlossPropFeeVo> propFeeList = propMain.getPrpLdlossPropFees();
						if(propFeeList != null && propFeeList.size() > 0){
							for(PrpLdlossPropFeeVo propFee : propFeeList){
								CarDefloss deflosses = new CarDefloss();
								if("1".equals(propFee.getValidFlag())){
									deflosses.setDeflossType("财产损失");
									if(StringUtils.isNotBlank(propFee.getLossItemName())){
										deflosses.setDeflossName(propFee.getLossItemName());
									}
									deflosses.setDeflossNum(DataUtils.NullToZero(propFee.getVeriLossQuantity()).toString());
									deflosses.setDeflossPrice(DataUtils.NullToZero(propFee.getVeriUnitPrice()).toString());
									deflosses.setDeflossSumAmt(DataUtils.NullToZero(propFee.getSumVeriLoss()).toString());
									
									carDefloss.add(deflosses);
								}
							}
						}
					}
				}
				respBodyVo.setCarDefloss(carDefloss);
			}
			//人伤
			if(dlossPersTraceMainVoList != null && dlossPersTraceMainVoList.size() > 0){
				for(PrpLDlossPersTraceMainVo persTraceMain : dlossPersTraceMainVoList){
					if(persTraceMain.getUnderwriteFlag() != null && ("1".equals(persTraceMain.getUnderwriteFlag()) || "2".equals(persTraceMain.getUnderwriteFlag()))){
						Long id = persTraceMain.getId();
						List<PrpLDlossPersTraceVo> persTraceVoList = persTraceService.findPersTraceVo(reqBodyVo.getRegistNo(), id);
						if(persTraceVoList != null && persTraceVoList.size() > 0){
							for(PrpLDlossPersTraceVo vo : persTraceVoList ){
								List<PrpLDlossPersTraceFeeVo> persTraceFeeList = vo.getPrpLDlossPersTraceFees();
								if(persTraceFeeList != null && persTraceFeeList.size() > 0){
									for(PrpLDlossPersTraceFeeVo feeVo : persTraceFeeList){
										CarDefloss defLoss = new CarDefloss();
										if("1".equals(feeVo.getStatus())){
											defLoss.setDeflossType("人伤损失");
											if(StringUtils.isNotBlank(feeVo.getFeeTypeName())){
												defLoss.setDeflossName(feeVo.getFeeTypeName());
											}
											defLoss.setDeflossNum(DataUtils.NullToZero(feeVo.getVeriQuantity()).toString());
											defLoss.setDeflossPrice(DataUtils.NullToZero(feeVo.getVeriUnitAmount()).toString());
											defLoss.setDeflossSumAmt(DataUtils.NullToZero(feeVo.getVeriDefloss()).toString());
											
											carDefloss.add(defLoss);
										}
									}
								}
							}
						}
					}
				}
				respBodyVo.setCarDefloss(carDefloss);
			}
			json.put("Data", respBodyVo);
			json.put("Status", "success");
			logger.info("返回信息： " + json.toString());
			return json;
			
		}else{
			json.put("Status", "500");
			json.put("Message", "该报案号查询结果为空，请确认该报案号是否正确！");
			logger.info("返回信息： " + json.toString());
			return json;
		}

	}
}


