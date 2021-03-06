package ins.interf;

import ins.framework.lang.Springs;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.service.CaseLeapGXService;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carinterface.vo.GXCaseBean;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.service.EndCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.text.ParseException;
import java.util.*;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebService(targetNamespace="http://interf/claimToGXXBService/",serviceName="claimToGXXBService")
public class ClaimToGXXBService extends SpringBeanAutowiringSupport{

	private Logger logger = LoggerFactory.getLogger(ClaimToGXXBService.class);
	
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	CaseLeapGXService caseLeapGXService;
	@Autowired
	EndCaseService endCaseService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	ClaimInterfaceLogService claimInterfaceLogService;
	@Autowired
	EndCasePubService endCasePubService;
	@Autowired
	CheckHandleService checkHandleService;
	
	public void doJob() {
		
		logger.info(this.getClass().getSimpleName()+"-ClaimToGXXB is run at:"+new Date());
		
		// ??????24?????????????????????????????????
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
		Date date = new Date();
		init();
		List<ClaimInterfaceLogVo> logListByRegist = 
				claimInterfaceLogService.findLogByRequestTime(calendar.getTime(),BusinessType.GXIS_regist.name(),"0","4");
		List<ClaimInterfaceLogVo> logListByEndCase = 
				claimInterfaceLogService.findLogByRequestTime(calendar.getTime(),BusinessType.GXIS_endCase.name(),"0","4");
		String url = SpringProperties.getProperty("GXIS_URL");
		String user = SpringProperties.getProperty("GXIS_USER");
		String pwd = SpringProperties.getProperty("GXIS_PWD");
		
		//???????????????????????????
		if(logListByRegist!=null && logListByRegist.size()>0){
			for(ClaimInterfaceLogVo logVo:logListByRegist){
				try{
					List<PrpLRegistVo> registVoList = new ArrayList<PrpLRegistVo>();
					PrpLRegistVo registVo = registQueryService.findByRegistNo(logVo.getRegistNo());
					registVoList.add(registVo);
					//???????????????????????????
					String flag = "";
					if(logVo.getRequestXml().contains("?????????")){
						flag = "1";
					}else if(logVo.getRequestXml().contains("?????????")){
						flag = "2";
					}
					List<GXCaseBean> caseBeanList = getBeanByRegist(registVoList,flag);
					caseLeapGXService.importCaseData(caseBeanList, url, user, pwd);
					claimInterfaceLogService.changeInterfaceLog(logVo.getId());
				}catch(Exception e){
					e.printStackTrace();
					logger.info("GXXB ????????????????????????????????????????????????"+logVo.getRegistNo(), e);
				}
				
			}
		}
		
		//???????????????????????????
		if(logListByEndCase!=null && logListByEndCase.size()>0){
			for(ClaimInterfaceLogVo logVo:logListByEndCase){
				try{
					List<PrpLEndCaseVo> endCaseVoList = new ArrayList<PrpLEndCaseVo>();
					PrpLEndCaseVo endCaseVo = endCasePubService.findEndCaseByPK(null, logVo.getCompensateNo());
					if(endCaseVo!=null && endCaseVo.getId()!=null){
						endCaseVoList.add(endCaseVo);
						List<GXCaseBean> caseBeanList = getBeanByEndCase(endCaseVoList);
						caseLeapGXService.importCaseData(caseBeanList, url, user, pwd);
						claimInterfaceLogService.changeInterfaceLog(logVo.getId());
					}
				}catch(Exception e){
					e.printStackTrace();
					logger.info("GXXB ????????????????????????????????????????????????"+logVo.getCompensateNo(), e);
				}
			}
		}
		
		//?????????????????????
		try{
			 List<PrpLRegistVo> registVoList = registQueryService.findRegistByQueryReportTime(calendar.getTime(),date,"11");
			 List<PrpLRegistVo> sendToGXRealData = new ArrayList<PrpLRegistVo>();
			 for(PrpLRegistVo registVo : registVoList){
				 //?????????  ????????????????????????????????????
				 if(!CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag()) && !CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
					 sendToGXRealData.add(registVo);
				 }
			 }
			 List<GXCaseBean> caseBeanList = getBeanByRegist(sendToGXRealData,"");
			 
			 if(caseBeanList!=null && caseBeanList.size()>0){
				 caseLeapGXService.importCaseData(caseBeanList, url, user, pwd);
			 }
		}catch(Exception e){
			e.printStackTrace();
			logger.info("GXXB ??????????????????????????????", e);
		}
		
