package ins.sino.claimcar.sdpolice.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.LossParty;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersExtVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropFeeVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.platform.service.CiCodeTranService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.sdpolice.policeInfoPo.po.PrpLsdpoliceInfo;
import ins.sino.claimcar.trafficplatform.service.SdpoliceService;
import ins.sino.claimcar.trafficplatform.vo.FittingDataVo;
import ins.sino.claimcar.trafficplatform.vo.HospitalInfoDataVo;
import ins.sino.claimcar.trafficplatform.vo.InjuryDataVo;
import ins.sino.claimcar.trafficplatform.vo.InjuryIdentifyInfoDataVo;
import ins.sino.claimcar.trafficplatform.vo.LossFeeDataVo;
import ins.sino.claimcar.trafficplatform.vo.LossPartDataVo;
import ins.sino.claimcar.trafficplatform.vo.PersonDataVo;
import ins.sino.claimcar.trafficplatform.vo.ProtectDataVo;
import ins.sino.claimcar.trafficplatform.vo.PrpLsdpoliceInfoVo;
import ins.sino.claimcar.trafficplatform.vo.RequestDLossBasePartVo;
import ins.sino.claimcar.trafficplatform.vo.RequestDLossBodyVo;
import ins.sino.claimcar.trafficplatform.vo.RequestHeadVo;
import ins.sino.claimcar.trafficplatform.vo.ResponseHeadVo;
import ins.sino.claimcar.trafficplatform.vo.SdPoliceDLossVo;
import ins.sino.claimcar.trafficplatform.vo.SdResponseVo;
import ins.sino.claimcar.trafficplatform.vo.VehicleDataVo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"} , timeout = 1000*60*10)
@Path("sdpoliceService")
public class SdpoliceServiceImpl implements SdpoliceService{
	private static Logger logger = LoggerFactory.getLogger(SdpoliceServiceImpl.class);

	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	private PersTraceService persTraceService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private PropTaskService propTaskService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
    private CiCodeTranService ciCodeTranService;
	@Autowired
    ClaimInterfaceLogService interfaceLogService;
	@Autowired
	PolicyViewService policyViewService;
	
	@Autowired
	CiClaimPlatformLogService platformLogService;
	@Autowired
	private DatabaseDao databaseDao;
    @Autowired
    RegistQueryService registQueryService;
	
