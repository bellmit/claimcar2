package ins.sino.claimcar.middlestagequery.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.lang.Springs;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.utils.DataUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.LossParty;
import ins.sino.claimcar.carinterface.po.ClaimInterfaceLog;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.middlestagequery.service.ClaimToMiddleStageOfCaseService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;











import javax.ws.rs.Path;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * ???????????????????????????rabbitMq???????????????
 */
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" })
@Path(value = "claimToMiddleStageOfCaseService")
public class ClaimToMiddleStageOfCaseServiceImpl implements ClaimToMiddleStageOfCaseService{
	
	private static Logger logger = LoggerFactory.getLogger(ClaimToMiddleStageOfCaseServiceImpl.class);
    
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
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	CompensateTaskService compeTaskService;

	
	@Override
	public void middleStageQuery(String registNo, String caseStatus){
		this.init();
		SendMsgToMqService sendMsg = new SendMsgToMqService();
		JSONObject respJson = new JSONObject();
		String status = null ; //????????????????????????

		if(StringUtils.isNotBlank(registNo)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
			respJson.put("reportNo", registNo);
			
			if(registVo != null){
				//????????????????????????????????????????????????????????????
				List<PrpLCInsuredVo> insuredList = registQueryService.findPrpLCInsuredVoByPolicyNoAndFlag(registVo.getPolicyNo(), "1");
				if(insuredList != null && insuredList.size() > 0){
					if(StringUtils.isNotBlank(insuredList.get(0).getIdentifyType())){
						respJson.put("policyInsuredCertType", insuredList.get(0).getIdentifyType());
					}
					if(StringUtils.isNotBlank(insuredList.get(0).getIdentifyNumber())){
						respJson.put("policyInsuredCertNo", insuredList.get(0).getIdentifyNumber());
					}
					if(StringUtils.isNotBlank(insuredList.get(0).getInsuredName())){
						respJson.put("policyInsuredName", insuredList.get(0).getInsuredName().replace(" ", ""));
					}
				}
			
				List<PrpLCMainVo> cmainVo = prplCMainService.findPrpLCMainsByRegistNo(registNo);
				if(cmainVo != null && cmainVo.size() > 0){
					for(PrpLCMainVo vo : cmainVo){
						if("1101".equals(vo.getRiskCode())){ 
							respJson.put("vehiclePolicyNo", vo.getPolicyNo());  //???????????????
							respJson.put("productCode", vo.getRiskCode());		//????????????
							if(StringUtils.isNotBlank(vo.getRiskCode())){
								respJson.put("productName", codeTranService.findCodeName("CarRiskCode", vo.getRiskCode())); //????????????
							}
							respJson.put("policyType", "01");					//????????????
						}else{
							respJson.put("policyNo", vo.getPolicyNo());			//???????????????
							respJson.put("productCode", vo.getRiskCode());		//????????????
							if(StringUtils.isNotBlank(vo.getRiskCode())){
								respJson.put("productName", codeTranService.findCodeName("CarRiskCode", vo.getRiskCode())); //????????????
							}							
							respJson.put("policyType", "01");					//????????????
						}
					}
				}
				if(registVo != null){
					if(registVo.getDamageTime() != null){
						respJson.put("accidentDate", format.format(registVo.getDamageTime()));
					}
					if(StringUtils.isNotBlank(registVo.getDamageCode())){
						respJson.put("accidentType", codeTranService.findCodeName("DamageCode", registVo.getDamageCode()));
					}
					if(StringUtils.isNotBlank(registVo.getDamageAddress())){
						respJson.put("accidentAddr", registVo.getDamageAddress());
					}
					//????????????
					if(registVo.getReportTime() != null){
						respJson.put("reportDate", format.format(registVo.getReportTime()));
					}
					//????????????
					if(StringUtils.isNotBlank(registVo.getSelfClaimFlag()) && "1".equals(registVo.getSelfClaimFlag())){
						respJson.put("reportType", "1"); //??????????????????e??????
					}else if(StringUtils.isNotBlank(registVo.getIsQuickCase()) && "1".equals(registVo.getIsQuickCase())){
						respJson.put("reportType", "2"); //????????????
					}else{
						respJson.put("reportType", "3"); //????????????
					}
					
					if(StringUtils.isNotBlank(registVo.getReportorName())){
						respJson.put("reporter", registVo.getReportorName().replace(" ", ""));
					}
					if(StringUtils.isNotBlank(registVo.getLinkerName())){
						respJson.put("linkMan", registVo.getLinkerName().replace(" ", ""));
					}
					if(StringUtils.isNotBlank(registVo.getLinkerMobile())){
						respJson.put("contactNo", registVo.getLinkerMobile());
					}else{
						if(StringUtils.isNotBlank(registVo.getLinkerPhone())){
							respJson.put("contactNo", registVo.getLinkerPhone());
						}	
					}
					//????????????
					if(StringUtils.isNotBlank(registVo.getDriverName())){
						respJson.put("mainDriver", registVo.getDriverName().replace(" ", ""));
					}
				}
				//????????????
				PrpLRegistExtVo registExtVo = registQueryService.getPrpLRegistExtInfo(registNo);
				if(registExtVo != null){
					if(StringUtils.isNotBlank(registExtVo.getDangerRemark())){
						respJson.put("accidentDesc", registExtVo.getDangerRemark());
					}
				}
				
				//????????????????????????Regis????????????Check????????????Compe????????????EndCas?????????/?????????cance??????????????????????????????
				List<PrpLClaimVo> claimVoList = claimService.findClaimListByRegistNo(registNo);
				boolean canceFlag = false;
				if(claimVoList != null && claimVoList.size() > 0){
					for(PrpLClaimVo vo : claimVoList){
						//0:?????????2?????????
						if(StringUtils.isNotBlank(vo.getValidFlag()) && ("0".equals(vo.getValidFlag()) ||"2".equals(vo.getValidFlag()))){
							respJson.put("reportStatus", "90");
							canceFlag = true;
						}
					}
				}
				if(canceFlag){
					logger.info("???????????? " + registNo + " ?????????????????????");
				}else{
					if("Regis".equals(caseStatus)){
						respJson.put("reportStatus", "1");
					}else if("Check".equals(caseStatus)){
						respJson.put("reportStatus", "3");
					}else if("Compe".equals(caseStatus)){
						respJson.put("reportStatus", "5");
					}else if("EndCas".equals(caseStatus)){
						respJson.put("reportStatus", "7");
					}
				}
				
				//??????????????????????????????????????? ????????????????????????Vin???
				List<PrpLCItemCarVo> itemCarVo = policyViewService.findPrpcItemcarByRegistNo(registNo);
				if(itemCarVo != null && itemCarVo.size() > 0){
					if(StringUtils.isNotBlank(itemCarVo.get(0).getLicenseNo())){
						respJson.put("carNo", itemCarVo.get(0).getLicenseNo());
					}
					if(StringUtils.isNotBlank(itemCarVo.get(0).getFrameNo())){
						respJson.put("vehicleNo", itemCarVo.get(0).getFrameNo());
					}
					if(StringUtils.isNotBlank(itemCarVo.get(0).getVinNo())){
						respJson.put("vinCode", itemCarVo.get(0).getVinNo());
					}else{
						if(StringUtils.isNotBlank(itemCarVo.get(0).getFrameNo())){
							respJson.put("vinCode", itemCarVo.get(0).getFrameNo());
						}
					}
					
				}
				//????????????
				BigDecimal sumRescueFee = BigDecimal.ZERO;
				//????????????????????????????????????
				BigDecimal claimAmount = BigDecimal.ZERO;
				
				DecimalFormat df = new DecimalFormat("#.00");
				//???
				
				List<PrpLCompensateVo> voList  = compeTaskService.queryCompensate(registNo, "N");
				
				if(voList != null && voList.size() > 0) {
					for(PrpLCompensateVo vo : voList) {
						if(StringUtils.isNotBlank(vo.getUnderwriteFlag()) && "1".equals(vo.getUnderwriteFlag())) {
							claimAmount = claimAmount.add(DataUtils.NullToZero(vo.getSumPaidAmt()));
							
							//?????????
							if(vo.getPrpLLossItems() != null && vo.getPrpLLossItems().size() > 0) {
								for(PrpLLossItemVo itemVo : vo.getPrpLLossItems()) {
									sumRescueFee = sumRescueFee.add(DataUtils.NullToZero(itemVo.getRescueFee()));
								}
							}
							if(vo.getPrpLLossProps() != null && vo.getPrpLLossProps().size() > 0) {
								for(PrpLLossPropVo propVo : vo.getPrpLLossProps()) {
									sumRescueFee = sumRescueFee.add(DataUtils.NullToZero(propVo.getRescueFee()));
								}
							}
						}
						
					}
				}

				//???????????????
				respJson.put("paidTotalAmount", df.format(DataUtils.NullToZero(claimAmount)));
				//????????????
				respJson.put("rescueTotalAmount", df.format(DataUtils.NullToZero(sumRescueFee)));
				//??????????????????
				List<PrpLWfTaskVo> wftaskoutLists = wfTaskHandleService.findPrpLWfTaskOutTimeDescByRegistNo(registNo);
				if(wftaskoutLists != null && wftaskoutLists.size() > 0){
					if( wftaskoutLists.get(0).getTaskOutTime() != null){
						String time = format2.format(wftaskoutLists.get(0).getTaskOutTime());
						respJson.put("gmtModified", time);
					}
				}
				
				logger.info("??????rabbitMq???????????????====> " + respJson.toString());
				
				try {
					sendMsg.send(respJson.toString(), registNo);
					status = "1";
				} catch (Exception e) {
					status = "0";
					e.printStackTrace();
				}
				
			}else{
				respJson.put("Status", "500");
				respJson.put("Message", "?????????????????????????????????????????????????????????????????????");
				logger.info("??????rabbitMq???????????????====> " + respJson.toString());
				try {
					sendMsg.send(respJson.toString(), registNo);
					status = "1";
				} catch (Exception e) {
					status = "0";
					e.printStackTrace();
				}
			}
		}else{
			respJson.put("Status", "500");
			respJson.put("Message", "??????????????????");
			logger.info("??????rabbitMq???????????????====> " + respJson.toString());
			try {
				sendMsg.send(respJson.toString(), registNo);
				status = "1";
			} catch (Exception e) {
				status = "0";
				e.printStackTrace();
			}
			
		}
		
		//????????????????????????
		this.saveLog(registNo, caseStatus, status, respJson.toString());
	}
	
