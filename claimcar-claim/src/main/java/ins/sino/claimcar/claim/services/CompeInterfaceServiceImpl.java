package ins.sino.claimcar.claim.services;

import java.math.BigDecimal;
import java.text.ParseException;
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
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import ins.platform.utils.DateUtils;
import ins.platform.utils.XstreamFactory;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.ciitc.service.AccidentService;
import ins.sino.claimcar.ciitc.service.CompeInterfaceService;
import ins.sino.claimcar.ciitc.vo.CiitcReqHeadVo;
import ins.sino.claimcar.ciitc.vo.CiitcResHeadVo;
import ins.sino.claimcar.ciitc.vo.PrplZBXPushInfoVo;
import ins.sino.claimcar.ciitc.vo.compe.CiitcCompeReqBody;
import ins.sino.claimcar.ciitc.vo.compe.CiitcCompeReqVo;
import ins.sino.claimcar.ciitc.vo.compe.CiitcCompeResVo;
import ins.sino.claimcar.ciitc.vo.compe.ReqPayInformation;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.trafficplatform.service.EarlyWarnService;
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" })
@Path("CompeInterfaceService")
public class CompeInterfaceServiceImpl implements CompeInterfaceService {

	private static Logger logger = LoggerFactory.getLogger(CompeInterfaceServiceImpl.class);
	
	@Autowired
	EarlyWarnService earlyWarnService;
	@Autowired
	ClaimInterfaceLogService claimInterfaceLogService;
	@Autowired
	AccidentService accidentService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	CertifyPubService certifyPubService;
	
	public void request(CiitcCompeReqVo reqVo,String comCode,SysUserVo userVo,String claimNo){
		String reqXML = "";
		String resXML = "";
		String url = SpringProperties.getProperty("CIITC_URL");
		String reportType = "";
		CiitcCompeResVo resultVo = new CiitcCompeResVo();
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		CiitcResHeadVo resHead = new CiitcResHeadVo();
		Date date = new Date();
		try{
			XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
			stream.autodetectAnnotations(true);
			stream.setMode(XStream.NO_REFERENCES);
			stream.aliasSystemAttribute(null,"class");// 去掉 class属性
			stream.addImplicitCollection(CiitcCompeReqBody.class, "payInfoList");//不生成payInfoList节点
			
			reportType = reqVo.getBody().getPayInfoList().get(0).getReportType();
			reqXML = stream.toXML(reqVo);
			logger.info("=================发送理赔信息给中国保信报文"+reqXML);
			resXML = earlyWarnService.requestSDEW(reqXML, url);
			logger.info("=================理赔信息送中国保信返回报文"+resXML);
			resultVo = XstreamFactory.xmlToObj(resXML, CiitcCompeResVo.class);
			resHead = resultVo.getHead();
			if("0".equals(resHead.getResCode())){
				logVo.setStatus("1");
	            logVo.setErrorMessage(resHead.getErrorMessage());
			}else{
				logVo.setStatus("0");
				logVo.setErrorCode(resHead.getErrorCode());
	            logVo.setErrorMessage(resHead.getErrorMessage());
			}
		}catch(Exception e){
			logVo.setStatus("0");
            logVo.setErrorCode("false");
            logVo.setErrorMessage(e.getMessage());
			e.printStackTrace();
			logger.info("=================发送理赔信息给中国保信报错"+e.getMessage());
		}finally{
			logVo.setBusinessType(BusinessType.CIITC_Compe.name());
	        logVo.setBusinessName(BusinessType.CIITC_Compe.getName());
	        logVo.setRegistNo(reqVo.getBody().getPayInfoList().get(0).getTraAcciNo());
	        if("00".equals(reportType)){
	        	logVo.setOperateNode("未生成报案号");
	        }else if("01".equals(reportType)){
	        	logVo.setOperateNode("报案");
	        }else if("02".equals(reportType)){
	        	logVo.setOperateNode("结案");
	        }else if("03".equals(reportType)){
	        	logVo.setOperateNode("报案注销");
	        }else if("04".equals(reportType)){
	        	logVo.setOperateNode("拒赔");
	        }else if("05".equals(reportType)){
	        	logVo.setOperateNode("立案注销");
	        }
            logVo.setComCode(comCode);
            logVo.setRequestTime(date);
            logVo.setRequestUrl(url);
            logVo.setCreateUser(userVo.getUserCode());
            logVo.setCreateTime(date);
            logVo.setRequestXml(reqXML);
            logVo.setResponseXml(resXML);
            logVo.setFlag(reportType);
            logVo.setClaimNo(claimNo);
            claimInterfaceLogService.save(logVo);
		}
		
	}
	
