package ins.sino.claimcar.selfHelpClaimCar.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ins.framework.lang.Springs;
import ins.platform.utils.ClaimBaseCoder;
import ins.sino.claimcar.selfHelpClaimCar.vo.LockPolicyReqPacketVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.LockPolicyResBodyVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.LockPolicyResPacketVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.PolicyInfoReqVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.PolicyInfoResVo;
import ins.sino.claimcar.regist.service.PolicyQueryService;
import ins.sino.claimcar.regist.vo.PolicyInfoVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.ResponseHeadVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.ResquestHeadVo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class LockPolicyNoServiceImpl implements ServiceInterface {
	
	private static Logger logger = LoggerFactory.getLogger(LockPolicyNoServiceImpl.class);
    @Autowired
	PolicyQueryService policyQueryService;
    
    @Override
	public Object service(String paramString, Object obj) {
		init();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		LockPolicyResPacketVo resVo=new LockPolicyResPacketVo();//响应Vo类
		ResponseHeadVo resHeadVo=new ResponseHeadVo();//响应头部
		LockPolicyResBodyVo resBodyVo=new LockPolicyResBodyVo();//响应体Vo类
		stream.processAnnotations(LockPolicyResPacketVo.class);
		stream.processAnnotations(LockPolicyReqPacketVo.class);
		
		try{
			String reqXml = ClaimBaseCoder.objToXmlUtf(obj);
			logger.info("自助理赔保单绑定请求Xml------>"+reqXml);
			
			LockPolicyReqPacketVo lockPolicyReqPacketVo	=(LockPolicyReqPacketVo)obj;
			ResquestHeadVo reqHeadVo=lockPolicyReqPacketVo.getHeadVo();//请求头部
			PolicyInfoReqVo infoReqVo=lockPolicyReqPacketVo.getBodyVo().getInfoVo();
			if (!"SELFCLAIM_001".equals(reqHeadVo.getRequestType())|| !"claim_user".equals(reqHeadVo.getUser())|| !"claim_psd".equals(reqHeadVo.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			
			//根据接收报文，查出符合的保单
			List<PolicyInfoVo> policyVoList=policyQueryService.findPolicyNoList(infoReqVo.getInsuredName(),infoReqVo.getLicenseNo(), infoReqVo.getIdentifyNumber(),infoReqVo.getIdentifyType(),infoReqVo.getEngineNo(),infoReqVo.getFrameNo());
			List<PolicyInfoResVo> infoVos=new ArrayList<PolicyInfoResVo>();
			if(policyVoList!=null && policyVoList.size()>0){
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for(PolicyInfoVo vo:policyVoList){
					if(StringUtils.isNotBlank(vo.getPolicyNo())){
						//筛选出当前时间在有效期间的保单
						String startDateString = DateFormatString(vo.getStartDate(),vo.getStartHour());
						Date startDate = dateFormat.parse(startDateString);
						String endDateString = DateFormatString(vo.getEndDate(),vo.getEndHour());
						Date endDate = dateFormat.parse(endDateString);
						Date nowDate = new Date();
						if((nowDate.getTime() >= startDate.getTime()) && (nowDate.getTime() <= endDate.getTime()) ){
							PolicyInfoResVo inforesVo=new PolicyInfoResVo();
							String relatedPolicyNo=policyQueryService.getRelatedPolicyNo(vo.getPolicyNo());
							if(StringUtils.isNotBlank(relatedPolicyNo)){
								inforesVo.setGlpolicyNo(relatedPolicyNo);//关联保单号
							}
							inforesVo.setPolicyNo(vo.getPolicyNo());//保单号
							inforesVo.setLicenseNo(vo.getLicenseNo());//车牌号
							inforesVo.setRiskCode(vo.getRiskType());//险种
							inforesVo.setStartDate(DateFormatString(vo.getStartDate(),vo.getStartHour()));//起保日期
							inforesVo.setEndDate(DateFormatString(vo.getEndDate(),vo.getEndHour()));//终保日期
							infoVos.add(inforesVo);
						}
					}
				}
			}
			resBodyVo.setPolicyInfoListVo(infoVos);
			resHeadVo.setErrno("1");//响应成功
			resHeadVo.setResponseType("SELFCLAIM_001");//响应类型
			resVo.setHeadVo(resHeadVo);
			resVo.setBodyVo(resBodyVo);
			
		}catch(Exception e){
			resHeadVo.setResponseType("SELFCLAIM_001");//响应类型
			resHeadVo.setErrno("0");
			resHeadVo.setErrmsg(e.getMessage());//错误信息
			resVo.setHeadVo(resHeadVo);
			logger.info("自助理赔保单绑定接口错误信息------>"+e.getMessage());
			e.printStackTrace();
		}
		logger.info("自助理赔保单绑定接口返回的XML------>"+stream.toXML(resVo));
		return resVo;
	}
    
	public void init() {
		if(policyQueryService==null){
			policyQueryService = (PolicyQueryService)Springs.getBean(PolicyQueryService.class);
		}
	}
	
	/**
	 * 时间转换方法
	 * Date 类型转换 String类型
	 * @param strDate
	 * @param hour
	 * @return
	 * @throws ParseException
	 */
	private String DateFormatString(Date strDate,Long hour) throws ParseException{
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar car =Calendar.getInstance();
		car.setTime(strDate);
		if(hour != null){
			car.add(Calendar.HOUR,hour.intValue());// 24小时制
		}
		if(strDate!=null){
			  str=format.format(car.getTime());
		}
		return str;
	}
}
