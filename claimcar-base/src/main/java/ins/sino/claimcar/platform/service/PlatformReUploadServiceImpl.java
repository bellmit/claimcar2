package ins.sino.claimcar.platform.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaFactorPowerVo;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SqlParamVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.base.po.PrpLClaim;
import ins.sino.claimcar.carinterface.po.ClaimInterfaceLog;
import ins.sino.claimcar.carinterface.service.CaseLeapHNService;
import ins.sino.claimcar.carinterface.service.CaseLeapService;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carplatform.po.CiClaimPlatformLog;
import ins.sino.claimcar.carplatform.po.CiClaimPlatformTask;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformTaskService;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.endcase.service.EndCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.genilex.service.ClaimToGenilexService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.payment.service.ClaimToPaymentService;
import ins.sino.claimcar.trafficplatform.service.RegistToCarRiskPaltformService;
import ins.sino.claimcar.trafficplatform.service.SzpoliceClaimInfoService;
import ins.sino.claimcar.trafficplatform.service.SzpoliceRegistService;
import ins.sino.claimcar.trafficplatform.vo.CarRiskRegistBasePartReqVo;
import ins.sino.claimcar.trafficplatform.vo.CarRiskRegistReqVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path("platformReuploadService")
public class PlatformReUploadServiceImpl implements PlatformReUploadService {

	private Logger logger = LoggerFactory.getLogger(PlatformReUploadServiceImpl.class);

	@Autowired
	DatabaseDao databaseDao; 

	@Autowired
	CiClaimPlatformLogService platformLogService;
	
	@Autowired
	CiClaimPlatformTaskService ciClaimPlatformTaskService;

	@Autowired
	CheckToPlatformService checkToPaltformService;

	@Autowired
	RegistToPaltformService registToPaltformService;

	@Autowired
	ClaimToPaltformService claimToPaltformService;

	@Autowired
	CertifyToPaltformService certifyToPaltformService;

	@Autowired
	LossToPlatformService lossToPlatformService;

	@Autowired
	private CompensateTaskService compensateTaskService;

	@Autowired
	ClaimTaskService claimTaskService;

	@Autowired
	CaseLeapService caseLeapService;

	@Autowired
	CaseLeapHNService caseLeapHNService;

	@Autowired
	EndCaseService endCaseService;

	@Autowired
	ClaimToPaymentService claimToPaymentService;

	@Autowired
	AssessorService assessorService;

	@Autowired
	ClaimInterfaceLogService ClaimInterfaceLogService;

	@Autowired
	RegistToCarRiskPaltformService registToCarRiskPaltformService;
	@Autowired
	ClaimInterfaceLogService claimInterfaceLogService;
	@Autowired
	SzpoliceClaimInfoService szpoliceClaimInfoService;
	@Autowired
	SzpoliceRegistService szpoliceRegistService;
	
	@Autowired
	ClaimToGenilexService claimToGenilexService;

	@Autowired
	private SaaUserPowerService saaUserPowerService;
	
	/**
	 * ??????????????????
	 * @param logId
	 * @throws Exception
	 */
	@Override
	public void platformReUpload(Long logId) throws Exception {
		CiClaimPlatformLogVo logVo = platformLogService.findLogById(logId);
		String comCode = logVo.getComCode();
		String reqType = logVo.getRequestType();

		String nodeName = "";
		if(comCode.startsWith("22")){// 22--????????????
			nodeName = CodeConstants.REQUESTTYPE_SH.get(reqType);
		}else{
			nodeName = CodeConstants.REQUESTTYPE_BASE.get(reqType);
		}

		if(StringUtils.isEmpty(nodeName)){
			throw new IllegalArgumentException("??????????????????????????????"); 
		}else{
			nodeName = nodeName.split("_")[0];
		}

		// return reuploadToAll(nodeName.split("_")[0],logVo);

		if(comCode.startsWith("22")){// ????????????
			reuploadToSH(nodeName, logVo);
		}else{// ????????????
			reuploadToAll(nodeName, logVo);
		}
	}

	/**
	 * ??????????????????
	 * @param nodeName
	 * @param CiClaimPlatformLogVo
	 * @return
	 * @throws Exception
	 */

	private void reuploadToAll(String nodeName, CiClaimPlatformLogVo logVo) throws Exception{
		if("Regis".equals(nodeName)){// ????????????
			registToPaltformService.uploadRegistToPaltform(logVo);
		}else if("Check".equals(nodeName)){// ????????????
			checkToPaltformService.sendToPaltform(logVo);
		}else if("Claim".equals(nodeName)){// ??????
			claimToPaltformService.sendClaimToPaltform(logVo);
		}else if("Cancel".equals(nodeName)){// ????????????
			claimToPaltformService.sendCancelToPaltform(logVo);
		}else if("VClaim".equals(nodeName)){// ??????
			claimToPaltformService.sendVClaimToPaltform(logVo);
		}else if("EndCase".equals(nodeName)){// ??????
			claimToPaltformService.sendEndCaseToPaltform(logVo);
		}else if("Payment".equals(nodeName)){// ????????????
			claimToPaltformService.sendPaymentToPaltform(logVo);
		}else if("Loss".equals(nodeName)){// ???????????????
			lossToPlatformService.uploadLossToPaltform(logVo);
		}else if("Certify".equals(nodeName)){// ????????????
			certifyToPaltformService.uploadCertifyToPaltform(logVo);
		}else if("ReOpen".equals(nodeName)){// ??????????????????
			claimToPaltformService.sendReOpenAppToPaltform(logVo);
		}
	}



	
	/**
	 * ??????????????????
	 * @param nodeName
	 * @param CiClaimPlatformLogVo
	 * @return
	 * @throws Exception
	 */

