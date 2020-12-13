package ins.sino.claimcar.regist.web.action;

import ins.framework.common.DateTime;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DataUtils;
import ins.platform.utils.ObjectUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.regist.service.PolicyQueryService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PolicyEndorseInfoVo;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLCengageVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrppheadVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.encoding.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author Dengkk
 *
 */
@Controller
@RequestMapping("/policyView/")
public class PolicyViewAction {
	
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private PolicyQueryService policyQueryService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	CodeTranService codeTranService;

	/**
	 * 查看保单信息
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/policyView.do", method = RequestMethod.GET)
	public ModelAndView policyView(String policyNo,String registNo) throws Exception {
		List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
		List<PrpLCInsuredVo> prpLCInsuredVoList = new ArrayList<PrpLCInsuredVo>();
		if(StringUtils.isBlank(registNo)){
			List<String> policyNoList = new ArrayList<String>();
			policyNoList.add(policyNo);
			prpLCMainVoList = policyQueryService.findPrpcMainByPolicyNos(policyNoList);
			prpLCInsuredVoList = registQueryService.findPrpCinsuredNatureByPolicyNo(policyNo);
		}else{
			prpLCMainVoList = policyViewService.getPolicyAllInfo(registNo);
			if(prpLCMainVoList != null && prpLCMainVoList.size() == 1 ){
				prpLCInsuredVoList = registQueryService.findPrpCinsuredNatureByPolicyNo(prpLCMainVoList.get(0).getPolicyNo());
			}else{
				for(PrpLCMainVo vo : prpLCMainVoList){
					if(!"1101".equals(vo.getRiskCode())){
						prpLCInsuredVoList = registQueryService.findPrpCinsuredNatureByPolicyNo(vo.getPolicyNo());
					}
				}
			}
		}
		//主信息表
		PrpLCMainVo prpLCMain = null;
		//商业险保单信息
		PrpLCMainVo prpLCMainB = null;
		//交强险保单信息
		PrpLCMainVo prpLCMainC = null;
		//投保人信息
		PrpLCInsuredVo prpLCICustomVo = null;
	    //被保险人信息
		PrpLCInsuredVo prpLCInsuredVo = null;
		//车辆信息
		PrpLCItemCarVo prpLCItemCarVo = null;
		//交强险别信息
		PrpLCItemKindVo prpLCItemKindVo_C = null;
		//商业险别信息
		List <PrpLCItemKindVo> prpLCItemKindVo_B = null;
		//交强险特别约定
		List <PrpLCengageVo> prpLCengageVo_C = null;
		Map<String,String> prpLCengageVoMap_C = new TreeMap<String,String>();
		//商业险特别约定
		List <PrpLCengageVo> prpLCengageVo_B = null;
		Map<String,String> prpLCengageVoMap_B = new TreeMap<String,String>();
		//交强险上期保单
		String define1 = "";
		//商业险上期保单
		String define2 = "";
		//交强折扣保费合计
		BigDecimal sumDisCountPremium = new BigDecimal(0);
		//保费
		BigDecimal sumItemPremium = new BigDecimal(0);
		//保额合计
		BigDecimal sumItemAmount = new BigDecimal(0);
		//基准保费合计
		BigDecimal sumBenchmarkPremium = new BigDecimal(0);
		//折扣保费合计
		BigDecimal sumDisCountPremiumBI = new BigDecimal(0);
		ModelAndView mav = new ModelAndView();
		//返回有数据
		boolean isDQZ = false;// 交强险代码 判断是否是交强险
		boolean isDAA = false;// 交强险代码 判断是否是交强险
		if(prpLCMainVoList.size() > 0){
			for(PrpLCMainVo prpLCMaintmp:prpLCMainVoList){
				String riskCode = prpLCMaintmp.getRiskCode();
				if("1101".equals(riskCode)){// 交强想
					prpLCMainC = prpLCMaintmp;
					isDQZ = true;
					//设置日期
					prpLCMainC = this.setPrpCmainDate(prpLCMainC.getPolicyNo(), prpLCMainC);
				}else{
					prpLCMainB = prpLCMaintmp;
					isDAA = true;
					//设置日期
					prpLCMainB = this.setPrpCmainDate(prpLCMainB.getPolicyNo(), prpLCMainB);
				}
				//取得相关保单的车辆信息
				if(prpLCMaintmp.getPrpCItemCars().size() > 0){
					prpLCItemCarVo = prpLCMaintmp.getPrpCItemCars().get(0);
				};
			};
			
			//初始化主信息
			if(isDQZ){
				prpLCMain = prpLCMainC;
			}else if(isDAA){
				prpLCMain = prpLCMainB;
			}
			//险别信息
			if(isDQZ){
				//交强险险别
				prpLCItemKindVo_C = prpLCMainC.getPrpCItemKinds().get(0);
				//特别约定
				prpLCengageVo_C = prpLCMainC.getPrpCengages();
				if(prpLCengageVo_C != null && prpLCengageVo_C.size() > 0){
					String name ="";
					/*for(PrpLCengageVo prpLCengageVo:prpLCengageVo_C){
						if("0".equals(prpLCengageVo.getTitleFlag())){
							 name = prpLCengageVo.getClauses();
						}
					}*/
					for(PrpLCengageVo prpLCengageVo:prpLCengageVo_C){
						if("0".equals(prpLCengageVo.getTitleFlag())){
							 name = prpLCengageVo.getClauses();
						}
	                     String code = prpLCengageVo.getClauseCode();
	                    // String name = prpLCengageVo.getClauseName();
	                     String clauses = prpLCengageVo.getClauses();
	                     String codeName = code+"--"+name;
	                     if(!"0".equals(prpLCengageVo.getTitleFlag())){
		                     if(prpLCengageVoMap_C.containsKey(codeName)){
		                    	 String clause = prpLCengageVoMap_C.get(codeName);
		                    	 prpLCengageVoMap_C.put(codeName,clause+clauses);
		                     }else{
		                    	 prpLCengageVoMap_C.put(codeName,clauses);
		                     }
	                     }
					}
				}
			}
			
			if(isDAA){
				//商业险险别
				prpLCItemKindVo_B = prpLCMainB.getPrpCItemKinds();
				if(prpLCItemKindVo_B!=null && prpLCItemKindVo_B.size()>0) {
					for(PrpLCItemKindVo kindVo:prpLCItemKindVo_B) {
						if(CodeConstants.KINDCODE.KINDCODE_B.equals(kindVo.getKindCode())) {
							if(StringUtils.isNotBlank(registNo)) {
								PrpLRegistVo prpLRegistVo=registQueryService.findByRegistNo(registNo);
								if(prpLRegistVo!=null && "1".equals(prpLRegistVo.getWeekDayriskFlag()) ) {
									kindVo.setAmount(kindVo.getAmount()!=null?kindVo.getAmount().divide(new BigDecimal(2)):new BigDecimal(0));
								}
							}
							
						}
					}
				}
				//特别约定
				prpLCengageVo_B = prpLCMainB.getPrpCengages();
				if(prpLCengageVo_B != null && prpLCengageVo_B.size() > 0){
					String name ="";
					/*for(PrpLCengageVo prpLCengageVo:prpLCengageVo_B){
						if("0".equals(prpLCengageVo.getTitleFlag())){
							 name = prpLCengageVo.getClauses();
						}
					}*/
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
	                     if(!"0".equals(prpLCengageVo.getTitleFlag())){
		                     if(prpLCengageVoMap_B.containsKey(codeName)){
		                    	 String clause = prpLCengageVoMap_B.get(codeName);
		                    	 prpLCengageVoMap_B.put(codeName,clause+clauses);
		                     }else{
		                    	 prpLCengageVoMap_B.put(codeName,clauses);
		                     }
	                     }
					}
				}
			}
			//投保人信息
			for(int i = 0; i<prpLCMain.getPrpCInsureds().size(); i++ ){
				PrpLCInsuredVo prpLCICustomVoTemp = new PrpLCInsuredVo();
				prpLCICustomVoTemp = prpLCMain.getPrpCInsureds().get(i);
				//关系人标志 1（被保险人）2（投保人）3（担保人）4（技术转让方）5（购车人）6（购车人单位）7（经销商）8（连带被保险人）9（受益人）A（投保人关联单位）B（质权人）
				String insuredFlag = prpLCICustomVoTemp.getInsuredFlag();
				 if("1".equals(insuredFlag)){
					 prpLCInsuredVo = prpLCICustomVoTemp;
					 if(prpLCInsuredVoList != null && prpLCInsuredVoList.size() > 0){
						 for(PrpLCInsuredVo vo:prpLCInsuredVoList){
							 if(vo.getAge() != null && "1".equals(vo.getInsuredFlag())){
								 prpLCInsuredVo.setAge(vo.getAge());
								 prpLCInsuredVo.setSex(vo.getSex());
							 }
						 }
					 }
				 }else if("2".equals(insuredFlag)){
					 prpLCICustomVo = prpLCICustomVoTemp;
					 if(prpLCInsuredVoList != null && prpLCInsuredVoList.size() > 0){
						 for(PrpLCInsuredVo vo:prpLCInsuredVoList){
							 if(vo.getAge() != null && "2".equals(vo.getInsuredFlag())){
								 prpLCICustomVo.setAge(vo.getAge());
								 prpLCICustomVo.setSex(vo.getSex());
							 }
						 }
					 }
				 }
			}
		//计算 开始
	    if( !ObjectUtils.isEmpty(prpLCItemKindVo_C)){
			BigDecimal basePremium = prpLCItemKindVo_C.getBasePremium();
			BigDecimal shortRate = prpLCItemKindVo_C.getShortRate();
			BigDecimal discount = prpLCItemKindVo_C.getDiscount();
			
			sumDisCountPremium = basePremium.multiply(shortRate)
					.divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP)
					.multiply(new BigDecimal(1).subtract(discount));
			prpLCMainC.setSumDisCountPremium(sumDisCountPremium.doubleValue());
		}
		
		
		if( !ObjectUtils.isEmpty(prpLCItemKindVo_B)){
			for(int i = 0; i<prpLCItemKindVo_B.size(); i++ ){
				PrpLCItemKindVo prpLCitemKind = prpLCItemKindVo_B.get(i);
				if("Y".equals(prpLCitemKind.getCalculateFlag())){
					sumItemAmount = sumItemAmount.add(prpLCitemKind.getAmount());
				}
				sumBenchmarkPremium = sumBenchmarkPremium.add(prpLCitemKind.getBenchMarkPremium());
				sumItemPremium = sumItemPremium.add(prpLCitemKind.getPremium());
			}
			BigDecimal shortRate = prpLCItemKindVo_B.get(0).getShortRate();
			sumDisCountPremiumBI = sumBenchmarkPremium.multiply(shortRate)
			.divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP).subtract(sumItemPremium);
			prpLCMainB.setSumItemAmount(sumItemAmount.doubleValue());
			prpLCMainB.setSumDisCountPremiumBI(sumDisCountPremiumBI.doubleValue());
			prpLCMainB.setSumBenchmarkPremium(sumBenchmarkPremium.doubleValue());
			prpLCMainB.setSumItemPremium(sumItemPremium.doubleValue());
		}
		//计算结束
		//返回处理结果
		ConfigUtil configUtil = new ConfigUtil(); 
		PrpLConfigValueVo configValueVo = configUtil.findConfigByCode(CodeConstants.STARFLAG);

		if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){
			prpLCICustomVo.setInsuredCode(DataUtils .replacePrivacy(prpLCICustomVo.getInsuredCode()));
			prpLCICustomVo.setPhoneNumber(DataUtils.replacePrivacy(prpLCICustomVo.getPhoneNumber()));
			prpLCICustomVo.setIdentifyNumber(DataUtils.replacePrivacy(prpLCICustomVo.getIdentifyNumber()));
			prpLCICustomVo.setMobile(DataUtils.replacePrivacy(prpLCICustomVo.getMobile()));
		}
		
		if(prpLCMain!=null){
			String agentName="";
		if(StringUtils.isNotBlank(prpLCMain.getAgentCode())){
				agentName=codeTranService.transCode("AgentCode",prpLCMain.getAgentCode());
				prpLCMain.setAgentName(agentName);
			
			}
		}
		
		//新条款保单增加“2014版示范条款”;
		String policyNoNewSign="";
		if(prpLCMainB!=null){
			if(StringUtils.isNotBlank(prpLCMainB.getPolicyNo())){
		         String	strSign=prpLCMainB.getPolicyNo().substring(11,15);
		         if("1206".equals(strSign) || "1207".equals(strSign) || "1208".equals(strSign)){
			       policyNoNewSign="2014版示范条款";
		         }else if ("1230".equals(strSign) || "1231".equals(strSign) || "1232".equals(strSign) || "1233".equals(strSign)){
					 policyNoNewSign="2020版示范条款";
				 }
		      }
		}
		mav.addObject("policyNoNewSign",policyNoNewSign);
		mav.addObject("prpLCMain",prpLCMain);
		//开关需到2级机构
		mav.addObject("prpLCMainBRepairPhone", "0");
		if(prpLCMainB != null){
			String comCode = "";
			if(prpLCMainB.getComCode().startsWith("0002")){
				comCode = "0002";
			}else{
				comCode = prpLCMainB.getComCode().substring(0, 2);
			}
			PrpLConfigValueVo configValueRepairPhoneVo = ConfigUtil.findConfigByCode(CodeConstants.REPAIRPHONE,comCode);
			if(configValueRepairPhoneVo != null && "1".equals(configValueRepairPhoneVo.getConfigValue())){
				mav.addObject("prpLCMainBRepairPhone", "1");
		    }
		}
		mav.addObject("prpLCMainCRepairPhone", "0");
		if(prpLCMainC != null){
			String comCode = "";
			if(prpLCMainC.getComCode().startsWith("0002")){
				comCode = "0002";
			}else{
				comCode = prpLCMainC.getComCode().substring(0, 2);
			}
			PrpLConfigValueVo configValueRepairPhoneVo = ConfigUtil.findConfigByCode(CodeConstants.REPAIRPHONE,comCode);
			if(configValueRepairPhoneVo != null && "1".equals(configValueRepairPhoneVo.getConfigValue())){
				mav.addObject("prpLCMainCRepairPhone", "1");
		    }
		}
