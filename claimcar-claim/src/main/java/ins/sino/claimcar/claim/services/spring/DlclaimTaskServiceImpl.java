package ins.sino.claimcar.claim.services.spring;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.Path;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carYxImage.service.CarXyImageService;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.DlclaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrplcodeDictVo;
import ins.sino.claimcar.claim.vo.PrploldCompensateVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.court.dlclaim.xyvo.ImageUrlbaseVo;
import ins.sino.claimcar.court.dlclaim.xyvo.LeafVo;
import ins.sino.claimcar.court.dlclaim.xyvo.NodeImageVo;
import ins.sino.claimcar.court.dlclaim.xyvo.PageVo;
import ins.sino.claimcar.dlclaim.vo.CheckVo;
import ins.sino.claimcar.dlclaim.vo.ClaimPhotosVo;
import ins.sino.claimcar.dlclaim.vo.ConfirmLossDiscussionVo;
import ins.sino.claimcar.dlclaim.vo.ConfirmLossVo;
import ins.sino.claimcar.dlclaim.vo.DriverVo;
import ins.sino.claimcar.dlclaim.vo.EstimateRepairVo;
import ins.sino.claimcar.dlclaim.vo.HistoricalClaimVo;
import ins.sino.claimcar.dlclaim.vo.InsuranceItemVo;
import ins.sino.claimcar.dlclaim.vo.InsuranceModificationVo;
import ins.sino.claimcar.dlclaim.vo.LaborVo;
import ins.sino.claimcar.dlclaim.vo.PolicyVo;
import ins.sino.claimcar.dlclaim.vo.ReportVo;
import ins.sino.claimcar.dlclaim.vo.SmallSparepartVo;
import ins.sino.claimcar.dlclaim.vo.SparepartVo;
import ins.sino.claimcar.dlclaim.vo.TransitClaimDocumentBaseVo;
import ins.sino.claimcar.dlclaim.vo.VehicleVo;
import ins.sino.claimcar.flow.service.RepairFactoryService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPersonLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPropLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrppheadVo;
import ins.sino.claimcar.regist.vo.PrpptextVo;

@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"} , timeout = 1000*60*10)
@Path("dlclaimTaskService")
public class DlclaimTaskServiceImpl implements DlclaimTaskService{
	private static Logger logger = LoggerFactory.getLogger(DlclaimTaskServiceImpl.class);
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
    private RegistService registService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	RepairFactoryService repairFactoryService;
	@Autowired
	private ClaimTextService claimTextService; 
	@Autowired
	private CarXyImageService carXyImageService;
	@Autowired
	ClaimInterfaceLogService claimInterfaceLogService;
	
	private final static String key="DHIC!@#456(*&";
	
    /**
     *理赔信息发送德联易控接口
     */
	@Override
	public void SendControlExpert(String registNo,SysUserVo userVo,String licenseNo,String lossCarMainId, String nodeFlag,String imageurl) {
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		String reqjsonStr="";//请求json
		String resjsonStr="";//返回json
		String url=SpringProperties.getProperty("CEReq_URL");;//请求地址
		try{
			TransitClaimDocumentBaseVo  transitClaimDocumentBaseVo=MakejsonContent(registNo,userVo,licenseNo,lossCarMainId,nodeFlag,imageurl);
			if(transitClaimDocumentBaseVo!=null){
				JsonConfig jsonConfig = new JsonConfig();
		        PropertyFilter filter = new PropertyFilter() {
		                public boolean apply(Object object, String fieldName, Object fieldValue) {
		                if(fieldValue instanceof List){
		                    List<Object> list = (List<Object>) fieldValue;
		                    if (list==null || list.size()==0) {
		                        return true;
		                    }
		                }
		                return null == fieldValue || "".equals(fieldValue);
		                }
		        };
		        jsonConfig.setJsonPropertyFilter(filter);
				JSONObject rejson = JSONObject.fromObject(transitClaimDocumentBaseVo,jsonConfig);
				if(rejson!=null){
					reqjsonStr=rejson.toString();//json格式
					System.out.println("=====================请求报文："+reqjsonStr);
				}
				
			}
			
			 byte[] a=postDL(url,reqjsonStr,"UTF-8");
			 if(a!=null){
				 resjsonStr=new String(a);
				 com.alibaba.fastjson.JSONObject jSONObject=JSON.parseObject(resjsonStr);
				 if(jSONObject!=null && "10000".equals(jSONObject.get("code"))){
					 logVo.setStatus("1");
				 }else{
					 logVo.setStatus("0"); 
					 if(jSONObject!=null){
						 logVo.setErrorMessage(jSONObject.get("message").toString());
						 logVo.setErrorCode(jSONObject.get("code").toString());
					 }
					 
				 }
			 }
		}catch(Exception e){
			logVo.setStatus("0"); 
			logVo.setErrorMessage("catch异常"+e.getMessage());
			logger.info("德联易控信息接口错误信息：",e);
		}finally{
			logVo.setRegistNo(registNo);
			logVo.setServiceType(lossCarMainId);//定损车辆主表Id
			logVo.setRequestTime(new Date());
			logVo.setPolicyNo(licenseNo);//车牌号
			logVo.setOperateNode(nodeFlag);//操作节点
			logVo.setRequestXml(reqjsonStr);
			logVo.setResponseXml(resjsonStr);
			logVo.setBusinessType(BusinessType.DL_CarInfo.name());
			logVo.setBusinessName(BusinessType.DL_CarInfo.getName());
			logVo.setCreateUser(userVo.getUserCode());
			logVo.setCreateTime(new Date());
			logVo.setRequestUrl(url);
			claimInterfaceLogService.save(logVo);
		}
		
	}
	
