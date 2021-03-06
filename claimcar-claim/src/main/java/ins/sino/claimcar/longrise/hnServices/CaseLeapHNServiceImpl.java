package ins.sino.claimcar.longrise.hnServices;

import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.Base64EncodedUtil;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.service.CaseLeapHNService;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.commom.vo.StringUtils;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("caseLeapHNService")
public class CaseLeapHNServiceImpl implements CaseLeapHNService{
	
	private Logger logger = LoggerFactory.getLogger(CaseLeapHNServiceImpl.class);
	
	@Autowired
	RegistService registService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	ClaimInterfaceLogService logService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	CertifyPubService certifyPubService;
	
	public  CaseBean importCaseData(CaseBean caseBean,String comCode) throws ParseException {
		if(caseBean==null){
			return null;
		}
		List<CaseBean> caseBeans = new ArrayList<CaseBean>();
		caseBeans.add(caseBean);
		return importCaseData(caseBeans,comCode).get(0);
	}

	public  CaseBean importCaseData(CaseBean caseBean,String url,String user,String pwd)  {
		if(caseBean==null){
			return null;
		}
		List<CaseBean> caseBeans = new ArrayList<CaseBean>();
		caseBeans.add(caseBean);
		return importCaseData(caseBeans,url,user,pwd).get(0);
	}

	public  List<CaseBean> importCaseData(List<CaseBean> caseBeans,String comCode) throws ParseException {
		if(caseBeans==null||caseBeans.isEmpty()){
			return caseBeans;
		}
		if(comCode==null||comCode.length()<2){
			for(CaseBean caseBean:caseBeans){
				caseBean.setIstatus("3");
				caseBean.setResultdesc("???????????????");
			}
			return caseBeans;
		}
		String comCodeSub = comCode.substring(0,2);
		String url = "";
		String user = "";
		String pwd = "";
		return importCaseData(caseBeans,url,user,pwd);
	}

