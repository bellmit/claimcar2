package ins.sino.claimcar.reinsurance.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.service.CodeTranService;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.spring.CodeTranServiceSpringImpl;
import ins.platform.utils.DataUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.ExpType;
import ins.sino.claimcar.CodeConstants.YN01;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.po.PrpLCompensate;
import ins.sino.claimcar.claim.po.PrplPayeeKindPayment;
import ins.sino.claimcar.claim.po.PrplReinsTrace;
import ins.sino.claimcar.claim.service.ClaimToReinsuranceService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindHisVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.claim.vo.PrplPayeeKindPaymentVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.reinsurance.vo.ClaimVo;
import ins.sino.claimcar.reinsurance.vo.PrpLChargeList;
import ins.sino.claimcar.reinsurance.vo.PrpLLossVo;
import ins.sino.claimcar.reinsurance.vo.PrpLpersonLossVo;
import ins.sino.claimcar.reinsurance.vo.ReinsCaseStatusVO;
import ins.sino.claimcar.reinsurance.webservice.CallReinsWebService;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * ??????????????????
 * 
 * @author ???NiuQian
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path("ClaimToReinsuranceService")
public class ClaimToReinsuranceServiceImpl implements ClaimToReinsuranceService {

	@Autowired
	private CallReinsWebService callReinsWebService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	RegistQueryService registService;
	@Autowired
	CodeTranServiceSpringImpl codeTranServiceSpringImpl;
	
	@Autowired
	AssessorService assessorService;
	
	@Autowired
	private DatabaseDao databaseDao;
	
	@Autowired
	ClaimInterfaceLogService logService;
	
	@Autowired
	private CompensateService compensateService;
	
	@Autowired
	VerifyClaimService verifyClaimService;
	
	@Autowired
	private BaseDaoService baseDaoService;

	private static Logger logger = LoggerFactory.getLogger(ClaimToReinsuranceServiceImpl.class);

	@Override
	public void TransDataForClaimVo(PrpLClaimVo claimVo,List<PrpLClaimKindHisVo> kindHisVoList,
									ClaimInterfaceLogVo claimInterfaceLogVo) throws Exception {
		ClaimVo ClaimDto = new ClaimVo();
		if(claimVo==null||kindHisVoList==null){
			logger.debug("claimVo??????kindHisVoList???null ?????????????????????????????????????????????????????????");
		}else{
			ins.sino.claimcar.reinsurance.vo.PrpLClaimVo PrpLclaimDto = new ins.sino.claimcar.reinsurance.vo.PrpLClaimVo();
			PrpLCMainVo cmain = policyViewService.findPolicyInfoByPaltform(claimVo.getRegistNo(),claimVo.getPolicyNo());
			PrpLRegistVo registVo = registService.findByRegistNo(claimVo.getRegistNo());
			if(cmain==null){
				logger.debug("cmain???NULL???????????????????????????????????????");
			}
			PrpLclaimDto.setClaimNo(claimVo.getClaimNo());
			PrpLclaimDto.setRiskCode(claimVo.getRiskCode());
			PrpLclaimDto.setRegistNo(claimVo.getRegistNo());
			PrpLclaimDto.setPolicyNo(claimVo.getPolicyNo());
			PrpLclaimDto.setCurrency("CNY");
			PrpLclaimDto.setDamageStartDate(claimVo.getDamageTime());
			PrpLclaimDto.setDamageEndDate(claimVo.getDamageTime());
			PrpLclaimDto.setDamageCode(claimVo.getDamageCode());
			PrpLclaimDto.setAddressCode(registVo.getDamageAreaCode());
			PrpLclaimDto.setAgentName(cmain.getAgentCode());
//			PrpLclaimDto.setAgentName("!");  //
			PrpLclaimDto.setMakeCom(cmain.getMakeCom());
			PrpLclaimDto.setHandlerCode(cmain.getHandlerCode());
			PrpLclaimDto.setInputDate(claimVo.getClaimTime());
//			SysCodeDictVo sys = codeTranServiceSpringImpl.findTransCodeDictVo("AreaCode",claimVo.getDamageAreaCode());
			PrpLclaimDto.setDamageAddress(registVo.getDamageAddress()); // ////// ???????????? **???????????????
			SysCodeDictVo vo = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode",claimVo.getDamageCode());
			if(vo == null){
				vo = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode2",claimVo.getDamageCode());
			}
			PrpLclaimDto.setCodeCName(vo.getCodeName());
			PrpLclaimDto.setDamageKind(claimVo.getRiskCode()); // ///////???????????? ** ???????????? DamageKind ???riskcode
			PrpLclaimDto.setSumClaim(DataUtils.NullToZero(claimVo.getSumClaim()).doubleValue());
			PrpLclaimDto.setDamageAreaPostCode(claimVo.getDamageAreaCode()); // ///////??????????????? ?????? ???????????????????????????
			PrpLclaimDto.setEndCaseFlag("0");
			PrpLclaimDto.setDangerNo(1);

			List<ins.sino.claimcar.reinsurance.vo.PrpLclaimHisVo> claimHisVoList = new ArrayList<ins.sino.claimcar.reinsurance.vo.PrpLclaimHisVo>();
			if(kindHisVoList!=null&& !kindHisVoList.isEmpty()){
				int itemKindNo = 1;

				for(PrpLClaimKindHisVo pp:kindHisVoList){
					if(pp.getClaimLossChg().doubleValue() != 0 && "1".equals(pp.getValidFlag())){
						ins.sino.claimcar.reinsurance.vo.PrpLclaimHisVo pLClaimVo = new ins.sino.claimcar.reinsurance.vo.PrpLclaimHisVo();
						pLClaimVo.setClaimNo(pp.getClaimNo());
						pLClaimVo.setRiskCode(pp.getRiskCode());
						pLClaimVo.setSerialNo(itemKindNo);
						pLClaimVo.setItemKindNo(itemKindNo);
						itemKindNo++ ;
						pLClaimVo.setKindCode(pp.getKindCode());
						String kindName = pp.getKindName();
						if(StringUtils.isBlank(kindName)){
							kindName = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",pp.getKindCode()).getCodeName();
						}
						pLClaimVo.setKindName(kindName);
						pLClaimVo.setItemCode(cmain.getPrpCItemKinds().get(0).getItemCode()); // /////// ???????????? **list
						pLClaimVo.setCurrency("CNY");
						pLClaimVo.setSumClaim(DataUtils.NullToZero(pp.getClaimLoss()).doubleValue());
						pLClaimVo.setInputDate(pp.getAdjustTime());
						pLClaimVo.setLossFeeType(pp.getFeeType()); // /**P ?????????Z ????????????
						pLClaimVo.setItemDetailName(pp.getLossItemName());
						pLClaimVo.setDangerNo(1);
						pLClaimVo.setChgSumClaim(DataUtils.NullToZero(pp.getClaimLossChg()).doubleValue());
						claimHisVoList.add(pLClaimVo);
					}

				}
			}
			ClaimDto.setPrpLClaimVo(PrpLclaimDto);
			ClaimDto.setPrpLclaimHisVo(claimHisVoList);
			claimInterfaceLogVo.setCompensateNo(claimVo.getClaimNo());
			callReinsWebService.callClaimForClient(ClaimDto,claimInterfaceLogVo);
		}

	}

	@Override
	public void TransDataForCompensateVo(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate) throws Exception {
		ins.sino.claimcar.reinsurance.vo.CompensateVo compensateVo = new ins.sino.claimcar.reinsurance.vo.CompensateVo();
		
		// ????????????
		if(claimVo!=null&&prpLCompensate!=null){
			
			//???????????????
			int times = logService.getReinsuranceInterfaceRequestTimes(claimVo.getRegistNo(),prpLCompensate.getCompensateNo());
			
			//???????????????????????????
			List<PrplReinsTrace> reinsTraceList = new ArrayList<PrplReinsTrace>();
			
			// step1:??????????????????????????????????????????????????????????????????????????????????????????
			//???????????????????????????????????? key???kindCode
			Map<String,Double> kindRealpayMap = new HashMap<String,Double>();
			
			// ??????????????????????????????????????????
			calculateKindSumRealPay(kindRealpayMap,prpLCompensate);
			
			// ??????????????????,key???kindCode
			Map<String, BigDecimal> kindPayRate = new HashMap<String, BigDecimal>();
			
			if(kindRealpayMap.get(CodeConstants.ALLKINDSUMREALPAY) != 0.00){
			// ????????????????????????????????????
			    for (String key : kindRealpayMap.keySet()) {
				    kindPayRate.put(key, new BigDecimal(kindRealpayMap.get(key) / kindRealpayMap.get(CodeConstants.ALLKINDSUMREALPAY)));
				}
			}
			
			ins.sino.claimcar.reinsurance.vo.PrpLClaimVo PrpLclaimDto = new ins.sino.claimcar.reinsurance.vo.PrpLClaimVo();
			PrpLCMainVo cmain = policyViewService.findPolicyInfoByPaltform(prpLCompensate.getRegistNo(),
					prpLCompensate.getPolicyNo());
			PrpLclaimDto.setClaimNo(claimVo.getClaimNo());
			PrpLclaimDto.setRiskCode(claimVo.getRiskCode());
			PrpLclaimDto.setRegistNo(claimVo.getRegistNo());
			PrpLclaimDto.setPolicyNo(claimVo.getPolicyNo());
			PrpLclaimDto.setCurrency("CNY");
			PrpLclaimDto.setDamageStartDate(claimVo.getDamageTime());
			PrpLclaimDto.setDamageEndDate(claimVo.getDamageTime());
			PrpLclaimDto.setDamageCode(claimVo.getDamageCode());
			PrpLclaimDto.setAddressCode(claimVo.getDamageAreaCode());
			// PrpLclaimDto.setAgentName(StringUtils.isEmpty(cmain.getAgentCode())?"":cmain.getAgentCode());
			// PrpLclaimDto.setMakeCom(StringUtils.isEmpty(cmain.getComCode())?"":cmain.getComCode());
			if(cmain!=null){
				PrpLclaimDto.setAgentName(cmain.getAgentCode());
				PrpLclaimDto.setMakeCom(cmain.getComCode());
				PrpLclaimDto.setHandlerCode(cmain.getHandlerCode());
			}else{
				PrpLclaimDto.setAgentName("");
				PrpLclaimDto.setMakeCom("");
				PrpLclaimDto.setHandlerCode("");
			}
			PrpLclaimDto.setInputDate(claimVo.getCreateTime());
			PrpLclaimDto.setDamageKind(claimVo.getRiskCode()); // ??????????????? ?????????????????? ** ???????????? DamageKind ???riskcode
			PrpLclaimDto.setSumClaim(DataUtils.NullToZero(claimVo.getSumClaim()).doubleValue());

			SysCodeDictVo sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode",claimVo.getDamageCode());
			if(sys == null){
				sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode2",claimVo.getDamageCode());
			}
			PrpLclaimDto.setCodeCName(sys.getCodeName());
			PrpLclaimDto.setDamageAreaPostCode(claimVo.getDamageAreaCode()); // ??????????????? ????????????
			PrpLclaimDto.setEndCaseFlag("0"); // 15869 ????????????????????????????????????????????????  2019-12-25 11:55:24
			PrpLclaimDto.setDangerNo(1);

			// ??????????????????
			ins.sino.claimcar.reinsurance.vo.PrpLCompensateVo prpLcompensateDto = new ins.sino.claimcar.reinsurance.vo.PrpLCompensateVo();
			prpLcompensateDto.setCompensateNo(prpLCompensate.getCompensateNo());
			prpLcompensateDto.setCaseNo(prpLCompensate.getCaseNo());
			System.out.println(prpLCompensate.getRiskCode());
			if(prpLCompensate!=null&&prpLCompensate.getRiskCode()!=null&&prpLCompensate.getRiskCode().length()>=2){
				prpLcompensateDto.setclassCode(prpLCompensate.getRiskCode().substring(0,2));
			}else{
				prpLcompensateDto.setclassCode("");
			}

			prpLcompensateDto.setRiskCode(prpLCompensate.getRiskCode());
			prpLcompensateDto.setClaimNo(prpLCompensate.getClaimNo());
			prpLcompensateDto.setPolicyNo(prpLCompensate.getPolicyNo());
			prpLcompensateDto.setCurrency("CNY");
			prpLcompensateDto.setSumLoss(DataUtils.NullToZero(prpLCompensate.getSumLoss()).doubleValue());
			prpLcompensateDto.setMakeCom(prpLCompensate.getMakeCom());
			prpLcompensateDto.setComCode(prpLCompensate.getComCode());
			prpLcompensateDto.setHandler1Code(prpLCompensate.getHandler1Code());
			prpLcompensateDto.setInputDate(prpLCompensate.getCreateTime());
			prpLcompensateDto.setUnderWriteEndDate(claimVo.getDamageTime());
			prpLcompensateDto.setUnderWriteFlag("1");
			prpLcompensateDto.setEndcaseDate(prpLCompensate.getUnderwriteDate());

			// ?????????????????????????????????
			List<PrpLLossVo> prpLlossList = new ArrayList<PrpLLossVo>();
			List<PrpLLossItemVo> prpLLossItemVo = prpLCompensate.getPrpLLossItems();
			if(prpLLossItemVo!=null&&prpLLossItemVo.size()>0){ // ????????????
				int serialNo = 1;
				for(PrpLLossItemVo prpLLoss:prpLLossItemVo){
					PrpLLossVo lossItem = new PrpLLossVo();
					lossItem.setCompensateNo(prpLCompensate.getCompensateNo());
					lossItem.setRiskCode(prpLLoss.getRiskCode());
					lossItem.setPolicyNo(prpLLoss.getPolicyNo());
					lossItem.setSerialNo(serialNo);
					lossItem.setKindCode(prpLLoss.getKindCode());
					SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",
							prpLLoss.getKindCode());
					lossItem.setKindName(sysVo.getCodeName());
					lossItem.setItemCode(prpLLoss.getItemId());
					lossItem.setLossName(prpLLoss.getItemName());
					lossItem.setSumLoss(DataUtils.NullToZero(prpLLoss.getSumLoss()).doubleValue());
					lossItem.setCurrency("CNY");
					//lossItem.setSumRealPay(DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue());
					// ???????????????????????????????????????????????????????????? ???????????????????????????????????????lossType???????????????lossId???????????????????????????
					lossItem.setSumRealPay(splitTaxFromPayment(prpLLoss.getKindCode(),DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue(),DataUtils.NullToZero(prpLLoss.getOffPreAmt()).doubleValue(), kindRealpayMap,kindPayRate, prpLCompensate)
							- addTaxFromPrepay(prpLCompensate.getRegistNo(),prpLCompensate.getRiskCode(), prpLLoss.getId().toString()));

					lossItem.setExceptDeductiblePay(0);
					lossItem.setDangerNo(1);
					prpLlossList.add(lossItem);
					
					//??????????????????????????????
					PrplReinsTrace prplReinsTrace = new PrplReinsTrace();
					prplReinsTrace.setRegistNo(claimVo.getRegistNo());
					prplReinsTrace.setRiskCode(claimVo.getRiskCode());
					prplReinsTrace.setKindCode(prpLLoss.getKindCode());
					prplReinsTrace.setLossId(prpLLoss.getId());
					prplReinsTrace.setSerialNo(new Long((long)serialNo));
					prplReinsTrace.setLossType(ExpType.CAR);
					prplReinsTrace.setTimes(new Long((long)times));
					prplReinsTrace.setVatBackTimes(0L);
					prplReinsTrace.setSendAmt(new BigDecimal(lossItem.getSumRealPay()));
					prplReinsTrace.setValidFlag(YN01.Y);
					prplReinsTrace.setCreateTime(new Date());
					prplReinsTrace.setUpdatetime(new Date());
					reinsTraceList.add(prplReinsTrace);
					
					serialNo++;
				}
			}
			List<PrpLLossPropVo> prpLLossPropVo = prpLCompensate.getPrpLLossProps();
			if(prpLLossPropVo!=null&& !prpLLossPropVo.isEmpty()){ // ????????????
				int serialNo = 10;
				for(PrpLLossPropVo prpLLoss:prpLLossPropVo){
					PrpLLossVo lossItem = new PrpLLossVo();
					lossItem.setCompensateNo(prpLCompensate.getCompensateNo());
					lossItem.setRiskCode(prpLLoss.getRiskCode());
					lossItem.setPolicyNo(prpLLoss.getPolicyNo());
					lossItem.setSerialNo(serialNo);
					lossItem.setKindCode(prpLLoss.getKindCode());
					SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",
							prpLLoss.getKindCode());
					lossItem.setKindName(sysVo.getCodeName());
					lossItem.setItemCode(prpLLoss.getItemId());
					lossItem.setLossName(prpLLoss.getItemName());
					lossItem.setSumLoss(DataUtils.NullToZero(prpLLoss.getSumLoss()).doubleValue());
					lossItem.setCurrency("CNY");
					//lossItem.setSumRealPay(DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue());
					lossItem.setSumRealPay(splitTaxFromPayment(prpLLoss.getKindCode(),DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue(),DataUtils.NullToZero(prpLLoss.getOffPreAmt()).doubleValue(),kindRealpayMap,kindPayRate,prpLCompensate));
					lossItem.setExceptDeductiblePay(0);
					lossItem.setDangerNo(1);
					prpLlossList.add(lossItem);
					
					//??????????????????????????????
					PrplReinsTrace prplReinsTrace = new PrplReinsTrace();
					prplReinsTrace.setRegistNo(claimVo.getRegistNo());
					prplReinsTrace.setRiskCode(claimVo.getRiskCode());
					prplReinsTrace.setKindCode(prpLLoss.getKindCode());
					prplReinsTrace.setLossId(prpLLoss.getId());
					prplReinsTrace.setSerialNo(new Long((long)serialNo));
					prplReinsTrace.setLossType(ExpType.PROP);
					prplReinsTrace.setTimes(new Long((long)times));
					prplReinsTrace.setVatBackTimes(0L);
					prplReinsTrace.setSendAmt(new BigDecimal(lossItem.getSumRealPay()));
					prplReinsTrace.setValidFlag(YN01.Y);
					prplReinsTrace.setCreateTime(new Date());
					prplReinsTrace.setUpdatetime(new Date());
					reinsTraceList.add(prplReinsTrace);
					
					serialNo++;
				}
			}