	private void reuploadToSH(String nodeName, CiClaimPlatformLogVo logVo) throws Exception{
		if("Regis".equals(nodeName)){// ????????????
			registToPaltformService.uploadRegistToPaltform(logVo);
		}else if("Loss".equals(nodeName)){// ???????????????
			lossToPlatformService.uploadLossToPaltform(logVo);
		}else if("Claim".equals(nodeName)){// ??????
			claimToPaltformService.sendClaimToPaltform(logVo);
		}else if("Cancel".equals(nodeName)){// ????????????
			claimToPaltformService.sendCancelToPaltform(logVo);
		}else if("VClaim".equals(nodeName)){// ??????
			claimToPaltformService.sendVClaimToPaltform(logVo);
		}else if("EndCase".equals(nodeName)){// ??????
			claimToPaltformService.sendEndCaseToPaltform(logVo);
		}else if("Payment".equals(nodeName)){// ????????????
			claimToPaltformService.sendPaymentToPaltform(logVo);
		}else if("Certify".equals(nodeName)){// ????????????
			certifyToPaltformService.uploadCertifyToPaltform(logVo);
		}else if("EndCase".equals(nodeName)){// ????????????
			claimToPaltformService.sendEndCaseToPaltform(logVo);
		}else if("EndCaseAdd".equals(nodeName)){// ??????????????????
			claimToPaltformService.sendEndCaseAddToPaltform(logVo);
		}
	}



