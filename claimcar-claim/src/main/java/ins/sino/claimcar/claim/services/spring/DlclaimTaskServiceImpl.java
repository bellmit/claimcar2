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
     *????????????????????????????????????
     */
	@Override
	public void SendControlExpert(String registNo,SysUserVo userVo,String licenseNo,String lossCarMainId, String nodeFlag,String imageurl) {
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		String reqjsonStr="";//??????json
		String resjsonStr="";//??????json
		String url=SpringProperties.getProperty("CEReq_URL");;//????????????
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
					reqjsonStr=rejson.toString();//json??????
					System.out.println("=====================???????????????"+reqjsonStr);
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
			logVo.setErrorMessage("catch??????"+e.getMessage());
			logger.info("???????????????????????????????????????",e);
		}finally{
			logVo.setRegistNo(registNo);
			logVo.setServiceType(lossCarMainId);//??????????????????Id
			logVo.setRequestTime(new Date());
			logVo.setPolicyNo(licenseNo);//?????????
			logVo.setOperateNode(nodeFlag);//????????????
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
         //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
          //JSSE????????????????????????????????????TrustManager???
         public void checkClientTrusted(X509Certificate[] chain, String authType)throws CertificateException {
         }
         //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
         public void checkServerTrusted(X509Certificate[] chain, String authType)throws CertificateException { }
         //??????????????????X509????????????.
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
     * ????????????https????????????
     * @param url
     * @param content
     * @param charset
     * @return
     * @throws Exception
     */
    @SuppressWarnings("restriction")
	private byte[] postDL(String url, String content, String charset) throws Exception{
    	String md5Content=MD5.sign(content, key,charset);//????????????
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
        //???????????????
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        conn.setRequestMethod("POST");
        conn.setRequestProperty("auth_id",CodeConstants.CEDHICCODE);
        conn.setRequestProperty("auth_token",md5Content);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setConnectTimeout(120*1000);//?????????????????????120???
        conn.setReadTimeout(90*1000);//?????????????????????90
        conn.connect();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(content.getBytes(charset));
        // ??????????????? 
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
    	//??????????????????
    	TransitClaimDocumentBaseVo baseVo=new TransitClaimDocumentBaseVo();
    	baseVo.setClaim_notification_no(registNo);
    	baseVo.setOperate_type(nodeFlag);//????????????
    	baseVo.setEstimate_company_code(CodeConstants.CEDHICCODE);
        baseVo.setEstimate_company_id("536");//?????????????????????????????????????????????
    
    	baseVo.setBusinessType("2");//????????????
    	baseVo.setRegist_type("1");//??????
    	//??????????????????
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
    			policyVo.setCompany_name("????????????");
    			if(citermsCarVos!=null && citermsCarVos.size()>0){
    				for(PrpLCItemCarVo carVo:citermsCarVos){
    					if(cmainVo.getRiskCode().equals(carVo.getRiskCode())){
    						policyVo.setPlate_color(carVo.getLicenseColorCode());
    						break;
    					}
    				}
    			}
    			if("1101".equals(cmainVo.getRiskCode())){
    				policyVo.setInsurance_category("1");//??????
    			}else{
    				policyVo.setInsurance_category("2");//??????
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
    			
    			//????????????
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
    			        //?????????vo
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
    			    	policyVo.setIs_continuous_policy("1");//????????????
    			    }else{
    			    	policyVo.setIs_continuous_policy("0");//????????????
    			    }
    			    
    				policyVo.setEvent_count(exNull(historicalAccident));//????????????
    				policyVo.setModel_name(citermsCarVos.get(0).getBrandName());
    				policyVo.setModel_code(citermsCarVos.get(0).getModelCode());
    				policyVo.setBrand_model(citermsCarVos.get(0).getBrandName());
    				policyVo.setLicense_plate_no(citermsCarVos.get(0).getLicenseNo());
    				if(StringUtils.isNotBlank(citermsCarVos.get(0).getLicenseNo()) && citermsCarVos.get(0).getLicenseNo().contains("???Z")){
    				policyVo.setHkmac_license_plate_no(citermsCarVos.get(0).getLicenseNo());
    				}
    				policyVo.setVehicle_frame_no(citermsCarVos.get(0).getFrameNo());
    				
    				
    			}
    			
    			//????????????????????????
    			if(cinsuredVos!=null && cinsuredVos.size()>0){
    				policyVo.setNature(cinsuredVos.get(0).getInsuredType());//????????????
    				if(StringUtils.isNotBlank(cinsuredVos.get(0).getIdentifyType()) && ("550".equals(cinsuredVos.get(0).getIdentifyType()) || "553".equals(cinsuredVos.get(0).getIdentifyType()) || "00".equals(cinsuredVos.get(0).getIdentifyType()))){
    					policyVo.setInsured_identify_type("99");
    				}else if(StringUtils.isNotBlank(cinsuredVos.get(0).getIdentifyType())){
    					policyVo.setInsured_identify_type(cinsuredVos.get(0).getIdentifyType());
    				}else{
    					policyVo.setInsured_identify_type("99");//????????????
    				}
    				if("1".equals(configValueVo.getConfigValue())){
    					policyVo.setInsured_name(cinsuredVos.get(0).getInsuredName());//??????????????????
        				policyVo.setInsured_identify_no(cinsuredVos.get(0).getIdentifyNumber());//????????????????????????	
    				}
    				
    				
    			}
    			
    			policyVo.setRemark("??????????????????");
    			
    			 double claimSum = 0;//?????????????????????
    		     int claimTimes = 0;//?????????????????????
    		     // ?????????
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

    	            // ?????????
    	            List<PrploldCompensateVo> oldcompensateVos = compensateTaskService.findPrpoldCompensateBypolicyNo(cmainVo.getPolicyNo());
    	            if (oldcompensateVos != null && oldcompensateVos.size() > 0) {
    	                for (PrploldCompensateVo oldVo : oldcompensateVos) {
    	                    if (oldVo.getEndcaseDate() != null) {
    	                        claimSum = claimSum + DataUtils.NullToZero(oldVo.getSumPaid()).doubleValue();
    	                        claimTimes = claimTimes + 1;
    	                    }
    	                }
    	            }
    	            policyVo.setPaid_times(claimTimes+"");//????????????
    	            policyVo.setPaid_total(claimSum+"");//????????????
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
    					policyVo.setImport_domestic("??????");
    				}else{
    					policyVo.setImport_domestic("??????");
    				}
    				policyVo.setAgency_name("??????????????????????????????");
    				
    				//??????????????????
    				List<InsuranceItemVo> insuranceItemVoList=new ArrayList<InsuranceItemVo>();
    				List<PrpLCItemKindVo> prpLCItemKindVos=cmainVo.getPrpCItemKinds();
    				if(prpLCItemKindVos!=null && prpLCItemKindVos.size()>0){
    					for(PrpLCItemKindVo itemKindVo:prpLCItemKindVos){
    						InsuranceItemVo insuranceItemVo=new InsuranceItemVo();
    						insuranceItemVo.setCode(itemKindVo.getKindCode());//??????-??????
    						insuranceItemVo.setName(itemKindVo.getKindName());//??????
    						if(itemKindVo.getAmount()!=null){
    							insuranceItemVo.setInsurance_amout(exNull(itemKindVo.getAmount()));//????????????
    						}else{
    							insuranceItemVo.setInsurance_amout("0.00");
    						}
    						if(itemKindVo.getQuantity()!=null){
    							insuranceItemVo.setUnit_count(itemKindVo.getQuantity().intValue()+"");
    						}else{
    							insuranceItemVo.setUnit_count("0");
    						}
    						insuranceItemVo.setUnit_amount(exNull(itemKindVo.getUnitAmount()));//????????????
    						
    						if("1".equals(itemKindVo.getNoDutyFlag())){//??????????????????
    							insuranceItemVo.setNon_deductible("1");
    						}else if("0".equals(itemKindVo.getNoDutyFlag())){
    							insuranceItemVo.setNon_deductible("0");
    						}else{
    							insuranceItemVo.setNon_deductible("0");
    						}
    						if(itemKindVo.getDeductibleRate()!=null){
    							insuranceItemVo.setDeductible_rate(exNull(itemKindVo.getDeductibleRate()));
    						}else{
    							insuranceItemVo.setDeductible_rate("0.00");//?????????
    						}
    						insuranceItemVo.setDeductible_money(exNull(itemKindVo.getDeductible()));
    						insuranceItemVo.setInsurance_fee(exNull(itemKindVo.getBenchMarkPremium()));//????????????
    						insuranceItemVo.setPremiums(exNull(itemKindVo.getAmount()));//??????
    						insuranceItemVo.setRemark("????????????");
    						insuranceItemVoList.add(insuranceItemVo);
    					}
    				}
    				if(insuranceItemVoList!=null && insuranceItemVoList.size()>0){
    					policyVo.setInsuranceItemVos(insuranceItemVoList);
    				}
    				
    				
    				//??????????????????
    				List<HistoricalClaimVo> historicalClaimVoList=new ArrayList<HistoricalClaimVo>();
    				List<PrpLClaimVo> claimVos=claimService.findPrpLClaimVosByPolicyNo(cmainVo.getPolicyNo());
    				if(claimVos!=null && claimVos.size()>0){
    					for(PrpLClaimVo claimVo:claimVos){
    						HistoricalClaimVo hclaimVo=new HistoricalClaimVo();
    						if("1101".equals(claimVo.getRiskCode())){
    							hclaimVo.setInsurance_category("1");//??????
    						}else{
    							hclaimVo.setInsurance_category("2");//??????
    						}
    						hclaimVo.setPaid_times(claimTimes+"");
    						hclaimVo.setPay_claim_number(claimVo.getCaseNo());//?????????
    						hclaimVo.setClaim_date(DateFormatString(claimVo.getEndCaseTime()));
    						hclaimVo.setEstimate_amount(exNull(claimVo.getSumDefLoss()));
    						hclaimVo.setClaim_date(DateFormatString(claimVo.getDamageTime()));
    						hclaimVo.setEvent_address(codeTranService.transCode("AreaCode",claimVo.getDamageAreaCode()));
    						hclaimVo.setLicense_plate_no(citermsCarVos.get(0).getLicenseNo());//?????????
    						hclaimVo.setReport_date(DateFormatString(claimVo.getReportTime()));
    						if(claimVo.getEndCaseTime()!=null){
    							hclaimVo.setPaid_amount(exNull(claimVo.getSumDefLoss()));//??????????????????
    						}
    						historicalClaimVoList.add(hclaimVo);
    					}
    				}
    				if(historicalClaimVoList!=null && historicalClaimVoList.size()>0){
    					policyVo.setHistoricalClaimVos(historicalClaimVoList);
    				}
    				
    				//????????????
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
								cationVo.setContent(endorContent);//????????????
							}else{
								cationVo.setContent("????????????");//????????????
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
    	     baseVo.setPolicy(policyVoList);//????????????
    	     
    	 //---------------------------------------------------------------------------------------------------
    	     //????????????
    	     ReportVo reportVo=new ReportVo();
    	     PrpLRegistExtVo prpLRegistExtVo=prpLRegistVo.getPrpLRegistExt();
    	     List<PrpLRegistPersonLossVo> prpLRegistPersonLossVos= prpLRegistVo.getPrpLRegistPersonLosses();
    	     List<PrpLRegistPropLossVo> prpLRegistPropLossVos=prpLRegistVo.getPrpLRegistPropLosses();
    	     if(prpLRegistVo.getCancelTime()!=null && prpLRegistExtVo!=null){
    	    	 reportVo.setCancel_date(DateFormatString(prpLRegistVo.getCancelTime()));
    	    	 if("1".equals(prpLRegistExtVo.getCancelReason()) || "2".equals(prpLRegistExtVo.getCancelReason())){
    	    		 reportVo.setCancel_reason("1");//????????????
    	    	 }else if("3".equals(prpLRegistExtVo.getCancelReason())){
    	    		 reportVo.setCancel_reason("2");
    	    	 }else if("4".equals(prpLRegistExtVo.getCancelReason())){
    	    		 reportVo.setCancel_reason("3");
    	    	 }else{
    	    		 reportVo.setCancel_reason("1");
    	    	 }
    	     }
    	     int sumHurt=0;//????????????
    	     if(prpLRegistPersonLossVos!=null && prpLRegistPersonLossVos.size()>0){
    	    	 for(PrpLRegistPersonLossVo personLossVo:prpLRegistPersonLossVos){
    	    		 sumHurt=sumHurt+personLossVo.getInjuredcount()+personLossVo.getDeathcount();
    	    	 }
    	    	 reportVo.setIs_protect_loss("1");//???????????????
    	     }else{
    	    	 reportVo.setIs_protect_loss("0");
    	     }
    	     reportVo.setHurt_num(sumHurt+"");
    	     reportVo.setAccept_ind("1");//????????????
    	     if(prpLRegistPropLossVos!=null && prpLRegistPropLossVos.size()>0){
    	    	 reportVo.setIs_person_injured("1");//???????????????
    	     }else{
    	    	 reportVo.setIs_person_injured("0");
    	     }
    	     if("1".equals(prpLRegistVo.getIsoffSite())){
    	    	 reportVo.setIs_outof_local_claim("1");//??????????????????
    	     }else{
    	    	 reportVo.setIs_outof_local_claim("0");//??????????????????
    	     }
    	     if("1".equals(prpLRegistExtVo.getIsSubRogation())){
    	    	 reportVo.setSubrogation_flag("1");
    	     }else{
    	    	 reportVo.setSubrogation_flag("0");//????????????
    	     }
    	    	 reportVo.setIs_closed("0");//????????????
    	    
    	     reportVo.setIs_simple_claim("0");//??????????????????
    	     PrplcodeDictVo  prplcodeDictVo0=claimService.findPrplcodeDictByCodeAndcodeType("IndemnityDuty",prpLRegistExtVo.getObliGation());
				if(prplcodeDictVo0!=null){
					reportVo.setAccident_liability(prplcodeDictVo0.getTcode());//????????????
				}
			 PrplcodeDictVo  prplcodeDictVo2=claimService.findPrplcodeDictByCodeAndcodeType("DamageCode",prpLRegistVo.getDamageCode());
				if(prplcodeDictVo2!=null){
					reportVo.setEvent_reason_type(prplcodeDictVo2.getTcode());
				}else{
					reportVo.setEvent_reason_type("A10095A");//??????
				}
				
				 PrplcodeDictVo  prplcodeDictVo3=claimService.findPrplcodeDictByCodeAndcodeType("AccidentManageType",prpLRegistExtVo.getManageType());
				if(prplcodeDictVo3!=null){
					reportVo.setEvent_process_mode(prplcodeDictVo3.getTcode());//??????????????????
				}else{
					reportVo.setEvent_process_mode("9");//??????
				}
				if(StringUtils.isNotBlank(prpLRegistExtVo.getIsAlarm())){
					reportVo.setIs_police(prpLRegistExtVo.getIsAlarm());//????????????
				}else{
					reportVo.setIs_police("0");
				}
				reportVo.setIs_mutual_collision_self_compensation(prpLRegistExtVo.getIsClaimSelf());//??????????????????
				if(prplcodeDictVo2!=null){
					reportVo.setDanger_cause(prplcodeDictVo2.getTcode());//????????????
				}else{
					reportVo.setDanger_cause("A10095A");//??????
				}
				if(prpLCMainVos!=null && prpLCMainVos.size()>0){
					if(prpLRegistVo.getDamageTime()!=null && prpLCMainVos.get(0).getStartDate()!=null && DateString(prpLRegistVo.getDamageTime()).equals(DateString(prpLCMainVos.get(0).getStartDate()))){
						reportVo.setIs_take_effect("1");//??????????????????insuredPhone
	    			}else{
	    				reportVo.setIs_take_effect("0");//??????????????????insuredPhone
	    			}
				}
				if("1".equals(configValueVo.getConfigValue())){
					reportVo.setDriver_name(prpLRegistVo.getDriverName());//???????????????
					reportVo.setReporter(prpLRegistVo.getReportorName());//???????????????
					reportVo.setReporter_phone(prpLRegistVo.getReportorPhone());//???????????????
				}
				reportVo.setDanger_address(prpLRegistVo.getDamageAddress());//???????????????
				reportVo.setDanger_time(DateFormatString(prpLRegistVo.getDamageTime()));
				reportVo.setDanger_area(prpLRegistVo.getDamageAddress());
				reportVo.setDanger_desc(prpLRegistExtVo.getDangerRemark());
				reportVo.setRemark("????????????");
				reportVo.setReport_type(prpLRegistExtVo.getIsOnSitReport());//????????????
				reportVo.setNotification_time(DateFormatString(prpLRegistVo.getReportTime()));//????????????
				reportVo.setStatus(nodeFlag);//????????????isCantravel
				reportVo.setIs_subject_normal(prpLRegistExtVo.getIsCantravel());//?????????????????????
				reportVo.setIs_rescue(prpLRegistExtVo.getIsRescue());//????????????
				reportVo.setIs_self_survey(prpLRegistExtVo.getIsCheckSelf());//??????????????????
				if(StringUtils.isNotBlank(prpLRegistVo.getInsuredPhone())){
					reportVo.setIs_insured_phone("1");//??????????????????????????????
				}else{
					reportVo.setIs_insured_phone("0");
				}
				baseVo.setReport(reportVo);//????????????
			//---------------------------------------------------------------------------------------------------
				
			String personFlag="0";//??????????????????
			String losssonFlag="0";//??????????????????
			//????????????
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
				 checkVo.setCheck_start_time(DateFormatString(prpLCheckTaskVo.getCheckDate()));//??????????????????
				 checkVo.setCheck_end_time(DateFormatString(prpLCheckVo.getChkSubmitTime()));//??????????????????
				 checkVo.setCheck_addr(prpLCheckTaskVo.getCheckAddress());//????????????
				 checkVo.setAppoint_check_addr(prpLCheckTaskVo.getCheckAddress());//??????????????????
				 checkVo.setChecker_name(prpLCheckTaskVo.getChecker());//?????????
				 checkVo.setChecker_code(prpLCheckTaskVo.getCheckerCode());
				 checkVo.setChecker_phone1(prpLCheckTaskVo.getCheckerPhone());//???????????????
				 checkVo.setDanger_address(prpLRegistVo.getDamageAddress());
				 if(prplcodeDictVo2!=null){
					 checkVo.setDanger_cause(prplcodeDictVo2.getTcode());//????????????
					}else{
					 checkVo.setDanger_cause("A10095A");//??????
				  }
				 checkVo.setDanger_desc(prpLRegistExtVo.getDangerRemark());
				 checkVo.setCase_type("2");//????????????
				 PrplcodeDictVo  prplcodeDictVo4=claimService.findPrplcodeDictByCodeAndcodeType("AccidentDutyType",prpLCheckVo.getDamageTypeCode());
					if(prplcodeDictVo4!=null){
						checkVo.setEvent_type(prplcodeDictVo4.getTcode());//????????????
					}else{
						checkVo.setEvent_type("2");//????????????--??????
					}
					if(prplcodeDictVo3!=null){
						checkVo.setEvent_process_mode(prplcodeDictVo3.getTcode());//??????????????????
					}else{
						checkVo.setEvent_process_mode("9");//??????
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
						checkVo.setEvent_duty_ratio("50.00");//??????????????????
					}
					//checkVo.setNo_fault_compensate("0");//????????????
					if("DM12".equals(prpLCheckVo.getDamageCode())){
						if("??????1???".equals(prpLCheckVo.getWaterLoggingLevel())){
							checkVo.setWater_level("2");
						}else if("??????2???".equals(prpLCheckVo.getWaterLoggingLevel())){
							checkVo.setWater_level("3");
						}else if("??????3???".equals(prpLCheckVo.getWaterLoggingLevel())){
							checkVo.setWater_level("4");
						}else{
							checkVo.setWater_level("5");//????????????
						}
						
					}
					if("1".equals(prpLCheckVo.getIsSubRogation())){
						checkVo.setSubrogation_flag("1");
					}else{
						checkVo.setSubrogation_flag("0");//??????????????????
					}
					checkVo.setIs_rescue(prpLRegistExtVo.getIsRescue());//??????????????????
					checkVo.setIs_person_injured(prpLCheckVo.getIsPersonLoss());//??????????????????
					checkVo.setBuckle_ded_reason("???");//??????????????????
					checkVo.setIs_mutual_collision_self_compensation(prpLCheckVo.getIsClaimSelf());//??????????????????
					checkVo.setIs_car_insurance("1");//???????????????????????????
					checkVo.setCheck_desc(prpLCheckTaskVo.getContexts());//????????????
					
					PrplcodeDictVo  prplcodeDictVo5=claimService.findPrplcodeDictByCodeAndcodeType("CheckType",prpLCheckVo.getCheckType());
					if(prplcodeDictVo5!=null){
						checkVo.setCheck_type(prplcodeDictVo5.getTcode());//????????????
					}else{
						checkVo.setCheck_type("1");
					}
					
					checkVo.setAudit_name(prpLCheckTaskVo.getChecker());//?????????
					checkVo.setAudit_code(prpLCheckTaskVo.getCheckerCode());
					checkVo.setAudit_opinion(prpLCheckTaskVo.getContexts());
					checkVo.setAudit_time(DateFormatString(prpLCheckVo.getChkSubmitTime()));//????????????
					
					//?????????????????????
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
		    					driverVo.setIdentify_type("99");//????????????
		    				}
							driverVo.setAllow_driving_vehicle(prpLCheckDriverVo.getDrivingCarType());//????????????
							driverVo.setIs_driving_license_effective("1");//???????????????????????????
							driverVo.setDriving_license_date(DateFormatString(prpLCheckDriverVo.getAcceptLicenseDate()));//??????????????????
							checkVo.setDriver(driverVo);
							break;
						 }
					 }
					}
					
					//????????????
					if(prpLCheckCarVos!=null && prpLCheckCarVos.size()>0){
						for(PrpLCheckCarVo carVo:prpLCheckCarVos){
							VehicleVo vehicleVo=new VehicleVo();
							PrpLCheckCarInfoVo prpLCheckCarInfoVo=carVo.getPrpLCheckCarInfo();
						if(licenseNo.equals(prpLCheckCarInfoVo.getLicenseNo())){
							if(1==carVo.getSerialNo()){
								vehicleVo.setSubject_third("1");//????????????
								if(citermsCarVos.get(0).getWholeWeight()!=null){
									vehicleVo.setTonnage(exNull((citermsCarVos.get(0).getWholeWeight()/1000)));//????????????
			    				}
								vehicleVo.setVehicle_price(exNull(citermsCarVos.get(0).getActualValue()));//??????????????????
							}else{
								vehicleVo.setSubject_third("0");
							}
							if("1".equals(configValueVo.getConfigValue())){
								vehicleVo.setOwner(prpLCheckCarInfoVo.getCarOwner());//??????
							}
							vehicleVo.setLicense_plate_no(prpLCheckCarInfoVo.getLicenseNo());//?????????
							if(prpLCMainVos!=null && prpLCMainVos.size()>0){
								for(PrpLCMainVo cmainVo:prpLCMainVos){
									if("1101".equals(cmainVo.getRiskCode())){
										vehicleVo.setBz_policy_no(cmainVo.getPolicyNo());
									}
								}
								
							}
							vehicleVo.setBz_company_code(CodeConstants.CEDHICCODE);
							vehicleVo.setSub_company_name("????????????????????????????????????");
							vehicleVo.setModel_name(prpLCheckCarInfoVo.getBrandName());
							vehicleVo.setModel_code(prpLCheckCarInfoVo.getModelCode());
							vehicleVo.setVin(prpLCheckCarInfoVo.getVinNo());
							vehicleVo.setVehicle_brand_code(prpLCheckCarInfoVo.getBrandName());//????????????
							//vehicleVo.setVehicle_series_code(prpLCheckCarInfoVo.getSeriName());//??????
							//vehicleVo.setGroup_veh_code(prpLDlossCarInfoVo.getGroupName());//??????
							 PrplcodeDictVo  prplcodeDictVo7=claimService.findPrplcodeDictByCodeAndcodeType("CarType",citermsCarVos.get(0).getCarType());
			    				if(prplcodeDictVo7!=null && 1==carVo.getSerialNo()){
			    					 vehicleVo.setCar_kind_code(prplcodeDictVo7.getTcode());
			    				}else if(1!=carVo.getSerialNo()){
			    					 vehicleVo.setCar_kind_code(prpLCheckCarInfoVo.getPlatformCarKindCode());//????????????
			    				}
							
			    			vehicleVo.setBrand_model(prpLCheckCarInfoVo.getBrandName());
			    			vehicleVo.setLicense_plate_type(prpLCheckCarInfoVo.getLicenseType());//????????????
			    			vehicleVo.setEngine_no(prpLCheckCarInfoVo.getEngineNo());//????????????
			    			vehicleVo.setPlate_color(prpLCheckCarInfoVo.getLicenseColor());//????????????
			    			if("1".equals(carVo.getLossFlag())){//????????????1??????????????????0???????????????
			    				vehicleVo.setIs_loss("0");//???????????????
			    			}else{
			    				vehicleVo.setIs_loss("1");//???????????????
			    			}
			    			String losspartStr="";//????????????
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
			    			vehicleVo.setLoss_part_data(losspartStr);//????????????
			    			// fix by LiYi ??????????????? ,?????????????????????????????????
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
			    					// otherNature?????? , ??????????????????
			    					registLicenseNo = false;
			    				}
			    			  }else{
			    				registLicenseNo = false;
			    			  }
			    			  if(registLicenseNo){
			    				  vehicleVo.setHas_car_license("1");//???????????????
			    			  }else{
			    				  vehicleVo.setHas_car_license("0");
			    			  }
			    			 
			    			}
			    			if(prpLCheckPropVos!=null && prpLCheckPropVos.size()>0){
			    				vehicleVo.setIs_protect_loss("1");//??????????????????
			    				losssonFlag="1";
			    			}else{
			    				vehicleVo.setIs_protect_loss("0");
			    			}
			    			if(prpLCheckPersonVos!=null && prpLCheckPersonVos.size()>0){
			    				vehicleVo.setIs_person_injured("1");//??????????????????
			    				personFlag="1";
			    			}else{
			    				vehicleVo.setIs_person_injured("0");//??????????????????
			    			}
			    			if(1==carVo.getSerialNo()){
			    				PrplcodeDictVo  prplcodeDictVo8=claimService.findPrplcodeDictByCodeAndcodeType("UseKind",citermsCarVos.get(0).getUseKindCode());
			    				if(prplcodeDictVo8!=null){
			    					vehicleVo.setUsing_properties(prplcodeDictVo8.getTcode());
			    				}
			    			}
			    			vehicleVo.setRescue_amount(exNull(carVo.getRescueFee()));//????????????
			    			vehicleVo.setEstimated_loss_amount(exNull(carVo.getLossFee()));//????????????
			    			vehicleVo.setRemark("??????????????????");
			    			checkVo.setVehicle(vehicleVo);
			    			break;
						}						
					 }
					}
					baseVo.setCheck(checkVo);//????????????
			  }	
			}
			
			//????????????
			ConfirmLossVo ConfirmLossVo=new ConfirmLossVo();
			//01--????????????????????? 03--??????????????????
			if(!"01".equals(nodeFlag) && !"03".equals(nodeFlag) && StringUtils.isNotBlank(lossCarMainId)){
				PrpLDlossCarMainVo  prpLDlossCarMainVo =lossCarService.findLossCarMainById(Long.valueOf(lossCarMainId));
				PrpLDlossCarInfoVo prpLDlossCarInfoVo=prpLDlossCarMainVo.getLossCarInfoVo();//????????????
				ConfirmLossVo.setEstimate_task_no(lossCarMainId);//???????????????????????????Id
				List<PrpLDlossCarMainVo> losscarMainVos=lossCarService.findLossCarMainByRegistNo(registNo);
				int addcount=0;
				if(losscarMainVos!=null && losscarMainVos.size()>0){
					for(PrpLDlossCarMainVo losscarVo:losscarMainVos){
						if(StringUtils.isNotBlank(prpLDlossCarMainVo.getLicenseNo()) && prpLDlossCarMainVo.getLicenseNo().equals(losscarVo.getLicenseNo()) && "DLCarAdd".equals(losscarVo.getFlowFlag())){
							addcount=addcount+1;
						}
					}
				}
				String losspartStr1="";//????????????
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
    			ConfirmLossVo.setLoss_part_data(losspartStr1);//????????????
				ConfirmLossVo.setEstimate_count("1");
				ConfirmLossVo.setAdd_count(addcount+"");//????????????
				ConfirmLossVo.setVin(prpLDlossCarInfoVo.getVinNo());//?????????
				if("1".equals(prpLDlossCarMainVo.getDeflossCarType()) && citermsCarVos!=null && citermsCarVos.size()>0){//?????????
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
					if("??????1???".equals(prpLDlossCarMainVo.getWaterFloodedLevel())){
						ConfirmLossVo.setWater_level("2");
					}else if("??????2???".equals(prpLDlossCarMainVo.getWaterFloodedLevel())){
						ConfirmLossVo.setWater_level("3");
					}else if("??????3???".equals(prpLDlossCarMainVo.getWaterFloodedLevel())){
						ConfirmLossVo.setWater_level("4");
					}else{
						ConfirmLossVo.setWater_level("5");//????????????
					}
					
				}
				ConfirmLossVo.setLicense_plate_type(prpLDlossCarInfoVo.getLicenseType());//????????????
				ConfirmLossVo.setModel_code(prpLDlossCarInfoVo.getModelCode());
				ConfirmLossVo.setModel_name(prpLDlossCarInfoVo.getModelName());//????????????
				if(StringUtils.isNotBlank(prpLDlossCarInfoVo.getIdentifyType()) && ("550".equals(prpLDlossCarInfoVo.getIdentifyType()) || "553".equals(prpLDlossCarInfoVo.getIdentifyType()) || "00".equals(prpLDlossCarInfoVo.getIdentifyType()))){
					ConfirmLossVo.setDriving_identify_type("99");
				}else if(StringUtils.isNotBlank(prpLDlossCarInfoVo.getIdentifyType())){
					ConfirmLossVo.setDriving_identify_type(prpLDlossCarInfoVo.getIdentifyType());
				}else{
					ConfirmLossVo.setDriving_identify_type("99");//????????????
				}
				ConfirmLossVo.setEstimate_start_time(DateFormatString(prpLDlossCarMainVo.getCreateTime()));//??????????????????
				ConfirmLossVo.setEstimate_end_time(DateFormatString(prpLDlossCarMainVo.getDefEndDate()));//??????????????????
				if("14".equals(nodeFlag)){//????????????
					ConfirmLossVo.setUnder_start_Time(DateFormatString(prpLDlossCarMainVo.getUnderWriteEndDate()));//??????????????????
					ConfirmLossVo.setUnder_end_Time(DateFormatString(prpLDlossCarMainVo.getUnderWriteEndDate()));//??????????????????
				}
				if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
					ConfirmLossVo.setLoss_task("1");//????????????--?????????
				}else{
					ConfirmLossVo.setLoss_task("2");//????????????--?????????
				}
				if("01".equals(prpLDlossCarMainVo.getCetainLossType())){
					ConfirmLossVo.setEstimate_mode("1");//????????????
				}else if("02".equals(prpLDlossCarMainVo.getCetainLossType())){
					ConfirmLossVo.setEstimate_mode("3");//????????????
				}else if("03".equals(prpLDlossCarMainVo.getCetainLossType())){
					ConfirmLossVo.setEstimate_mode("3");//????????????
				}else if("04".equals(prpLDlossCarMainVo.getCetainLossType())){
					ConfirmLossVo.setEstimate_mode("2");//????????????
				}else if("06".equals(prpLDlossCarMainVo.getCetainLossType())){
					ConfirmLossVo.setEstimate_mode("2");//????????????
				}else{
					ConfirmLossVo.setEstimate_mode("9999");//????????????
				}
				ConfirmLossVo.setEstimate_address(prpLDlossCarMainVo.getDefSite());
				ConfirmLossVo.setRepair_factory_category("1");//???????????????
				if("001".equals(prpLDlossCarMainVo.getRepairFactoryType())){
					ConfirmLossVo.setRepair_factory_type("1");//???????????????
				}else if("002".equals(prpLDlossCarMainVo.getRepairFactoryType())){
					ConfirmLossVo.setRepair_factory_type("0");//???????????????
				}else{
					ConfirmLossVo.setRepair_factory_type("2");//???????????????
				}
				
				ConfirmLossVo.setRepair_factory_contact(prpLDlossCarMainVo.getFactoryMobile());//???????????????
				if(StringUtils.isNotBlank(prpLDlossCarMainVo.getRepairFactoryCode())){
					PrpLRepairFactoryVo prpLRepairFactoryVo=repairFactoryService.findFactoryByCode(prpLDlossCarMainVo.getRepairFactoryCode());
					if(prpLRepairFactoryVo!=null){
						ConfirmLossVo.setRepair_factory_contact(prpLRepairFactoryVo.getMobile());//??????????????????
						ConfirmLossVo.setRepair_factory_code(prpLRepairFactoryVo.getFactoryCode());//???????????????
						ConfirmLossVo.setRepair_factory_name(prpLRepairFactoryVo.getFactoryName());//???????????????
					}else{
						ConfirmLossVo.setRepair_factory_contact(prpLDlossCarMainVo.getFactoryMobile());//??????????????????
					}
					
			     }
				if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
					ConfirmLossVo.setSubject_third("1");//????????????
				}else{
					ConfirmLossVo.setSubject_third("0");
				}
				ConfirmLossVo.setEstimate_code(prpLDlossCarMainVo.getHandlerCode());//???????????????
				ConfirmLossVo.setEstimate_name(prpLDlossCarMainVo.getHandlerName());
				ConfirmLossVo.setEstimate_first_total(exNull(prpLDlossCarMainVo.getSumLossFee()));//??????????????????
				ConfirmLossVo.setInsurance_estimate_total(exNull(prpLDlossCarMainVo.getSumLossFee()));
				ConfirmLossVo.setInsurance_first_rescue_fee(exNull(prpLDlossCarMainVo.getSumRescueFee()));
				ConfirmLossVo.setInsurance_rescue_fee(exNull(prpLDlossCarMainVo.getSumRescueFee()));
				if("14".equals(nodeFlag) || "15".equals(nodeFlag)){//14--?????????15--??????
					ConfirmLossVo.setVerify_price_code(prpLDlossCarMainVo.getVeripHandlerCode());//????????????????????? 
					ConfirmLossVo.setVerify_price_name(prpLDlossCarMainVo.getVeripHandlerName());//?????????????????????
					ConfirmLossVo.setVerify_price_total(exNull(prpLDlossCarMainVo.getSumVeripLoss()));
				}
				if("14".equals(nodeFlag)){//??????
					ConfirmLossVo.setUnder_write_code(prpLDlossCarMainVo.getUnderWriteCode());
					ConfirmLossVo.setUnder_write_name(prpLDlossCarMainVo.getUnderWriteName());
					ConfirmLossVo.setUnder_first_total(exNull(prpLDlossCarMainVo.getSumVeriLossFee()));
					ConfirmLossVo.setInsurance_under_total(exNull(prpLDlossCarMainVo.getSumVeriLossFee()));
				}
				
				//????????????
				VehicleVo vehicleVo=new VehicleVo();
				if("1".equals(configValueVo.getConfigValue())){
					vehicleVo.setOwner(prpLDlossCarInfoVo.getCarOwner());//??????
				}
				if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
					vehicleVo.setSubject_third("1");//????????????
					if(citermsCarVos.get(0).getWholeWeight()!=null){
						vehicleVo.setTonnage(exNull((citermsCarVos.get(0).getWholeWeight()/1000)));//????????????
    				}
					vehicleVo.setVehicle_price(exNull(citermsCarVos.get(0).getActualValue()));//??????????????????
				}else{
					vehicleVo.setSubject_third("0");
				}
				vehicleVo.setLicense_plate_no(prpLDlossCarInfoVo.getLicenseNo());//?????????
				if(prpLCMainVos!=null && prpLCMainVos.size()>0){
					for(PrpLCMainVo cmainVo:prpLCMainVos){
						if("1101".equals(cmainVo.getRiskCode())){
							vehicleVo.setBz_policy_no(cmainVo.getPolicyNo());
						}
					}
					
				}
				vehicleVo.setBz_company_code(CodeConstants.CEDHICCODE);
				vehicleVo.setSub_company_name("????????????????????????????????????");
				vehicleVo.setModel_name(prpLDlossCarInfoVo.getModelName());
				vehicleVo.setModel_code(prpLDlossCarInfoVo.getModelCode());
				vehicleVo.setVin(prpLDlossCarInfoVo.getVinNo());
				vehicleVo.setVehicle_brand_code(prpLDlossCarInfoVo.getBrandName());//????????????
				vehicleVo.setVehicle_series_code(prpLDlossCarInfoVo.getSeriName());//??????
				vehicleVo.setGroup_veh_code(prpLDlossCarInfoVo.getGroupName());//??????
				if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
					PrplcodeDictVo  prplcodeDictVo7=claimService.findPrplcodeDictByCodeAndcodeType("CarType",citermsCarVos.get(0).getCarType());
					if(prplcodeDictVo7!=null){
						vehicleVo.setCar_kind_code(prplcodeDictVo7.getTcode());
					}
					 
				}else{
					 vehicleVo.setCar_kind_code(prpLDlossCarInfoVo.getPlatformCarKindCode());//????????????
				}
				vehicleVo.setVehicle_brand_code(prpLDlossCarInfoVo.getBrandName());//????????????
				vehicleVo.setVehicle_series_code(prpLDlossCarInfoVo.getSeriName());////????????????
				vehicleVo.setGroup_veh_code(prpLDlossCarInfoVo.getGroupName());//????????????
    			vehicleVo.setBrand_model(prpLDlossCarInfoVo.getBrandName());
    			vehicleVo.setLicense_plate_type(prpLDlossCarInfoVo.getLicenseType());//????????????
    			vehicleVo.setEngine_no(prpLDlossCarInfoVo.getEngineNo());//????????????
    			//vehicleVo.setPlate_color("");//????????????
    			if("05".equals(prpLDlossCarMainVo.getCetainLossType())){
    				vehicleVo.setIs_loss("0");//???????????????
    			}else{
    				vehicleVo.setIs_loss("1");//???????????????
    			}
    			String losspartStr="";//????????????
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
    			vehicleVo.setLoss_part_data(losspartStr);//????????????
    			// fix by LiYi ??????????????? ,?????????????????????????????????
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
    					// otherNature?????? , ??????????????????
    					registLicenseNo = false;
    				}
    			  }else{
    				registLicenseNo = false;
    			  }
    			  if(registLicenseNo){
    				  vehicleVo.setHas_car_license("1");//???????????????
    			  }else{
    				  vehicleVo.setHas_car_license("0");
    			  }
    			 
    			}
    			vehicleVo.setIs_protect_loss(losssonFlag);//??????????????????
    			vehicleVo.setIs_person_injured(personFlag);//??????????????????
    			if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
    				PrplcodeDictVo  prplcodeDictVo8=claimService.findPrplcodeDictByCodeAndcodeType("UseKind",citermsCarVos.get(0).getUseKindCode());
    				if(prplcodeDictVo8!=null){
    					vehicleVo.setUsing_properties(prplcodeDictVo8.getTcode());
    				}
    			}
    			vehicleVo.setRescue_amount(exNull(prpLDlossCarMainVo.getSumRescueFee()));//????????????
    			vehicleVo.setEstimated_loss_amount(exNull(prpLDlossCarMainVo.getSumLossFee()));//????????????
    			vehicleVo.setRemark("??????????????????");
    			ConfirmLossVo.setVehicle(vehicleVo);
				
    			//???????????????????????????
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
					driver.setIdentify_type("99");//????????????
				}
				//driver.setAllow_driving_vehicle("");//????????????
				driver.setIs_driving_license_effective("1");//???????????????????????????
				//driver.setDriving_license_date(DateFormatString(prpLCheckDriverVo.getAcceptLicenseDate()));//??????????????????
				ConfirmLossVo.setDriver(driver);
				
				
				//????????????
				List<SparepartVo> sparepartVos=new ArrayList<SparepartVo>();
				List<PrpLDlossCarCompVo> carCompVos=prpLDlossCarMainVo.getPrpLDlossCarComps();
				if(carCompVos!=null && carCompVos.size()>0){
					for(PrpLDlossCarCompVo carCompVo:carCompVos){
						SparepartVo sparepartVo=new SparepartVo();
						sparepartVo.setInsurance_fit_id(carCompVo.getIndId());//????????????id
						sparepartVo.setOem_code(carCompVo.getOriginalId());
						sparepartVo.setFitting_name(carCompVo.getCompName());
						sparepartVo.setFitting_num(exNull(carCompVo.getQuantity()));
						if("001".equals(prpLDlossCarMainVo.getRepairFactoryType())){
							sparepartVo.setPrice_scheme("1");//????????????
						}else{
							sparepartVo.setPrice_scheme("2");
						}
						sparepartVo.setFirst_estimate_price(exNull(carCompVo.getMaterialFee()));//????????????
						sparepartVo.setFirst_estimate_quantity(exNull(carCompVo.getQuantity()));
						sparepartVo.setFirst_estimate_total(exNull(carCompVo.getSumDefLoss()));
						sparepartVo.setFirst_estimate_remnant(exNull(carCompVo.getRestFee()));//??????
						sparepartVo.setFirst_estimate_is_recycle(carCompVo.getRecycleFlag());//??????
						sparepartVo.setEstimate_price(exNull(carCompVo.getMaterialFee()));
						sparepartVo.setEstimate_quantity(exNull(carCompVo.getQuantity()));
						sparepartVo.setEstimate_total(exNull(carCompVo.getSumDefLoss()));
						sparepartVo.setEstimate_remnant(exNull(carCompVo.getRestFee()));
						sparepartVo.setEstimate_is_recycle(carCompVo.getRecycleFlag());
						if("14".equals(nodeFlag) || "15".equals(nodeFlag)){//14--?????????15--??????
							sparepartVo.setVerify_price_price(exNull(carCompVo.getMaterialFee()));
							sparepartVo.setVerify_price_quantity(exNull(carCompVo.getQuantity()));
							sparepartVo.setVerify_price_total(exNull(carCompVo.getSumCheckLoss()));
							sparepartVo.setVerify_price_is_recycle(carCompVo.getRecycleFlag());
							sparepartVo.setVerify_price_remnant(exNull(carCompVo.getVeripRestFee()));
						}
						if("14".equals(nodeFlag)){//14--?????????15--?????? 
							sparepartVo.setUnder_price(exNull(carCompVo.getVeriMaterFee()));
							sparepartVo.setUnder_quantity(exNull(carCompVo.getVeriQuantity()));
							sparepartVo.setUnder_total(exNull(carCompVo.getSumVeriLoss()));
							sparepartVo.setUnder_is_recycle(carCompVo.getRecycleFlag());
							sparepartVo.setUnder_remnant(exNull(carCompVo.getVeriRestFee()));
						}
						//sparepartVo.setDescription("????????????");
						sparepartVo.setIs_hand(carCompVo.getSelfConfigFlag());//??????????????????
						sparepartVo.setOperate_code(prpLDlossCarMainVo.getHandlerCode());
						sparepartVo.setOperate_name(prpLDlossCarMainVo.getHandlerName());//??????????????????
						sparepartVos.add(sparepartVo);
					}
					ConfirmLossVo.setSparepart(sparepartVos);//????????????
				}
				
				//????????????
				List<SmallSparepartVo> smallSparepartVos=new ArrayList<SmallSparepartVo>();
				List<PrpLDlossCarMaterialVo> MaterialVos=prpLDlossCarMainVo.getPrpLDlossCarMaterials();
				if(MaterialVos!=null && MaterialVos.size()>0){
					for(PrpLDlossCarMaterialVo materialVo:MaterialVos){
						SmallSparepartVo SmallSparepartVo=new SmallSparepartVo();
						SmallSparepartVo.setInsurance_material_id(materialVo.getAssistId());//??????Id
						SmallSparepartVo.setBatch_no(exNull(prpLDlossCarMainVo.getId()));//?????????
						SmallSparepartVo.setMaterial_code(materialVo.getAssistId());//????????????
						SmallSparepartVo.setMaterial_name(materialVo.getMaterialName());
						SmallSparepartVo.setFirst_estimate_price(exNull(materialVo.getUnitPrice()));//???????????????
						SmallSparepartVo.setFirst_estimate_quantity(exNull(materialVo.getAssisCount()));
						SmallSparepartVo.setFirst_estimate_total(exNull(materialVo.getMaterialFee()));//?????????????????????
						SmallSparepartVo.setEstimate_price(exNull(materialVo.getUnitPrice()));//??????
						SmallSparepartVo.setEstimate_quantity(exNull(materialVo.getAssisCount()));//????????????
						SmallSparepartVo.setEstimate_total(exNull(materialVo.getMaterialFee()));
						if("001".equals(prpLDlossCarMainVo.getRepairFactoryType())){
							SmallSparepartVo.setPrice_scheme("1");//????????????
						}else{
							SmallSparepartVo.setPrice_scheme("2");
						}
						if("14".equals(nodeFlag) || "15".equals(nodeFlag)){//14--?????????15--??????
							SmallSparepartVo.setVerify_price_price(exNull(materialVo.getAuditPrice()));//????????????
							SmallSparepartVo.setVerify_price_quantity(exNull(materialVo.getAuditCount()));
							SmallSparepartVo.setVerify_price_total(exNull(materialVo.getAuditMaterialFee()));
						}
						if("14".equals(nodeFlag)){//14--??????
							SmallSparepartVo.setUnder_price(exNull(materialVo.getAuditLossPrice()));
							SmallSparepartVo.setUnder_quantity(exNull(materialVo.getAuditLossCount()));
							SmallSparepartVo.setUnder_total(exNull(materialVo.getAuditLossMaterialFee()));
						}
						SmallSparepartVo.setIs_hand(materialVo.getSelfConfigFlag());//??????????????????
						SmallSparepartVo.setOperate_code(prpLDlossCarMainVo.getHandlerCode());
						SmallSparepartVo.setOperate_name(prpLDlossCarMainVo.getHandlerName());
						//SmallSparepartVo.setDescription("????????????");
						smallSparepartVos.add(SmallSparepartVo);
					}
					ConfirmLossVo.setSmallSparepart(smallSparepartVos);//????????????
				}
				
				//????????????
				List<LaborVo> laborVos=new ArrayList<LaborVo>();
				List<PrpLDlossCarRepairVo> prpLDlossCarRepairVos=prpLDlossCarMainVo.getPrpLDlossCarRepairs();
				if(prpLDlossCarRepairVos!=null && prpLDlossCarRepairVos.size()>0){
					for(PrpLDlossCarRepairVo repairVo:prpLDlossCarRepairVos){
						LaborVo laborVo=new LaborVo();
						laborVo.setInsurance_labor_id(repairVo.getRepairId());//??????????????????ID
						laborVo.setBatch_no(exNull(prpLDlossCarMainVo.getId()));//?????????
						if("001".equals(prpLDlossCarMainVo.getRepairFactoryType())){
							laborVo.setPrice_scheme("1");//????????????
						}else{
							laborVo.setPrice_scheme("2");
						}
						laborVo.setRepair_code(repairVo.getCompCode());
						laborVo.setRepair_name(repairVo.getCompName());
						PrplcodeDictVo  prplcodeDictVo7=claimService.findPrplcodeDictByCodeAndcodeType("RepairType",repairVo.getCompCode());
						if(prplcodeDictVo7!=null){
							laborVo.setRepair_type(prplcodeDictVo7.getTcode());
						}else{
							laborVo.setRepair_type("99");//??????
						}
						laborVo.setFirst_estimate_unit_price(exNull(repairVo.getManHourUnitPrice()));//??????
						laborVo.setFirst_estimate_quantity(exNull(repairVo.getManHour()));//?????????
						laborVo.setFirst_estimate_price(exNull(repairVo.getSumDefLoss()));//??????
						laborVo.setLabor(exNull(repairVo.getManHour()));
						laborVo.setLabor_price(exNull(repairVo.getManHourUnitPrice()));
						laborVo.setEstimate_price(exNull(repairVo.getManHourFee()));
						if("14".equals(nodeFlag) || "15".equals(nodeFlag)){//14--?????????15--??????
							laborVo.setVerify_price_price(exNull(repairVo.getSumDefLoss()));//????????????
							laborVo.setVerify_price_quantity(exNull(repairVo.getManHour()));//???????????????
							laborVo.setVerify_price_unit_price(exNull(repairVo.getManHourUnitPrice()));//????????????
							
						}
						if("14".equals(nodeFlag)){//14--??????
							laborVo.setUnder_quantity(exNull(repairVo.getVeriManHour()));//???????????????
							laborVo.setUnder_unit_price(exNull(repairVo.getVeriManUnitPrice()));//????????????
							laborVo.setUnder_price(exNull(repairVo.getSumVeriLoss()));//????????????
						}
						//laborVo.setDescription("????????????");
						laborVo.setIs_hand(repairVo.getSelfConfigFlag());//??????????????????	0?????? 1??????
						laborVo.setOperate_code(prpLDlossCarMainVo.getHandlerCode());//???????????????????????????
						laborVo.setOperate_name(prpLDlossCarMainVo.getHandlerName());//???????????????????????????
						laborVos.add(laborVo);
					}
					ConfirmLossVo.setLabor(laborVos);//????????????
				}
				
				//????????????????????????
				List<EstimateRepairVo> estimateRepairVos=new ArrayList<EstimateRepairVo>();
				if(prpLDlossCarRepairVos!=null && prpLDlossCarRepairVos.size()>0){
					for(PrpLDlossCarRepairVo repairVo:prpLDlossCarRepairVos){
						if(CodeConstants.RepairFlag.OUTREPAIR.equals(repairVo.getRepairFlag())){//??????
							EstimateRepairVo estimateRepairVo=new EstimateRepairVo();
							estimateRepairVo.setFit_name(repairVo.getCompName());
							estimateRepairVo.setEstimate_total(exNull(repairVo.getSumDefLoss()));//????????????
							estimateRepairVo.setVerify_price_total(exNull(repairVo.getSumDefLoss()));
							estimateRepairVo.setUnder_total(exNull(repairVo.getSumVeriLoss()));//????????????
							//estimateRepairVo.setDescription("????????????");
							estimateRepairVos.add(estimateRepairVo);
						}
					}
				}
				
				if(estimateRepairVos!=null && estimateRepairVos.size()>0){
					ConfirmLossVo.setEstimateRepair(estimateRepairVos);
				}
				
				
				
				//???????????????
				List<ConfirmLossDiscussionVo> lossDiscussionVos=new ArrayList<ConfirmLossDiscussionVo>();
				List<PrpLClaimTextVo> prpLClaimTextVoList = claimTextService.findClaimTextByregistNoAndbussTaskId(prpLDlossCarMainVo.getRegistNo(),prpLDlossCarMainVo.getId());
				if(prpLClaimTextVoList!=null && prpLClaimTextVoList.size()>0){
					for(PrpLClaimTextVo textVo:prpLClaimTextVoList){
						if(StringUtils.isNotBlank(textVo.getNodeCode()) && (textVo.getNodeCode().contains("DLCar") || textVo.getNodeCode().contains("VLCar") || textVo.getNodeCode().contains("VPCar"))){
							ConfirmLossDiscussionVo discussionVo=new ConfirmLossDiscussionVo();
							if(textVo.getNodeCode().contains("DLCar")){
								discussionVo.setTask_type("04");//??????
								discussionVo.setAmount(exNull(prpLDlossCarMainVo.getSumLossFee()));
							}else if(textVo.getNodeCode().contains("VPCar")){
								discussionVo.setTask_type("15");//??????
								discussionVo.setAmount(exNull(prpLDlossCarMainVo.getSumVeripLoss()));
							}else{
								discussionVo.setTask_type("14");//??????
								discussionVo.setAmount(exNull(prpLDlossCarMainVo.getSumVeriLossFee()));
							}
							discussionVo.setComment(textVo.getDescription());
							discussionVo.setCompany_name("????????????????????????????????????");
							discussionVo.setPublish_date(DateFormatString(textVo.getInputTime()));
							discussionVo.setPerson_code(textVo.getOperatorCode());
							discussionVo.setPerson_name(textVo.getOperatorName());
							if(StringUtils.isNotBlank(textVo.getDescription()) && textVo.getDescription().contains("??????")){
								discussionVo.setIs_pass("1");
							}else{
								discussionVo.setIs_pass("0");
							}
							lossDiscussionVos.add(discussionVo);
						}
						
					}
					ConfirmLossVo.setConfirmLossDiscussion(lossDiscussionVos);//????????????
				}
				baseVo.setConfirmLoss(ConfirmLossVo);//????????????
				
				    //?????????????????????????????????????????????
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
					 checkVo.setCheck_start_time(DateFormatString(prpLCheckTaskVo.getCheckDate()));//??????????????????
					 checkVo.setCheck_end_time(DateFormatString(prpLCheckVo.getChkSubmitTime()));//??????????????????
					 checkVo.setCheck_addr(prpLCheckTaskVo.getCheckAddress());//????????????
					 checkVo.setAppoint_check_addr(prpLCheckTaskVo.getCheckAddress());//??????????????????
					 checkVo.setChecker_name(prpLCheckTaskVo.getChecker());//?????????
					 checkVo.setChecker_code(prpLCheckTaskVo.getCheckerCode());
					 checkVo.setChecker_phone1(prpLCheckTaskVo.getCheckerPhone());//???????????????
					 checkVo.setDanger_address(prpLRegistVo.getDamageAddress());
					 if(prplcodeDictVo2!=null){
						 checkVo.setDanger_cause(prplcodeDictVo2.getTcode());//????????????
						}else{
						 checkVo.setDanger_cause("A10095A");//??????
					  }
					 checkVo.setDanger_desc(prpLRegistExtVo.getDangerRemark());
					 checkVo.setCase_type("2");//????????????
					 PrplcodeDictVo  prplcodeDictVo4=claimService.findPrplcodeDictByCodeAndcodeType("AccidentDutyType",prpLCheckVo.getDamageTypeCode());
						if(prplcodeDictVo4!=null){
							checkVo.setEvent_type(prplcodeDictVo4.getTcode());//????????????
						}else{
							checkVo.setEvent_type("2");//????????????--??????
						}
						if(prplcodeDictVo3!=null){
							checkVo.setEvent_process_mode(prplcodeDictVo3.getTcode());//??????????????????
						}else{
							checkVo.setEvent_process_mode("9");//??????
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
							checkVo.setEvent_duty_ratio("50.00");//??????????????????
						}
						//checkVo.setNo_fault_compensate("0");//????????????
						if("DM12".equals(prpLCheckVo.getDamageCode())){
							if("??????1???".equals(prpLCheckVo.getWaterLoggingLevel())){
								checkVo.setWater_level("2");
							}else if("??????2???".equals(prpLCheckVo.getWaterLoggingLevel())){
								checkVo.setWater_level("3");
							}else if("??????3???".equals(prpLCheckVo.getWaterLoggingLevel())){
								checkVo.setWater_level("4");
							}else{
								checkVo.setWater_level("5");//????????????
							}
							
						}
						if("1".equals(prpLCheckVo.getIsSubRogation())){
							checkVo.setSubrogation_flag("1");
						}else{
							checkVo.setSubrogation_flag("0");//??????????????????
						}
						checkVo.setIs_rescue(prpLRegistExtVo.getIsRescue());//??????????????????
						checkVo.setIs_person_injured(prpLCheckVo.getIsPersonLoss());//??????????????????
						checkVo.setBuckle_ded_reason("???");//??????????????????
						checkVo.setIs_mutual_collision_self_compensation(prpLCheckVo.getIsClaimSelf());//??????????????????
						checkVo.setIs_car_insurance("1");//???????????????????????????
						checkVo.setCheck_desc(prpLCheckTaskVo.getContexts());//????????????
						
						PrplcodeDictVo  prplcodeDictVo5=claimService.findPrplcodeDictByCodeAndcodeType("CheckType",prpLCheckVo.getCheckType());
						if(prplcodeDictVo5!=null){
							checkVo.setCheck_type(prplcodeDictVo5.getTcode());//????????????
						}else{
							checkVo.setCheck_type("1");
						}
						
						checkVo.setAudit_name(prpLCheckTaskVo.getChecker());//?????????
						checkVo.setAudit_code(prpLCheckTaskVo.getCheckerCode());
						checkVo.setAudit_opinion(prpLCheckTaskVo.getContexts());
						checkVo.setAudit_time(DateFormatString(prpLCheckVo.getChkSubmitTime()));//????????????
						
						//?????????????????????
						driver.setLicense_plate_no(prpLDlossCarInfoVo.getLicenseNo());
		    			driver.setDriver_name(prpLDlossCarInfoVo.getDriveName());
		    			driver.setDriving_license_no(prpLDlossCarInfoVo.getDrivingLicenseNo());
						PrplcodeDictVo  prplcodeDictVo06=claimService.findPrplcodeDictByCodeAndcodeType("IdentifyType",prpLDlossCarInfoVo.getIdentifyType());
						if(prplcodeDictVo06!=null){
							driver.setIdentify_type(prplcodeDictVo06.getTcode());
						}else{
							driver.setIdentify_type("99");//????????????
						}
						//driver.setAllow_driving_vehicle("");//????????????
						driver.setIs_driving_license_effective("1");//???????????????????????????
						
						DriverVo driverVo=new DriverVo();
						driverVo.setLicense_plate_no(prpLDlossCarInfoVo.getLicenseNo());
						driverVo.setDriver_name(prpLDlossCarInfoVo.getDriveName());
						driverVo.setDriving_license_no(prpLDlossCarInfoVo.getDrivingLicenseNo());
						PrplcodeDictVo  prplcodeDictVo04=claimService.findPrplcodeDictByCodeAndcodeType("IdentifyType",prpLDlossCarInfoVo.getIdentifyType());
						if(prplcodeDictVo04!=null){
							driverVo.setIdentify_type(prplcodeDictVo04.getTcode());
	    				}else{
	    					driverVo.setIdentify_type("99");//????????????
	    				}
						//driverVo.setAllow_driving_vehicle(prpLCheckDriverVo.getDrivingCarType());//????????????
						driverVo.setIs_driving_license_effective("1");//???????????????????????????
						//driverVo.setDriving_license_date(DateFormatString(prpLCheckDriverVo.getAcceptLicenseDate()));//??????????????????
						checkVo.setDriver(driverVo);
						//????????????
						//????????????
						VehicleVo vehiclecVo=new VehicleVo();
						vehiclecVo.setOwner(prpLDlossCarInfoVo.getCarOwner());//??????
						if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
							vehiclecVo.setSubject_third("1");//????????????
							if(citermsCarVos.get(0).getWholeWeight()!=null){
								vehiclecVo.setTonnage(exNull((citermsCarVos.get(0).getWholeWeight()/1000)));//????????????
		    				}
							vehiclecVo.setVehicle_price(exNull(citermsCarVos.get(0).getActualValue()));//??????????????????
						}else{
							vehiclecVo.setSubject_third("0");
						}
						vehiclecVo.setLicense_plate_no(prpLDlossCarInfoVo.getLicenseNo());//?????????
						if(prpLCMainVos!=null && prpLCMainVos.size()>0){
							for(PrpLCMainVo cmainVo:prpLCMainVos){
								if("1101".equals(cmainVo.getRiskCode())){
									vehiclecVo.setBz_policy_no(cmainVo.getPolicyNo());
								}
							}
							
						}
						vehiclecVo.setBz_company_code(CodeConstants.CEDHICCODE);
						vehiclecVo.setSub_company_name("????????????????????????????????????");
						vehiclecVo.setModel_name(prpLDlossCarInfoVo.getModelName());
						vehiclecVo.setModel_code(prpLDlossCarInfoVo.getModelCode());
						vehiclecVo.setVin(prpLDlossCarInfoVo.getVinNo());
						vehiclecVo.setVehicle_brand_code(prpLDlossCarInfoVo.getBrandName());//????????????
						vehiclecVo.setVehicle_series_code(prpLDlossCarInfoVo.getSeriName());//??????
						vehiclecVo.setGroup_veh_code(prpLDlossCarInfoVo.getGroupName());//??????
						if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
							PrplcodeDictVo  prplcodeDictVo7=claimService.findPrplcodeDictByCodeAndcodeType("CarType",citermsCarVos.get(0).getCarType());
							if(prplcodeDictVo7!=null){
								vehiclecVo.setCar_kind_code(prplcodeDictVo7.getTcode());
							}
							 
						}else{
							vehiclecVo.setCar_kind_code(prpLDlossCarInfoVo.getPlatformCarKindCode());//????????????
						}
						vehiclecVo.setVehicle_brand_code(prpLDlossCarInfoVo.getBrandName());//????????????
						vehiclecVo.setVehicle_series_code(prpLDlossCarInfoVo.getSeriName());////????????????
						vehiclecVo.setGroup_veh_code(prpLDlossCarInfoVo.getGroupName());//????????????
						vehiclecVo.setBrand_model(prpLDlossCarInfoVo.getBrandName());
						vehiclecVo.setLicense_plate_type(prpLDlossCarInfoVo.getLicenseType());//????????????
						vehiclecVo.setEngine_no(prpLDlossCarInfoVo.getEngineNo());//????????????
		    			//vehicleVo.setPlate_color("");//????????????
		    			if("05".equals(prpLDlossCarMainVo.getCetainLossType())){
		    				vehiclecVo.setIs_loss("0");//???????????????
		    			}else{
		    				vehiclecVo.setIs_loss("1");//???????????????
		    			}
		    			String losspartStrc="";//????????????
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
		    			vehiclecVo.setLoss_part_data(losspartStr);//????????????
		    			// fix by LiYi ??????????????? ,?????????????????????????????????
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
		    					// otherNature?????? , ??????????????????
		    					registLicenseNo = false;
		    				}
		    			  }else{
		    				registLicenseNo = false;
		    			  }
		    			  if(registLicenseNo){
		    				  vehiclecVo.setHas_car_license("1");//???????????????
		    			  }else{
		    				  vehiclecVo.setHas_car_license("0");
		    			  }
		    			 
		    			}
		    			vehiclecVo.setIs_protect_loss(losssonFlag);//??????????????????
		    			vehiclecVo.setIs_person_injured(personFlag);//??????????????????
		    			if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
		    				PrplcodeDictVo  prplcodeDictVo8=claimService.findPrplcodeDictByCodeAndcodeType("UseKind",citermsCarVos.get(0).getUseKindCode());
		    				if(prplcodeDictVo8!=null){
		    					vehiclecVo.setUsing_properties(prplcodeDictVo8.getTcode());
		    				}
		    			}
		    			vehiclecVo.setRescue_amount(exNull(prpLDlossCarMainVo.getSumRescueFee()));//????????????
		    			vehiclecVo.setEstimated_loss_amount(exNull(prpLDlossCarMainVo.getSumLossFee()));//????????????
		    			vehiclecVo.setRemark("??????????????????");
		    			checkVo.setVehicle(vehiclecVo);
						baseVo.setCheck(checkVo);//????????????
				  }	
				
			}
			
			//????????????
			String ceNumber=SpringProperties.getProperty("CE_NUMBER");//????????????????????????
			int imageNum=100;//??????????????????
			int imageN=0;//?????????????????????
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
									if(imageN<=100 && "1".equals(configValueVo.getConfigValue())){//?????????????????????100?????????????????????????????????
										imageUrr(nodeImageVos1,claimPhotosVos,licenseNo,imageUrlbaseVo);										
									}else{//????????????????????????????????????
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
																	/*if(imageVo2.getName().contains("??????")){
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
			//??????
			if(claimPhotosVos!=null && claimPhotosVos.size()>0){
				baseVo.setClaimPhotos(claimPhotosVos);
			}
			
    	return baseVo;
    }
    
    /**
	 * ??????????????????
	 * Date ???????????? String??????
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
	 * ??????????????????
	 * Date ???????????? String??????
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
     * ??????????????????????????????
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
     * ????????????
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
     * ????????????????????????
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
     * ??????????????????
     * @param imageType,
     * @return
     */
    private String imageType(String imageId){
    	String imagetype="";
    	if(StringUtils.isNotBlank(imageId)){
    		if(imageId.contains("images_1")){
    			if(imageId.equals("images_1_1")){
    				imagetype="carlossa";//?????????
    			}else{
    				imagetype="carlossb";//?????????
    			}
    		}else if(imageId.contains("images_2")){
    			imagetype="propLoss";//????????????
    		}else if(imageId.contains("images_3")){
    			imagetype="personLoss_p";//????????????
    		}else if(imageId.contains("scenePicture")){
    			imagetype="scenePicture";//????????????
    		}else if(imageId.contains("checkReport")){
    			imagetype="checkReport";//????????????
    		}else if(imageId.contains("assessorsFee")){
    			imagetype="assessorsFee";//???????????????
    		}else if(imageId.contains("checksFee")){
    			imagetype="checksFee";//???????????????
    		}else{
    			imagetype="untype";//??????
    		}
    	}
    	
    	return imagetype;
    	
    }
}