	private void saveLog(String registNo, String caseStatus, String status, String message){
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		
		logVo.setRegistNo(registNo);
		if(status == null){
			status = "0";
		}
		logVo.setStatus(status);
		logVo.setOperateNode(caseStatus);
		logVo.setBusinessType("MiddleStageMessage");
		logVo.setBusinessName("?????????????????????");
		logVo.setRequestTime(new Date());
		String usercode = ServiceUserUtils.getUserCode();
		if(StringUtils.isNotBlank(usercode)){
			logVo.setCreateUser(usercode);
		}else{
			logVo.setCreateUser("0000000000");
		}
		logVo.setRequestXml(message);
		
		//????????????
		ClaimInterfaceLogVo log =  this.commitSave(logVo);
		logger.info(log.toString());
	}
	
	public ClaimInterfaceLogVo commitSave(ClaimInterfaceLogVo logVo) {
		logVo.setCreateTime(new Date());
		ClaimInterfaceLog platLogPo = new ClaimInterfaceLog();
		Beans.copy().from(logVo).to(platLogPo);
        databaseDao.save(ClaimInterfaceLog.class,platLogPo);
		logVo.setId(platLogPo.getId());
		return logVo;
	}
	
	//?????????
	private void init(){
		if( registService == null){
			registService = (RegistService)Springs.getBean(RegistService.class);
		}
		if( prplCMainService == null){
			prplCMainService = (PrpLCMainService)Springs.getBean(PrpLCMainService.class);
		}
		if( codeTranService == null){
			codeTranService = (CodeTranService)Springs.getBean(CodeTranService.class);
		}
		if( registQueryService == null){
			registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
		}
		if( policyViewService == null){
			policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
		}
		if( claimService == null){
			claimService = (ClaimService)Springs.getBean(ClaimService.class);
		}
		if( lossCarService == null){
			lossCarService = (LossCarService)Springs.getBean(LossCarService.class);
		}
		if( propTaskService == null){
			propTaskService = (PropTaskService)Springs.getBean(PropTaskService.class);
		}
		if( persTraceService == null){
			persTraceService = (PersTraceService)Springs.getBean(PersTraceService.class);
		}
		if( wfTaskHandleService == null){
			wfTaskHandleService = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
		}
	}
}