    private static class TrustAnyTrustManager implements X509TrustManager {
         //该方法检查客户端的证书，若不信任该证书则抛出异常。由于我们不需要对客户端进行认证，因此我们只需要执行默认的信任管理器的这个方法。
          //JSSE中，默认的信任管理器类为TrustManager。
         public void checkClientTrusted(X509Certificate[] chain, String authType)throws CertificateException {
         }
         //该方法检查服务器的证书，若不信任该证书同样抛出异常。通过自己实现该方法，可以使之信任我们指定的任何证书。在实现该方法时，也可以简单的不做任何处理，即一个空的函数体，由于不会抛出异常，它就会信任任何证书。
         public void checkServerTrusted(X509Certificate[] chain, String authType)throws CertificateException { }
         //返回受信任的X509证书数组.
         public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
           }
     }
            
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
         public boolean verify(String hostname, SSLSession session) {
                return true;
         }
     }
            
    /**
     * 德联易控https组装发送
     * @param url
     * @param content
     * @param charset
     * @return
     * @throws Exception
     */
    @SuppressWarnings("restriction")
	private byte[] postDL(String url, String content, String charset) throws Exception{
    	String md5Content=MD5.sign(content, key,charset);//加密签名
       /* SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },new java.security.SecureRandom());

		URL console = new URL(null, url, new sun.net.www.protocol.https.Handler());
        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
        conn.setSSLSocketFactory(sc.getSocketFactory());
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());*/
    	URL console =new URL(url);
    	HttpURLConnection conn=(HttpURLConnection)console.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        //设置请求头
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        conn.setRequestMethod("POST");
        conn.setRequestProperty("auth_id",CodeConstants.CEDHICCODE);
        conn.setRequestProperty("auth_token",md5Content);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setConnectTimeout(120*1000);//连接超时时间为120秒
        conn.setReadTimeout(90*1000);//读取超时时间为90
        conn.connect();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(content.getBytes(charset));
        // 刷新、关闭 
        out.flush();
        out.close();
        InputStream is = conn.getInputStream();
        if (is != null) {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                   outStream.write(buffer, 0, len);
            }
            is.close();
            return outStream.toByteArray();
             }
       return null;
       }
    
    private TransitClaimDocumentBaseVo MakejsonContent(String registNo,SysUserVo userVo, String licenseNo,String lossCarMainId, String nodeFlag,String url) throws Exception{
    	PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.controlExpert,userVo.getComCode());
    	 PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(registNo);
    	//基础报文组装
    	TransitClaimDocumentBaseVo baseVo=new TransitClaimDocumentBaseVo();
    	baseVo.setClaim_notification_no(registNo);
    	baseVo.setOperate_type(nodeFlag);//业务节点
    	baseVo.setEstimate_company_code(CodeConstants.CEDHICCODE);
        baseVo.setEstimate_company_id("536");//该字段为定损员的归属机构，暂定
    
    	baseVo.setBusinessType("2");//通用案件
    	baseVo.setRegist_type("1");//正常
    	//保单信息组装
    	List<PrpLCMainVo> prpLCMainVos=policyViewService.findPrpLCMainVoListByRegistNo(registNo);
    	List<PrpLCItemCarVo> citermsCarVos=new ArrayList<PrpLCItemCarVo>();
    	List<PolicyVo> policyVoList=new ArrayList<PolicyVo>();
    	if(prpLCMainVos!=null && prpLCMainVos.size()>0){
    		for(PrpLCMainVo cmainVo:prpLCMainVos){
    			citermsCarVos=cmainVo.getPrpCItemCars();
    			List<PrpLCInsuredVo> cinsuredVos=cmainVo.getPrpCInsureds();
    			PolicyVo policyVo=new PolicyVo();
    			policyVo.setPolicy_no(cmainVo.getPolicyNo());
    			policyVo.setCompany_code(CodeConstants.CEDHICCODE);
    			policyVo.setCompany_id("536");
    			policyVo.setCompany_name("鼎和财产");
    			if(citermsCarVos!=null && citermsCarVos.size()>0){
    				for(PrpLCItemCarVo carVo:citermsCarVos){
    					if(cmainVo.getRiskCode().equals(carVo.getRiskCode())){
    						policyVo.setPlate_color(carVo.getLicenseColorCode());
    						break;
    					}
    				}
    			}
    			if("1101".equals(cmainVo.getRiskCode())){
    				policyVo.setInsurance_category("1");//交强
    			}else{
    				policyVo.setInsurance_category("2");//商业
    			}
    			policyVo.setInsurance_fee(exNull(cmainVo.getSumPremium()));
    			policyVo.setInsurance_amount(exNull(cmainVo.getSumAmount()));
    			if(cmainVo.getStartDate()!=null){
    				Calendar c = Calendar.getInstance();
    				c.setTime(cmainVo.getStartDate());
    				if(cmainVo.getStartHour()!=null){
    				c.add(Calendar.HOUR,cmainVo.getStartHour().intValue());
    				}
    				policyVo.setStart_date(DateFormatString(c.getTime()));
    			}
    			if(cmainVo.getEndDate()!=null){
    				Calendar c=Calendar.getInstance();
    				c.setTime(cmainVo.getEndDate());
    				if(cmainVo.getEndHour()!=null){
        				c.add(Calendar.HOUR,cmainVo.getEndHour().intValue());
        			}
    				policyVo.setEnd_date(DateFormatString(c.getTime()));
    			}
    			policyVo.setSign_date(DateFormatString(cmainVo.getSignDate()));
    			
    			//车辆信息
    			if(citermsCarVos!=null && citermsCarVos.size()>0){
    				policyVo.setFirst_registration_date(DateFormatString(citermsCarVos.get(0).getEnrollDate()));
    				if(citermsCarVos.get(0).getWholeWeight()!=null){
    					policyVo.setTonnage(exNull((citermsCarVos.get(0).getWholeWeight()/1000)));
    				}
    				policyVo.setNew_vehicle_price(exNull(citermsCarVos.get(0).getPurchasePrice()));
    				policyVo.setVehicle_price(exNull(citermsCarVos.get(0).getActualValue()));
    				if(StringUtils.isNotBlank(citermsCarVos.get(0).getOtherNature())){
    					if("1".equals(citermsCarVos.get(0).getOtherNature().substring(4, 5))){
    						policyVo.setIs_transfer("1");
    					}else{
    						policyVo.setIs_transfer("0");
    					}
    					
    				}else{
    					policyVo.setIs_transfer("0");
    				}
    				PrplcodeDictVo  prplcodeDictVo=claimService.findPrplcodeDictByCodeAndcodeType("UseKind",citermsCarVos.get(0).getUseKindCode());
    				if(prplcodeDictVo!=null){
    					policyVo.setUsing_properties(prplcodeDictVo.getTcode());
    				}
    				policyVo.setEngine_no(citermsCarVos.get(0).getEngineNo());
    				//policyVo.setSeats_qualities(citermsCarVos.get(0).getTonCount()+"");
    				 Map<String, String> registRiskInfoMap = registRiskInfo(cmainVo.getRegistNo());
    			        //返回的vo
    			        int historicalAccident = 0;
    			        if (StringUtils.isNotEmpty(registRiskInfoMap.get("CI-No"))) {
    			            if (!StringUtils.isEmpty(registRiskInfoMap.get("CI-DangerNum"))) {
    			                historicalAccident = historicalAccident + Integer.parseInt(registRiskInfoMap.get("CI-DangerNum"));
    			                    if("1101".equals(cmainVo.getRiskCode())){
    			                        historicalAccident = historicalAccident-1;
    			                    }
    			            }
    			        }
    			        if (StringUtils.isNotEmpty(registRiskInfoMap.get("BI-No"))) {
    			            if (!StringUtils.isEmpty(registRiskInfoMap.get("BI-DangerNum"))) {
    			                historicalAccident = historicalAccident
    			                        + Integer.parseInt(registRiskInfoMap.get("BI-DangerNum"));
    			                    if(!"1101".equals(cmainVo.getRiskCode())){
    			                        historicalAccident = historicalAccident-1;
    			                    }
    			            }
    			        }
    			    if(StringUtils.isNotBlank(cmainVo.getOthFlag()) && "1".equals(cmainVo.getOthFlag().substring(0, 1))){
    			    	policyVo.setIs_continuous_policy("1");//是否续保
    			    }else{
    			    	policyVo.setIs_continuous_policy("0");//是否续保
    			    }
    			    
    				policyVo.setEvent_count(exNull(historicalAccident));//出险次数
    				policyVo.setModel_name(citermsCarVos.get(0).getBrandName());
    				policyVo.setModel_code(citermsCarVos.get(0).getModelCode());
    				policyVo.setBrand_model(citermsCarVos.get(0).getBrandName());
    				policyVo.setLicense_plate_no(citermsCarVos.get(0).getLicenseNo());
    				if(StringUtils.isNotBlank(citermsCarVos.get(0).getLicenseNo()) && citermsCarVos.get(0).getLicenseNo().contains("粤Z")){
    				policyVo.setHkmac_license_plate_no(citermsCarVos.get(0).getLicenseNo());
    				}
    				policyVo.setVehicle_frame_no(citermsCarVos.get(0).getFrameNo());
    				
    				
    			}
    			
    			//被保险人相关信息
    			if(cinsuredVos!=null && cinsuredVos.size()>0){
    				policyVo.setNature(cinsuredVos.get(0).getInsuredType());//客户类型
    				if(StringUtils.isNotBlank(cinsuredVos.get(0).getIdentifyType()) && ("550".equals(cinsuredVos.get(0).getIdentifyType()) || "553".equals(cinsuredVos.get(0).getIdentifyType()) || "00".equals(cinsuredVos.get(0).getIdentifyType()))){
    					policyVo.setInsured_identify_type("99");
    				}else if(StringUtils.isNotBlank(cinsuredVos.get(0).getIdentifyType())){
    					policyVo.setInsured_identify_type(cinsuredVos.get(0).getIdentifyType());
    				}else{
    					policyVo.setInsured_identify_type("99");//证件类型
    				}
    				if("1".equals(configValueVo.getConfigValue())){
    					policyVo.setInsured_name(cinsuredVos.get(0).getInsuredName());//被保险人姓名
        				policyVo.setInsured_identify_no(cinsuredVos.get(0).getIdentifyNumber());//被保险人证件号码	
    				}
    				
    				
    			}
    			
    			policyVo.setRemark("保单信息明细");
    			
    			 double claimSum = 0;//本保单赔款总额
    		     int claimTimes = 0;//本保单赔款次数
    		     // 新理赔
    	            List<PrpLCompensateVo> compensateNewVoList = compensateTaskService.queryCompensateByOther(null, "N", cmainVo.getPolicyNo(),"1");
    	            if (compensateNewVoList != null && compensateNewVoList.size() > 0) {
    	                for (PrpLCompensateVo compensateVo : compensateNewVoList) {
    	                    if ("1".equals(compensateVo.getUnderwriteFlag())) {
    	                        // claimSum =
    	                        // claimSum.add(compensateVo.getSumPaidAmt());
    	                        claimSum = claimSum + DataUtils.NullToZero(compensateVo.getSumPaidAmt()).doubleValue();
    	                        claimTimes = claimTimes + 1;
    	                    }
    	                }
    	            }

    	            // 旧理赔
    	            List<PrploldCompensateVo> oldcompensateVos = compensateTaskService.findPrpoldCompensateBypolicyNo(cmainVo.getPolicyNo());
    	            if (oldcompensateVos != null && oldcompensateVos.size() > 0) {
    	                for (PrploldCompensateVo oldVo : oldcompensateVos) {
    	                    if (oldVo.getEndcaseDate() != null) {
    	                        claimSum = claimSum + DataUtils.NullToZero(oldVo.getSumPaid()).doubleValue();
    	                        claimTimes = claimTimes + 1;
    	                    }
    	                }
    	            }
    	            policyVo.setPaid_times(claimTimes+"");//赔款次数
    	            policyVo.setPaid_total(claimSum+"");//赔款总计
    	            policyVo.setIssue_company_code(cmainVo.getComCode());
    	            if(StringUtils.isNotBlank(cmainVo.getComCode())){
    	            	policyVo.setIssue_company_name(codeTranService.transCode("ComCodeFull",cmainVo.getComCode()));
    				}
    	            PrplcodeDictVo  prplcodeDictVo1=claimService.findPrplcodeDictByCodeAndcodeType("CarType",citermsCarVos.get(0).getCarType());
    				if(prplcodeDictVo1!=null){
    					 policyVo.setCar_kind_code(prplcodeDictVo1.getTcode());
    				}
    	           
    				policyVo.setUsed_years(exNull(citermsCarVos.get(0).getUseYears()));
    				if("A".equals(citermsCarVos.get(0).getCountryNature())){
    					policyVo.setImport_domestic("进口");
    				}else{
    					policyVo.setImport_domestic("国产");
    				}
    				policyVo.setAgency_name("鼎和财产保险代理公司");
    				
    				//保单险别集合
    				List<InsuranceItemVo> insuranceItemVoList=new ArrayList<InsuranceItemVo>();
    				List<PrpLCItemKindVo> prpLCItemKindVos=cmainVo.getPrpCItemKinds();
    				if(prpLCItemKindVos!=null && prpLCItemKindVos.size()>0){
    					for(PrpLCItemKindVo itemKindVo:prpLCItemKindVos){
    						InsuranceItemVo insuranceItemVo=new InsuranceItemVo();
    						insuranceItemVo.setCode(itemKindVo.getKindCode());//险别-待定
    						insuranceItemVo.setName(itemKindVo.getKindName());//待定
    						if(itemKindVo.getAmount()!=null){
    							insuranceItemVo.setInsurance_amout(exNull(itemKindVo.getAmount()));//保险限额
    						}else{
    							insuranceItemVo.setInsurance_amout("0.00");
    						}
    						if(itemKindVo.getQuantity()!=null){
    							insuranceItemVo.setUnit_count(itemKindVo.getQuantity().intValue()+"");
    						}else{
    							insuranceItemVo.setUnit_count("0");
    						}
    						insuranceItemVo.setUnit_amount(exNull(itemKindVo.getUnitAmount()));//单位保额
    						
    						if("1".equals(itemKindVo.getNoDutyFlag())){//承保不计免费
    							insuranceItemVo.setNon_deductible("1");
    						}else if("0".equals(itemKindVo.getNoDutyFlag())){
    							insuranceItemVo.setNon_deductible("0");
    						}else{
    							insuranceItemVo.setNon_deductible("0");
    						}
    						if(itemKindVo.getDeductibleRate()!=null){
    							insuranceItemVo.setDeductible_rate(exNull(itemKindVo.getDeductibleRate()));
    						}else{
    							insuranceItemVo.setDeductible_rate("0.00");//免赔率
    						}
    						insuranceItemVo.setDeductible_money(exNull(itemKindVo.getDeductible()));
    						insuranceItemVo.setInsurance_fee(exNull(itemKindVo.getBenchMarkPremium()));//标准保费
    						insuranceItemVo.setPremiums(exNull(itemKindVo.getAmount()));//保费
    						insuranceItemVo.setRemark("险别信息");
    						insuranceItemVoList.add(insuranceItemVo);
    					}
    				}
    				if(insuranceItemVoList!=null && insuranceItemVoList.size()>0){
    					policyVo.setInsuranceItemVos(insuranceItemVoList);
    				}
    				
    				
    				//保单出险记录
    				List<HistoricalClaimVo> historicalClaimVoList=new ArrayList<HistoricalClaimVo>();
    				List<PrpLClaimVo> claimVos=claimService.findPrpLClaimVosByPolicyNo(cmainVo.getPolicyNo());
    				if(claimVos!=null && claimVos.size()>0){
    					for(PrpLClaimVo claimVo:claimVos){
    						HistoricalClaimVo hclaimVo=new HistoricalClaimVo();
    						if("1101".equals(claimVo.getRiskCode())){
    							hclaimVo.setInsurance_category("1");//交强
    						}else{
    							hclaimVo.setInsurance_category("2");//商业
    						}
    						hclaimVo.setPaid_times(claimTimes+"");
    						hclaimVo.setPay_claim_number(claimVo.getCaseNo());//赔案号
    						hclaimVo.setClaim_date(DateFormatString(claimVo.getEndCaseTime()));
    						hclaimVo.setEstimate_amount(exNull(claimVo.getSumDefLoss()));
    						hclaimVo.setClaim_date(DateFormatString(claimVo.getDamageTime()));
    						hclaimVo.setEvent_address(codeTranService.transCode("AreaCode",claimVo.getDamageAreaCode()));
    						hclaimVo.setLicense_plate_no(citermsCarVos.get(0).getLicenseNo());//车牌号
    						hclaimVo.setReport_date(DateFormatString(claimVo.getReportTime()));
    						if(claimVo.getEndCaseTime()!=null){
    							hclaimVo.setPaid_amount(exNull(claimVo.getSumDefLoss()));//结案赔款金额
    						}
    						historicalClaimVoList.add(hclaimVo);
    					}
    				}
    				if(historicalClaimVoList!=null && historicalClaimVoList.size()>0){
    					policyVo.setHistoricalClaimVos(historicalClaimVoList);
    				}
    				
    				//批单信息
    				List<InsuranceModificationVo> insuranceModificationVos =new ArrayList<InsuranceModificationVo>();
    				List<PrppheadVo> vo1s =registQueryService.findByPolicyNo(cmainVo.getPolicyNo());
    				if(vo1s!=null && vo1s.size()>0){
    					for(PrppheadVo headVo:vo1s){
    						InsuranceModificationVo cationVo=new InsuranceModificationVo();
    					 List<PrpptextVo> prppTextVoList =registQueryService.findPrppTextByPolicyNo(headVo.getEndorseNo());
    					   String endorContent = "";
							for(PrpptextVo prpptextVo :prppTextVoList){
								if(StringUtils.isNotBlank(prpptextVo.getEndorseText())){
									endorContent = endorContent+prpptextVo.getEndorseText();
								}
							}
							cationVo.setModification_bill_no(headVo.getEndorseNo());
							cationVo.setModification_time(DateFormatString(headVo.getEndorDate()));
							if(StringUtils.isNotBlank(endorContent)){
								cationVo.setContent(endorContent);//批改内容
							}else{
								cationVo.setContent("批单内容");//批改内容
							}
							insuranceModificationVos.add(cationVo);
    					}
    				}
    				if(insuranceModificationVos!=null && insuranceModificationVos.size()>0){
    					policyVo.setInsuranceModification(insuranceModificationVos);
    				}
    				policyVoList.add(policyVo);
    	        }
    		}
    	     baseVo.setPolicy(policyVoList);//保单信息
    	     
    	 //---------------------------------------------------------------------------------------------------
    	     //报案信息
    	     ReportVo reportVo=new ReportVo();
    	     PrpLRegistExtVo prpLRegistExtVo=prpLRegistVo.getPrpLRegistExt();
    	     List<PrpLRegistPersonLossVo> prpLRegistPersonLossVos= prpLRegistVo.getPrpLRegistPersonLosses();
    	     List<PrpLRegistPropLossVo> prpLRegistPropLossVos=prpLRegistVo.getPrpLRegistPropLosses();
    	     if(prpLRegistVo.getCancelTime()!=null && prpLRegistExtVo!=null){
    	    	 reportVo.setCancel_date(DateFormatString(prpLRegistVo.getCancelTime()));
    	    	 if("1".equals(prpLRegistExtVo.getCancelReason()) || "2".equals(prpLRegistExtVo.getCancelReason())){
    	    		 reportVo.setCancel_reason("1");//注销原因
    	    	 }else if("3".equals(prpLRegistExtVo.getCancelReason())){
    	    		 reportVo.setCancel_reason("2");
    	    	 }else if("4".equals(prpLRegistExtVo.getCancelReason())){
    	    		 reportVo.setCancel_reason("3");
    	    	 }else{
    	    		 reportVo.setCancel_reason("1");
    	    	 }
    	     }
    	     int sumHurt=0;//受伤人数
    	     if(prpLRegistPersonLossVos!=null && prpLRegistPersonLossVos.size()>0){
    	    	 for(PrpLRegistPersonLossVo personLossVo:prpLRegistPersonLossVos){
    	    		 sumHurt=sumHurt+personLossVo.getInjuredcount()+personLossVo.getDeathcount();
    	    	 }
    	    	 reportVo.setIs_protect_loss("1");//是否有人伤
    	     }else{
    	    	 reportVo.setIs_protect_loss("0");
    	     }
    	     reportVo.setHurt_num(sumHurt+"");
    	     reportVo.setAccept_ind("1");//受理标志
    	     if(prpLRegistPropLossVos!=null && prpLRegistPropLossVos.size()>0){
    	    	 reportVo.setIs_person_injured("1");//是否有物损
    	     }else{
    	    	 reportVo.setIs_person_injured("0");
    	     }
    	     if("1".equals(prpLRegistVo.getIsoffSite())){
    	    	 reportVo.setIs_outof_local_claim("1");//是否异地出险
    	     }else{
    	    	 reportVo.setIs_outof_local_claim("0");//是否异地出险
    	     }
    	     if("1".equals(prpLRegistExtVo.getIsSubRogation())){
    	    	 reportVo.setSubrogation_flag("1");
    	     }else{
    	    	 reportVo.setSubrogation_flag("0");//是否代位
    	     }
    	    	 reportVo.setIs_closed("0");//是否结案
    	    
    	     reportVo.setIs_simple_claim("0");//是否简易赔案
    	     PrplcodeDictVo  prplcodeDictVo0=claimService.findPrplcodeDictByCodeAndcodeType("IndemnityDuty",prpLRegistExtVo.getObliGation());
				if(prplcodeDictVo0!=null){
					reportVo.setAccident_liability(prplcodeDictVo0.getTcode());//事故责任
				}
			 PrplcodeDictVo  prplcodeDictVo2=claimService.findPrplcodeDictByCodeAndcodeType("DamageCode",prpLRegistVo.getDamageCode());
				if(prplcodeDictVo2!=null){
					reportVo.setEvent_reason_type(prplcodeDictVo2.getTcode());
				}else{
					reportVo.setEvent_reason_type("A10095A");//其他
				}
				
				 PrplcodeDictVo  prplcodeDictVo3=claimService.findPrplcodeDictByCodeAndcodeType("AccidentManageType",prpLRegistExtVo.getManageType());
				if(prplcodeDictVo3!=null){
					reportVo.setEvent_process_mode(prplcodeDictVo3.getTcode());//事故处理类型
				}else{
					reportVo.setEvent_process_mode("9");//其他
				}
				if(StringUtils.isNotBlank(prpLRegistExtVo.getIsAlarm())){
					reportVo.setIs_police(prpLRegistExtVo.getIsAlarm());//是否报警
				}else{
					reportVo.setIs_police("0");
				}
				reportVo.setIs_mutual_collision_self_compensation(prpLRegistExtVo.getIsClaimSelf());//是否互碰自赔
				if(prplcodeDictVo2!=null){
					reportVo.setDanger_cause(prplcodeDictVo2.getTcode());//出险原因
				}else{
					reportVo.setDanger_cause("A10095A");//其他
				}
				if(prpLCMainVos!=null && prpLCMainVos.size()>0){
					if(prpLRegistVo.getDamageTime()!=null && prpLCMainVos.get(0).getStartDate()!=null && DateString(prpLRegistVo.getDamageTime()).equals(DateString(prpLCMainVos.get(0).getStartDate()))){
						reportVo.setIs_take_effect("1");//是否即时出险insuredPhone
	    			}else{
	    				reportVo.setIs_take_effect("0");//是否即时出险insuredPhone
	    			}
				}
				if("1".equals(configValueVo.getConfigValue())){
					reportVo.setDriver_name(prpLRegistVo.getDriverName());//驾驶员姓名
					reportVo.setReporter(prpLRegistVo.getReportorName());//报案人姓名
					reportVo.setReporter_phone(prpLRegistVo.getReportorPhone());//报案人电话
				}
				reportVo.setDanger_address(prpLRegistVo.getDamageAddress());//出险所在地
				reportVo.setDanger_time(DateFormatString(prpLRegistVo.getDamageTime()));
				reportVo.setDanger_area(prpLRegistVo.getDamageAddress());
				reportVo.setDanger_desc(prpLRegistExtVo.getDangerRemark());
				reportVo.setRemark("报案信息");
				reportVo.setReport_type(prpLRegistExtVo.getIsOnSitReport());//报案方式
				reportVo.setNotification_time(DateFormatString(prpLRegistVo.getReportTime()));//报案日期
				reportVo.setStatus(nodeFlag);//业务节点isCantravel
				reportVo.setIs_subject_normal(prpLRegistExtVo.getIsCantravel());//标的车是否可行
				reportVo.setIs_rescue(prpLRegistExtVo.getIsRescue());//是否施救
				reportVo.setIs_self_survey(prpLRegistExtVo.getIsCheckSelf());//是否自助查勘
				if(StringUtils.isNotBlank(prpLRegistVo.getInsuredPhone())){
					reportVo.setIs_insured_phone("1");//是否提供被保险人电话
				}else{
					reportVo.setIs_insured_phone("0");
				}
				baseVo.setReport(reportVo);//报案信息
			//---------------------------------------------------------------------------------------------------
				
			String personFlag="0";//是否包含人伤
			String losssonFlag="0";//是否包含物损
			//查勘信息
			if(!"01".equals(nodeFlag)){
				List<PrpLCheckCarInfoVo> prpLCheckCarInfoVos=checkTaskService.findPrpLCheckCarInfoVoListByOther(registNo, licenseNo);
				if(prpLCheckCarInfoVos!=null && prpLCheckCarInfoVos.size()>0){
				 PrpLCheckVo  prpLCheckVo=checkTaskService.findCheckVoByRegistNo(registNo);
				 PrpLCheckDutyVo prpLCheckDutyVo=checkTaskService.findCheckDuty(registNo, 1);
				 PrpLCheckTaskVo prpLCheckTaskVo=new PrpLCheckTaskVo();
				 prpLCheckTaskVo =prpLCheckVo.getPrpLCheckTask();
				 List<PrpLCheckPropVo> prpLCheckPropVos= prpLCheckTaskVo.getPrpLCheckProps();
				 List<PrpLCheckPersonVo> prpLCheckPersonVos=prpLCheckTaskVo.getPrpLCheckPersons();
				 List<PrpLCheckCarVo> prpLCheckCarVos=checkTaskService.findCheckCarVo(registNo);
				 CheckVo checkVo=new CheckVo();
				 checkVo.setCheck_start_time(DateFormatString(prpLCheckTaskVo.getCheckDate()));//查勘开始时间
				 checkVo.setCheck_end_time(DateFormatString(prpLCheckVo.getChkSubmitTime()));//查勘提交时间
				 checkVo.setCheck_addr(prpLCheckTaskVo.getCheckAddress());//查勘地点
				 checkVo.setAppoint_check_addr(prpLCheckTaskVo.getCheckAddress());//约定查勘地点
				 checkVo.setChecker_name(prpLCheckTaskVo.getChecker());//查勘人
				 checkVo.setChecker_code(prpLCheckTaskVo.getCheckerCode());
				 checkVo.setChecker_phone1(prpLCheckTaskVo.getCheckerPhone());//查勘人电话
				 checkVo.setDanger_address(prpLRegistVo.getDamageAddress());
				 if(prplcodeDictVo2!=null){
					 checkVo.setDanger_cause(prplcodeDictVo2.getTcode());//出险原因
					}else{
					 checkVo.setDanger_cause("A10095A");//其他
				  }
				 checkVo.setDanger_desc(prpLRegistExtVo.getDangerRemark());
				 checkVo.setCase_type("2");//案件类型
				 PrplcodeDictVo  prplcodeDictVo4=claimService.findPrplcodeDictByCodeAndcodeType("AccidentDutyType",prpLCheckVo.getDamageTypeCode());
					if(prplcodeDictVo4!=null){
						checkVo.setEvent_type(prplcodeDictVo4.getTcode());//事故类型
					}else{
						checkVo.setEvent_type("2");//事故类型--暂定
					}
					if(prplcodeDictVo3!=null){
						checkVo.setEvent_process_mode(prplcodeDictVo3.getTcode());//事故处理类型
					}else{
						checkVo.setEvent_process_mode("9");//其他
					}
					if(prpLCheckDutyVo!=null && StringUtils.isNotBlank(prpLCheckDutyVo.getIndemnityDuty())){
						if("0".equals(prpLCheckDutyVo.getIndemnityDuty())){
							checkVo.setEvent_duty_ratio("100.00");
						}else if("1".equals(prpLCheckDutyVo.getIndemnityDuty())){
							checkVo.setEvent_duty_ratio("70.00");
						}else if("2".equals(prpLCheckDutyVo.getIndemnityDuty())){
							checkVo.setEvent_duty_ratio("50.00");
						}else if("3".equals(prpLCheckDutyVo.getIndemnityDuty())){
							checkVo.setEvent_duty_ratio("30.00");
						}else{
							checkVo.setEvent_duty_ratio("0.00");
						}
					}else{
						checkVo.setEvent_duty_ratio("50.00");//事故责任比例
					}
					//checkVo.setNo_fault_compensate("0");//无责代赔
					if("DM12".equals(prpLCheckVo.getDamageCode())){
						if("水淹1级".equals(prpLCheckVo.getWaterLoggingLevel())){
							checkVo.setWater_level("2");
						}else if("水淹2级".equals(prpLCheckVo.getWaterLoggingLevel())){
							checkVo.setWater_level("3");
						}else if("水淹3级".equals(prpLCheckVo.getWaterLoggingLevel())){
							checkVo.setWater_level("4");
						}else{
							checkVo.setWater_level("5");//水淹等级
						}
						
					}
					if("1".equals(prpLCheckVo.getIsSubRogation())){
						checkVo.setSubrogation_flag("1");
					}else{
						checkVo.setSubrogation_flag("0");//是否需要代位
					}
					checkVo.setIs_rescue(prpLRegistExtVo.getIsRescue());//是否需要施救
					checkVo.setIs_person_injured(prpLCheckVo.getIsPersonLoss());//是否包含人伤
					checkVo.setBuckle_ded_reason("无");//加扣免赔原因
					checkVo.setIs_mutual_collision_self_compensation(prpLCheckVo.getIsClaimSelf());//是否互碰自赔
					checkVo.setIs_car_insurance("1");//车辆是否与保单相符
					checkVo.setCheck_desc(prpLCheckTaskVo.getContexts());//查勘意见
					
					PrplcodeDictVo  prplcodeDictVo5=claimService.findPrplcodeDictByCodeAndcodeType("CheckType",prpLCheckVo.getCheckType());
					if(prplcodeDictVo5!=null){
						checkVo.setCheck_type(prplcodeDictVo5.getTcode());//查勘类型
					}else{
						checkVo.setCheck_type("1");
					}
					
					checkVo.setAudit_name(prpLCheckTaskVo.getChecker());//查勘人
					checkVo.setAudit_code(prpLCheckTaskVo.getCheckerCode());
					checkVo.setAudit_opinion(prpLCheckTaskVo.getContexts());
					checkVo.setAudit_time(DateFormatString(prpLCheckVo.getChkSubmitTime()));//查勘时间
					
					//查勘驾驶员信息
					if(prpLCheckCarVos!=null && prpLCheckCarVos.size()>0){
						for(PrpLCheckCarVo carVo:prpLCheckCarVos){
							DriverVo driverVo=new DriverVo();
							PrpLCheckCarInfoVo prpLCheckCarInfoVo=carVo.getPrpLCheckCarInfo();
						  if(licenseNo.equals(prpLCheckCarInfoVo.getLicenseNo())){
							PrpLCheckDriverVo prpLCheckDriverVo=carVo.getPrpLCheckDriver();
							driverVo.setLicense_plate_no(prpLCheckCarInfoVo.getLicenseNo());
							driverVo.setDriver_name(prpLCheckDriverVo.getDriverName());
							driverVo.setDriving_license_no(prpLCheckDriverVo.getDrivingLicenseNo());
							PrplcodeDictVo  prplcodeDictVo6=claimService.findPrplcodeDictByCodeAndcodeType("IdentifyType",prpLCheckDriverVo.getIdentifyType());
							if(prplcodeDictVo6!=null){
								driverVo.setIdentify_type(prplcodeDictVo6.getTcode());
		    				}else{
		    					driverVo.setIdentify_type("99");//证件类型
		    				}
							driverVo.setAllow_driving_vehicle(prpLCheckDriverVo.getDrivingCarType());//准驾车型
							driverVo.setIs_driving_license_effective("1");//驾驶员证件是否有效
							driverVo.setDriving_license_date(DateFormatString(prpLCheckDriverVo.getAcceptLicenseDate()));//初次领证日期
							checkVo.setDriver(driverVo);
							break;
						 }
					 }
					}
					
					//车辆信息
					if(prpLCheckCarVos!=null && prpLCheckCarVos.size()>0){
						for(PrpLCheckCarVo carVo:prpLCheckCarVos){
							VehicleVo vehicleVo=new VehicleVo();
							PrpLCheckCarInfoVo prpLCheckCarInfoVo=carVo.getPrpLCheckCarInfo();
						if(licenseNo.equals(prpLCheckCarInfoVo.getLicenseNo())){
							if(1==carVo.getSerialNo()){
								vehicleVo.setSubject_third("1");//是否标的
								if(citermsCarVos.get(0).getWholeWeight()!=null){
									vehicleVo.setTonnage(exNull((citermsCarVos.get(0).getWholeWeight()/1000)));//整备重量
			    				}
								vehicleVo.setVehicle_price(exNull(citermsCarVos.get(0).getActualValue()));//车俩实际价值
							}else{
								vehicleVo.setSubject_third("0");
							}
							if("1".equals(configValueVo.getConfigValue())){
								vehicleVo.setOwner(prpLCheckCarInfoVo.getCarOwner());//车主
							}
							vehicleVo.setLicense_plate_no(prpLCheckCarInfoVo.getLicenseNo());//车牌号
							if(prpLCMainVos!=null && prpLCMainVos.size()>0){
								for(PrpLCMainVo cmainVo:prpLCMainVos){
									if("1101".equals(cmainVo.getRiskCode())){
										vehicleVo.setBz_policy_no(cmainVo.getPolicyNo());
									}
								}
								
							}
							vehicleVo.setBz_company_code(CodeConstants.CEDHICCODE);
							vehicleVo.setSub_company_name("鼎和财产保险股份有限公司");
							vehicleVo.setModel_name(prpLCheckCarInfoVo.getBrandName());
							vehicleVo.setModel_code(prpLCheckCarInfoVo.getModelCode());
							vehicleVo.setVin(prpLCheckCarInfoVo.getVinNo());
							vehicleVo.setVehicle_brand_code(prpLCheckCarInfoVo.getBrandName());//车辆品牌
							//vehicleVo.setVehicle_series_code(prpLCheckCarInfoVo.getSeriName());//车系
							//vehicleVo.setGroup_veh_code(prpLDlossCarInfoVo.getGroupName());//车组
							 PrplcodeDictVo  prplcodeDictVo7=claimService.findPrplcodeDictByCodeAndcodeType("CarType",citermsCarVos.get(0).getCarType());
			    				if(prplcodeDictVo7!=null && 1==carVo.getSerialNo()){
			    					 vehicleVo.setCar_kind_code(prplcodeDictVo7.getTcode());
			    				}else if(1!=carVo.getSerialNo()){
			    					 vehicleVo.setCar_kind_code(prpLCheckCarInfoVo.getPlatformCarKindCode());//车型代码
			    				}
							
			    			vehicleVo.setBrand_model(prpLCheckCarInfoVo.getBrandName());
			    			vehicleVo.setLicense_plate_type(prpLCheckCarInfoVo.getLicenseType());//号牌种类
			    			vehicleVo.setEngine_no(prpLCheckCarInfoVo.getEngineNo());//发动机号
			    			vehicleVo.setPlate_color(prpLCheckCarInfoVo.getLicenseColor());//号牌底色
			    			if("1".equals(carVo.getLossFlag())){//理赔这边1代表无损失，0代表有损失
			    				vehicleVo.setIs_loss("0");//是否有车损
			    			}else{
			    				vehicleVo.setIs_loss("1");//是否有车损
			    			}
			    			String losspartStr="";//损失部位
			    			if(StringUtils.isNotBlank(carVo.getLossPart())){
			    				String[] lossparts=carVo.getLossPart().split(",");
			    				if(lossparts!=null && lossparts.length>0){
			    					for(int i=0;i<lossparts.length;i++){
			    						PrplcodeDictVo  prplcodeDictVo9=claimService.findPrplcodeDictByCodeAndcodeType("LossPart",lossparts[i]);
			    						if(prplcodeDictVo9 !=null){
			    							losspartStr=losspartStr+prplcodeDictVo9.getTcode();
			    							if(lossparts.length>1 && i<(lossparts.length-1))
			    							losspartStr=losspartStr+",";
			    						}
			    					}
			    				}
			    			}
			    			vehicleVo.setLoss_part_data(losspartStr);//损失部位
			    			// fix by LiYi 拿到标的车 ,判断是否上牌，默认上牌
			    			if(1==carVo.getSerialNo()){
			    			  boolean registLicenseNo = true;
			    			  if(citermsCarVos!=null&& !citermsCarVos.isEmpty()){
			    				PrpLCItemCarVo cItemCarVo = citermsCarVos.get(0);
			    				String otherNature = cItemCarVo.getOtherNature();
			    				if(StringUtils.isNotEmpty(otherNature)){
			    					String substring = otherNature.substring(6,7);
			    					if(substring.equals("1")){
			    						registLicenseNo = false;
			    					}
			    				}else{
			    					// otherNature为空 , 默认为未上牌
			    					registLicenseNo = false;
			    				}
			    			  }else{
			    				registLicenseNo = false;
			    			  }
			    			  if(registLicenseNo){
			    				  vehicleVo.setHas_car_license("1");//是否已上牌
			    			  }else{
			    				  vehicleVo.setHas_car_license("0");
			    			  }
			    			 
			    			}
			    			if(prpLCheckPropVos!=null && prpLCheckPropVos.size()>0){
			    				vehicleVo.setIs_protect_loss("1");//是否包含物损
			    				losssonFlag="1";
			    			}else{
			    				vehicleVo.setIs_protect_loss("0");
			    			}
			    			if(prpLCheckPersonVos!=null && prpLCheckPersonVos.size()>0){
			    				vehicleVo.setIs_person_injured("1");//是否包含人伤
			    				personFlag="1";
			    			}else{
			    				vehicleVo.setIs_person_injured("0");//是否包含人伤
			    			}
			    			if(1==carVo.getSerialNo()){
			    				PrplcodeDictVo  prplcodeDictVo8=claimService.findPrplcodeDictByCodeAndcodeType("UseKind",citermsCarVos.get(0).getUseKindCode());
			    				if(prplcodeDictVo8!=null){
			    					vehicleVo.setUsing_properties(prplcodeDictVo8.getTcode());
			    				}
			    			}
			    			vehicleVo.setRescue_amount(exNull(carVo.getRescueFee()));//施救金额
			    			vehicleVo.setEstimated_loss_amount(exNull(carVo.getLossFee()));//估损金额
			    			vehicleVo.setRemark("查勘车辆信息");
			    			checkVo.setVehicle(vehicleVo);
			    			break;
						}						
					 }
					}
					baseVo.setCheck(checkVo);//查勘信息
			  }	
			}
			
			//定损信息
			ConfirmLossVo ConfirmLossVo=new ConfirmLossVo();
			//01--表示报案节点， 03--表示查勘节点
			if(!"01".equals(nodeFlag) && !"03".equals(nodeFlag) && StringUtils.isNotBlank(lossCarMainId)){
				PrpLDlossCarMainVo  prpLDlossCarMainVo =lossCarService.findLossCarMainById(Long.valueOf(lossCarMainId));
				PrpLDlossCarInfoVo prpLDlossCarInfoVo=prpLDlossCarMainVo.getLossCarInfoVo();//车辆信息
				ConfirmLossVo.setEstimate_task_no(lossCarMainId);//保险公司任务号，传Id
				List<PrpLDlossCarMainVo> losscarMainVos=lossCarService.findLossCarMainByRegistNo(registNo);
				int addcount=0;
				if(losscarMainVos!=null && losscarMainVos.size()>0){
					for(PrpLDlossCarMainVo losscarVo:losscarMainVos){
						if(StringUtils.isNotBlank(prpLDlossCarMainVo.getLicenseNo()) && prpLDlossCarMainVo.getLicenseNo().equals(losscarVo.getLicenseNo()) && "DLCarAdd".equals(losscarVo.getFlowFlag())){
							addcount=addcount+1;
						}
					}
				}
				String losspartStr1="";//损失部位
    			if(StringUtils.isNotBlank(prpLDlossCarMainVo.getLossPart())){
    				String[] lossparts=prpLDlossCarMainVo.getLossPart().split(",");
    				if(lossparts!=null && lossparts.length>0){
    					for(int i=0;i<lossparts.length;i++){
    						PrplcodeDictVo  prplcodeDictVo9=claimService.findPrplcodeDictByCodeAndcodeType("LossPart",lossparts[i]);
    						if(prplcodeDictVo9 !=null){
    							losspartStr1=losspartStr1+prplcodeDictVo9.getTcode();
    							if(lossparts.length>1 && i<(lossparts.length-1))
    							losspartStr1=losspartStr1+",";
    						}
    					}
    				}
    			}
    			ConfirmLossVo.setLoss_part_data(losspartStr1);//损失部位
				ConfirmLossVo.setEstimate_count("1");
				ConfirmLossVo.setAdd_count(addcount+"");//追加次数
				ConfirmLossVo.setVin(prpLDlossCarInfoVo.getVinNo());//车架号
				if("1".equals(prpLDlossCarMainVo.getDeflossCarType()) && citermsCarVos!=null && citermsCarVos.size()>0){//标的车
					ConfirmLossVo.setFirst_registration_date(DateFormatString(citermsCarVos.get(0).getEnrollDate()));
					PrplcodeDictVo  prplcodeDictVo10=claimService.findPrplcodeDictByCodeAndcodeType("UseKind",citermsCarVos.get(0).getUseKindCode());
    				if(prplcodeDictVo10!=null){
    					ConfirmLossVo.setUsing_properties(prplcodeDictVo10.getTcode());
    				}
    				ConfirmLossVo.setVehicle_price(exNull(citermsCarVos.get(0).getActualValue()));
    				ConfirmLossVo.setNew_vehicle_price(exNull(citermsCarVos.get(0).getPurchasePrice()));
    				if(citermsCarVos.get(0).getWholeWeight()!=null){
    					ConfirmLossVo.setTonnage(exNull((citermsCarVos.get(0).getWholeWeight()/1000)));
    				}
    				ConfirmLossVo.setUsed_years(exNull(citermsCarVos.get(0).getUseYears()));
    				
				}
				if("1".equals(prpLDlossCarMainVo.getIsWaterFloaded())){
					if("水淹1级".equals(prpLDlossCarMainVo.getWaterFloodedLevel())){
						ConfirmLossVo.setWater_level("2");
					}else if("水淹2级".equals(prpLDlossCarMainVo.getWaterFloodedLevel())){
						ConfirmLossVo.setWater_level("3");
					}else if("水淹3级".equals(prpLDlossCarMainVo.getWaterFloodedLevel())){
						ConfirmLossVo.setWater_level("4");
					}else{
						ConfirmLossVo.setWater_level("5");//水淹等级
					}
					
				}
				ConfirmLossVo.setLicense_plate_type(prpLDlossCarInfoVo.getLicenseType());//号牌种类
				ConfirmLossVo.setModel_code(prpLDlossCarInfoVo.getModelCode());
				ConfirmLossVo.setModel_name(prpLDlossCarInfoVo.getModelName());//车型名称
				if(StringUtils.isNotBlank(prpLDlossCarInfoVo.getIdentifyType()) && ("550".equals(prpLDlossCarInfoVo.getIdentifyType()) || "553".equals(prpLDlossCarInfoVo.getIdentifyType()) || "00".equals(prpLDlossCarInfoVo.getIdentifyType()))){
					ConfirmLossVo.setDriving_identify_type("99");
				}else if(StringUtils.isNotBlank(prpLDlossCarInfoVo.getIdentifyType())){
					ConfirmLossVo.setDriving_identify_type(prpLDlossCarInfoVo.getIdentifyType());
				}else{
					ConfirmLossVo.setDriving_identify_type("99");//证件类型
				}
				ConfirmLossVo.setEstimate_start_time(DateFormatString(prpLDlossCarMainVo.getCreateTime()));//定损开始时间
				ConfirmLossVo.setEstimate_end_time(DateFormatString(prpLDlossCarMainVo.getDefEndDate()));//定损完成时间
				if("14".equals(nodeFlag)){//核损信息
					ConfirmLossVo.setUnder_start_Time(DateFormatString(prpLDlossCarMainVo.getUnderWriteEndDate()));//核损开始时间
					ConfirmLossVo.setUnder_end_Time(DateFormatString(prpLDlossCarMainVo.getUnderWriteEndDate()));//核损结束时间
				}
				if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
					ConfirmLossVo.setLoss_task("1");//损失任务--标的车
				}else{
					ConfirmLossVo.setLoss_task("2");//损失任务--三者车
				}
				if("01".equals(prpLDlossCarMainVo.getCetainLossType())){
					ConfirmLossVo.setEstimate_mode("1");//定损方式
				}else if("02".equals(prpLDlossCarMainVo.getCetainLossType())){
					ConfirmLossVo.setEstimate_mode("3");//定损方式
				}else if("03".equals(prpLDlossCarMainVo.getCetainLossType())){
					ConfirmLossVo.setEstimate_mode("3");//定损方式
				}else if("04".equals(prpLDlossCarMainVo.getCetainLossType())){
					ConfirmLossVo.setEstimate_mode("2");//定损方式
				}else if("06".equals(prpLDlossCarMainVo.getCetainLossType())){
					ConfirmLossVo.setEstimate_mode("2");//定损方式
				}else{
					ConfirmLossVo.setEstimate_mode("9999");//定损方式
				}
				ConfirmLossVo.setEstimate_address(prpLDlossCarMainVo.getDefSite());
				ConfirmLossVo.setRepair_factory_category("1");//修理厂类别
				if("001".equals(prpLDlossCarMainVo.getRepairFactoryType())){
					ConfirmLossVo.setRepair_factory_type("1");//修理厂类型
				}else if("002".equals(prpLDlossCarMainVo.getRepairFactoryType())){
					ConfirmLossVo.setRepair_factory_type("0");//修理厂类型
				}else{
					ConfirmLossVo.setRepair_factory_type("2");//修理厂类型
				}
				
				ConfirmLossVo.setRepair_factory_contact(prpLDlossCarMainVo.getFactoryMobile());//修理厂电话
				if(StringUtils.isNotBlank(prpLDlossCarMainVo.getRepairFactoryCode())){
					PrpLRepairFactoryVo prpLRepairFactoryVo=repairFactoryService.findFactoryByCode(prpLDlossCarMainVo.getRepairFactoryCode());
					if(prpLRepairFactoryVo!=null){
						ConfirmLossVo.setRepair_factory_contact(prpLRepairFactoryVo.getMobile());//修理厂联系人
						ConfirmLossVo.setRepair_factory_code(prpLRepairFactoryVo.getFactoryCode());//修理厂代码
						ConfirmLossVo.setRepair_factory_name(prpLRepairFactoryVo.getFactoryName());//修理厂名称
					}else{
						ConfirmLossVo.setRepair_factory_contact(prpLDlossCarMainVo.getFactoryMobile());//修理厂联系人
					}
					
			     }
				if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
					ConfirmLossVo.setSubject_third("1");//是否标的
				}else{
					ConfirmLossVo.setSubject_third("0");
				}
				ConfirmLossVo.setEstimate_code(prpLDlossCarMainVo.getHandlerCode());//定损员代码
				ConfirmLossVo.setEstimate_name(prpLDlossCarMainVo.getHandlerName());
				ConfirmLossVo.setEstimate_first_total(exNull(prpLDlossCarMainVo.getSumLossFee()));//首次定损金额
				ConfirmLossVo.setInsurance_estimate_total(exNull(prpLDlossCarMainVo.getSumLossFee()));
				ConfirmLossVo.setInsurance_first_rescue_fee(exNull(prpLDlossCarMainVo.getSumRescueFee()));
				ConfirmLossVo.setInsurance_rescue_fee(exNull(prpLDlossCarMainVo.getSumRescueFee()));
				if("14".equals(nodeFlag) || "15".equals(nodeFlag)){//14--核损，15--核价
					ConfirmLossVo.setVerify_price_code(prpLDlossCarMainVo.getVeripHandlerCode());//核价处理人代码 
					ConfirmLossVo.setVerify_price_name(prpLDlossCarMainVo.getVeripHandlerName());//核价处理人名称
					ConfirmLossVo.setVerify_price_total(exNull(prpLDlossCarMainVo.getSumVeripLoss()));
				}
				if("14".equals(nodeFlag)){//核损
					ConfirmLossVo.setUnder_write_code(prpLDlossCarMainVo.getUnderWriteCode());
					ConfirmLossVo.setUnder_write_name(prpLDlossCarMainVo.getUnderWriteName());
					ConfirmLossVo.setUnder_first_total(exNull(prpLDlossCarMainVo.getSumVeriLossFee()));
					ConfirmLossVo.setInsurance_under_total(exNull(prpLDlossCarMainVo.getSumVeriLossFee()));
				}
				
				//定损车辆
				VehicleVo vehicleVo=new VehicleVo();
				if("1".equals(configValueVo.getConfigValue())){
					vehicleVo.setOwner(prpLDlossCarInfoVo.getCarOwner());//车主
				}
				if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
					vehicleVo.setSubject_third("1");//是否标的
					if(citermsCarVos.get(0).getWholeWeight()!=null){
						vehicleVo.setTonnage(exNull((citermsCarVos.get(0).getWholeWeight()/1000)));//整备重量
    				}
					vehicleVo.setVehicle_price(exNull(citermsCarVos.get(0).getActualValue()));//车俩实际价值
				}else{
					vehicleVo.setSubject_third("0");
				}
				vehicleVo.setLicense_plate_no(prpLDlossCarInfoVo.getLicenseNo());//车牌号
				if(prpLCMainVos!=null && prpLCMainVos.size()>0){
					for(PrpLCMainVo cmainVo:prpLCMainVos){
						if("1101".equals(cmainVo.getRiskCode())){
							vehicleVo.setBz_policy_no(cmainVo.getPolicyNo());
						}
					}
					
				}
				vehicleVo.setBz_company_code(CodeConstants.CEDHICCODE);
				vehicleVo.setSub_company_name("鼎和财产保险股份有限公司");
				vehicleVo.setModel_name(prpLDlossCarInfoVo.getModelName());
				vehicleVo.setModel_code(prpLDlossCarInfoVo.getModelCode());
				vehicleVo.setVin(prpLDlossCarInfoVo.getVinNo());
				vehicleVo.setVehicle_brand_code(prpLDlossCarInfoVo.getBrandName());//车辆品牌
				vehicleVo.setVehicle_series_code(prpLDlossCarInfoVo.getSeriName());//车系
				vehicleVo.setGroup_veh_code(prpLDlossCarInfoVo.getGroupName());//车组
				if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
					PrplcodeDictVo  prplcodeDictVo7=claimService.findPrplcodeDictByCodeAndcodeType("CarType",citermsCarVos.get(0).getCarType());
					if(prplcodeDictVo7!=null){
						vehicleVo.setCar_kind_code(prplcodeDictVo7.getTcode());
					}
					 
				}else{
					 vehicleVo.setCar_kind_code(prpLDlossCarInfoVo.getPlatformCarKindCode());//车型代码
				}
				vehicleVo.setVehicle_brand_code(prpLDlossCarInfoVo.getBrandName());//车辆品牌
				vehicleVo.setVehicle_series_code(prpLDlossCarInfoVo.getSeriName());////车系名称
				vehicleVo.setGroup_veh_code(prpLDlossCarInfoVo.getGroupName());//车组名称
    			vehicleVo.setBrand_model(prpLDlossCarInfoVo.getBrandName());
    			vehicleVo.setLicense_plate_type(prpLDlossCarInfoVo.getLicenseType());//号牌种类
    			vehicleVo.setEngine_no(prpLDlossCarInfoVo.getEngineNo());//发动机号
    			//vehicleVo.setPlate_color("");//号牌底色
    			if("05".equals(prpLDlossCarMainVo.getCetainLossType())){
    				vehicleVo.setIs_loss("0");//是否有车损
    			}else{
    				vehicleVo.setIs_loss("1");//是否有车损
    			}
    			String losspartStr="";//损失部位
    			if(StringUtils.isNotBlank(prpLDlossCarMainVo.getLossPart())){
    				String[] lossparts=prpLDlossCarMainVo.getLossPart().split(",");
    				if(lossparts!=null && lossparts.length>0){
    					for(int i=0;i<lossparts.length;i++){
    						PrplcodeDictVo  prplcodeDictVo9=claimService.findPrplcodeDictByCodeAndcodeType("LossPart",lossparts[i]);
    						if(prplcodeDictVo9 !=null){
    							losspartStr=losspartStr+prplcodeDictVo9.getTcode();
    							if(lossparts.length>1 && i<(lossparts.length-1))
    							losspartStr=losspartStr+",";
    						}
    					}
    				}
    			}
    			vehicleVo.setLoss_part_data(losspartStr);//损失部位
    			// fix by LiYi 拿到标的车 ,判断是否上牌，默认上牌
    			if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
    			  boolean registLicenseNo = true;
    			  if(citermsCarVos!=null&& !citermsCarVos.isEmpty()){
    				PrpLCItemCarVo cItemCarVo = citermsCarVos.get(0);
    				String otherNature = cItemCarVo.getOtherNature();
    				if(StringUtils.isNotEmpty(otherNature)){
    					String substring = otherNature.substring(6,7);
    					if(substring.equals("1")){
    						registLicenseNo = false;
    					}
    				}else{
    					// otherNature为空 , 默认为未上牌
    					registLicenseNo = false;
    				}
    			  }else{
    				registLicenseNo = false;
    			  }
    			  if(registLicenseNo){
    				  vehicleVo.setHas_car_license("1");//是否已上牌
    			  }else{
    				  vehicleVo.setHas_car_license("0");
    			  }
    			 
    			}
    			vehicleVo.setIs_protect_loss(losssonFlag);//是否包含物损
    			vehicleVo.setIs_person_injured(personFlag);//是否包含人伤
    			if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
    				PrplcodeDictVo  prplcodeDictVo8=claimService.findPrplcodeDictByCodeAndcodeType("UseKind",citermsCarVos.get(0).getUseKindCode());
    				if(prplcodeDictVo8!=null){
    					vehicleVo.setUsing_properties(prplcodeDictVo8.getTcode());
    				}
    			}
    			vehicleVo.setRescue_amount(exNull(prpLDlossCarMainVo.getSumRescueFee()));//施救金额
    			vehicleVo.setEstimated_loss_amount(exNull(prpLDlossCarMainVo.getSumLossFee()));//估损金额
    			vehicleVo.setRemark("定损车辆信息");
    			ConfirmLossVo.setVehicle(vehicleVo);
				
    			//定损车辆驾驶员信息
    			DriverVo driver=new DriverVo();
    			driver.setLicense_plate_no(prpLDlossCarInfoVo.getLicenseNo());
    			if("1".equals(configValueVo.getConfigValue())){
    				driver.setDriver_name(prpLDlossCarInfoVo.getDriveName());
        			driver.setDriving_license_no(prpLDlossCarInfoVo.getDrivingLicenseNo());	
    			}
    		    PrplcodeDictVo  prplcodeDictVo6=claimService.findPrplcodeDictByCodeAndcodeType("IdentifyType",prpLDlossCarInfoVo.getIdentifyType());
				if(prplcodeDictVo6!=null){
					driver.setIdentify_type(prplcodeDictVo6.getTcode());
				}else{
					driver.setIdentify_type("99");//证件类型
				}
				//driver.setAllow_driving_vehicle("");//准驾车型
				driver.setIs_driving_license_effective("1");//驾驶员证件是否有效
				//driver.setDriving_license_date(DateFormatString(prpLCheckDriverVo.getAcceptLicenseDate()));//初次领证日期
				ConfirmLossVo.setDriver(driver);
				
				
				//配件信息
				List<SparepartVo> sparepartVos=new ArrayList<SparepartVo>();
				List<PrpLDlossCarCompVo> carCompVos=prpLDlossCarMainVo.getPrpLDlossCarComps();
				if(carCompVos!=null && carCompVos.size()>0){
					for(PrpLDlossCarCompVo carCompVo:carCompVos){
						SparepartVo sparepartVo=new SparepartVo();
						sparepartVo.setInsurance_fit_id(carCompVo.getIndId());//配件唯一id
						sparepartVo.setOem_code(carCompVo.getOriginalId());
						sparepartVo.setFitting_name(carCompVo.getCompName());
						sparepartVo.setFitting_num(exNull(carCompVo.getQuantity()));
						if("001".equals(prpLDlossCarMainVo.getRepairFactoryType())){
							sparepartVo.setPrice_scheme("1");//价格方案
						}else{
							sparepartVo.setPrice_scheme("2");
						}
						sparepartVo.setFirst_estimate_price(exNull(carCompVo.getMaterialFee()));//首次单价
						sparepartVo.setFirst_estimate_quantity(exNull(carCompVo.getQuantity()));
						sparepartVo.setFirst_estimate_total(exNull(carCompVo.getSumDefLoss()));
						sparepartVo.setFirst_estimate_remnant(exNull(carCompVo.getRestFee()));//残值
						sparepartVo.setFirst_estimate_is_recycle(carCompVo.getRecycleFlag());//回收
						sparepartVo.setEstimate_price(exNull(carCompVo.getMaterialFee()));
						sparepartVo.setEstimate_quantity(exNull(carCompVo.getQuantity()));
						sparepartVo.setEstimate_total(exNull(carCompVo.getSumDefLoss()));
						sparepartVo.setEstimate_remnant(exNull(carCompVo.getRestFee()));
						sparepartVo.setEstimate_is_recycle(carCompVo.getRecycleFlag());
						if("14".equals(nodeFlag) || "15".equals(nodeFlag)){//14--核损，15--核价
							sparepartVo.setVerify_price_price(exNull(carCompVo.getMaterialFee()));
							sparepartVo.setVerify_price_quantity(exNull(carCompVo.getQuantity()));
							sparepartVo.setVerify_price_total(exNull(carCompVo.getSumCheckLoss()));
							sparepartVo.setVerify_price_is_recycle(carCompVo.getRecycleFlag());
							sparepartVo.setVerify_price_remnant(exNull(carCompVo.getVeripRestFee()));
						}
						if("14".equals(nodeFlag)){//14--核损，15--核价 
							sparepartVo.setUnder_price(exNull(carCompVo.getVeriMaterFee()));
							sparepartVo.setUnder_quantity(exNull(carCompVo.getVeriQuantity()));
							sparepartVo.setUnder_total(exNull(carCompVo.getSumVeriLoss()));
							sparepartVo.setUnder_is_recycle(carCompVo.getRecycleFlag());
							sparepartVo.setUnder_remnant(exNull(carCompVo.getVeriRestFee()));
						}
						//sparepartVo.setDescription("配件信息");
						sparepartVo.setIs_hand(carCompVo.getSelfConfigFlag());//是否手动录入
						sparepartVo.setOperate_code(prpLDlossCarMainVo.getHandlerCode());
						sparepartVo.setOperate_name(prpLDlossCarMainVo.getHandlerName());//操作员人姓名
						sparepartVos.add(sparepartVo);
					}
					ConfirmLossVo.setSparepart(sparepartVos);//配件信息
				}
				
				//辅料信息
				List<SmallSparepartVo> smallSparepartVos=new ArrayList<SmallSparepartVo>();
				List<PrpLDlossCarMaterialVo> MaterialVos=prpLDlossCarMainVo.getPrpLDlossCarMaterials();
				if(MaterialVos!=null && MaterialVos.size()>0){
					for(PrpLDlossCarMaterialVo materialVo:MaterialVos){
						SmallSparepartVo SmallSparepartVo=new SmallSparepartVo();
						SmallSparepartVo.setInsurance_material_id(materialVo.getAssistId());//辅料Id
						SmallSparepartVo.setBatch_no(exNull(prpLDlossCarMainVo.getId()));//批次号
						SmallSparepartVo.setMaterial_code(materialVo.getAssistId());//辅料编码
						SmallSparepartVo.setMaterial_name(materialVo.getMaterialName());
						SmallSparepartVo.setFirst_estimate_price(exNull(materialVo.getUnitPrice()));//第一次单价
						SmallSparepartVo.setFirst_estimate_quantity(exNull(materialVo.getAssisCount()));
						SmallSparepartVo.setFirst_estimate_total(exNull(materialVo.getMaterialFee()));//第一次定损总价
						SmallSparepartVo.setEstimate_price(exNull(materialVo.getUnitPrice()));//单价
						SmallSparepartVo.setEstimate_quantity(exNull(materialVo.getAssisCount()));//定损数量
						SmallSparepartVo.setEstimate_total(exNull(materialVo.getMaterialFee()));
						if("001".equals(prpLDlossCarMainVo.getRepairFactoryType())){
							SmallSparepartVo.setPrice_scheme("1");//价格方案
						}else{
							SmallSparepartVo.setPrice_scheme("2");
						}
						if("14".equals(nodeFlag) || "15".equals(nodeFlag)){//14--核损，15--核价
							SmallSparepartVo.setVerify_price_price(exNull(materialVo.getAuditPrice()));//核价单价
							SmallSparepartVo.setVerify_price_quantity(exNull(materialVo.getAuditCount()));
							SmallSparepartVo.setVerify_price_total(exNull(materialVo.getAuditMaterialFee()));
						}
						if("14".equals(nodeFlag)){//14--核损
							SmallSparepartVo.setUnder_price(exNull(materialVo.getAuditLossPrice()));
							SmallSparepartVo.setUnder_quantity(exNull(materialVo.getAuditLossCount()));
							SmallSparepartVo.setUnder_total(exNull(materialVo.getAuditLossMaterialFee()));
						}
						SmallSparepartVo.setIs_hand(materialVo.getSelfConfigFlag());//是否手动录入
						SmallSparepartVo.setOperate_code(prpLDlossCarMainVo.getHandlerCode());
						SmallSparepartVo.setOperate_name(prpLDlossCarMainVo.getHandlerName());
						//SmallSparepartVo.setDescription("辅料信息");
						smallSparepartVos.add(SmallSparepartVo);
					}
					ConfirmLossVo.setSmallSparepart(smallSparepartVos);//辅料信息
				}
				
				//工时信息
				List<LaborVo> laborVos=new ArrayList<LaborVo>();
				List<PrpLDlossCarRepairVo> prpLDlossCarRepairVos=prpLDlossCarMainVo.getPrpLDlossCarRepairs();
				if(prpLDlossCarRepairVos!=null && prpLDlossCarRepairVos.size()>0){
					for(PrpLDlossCarRepairVo repairVo:prpLDlossCarRepairVos){
						LaborVo laborVo=new LaborVo();
						laborVo.setInsurance_labor_id(repairVo.getRepairId());//保险公司工时ID
						laborVo.setBatch_no(exNull(prpLDlossCarMainVo.getId()));//批次号
						if("001".equals(prpLDlossCarMainVo.getRepairFactoryType())){
							laborVo.setPrice_scheme("1");//价格方案
						}else{
							laborVo.setPrice_scheme("2");
						}
						laborVo.setRepair_code(repairVo.getCompCode());
						laborVo.setRepair_name(repairVo.getCompName());
						PrplcodeDictVo  prplcodeDictVo7=claimService.findPrplcodeDictByCodeAndcodeType("RepairType",repairVo.getCompCode());
						if(prplcodeDictVo7!=null){
							laborVo.setRepair_type(prplcodeDictVo7.getTcode());
						}else{
							laborVo.setRepair_type("99");//工种
						}
						laborVo.setFirst_estimate_unit_price(exNull(repairVo.getManHourUnitPrice()));//单价
						laborVo.setFirst_estimate_quantity(exNull(repairVo.getManHour()));//工时数
						laborVo.setFirst_estimate_price(exNull(repairVo.getSumDefLoss()));//小计
						laborVo.setLabor(exNull(repairVo.getManHour()));
						laborVo.setLabor_price(exNull(repairVo.getManHourUnitPrice()));
						laborVo.setEstimate_price(exNull(repairVo.getManHourFee()));
						if("14".equals(nodeFlag) || "15".equals(nodeFlag)){//14--核损，15--核价
							laborVo.setVerify_price_price(exNull(repairVo.getSumDefLoss()));//核价小计
							laborVo.setVerify_price_quantity(exNull(repairVo.getManHour()));//核价工时数
							laborVo.setVerify_price_unit_price(exNull(repairVo.getManHourUnitPrice()));//核价单价
							
						}
						if("14".equals(nodeFlag)){//14--核损
							laborVo.setUnder_quantity(exNull(repairVo.getVeriManHour()));//核损工时数
							laborVo.setUnder_unit_price(exNull(repairVo.getVeriManUnitPrice()));//核损单价
							laborVo.setUnder_price(exNull(repairVo.getSumVeriLoss()));//核损小计
						}
						//laborVo.setDescription("工时信息");
						laborVo.setIs_hand(repairVo.getSelfConfigFlag());//是否手动录入	0：否 1：是
						laborVo.setOperate_code(prpLDlossCarMainVo.getHandlerCode());//保险公司操作人编码
						laborVo.setOperate_name(prpLDlossCarMainVo.getHandlerName());//保险公司操作人姓名
						laborVos.add(laborVo);
					}
					ConfirmLossVo.setLabor(laborVos);//工时信息
				}
				
				//定损外修配件信息
				List<EstimateRepairVo> estimateRepairVos=new ArrayList<EstimateRepairVo>();
				if(prpLDlossCarRepairVos!=null && prpLDlossCarRepairVos.size()>0){
					for(PrpLDlossCarRepairVo repairVo:prpLDlossCarRepairVos){
						if(CodeConstants.RepairFlag.OUTREPAIR.equals(repairVo.getRepairFlag())){//外修
							EstimateRepairVo estimateRepairVo=new EstimateRepairVo();
							estimateRepairVo.setFit_name(repairVo.getCompName());
							estimateRepairVo.setEstimate_total(exNull(repairVo.getSumDefLoss()));//定损金额
							estimateRepairVo.setVerify_price_total(exNull(repairVo.getSumDefLoss()));
							estimateRepairVo.setUnder_total(exNull(repairVo.getSumVeriLoss()));//核损金额
							//estimateRepairVo.setDescription("外修配件");
							estimateRepairVos.add(estimateRepairVo);
						}
					}
				}
				
				if(estimateRepairVos!=null && estimateRepairVos.size()>0){
					ConfirmLossVo.setEstimateRepair(estimateRepairVos);
				}
				
				
				
				//定核损意见
				List<ConfirmLossDiscussionVo> lossDiscussionVos=new ArrayList<ConfirmLossDiscussionVo>();
				List<PrpLClaimTextVo> prpLClaimTextVoList = claimTextService.findClaimTextByregistNoAndbussTaskId(prpLDlossCarMainVo.getRegistNo(),prpLDlossCarMainVo.getId());
				if(prpLClaimTextVoList!=null && prpLClaimTextVoList.size()>0){
					for(PrpLClaimTextVo textVo:prpLClaimTextVoList){
						if(StringUtils.isNotBlank(textVo.getNodeCode()) && (textVo.getNodeCode().contains("DLCar") || textVo.getNodeCode().contains("VLCar") || textVo.getNodeCode().contains("VPCar"))){
							ConfirmLossDiscussionVo discussionVo=new ConfirmLossDiscussionVo();
							if(textVo.getNodeCode().contains("DLCar")){
								discussionVo.setTask_type("04");//定损
								discussionVo.setAmount(exNull(prpLDlossCarMainVo.getSumLossFee()));
							}else if(textVo.getNodeCode().contains("VPCar")){
								discussionVo.setTask_type("15");//核价
								discussionVo.setAmount(exNull(prpLDlossCarMainVo.getSumVeripLoss()));
							}else{
								discussionVo.setTask_type("14");//核损
								discussionVo.setAmount(exNull(prpLDlossCarMainVo.getSumVeriLossFee()));
							}
							discussionVo.setComment(textVo.getDescription());
							discussionVo.setCompany_name("鼎和财产保险股份有限公司");
							discussionVo.setPublish_date(DateFormatString(textVo.getInputTime()));
							discussionVo.setPerson_code(textVo.getOperatorCode());
							discussionVo.setPerson_name(textVo.getOperatorName());
							if(StringUtils.isNotBlank(textVo.getDescription()) && textVo.getDescription().contains("同意")){
								discussionVo.setIs_pass("1");
							}else{
								discussionVo.setIs_pass("0");
							}
							lossDiscussionVos.add(discussionVo);
						}
						
					}
					ConfirmLossVo.setConfirmLossDiscussion(lossDiscussionVos);//意见列表
				}
				baseVo.setConfirmLoss(ConfirmLossVo);//定损信息
				
				    //车辆调度定损，补全车辆查勘信息
					List<PrpLCheckCarInfoVo> prpLCheckCarInfoVos=checkTaskService.findPrpLCheckCarInfoVoListByOther(registNo, licenseNo);
					if(prpLCheckCarInfoVos==null || prpLCheckCarInfoVos.size()==0){
					 PrpLCheckVo  prpLCheckVo=checkTaskService.findCheckVoByRegistNo(registNo);
					 PrpLCheckDutyVo prpLCheckDutyVo=checkTaskService.findCheckDuty(registNo, 1);
					 PrpLCheckTaskVo prpLCheckTaskVo=new PrpLCheckTaskVo();
					 prpLCheckTaskVo =prpLCheckVo.getPrpLCheckTask();
					 List<PrpLCheckPropVo> prpLCheckPropVos= prpLCheckTaskVo.getPrpLCheckProps();
					 List<PrpLCheckPersonVo> prpLCheckPersonVos=prpLCheckTaskVo.getPrpLCheckPersons();
					 List<PrpLCheckCarVo> prpLCheckCarVos=checkTaskService.findCheckCarVo(registNo);
					 CheckVo checkVo=new CheckVo();
					 checkVo.setCheck_start_time(DateFormatString(prpLCheckTaskVo.getCheckDate()));//查勘开始时间
					 checkVo.setCheck_end_time(DateFormatString(prpLCheckVo.getChkSubmitTime()));//查勘提交时间
					 checkVo.setCheck_addr(prpLCheckTaskVo.getCheckAddress());//查勘地点
					 checkVo.setAppoint_check_addr(prpLCheckTaskVo.getCheckAddress());//约定查勘地点
					 checkVo.setChecker_name(prpLCheckTaskVo.getChecker());//查勘人
					 checkVo.setChecker_code(prpLCheckTaskVo.getCheckerCode());
					 checkVo.setChecker_phone1(prpLCheckTaskVo.getCheckerPhone());//查勘人电话
					 checkVo.setDanger_address(prpLRegistVo.getDamageAddress());
					 if(prplcodeDictVo2!=null){
						 checkVo.setDanger_cause(prplcodeDictVo2.getTcode());//出险原因
						}else{
						 checkVo.setDanger_cause("A10095A");//其他
					  }
					 checkVo.setDanger_desc(prpLRegistExtVo.getDangerRemark());
					 checkVo.setCase_type("2");//案件类型
					 PrplcodeDictVo  prplcodeDictVo4=claimService.findPrplcodeDictByCodeAndcodeType("AccidentDutyType",prpLCheckVo.getDamageTypeCode());
						if(prplcodeDictVo4!=null){
							checkVo.setEvent_type(prplcodeDictVo4.getTcode());//事故类型
						}else{
							checkVo.setEvent_type("2");//事故类型--暂定
						}
						if(prplcodeDictVo3!=null){
							checkVo.setEvent_process_mode(prplcodeDictVo3.getTcode());//事故处理类型
						}else{
							checkVo.setEvent_process_mode("9");//其他
						}
						if(prpLCheckDutyVo!=null && StringUtils.isNotBlank(prpLCheckDutyVo.getIndemnityDuty())){
							if("0".equals(prpLCheckDutyVo.getIndemnityDuty())){
								checkVo.setEvent_duty_ratio("100.00");
							}else if("1".equals(prpLCheckDutyVo.getIndemnityDuty())){
								checkVo.setEvent_duty_ratio("70.00");
							}else if("2".equals(prpLCheckDutyVo.getIndemnityDuty())){
								checkVo.setEvent_duty_ratio("50.00");
							}else if("3".equals(prpLCheckDutyVo.getIndemnityDuty())){
								checkVo.setEvent_duty_ratio("30.00");
							}else{
								checkVo.setEvent_duty_ratio("0.00");
							}
						}else{
							checkVo.setEvent_duty_ratio("50.00");//事故责任比例
						}
						//checkVo.setNo_fault_compensate("0");//无责代赔
						if("DM12".equals(prpLCheckVo.getDamageCode())){
							if("水淹1级".equals(prpLCheckVo.getWaterLoggingLevel())){
								checkVo.setWater_level("2");
							}else if("水淹2级".equals(prpLCheckVo.getWaterLoggingLevel())){
								checkVo.setWater_level("3");
							}else if("水淹3级".equals(prpLCheckVo.getWaterLoggingLevel())){
								checkVo.setWater_level("4");
							}else{
								checkVo.setWater_level("5");//水淹等级
							}
							
						}
						if("1".equals(prpLCheckVo.getIsSubRogation())){
							checkVo.setSubrogation_flag("1");
						}else{
							checkVo.setSubrogation_flag("0");//是否需要代位
						}
						checkVo.setIs_rescue(prpLRegistExtVo.getIsRescue());//是否需要施救
						checkVo.setIs_person_injured(prpLCheckVo.getIsPersonLoss());//是否包含人伤
						checkVo.setBuckle_ded_reason("无");//加扣免赔原因
						checkVo.setIs_mutual_collision_self_compensation(prpLCheckVo.getIsClaimSelf());//是否互碰自赔
						checkVo.setIs_car_insurance("1");//车辆是否与保单相符
						checkVo.setCheck_desc(prpLCheckTaskVo.getContexts());//查勘意见
						
						PrplcodeDictVo  prplcodeDictVo5=claimService.findPrplcodeDictByCodeAndcodeType("CheckType",prpLCheckVo.getCheckType());
						if(prplcodeDictVo5!=null){
							checkVo.setCheck_type(prplcodeDictVo5.getTcode());//查勘类型
						}else{
							checkVo.setCheck_type("1");
						}
						
						checkVo.setAudit_name(prpLCheckTaskVo.getChecker());//查勘人
						checkVo.setAudit_code(prpLCheckTaskVo.getCheckerCode());
						checkVo.setAudit_opinion(prpLCheckTaskVo.getContexts());
						checkVo.setAudit_time(DateFormatString(prpLCheckVo.getChkSubmitTime()));//查勘时间
						
						//查勘驾驶员信息
						driver.setLicense_plate_no(prpLDlossCarInfoVo.getLicenseNo());
		    			driver.setDriver_name(prpLDlossCarInfoVo.getDriveName());
		    			driver.setDriving_license_no(prpLDlossCarInfoVo.getDrivingLicenseNo());
						PrplcodeDictVo  prplcodeDictVo06=claimService.findPrplcodeDictByCodeAndcodeType("IdentifyType",prpLDlossCarInfoVo.getIdentifyType());
						if(prplcodeDictVo06!=null){
							driver.setIdentify_type(prplcodeDictVo06.getTcode());
						}else{
							driver.setIdentify_type("99");//证件类型
						}
						//driver.setAllow_driving_vehicle("");//准驾车型
						driver.setIs_driving_license_effective("1");//驾驶员证件是否有效
						
						DriverVo driverVo=new DriverVo();
						driverVo.setLicense_plate_no(prpLDlossCarInfoVo.getLicenseNo());
						driverVo.setDriver_name(prpLDlossCarInfoVo.getDriveName());
						driverVo.setDriving_license_no(prpLDlossCarInfoVo.getDrivingLicenseNo());
						PrplcodeDictVo  prplcodeDictVo04=claimService.findPrplcodeDictByCodeAndcodeType("IdentifyType",prpLDlossCarInfoVo.getIdentifyType());
						if(prplcodeDictVo04!=null){
							driverVo.setIdentify_type(prplcodeDictVo04.getTcode());
	    				}else{
	    					driverVo.setIdentify_type("99");//证件类型
	    				}
						//driverVo.setAllow_driving_vehicle(prpLCheckDriverVo.getDrivingCarType());//准驾车型
						driverVo.setIs_driving_license_effective("1");//驾驶员证件是否有效
						//driverVo.setDriving_license_date(DateFormatString(prpLCheckDriverVo.getAcceptLicenseDate()));//初次领证日期
						checkVo.setDriver(driverVo);
						//车辆信息
						//定损车辆
						VehicleVo vehiclecVo=new VehicleVo();
						vehiclecVo.setOwner(prpLDlossCarInfoVo.getCarOwner());//车主
						if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
							vehiclecVo.setSubject_third("1");//是否标的
							if(citermsCarVos.get(0).getWholeWeight()!=null){
								vehiclecVo.setTonnage(exNull((citermsCarVos.get(0).getWholeWeight()/1000)));//整备重量
		    				}
							vehiclecVo.setVehicle_price(exNull(citermsCarVos.get(0).getActualValue()));//车俩实际价值
						}else{
							vehiclecVo.setSubject_third("0");
						}
						vehiclecVo.setLicense_plate_no(prpLDlossCarInfoVo.getLicenseNo());//车牌号
						if(prpLCMainVos!=null && prpLCMainVos.size()>0){
							for(PrpLCMainVo cmainVo:prpLCMainVos){
								if("1101".equals(cmainVo.getRiskCode())){
									vehiclecVo.setBz_policy_no(cmainVo.getPolicyNo());
								}
							}
							
						}
						vehiclecVo.setBz_company_code(CodeConstants.CEDHICCODE);
						vehiclecVo.setSub_company_name("鼎和财产保险股份有限公司");
						vehiclecVo.setModel_name(prpLDlossCarInfoVo.getModelName());
						vehiclecVo.setModel_code(prpLDlossCarInfoVo.getModelCode());
						vehiclecVo.setVin(prpLDlossCarInfoVo.getVinNo());
						vehiclecVo.setVehicle_brand_code(prpLDlossCarInfoVo.getBrandName());//车辆品牌
						vehiclecVo.setVehicle_series_code(prpLDlossCarInfoVo.getSeriName());//车系
						vehiclecVo.setGroup_veh_code(prpLDlossCarInfoVo.getGroupName());//车组
						if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
							PrplcodeDictVo  prplcodeDictVo7=claimService.findPrplcodeDictByCodeAndcodeType("CarType",citermsCarVos.get(0).getCarType());
							if(prplcodeDictVo7!=null){
								vehiclecVo.setCar_kind_code(prplcodeDictVo7.getTcode());
							}
							 
						}else{
							vehiclecVo.setCar_kind_code(prpLDlossCarInfoVo.getPlatformCarKindCode());//车型代码
						}
						vehiclecVo.setVehicle_brand_code(prpLDlossCarInfoVo.getBrandName());//车辆品牌
						vehiclecVo.setVehicle_series_code(prpLDlossCarInfoVo.getSeriName());////车系名称
						vehiclecVo.setGroup_veh_code(prpLDlossCarInfoVo.getGroupName());//车组名称
						vehiclecVo.setBrand_model(prpLDlossCarInfoVo.getBrandName());
						vehiclecVo.setLicense_plate_type(prpLDlossCarInfoVo.getLicenseType());//号牌种类
						vehiclecVo.setEngine_no(prpLDlossCarInfoVo.getEngineNo());//发动机号
		    			//vehicleVo.setPlate_color("");//号牌底色
		    			if("05".equals(prpLDlossCarMainVo.getCetainLossType())){
		    				vehiclecVo.setIs_loss("0");//是否有车损
		    			}else{
		    				vehiclecVo.setIs_loss("1");//是否有车损
		    			}
		    			String losspartStrc="";//损失部位
		    			if(StringUtils.isNotBlank(prpLDlossCarMainVo.getLossPart())){
		    				String[] lossparts=prpLDlossCarMainVo.getLossPart().split(",");
		    				if(lossparts!=null && lossparts.length>0){
		    					for(int i=0;i<lossparts.length;i++){
		    						PrplcodeDictVo  prplcodeDictVo9=claimService.findPrplcodeDictByCodeAndcodeType("LossPart",lossparts[i]);
		    						if(prplcodeDictVo9 !=null){
		    							losspartStrc=losspartStrc+prplcodeDictVo9.getTcode();
		    							if(lossparts.length>1 && i<(lossparts.length-1))
		    								losspartStrc=losspartStrc+",";
		    						}
		    					}
		    				}
		    			}
		    			vehiclecVo.setLoss_part_data(losspartStr);//损失部位
		    			// fix by LiYi 拿到标的车 ,判断是否上牌，默认上牌
		    			if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
		    			  boolean registLicenseNo = true;
		    			  if(citermsCarVos!=null&& !citermsCarVos.isEmpty()){
		    				PrpLCItemCarVo cItemCarVo = citermsCarVos.get(0);
		    				String otherNature = cItemCarVo.getOtherNature();
		    				if(StringUtils.isNotEmpty(otherNature)){
		    					String substring = otherNature.substring(6,7);
		    					if(substring.equals("1")){
		    						registLicenseNo = false;
		    					}
		    				}else{
		    					// otherNature为空 , 默认为未上牌
		    					registLicenseNo = false;
		    				}
		    			  }else{
		    				registLicenseNo = false;
		    			  }
		    			  if(registLicenseNo){
		    				  vehiclecVo.setHas_car_license("1");//是否已上牌
		    			  }else{
		    				  vehiclecVo.setHas_car_license("0");
		    			  }
		    			 
		    			}
		    			vehiclecVo.setIs_protect_loss(losssonFlag);//是否包含物损
		    			vehiclecVo.setIs_person_injured(personFlag);//是否包含人伤
		    			if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
		    				PrplcodeDictVo  prplcodeDictVo8=claimService.findPrplcodeDictByCodeAndcodeType("UseKind",citermsCarVos.get(0).getUseKindCode());
		    				if(prplcodeDictVo8!=null){
		    					vehiclecVo.setUsing_properties(prplcodeDictVo8.getTcode());
		    				}
		    			}
		    			vehiclecVo.setRescue_amount(exNull(prpLDlossCarMainVo.getSumRescueFee()));//施救金额
		    			vehiclecVo.setEstimated_loss_amount(exNull(prpLDlossCarMainVo.getSumLossFee()));//估损金额
		    			vehiclecVo.setRemark("查勘车辆信息");
		    			checkVo.setVehicle(vehiclecVo);
						baseVo.setCheck(checkVo);//查勘信息
				  }	
				
			}
			
			//图片信息
			String ceNumber=SpringProperties.getProperty("CE_NUMBER");//图片数量限制数值
			int imageNum=100;//照片限制数值
			int imageN=0;//该案子图片数量
			if(StringUtils.isNotBlank(ceNumber)){
				imageNum=Integer.parseInt(ceNumber);
			}
			List<ClaimPhotosVo> claimPhotosVos=new ArrayList<ClaimPhotosVo>();
			String resXml=carXyImageService.reqResourceFromXy(userVo, registNo,url);
			if(StringUtils.isNotBlank(resXml) && resXml.contains("<RESPONSE_CODE>200</RESPONSE_CODE>")){
				String imgXml=changeXml(resXml);
			  if(StringUtils.isNotBlank(imgXml)){
				ImageUrlbaseVo imageUrlbaseVo=ClaimBaseCoder.xmlToObj(imgXml,ImageUrlbaseVo.class);
				if(imageUrlbaseVo.getPagesVo()!=null && imageUrlbaseVo.getPagesVo().size()>0){
					for(PageVo pageVo:imageUrlbaseVo.getPagesVo()){
						imageN++;
						
					}
				}
				
				if(imageUrlbaseVo!=null && imageUrlbaseVo.getSydVo()!=null && imageUrlbaseVo.getSydVo().getDocVo()!=null &&
				   imageUrlbaseVo.getSydVo().getDocVo().getDocInfoVo()!=null && imageUrlbaseVo.getSydVo().getDocVo().getVtreeImageVo()!=null){
					List<NodeImageVo> nodeImageVos0=imageUrlbaseVo.getSydVo().getDocVo().getVtreeImageVo().getNodeImageVos();
					if(nodeImageVos0!=null && nodeImageVos0.size()>0){
						for(NodeImageVo imageVo0:nodeImageVos0){
							if("claim-picture".equals(imageVo0.getId())){
								List<NodeImageVo> nodeImageVos1=imageVo0.getNodeImageVos();
								if(nodeImageVos1!=null && nodeImageVos1.size()>0){
									if(imageN<=100 && "1".equals(configValueVo.getConfigValue())){//图片数量不大于100张，则所有图片传给德联
										imageUrr(nodeImageVos1,claimPhotosVos,licenseNo,imageUrlbaseVo);										
									}else{//其它传车损图片与现场图片
										for(NodeImageVo imageVo1:nodeImageVos1){
											if("carLoss".equals(imageVo1.getId())){
												List<NodeImageVo> nodeImageVos2=imageVo1.getNodeImageVos();
												if(nodeImageVos2!=null && nodeImageVos2.size()>0){
													for(NodeImageVo imageVo2:nodeImageVos2){
														if(StringUtils.isNotBlank(imageVo2.getName())){
															List<LeafVo> leafVos=imageVo2.getLeafVos();
															if(leafVos!=null && leafVos.size()>0){
																for(LeafVo lfVo:leafVos){
																	ClaimPhotosVo claimPhotosVo=new ClaimPhotosVo();
																	/*String[] a=imageVo2.getName().split("\\(");
																	String[] b=a[1].split("\\)");*/
																	claimPhotosVo.setImg_name(imageVo2.getName());
																	/*if(imageVo2.getName().contains("标的")){
																		claimPhotosVo.setImg_type("0602");
																	}else{*/
																		claimPhotosVo.setImg_type(imageType(imageVo2.getId()));
																	//}
																	claimPhotosVo.setLicense_plate_no(licenseNo);
																	List<PageVo> pagevos=imageUrlbaseVo.getPagesVo();
																	if(pagevos!=null && pagevos.size()>0){
																		for(PageVo pageVo:pagevos){
																			if(pageVo.getPageId().equals(lfVo.getValues())){
																				String imgUrl=urlChange(pageVo.getPageUrl());
																				claimPhotosVo.setIc_img_url(imgUrl);
																				claimPhotosVo.setImg_code(lfVo.getValues());
																			}
																		}
																	}
																	claimPhotosVos.add(claimPhotosVo);
																}
															}
															
															
														}
													}
												}
												
											}
											
	                                       if("scenePicture".equals(imageVo1.getId())){
												List<NodeImageVo> nodeImageVos2=imageVo1.getNodeImageVos();
												if(nodeImageVos2!=null && nodeImageVos2.size()>0){
													for(NodeImageVo imageVo2:nodeImageVos2){
														if(StringUtils.isNotBlank(imageVo2.getName())){
															List<LeafVo> leafVos=imageVo2.getLeafVos();
															if(leafVos!=null && leafVos.size()>0){
																for(LeafVo lfVo:leafVos){
																	ClaimPhotosVo claimPhotosVo=new ClaimPhotosVo();
																	claimPhotosVo.setImg_type(imageType(imageVo2.getId()));
																	claimPhotosVo.setLicense_plate_no(licenseNo);
																	claimPhotosVo.setImg_name(imageVo2.getName());
																	List<PageVo> pagevos=imageUrlbaseVo.getPagesVo();
																	if(pagevos!=null && pagevos.size()>0){
																		for(PageVo pageVo:pagevos){
																			if(pageVo.getPageId().equals(lfVo.getValues())){
																				String imgUrl=urlChange(pageVo.getPageUrl());
																				claimPhotosVo.setIc_img_url(imgUrl);
																				claimPhotosVo.setImg_code(lfVo.getValues());
																			}
																		}
																	}
																	claimPhotosVos.add(claimPhotosVo);
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
						}
					}
				}
			 }
			}
			//图片
			if(claimPhotosVos!=null && claimPhotosVos.size()>0){
				baseVo.setClaimPhotos(claimPhotosVos);
			}
			
    	return baseVo;
    }
    
    /**
	 * 时间转换方法
	 * Date 类型转换 String类型
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private String DateFormatString(Date strDate){
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		return str;
	}
	 /**
	 * 时间转换方法
	 * Date 类型转换 String类型
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private static String DateString(Date strDate){
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		return str;
	}
	
	public Map<String, String> registRiskInfo(String registNo){
        Map<String, String> registRiskInfoMap = new HashMap<String, String>();
        if (!StringUtils.isEmpty(registNo)) {
            registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
        }
        return registRiskInfoMap;
    }

    private static String exNull(Object object){
    	if(object==null || StringUtils.isBlank(object.toString())){
    		return null;
    	}else{
    		return object.toString();
    	}
    }
    
    /**
     * 请求报文部分截取替换
     * @param reqXml
     * @return
     */
    private String changeXml(String reqXml){
    	int str1=reqXml.indexOf("<PAGES>");
		int str2=reqXml.indexOf("</PAGES>");
		String endStr="";
		if(str2>1){
			String stra=reqXml.substring(str1+7, str2+8);
			String strb=stra.replace("<PAGE","<UAGE");
			String strc="<PAGES>".concat(strb);
			String strd=reqXml.substring(str1, str2+8);
			endStr=reqXml.replace(strd, strc);
		}
		
		
    	return endStr;
    }
    /**
     * 转换地址
     * @param url
     * @return
     */
    private String urlChange(String url){
    	StringBuffer urlBuffer=new StringBuffer();
    	if(StringUtils.isNotBlank(url)){
    		String [] urlArry=url.split("\\?");
    		if(urlArry!=null && urlArry.length>0){
    			String ceUrl =SpringProperties.getProperty("CE_URL");
    			urlBuffer.append(ceUrl).append("?").append(urlArry[1]);
    		}
    	}
    	return urlBuffer.toString();
    }
    /**
     * 循环迭代图片赋值
     * @param nodeImageVos
     */
    private void imageUrr(List<NodeImageVo> nodeImageVos,List<ClaimPhotosVo> claimPhotosVos,String licenseNo,ImageUrlbaseVo imageUrlbaseVo){
    
    	if(nodeImageVos!=null && nodeImageVos.size()>0){
    	  for(NodeImageVo imageVo1:nodeImageVos){
    		  if(imageVo1.getLeafVos()==null){
    			  if(imageVo1.getNodeImageVos()!=null && imageVo1.getNodeImageVos().size()>0){
      				imageUrr(imageVo1.getNodeImageVos(),claimPhotosVos,licenseNo,imageUrlbaseVo);
      			} 
    		  }else{
					if(StringUtils.isNotBlank(imageVo1.getName())){
						List<LeafVo> leafVos=imageVo1.getLeafVos();
						if(leafVos!=null && leafVos.size()>0){
							for(LeafVo lfVo:leafVos){
								ClaimPhotosVo claimPhotosVo=new ClaimPhotosVo();
								claimPhotosVo.setImg_type(imageType(imageVo1.getId()));
								claimPhotosVo.setLicense_plate_no(licenseNo);
								claimPhotosVo.setImg_name(imageVo1.getName());
								List<PageVo> pagevos=imageUrlbaseVo.getPagesVo();
								if(pagevos!=null && pagevos.size()>0){
									for(PageVo pageVo:pagevos){
										if(pageVo.getPageId().equals(lfVo.getValues())){
											String imgUrl=urlChange(pageVo.getPageUrl());
											claimPhotosVo.setIc_img_url(imgUrl);
											claimPhotosVo.setImg_code(lfVo.getValues());
										}
									}
								}
								claimPhotosVos.add(claimPhotosVo);
							}
						}
						
						
					}
				}
			
    		
    	  }
    	}
    }
    
    /**
     * 图片类型汇总
     * @param imageType,
     * @return
     */
    private String imageType(String imageId){
    	String imagetype="";
    	if(StringUtils.isNotBlank(imageId)){
    		if(imageId.contains("images_1")){
    			if(imageId.equals("images_1_1")){
    				imagetype="carlossa";//标的车
    			}else{
    				imagetype="carlossb";//三者车
    			}
    		}else if(imageId.contains("images_2")){
    			imagetype="propLoss";//财产损失
    		}else if(imageId.contains("images_3")){
    			imagetype="personLoss_p";//人员损失
    		}else if(imageId.contains("scenePicture")){
    			imagetype="scenePicture";//现场资料
    		}else if(imageId.contains("checkReport")){
    			imagetype="checkReport";//查勘报告
    		}else if(imageId.contains("assessorsFee")){
    			imagetype="assessorsFee";//公估费资料
    		}else if(imageId.contains("checksFee")){
    			imagetype="checksFee";//查勘费资料
    		}else{
    			imagetype="untype";//其它
    		}
    	}
    	
    	return imagetype;
    	
    }
}