	@Override
	public void sendDlossRegister(PrpLCMainVo prpLCMainVo,SysUserVo userVo) throws Exception {
		//??????
		String url = "";
		String user = "";
		String passWord = "";
		String businessType = "";
		String businessName = "";
		if(prpLCMainVo.getComCode().startsWith("62")){//??????
			url = SpringProperties.getProperty("SDWARN_URL");
			user = SpringProperties.getProperty("SDWARN_USER");
			passWord =  SpringProperties.getProperty("SDWARN_PW");
			businessType = BusinessType.SDEW_vLoss.name();
			businessName = BusinessType.SDEW_vLoss.getName();
		}else if(prpLCMainVo.getComCode().startsWith("10")){//??????
			url = SpringProperties.getProperty("GDWARN_URL");
			user = SpringProperties.getProperty("GDWARN_USER");
			passWord = SpringProperties.getProperty("GDWARN_PW");
			businessType = BusinessType.GDEW_vLoss.name();
			businessName = BusinessType.GDEW_vLoss.getName();
		}else if(prpLCMainVo.getComCode().startsWith("50")){//??????
			url = SpringProperties.getProperty("HNWARN_URL");
			user = SpringProperties.getProperty("HNWARN_USER");
			passWord = SpringProperties.getProperty("HNWARN_PW");
			businessType = BusinessType.HNEW_vLoss.name();
			businessName = BusinessType.HNEW_vLoss.getName();
		}
	    RequestHeadVo headVo = new RequestHeadVo();
		headVo.setRequestType("C0304");//????????????
		headVo.setUser(user);//????????????
		headVo.setPassword(passWord);//????????????
		SdPoliceDLossVo sdPoliceDLossVo=setParamsForSdDLossPoliceVo(prpLCMainVo);
		sdPoliceDLossVo.setHeadVo(headVo);
		String registNo = prpLCMainVo.getRegistNo();
		String policyNo = prpLCMainVo.getPolicyNo();
		String requestXml=ClaimBaseCoder.objToXml(sdPoliceDLossVo);
		String returnXml="";
		 try{
			 returnXml=requestSdpolice(requestXml,url,200);
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally{
			 ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
			 SdResponseVo sdResponseVo=new SdResponseVo();
			 sdResponseVo=ClaimBaseCoder.xmlToObj(returnXml,SdResponseVo.class);
			 ResponseHeadVo resHeadVo=new ResponseHeadVo();
			 if(sdResponseVo!=null){
				 resHeadVo=sdResponseVo.getHeadVo(); 
			 }		 
             logVo.setRegistNo(registNo);
             logVo.setOperateNode("VDloss"); //????????????
             logVo.setPolicyNo(policyNo);   //?????????
             logVo.setCreateUser(userVo.getUserCode());
             logVo.setServiceType(resHeadVo.getRequestType());
             logVo.setComCode(userVo.getComCode());
             logVo.setRequestUrl(url);
             this.logUtil(requestXml,returnXml,logVo,resHeadVo.getResponseCode(),resHeadVo.getErrorMessage(),
            		 businessType,businessName);
		 }
		
	}

	
	
	
	/**
	 * ??????????????????
	 * @param requestXML
	 * @param urlStr
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	private String requestSdpolice(String requestXML,String urlStr,int seconds) throws Exception {
		long t1 = System.currentTimeMillis();
		String responseXml="";
		  StringBuffer buffer = new StringBuffer();    
	        try {    
	         
	            URL url = new URL(urlStr);
	            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
	            httpUrlConn.setDoOutput(true);    
	            httpUrlConn.setDoInput(true);    
	            httpUrlConn.setUseCaches(false);    
	            // ?????????????????????GET/POST???    
	            httpUrlConn.setRequestMethod("POST"); 
	            httpUrlConn.setRequestProperty("Content-Type", "text/xml;charset=GBK");
	            httpUrlConn.setConnectTimeout(seconds * 1000);
		        
	            httpUrlConn.connect();    
	    
	            String outputStr =requestXML;
	            			
	            OutputStream outputStream = httpUrlConn.getOutputStream();  		        
	            // ???????????????????????????    
	            if (null != outputStr) {    
	                // ???????????????????????????????????????    outputStream.write
	                outputStream.write(outputStr.getBytes("GBK"));    
	            }    
	    
	            // ???????????????????????????????????????    
	            InputStream inputStream = httpUrlConn.getInputStream();    
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");    
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
	    
	            String str = null;    
	            while ((str = bufferedReader.readLine()) != null) {
	            	
	                buffer.append(str);    
	            } 
	            if (buffer.length() < 1) {
					throw new Exception("??????????????????????????????????????????");
				}
	            bufferedReader.close();    
	            inputStreamReader.close();    
	            // ????????????  
	            outputStream.flush();
	            outputStream.close();
	            inputStream.close();    
	            inputStream = null;    
	            httpUrlConn.disconnect(); 
	            System.out.println(buffer);
	            responseXml=buffer.toString();
	            
	        } catch (ConnectException ce) {
	        	throw new Exception("?????????????????????????????????????????????????????????", ce);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	throw new Exception("?????????????????????????????????????????????", e);
	        	
	        } finally {
				logger.warn("??????({})????????????{}ms", urlStr, System.currentTimeMillis() - t1);
			}    
	        return responseXml;
	}
	/**
	 * ???????????????????????????
	 * @param registNo
	 * @param policyNoType
	 * @return
	 */
	private SdPoliceDLossVo setParamsForSdDLossPoliceVo(PrpLCMainVo cmainVo){
		SdPoliceDLossVo sdPoliceDLossVo=new SdPoliceDLossVo();
		String registNo = cmainVo.getRegistNo();
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);
		List<PrpLdlossPropMainVo> dlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);
        List<PrpLDlossCarMainVo> dlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
        List<PrpLDlossPersTraceMainVo> dlossPersTraceMainVoList = persTraceService.findPersTraceMainVo(registNo);
        // ????????????????????????
	    List<VehicleDataVo> vehicleDataVos = new ArrayList<VehicleDataVo>();
	    //??????????????????
	    List<PersonDataVo> personDataLists=new ArrayList<PersonDataVo>();
		RequestDLossBodyVo bodyVo=new RequestDLossBodyVo();
		RequestDLossBasePartVo basePartVo=new RequestDLossBasePartVo();
		basePartVo.setClaimSequenceNo(cmainVo.getClaimSequenceNo());//????????????
		basePartVo.setClaimNotificationNo(registNo);//?????????
		basePartVo.setConfirmSequenceNo(cmainVo.getValidNo());//???????????????
		// ???????????????????????????1--??????????????????
		String duty = checkDutyVo.getIndemnityDuty();
		Long dut = Long.parseLong(duty)+1L;
		basePartVo.setAccidentLiability(dut.toString());//??????????????????
		basePartVo.setOptionType(checkVo.getManageType());//??????????????????
		basePartVo.setIsSingleAccident(checkVo.getSingleAccidentFlag());//??????????????????
		Double sumDefLoss = 0D;
	    basePartVo.setUnderTotalDefLoss("0");//???????????????
	    if(dlossCarMainVoList!=null && dlossCarMainVoList.size()>0){
	    	for(PrpLDlossCarMainVo carMainVo:dlossCarMainVoList){
	    		if(LossParty.THIRD.equals(carMainVo.getDeflossCarType()) && 
						CodeConstants.CetainLossType.DEFLOSS_NULL.equals(carMainVo.getCetainLossType())){
					continue;
				}
	    		//?????????????????????????????????
	    		if(LossParty.THIRD.equals(carMainVo.getDeflossCarType()) && 
	    				"1".equals(checkVo.getSingleAccidentFlag())){
	    			continue;
	    		}
	    		Double iLossF = DataUtils.NullToZero(carMainVo.getSumVeriLossFee()).doubleValue();
				sumDefLoss += iLossF;// ???????????????????????????
				VehicleDataVo vehicleDataVo=new VehicleDataVo();
				if("1".equals(carMainVo.getDeflossCarType())){//?????????
					List<PrpLCItemCarVo> prpLCItemCarVos= cmainVo.getPrpCItemCars();
					PrpLCItemCarVo	prpLCItemCarVo=null;
					if(prpLCItemCarVos!=null && prpLCItemCarVos.size()>0){
						prpLCItemCarVo=prpLCItemCarVos.get(0);
					}
					// fix by LiYi ??????????????? ,?????????????????????????????????
					if(prpLCItemCarVo!=null && StringUtils.isNotBlank(prpLCItemCarVo.getOtherNature())
						&& "1".equals(prpLCItemCarVo.getOtherNature().substring(6,7))){
						vehicleDataVo.setLicensePlateNo("");
						vehicleDataVo.setLicensePlateType("");
					}else{//?????????
						vehicleDataVo.setLicensePlateNo(carMainVo.getLicenseNo());//????????????????????????
						String licenseType = carMainVo.getLossCarInfoVo().getLicenseType();
						if(StringUtils.isBlank(licenseType)){
							licenseType = "02";
						}
						if("82".equals(licenseType)){
							licenseType = "32";//?????????????????????????????????????????????????????????
						}
						if("12".equals(cmainVo.getRiskCode().substring(0, 2))){//??????
							if("25".equals(licenseType)){
								vehicleDataVo.setLicensePlateType("99");
							}
						}else{//??????
							if("99".equals(licenseType)){
								vehicleDataVo.setLicensePlateType("25");
							}
						}
						vehicleDataVo.setLicensePlateType(licenseType);//??????????????????????????????
					}
				
				}else{//?????????
					vehicleDataVo.setLicensePlateNo(carMainVo.getLicenseNo());//????????????????????????
					String licenseType = carMainVo.getLossCarInfoVo().getLicenseType();
					if(StringUtils.isBlank(licenseType)){
						licenseType = "02";
					}
					if("82".equals(licenseType)){
						licenseType = "32";//?????????????????????????????????????????????????????????
					}
					if("12".equals(cmainVo.getRiskCode().substring(0, 2))){//??????
						if("25".equals(licenseType)){
							vehicleDataVo.setLicensePlateType("99");
						}
					}else{//??????
						if("99".equals(licenseType)){
							vehicleDataVo.setLicensePlateType("25");
						}
					}
					vehicleDataVo.setLicensePlateType(licenseType);//??????????????????????????????
				}
				
				if(cmainVo.getComCode().startsWith("10")){
					if(carMainVo.getSerialNo() == 1){//?????????????????????????????????????????????????????????????????????????????????????????????
						List<PrpLCItemCarVo> prpLCItemCarVos = registQueryService.findCItemCarByOther(cmainVo.getRegistNo(),cmainVo.getRiskCode().substring(0, 2));
						if(prpLCItemCarVos != null && prpLCItemCarVos.size() > 0 ){
							PrpLCItemCarVo prpLCItemCarVo = prpLCItemCarVos.get(0);
							//????????????
							if(StringUtils.isNotBlank(prpLCItemCarVo.getOtherNature()) && "1".equals(prpLCItemCarVo.getOtherNature().substring(6,7))){
								vehicleDataVo.setLicensePlateNo("");
								vehicleDataVo.setLicensePlateType("");
							}else{//????????????
								vehicleDataVo.setLicensePlateNo(prpLCItemCarVo.getLicenseNo());
								vehicleDataVo.setLicensePlateType(prpLCItemCarVo.getLicenseKindCode());
								if("12".equals(cmainVo.getRiskCode().substring(0, 2))){//??????
									if("25".equals(prpLCItemCarVo.getLicenseKindCode())){
										vehicleDataVo.setLicensePlateType("99");
									}
								}else{//??????
									if("99".equals(prpLCItemCarVo.getLicenseKindCode())){
										vehicleDataVo.setLicensePlateType("25");
									}
								}
							}
							
						}
					}
				}
				// ????????????????????????????????????????????????????????????????????????????????????????????????
				if(StringUtils.isBlank(vehicleDataVo.getLicensePlateType()) || StringUtils.isBlank(vehicleDataVo.getLicensePlateNo())){
					vehicleDataVo.setLicensePlateNo("");
					vehicleDataVo.setLicensePlateType("");
				}
				vehicleDataVo.setVin(carMainVo.getLossCarInfoVo().getVinNo());//????????????VIN???
				vehicleDataVo.setEngineNo(carMainVo.getLossCarInfoVo().getEngineNo());//?????????????????????
				vehicleDataVo.setModel(carMainVo.getLossCarInfoVo().getModelCode());//????????????????????????
				vehicleDataVo.setDriverName(carMainVo.getLossCarInfoVo().getDriveName());//???????????????????????????
				SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo
						("IdentifyType",carMainVo.getLossCarInfoVo().getIdentifyType());
				vehicleDataVo.setCertiType(sysVo==null?"99":sysVo.getProperty2());// ???????????????????????????
				vehicleDataVo.setCertiCode(carMainVo.getLossCarInfoVo().getIdentifyNo());//???????????????????????????
				vehicleDataVo.setDriverLicenseNo(carMainVo.getLossCarInfoVo().getDrivingLicenseNo());//??????????????????????????? 
				vehicleDataVo.setUnderDefLoss(DataUtils.NullToZero(carMainVo.getSumVeriLossFee()).doubleValue()+"");//????????????
				vehicleDataVo.setVehicleProperty(carMainVo.getSerialNo()==1 ? "1" : "2");//????????????
				vehicleDataVo.setIsRobber("03".equals(carMainVo.getCetainLossType()) ? "1" : "0");//????????????
				vehicleDataVo.setFieldType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "2" : "3");//????????????
				vehicleDataVo.setEstimateName(carMainVo.getHandlerName());//??????????????????
				vehicleDataVo.setEstimateCode(carMainVo.getHandlerCode());// ??????????????????
				vehicleDataVo.setEstimateCertiCode(carMainVo.getHandlerIdNo());//???????????????????????????
				vehicleDataVo.setUnderWriteName(carMainVo.getUnderWriteName());//??????????????????
				vehicleDataVo.setUnderWriteCode(carMainVo.getUnderWriteCode());//??????????????????
				vehicleDataVo.setUnderWriteCertiCode(carMainVo.getUnderWiteIdNo());//???????????????????????????
				vehicleDataVo.setEstimateStartTime(DateUtils.dateToStr(carMainVo.getCreateTime(), DateUtils.YMDHM));//?????????????????????????????? ??????????????????
				Date endDate = carMainVo.getUnderWriteEndDate();
				if(endDate==null){
					endDate = new Date();
				}
				vehicleDataVo.setUnderEndTime(DateUtils.dateToStr(endDate, DateUtils.YMDHM));//?????????????????????????????? ??????????????????
				vehicleDataVo.setEstimateAddr(carMainVo.getDefSite());//????????????
				vehicleDataVo.setRemnant(DataUtils.NullToZero(carMainVo.getSumVeriRemnant()).doubleValue()+"");//????????????????????????
				Double totalManHour = 0.0;
				for(PrpLDlossCarRepairVo repairVo:carMainVo.getPrpLDlossCarRepairs()){
					totalManHour += DataUtils.NullToZero(repairVo.getVeriManHour()).doubleValue();
				}
				String strArray = totalManHour.toString();
				vehicleDataVo.setTotalManHour(strArray.substring(0,strArray.indexOf(".")));//???????????????
				String isChangeOrRepair = "0";
				if(( carMainVo.getPrpLDlossCarComps()!=null&&carMainVo.getPrpLDlossCarComps().size()>0 )||( carMainVo
						.getPrpLDlossCarRepairs()!=null&&carMainVo.getPrpLDlossCarRepairs().size()>0 )){
					isChangeOrRepair = "1";
				}
				vehicleDataVo.setIsChangeOrRepair(isChangeOrRepair);//??????????????????????????? ,0 ??? ,1 ???
				String factoryN = carMainVo.getRepairFactoryName();
				String factoryT = carMainVo.getRepairFactoryType();
				vehicleDataVo.setRepairFactoryName(StringUtils.isNotBlank(factoryN) ? factoryN : "??????");//??????????????????
				vehicleDataVo.setRepairFactoryCertiCode("");//??????????????????????????????
				vehicleDataVo.setRepairFactoryType(StringUtils.isBlank(factoryT) ? "003" : factoryT);//??????????????????
				vehicleDataVo.setIsTotalLoss(carMainVo.getIsTotalLoss());//????????????
				vehicleDataVo.setIsGlassBroken(StringUtils.isNotBlank(carMainVo.getIsGlassBroken())?carMainVo.getIsGlassBroken():"0");//????????????????????????
				vehicleDataVo.setIsNotFindThird(StringUtils.isNotBlank(carMainVo.getIsNotFindThird())?carMainVo.getIsNotFindThird():"0");//?????????????????????????????????
				
				//????????????????????????
			    List<LossPartDataVo> LossPartDataVos=new ArrayList<LossPartDataVo>();
				String lossPartList = carMainVo.getLossPart();
				if(StringUtils.isNotBlank(lossPartList)){
					String[] lossPartArray = lossPartList.split(",");
					for(int i = 0; i<lossPartArray.length; i++ ){
						String lossPart = codeTranService.findTransCodeDictVo("LossPart",lossPartArray[i]).getProperty3();
						if(StringUtils.isNotBlank(lossPart)){
							LossPartDataVo lossPartDataVo = new LossPartDataVo();
							lossPartDataVo.setLossPart(lossPart);
							LossPartDataVos.add(lossPartDataVo);
						}
					}
					if(LossPartDataVos == null || LossPartDataVos.size() == 0){
						LossPartDataVo lossPartDataVo = new LossPartDataVo();
						lossPartDataVo.setLossPart("01");
						LossPartDataVos.add(lossPartDataVo);
					}
					vehicleDataVo.setLossPartDataList(LossPartDataVos);
				}
	    	
				
				// ??????????????????
				List<FittingDataVo> lossCarFittingDataVos = new ArrayList<FittingDataVo>();
				for(PrpLDlossCarRepairVo repairVo:carMainVo.getPrpLDlossCarRepairs()){// ??????
					FittingDataVo fittingDataVo = new FittingDataVo();
					fittingDataVo.setChangeOrRepair("2");//?????????????????????
					String compName = repairVo.getCompName();
					if(StringUtils.isNotBlank(compName) && compName.length() > 50){
						compName = compName.substring(0,49);
					}
					fittingDataVo.setFittingName(compName);//??????/??????????????????
					fittingDataVo.setFittingNum(DataUtils.NullToZero(repairVo.getVeriMaterQuantity()).toString());//??????/??????????????????
					fittingDataVo.setMaterialFee(DataUtils.NullToZero(repairVo.getVeriManUnitPrice()).doubleValue()+"");// ??????/?????????????????????????????????
					fittingDataVo.setManHour(DataUtils.NullToZero(repairVo.getVeriManHour()).toString());//??????/??????????????????
					fittingDataVo.setManPowerFee(DataUtils.NullToZero(repairVo.getVeriManHourFee()).doubleValue()+"");//??????/?????????????????????
					// fittingDataVo.setIsSubjoin(isSubjoin);//?????????????????????
					lossCarFittingDataVos.add(fittingDataVo);
				}
				for(PrpLDlossCarCompVo compVo:carMainVo.getPrpLDlossCarComps()){// ??????
					FittingDataVo fittingDataVo = new FittingDataVo();
					fittingDataVo.setChangeOrRepair("1");
					String compName = compVo.getCompName();
					if(StringUtils.isNotBlank(compName) && compName.length() > 50){
						compName = compName.substring(0,49);
					}
					fittingDataVo.setFittingName(compName);
					fittingDataVo.setFittingNum(DataUtils.NullToZeroInt(compVo.getVeriQuantity()).toString());
					fittingDataVo.setMaterialFee(DataUtils.NullToZero(compVo.getVeriMaterFee()).doubleValue()+"");
					fittingDataVo.setManHour(DataUtils.NullToZero(compVo.getManHour()).toString());
					fittingDataVo.setManPowerFee(DataUtils.NullToZero(compVo.getVeriManHourFee()).doubleValue()+"");
					// fittingDataVo.setIsSubjoin(isSubjoin);//?????????????????????
					lossCarFittingDataVos.add(fittingDataVo);
				}
				vehicleDataVo.setFittingDataList(lossCarFittingDataVos);
				vehicleDataVos.add(vehicleDataVo);
			}
	    }
	    	
	    // ????????????????????????
		List<ProtectDataVo> protectDataVos = new ArrayList<ProtectDataVo>();
	    if(dlossPropMainVoList!=null&&dlossPropMainVoList.size()>0){
			for(PrpLdlossPropMainVo propMainVo:dlossPropMainVoList){
				sumDefLoss += DataUtils.NullToZero(propMainVo.getSumVeriLoss()).doubleValue();
				for(PrpLdlossPropFeeVo feeVo:propMainVo.getPrpLdlossPropFees()){
					ProtectDataVo lossProtectDataVo = new ProtectDataVo();
					lossProtectDataVo.setProtectName(feeVo.getLossItemName());
					// lossProtectDataVo.setLossDesc(lossDesc);//????????????
					lossProtectDataVo.setLossNum(DataUtils.NullToZero(feeVo.getLossQuantity()).toString());
					lossProtectDataVo.setUnderDefLoss(DataUtils.NullToZero(feeVo.getSumVeriLoss()).doubleValue()+"");
					// lossProtectDataVo.setOwner(owner);
					lossProtectDataVo.setProtectProperty("01".equals(propMainVo.getLossType()) ? "1" : "2");
					lossProtectDataVo.setFieldType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "2" : "3");
					lossProtectDataVo.setEstimateName(propMainVo.getHandlerName());
					lossProtectDataVo.setEstimateCode(propMainVo.getHandlerCode());
					lossProtectDataVo.setEstimateCertiCode(propMainVo.getHandlerIdCard());
					lossProtectDataVo.setUnderWriteName(propMainVo.getUnderWriteName());
					lossProtectDataVo.setUnderWriteCode(propMainVo.getUnderWriteCode());
					lossProtectDataVo.setUnderWriteCertiCode(propMainVo.getUnderWriteIdCard());
					lossProtectDataVo.setEstimateStartTime(DateUtils.dateToStr(propMainVo.getCreateTime(), DateUtils.YMDHM));
					Date endDate = propMainVo.getUnderWriteEndDate();
					if(endDate == null){
						endDate = new Date();
					}
					lossProtectDataVo.setUnderEndTime(DateUtils.dateToStr(endDate, DateUtils.YMDHM));
					// lossProtectDataVo.setEstimatePlace(estimatePlace);
					protectDataVos.add(lossProtectDataVo);
				}
			}
    	}
	    	
	    	
    	if(dlossPersTraceMainVoList!=null&&dlossPersTraceMainVoList.size()>0){
			for(PrpLDlossPersTraceMainVo persTraceMainVo:dlossPersTraceMainVoList){
				List<PrpLDlossPersTraceVo> persTraceVoList = persTraceMainVo.getPrpLDlossPersTraces();
				if( !"10".equals(persTraceMainVo.getCaseProcessType())&&persTraceVoList!=null&&persTraceVoList.size()>0){
					for(PrpLDlossPersTraceVo persTraceVo:persTraceMainVo.getPrpLDlossPersTraces()){
						if("1".equals(persTraceVo.getValidFlag())){//??????????????????????????????????????????
							
							sumDefLoss += DataUtils.NullToZero(persTraceVo.getSumVeriDefloss()).doubleValue();

							PersonDataVo personDataVo = new PersonDataVo();
							personDataVo.setPersonName(persTraceVo.getPrpLDlossPersInjured().getPersonName());
							SysCodeDictVo sys = codeTranService.findTransCodeDictVo
									("IdentifyType",persTraceVo.getPrpLDlossPersInjured().getCertiType());
							personDataVo.setCertiType(sys==null?"99":sys.getProperty3());
							personDataVo.setCertiCode(persTraceVo.getPrpLDlossPersInjured().getCertiCode());

							String lossItemType = persTraceVo.getPrpLDlossPersInjured().getLossItemType();
							personDataVo.setPersonProperty(ciCodeTranService.findTranCodeCode("LossItemType",lossItemType));
							personDataVo.setMedicalType(persTraceVo.getPrpLDlossPersInjured().getTreatSituation());//???????????? ?????????
							String woundCode = persTraceVo.getPrpLDlossPersInjured().getWoundCode();
							personDataVo.setInjuryType(StringUtils.isEmpty(woundCode) ? "01" : woundCode);
//							personDataVo.setInjuryType(persTraceVo.getPrpLDlossPersInjured().getWoundCode());
							// personDataVo.setInjuryLevel(injuryLevel);
							personDataVo.setUnderDefLoss(DataUtils.NullToZero(persTraceVo.getSumVeriDefloss()).doubleValue()+"");
							personDataVo.setEstimateName(persTraceMainVo.getOperatorName());
							personDataVo.setEstimateCode(persTraceMainVo.getOperatorCode());
							if(StringUtils.isBlank(persTraceMainVo.getOperatorCertiCode())){
								personDataVo.setEstimateCertiCode(persTraceMainVo.getPlfCertiCode());
							} else {
								personDataVo.setEstimateCertiCode(persTraceMainVo.getOperatorCertiCode());
							}
							personDataVo.setUnderWriteName(persTraceMainVo.getUnderwriteName());
							personDataVo.setUnderWriteCode(persTraceMainVo.getUnderwriteCode());
							personDataVo.setUnderWriteCertiCode(persTraceMainVo.getVerifyCertiCode());
							personDataVo.setEstimateStartTime(DateUtils.dateToStr(persTraceMainVo.getCreateTime(), DateUtils.YMDHM));
							personDataVo.setUnderEndTime(DateUtils.dateToStr(persTraceMainVo.getUndwrtFeeEndDate(), DateUtils.YMDHM));
							// personDataVo.setEstimateAddr(estimateAddr);
							// personDataVo.setUnderWriteCode(persTraceMainVo.getun);//?????????
							Date deathDate = persTraceVo.getPrpLDlossPersInjured().getDeathTime();
							if(deathDate != null){
								personDataVo.setDeathTime(DateUtils.dateToStr(deathDate, DateUtils.YMDHM));
//								personDataVo.setDeathTime(persTraceVo.getPrpLDlossPersInjured().getDeathTime());
							}

							// ??????????????????????????????
							List<LossFeeDataVo> feeDataVoList = new ArrayList<LossFeeDataVo>();
							for(PrpLDlossPersTraceFeeVo persTraceFeeVo:persTraceVo.getPrpLDlossPersTraceFees()){
								LossFeeDataVo lossFeeDataVo = new LossFeeDataVo();
								SysCodeDictVo sysVo = codeTranService.findCodeDictVo("FeeType",persTraceFeeVo.getFeeTypeCode());
								if(sysVo != null){
									lossFeeDataVo.setFeeType(sysVo.getProperty1());// ???????????????
								}
								lossFeeDataVo.setUnderDefLoss(DataUtils.NullToZero(persTraceFeeVo.getVeriDefloss()).doubleValue()+"");
								feeDataVoList.add(lossFeeDataVo);
							}
							personDataVo.setLossFeeDataList(feeDataVoList);;

							// ??????????????????
							List<InjuryDataVo> injuryDataVoList = new ArrayList<InjuryDataVo>();
							for(PrpLDlossPersExtVo persExtVo:persTraceVo.getPrpLDlossPersInjured()
									.getPrpLDlossPersExts()){
								InjuryDataVo injuryDataVo = new InjuryDataVo();
								injuryDataVo.setInjuryPart(persExtVo.getInjuredPart());
								String woundGrade = persExtVo.getWoundGrade();
								if(StringUtils.isNotBlank(woundGrade) && !"10".equals(woundGrade)){
									woundGrade = "0" + woundGrade;
								}else{
									woundGrade = "01";//???????????????
								}
								injuryDataVo.setInjuryLevelCode(woundGrade);
								injuryDataVoList.add(injuryDataVo);
							}
							personDataVo.setInjuryDataList(injuryDataVoList);;

							// ??????????????????
							List<HospitalInfoDataVo> hospitalInfoVoList = new ArrayList<HospitalInfoDataVo>();
							// ???????????????????????????????????????????????????????????????????????????
							for(PrpLDlossPersHospitalVo persHospitalVo:persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersHospitals()){
								HospitalInfoDataVo hospitalInfoDataVo = new HospitalInfoDataVo();
								String hospitalName = persHospitalVo.getHospitalName();
								if(StringUtils.isBlank(hospitalName)){
									hospitalName = "????????????????????????";
								}
								hospitalInfoDataVo.setHospitalName(hospitalName);
								// hospitalInfoDataVo.setHospitalFactoryCertiCode(persHospitalVo.getHospitalCode());
								hospitalInfoVoList.add(hospitalInfoDataVo);
							}
							// ???????????????????????????????????????????????????????????????????????????
							if(hospitalInfoVoList == null || hospitalInfoVoList.size() == 0){
								HospitalInfoDataVo hospitalInfoDataVo = new HospitalInfoDataVo();
								hospitalInfoDataVo.setHospitalName("????????????????????????");
								// hospitalInfoDataVo.setHospitalFactoryCertiCode(persHospitalVo.get);
								hospitalInfoVoList.add(hospitalInfoDataVo);
							}
							personDataVo.setHospitalInfoDataList(hospitalInfoVoList);;

						    // ????????????????????????,,,--????????????????????????????????????????????????????????????????????????
						    List<InjuryIdentifyInfoDataVo> identfyVoList = new ArrayList<InjuryIdentifyInfoDataVo>();
						    if(dlossPersTraceMainVoList != null && dlossPersTraceMainVoList.size() > 0){
								for(PrpLDlossPersTraceMainVo dlossPersTraceMainVo : dlossPersTraceMainVoList){
									List<PrpLDlossPersTraceVo> dlossPersTraceVoList = dlossPersTraceMainVo.getPrpLDlossPersTraces();
									if( !"10".equals(dlossPersTraceMainVo.getCaseProcessType()) && dlossPersTraceVoList != null&&dlossPersTraceVoList.size() > 0){
										for(PrpLDlossPersTraceVo losspersTraceVo : dlossPersTraceMainVo.getPrpLDlossPersTraces()){
											if("1".equals(losspersTraceVo.getValidFlag())){//??????????????????????????????????????????
												if("02".equals(losspersTraceVo.getPrpLDlossPersInjured().getWoundCode())){
													InjuryIdentifyInfoDataVo identifyInfoDataVo = new InjuryIdentifyInfoDataVo();
													String chkName = losspersTraceVo.getPrpLDlossPersInjured().getChkComName();
													if(StringUtils.isEmpty(chkName)){
														chkName = "??????????????????????????????";
													}
													identifyInfoDataVo.setInjuryIdentifyName(chkName);
													//identifyInfoDataVo.setInjuryIdentifyCertiCode(persTraceVo.getPrpLDlossPersInjured().getChkComCode());
													identfyVoList.add(identifyInfoDataVo);
												}
											}
										}
									}
							     }
						    }
						    personDataVo.setInjuryIdentifyInfoDataList(identfyVoList);
							personDataLists.add(personDataVo);
						}
						
					}
				}
			}
		}
	     
	    String sumTot = new DecimalFormat("######0.00").format(sumDefLoss);
	    basePartVo.setUnderTotalDefLoss(sumTot);//???????????????
	    bodyVo.setBasePartVo(basePartVo);//????????????
	    bodyVo.setPersonDataList(personDataLists);//??????????????????
	    bodyVo.setProtectDataList(protectDataVos);//??????????????????
	    bodyVo.setVehicleDataList(vehicleDataVos);//????????????????????????
	    
	    sdPoliceDLossVo.setBodyVo(bodyVo);
		return sdPoliceDLossVo;
	    
	}
	
	
	

	/**
	 * ????????????????????????
	 * @param reqXml
	 * @param resXml
	 * @param logVo
	 * @param flag
	 * @param errorMsg
	 * @param interfaceType--??????????????????
	 */
	private  void logUtil(String reqXml,String resXml, ClaimInterfaceLogVo logVo,String flag,String errorMsg,
			String businessType,String businessName) {
		logger.info("===============??????????????????===========");
        logger.info(reqXml);
        logger.info(resXml);
        logVo.setBusinessType(businessType);
        logVo.setBusinessName(businessName);
      
        logVo.setRequestXml(reqXml);
        logVo.setResponseXml(resXml);
        logVo.setCreateTime(new Date());
        logVo.setRequestTime(new Date());
        if("1".equals(flag)){
            logVo.setStatus("1");
            logVo.setErrorCode("true");
            logVo.setErrorMessage(errorMsg);;
        }else{
            logVo.setStatus("0");
            logVo.setErrorCode("false");
            logVo.setErrorMessage(errorMsg);
            
        }
        interfaceLogService.save(logVo);
	}

	@Override
	public void saveprplsdpoliceInfo(List<PrpLsdpoliceInfoVo> infoVoList)throws Exception {
		List<PrpLsdpoliceInfo> infoPoList=new ArrayList<PrpLsdpoliceInfo>();
		if(infoVoList!=null && infoVoList.size()>0){
			for(PrpLsdpoliceInfoVo infoVo:infoVoList){
				if(StringUtils.isNotBlank(infoVo.getClaimsequenceNo())){
					PrpLsdpoliceInfo infoPo=new PrpLsdpoliceInfo();
					Beans.copy().from(infoVo).to(infoPo);
					infoPoList.add(infoPo);
				}
			}
		}
		databaseDao.saveAll(PrpLsdpoliceInfo.class,infoPoList);
	}




	@Override
	public List<PrpLsdpoliceInfoVo> findPrpLsdpoliceInfoVoByRegistNo(String registNo) {
		QueryRule rule=QueryRule.getInstance();
		rule.addEqual("registNo",registNo);
		rule.addDescOrder("createTime");
		List<PrpLsdpoliceInfo> infoPoList=databaseDao.findAll(PrpLsdpoliceInfo.class,rule);
		List<PrpLsdpoliceInfoVo> infoVoList=new ArrayList<PrpLsdpoliceInfoVo>();
		if(infoPoList!=null && infoPoList.size()>0){
			infoVoList=Beans.copyDepth().from(infoPoList).toList(PrpLsdpoliceInfoVo.class);
		}
		return infoVoList;
	}

}
