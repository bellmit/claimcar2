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
		c.setTime(accidentDate); // 设置日期
		c.add(Calendar.HOUR,12); // 日期加12小时
	    Date timeEnd = new Date();
	    timeEnd = c.getTime();
	    
		c.setTime(timeEnd); // 设置日期
		c.add(Calendar.DATE, -1); // 日期减1天
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
					if(notCase){// 未结案
						break;
					}else{// 结案
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
		boolean isDataEncrypt = true; // 数据加密
		boolean isSign = true; // 签名
        JSONObject json = new JSONObject();
		json.put("insuranceCode",insuranceCode);// 业务数据加密字符串（加密方式见”4.2对称加密”章节）
        String data = json.toString();
        
		try {
			// 理赔信息上传
	        OpenApiClient openApiClient = new OpenApiClient(gateway, appId, appKey, isDataEncrypt, isSign);
			OpenApiReq req = openApiClient.genReqJson(method, data);
	        OpenApiRsp rsp = openApiClient.execute(req);
			logger.info("返回数据rsp============================="+rsp.toJsonString());
	        JSONObject rejson = JSONObject.fromObject(rsp);
			SdSzResponseVo sdSzResponseVo = (SdSzResponseVo) JSONObject.toBean(rejson, SdSzResponseVo.class);
			logger.info("返回数据============================="+sdSzResponseVo.getData());
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
						// 筛选重复数据AccidentResInfo
    					if(hashMap.size() > 0){
							if( !hashMap.containsKey(accidentNo)){// 去重
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
				// 保存AccidentResInfo
    			this.savePrpLAccidentResInfoPo(accidentResInfoList);
    			if(accidentResInfos != null && accidentResInfos.size() > 0){
    				for(AccidentResInfo accidentResInfo : accidentResInfos){
    					for(PlateData plateData : accidentResInfo.getPlateData()){
							// 根据车牌号、出险时间（前后12小时）查询该事故信息是否在理赔系统存在未结案且未绑定事故编号的报案
    						String registNo = this.findAccidentInfoByPlateNoAndTime(plateData.getPlateNo(), accidentResInfo.getAccidentTime(),"0002","1");
    						if(StringUtils.isNotBlank(registNo)){
    							PrpLAccidentResInfoPo resInfo = this.findAccidentInfoPoByAccidentNo(accidentResInfo.getAccidentNo());
        						resInfo.setRegistNo(registNo);
        						resInfo.setUpdateTime(new Date());
    							PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
        						prpLRegistVo.setAccidentNo(accidentResInfo.getAccidentNo());
        						prpLRegistVo.setUpdateTime(new Date());
								registService.saveOrUpdate(prpLRegistVo);// 保存AccidentNo到PrpLRegist
								registNoAccidentNoMap.put(accidentResInfo.getAccidentNo(), registNo);
    						}
    					}
    				}
    			}
    			long  time = new Date().getTime();
    			logger.info("深圳警保下载图片=============================开始"+time);
    			this.dowmloadAcciPhoto(accidentResInfoList,registNoAccidentNoMap);
    			logger.info("深圳警保下载图片=============================结束,耗时："+(new Date().getTime()-time));
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
		dar.add(Calendar.DATE, -1);// 前一天时间
		Date Bdate=dar.getTime();//
		List<String> strList=new ArrayList<String>();
		Map<String,Long> map=new HashMap<String,Long>();//key-claimNo,value-id
		Set<String> set = new HashSet<String>();// 去重
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
		boolean isDataEncrypt = true; // 数据加密
		boolean isSign = true; // 签名
		JSONObject json = new JSONObject();
		json.put("insuranceCode",insuranceCode);// 业务数据加密字符串（加密方式见”4.2对称加密”章节）
		String data = json.toString();
		
		try{
			OpenApiClient openApiClient = new OpenApiClient(gateway, appId, appKey, isDataEncrypt, isSign);
			OpenApiReq req = openApiClient.genReqJson(method, data);
			OpenApiRsp rsp = openApiClient.execute(req);
			logger.info("dispatchInfoDownload===深圳警保调度信息下载接口返回数据=============================" + rsp.toJsonString());
			JSONObject rejson = JSONObject.fromObject(rsp);
			SdSzResponseVo sdSzResponseVo = (SdSzResponseVo) JSONObject.toBean(rejson, SdSzResponseVo.class);
			String resData = sdSzResponseVo.getData();
			
			logger.info("dispatchInfoDownload===深圳警保调度信息下载接口返回data数据=======" + resData);
			// 保存数据
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
						// 根据调度信息决定是否自动生成查勘任务
						tryToGenerateSurveyTasks(dispatchInfo);
					}
					databaseDao.saveAll(PrpLDispatchInfo.class, prpLDispatchInfoList);
				}
			}
		}catch(Exception e){
			logger.error("深圳警保调度信息下载接口报错："+e);
		}
	}
	
	public void tryToGenerateSurveyTasks(DispatchInfo dispatchInfo) {
		if (dispatchInfo == null) {
			logger.info("dispatchInfoDownload()---深圳警保调度信息下载自动生成查勘任务 调度信息为空，无法生成查勘任务！");
		} else {
			try {// 抢单人员归属机构为鼎和
				if (CodeConstants.DHICCODE.equals(dispatchInfo.getSurveyorCo()) || CodeConstants.DHICNAME.equals(dispatchInfo.getSurveyorCo())) {
					// 是否生成查勘任务
					List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findPrpWfTaskVo(dispatchInfo.getReportNo(), FlowNode.Check.name(), FlowNode.Chk.name());
					if(prpLWfTaskVoList != null && !prpLWfTaskVoList.isEmpty()){
						logger.info("tryToGenerateSurveyTasks()--- 深圳警保调度信息下载自动生成查勘任务失败，报案号："+ dispatchInfo.getReportNo() +"已有查勘任务！");
					} else {
						logger.info("autoGenerateSurveyTasks()--- "+ dispatchInfo.getReportNo() +" ---深圳警保调度信息下载自动生成查勘任务开始---");
						autoGenerateSurveyTasks(dispatchInfo);
						logger.info("autoGenerateSurveyTasks()--- "+ dispatchInfo.getReportNo() +" ---深圳警保调度信息下载自动生成查勘任务结束---");
					}
				}
			} catch (Exception e) {

				logger.info("tryToGenerateSurveyTasks()---深圳警保调度信息下载自动生成查勘任务失败：" + e.getStackTrace());
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
		boolean isDataEncrypt = true; // 数据加密
		boolean isSign = true; // 签名
		JSONObject json = new JSONObject();
		json.put("insuranceCode",insuranceCode);// 业务数据加密字符串（加密方式见”4.2对称加密”章节）
		String data = json.toString();
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		String responseXml = "";
		try{
			OpenApiClient openApiClient = new OpenApiClient(gateway, appId, appKey, isDataEncrypt, isSign);
			OpenApiReq req = openApiClient.genReqJson(method, data);
	        OpenApiRsp rsp = openApiClient.execute(req);
			logger.info("警情信息下载返回数据warningInstanceDownloadrsp============================="+rsp.toJsonString());
			responseXml = rsp.toJsonString();
		}catch(Exception e){
			logger.info("警情信息下载接口报错：",e);
		    logVo.setStatus("0");
            logVo.setErrorCode("false");
		}finally{
			//主要保存返回报文，其它部分字段写死
			logVo.setBusinessType(BusinessType.SZWARNINGINSTANCE.name());
	        logVo.setBusinessName(BusinessType.SZWARNINGINSTANCE.getName());
	        logVo.setCompensateNo(method);
	        logVo.setStatus("1");
            logVo.setErrorCode("true");
            logVo.setRegistNo(BusinessType.SZWARNINGINSTANCE.name());
            logVo.setOperateNode("自动");
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
	//文件下载功能实现=====================================start
	//用于避开SSL证书验证
    private class NullHostNameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            // TODO Auto-generated method stub
            return true;
        }
    }
    //加载https请求
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
	//从警保平台下载图片
    public  void dowmLoadForGet(String urlStr,File file) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        URL url = new URL(urlStr);
        // 打开restful链接
        HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
        httpUrlConn.setDoOutput(true);
        httpUrlConn.setDoInput(true);
        httpUrlConn.setUseCaches(false);
        // 设置请求方式（GET/POST）
        httpUrlConn.setRequestMethod("GET");
        httpUrlConn.connect();
        // 将返回的输入流转换成字符串
        InputStream inputStream = httpUrlConn.getInputStream();
        // 定义文件输出流，用来把信息写入afterfile文件中
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file, false));
        // 文件缓存区
        byte[] bytes = new byte[1024];
        int i = 0;
        while ((i = inputStream.read(bytes)) != -1) {
            // 将缓存区中的内容写到文件中
            out.write(bytes, 0, i);
        }
        out.flush();
        out.close();
        
        logger.info("深圳警保案件信息下载，图片大小:"+file.length());
        httpUrlConn.disconnect();
    }
    /**删除旧的影像上传功能，改成信雅达影像上传
    //上传图片到公司影像平台
    public void uploadAcciPhoto(File file,String serviceNo) throws IOException{
        final String imgDataUrl = SpringProperties.getProperty("IMG_MANAGER_URL_IN");//从配置文件读取影像数据地址
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
	 		imgUploadVo.setTypeName("理赔-警保照片-深圳警保照片");
	 		imgUploadVo.setFileOrgName(file.getName());
	 		imgUploadVo.setOperatorCode("operatorCode");
	 		imgUploadVo.setShootModel("相机型号xxx");
	 		imgUploadVo.setShootTime(sdf.format(new Date()));
	 		imgUploadVo.setRemark("备注");
	 		imgUploadVo.setAuditFlag("0");//审核状态:""-无需审核;0-待审核，
	        byte[] fileContent=ImgCommUtils.readBytes(file);
	        imgUploadVo=imgAction.uploadFile(imgUploadVo,fileContent);
    }
    	**/
	@Override
	public void dowmloadAcciPhoto(List<AccidentResInfo> infoVoList,HashMap<String,String> registNoAccidentNoMap){
		//图片下载地址前缀-外部
		String imgUrlOut = SpringProperties.getProperty("SZWARN_IMAGE_URL_OUT");
		//图片下载地址前缀-内部
		String imgUrlInner = SpringProperties.getProperty("SZWARN_IMAGE_URL_INNER");
		//獲取一個跟路徑，用於存放臨時文件
		String path = this.getClass().getClassLoader().getResource("").getPath();
		//创建临时文件目录
		File file0 = new File(path + "/Temp/");
        if (!file0.exists()) {
            file0.mkdirs();
        }
		try{
				if(infoVoList != null && infoVoList.size() > 0){
					for(AccidentResInfo vo : infoVoList){
						//获取业务号
						String serviceNo = "";
						if(vo.getRegistNo()!=null&&!"".equals(vo.getRegistNo())){
							serviceNo = vo.getRegistNo();
						}else if(vo.getAccidentNo()!=null&&!"".equals(vo.getAccidentNo())){
							serviceNo = registNoAccidentNoMap.get(vo.getAccidentNo());
							if(serviceNo==null || "".equals(serviceNo)){
								serviceNo = vo.getAccidentNo();
							}
						}else{
							logger.info("深圳警保下载图片，出现异常。报案号与事故号同时为空！");
							continue;
						}
						if(vo.getPhotoData() != null && vo.getPhotoData().size() > 0){
							for(PhotoData photoData : vo.getPhotoData()){
								//dowmLoadForGet(photoData.getPhotoUrl(),)
								File file = new File(path + "/Temp/" + UUID.randomUUID().toString() + photoData.getPhotoName());
								String downUrl = photoData.getPhotoUrl().replace(imgUrlOut, imgUrlInner);
								//从警保平台下载文件
								this.dowmLoadForGet(downUrl,file);
								//上传到公司影像平台
								if(file.length()>0){
									uploadAcciPhotoNew(file,serviceNo);
								}else{
									//若下载不成功，递归四次。
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
								//删除临时文件
								if(file.exists()&&file.isFile()){
						             file.delete();
								}
							}
						}
					}
				}
			}catch(Exception e){
				logger.info("深圳警保下载图片，出现异常："+e.getMessage());
		}
	}

	//文件下载功能实现=====================================end

    /**
     * 信雅达影像上传===================================start=================================================
     */
    public void uploadAcciPhotoNew(File file,String serviceNo) throws IOException{
    	 try {
    	   	    //将河南快赔传过来的图片上传到影像系统
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
    		   	    	  if(StringUtils.isNotBlank(returnXml) && returnXml.contains("上传成功")){
    		   	    		logger.info("深圳警保照片上传成功：："+returnXml);
    		   	    	  }else{
    		   	    		logger.info("深圳警保照片上传出错："+returnXml);
    		   	    	 } 
    	   	 } catch (Exception e) {
    	   		logger.error("信雅达影像上传接口报错：",e);
    		}
    		    	
    }
    
    /**
	 * 组装请求数据
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
		baseDataVo.setOrgName("鼎和保险");
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
		//加载父节点
		List<ReqSonNodeVo> sonNodeVos1=new ArrayList<ReqSonNodeVo>();
		ReqSonNodeVo sonNodeVo1=new ReqSonNodeVo();
		sonNodeVo1.setId(CodeConstants.JBPFNODE);
		sonNodeVo1.setMaxpages("99999");
		sonNodeVo1.setMinpages("0");
		sonNodeVo1.setName("警保照片");
		sonNodeVo1.setReseize("800*600");
		sonNodeVo1.setRight(CodeConstants.YXRIGHTCODE);
		sonNodeVo1.setChildFlag("0");
		sonNodeVo1.setBarCode("");
		
		//加载子节点
		List<ReqSonNodeVo> sonNodeVos=new ArrayList<ReqSonNodeVo>();
		ReqSonNodeVo sonNodeVo01=new ReqSonNodeVo();
		sonNodeVo01.setId(CodeConstants.JBPFNODE_SZ);
		sonNodeVo01.setName("深圳警保照片");
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
							pageVo.setRemark("车损图片");
							pageVo.setUpUser("0000000000");
							pageVo.setUpUsername("0000000000");
							pageVo.setUpOrgname("总公司");
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
     * 信雅达影像上传===================================end=================================================
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
		// hql查询语句
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
		// 执行查询
		Page page = databaseDao.findPageByHql(queryString.toString(), (start/ length + 1), length, paramValues.toArray());
		return assemblyAccidentResInfo(page);
	}
	
    	
	private Page assemblyAccidentResInfo(Page page) {
		for (int i = 0; i < page.getResult().size(); i++) {
			PrpLAccidentResInfoPoVO plyVo = new PrpLAccidentResInfoPoVO();
			PrpLAccidentResInfoPo pm =  (PrpLAccidentResInfoPo)page.getResult().get(i);
			plyVo = Beans.copyDepth().from(pm).to(PrpLAccidentResInfoPoVO.class);
			//组装查勘员信息
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
		
		// 报案号
		String registNo = dispatchInfo.getReportNo();
		// 查勘人员接单时间
		String receiveTime = dispatchInfo.getReceiveTime();
		// 调度类型 1-抢单 2-派单 3-兜底 4-转派
		String dispatchType = dispatchInfo.getDispatchType();
		// 任务类型 1-完成 100-取消
		String taskType = dispatchInfo.getTaskType();
		// 取消原因 taskType = '100' 时必传
		String cancelReason = dispatchInfo.getCancelReason();
		// 查勘人员ID
		String userId = dispatchInfo.getUserId();
		// 查勘人员所属公司代码
		String surveyorCo = dispatchInfo.getSurveyorCo();
		// 查勘人员姓名
		String surveyorName = dispatchInfo.getSurveyorName();
		// 查勘人员手机号码
		String surveyorPhone = dispatchInfo.getSurveyorPhone();
		
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		// 将报案创建人设置为全局变量
		content.setAttachment("UserCode", prpLRegistVo.getCreateUser());
		String flowId = prpLRegistVo.getFlowId();
		System.out.println("全局变量 ==== " + ServiceUserUtils.getUserCode());
		
		PrpLWfTaskVo taskVo = new PrpLWfTaskVo();
		List<String> nodeCodes = new ArrayList<String>();
		// 调度节点
		nodeCodes.add("Sched");
		List<PrpLWfTaskVo> taskIns = wfTaskHandleService.findPrpLWfTaskByregistNoAndnodeCode(registNo, "1", nodeCodes);
		if (taskIns != null && taskIns.size() > 0) {
			taskVo = taskIns.get(0);
		}
		
		// 查勘员信息
		List<SysUserVo> sysUserList = sysUserService.findByUserOrName(surveyorName);
		SysUserVo sysUser = new SysUserVo();
		for (SysUserVo user : sysUserList) {
			// 默认取取深圳分公司名字符合的第一个人员信息
			if (user.getComCode().startsWith("0002")) {
				sysUser = user;
				break;
			}
		}
		String userCode = sysUser.getUserCode();
		String comCode = sysUser.getComCode();
		
		
		PrpLScheduleTaskVo scheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(registNo);
		// 修改调度任务状态，未处理任务改为处理中状态
		if (StringUtils.equals(scheduleTaskVo.getScheduleStatus(), CodeConstants.ScheduleStatus.NOT_SCHEDULED)) {
			scheduleHandlerService.updateScheduleStatus(scheduleTaskVo);
			Double taskId = taskVo.getTaskId().doubleValue();
			wfTaskHandleService.tempSaveTask(taskId, taskVo.getRegistNo(), userCode, comCode);
		}
		scheduleTaskVo.setIsAutoCheck("0");
		
		// 遍历财产调度信息，设置手机号码
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
		
		// 调度提交
		// 调度任务表对象
		// PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(registNo);		
		if ("".equals(policyViewService.getPolicyComCode(registNo))) {
			submitVo.setComCode(comCode);
		} else {
			submitVo.setComCode(policyViewService.getPolicyComCode(registNo));
		}
		
		submitVo.setTaskInKey(registNo);
		submitVo.setTaskInUser(userCode);
		submitVo.setSubmitType(SubmitType.N);
		// 任务处理人
		submitVo.setHandleruser(userCode);
		submitVo.setComCode(comCode);
		// 调度查勘员的机构
		String code = scheduleTaskVo.getScheduledComcode();
		
		// 写入通赔标识
		List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
		prpLCMainVoList = policyViewService.getPolicyAllInfo(scheduleTaskVo.getRegistNo());
		if (prpLCMainVoList.size() > 0 && prpLCMainVoList != null) {
			// 承保地区
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
				if ("0002".equals(code.substring(0, 4))) {// 深圳
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
			logger.info("autoGenerateSurveyTasks()---警保调度信息下载自动生成查勘任务---送方正客服系统失败！");
			e.printStackTrace();
		}
		
		// 发送短信
		scheduleHandlerService.sendMsg(scheduleTaskVo, prpLScheduleItemses, sysUser, "1");	
		
		// 新的一键呼出
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
				throw new Exception("未配置方正客服系统服务地址！");
			}
			try {
				founderService.sendCallPhoneToFounder(vo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		// 查询查勘任务的taskId, 生成prplCheck表数据
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
				if (HandlerStatus.INIT.equals(wfTaskVo.getHandlerStatus())) {// 未接受任务

					Long scheduleTaskId = Long.parseLong(wfTaskVo.getHandlerIdKey());
					// 查勘初始化
					CheckActionVo checkActionVo = checkHandleService.initCheckBySchedule(scheduleTaskId, wfTaskVo.getRegistNo(), userVo);
					// 保存
					checkId = checkHandleService.saveCheckOnAccept(checkActionVo, userVo);

				} else {
					// 正在处理
					throw new IllegalArgumentException("任务已被接收！接收人：" + wfTaskVo.getHandlerUser());
				}
			} else if (!FlowNode.Chk.equals(wfTaskVo.getSubNodeCode())) {
				// 未接受任务
				if (HandlerStatus.INIT.equals(wfTaskVo.getHandlerStatus())) {
					checkId = Long.parseLong(wfTaskVo.getHandlerIdKey());
				} else {
					// 正在处理
					throw new IllegalArgumentException("任务已被接收！接收人：" + wfTaskVo.getHandlerUser());
				}
			} else {
				throw new IllegalArgumentException("非查勘任务！");
			}
			// 接收任务
			// 暂存（正在处理）
			wfTaskHandleService.tempSaveTask(flowTaskId, checkId.toString(), userVo.getUserCode(), userVo.getComCode());
			
		} catch (Exception e) {
			logger.error("查勘接收任务失败！", e);
		}
	}

}	



