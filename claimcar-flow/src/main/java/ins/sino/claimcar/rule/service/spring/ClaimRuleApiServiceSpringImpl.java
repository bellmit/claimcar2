package ins.sino.claimcar.rule.service.spring;

import ins.sino.claimcar.rule.service.ClaimRuleApiService;
import ins.sino.claimcar.rule.utils.Drools5RuleAgent;
import ins.sino.claimcar.rule.vo.LossPropToVerifyRuleVo;
import ins.sino.claimcar.rule.vo.VerifyClaimCancelRuleVo;
import ins.sino.claimcar.rule.vo.VerifyClaimRuleVo;
import ins.sino.claimcar.rule.vo.VerifyLossRuleVo;
import ins.sino.claimcar.rule.vo.VerifyPersonRuleVo;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("claimRuleApiService")
public class ClaimRuleApiServiceSpringImpl implements ClaimRuleApiService {

	private static final String LossCarToVerifyRulePkg = "claimcar.verify.LossCarToVerify";
	private static final String LossCarToPriceRulePkg = "claimcar.price.LossCarToPrice";
	private static final String LossPersonToVerifyRulePkg = "claimcar.person.LossPersonToVerify";
	private static final String LossPersonToPriceRulePkg = "claimcar.person.LossPersonToPrice";
	private static final String LossPropToVerifyRulePkg = "claimcar.prop.LossPropToVerify";
	/** 理算提交核赔时调用的规则 */
	private static final String CompToVClaimRulePkg = "claimcar.vclaim.CompToVClaimRule";       
//	private static final String CompCIToVClaimRulePkg = "claimcar.vclaim.CompCIToVClaim";   
	
	private static final Map<String,String> comMap = new HashMap<String,String>();
	static{
		comMap.put("11","_gx");//广西
		comMap.put("13","_gz");//贵州
		comMap.put("30","_hb");//湖北
		comMap.put("20","_hain");//海南
		comMap.put("00","_sz");//深圳
		comMap.put("10","_gd");//广东
		comMap.put("21","_sc");//四川
		comMap.put("12","_yn");//云南
		comMap.put("22","_sh");//上海
		comMap.put("60","_jx");//江西
		comMap.put("50","_hn");//河南
		comMap.put("61","_sx");//陕西
		comMap.put("62","_sd");//山东
		comMap.put("63","_heb");//河北
	
	} 
	
	private static Logger logger = LoggerFactory.getLogger(ClaimRuleApiServiceSpringImpl.class);
	@Override
	public VerifyLossRuleVo lossCarToVerifyRule(VerifyLossRuleVo ruleVo) {
		String rulePath ="";
		String subComCode = ruleVo.getComCode().substring(0, 2);
		if("1".equals(ruleVo.getTopComp())){
			rulePath = LossCarToVerifyRulePkg +"_zgs";
		}else{
			if(comMap.containsKey(subComCode)){
				rulePath = LossCarToVerifyRulePkg +comMap.get(subComCode);
			}else{
				rulePath = LossCarToVerifyRulePkg;
			}
		}
		
		Drools5RuleAgent.executeRules(rulePath,ruleVo);
		Integer backLevel = ruleVo.getBackLevel();
		if(backLevel==null) throw new IllegalArgumentException(rulePath+"规则错误,未获得返回信息");
		return ruleVo;

	}

	@Override
	public VerifyLossRuleVo lossCarToPriceRule(VerifyLossRuleVo ruleVo) {
		String rulePath ="";
		String subComCode = ruleVo.getComCode().substring(0, 2);
		if("1".equals(ruleVo.getTopComp())){
			rulePath = LossCarToPriceRulePkg +"_zgs";
		}else{
			if(comMap.containsKey(subComCode)){
				rulePath = LossCarToPriceRulePkg +comMap.get(subComCode);
			}else{
				rulePath = LossCarToPriceRulePkg;
			}
		}
		
		Drools5RuleAgent.executeRules(rulePath,ruleVo);
		Integer backLevel = ruleVo.getBackLevel();
		if(backLevel==null) throw new IllegalArgumentException(rulePath+"规则错误,未获得返回信息");
		return ruleVo;
	}

