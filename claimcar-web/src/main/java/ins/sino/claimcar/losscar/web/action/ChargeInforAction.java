package ins.sino.claimcar.losscar.web.action;

import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/charge")
public class ChargeInforAction {
	@Autowired
	LossChargeService lossChargeService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	PersTraceDubboService persTraceDubboService;
	@Autowired
	PropTaskService propTaskService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	RegistService registService;

	// 费用信息
	@RequestMapping(value = "/chargemessage.ajax")
	@ResponseBody
	public ModelAndView chargemessage(String registNo) {
		ModelAndView mv = new ModelAndView();
		Map<Long, PrpLDlossCarMainVo> carMap = new HashMap<Long, PrpLDlossCarMainVo>();
		Map<Long, PrpLdlossPropMainVo> dLPropMap = new HashMap<Long, PrpLdlossPropMainVo>();
		Map<Long, PrpLDlossPersTraceMainVo> PLossMap = new HashMap<Long, PrpLDlossPersTraceMainVo>();
		List<PrpLDlossChargeVo> lossChargeList = lossChargeService.findLossChargeVoByRegistNo(registNo);
		PrpLRegistVo registVos = registService.findRegistByRegistNo(registNo);
		if (lossChargeList != null && lossChargeList.size() > 0) {
			for (PrpLDlossChargeVo lossChargeVo : lossChargeList) {
				if (lossChargeVo.getBusinessType().equals("DLCar")) {
					PrpLDlossCarMainVo lossCarVo = null;
					if (!carMap.containsKey(lossChargeVo.getBusinessId())) {
						lossCarVo = lossCarService.findLossCarMainById(lossChargeVo.getBusinessId());
						carMap.put(lossChargeVo.getBusinessId(), lossCarVo);
					}else{
						lossCarVo = carMap.get(lossChargeVo.getBusinessId());
					}
					if (lossCarVo.getUnderWriteFlag().equals("1")) {
						lossChargeVo.setIsVerify("1");
					} else {
						lossChargeVo.setIsVerify("0");
					}
					lossChargeVo.setLicense(lossCarVo.getLicenseNo());
					lossChargeVo.setSerialNo(lossCarVo.getSerialNo());
				} else if (lossChargeVo.getBusinessType().equals("DLProp")) {
					PrpLdlossPropMainVo prplopropVo = null;
					if (!dLPropMap.containsKey(lossChargeVo.getBusinessId())) {
						 prplopropVo = propTaskService.findPropMainVoById(lossChargeVo.getBusinessId());
						 dLPropMap.put(lossChargeVo.getBusinessId(), prplopropVo);
					}else{
						prplopropVo = dLPropMap.get(lossChargeVo.getBusinessId());
					}

					if (prplopropVo.getUnderWriteFlag().equals("1")) {
						lossChargeVo.setIsVerify("1");//是否核损通过
					} else {
						lossChargeVo.setIsVerify("0");
					}
					if(prplopropVo.getSerialNo()==0){//地面
						lossChargeVo.setLicense("地面");
					}else{
						lossChargeVo.setLicense(prplopropVo.getLicense());
					}

				} else if (lossChargeVo.getBusinessType().equals("PLoss")) {
					PrpLDlossPersTraceMainVo prpTmainVo=null;
					if (!PLossMap.containsKey(lossChargeVo.getBusinessId())) {
						 prpTmainVo = persTraceDubboService.findPersTraceMainByPk(lossChargeVo.getBusinessId());
						PLossMap.put(lossChargeVo.getBusinessId(), prpTmainVo);
					}else{
						 prpTmainVo=PLossMap.get(lossChargeVo.getBusinessId());
					}

					if ( prpTmainVo.getUnderwriteFlag().equals("1")) {
						lossChargeVo.setIsVerify("1");
					} else {
						lossChargeVo.setIsVerify("0");
					}
					lossChargeVo.setLicense("--");
				} else {
					lossChargeVo.setIsVerify("--");
					lossChargeVo.setLicense("--");
				}
			}
		}
		mv.addObject("lossChargeList", lossChargeList);
		mv.addObject("riskCode", registVos.getRiskCode());
		mv.setViewName("loss-common/ChargeInfor");
		return mv;
	}
}
