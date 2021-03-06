package ins.sino.claimcar.policeSz.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.ScheduleType;
import ins.sino.claimcar.base.polisceSZ.po.PrpLAccidentResInfoPo;
import ins.sino.claimcar.base.polisceSZ.po.PrpLDispatchInfo;
import ins.sino.claimcar.base.polisceSZ.po.PrpLPhotoDataPo;
import ins.sino.claimcar.base.polisceSZ.po.PrpLPlateDataPo;
import ins.sino.claimcar.carinterface.po.ClaimInterfaceLog;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.CheckActionVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneBodyReq;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneHeadReq;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneOutdateReq;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneReqVo;
import ins.sino.claimcar.regist.service.FounderCustomService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.hnbxrest.vo.CaseimageVo;
import ins.sino.claimcar.hnbxrest.vo.SubmitcaseimageinforVo;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.ScheduleHandlerService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.sunyardimage.vo.common.BaseDataVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqBatchVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqFatherNodeVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqImageBaseRootVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqMetaDataVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqPageNodeVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqPageVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqPagesVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqParameterVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqPhotoVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqSonNodeVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqVtreeVo;
import ins.sino.claimcar.trafficplatform.service.SzpoliceCaseService;
import ins.sino.claimcar.trafficplatform.vo.AccidentResInfo;
import ins.sino.claimcar.trafficplatform.vo.DispatchInfo;
import ins.sino.claimcar.trafficplatform.vo.PhotoData;
import ins.sino.claimcar.trafficplatform.vo.PlateData;
import ins.sino.claimcar.trafficplatform.vo.PrpLAccidentResInfoPoVO;
import ins.sino.claimcar.trafficplatform.vo.SdSzResponseVo;
import ins.sino.claimcar.xydImageUpload.service.XydImageUploadAllMethodService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.Path;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.sinosoft.image.action.ImgRemoteFileAction;
import com.sinosoft.image.util.ImgCommUtils;
import com.sinosoft.image.vo.ImgUploadVo;
import com.tc.qrpay.openapi.sdk.OpenApiClient;
import com.tc.qrpay.openapi.sdk.protocol.OpenApiReq;
import com.tc.qrpay.openapi.sdk.protocol.OpenApiRsp;

@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" }, timeout = 1000 * 60 * 10)
@Path("szpoliceCaseService")
public class SzpoliceCaseServiceImpl implements SzpoliceCaseService {
	private static Logger logger = LoggerFactory.getLogger(SzpoliceCaseServiceImpl.class);
	@Autowired
	private DatabaseDao databaseDao;    
	@Autowired
    BaseDaoService baseDaoService;
	@Autowired
	private RegistService registService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	ClaimInterfaceLogService interfaceLogService;
	@Autowired
	private ScheduleHandlerService scheduleHandlerService;
	@Autowired
	private ScheduleTaskService scheduleTaskService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	SysUserService sysUserService;
	@Autowired
	FounderCustomService founderService;
	@Autowired
    private InterfaceAsyncService interfaceAsyncService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	CheckHandleService checkHandleService;

	@Override
	public AccidentResInfo findAccidentResInfoByRegistNo(String registNo) {
		QueryRule query=QueryRule.getInstance();
		query.addEqual("registNo",registNo);
		List<PrpLAccidentResInfoPo> pos=databaseDao.findAll(PrpLAccidentResInfoPo.class, query);
		AccidentResInfo vo=null;
		if(pos!=null && pos.size()>0){
			vo=new AccidentResInfo();
			Beans.copy().from(pos.get(0)).to(vo);
		}
		return vo;
	}

	@Override
	public void savePrpLAccidentResInfoPo(List<AccidentResInfo> infoVoList){
		List<PrpLAccidentResInfoPo> infoPoList=new ArrayList<PrpLAccidentResInfoPo>();
		infoPoList = Beans.copyDepth().from(infoVoList).toList(PrpLAccidentResInfoPo.class);
		if(infoVoList != null && infoVoList.size() > 0){
			for(PrpLAccidentResInfoPo po : infoPoList){
				po.setCreateTime(new Date());
				po.setUpdateTime(new Date());
				if(po.getPlateData() != null && po.getPlateData().size() > 0){
					for(PrpLPlateDataPo plateData : po.getPlateData()){
						plateData.setPrpLAccidentResInfoPo(po);
						plateData.setCreateTime(new Date());
						plateData.setUpdateTime(new Date());
					}
				}
				if(po.getPhotoData() != null && po.getPhotoData().size() > 0){
					for(PrpLPhotoDataPo photoData : po.getPhotoData()){
						photoData.setPrpLAccidentResInfoPo(po);
						photoData.setCreateTime(new Date());
						photoData.setUpdateTime(new Date());
					}
				}
			}
			databaseDao.saveAll(PrpLAccidentResInfoPo.class,infoPoList);
		}
	}




