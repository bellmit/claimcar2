package ins.sino.claimcar.court.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Path;

import ins.framework.utils.Beans;
import ins.framework.utils.copier.SimpleCopier;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.court.service.CourtService;
import ins.sino.claimcar.court.vo.CourtAccidentInfo;
import ins.sino.claimcar.court.vo.CourtAgentInfo;
import ins.sino.claimcar.court.vo.CourtCompensationInfo;
import ins.sino.claimcar.court.vo.CourtIdentifyInfo;
import ins.sino.claimcar.court.vo.CourtLitigationMediationInfo;
import ins.sino.claimcar.court.vo.CourtMediationInfo;
import ins.sino.claimcar.court.vo.CourtOnlineRegistrationInfo;
import ins.sino.claimcar.court.vo.CourtPartyInfo;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.sino.claimcar.court.po.PrpLCourtAgent;
import ins.sino.claimcar.court.po.PrpLCourtClaim;
import ins.sino.claimcar.court.po.PrpLCourtCompensation;
import ins.sino.claimcar.court.po.PrpLCourtConfirm;
import ins.sino.claimcar.court.po.PrpLCourtFile;
import ins.sino.claimcar.court.po.PrpLCourtIdentify;
import ins.sino.claimcar.court.po.PrpLCourtLitigation;
import ins.sino.claimcar.court.po.PrpLCourtMediation;
import ins.sino.claimcar.court.po.PrpLCourtMessage;
import ins.sino.claimcar.court.po.PrpLCourtParty;
import ins.sino.claimcar.court.vo.CourtBasicInfo;
import ins.sino.claimcar.court.vo.CourtFileInfo;
import ins.sino.claimcar.court.vo.CourtJudicialConfirmInfo;
import ins.sino.claimcar.court.vo.CourtRegistInfo;
import ins.sino.claimcar.court.vo.PrpLCourtAccidentVo;
import ins.sino.claimcar.court.vo.PrpLCourtAgentVo;
import ins.sino.claimcar.court.vo.PrpLCourtClaimVo;
import ins.sino.claimcar.court.vo.PrpLCourtCompensationVo;
import ins.sino.claimcar.court.vo.PrpLCourtConfirmVo;
import ins.sino.claimcar.court.vo.PrpLCourtFileVo;
import ins.sino.claimcar.court.vo.PrpLCourtIdentifyVo;
import ins.sino.claimcar.court.vo.PrpLCourtLitigationVo;
import ins.sino.claimcar.court.vo.PrpLCourtMediationVo;
import ins.sino.claimcar.court.vo.PrpLCourtMessageVo;
import ins.sino.claimcar.court.vo.PrpLCourtPartyVo;
import ins.sino.claimcar.court.vo.PrpLCourtRegistVo;
import ins.sino.claimcar.court.vo.SeniorCourtQueryReqVo;
import ins.sino.claimcar.court.vo.SeniorCourtQueryResVo;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path("courtService")
public class CourtServiceImpl implements CourtService {
	
	private static Logger log = LoggerFactory.getLogger(CourtServiceImpl.class);
	
	@Autowired
    ClaimInterfaceLogService interfaceLogService;
	@Autowired
	private DatabaseDao dataBaseDao;
	@Autowired
	BaseDaoService baseDaoService;

