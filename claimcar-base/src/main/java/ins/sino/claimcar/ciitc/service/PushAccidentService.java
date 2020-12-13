package ins.sino.claimcar.ciitc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.lang.Springs;
import ins.framework.utils.Beans;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.ciitc.po.PrplZBXPushInfo;
import ins.sino.claimcar.ciitc.push.vo.DataInformation;
import ins.sino.claimcar.ciitc.push.vo.PushAccidentReqBodyVo;
import ins.sino.claimcar.ciitc.push.vo.PushAccidentReqVo;
import ins.sino.claimcar.ciitc.push.vo.PushAccidentResBodyVo;
import ins.sino.claimcar.ciitc.push.vo.PushAccidentResVo;
import ins.sino.claimcar.ciitc.push.vo.ResInformation;
import ins.sino.claimcar.ciitc.vo.CiitcReqHeadVo;
import ins.sino.claimcar.ciitc.vo.CiitcResHeadVo;
import ins.sino.claimcar.ciitc.vo.PrplZBXAreaVo;
import ins.sino.claimcar.ciitc.vo.PrplZBXPushInfoVo;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class PushAccidentService implements ServiceInterface{

	private static Logger logger = LoggerFactory.getLogger(PushAccidentService.class);
	@Autowired
	AccidentService accidentService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	
	@Autowired
    private DatabaseDao databaseDao;
	

	private List<String> licenseTypeEnum = new ArrayList<String>();
	private List<String> acciDutyEnum = new ArrayList<String>();
	private List<String> caseSourceEnum = new ArrayList<String>();
	
	@Override
	public Object service(String arg0, Object arg1) {
		this.init();
		Date now = DateUtils.now();
/*		String username = SpringProperties.getProperty("CIITC_USER");
		String password = SpringProperties.getProperty("CIITC_PWD");*/
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		CiitcResHeadVo ciitcResHeadVo = new CiitcResHeadVo();
		PushAccidentResVo pushAccidentResVo = new PushAccidentResVo();
		PushAccidentResBodyVo pushAccidentResBodyVo = new PushAccidentResBodyVo();
		List<ResInformation> resInformations = new ArrayList<ResInformation>();
		List<DataInformation> dataInformations = new ArrayList<DataInformation>();
		if(interfaceAsyncService == null) {
			interfaceAsyncService = (InterfaceAsyncService)Springs.getBean(InterfaceAsyncService.class);
		}
		boolean isInsured = false;
		try{
			stream.processAnnotations(PushAccidentReqVo.class);
			PushAccidentReqVo pushAccidentReqVo = (PushAccidentReqVo) arg1;
			String xml = stream.toXML(pushAccidentReqVo);
			logger.info("中保信信息推送接收报文: \n"+xml);
			PushAccidentReqBodyVo pushAccidentReqBodyVo = pushAccidentReqVo.getBody();
			dataInformations = pushAccidentReqBodyVo.getDataInformations();
			CiitcReqHeadVo ciitcReqHeadVo = pushAccidentReqVo.getHead();
			
			if(!"01".equals(ciitcReqHeadVo.getRequestType())){
				ciitcResHeadVo.setRequestType("01");
				ciitcResHeadVo.setResCode("1");
				ciitcResHeadVo.setErrorCode("12005");
				ciitcResHeadVo.setErrorMessage("请求类型不正确");
				pushAccidentResVo.setHead(ciitcResHeadVo);
				logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
				return pushAccidentResVo;
			}
		/*	if(StringUtils.isBlank(ciitcReqHeadVo.getUserName())||StringUtils.isBlank(ciitcReqHeadVo.getPassWord())){
				ciitcResHeadVo.setRequestType("01");
				ciitcResHeadVo.setResCode("1");
				ciitcResHeadVo.setErrorCode("11001");
				ciitcResHeadVo.setErrorMessage("用户名或密码不能为空");
				pushAccidentResVo.setHead(ciitcResHeadVo);
				logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
				return pushAccidentResVo;
			}
			if(!username.equals(ciitcReqHeadVo.getUserName())||!password.equals(ciitcReqHeadVo.getPassWord())){
				ciitcResHeadVo.setRequestType("01");
				ciitcResHeadVo.setResCode("1");
				ciitcResHeadVo.setErrorCode("11002");
				ciitcResHeadVo.setErrorMessage("用户名或密码不正确");
				pushAccidentResVo.setHead(ciitcResHeadVo);
				logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
				return pushAccidentResVo;
			}*/
			if(!"DHIC".equals(ciitcReqHeadVo.getInstitutionCode())){
				ciitcResHeadVo.setRequestType("01");
				ciitcResHeadVo.setResCode("1");
				ciitcResHeadVo.setErrorCode("11006");
				ciitcResHeadVo.setErrorMessage("用户所属保险公司不匹配");
				pushAccidentResVo.setHead(ciitcResHeadVo);
				logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
				return pushAccidentResVo;
			}
			if(!this.isEachDistrict(ciitcReqHeadVo.getAcciAreaCode())){
				ciitcResHeadVo.setRequestType("01");
				ciitcResHeadVo.setResCode("1");
				ciitcResHeadVo.setErrorCode("11007");
				ciitcResHeadVo.setErrorMessage("用户所属地区不匹配");
				pushAccidentResVo.setHead(ciitcResHeadVo);
				logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
				return pushAccidentResVo;
			}
			
			for (DataInformation dataInformation : dataInformations) {
				if(StringUtils.isBlank(dataInformation.getAcciNo())){
					ciitcResHeadVo.setRequestType("01");
					ciitcResHeadVo.setResCode("1");
					ciitcResHeadVo.setErrorCode("12002");
					ciitcResHeadVo.setErrorMessage("事故编号为空");
					pushAccidentResVo.setHead(ciitcResHeadVo);
					logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
					return pushAccidentResVo;
				}
				if(StringUtils.isBlank(dataInformation.getAcciAreaCode())){
					ciitcResHeadVo.setRequestType("01");
					ciitcResHeadVo.setResCode("1");
					ciitcResHeadVo.setErrorCode("12002");
					ciitcResHeadVo.setErrorMessage("事故地区代码为空");
					pushAccidentResVo.setHead(ciitcResHeadVo);
					logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
					return pushAccidentResVo;
				}
				if(!this.isEachDistrict(dataInformation.getAcciAreaCode())){
					ciitcResHeadVo.setRequestType("01");
					ciitcResHeadVo.setResCode("1");
					ciitcResHeadVo.setErrorCode("12003");
					ciitcResHeadVo.setErrorMessage("事故地区代码枚举值不在范围内");
					pushAccidentResVo.setHead(ciitcResHeadVo);
					logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
					return pushAccidentResVo;
				}
				if(StringUtils.isBlank(dataInformation.getCaseFlag())){
					ciitcResHeadVo.setRequestType("01");
					ciitcResHeadVo.setResCode("1");
					ciitcResHeadVo.setErrorCode("12002");
					ciitcResHeadVo.setErrorMessage("业务类型为空");
					pushAccidentResVo.setHead(ciitcResHeadVo);
					logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
					return pushAccidentResVo;
				}
				if(!"01".equals(dataInformation.getCaseFlag())&&!"02".equals(dataInformation.getCaseFlag())){
					ciitcResHeadVo.setRequestType("01");
					ciitcResHeadVo.setResCode("1");
					ciitcResHeadVo.setErrorCode("12003");
					ciitcResHeadVo.setErrorMessage("业务类型枚举值不在范围内");
					pushAccidentResVo.setHead(ciitcResHeadVo);
					logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
					return pushAccidentResVo;
				}
				if(StringUtils.isBlank(dataInformation.getAcciDuty())){
					ciitcResHeadVo.setRequestType("01");
					ciitcResHeadVo.setResCode("1");
					ciitcResHeadVo.setErrorCode("12002");
					ciitcResHeadVo.setErrorMessage("事故责任为空");
					pushAccidentResVo.setHead(ciitcResHeadVo);
					logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
					return pushAccidentResVo;
				}
				if(!acciDutyEnum.contains(dataInformation.getAcciDuty())){
					ciitcResHeadVo.setRequestType("01");
					ciitcResHeadVo.setResCode("1");
					ciitcResHeadVo.setErrorCode("12003");
					ciitcResHeadVo.setErrorMessage("事故责任枚举值不在范围内");
					pushAccidentResVo.setHead(ciitcResHeadVo);
					logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
					return pushAccidentResVo;
				}
			/*	if(StringUtils.isBlank(dataInformation.getComAreaCode())){
					ciitcResHeadVo.setRequestType("01");
					ciitcResHeadVo.setResCode("1");
					ciitcResHeadVo.setErrorCode("12002");
					ciitcResHeadVo.setErrorMessage("承保公司所在地区代码为空");
					pushAccidentResVo.setHead(ciitcResHeadVo);
					return pushAccidentResVo;
				}
				if(!this.isEachDistrict(dataInformation.getComAreaCode())){
					ciitcResHeadVo.setRequestType("01");
					ciitcResHeadVo.setResCode("1");
					ciitcResHeadVo.setErrorCode("12003");
					ciitcResHeadVo.setErrorMessage("承保公司所在地区代码枚举值不在范围内");
					pushAccidentResVo.setHead(ciitcResHeadVo);
					return pushAccidentResVo;
				}*/
				if(StringUtils.isBlank(dataInformation.getLicenseNo())){
					ciitcResHeadVo.setRequestType("01");
					ciitcResHeadVo.setResCode("1");
					ciitcResHeadVo.setErrorCode("12002");
					ciitcResHeadVo.setErrorMessage("车辆号牌号码为空");
					pushAccidentResVo.setHead(ciitcResHeadVo);
					logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
					return pushAccidentResVo;
				}
				if(!licenseTypeEnum.contains(dataInformation.getLicenseType())){
					ciitcResHeadVo.setRequestType("01");
					ciitcResHeadVo.setResCode("1");
					ciitcResHeadVo.setErrorCode("12003");
					ciitcResHeadVo.setErrorMessage("车辆号牌种类枚举值不在范围内");
					pushAccidentResVo.setHead(ciitcResHeadVo);
					logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
					return pushAccidentResVo;
				}
				if(StringUtils.isBlank(dataInformation.getReportDate())){
					ciitcResHeadVo.setRequestType("01");
					ciitcResHeadVo.setResCode("1");
					ciitcResHeadVo.setErrorCode("12002");
					ciitcResHeadVo.setErrorMessage("事故发生时间为空");
					pushAccidentResVo.setHead(ciitcResHeadVo);
					logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
					return pushAccidentResVo;
				}
				if(StringUtils.isBlank(dataInformation.getCaseFlag())){
					ciitcResHeadVo.setRequestType("01");
					ciitcResHeadVo.setResCode("1");
					ciitcResHeadVo.setErrorCode("12002");
					ciitcResHeadVo.setErrorMessage("案件来源为空");
					pushAccidentResVo.setHead(ciitcResHeadVo);
					logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
					return pushAccidentResVo;
				}
				if(!caseSourceEnum.contains(dataInformation.getCaseSource())){
					ciitcResHeadVo.setRequestType("01");
					ciitcResHeadVo.setResCode("1");
					ciitcResHeadVo.setErrorCode("12003");
					ciitcResHeadVo.setErrorMessage("案件来源枚举值不在范围内");
					pushAccidentResVo.setHead(ciitcResHeadVo);
					logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
					return pushAccidentResVo;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			ciitcResHeadVo.setRequestType("01");
			ciitcResHeadVo.setResCode("1");
			ciitcResHeadVo.setErrorCode("12004");
			ciitcResHeadVo.setErrorMessage("报文解析异常");
			pushAccidentResVo.setHead(ciitcResHeadVo);
			logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
			return pushAccidentResVo;
		}
		
		try {
			List<PrplZBXPushInfoVo> prplZBXPushInfoVos = new ArrayList<PrplZBXPushInfoVo>();
			prplZBXPushInfoVos = Beans.copyDepth().from(dataInformations).toList(PrplZBXPushInfoVo.class);
			for (PrplZBXPushInfoVo prplZBXPushInfoVo : prplZBXPushInfoVos) {
				 PrplZBXPushInfoVo oldPrplZBXPushInfo = accidentService.findInfoByAcciNo(prplZBXPushInfoVo.getAcciNo());
				if(oldPrplZBXPushInfo != null){//查询是否已经存在相同的事故
					accidentService.updateAccidentInfo(oldPrplZBXPushInfo);
				} else{
					List<PrpLCItemCarVo> prpLCItemCarVos = policyViewService.findPrpcItemcar(prplZBXPushInfoVo.getLicenseNo(), prplZBXPushInfoVo.getEngineNo(), prplZBXPushInfoVo.getVin());
					if(prpLCItemCarVos.isEmpty()){
						ResInformation resInformation = new ResInformation();
						resInformation.setLicenseNo(prplZBXPushInfoVo.getLicenseNo());
						resInformation.setLicenseType(prplZBXPushInfoVo.getLicenseType());
						resInformation.setEngineNo(prplZBXPushInfoVo.getEngineNo());
						resInformation.setVin(prplZBXPushInfoVo.getVin());
						resInformations.add(resInformation);
						prplZBXPushInfoVo.setIsInsured("0");
					}else {
						isInsured = true;
						List<String> policyNos = new ArrayList<String>();
						for(PrpLCItemCarVo  prpLCItemCarVo: prpLCItemCarVos){
							policyNos.add(prpLCItemCarVo.getPolicyNo());
						}
						List<PrpLRegistVo> prpLRegistVos = registQueryService.findPrpLRegistByPolicyNos(policyNos);
						if(!prpLRegistVos.isEmpty()){
							Date reportDate = DateUtils.strToDate(prplZBXPushInfoVo.getReportDate(),DateUtils.YMDHMSS);
							//如果消息推送的事故出险时间在报案的出险时间的前后12小时之内，则进行消息推送
							if(DateUtils.compareMinutes(reportDate,prpLRegistVos.get(0).getDamageTime())/60 <12 
									&& DateUtils.compareMinutes(reportDate,prpLRegistVos.get(0).getDamageTime())/60 >-12){
								SysUserVo userVo = new SysUserVo();
								userVo.setComCode("00000000");
								userVo.setUserCode("0000000000");
								//如果报案暂存，则推送消息给中保信
								if(prpLRegistVos.get(0).getRegistTaskFlag().equals("0")){
									interfaceAsyncService.reqByRegist(prpLRegistVos.get(0), userVo, "01");
								}
								String registNo = prpLRegistVos.get(0).getRegistNo();
								prplZBXPushInfoVo.setRegistNo(registNo);
							}
						}
						prplZBXPushInfoVo.setIsInsured("1");	
					}
					prplZBXPushInfoVo.setCreateUser("0000000000");
					prplZBXPushInfoVo.setCreateTime(now);
					prplZBXPushInfoVo.setUpdateUser("0000000000");
					prplZBXPushInfoVo.setUpdateTime(now);
					accidentService.saveAccidentInfo(prplZBXPushInfoVos);
				}
				}
		} catch (Exception e) {
			e.printStackTrace();
			ciitcResHeadVo.setRequestType("01");
			ciitcResHeadVo.setResCode("1");
			ciitcResHeadVo.setErrorCode("20001");
			ciitcResHeadVo.setErrorMessage("操作数据库异常，请联系管理员处理");
			pushAccidentResVo.setHead(ciitcResHeadVo);
			logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
			return pushAccidentResVo;
		}
		
		if(isInsured){//存在承保车辆
			if(resInformations.isEmpty()){
				ciitcResHeadVo.setRequestType("01");
				ciitcResHeadVo.setResCode("0");
				ciitcResHeadVo.setErrorCode("00000");
				ciitcResHeadVo.setErrorMessage("成功");
				pushAccidentResVo.setHead(ciitcResHeadVo);
				logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
				return pushAccidentResVo;
			}else {
				ciitcResHeadVo.setRequestType("01");
				ciitcResHeadVo.setResCode("0");
				ciitcResHeadVo.setErrorCode("00000");
				ciitcResHeadVo.setErrorMessage("成功");
				pushAccidentResVo.setHead(ciitcResHeadVo);
				pushAccidentResBodyVo.setResInformation(resInformations);
				pushAccidentResVo.setBody(pushAccidentResBodyVo);
				logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
				return pushAccidentResVo;
			}	
		}else {//不存在承包车辆
			ciitcResHeadVo.setRequestType("01");
			ciitcResHeadVo.setResCode("0");
			ciitcResHeadVo.setErrorCode("12008");
			ciitcResHeadVo.setErrorMessage("事故车辆不在本公司承保");
			pushAccidentResVo.setHead(ciitcResHeadVo);
			pushAccidentResBodyVo.setResInformation(resInformations);
			pushAccidentResVo.setBody(pushAccidentResBodyVo);
			logger.info("中保信信息推送返回报文: \n" +  stream.toXML(pushAccidentResVo));
			return pushAccidentResVo;
		}			
	}
	

	private void init(){
		if(accidentService == null){
			accidentService = (AccidentService)Springs.getBean(AccidentService.class);
		}
		if(policyViewService == null){
			policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
		}
		if(registQueryService == null){
			registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
		}
		/**
		 * 号牌种类枚举
		 */
		String[] licenseTypeString = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12","13"
				,"14","15","16","17","18","19","20","21","22","23","24","25","31","32","51","52"};
		licenseTypeEnum = Arrays.asList(licenseTypeString);
		
		/**
		 * 事故责任枚举
		 */
		String[] acciDutyString = new String[]{"0","1","2","3","4","5","99"};
		acciDutyEnum = Arrays.asList(acciDutyString);
		
		/**
		 * 案件来源枚举
		 */
		String[] caseSourceString = new String[]{"01","02","03","04","05","06","07","08","09"};
		caseSourceEnum = Arrays.asList(caseSourceString);
	}
	
	/**
	 * 判断是否存在该区域
	 * @param acciAreaCode
	 * @return
	 */
	private boolean isEachDistrict(String acciAreaCode) {
		boolean isEachDistrict = false;
		PrplZBXAreaVo prplZBXAreaVo = accidentService.findZBXArea(acciAreaCode);
		if(prplZBXAreaVo!=null){
			isEachDistrict = true;
		}
		return isEachDistrict;
	}
	
}