	/**
	 * ??????????????????
	 * @param CiClaimPlatformLogVo
	 * @param start,length
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultPage<CiClaimPlatformLogVo> findPaltformInfoForPage(CiClaimPlatformLogVo queryVo,Integer start,Integer length) throws Exception {
		List<CiClaimPlatformLogVo> logVoList = new ArrayList<CiClaimPlatformLogVo>();
		long total = 0; 
		//?????????????????????????????????????????????????????????????????????????????????
		if(StringUtils.isNotBlank(queryVo.getBussNo()) || StringUtils.isNotBlank(queryVo.getClaimSeqNo())){
			// ????????????list???ps?????????????????????????????????object??????
			SqlJoinUtils sqlUtil = new SqlJoinUtils();
			sqlUtil.append(" FROM CiClaimPlatformLog cpl where 1=1");
			// hql????????????
			// ????????????
			if(StringUtils.isNotBlank(queryVo.getClaimSeqNo())){
				sqlUtil.append(" AND cpl.claimSeqNo = ? ");
				sqlUtil.addParamValue(queryVo.getClaimSeqNo().trim());
			}
			// ?????????
			if(StringUtils.isNotBlank(queryVo.getBussNo())){
				String bussNo = queryVo.getBussNo().replaceAll("\\s*", ""); 
				sqlUtil.append("AND (cpl.bussNo = ? ");
				sqlUtil.addParamValue(bussNo);
				//???????????????????????????
				if ((bussNo.length() == 21 || bussNo.length() == 22) && bussNo.startsWith("4")) {
					List<PrpLClaimVo> claimVos = claimTaskService.findClaimListByRegistNo(bussNo);
					if(claimVos != null && claimVos.size()>0){
						sqlUtil.append(" or  cpl.bussNo in (");
						for(int i = 0;i<claimVos.size();i++){
							if(StringUtils.isNotBlank(claimVos.get(i).getClaimNo())){
								sqlUtil.append("?");
								sqlUtil.addParamValue(claimVos.get(i).getClaimNo());
							}
							if (i < claimVos.size() -1) {
								sqlUtil.append(",");
							}
						}
						sqlUtil.append(")");
					}
				} 
				sqlUtil.append(")");
			}
			// ????????????
			if(StringUtils.isNotBlank(queryVo.getRequestType())){
				sqlUtil.andEquals(queryVo,"cpl","requestType");
			}
			// ??????
			if(StringUtils.isNotBlank(queryVo.getComCode())){
				sqlUtil.andEquals(queryVo,"cpl","comCode");
			}
			// ??????
			if(StringUtils.isNotBlank(queryVo.getStatus())){
				sqlUtil.andEquals(queryVo,"cpl","status");
			}
			//?????????????????????????????????????????????????????????????????? 
			if (StringUtils.isBlank(queryVo.getClaimSeqNo()) && StringUtils.isBlank(queryVo.getBussNo())) {
				// ????????????
				sqlUtil.andDate(queryVo,"cpl","requestTime");
			}
			//29962 ???????????????????????? ?????????????????? ????????????????????????????????????????????????????????????????????????????????????????????????
			String userCode = queryVo.getUserCode();
			SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(userCode);
			Map<String, List<SaaFactorPowerVo>> factorPowerMap = userPowerVo.getPermitFactorMap();
			if (!("0000000000").equals(userCode)) {
				if (null != factorPowerMap && !factorPowerMap.isEmpty()) {
					List<SaaFactorPowerVo> factorPowerVoList = factorPowerMap.get("FF_COMCODE");
					if(factorPowerVoList == null || factorPowerVoList.size() == 0){
						return new ResultPage<CiClaimPlatformLogVo>(start,length,total,logVoList);
					}else{
						logger.info("????????????????????????????????????==========");
						boolean isMainCom = false;
						for(SaaFactorPowerVo factorPowerVo:factorPowerVoList){
							String comCode = factorPowerVo.getDataValue();
							if("0001%".equals(comCode)){//???????????????
								isMainCom = true;
								break;
							}
						}
						if(!isMainCom){
							sqlUtil.append(" AND (");
							for(int i = 0; i < factorPowerVoList.size(); i++){
								SaaFactorPowerVo factorPowerVo = factorPowerVoList.get(i);
								String comCode = factorPowerVo.getDataValue();
								if(i > 0){
									sqlUtil.append("  OR  ");
								}
								sqlUtil.append(this.genSubPowerHql(factorPowerVo,"comCode","cpl"));// ??????alias. fieldCode = ?);
								sqlUtil.addParamValue(comCode);
							}
							sqlUtil.append(" )");
						}
					}
				} else {
					return new ResultPage<CiClaimPlatformLogVo>(start,length,total,logVoList);
				}
			}

		sqlUtil.append(" Order By cpl.createTime desc ");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();

		logger.info("===========SQL:=================="+sql);
		logger.info("queryVo.getRequestTimeStart():=="+queryVo.getRequestTimeStart()+"===========VAL:=================="+values.toString());
		// ????????????
		Page<CiClaimPlatformLog> page = databaseDao.findPageByHql(CiClaimPlatformLog.class,sql,( start/length+1 ),length,values);

			// ????????????
			List<CiClaimPlatformLog> resultList = page.getResult();
			if(resultList!=null&& !resultList.isEmpty()){
				for(CiClaimPlatformLog result:resultList){
					CiClaimPlatformLogVo logVo = new CiClaimPlatformLogVo();
					Beans.copy().from(result).to(logVo);
					if(!StringUtils.isBlank(logVo.getErrorMessage())){
						logVo.setErrorMessage(logVo.getErrorMessage().replaceAll("\""," "));
					}
					logVoList.add(logVo);
				}
			}
			total = page.getTotalCount();
		} else {
			throw new RuntimeException("?????????????????????????????????");
		}
		ResultPage<CiClaimPlatformLogVo> resultPage = new ResultPage<CiClaimPlatformLogVo>(start,length,total,logVoList);
		return resultPage;
	}

	@Override
	public ResultPage<CiClaimPlatformTaskVo> findPaltformTaskForPage(CiClaimPlatformTaskVo queryVo,Integer start,Integer length) throws Exception{
		List<CiClaimPlatformTaskVo> taskVoList = new ArrayList<CiClaimPlatformTaskVo>();
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		
		sqlUtil.append(" FROM CiClaimPlatformTask t where 1=1");
		
		// ????????????
		if(StringUtils.isNotBlank(queryVo.getClaimSeqNo())){
			sqlUtil.append(" AND t.claimSeqNo = ? ");
			sqlUtil.addParamValue(queryVo.getClaimSeqNo().trim());
		}
		//?????????
		if(StringUtils.isNotBlank(queryVo.getRegistNo())){
			sqlUtil.append(" AND t.registNo = ? ");
			sqlUtil.addParamValue(queryVo.getRegistNo().trim());
		}
		//?????????
		if(StringUtils.isNotBlank(queryVo.getBussNo())){
			sqlUtil.append(" AND t.bussNo = ? ");
			sqlUtil.addParamValue(queryVo.getBussNo().trim());
		}
		// ????????????
		if(StringUtils.isNotBlank(queryVo.getRequestType())){
			sqlUtil.append(" AND t.requestType = ? ");
			sqlUtil.addParamValue(queryVo.getRequestType());
		}
		// ??????
		if(StringUtils.isNotBlank(queryVo.getComCode())){
			sqlUtil.append(" AND EXISTS(select 1 from  PrpLRegist r where t.registNo=r.registNo and r.comCode = ? ) ");
			sqlUtil.addParamValue(queryVo.getComCode());
		}
		// ??????
		if(StringUtils.isNotBlank(queryVo.getStatus())){
			sqlUtil.append(" AND t.status = ? ");
			sqlUtil.addParamValue(queryVo.getStatus());
		}
		//?????????????????????????????????????????????????????? 
		if (StringUtils.isBlank(queryVo.getClaimSeqNo()) && StringUtils.isBlank(queryVo.getBussNo()) 
				&& StringUtils.isBlank(queryVo.getRegistNo())) {
			// ????????????
			sqlUtil.andDate(queryVo,"t","lastDate");
		}
		//29962 ???????????????????????? ?????????????????????????????? ????????????????????????????????????????????????????????????????????????????????????????????????
		String userCode = queryVo.getUserCode();
		SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(userCode);
		Map<String, List<SaaFactorPowerVo>> factorPowerMap = userPowerVo.getPermitFactorMap();
		if (!("0000000000").equals(userCode)) {
			if (null != factorPowerMap && !factorPowerMap.isEmpty()) {
				List<SaaFactorPowerVo> factorPowerVoList = factorPowerMap.get("FF_COMCODE");
				if(factorPowerVoList == null || factorPowerVoList.size() == 0){
					return new ResultPage<CiClaimPlatformTaskVo>(start,length,0,taskVoList);
				}else{
					logger.info("????????????????????????????????????????????????==========");
					boolean isMainCom = false;
					for(SaaFactorPowerVo factorPowerVo:factorPowerVoList){
						String comCode = factorPowerVo.getDataValue();
						if("0001%".equals(comCode)){//???????????????
							isMainCom = true;
							break;
						}
					}
					if(!isMainCom){
						sqlUtil.append(" AND (");
						for(int i = 0; i < factorPowerVoList.size(); i++){
							SaaFactorPowerVo factorPowerVo = factorPowerVoList.get(i);
							String comCode = factorPowerVo.getDataValue();
							if(i > 0){
								sqlUtil.append("  OR  ");
							}
							sqlUtil.append("EXISTS(select 1 from  PrpLRegist r where t.registNo=r.registNo and  ");
							sqlUtil.append(this.genSubPowerHql(factorPowerVo,"comCode","r"));// ??????alias. fieldCode = ?);
							sqlUtil.addParamValue(comCode);
							sqlUtil.append(" )");
						}
						sqlUtil.append(" )");
					}
				}
			} else {
				return new ResultPage<CiClaimPlatformTaskVo>(start,length,0,taskVoList);
			}
		}


		sqlUtil.append(" Order By t.lastDate desc ");
		String sql = sqlUtil.getSql();
		logger.info("??????????????????????????????sql: " + sql);
		Object[] values = sqlUtil.getParamValues();
		logger.info("queryVo.getRequestTimeStart():=="+queryVo.getRequestTimeStart()+"===========VAL:=================="+values.toString());
		
		// ????????????
		Page<CiClaimPlatformTask> page = databaseDao.findPageByHql(CiClaimPlatformTask.class,sql,( start/length+1 ),length,values);
		
		// ????????????
		List<CiClaimPlatformTask> resultList = page.getResult();
		if(resultList!=null&& !resultList.isEmpty()){
			for(CiClaimPlatformTask result:resultList){
				CiClaimPlatformTaskVo taskVo = new CiClaimPlatformTaskVo();
				Beans.copy().from(result).to(taskVo);
				if(!StringUtils.isBlank(taskVo.getRemark())){
					taskVo.setRemark(taskVo.getRemark().replaceAll("\""," "));
				}
				taskVoList.add(taskVo);
			}
		}
		Long total = page.getTotalCount();
		ResultPage<CiClaimPlatformTaskVo> resultPage = new ResultPage<CiClaimPlatformTaskVo>(start,length,total,taskVoList);
		return resultPage;
	}

	
	/**
	 * ?????????????????????????????????
	 * @param logId
	 * @return
	 */
	@Override
	public CiClaimPlatformLogVo findLogById(Long logId) {
		return platformLogService.findLogById(logId);
	}
	