	@Override
	public void reqByRegist(PrpLRegistVo registVo,SysUserVo userVo,String reportType) throws Exception{
		List<PrpLCItemCarVo> prpLCItemCarVos = policyViewService.findPrpcItemcarByRegistNo(registVo.getRegistNo());
		PrpLCItemCarVo prpLCItemCarVo = prpLCItemCarVos.get(0);
		String frameNo = prpLCItemCarVo.getVinNo();
		if(StringUtils.isBlank(prpLCItemCarVo.getVinNo())  || prpLCItemCarVo.getVinNo().equals("")){
			frameNo= prpLCItemCarVo.getFrameNo();
		}
		List<PrplZBXPushInfoVo> infoVoList = accidentService.findAccidentInfo(frameNo,prpLCItemCarVo.getEngineNo(),registVo.getPrpLRegistExt().getLicenseNo());
		if(infoVoList!=null && infoVoList.size()>0){
			/*  案件来源为交科所（代码为07-09）的回执类型：未生成报案号、报案；
				案件来源为北京交警APP（代码为01-06）的回执类型：未生成报案号、报案、结案、拒赔、报案注销、立案注销。*/
			if("01".equals(reportType) || ("03".equals(reportType) && "010203040506".indexOf(infoVoList.get(0).getCaseSource())!=-1)){
				List<PrpLCMainVo> prpLCMainList=policyViewService.getPolicyAllInfo(registVo.getRegistNo());
				String userName = SpringProperties.getProperty("CIITC_VIEW_USER");
				String passWord = SpringProperties.getProperty("CIITC_VIEW_PWD");
				
				CiitcCompeReqVo reqVo = new CiitcCompeReqVo();
				CiitcReqHeadVo head = new CiitcReqHeadVo();
				CiitcCompeReqBody body = new CiitcCompeReqBody();
				List<ReqPayInformation>  payInfoList = new ArrayList<ReqPayInformation>();
				
				head.setRequestType("03");
				head.setUserName(userName);
				head.setPassWord(passWord);
				head.setInstitutionCode("DHIC");
				head.setAcciAreaCode(infoVoList.get(0).getAcciAreaCode());
				reqVo.setHead(head);
				
				for(PrpLCMainVo cmainVo:prpLCMainList){
					ReqPayInformation payInfoVo = new ReqPayInformation();
					payInfoVo.setAcciNo(infoVoList.get(0).getAcciNo());
					payInfoVo.setAcciAreaCode(infoVoList.get(0).getAcciAreaCode());
					payInfoVo.setReportType(reportType);
					payInfoVo.setRiskTypeCode("1101".equals(cmainVo.getRiskCode()) ? "01":"02");
					payInfoVo.setPolicyNo(cmainVo.getPolicyNo());
					payInfoVo.setTraAcciNo(registVo.getRegistNo());
					payInfoVo.setTraAcciTime(DateUtils.dateToStr(registVo.getReportTime(), DateUtils.YMDHM));
					if("03".equals(reportType) && registVo.getCancelTime() != null){//报案注销的理赔时间必传
						payInfoVo.setPayTime(DateUtils.dateToStr(registVo.getCancelTime(), DateUtils.YMDHM));
					}else{
						payInfoVo.setPayTime("");
					}
					payInfoVo.setPayMoney("");
					payInfoList.add(payInfoVo);
				}
				body.setPayInfoList(payInfoList);
				reqVo.setBody(body);
				this.request(reqVo, registVo.getComCode(), userVo,null);	
			}
		}
	
	}
	