	@Override
	public void seniorCourtQuery(PrpLCourtMessageVo courtMsgVo) {
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		String url = "";
		String requestContent = "";
		String responContent = "";
		Date requestTime = null;
		//先更新日志表为已补送
		/*try{
			if(courtMsgVo.getExecuteTimes()>0){
				StringBuffer sqlBuffer = new StringBuffer();
				sqlBuffer.append("UPATE CLAIMUSER.CLAIMINTERFACELOG A SET A.STATUS='2' WHERE A.REGISTNO='");
				sqlBuffer.append(courtMsgVo.getCaseNo());
				sqlBuffer.append("' AND A.BUSINESSTYPE='");
				sqlBuffer.append(BusinessType.COURT_Search.name());
				sqlBuffer.append("' AND A.STATUS='0' AND A.REMARK='");
				sqlBuffer.append(courtMsgVo.getId().toString());
				sqlBuffer.append("'");
				baseDaoService.executeSQL(sqlBuffer.toString());
			}
		}catch(Exception e){
			log.error(courtMsgVo.getCaseNo()+"高院查询服务更新日志表报错：", e.getMessage(), e);
		}*/
		//接口请求
		try{
			SeniorCourtQueryReqVo queryReqVo = organizeReqVo(courtMsgVo);
			url = SpringProperties.getProperty("COURT_URL");
			JSONObject jsonObject = JSONObject.fromObject(queryReqVo);
			JSONObject reqJson = new JSONObject();
			reqJson.put("arg0", jsonObject);
			requestContent = reqJson.toString();
			log.info(courtMsgVo.getCaseNo()+"===========================高院查询请求报文："+requestContent);
			 
			HttpPost httpPost = new HttpPost(url);
			if(requestContent!=null&& !requestContent.trim().equals("")){
	               StringEntity requestEntity = new StringEntity(requestContent,ContentType.create("application/json","UTF-8"));
	               httpPost.setEntity(requestEntity);
	        }
			DefaultHttpClient httpClient = new DefaultHttpClient();
			requestTime = new Date();
	        HttpResponse response = httpClient.execute(httpPost);
	        HttpEntity httpEntity = response.getEntity();
	        responContent = EntityUtils.toString(httpEntity);
	        responContent = new String(responContent.getBytes("UTF-8"), "UTF-8");
	        log.info(courtMsgVo.getCaseNo()+"===========================高院查询返回报文："+responContent);
	        
	        Map<String,Object> classMap = new HashMap<String,Object>();
			classMap.put("partyInfo", CourtPartyInfo.class);
			classMap.put("agentInfo", CourtAgentInfo.class);
			classMap.put("identifyInfo", CourtIdentifyInfo.class);
			classMap.put("mediationInfo", CourtMediationInfo.class);
			classMap.put("compensationInfo", CourtCompensationInfo.class);
			classMap.put("onlineRegistrationInfo", CourtOnlineRegistrationInfo.class);
			classMap.put("litigationMediationInfo", CourtLitigationMediationInfo.class);
			classMap.put("judicialconfirmInfo", CourtJudicialConfirmInfo.class);
			classMap.put("fileInfo", CourtFileInfo.class);
			
	        SeniorCourtQueryResVo resVo = (SeniorCourtQueryResVo) JSONObject.toBean(JSONObject.fromObject(responContent), SeniorCourtQueryResVo.class,classMap);
	        
	        if("1".equals(resVo.getRetCode())){
	        	logVo.setStatus("1");
	        }else{
            	logVo.setStatus("0");
            	logVo.setErrorCode(resVo.getRetMessage());
            }
			
	        //保存高院返回的信息
	        organizeCourtMsg(resVo, courtMsgVo);
	        saveCourtMessage(courtMsgVo);
		}catch(Exception e){
			logVo.setStatus("0");
        	logVo.setErrorMessage(e.getMessage());
        	courtMsgVo.setRetCode("0");
        	log.error(courtMsgVo.getCaseNo()+"高院查询报错：", e);
		}finally{
			logVo.setRequestUrl(url);
            logVo.setRequestXml(requestContent);
            logVo.setResponseXml(responContent);
            logVo.setRegistNo(courtMsgVo.getCaseNo());
            logVo.setBusinessType(BusinessType.COURT_Search.name());
            logVo.setBusinessName(BusinessType.COURT_Search.getName());
            logVo.setRequestTime(requestTime);
            logVo.setCreateTime(new Date());
            logVo.setCreateUser("0000000000");
            logVo.setRemark(courtMsgVo.getId().toString());
            interfaceLogService.save(logVo);
		}
	}
	
	
	public SeniorCourtQueryReqVo organizeReqVo(PrpLCourtMessageVo courtMsgVo){
		SeniorCourtQueryReqVo queryReqVo = new SeniorCourtQueryReqVo();
		
		queryReqVo.setCompanyCode("DHIC");
		queryReqVo.setAreaCode(courtMsgVo.getAreaCode());
		queryReqVo.setCaseNo(courtMsgVo.getCaseNo());
		queryReqVo.setSearchCode(courtMsgVo.getSearchCode());
		queryReqVo.setUserName(courtMsgVo.getUserName());
		queryReqVo.setPassWord(courtMsgVo.getPassWord());
		queryReqVo.setRequestType("02");
		queryReqVo.setJkptUuid(UUID.randomUUID().toString());
		queryReqVo.setJkptUuid(courtMsgVo.getJkptUuid());
		
		return queryReqVo;
	}
	