	@Override
	public CiClaimPlatformLogVo findLogByTaskId(Long taskId){
		return ciClaimPlatformTaskService.findLogByTaskId(taskId);
	}
	
	@Override
	public CiClaimPlatformTaskVo findPlatformTaskById(Long taskId){
		return ciClaimPlatformTaskService.findPlatformTaskByPK(taskId);
	}

	/**
	 * ????????????????????????
	 * @param reqType
	 * @param bussNo
	 * @param comCode
	 * @return
	 */
	@Override
	public CiClaimPlatformLogVo findLogByBussNo(String reqType,String bussNo,String comCode) {
		return platformLogService.findLogByBussNo(reqType,bussNo,comCode);
	}
	
	/**
	 * ????????????????????????????????????????????????????????????????????????flag=1????????????????????????
	 * @param reqType
	 * @param bussNo
	 * @param status
	 * @param flag
	 * @return
	 */
	@Override
	public CiClaimPlatformLogVo findPlatformLog(String reqType,String bussNo,String status,String flag) {
		return platformLogService.findPlatformLog(reqType, bussNo, status, flag);
	}

	@Override
	public void platformLogUpdate(Long logId) {
		platformLogService.platformLogUpdate(logId);
	}

	@Override
	public String dataReloadSend(String uploadNode, String bussNoArray, SysUserVo userVo) throws Exception{
		String returnData = "";// ???????????????????????????
		String[] bussArray = bussNoArray.split("\n");// ???????????????
		if ( bussArray == null || bussArray.length == 0 ) {
			return returnData;
		}
		// ???????????????????????????????????????
		if("payment".equals(uploadNode)||"paymentSH".equals(uploadNode)){ // ????????????(???????????????)

			//
			returnData = this.sendPaymentToPlatform(bussArray);
		}else if(uploadNode.startsWith("HNIS_")||uploadNode.startsWith("GZIS_")){// ??????
			returnData = this.reUploadHNOrGZ(uploadNode, bussArray);
		} else if ("platform_reSendAll".equals(uploadNode)){
		    returnData =  reSendToPlatformAll(uploadNode, bussArray);
		}else if("Invoice".equals(uploadNode)){ // ?????????????????????????????????
            returnData = sendInvoice(bussArray);
        } else if (BusinessType.AssessorFee_Invoice.toString().equals(uploadNode)) {
            returnData = sendAssessorFeeInvoice(bussArray);
        } else if (BusinessType.SDEW_regist.toString().equals(uploadNode)) {
            returnData = sendRegistToCarRiskPlatformByCmain(bussArray);
        } else if ("SZClaim".toString().equals(uploadNode)) {
            returnData = sendClaimInfoToSZPoliceByclaimNo(bussArray);
        }else if("SZReg".equals(uploadNode)){
			returnData = szpoliceRegistService.batchSendToSZJB(bussArray);
		}else if("platform_reSendRegist".equals(uploadNode)){
			returnData = registToPaltformService.sendRegistToPlatform2(bussArray);
		}else if("genilex".equals(uploadNode)){//??????????????????
			returnData = claimToGenilexService.uploadGenilex(bussArray, userVo);
		}else{
			returnData = "????????????????????????";
		}
		return returnData;
	}

