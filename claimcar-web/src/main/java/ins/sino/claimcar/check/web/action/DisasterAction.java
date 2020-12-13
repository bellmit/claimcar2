package ins.sino.claimcar.check.web.action;

import ins.framework.service.CodeTranService;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.service.facade.CodeDictService;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.check.vo.PrpLDisasterVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.commom.vo.AjaxDataVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author dengkk
 *
 */
@Controller
@RequestMapping(value = "/disaster")
public class DisasterAction {

	@Autowired
	private CheckHandleService checkHandleService;
	
	@Autowired
	private ClaimTaskService claimTaskService;
	
	@Autowired
	private CodeDictService codeDictService;
	
	@Autowired
	private RegistQueryService registQueryService;
	
	@Autowired
	private PolicyViewService policyViewService;
	
	@Autowired
	private CodeTranService codeTranService;
	

	/**
	 * 巨灾补录初始化
	 * @param claimNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/initDisaster.do")
	public ModelAndView init() {
		// 处理结果集
		ModelAndView mav = new ModelAndView();
		mav.setViewName("disaster/disasterEdit");
		return mav;
	}
	
	/**
	 * 巨灾补录查询
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchDisaster.do")
	@ResponseBody
	public AjaxResult search(@RequestParam(value = "claimNo") String claimNo){
		PrpLClaimVo prpLClaimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
		AjaxDataVo ajaxDataVo = new AjaxDataVo();
		if(prpLClaimVo == null){
			ajaxDataVo.setStatus("fail");
			ajaxDataVo.setData("未查到该案件，请核实立案号！");
			return returnResult(ajaxDataVo);
		}
		String registNo = prpLClaimVo.getRegistNo();
		PrpLDisasterVo disasterVo = checkHandleService.findDisasterVoByRegistNo(registNo);
		if(disasterVo != null){
			if(!StringUtils.isBlank(disasterVo.getDisasterCodeOne()) || !StringUtils.isBlank(disasterVo.getDisasterCodeTwo())
				||	!StringUtils.isBlank(disasterVo.getDisasterNameOne()) || !StringUtils.isBlank(disasterVo.getDisasterNameTwo())){
				ajaxDataVo.setStatus("fail");
				ajaxDataVo.setData("该案件已录入巨灾信息！");
				return returnResult(ajaxDataVo);
			}
			prpLClaimVo.setDisasterId(disasterVo.getId().toString());
		}
		ajaxDataVo.setStatus("success");
		ajaxDataVo.setData(prpLClaimVo);
		return returnResult(ajaxDataVo);
	}
	
	
	@RequestMapping(value = "/saveDisaster.do")
	@ResponseBody
	public AjaxResult save(@FormModel("disasterVo") PrpLDisasterVo disasterVo){
		disasterVo.setValidFlag(CodeConstants.ValidFlag.VALID);
		if(disasterVo.getId() == null){
			disasterVo.setCreateUser(WebUserUtils.getUserCode());
			disasterVo.setCreateTime(new Date());
		}
		disasterVo.setUpdateUser(WebUserUtils.getUserCode());
		disasterVo.setUpdateTime(new Date());
		checkHandleService.saveDisaster(disasterVo);
		AjaxDataVo ajaxDataVo = new AjaxDataVo();
		ajaxDataVo.setStatus("success");
		return returnResult(ajaxDataVo);
	}
	
	
	private AjaxResult returnResult(AjaxDataVo ajaxDataVo){
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(ajaxDataVo);
		return ajaxResult;
	}
	
	/**
	 * <pre>费用赔款信息初始化</pre>
	 * @param chargeCodes
	 * @param intermFlag
	 * @return
	 * @modified:
	 * ☆Luwei(2017年1月10日 上午10:39:53): <br>
	 */
	@RequestMapping(value="/initChargeType.ajax")
	public ModelAndView initChargeType(String chargeCodes,String intermFlag) { 
		List<String> chargeCodeList = new ArrayList<String>();
		if(chargeCodes!=null && !"".equals(chargeCodes)){
			String[] chargeArray = chargeCodes.split(",");
			//chargeCodeList = Arrays.asList(chargeArray);
			chargeCodeList = new ArrayList<String>(Arrays.asList(chargeArray));  
		}
		//都不能选择公估费
		chargeCodeList.add("13");
		
		ModelAndView mv = new ModelAndView();
		List<SysCodeDictVo> sysCodes = codeDictService.findCodeListByQuery("ChargeCode",chargeCodeList);
		
		mv.addObject("sysCodes",sysCodes);
		mv.setViewName("loss-common/ChargeDialog");
		return mv; 
	}
	
	@RequestMapping(value="/loadChargeTr.ajax")
	public ModelAndView loadChargeTr(int size,String[] chargeTypes,String registNo) { 
		ModelAndView mv = new ModelAndView();
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		String kindCode ="";
		Map<String,String> kindMap = new HashMap<String,String>(); 
		List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,null);
		for(PrpLCItemKindVo itemKind :itemKinds){
			if(!(itemKind.getKindCode().endsWith("M") || CodeConstants.NOSUBRISK_MAP.containsKey(itemKind.getKindCode()))){
				kindMap.put(itemKind.getKindCode(),itemKind.getKindName());
			}
		}
		//玻璃破碎、车身划痕、盗抢、自燃损失时，则对应默认为：玻璃破碎险、车身划痕险、盗抢险、自燃损失险（前提保单承保里有此险别）
		if("DM02".equals(registVo.getDamageCode()) && kindMap.containsKey("F")){//玻璃单独破碎
			kindCode ="F";
		}else if("DM03".equals(registVo.getDamageCode()) && kindMap.containsKey("L")){//车身划痕
			kindCode ="L";
		}else if("DM04".equals(registVo.getDamageCode()) && kindMap.containsKey("G")){//盗抢
			kindCode ="G";
		}else if("DM05".equals(registVo.getDamageCode()) && kindMap.containsKey("Z")){//自燃损失
			kindCode ="Z";
		}else if(kindMap.containsKey("A")){
			kindCode ="A";
		}else{
			kindCode ="";
		}
		
		List<PrpLDlossChargeVo> lossChargeVos = new ArrayList<PrpLDlossChargeVo>();
		if(chargeTypes!=null){
			for(String chargeCode : chargeTypes){
				PrpLDlossChargeVo lossChargeVo = new PrpLDlossChargeVo();
				lossChargeVo.setChargeCode(chargeCode);
				PrpLCheckVo checkVo = checkHandleService.queryPrpLCheckVo(registNo);
				lossChargeVo.setBusinessId(checkVo.getId());
				lossChargeVo.setRegistNo(registNo);
				lossChargeVo.setKindCode(kindCode);
				lossChargeVo.setBusinessType(FlowNode.Check.name());
				lossChargeVo.setChargeName(codeTranService.transCode("ChargeCode",chargeCode));
				
				lossChargeVos.add(lossChargeVo);
			}
		}
		
		mv.addObject("kindMap",kindMap);
		mv.addObject("lossChargeVos",lossChargeVos);
		mv.addObject("size",size);
		mv.setViewName("check/checkEdit/CheckFeePay_Tr");
		return mv; 
	}
}
