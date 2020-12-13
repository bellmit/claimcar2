/******************************************************************************
 * CREATETIME : 2016年2月24日 下午7:53:17
 ******************************************************************************/
package ins.sino.claimcar.lossperson.web.action;

import ins.framework.service.CodeTranService;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.lossperson.service.PersTraceHandleService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersExtVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossperson.vo.SubmitNextVo;
import ins.sino.claimcar.mail.service.MailModelService;
import ins.sino.claimcar.manager.vo.PrpdEmailVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 人伤大案审核
 * @author ★XMSH
 */
@Controller
@RequestMapping("/persTracePLBig")
public class PersTracePLBigAction {

	private static Logger logger = LoggerFactory.getLogger(PersTracePLBigAction.class);

	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private PersTraceService persTraceService;
	@Autowired
	private ClaimTextService claimTextSerVice;
	@Autowired
	private LossChargeService lossChargeService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private PersTraceHandleService persTraceHandleService;
	@Autowired
	MailModelService mailModelService;

	/**
	 * 初始化大案审核界面
	 * @param flowTaskId
	 * @return
	 * @modified: ☆XMSH(2016年2月24日 下午8:02:52): <br>
	 */
	@RequestMapping(value = "/persTracePLBig.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView PersTracePLBig(Double flowTaskId) {
		logger.info("=====flowTaskId====="+flowTaskId);
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);
		String registNo = taskVo.getRegistNo();
		String handlerStatus = taskVo.getHandlerStatus();
		PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo = null;// 人伤主表
		List<PrpLDlossPersTraceVo> persTraceVos = null;// 人员跟踪表
		PrpLClaimTextVo prpLClaimTextVo = null;// 当前节点意见
		List<PrpLDlossChargeVo> prpLDlossChargeVos = null;// 费用赔款信息
		List<PrpLClaimTextVo> prpLClaimTextVos = null; // 意见列表
		int persTracesNum = 0;// 首次跟踪人伤数目为0
		List<String> injuredParts = new ArrayList<String>();// 选中的受伤部位
		PrpLCheckDutyVo prpLCheckDutyVo = null;// 车辆事故责任表
		ModelAndView mav = new ModelAndView();