	private String sendInvoice(String[] bussArray) {
		StringBuffer strBuf = new StringBuffer();
		bussArray = this.getNoRepeatArray(bussArray);

		for(String bussNo:bussArray){
			List<ClaimInterfaceLogVo> claimInterfaceLogVoList = findLogVo("Invoice",bussNo);
			if(claimInterfaceLogVoList!=null&&claimInterfaceLogVoList.size()>0){
				List<ClaimInterfaceLogVo> claimInterfaceLogVos = new ArrayList<ClaimInterfaceLogVo>();
				for(ClaimInterfaceLogVo claimInterfaceLogVo:claimInterfaceLogVoList){
					if("0".equals(claimInterfaceLogVo.getStatus())){
						changeInterfaceLog(claimInterfaceLogVo.getId());// ?????????
					}
					if("1".equals(claimInterfaceLogVo.getStatus())){
						claimInterfaceLogVos.add(claimInterfaceLogVo);
					}
				}
				if(claimInterfaceLogVos!=null&&claimInterfaceLogVos.size()>0){
					logger.info("????????????????????????");
					strBuf.append("?????????????????????"+bussNo+"???????????????????????????????????????????????????\n");

				}else{
					try{
						boolean result = false;
						if(bussNo.startsWith("Y")){
							result = claimToPaymentService.pushPreCharge(bussNo,null);
						}else{
							result = claimToPaymentService.pushCharge(bussNo,null);
						}
						if(result){
							strBuf.append(bussNo+" : ???????????????\n");
						}else{
							strBuf.append(bussNo+" : ???????????????\n");
						}

					}catch(Exception e){
						logger.info("???????????????????????????"+e.getStackTrace());
						strBuf.append(bussNo+" : ??????????????????????????????\n");
					}
				}
			}else{
				strBuf.append(bussNo+": ????????????????????????????????????????????????????????????????????????\n");
			}
		}
		return strBuf.toString();
	}

    private String sendAssessorFeeInvoice(String[] bussArray) {
        StringBuffer strBuf = new StringBuffer();
        for (String bussNo : bussArray) {
            List<ClaimInterfaceLogVo> claimInterfaceLogVoList = findLogVo("AssessorFee_Invoice",bussNo);
            if(claimInterfaceLogVoList!=null&&claimInterfaceLogVoList.size()>0){
                List<ClaimInterfaceLogVo> claimInterfaceLogVos = new ArrayList<ClaimInterfaceLogVo>();
                for(ClaimInterfaceLogVo claimInterfaceLogVo:claimInterfaceLogVoList){
                    if("0".equals(claimInterfaceLogVo.getStatus())){
						changeInterfaceLog(claimInterfaceLogVo.getId());// ?????????
                    }
                    if("1".equals(claimInterfaceLogVo.getStatus())){
                        claimInterfaceLogVos.add(claimInterfaceLogVo);
                    }
                }
                if(claimInterfaceLogVos!=null&&claimInterfaceLogVos.size()>0){
					logger.info("????????????????????????");
					strBuf.append("?????????????????????"+bussNo+"???????????????????????????????????????????????????\n");
                }else {
                    try{
                        SysUserVo userVo = new SysUserVo();
                        userVo.setUserCode(claimInterfaceLogVoList.get(0).getCreateUser());
                        userVo.setComCode(claimInterfaceLogVoList.get(0).getComCode());
                        PrpLAssessorFeeVo assFeeVo = assessorService.findAssessorFeeVoByComp(claimInterfaceLogVoList.get(0).getRegistNo(), claimInterfaceLogVoList.get(0).getCompensateNo());
                        boolean result = claimToPaymentService.pushAssessorFee(assFeeVo, userVo);
                        if(result){
							strBuf.append(bussNo+" : ???????????????\n");
                        }
                        else {
							strBuf.append(bussNo+" : ???????????????\n");
                        }
                     }
                     catch(Exception e){
						logger.info("???????????????????????????"+e.getStackTrace());
						strBuf.append(bussNo+" : ??????????????????????????????\n");
                     }
                }
            }else {
				strBuf.append(bussNo+": ??????????????????????????????????????????\n");
            }     
        }
        return strBuf.toString();
    }
	
