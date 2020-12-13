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
 * 调用再保接口
 * 
 * @author ★NiuQian
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
			logger.debug("claimVo或者kindHisVoList为null 不能送再保！！！！！！！！！！！！！！");
		}else{
			ins.sino.claimcar.reinsurance.vo.PrpLClaimVo PrpLclaimDto = new ins.sino.claimcar.reinsurance.vo.PrpLClaimVo();
			PrpLCMainVo cmain = policyViewService.findPolicyInfoByPaltform(claimVo.getRegistNo(),claimVo.getPolicyNo());
			PrpLRegistVo registVo = registService.findByRegistNo(claimVo.getRegistNo());
			if(cmain==null){
				logger.debug("cmain为NULL！！！！！！！！！！！！！");
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
			PrpLclaimDto.setDamageAddress(registVo.getDamageAddress()); // ////// 需要讨论 **出险地名称
			SysCodeDictVo vo = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode",claimVo.getDamageCode());
			if(vo == null){
				vo = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode2",claimVo.getDamageCode());
			}
			PrpLclaimDto.setCodeCName(vo.getCodeName());
			PrpLclaimDto.setDamageKind(claimVo.getRiskCode()); // ///////需要讨论 ** 出险险别 DamageKind 就riskcode
			PrpLclaimDto.setSumClaim(DataUtils.NullToZero(claimVo.getSumClaim()).doubleValue());
			PrpLclaimDto.setDamageAreaPostCode(claimVo.getDamageAreaCode()); // ///////出险地邮编 讨论 那就传出险地代码吧
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
						pLClaimVo.setItemCode(cmain.getPrpCItemKinds().get(0).getItemCode()); // /////// 需要讨论 **list
						pLClaimVo.setCurrency("CNY");
						pLClaimVo.setSumClaim(DataUtils.NullToZero(pp.getClaimLoss()).doubleValue());
						pLClaimVo.setInputDate(pp.getAdjustTime());
						pLClaimVo.setLossFeeType(pp.getFeeType()); // /**P 赔款，Z 费用汇总
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
		
		// 立案主表
		if(claimVo!=null&&prpLCompensate!=null){
			
			//送再保次数
			int times = logService.getReinsuranceInterfaceRequestTimes(claimVo.getRegistNo(),prpLCompensate.getCompensateNo());
			
			//记录送再保信息集合
			List<PrplReinsTrace> reinsTraceList = new ArrayList<PrplReinsTrace>();
			
			// step1:计算险别赔款比例和送再保险别标的实赔金额占险别赔付金额的比例
			//赔案下险别实赔金额汇总， key为kindCode
			Map<String,Double> kindRealpayMap = new HashMap<String,Double>();
			
			// 统计各险别扣除预赔的实赔金额
			calculateKindSumRealPay(kindRealpayMap,prpLCompensate);
			
			// 存放险别比例,key为kindCode
			Map<String, BigDecimal> kindPayRate = new HashMap<String, BigDecimal>();
			
			if(kindRealpayMap.get(CodeConstants.ALLKINDSUMREALPAY) != 0.00){
			// 计算各险别的险别赔款比例
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
			PrpLclaimDto.setDamageKind(claimVo.getRiskCode()); // 和对方确认 这边无法取值 ** 出险险别 DamageKind 就riskcode
			PrpLclaimDto.setSumClaim(DataUtils.NullToZero(claimVo.getSumClaim()).doubleValue());

			SysCodeDictVo sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode",claimVo.getDamageCode());
			if(sys == null){
				sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode2",claimVo.getDamageCode());
			}
			PrpLclaimDto.setCodeCName(sys.getCodeName());
			PrpLclaimDto.setDamageAreaPostCode(claimVo.getDamageAreaCode()); // 出险地邮编 需要商量
			PrpLclaimDto.setEndCaseFlag("0"); // 15869 车理赔已决数据送再保结案状态错误  2019-12-25 11:55:24
			PrpLclaimDto.setDangerNo(1);

			// 理算主表信息
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

			// 理算车辆损失财产表信息
			List<PrpLLossVo> prpLlossList = new ArrayList<PrpLLossVo>();
			List<PrpLLossItemVo> prpLLossItemVo = prpLCompensate.getPrpLLossItems();
			if(prpLLossItemVo!=null&&prpLLossItemVo.size()>0){ // 车辆损失
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
					// 获取支付对象类型为修理厂的预付信息集合， 预付已经归属到了险别，根据lossType字段拆分出lossId可以明确到出险标的
					lossItem.setSumRealPay(splitTaxFromPayment(prpLLoss.getKindCode(),DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue(),DataUtils.NullToZero(prpLLoss.getOffPreAmt()).doubleValue(), kindRealpayMap,kindPayRate, prpLCompensate)
							- addTaxFromPrepay(prpLCompensate.getRegistNo(),prpLCompensate.getRiskCode(), prpLLoss.getId().toString()));

					lossItem.setExceptDeductiblePay(0);
					lossItem.setDangerNo(1);
					prpLlossList.add(lossItem);
					
					//记录送再保的车损信息
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
			if(prpLLossPropVo!=null&& !prpLLossPropVo.isEmpty()){ // 财产损失
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
					
					//记录送再保的财损信息
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

			// 人伤损失表信息
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
					
					//记录送再保的人伤信息
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

			// 费用信息表
			List<PrpLChargeList> prpLChargeList = new ArrayList<PrpLChargeList>();
			List<PrpLChargeVo> prpLChargeVo = prpLCompensate.getPrpLCharges();
			if(prpLChargeVo!=null&& !prpLChargeVo.isEmpty()){
				int serialNo = 1;
				for(PrpLChargeVo prpLCharge:prpLChargeVo){
					
					// 2151 需要判断费用类型，查勘费(系统无该费用类型)、公估费不需要送再保
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
							// 收款人税率
							BigDecimal payeeAddTaxRate = (new BigDecimal(prpLCharge.getVatTaxRate())).divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);
							//根据税率计算出价，送给再保
							PrpLChargeVo.setChargeAmount(((prpLCharge.getFeeAmt().subtract(prpLCharge.getOffAmt())).divide((new BigDecimal(1)).add(payeeAddTaxRate),8,BigDecimal.ROUND_HALF_UP)).doubleValue());
						}else{
							PrpLChargeVo.setChargeAmount(DataUtils.NullToZero(prpLCharge.getFeeAmt()).doubleValue()-
									DataUtils.NullToZero(prpLCharge.getOffAmt()).doubleValue());   // 预付---送再保   改2016-12-19 减去 扣减金额
						}
						PrpLChargeVo.setSumRealPay(0); // //传固定值 商量 ***有实赔金额就传实赔金额没有的话 你们穿0
						PrpLChargeVo.setDangerNo(1);
						PrpLChargeVo.setExceptDeductiblePay(0); // /
						prpLChargeList.add(PrpLChargeVo);
						
						//记录送再保的费用信息
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
		/*	// 公估费信息 
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
				PrpLChargeVo.setChargeName("公估费");
				PrpLChargeVo.setCurrency("CNY");
				PrpLChargeVo.setChargeAmount(DataUtils.NullToZero(aeesssorFee.getAmount()).doubleValue());   // 预付---送再保   改2016-12-19 减去 扣减金额
				PrpLChargeVo.setSumRealPay(0); // //传固定值 商量 ***有实赔金额就传实赔金额没有的话 你们穿0
				PrpLChargeVo.setDangerNo(1);
				PrpLChargeVo.setExceptDeductiblePay(0); // /
				prpLChargeList.add(PrpLChargeVo);

			}
*/
			// 共保赔付表信息
			// List<PrpLcfeecoinsVo> prpLcfeecoinsList = new ArrayList<PrpLcfeecoinsVo>();
			if(claimVo.getCaseNo()==null){
				compensateVo.setBusinessType("0"); // 这边没有对应字段 如何传值？？？ 结案标志
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

			ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // 填写日志表
			claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
			claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
			claimInterfaceLogVo.setCompensateNo(prpLCompensate.getCompensateNo());
			claimInterfaceLogVo.setComCode(claimVo.getComCode());
			claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
			claimInterfaceLogVo.setCreateTime(new Date());
			claimInterfaceLogVo.setOperateNode(FlowNode.VClaim.getName());
			claimInterfaceLogVo.setBusinessType(BusinessType.Reinsurance_verify.name());
			claimInterfaceLogVo.setBusinessName(BusinessType.Reinsurance_verify.getName());
			
			//记录赔款、费用价税计算轨迹
			recordPaymentAndFeeSplitTaxTrace(kindPayRate,prpLCompensate);
			
			//记录送再保的信息
			recordReinsTraces(claimVo.getRegistNo(),claimVo.getRiskCode(),times,reinsTraceList);
			
			callReinsWebService.callCompensateForClient(compensateVo,claimInterfaceLogVo);
		}else{
			logger.info("传入参数不能为空！！！");
		}

	}
	
	/**
	 * 统计险别扣除预赔的赔付金额
	 * @param kindRealpayMap
	 * @param prpLCompensate
	 */
	private void calculateKindSumRealPay(Map<String,Double> kindRealpayMap,PrpLCompensateVo prpLCompensate){
		//计算总赔款
        Double sumRealPay = 0.00;
        
		List<PrpLLossItemVo> prpLLossItemVo = prpLCompensate.getPrpLLossItems();
		if(prpLLossItemVo!=null&&prpLLossItemVo.size()>0){ // 车辆损失
			for(PrpLLossItemVo prpLLoss:prpLLossItemVo){
				
				BigDecimal lossItemAmt = prpLLoss.getSumRealPay();
				//减去预付金额
				if(prpLLoss.getOffPreAmt() != null){
					lossItemAmt = lossItemAmt.subtract(prpLLoss.getOffPreAmt());
				}
				
				//汇总险别赔付金额
				if (kindRealpayMap.containsKey(prpLLoss.getKindCode())) {
					kindRealpayMap.put(prpLLoss.getKindCode(),kindRealpayMap.get(prpLLoss.getKindCode())+DataUtils.NullToZero(lossItemAmt).doubleValue());
				} else {
					kindRealpayMap.put(prpLLoss.getKindCode(),
							DataUtils.NullToZero(lossItemAmt).doubleValue());
				}
				sumRealPay = sumRealPay+DataUtils.NullToZero(lossItemAmt).doubleValue();
			}
		}
		// 财产损失
		List<PrpLLossPropVo> prpLLossPropVo = prpLCompensate.getPrpLLossProps();
		if(prpLLossPropVo!=null&& !prpLLossPropVo.isEmpty()){ 
			
			for(PrpLLossPropVo prpLLoss:prpLLossPropVo){
				
				BigDecimal lossItemAmt = prpLLoss.getSumRealPay();
				//减去预付金额
				if(prpLLoss.getOffPreAmt() != null){
					lossItemAmt = lossItemAmt.subtract(prpLLoss.getOffPreAmt());
				}
				
				//汇总险别赔付金额
				if (kindRealpayMap.containsKey(prpLLoss.getKindCode())) {
					kindRealpayMap.put(prpLLoss.getKindCode(),kindRealpayMap.get(prpLLoss.getKindCode())+DataUtils.NullToZero(lossItemAmt).doubleValue());
				} else {
					kindRealpayMap.put(prpLLoss.getKindCode(),
							DataUtils.NullToZero(lossItemAmt).doubleValue());
				}
				sumRealPay = sumRealPay+DataUtils.NullToZero(lossItemAmt).doubleValue();
			}
		}

		// 人伤损失表信息
		List<PrpLLossPersonVo> prpLLossPersonVo = prpLCompensate.getPrpLLossPersons();
		if(prpLLossPersonVo!=null&& !prpLLossPersonVo.isEmpty()){
			for(PrpLLossPersonVo prpLLossPerson:prpLLossPersonVo){
				
				BigDecimal personLossAmt = prpLLossPerson.getSumRealPay();
				
				if(prpLLossPerson.getOffPreAmt() != null){
					personLossAmt = personLossAmt.subtract(prpLLossPerson.getOffPreAmt());
				}
				//汇总险别赔付金额
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
	 * 从结案赔款支付信息中剔除税后的实赔金额
	 * @param kindCode 险别
	 * @param kindItemRealPay 险别标的实赔金额(含税金额)
	 * @param kindRealpayMap 险别赔款Map
	 * @param prpLCompensate 
	 * @return
	 */
	private double splitTaxFromPayment(String kindCode,Double kindItemRealPay,Double offPreAmt,Map<String,Double> kindRealpayMap,Map<String, BigDecimal> kindPayRate,PrpLCompensateVo prpLCompensate){
		if (kindItemRealPay == 0.00) {
			return kindItemRealPay;
		} else {

			// step2:计算送再保险别下应扣除收款信息为修理厂所占赔款的税
			
			//扣除预付的金额
			double splitOffPreRealPay = kindItemRealPay - offPreAmt;
			
			// 险别标的实赔金额占险别赔付金额的比例
			BigDecimal itemKindRate = new BigDecimal(1);
			
			if(kindRealpayMap.get(kindCode) != 0.00){
				itemKindRate= new BigDecimal(splitOffPreRealPay / kindRealpayMap.get(kindCode));
			}
			// 收款信息集合，需将其中支付对象为修理厂并且发票类型为专票的按险别赔款比例拆分
			List<PrpLPaymentVo> payments = prpLCompensate.getPrpLPayments();
			
			
			// 计算修理厂赔款中分摊到送再保险别标的税
			BigDecimal splitSumTax = new BigDecimal(0.00);

			
			//从结案赔付中扣税
			if(payments !=null && payments.size()>0){
				for (PrpLPaymentVo prpLPayment : payments) {
	
					//发票类型为专票才参与扣减税额的计算
					if ((YN01.Y).equals(prpLPayment.getVatInvoiceFlag())) {
						for (String key : kindPayRate.keySet()) {
	
							if (kindCode.equals(key)) {
								// 收款信息险别拆分金额
								BigDecimal payeeKindrealPay = new BigDecimal(prpLPayment.getSumRealPay().multiply(kindPayRate.get(key)).doubleValue());
								// 收款人税率
								BigDecimal payeeAddTaxRate = (new BigDecimal(prpLPayment.getVatTaxRate())).divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);
								// 计算分摊到收款信息险别上的税
								splitSumTax = splitSumTax.add(payeeKindrealPay.subtract(payeeKindrealPay.divide((new BigDecimal(1)).add(payeeAddTaxRate),8, BigDecimal.ROUND_HALF_UP)));
							}
						}
					}
				}
			}
			
			
			
			// 按险别标的实赔金额占险别赔付金额的比例计算需要分摊的税
			splitSumTax = splitSumTax.multiply(itemKindRate);

			// step3:计算送再保险别对应标的实赔金额剔除分摊税额后的金额
			kindItemRealPay = kindItemRealPay - splitSumTax.doubleValue();

			return kindItemRealPay;
		}
	}
	
	/**
	 * 计算预付需要扣除的税
	 * @param registNo
	 * @param riskCode
	 * @param lossId
	 * @return
	 */
	private double addTaxFromPrepay(String registNo,String riskCode,String lossId){
		//获取预付信息
		double addTax = 0.00;
		BigDecimal tax = new BigDecimal(0.00);
		List<PrpLPrePayVo> prePayVos = compensateService.queryCarLossPrePay(registNo, riskCode, lossId);
		if(prePayVos != null && !prePayVos.isEmpty()){
			for(PrpLPrePayVo prepay:prePayVos){
				if(YN01.Y.equals(prepay.getVatInvoiceFlag())){
					// 收款人税率
					BigDecimal payeeAddTaxRate = (new BigDecimal(prepay.getVatTaxRate())).divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
					
					tax =tax.add(prepay.getPayAmt().subtract(prepay.getPayAmt().divide((new BigDecimal(1)).add(payeeAddTaxRate),8, BigDecimal.ROUND_HALF_UP)));
				}
			}
			addTax=  tax.doubleValue();
		}
		
		
		return addTax;
	}
	
	/**
	 * 记录赔款，费用价税拆分轨迹
	 * @param kindRealpayMap 险别实赔金额map
	 * @param kindPayRate 各险别占总赔款比例
	 * @param prpLCompensate 
	 */
	private void recordPaymentAndFeeSplitTaxTrace(Map<String, BigDecimal> kindPayRate,PrpLCompensateVo prpLCompensate){
		// 需要查询表中是否已存在记录，如果存在则不插入
		List<PrplPayeeKindPaymentVo> PayeeKindPaymentList =  verifyClaimService.findPayeeKindPaymentsByTimes(prpLCompensate.getRegistNo(), prpLCompensate.getRiskCode(), 0);
		
		if(PayeeKindPaymentList != null && !PayeeKindPaymentList.isEmpty()){
			logger.info("已存在核赔时记录的赔款，费用价税拆分轨迹，报案号{}，险种{}",prpLCompensate.getRegistNo(),prpLCompensate.getRiskCode());
		}else{
		
			List<PrplPayeeKindPayment> payeeKindPayments = new ArrayList<PrplPayeeKindPayment>();
			
			// 收款信息集合，需将其中支付对象为修理厂并且发票类型为专票的按险别赔款比例拆分
		    List<PrpLPaymentVo> payments = prpLCompensate.getPrpLPayments();
		    
		    //记录预赔的金额
		    List<PrpLPrePayVo> prplPrePays = compensateService.queryLossPrePay(prpLCompensate.getRegistNo(), prpLCompensate.getRiskCode());
		    
		    if(prplPrePays != null && prplPrePays.size()>0){
		    	for(PrpLPrePayVo prePay:prplPrePays){
		    		if((YN01.Y).equals(prePay.getVatInvoiceFlag())){
		    			PrplPayeeKindPayment payeeKindPayment = new PrplPayeeKindPayment();
						payeeKindPayment.setCompensateNo(prePay.getCompensateNo());
						// 处理lossId,从lossType中截取
						String lossType = prePay.getLossType();
						if (StringUtils.isNotBlank(lossType)) {
							int i = lossType.lastIndexOf("_");
							if (i != -1) {
								String lossId = lossType.substring(i + 1);
								try {
									payeeKindPayment.setLossId(Long.parseLong(lossId));
								} catch (NumberFormatException e) {
									logger.error("PrpLPrePay lossType 截取到的lossId非数字类型", e);
								}
							}

						}
						payeeKindPayment.setPayeeId(prePay.getPayeeId());
						payeeKindPayment.setRegistNo(prpLCompensate.getRegistNo());
						payeeKindPayment.setRiskCode(prpLCompensate.getRiskCode());
						payeeKindPayment.setTimes(0L);//核赔通过送再保记录的次数为0， 如果预赔的记录在核赔之前回写，预赔记录的times从1开始
						payeeKindPayment.setKindCode(prePay.getKindCode());
						payeeKindPayment.setKindSumPay(prePay.getPayAmt());
						
						// 收款人税率
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
		
					//发票类型为专票才参与拆分计算
					if ((YN01.Y).equals(prpLPayment.getVatInvoiceFlag())) {
						for (String key : kindPayRate.keySet()) {
		
							if (CodeConstants.ALLKINDSUMREALPAY.equals(key)) {
								continue;
							}else{
								// 收款信息险别拆分金额
								BigDecimal payeeKindrealPay = new BigDecimal(prpLPayment.getSumRealPay().multiply(kindPayRate.get(key)).doubleValue());
								// 收款人税率
								BigDecimal payeeAddTaxRate = (new BigDecimal(prpLPayment.getVatTaxRate())).divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
								// 计算分摊到收款信息险别上的税
								BigDecimal splitSumTax = new BigDecimal(0.00);
								splitSumTax= splitSumTax.add(payeeKindrealPay.subtract(payeeKindrealPay.divide((new BigDecimal(1)).add(payeeAddTaxRate),8, BigDecimal.ROUND_HALF_UP)));
								
								PrplPayeeKindPayment payeeKindPayment = new PrplPayeeKindPayment();
								payeeKindPayment.setCompensateNo(prpLCompensate.getCompensateNo());
								payeeKindPayment.setPayeeId(prpLPayment.getPayeeId());
								payeeKindPayment.setRegistNo(prpLCompensate.getRegistNo());
								payeeKindPayment.setRiskCode(prpLCompensate.getRiskCode());
								payeeKindPayment.setTimes(0L);//核赔通过送再保记录的次数为0， 如果预赔的记录在核赔之前回写，预赔记录的times从1开始
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
			
			//费用集合，仅对专票的费用记录轨迹
			List<PrpLChargeVo> prpLCharges = prpLCompensate.getPrpLCharges();
			if(prpLCharges!=null && !prpLCharges.isEmpty()){
				for(PrpLChargeVo prpLCharge:prpLCharges){
					if((YN01.Y).equals(prpLCharge.getVatInvoiceFlag())){
						// 收款人税率
						BigDecimal payeeAddTaxRate = (new BigDecimal(prpLCharge.getVatTaxRate())).divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
						//税额
						BigDecimal splitSumTax = new BigDecimal(0.00);
						splitSumTax= splitSumTax.add((prpLCharge.getFeeAmt().subtract(prpLCharge.getOffAmt())).subtract((prpLCharge.getFeeAmt().subtract(prpLCharge.getOffAmt())).divide((new BigDecimal(1)).add(payeeAddTaxRate),8, BigDecimal.ROUND_HALF_UP)));
						
						PrplPayeeKindPayment payeeKindPayment = new PrplPayeeKindPayment();
						payeeKindPayment.setCompensateNo(prpLCompensate.getCompensateNo());
						payeeKindPayment.setPayeeId(prpLCharge.getPayeeId());
						payeeKindPayment.setRegistNo(prpLCompensate.getRegistNo());
						payeeKindPayment.setRiskCode(prpLCompensate.getRiskCode());
						payeeKindPayment.setChargeCode(prpLCharge.getChargeCode());
						payeeKindPayment.setTimes(0L);//核赔通过送再保记录的次数为0， 如果预赔的记录在核赔之前回写，预赔记录的times从1开始
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
			
			// 存储拆分的轨迹
			if (!payeeKindPayments.isEmpty()) {
				for (PrplPayeeKindPayment prplPayeeKindPayment : payeeKindPayments) {
					databaseDao.save(PrplPayeeKindPayment.class,prplPayeeKindPayment);
				}
			}
		}
		
	}
	
	/**
	 * 记录送再保的信息
	 * @param registNo 报案号
	 * @param riskCode 险种
	 * @param times 送再保次数
	 * @param reinsTraces 需要插入的记录
	 */
	private void recordReinsTraces(String registNo, String riskCode, int times,List<PrplReinsTrace> reinsTraces) {

		if (!reinsTraces.isEmpty()) {
			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addEqual("registNo", registNo);
			queryRule.addEqual("riskCode", riskCode);
			queryRule.addEqual("times", new Long((long)times));

			//相同次数的送再保记录只保留一份有效记录
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

		// 结案0 注销1 重开3 拒赔2
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
			System.out.println("没有公估费！");
			return;
		}
		ins.sino.claimcar.reinsurance.vo.CompensateVo compensateVo = new ins.sino.claimcar.reinsurance.vo.CompensateVo();
		// 立案主表
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
			PrpLclaimDto.setDamageKind(claimVo.getRiskCode()); // 和对方确认 这边无法取值 ** 出险险别 DamageKind 就riskcode
			PrpLclaimDto.setSumClaim(DataUtils.NullToZero(claimVo.getSumClaim()).doubleValue());

			SysCodeDictVo sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode",claimVo.getDamageCode());
			if(sys == null){
				sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode2",claimVo.getDamageCode());
			}
			PrpLclaimDto.setCodeCName(sys.getCodeName());
			PrpLclaimDto.setDamageAreaPostCode(claimVo.getDamageAreaCode()); // 出险地邮编 需要商量
			PrpLclaimDto.setEndCaseFlag("1");
			PrpLclaimDto.setDangerNo(1);

			// 理算主表信息
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



			// 公估费 assessorFeeVo
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
			PrpLChargeVo.setChargeName("公估费");
			PrpLChargeVo.setCurrency("CNY");
			PrpLChargeVo.setChargeAmount(DataUtils.NullToZero(assessorFeeVo.getAmount()).doubleValue());   // 预付---送再保   改2016-12-19 减去 扣减金额
			PrpLChargeVo.setSumRealPay(0); // //传固定值 商量 ***有实赔金额就传实赔金额没有的话 你们穿0
			PrpLChargeVo.setDangerNo(1);
			PrpLChargeVo.setExceptDeductiblePay(0); // /
			prpLChargeList.add(PrpLChargeVo);


			if(claimVo.getCaseNo()==null){
				compensateVo.setBusinessType("0"); // 这边没有对应字段 如何传值？？？ 结案标志
			}else{
				compensateVo.setBusinessType("1");
			}
			compensateVo.setPrpLClaimVo(PrpLclaimDto);
			compensateVo.setPrpLCompensateVo(prpLcompensateDto);
			compensateVo.setPrpLChargeList(prpLChargeList);

			ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // 填写日志表
			claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
			claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
			claimInterfaceLogVo.setCompensateNo(assessorFeeVo.getCompensateNo());
			claimInterfaceLogVo.setComCode(claimVo.getComCode());
			claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
			claimInterfaceLogVo.setCreateTime(new Date());
			claimInterfaceLogVo.setOperateNode("公估费审核");
			claimInterfaceLogVo.setBusinessType(BusinessType.Reinsurance_assessor.name());
			claimInterfaceLogVo.setBusinessName(BusinessType.Reinsurance_assessor.getName());
			callReinsWebService.callCompensateForClient(compensateVo,claimInterfaceLogVo);
		}else{
			logger.info("传入参数不能为空！！！");
		}
	}
	public void checkFeeToReins(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate,PrpLCheckFeeVo checkFeeVo) throws Exception{
		if(checkFeeVo == null || BigDecimal.ZERO.equals(checkFeeVo.getAmount())){
			System.out.println("没有查勘费！");
			return;
		}
		ins.sino.claimcar.reinsurance.vo.CompensateVo compensateVo = new ins.sino.claimcar.reinsurance.vo.CompensateVo();
		// 立案主表
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
			PrpLclaimDto.setDamageKind(claimVo.getRiskCode()); // 和对方确认 这边无法取值 ** 出险险别 DamageKind 就riskcode
			PrpLclaimDto.setSumClaim(DataUtils.NullToZero(claimVo.getSumClaim()).doubleValue());

			SysCodeDictVo sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode",claimVo.getDamageCode());
			if(sys == null){
				sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode2",claimVo.getDamageCode());
			}
			PrpLclaimDto.setCodeCName(sys.getCodeName());
			PrpLclaimDto.setDamageAreaPostCode(claimVo.getDamageAreaCode()); // 出险地邮编 需要商量
			PrpLclaimDto.setEndCaseFlag("1");
			PrpLclaimDto.setDangerNo(1);

			// 理算主表信息
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



			// 查勘费 checkFeeVo
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
			PrpLChargeVo.setChargeName("查勘费");
			PrpLChargeVo.setCurrency("CNY");
			PrpLChargeVo.setChargeAmount(DataUtils.NullToZero(checkFeeVo.getAmount()).doubleValue());   // 预付---送再保   改2016-12-19 减去 扣减金额
			PrpLChargeVo.setSumRealPay(0); // //传固定值 商量 ***有实赔金额就传实赔金额没有的话 你们穿0
			PrpLChargeVo.setDangerNo(1);
			PrpLChargeVo.setExceptDeductiblePay(0); // /
			prpLChargeList.add(PrpLChargeVo);


			if(claimVo.getCaseNo()==null){
				compensateVo.setBusinessType("0"); // 这边没有对应字段 如何传值？？？ 结案标志
			}else{
				compensateVo.setBusinessType("1");
			}
			compensateVo.setPrpLClaimVo(PrpLclaimDto);
			compensateVo.setPrpLCompensateVo(prpLcompensateDto);
			compensateVo.setPrpLChargeList(prpLChargeList);

			ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // 填写日志表
			claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
			claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
			claimInterfaceLogVo.setCompensateNo(checkFeeVo.getCompensateNo());
			claimInterfaceLogVo.setComCode(claimVo.getComCode());
			claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
			claimInterfaceLogVo.setCreateTime(new Date());
			claimInterfaceLogVo.setOperateNode("查勘费审核");
			claimInterfaceLogVo.setBusinessType(BusinessType.Reinsurance_checkFee.name());
			claimInterfaceLogVo.setBusinessName(BusinessType.Reinsurance_checkFee.getName());
			callReinsWebService.callCompensateForClient(compensateVo,claimInterfaceLogVo);
		}else{
			logger.info("传入参数不能为空！！！");
		}
	}
	
	@Override
	public void asseFeeToReinsOut(PrpLClaimVo claimVo,
			PrpLCompensateVo prpLCompensate, PrpLAssessorFeeVo assessorFeeVo)
			throws Exception {
		
		ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // 填写日志表
		claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
		claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
		claimInterfaceLogVo.setComCode(claimVo.getComCode());
		claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
		claimInterfaceLogVo.setCreateTime(new Date());
		claimInterfaceLogVo.setOperateNode("公估费审核");
		// 重开
		TransDataForReinsCaseVo("3",claimVo, claimInterfaceLogVo);
		// 公估费已决数据
		asseFeeToReins(claimVo,prpLCompensate, assessorFeeVo);
		// 重开
		TransDataForReinsCaseVo("3",claimVo, claimInterfaceLogVo);
	}

	@Override
	public void checkFeeToReinsOut(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate, PrpLCheckFeeVo checkFeeVo)throws Exception {
		ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // 填写日志表
		claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
		claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
		claimInterfaceLogVo.setComCode(claimVo.getComCode());
		claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
		claimInterfaceLogVo.setCreateTime(new Date());
		claimInterfaceLogVo.setOperateNode("查勘费审核");
		// 重开
		TransDataForReinsCaseVo("3",claimVo, claimInterfaceLogVo);
		// 查勘费已决数据
		checkFeeToReins(claimVo, prpLCompensate, checkFeeVo);
		// 重开
		TransDataForReinsCaseVo("3",claimVo, claimInterfaceLogVo);
	}
	
	/**
	 * vat回写送再保
	 * @param claimVo
	 * @param prpLCompensate
	 * @param curTime
	 * @param kindPrePayTaxDiffrentValueMap
	 * @param kindPaymentTaxDiffrentValueMap
	 * @param kindChargeTaxDiffrentValueMap
	 * @throws Exception
	 */
	public  void vatWriteBackTransDataForCompensateVo(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate,int curTime,Map<Long,BigDecimal> kindPrePayTaxDiffrentValueMap,Map<String,BigDecimal> kindPaymentTaxDiffrentValueMap,Map<String,BigDecimal> kindChargeTaxDiffrentValueMap) throws Exception{
		// vat回写送再保 ,将险别的差值按比例分摊
		ins.sino.claimcar.reinsurance.vo.CompensateVo compensateVo = new ins.sino.claimcar.reinsurance.vo.CompensateVo();
		// 立案主表
		if(claimVo!=null&&prpLCompensate!=null){
			int times = logService.getReinsuranceInterfaceRequestTimes(claimVo.getRegistNo(),prpLCompensate.getCompensateNo())+1;
			//记录送再保信息集合
			List<PrplReinsTrace> reinsTraceList = new ArrayList<PrplReinsTrace>();
			
			// step1:计算险别赔款比例和送再保险别标的实赔金额占险别赔付金额的比例
			//赔案下险别实赔金额汇总， key为kindCode
			Map<String,Double> kindRealpayMap = new HashMap<String,Double>();
			
			// 统计各险别的实赔金额
			calculateKindSumRealPay(kindRealpayMap,prpLCompensate);
			
			// 存放险别比例,key为kindCode
			Map<String, BigDecimal> kindPayRate = new HashMap<String, BigDecimal>();
			
			if(kindRealpayMap.get(CodeConstants.ALLKINDSUMREALPAY) != 0.00){
			// 计算各险别的险别赔款比例
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
			PrpLclaimDto.setDamageKind(claimVo.getRiskCode()); // 和对方确认 这边无法取值 ** 出险险别 DamageKind 就riskcode
			PrpLclaimDto.setSumClaim(DataUtils.NullToZero(claimVo.getSumClaim()).doubleValue());

			SysCodeDictVo sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode",claimVo.getDamageCode());
			if(sys == null){
				sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode2",claimVo.getDamageCode());
			}
			PrpLclaimDto.setCodeCName(sys.getCodeName());
			PrpLclaimDto.setDamageAreaPostCode(claimVo.getDamageAreaCode()); // 出险地邮编 需要商量
			PrpLclaimDto.setEndCaseFlag("0"); // 15869 车理赔已决数据送再保结案状态错误  2019-12-25 11:55:24
			PrpLclaimDto.setDangerNo(1);

			// 理算主表信息
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

			// 理算车辆损失财产表信息
			List<PrpLLossVo> prpLlossList = new ArrayList<PrpLLossVo>();
			List<PrpLLossItemVo> prpLLossItemVo = prpLCompensate.getPrpLLossItems();
			if(prpLLossItemVo!=null&&prpLLossItemVo.size()>0){ // 车辆损失
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
					// 险别标的赔付金额要扣除预付
					lossItem.setSumRealPay(sharePaymentTax(prpLLoss.getKindCode(),DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue(),DataUtils.NullToZero(prpLLoss.getOffPreAmt()).doubleValue(),kindRealpayMap,kindPaymentTaxDiffrentValueMap));
					
					lossItem.setExceptDeductiblePay(0);
					lossItem.setDangerNo(1);
					prpLlossList.add(lossItem);
					
					//记录送再保的车损信息
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
			if(prpLLossPropVo!=null&& !prpLLossPropVo.isEmpty()){ // 财产损失
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
					
					//记录送再保的财损信息
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

			// 人伤损失表信息
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
					
					//记录送再保的人伤信息
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

			// 费用信息表
			List<PrpLChargeList> prpLChargeList = new ArrayList<PrpLChargeList>();
			List<PrpLChargeVo> prpLChargeVo = prpLCompensate.getPrpLCharges();
			if(prpLChargeVo!=null&& !prpLChargeVo.isEmpty()){
				int serialNo = 1;
				for(PrpLChargeVo prpLCharge:prpLChargeVo){
					
					// 2151 需要判断费用类型，查勘费(系统无该费用类型)、公估费不需要送再保
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
							// 组装key
							StringBuffer keyBF = new StringBuffer(prpLCharge.getPayeeId().toString()).append("-").append(prpLCharge.getKindCode()).append("-").append(prpLCharge.getChargeCode());
							
							//根据税率计算出价，送给再保
							if(kindChargeTaxDiffrentValueMap.containsKey(keyBF.toString())){
							    PrpLChargeVo.setChargeAmount(kindChargeTaxDiffrentValueMap.get(keyBF.toString()).doubleValue());
							}else{
								PrpLChargeVo.setChargeAmount(0.00);
							}
						}else{
							PrpLChargeVo.setChargeAmount(0.00);   
						}
						PrpLChargeVo.setSumRealPay(0); // //传固定值 商量 ***有实赔金额就传实赔金额没有的话 你们穿0
						PrpLChargeVo.setDangerNo(1);
						PrpLChargeVo.setExceptDeductiblePay(0); // /
						prpLChargeList.add(PrpLChargeVo);
						
						//记录送再保的费用信息
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
				compensateVo.setBusinessType("0"); // 这边没有对应字段 如何传值？？？ 结案标志
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

			ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // 填写日志表
			claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
			claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
			claimInterfaceLogVo.setCompensateNo(prpLCompensate.getCompensateNo());
			claimInterfaceLogVo.setComCode(claimVo.getComCode());
			claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
			claimInterfaceLogVo.setCreateTime(new Date());
			claimInterfaceLogVo.setOperateNode(FlowNode.VClaim.getName());
			claimInterfaceLogVo.setBusinessType(BusinessType.Reinsurance_vatBack.name());
			claimInterfaceLogVo.setBusinessName(BusinessType.Reinsurance_vatBack.getName());
			
			//记录送再保的信息
			recordReinsTraces(claimVo.getRegistNo(),claimVo.getRiskCode(),times,reinsTraceList);
			
			callReinsWebService.callCompensateForClient(compensateVo,claimInterfaceLogVo);
			
			
		}else{
			logger.info("传入参数不能为空！！！");
		}
	}
	
	/**
	 * 分摊险别标的税额差值
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
			
			// 险别标的实赔金额占险别赔付金额的比例
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
	 * 送再保冲销数据接口
	 * <pre></pre>
	 * 
	 * @param claimVo
	 * @param prpLCompensate
	 * @throws Exception
	 *
	 */
	@Override
	public  void transDataForWashTransaction(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate) throws Exception{
		// 冲销送再保金额需根据送再保轨迹中的金额进行计算
        ins.sino.claimcar.reinsurance.vo.CompensateVo compensateVo = new ins.sino.claimcar.reinsurance.vo.CompensateVo();
		
		// 立案主表
		if(claimVo!=null&&prpLCompensate!=null){
			
			//送再保次数
			int times = logService.getReinsuranceInterfaceRequestTimes(claimVo.getRegistNo(),prpLCompensate.getCompensateNo());
			
			//记录送再保信息集合
			List<PrplReinsTrace> reinsTraceList = new ArrayList<PrplReinsTrace>();
			
			//判断是否是历史案件冲销
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
			PrpLclaimDto.setDamageKind(claimVo.getRiskCode()); // 和对方确认 这边无法取值 ** 出险险别 DamageKind 就riskcode
			PrpLclaimDto.setSumClaim(DataUtils.NullToZero(claimVo.getSumClaim()).doubleValue());

			SysCodeDictVo sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode",claimVo.getDamageCode());
			if(sys == null){
				sys = codeTranServiceSpringImpl.findTransCodeDictVo("DamageCode2",claimVo.getDamageCode());
			}
			PrpLclaimDto.setCodeCName(sys.getCodeName());
			PrpLclaimDto.setDamageAreaPostCode(claimVo.getDamageAreaCode()); // 出险地邮编 需要商量
			PrpLclaimDto.setEndCaseFlag("0"); // 15869 车理赔已决数据送再保结案状态错误  2019-12-25 11:55:24
			PrpLclaimDto.setDangerNo(1);

			// 理算主表信息
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

			// 理算车辆损失财产表信息
			List<PrpLLossVo> prpLlossList = new ArrayList<PrpLLossVo>();
			List<PrpLLossItemVo> prpLLossItemVo = prpLCompensate.getPrpLLossItems();
			if(prpLLossItemVo!=null&&prpLLossItemVo.size()>0){ // 车辆损失
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
					// 获取支付对象类型为修理厂的预付信息集合， 预付已经归属到了险别，根据lossType字段拆分出lossId可以明确到出险标的
					if(hisFlag){
						lossItem.setSumRealPay(DataUtils.NullToZero(prpLLoss.getSumRealPay()).doubleValue());
					}else{
						lossItem.setSumRealPay(getWashTransactionAmt(prpLCompensate.getRegistNo(),prpLCompensate.getRiskCode(),prpLLoss.getKindCode(),prpLLoss.getId().toString(),""));
					}
					lossItem.setExceptDeductiblePay(0);
					lossItem.setDangerNo(1);
					prpLlossList.add(lossItem);
					
					//记录送再保的车损信息
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
			if(prpLLossPropVo!=null&& !prpLLossPropVo.isEmpty()){ // 财产损失
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
					
					//记录送再保的财损信息
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

			// 人伤损失表信息
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
					
					//记录送再保的人伤信息
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

			// 费用信息表
			List<PrpLChargeList> prpLChargeList = new ArrayList<PrpLChargeList>();
			List<PrpLChargeVo> prpLChargeVo = prpLCompensate.getPrpLCharges();
			if(prpLChargeVo!=null&& !prpLChargeVo.isEmpty()){
				int serialNo = 1;
				for(PrpLChargeVo prpLCharge:prpLChargeVo){
					
					// 2151 需要判断费用类型，查勘费(系统无该费用类型)、公估费不需要送再保
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
						PrpLChargeVo.setSumRealPay(0); // //传固定值 商量 ***有实赔金额就传实赔金额没有的话 你们穿0
						PrpLChargeVo.setDangerNo(1);
						PrpLChargeVo.setExceptDeductiblePay(0); // /
						prpLChargeList.add(PrpLChargeVo);
						
						//记录送再保的费用信息
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
				compensateVo.setBusinessType("0"); // 这边没有对应字段 如何传值？？？ 结案标志
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

			ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // 填写日志表
			claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
			claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
			claimInterfaceLogVo.setCompensateNo(prpLCompensate.getCompensateNo());
			claimInterfaceLogVo.setComCode(claimVo.getComCode());
			claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
			claimInterfaceLogVo.setCreateTime(new Date());
			claimInterfaceLogVo.setOperateNode(FlowNode.VClaim.getName());
			claimInterfaceLogVo.setBusinessType(BusinessType.Reinsurance_washTransaction.name());
			claimInterfaceLogVo.setBusinessName(BusinessType.Reinsurance_washTransaction.getName());
			
			
			//记录送再保的信息
			recordReinsTraces(claimVo.getRegistNo(),claimVo.getRiskCode(),times,reinsTraceList);
			
			callReinsWebService.callCompensateForClient(compensateVo,claimInterfaceLogVo);
		}else{
			logger.info("传入参数不能为空！！！");
		}

	}
	
	/**
	 * 校验是否历史案件
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
			logger.error("判断是否历史案件冲销失败",e);
			
		}
		
		return true;
	}

	/**
	 * 获取冲销金额
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
			logger.error("获取冲销金额失败",e);
			washTransactionAmt = 0;
		}
		
		return washTransactionAmt;
	}
	
}