	public void organizeCourtMsg(SeniorCourtQueryResVo resVo,PrpLCourtMessageVo courtMsgVo) throws ParseException{
		
		courtMsgVo.setExecuteTime(new Date());
		courtMsgVo.setExecuteTimes(courtMsgVo.getExecuteTimes()+1);
		courtMsgVo.setRetCode(resVo.getRetCode());
		courtMsgVo.setRetMessage(resVo.getRetMessage());
		
		//事故信息表
		if(resVo.getCaseinfo()!=null && resVo.getCaseinfo().getAccidentInfo()!=null){
			CourtAccidentInfo accidentInfo = resVo.getCaseinfo().getAccidentInfo();
			PrpLCourtAccidentVo prpLCourtAccident = new PrpLCourtAccidentVo();
			prpLCourtAccident.setId(courtMsgVo.getId());
			prpLCourtAccident.setDutyNo(accidentInfo.getDutyNo());
			prpLCourtAccident.setAcciNo(accidentInfo.getAcciNo());
			prpLCourtAccident.setReportDate(DateUtils.strToDate(accidentInfo.getReportDate(),"yyyyMMdd HH:mm"));
			prpLCourtAccident.setAcciAddress(accidentInfo.getAcciAddress());
			prpLCourtAccident.setAcciResult(accidentInfo.getAcciResult());
			prpLCourtAccident.setAcciDescribe(accidentInfo.getAcciDescribe());
			prpLCourtAccident.setAcciType(accidentInfo.getAcciType());
			prpLCourtAccident.setDsfqk(accidentInfo.getDsfqk());
			prpLCourtAccident.setJbss(accidentInfo.getJbss());
			prpLCourtAccident.setBaryj(accidentInfo.getBaryj());
			prpLCourtAccident.setRdnr(accidentInfo.getRdnr());
			prpLCourtAccident.setWeather(accidentInfo.getWeather());
			prpLCourtAccident.setHighSpeed(accidentInfo.getHighSpeed());
			prpLCourtAccident.setDataSource(accidentInfo.getDataSource());
			courtMsgVo.setPrpLCourtAccident(prpLCourtAccident);
		}
		
		//当事人信息表
		if(resVo.getCaseinfo()!=null && resVo.getCaseinfo().getPartyInfo()!=null){
			List<PrpLCourtPartyVo> prpLCourtPartys = new ArrayList<PrpLCourtPartyVo>();
			for(CourtPartyInfo partyInfo:resVo.getCaseinfo().getPartyInfo()){
				PrpLCourtPartyVo partyVo = new PrpLCourtPartyVo();
				Beans.copy().from(partyInfo).excludeNull().to(partyVo);
				partyVo.setBirth(DateUtils.strToDate(partyInfo.getBirth(),"yyyyMMdd HH:mm"));
				partyVo.setPersonidtype(partyInfo.getPersonIDType());
				partyVo.setPersontype(partyInfo.getPersonType());
				partyVo.setCarno(partyInfo.getCarNo());
				partyVo.setSdaddr(partyInfo.getSdAddr());
				partyVo.setNediationnum(partyInfo.getNediationNum());
				partyVo.setPersonvehicle(partyInfo.getPersonVehicle());
				if(partyInfo.getApplicationMoney() != null){
					partyVo.setApplicationmoney(new BigDecimal(partyInfo.getApplicationMoney()));
				}
				if(partyInfo.getProtocolMoey() != null){
					partyVo.setProtocolmoey(new BigDecimal(partyInfo.getProtocolMoey()));
				}
				if(partyInfo.getJslxje() != null){
					partyVo.setJslxje(new BigDecimal(partyInfo.getJslxje()));
				}
				prpLCourtPartys.add(partyVo);
			}
			courtMsgVo.setPrpLCourtPartys(prpLCourtPartys);
		}
		
		//代理人信息表
		if(resVo.getCaseinfo()!=null && resVo.getCaseinfo().getAgentInfo()!=null){
			List<PrpLCourtAgentVo> prpLCourtAgents = new ArrayList<PrpLCourtAgentVo>();
			for(CourtAgentInfo agenInfo:resVo.getCaseinfo().getAgentInfo()){
				PrpLCourtAgentVo agentVo = new PrpLCourtAgentVo();
				Beans.copy().from(agenInfo).excludeNull().to(agentVo);
				prpLCourtAgents.add(agentVo);
			}
			courtMsgVo.setPrpLCourtAgents(prpLCourtAgents);
		}
		
		//鉴定信息表
		if(resVo.getCaseinfo()!=null && resVo.getCaseinfo().getIdentifyInfo()!=null){
			List<PrpLCourtIdentifyVo> prpLCourtIdentifys = new ArrayList<PrpLCourtIdentifyVo>();
			for(CourtIdentifyInfo identifyInfo:resVo.getCaseinfo().getIdentifyInfo()){
				PrpLCourtIdentifyVo identifyVo = new PrpLCourtIdentifyVo();
				Beans.copy().from(identifyInfo).excludeNull().to(identifyVo);
				identifyVo.setApplicantdate(DateUtils.strToDate(identifyInfo.getApplicantDate(),"yyyyMMdd HH:mm"));
				identifyVo.setRexeptiontime(DateUtils.strToDate(identifyInfo.getRexeptiontime(),"yyyyMMdd HH:mm"));
				identifyVo.setAppraisaldate(DateUtils.strToDate(identifyInfo.getAppraisalDate(),"yyyyMMdd HH:mm"));
				identifyVo.setAppraisalno(identifyInfo.getAppraisalNo());
				identifyVo.setAccidentno(identifyInfo.getAccidentNo());
				identifyVo.setAcciNo(identifyInfo.getAcciNo());
				identifyVo.setApplicantname(identifyInfo.getApplicantName());
				identifyVo.setAppraisal(identifyInfo.getAppraisal());
				if(identifyInfo.getAppraisalSum()!=null){
					identifyVo.setAppraisalsum(new BigDecimal(identifyInfo.getAppraisalSum()));
				}
				identifyVo.setAppraisaladdr(identifyInfo.getAppraisalAddr());
				identifyVo.setAppraiser(identifyInfo.getAppraiser());
				identifyVo.setBappraiser(identifyInfo.getbAppraiser());
				identifyVo.setAppraisalproj(identifyInfo.getAppraisalProj());
				identifyVo.setAppraisaltype(identifyInfo.getAppraisalType());
				identifyVo.setPresence(identifyInfo.getPresence());
				identifyVo.setCasesummary(identifyInfo.getCaseSummary());
				identifyVo.setMedicalsummary(identifyInfo.getMedicalSummary());
				identifyVo.setAnalyexplain(identifyInfo.getAnalyExplain());
				identifyVo.setAppraisalopinion(identifyInfo.getAppraisalOpinion());
				identifyVo.setScdj(identifyInfo.getScdj());
				identifyVo.setAppraisalrep(identifyInfo.getAppraisalrep());
				prpLCourtIdentifys.add(identifyVo);
			}
			courtMsgVo.setPrpLCourtIdentifys(prpLCourtIdentifys);
		}
		
		//调解信息表
		if(resVo.getCaseinfo()!=null && resVo.getCaseinfo().getMediationInfo()!=null){
			List<PrpLCourtMediationVo> prpLCourtMediations = new ArrayList<PrpLCourtMediationVo>();
			for(CourtMediationInfo mediationInfo:resVo.getCaseinfo().getMediationInfo()){
				PrpLCourtMediationVo mediationVo = new PrpLCourtMediationVo();
				Beans.copy().from(mediationInfo).excludeNull().to(mediationVo);
				mediationVo.setApplydate(DateUtils.strToDate(mediationInfo.getApplyDate(),"yyyyMMdd HH:mm"));
				mediationVo.setAcceptdate(DateUtils.strToDate(mediationInfo.getAcceptDate(),"yyyyMMdd HH:mm"));
				mediationVo.setMediationdate(DateUtils.strToDate(mediationInfo.getMediationDate(),"yyyyMMdd HH:mm"));
				mediationVo.setLxsx(DateUtils.strToDate(mediationInfo.getLxsx(),"yyyyMMdd HH:mm"));
				mediationVo.setMediationnum(mediationInfo.getMediationNum());
				mediationVo.setMediationtype(mediationInfo.getMediationType());
				mediationVo.setMediationaddr(mediationInfo.getMediationAddr());
				mediationVo.setIsappraisal(mediationInfo.getIsAppraisal());
				mediationVo.setIssfqr(mediationInfo.getIsSfqr());
				mediationVo.setDealresult(mediationInfo.getDealResult());
				mediationVo.setMediationcontent(mediationInfo.getMediationContent());
				prpLCourtMediations.add(mediationVo);				
			}
			courtMsgVo.setPrpLCourtMediations(prpLCourtMediations);
		}
		
		//赔偿信息表
		if(resVo.getCaseinfo()!=null && resVo.getCaseinfo().getCompensationInfo()!=null){
			List<PrpLCourtCompensationVo> prpLCourtCompensations = new ArrayList<PrpLCourtCompensationVo>();
			for(CourtCompensationInfo compensationInfo:resVo.getCaseinfo().getCompensationInfo()){
				PrpLCourtCompensationVo compensationVo = new PrpLCourtCompensationVo();
				Beans.copy().from(compensationInfo).excludeNull().to(compensationVo);
				compensationVo.setYlf(strToBigDecimal(compensationInfo.getYlf()));
				compensationVo.setZyhsbz(strToBigDecimal(compensationInfo.getZyhsbz()));
				compensationVo.setYyf(strToBigDecimal(compensationInfo.getYyf()));
				compensationVo.setHxzlf(strToBigDecimal(compensationInfo.getHxzlf()));
				compensationVo.setZrf(strToBigDecimal(compensationInfo.getZrf()));
				compensationVo.setHlf(strToBigDecimal(compensationInfo.getHlf()));
				compensationVo.setWgf(strToBigDecimal(compensationInfo.getWgf()));
				compensationVo.setCjpcj(strToBigDecimal(compensationInfo.getCjpcj()));
				compensationVo.setSwpcj(strToBigDecimal(compensationInfo.getSwpcj()));
				compensationVo.setSzf(strToBigDecimal(compensationInfo.getSzf()));
				compensationVo.setJswgf(strToBigDecimal(compensationInfo.getJswgf()));
				compensationVo.setJsjtf(strToBigDecimal(compensationInfo.getJsjtf()));
				compensationVo.setJszsf(strToBigDecimal(compensationInfo.getJszsf()));
				compensationVo.setBfyrshf(strToBigDecimal(compensationInfo.getBfyrshf()));
				compensationVo.setJsshfwj(strToBigDecimal(compensationInfo.getJsshfwj()));
				compensationVo.setCjfzqjf(strToBigDecimal(compensationInfo.getCjfzqjf()));
				compensationVo.setJtf(strToBigDecimal(compensationInfo.getJtf()));
				compensationVo.setYwssf(strToBigDecimal(compensationInfo.getYwssf()));
				compensationVo.setClsjf(strToBigDecimal(compensationInfo.getClsjf()));
				compensationVo.setClxlf(strToBigDecimal(compensationInfo.getClxlf()));
				compensationVo.setPgf(strToBigDecimal(compensationInfo.getPgf()));
				compensationVo.setCwssf(strToBigDecimal(compensationInfo.getCwssf()));
				compensationVo.setJdf(strToBigDecimal(compensationInfo.getJdf()));
				compensationVo.setZyypf(strToBigDecimal(compensationInfo.getZyypf()));
				compensationVo.setZsf(strToBigDecimal(compensationInfo.getZsf()));
				compensationVo.setTyssf(strToBigDecimal(compensationInfo.getTyssf()));
				compensationVo.setClbsf(strToBigDecimal(compensationInfo.getClbsf()));
				compensationVo.setLsdlf(strToBigDecimal(compensationInfo.getLsdlf()));
				prpLCourtCompensations.add(compensationVo);
			}
			courtMsgVo.setPrpLCourtCompensations(prpLCourtCompensations);
		}
		
		//网上立案信息表
		if(resVo.getCaseinfo()!=null && resVo.getCaseinfo().getOnlineRegistrationInfo()!=null){
			List<PrpLCourtClaimVo> prpLCourtClaims = new ArrayList<PrpLCourtClaimVo>();
			for(CourtOnlineRegistrationInfo vo:resVo.getCaseinfo().getOnlineRegistrationInfo()){
				PrpLCourtClaimVo claimVo = new PrpLCourtClaimVo();
				Beans.copy().from(vo).excludeNull().to(claimVo);
				claimVo.setLadate(DateUtils.strToDate(vo.getLadate(),"yyyyMMdd HH:mm"));
				claimVo.setIssqtj(vo.getIsSqtj());
				prpLCourtClaims.add(claimVo);
			}
			courtMsgVo.setPrpLCourtClaims(prpLCourtClaims);
		}
		
		//诉前调解表
		if(resVo.getCaseinfo()!=null && resVo.getCaseinfo().getLitigationMediationInfo()!=null){
			List<PrpLCourtLitigationVo> prpLCourtLitigations = new ArrayList<PrpLCourtLitigationVo>();
			for(CourtLitigationMediationInfo litigationInfo:resVo.getCaseinfo().getLitigationMediationInfo()){
				PrpLCourtLitigationVo litigationVo = new PrpLCourtLitigationVo();
				Beans.copy().from(litigationInfo).excludeNull().to(litigationVo);
				litigationVo.setJrtjrq(DateUtils.strToDate(litigationInfo.getJrtjrq(),"yyyyMMdd HH:mm"));
				litigationVo.setTjwcrq(DateUtils.strToDate(litigationInfo.getTjwcrq(),"yyyyMMdd HH:mm"));
				litigationVo.setQsrq(DateUtils.strToDate(litigationInfo.getQsrq(),"yyyyMMdd HH:mm"));
				litigationVo.setJzrq(DateUtils.strToDate(litigationInfo.getJzrq(),"yyyyMMdd HH:mm"));
				litigationVo.setSqrq(DateUtils.strToDate(litigationInfo.getSqrq(),"yyyyMMdd HH:mm"));
				litigationVo.setSlrq(DateUtils.strToDate(litigationInfo.getSlrq(),"yyyyMMdd HH:mm"));
				prpLCourtLitigations.add(litigationVo);
			}
			courtMsgVo.setPrpLCourtLitigations(prpLCourtLitigations);
		}
		
		//案件基本信息表
		if(resVo.getCaseinfo()!=null && resVo.getCaseinfo().getRegistInfo()!=null){
			PrpLCourtRegistVo prpLCourtRegist = new PrpLCourtRegistVo();
			CourtRegistInfo registInfo = resVo.getCaseinfo().getRegistInfo();
			Beans.copy().from(registInfo).excludeNull().to(prpLCourtRegist);
			prpLCourtRegist.setId(courtMsgVo.getId());
			prpLCourtRegist.setSqje(strToBigDecimal(registInfo.getSqje()));
			prpLCourtRegist.setSjrq(DateUtils.strToDate(registInfo.getSjrq(),"yyyyMMdd HH:mm"));
			prpLCourtRegist.setYsslf(strToBigDecimal(registInfo.getYsslf()));
			prpLCourtRegist.setSsrq(DateUtils.strToDate(registInfo.getSsrq(),"yyyyMMdd HH:mm"));
			prpLCourtRegist.setLarq(DateUtils.strToDate(registInfo.getLarq(),"yyyyMMdd HH:mm"));
			prpLCourtRegist.setKttime(DateUtils.strToDate(registInfo.getKtTime(),"yyyyMMdd HH:mm"));
			courtMsgVo.setPrpLCourtRegist(prpLCourtRegist);
		}
		
		//司法确认信息表
		if(resVo.getCaseinfo()!=null && resVo.getCaseinfo().getJudicialconfirmInfo()!=null){
			List<PrpLCourtConfirmVo> prpLCourtConfirms = new ArrayList<PrpLCourtConfirmVo>();
			for(CourtJudicialConfirmInfo confirmInfo:resVo.getCaseinfo().getJudicialconfirmInfo()){
				PrpLCourtConfirmVo confirmVo = new PrpLCourtConfirmVo();
				Beans.copy().from(confirmInfo).excludeNull().to(confirmVo);
				confirmVo.setSqsj(DateUtils.strToDate(confirmInfo.getSqsj(),"yyyyMMdd HH:mm"));
				confirmVo.setSfqrkssj(DateUtils.strToDate(confirmInfo.getSfqrkssj(),"yyyyMMdd HH:mm"));
				confirmVo.setSfqrjzsj(DateUtils.strToDate(confirmInfo.getSfqrjzsj(),"yyyyMMdd HH:mm"));
				prpLCourtConfirms.add(confirmVo);
			}
			courtMsgVo.setPrpLCourtConfirms(prpLCourtConfirms);
		}
		
		//文件信息表
		if(resVo.getCaseinfo()!=null && resVo.getCaseinfo().getFileInfo()!=null){
			List<PrpLCourtFileVo> prpLCourtFiles = new ArrayList<PrpLCourtFileVo>();
			for(CourtFileInfo fileInfo:resVo.getCaseinfo().getFileInfo()){
				PrpLCourtFileVo fileVo = new PrpLCourtFileVo();
				Beans.copy().from(fileInfo).excludeNull().to(fileVo);
				fileVo.setScsj(DateUtils.strToDate(fileInfo.getScsj(),"yyyyMMdd HH:mm"));
				fileVo.setCaseno(fileInfo.getCaseNo());
				fileVo.setDsrno(fileInfo.getDsrNo());
				prpLCourtFiles.add(fileVo);
			}
			courtMsgVo.setPrpLCourtFiles(prpLCourtFiles);
		}
		
	}
	