	@Override
	public VerifyPersonRuleVo lossPersonToVerify(VerifyPersonRuleVo ruleVo) {
		String rulePath ="";
		String subComCode = ruleVo.getComCode().substring(0, 2);
		if("1".equals(ruleVo.getTopComp())){
			rulePath = LossPersonToVerifyRulePkg +"_zgs";
		}else{
			if(comMap.containsKey(subComCode)){
				rulePath = LossPersonToVerifyRulePkg +comMap.get(subComCode);
			}else{
				rulePath = LossPersonToVerifyRulePkg;
			}
		}
		Drools5RuleAgent.executeRules(rulePath,ruleVo);
		Integer backLevel = ruleVo.getBackLevel();
		if(backLevel==null) throw new IllegalArgumentException(rulePath+"规则错误,未获得返回信息");
		return ruleVo;
	}

	@Override
	public VerifyPersonRuleVo lossPersonToPrice(VerifyPersonRuleVo ruleVo) {
		String rulePath ="";
		String subComCode = ruleVo.getComCode().substring(0, 2);
		if("1".equals(ruleVo.getTopComp())){
			rulePath = LossPersonToPriceRulePkg +"_zgs";
		}else{
			if(comMap.containsKey(subComCode)){
				rulePath = LossPersonToPriceRulePkg +comMap.get(subComCode);
			}else{
				rulePath = LossPersonToPriceRulePkg;
			}
		}
		Drools5RuleAgent.executeRules(rulePath,ruleVo);
		Integer backLevel = ruleVo.getBackLevel();
		if(backLevel==null) throw new IllegalArgumentException(rulePath+"规则错误,未获得返回信息");
		return ruleVo;
	}

	@Override
	public VerifyClaimRuleVo compToVClaim(VerifyClaimRuleVo ruleVo) {
		String rulePath ="";
		String subComCode = ruleVo.getComCode().substring(0, 2);
		if("1".equals(ruleVo.getTopComp())){
			rulePath = CompToVClaimRulePkg +"_zgs";
		}else{
			if(comMap.containsKey(subComCode)){
				rulePath = CompToVClaimRulePkg +comMap.get(subComCode);
			}else{
				rulePath = CompToVClaimRulePkg;
			}
		}
		Drools5RuleAgent.executeRules(rulePath,ruleVo);
		Integer backLevel = ruleVo.getBackLevel();
		if(backLevel==null) throw new IllegalArgumentException(rulePath+"规则错误,未获得返回信息");
		return ruleVo;
	}
	
//	@Override
//	public VerifyClaimRuleVo compCIToVClaim(VerifyClaimRuleVo ruleVo){
//		Drools5RuleAgent.executeRules(CompCIToVClaimRulePkg,ruleVo);
//		Integer backLevel = ruleVo.getBackLevel();
//		if(backLevel==null) throw new IllegalArgumentException(CompCIToVClaimRulePkg+"规则错误,未获得返回信息");
//		return ruleVo;
//	}

	@Override
	public VerifyClaimCancelRuleVo claimCanCelToPriceRule(VerifyClaimCancelRuleVo ruleVo) {
		//Drools5RuleAgent.executeRules(CompToVClaimRulePkg,ruleVo);
	/*	if(ruleVo.getSumLossFee()<=1000){
			ruleVo.setBackLevel(1);
		}else if(ruleVo.getSumLossFee()>1000){
			ruleVo.setBackLevel(2);
		}else{
			ruleVo.setBackLevel(3);
		}*/
		ruleVo.setBackLevel(3);
		Integer backLevel = ruleVo.getBackLevel();
		if(backLevel==null) throw new IllegalArgumentException(CompToVClaimRulePkg+"规则错误,未获得返回信息");
		return ruleVo;
	}
	
	@Override
	public LossPropToVerifyRuleVo lossPropToVerifyRule(LossPropToVerifyRuleVo ruleVo){
		String rulePath ="";
		String subComCode = ruleVo.getComCode().substring(0, 2);
		if("1".equals(ruleVo.getTopComp())){
			rulePath = LossPropToVerifyRulePkg +"_zgs";
		}else{
			if(comMap.containsKey(subComCode)){
				rulePath = LossPropToVerifyRulePkg +comMap.get(subComCode);
			}else{
				rulePath = LossPropToVerifyRulePkg;
			}
		}
		
		Drools5RuleAgent.executeRules(rulePath,ruleVo);
		Integer backLevel = ruleVo.getBackLevel();
		if(backLevel==null) throw new IllegalArgumentException(rulePath+"规则错误,未获得返回信息");
		return ruleVo;
	}
}
