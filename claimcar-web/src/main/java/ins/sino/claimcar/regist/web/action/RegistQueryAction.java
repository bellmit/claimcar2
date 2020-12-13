/******************************************************************************
* CREATETIME : 2015年11月28日 下午3:15:33
******************************************************************************/
package ins.sino.claimcar.regist.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.support.ObjectToMapCallback;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.CodeConstants.YN01;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistHandlerService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.RegistTaskQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2015年11月28日
 */
@Controller
@RequestMapping("/registQuery")
public class RegistQueryAction {

	private static Logger logger = LoggerFactory.getLogger(RegistEditAction.class);
	@Autowired
	private PolicyViewService policyViewService;

	@Autowired
	private RegistHandlerService registHandlerService;

	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private RegistService registService;
	
	@Autowired
	private RegistTaskQueryService registTaskQueryService;

	/**
	 * 报案查询
	 * @return
	 * @modified: ☆LiuPing(2015年11月28日 下午3:18:39): <br>
	 */
	@RequestMapping(value = "/registList.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initPage() {
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		ModelAndView mav = new ModelAndView();
		mav.addObject("taskInTimeStart", startDate);
		mav.addObject("taskInTimeEnd", endDate);
		mav.addObject("reportTimeStart", startDate);
		mav.addObject("reportTimeEnd", endDate);
		mav.setViewName("regist/registQuery/RegistList");

		return mav;
	}

	
	
	

	private void testSubmitRegist(){
		String registNo = "4201611010000003";
		PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
		WfTaskSubmitVo submitVo=new WfTaskSubmitVo();

		submitVo.setSubmitType(SubmitType.N);
		submitVo.setFlowId(registVo.getFlowId());
		submitVo.setFlowTaskId(new BigDecimal(0));
		submitVo.setComCode(registVo.getComCode());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setTaskInKey(registNo);

		PrpLWfTaskVo taskVo = wfTaskHandleService.submitRegist(registVo,submitVo);

		logger.debug("===testSubmitRegist=taskVo="+taskVo.getTaskId());
	}

	private void testAcceptTask(){
		Double flowTaskId = new Double(282);
		String handlerIdKey="sched_id";
		String handlerUser = "liuping";
		String handlerCom = "0000";

		wfTaskHandleService.acceptTask(flowTaskId,handlerUser,handlerCom);
		PrpLWfTaskVo taskVo = wfTaskHandleService.tempSaveTask(flowTaskId,handlerIdKey,handlerUser,handlerCom);

		logger.debug("===testSubmitRegist=taskVo="+taskVo.getTaskId());
	}

	

	@RequestMapping(value = "/search.do", method = RequestMethod.POST)
	@ResponseBody
	public String search(@FormModel("registVo") PrpLRegistVo prpLRegistVo,@RequestParam(value = "start", defaultValue = "0") Integer start,
							@RequestParam(value = "length", defaultValue = "10") Integer length,String registTaskFlag,String keyProperty) throws Exception {
		ResultPage<PrpLRegistVo> page = null;

		prpLRegistVo.setRegistTaskFlag(registTaskFlag);
		page = registTaskQueryService.findTaskForPage(prpLRegistVo,start,length,keyProperty);

		String jsonData = ResponseUtils.toDataTableJson(page,searchCallBack(),"registNo","policyNo","frameNo","damageAddress","damageTime","reportTime","policyType",
				"updateUser","licenseNo");
		return jsonData;
	}
	private ObjectToMapCallback searchCallBack() {
		ObjectToMapCallback callBack = new ObjectToMapCallback() {

			public Map<String,Object> toMap(Object object) {
				Map<String,Object> dataMap = new HashMap<String,Object>();
				PrpLRegistVo resultVo = (PrpLRegistVo)object;

				StringBuffer bussTagHtml = new StringBuffer();
				bussTagHtml.append("<span class='badge label-danger radius '>vip</span>");
				if(YN01.Y.equals(resultVo.getMercyFlag())){
					bussTagHtml.append("<span class='badge label-warning radius' title='紧急'>急</span>");
				}
				bussTagHtml.append("<span class='badge label-primary radius' title='互碰自赔'>互</span>");
				dataMap.put("bussTagHtml",bussTagHtml.toString());

				dataMap.put("unHandleBtn","<button>未处理原因</button>");
				dataMap.put("imageBtn","<button>照片上传</button>");

				return dataMap;
			}
		};
		return callBack;
	}
}