	private BigDecimal strToBigDecimal(String param){
		if(StringUtils.isNotEmpty(param)){
			return new BigDecimal(param);
		}else{
			return null;
		}
	}


	@Override
	public void saveByCourtMessage(PrpLCourtMessageVo prpLCourtMessageVo) {
		if(prpLCourtMessageVo != null){
			PrpLCourtMessage prpLCourtMessage = new PrpLCourtMessage();
			Beans.copy().from(prpLCourtMessageVo).to(prpLCourtMessage);
			dataBaseDao.save(PrpLCourtMessage.class,prpLCourtMessage);
		}
	}
	
	public void saveCourtMessage(PrpLCourtMessageVo prpLCourtMessageVo) {
		
		PrpLCourtMessage prpLCourtMessage = Beans.copyDepth().from(prpLCourtMessageVo).to(PrpLCourtMessage.class);
		
		//维护主子表关系
		if(prpLCourtMessage.getPrpLCourtAccident() != null){
			prpLCourtMessage.getPrpLCourtAccident().setPrpLCourtMessage(prpLCourtMessage);
		}
		if(prpLCourtMessage.getPrpLCourtPartys()!=null && prpLCourtMessage.getPrpLCourtPartys().size()>0){
			for(PrpLCourtParty po:prpLCourtMessage.getPrpLCourtPartys()){
				po.setPrpLCourtMessage(prpLCourtMessage);
			}
		}
		if(prpLCourtMessage.getPrpLCourtAgents()!=null && prpLCourtMessage.getPrpLCourtAgents().size()>0){
			for(PrpLCourtAgent po:prpLCourtMessage.getPrpLCourtAgents()){
				po.setPrpLCourtMessage(prpLCourtMessage);
			}
		}
		if(prpLCourtMessage.getPrpLCourtIdentifys()!=null && prpLCourtMessage.getPrpLCourtIdentifys().size()>0){
			for(PrpLCourtIdentify po:prpLCourtMessage.getPrpLCourtIdentifys()){
				po.setPrpLCourtMessage(prpLCourtMessage);
			}
		}
		if(prpLCourtMessage.getPrpLCourtMediations()!=null && prpLCourtMessage.getPrpLCourtMediations().size()>0){
			for(PrpLCourtMediation po:prpLCourtMessage.getPrpLCourtMediations()){
				po.setPrpLCourtMessage(prpLCourtMessage);
			}
		}
		if(prpLCourtMessage.getPrpLCourtCompensations()!=null && prpLCourtMessage.getPrpLCourtCompensations().size()>0){
			for(PrpLCourtCompensation po:prpLCourtMessage.getPrpLCourtCompensations()){
				po.setPrpLCourtMessage(prpLCourtMessage);
			}
		}
		if(prpLCourtMessage.getPrpLCourtClaims()!=null && prpLCourtMessage.getPrpLCourtClaims().size()>0){
			for(PrpLCourtClaim po:prpLCourtMessage.getPrpLCourtClaims()){
				po.setPrpLCourtMessage(prpLCourtMessage);
			}
		}
		if(prpLCourtMessage.getPrpLCourtLitigations()!=null && prpLCourtMessage.getPrpLCourtLitigations().size()>0){
			for(PrpLCourtLitigation po:prpLCourtMessage.getPrpLCourtLitigations()){
				po.setPrpLCourtMessage(prpLCourtMessage);
			}
		}
		if(prpLCourtMessage.getPrpLCourtRegist()!=null){
			prpLCourtMessage.getPrpLCourtRegist().setPrpLCourtMessage(prpLCourtMessage);
		}
		if(prpLCourtMessage.getPrpLCourtConfirms()!=null && prpLCourtMessage.getPrpLCourtConfirms().size()>0){
			for(PrpLCourtConfirm po:prpLCourtMessage.getPrpLCourtConfirms()){
				po.setPrpLCourtMessage(prpLCourtMessage);
			}
		}
		if(prpLCourtMessage.getPrpLCourtFiles()!=null && prpLCourtMessage.getPrpLCourtFiles().size()>0){
			for(PrpLCourtFile po:prpLCourtMessage.getPrpLCourtFiles()){
				po.setPrpLCourtMessage(prpLCourtMessage);
			}
		}
		
		dataBaseDao.clear();
		dataBaseDao.update(PrpLCourtMessage.class,prpLCourtMessage);
		
	}
	