	@Override
	public String findAccidentInfoByPlateNoAndTime(
			String plateNo, String accidentTime,String comCode,String types) {
		List<Object[]> paramValues = new ArrayList<Object[]>();
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		String bussNo = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date accidentDate = null;
		try {
			accidentDate = formatter.parse(accidentTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance(); 
		c.setTime(accidentDate); // ????????????
		c.add(Calendar.HOUR,12); // ?????????12??????
	    Date timeEnd = new Date();
	    timeEnd = c.getTime();
	    
		c.setTime(timeEnd); // ????????????
		c.add(Calendar.DATE, -1); // ?????????1???
	    Date timeStart = new Date();
	    timeStart = c.getTime();
		if("1".equals(types)){
			sqlUtil.append(" SELECT REGIST.REGISTNO FROM PRPLREGIST REGIST WHERE 1=1 AND REGIST.ACCIDENTNO IS NULL");
			sqlUtil.append(" AND exists  (select  1 from PRPLREGISTCARLOSS LOSS where LOSS.REGISTNO = REGIST.REGISTNO AND LOSS.LOSSPARTY = 1 AND LOSS.LICENSENO = ? )");

			sqlUtil.addParamValue(plateNo);
			if(StringUtils.isNotBlank(comCode)){
				sqlUtil.append(" AND REGIST.COMCODE LIKE ?");
				sqlUtil.addParamValue(comCode+"%");
			}
			sqlUtil.append(" and (REGIST.DAMAGETIME >= ? and REGIST.DAMAGETIME <= ?) ");
			sqlUtil.addParamValue(timeStart);
			sqlUtil.addParamValue(timeEnd);
			sqlUtil.append(" ORDER BY REGIST.DAMAGETIME DESC");
			paramValues = baseDaoService.getAllBySql(sqlUtil.getSql(),sqlUtil.getParamValues());
			logger.info("Sql=1========"+sqlUtil.getSql()+"Values"+plateNo+comCode+timeStart+timeEnd);
			if(paramValues != null && paramValues.size() > 0){
				for(Object obj : paramValues){
					bussNo = obj ==null ? "" : obj.toString();
					Boolean notCase = claimService.findNotCaseByRegistNo(bussNo);
					if(notCase){// ?????????
						break;
					}else{// ??????
						bussNo = "";
					}
				}
			}
		}else if("0".equals(types)){
			sqlUtil.append(" SELECT ACCIDENTNO FROM PRPLACCIDENTRESINFOPO ACCIDENTRESINFO,PRPLPLATEDATAPO PLATEDATA WHERE 1=1");
			sqlUtil.append(" AND ACCIDENTRESINFO.ACCIDENTNO = PLATEDATA.ACCIDENTID AND ACCIDENTRESINFO.REGISTNO IS NULL");
			sqlUtil.append(" AND PLATEDATA.plateNo = ?");
			sqlUtil.addParamValue(plateNo);
			sqlUtil.append(" and (ACCIDENTRESINFO.accidentDate >= ? and ACCIDENTRESINFO.accidentDate <= ?) ");
			sqlUtil.addParamValue(timeStart);
			sqlUtil.addParamValue(timeEnd);
			sqlUtil.append(" ORDER BY ACCIDENTRESINFO.accidentDate DESC");
			paramValues = baseDaoService.getAllBySql(sqlUtil.getSql(),sqlUtil.getParamValues());
			logger.info("Sql=02========"+sqlUtil.getSql()+"Values"+plateNo+comCode+timeStart+timeEnd);
			if(paramValues != null && paramValues.size() > 0){
				for(Object obj : paramValues){
					bussNo = obj ==null ? "" : obj.toString();
					break;
				}
			}
		}
		return bussNo;
	}




	@Override
	public AccidentResInfo findAccidentResInfoByAccidentNo(String accidentNo) {
		PrpLAccidentResInfoPo accidentResInfoPo = databaseDao.findByPK(PrpLAccidentResInfoPo.class, accidentNo);
		AccidentResInfo accidentResInfo = null;
		accidentResInfo = Beans.copyDepth().from(accidentResInfoPo).to(AccidentResInfo.class);
		return accidentResInfo;
	}




	@Override
	public void updateAccidentResInfo(AccidentResInfo infoVo) {
		PrpLAccidentResInfoPo po = Beans.copyDepth().from(infoVo).to(PrpLAccidentResInfoPo.class);
		po.setUpdateTime(new Date());
		if(po.getPlateData() != null && po.getPlateData().size() > 0){
			for(PrpLPlateDataPo plateData : po.getPlateData()){
				plateData.setPrpLAccidentResInfoPo(po);
				plateData.setUpdateTime(new Date());
			}
		}
		if(po.getPhotoData() != null && po.getPhotoData().size() > 0){
			for(PrpLPhotoDataPo photoData : po.getPhotoData()){
				photoData.setPrpLAccidentResInfoPo(po);
				photoData.setUpdateTime(new Date());
			}
		}
		databaseDao.update(PrpLAccidentResInfoPo.class, po);
	}
	

	@Override
	public void accidentInfoDownLoad() {
		String gateway = SpringProperties.getProperty("SZWARN_URL");
		String appId = SpringProperties.getProperty("SZWARN_APPID");
		String appKey = SpringProperties.getProperty("SZWARN_APPKEY");
		String method = SpringProperties.getProperty("SZWARN_METHOD");
		String insuranceCode = SpringProperties.getProperty("SZWARN_INSURANCECODE");
		boolean isDataEncrypt = true; // ????????????
		boolean isSign = true; // ??????
        JSONObject json = new JSONObject();
		json.put("insuranceCode",insuranceCode);// ????????????????????????????????????????????????4.2????????????????????????
        String data = json.toString();
        
		try {
			// ??????????????????
	        OpenApiClient openApiClient = new OpenApiClient(gateway, appId, appKey, isDataEncrypt, isSign);
			OpenApiReq req = openApiClient.genReqJson(method, data);
	        OpenApiRsp rsp = openApiClient.execute(req);
			logger.info("????????????rsp============================="+rsp.toJsonString());
	        JSONObject rejson = JSONObject.fromObject(rsp);
			SdSzResponseVo sdSzResponseVo = (SdSzResponseVo) JSONObject.toBean(rejson, SdSzResponseVo.class);
			logger.info("????????????============================="+sdSzResponseVo.getData());
			String resData = sdSzResponseVo.getData();
            if(StringUtils.isNotEmpty(resData)){
            	Map<String, Class> classMap = new HashMap<String, Class>();
            	classMap.put("plateData", PlateData.class);
            	classMap.put("photoData", PhotoData.class);
    			List<AccidentResInfo> accidentResInfos = JSONArray.toList(JSONArray.fromObject(resData), AccidentResInfo.class,classMap);
    			
    			List<AccidentResInfo> accidentResInfoList = new ArrayList<AccidentResInfo>();
    			Map<String, AccidentResInfo> hashMap = new HashMap<String, AccidentResInfo>();
    			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    			if(accidentResInfos != null && accidentResInfos.size() > 0){
    				for(AccidentResInfo accidentResInfo : accidentResInfos){
    					if(accidentResInfo.getAccidentTime() != null){
    						Date accidentDate = formatter.parse(accidentResInfo.getAccidentTime());
    						accidentResInfo.setAccidentDate(accidentDate);
    					}
    					String accidentNo = accidentResInfo.getAccidentNo();
						// ??????????????????AccidentResInfo
    					if(hashMap.size() > 0){
							if( !hashMap.containsKey(accidentNo)){// ??????
								AccidentResInfo resInfo = this.findAccidentResInfoByAccidentNo(accidentNo);
								if(resInfo == null){
									hashMap.put(accidentNo, accidentResInfo);
									accidentResInfoList.add(accidentResInfo);
								}
							}
						}else{
    						AccidentResInfo resInfo = this.findAccidentResInfoByAccidentNo(accidentNo);
    						if(resInfo == null){
    							hashMap.put(accidentNo, accidentResInfo);
    							accidentResInfoList.add(accidentResInfo);
    						}
    					}
    				}
    			}
    			HashMap registNoAccidentNoMap= new HashMap<String,String>();
				// ??????AccidentResInfo
    			this.savePrpLAccidentResInfoPo(accidentResInfoList);
    			if(accidentResInfos != null && accidentResInfos.size() > 0){
    				for(AccidentResInfo accidentResInfo : accidentResInfos){
    					for(PlateData plateData : accidentResInfo.getPlateData()){
							// ???????????????????????????????????????12???????????????????????????????????????????????????????????????????????????????????????????????????
    						String registNo = this.findAccidentInfoByPlateNoAndTime(plateData.getPlateNo(), accidentResInfo.getAccidentTime(),"0002","1");
    						if(StringUtils.isNotBlank(registNo)){
    							PrpLAccidentResInfoPo resInfo = this.findAccidentInfoPoByAccidentNo(accidentResInfo.getAccidentNo());
        						resInfo.setRegistNo(registNo);
        						resInfo.setUpdateTime(new Date());
    							PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
        						prpLRegistVo.setAccidentNo(accidentResInfo.getAccidentNo());
        						prpLRegistVo.setUpdateTime(new Date());
								registService.saveOrUpdate(prpLRegistVo);// ??????AccidentNo???PrpLRegist
								registNoAccidentNoMap.put(accidentResInfo.getAccidentNo(), registNo);
    						}
    					}
    				}
    			}
    			long  time = new Date().getTime();
    			logger.info("????????????????????????=============================??????"+time);
    			this.dowmloadAcciPhoto(accidentResInfoList,registNoAccidentNoMap);
    			logger.info("????????????????????????=============================??????,?????????"+(new Date().getTime()-time));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public PrpLAccidentResInfoPo findAccidentInfoPoByAccidentNo(String accidentNo) {
		PrpLAccidentResInfoPo accidentResInfoPo = databaseDao.findByPK(PrpLAccidentResInfoPo.class, accidentNo);
		return accidentResInfoPo;
	}

	@Override
	public Map<String,Long> findClaimInterfaceLog() {
		QueryRule query=QueryRule.getInstance();
		Date nowDate=new Date();
		Calendar dar=Calendar.getInstance();
		dar.setTime(nowDate);
		dar.add(Calendar.DATE, -1);// ???????????????
		Date Bdate=dar.getTime();//
		List<String> strList=new ArrayList<String>();
		Map<String,Long> map=new HashMap<String,Long>();//key-claimNo,value-id
		Set<String> set = new HashSet<String>();// ??????
		strList.add("SZClaim_BI");
		strList.add("SZClaim_CI");
		query.addIn("businessType",strList);
		query.addEqual("status","0");
		query.addBetween("createTime",Bdate,nowDate);
		List<ClaimInterfaceLog> loglists=databaseDao.findAll(ClaimInterfaceLog.class,query);
		if(loglists!=null && loglists.size()>0){
			for(ClaimInterfaceLog log:loglists){
				if(set.add(log.getClaimNo())){
					map.put(log.getClaimNo(),log.getId());
				}
				
			}
		}
		
		return map;
	}

	@Override
	public void dispatchInfoDownload() {
		String gateway = SpringProperties.getProperty("SZWARN_URL");
		String appId = SpringProperties.getProperty("SZWARN_APPID");
		String appKey = SpringProperties.getProperty("SZWARN_APPKEY");
		String method = SpringProperties.getProperty("SZWARN_DISPATCHINFODOWNLOAD");
		String insuranceCode = SpringProperties.getProperty("SZWARN_INSURANCECODE");
		boolean isDataEncrypt = true; // ????????????
		boolean isSign = true; // ??????
		JSONObject json = new JSONObject();
		json.put("insuranceCode",insuranceCode);// ????????????????????????????????????????????????4.2????????????????????????
		String data = json.toString();
		
		try{
			OpenApiClient openApiClient = new OpenApiClient(gateway, appId, appKey, isDataEncrypt, isSign);
			OpenApiReq req = openApiClient.genReqJson(method, data);
			OpenApiRsp rsp = openApiClient.execute(req);
			logger.info("dispatchInfoDownload===????????????????????????????????????????????????=============================" + rsp.toJsonString());
			JSONObject rejson = JSONObject.fromObject(rsp);
			SdSzResponseVo sdSzResponseVo = (SdSzResponseVo) JSONObject.toBean(rejson, SdSzResponseVo.class);
			String resData = sdSzResponseVo.getData();
			
			logger.info("dispatchInfoDownload===??????????????????????????????????????????data??????=======" + resData);
			// ????????????
			if (StringUtils.isNotEmpty(resData)) {
				List<DispatchInfo> dispatchInfoList = JSONArray.toList(JSONArray.fromObject(resData), DispatchInfo.class);
				if (dispatchInfoList != null && dispatchInfoList.size() > 0) {
					List<PrpLDispatchInfo> prpLDispatchInfoList = new ArrayList<PrpLDispatchInfo>();
					for (DispatchInfo dispatchInfo : dispatchInfoList) {
						PrpLDispatchInfo prpLDispatchInfo = new PrpLDispatchInfo();
						Beans.copy().from(dispatchInfo).to(prpLDispatchInfo);
						prpLDispatchInfo.setRegistNo(dispatchInfo.getReportNo());
						prpLDispatchInfo.setReceiveTime(DateUtils.strToDate(dispatchInfo.getReceiveTime()));
						prpLDispatchInfoList.add(prpLDispatchInfo);
						// ??????????????????????????????????????????????????????
						tryToGenerateSurveyTasks(dispatchInfo);
					}
					databaseDao.saveAll(PrpLDispatchInfo.class, prpLDispatchInfoList);
				}
			}
		}catch(Exception e){
			logger.error("?????????????????????????????????????????????"+e);
		}
	}
	
	public void tryToGenerateSurveyTasks(DispatchInfo dispatchInfo) {
		if (dispatchInfo == null) {
			logger.info("dispatchInfoDownload()---?????????????????????????????????????????????????????? ????????????????????????????????????????????????");
		} else {
			try {// ?????????????????????????????????
				if (CodeConstants.DHICCODE.equals(dispatchInfo.getSurveyorCo()) || CodeConstants.DHICNAME.equals(dispatchInfo.getSurveyorCo())) {
					// ????????????????????????
					List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findPrpWfTaskVo(dispatchInfo.getReportNo(), FlowNode.Check.name(), FlowNode.Chk.name());
					if(prpLWfTaskVoList != null && !prpLWfTaskVoList.isEmpty()){
						logger.info("tryToGenerateSurveyTasks()--- ???????????????????????????????????????????????????????????????????????????"+ dispatchInfo.getReportNo() +"?????????????????????");
					} else {
						logger.info("autoGenerateSurveyTasks()--- "+ dispatchInfo.getReportNo() +" ---????????????????????????????????????????????????????????????---");
						autoGenerateSurveyTasks(dispatchInfo);
						logger.info("autoGenerateSurveyTasks()--- "+ dispatchInfo.getReportNo() +" ---????????????????????????????????????????????????????????????---");
					}
				}
			} catch (Exception e) {

				logger.info("tryToGenerateSurveyTasks()---???????????????????????????????????????????????????????????????" + e.getStackTrace());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void warningInstanceDownload() {

		String gateway = SpringProperties.getProperty("SZWARN_URL");
		String appId = SpringProperties.getProperty("SZWARN_APPID");
		String appKey = SpringProperties.getProperty("SZWARN_APPKEY");
		String method = SpringProperties.getProperty("SZWARN_WARNINGINSTANCEDOWNLOAD");
		String insuranceCode = SpringProperties.getProperty("SZWARN_INSURANCECODE");
		boolean isDataEncrypt = true; // ????????????
		boolean isSign = true; // ??????
		JSONObject json = new JSONObject();
		json.put("insuranceCode",insuranceCode);// ????????????????????????????????????????????????4.2????????????????????????
		String data = json.toString();
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		String responseXml = "";
		try{
			OpenApiClient openApiClient = new OpenApiClient(gateway, appId, appKey, isDataEncrypt, isSign);
			OpenApiReq req = openApiClient.genReqJson(method, data);
	        OpenApiRsp rsp = openApiClient.execute(req);
			logger.info("??????????????????????????????warningInstanceDownloadrsp============================="+rsp.toJsonString());
			responseXml = rsp.toJsonString();
		}catch(Exception e){
			logger.info("?????????????????????????????????",e);
		    logVo.setStatus("0");
            logVo.setErrorCode("false");
		}finally{
			//???????????????????????????????????????????????????
			logVo.setBusinessType(BusinessType.SZWARNINGINSTANCE.name());
	        logVo.setBusinessName(BusinessType.SZWARNINGINSTANCE.getName());
	        logVo.setCompensateNo(method);
	        logVo.setStatus("1");
            logVo.setErrorCode("true");
            logVo.setRegistNo(BusinessType.SZWARNINGINSTANCE.name());
            logVo.setOperateNode("??????");
            logVo.setComCode("00000000");
            Date date = new Date();
            logVo.setRequestTime(date);
            logVo.setRequestUrl("");
            logVo.setCreateUser("0000000000");
            logVo.setCreateTime(date);
            logVo.setRequestXml(data);
            logVo.setResponseXml(responseXml);
            interfaceLogService.save(logVo);
		}
	}
	//????????????????????????=====================================start
	//????????????SSL????????????
    private class NullHostNameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            // TODO Auto-generated method stub
            return true;
        }
    }
    //??????https??????
	private TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    } };
	//???????????????????????????
    public  void dowmLoadForGet(String urlStr,File file) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        URL url = new URL(urlStr);
        // ??????restful??????
        HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
        httpUrlConn.setDoOutput(true);
        httpUrlConn.setDoInput(true);
        httpUrlConn.setUseCaches(false);
        // ?????????????????????GET/POST???
        httpUrlConn.setRequestMethod("GET");
        httpUrlConn.connect();
        // ???????????????????????????????????????
        InputStream inputStream = httpUrlConn.getInputStream();
        // ?????????????????????????????????????????????afterfile?????????
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file, false));
        // ???????????????
        byte[] bytes = new byte[1024];
        int i = 0;
        while ((i = inputStream.read(bytes)) != -1) {
            // ???????????????????????????????????????
            out.write(bytes, 0, i);
        }
        out.flush();
        out.close();
        
        logger.info("?????????????????????????????????????????????:"+file.length());
        httpUrlConn.disconnect();
    }
    /**????????????????????????????????????????????????????????????
    //?????????????????????????????????
    public void uploadAcciPhoto(File file,String serviceNo) throws IOException{
        final String imgDataUrl = SpringProperties.getProperty("IMG_MANAGER_URL_IN");//???????????????????????????????????????
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	ImgRemoteFileAction imgAction=new ImgRemoteFileAction(imgDataUrl);
	    	ImgUploadVo imgUploadVo = new ImgUploadVo();
	        imgUploadVo.setBussNo(serviceNo);
	        imgUploadVo.setBussType("C");
	 		imgUploadVo.setPolicyNo("");
	 		imgUploadVo.setRiskCode("0000");
	 		imgUploadVo.setUserCode("userCode");
	 		imgUploadVo.setComCode("comCode");
	 		imgUploadVo.setSystemCode("claim");
	 		imgUploadVo.setTypePath("C-C10-C1001");
	 		imgUploadVo.setTypeName("??????-????????????-??????????????????");
	 		imgUploadVo.setFileOrgName(file.getName());
	 		imgUploadVo.setOperatorCode("operatorCode");
	 		imgUploadVo.setShootModel("????????????xxx");
	 		imgUploadVo.setShootTime(sdf.format(new Date()));
	 		imgUploadVo.setRemark("??????");
	 		imgUploadVo.setAuditFlag("0");//????????????:""-????????????;0-????????????
	        byte[] fileContent=ImgCommUtils.readBytes(file);
	        imgUploadVo=imgAction.uploadFile(imgUploadVo,fileContent);
    }
    	**/
	@Override
	public void dowmloadAcciPhoto(List<AccidentResInfo> infoVoList,HashMap<String,String> registNoAccidentNoMap){
		//????????????????????????-??????
		String imgUrlOut = SpringProperties.getProperty("SZWARN_IMAGE_URL_OUT");
		//????????????????????????-??????
		String imgUrlInner = SpringProperties.getProperty("SZWARN_IMAGE_URL_INNER");
		//????????????????????????????????????????????????
		String path = this.getClass().getClassLoader().getResource("").getPath();
		//????????????????????????
		File file0 = new File(path + "/Temp/");
        if (!file0.exists()) {
            file0.mkdirs();
        }
		try{
				if(infoVoList != null && infoVoList.size() > 0){
					for(AccidentResInfo vo : infoVoList){
						//???????????????
						String serviceNo = "";
						if(vo.getRegistNo()!=null&&!"".equals(vo.getRegistNo())){
							serviceNo = vo.getRegistNo();
						}else if(vo.getAccidentNo()!=null&&!"".equals(vo.getAccidentNo())){
							serviceNo = registNoAccidentNoMap.get(vo.getAccidentNo());
							if(serviceNo==null || "".equals(serviceNo)){
								serviceNo = vo.getAccidentNo();
							}
						}else{
							logger.info("??????????????????????????????????????????????????????????????????????????????");
							continue;
						}
						if(vo.getPhotoData() != null && vo.getPhotoData().size() > 0){
							for(PhotoData photoData : vo.getPhotoData()){
								//dowmLoadForGet(photoData.getPhotoUrl(),)
								File file = new File(path + "/Temp/" + UUID.randomUUID().toString() + photoData.getPhotoName());
								String downUrl = photoData.getPhotoUrl().replace(imgUrlOut, imgUrlInner);
								//???????????????????????????
								this.dowmLoadForGet(downUrl,file);
								//???????????????????????????
								if(file.length()>0){
									uploadAcciPhotoNew(file,serviceNo);
								}else{
									//????????????????????????????????????
									this.dowmLoadForGet(downUrl,file);
										if(file.length()>0){
											uploadAcciPhotoNew(file,serviceNo);
										}else{
											this.dowmLoadForGet(downUrl,file);
											if(file.length()>0){
												uploadAcciPhotoNew(file,serviceNo);
											}else{
												this.dowmLoadForGet(downUrl,file);
												if(file.length()>0){
													uploadAcciPhotoNew(file,serviceNo);
												}else{
													this.dowmLoadForGet(downUrl,file);
												}
											}
										}
								}
								//??????????????????
								if(file.exists()&&file.isFile()){
						             file.delete();
								}
							}
						}
					}
				}
			}catch(Exception e){
				logger.info("??????????????????????????????????????????"+e.getMessage());
		}
	}

	//????????????????????????=====================================end

    /**
     * ?????????????????????===================================start=================================================
     */
    public void uploadAcciPhotoNew(File file,String serviceNo) throws IOException{
    	 try {
    	   	    //??????????????????????????????????????????????????????
    	   	    	ReqImageBaseRootVo imageBaseRootVo=buildParams(serviceNo);
    	   	    	XydImageUploadAllMethodService xydImageUploadAllMethodService=new XydImageUploadAllMethodService();
    				String ip=SpringProperties.getProperty("YX_URL");
    	   	    	String port=SpringProperties.getProperty("YX_PORT");
    	   	    	String key=SpringProperties.getProperty("YX_key");
    	   	    	String id=SpringProperties.getProperty("YX_ID");
    	   	    	ReqParameterVo parameterVo =new ReqParameterVo();
    	   	    	parameterVo.setAppCode(CodeConstants.APPCODEL2);
    	   	    	parameterVo.setSunIcmsIp(ip);
    	   	    	parameterVo.setSocketNo(port!=null?Integer.valueOf(port).intValue():null);
    	   	    	parameterVo.setKey(key);
    	   	    	parameterVo.setId(id);
    	   	    	String returnXml=xydImageUploadAllMethodService.uploadImage(imageBaseRootVo, file, parameterVo);
    		   	    	  if(StringUtils.isNotBlank(returnXml) && returnXml.contains("????????????")){
    		   	    		logger.info("????????????????????????????????????"+returnXml);
    		   	    	  }else{
    		   	    		logger.info("?????????????????????????????????"+returnXml);
    		   	    	 } 
    	   	 } catch (Exception e) {
    	   		logger.error("????????????????????????????????????",e);
    		}
    		    	
    }
    
    /**
	 * ??????????????????
	 * @param submitcaseimageinforVo
	 * @return
	 */
	private ReqImageBaseRootVo buildParams(String serviceNo){
		ReqImageBaseRootVo root=new ReqImageBaseRootVo();
		BaseDataVo baseDataVo=new BaseDataVo();
		ReqMetaDataVo metaDataVo=new ReqMetaDataVo();
		baseDataVo.setComCode("00020000");
		baseDataVo.setUserCode("0000000000");
		baseDataVo.setUserName("0000000000");
		baseDataVo.setOrgCode("DHIC");
		baseDataVo.setOrgName("????????????");
		baseDataVo.setRoleCode(CodeConstants.APPROLE);
		ReqBatchVo batchVo=new ReqBatchVo();
		batchVo.setAppCode(CodeConstants.APPCODECLAIM);
		batchVo.setAppName(CodeConstants.APPNAMECLAIM);
		batchVo.setBusiNo(serviceNo);
		batchVo.setComCode("00020000");
		ReqVtreeVo vtreeVo=new ReqVtreeVo();
		vtreeVo.setAppCode(CodeConstants.APPCODECLAIM);
		vtreeVo.setAppName(CodeConstants.APPNAMECLAIM);
		
		ReqFatherNodeVo fatherNodeVo=new ReqFatherNodeVo();
		fatherNodeVo.setId(CodeConstants.APPCODEL2);
		fatherNodeVo.setName(CodeConstants.APPNAMEL2);
		fatherNodeVo.setBarCode("");
		fatherNodeVo.setChildFlag("0");
		fatherNodeVo.setMinpages("0");
		fatherNodeVo.setMaxpages("99999");
		fatherNodeVo.setReseize("800*600");
		fatherNodeVo.setRight(CodeConstants.YXRIGHTCODE);
		//???????????????
		List<ReqSonNodeVo> sonNodeVos1=new ArrayList<ReqSonNodeVo>();
		ReqSonNodeVo sonNodeVo1=new ReqSonNodeVo();
		sonNodeVo1.setId(CodeConstants.JBPFNODE);
		sonNodeVo1.setMaxpages("99999");
		sonNodeVo1.setMinpages("0");
		sonNodeVo1.setName("????????????");
		sonNodeVo1.setReseize("800*600");
		sonNodeVo1.setRight(CodeConstants.YXRIGHTCODE);
		sonNodeVo1.setChildFlag("0");
		sonNodeVo1.setBarCode("");
		
		//???????????????
		List<ReqSonNodeVo> sonNodeVos=new ArrayList<ReqSonNodeVo>();
		ReqSonNodeVo sonNodeVo01=new ReqSonNodeVo();
		sonNodeVo01.setId(CodeConstants.JBPFNODE_SZ);
		sonNodeVo01.setName("??????????????????");
		sonNodeVo01.setRight(CodeConstants.YXRIGHTCODE);
		sonNodeVo01.setReseize("800*600");
		sonNodeVo01.setChildFlag("1");
		sonNodeVo01.setBarCode("");
		sonNodeVo01.setMaxpages("99999");
		sonNodeVo01.setMinpages("0");
		sonNodeVos.add(sonNodeVo01);
		
		sonNodeVo1.setSonNodes(sonNodeVos);
		sonNodeVos1.add(sonNodeVo1);
		fatherNodeVo.setSonNodes(sonNodeVos1);
		List<ReqFatherNodeVo> fatherNodeVos=new ArrayList<ReqFatherNodeVo>();
		fatherNodeVos.add(fatherNodeVo);
		vtreeVo.setFatherNodeVos(fatherNodeVos);
		batchVo.setVtreeVo(vtreeVo);
		
		/*
		ReqPagesVo reqPagesVo=new ReqPagesVo();
		List<ReqPageNodeVo> pages=new ArrayList<ReqPageNodeVo>();
		if(sonNodeVos!=null && sonNodeVos.size()>0){
			for(ReqSonNodeVo sonNodeVo:sonNodeVos){
				ReqPageNodeVo pageNodeVo=new ReqPageNodeVo();
				pageNodeVo.setId(sonNodeVo.getId());
				pageNodeVo.setAction("ADD");
				List<ReqPageVo> pageVos=new ArrayList<ReqPageVo>();
				for(CaseimageVo photoVo:submitcaseimageinforVo.getCaseimagelist()){
						ReqPageVo pageVo=new ReqPageVo();
						if(StringUtils.isNotBlank(photoVo.getImageurl())){
							String[] str1=photoVo.getImageurl().split("/");
							pageVo.setFileName(str1[str1.length-1]);
							pageVo.setRemark("????????????");
							pageVo.setUpUser("0000000000");
							pageVo.setUpUsername("0000000000");
							pageVo.setUpOrgname("?????????");
							pageVo.setUp_org("00020000");
							pageVo.setUpTime(DateFormatString(new Date()));
							pageVos.add(pageVo);
						}
				}
				pageNodeVo.setPageVos(pageVos);
				pages.add(pageNodeVo);
			}
		}
		reqPagesVo.setReqPageNodeVos(pages);
		batchVo.setReqPagesVo(reqPagesVo);
		*/
		metaDataVo.setBatchVo(batchVo);
		root.setBaseDataVo(baseDataVo);
		root.setMetaDataVo(metaDataVo);
	return root;
	}
	private String DateFormatString(Date strDate){
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		return str;
	}
    /**
     * ?????????????????????===================================end=================================================
     */


	
//	private String reverse(String str) {
//		if(str!=null){
//			return new StringBuilder(str.trim()).reverse().toString();
//		}
//
//		return null;
//	}

	@Override
	public Page findPolicyAccidentForPage(AccidentResInfo accidentResInfo,Integer start,Integer length) {
		
		List<AccidentResInfo> returnAccidentResInfo = new ArrayList<AccidentResInfo>();
		List<Object> paramValues = new ArrayList<Object>();
		// hql????????????
		StringBuffer queryString = new StringBuffer("from PrpLAccidentResInfoPo pf where 1=1 ");
		if (StringUtils.isNotBlank(accidentResInfo.getRegistNo())) {
			queryString.append(" AND pf.registNo = ? ");
			paramValues.add(accidentResInfo.getRegistNo());
		}
		if (StringUtils.isNotBlank(accidentResInfo.getAccidentNo())) {
			queryString.append(" AND pf.accidentNo = ? ");
			paramValues.add(accidentResInfo.getAccidentNo());
		}
		
		
		queryString.append(" Order By pf.accidentDate ");
		// ????????????
		Page page = databaseDao.findPageByHql(queryString.toString(), (start/ length + 1), length, paramValues.toArray());
		return assemblyAccidentResInfo(page);
	}
	
    	
	private Page assemblyAccidentResInfo(Page page) {
		for (int i = 0; i < page.getResult().size(); i++) {
			PrpLAccidentResInfoPoVO plyVo = new PrpLAccidentResInfoPoVO();
			PrpLAccidentResInfoPo pm =  (PrpLAccidentResInfoPo)page.getResult().get(i);
			plyVo = Beans.copyDepth().from(pm).to(PrpLAccidentResInfoPoVO.class);
			//?????????????????????
			if (StringUtils.isNotBlank(pm.getRegistNo())) {
				SqlJoinUtils sqlUtils = new SqlJoinUtils();
				sqlUtils.append(" FROM PrpLDispatchInfo dispatchInfo where dispatchInfo.registNo = ? ");
				sqlUtils.addParamValue(pm.getRegistNo());
				Object[] params = sqlUtils.getParamValues();
				String sql = sqlUtils.getSql();
				List<PrpLDispatchInfo> list = databaseDao.findAllByHql(PrpLDispatchInfo.class,sql, params);
				if(list!=null && list.size()>0){
						PrpLDispatchInfo prpLDispatchInfo=list.get(0);
						plyVo.setSurveyorName(prpLDispatchInfo.getSurveyorName());
						plyVo.setSurveyorPhone(prpLDispatchInfo.getSurveyorPhone());
				}
			}
			if (pm.getPlateData()!=null && pm.getPlateData().size()!=0) {
				String plateNos = "";
				for (int j = 0; j < pm.getPlateData().size(); j++) {
					if(j!=0){
						plateNos+=",";
					}
					plateNos += pm.getPlateData().get(j).getPlateNo();
				}
				plyVo.setPlateNos(plateNos);
				
			}
			page.getResult().set(i,plyVo);
		}
		return page;
	}

public void autoGenerateSurveyTasks(DispatchInfo dispatchInfo) throws Exception {
		RpcContext content = RpcContext.getContext();
		
		// ?????????
		String registNo = dispatchInfo.getReportNo();
		// ????????????????????????
		String receiveTime = dispatchInfo.getReceiveTime();
		// ???????????? 1-?????? 2-?????? 3-?????? 4-??????
		String dispatchType = dispatchInfo.getDispatchType();
		// ???????????? 1-?????? 100-??????
		String taskType = dispatchInfo.getTaskType();
		// ???????????? taskType = '100' ?????????
		String cancelReason = dispatchInfo.getCancelReason();
		// ????????????ID
		String userId = dispatchInfo.getUserId();
		// ??????????????????????????????
		String surveyorCo = dispatchInfo.getSurveyorCo();
		// ??????????????????
		String surveyorName = dispatchInfo.getSurveyorName();
		// ????????????????????????
		String surveyorPhone = dispatchInfo.getSurveyorPhone();
		
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		// ???????????????????????????????????????
		content.setAttachment("UserCode", prpLRegistVo.getCreateUser());
		String flowId = prpLRegistVo.getFlowId();
		System.out.println("???????????? ==== " + ServiceUserUtils.getUserCode());
		
		PrpLWfTaskVo taskVo = new PrpLWfTaskVo();
		List<String> nodeCodes = new ArrayList<String>();
		// ????????????
		nodeCodes.add("Sched");
		List<PrpLWfTaskVo> taskIns = wfTaskHandleService.findPrpLWfTaskByregistNoAndnodeCode(registNo, "1", nodeCodes);
		if (taskIns != null && taskIns.size() > 0) {
			taskVo = taskIns.get(0);
		}
		
		// ???????????????
		List<SysUserVo> sysUserList = sysUserService.findByUserOrName(surveyorName);
		SysUserVo sysUser = new SysUserVo();
		for (SysUserVo user : sysUserList) {
			// ???????????????????????????????????????????????????????????????
			if (user.getComCode().startsWith("0002")) {
				sysUser = user;
				break;
			}
		}
		String userCode = sysUser.getUserCode();
		String comCode = sysUser.getComCode();
		
		
		PrpLScheduleTaskVo scheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(registNo);
		// ???????????????????????????????????????????????????????????????
		if (StringUtils.equals(scheduleTaskVo.getScheduleStatus(), CodeConstants.ScheduleStatus.NOT_SCHEDULED)) {
			scheduleHandlerService.updateScheduleStatus(scheduleTaskVo);
			Double taskId = taskVo.getTaskId().doubleValue();
			wfTaskHandleService.tempSaveTask(taskId, taskVo.getRegistNo(), userCode, comCode);
		}
		scheduleTaskVo.setIsAutoCheck("0");
		
		// ?????????????????????????????????????????????
		for (PrpLScheduleItemsVo vo : scheduleTaskVo.getPrpLScheduleItemses()) {
			if (StringUtils.isNotBlank(vo.getScheduledUsercode())) {
				SysUserVo sysUserVo = sysUserService.findByUserCode(vo.getScheduledUsercode());
				if (sysUserVo != null && StringUtils.isNotBlank(sysUserVo.getMobile())) {
					vo.setMoblie(sysUserVo.getMobile());
				}
			}
		}
		
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(flowId);
		submitVo.setFlowTaskId(taskVo.getTaskId());
		submitVo.setComCode(comCode);
		
		// ????????????
		// ?????????????????????
		// PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(registNo);		
		if ("".equals(policyViewService.getPolicyComCode(registNo))) {
			submitVo.setComCode(comCode);
		} else {
			submitVo.setComCode(policyViewService.getPolicyComCode(registNo));
		}
		
		submitVo.setTaskInKey(registNo);
		submitVo.setTaskInUser(userCode);
		submitVo.setSubmitType(SubmitType.N);
		// ???????????????
		submitVo.setHandleruser(userCode);
		submitVo.setComCode(comCode);
		// ????????????????????????
		String code = scheduleTaskVo.getScheduledComcode();
		
		// ??????????????????
		List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
		prpLCMainVoList = policyViewService.getPolicyAllInfo(scheduleTaskVo.getRegistNo());
		if (prpLCMainVoList.size() > 0 && prpLCMainVoList != null) {
			// ????????????
			if (prpLCMainVoList.size() == 2) {
				for (PrpLCMainVo vo : prpLCMainVoList) {
					if (("12").equals(vo.getRiskCode().substring(0, 2))) {
						comCode = vo.getComCode();
					}
				}
			} else {
				comCode = prpLCMainVoList.get(0).getComCode();
			}
			if (code != null && comCode != "") {
				if ("0002".equals(code.substring(0, 4))) {// ??????
					if (!code.substring(0, 4).equals(comCode.substring(0, 4))) {

						prpLRegistVo.setTpFlag("1");
						prpLRegistVo.setIsoffSite("1");
						registService.saveOrUpdate(prpLRegistVo);
					}

				} else {
					if (!code.substring(0, 2).equals(comCode.substring(0, 2))) {
						prpLRegistVo.setTpFlag("1");
						prpLRegistVo.setIsoffSite("1");
						registService.saveOrUpdate(prpLRegistVo);
					}
				}
			}
		}
		
		scheduleTaskVo.setTypes("1");
		scheduleTaskVo.setScheduledTime(new Date());
		List<PrpLScheduleItemsVo> prpLScheduleItemses = scheduleTaskVo.getPrpLScheduleItemses();
		
		for (PrpLScheduleItemsVo scheduleItem : prpLScheduleItemses) {
			scheduleItem.setUpdateUser(prpLRegistVo.getCreateUser());
			scheduleItem.setUpdateTime(new Date());
		}
		scheduleTaskVo.setScheduledUsercode(userCode);
		scheduleTaskVo.setScheduledComcode(comCode);
		scheduleHandlerService.saveScheduleItemTask(scheduleTaskVo, prpLScheduleItemses, submitVo);
		
		try {
			if (prpLScheduleItemses != null && prpLScheduleItemses.size() > 0) {
				for (PrpLScheduleItemsVo vo : prpLScheduleItemses) {
					if (StringUtils.equals(vo.getItemType(), "4")) {
						scheduleTaskVo.setScheduleStatus("3");
						scheduleTaskVo.setIsPersonFlag("1");
						scheduleTaskVo.setPrpLScheduleItemses(prpLScheduleItemses);
					}
				}
			}
			interfaceAsyncService.scheduleInfoToFounder(scheduleTaskVo, ScheduleType.CHECK_SCHEDULE);
		} catch (Exception e) {
			logger.info("autoGenerateSurveyTasks()---????????????????????????????????????????????????---??????????????????????????????");
			e.printStackTrace();
		}
		
		// ????????????
		scheduleHandlerService.sendMsg(scheduleTaskVo, prpLScheduleItemses, sysUser, "1");	
		
		// ??????????????????
		if (StringUtils.isNotBlank(scheduleTaskVo.getCallNumber())) {
			CallPhoneReqVo vo = new CallPhoneReqVo();
			CallPhoneBodyReq callPhoneBodyReq = new CallPhoneBodyReq();
			CallPhoneHeadReq callPhoneHeadReq = new CallPhoneHeadReq();
			callPhoneHeadReq.setSystemCode("CC1007");
			CallPhoneOutdateReq callPhoneOutdateReq = new CallPhoneOutdateReq();
			callPhoneOutdateReq.setClmNo(scheduleTaskVo.getRegistNo());
			callPhoneOutdateReq.setExaminePhone(scheduleTaskVo.getCallNumber());
			callPhoneBodyReq.setOutdate(callPhoneOutdateReq);
			vo.setHead(callPhoneHeadReq);
			vo.setBody(callPhoneBodyReq);
			String url = SpringProperties.getProperty("FOUNDER_URL");
			if (StringUtils.isEmpty(url)) {
				throw new Exception("??????????????????????????????????????????");
			}
			try {
				founderService.sendCallPhoneToFounder(vo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		// ?????????????????????taskId, ??????prplCheck?????????
		nodeCodes = new ArrayList<String>();
		nodeCodes.add("Check");
		List<PrpLWfTaskVo> taskInChecks = wfTaskHandleService.findPrpLWfTaskByregistNoAndnodeCode(registNo, "1", nodeCodes);
		if (taskInChecks != null && taskInChecks.size() > 0) {
			taskVo = taskInChecks.get(0);
		}
		createPrplCheckDatas(new Double(taskVo.getTaskId().toString()), sysUser);
		
	}
	
	public void createPrplCheckDatas(Double flowTaskId, SysUserVo userVo) throws Exception {
		Long checkId = null;
		try {
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
			if (FlowNode.Check.equals(wfTaskVo.getNodeCode()) && FlowNode.Chk.equals(wfTaskVo.getSubNodeCode())) {
				if (HandlerStatus.INIT.equals(wfTaskVo.getHandlerStatus())) {// ???????????????

					Long scheduleTaskId = Long.parseLong(wfTaskVo.getHandlerIdKey());
					// ???????????????
					CheckActionVo checkActionVo = checkHandleService.initCheckBySchedule(scheduleTaskId, wfTaskVo.getRegistNo(), userVo);
					// ??????
					checkId = checkHandleService.saveCheckOnAccept(checkActionVo, userVo);

				} else {
					// ????????????
					throw new IllegalArgumentException("?????????????????????????????????" + wfTaskVo.getHandlerUser());
				}
			} else if (!FlowNode.Chk.equals(wfTaskVo.getSubNodeCode())) {
				// ???????????????
				if (HandlerStatus.INIT.equals(wfTaskVo.getHandlerStatus())) {
					checkId = Long.parseLong(wfTaskVo.getHandlerIdKey());
				} else {
					// ????????????
					throw new IllegalArgumentException("?????????????????????????????????" + wfTaskVo.getHandlerUser());
				}
			} else {
				throw new IllegalArgumentException("??????????????????");
			}
			// ????????????
			// ????????????????????????
			wfTaskHandleService.tempSaveTask(flowTaskId, checkId.toString(), userVo.getUserCode(), userVo.getComCode());
			
		} catch (Exception e) {
			logger.error("???????????????????????????", e);
		}
	}

}	