			// ?????????????????????
			List<PrpLpersonLossVo> prpLpersonLossList = new ArrayList<PrpLpersonLossVo>();
			List<PrpLLossPersonVo> prpLLossPersonVo = prpLCompensate.getPrpLLossPersons();
			if(prpLLossPersonVo!=null&& !prpLLossPersonVo.isEmpty()){
				int serialNo = 1;
				for(PrpLLossPersonVo prpLLossPerson:prpLLossPersonVo){
					PrpLpersonLossVo PrpLpersonLoss = new PrpLpersonLossVo();
					PrpLpersonLoss.setCompensateNo(prpLCompensate.getCompensateNo());
					PrpLpersonLoss.setRiskCode(prpLLossPerson.getRiskCode());
					PrpLpersonLoss.setPolicyNo(prpLLossPerson.getPolicyNo());
					PrpLpersonLoss.setSerialNo(serialNo);
					PrpLpersonLoss.setPersonNo(serialNo);
					PrpLpersonLoss.setItemKindNo(serialNo);
					PrpLpersonLoss.setKindCode(prpLLossPerson.getKindCode());
					SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",
							prpLLossPerson.getKindCode());
					PrpLpersonLoss.setKindName(sysVo.getCodeName());
					PrpLpersonLoss.setCurrency("CNY");
					//PrpLpersonLoss.setSumRealPay(DataUtils.NullToZero(prpLLossPerson.getSumRealPay()).doubleValue());
					PrpLpersonLoss.setSumRealPay(splitTaxFromPayment(prpLLossPerson.getKindCode(),DataUtils.NullToZero(prpLLossPerson.getSumRealPay()).doubleValue(),DataUtils.NullToZero(prpLLossPerson.getOffPreAmt()).doubleValue(),kindRealpayMap,kindPayRate,prpLCompensate));
					PrpLpersonLoss.setExceptDeductiblePay(0);
					PrpLpersonLoss.setDangerNo(1);
					prpLpersonLossList.add(PrpLpersonLoss);
					
					//??????????????????????????????
					PrplReinsTrace prplReinsTrace = new PrplReinsTrace();
					prplReinsTrace.setRegistNo(claimVo.getRegistNo());
					prplReinsTrace.setRiskCode(claimVo.getRiskCode());
					prplReinsTrace.setKindCode(prpLLossPerson.getKindCode());
					prplReinsTrace.setLossId(prpLLossPerson.getId());
					prplReinsTrace.setSerialNo(new Long((long)serialNo));
					prplReinsTrace.setLossType(ExpType.PERSON);
					prplReinsTrace.setTimes(new Long((long)times));
					prplReinsTrace.setVatBackTimes(0L);
					prplReinsTrace.setSendAmt(new BigDecimal(PrpLpersonLoss.getSumRealPay()));
					prplReinsTrace.setValidFlag(YN01.Y);
					prplReinsTrace.setCreateTime(new Date());
					prplReinsTrace.setUpdatetime(new Date());
					reinsTraceList.add(prplReinsTrace);
					
					
					serialNo++ ;
					
				}
			}

			// ???????????????
			List<PrpLChargeList> prpLChargeList = new ArrayList<PrpLChargeList>();
			List<PrpLChargeVo> prpLChargeVo = prpLCompensate.getPrpLCharges();
			if(prpLChargeVo!=null&& !prpLChargeVo.isEmpty()){
				int serialNo = 1;
				for(PrpLChargeVo prpLCharge:prpLChargeVo){
					
					// 2151 ????????????????????????????????????(????????????????????????)??????????????????????????????
					if(!CodeConstants.CHARGECODE_13.equals(prpLCharge.getChargeCode())){
						ins.sino.claimcar.reinsurance.vo.PrpLChargeList PrpLChargeVo = new ins.sino.claimcar.reinsurance.vo.PrpLChargeList();
						PrpLChargeVo.setCompensateNo(prpLCompensate.getCompensateNo());
						PrpLChargeVo.setRiskCode(prpLCharge.getRiskCode());
						PrpLChargeVo.setPolicyNo(prpLCharge.getPolicyNo());
						PrpLChargeVo.setSerialNo((serialNo)+"");
						PrpLChargeVo.setKindCode(prpLCharge.getKindCode());
	
						SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",prpLCharge.getKindCode());
	
						PrpLChargeVo.setKindName(sysVo.getCodeName());
						PrpLChargeVo.setChargeCode(prpLCharge.getChargeCode());
						PrpLChargeVo.setChargeName(prpLCharge.getChargeName());
						PrpLChargeVo.setCurrency("CNY");
						
						if((YN01.Y).equals(prpLCharge.getVatInvoiceFlag())){
							// ???????????????
							BigDecimal payeeAddTaxRate = (new BigDecimal(prpLCharge.getVatTaxRate())).divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);
							//???????????????????????????????????????
							PrpLChargeVo.setChargeAmount(((prpLCharge.getFeeAmt().subtract(prpLCharge.getOffAmt())).divide((new BigDecimal(1)).add(payeeAddTaxRate),8,BigDecimal.ROUND_HALF_UP)).doubleValue());
						}else{
							PrpLChargeVo.setChargeAmount(DataUtils.NullToZero(prpLCharge.getFeeAmt()).doubleValue()-
									DataUtils.NullToZero(prpLCharge.getOffAmt()).doubleValue());   // ??????---?????????   ???2016-12-19 ?????? ????????????
						}
						PrpLChargeVo.setSumRealPay(0); // //???????????? ?????? ***????????????????????????????????????????????? ?????????0
						PrpLChargeVo.setDangerNo(1);
						PrpLChargeVo.setExceptDeductiblePay(0); // /
						prpLChargeList.add(PrpLChargeVo);
						
						//??????????????????????????????
						PrplReinsTrace prplReinsTrace = new PrplReinsTrace();
						prplReinsTrace.setRegistNo(claimVo.getRegistNo());
						prplReinsTrace.setRiskCode(claimVo.getRiskCode());
						prplReinsTrace.setKindCode(prpLCharge.getKindCode());
						prplReinsTrace.setChargeCode(prpLCharge.getChargeCode());
						prplReinsTrace.setSerialNo(new Long((long)serialNo));
						prplReinsTrace.setTimes(new Long((long)times));
						prplReinsTrace.setVatBackTimes(0L);
						prplReinsTrace.setSendAmt(new BigDecimal(PrpLChargeVo.getChargeAmount()));
						prplReinsTrace.setValidFlag(YN01.Y);
						prplReinsTrace.setCreateTime(new Date());
						prplReinsTrace.setUpdatetime(new Date());
						reinsTraceList.add(prplReinsTrace);
						
						serialNo++;
					}
				}

			}
		/*	// ??????????????? 
			PrpLAssessorFeeVo aeesssorFee = assessorService.findAssessorFeeVoByComp(claimVo.getClaimNo());
			if(aeesssorFee != null &&  !BigDecimal.ZERO.equals(aeesssorFee.getAmount())){
				ins.sino.claimcar.reinsurance.vo.PrpLChargeList PrpLChargeVo = new ins.sino.claimcar.reinsurance.vo.PrpLChargeList();
				PrpLChargeVo.setCompensateNo(aeesssorFee.getCompensateNo());
				PrpLChargeVo.setRiskCode(claimVo.getRiskCode());
				PrpLChargeVo.setPolicyNo(aeesssorFee.getPolicyNo());
				PrpLChargeVo.setSerialNo(prpLChargeList.size()+"");
				PrpLChargeVo.setKindCode(aeesssorFee.getKindCode());

				SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",
						aeesssorFee.getKindCode());

				PrpLChargeVo.setKindName(sysVo.getCodeName());
				PrpLChargeVo.setChargeCode("13");
				PrpLChargeVo.setChargeName("?????????");
				PrpLChargeVo.setCurrency("CNY");
				PrpLChargeVo.setChargeAmount(DataUtils.NullToZero(aeesssorFee.getAmount()).doubleValue());   // ??????---?????????   ???2016-12-19 ?????? ????????????
				PrpLChargeVo.setSumRealPay(0); // //???????????? ?????? ***????????????????????????????????????????????? ?????????0
				PrpLChargeVo.setDangerNo(1);
				PrpLChargeVo.setExceptDeductiblePay(0); // /
				prpLChargeList.add(PrpLChargeVo);

			}
*/
			// ?????????????????????
			// List<PrpLcfeecoinsVo> prpLcfeecoinsList = new ArrayList<PrpLcfeecoinsVo>();
			if(claimVo.getCaseNo()==null){
				compensateVo.setBusinessType("0"); // ???????????????????????? ????????????????????? ????????????
			}else{
				compensateVo.setBusinessType("1");
			}
			
			compensateVo.setAdjustTimes(times+"");
			compensateVo.setPrpLClaimVo(PrpLclaimDto);
			compensateVo.setPrpLCompensateVo(prpLcompensateDto);
			compensateVo.setPrpLLossVo(prpLlossList);
			compensateVo.setPrpLpersonLossVo(prpLpersonLossList);
			compensateVo.setPrpLChargeList(prpLChargeList);
			compensateVo.setPrpLcfeecoinsVo(null);

			ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // ???????????????
			claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
			claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
			claimInterfaceLogVo.setCompensateNo(prpLCompensate.getCompensateNo());
			claimInterfaceLogVo.setComCode(claimVo.getComCode());
			claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
			claimInterfaceLogVo.setCreateTime(new Date());
			claimInterfaceLogVo.setOperateNode(FlowNode.VClaim.getName());
			claimInterfaceLogVo.setBusinessType(BusinessType.Reinsurance_verify.name());
			claimInterfaceLogVo.setBusinessName(BusinessType.Reinsurance_verify.getName());
			
			//???????????????????????????????????????
			recordPaymentAndFeeSplitTaxTrace(kindPayRate,prpLCompensate);
			
			//????????????????????????
			recordReinsTraces(claimVo.getRegistNo(),claimVo.getRiskCode(),times,reinsTraceList);
			
			callReinsWebService.callCompensateForClient(compensateVo,claimInterfaceLogVo);
		}else{
			logger.info("?????????????????????????????????");
		}

	}
	
	/**
	 * ???????????????????????????????????????
	 * @param kindRealpayMap
	 * @param prpLCompensate
	 */
	private void calculateKindSumRealPay(Map<String,Double> kindRealpayMap,PrpLCompensateVo prpLCompensate){
		//???????????????
        Double sumRealPay = 0.00;
        
		List<PrpLLossItemVo> prpLLossItemVo = prpLCompensate.getPrpLLossItems();
		if(prpLLossItemVo!=null&&prpLLossItemVo.size()>0){ // ????????????
			for(PrpLLossItemVo prpLLoss:prpLLossItemVo){
				
				BigDecimal lossItemAmt = prpLLoss.getSumRealPay();
				//??????????????????
				if(prpLLoss.getOffPreAmt() != null){
					lossItemAmt = lossItemAmt.subtract(prpLLoss.getOffPreAmt());
				}
				
				//????????????????????????
				if (kindRealpayMap.containsKey(prpLLoss.getKindCode())) {
					kindRealpayMap.put(prpLLoss.getKindCode(),kindRealpayMap.get(prpLLoss.getKindCode())+DataUtils.NullToZero(lossItemAmt).doubleValue());
				} else {
					kindRealpayMap.put(prpLLoss.getKindCode(),
							DataUtils.NullToZero(lossItemAmt).doubleValue());
				}
				sumRealPay = sumRealPay+DataUtils.NullToZero(lossItemAmt).doubleValue();
			}
		}
		// ????????????
		List<PrpLLossPropVo> prpLLossPropVo = prpLCompensate.getPrpLLossProps();
		if(prpLLossPropVo!=null&& !prpLLossPropVo.isEmpty()){ 
			
			for(PrpLLossPropVo prpLLoss:prpLLossPropVo){
				
				BigDecimal lossItemAmt = prpLLoss.getSumRealPay();
				//??????????????????
				if(prpLLoss.getOffPreAmt() != null){
					lossItemAmt = lossItemAmt.subtract(prpLLoss.getOffPreAmt());
				}
				
				//????????????????????????
				if (kindRealpayMap.containsKey(prpLLoss.getKindCode())) {
					kindRealpayMap.put(prpLLoss.getKindCode(),kindRealpayMap.get(prpLLoss.getKindCode())+DataUtils.NullToZero(lossItemAmt).doubleValue());
				} else {
					kindRealpayMap.put(prpLLoss.getKindCode(),
							DataUtils.NullToZero(lossItemAmt).doubleValue());
				}
				sumRealPay = sumRealPay+DataUtils.NullToZero(lossItemAmt).doubleValue();
			}
		}

		// ?????????????????????
		List<PrpLLossPersonVo> prpLLossPersonVo = prpLCompensate.getPrpLLossPersons();
		if(prpLLossPersonVo!=null&& !prpLLossPersonVo.isEmpty()){
			for(PrpLLossPersonVo prpLLossPerson:prpLLossPersonVo){
				
				BigDecimal personLossAmt = prpLLossPerson.getSumRealPay();
				
				if(prpLLossPerson.getOffPreAmt() != null){
					personLossAmt = personLossAmt.subtract(prpLLossPerson.getOffPreAmt());
				}
				//????????????????????????
				if (kindRealpayMap.containsKey(prpLLossPerson.getKindCode())) {
					kindRealpayMap.put(prpLLossPerson.getKindCode(),kindRealpayMap.get(prpLLossPerson.getKindCode())+DataUtils.NullToZero(personLossAmt).doubleValue());
				} else {
					kindRealpayMap.put(prpLLossPerson.getKindCode(),DataUtils.NullToZero(personLossAmt).doubleValue());
				}
				sumRealPay = sumRealPay+DataUtils.NullToZero(personLossAmt).doubleValue();
			}
		}
		kindRealpayMap.put(CodeConstants.ALLKINDSUMREALPAY, sumRealPay);
	}
	
	/**
	 * ?????????????????????????????????????????????????????????
	 * @param kindCode ??????
	 * @param kindItemRealPay ????????????????????????(????????????)
	 * @param kindRealpayMap ????????????Map
	 * @param prpLCompensate 
	 * @return
	 */
	private double splitTaxFromPayment(String kindCode,Double kindItemRealPay,Double offPreAmt,Map<String,Double> kindRealpayMap,Map<String, BigDecimal> kindPayRate,PrpLCompensateVo prpLCompensate){
		if (kindItemRealPay == 0.00) {
			return kindItemRealPay;
		} else {

			// step2:???????????????????????????????????????????????????????????????????????????
			
			//?????????????????????
			double splitOffPreRealPay = kindItemRealPay - offPreAmt;
			
			// ??????????????????????????????????????????????????????
			BigDecimal itemKindRate = new BigDecimal(1);
			
			if(kindRealpayMap.get(kindCode) != 0.00){
				itemKindRate= new BigDecimal(splitOffPreRealPay / kindRealpayMap.get(kindCode));
			}
			// ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			List<PrpLPaymentVo> payments = prpLCompensate.getPrpLPayments();
			
			
			// ?????????????????????????????????????????????????????????
			BigDecimal splitSumTax = new BigDecimal(0.00);

			
			//????????????????????????
			if(payments !=null && payments.size()>0){
				for (PrpLPaymentVo prpLPayment : payments) {
	
					//???????????????????????????????????????????????????
					if ((YN01.Y).equals(prpLPayment.getVatInvoiceFlag())) {
						for (String key : kindPayRate.keySet()) {
	
							if (kindCode.equals(key)) {
								// ??????????????????????????????
								BigDecimal payeeKindrealPay = new BigDecimal(prpLPayment.getSumRealPay().multiply(kindPayRate.get(key)).doubleValue());
								// ???????????????
								BigDecimal payeeAddTaxRate = (new BigDecimal(prpLPayment.getVatTaxRate())).divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);
								// ??????????????????????????????????????????
								splitSumTax = splitSumTax.add(payeeKindrealPay.subtract(payeeKindrealPay.divide((new BigDecimal(1)).add(payeeAddTaxRate),8, BigDecimal.ROUND_HALF_UP)));
							}
						}
					}
				}
			}
			
			
			
			// ?????????????????????????????????????????????????????????????????????????????????
			splitSumTax = splitSumTax.multiply(itemKindRate);

			// step3:???????????????????????????????????????????????????????????????????????????
			kindItemRealPay = kindItemRealPay - splitSumTax.doubleValue();

			return kindItemRealPay;
		}
	}
	
	/**
	 * ??????????????????????????????
	 * @param registNo
	 * @param riskCode
	 * @param lossId
	 * @return
	 */
	private double addTaxFromPrepay(String registNo,String riskCode,String lossId){
		//??????????????????
		double addTax = 0.00;
		BigDecimal tax = new BigDecimal(0.00);
		List<PrpLPrePayVo> prePayVos = compensateService.queryCarLossPrePay(registNo, riskCode, lossId);
		if(prePayVos != null && !prePayVos.isEmpty()){
			for(PrpLPrePayVo prepay:prePayVos){
				if(YN01.Y.equals(prepay.getVatInvoiceFlag())){
					// ???????????????
					BigDecimal payeeAddTaxRate = (new BigDecimal(prepay.getVatTaxRate())).divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
					
					tax =tax.add(prepay.getPayAmt().subtract(prepay.getPayAmt().divide((new BigDecimal(1)).add(payeeAddTaxRate),8, BigDecimal.ROUND_HALF_UP)));
				}
			}
			addTax=  tax.doubleValue();
		}
		
		
		return addTax;
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param kindRealpayMap ??????????????????map
	 * @param kindPayRate ???????????????????????????
	 * @param prpLCompensate 
	 */
	private void recordPaymentAndFeeSplitTaxTrace(Map<String, BigDecimal> kindPayRate,PrpLCompensateVo prpLCompensate){
		// ??????????????????????????????????????????????????????????????????
		List<PrplPayeeKindPaymentVo> PayeeKindPaymentList =  verifyClaimService.findPayeeKindPaymentsByTimes(prpLCompensate.getRegistNo(), prpLCompensate.getRiskCode(), 0);
		
		if(PayeeKindPaymentList != null && !PayeeKindPaymentList.isEmpty()){
			logger.info("????????????????????????????????????????????????????????????????????????{}?????????{}",prpLCompensate.getRegistNo(),prpLCompensate.getRiskCode());
		}else{
		
			List<PrplPayeeKindPayment> payeeKindPayments = new ArrayList<PrplPayeeKindPayment>();
			
			// ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		    List<PrpLPaymentVo> payments = prpLCompensate.getPrpLPayments();
		    
		    //?????????????????????
		    List<PrpLPrePayVo> prplPrePays = compensateService.queryLossPrePay(prpLCompensate.getRegistNo(), prpLCompensate.getRiskCode());
		    
		    if(prplPrePays != null && prplPrePays.size()>0){
		    	for(PrpLPrePayVo prePay:prplPrePays){
		    		if((YN01.Y).equals(prePay.getVatInvoiceFlag())){
		    			PrplPayeeKindPayment payeeKindPayment = new PrplPayeeKindPayment();
						payeeKindPayment.setCompensateNo(prePay.getCompensateNo());
						// ??????lossId,???lossType?????????
						String lossType = prePay.getLossType();
						if (StringUtils.isNotBlank(lossType)) {
							int i = lossType.lastIndexOf("_");
							if (i != -1) {
								String lossId = lossType.substring(i + 1);
								try {
									payeeKindPayment.setLossId(Long.parseLong(lossId));
								} catch (NumberFormatException e) {
									logger.error("PrpLPrePay lossType ????????????lossId???????????????", e);
								}
							}

						}
						payeeKindPayment.setPayeeId(prePay.getPayeeId());
						payeeKindPayment.setRegistNo(prpLCompensate.getRegistNo());
						payeeKindPayment.setRiskCode(prpLCompensate.getRiskCode());
						payeeKindPayment.setTimes(0L);//???????????????????????????????????????0??? ????????????????????????????????????????????????????????????times???1??????
						payeeKindPayment.setKindCode(prePay.getKindCode());
						payeeKindPayment.setKindSumPay(prePay.getPayAmt());
						
						// ???????????????
						BigDecimal payeeAddTaxRate = (new BigDecimal(prePay.getVatTaxRate())).divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
						BigDecimal tax =prePay.getPayAmt().subtract(prePay.getPayAmt().divide((new BigDecimal(1)).add(payeeAddTaxRate),8, BigDecimal.ROUND_HALF_UP));
						
						payeeKindPayment.setAddTaxValue(tax);
						payeeKindPayment.setAddTaxRate(prePay.getVatTaxRate());
						payeeKindPayment.setNoTaxValue((prePay.getPayAmt().subtract(tax)));
						payeeKindPayment.setToReinsFlag(YN01.Y);
						payeeKindPayment.setCreateTime(new Date());
						payeeKindPayment.setUpdatetime(new Date());
						payeeKindPayments.add(payeeKindPayment);
		    		}
		    	}
		    }
		    
		    
			if(payments != null && payments.size()>0){
				for (PrpLPaymentVo prpLPayment : payments) {
		
					//??????????????????????????????????????????
					if ((YN01.Y).equals(prpLPayment.getVatInvoiceFlag())) {
						for (String key : kindPayRate.keySet()) {
		
							if (CodeConstants.ALLKINDSUMREALPAY.equals(key)) {
								continue;
							}else{
								// ??????????????????????????????
								BigDecimal payeeKindrealPay = new BigDecimal(prpLPayment.getSumRealPay().multiply(kindPayRate.get(key)).doubleValue());
								// ???????????????
								BigDecimal payeeAddTaxRate = (new BigDecimal(prpLPayment.getVatTaxRate())).divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
								// ??????????????????????????????????????????
								BigDecimal splitSumTax = new BigDecimal(0.00);
								splitSumTax= splitSumTax.add(payeeKindrealPay.subtract(payeeKindrealPay.divide((new BigDecimal(1)).add(payeeAddTaxRate),8, BigDecimal.ROUND_HALF_UP)));
								
								PrplPayeeKindPayment payeeKindPayment = new PrplPayeeKindPayment();
								payeeKindPayment.setCompensateNo(prpLCompensate.getCompensateNo());
								payeeKindPayment.setPayeeId(prpLPayment.getPayeeId());
								payeeKindPayment.setRegistNo(prpLCompensate.getRegistNo());
								payeeKindPayment.setRiskCode(prpLCompensate.getRiskCode());
								payeeKindPayment.setTimes(0L);//???????????????????????????????????????0??? ????????????????????????????????????????????????????????????times???1??????
								payeeKindPayment.setKindCode(key);
								payeeKindPayment.setKindSumPay(payeeKindrealPay);
								payeeKindPayment.setAddTaxValue(splitSumTax);
								payeeKindPayment.setAddTaxRate(prpLPayment.getVatTaxRate());
								payeeKindPayment.setNoTaxValue(payeeKindrealPay.subtract(splitSumTax));
								payeeKindPayment.setToReinsFlag(YN01.Y);
								payeeKindPayment.setCreateTime(new Date());
								payeeKindPayment.setUpdatetime(new Date());
								payeeKindPayments.add(payeeKindPayment);
							}
						}
					}
				}
			}
			
			//????????????????????????????????????????????????
			List<PrpLChargeVo> prpLCharges = prpLCompensate.getPrpLCharges();
			if(prpLCharges!=null && !prpLCharges.isEmpty()){
				for(PrpLChargeVo prpLCharge:prpLCharges){
					if((YN01.Y).equals(prpLCharge.getVatInvoiceFlag())){
						// ???????????????
						BigDecimal payeeAddTaxRate = (new BigDecimal(prpLCharge.getVatTaxRate())).divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
						//??????
						BigDecimal splitSumTax = new BigDecimal(0.00);
						splitSumTax= splitSumTax.add((prpLCharge.getFeeAmt().subtract(prpLCharge.getOffAmt())).subtract((prpLCharge.getFeeAmt().subtract(prpLCharge.getOffAmt())).divide((new BigDecimal(1)).add(payeeAddTaxRate),8, BigDecimal.ROUND_HALF_UP)));
						
						PrplPayeeKindPayment payeeKindPayment = new PrplPayeeKindPayment();
						payeeKindPayment.setCompensateNo(prpLCompensate.getCompensateNo());
						payeeKindPayment.setPayeeId(prpLCharge.getPayeeId());
						payeeKindPayment.setRegistNo(prpLCompensate.getRegistNo());
						payeeKindPayment.setRiskCode(prpLCompensate.getRiskCode());
						payeeKindPayment.setChargeCode(prpLCharge.getChargeCode());
						payeeKindPayment.setTimes(0L);//???????????????????????????????????????0??? ????????????????????????????????????????????????????????????times???1??????
						payeeKindPayment.setKindCode(prpLCharge.getKindCode());
						payeeKindPayment.setKindSumPay(prpLCharge.getFeeAmt().subtract(prpLCharge.getOffAmt()));
						payeeKindPayment.setAddTaxValue(splitSumTax);
						payeeKindPayment.setAddTaxRate(prpLCharge.getVatTaxRate());
						payeeKindPayment.setNoTaxValue(prpLCharge.getFeeAmt().subtract(prpLCharge.getOffAmt()).subtract(splitSumTax));
						payeeKindPayment.setToReinsFlag(YN01.Y);
						payeeKindPayment.setCreateTime(new Date());
						payeeKindPayment.setUpdatetime(new Date());
						payeeKindPayments.add(payeeKindPayment);
					}
				}
			}
			
			// ?????????????????????
			if (!payeeKindPayments.isEmpty()) {
				for (PrplPayeeKindPayment prplPayeeKindPayment : payeeKindPayments) {
					databaseDao.save(PrplPayeeKindPayment.class,prplPayeeKindPayment);
				}
			}
		}
		
	}
	
	/**
	 * ????????????????????????
	 * @param registNo ?????????
	 * @param riskCode ??????
	 * @param times ???????????????
	 * @param reinsTraces ?????????????????????
	 */
	private void recordReinsTraces(String registNo, String riskCode, int times,List<PrplReinsTrace> reinsTraces) {

		if (!reinsTraces.isEmpty()) {
			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addEqual("registNo", registNo);
			queryRule.addEqual("riskCode", riskCode);
			queryRule.addEqual("times", new Long((long)times));

			//?????????????????????????????????????????????????????????
			List<PrplReinsTrace> hisRreinsTraces = this.databaseDao.findAll(PrplReinsTrace.class, queryRule);

			if (hisRreinsTraces != null && !hisRreinsTraces.isEmpty()) {
				for (PrplReinsTrace hisRreinsTrace : hisRreinsTraces) {
					hisRreinsTrace.setValidFlag(YN01.N);
					databaseDao.update(PrplReinsTrace.class, hisRreinsTrace);
				}
			}
			
			for(PrplReinsTrace reinsTrace:reinsTraces){
				databaseDao.save(PrplReinsTrace.class, reinsTrace);
			}
		}
	}

	@Override
	public void TransDataForReinsCaseVo(String businessType,PrpLClaimVo claimVo,ClaimInterfaceLogVo claimInterfaceLogVo) throws Exception {

		// ??????0 ??????1 ??????3 ??????2
		ReinsCaseStatusVO reinsCaseStatusVO = new ReinsCaseStatusVO();
		reinsCaseStatusVO.setBusinessType(businessType);
		reinsCaseStatusVO.setClaimNo(claimVo.getClaimNo());
		Date time = new Date();
		reinsCaseStatusVO.setOperateDate(time);
		PrpLCMainVo cmain = policyViewService.findPolicyInfoByPaltform(claimVo.getRegistNo(),claimVo.getPolicyNo());
		reinsCaseStatusVO.setOperateComCode(cmain.getComCode());

		reinsCaseStatusVO.setOperaterCode(cmain.getCreateUser());
		claimInterfaceLogVo.setFlag(businessType);
		claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
		claimInterfaceLogVo.setCompensateNo(claimVo.getClaimNo());
		callReinsWebService.callReinsCaseStatusForClient(reinsCaseStatusVO,claimInterfaceLogVo);

	}
	
	public void asseFeeToReins(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate,PrpLAssessorFeeVo assessorFeeVo) throws Exception{
		if(assessorFeeVo == null || BigDecimal.ZERO.equals(assessorFeeVo.getAmount())){
			System.out.println("??????????????????");
			return;
		}
		ins.sino.claimcar.reinsurance.vo.CompensateVo compensateVo = new ins.sino.claimcar.reinsurance.vo.CompensateVo();
		// ????????????
		if(claimVo!=null&&prpLCompensate!=null){
			ins.sino.claimcar.reinsurance.vo.PrpLClaimVo PrpLclaimDto = new ins.sino.claimcar.reinsurance.vo.PrpLClaimVo();
			PrpLCMainVo cmain = policyViewService.findPolicyInfoByPaltform(prpLCompensate.getRegistNo(),
					prpLCompensate.getPolicyNo());
			PrpLclaimDto.setClaimNo(claimVo.getClaimNo());
			PrpLclaimDto.setRiskCode(claimVo.getRiskCode());
			PrpLclaimDto.setRegistNo(claimVo.getRegistNo());
			PrpLclaimDto.setPolicyNo(claimVo.getPolicyNo());
			PrpLclaimDto.setCurrency("CNY");
			PrpLclaimDto.setDamageStartDate(claimVo.getDamageTime());
			PrpLclaimDto.setDamageEndDate(claimVo.getDamageTime());
			PrpLclaimDto.setDamageCode(claimVo.getDamageCode());
			PrpLclaimDto.setAddressCode(claimVo.getDamageAreaCode());
			if(cmain!=null){
				PrpLclaimDto.setAgentName(cmain.getAgentCode());
				PrpLclaimDto.setMakeCom(cmain.getComCode());
				PrpLclaimDto.setHandlerCode(cmain.getHandlerCode());
			}else{
				PrpLclaimDto.setAgentName("");
				PrpLclaimDto.setMakeCom("");
				PrpLclaimDto.setHandlerCode("");
			}
			PrpLclaimDto.setInputDate(claimVo.getCreateTime());
			PrpLclaimDto.setDamageKind(claimVo.getRiskCode()); // ??????????????? ?????????????????? ** ???????????? DamageKind ???riskcode
			PrpLclaimDto.setSumClaim(DataUtils.NullToZero(claimVo.getSumClaim()).doubleValue());

			SysCodeDictVo sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode",claimVo.getDamageCode());
			if(sys == null){
				sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode2",claimVo.getDamageCode());
			}
			PrpLclaimDto.setCodeCName(sys.getCodeName());
			PrpLclaimDto.setDamageAreaPostCode(claimVo.getDamageAreaCode()); // ??????????????? ????????????
			PrpLclaimDto.setEndCaseFlag("1");
			PrpLclaimDto.setDangerNo(1);

			// ??????????????????
			ins.sino.claimcar.reinsurance.vo.PrpLCompensateVo prpLcompensateDto = new ins.sino.claimcar.reinsurance.vo.PrpLCompensateVo();
			prpLcompensateDto.setCompensateNo(prpLCompensate.getCompensateNo());
			prpLcompensateDto.setCaseNo(prpLCompensate.getCaseNo());
			System.out.println(prpLCompensate.getRiskCode());
			if(prpLCompensate!=null&&prpLCompensate.getRiskCode()!=null&&prpLCompensate.getRiskCode().length()>=2){
				prpLcompensateDto.setclassCode(prpLCompensate.getRiskCode().substring(0,2));
			}else{
				prpLcompensateDto.setclassCode("");
			}

			prpLcompensateDto.setRiskCode(prpLCompensate.getRiskCode());
			prpLcompensateDto.setClaimNo(prpLCompensate.getClaimNo());
			prpLcompensateDto.setPolicyNo(prpLCompensate.getPolicyNo());
			prpLcompensateDto.setCurrency("CNY");
			prpLcompensateDto.setSumLoss(DataUtils.NullToZero(prpLCompensate.getSumLoss()).doubleValue());
			prpLcompensateDto.setMakeCom(prpLCompensate.getMakeCom());
			prpLcompensateDto.setComCode(prpLCompensate.getComCode());
			prpLcompensateDto.setHandler1Code(prpLCompensate.getHandler1Code());
			prpLcompensateDto.setInputDate(prpLCompensate.getCreateTime());
			prpLcompensateDto.setUnderWriteEndDate(claimVo.getDamageTime());
			prpLcompensateDto.setUnderWriteFlag("1");
			prpLcompensateDto.setEndcaseDate(prpLCompensate.getUnderwriteDate());



			// ????????? assessorFeeVo
			List<PrpLChargeList> prpLChargeList = new ArrayList<PrpLChargeList>();
			ins.sino.claimcar.reinsurance.vo.PrpLChargeList PrpLChargeVo = new ins.sino.claimcar.reinsurance.vo.PrpLChargeList();
			PrpLChargeVo.setCompensateNo(assessorFeeVo.getCompensateNo());
			PrpLChargeVo.setRiskCode(claimVo.getRiskCode());
			PrpLChargeVo.setPolicyNo(assessorFeeVo.getPolicyNo());
			PrpLChargeVo.setSerialNo("0");
			PrpLChargeVo.setKindCode(assessorFeeVo.getKindCode());

			SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",
					assessorFeeVo.getKindCode());

			PrpLChargeVo.setKindName(sysVo.getCodeName());
			PrpLChargeVo.setChargeCode("13");
			PrpLChargeVo.setChargeName("?????????");
			PrpLChargeVo.setCurrency("CNY");
			PrpLChargeVo.setChargeAmount(DataUtils.NullToZero(assessorFeeVo.getAmount()).doubleValue());   // ??????---?????????   ???2016-12-19 ?????? ????????????
			PrpLChargeVo.setSumRealPay(0); // //???????????? ?????? ***????????????????????????????????????????????? ?????????0
			PrpLChargeVo.setDangerNo(1);
			PrpLChargeVo.setExceptDeductiblePay(0); // /
			prpLChargeList.add(PrpLChargeVo);


			if(claimVo.getCaseNo()==null){
				compensateVo.setBusinessType("0"); // ???????????????????????? ????????????????????? ????????????
			}else{
				compensateVo.setBusinessType("1");
			}
			compensateVo.setPrpLClaimVo(PrpLclaimDto);
			compensateVo.setPrpLCompensateVo(prpLcompensateDto);
			compensateVo.setPrpLChargeList(prpLChargeList);

			ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // ???????????????
			claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
			claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
			claimInterfaceLogVo.setCompensateNo(assessorFeeVo.getCompensateNo());
			claimInterfaceLogVo.setComCode(claimVo.getComCode());
			claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
			claimInterfaceLogVo.setCreateTime(new Date());
			claimInterfaceLogVo.setOperateNode("???????????????");
			claimInterfaceLogVo.setBusinessType(BusinessType.Reinsurance_assessor.name());
			claimInterfaceLogVo.setBusinessName(BusinessType.Reinsurance_assessor.getName());
			callReinsWebService.callCompensateForClient(compensateVo,claimInterfaceLogVo);
		}else{
			logger.info("?????????????????????????????????");
		}
	}
	public void checkFeeToReins(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate,PrpLCheckFeeVo checkFeeVo) throws Exception{
		if(checkFeeVo == null || BigDecimal.ZERO.equals(checkFeeVo.getAmount())){
			System.out.println("??????????????????");
			return;
		}
		ins.sino.claimcar.reinsurance.vo.CompensateVo compensateVo = new ins.sino.claimcar.reinsurance.vo.CompensateVo();
		// ????????????
		if(claimVo!=null&&prpLCompensate!=null){
			ins.sino.claimcar.reinsurance.vo.PrpLClaimVo PrpLclaimDto = new ins.sino.claimcar.reinsurance.vo.PrpLClaimVo();
			PrpLCMainVo cmain = policyViewService.findPolicyInfoByPaltform(prpLCompensate.getRegistNo(),
					prpLCompensate.getPolicyNo());
			PrpLclaimDto.setClaimNo(claimVo.getClaimNo());
			PrpLclaimDto.setRiskCode(claimVo.getRiskCode());
			PrpLclaimDto.setRegistNo(claimVo.getRegistNo());
			PrpLclaimDto.setPolicyNo(claimVo.getPolicyNo());
			PrpLclaimDto.setCurrency("CNY");
			PrpLclaimDto.setDamageStartDate(claimVo.getDamageTime());
			PrpLclaimDto.setDamageEndDate(claimVo.getDamageTime());
			PrpLclaimDto.setDamageCode(claimVo.getDamageCode());
			PrpLclaimDto.setAddressCode(claimVo.getDamageAreaCode());
			if(cmain!=null){
				PrpLclaimDto.setAgentName(cmain.getAgentCode());
				PrpLclaimDto.setMakeCom(cmain.getComCode());
				PrpLclaimDto.setHandlerCode(cmain.getHandlerCode());
			}else{
				PrpLclaimDto.setAgentName("");
				PrpLclaimDto.setMakeCom("");
				PrpLclaimDto.setHandlerCode("");
			}
			PrpLclaimDto.setInputDate(claimVo.getCreateTime());
			PrpLclaimDto.setDamageKind(claimVo.getRiskCode()); // ??????????????? ?????????????????? ** ???????????? DamageKind ???riskcode
			PrpLclaimDto.setSumClaim(DataUtils.NullToZero(claimVo.getSumClaim()).doubleValue());

			SysCodeDictVo sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode",claimVo.getDamageCode());
			if(sys == null){
				sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode2",claimVo.getDamageCode());
			}
			PrpLclaimDto.setCodeCName(sys.getCodeName());
			PrpLclaimDto.setDamageAreaPostCode(claimVo.getDamageAreaCode()); // ??????????????? ????????????
			PrpLclaimDto.setEndCaseFlag("1");
			PrpLclaimDto.setDangerNo(1);

			// ??????????????????
			ins.sino.claimcar.reinsurance.vo.PrpLCompensateVo prpLcompensateDto = new ins.sino.claimcar.reinsurance.vo.PrpLCompensateVo();
			prpLcompensateDto.setCompensateNo(prpLCompensate.getCompensateNo());
			prpLcompensateDto.setCaseNo(prpLCompensate.getCaseNo());
			System.out.println(prpLCompensate.getRiskCode());
			if(prpLCompensate!=null&&prpLCompensate.getRiskCode()!=null&&prpLCompensate.getRiskCode().length()>=2){
				prpLcompensateDto.setclassCode(prpLCompensate.getRiskCode().substring(0,2));
			}else{
				prpLcompensateDto.setclassCode("");
			}

			prpLcompensateDto.setRiskCode(prpLCompensate.getRiskCode());
			prpLcompensateDto.setClaimNo(prpLCompensate.getClaimNo());
			prpLcompensateDto.setPolicyNo(prpLCompensate.getPolicyNo());
			prpLcompensateDto.setCurrency("CNY");
			prpLcompensateDto.setSumLoss(DataUtils.NullToZero(prpLCompensate.getSumLoss()).doubleValue());
			prpLcompensateDto.setMakeCom(prpLCompensate.getMakeCom());
			prpLcompensateDto.setComCode(prpLCompensate.getComCode());
			prpLcompensateDto.setHandler1Code(prpLCompensate.getHandler1Code());
			prpLcompensateDto.setInputDate(prpLCompensate.getCreateTime());
			prpLcompensateDto.setUnderWriteEndDate(claimVo.getDamageTime());
			prpLcompensateDto.setUnderWriteFlag("1");
			prpLcompensateDto.setEndcaseDate(prpLCompensate.getUnderwriteDate());



			// ????????? checkFeeVo
			List<PrpLChargeList> prpLChargeList = new ArrayList<PrpLChargeList>();
			ins.sino.claimcar.reinsurance.vo.PrpLChargeList PrpLChargeVo = new ins.sino.claimcar.reinsurance.vo.PrpLChargeList();
			PrpLChargeVo.setCompensateNo(checkFeeVo.getCompensateNo());
			PrpLChargeVo.setRiskCode(claimVo.getRiskCode());
			PrpLChargeVo.setPolicyNo(checkFeeVo.getPolicyNo());
			PrpLChargeVo.setSerialNo("0");
			PrpLChargeVo.setKindCode(checkFeeVo.getKindCode());

			SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",
					checkFeeVo.getKindCode());

			PrpLChargeVo.setKindName(sysVo.getCodeName());
			PrpLChargeVo.setChargeCode("04");
			PrpLChargeVo.setChargeName("?????????");
			PrpLChargeVo.setCurrency("CNY");
			PrpLChargeVo.setChargeAmount(DataUtils.NullToZero(checkFeeVo.getAmount()).doubleValue());   // ??????---?????????   ???2016-12-19 ?????? ????????????
			PrpLChargeVo.setSumRealPay(0); // //???????????? ?????? ***????????????????????????????????????????????? ?????????0
			PrpLChargeVo.setDangerNo(1);
			PrpLChargeVo.setExceptDeductiblePay(0); // /
			prpLChargeList.add(PrpLChargeVo);


			if(claimVo.getCaseNo()==null){
				compensateVo.setBusinessType("0"); // ???????????????????????? ????????????????????? ????????????
			}else{
				compensateVo.setBusinessType("1");
			}
			compensateVo.setPrpLClaimVo(PrpLclaimDto);
			compensateVo.setPrpLCompensateVo(prpLcompensateDto);
			compensateVo.setPrpLChargeList(prpLChargeList);

			ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // ???????????????
			claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
			claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
			claimInterfaceLogVo.setCompensateNo(checkFeeVo.getCompensateNo());
			claimInterfaceLogVo.setComCode(claimVo.getComCode());
			claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
			claimInterfaceLogVo.setCreateTime(new Date());
			claimInterfaceLogVo.setOperateNode("???????????????");
			claimInterfaceLogVo.setBusinessType(BusinessType.Reinsurance_checkFee.name());
			claimInterfaceLogVo.setBusinessName(BusinessType.Reinsurance_checkFee.getName());
			callReinsWebService.callCompensateForClient(compensateVo,claimInterfaceLogVo);
		}else{
			logger.info("?????????????????????????????????");
		}
	}
	
	@Override
	public void asseFeeToReinsOut(PrpLClaimVo claimVo,
			PrpLCompensateVo prpLCompensate, PrpLAssessorFeeVo assessorFeeVo)
			throws Exception {
		
		ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // ???????????????
		claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
		claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
		claimInterfaceLogVo.setComCode(claimVo.getComCode());
		claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
		claimInterfaceLogVo.setCreateTime(new Date());
		claimInterfaceLogVo.setOperateNode("???????????????");
		// ??????
		TransDataForReinsCaseVo("3",claimVo, claimInterfaceLogVo);
		// ?????????????????????
		asseFeeToReins(claimVo,prpLCompensate, assessorFeeVo);
		// ??????
		TransDataForReinsCaseVo("3",claimVo, claimInterfaceLogVo);
	}

	@Override
	public void checkFeeToReinsOut(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate, PrpLCheckFeeVo checkFeeVo)throws Exception {
		ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // ???????????????
		claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
		claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
		claimInterfaceLogVo.setComCode(claimVo.getComCode());
		claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
		claimInterfaceLogVo.setCreateTime(new Date());
		claimInterfaceLogVo.setOperateNode("???????????????");
		// ??????
		TransDataForReinsCaseVo("3",claimVo, claimInterfaceLogVo);
		// ?????????????????????
		checkFeeToReins(claimVo, prpLCompensate, checkFeeVo);
		// ??????
		TransDataForReinsCaseVo("3",claimVo, claimInterfaceLogVo);
	}
	
	/**
	 * vat???????????????
	 * @param claimVo
	 * @param prpLCompensate
	 * @param curTime
	 * @param kindPrePayTaxDiffrentValueMap
	 * @param kindPaymentTaxDiffrentValueMap
	 * @param kindChargeTaxDiffrentValueMap
	 * @throws Exception
	 */
	public  void vatWriteBackTransDataForCompensateVo(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate,int curTime,Map<Long,BigDecimal> kindPrePayTaxDiffrentValueMap,Map<String,BigDecimal> kindPaymentTaxDiffrentValueMap,Map<String,BigDecimal> kindChargeTaxDiffrentValueMap) throws Exception{
		// vat??????????????? ,?????????????????????????????????
		ins.sino.claimcar.reinsurance.vo.CompensateVo compensateVo = new ins.sino.claimcar.reinsurance.vo.CompensateVo();
		// ????????????
		if(claimVo!=null&&prpLCompensate!=null){
			int times = logService.getReinsuranceInterfaceRequestTimes(claimVo.getRegistNo(),prpLCompensate.getCompensateNo())+1;
			//???????????????????????????
			List<PrplReinsTrace> reinsTraceList = new ArrayList<PrplReinsTrace>();
			
			// step1:??????????????????????????????????????????????????????????????????????????????????????????
			//???????????????????????????????????? key???kindCode
			Map<String,Double> kindRealpayMap = new HashMap<String,Double>();
			
			// ??????????????????????????????
			calculateKindSumRealPay(kindRealpayMap,prpLCompensate);
			
			// ??????????????????,key???kindCode
			Map<String, BigDecimal> kindPayRate = new HashMap<String, BigDecimal>();
			
			if(kindRealpayMap.get(CodeConstants.ALLKINDSUMREALPAY) != 0.00){
			// ????????????????????????????????????
			    for (String key : kindRealpayMap.keySet()) {
				    kindPayRate.put(key, new BigDecimal(kindRealpayMap.get(key) / kindRealpayMap.get(CodeConstants.ALLKINDSUMREALPAY)));
				}
			}
			
			ins.sino.claimcar.reinsurance.vo.PrpLClaimVo PrpLclaimDto = new ins.sino.claimcar.reinsurance.vo.PrpLClaimVo();
			PrpLCMainVo cmain = policyViewService.findPolicyInfoByPaltform(prpLCompensate.getRegistNo(),
					prpLCompensate.getPolicyNo());
			PrpLclaimDto.setClaimNo(claimVo.getClaimNo());
			PrpLclaimDto.setRiskCode(claimVo.getRiskCode());
			PrpLclaimDto.setRegistNo(claimVo.getRegistNo());
			PrpLclaimDto.setPolicyNo(claimVo.getPolicyNo());
			PrpLclaimDto.setCurrency("CNY");
			PrpLclaimDto.setDamageStartDate(claimVo.getDamageTime());
			PrpLclaimDto.setDamageEndDate(claimVo.getDamageTime());
			PrpLclaimDto.setDamageCode(claimVo.getDamageCode());
			PrpLclaimDto.setAddressCode(claimVo.getDamageAreaCode());
			// PrpLclaimDto.setAgentName(StringUtils.isEmpty(cmain.getAgentCode())?"":cmain.getAgentCode());
			// PrpLclaimDto.setMakeCom(StringUtils.isEmpty(cmain.getComCode())?"":cmain.getComCode());
			if(cmain!=null){
				PrpLclaimDto.setAgentName(cmain.getAgentCode());
				PrpLclaimDto.setMakeCom(cmain.getComCode());
				PrpLclaimDto.setHandlerCode(cmain.getHandlerCode());
			}else{
				PrpLclaimDto.setAgentName("");
				PrpLclaimDto.setMakeCom("");
				PrpLclaimDto.setHandlerCode("");
			}
			PrpLclaimDto.setInputDate(claimVo.getCreateTime());
			PrpLclaimDto.setDamageKind(claimVo.getRiskCode()); // ??????????????? ?????????????????? ** ???????????? DamageKind ???riskcode
			PrpLclaimDto.setSumClaim(DataUtils.NullToZero(claimVo.getSumClaim()).doubleValue());

			SysCodeDictVo sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode",claimVo.getDamageCode());
			if(sys == null){
				sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode2",claimVo.getDamageCode());
			}
			PrpLclaimDto.setCodeCName(sys.getCodeName());
			PrpLclaimDto.setDamageAreaPostCode(claimVo.getDamageAreaCode()); // ??????????????? ????????????
			PrpLclaimDto.setEndCaseFlag("0"); // 15869 ????????????????????????????????????????????????  2019-12-25 11:55:24
			PrpLclaimDto.setDangerNo(1);

			// ??????????????????
			ins.sino.claimcar.reinsurance.vo.PrpLCompensateVo prpLcompensateDto = new ins.sino.claimcar.reinsurance.vo.PrpLCompensateVo();
			prpLcompensateDto.setCompensateNo(prpLCompensate.getCompensateNo());
			prpLcompensateDto.setCaseNo(prpLCompensate.getCaseNo());
			System.out.println(prpLCompensate.getRiskCode());
			if(prpLCompensate!=null&&prpLCompensate.getRiskCode()!=null&&prpLCompensate.getRiskCode().length()>=2){
				prpLcompensateDto.setclassCode(prpLCompensate.getRiskCode().substring(0,2));
			}else{
				prpLcompensateDto.setclassCode("");
			}

			prpLcompensateDto.setRiskCode(prpLCompensate.getRiskCode());
			prpLcompensateDto.setClaimNo(prpLCompensate.getClaimNo());
			prpLcompensateDto.setPolicyNo(prpLCompensate.getPolicyNo());
			prpLcompensateDto.setCurrency("CNY");
			prpLcompensateDto.setSumLoss(DataUtils.NullToZero(prpLCompensate.getSumLoss()).doubleValue());
			prpLcompensateDto.setMakeCom(prpLCompensate.getMakeCom());
			prpLcompensateDto.setComCode(prpLCompensate.getComCode());
			prpLcompensateDto.setHandler1Code(prpLCompensate.getHandler1Code());
			prpLcompensateDto.setInputDate(prpLCompensate.getCreateTime());
			prpLcompensateDto.setUnderWriteEndDate(claimVo.getDamageTime());
			prpLcompensateDto.setUnderWriteFlag("1");
			prpLcompensateDto.setEndcaseDate(prpLCompensate.getUnderwriteDate());

			// ?????????????????????????????????
			List<PrpLLossVo> prpLlossList = new ArrayList<PrpLLossVo>();
			List<PrpLLossItemVo> prpLLossItemVo = prpLCompensate.getPrpLLossItems();
			if(prpLLossItemVo!=null&&prpLLossItemVo.size()>0){ // ????????????
				int serialNo = 1;
				for(PrpLLossItemVo prpLLoss:prpLLossItemVo){
					PrpLLossVo lossItem = new PrpLLossVo();
					lossItem.setCompensateNo(prpLCompensate.getCompensateNo());
					lossItem.setRiskCode(prpLLoss.getRiskCode());
					lossItem.setPolicyNo(prpLLoss.getPolicyNo());
					lossItem.setSerialNo(serialNo);
					lossItem.setKindCode(prpLLoss.getKindCode());
					SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",
							prpLLoss.getKindCode());
					lossItem.setKindName(sysVo.getCodeName());
					lossItem.setItemCode(prpLLoss.getItemId());
					lossItem.setLossName(prpLLoss.getItemName());
					lossItem.setSumLoss(DataUtils.NullToZero(prpLLoss.getSumLoss()).doubleValue());
					lossItem.setCurrency("CNY");
					//lossItem.setSumRealPay(DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue());
					// ???????????????????????????????????????
					lossItem.setSumRealPay(sharePaymentTax(prpLLoss.getKindCode(),DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue(),DataUtils.NullToZero(prpLLoss.getOffPreAmt()).doubleValue(),kindRealpayMap,kindPaymentTaxDiffrentValueMap));
					
					lossItem.setExceptDeductiblePay(0);
					lossItem.setDangerNo(1);
					prpLlossList.add(lossItem);
					
					//??????????????????????????????
					PrplReinsTrace prplReinsTrace = new PrplReinsTrace();
					prplReinsTrace.setRegistNo(claimVo.getRegistNo());
					prplReinsTrace.setRiskCode(claimVo.getRiskCode());
					prplReinsTrace.setKindCode(prpLLoss.getKindCode());
					prplReinsTrace.setLossId(prpLLoss.getId());
					prplReinsTrace.setSerialNo(new Long((long)serialNo));
					prplReinsTrace.setLossType(ExpType.CAR);
					prplReinsTrace.setTimes(new Long((long)times));
					prplReinsTrace.setVatBackTimes(new Long((long)curTime));
					prplReinsTrace.setSendAmt(new BigDecimal(lossItem.getSumRealPay()));
					prplReinsTrace.setValidFlag(YN01.Y);
					prplReinsTrace.setCreateTime(new Date());
					prplReinsTrace.setUpdatetime(new Date());
					reinsTraceList.add(prplReinsTrace);
					serialNo++;
				}
			}
			List<PrpLLossPropVo> prpLLossPropVo = prpLCompensate.getPrpLLossProps();
			if(prpLLossPropVo!=null&& !prpLLossPropVo.isEmpty()){ // ????????????
				int serialNo = 10;
				for(PrpLLossPropVo prpLLoss:prpLLossPropVo){
					PrpLLossVo lossItem = new PrpLLossVo();
					lossItem.setCompensateNo(prpLCompensate.getCompensateNo());
					lossItem.setRiskCode(prpLLoss.getRiskCode());
					lossItem.setPolicyNo(prpLLoss.getPolicyNo());
					lossItem.setSerialNo(serialNo);
					lossItem.setKindCode(prpLLoss.getKindCode());
					SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",
							prpLLoss.getKindCode());
					lossItem.setKindName(sysVo.getCodeName());
					lossItem.setItemCode(prpLLoss.getItemId());
					lossItem.setLossName(prpLLoss.getItemName());
					lossItem.setSumLoss(DataUtils.NullToZero(prpLLoss.getSumLoss()).doubleValue());
					lossItem.setCurrency("CNY");
					//lossItem.setSumRealPay(DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue());
					if (kindPrePayTaxDiffrentValueMap.containsKey(prpLLoss.getId())) {
						
						double preDiffrentTax= kindPrePayTaxDiffrentValueMap.get(prpLLoss.getId()).doubleValue();
						lossItem.setSumRealPay(sharePaymentTax(prpLLoss.getKindCode(),
								DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue(),
								DataUtils.NullToZero(prpLLoss.getOffPreAmt()).doubleValue(), kindRealpayMap,
								kindPaymentTaxDiffrentValueMap)+preDiffrentTax);
					} else {
						lossItem.setSumRealPay(sharePaymentTax(prpLLoss.getKindCode(),
								DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue(),
								DataUtils.NullToZero(prpLLoss.getOffPreAmt()).doubleValue(), kindRealpayMap,
								kindPaymentTaxDiffrentValueMap));
					}
					lossItem.setExceptDeductiblePay(0);
					lossItem.setDangerNo(1);
					prpLlossList.add(lossItem);
					
					//??????????????????????????????
					PrplReinsTrace prplReinsTrace = new PrplReinsTrace();
					prplReinsTrace.setRegistNo(claimVo.getRegistNo());
					prplReinsTrace.setRiskCode(claimVo.getRiskCode());
					prplReinsTrace.setKindCode(prpLLoss.getKindCode());
					prplReinsTrace.setLossId(prpLLoss.getId());
					prplReinsTrace.setSerialNo(new Long((long)serialNo));
					prplReinsTrace.setLossType(ExpType.PROP);
					prplReinsTrace.setTimes(new Long((long)times));
					prplReinsTrace.setVatBackTimes(new Long((long)curTime));
					prplReinsTrace.setSendAmt(new BigDecimal(lossItem.getSumRealPay()));
					prplReinsTrace.setValidFlag(YN01.Y);
					prplReinsTrace.setCreateTime(new Date());
					prplReinsTrace.setUpdatetime(new Date());
					reinsTraceList.add(prplReinsTrace);
					serialNo++;
				}
			}

			// ?????????????????????
			List<PrpLpersonLossVo> prpLpersonLossList = new ArrayList<PrpLpersonLossVo>();
			List<PrpLLossPersonVo> prpLLossPersonVo = prpLCompensate.getPrpLLossPersons();
			if(prpLLossPersonVo!=null&& !prpLLossPersonVo.isEmpty()){
				int serialNo = 1;
				for(PrpLLossPersonVo prpLLossPerson:prpLLossPersonVo){
					PrpLpersonLossVo PrpLpersonLoss = new PrpLpersonLossVo();
					PrpLpersonLoss.setCompensateNo(prpLCompensate.getCompensateNo());
					PrpLpersonLoss.setRiskCode(prpLLossPerson.getRiskCode());
					PrpLpersonLoss.setPolicyNo(prpLLossPerson.getPolicyNo());
					PrpLpersonLoss.setSerialNo(serialNo);
					PrpLpersonLoss.setPersonNo(serialNo);
					PrpLpersonLoss.setItemKindNo(serialNo);
					PrpLpersonLoss.setKindCode(prpLLossPerson.getKindCode());
					SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",
							prpLLossPerson.getKindCode());
					PrpLpersonLoss.setKindName(sysVo.getCodeName());
					PrpLpersonLoss.setCurrency("CNY");
					//PrpLpersonLoss.setSumRealPay(DataUtils.NullToZero(prpLLossPerson.getSumRealPay()).doubleValue());
					PrpLpersonLoss.setSumRealPay(sharePaymentTax(prpLLossPerson.getKindCode(),DataUtils.NullToZero(prpLLossPerson.getSumRealPay()).doubleValue(),DataUtils.NullToZero(prpLLossPerson.getOffPreAmt()).doubleValue(),kindRealpayMap,kindPaymentTaxDiffrentValueMap));
					PrpLpersonLoss.setExceptDeductiblePay(0);
					PrpLpersonLoss.setDangerNo(1);
					prpLpersonLossList.add(PrpLpersonLoss);
					
					//??????????????????????????????
					PrplReinsTrace prplReinsTrace = new PrplReinsTrace();
					prplReinsTrace.setRegistNo(claimVo.getRegistNo());
					prplReinsTrace.setRiskCode(claimVo.getRiskCode());
					prplReinsTrace.setKindCode(prpLLossPerson.getKindCode());
					prplReinsTrace.setLossId(prpLLossPerson.getId());
					prplReinsTrace.setSerialNo(new Long((long)serialNo));
					prplReinsTrace.setLossType(ExpType.PERSON);
					prplReinsTrace.setTimes(new Long((long)times));
					prplReinsTrace.setVatBackTimes(new Long((long)curTime));
					prplReinsTrace.setSendAmt(new BigDecimal(PrpLpersonLoss.getSumRealPay()));
					prplReinsTrace.setValidFlag(YN01.Y);
					prplReinsTrace.setCreateTime(new Date());
					prplReinsTrace.setUpdatetime(new Date());
					reinsTraceList.add(prplReinsTrace);
					
					serialNo++ ;
					
				}
			}

			// ???????????????
			List<PrpLChargeList> prpLChargeList = new ArrayList<PrpLChargeList>();
			List<PrpLChargeVo> prpLChargeVo = prpLCompensate.getPrpLCharges();
			if(prpLChargeVo!=null&& !prpLChargeVo.isEmpty()){
				int serialNo = 1;
				for(PrpLChargeVo prpLCharge:prpLChargeVo){
					
					// 2151 ????????????????????????????????????(????????????????????????)??????????????????????????????
					if(!CodeConstants.CHARGECODE_13.equals(prpLCharge.getChargeCode())){
						ins.sino.claimcar.reinsurance.vo.PrpLChargeList PrpLChargeVo = new ins.sino.claimcar.reinsurance.vo.PrpLChargeList();
						PrpLChargeVo.setCompensateNo(prpLCompensate.getCompensateNo());
						PrpLChargeVo.setRiskCode(prpLCharge.getRiskCode());
						PrpLChargeVo.setPolicyNo(prpLCharge.getPolicyNo());
						PrpLChargeVo.setSerialNo((serialNo)+"");
						PrpLChargeVo.setKindCode(prpLCharge.getKindCode());
	
						SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",prpLCharge.getKindCode());
	
						PrpLChargeVo.setKindName(sysVo.getCodeName());
						PrpLChargeVo.setChargeCode(prpLCharge.getChargeCode());
						PrpLChargeVo.setChargeName(prpLCharge.getChargeName());
						PrpLChargeVo.setCurrency("CNY");
						
						if((YN01.Y).equals(prpLCharge.getVatInvoiceFlag())){
							// ??????key
							StringBuffer keyBF = new StringBuffer(prpLCharge.getPayeeId().toString()).append("-").append(prpLCharge.getKindCode()).append("-").append(prpLCharge.getChargeCode());
							
							//???????????????????????????????????????
							if(kindChargeTaxDiffrentValueMap.containsKey(keyBF.toString())){
							    PrpLChargeVo.setChargeAmount(kindChargeTaxDiffrentValueMap.get(keyBF.toString()).doubleValue());
							}else{
								PrpLChargeVo.setChargeAmount(0.00);
							}
						}else{
							PrpLChargeVo.setChargeAmount(0.00);   
						}
						PrpLChargeVo.setSumRealPay(0); // //???????????? ?????? ***????????????????????????????????????????????? ?????????0
						PrpLChargeVo.setDangerNo(1);
						PrpLChargeVo.setExceptDeductiblePay(0); // /
						prpLChargeList.add(PrpLChargeVo);
						
						//??????????????????????????????
						PrplReinsTrace prplReinsTrace = new PrplReinsTrace();
						prplReinsTrace.setRegistNo(claimVo.getRegistNo());
						prplReinsTrace.setRiskCode(claimVo.getRiskCode());
						prplReinsTrace.setKindCode(prpLCharge.getKindCode());
						prplReinsTrace.setChargeCode(prpLCharge.getChargeCode());
						prplReinsTrace.setSerialNo(new Long((long)serialNo));
						prplReinsTrace.setTimes(new Long((long)times));
						prplReinsTrace.setVatBackTimes(new Long((long)curTime));
						prplReinsTrace.setSendAmt(new BigDecimal(PrpLChargeVo.getChargeAmount()));
						prplReinsTrace.setValidFlag(YN01.Y);
						prplReinsTrace.setCreateTime(new Date());
						prplReinsTrace.setUpdatetime(new Date());
						reinsTraceList.add(prplReinsTrace);
						
						serialNo++;
					}
				}

			}
			
			if(claimVo.getCaseNo()==null){
				compensateVo.setBusinessType("0"); // ???????????????????????? ????????????????????? ????????????
			}else{
				compensateVo.setBusinessType("1");
			}
			
			compensateVo.setAdjustTimes((times)+"");
			compensateVo.setPrpLClaimVo(PrpLclaimDto);
			compensateVo.setPrpLCompensateVo(prpLcompensateDto);
			compensateVo.setPrpLLossVo(prpLlossList);
			compensateVo.setPrpLpersonLossVo(prpLpersonLossList);
			compensateVo.setPrpLChargeList(prpLChargeList);
			compensateVo.setPrpLcfeecoinsVo(null);

			ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // ???????????????
			claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
			claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
			claimInterfaceLogVo.setCompensateNo(prpLCompensate.getCompensateNo());
			claimInterfaceLogVo.setComCode(claimVo.getComCode());
			claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
			claimInterfaceLogVo.setCreateTime(new Date());
			claimInterfaceLogVo.setOperateNode(FlowNode.VClaim.getName());
			claimInterfaceLogVo.setBusinessType(BusinessType.Reinsurance_vatBack.name());
			claimInterfaceLogVo.setBusinessName(BusinessType.Reinsurance_vatBack.getName());
			
			//????????????????????????
			recordReinsTraces(claimVo.getRegistNo(),claimVo.getRiskCode(),times,reinsTraceList);
			
			callReinsWebService.callCompensateForClient(compensateVo,claimInterfaceLogVo);
			
			
		}else{
			logger.info("?????????????????????????????????");
		}
	}
	
	/**
	 * ??????????????????????????????
	 * @param kindCode
	 * @param kindItemRealPay
	 * @param kindRealpayMap
	 * @param kindPaymentTaxDiffrentValueMap
	 * @return
	 */
	private double sharePaymentTax(String kindCode,Double kindItemRealPay,Double offPreAmt,Map<String,Double> kindRealpayMap,Map<String,BigDecimal> kindPaymentTaxDiffrentValueMap){
		if (kindItemRealPay == 0.00) {
			return kindItemRealPay;
		} else {

			double splitTax = 0.00;
			
			// ??????????????????????????????????????????????????????
			BigDecimal itemKindRate = new BigDecimal(1);
			
			if(kindRealpayMap.get(kindCode) != 0.00){
				itemKindRate= new BigDecimal(kindItemRealPay / kindRealpayMap.get(kindCode));
			}
			if(kindPaymentTaxDiffrentValueMap.containsKey(kindCode)){
			   splitTax = kindPaymentTaxDiffrentValueMap.get(kindCode).multiply(itemKindRate).doubleValue();
			}
			return splitTax;
			
		}
		
	}
	
	
	/**
	 * ???????????????????????????
	 * <pre></pre>
	 * 
	 * @param claimVo
	 * @param prpLCompensate
	 * @throws Exception
	 *
	 */
	@Override
	public  void transDataForWashTransaction(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate) throws Exception{
		// ?????????????????????????????????????????????????????????????????????
        ins.sino.claimcar.reinsurance.vo.CompensateVo compensateVo = new ins.sino.claimcar.reinsurance.vo.CompensateVo();
		
		// ????????????
		if(claimVo!=null&&prpLCompensate!=null){
			
			//???????????????
			int times = logService.getReinsuranceInterfaceRequestTimes(claimVo.getRegistNo(),prpLCompensate.getCompensateNo());
			
			//???????????????????????????
			List<PrplReinsTrace> reinsTraceList = new ArrayList<PrplReinsTrace>();
			
			//?????????????????????????????????
			boolean hisFlag = verifyIsHisWashTransaction(prpLCompensate.getRegistNo(),prpLCompensate.getRiskCode());
			
			
			ins.sino.claimcar.reinsurance.vo.PrpLClaimVo PrpLclaimDto = new ins.sino.claimcar.reinsurance.vo.PrpLClaimVo();
			PrpLCMainVo cmain = policyViewService.findPolicyInfoByPaltform(prpLCompensate.getRegistNo(),
					prpLCompensate.getPolicyNo());
			PrpLclaimDto.setClaimNo(claimVo.getClaimNo());
			PrpLclaimDto.setRiskCode(claimVo.getRiskCode());
			PrpLclaimDto.setRegistNo(claimVo.getRegistNo());
			PrpLclaimDto.setPolicyNo(claimVo.getPolicyNo());
			PrpLclaimDto.setCurrency("CNY");
			PrpLclaimDto.setDamageStartDate(claimVo.getDamageTime());
			PrpLclaimDto.setDamageEndDate(claimVo.getDamageTime());
			PrpLclaimDto.setDamageCode(claimVo.getDamageCode());
			PrpLclaimDto.setAddressCode(claimVo.getDamageAreaCode());
			// PrpLclaimDto.setAgentName(StringUtils.isEmpty(cmain.getAgentCode())?"":cmain.getAgentCode());
			// PrpLclaimDto.setMakeCom(StringUtils.isEmpty(cmain.getComCode())?"":cmain.getComCode());
			if(cmain!=null){
				PrpLclaimDto.setAgentName(cmain.getAgentCode());
				PrpLclaimDto.setMakeCom(cmain.getComCode());
				PrpLclaimDto.setHandlerCode(cmain.getHandlerCode());
			}else{
				PrpLclaimDto.setAgentName("");
				PrpLclaimDto.setMakeCom("");
				PrpLclaimDto.setHandlerCode("");
			}
			PrpLclaimDto.setInputDate(claimVo.getCreateTime());
			PrpLclaimDto.setDamageKind(claimVo.getRiskCode()); // ??????????????? ?????????????????? ** ???????????? DamageKind ???riskcode
			PrpLclaimDto.setSumClaim(DataUtils.NullToZero(claimVo.getSumClaim()).doubleValue());

			SysCodeDictVo sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode",claimVo.getDamageCode());
			if(sys == null){
				sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode2",claimVo.getDamageCode());
			}
			PrpLclaimDto.setCodeCName(sys.getCodeName());
			PrpLclaimDto.setDamageAreaPostCode(claimVo.getDamageAreaCode()); // ??????????????? ????????????
			PrpLclaimDto.setEndCaseFlag("0"); // 15869 ????????????????????????????????????????????????  2019-12-25 11:55:24
			PrpLclaimDto.setDangerNo(1);

			// ??????????????????
			ins.sino.claimcar.reinsurance.vo.PrpLCompensateVo prpLcompensateDto = new ins.sino.claimcar.reinsurance.vo.PrpLCompensateVo();
			prpLcompensateDto.setCompensateNo(prpLCompensate.getCompensateNo());
			prpLcompensateDto.setCaseNo(prpLCompensate.getCaseNo());
			System.out.println(prpLCompensate.getRiskCode());
			if(prpLCompensate!=null&&prpLCompensate.getRiskCode()!=null&&prpLCompensate.getRiskCode().length()>=2){
				prpLcompensateDto.setclassCode(prpLCompensate.getRiskCode().substring(0,2));
			}else{
				prpLcompensateDto.setclassCode("");
			}

			prpLcompensateDto.setRiskCode(prpLCompensate.getRiskCode());
			prpLcompensateDto.setClaimNo(prpLCompensate.getClaimNo());
			prpLcompensateDto.setPolicyNo(prpLCompensate.getPolicyNo());
			prpLcompensateDto.setCurrency("CNY");
			prpLcompensateDto.setSumLoss(DataUtils.NullToZero(prpLCompensate.getSumLoss()).doubleValue());
			prpLcompensateDto.setMakeCom(prpLCompensate.getMakeCom());
			prpLcompensateDto.setComCode(prpLCompensate.getComCode());
			prpLcompensateDto.setHandler1Code(prpLCompensate.getHandler1Code());
			prpLcompensateDto.setInputDate(prpLCompensate.getCreateTime());
			prpLcompensateDto.setUnderWriteEndDate(claimVo.getDamageTime());
			prpLcompensateDto.setUnderWriteFlag("1");
			prpLcompensateDto.setEndcaseDate(prpLCompensate.getUnderwriteDate());

			// ?????????????????????????????????
			List<PrpLLossVo> prpLlossList = new ArrayList<PrpLLossVo>();
			List<PrpLLossItemVo> prpLLossItemVo = prpLCompensate.getPrpLLossItems();
			if(prpLLossItemVo!=null&&prpLLossItemVo.size()>0){ // ????????????
				int serialNo = 1;
				for(PrpLLossItemVo prpLLoss:prpLLossItemVo){
					PrpLLossVo lossItem = new PrpLLossVo();
					lossItem.setCompensateNo(prpLCompensate.getCompensateNo());
					lossItem.setRiskCode(prpLLoss.getRiskCode());
					lossItem.setPolicyNo(prpLLoss.getPolicyNo());
					lossItem.setSerialNo(serialNo);
					lossItem.setKindCode(prpLLoss.getKindCode());
					SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",
							prpLLoss.getKindCode());
					lossItem.setKindName(sysVo.getCodeName());
					lossItem.setItemCode(prpLLoss.getItemId());
					lossItem.setLossName(prpLLoss.getItemName());
					lossItem.setSumLoss(DataUtils.NullToZero(prpLLoss.getSumLoss()).doubleValue());
					lossItem.setCurrency("CNY");
					//lossItem.setSumRealPay(DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue());
					// ???????????????????????????????????????????????????????????? ???????????????????????????????????????lossType???????????????lossId???????????????????????????
					if(hisFlag){
						lossItem.setSumRealPay(DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue());
					}else{
						lossItem.setSumRealPay(getWashTransactionAmt(prpLCompensate.getRegistNo(),prpLCompensate.getRiskCode(),prpLLoss.getKindCode(),prpLLoss.getId().toString(),""));
					}
					lossItem.setExceptDeductiblePay(0);
					lossItem.setDangerNo(1);
					prpLlossList.add(lossItem);
					
					//??????????????????????????????
					PrplReinsTrace prplReinsTrace = new PrplReinsTrace();
					prplReinsTrace.setRegistNo(claimVo.getRegistNo());
					prplReinsTrace.setRiskCode(claimVo.getRiskCode());
					prplReinsTrace.setKindCode(prpLLoss.getKindCode());
					prplReinsTrace.setLossId(prpLLoss.getId());
					prplReinsTrace.setSerialNo(new Long((long)serialNo));
					prplReinsTrace.setLossType(ExpType.CAR);
					prplReinsTrace.setTimes(new Long((long)times));
					prplReinsTrace.setVatBackTimes(0L);
					prplReinsTrace.setSendAmt(new BigDecimal(lossItem.getSumRealPay()));
					prplReinsTrace.setValidFlag(YN01.Y);
					prplReinsTrace.setCreateTime(new Date());
					prplReinsTrace.setUpdatetime(new Date());
					reinsTraceList.add(prplReinsTrace);
					
					serialNo++;
				}
			}
			List<PrpLLossPropVo> prpLLossPropVo = prpLCompensate.getPrpLLossProps();
			if(prpLLossPropVo!=null&& !prpLLossPropVo.isEmpty()){ // ????????????
				int serialNo = 10;
				for(PrpLLossPropVo prpLLoss:prpLLossPropVo){
					PrpLLossVo lossItem = new PrpLLossVo();
					lossItem.setCompensateNo(prpLCompensate.getCompensateNo());
					lossItem.setRiskCode(prpLLoss.getRiskCode());
					lossItem.setPolicyNo(prpLLoss.getPolicyNo());
					lossItem.setSerialNo(serialNo);
					lossItem.setKindCode(prpLLoss.getKindCode());
					SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",
							prpLLoss.getKindCode());
					lossItem.setKindName(sysVo.getCodeName());
					lossItem.setItemCode(prpLLoss.getItemId());
					lossItem.setLossName(prpLLoss.getItemName());
					lossItem.setSumLoss(DataUtils.NullToZero(prpLLoss.getSumLoss()).doubleValue());
					lossItem.setCurrency("CNY");
					if(hisFlag){
						lossItem.setSumRealPay(DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue());
					}else{
						lossItem.setSumRealPay(getWashTransactionAmt(prpLCompensate.getRegistNo(),prpLCompensate.getRiskCode(),prpLLoss.getKindCode(),prpLLoss.getId().toString(),""));
					}
					lossItem.setExceptDeductiblePay(0);
					lossItem.setDangerNo(1);
					prpLlossList.add(lossItem);
					
					//??????????????????????????????
					PrplReinsTrace prplReinsTrace = new PrplReinsTrace();
					prplReinsTrace.setRegistNo(claimVo.getRegistNo());
					prplReinsTrace.setRiskCode(claimVo.getRiskCode());
					prplReinsTrace.setKindCode(prpLLoss.getKindCode());
					prplReinsTrace.setLossId(prpLLoss.getId());
					prplReinsTrace.setSerialNo(new Long((long)serialNo));
					prplReinsTrace.setLossType(ExpType.PROP);
					prplReinsTrace.setTimes(new Long((long)times));
					prplReinsTrace.setVatBackTimes(0L);
					prplReinsTrace.setSendAmt(new BigDecimal(lossItem.getSumRealPay()));
					prplReinsTrace.setValidFlag(YN01.Y);
					prplReinsTrace.setCreateTime(new Date());
					prplReinsTrace.setUpdatetime(new Date());
					reinsTraceList.add(prplReinsTrace);
					
					serialNo++;
				}
			}

			// ?????????????????????
			List<PrpLpersonLossVo> prpLpersonLossList = new ArrayList<PrpLpersonLossVo>();
			List<PrpLLossPersonVo> prpLLossPersonVo = prpLCompensate.getPrpLLossPersons();
			if(prpLLossPersonVo!=null&& !prpLLossPersonVo.isEmpty()){
				int serialNo = 1;
				for(PrpLLossPersonVo prpLLossPerson:prpLLossPersonVo){
					PrpLpersonLossVo PrpLpersonLoss = new PrpLpersonLossVo();
					PrpLpersonLoss.setCompensateNo(prpLCompensate.getCompensateNo());
					PrpLpersonLoss.setRiskCode(prpLLossPerson.getRiskCode());
					PrpLpersonLoss.setPolicyNo(prpLLossPerson.getPolicyNo());
					PrpLpersonLoss.setSerialNo(serialNo);
					PrpLpersonLoss.setPersonNo(serialNo);
					PrpLpersonLoss.setItemKindNo(serialNo);
					PrpLpersonLoss.setKindCode(prpLLossPerson.getKindCode());
					SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",
							prpLLossPerson.getKindCode());
					PrpLpersonLoss.setKindName(sysVo.getCodeName());
					PrpLpersonLoss.setCurrency("CNY");
					if(hisFlag){
						PrpLpersonLoss.setSumRealPay(DataUtils.NullToZero(prpLLossPerson.getSumRealPay()).doubleValue());
					}else{
					    PrpLpersonLoss.setSumRealPay(getWashTransactionAmt(prpLCompensate.getRegistNo(),prpLCompensate.getRiskCode(),prpLLossPerson.getKindCode(),prpLLossPerson.getId().toString(),""));
					}
					PrpLpersonLoss.setExceptDeductiblePay(0);
					PrpLpersonLoss.setDangerNo(1);
					prpLpersonLossList.add(PrpLpersonLoss);
					
					//??????????????????????????????
					PrplReinsTrace prplReinsTrace = new PrplReinsTrace();
					prplReinsTrace.setRegistNo(claimVo.getRegistNo());
					prplReinsTrace.setRiskCode(claimVo.getRiskCode());
					prplReinsTrace.setKindCode(prpLLossPerson.getKindCode());
					prplReinsTrace.setLossId(prpLLossPerson.getId());
					prplReinsTrace.setSerialNo(new Long((long)serialNo));
					prplReinsTrace.setLossType(ExpType.PERSON);
					prplReinsTrace.setTimes(new Long((long)times));
					prplReinsTrace.setVatBackTimes(0L);
					prplReinsTrace.setSendAmt(new BigDecimal(PrpLpersonLoss.getSumRealPay()));
					prplReinsTrace.setValidFlag(YN01.Y);
					prplReinsTrace.setCreateTime(new Date());
					prplReinsTrace.setUpdatetime(new Date());
					reinsTraceList.add(prplReinsTrace);
					
					
					serialNo++ ;
					
				}
			}

			// ???????????????
			List<PrpLChargeList> prpLChargeList = new ArrayList<PrpLChargeList>();
			List<PrpLChargeVo> prpLChargeVo = prpLCompensate.getPrpLCharges();
			if(prpLChargeVo!=null&& !prpLChargeVo.isEmpty()){
				int serialNo = 1;
				for(PrpLChargeVo prpLCharge:prpLChargeVo){
					
					// 2151 ????????????????????????????????????(????????????????????????)??????????????????????????????
					if(!CodeConstants.CHARGECODE_13.equals(prpLCharge.getChargeCode())){
						ins.sino.claimcar.reinsurance.vo.PrpLChargeList PrpLChargeVo = new ins.sino.claimcar.reinsurance.vo.PrpLChargeList();
						PrpLChargeVo.setCompensateNo(prpLCompensate.getCompensateNo());
						PrpLChargeVo.setRiskCode(prpLCharge.getRiskCode());
						PrpLChargeVo.setPolicyNo(prpLCharge.getPolicyNo());
						PrpLChargeVo.setSerialNo((serialNo)+"");
						PrpLChargeVo.setKindCode(prpLCharge.getKindCode());
	
						SysCodeDictVo sysVo = codeTranServiceSpringImpl.findTransCodeDictVo("KindCode",prpLCharge.getKindCode());
	
						PrpLChargeVo.setKindName(sysVo.getCodeName());
						PrpLChargeVo.setChargeCode(prpLCharge.getChargeCode());
						PrpLChargeVo.setChargeName(prpLCharge.getChargeName());
						PrpLChargeVo.setCurrency("CNY");
						if(hisFlag){
							PrpLChargeVo.setChargeAmount(DataUtils.NullToZero(prpLCharge.getFeeAmt()).doubleValue()-
									DataUtils.NullToZero(prpLCharge.getOffAmt()).doubleValue());
						}else{
						    PrpLChargeVo.setChargeAmount(getWashTransactionAmt(prpLCompensate.getRegistNo(),prpLCompensate.getRiskCode(),prpLCharge.getKindCode(),"",prpLCharge.getChargeCode()));
						}
						PrpLChargeVo.setSumRealPay(0); // //???????????? ?????? ***????????????????????????????????????????????? ?????????0
						PrpLChargeVo.setDangerNo(1);
						PrpLChargeVo.setExceptDeductiblePay(0); // /
						prpLChargeList.add(PrpLChargeVo);
						
						//??????????????????????????????
						PrplReinsTrace prplReinsTrace = new PrplReinsTrace();
						prplReinsTrace.setRegistNo(claimVo.getRegistNo());
						prplReinsTrace.setRiskCode(claimVo.getRiskCode());
						prplReinsTrace.setKindCode(prpLCharge.getKindCode());
						prplReinsTrace.setChargeCode(prpLCharge.getChargeCode());
						prplReinsTrace.setSerialNo(new Long((long)serialNo));
						prplReinsTrace.setTimes(new Long((long)times));
						prplReinsTrace.setVatBackTimes(0L);
						prplReinsTrace.setSendAmt(new BigDecimal(PrpLChargeVo.getChargeAmount()));
						prplReinsTrace.setValidFlag(YN01.Y);
						prplReinsTrace.setCreateTime(new Date());
						prplReinsTrace.setUpdatetime(new Date());
						reinsTraceList.add(prplReinsTrace);
						
						serialNo++;
					}
				}

			}
		
			if(claimVo.getCaseNo()==null){
				compensateVo.setBusinessType("0"); // ???????????????????????? ????????????????????? ????????????
			}else{
				compensateVo.setBusinessType("1");
			}
			
			compensateVo.setAdjustTimes(times+"");
			compensateVo.setPrpLClaimVo(PrpLclaimDto);
			compensateVo.setPrpLCompensateVo(prpLcompensateDto);
			compensateVo.setPrpLLossVo(prpLlossList);
			compensateVo.setPrpLpersonLossVo(prpLpersonLossList);
			compensateVo.setPrpLChargeList(prpLChargeList);
			compensateVo.setPrpLcfeecoinsVo(null);

			ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // ???????????????
			claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
			claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
			claimInterfaceLogVo.setCompensateNo(prpLCompensate.getCompensateNo());
			claimInterfaceLogVo.setComCode(claimVo.getComCode());
			claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
			claimInterfaceLogVo.setCreateTime(new Date());
			claimInterfaceLogVo.setOperateNode(FlowNode.VClaim.getName());
			claimInterfaceLogVo.setBusinessType(BusinessType.Reinsurance_washTransaction.name());
			claimInterfaceLogVo.setBusinessName(BusinessType.Reinsurance_washTransaction.getName());
			
			
			//????????????????????????
			recordReinsTraces(claimVo.getRegistNo(),claimVo.getRiskCode(),times,reinsTraceList);
			
			callReinsWebService.callCompensateForClient(compensateVo,claimInterfaceLogVo);
		}else{
			logger.info("?????????????????????????????????");
		}

	}
	
	/**
	 * ????????????????????????
	 * @param registNo
	 * @param riskCode
	 * @return
	 */
	private boolean verifyIsHisWashTransaction(String registNo,String riskCode){
		List<Object> paramValues = new ArrayList<Object>();
		StringBuffer queryString = new StringBuffer("select count(1) from claimuser.prplreinstrace t where  t.validflag = '1'  ");
		if(!registNo.isEmpty()){
			queryString.append(" and t.registno = ?");
			paramValues.add(registNo.trim());
		}
		if(!riskCode.isEmpty()){
			queryString.append(" and t.riskcode = ?");
			paramValues.add(riskCode.trim());
		}
		
		try {
			Object singleObj = baseDaoService.findListBySql(queryString.toString(),paramValues.toArray()).get(0);
			BigDecimal countValue= (BigDecimal)singleObj;
			int countResult = Integer.parseInt(countValue.toString()) ;
			
			if(countResult > 0){
				return false;
			}else{
				return true;
			}
			
		} catch (Exception e) {
			logger.error("????????????????????????????????????",e);
			
		}
		
		return true;
	}

	/**
	 * ??????????????????
	 * @param registNo
	 * @param riskCode
	 * @param kindCode
	 * @param lossId
	 * @param chargeCode
	 * @return
	 */
	private double getWashTransactionAmt(String registNo,String riskCode,String kindCode,String lossId,String chargeCode){
		double washTransactionAmt = -1;
		List<Object> paramValues = new ArrayList<Object>();
		StringBuffer queryString = new StringBuffer("select nvl(sum(t.sendamt),0) from claimuser.prplreinstrace t where  t.validflag = '1'  ");
		if(!registNo.isEmpty()){
			queryString.append(" and t.registno = ?");
			paramValues.add(registNo.trim());
		}
		if(!riskCode.isEmpty()){
			queryString.append(" and t.riskcode = ?");
			paramValues.add(riskCode.trim());
		}
		if(!kindCode.isEmpty()){
			queryString.append(" and t.kindcode = ?");
			paramValues.add(registNo.trim());
		}
		if(!lossId.isEmpty()){
			queryString.append(" and t.lossid = ?");
			paramValues.add(registNo.trim());
		}
		if(!chargeCode.isEmpty()){
			queryString.append(" and t.chargecode = ?");
			paramValues.add(registNo.trim());
		}
	
		try {
			Object singleObj = baseDaoService.findListBySql(queryString.toString(),paramValues.toArray()).get(0);
			BigDecimal amtValue= (BigDecimal)singleObj;
			washTransactionAmt = amtValue.doubleValue() * washTransactionAmt;
		} catch (Exception e) {
			logger.error("????????????????????????",e);
			washTransactionAmt = 0;
		}
		
		return washTransactionAmt;
	}
	
}