		prpLDlossPersTraceMainVo = persTraceService.findPersTraceMainVoById(Long.decode(taskVo.getHandlerIdKey()));
		if(prpLDlossPersTraceMainVo==null){// 人伤跟踪审核不存在为空情况
			return null;
		}else{
			prpLDlossPersTraceMainVo = persTraceService.findPersTraceMainVoById(Long.decode(taskVo.getHandlerIdKey()));

//			prpLClaimTextVo = claimTextSerVice.findClaimTextByNode(prpLDlossPersTraceMainVo.getId(),taskVo.getSubNodeCode(),"0");
			prpLClaimTextVos = claimTextSerVice.findClaimTextList(prpLDlossPersTraceMainVo.getId(),prpLDlossPersTraceMainVo.getRegistNo(),FlowNode.PLBig.name());
			prpLClaimTextVos.addAll(claimTextSerVice.findClaimTextList(prpLDlossPersTraceMainVo.getId(),prpLDlossPersTraceMainVo.getRegistNo(),FlowNode.PLoss.name())) ;
			
			Collections.sort(prpLClaimTextVos,new Comparator<PrpLClaimTextVo>(){ 
	            public int compare(PrpLClaimTextVo arg0, PrpLClaimTextVo arg1) { 
	                return arg0.getUpdateTime().compareTo(arg1.getUpdateTime()); 
	            } 
	        }); 
			
			prpLDlossChargeVos = lossChargeService.findLossChargeVos(prpLDlossPersTraceMainVo.getId(),FlowNode.PLoss.name());
			persTraceVos = persTraceService.findPersTraceVo(registNo,prpLDlossPersTraceMainVo.getId());
			
			if(persTraceVos!=null){
				persTracesNum = persTraceVos.size();
				for(PrpLDlossPersTraceVo persTraceVo:persTraceVos){
					List<PrpLDlossPersExtVo> persExtVos = persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersExts();
					String injuredPart = "";
					for(PrpLDlossPersExtVo persExt:persExtVos){
						injuredPart += persExt.getInjuredPart()+",";
					}
					injuredParts.add(injuredPart);
				}
			}

			//费用险别
			List<SysCodeDictVo> dictVos = new ArrayList<SysCodeDictVo>();
			List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,"Y");
			for(PrpLCItemKindVo itemKind:itemKinds){
				SysCodeDictVo dictVo = new SysCodeDictVo();
				dictVo.setCodeCode(itemKind.getKindCode());
				dictVo.setCodeName(itemKind.getKindName());
				dictVos.add(dictVo);
			}
			mav.addObject("dictVos",dictVos);
		}

		// 查询其他节点的交强险责任类型和事故责任比例
		prpLCheckDutyVo = checkTaskService.findCheckDuty(registNo,1);
		if(prpLCheckDutyVo==null||prpLCheckDutyVo.getId()==null){
			prpLCheckDutyVo = new PrpLCheckDutyVo();
			prpLCheckDutyVo.setRegistNo(registNo);
			prpLCheckDutyVo.setSerialNo(1);
		}

		if(prpLClaimTextVo==null||prpLClaimTextVo.getId()==null){
			prpLClaimTextVo = new PrpLClaimTextVo();
			prpLClaimTextVo.setRegistNo(registNo);
			prpLClaimTextVo.setTextType(CodeConstants.ClaimText.OPINION);
			prpLClaimTextVo.setNodeCode(taskVo.getSubNodeCode());
		}
		//标的车和三者车牌号
		Map<String,String> dataSourceMap = checkTaskService.getCarLossParty(registNo);
		//脱敏处理
		if(CodeConstants.WorkStatus.END.equals(taskVo.getWorkStatus())){
			//reportorPhone
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
			if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){//开关
				for(PrpLDlossPersTraceVo vo : persTraceVos){
					vo.getPrpLDlossPersInjured().setCertiCode(DataUtils.replacePrivacy(vo.getPrpLDlossPersInjured().getCertiCode()));
					vo.getPrpLDlossPersInjured().setPhoneNumber(DataUtils.replacePrivacy(vo.getPrpLDlossPersInjured().getPhoneNumber()));
				}
			}
		}
		//人伤大案审核页面意见列表只显示大案意见,其余意见去掉
		List<PrpLClaimTextVo> prpLClaimTextLists=new ArrayList<PrpLClaimTextVo>();
		if(prpLClaimTextVos!=null && prpLClaimTextVos.size()>0){
			for(PrpLClaimTextVo vo:prpLClaimTextVos){
				if(StringUtils.isNotBlank(vo.getNodeCode())){ 
					if(vo.getNodeCode().contains("Big")){
						prpLClaimTextLists.add(vo);
					}
				}
			}
		}
		mav.addObject("registNo",registNo);
		mav.addObject("handlerStatus",handlerStatus);
		mav.addObject("flowTaskId",flowTaskId);
		mav.addObject("flowNodeCode",taskVo.getSubNodeCode());
		mav.addObject("flowNodeName",taskVo.getTaskName());
		mav.addObject("dataSourceMap",dataSourceMap);
		mav.addObject("prpLDlossPersTraceMainVo",prpLDlossPersTraceMainVo);
		mav.addObject("prpLDlossPersTraces",persTraceVos);
		mav.addObject("prpLCheckDutyVo",prpLCheckDutyVo);
		mav.addObject("injuredParts",injuredParts);
		mav.addObject("lossChargeVos",prpLDlossChargeVos);
		mav.addObject("prpLClaimTextVo",prpLClaimTextVo);
		mav.addObject("prpLClaimTextVos",prpLClaimTextLists);//y
		mav.addObject("taskVo",taskVo);
		mav.addObject("persTracesNum",persTracesNum);
		mav.addObject("tabPageNo",0);
		
		String comCode = policyViewService.getPolicyComCode(registNo);
		mav.addObject("comCode",comCode);

		mav.setViewName("lossperson/persTracePLBig/PersTracePLBig");

		return mav;
	}

	@RequestMapping(value = "/saveOrSubmit.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveOrSubmit(@FormModel("prpLDlossPersTraceMainVo") PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo,
	                               @FormModel("submitNextVo") SubmitNextVo submitNextVo,String sendMail) {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			SysUserVo userVo = WebUserUtils.getUser();
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(submitNextVo.getFlowTaskId()));
			String auditStatus = persTraceHandleService.saveOrSubmitPLBig(prpLDlossPersTraceMainVo,submitNextVo,userVo);
			ajaxResult.setData(CodeConstants.AuditStatus.SAVE.equals(auditStatus)?"暂存成功":"提交成功");
			try{
				// 人伤大案审核，在审核通过时直接发送邮件
				if(StringUtils.isNotBlank(submitNextVo.getNextNode()) && FlowNode.END.name().equals(submitNextVo.getNextNode())){
					List<PrpdEmailVo> prpdEmailVoList = mailModelService.sendMail(wfTaskVo,userVo);
					String mailTableStr = mailModelService.getMailTable(prpdEmailVoList);
					ajaxResult.setData(mailTableStr);
				}
			}catch(Exception e){
				logger.error(wfTaskVo.getRegistNo()+"人伤大案审核邮件上报错误："+e);
			}
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}
}
