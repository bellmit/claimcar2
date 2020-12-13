package ins.sino.claimcar.moblie.service;

import ins.framework.lang.Springs;
import ins.platform.common.util.CodeTranUtil;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.mobile.vo.EndorInfoListVo;
import ins.sino.claimcar.mobile.vo.EngageInfoListVo;
import ins.sino.claimcar.mobile.vo.KindInfoListVo;
import ins.sino.claimcar.mobile.vo.PolicyInfoListVo;
import ins.sino.claimcar.mobile.vo.PolicyInfoReqBodyVo;
import ins.sino.claimcar.mobile.vo.PolicyInfoReqVo;
import ins.sino.claimcar.mobile.vo.PolicyInfoResBodyVo;
import ins.sino.claimcar.mobile.vo.PolicyInfoResVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckRequest;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistHandlerService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLCengageVo;
import ins.sino.claimcar.regist.vo.PrppMainVo;
import ins.sino.claimcar.regist.vo.PrppheadVo;
import ins.sino.claimcar.regist.vo.PrpptextVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;


public class PolicyInfoServiceImpl implements ServiceInterface{
	

	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private PrpLCMainService prpLCMainService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	ClaimTaskService claimTaskService ;
	@Autowired
	RegistService registService;
	@Autowired
	RegistHandlerService registHandlerService;
	@Autowired
	CompensateTaskService compensateTaskService;
	private static Logger logger = LoggerFactory.getLogger(PolicyInfoServiceImpl.class);
	/* 
	 * 保单基本信息接口
	 * 
	 * zjd
	 * 
	 */
	
	
     private void init() {
    		if(registQueryService==null){
    			registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
    		}
    		if(prpLCMainService==null){
    			prpLCMainService = (PrpLCMainService)Springs.getBean(PrpLCMainService.class);
    		}
    		if(policyViewService==null){
    			policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
    		}
    		if(checkTaskService==null){
    			checkTaskService = (CheckTaskService)Springs.getBean(CheckTaskService.class);
    		}
    		if(claimTaskService==null){
    			claimTaskService = (ClaimTaskService)Springs.getBean(ClaimTaskService.class);
    		}
    		if(registService==null){
    			registService = (RegistService)Springs.getBean(RegistService.class);
    		}
    		if(registHandlerService==null){
    			registHandlerService = (RegistHandlerService)Springs.getBean(RegistHandlerService.class);
    		}
    		if(compensateTaskService==null){
    			compensateTaskService = (CompensateTaskService)Springs.getBean(CompensateTaskService.class);
    		}
		
	}