/**************************************************投保人信息脱敏************************************************/
		if(prpLCICustomVo.getIdentifyNumber() != null && !prpLCICustomVo.getIdentifyNumber().isEmpty()){
			String identifyNumber = prpLCICustomVo.getIdentifyNumber().substring(0, 4) + "**********" + prpLCICustomVo.getIdentifyNumber().substring(prpLCICustomVo.getIdentifyNumber().length() - 4);
			prpLCICustomVo.setIdentifyNumber(identifyNumber);
		}
		//投保人姓名不做脱敏处理
		if(prpLCICustomVo.getInsuredName() != null && !prpLCICustomVo.getInsuredName().isEmpty()){
			String insuredName = prpLCICustomVo.getInsuredName();
			prpLCICustomVo.setInsuredName(insuredName);
		}else{
			prpLCICustomVo.setInsuredName("");
		}
		if(prpLCICustomVo.getInsuredAddress() != null && !prpLCICustomVo.getInsuredAddress().isEmpty()){
			String insuredAddress = prpLCICustomVo.getInsuredAddress().substring(0, 2) + "**********" + prpLCICustomVo.getInsuredAddress().substring(prpLCICustomVo.getInsuredAddress().length()-2);
			prpLCICustomVo.setInsuredAddress(insuredAddress);
		}
		if(prpLCICustomVo.getEmail() != null && !prpLCICustomVo.getEmail().isEmpty()){
			String email = prpLCICustomVo.getEmail().substring(0, 2) + "**********" + prpLCICustomVo.getEmail().substring(prpLCICustomVo.getEmail().length() - 2);
			prpLCICustomVo.setEmail(email);
		}
		if(prpLCICustomVo.getLinkerName() != null && !prpLCICustomVo.getLinkerName().isEmpty()){
			String linkerName = prpLCICustomVo.getLinkerName().substring(0, 1) + "**";
			prpLCICustomVo.setLinkerName(linkerName);
		}
		if(prpLCICustomVo.getPhoneNumber() != null && !prpLCICustomVo.getPhoneNumber().isEmpty()){
			String phoneNumber = prpLCICustomVo.getPhoneNumber().substring(0, 3) + "****" + prpLCICustomVo.getPhoneNumber().substring(prpLCICustomVo.getPhoneNumber().length() - 3);
			prpLCICustomVo.setPhoneNumber(phoneNumber);
		}
		if(prpLCICustomVo.getMobile() != null && !prpLCICustomVo.getMobile().isEmpty()){
			String mobile = prpLCICustomVo.getMobile().substring(0, 3) + "*****" + prpLCICustomVo.getMobile().substring(prpLCICustomVo.getMobile().length() - 3);
			prpLCICustomVo.setMobile(mobile);
		}