	@Override
	public List<PrpLCourtMessageVo> searchPrpLCourtMessageVo(String status,String maxTimes){
		
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("retCode", status);
		if(StringUtils.isNotEmpty(maxTimes)){
			queryRule.addLessThan("executeTimes", Integer.valueOf(maxTimes));
		}
		List<PrpLCourtMessage> prpLCourtMessageList = dataBaseDao.findAll(PrpLCourtMessage.class, queryRule);
		if(prpLCourtMessageList!=null && prpLCourtMessageList.size()>0){
			List<PrpLCourtMessageVo> prpLCourtMessageVoList = new ArrayList<PrpLCourtMessageVo>();
			for(int i=0;i<prpLCourtMessageList.size();i++){
				PrpLCourtMessageVo prpLCourtMessageVo = new PrpLCourtMessageVo();
				Beans.copy().from(prpLCourtMessageList.get(i)).to(prpLCourtMessageVo);
				prpLCourtMessageVoList.add(prpLCourtMessageVo);
			}
			return prpLCourtMessageVoList;
		}
		return null;
	}


	@Override
	public PrpLCourtMessageVo findCourtMessage(String registNo, int status) {
		// TODO Auto-generated method stub
		PrpLCourtMessageVo courtMessageVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("caseNo", registNo);
		queryRule.addDescOrder("executeTime");
		queryRule.addDescOrder("retCode");
		List<PrpLCourtMessage> courtMessages =  dataBaseDao.findTop(PrpLCourtMessage.class, queryRule, 1);
		if(courtMessages != null && courtMessages.size()>0){
			PrpLCourtMessage courtMessage = courtMessages.get(0);
			courtMessageVo = Beans.copyDepth().from(courtMessage).to(PrpLCourtMessageVo.class);
			return courtMessageVo;
		} 
		return null;
	}


