package ins.sino.claimcar.longrise.services;

import ins.framework.service.CodeTranService;
import ins.platform.utils.Base64EncodedUtil;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.service.CaseLeapService;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
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
import java.math.BigDecimal;
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
@Path("caseLeapService")
public class CaseLeapServiceImpl implements CaseLeapService{

	private static final Logger logger = LoggerFactory.getLogger(CaseLeapServiceImpl.class);

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

	public  CaseBean importCaseData(CaseBean caseBean,String comCode) {
		if(caseBean==null){
			return null;
		}
		List<CaseBean> caseBeans = new ArrayList<CaseBean>();
		caseBeans.add(caseBean);
		return importCaseData(caseBeans,comCode).get(0);
	}

	public  CaseBean importCaseData(CaseBean caseBean,String url,String user,String pwd) {
		if(caseBean==null){
			return null;
		}
		List<CaseBean> caseBeans = new ArrayList<CaseBean>();
		caseBeans.add(caseBean);
		return importCaseData(caseBeans,url,user,pwd).get(0);
	}

	public  List<CaseBean> importCaseData(List<CaseBean> caseBeans,String comCode) {
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
				caseBean.setResultdesc(String.valueOf(jobj.get("imessage")));
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

	@Override
	public  ClaimInterfaceLogVo claimToGZ(PrpLClaimVo prpLClaimVo,SysUserVo userVo) throws ParseException{
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLClaimVo.getRegistNo());
		//????????? ????????????????????????????????????
		if(CodeConstants.GBFlag.MINORCOMMON.equals(prpLRegistVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(prpLRegistVo.getIsGBFlag())){
			return null;
		}
		PrpLCMainVo prpLCMain = policyViewService.getPolicyInfo(prpLRegistVo.getRegistNo(),
				prpLRegistVo.getPolicyNo());
		//?????????????????????????????????????????????????????????????????????
		String subComCode = prpLCMain.getComCode().substring(0, 4);
		Boolean bl = false;
		if(((("1311".equals(subComCode)||"1301".equals(subComCode))&&prpLRegistVo.getDamageAreaCode().startsWith("5201"))||//??????
				("1302".equals(subComCode)&&prpLRegistVo.getDamageAreaCode().startsWith("5203"))||//??????
				("1308".equals(subComCode)&&prpLRegistVo.getDamageAreaCode().startsWith("5202"))||//?????????
				("1305".equals(subComCode)&&prpLRegistVo.getDamageAreaCode().startsWith("5204"))||//??????
				("1309".equals(subComCode)&&prpLRegistVo.getDamageAreaCode().startsWith("5227"))||//??????
				("1310".equals(subComCode)&&prpLRegistVo.getDamageAreaCode().startsWith("5226"))||//?????????
				("1307".equals(subComCode)&&(prpLRegistVo.getDamageAreaCode().startsWith("5205")||prpLRegistVo.getDamageAreaCode().startsWith("5223")))||//?????????
				("1307".equals(subComCode)&&prpLRegistVo.getDamageAreaCode().startsWith("52050"))||//??????
				("1306".equals(subComCode)&&prpLRegistVo.getDamageAreaCode().startsWith("5206")))&&//??????
				prpLRegistVo.getReportorPhone().startsWith("1")){//????????????
			bl = true;
		}
		if(bl){
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
			String areaid = this.getAreaid(prpLRegistVo.getDamageAreaCode());
			caseBean.setAreaid(areaid);//??????
			caseBean.setClaimsnum(prpLCMain.getClaimTimes().toString());//????????????
			caseBean.setMobile(prpLRegistVo.getReportorPhone());
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
			//?????????
			if(prpLRegistVo.getPrpLRegistExt()!=null && prpLRegistVo.getPrpLRegistExt().getLicenseNo()!=null){
				caseBean.setCarbrandno(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
			}
			//?????????
			if(prpLRegistVo.getPrpLRegistExt()!=null&&prpLRegistVo.getPrpLRegistExt().getFrameNo()!=null){
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
//			 for(PrpLCInsuredVo prpLCInsuredVo:prpLCMain.getPrpCInsureds()){
//				 if("1".equals(prpLCInsuredVo.getInsuredFlag())){
//					 caseBean.setInsuredidcard(prpLCInsuredVo.getIdentifyNumber());
//					 break;
//				 }
//			 }
			String url = SpringProperties.getProperty("GZIS_RUL");
			String user = SpringProperties.getProperty("GZIS_USER");
			String pwd = SpringProperties.getProperty("GZIS_PWD");
			XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
			String requestXml = stream.toXML(caseBean);
			caseBean = importCaseData(caseBean,url,user,pwd);
			String responseXml = stream.toXML(caseBean);
			logger.info("?????????????????????????????????--->"+responseXml);
			ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
			logVo.setRegistNo(prpLClaimVo.getRegistNo());
			logVo.setBusinessType(BusinessType.GZIS_claim.name());
			logVo.setBusinessName(BusinessType.GZIS_claim.getName());
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
	public ClaimInterfaceLogVo endCaseToGZ(PrpLEndCaseVo endCaseVo,String userCode){
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(endCaseVo.getRegistNo());
		//????????? ????????????????????????????????????
		if(CodeConstants.GBFlag.MINORCOMMON.equals(prpLRegistVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(prpLRegistVo.getIsGBFlag())){
			return null;
		}
		PrpLCMainVo prpLCMain = policyViewService.getPolicyInfo(prpLRegistVo.getRegistNo(),
				prpLRegistVo.getPolicyNo());
		PrpLCompensateVo compensateVo = compensateTaskService.findCompByPK(endCaseVo.getCompensateNo());
		//??????????????????????????????????????????????????????0???????????????????????????????????????
		String subComCode = prpLCMain.getComCode().substring(0, 4);
		Boolean bl = false;
		if(((("1311".equals(subComCode)||"1301".equals(subComCode))&&prpLRegistVo.getDamageAreaCode().startsWith("5201"))||//??????
				("1302".equals(subComCode)&&prpLRegistVo.getDamageAreaCode().startsWith("5203"))||//??????
				("1308".equals(subComCode)&&prpLRegistVo.getDamageAreaCode().startsWith("5202"))||//?????????
				("1305".equals(subComCode)&&prpLRegistVo.getDamageAreaCode().startsWith("5204"))||//??????
				("1309".equals(subComCode)&&prpLRegistVo.getDamageAreaCode().startsWith("5227"))||//??????
				("1310".equals(subComCode)&&prpLRegistVo.getDamageAreaCode().startsWith("5226"))||//?????????
				("1307".equals(subComCode)&&(prpLRegistVo.getDamageAreaCode().startsWith("5205")||prpLRegistVo.getDamageAreaCode().startsWith("5223")))||//?????????
				("1307".equals(subComCode)&&prpLRegistVo.getDamageAreaCode().startsWith("52050"))||//??????
				("1306".equals(subComCode)&&prpLRegistVo.getDamageAreaCode().startsWith("5206")))&&//??????
				prpLRegistVo.getReportorPhone().startsWith("1")&&compensateVo!=null&&compensateVo.getSumAmt()!=null&&//?????????????????????????????????0
				BigDecimal.ZERO.compareTo(compensateVo.getSumAmt())==-1){
			bl = true;
		}
		if(bl){
			Date date = new Date();
			CaseBean caseBean = new CaseBean();
			String kindCode = getKindCode(endCaseVo.getRegistNo(),endCaseVo.getRiskCode());
			caseBean.setInstype("1");// ??????
			caseBean.setInssort(kindCode);
			caseBean.setPhase("3");//??????
			caseBean.setPolicyno(endCaseVo.getPolicyNo());
			if(endCaseVo.getEndCaseNo()!=null&&!"".equals(endCaseVo.getEndCaseNo())){
				caseBean.setCaseno(endCaseVo.getEndCaseNo());
			}
			caseBean.setName(userCode);
			caseBean.setClaimsnum(prpLCMain.getClaimTimes().toString());//????????????
			caseBean.setMobile(prpLRegistVo.getReportorPhone());
			caseBean.setMoneys(Double.valueOf(compensateVo.getSumAmt().toString()));//????????????
			caseBean.setEffectivedate(DateUtils.dateToStr(prpLCMain.getStartDate(), DateUtils.YToDay));//??????????????????
			String areaid = this.getAreaid(prpLRegistVo.getDamageAreaCode());
			caseBean.setAreaid(areaid);//??????
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
			//?????????
			if(prpLRegistVo.getPrpLRegistExt()!=null && prpLRegistVo.getPrpLRegistExt().getLicenseNo()!=null){
				caseBean.setCarbrandno(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
			}
			//?????????
			if(prpLRegistVo.getPrpLRegistExt()!=null&&prpLRegistVo.getPrpLRegistExt().getFrameNo()!=null){
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
			caseBean.setClosetime(DateUtils.dateToStr(endCaseVo.getEndCaseDate(), DateUtils.YToDay));
			//????????????
			if(prpLRegistVo.getDamageTime() != null){
				caseBean.setStarttime(DateUtils.dateToStr(prpLRegistVo.getDamageTime(), DateUtils.YToDay));
			}
			//????????????
			if(prpLRegistVo.getDamageAddress() != null){
				caseBean.setAddress(prpLRegistVo.getDamageAddress());
			}
			//?????????????????????
//			for(PrpLCInsuredVo prpLCInsuredVo:prpLCMain.getPrpCInsureds()){
//				 if("1".equals(prpLCInsuredVo.getInsuredFlag())){
//					 caseBean.setInsuredidcard(prpLCInsuredVo.getIdentifyNumber());
//					 break;
//				 }
//			 }
			String url = SpringProperties.getProperty("GZIS_RUL");
			String user = SpringProperties.getProperty("GZIS_USER");
			String pwd = SpringProperties.getProperty("GZIS_PWD");
			XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
			String requestXml = stream.toXML(caseBean);
			caseBean = importCaseData(caseBean,url,user,pwd);
			String responseXml = stream.toXML(caseBean);
			logger.info("?????????????????????????????????--->"+responseXml);
			ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
			logVo.setRegistNo(endCaseVo.getRegistNo());
			logVo.setBusinessType(BusinessType.GZIS_endCase.name());
			logVo.setBusinessName(BusinessType.GZIS_endCase.getName());
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

	public String getAreaid(String DamageAreaCode){
		String areaid = "???";
		if(DamageAreaCode.startsWith("5201")){
			areaid = "?????????";
		}else if(DamageAreaCode.startsWith("5203")){
			areaid = "?????????";
		}else if(DamageAreaCode.startsWith("5202")){
			areaid = "????????????";
		}else if(DamageAreaCode.startsWith("5204")){
			areaid = "?????????";
		}else if(DamageAreaCode.startsWith("5227")){
			areaid = "??????????????????????????????";
		}else if(DamageAreaCode.startsWith("5226")){
			areaid = "??????????????????????????????";
		}else if(DamageAreaCode.startsWith("52050")){
			areaid = "????????????";
		}else if(DamageAreaCode.startsWith("5205")||DamageAreaCode.startsWith("5223")){
			areaid = "??????????????????????????????";
		}else if(DamageAreaCode.startsWith("5206")){
			areaid = "????????????";
		}
		return areaid;
	}

//public  void main(String[] args) throws Exception {
//
//	CaseBean caseBean = new CaseBean();
//		caseBean.setInstype("2");// ??????
//	caseBean.setPhase("2");
//	caseBean.setCaseno("1231567");
//	caseBean.setMobile("132456465");
//	caseBean.setInsuredname("ste");
//	caseBean.setName("?????????");
//	caseBean.setDotime("2016-08-08");
//	caseBean.setDoaddress("dress");
//	String url = "http://59.173.241.186:8042/GZIS/services/com.longrise.services.leap";
//	String user = "520000003065";
//	String pwd = "123456";
//	caseBean = importCaseData(caseBean,url,user,pwd);
//		System.out.println("====caseBean=="+caseBean);
//		System.out.println("====caseBean.getCaseno()=="+caseBean.getCaseno());
//		System.out.println("====caseBean.getIstatus()=="+caseBean.getIstatus());
//		System.out.println("====caseBean.getImessage()=="+caseBean.getImessage());
//	try{
//	}catch(Exception e){
//		e.printStackTrace();
//	}
//	}

}