	protected  List<CaseBean> importCaseData(List<CaseBean> caseBeans,String url,String user,String pwd) {
		if(caseBeans==null||caseBeans.isEmpty()){
			for(CaseBean caseBean:caseBeans){
				caseBean.setIstatus("3");
				caseBean.setResultdesc("?????????????????????");
			}
			return caseBeans;
		}
		if(url==null||url.trim().length()==0){
			for(CaseBean caseBean:caseBeans){
				caseBean.setIstatus("3");
				caseBean.setResultdesc("?????????????????????????????????");
			}
			return caseBeans;
		}
		try{
			IleapProxy ileapProxy = new IleapProxy(url);
			JSONArray jArray = JSONArray.fromObject(caseBeans);

			user = Base64EncodedUtil.encode(StringUtils.rightTrim(user).getBytes());
			pwd = Base64EncodedUtil.encode(StringUtils.rightTrim(pwd).getBytes());
			//System.out.println(jArray.toString());
			byte[] reBytes = ileapProxy.importCaseinfo(jArray.toString().getBytes("UTF-8"),user,pwd);
			if(reBytes==null){
				for(CaseBean caseBean:caseBeans){
					caseBean.setIstatus("");
					caseBean.setResultdesc("?????????????????????");
				}
				return caseBeans;
			}
			jArray = JSONArray.fromObject(new String(reBytes,"UTF-8"));
			int index = 0;
			for(CaseBean caseBean:caseBeans){
				JSONObject jobj = jArray.getJSONObject(index);
				caseBean.setIstatus(String.valueOf(jobj.get("istatus")));
				caseBean.setResultdesc(String.valueOf(jobj.get("resultdesc")));
			}
		}
		catch(RemoteException e){
			for(CaseBean caseBean:caseBeans){
				caseBean.setIstatus("3");
				caseBean.setResultdesc(e.getMessage());
			}
			e.printStackTrace();
		}
		catch(UnsupportedEncodingException e){
			for(CaseBean caseBean:caseBeans){
				caseBean.setIstatus("3");
				caseBean.setResultdesc(e.getMessage());
			}
			e.printStackTrace();
		}
		catch(Exception e){
			for(CaseBean caseBean:caseBeans){
				caseBean.setIstatus("3");
				caseBean.setResultdesc(e.getMessage());
			}
			e.printStackTrace();
		}
		return caseBeans;
	}
	
	
//public  void main(String[] args) throws Exception {
//	CaseBean caseBean = new CaseBean();
//		caseBean.setInstype("1");// ??????
//	caseBean.setPhase("3");
//	caseBean.setCaseno("523044777");
//	caseBean.setMobile("132456465");
//	caseBean.setName("ste");
//	caseBean.setDotime("2016-09-09");
//	caseBean.setDoaddress("dress");
//	caseBean.setInssort("2313");
//	String url = "http://59.173.241.186:8042/HNIS/services/com.longrise.services.leap";
//	String user = "410000003065";
//	String pwd = "410000003065";
//	caseBean = importCaseData(caseBean,url,user,pwd);
//		System.out.println("====caseBean=="+caseBean);
//		System.out.println("====caseBean.getCaseno()=="+caseBean.getCaseno());
//		System.out.println("====caseBean.getIstatus()=="+caseBean.getIstatus());
//		System.out.println("====caseBean.getImessage()=="+caseBean.getImessage());
//	}
	
/**
 * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	??????????????????????????????????????????????????????????????????????????????????????????
 * @param prpLRegistVo
 * @param userVo
 * @throws ParseException 
 */
    @Override
	public  ClaimInterfaceLogVo claimToHN(PrpLClaimVo prpLClaimVo,SysUserVo userVo) throws ParseException{
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLClaimVo.getRegistNo());
		//????????? ????????????????????????????????????
		if(CodeConstants.GBFlag.MINORCOMMON.equals(prpLRegistVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(prpLRegistVo.getIsGBFlag())){
			return null;
		}
    	PrpLCMainVo prpLCMain = policyViewService.getPolicyInfo(prpLRegistVo.getRegistNo(), 
				prpLRegistVo.getPolicyNo());
    	if(prpLCMain.getComCode().startsWith("50")){
    		Date date = new Date();
    		CaseBean caseBean = new CaseBean();
    		String kindCode = getKindCode(prpLClaimVo.getRegistNo(),prpLClaimVo.getRiskCode());
    		caseBean.setInstype("1");// ??????
    		caseBean.setInssort(kindCode);
    		caseBean.setPhase("2");
    		caseBean.setPolicyno(prpLClaimVo.getPolicyNo());
    		if(prpLClaimVo.getClaimNo() != null && !"".equals(prpLClaimVo.getClaimNo())){
				caseBean.setCaseno(prpLClaimVo.getClaimNo());
			}
    		caseBean.setName(userVo.getUserCode());
    		//TODO ???????????????????????????ywuser.PrpDcompany???
    		caseBean.setAreaid("?????????");
    		caseBean.setMobile(prpLRegistVo.getReportorPhone());
    		caseBean.setClaimsnum(prpLCMain.getClaimTimes().toString());//????????????
    		caseBean.setEffectivedate(DateUtils.dateToStr(prpLCMain.getStartDate(), DateUtils.YToDay));//??????????????????
    		if(prpLRegistVo.getPrpLRegistExt() != null && prpLRegistVo.getPrpLRegistExt().getInsuredName()!=null){
    			caseBean.setInsuredname(prpLRegistVo.getPrpLRegistExt().getInsuredName());
    		}
    		//???????????????
    		if("1".equals(prpLRegistVo.getPrpLRegistExt().getIsPersonLoss())){
    			caseBean.setIspeoplehurt("???");
    		}else{
    			caseBean.setIspeoplehurt("???");
    		}
    		if(prpLRegistVo.getInsuredPhone() != null){
    			caseBean.setInsuredmobile(prpLRegistVo.getInsuredPhone());
    		}
    		caseBean.setXzname(codeTranService.findCodeName("CarRiskCode", prpLClaimVo.getRiskCode()));
    		//?????????
    		if(prpLRegistVo.getPrpLRegistExt().getLicenseNo()!=null && 
    				!"".equals(prpLRegistVo.getPrpLRegistExt().getLicenseNo())){
    			caseBean.setCarbrandno(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
    		}
    		//?????????
    		if(prpLRegistVo.getPrpLRegistExt().getFrameNo()!=null && 
    				!"".equals(prpLRegistVo.getPrpLRegistExt().getFrameNo())){
    			caseBean.setCarframeno(prpLRegistVo.getPrpLRegistExt().getFrameNo());
    		}
    		//???????????????????????????
    		String dutybrandno = "";
    		String dutyframeno = "";
    		for(PrpLRegistCarLossVo registCarLossVo:prpLRegistVo.getPrpLRegistCarLosses()){
    			if("3".equals(registCarLossVo.getLossparty())){
    				if(registCarLossVo.getLicenseNo()!=null&&!registCarLossVo.getLicenseNo().isEmpty()){
    					dutybrandno = dutybrandno+registCarLossVo.getLicenseNo()+",";
    				}
    				if(registCarLossVo.getFrameNo()!=null&&!registCarLossVo.getFrameNo().isEmpty()){
    					dutyframeno = dutyframeno+registCarLossVo.getFrameNo()+",";
    				}
    				break;
    			}
    		}
    		if(!"".equals(dutybrandno)){
    			dutybrandno = dutybrandno.substring(0, dutybrandno.length()-1);
    			caseBean.setDutybrandno(dutybrandno);
    		}
    		if(!"".equals(dutyframeno)){
    			dutyframeno = dutyframeno.substring(0, dutyframeno.length()-1);
    			caseBean.setDutyframeno(dutyframeno);
    		}
    		//????????????
    		 if(prpLRegistVo.getDamageTime() != null){
    			 caseBean.setStarttime(DateUtils.dateToStr(prpLRegistVo.getDamageTime(), DateUtils.YToDay));
    		 }
    		 //????????????
    		 if(prpLRegistVo.getDamageAddress() != null){
    			 caseBean.setAddress(prpLRegistVo.getDamageAddress());
    		 }
    		 //?????????????????????
//    		 for(PrpLCInsuredVo prpLCInsuredVo:prpLCMain.getPrpCInsureds()){
//    			 if("1".equals(prpLCInsuredVo.getInsuredFlag())){
//    				 caseBean.setInsuredidcard(prpLCInsuredVo.getIdentifyNumber());
//    				 break;
//    			 }
//    		 }
    		 String url = SpringProperties.getProperty("HNIS_RUL");
    		 String user = SpringProperties.getProperty("HNIS_USER");
    		 String pwd = SpringProperties.getProperty("HNIS_PWD");
    		 XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
    		 String requestXml = stream.toXML(caseBean);
    		 caseBean = importCaseData(caseBean,url,user,pwd);
    		 String responseXml = stream.toXML(caseBean);
    		 logger.info("?????????????????????????????????--->"+responseXml);
    		 ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
    		 logVo.setRegistNo(prpLClaimVo.getRegistNo());
    		 logVo.setBusinessType(BusinessType.HNIS_claim.name());
    		 logVo.setBusinessName(BusinessType.HNIS_claim.getName());
    		 logVo.setOperateNode(FlowNode.Claim.getName());
    		 logVo.setComCode(prpLCMain.getComCode());
    		 logVo.setClaimNo(prpLClaimVo.getClaimNo());
    		 if("5".equals(caseBean.getIstatus())){
    			 logVo.setStatus("1");
    		 }else{
    			 logVo.setStatus("0");
    		 }
    		 if(caseBean.getResultdesc().isEmpty()||"null".equals(caseBean.getResultdesc())){
    			 String msg = this.getResultdesc(caseBean.getIstatus());
    			 logVo.setErrorMessage(msg);
    		 }else{

    			 logVo.setErrorMessage(caseBean.getResultdesc());
    		 }
    		 logVo.setRequestTime(date);
    		 logVo.setRequestUrl(url);
    		 logVo.setErrorCode(caseBean.getIstatus());
    		 logVo.setCreateTime(date);
    		 logVo.setCreateUser(userVo.getUserCode());
    		 logVo.setRequestXml(requestXml);
    		 logVo.setResponseXml(responseXml);
    		 logService.save(logVo);
    		 return logVo;
    	}
		return null;
	}
    
@Override
public ClaimInterfaceLogVo endCaseToHN(PrpLEndCaseVo endCaseVo,String userCode) {
	PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(endCaseVo.getRegistNo());
	//????????? ????????????????????????????????????
	if(CodeConstants.GBFlag.MINORCOMMON.equals(prpLRegistVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(prpLRegistVo.getIsGBFlag())){
		return null;
	}
	PrpLCMainVo prpLCMain = policyViewService.getPolicyInfo(prpLRegistVo.getRegistNo(), 
			prpLRegistVo.getPolicyNo());
	PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(endCaseVo.getClaimNo());
	PrpLCertifyMainVo certiMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(endCaseVo.getRegistNo());
	
	if(prpLCMain.getComCode().startsWith("50")){
		Date date = new Date();
		CaseBean caseBean = new CaseBean();
		String kindCode = getKindCode(endCaseVo.getRegistNo(),endCaseVo.getRiskCode());
		PrpLCompensateVo compensateVo = compensateTaskService.findCompByPK(endCaseVo.getCompensateNo());
		caseBean.setInstype("1");// ??????
		caseBean.setInssort(kindCode);
		caseBean.setPhase("3");//??????
		caseBean.setPolicyno(endCaseVo.getPolicyNo());
		if(endCaseVo.getEndCaseNo()!=null&&!"".equals(endCaseVo.getEndCaseNo())){
			caseBean.setCaseno(endCaseVo.getEndCaseNo());
		}
		caseBean.setName(userCode);
		caseBean.setMobile(prpLRegistVo.getReportorPhone());
		caseBean.setClaimsnum(prpLCMain.getClaimTimes().toString());//????????????
		if(compensateVo!=null&&compensateVo.getSumAmt()!=null){
			caseBean.setMoneys(Double.valueOf(compensateVo.getSumAmt().toString()));//????????????
		}else{
			caseBean.setMoneys(Double.valueOf("0"));//????????????
		}
		//?????????????????????0
		if("2".equals(claimVo.getValidFlag()) || "1".equals(certiMainVo.getIsFraud()) ||
				("1101".equals(claimVo.getRiskCode())&&"1".equals(certiMainVo.getIsJQFraud())) ||
				(!"1101".equals(claimVo.getRiskCode())&&"1".equals(certiMainVo.getIsSYFraud()))){
			caseBean.setResult("??????");
			caseBean.setMoneys(Double.valueOf("0"));
		}else{
			caseBean.setResult("??????");
		}
		caseBean.setXzname(codeTranService.findCodeName("CarRiskCode", endCaseVo.getRiskCode()));
		
		caseBean.setEffectivedate(DateUtils.dateToStr(prpLCMain.getStartDate(), DateUtils.YToDay));//??????????????????
//		caseBean.setAreaid(codeTranService.transCode("ComCodeFull",prpLCMain.getComCode()));//??????
		caseBean.setAreaid("?????????");
		if(prpLRegistVo.getPrpLRegistExt() != null && prpLRegistVo.getPrpLRegistExt().getInsuredName()!=null){
			caseBean.setInsuredname(prpLRegistVo.getPrpLRegistExt().getInsuredName());
		}
		if(prpLRegistVo.getInsuredPhone() != null){
			caseBean.setInsuredmobile(prpLRegistVo.getInsuredPhone());
		}
		//???????????????
		if("1".equals(prpLRegistVo.getPrpLRegistExt().getIsPersonLoss())){
			caseBean.setIspeoplehurt("???");
		}else{
			caseBean.setIspeoplehurt("???");
		}
		//?????????
		if(prpLRegistVo.getPrpLRegistExt()!=null && 
				StringUtils.isNotBlank(prpLRegistVo.getPrpLRegistExt().getLicenseNo())){
			caseBean.setCarbrandno(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
		}
		//?????????
		if(prpLRegistVo.getPrpLRegistExt()!=null&&
				StringUtils.isNotBlank(prpLRegistVo.getPrpLRegistExt().getFrameNo())){
			caseBean.setCarframeno(prpLRegistVo.getPrpLRegistExt().getFrameNo());
		}
		//???????????????????????????
		String dutybrandno = "";
		String dutyframeno = "";
		for(PrpLRegistCarLossVo registCarLossVo:prpLRegistVo.getPrpLRegistCarLosses()){
			if("3".equals(registCarLossVo.getLossparty())){
				if(registCarLossVo.getLicenseNo()!=null&&!registCarLossVo.getLicenseNo().isEmpty()){
					dutybrandno = dutybrandno+registCarLossVo.getLicenseNo()+",";
				}
				if(registCarLossVo.getFrameNo()!=null&&!registCarLossVo.getFrameNo().isEmpty()){
					dutyframeno = dutyframeno+registCarLossVo.getFrameNo()+",";
				}
				break;
			}
		}
		if(StringUtils.isNotBlank(dutybrandno)){
			dutybrandno = dutybrandno.substring(0, dutybrandno.length()-1);
			caseBean.setDutybrandno(dutybrandno);
		}
		if(StringUtils.isNotBlank(dutyframeno)){
			dutyframeno = dutyframeno.substring(0, dutyframeno.length()-1);
			caseBean.setDutyframeno(dutyframeno);
		}
		//????????????
		caseBean.setClosetime(DateUtils.dateToStr(endCaseVo.getEndCaseDate(), DateUtils.YToDay));
		//????????????
		if(prpLRegistVo.getDamageTime() != null){
			caseBean.setStarttime(DateUtils.dateToStr(prpLRegistVo.getDamageTime(), DateUtils.YToDay));
		}
		//????????????
		if(StringUtils.isNotBlank(prpLRegistVo.getDamageAddress())){
			caseBean.setAddress(prpLRegistVo.getDamageAddress());
		}
		//??????????????????
//		for(PrpLCInsuredVo prpLCInsuredVo:prpLCMain.getPrpCInsureds()){
//			 if("1".equals(prpLCInsuredVo.getInsuredFlag())){
//				 caseBean.setInsuredidcard(prpLCInsuredVo.getIdentifyNumber());
//				 break;
//			 }
//		 }
		 String url = SpringProperties.getProperty("HNIS_RUL");
		 String user = SpringProperties.getProperty("HNIS_USER");
		 String pwd = SpringProperties.getProperty("HNIS_PWD");
		 XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		 String requestXml = stream.toXML(caseBean);
		 caseBean = importCaseData(caseBean,url,user,pwd);
		 String responseXml = stream.toXML(caseBean);
		 logger.info("?????????????????????????????????--->"+responseXml);
		 ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		 logVo.setRegistNo(endCaseVo.getRegistNo());
		 logVo.setBusinessType(BusinessType.HNIS_endCase.name());
		 logVo.setBusinessName(BusinessType.HNIS_endCase.getName());
		 logVo.setOperateNode(FlowNode.EndCas.getName());
		 logVo.setComCode(prpLCMain.getComCode());
		 logVo.setClaimNo(endCaseVo.getClaimNo());
		 if(endCaseVo.getCompensateNo()!=null&&!"".equals(endCaseVo.getCompensateNo())){
			 logVo.setCompensateNo(endCaseVo.getCompensateNo());
		 }
		 if("5".equals(caseBean.getIstatus())){
			 logVo.setStatus("1");
		 }else{
			 logVo.setStatus("0");
		 }
		 if(caseBean.getResultdesc().isEmpty()||"null".equals(caseBean.getResultdesc())){
			 String msg = this.getResultdesc(caseBean.getIstatus());
			 logVo.setErrorMessage(msg);
		 }else{

			 logVo.setErrorMessage(caseBean.getResultdesc());
		 }
		 logVo.setRequestTime(date);
		 logVo.setRequestUrl(url);
		 logVo.setErrorCode(caseBean.getIstatus());
		 logVo.setCreateTime(date);
		 logVo.setCreateUser(userCode);
		 logVo.setRequestXml(requestXml);
		 logVo.setResponseXml(responseXml);
		 logService.save(logVo);
		 return logVo;
	}
	return null;
}
	
	public  String getKindCode(String registNo,String riskcode){
		String kindCode = "";
		if("1101".equals(riskcode)){
			kindCode = "??????";//??????
		}else{
			List<PrpLCItemKindVo> CItemKindVoList = registService.findCItemKindByPolicyNo(registNo);
			if(CItemKindVoList != null && CItemKindVoList.size() > 0){
				for(PrpLCItemKindVo prpLCItemKindVo:CItemKindVoList){
					if("A".equals(prpLCItemKindVo.getKindCode())){
						kindCode = "?????????";//??????
						break;
					}else if("B".equals(prpLCItemKindVo.getKindCode())){
						kindCode = "???????????????";//????????????
						break;
					}else if("G".equals(prpLCItemKindVo.getKindCode())){
						kindCode = "?????????";//??????
						break;
					}else if(prpLCItemKindVo.getKindCode().startsWith("D1")){
						kindCode = "????????????";//????????????
						break;
					}else if("F".equals(prpLCItemKindVo.getKindCode())){
						kindCode = "???????????????";
						break;
					}else if(prpLCItemKindVo.getKindCode().startsWith("L")){
						kindCode = "?????????";
						break;
					}else if(prpLCItemKindVo.getKindCode().startsWith("Z")){
						kindCode = "?????????";
						break;
					}else{
						kindCode = "???????????????";//???????????????
					}
				}
			}
		}
		return kindCode;
	}
	
	public String getResultdesc(String istatus){
		String resultdesc="";
		if("-4".equals(istatus)){
			resultdesc = "??????????????????????????????";
		}else if("-3".equals(istatus)){
			resultdesc = "????????????";
		}else if("-2".equals(istatus)){
			resultdesc = "????????????";
		}else if("-1".equals(istatus)){
			resultdesc = "????????????????????????";
		}else if("0".equals(istatus)){
			resultdesc = "???????????????????????????";
		}else if("1".equals(istatus)){
			resultdesc = "????????????";
		}else if("2".equals(istatus)){
			resultdesc = "??????????????????";
		}else if("3".equals(istatus)){
			resultdesc = "??????????????????";
		}else if("4".equals(istatus)){
			resultdesc = "?????????????????????";
		}else if("5".equals(istatus)){
			resultdesc = "??????????????????";
		}
		return resultdesc;
	}
}