	// ????????????(??????) || ????????????(??????) //
	private String sendPaymentToPlatform(String[] bussArray) {
		StringBuffer strBuf = new StringBuffer();
		for(String bussNo:bussArray){
			PrpLCompensateVo compeVo = compensateTaskService.findCompByPK(bussNo);
			if(compeVo!=null&&bussNo.startsWith("7")){
				try{
					CiClaimPlatformLogVo logVo = claimToPaltformService.paymentListResend(compeVo);
					if(logVo!=null&&"1".equals(logVo.getStatus())){
						strBuf.append("\n"+bussNo+" : ???????????????");
					} else {
						strBuf.append("\n"+bussNo+" : ???????????????"+"???????????????"+logVo.getErrorCode()+"??????????????????"+logVo.getErrorMessage());
					}
				}catch(Exception e){
					logger.info("???????????????????????????", e);
					strBuf.append("\n"+bussNo+" : ??????????????????????????????");
				}
			}else{
				strBuf.append("\n"+bussNo+" : ??????????????????????????????????????????");
				// 711060020171206000000286:??????,
			}
		}
		return strBuf.toString();
	}


	// ??????
	public String reUploadHNOrGZ(String uploadNode, String[] bussArray) throws Exception {
		StringBuffer strBuf = new StringBuffer();
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		for(String bussNo:bussArray){
			try{
				PrpLClaimVo prpLClaimVo = claimTaskService.findClaimVoByClaimNo(bussNo);
				if(prpLClaimVo!=null){
				    SysUserVo userVo = new SysUserVo();
	                userVo.setUserCode("0000000000");
					if(BusinessType.GZIS_claim.name().equals(uploadNode)){// ?????????????????????
	                    logVo=caseLeapService.claimToGZ(prpLClaimVo, userVo);
					}else if(BusinessType.HNIS_claim.name().equals(uploadNode)){ // ?????????????????????
	                    logVo=caseLeapHNService.claimToHN(prpLClaimVo, userVo);
					}else if(BusinessType.GZIS_endCase.name().equals(uploadNode)){ // ?????????????????????
	                    PrpLEndCaseVo prpLEndCaseVo = endCaseService.queryEndCaseVo(prpLClaimVo.getRegistNo(),bussNo);
	                    logVo=caseLeapService.endCaseToGZ(prpLEndCaseVo,userVo.getUserCode());
					}else if(BusinessType.HNIS_endCase.name().equals(uploadNode)){ // ?????????????????????
	                    PrpLEndCaseVo prpLEndCaseVo = endCaseService.queryEndCaseVo(prpLClaimVo.getRegistNo(),bussNo);
	                    logVo=caseLeapHNService.endCaseToHN(prpLEndCaseVo,userVo.getUserCode());
	                }
	                if (logVo != null && "1".equals(logVo.getStatus())) {
						strBuf.append(bussNo+" : ???????????????"+"\n");
	                }else if(logVo != null && !"1".equals(logVo.getStatus())){
						strBuf.append(bussNo+" : ???????????????"+"???????????????"+logVo.getErrorCode()+"??????????????????"+logVo.getErrorMessage()+"\n");
	                }else{
						strBuf.append(bussNo+" : ???????????????"+"???????????????????????????????????????"+"\n");
	                }
				}else {
					strBuf.append(bussNo+": ??????????????????????????????????????????\n");
                }
			}catch(Exception e){
				logger.info("???????????????????????????"+e.getStackTrace());
				strBuf.append(bussNo+" : ??????????????????????????????"+"\n");
			}
		}
		return strBuf.toString();
	}

	private String reSendToPlatformAll(String uploadNode, String[] bussArray ) throws Exception{
	    StringBuffer StringBuffer = new StringBuffer();
	    String bussno ="";
	    try{
	    for (String bussNo : bussArray) {
	        bussno = bussNo;
	        if(StringUtils.isBlank(bussNo)){
	            continue;
	        }
				List<CiClaimPlatformLogVo> logVoList = findPlatformLogByClaimseq(bussNo);// ?????????????????????
	        Map<Integer,CiClaimPlatformLogVo> map =   sortPlatformLogByNode(logVoList);
				if(map.containsKey(10)){ // ???????????????????????? ??????????????????
					StringBuffer.append(bussNo+"--???????????????????????????????????????????????????????????????"+"\n");
	            StringBuffer.append("\n");
	            continue;
	            
	        }
	        if(!map.isEmpty()){
	            for(Map.Entry<Integer,CiClaimPlatformLogVo> entryMap : map.entrySet() ){
	                if(entryMap.getKey() < 10){
	                    CiClaimPlatformLogVo logVo = (CiClaimPlatformLogVo)entryMap.getValue();
	                    String requestName = logVo.getRequestName();
							if(requestName.contains("????????????")){ // ????????????
	                        requestName = "Regis";
							}else if(requestName.contains("????????????")){ // ????????????
                            requestName = "Cancel";
							}else if(requestName.contains("????????????")){ // ????????????
	                        requestName = "Check";
							}else if(requestName.contains("????????????")){ // ????????????
	                        requestName = "Claim";
							}else if(requestName.contains("??????/??????/????????????")||requestName.contains("???????????????")){ // ???????????????
	                        requestName = "Loss";
							}else if(requestName.contains("????????????")){ // ????????????
	                        requestName = "Certify";
							}else if(requestName.contains("????????????")){ // ????????????
	                        requestName = "VClaim";
							}else if(requestName.contains("????????????")||requestName.contains("??????????????????")){ // ????????????
	                        requestName = "EndCase";
							}else if(requestName.contains("??????????????????")){ // ??????????????????
	                        requestName = "Payment";
	                    }
	                    this.reuploadToAll(requestName,logVo);
	                    this.platformLogUpdate(logVo.getId());
	                    CiClaimPlatformLogVo newLogVo = findLogByBussNo(logVo.getRequestType(),logVo.getBussNo(),logVo.getComCode());
	                    if(newLogVo != null){
								StringBuffer.append(bussNo+"--"+logVo.getRequestName()+"--?????????????????????"+"\n");
	                    }else{
								StringBuffer.append(bussNo+"--"+logVo.getRequestName()+" --?????????????????????"+"\n");
	                    }
	                }
	               }
	            }
	        StringBuffer.append("\n");
	        }
	  
			StringBuffer.append("????????????"+"\n");
	    }catch(Exception e){
			throw new RuntimeException("</br>"+"????????????:"+bussno+"</br>"+"????????????:"+e.getMessage());
	    }
	    return StringBuffer.toString();
	}