		//?????????????????????
		try{
			List<PrpLEndCaseVo> endCaseVoList = endCaseService.findEndCaseByEndCaseDate(calendar.getTime(),date);
			List<PrpLEndCaseVo> endCaseVos = new ArrayList<PrpLEndCaseVo>();
			if(endCaseVoList!=null && !endCaseVoList.isEmpty()){
				String[] registNos = new String[endCaseVoList.size()];
				for(int i = 0 ;i<endCaseVoList.size();i++){
					registNos[i]=endCaseVoList.get(i).getRegistNo();
				}
				List<PrpLRegistVo> registVos = registQueryService.findPrpLRegistVosByRegistNos(registNos);
				if(registVos!=null && !registVos.isEmpty()){
					for(PrpLRegistVo registVo : registVos){
						if(!CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag()) && !CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
							for(PrpLEndCaseVo endCaseVo : endCaseVoList){
								if(endCaseVo.getRegistNo().equals(registVo.getRegistNo())){
									endCaseVos.add(endCaseVo);
								}
							}
						}
					}
				}
			}
			List<GXCaseBean> caseBeanList = getBeanByEndCase(endCaseVos);
			
			if(caseBeanList!=null && caseBeanList.size()>0){
				 caseLeapGXService.importCaseData(caseBeanList, url, user, pwd);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("GXXB ??????????????????????????????", e);
		}
		
	}
	
	/**
	 * ??????flag??????1??????????????????????????????2?????????????????????????????????????????????
	 * @param registVoList
	 * @param flag
	 * @return
	 */
	public List<GXCaseBean> getBeanByRegist(List<PrpLRegistVo> registVoList,String flag){
		List<GXCaseBean> caseBeanList = new ArrayList<GXCaseBean>();
		//??????????????????
		 String comCodeList = SpringProperties.getProperty("XB_COMCODE");
		if(registVoList!=null && registVoList.size()>0){
			for(PrpLRegistVo registVo:registVoList){
				//??????????????????????????????????????????
				if(comCodeList!=null && registVo.getComCode()!=null && 
						comCodeList.contains(registVo.getComCode().substring(0, 4))){
					//??????????????????
					List<PrpLCMainVo> cMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registVo.getRegistNo());
					if(cMainVoList!=null && cMainVoList.size()>0){
						for(PrpLCMainVo cMainVo:cMainVoList){
							//??????flag??????1??????????????????????????????2?????????????????????????????????????????????
							if("1".equals(flag)&&!"1101".equals(cMainVo.getRiskCode())){
								continue;
							}else if("2".equals(flag)&&"1101".equals(cMainVo.getRiskCode())){
								continue;
							}
							GXCaseBean caseBean = new GXCaseBean();
							caseBean.setPhase("2");
							if("1101".equals(cMainVo.getRiskCode())){
								caseBean.setInssort("?????????");
							}else{
								caseBean.setInssort("?????????");
							}

							caseBean.setName(registVo.getReportorName());
							if(registVo.getPrpLRegistPersonLosses()!=null && registVo.getPrpLRegistPersonLosses().size()>0){
								caseBean.setIspeoplehurt("???");
							}else{
								caseBean.setIspeoplehurt("???");
							}
							
							caseBean.setAccidenttime(DateUtils.dateToStr(registVo.getDamageTime(), DateUtils.YToDay));
							caseBean.setStarttime(DateUtils.dateToStr(registVo.getReportTime(), DateUtils.YToDay));
							caseBean.setCaseno(cMainVo.getRegistNo());
							caseBean.setMobile(registVo.getLinkerMobile());
							caseBean.setSex("???");
							caseBean.setInstype("1");
							caseBean.setPolicyno(cMainVo.getPolicyNo());
							caseBean.setAreaid(getAreaid(cMainVo.getComCode()));
							if(StringUtils.isNotEmpty(registVo.getReportorIdfNo())){
								caseBean.setIdcard(registVo.getReportorIdfNo());
							}
							if(StringUtils.isNotEmpty(registVo.getPrpLRegistExt().getInsuredName())){
								caseBean.setInsuredname(registVo.getPrpLRegistExt().getInsuredName());
							}
							
							caseBean.setInsuredidcard("???");
							caseBean.setInsuredmobile("???");
							caseBean.setCarbrandno(registVo.getPrpLRegistExt().getLicenseNo());
							caseBean.setCarframeno(registVo.getPrpLRegistExt().getFrameNo());
							caseBean.setDutybrandno(getDutyBrandNo(registVo.getPrpLRegistCarLosses()));
							caseBean.setDutyframeno(getDutyFrameNo(registVo.getPrpLRegistCarLosses()));
							caseBean.setAddress(registVo.getDamageAddress());
							
							caseBeanList.add(caseBean);
						}
					}
				}
			}
		}
		
		return caseBeanList;
	}
	
	public List<GXCaseBean> getBeanByEndCase(List<PrpLEndCaseVo> endCaseVoList){
		List<GXCaseBean> caseBeanList = new ArrayList<GXCaseBean>();
		String comCodeList = SpringProperties.getProperty("XB_COMCODE");
		if(endCaseVoList!=null && endCaseVoList.size()>0){
			List<PrpLCMainVo> cMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(endCaseVoList.get(0).getRegistNo());
			Map<String, String> comcodeMap = new HashMap<String, String >();
			if (cMainVoList != null && cMainVoList.size() > 0) {
				for (PrpLCMainVo cmainVo : cMainVoList) {
					comcodeMap.put(cmainVo.getRiskCode(), cmainVo.getComCode());
				}
			}

			for(PrpLEndCaseVo endCaseVo:endCaseVoList){
				PrpLRegistVo registVo = registQueryService.findByRegistNo(endCaseVo.getRegistNo());
				if(comCodeList!=null && registVo.getComCode()!=null && 
						comCodeList.contains(registVo.getComCode().substring(0, 4))){
					//??????????????????
					PrpLCompensateVo compensateVo = compensateTaskService.findCompByPK(endCaseVo.getCompensateNo());
					List<PrpLCheckCarVo> checkCarVoList = checkHandleService.findPrpLcheckCarVoByRegistNo(endCaseVo.getRegistNo());
					GXCaseBean caseBean = new GXCaseBean();
					caseBean.setPhase("3");
					if("1101".equals(endCaseVo.getRiskCode())){
						caseBean.setInssort("?????????");
					}else{
						caseBean.setInssort("?????????");
					}
					
					caseBean.setName(registVo.getReportorName());
					if(registVo.getPrpLRegistPersonLosses()!=null && registVo.getPrpLRegistPersonLosses().size()>0){
						caseBean.setIspeoplehurt("???");
					}else{
						caseBean.setIspeoplehurt("???");
					}
					
					caseBean.setAccidenttime(DateUtils.dateToStr(registVo.getDamageTime(), DateUtils.YToDay));
					caseBean.setStarttime(DateUtils.dateToStr(registVo.getReportTime(), DateUtils.YToDay));
					caseBean.setCaseno(endCaseVo.getRegistNo());
					caseBean.setMobile(registVo.getLinkerMobile());
					caseBean.setSex("???");
					caseBean.setInstype("1");
					caseBean.setPolicyno(endCaseVo.getPolicyNo());
					caseBean.setAreaid(getAreaid(comcodeMap.get(endCaseVo.getRiskCode())));

					if(StringUtils.isNotEmpty(registVo.getReportorIdfNo())){
						caseBean.setIdcard(registVo.getReportorIdfNo());
					}
					if(StringUtils.isNotEmpty(registVo.getPrpLRegistExt().getInsuredName())){
						caseBean.setInsuredname(registVo.getPrpLRegistExt().getInsuredName());
					}
					
					caseBean.setInsuredidcard("???");
					caseBean.setInsuredmobile("???");
					caseBean.setCarbrandno(registVo.getPrpLRegistExt().getLicenseNo());
					caseBean.setCarframeno(registVo.getPrpLRegistExt().getFrameNo());
					caseBean.setDutybrandno(getDutyBrandNo(registVo.getPrpLRegistCarLosses()));
					caseBean.setDutyframeno(getDutyFrameNo(registVo.getPrpLRegistCarLosses()));
					caseBean.setAddress(registVo.getDamageAddress());
					caseBean.setClosetime(DateUtils.dateToStr(endCaseVo.getEndCaseDate(), DateUtils.YToDay));
					if(compensateVo!=null&&compensateVo.getSumAmt()!=null){
						caseBean.setMoneys(Double.valueOf(compensateVo.getSumAmt().toString()));//????????????
					}else{
						caseBean.setMoneys(Double.valueOf("0"));
					}
					caseBean.setEndorsementno(endCaseVo.getEndCaseNo());
					caseBean.setDutymobile(getDutymobile(checkCarVoList));
					
					caseBeanList.add(caseBean);
				}
			}
		}
		
		return caseBeanList;
	}
	
	/**
	 * ????????????????????????
	 * @return
	 */
	public String getDutymobile(List<PrpLCheckCarVo> checkCarVoList){
		String dutymobile = "";
		for(PrpLCheckCarVo checkCarVo:checkCarVoList){
			if(checkCarVo.getSerialNo()!=1 && StringUtils.isNotEmpty(checkCarVo.getPrpLCheckDriver().getLinkPhoneNumber())){
				dutymobile = dutymobile+checkCarVo.getPrpLCheckDriver().getLinkPhoneNumber()+",";
			}
		}
		if("".equals(dutymobile)){
			dutymobile = "???";
		}else{
			dutymobile = dutymobile.substring(0, dutymobile.length()-1);
		}
		return dutymobile;
	}
	
	/**
	 * ???????????????????????????????????????????????????????????????
	 * @param carLossVoList
	 * @return
	 */
	public String getDutyBrandNo(List<PrpLRegistCarLossVo> carLossVoList){
		String dutyBrandNo = "";
		if(carLossVoList!=null && carLossVoList.size()>0){
			for(PrpLRegistCarLossVo carLossVo:carLossVoList){
				if("3".equals(carLossVo.getLossparty()) && StringUtils.isNotEmpty(carLossVo.getLicenseNo())){
					dutyBrandNo = dutyBrandNo+carLossVo.getLicenseNo()+",";
				}
			}
		}
		if("".equals(dutyBrandNo)){
			dutyBrandNo = "???";
		}else{
			dutyBrandNo = dutyBrandNo.substring(0, dutyBrandNo.length()-1);
		}
		return dutyBrandNo;
	}
	
	
	public String getDutyFrameNo(List<PrpLRegistCarLossVo> carLossVoList){
		String dutyFrameNo = "";
		if(carLossVoList!=null && carLossVoList.size()>0){
			for(PrpLRegistCarLossVo carLossVo:carLossVoList){
				if("3".equals(carLossVo.getLossparty()) && StringUtils.isNotEmpty(carLossVo.getFrameNo())){
					dutyFrameNo = dutyFrameNo+carLossVo.getFrameNo()+",";
				}
			}
		}
		if("".equals(dutyFrameNo)){
			dutyFrameNo = "???";
		}else{
			dutyFrameNo = dutyFrameNo.substring(0, dutyFrameNo.length()-1);
		}
		return dutyFrameNo;
	}
	
	
	public String getAreaid(String comCode){
		String areaId = CodeTranUtil.transCode("AreaId", comCode.substring(0, 4));
		
		if(areaId==null || !areaId.contains("???")){
			areaId = "???";
		}
		
		return areaId;
	}
	
	public static void main(String[] args) throws ParseException{
		//doJob();
	}
    private void init() {
        
        if(registQueryService==null){
            registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
        }
        if(policyViewService==null){
            policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
        }
        if(caseLeapGXService==null){
            caseLeapGXService = (CaseLeapGXService)Springs.getBean(CaseLeapGXService.class);
        }
        
        if(endCaseService==null){
            endCaseService = (EndCaseService)Springs.getBean(EndCaseService.class);
        }
        if(compensateTaskService==null){
            compensateTaskService = (CompensateTaskService)Springs.getBean(CompensateTaskService.class);
        }
        if(claimInterfaceLogService==null){
            claimInterfaceLogService = (ClaimInterfaceLogService)Springs.getBean(ClaimInterfaceLogService.class);
        }
        if(endCasePubService==null){
            endCasePubService = (EndCasePubService)Springs.getBean(EndCasePubService.class);
        }
        if(checkHandleService==null){
            checkHandleService = (CheckHandleService)Springs.getBean(CheckHandleService.class);
        }
    }
}
