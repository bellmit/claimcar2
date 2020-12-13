package ins.sino.claimcar.claim.web.action;

import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DataUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.check.vo.PrpLDisasterVo;
import ins.sino.claimcar.claim.service.ClaimKindHisService;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
/*import ins.sino.claimcar.claim.service.ConfigService;*/
import ins.sino.claimcar.claim.vo.PrpLClaimKindFeeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindHisVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLCengageVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/claim")
public class ClaimAction {
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private CheckTaskService checkTaskService;
	/*
	 * @Autowired private ConfigService prpLDLimitService;
	 */
	@Autowired
	private ClaimService claimService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private ClaimTextService claimTextService;
	@Autowired
	private ClaimKindHisService claimKindHisService;

	/**
	 * 此方法供从工作流点击进入立案页面使用
	 * 
	 * <pre></pre>
	 * @param flowTaskId
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月12日 下午5:35:13): <br>
	 */
	@RequestMapping("/claimEdit.do")
	public ModelAndView claimEdit(Double flowTaskId) {
		List<PrpLClaimKindHisVo> prpLClaimKindHisList1 = new ArrayList<PrpLClaimKindHisVo>();
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);
		System.out.println(flowTaskId);
		PrpLClaimVo prplclaimVo = claimService.findByClaimNo(taskVo.getClaimNo());
		// 0-立案注销 2-立案拒赔 1-正常数据
		String validFlag = "1";

		List<PrpLCMainVo> prpLCMainList = policyViewService.getPolicyAllInfo(prplclaimVo.getRegistNo());
		PrpLCMainVo prpLCMainVo = new PrpLCMainVo();
		List<PrpLClaimKindVo> prpLClaimKindVos = prplclaimVo.getPrpLClaimKinds();
		List<PrpLClaimKindHisVo> prpLClaimKindHisVos = claimKindHisService.findAllKindHisVoList(taskVo.getClaimNo());
		// 交强险全部显示到页面，商业险去掉不计免赔险
		if (prpLClaimKindHisVos != null && prpLClaimKindHisVos.size() > 0) {
			for (int j = 0; j < prpLClaimKindHisVos.size(); j++) {
		// 如果估计赔款 及其变化量字段都是0，则不展示
				if(adjustClaimHisShow(prpLClaimKindHisVos.get(j))){
					if (prpLClaimKindHisVos.get(j).getRiskCode().equals("1101")) {
						prpLClaimKindHisList1.add(prpLClaimKindHisVos.get(j));

					} else {
						if (!CodeConstants.NOSUBRISK_MAP.containsKey(prpLClaimKindHisVos.get(j).getKindCode())
								&& !prpLClaimKindHisVos.get(j).getKindCode().endsWith("M")) {
							prpLClaimKindHisList1.add(prpLClaimKindHisVos.get(j));

						}
					}
				}
			}
		}