	@Override
	public PrpLCourtPartyVo findCourtParty(Long id) {
		PrpLCourtPartyVo  prpLCourtPartyVo = null;
		PrpLCourtParty prpLCourtParty = dataBaseDao.findByPK(PrpLCourtParty.class,id);
		if(prpLCourtParty != null){
			prpLCourtPartyVo = new PrpLCourtPartyVo();
			Beans.copy().from(prpLCourtParty).to(prpLCourtPartyVo);
		}
		return prpLCourtPartyVo;
	}


	@Override
	public PrpLCourtIdentifyVo findCourtIdentify(Long id) {
		PrpLCourtIdentifyVo  prpLCourtIdenVo = null;
		PrpLCourtIdentify prplCourtIdentify = dataBaseDao.findByPK(PrpLCourtIdentify.class,id);
		if(prplCourtIdentify != null){
			prpLCourtIdenVo = new PrpLCourtIdentifyVo();
			Beans.copy().from(prplCourtIdentify).to(prpLCourtIdenVo);
		}
		return prpLCourtIdenVo;
	}


	@Override
	public PrpLCourtCompensationVo findCourtCompensate(Long id) {
		PrpLCourtCompensationVo  prpLCourtCompensationVo = null;
		PrpLCourtCompensation prplCourtCompensation = dataBaseDao.findByPK(PrpLCourtCompensation.class,id);
		if(prplCourtCompensation != null){
			prpLCourtCompensationVo = new PrpLCourtCompensationVo();
			Beans.copy().from(prplCourtCompensation).to(prpLCourtCompensationVo);
		}
		return prpLCourtCompensationVo;
	}