	@Override
	public void reqByEndCase(PrpLEndCaseVo endCaseVo,SysUserVo userVo){
		List<PrplZBXPushInfoVo> infoVoList = accidentService.findAccidentInfoByOther(endCaseVo.getRegistNo(), null);
		if(infoVoList!=null && infoVoList.size()>0 && "010203040506".indexOf(infoVoList.get(0).getCaseSource())!=-1){
			PrpLRegistVo registVo = registQueryService.findByRegistNo(endCaseVo.getRegistNo());
			PrpLCompensateVo compeVo = compensateTaskService.findCompByPK(endCaseVo.getCompensateNo());
			PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(endCaseVo.getClaimNo());
			PrpLCertifyMainVo certiMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(endCaseVo.getRegistNo());
			String userName = SpringProperties.getProperty("CIITC_VIEW_USER");
			String passWord = SpringProperties.getProperty("CIITC_VIEW_PWD");
			
			CiitcCompeReqVo reqVo = new CiitcCompeReqVo();
			CiitcReqHeadVo head = new CiitcReqHeadVo();
			CiitcCompeReqBody body = new CiitcCompeReqBody();
			List<ReqPayInformation>  payInfoList = new ArrayList<ReqPayInformation>();
			
			head.setRequestType("03");
			head.setUserName(userName);
			head.setPassWord(passWord);
			head.setInstitutionCode("DHIC");
			head.setAcciAreaCode(infoVoList.get(0).getAcciAreaCode());
			reqVo.setHead(head);
			
			ReqPayInformation payInfoVo = new ReqPayInformation();
			payInfoVo.setAcciNo(infoVoList.get(0).getAcciNo());
			payInfoVo.setAcciAreaCode(infoVoList.get(0).getAcciAreaCode());
			payInfoVo.setRiskTypeCode("1101".equals(endCaseVo.getRiskCode()) ? "01":"02");
			payInfoVo.setPolicyNo(endCaseVo.getPolicyNo());
			payInfoVo.setTraAcciNo(registVo.getRegistNo());
			payInfoVo.setTraAcciTime(DateUtils.dateToStr(registVo.getReportTime(), DateUtils.YMDHM));
			if("CompeWfZero".equals(endCaseVo.getCompensateNo())){
				payInfoVo.setPayMoney("0.0");
			}else{
				payInfoVo.setPayMoney(String.valueOf(NullToZero(compeVo.getSumAmt()).setScale(2, BigDecimal.ROUND_HALF_UP)));
			}
			//拒赔金额默认为0
			if("2".equals(claimVo.getValidFlag()) || "1".equals(certiMainVo.getIsFraud()) ||
					("1101".equals(claimVo.getRiskCode())&&"1".equals(certiMainVo.getIsJQFraud())) ||
					(!"1101".equals(claimVo.getRiskCode())&&"1".equals(certiMainVo.getIsSYFraud()))){
				payInfoVo.setReportType("04");
				payInfoVo.setPayMoney("0.0");
			}else{
				payInfoVo.setReportType("02");
			}
			if(compeVo!=null && compeVo.getUnderwriteDate()!=null){
				payInfoVo.setPayTime(DateUtils.dateToStr(compeVo.getUnderwriteDate(), DateUtils.YMDHM));
			}else{
				payInfoVo.setPayTime(DateUtils.dateToStr(endCaseVo.getEndCaseDate(), DateUtils.YMDHM));
			}
			payInfoList.add(payInfoVo);
			body.setPayInfoList(payInfoList);
			reqVo.setBody(body);
			this.request(reqVo, registVo.getComCode(), userVo, endCaseVo.getClaimNo());
		}
	}
	
	@Override
	public void reqByCancel(PrpLClaimVo claimVo,SysUserVo userVo){
		List<PrplZBXPushInfoVo> infoVoList = accidentService.findAccidentInfoByOther(claimVo.getRegistNo(), null);
		if(infoVoList!=null && infoVoList.size()>0 && "010203040506".indexOf(infoVoList.get(0).getCaseSource())!=-1){
			PrpLRegistVo registVo = registQueryService.findByRegistNo(claimVo.getRegistNo());
			String userName = SpringProperties.getProperty("CIITC_VIEW_USER");
			String passWord = SpringProperties.getProperty("CIITC_VIEW_PWD");
			
			CiitcCompeReqVo reqVo = new CiitcCompeReqVo();
			CiitcReqHeadVo head = new CiitcReqHeadVo();
			CiitcCompeReqBody body = new CiitcCompeReqBody();
			List<ReqPayInformation>  payInfoList = new ArrayList<ReqPayInformation>();
			
			head.setRequestType("03");
			head.setUserName(userName);
			head.setPassWord(passWord);
			head.setInstitutionCode("DHIC");
			head.setAcciAreaCode(infoVoList.get(0).getAcciAreaCode());
			reqVo.setHead(head);
			
			ReqPayInformation payInfoVo = new ReqPayInformation();
			payInfoVo.setAcciNo(infoVoList.get(0).getAcciNo());
			payInfoVo.setAcciAreaCode(infoVoList.get(0).getAcciAreaCode());
			payInfoVo.setRiskTypeCode("1101".equals(claimVo.getRiskCode()) ? "01":"02");
			payInfoVo.setPolicyNo(claimVo.getPolicyNo());
			payInfoVo.setTraAcciNo(registVo.getRegistNo());
			payInfoVo.setTraAcciTime(DateUtils.dateToStr(registVo.getReportTime(), DateUtils.YMDHM));
			payInfoVo.setReportType("05");
			if(claimVo.getCancelTime()!=null){
				payInfoVo.setPayTime(DateUtils.dateToStr(claimVo.getCancelTime(), DateUtils.YMDHM));
			}else{
				payInfoVo.setPayTime(DateUtils.dateToStr(new Date(), DateUtils.YMDHM));
			}
			payInfoList.add(payInfoVo);
			body.setPayInfoList(payInfoList);
			reqVo.setBody(body);
			this.request(reqVo, registVo.getComCode(), userVo, claimVo.getClaimNo());
		}
	}
	
	private static BigDecimal NullToZero(BigDecimal strNum) {
		if(strNum==null){
			return new BigDecimal("0");
		}
		return strNum;
	}
	
}