	private Map<Integer,CiClaimPlatformLogVo> sortPlatformLogByNode(  List<CiClaimPlatformLogVo> logVoList){
        Map<Integer,CiClaimPlatformLogVo> map = new TreeMap<Integer,CiClaimPlatformLogVo>();
        if(logVoList != null && logVoList.size() > 0){
			for(CiClaimPlatformLogVo logVo:logVoList){// ???????????? ???????????????
                String requestName = logVo.getRequestName();
				if(requestName.contains("????????????")){ // ????????????
                    map.put(1,logVo);
				}else if(requestName.contains("????????????")){ // ??????????????????
                    map.put(2,logVo);
				}else if(requestName.contains("????????????")){ // ????????????
                    map.put(3,logVo);
				}else if(requestName.contains("????????????")){ // ????????????
                    map.put(4,logVo);
				}else if(requestName.contains("??????/??????/????????????")||requestName.contains("???????????????")){ // ???????????????
                    map.put(5,logVo);
				}else if(requestName.contains("????????????")){ // ????????????
                    map.put(6,logVo);
				}else if(requestName.contains("????????????")){ // ????????????
                    map.put(7,logVo);
				}else if(requestName.contains("????????????")||requestName.contains("??????????????????")){ // ????????????
                    map.put(8,logVo);
				}else if(requestName.contains("??????????????????")){ // ??????????????????
                    map.put(9,logVo);
				}else if(requestName.contains("????????????")||requestName.contains("????????????")){ // ????????????
                    map.put(10,logVo);
				}else{ // ?????? ?????????
                    map.put(11,logVo);
                }
            }
        }
        return map;
    }
	
    private List<CiClaimPlatformLogVo> findPlatformLogByClaimseq(String claimSeqNo){
	    List<CiClaimPlatformLogVo> platformLogVoList = new  ArrayList<CiClaimPlatformLogVo>();
	    QueryRule queryRule = QueryRule.getInstance();
	    queryRule.addEqual("claimSeqNo",claimSeqNo);
	    queryRule.addEqual("status","0");
	    List<CiClaimPlatformLog> platformLogPoList = databaseDao.findAll(CiClaimPlatformLog.class,queryRule);
	    if(platformLogPoList !=null && platformLogPoList.size() > 0){
	        for(CiClaimPlatformLog platformLogPo : platformLogPoList){
	            CiClaimPlatformLogVo logVo = new CiClaimPlatformLogVo();
	              Beans.copy().from(platformLogPo).to(logVo); 
	              platformLogVoList.add(logVo);
	        }
	    }else{
			throw new RuntimeException("???????????????");
	    }
	    return platformLogVoList;

	}

	private List<ClaimInterfaceLogVo> findLogVo(String bussineType,String compensateNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("businessType",bussineType);
		queryRule.addEqual("compensateNo",compensateNo);
		List<ClaimInterfaceLog> logVoList = databaseDao.findAll(ClaimInterfaceLog.class,queryRule);
		List<ClaimInterfaceLogVo> claimInterfaceLogVos = new ArrayList<ClaimInterfaceLogVo>();
		if(logVoList!=null&&logVoList.size()>0){
			claimInterfaceLogVos = Beans.copyDepth().from(logVoList).toList(ClaimInterfaceLogVo.class);
		}
		return claimInterfaceLogVos;

	}

	public void changeInterfaceLog(Long id) {
		ClaimInterfaceLog logPo = databaseDao.findByPK(ClaimInterfaceLog.class,id);
		if(logPo!=null){
			logPo.setStatus("2");// ?????????
			databaseDao.update(ClaimInterfaceLog.class,logPo);
		}
	}

	/**
	 * bussArray ??????
	 * 
	 * <pre></pre>
	 * @param reArray
	 * @return
	 * @modified: ???WLL(2017???9???19??? ??????4:22:16): <br>
	 */
	private String[] getNoRepeatArray(String[] reArray) {
		String[] NoRepeatArray = null;
		if(reArray!=null&&reArray.length>0){
			List<String> list = Arrays.asList(reArray);
			Set<String> set = new HashSet<String>(list);
			NoRepeatArray = (String[])set.toArray(new String[0]);
		}
		return NoRepeatArray;
	}