		//实现立案轨迹根据调整次数折叠显示，根据更新次数降序输出，相同次数的调整记录放到一个Map对象中
		Map<Integer, List<PrpLClaimKindHisVo>> prpLClaimKindHisMap = new TreeMap<Integer, List<PrpLClaimKindHisVo>>(
                new Comparator<Integer>() {
                    public int compare(Integer obj1, Integer obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
		
		for(PrpLClaimKindHisVo claimKindHisVo:prpLClaimKindHisList1){
			if(prpLClaimKindHisMap.containsKey(claimKindHisVo.getEstiTimes())){
				prpLClaimKindHisMap.get(claimKindHisVo.getEstiTimes()).add(claimKindHisVo);
			}else{
				List<PrpLClaimKindHisVo> claimKindHisVoListForMap = new ArrayList<PrpLClaimKindHisVo>();
				claimKindHisVoListForMap.add(claimKindHisVo);
				prpLClaimKindHisMap.put(claimKindHisVo.getEstiTimes(),claimKindHisVoListForMap);
			}
		}
		
		List<PrpLClaimKindFeeVo> prpLClaimKindFeeVos = prplclaimVo
				.getPrpLClaimKindFees();
		PrpLCheckVo prpLCheckVo = checkTaskService
				.findCheckVoByRegistNo(prplclaimVo.getRegistNo());
		PrpLRegistVo registVo = registQueryService.findByRegistNo(prplclaimVo
				.getRegistNo());
		PrpLRegistExtVo registExt = registVo.getPrpLRegistExt();
		// 巨灾信息查询
		PrpLDisasterVo disasterVo = checkTaskService.findDisasterVoByRegistNo(prplclaimVo.getRegistNo());
		
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(
				prplclaimVo.getRegistNo(), 1);
		Map<String, String> map = new HashMap<String, String>();
		map.put("bussNo", taskVo.getClaimNo());
		List<PrpLClaimTextVo> prpLClaimTextVoList = claimTextService.findClaimTextList(map);

		for (PrpLCMainVo cMain : prpLCMainList) {
			if (cMain.getRiskCode().equals(prplclaimVo.getRiskCode())) {
				prpLCMainVo = cMain;
			}
		}
		/*
		 * for(int i = 0;i < prpLCMainList.size();i++){ if
		 * (prplclaimVo.getRiskCode().substring(0, 2).equals("11")) {
		 * prpLCMainList.remove(i); System.out.println(prpLCMainList); i--;
		 * prpLCMainVo=prpLCMainList.get(i);
		 * 
		 * } if (prplclaimVo.getRiskCode().substring(0, 2).equals("12")) {
		 * prpLCMainList.remove(i); System.out.println(prpLCMainList); i--;
		 * prpLCMainVo=prpLCMainList.get(i); // List<PrpLCItemCarVo>
		 * PrpCItemCars=prpLCMainVo.getPrpCItemCars();
		 * 
		 * } }
		 */
		
		//特别约定显示处理  条款重复只显示一条
		List<PrpLCengageVo> prpLCengageVos = prpLCMainVo.getPrpCengages();
		Map<String,String> prpLCengageMap = new HashMap<String,String>();
		if(prpLCengageVos!=null&&prpLCengageVos.size()>0){
			for(PrpLCengageVo cengageVo:prpLCengageVos){
				if(!prpLCengageMap.containsKey(cengageVo.getClauseCode())){
					prpLCengageMap.put(cengageVo.getClauseCode(),cengageVo.getClauseName());
				}
			}
		}
		
		ModelAndView modelAndView = new ModelAndView();
		
		if (prpLClaimTextVoList != null && !prpLClaimTextVoList.isEmpty()) {
			modelAndView.addObject("prpLClaimTextList", prpLClaimTextVoList);
		}

		// 判断claimKind表中是否有两条B险记录
		int kindBFlag = 0;
		PrpLClaimKindVo claimKindBLoss = new PrpLClaimKindVo();// 险别估损信息B险需要分成两条展示，B险车财
		PrpLClaimKindVo claimKindBPers = new PrpLClaimKindVo();// B险人
		List<PrpLClaimKindVo> claimKindListTempNoM = new ArrayList<PrpLClaimKindVo>();//用于排除m险的临时List
		for (PrpLClaimKindVo claimKindVo : prpLClaimKindVos) {
			// 估损信息不显示不计免赔险 排除
			if(!claimKindVo.getKindCode().endsWith("M")){
				claimKindListTempNoM.add(claimKindVo);
			}
			if (CodeConstants.KINDCODE.KINDCODE_B.equals(claimKindVo
					.getKindCode())) {
				kindBFlag++;
			}
		}
		prpLClaimKindVos = claimKindListTempNoM;
		// 如果B险有两条记录，则将B险在prpLClaimKindVos里移除，然后分条展示
		if (kindBFlag == 2) {
			List<PrpLClaimKindVo> claimKindListTemp = new ArrayList<PrpLClaimKindVo>();
			for (PrpLClaimKindVo claimKindVoDuc : prpLClaimKindVos) {
				if (CodeConstants.KINDCODE.KINDCODE_B.equals(claimKindVoDuc
						.getKindCode())) {
					if("车物".equals(claimKindVoDuc.getLossItemName())){
						claimKindBLoss = claimKindVoDuc;
					} else {
						claimKindBPers = claimKindVoDuc;
					}
				} else {
					claimKindListTemp.add(claimKindVoDuc);
				}
			}
			prpLClaimKindVos = claimKindListTemp;

		}
		// 强制立案的时候checkVo为null，界面显示取立案表的值
		if(prpLCheckVo == null){
			prpLCheckVo = new PrpLCheckVo();
			prpLCheckVo.setDamageCode(prplclaimVo.getDamageCode());
			prpLCheckVo.setDamageTypeCode(prplclaimVo.getDamageTypeCode());
			prpLCheckVo.setLossType(prplclaimVo.getIsTotalLoss());
			prpLCheckVo.setIsClaimSelf("0");//强制立案是否互碰自赔默认为否
			prpLCheckVo.setIsSubRogation(prplclaimVo.getIsSubRogation());
		}
		// 强制立案的时候checkDutyVo为null，界面显示取立案表的值
		if(checkDutyVo == null){
			checkDutyVo = new PrpLCheckDutyVo();
			checkDutyVo.setCiDutyFlag(prplclaimVo.getCiIndemDuty());
			checkDutyVo.setIndemnityDuty(prplclaimVo.getIndemnityDuty());
			checkDutyVo.setIndemnityDutyRate(prplclaimVo.getIndemnityDutyRate());
		}
		// 判断是否重开赔案
		String endCaseFlag = claimService.adjustCaseHadEnd(prplclaimVo.getClaimNo());

		modelAndView.addObject("endCaseFlag",endCaseFlag);
		modelAndView.addObject("validFlag",validFlag);
		modelAndView.addObject("prpLDisaster", disasterVo);
		modelAndView.addObject("prpLCMainList", prpLCMainList);
		modelAndView.addObject("prpLCheck", prpLCheckVo);
		modelAndView.addObject("prpLRegist", registVo);
		modelAndView.addObject("registExt", registExt);
		modelAndView.addObject("prpLCMain", prpLCMainVo);
		modelAndView.addObject("prpLClaim", prplclaimVo);
		modelAndView.addObject("prpLCheckDuty", checkDutyVo);
		modelAndView.addObject("prpLCengages", prpLCengageMap);
		modelAndView.addObject("prpLClaimKinds", prpLClaimKindVos);
		modelAndView.addObject("prpLClaimKindHisMap", prpLClaimKindHisMap);
		/*modelAndView.addObject("prpLClaimKindHiss", prpLClaimKindHisVos);
		modelAndView.addObject("prpLClaimKindHiss1", prpLClaimKindHisList1);*/
		modelAndView.addObject("prpLClaimKindFees", prpLClaimKindFeeVos);
		// claimKindFee表车财人展示的标志位，用于分辨是否需要将B险分两条展示
		modelAndView.addObject("claimKindFlag", kindBFlag);
		modelAndView.addObject("claimKindBLoss", claimKindBLoss);
		modelAndView.addObject("claimKindBPers", claimKindBPers);
		modelAndView.addObject("taskVo", taskVo);
		modelAndView.addObject("riskCode", registVo.getRiskCode());
		// modelAndView.addObject("prpLCItemCarVos",prpLCItemCarVos);

		modelAndView.setViewName("claimEdit/ClaimEdit");

		return modelAndView;
	}

	/**
	 * 立案查看
	 * 
	 * <pre></pre>
	 * @param claimNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月12日 下午7:15:51): <br>
	 */
	@RequestMapping("/claimView.do")
	public ModelAndView claimView(String claimNo, Double flowTaskId) {
		ModelAndView modelAndView = new ModelAndView();
		List<PrpLClaimKindHisVo> prpLClaimKindHisList1 = new ArrayList<PrpLClaimKindHisVo>();
		PrpLClaimVo prplclaimVo = claimService.findByClaimNo(claimNo);
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);
        List<PrpLCMainVo> prpLCMainList = policyViewService
				.getPolicyAllInfo(prplclaimVo.getRegistNo());
		PrpLCMainVo prpLCMainVo = new PrpLCMainVo();
		List<PrpLClaimKindVo> prpLClaimKindVos = prplclaimVo
				.getPrpLClaimKinds();
		List<PrpLClaimKindHisVo> prpLClaimKindHisVos = claimKindHisService
				.findAllKindHisVoList(claimNo);
		// 0-立案注销 2-立案拒赔 1-正常数据
		String validFlag = "1";
		// 交强险全部显示到页面，商业险去掉不计免赔险
		if (prpLClaimKindHisVos != null && prpLClaimKindHisVos.size() > 0) {
			for (int j = 0; j < prpLClaimKindHisVos.size(); j++) {
				// 如果估损金额 施救费 及其变化量字段都是0，则不展示
				if(adjustClaimHisShow(prpLClaimKindHisVos.get(j))){
					if (prpLClaimKindHisVos.get(j).getRiskCode().equals("1101")) {
						prpLClaimKindHisList1.add(prpLClaimKindHisVos.get(j));
	
					} else {
						if (!CodeConstants.NOSUBRISK_MAP
								.containsKey(prpLClaimKindHisVos.get(j)
										.getKindCode())
								&& !prpLClaimKindHisVos.get(j).getKindCode()
										.endsWith("M")) {
							prpLClaimKindHisList1.add(prpLClaimKindHisVos.get(j));
	
						}
					}
				}
			}
		}

		//实现立案轨迹根据调整次数折叠显示，根据更新次数降序输出，相同次数的调整记录放到一个Map对象中
		Map<Integer, List<PrpLClaimKindHisVo>> prpLClaimKindHisMap = new TreeMap<Integer, List<PrpLClaimKindHisVo>>(
                new Comparator<Integer>() {
                    public int compare(Integer obj1, Integer obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
		
		for(PrpLClaimKindHisVo claimKindHisVo:prpLClaimKindHisList1){
			if(prpLClaimKindHisMap.containsKey(claimKindHisVo.getEstiTimes())){
				prpLClaimKindHisMap.get(claimKindHisVo.getEstiTimes()).add(claimKindHisVo);
			}else{
				List<PrpLClaimKindHisVo> claimKindHisVoListForMap = new ArrayList<PrpLClaimKindHisVo>();
				claimKindHisVoListForMap.add(claimKindHisVo);
				prpLClaimKindHisMap.put(claimKindHisVo.getEstiTimes(),claimKindHisVoListForMap);
			}
		}
		
		List<PrpLClaimKindFeeVo> prpLClaimKindFeeVos = prplclaimVo
				.getPrpLClaimKindFees();
		PrpLCheckVo prpLCheckVo = checkTaskService
				.findCheckVoByRegistNo(prplclaimVo.getRegistNo());
		PrpLRegistVo registVo = registQueryService.findByRegistNo(prplclaimVo
				.getRegistNo());
		PrpLRegistExtVo registExt = registVo.getPrpLRegistExt();
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(
				prplclaimVo.getRegistNo(), 1);
		Map<String, String> map = new HashMap<String, String>();
		map.put("bussNo", claimNo);
		List<PrpLClaimTextVo> prpLClaimTextVoList = claimTextService
				.findClaimTextList(map);
		if (prpLClaimTextVoList != null && !prpLClaimTextVoList.isEmpty()) {
			modelAndView.addObject("prpLClaimTextList", prpLClaimTextVoList);
		}
		/*for (int i = 0; i < prpLCMainList.size(); i++) {
			if (prplclaimVo.getRiskCode().substring(0, 2).equals("11")) {
				prpLCMainVo = prpLCMainList.get(i);

			}
			if (prplclaimVo.getRiskCode().substring(0, 2).equals("12")) {
				prpLCMainVo = prpLCMainList.get(i);

			}
		}*/
		
		for (PrpLCMainVo cMain : prpLCMainList) {
			if (cMain.getRiskCode().equals(prplclaimVo.getRiskCode())) {
				prpLCMainVo = cMain;
			}
		}

		// 判断claimKind表中是否有两条B险记录
		int kindBFlag = 0;
		PrpLClaimKindVo claimKindBLoss = new PrpLClaimKindVo();// 险别估损信息B险需要分成两条展示，B险车财
		PrpLClaimKindVo claimKindBPers = new PrpLClaimKindVo();// B险人
		List<PrpLClaimKindVo> claimKindListTempNoM = new ArrayList<PrpLClaimKindVo>();//用于排除m险的临时List
		for (PrpLClaimKindVo claimKindVo : prpLClaimKindVos) {
			// 估损信息不显示不计免赔险 排除
			if(!claimKindVo.getKindCode().endsWith("M")){
				claimKindListTempNoM.add(claimKindVo);
			}
			if (CodeConstants.KINDCODE.KINDCODE_B.equals(claimKindVo
					.getKindCode())) {
				kindBFlag++;
			}
		}
		prpLClaimKindVos = claimKindListTempNoM;
		// 如果B险有两条记录，则将B险在prpLClaimKindVos里移除，然后分条展示
		if (kindBFlag == 2) {
			List<PrpLClaimKindVo> claimKindListTemp = new ArrayList<PrpLClaimKindVo>();
			for (PrpLClaimKindVo claimKindVoDuc : prpLClaimKindVos) {
				if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimKindVoDuc.getKindCode())){
					if("车物".equals(claimKindVoDuc.getLossItemName())){
						claimKindBLoss = claimKindVoDuc;
					}else{
						claimKindBPers = claimKindVoDuc;
					}
				}else{
					// 估损信息不显示不计免赔险 排除
					if(!claimKindVoDuc.getKindCode().endsWith("M")){
						claimKindListTemp.add(claimKindVoDuc);
					}
				}
			}
			prpLClaimKindVos = claimKindListTemp;

		}
		
		// 巨灾信息查询
		PrpLDisasterVo disasterVo = checkTaskService.findDisasterVoByRegistNo(prplclaimVo.getRegistNo());
		
		//特别约定显示处理  条款重复只显示一条
		List<PrpLCengageVo> prpLCengageVos = prpLCMainVo.getPrpCengages();
		Map<String,String> prpLCengageMap = new HashMap<String,String>();
		if(prpLCengageVos!=null&&prpLCengageVos.size()>0){
			for(PrpLCengageVo cengageVo:prpLCengageVos){
				if(!prpLCengageMap.containsKey(cengageVo.getClauseCode())){
					prpLCengageMap.put(cengageVo.getClauseCode(),cengageVo.getClauseName());
				}
			}
		}
		// 判断是否重开赔案
		String endCaseFlag = claimService.adjustCaseHadEnd(prplclaimVo.getClaimNo());

		modelAndView.addObject("endCaseFlag",endCaseFlag);
		modelAndView.addObject("validFlag",validFlag);
		modelAndView.addObject("prpLDisaster", disasterVo);
		modelAndView.addObject("prpLCMainList", prpLCMainList);
		modelAndView.addObject("prpLCheck", prpLCheckVo);
		modelAndView.addObject("prpLRegist", registVo);
		modelAndView.addObject("registExt", registExt);
		modelAndView.addObject("prpLCMain", prpLCMainVo);
		modelAndView.addObject("prpLClaim", prplclaimVo);
		modelAndView.addObject("prpLCheckDuty", checkDutyVo);
		modelAndView.addObject("prpLCengages", prpLCengageMap);
		modelAndView.addObject("prpLClaimKinds", prpLClaimKindVos);
		modelAndView.addObject("prpLClaimKindHisMap", prpLClaimKindHisMap);
		/*modelAndView.addObject("prpLClaimKindHiss", prpLClaimKindHisVos);
		modelAndView.addObject("prpLClaimKindHiss1", prpLClaimKindHisList1);*/
		modelAndView.addObject("prpLClaimKindFees", prpLClaimKindFeeVos);
		modelAndView.addObject("nodeCode", taskVo.getNodeCode());
		modelAndView.addObject("taskVo", taskVo);
		// claimKindFee表车财人展示的标志位，用于分辨是否需要将B险分两条展示
		modelAndView.addObject("claimKindFlag",kindBFlag);
		modelAndView.addObject("claimKindBLoss",claimKindBLoss);
		modelAndView.addObject("claimKindBPers",claimKindBPers);
		modelAndView.addObject("riskCode", registVo.getRiskCode());
		modelAndView.setViewName("claimEdit/ClaimEdit");
		return modelAndView;
	}
	
	/**
	 * 保存立案修改
	 * 
	 * <pre></pre>
	 * @param flowTaskId
	 * @return
	 * @throws Exception
	 * @modified: ☆WLL(2016年7月19日 下午5:54:28): <br>
	 */
	@RequestMapping("/saveClaimModify.do")
	@ResponseBody
	public AjaxResult saveClaimModify(@FormModel("prplClaimText") PrpLClaimTextVo prpLClaimTextVo,
	                    			@FormModel("prpLClaimKindFee") List<PrpLClaimKindFeeVo> prpLClaimKindFeeVoList,
	                    			@FormModel("prpLClaimKind") List<PrpLClaimKindVo> prpLClaimKindVoList) throws Exception{
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo userVo = WebUserUtils.getUser();
		PrpLClaimTextVo claimTextVo = createClaimText(prpLClaimTextVo.getBussNo(),prpLClaimTextVo,userVo);
		claimService.saveClaimModifity(claimTextVo,prpLClaimKindVoList,prpLClaimKindFeeVoList);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	@RequestMapping("/amtModifyCompareComp.ajax")
	@ResponseBody
	public AjaxResult amtModifyCompareComp(String claimNo,BigDecimal amt,String kindCode) {
		AjaxResult ajaxResult = new AjaxResult();
		String flag = "YES";
		// 查询该立案号是否存在核赔通过的计算书
		PrpLCompensateVo compenVo = compensateTaskService.findCompByClaimNo(claimNo);
		if(compenVo!=null&&CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE.equals(compenVo.getUnderwriteFlag())||CodeConstants.UnderWriteFlag.AUTO_UNDERWRITE
				.equals(compenVo.getUnderwriteFlag())){
			//如果理算核赔通过，汇总该险别的核赔金额
			BigDecimal sumClaimKindOne = BigDecimal.ZERO;
			//汇总车财人表该险别的核赔金额
			if(compenVo.getPrpLLossItems()!=null&&compenVo.getPrpLLossItems().size()>0){
				for(PrpLLossItemVo lossItemVo:compenVo.getPrpLLossItems()){
					if(kindCode.equals(lossItemVo.getKindCode())){
						sumClaimKindOne = sumClaimKindOne.add(lossItemVo.getSumRealPay());
					}
				}
			}
			if(compenVo.getPrpLLossProps()!=null&&compenVo.getPrpLLossProps().size()>0){
				for(PrpLLossPropVo lossPropVo:compenVo.getPrpLLossProps()){
					if(kindCode.equals(lossPropVo.getKindCode())){
						sumClaimKindOne = sumClaimKindOne.add(lossPropVo.getSumRealPay());
					}
				}
			}
			if(compenVo.getPrpLLossPersons()!=null&&compenVo.getPrpLLossPersons().size()>0){
				for(PrpLLossPersonVo lossPersVo:compenVo.getPrpLLossPersons()){
					if(kindCode.equals(lossPersVo.getKindCode())){
						for(PrpLLossPersonFeeVo persFee:lossPersVo.getPrpLLossPersonFees()){
							sumClaimKindOne = sumClaimKindOne.add(persFee.getFeeRealPay());
						}
					}
				}
			}
			//比较修改的金额和核赔金额
			if(amt.compareTo(sumClaimKindOne)==1){
				flag = "NO";
			}
		}
		ajaxResult.setData(flag);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	/**
	 * 新建claimTextVo
	 * 
	 * <pre></pre>
	 * @param claimNo
	 * @param prpLClaimTextVo
	 * @param userVo
	 * @return
	 * @throws Exception
	 * @modified: ☆WLL(2016年7月19日 下午7:18:43): <br>
	 */
	public PrpLClaimTextVo createClaimText(String claimNo,PrpLClaimTextVo prpLClaimTextVo,SysUserVo userVo) throws Exception {
   		String userCode = userVo.getUserCode();
   		String comCode = userVo.getComCode();
   		String AuditStatus = "claimModify";
   		Date date = new Date();
		// 1表示節點已完成
   		String flag = "1";
   		String nodeCode = FlowNode.ClaimCI.name();
   		
   		PrpLClaimVo claimVo = claimService.findByClaimNo(claimNo);
   		if(!"1101".equals(claimVo.getRiskCode())){
   			nodeCode = FlowNode.ClaimBI.name();
   		}
   		//PrpLClaimTextVo claimTextVo = claimTextService.findClaimTextByNode(Long.parseLong(claimNo),nodeCode,flag);
   		PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
		claimTextVo = new PrpLClaimTextVo();
		//claimTextVo.setBussTaskId(Long.parseLong(claimNo));
		claimTextVo.setRegistNo(claimVo.getRegistNo());
		claimTextVo.setBussNo(claimVo.getClaimNo());
		claimTextVo.setTextType(CodeConstants.ClaimText.OPINION);
		claimTextVo.setNodeCode(nodeCode.toString());
		claimTextVo.setOperatorCode(userCode);//claimTextVo.setOperatorName(userName);
		claimTextVo.setOperatorName(userVo.getUserName());
		claimTextVo.setComCode(comCode);//claimTextVo.setComName(comName);
		claimTextVo.setInputTime(date);
		claimTextVo.setBigNode(FlowNode.valueOf(nodeCode).getUpperNode().toString());
		claimTextVo.setDescription(prpLClaimTextVo.getDescription());
		claimTextVo.setStatus(AuditStatus);
		claimTextVo.setFlag(flag);
		claimTextVo.setCreateUser(userCode);
		claimTextVo.setCreateTime(date);
		claimTextVo.setUpdateUser(userCode);
		claimTextVo.setUpdateTime(date);
   		return claimTextVo;
   	}
	
	/**
	 * 判断该条轨迹记录是否无变化，无变化就不展示，返回false
	 * 
	 * <pre></pre>
	 * @param claimKindHisVo
	 * @return
	 * @modified: ☆WLL(2016年7月21日 上午11:16:13): <br>
	 */
	private boolean adjustClaimHisShow(PrpLClaimKindHisVo claimKindHisVo){
		boolean isChangedFlag =  true;
		if(BigDecimal.ZERO.compareTo(DataUtils.NullToZero(claimKindHisVo.getClaimLoss()))==0
				&&BigDecimal.ZERO.compareTo(DataUtils.NullToZero(claimKindHisVo.getClaimLossChg()))==0){
			isChangedFlag =  false;
		}
		return isChangedFlag;
	}
	
	/**
	 * 再保系统通过立案号，查询理赔系统立案页面信息
	 * @param claimNo
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/claimEdits.do")
	public ModelAndView reinsuranceToClaim(HttpServletResponse resp, String claimNo) throws IOException {
		
		ModelAndView modelAndView = new ModelAndView();
		String registNo = null;
		Double flowTaskId = null;
		String subNodeCode = null;
		PrpLClaimVo vo = claimService.findByClaimNo(claimNo);
		if(vo != null && !vo.equals("")){
			registNo = vo.getRegistNo();
			if(vo.getRiskCode() != null && vo.getRiskCode().equals("1101")){
				subNodeCode = "ClaimCI";
			}else if(vo.getRiskCode() != null && vo.getRiskCode().equals("1206")){
				subNodeCode = "ClaimBI";
			}
		}
		//查询已经处理完成的立案节点
		PrpLWfTaskVo voo = wfTaskHandleService.queryTaskByAny(registNo, claimNo, subNodeCode, "3");
		if(voo != null && !voo.equals("")){
			flowTaskId = (voo.getTaskId() == null ? 0d : voo.getTaskId().doubleValue());
		}else{
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("Text/plain");
			PrintWriter out = resp.getWriter();
			if(registNo != null && !registNo.equals("")){
				out.print("该立案号对应的案子，立案节点还没有处理完！");
			}else{
				out.print("该立案号没有对应的案子！");
			}
			out.close();
			return null;
		}
		
		List<PrpLClaimKindHisVo> prpLClaimKindHisList1 = new ArrayList<PrpLClaimKindHisVo>();
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);
		System.out.println(flowTaskId);
		PrpLClaimVo prplclaimVo = claimService.findByClaimNo(taskVo.getClaimNo());
		// 0-立案注销 2-立案拒赔 1-正常数据
		String validFlag = "1";

		List<PrpLCMainVo> prpLCMainList = policyViewService.getPolicyAllInfo(prplclaimVo.getRegistNo());
		PrpLCMainVo prpLCMainVo = new PrpLCMainVo();
		List<PrpLClaimKindVo> prpLClaimKindVos = prplclaimVo.getPrpLClaimKinds();
		List<PrpLClaimKindHisVo> prpLClaimKindHisVos = claimKindHisService.findAllKindHisVoList(taskVo.getClaimNo());
		// 交强险全部显示到页面，商业险去掉不计免赔险
		if (prpLClaimKindHisVos != null && prpLClaimKindHisVos.size() > 0) {
			for (int j = 0; j < prpLClaimKindHisVos.size(); j++) {
		// 如果估计赔款 及其变化量字段都是0，则不展示
				if(adjustClaimHisShow(prpLClaimKindHisVos.get(j))){
					if (prpLClaimKindHisVos.get(j).getRiskCode().equals("1101")) {
						prpLClaimKindHisList1.add(prpLClaimKindHisVos.get(j));

					} else {
						if (!CodeConstants.NOSUBRISK_MAP.containsKey(prpLClaimKindHisVos.get(j).getKindCode())
								&& !prpLClaimKindHisVos.get(j).getKindCode().endsWith("M")) {
							prpLClaimKindHisList1.add(prpLClaimKindHisVos.get(j));

						}
					}
				}
			}
		}

		//实现立案轨迹根据调整次数折叠显示，根据更新次数降序输出，相同次数的调整记录放到一个Map对象中
		Map<Integer, List<PrpLClaimKindHisVo>> prpLClaimKindHisMap = new TreeMap<Integer, List<PrpLClaimKindHisVo>>(
                new Comparator<Integer>() {
                    public int compare(Integer obj1, Integer obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
		
		for(PrpLClaimKindHisVo claimKindHisVo:prpLClaimKindHisList1){
			if(prpLClaimKindHisMap.containsKey(claimKindHisVo.getEstiTimes())){
				prpLClaimKindHisMap.get(claimKindHisVo.getEstiTimes()).add(claimKindHisVo);
			}else{
				List<PrpLClaimKindHisVo> claimKindHisVoListForMap = new ArrayList<PrpLClaimKindHisVo>();
				claimKindHisVoListForMap.add(claimKindHisVo);
				prpLClaimKindHisMap.put(claimKindHisVo.getEstiTimes(),claimKindHisVoListForMap);
			}
		}
		
		List<PrpLClaimKindFeeVo> prpLClaimKindFeeVos = prplclaimVo
				.getPrpLClaimKindFees();
		PrpLCheckVo prpLCheckVo = checkTaskService
				.findCheckVoByRegistNo(prplclaimVo.getRegistNo());
		PrpLRegistVo registVo = registQueryService.findByRegistNo(prplclaimVo
				.getRegistNo());
		PrpLRegistExtVo registExt = registVo.getPrpLRegistExt();
		// 巨灾信息查询
		PrpLDisasterVo disasterVo = checkTaskService.findDisasterVoByRegistNo(prplclaimVo.getRegistNo());
		
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(
				prplclaimVo.getRegistNo(), 1);
		Map<String, String> map = new HashMap<String, String>();
		map.put("bussNo", taskVo.getClaimNo());
		List<PrpLClaimTextVo> prpLClaimTextVoList = claimTextService.findClaimTextList(map);

		for (PrpLCMainVo cMain : prpLCMainList) {
			if (cMain.getRiskCode().equals(prplclaimVo.getRiskCode())) {
				prpLCMainVo = cMain;
			}
		}
		
		
		//特别约定显示处理  条款重复只显示一条
		List<PrpLCengageVo> prpLCengageVos = prpLCMainVo.getPrpCengages();
		Map<String,String> prpLCengageMap = new HashMap<String,String>();
		if(prpLCengageVos!=null&&prpLCengageVos.size()>0){
			for(PrpLCengageVo cengageVo:prpLCengageVos){
				if(!prpLCengageMap.containsKey(cengageVo.getClauseCode())){
					prpLCengageMap.put(cengageVo.getClauseCode(),cengageVo.getClauseName());
				}
			}
		}
		
		
		if (prpLClaimTextVoList != null && !prpLClaimTextVoList.isEmpty()) {
			modelAndView.addObject("prpLClaimTextList", prpLClaimTextVoList);
		}

		// 判断claimKind表中是否有两条B险记录
		int kindBFlag = 0;
		PrpLClaimKindVo claimKindBLoss = new PrpLClaimKindVo();// 险别估损信息B险需要分成两条展示，B险车财
		PrpLClaimKindVo claimKindBPers = new PrpLClaimKindVo();// B险人
		List<PrpLClaimKindVo> claimKindListTempNoM = new ArrayList<PrpLClaimKindVo>();//用于排除m险的临时List
		for (PrpLClaimKindVo claimKindVo : prpLClaimKindVos) {
			// 估损信息不显示不计免赔险 排除
			if(!claimKindVo.getKindCode().endsWith("M")){
				claimKindListTempNoM.add(claimKindVo);
			}
			if (CodeConstants.KINDCODE.KINDCODE_B.equals(claimKindVo
					.getKindCode())) {
				kindBFlag++;
			}
		}
		prpLClaimKindVos = claimKindListTempNoM;
		// 如果B险有两条记录，则将B险在prpLClaimKindVos里移除，然后分条展示
		if (kindBFlag == 2) {
			List<PrpLClaimKindVo> claimKindListTemp = new ArrayList<PrpLClaimKindVo>();
			for (PrpLClaimKindVo claimKindVoDuc : prpLClaimKindVos) {
				if (CodeConstants.KINDCODE.KINDCODE_B.equals(claimKindVoDuc
						.getKindCode())) {
					if("车物".equals(claimKindVoDuc.getLossItemName())){
						claimKindBLoss = claimKindVoDuc;
					} else {
						claimKindBPers = claimKindVoDuc;
					}
				} else {
					claimKindListTemp.add(claimKindVoDuc);
				}
			}
			prpLClaimKindVos = claimKindListTemp;

		}
		// 强制立案的时候checkVo为null，界面显示取立案表的值
		if(prpLCheckVo == null){
			prpLCheckVo = new PrpLCheckVo();
			prpLCheckVo.setDamageCode(prplclaimVo.getDamageCode());
			prpLCheckVo.setDamageTypeCode(prplclaimVo.getDamageTypeCode());
			prpLCheckVo.setLossType(prplclaimVo.getIsTotalLoss());
			prpLCheckVo.setIsClaimSelf("0");//强制立案是否互碰自赔默认为否
			prpLCheckVo.setIsSubRogation(prplclaimVo.getIsSubRogation());
		}
		// 强制立案的时候checkDutyVo为null，界面显示取立案表的值
		if(checkDutyVo == null){
			checkDutyVo = new PrpLCheckDutyVo();
			checkDutyVo.setCiDutyFlag(prplclaimVo.getCiIndemDuty());
			checkDutyVo.setIndemnityDuty(prplclaimVo.getIndemnityDuty());
			checkDutyVo.setIndemnityDutyRate(prplclaimVo.getIndemnityDutyRate());
		}
		// 判断是否重开赔案
		String endCaseFlag = claimService.adjustCaseHadEnd(prplclaimVo.getClaimNo());

		modelAndView.addObject("endCaseFlag",endCaseFlag);
		modelAndView.addObject("validFlag",validFlag);
		modelAndView.addObject("prpLDisaster", disasterVo);
		modelAndView.addObject("prpLCMainList", prpLCMainList);
		modelAndView.addObject("prpLCheck", prpLCheckVo);
		modelAndView.addObject("prpLRegist", registVo);
		modelAndView.addObject("registExt", registExt);
		modelAndView.addObject("prpLCMain", prpLCMainVo);
		modelAndView.addObject("prpLClaim", prplclaimVo);
		modelAndView.addObject("prpLCheckDuty", checkDutyVo);
		modelAndView.addObject("prpLCengages", prpLCengageMap);
		modelAndView.addObject("prpLClaimKinds", prpLClaimKindVos);
		modelAndView.addObject("prpLClaimKindHisMap", prpLClaimKindHisMap);
		/*modelAndView.addObject("prpLClaimKindHiss", prpLClaimKindHisVos);
		modelAndView.addObject("prpLClaimKindHiss1", prpLClaimKindHisList1);*/
		modelAndView.addObject("prpLClaimKindFees", prpLClaimKindFeeVos);
		// claimKindFee表车财人展示的标志位，用于分辨是否需要将B险分两条展示
		modelAndView.addObject("claimKindFlag", kindBFlag);
		modelAndView.addObject("claimKindBLoss", claimKindBLoss);
		modelAndView.addObject("claimKindBPers", claimKindBPers);
		modelAndView.addObject("taskVo", taskVo);
		// modelAndView.addObject("prpLCItemCarVos",prpLCItemCarVos);
		modelAndView.addObject("riskCode", registVo.getRiskCode());
		modelAndView.setViewName("claimEdit/ClaimEdit");

		return modelAndView;
	}

}