     public List<PolicyInfoListVo> setPolicyInfo(PolicyInfoReqBodyVo policyInfoReqBodyVo,List<PolicyInfoListVo> policyInfoListVo,String flags,PrpLCMainVo prpLCMainVo){

			PolicyInfoListVo policyInfoVo = new PolicyInfoListVo();
			List<KindInfoListVo> kindInfoListVo = new ArrayList<KindInfoListVo>();
			List<EngageInfoListVo> engageInfoListVo = new ArrayList<EngageInfoListVo>();
			List<EndorInfoListVo> endorInfoListVo = new ArrayList<EndorInfoListVo>();
			/*PrpLCMainVo prpLCMainVo = new PrpLCMainVo();
			if("B".equals(flags)){
				prpLCMainVo = PrpLCMainService.findPrpLCMain(policyInfoReqBodyVo.getRegistNo(), policyInfoReqBodyVo.getBusiPolicyNo());
			}else{
				prpLCMainVo = PrpLCMainService.findPrpLCMain(policyInfoReqBodyVo.getRegistNo(), policyInfoReqBodyVo.getPolicyNo());
			}*/
			//policyInfoResBodyVo.setRegistNo(prpLCMainVo.getRegistNo());
			policyInfoVo.setPolicyNo(prpLCMainVo.getPolicyNo());
			policyInfoVo.setLicenseNo(prpLCMainVo.getPrpCItemCars().get(0).getLicenseNo());
			policyInfoVo.setFrameNo(prpLCMainVo.getPrpCItemCars().get(0).getFrameNo());
			policyInfoVo.setEngineNo(prpLCMainVo.getPrpCItemCars().get(0).getEngineNo());
			policyInfoVo.setModelCode(prpLCMainVo.getPrpCItemCars().get(0).getBrandName());
			policyInfoVo.setInuredName(prpLCMainVo.getInsuredName());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String startDate = formatter.format(prpLCMainVo.getStartDate());
			String endDate = formatter.format(prpLCMainVo.getEndDate());
			policyInfoVo.setStartDate(startDate);
			policyInfoVo.setEndDate(endDate);
			if("B".equals(flags)){
				policyInfoVo.setClaimType("商业");
			}else{
				policyInfoVo.setClaimType("交强");
			}
			//policyInfoVo.setCustomerFlag("");客户标示
			policyInfoVo.setIsVip("0");
			policyInfoVo.setCompanyCode(prpLCMainVo.getComCode());
			String  companyName = CodeTranUtil.transCode("ComCodeFull",prpLCMainVo.getComCode());
			policyInfoVo.setCompanyName(companyName);
			if(prpLCMainVo.getSumAmount() != null){
				policyInfoVo.setTotalInsSum(prpLCMainVo.getSumAmount().toString());
			}else{
				policyInfoVo.setTotalInsSum("0");
			}
			prpLCMainVo.getInputDate();
			if(StringUtils.isNotBlank(prpLCMainVo.getBusinessChannel())){
				policyInfoVo.setChannelCode(prpLCMainVo.getBusinessChannel());
				if(StringUtils.isNotBlank(prpLCMainVo.getBusinessChannel())){
					String businessChannelName = CodeTranUtil.transCode("BusinessChannel",prpLCMainVo.getBusinessChannel());
					policyInfoVo.setChannelName(businessChannelName);
				}
			}else{
				policyInfoVo.setChannelCode("0");
				policyInfoVo.setChannelName("无");
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String operateDate = format.format(prpLCMainVo.getOperateDate());
			String inputdate = format.format(prpLCMainVo.getInputDate());
			policyInfoVo.setBillDate(inputdate);//出单日期
			policyInfoVo.setPolicyHoldDate(operateDate);//投保日期
			List<PrpLCItemCarVo> prpCItemCars = prpLCMainVo.getPrpCItemCars();
			PrpLCItemCarVo prpLCItemCarVo = prpCItemCars.get(0);//取标的
			policyInfoVo.setCarOwner(prpLCItemCarVo.getCarOwner());
			if(prpLCItemCarVo.getEnrollDate() != null){
				String enrollDate = format.format(prpLCItemCarVo.getEnrollDate());
				policyInfoVo.setEnrolDate(enrollDate);//初次登记年月
			}else{
				Calendar nowDate = Calendar.getInstance();
				policyInfoVo.setEnrolDate(String.valueOf(nowDate.get(Calendar.YEAR))+"-01-01");//初次登记年月
			}
			//policyInfoVo.setDamageCount(damageCount);历史出现次数
			//标的信息
			//List<PrpLCItemKindVo> cIemKindVoList = policyViewService.findItemKinds(policyInfoReqBodyVo.getRegistNo(), null);
			List<PrpLCItemKindVo> cIemKindVoList = new ArrayList<PrpLCItemKindVo>();
			List<PrpLCItemKindVo> cIemKindVoListM = new ArrayList<PrpLCItemKindVo>();
			if("B".equals(flags)){
				if(prpLCMainVo.getPrpCItemKinds()!=null && prpLCMainVo.getPrpCItemKinds().size()>0){
					cIemKindVoList = prpLCMainVo.getPrpCItemKinds();
					cIemKindVoListM= prpLCMainVo.getPrpCItemKinds();
				}
				for(PrpLCItemKindVo vo :cIemKindVoList){
					KindInfoListVo kindInfoVo = new KindInfoListVo();
//					if("1".equals(vo.getNoDutyFlag())){
//						kindInfoVo.setIsDeduct("0");//是
//					}else{
//						kindInfoVo.setIsDeduct("1");
//					}
					
					
					if(!"1101".equals(vo.getRiskCode())&&!CodeConstants.ISNEWCLAUSECODE_MAP.get(vo.getRiskCode())&&vo.getFlag().length() > 4
							&& String.valueOf(vo.getFlag().charAt(4)).equals("1")) {
						kindInfoVo.setIsDeduct("1");//是
						kindInfoVo.setDeduct(vo.getDeductible()!=null ? vo.getDeductible().toString():"0");
						kindInfoVo.setAmount(vo.getAmount()!=null ? vo.getAmount().toString():"");
						kindInfoVo.setKindName(vo.getKindName());
						kindInfoVo.setKindCode(vo.getKindCode());
						kindInfoListVo.add(kindInfoVo);
						
					}else{

					  if(StringUtils.isNotBlank(vo.getKindCode())&& !vo.getKindCode().substring(vo.getKindCode().
							length() - 1,vo.getKindCode().length()).equals("M")){
						String flag="0";
						for(PrpLCItemKindVo kindVo :cIemKindVoListM){
							if((vo.getKindCode()+"M").equals(kindVo.getKindCode())){
								flag="1";
							}
						}
						if("0".equals(flag)){
							
							kindInfoVo.setIsDeduct("0");
							kindInfoVo.setAmount(vo.getAmount()!=null ? vo.getAmount().toString():"");
							kindInfoVo.setKindName(vo.getKindName());
							kindInfoVo.setKindCode(vo.getKindCode());
							kindInfoListVo.add(kindInfoVo);
							
						}else{
							kindInfoVo.setAmount(vo.getAmount()!=null ? vo.getAmount().toString():"");
							kindInfoVo.setKindName(vo.getKindName());
							kindInfoVo.setKindCode(vo.getKindCode());
							kindInfoVo.setIsDeduct("1");
							kindInfoVo.setDeduct(vo.getDeductible()!=null ? vo.getDeductible().toString():"0");
							kindInfoListVo.add(kindInfoVo);
						     }
						}
						
					}
					
					
				}
			}else{
				if(prpLCMainVo.getPrpCItemKinds()!=null && prpLCMainVo.getPrpCItemKinds().size()>0){
					cIemKindVoList.add(prpLCMainVo.getPrpCItemKinds().get(0));
				}
				for(PrpLCItemKindVo vo :cIemKindVoList){
					KindInfoListVo kindInfoVo = new KindInfoListVo();
					kindInfoVo.setAmount(vo.getAmount()!=null ? vo.getAmount().toString():"");
					kindInfoVo.setKindName(vo.getKindName());
					kindInfoVo.setIsDeduct("0");//交强没有不计免赔
					kindInfoVo.setDeduct(vo.getDeductible()!=null ? vo.getDeductible().toString():"0");
					kindInfoVo.setKindCode(vo.getKindCode());
					kindInfoListVo.add(kindInfoVo);
				}
			}
			policyInfoVo.setKindList(kindInfoListVo);
			
			//商业险特别约定
			List <PrpLCengageVo> prpLCengageVo_B = null;
			//特别约定
			prpLCengageVo_B = prpLCMainVo.getPrpCengages();
			Map<String,String> prpLCengageVoMap_B = new TreeMap<String,String>();
			if(prpLCengageVo_B != null && prpLCengageVo_B.size() > 0){
				String name ="";
				for(PrpLCengageVo prpLCengageVo:prpLCengageVo_B){
                  String code = prpLCengageVo.getClauseCode();
                 // String name = prpLCengageVo.getClauseName();
                  String clauses = prpLCengageVo.getClauses();
                  String codeName = code;
                  
                  if("0".equals(prpLCengageVo.getTitleFlag())){
						 name = prpLCengageVo.getClauses();
                  	}
                  if(StringUtils.isNotBlank(name)){
                 	 codeName = codeName + "--" + name;
                  }
                  //EngageInfoListVo engageInfoVo = new EngageInfoListVo();
                  if(!"0".equals(prpLCengageVo.getTitleFlag())){
	                     if(prpLCengageVoMap_B.containsKey(codeName)){
	                    	 String clause = prpLCengageVoMap_B.get(codeName);
	                    	 prpLCengageVoMap_B.put(codeName,clause+clauses);
	                    	 /*engageInfoVo.setClauses(clause+clauses);
	                    	 engageInfoVo.setClausesName(codeName);
	                    	 engageInfoListVo.add(engageInfoVo);*/
	                     }else{
	                    	 prpLCengageVoMap_B.put(codeName,clauses);
	                    	 /*engageInfoVo.setClauses(clauses);
	                    	 engageInfoVo.setClausesName(codeName);
	                    	 engageInfoListVo.add(engageInfoVo);*/
	                     }
                  }
				}
			}
			for(Iterator iter = prpLCengageVoMap_B.entrySet().iterator();iter.hasNext();){
                Map.Entry entry = (Entry) iter.next();
                EngageInfoListVo engageInfoVo = new EngageInfoListVo();
                engageInfoVo.setClauses(entry.getValue()!=null ? entry.getValue().toString():"");
                engageInfoVo.setClausesName((String) entry.getKey());
                engageInfoListVo.add(engageInfoVo);
            }
			
			policyInfoVo.setEngageList(engageInfoListVo);
			
			//批单信息
			List<PrppheadVo> vos = new ArrayList<PrppheadVo>();
			vos = registQueryService.findByPolicyNo(prpLCMainVo.getPolicyNo());
			for(PrppheadVo vo:vos){
				EndorInfoListVo endorInfoVo = new EndorInfoListVo();
				endorInfoVo.setEndorNo(vo.getEndorseNo());
				String endorDate = formatter.format(vo.getEndorDate());
				String validDate = formatter.format(vo.getValidDate());
				endorInfoVo.setEndorDate(endorDate);
				endorInfoVo.setEndorVerifiedDate(validDate);
				List<PrpptextVo> prppTextVoList =registQueryService.findPrppTextByPolicyNo(vo.getEndorseNo());
				String endorContent = "";
				for(PrpptextVo prpptextVo :prppTextVoList){
					if(StringUtils.isNotBlank(prpptextVo.getEndorseText())){
						endorContent = endorContent+prpptextVo.getEndorseText();
					}
				}
				endorInfoVo.setEndorContent(endorContent.toString());
				List<PrppMainVo> prppMainVoList = registQueryService.findprppMainByPolicyNo(vo.getEndorseNo());
				if(prppMainVoList!=null && prppMainVoList.size()>0){
					endorInfoVo.setAmountChange(prppMainVoList.get(0).getChgAmount()!=null ? prppMainVoList.get(0).getChgAmount().toString():"");
				}
				//endorInfoVo.setAmountChange("");
				/*endorInfoVo.setRiskChange("");*/
				endorInfoListVo.add(endorInfoVo);
			}
			policyInfoVo.setEndorlist(endorInfoListVo);
			policyInfoListVo.add(policyInfoVo);
			
			return policyInfoListVo;
		
     }

     
     public Map<String, String> registRiskInfo(String registNo) throws ParseException {
 		
 		Map<String, String> registRiskInfoMap = new HashMap<String, String>();
 		if (!StringUtils.isEmpty(registNo)) {
 			registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
 		} 
 		return registRiskInfoMap;
 	}


	@Override
	public Object service(String arg0, Object arg1) {
		init();
		
		//返回的vo
		PolicyInfoResVo resVo = new PolicyInfoResVo();
		PolicyInfoResBodyVo policyInfoResBodyVo = new PolicyInfoResBodyVo();
		List<PolicyInfoListVo> policyInfoListVo = new ArrayList<PolicyInfoListVo>();
		
		MobileCheckResponseHead head = new MobileCheckResponseHead();
		MobileCheckHead mobileCheckHead = new MobileCheckHead();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		String registNo = "";
		try{
			
			stream.processAnnotations(PolicyInfoReqVo.class);
			//PolicyInfoReqVo policyInfoReqVo = (PolicyInfoReqVo)stream.fromXML(xml);
			PolicyInfoReqVo policyInfoReqVo = (PolicyInfoReqVo) arg1;
			stream.processAnnotations(MobileCheckRequest.class);
			String xml = stream.toXML(policyInfoReqVo);
			logger.info("移动查勘保单信息接收报文: \n"+xml);
			PolicyInfoReqBodyVo policyInfoReqBodyVo = policyInfoReqVo.getBody();
			mobileCheckHead = policyInfoReqVo.getHead();
			if (!"002".equals(mobileCheckHead.getRequestType())|| !"claim_user".equals(mobileCheckHead.getUser())|| !"claim_psd".equals(mobileCheckHead.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			if(StringUtils.isBlank(xml)){
				throw new IllegalArgumentException("报文为空");
			}
			if(!StringUtils.isNotBlank(mobileCheckHead.getRequestType())){
				throw new IllegalArgumentException("请求类型不能为空");
			}
			if(!StringUtils.isNotBlank(mobileCheckHead.getRequestType())){
				throw new IllegalArgumentException("用户名不能为空");		
			}
			if(!StringUtils.isNotBlank(mobileCheckHead.getRequestType())){
				throw new IllegalArgumentException("密码不能为空");
			}
			if(!StringUtils.isNotBlank(policyInfoReqBodyVo.getRegistNo())){
				throw new IllegalArgumentException("报案号不能为空");
			}
			registNo = policyInfoReqBodyVo.getRegistNo();
			/*if((!StringUtils.isNotBlank(policyInfoReqBodyVo.getBusiPolicyNo())) && (!StringUtils.isNotBlank(policyInfoReqBodyVo.getPolicyNo()))){
				throw new IllegalArgumentException("交强险保单号 和商业强险保单号不能同时为空");
			}*/
			Map<String, String> registRiskInfoMap = registRiskInfo(policyInfoReqBodyVo.getRegistNo());
			//返回的vo
			List<PrpLCMainVo> prpLCMainVoList= prpLCMainService.findPrpLCMainsByRegistNo(policyInfoReqBodyVo.getRegistNo());
			for(PrpLCMainVo vo : prpLCMainVoList){
				if("12".equals(vo.getRiskCode().substring(0, 2))){
					policyInfoListVo = this.setPolicyInfo(policyInfoReqBodyVo, policyInfoListVo, "B",vo);
				}else{
					policyInfoListVo = this.setPolicyInfo(policyInfoReqBodyVo, policyInfoListVo, "C",vo);
				}
			}
			/*if(StringUtils.isNotBlank(policyInfoReqBodyVo.getBusiPolicyNo())){
				policyInfoListVo = this.setPolicyInfo(policyInfoReqBodyVo, policyInfoListVo, "B");
			}
			if(StringUtils.isNotBlank(policyInfoReqBodyVo.getPolicyNo())){
				policyInfoListVo = this.setPolicyInfo(policyInfoReqBodyVo, policyInfoListVo, "C");
			}*/
			
			for(PolicyInfoListVo vo : policyInfoListVo){
				if("交强".equals(vo.getClaimType())){
					if(StringUtils.isNotEmpty(registRiskInfoMap.get("CI-No"))){
						if(StringUtils.isEmpty(registRiskInfoMap.get("CI-DangerNum"))){
							vo.setDamageCount("0");
			 			}else{
			 				vo.setDamageCount(registRiskInfoMap.get("CI-DangerNum"));
			 			}
					}
				}else{
					if(StringUtils.isNotEmpty(registRiskInfoMap.get("BI-No"))){
						if(StringUtils.isEmpty(registRiskInfoMap.get("BI-DangerNum"))){
							vo.setDamageCount("0");
			 			}else{
			 				vo.setDamageCount(registRiskInfoMap.get("BI-DangerNum"));
			 			}
					}
				}
			}
			//resVo.setBody(policyInfoResBodyVo);
			policyInfoResBodyVo.setRegistNo(policyInfoReqBodyVo.getRegistNo());
			policyInfoResBodyVo.setPolicyList(policyInfoListVo);
			resVo.setBody(policyInfoResBodyVo);
			head.setResponseType(mobileCheckHead.getRequestType());
			head.setResponseCode("YES");
			head.setResponseMessage("Success");
			resVo.setHead(head);
		}
		catch(Exception e){
			head.setResponseType(mobileCheckHead.getRequestType());
			head.setResponseCode("NO");
			head.setResponseMessage(e.getMessage());
			resVo.setHead(head);
			logger.info("移动查勘保单信息异常信息：\n");
			e.printStackTrace();
		}
		stream.processAnnotations(PolicyInfoResVo.class);
		logger.info("移动查勘保单信息返回报文=========：\n"+stream.toXML(resVo));
		//return stream.toXML(resVo);
		return resVo;
	}
}