	private String sendRegistToCarRiskPlatformByCmain(String[] bussArray) {
		StringBuffer strBuf = new StringBuffer();
		// ????????????
		SysUserVo userVo = new SysUserVo();
		userVo.setComCode("00000000");
		userVo.setUserCode("0000000000");
		for(String bussNo:bussArray){
			try{
				ClaimInterfaceLogVo logVo = claimInterfaceLogService.findLogByPK(Long.parseLong(bussNo));
				CarRiskRegistReqVo reqVo = ClaimBaseCoder.xmlToObj(logVo.getRequestXml(),CarRiskRegistReqVo.class);
				CarRiskRegistBasePartReqVo basePartReqVoreqVo = reqVo.getBody().getCarRiskRegistBasePartReqVo();
				registToCarRiskPaltformService.sendRegistToCarRiskPlatformRe(logVo.getRegistNo(),basePartReqVoreqVo.getPolicyNo(),userVo);
				strBuf.append(bussNo+" : ???????????????\n");
				// ???????????????????????????????????????????????? - ????????????
				claimInterfaceLogService.changeInterfaceLog(Long.parseLong(bussNo));
			}catch(Exception e){
				strBuf.append(bussNo+" : ???????????????\n");
			}
		}
		return strBuf.toString();
	}
    
	/**
	 * ?????????????????????????????????????????????
	 * @param bussArray
	 * @return
	 */
    private String sendClaimInfoToSZPoliceByclaimNo(String[] bussArray){
    	StringBuffer strBuf = new StringBuffer();
    	QueryRule queryLog=QueryRule.getInstance();
    	QueryRule queryClaim=QueryRule.getInstance();
    	QueryRule queryPolicy=QueryRule.getInstance();
    	Set<String> ClaimNoset=new HashSet<String>();
    	List<String> claimNoList=new ArrayList<String>();
    	List<String> claimNoSzList=new ArrayList<String>();
    	
    	for(String bussNo : bussArray){
    		if(ClaimNoset.add(bussNo.trim())){
    			claimNoList.add(bussNo.trim());
    		}
    	}
    	if(claimNoList!=null && claimNoList.size()>0){
    	queryClaim.addIn("claimNo",claimNoList);
			queryPolicy.addLike("comCode","0002%");// ?????????????????????
    	List<PrpLClaim> claimPos=databaseDao.findAll(PrpLClaim.class,queryClaim);
    	if(claimPos!=null && claimPos.size()>0){
    		for(PrpLClaim prpLClaim:claimPos){
    				claimNoSzList.add(prpLClaim.getClaimNo());
    			
    		}
    	}
        	for(String claimNo:claimNoList){
        		if(!claimNoSzList.contains(claimNo)){
					strBuf.append(claimNo+" : ?????????????????????????????????????????????\n");
        		}
        	}
        	if(claimNoList!=null && claimNoList.size()>0){
				// ?????????????????????????????? - ?????????
        	queryLog.addLike("businessType","SZClaim%");
    		queryLog.addEqual("status","0");
    		queryLog.addIn("claimNo",claimNoSzList);
    		List<ClaimInterfaceLog> logs=databaseDao.findAll(ClaimInterfaceLog.class,queryLog);
    		if(logs!=null && logs.size()>0){
    			for(ClaimInterfaceLog logpo:logs){
    				logpo.setStatus("2");
    			}
    		}
        	
            for (String claimNo : claimNoSzList) {
              String status=szpoliceClaimInfoService.settleClaimPLReUpload(claimNo);
    	       if("1".equals(status)){
						strBuf.append(claimNo+" : ???????????????\n");
    	        }else{
						strBuf.append(claimNo+" : ???????????????\n");
    	        }
    		 
    				
    			
            }
        	}
    	}
        return strBuf.toString();
    }

	@Override
	public CiClaimPlatformLogVo findLastestLogByReqTypeBussNoComCode(String reqType,String bussNo,String comCode) {
		return platformLogService.findLastestLogByReqTypeBussNoComCode(reqType,bussNo,comCode);
	}

	@Override
	public void platformFirstUpload(CiClaimPlatformLogVo logVo) throws Exception {
		String comCode = logVo.getComCode();
		String reqType = logVo.getRequestType();

		String nodeName = "";
		if(comCode.startsWith("22")){// 22--????????????
			nodeName = CodeConstants.REQUESTTYPE_SH.get(reqType);
		}else{
			nodeName = CodeConstants.REQUESTTYPE_BASE.get(reqType);
		}

		if(StringUtils.isEmpty(nodeName)){
			throw new IllegalArgumentException("??????????????????????????????");
		}else{
			nodeName = nodeName.split("_")[0];
		}
		if(comCode.startsWith("22")){// ????????????
			reuploadToSH(nodeName,logVo);
		}else{// ????????????
			reuploadToAll(nodeName,logVo);
		}
	}

	private String genSubPowerHql(SaaFactorPowerVo factorPowerVo,String fieldCode,String alias) {

		SqlParamVo subPowerSqlVo = new SqlParamVo();
		String dataOper = factorPowerVo.getDataOper().toLowerCase();
		if("=".equals(dataOper)||"<".equals(dataOper)||">".equals(dataOper)||"<=".equals(dataOper)||">=".equals(dataOper)||"like".equals(dataOper)){// ???????????????????????????
			subPowerSqlVo.getSql().append(alias).append(".").append(fieldCode).append(" ").append(dataOper).append(" ? ");
		}
		return subPowerSqlVo.getSql().toString();

	}
}
