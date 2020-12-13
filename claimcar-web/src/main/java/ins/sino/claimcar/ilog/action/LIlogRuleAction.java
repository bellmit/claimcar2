package ins.sino.claimcar.ilog.action;

import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.utils.ObjectUtils;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.ilog.rule.service.IlogRuleService;
import ins.sino.claimcar.ilog.rule.vo.IlogDataProcessingVo;
import ins.sino.claimcar.ilog.rule.vo.PrpLRuleBaseInfoVo;
import ins.sino.claimcar.ilog.rule.vo.PrpLRuleDetailInfoVo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/lIlogRule")
public class LIlogRuleAction {

	private static Logger logger = LoggerFactory.getLogger(LIlogRuleAction.class);
	@Autowired
	IlogRuleService ilogRuleService;
	@Autowired
	SaaUserPowerService saaUserPowerService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;

	@RequestMapping(value = "/ruleView.do")
	@ResponseBody
	public ModelAndView ruleView(HttpServletRequest request) throws UnsupportedEncodingException {
		// ILOG规则信息页面显示
		ModelAndView mv = new ModelAndView();

		String registNo = request.getParameter("registNo");// 报案号
		String taskId = request.getParameter("taskId");// 当前节点ID
		String ruleNode = request.getParameter("ruleNode");// 当前节点
		String licenseNo = request.getParameter("licenseNo");// 车牌号
		if(licenseNo!=null&& !"".equals(licenseNo)){
			licenseNo = URLDecoder.decode(licenseNo,"UTF-8");
		}
		
		logger.info(ruleNode);
		
		String riskCode = request.getParameter("riskCode");// 险种

		String flag = "0";// 1 为ilog上线后的业务 0为ilog上线前的业务

		List<PrpLRuleDetailInfoVo> vPriceDetailInfoVoList = new ArrayList<PrpLRuleDetailInfoVo>();
		List<PrpLRuleDetailInfoVo> vLCarDetailInfoVoList = new ArrayList<PrpLRuleDetailInfoVo>();
		List<PrpLRuleDetailInfoVo> vLPropDetailInfoVoList = new ArrayList<PrpLRuleDetailInfoVo>();
		List<PrpLRuleDetailInfoVo> pLVerifyDetailInfoVoList = new ArrayList<PrpLRuleDetailInfoVo>();
		List<PrpLRuleDetailInfoVo> certiDetailInfoVoList = new ArrayList<PrpLRuleDetailInfoVo>();
		List<PrpLRuleDetailInfoVo> compeDetailInfoVoList = new ArrayList<PrpLRuleDetailInfoVo>();
		List<PrpLRuleDetailInfoVo> vClaimDetailInfoVoList = new ArrayList<PrpLRuleDetailInfoVo>();

		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(taskId));
		if(wfTaskVo==null){
			throw new IllegalArgumentException("没找到工作流数据！");
		}
		String upperTaskId = wfTaskVo.getUpperTaskId().toString();

		PrpLWfTaskVo upperWfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(upperTaskId));
		if(upperWfTaskVo==null){
			throw new IllegalArgumentException("没找到工作流数据！");
		}
		// 获取当前节点最新的规则基础数据
		String lossParty = "";
		try{
			PrpLRuleBaseInfoVo prpLRuleBaseInfoVo = ilogRuleService.findRuleBaseInfo(registNo,ruleNode,riskCode,licenseNo,upperTaskId);
			logger.info("规则信息查看 ");
			if( !ObjectUtils.isEmpty(prpLRuleBaseInfoVo)){
				lossParty = prpLRuleBaseInfoVo.getLossParty(); // 车架号
				flag = "1";
				String upperRuleNode = upperWfTaskVo.getNodeCode();
				if("PrePay".equals(upperRuleNode)||"PrePayWf".equals(upperRuleNode)||"PadPay".equals(upperRuleNode)||"Cancel".equals(upperRuleNode)||"CompeWf"
						.equals(upperRuleNode)){}else{
					upperTaskId = "";
				}
				// 处理规则节点明细规则
				IlogDataProcessingVo ilogDataProcessingVo = new IlogDataProcessingVo();
				ilogDataProcessingVo = ilogRuleService.findDetailInfoList(registNo,ruleNode,riskCode,licenseNo,taskId);
				vPriceDetailInfoVoList = ilogDataProcessingVo.getvPriceDetailInfoVoList();
				vLCarDetailInfoVoList = ilogDataProcessingVo.getvLCarDetailInfoVoList();
				vLPropDetailInfoVoList = ilogDataProcessingVo.getvLPropDetailInfoVoList();
				pLVerifyDetailInfoVoList = ilogDataProcessingVo.getpLossDetailInfoVoList();
				certiDetailInfoVoList = ilogDataProcessingVo.getCertiDetailInfoVoList();
				compeDetailInfoVoList = ilogDataProcessingVo.getCompeDetailInfoVoList();
				vClaimDetailInfoVoList = ilogDataProcessingVo.getvClaimDetailInfoVoList();
			}
		}
		catch(Exception e){
			e.printStackTrace();
			throw new IllegalArgumentException("ILOG规则信息查看数据整理异常！ "+e.getMessage());
		}

		mv.addObject("flag",flag);
		mv.addObject("registNo",registNo);
		mv.addObject("ruleNode",ruleNode);
		mv.addObject("lossParty",lossParty);
		mv.addObject("licenseNo",licenseNo);
		mv.addObject("vPriceDetailInfoVoList",vPriceDetailInfoVoList);
		mv.addObject("vLCarDetailInfoVoList",vLCarDetailInfoVoList);
		mv.addObject("vLPropDetailInfoVoList",vLPropDetailInfoVoList);
		mv.addObject("pLVerifyDetailInfoVoList",pLVerifyDetailInfoVoList);
		mv.addObject("certiDetailInfoVoList",certiDetailInfoVoList);
		mv.addObject("compeDetailInfoVoList",compeDetailInfoVoList);
		mv.addObject("vClaimDetailInfoVoList",vClaimDetailInfoVoList);

		mv.setViewName("ilog/IlogRuleView");

		return mv;
	}

	@RequestMapping(value = "/ruleDetailInfo.do")
	@ResponseBody
	public ModelAndView ruleDetailInfo(String ruleId,String serialNo,String ruleNode) throws UnsupportedEncodingException{
		// 规则明细提示信息查看
		ModelAndView mav = new ModelAndView();
		String ruleContent = "";
		logger.info("规则信息明细查看 ");
		ruleContent = ilogRuleService.findRuleContent(ruleId,serialNo,ruleNode);
		mav.addObject("ruleContent",ruleContent);
		mav.setViewName("ilog/IlogRuleDetailView");
		return mav;

	}
}