/*******************************************被保险人信息脱敏**********************************************/
		if(prpLCInsuredVo.getIdentifyNumber() != null && !prpLCInsuredVo.getIdentifyNumber().isEmpty()){
			String identifyNumber = prpLCInsuredVo.getIdentifyNumber().substring(0, 4) + "**********" + prpLCInsuredVo.getIdentifyNumber().substring(prpLCInsuredVo.getIdentifyNumber().length() - 4);
			prpLCInsuredVo.setIdentifyNumber(identifyNumber);
		}
		//被保险人姓名不做脱敏处理
		if(prpLCInsuredVo.getInsuredName() != null && !prpLCInsuredVo.getInsuredName().isEmpty()){
			String insuredName = prpLCInsuredVo.getInsuredName();
			prpLCInsuredVo.setInsuredName(insuredName);
		}else{
			prpLCInsuredVo.setInsuredName("");
		}
		if(prpLCInsuredVo.getInsuredAddress() != null && !prpLCInsuredVo.getInsuredAddress().isEmpty()){
			String insuredAddress = prpLCInsuredVo.getInsuredAddress().substring(0, 2) + "**********" + prpLCInsuredVo.getInsuredAddress().substring(prpLCInsuredVo.getInsuredAddress().length()-2);
			prpLCInsuredVo.setInsuredAddress(insuredAddress);
		}
		if(prpLCInsuredVo.getEmail() != null && !prpLCInsuredVo.getEmail().isEmpty()){
			String email = prpLCInsuredVo.getEmail().substring(0, 2) + "**********" + prpLCInsuredVo.getEmail().substring(prpLCInsuredVo.getEmail().length() - 2);
			prpLCInsuredVo.setEmail(email);
		}
		if(prpLCInsuredVo.getLinkerName() != null && !prpLCInsuredVo.getLinkerName().isEmpty()){
			String linkerName = prpLCInsuredVo.getLinkerName().substring(0, 1) + "**";
			prpLCInsuredVo.setLinkerName(linkerName);
		}
		if(prpLCInsuredVo.getPhoneNumber() != null && !prpLCInsuredVo.getPhoneNumber().isEmpty()){
			String phoneNumber = prpLCInsuredVo.getPhoneNumber().substring(0, 3) + "****" + prpLCInsuredVo.getPhoneNumber().substring(prpLCInsuredVo.getPhoneNumber().length() - 3);
			prpLCInsuredVo.setPhoneNumber(phoneNumber);
		}
		if(prpLCInsuredVo.getMobile() != null && !prpLCInsuredVo.getMobile().isEmpty()){
			String mobile = prpLCInsuredVo.getMobile().substring(0, 3) + "*****" + prpLCInsuredVo.getMobile().substring(prpLCInsuredVo.getMobile().length() - 3);
			prpLCInsuredVo.setMobile(mobile);
		}
		//define3为0的时候则为旧单，为1的时候则是新单
		String define3 = "0";//判断商业新旧单
        if(prpLCMainB!=null && prpLCMainB.getRiskCode() != null){
            if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(prpLCMainB.getRiskCode()) != null &&
                    CodeConstants.ISNEWCLAUSECODE2020_MAP.get(prpLCMainB.getRiskCode())){
                define3 = "1";
            }
        }
		mav.addObject("prpLCMainB", prpLCMainB);
		mav.addObject("prpLCMainC", prpLCMainC);
		mav.addObject("prpLCICustomVo", prpLCICustomVo);
		mav.addObject("prpLCInsuredVo", prpLCInsuredVo);
		mav.addObject("prpLCItemCarVo", prpLCItemCarVo);
		mav.addObject("prpLCItemKindVo_B", prpLCItemKindVo_B);
		mav.addObject("prpLCItemKindVo_C", prpLCItemKindVo_C);
		mav.addObject("prpLCengageVoMap_C", prpLCengageVoMap_C);
		mav.addObject("prpLCengageVoMap_B", prpLCengageVoMap_B);
		mav.addObject("define1", define1);
		mav.addObject("define2", define2);
		mav.addObject("define3", define3);
		mav.setViewName("policy/policyView/policyView");
	    }else{
	    	//TODO 跳转到公共无数据页面
			mav.setViewName("policy/policyView/policyView");
	    }
		return mav;
	}
	
	/**
	 * <pre>出险保单信息公共按钮</pre>
	 * @param request
	 * @return
	 * @modified:
	 * ☆Luwei(2016年9月6日 下午5:52:22): <br>
	 */
	@RequestMapping(value = "/viewPolicyInfo.do")
	public ModelAndView viewPolicyInfo(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String registNo = request.getParameter("registNo");
		List<PrpLCMainVo> policyInfos = policyViewService.getPolicyAllInfo(registNo);
		mav.addObject("policyInfos", policyInfos);
		mav.setViewName("policy/policyView/viewPolicyInfo");
		return mav;
	}
	
	/**
	 * <pre>保单批改纪录公共按钮</pre>
	 * @param request
	 * @return
	 * @modified:
	 * ☆Luwei(2016年9月6日 下午5:52:22): <br>
	 */
	@RequestMapping(value = "/viewEndorseInfo.do")
	public ModelAndView viewEndorseInfo(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String registNo = request.getParameter("registNo");
		List<PolicyEndorseInfoVo> infoVoList = policyViewService.findPolicyEndorseInfo(registNo);
		mav.addObject("infoVoList", infoVoList);
		String userCode = WebUserUtils.getUserCode();
		String comCode = WebUserUtils.getComCode();
		String hskey = new String(Base64.encode(
		(userCode+","+comCode+","+DateTime.current().toString(DateTime.YEAR_TO_DAY).replace("-","")).getBytes()));
		String prpUrl = SpringProperties.getProperty("PRPCAR_URL");
		mav.addObject("prpUrl",prpUrl);
		mav.addObject("hskey", hskey);
		mav.setViewName("policy/policyView/viewEndorseInfo");
		return mav;
	}
	public PrpLCMainVo setPrpCmainDate(String policyNo,PrpLCMainVo prpLCMainVo) {
		List<PrppheadVo> vos = registQueryService.findByPolicyNo(policyNo);
		 String endDateFlags = "0";
		 for(PrppheadVo vo : vos){
			 if(vo.getEndorType().length() > 2){
				 String a[] = vo.getEndorType().split(",");
					for(int j=0; j < a.length; j++){
						if("37".equals(a[j])){
							 prpLCMainVo.setEndHour(vo.getValidHour());
							 endDateFlags = "1";
							 break;
						}
					}
			 }else{
				if("37".equals(vo.getEndorType())){
					prpLCMainVo.setEndHour(vo.getValidHour());
					 endDateFlags = "1";
					 break;
				}
			 }
			 if("1".equals(endDateFlags)){
				 break;
			 }
		 }
		return prpLCMainVo;
	}
	/**
	 * 查看保单信息
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/policyViewToCB.do", method = RequestMethod.GET)
	public String policyViewToCB(String policyNo,String registNo) throws Exception {
		
		String strUserCode = WebUserUtils.getUserCode();
		String strComCode = WebUserUtils.getComCode();
	/*	String strComCode = "00000000";
		String strUserCode = "0000000000";*/
		String url =SpringProperties.getProperty("PRPCAR_URL");
			Base64 base64 = new Base64();
			String hskey = new String(base64.encode((strUserCode+","+strComCode+","+ DateTime.current().toString(DateTime.YEAR_TO_DAY).replace("-","")).getBytes()));
				url += "/prpc/view?hskey="+hskey+"&policyNo="+policyNo;
		return "redirect:"+url;
	}
}