	@Override
	public PrpLCourtClaimVo findCourtClaim(Long id) {
		PrpLCourtClaimVo  prpLCourtClaimVo = null;
		PrpLCourtClaim prpLCourtClaim = dataBaseDao.findByPK(PrpLCourtClaim.class,id);
		if(prpLCourtClaim != null){
			prpLCourtClaimVo = new PrpLCourtClaimVo();
			Beans.copy().from(prpLCourtClaim).to(prpLCourtClaimVo);
		}
		return prpLCourtClaimVo;
	}


	@Override
	public PrpLCourtLitigationVo findCourtLitigation(Long id) {
		PrpLCourtLitigationVo  prpLCourtLitigationVo = null;
		PrpLCourtLitigation prpLCourtLitigation = dataBaseDao.findByPK(PrpLCourtLitigation.class,id);
		if(prpLCourtLitigation != null){
			prpLCourtLitigationVo = new  PrpLCourtLitigationVo();
			Beans.copy().from(prpLCourtLitigation).to(prpLCourtLitigationVo);
		}
		return prpLCourtLitigationVo;
	}


	@Override
	public PrpLCourtConfirmVo findCourtConfirm(Long id) {
		PrpLCourtConfirmVo  prpLCourtConfirmVo = null;
		PrpLCourtConfirm prpLCourtConfirm = dataBaseDao.findByPK(PrpLCourtConfirm.class,id);
		if(prpLCourtConfirm != null){
			prpLCourtConfirmVo = new  PrpLCourtConfirmVo();
			Beans.copy().from(prpLCourtConfirm).to(prpLCourtConfirmVo);
		}
		return prpLCourtConfirmVo;
	}


	@Override
	public PrpLCourtMediationVo findCourtMediation(Long id) {
		PrpLCourtMediationVo  prpLCourtMediationVo = null;
		PrpLCourtMediation prpLCourtMediation = dataBaseDao.findByPK(PrpLCourtMediation.class,id);
		if(prpLCourtMediation != null){
			prpLCourtMediationVo = new  PrpLCourtMediationVo();
			Beans.copy().from(prpLCourtMediation).to(prpLCourtMediationVo);
		}
		return prpLCourtMediationVo;
	}


}
