package ins.sino.claimcar.account.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.other.service.AccountQueryService;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 账号修改查询Action
 * @author ★wu
 */
@Controller
@RequestMapping("/accountQueryAction")
public class AccountQueryAction {
	
	@Autowired
	private AccountQueryService accountQueryService;
	@Autowired
	WfTaskQueryService wfTaskQueryService;
	
	/**
	 * <pre>账号信息修改查询</pre>
	 * @return
	 * @modified:
	 */
	@RequestMapping("/accountInfoList.do")
	@ResponseBody
	public ModelAndView accountInfoList() {
		ModelAndView modelAndView = new ModelAndView();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		modelAndView.addObject("taskInTimeStart", startDate);
		modelAndView.addObject("taskInTimeEnd", endDate);
		modelAndView.addObject("userName", WebUserUtils.getUserName());
		modelAndView.setViewName("accountInfo/AccountInfoList");
		return modelAndView;
	}
	
	/**
	 * <pre>自动理算退票修改查询</pre>
	 * @return
	 * @modified:
	 */
	@RequestMapping("/returnTticketInfoList.do")
	@ResponseBody
	public ModelAndView returnTticketInfoList() {
		ModelAndView modelAndView = new ModelAndView();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		modelAndView.addObject("taskInTimeStart", startDate);
		modelAndView.addObject("taskInTimeEnd", endDate);
		modelAndView.addObject("userName", WebUserUtils.getUserName());
		modelAndView.setViewName("accountInfo/ReturnTticketInfoList");
		return modelAndView;
	}
	
	/**
	 * <pre>账号修改审核查询</pre>
	 * @return
	 * @modified:
	 * ☆Luwei(2016年12月26日 下午3:46:41): <br>
	 */
	@RequestMapping("/accountInfoVerifyList.do")
	@ResponseBody
	public ModelAndView accountInfoQuery() {
		ModelAndView modelAndView = new ModelAndView();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		modelAndView.addObject("taskInTimeStart", startDate);
		modelAndView.addObject("taskInTimeEnd", endDate);
		modelAndView.addObject("isVerify", RadioValue.RADIO_YES);//是否审核标志，是
		modelAndView.setViewName("accountInfo/AccountInfoList");
		return modelAndView;
	}
	
	@RequestMapping(value = "/returnTticketInfoSearch.do", method = RequestMethod.POST)
	@ResponseBody
	public String returnTticketInfoSearch(@RequestParam("handleStatus") String handleStatus,
						@FormModel("prpLWfTaskQueryVo") PrpLWfTaskQueryVo queryVo,// 页面组装VO
						@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
						@RequestParam(value = "length", defaultValue = "10") Integer length) throws ParseException {
		queryVo.setCompensateNo(queryVo.getCompensateNo().replaceAll("\\s*",""));// 去掉所有空格符
		queryVo.setClaimNo(queryVo.getClaimNo().replaceAll("\\s*",""));// 去掉所有空格符
		queryVo.setComCode(WebUserUtils.getComCode());
		queryVo.setUserCode(WebUserUtils.getUserCode());
		//查询账户信息退票修改未处理的
		if(!"1".equals(queryVo.getIncludeLower()) && "0".equals(handleStatus)){
			ResultPage<PrpLPayBankVo> page = accountQueryService.returnTticketInfoSearch(queryVo,start,length);
			String jsonData = ResponseUtils.toDataTableJson(page,"compensateNo","bankName:BankCode","errorMessage","accountId",
					"accountName","remark","accountNo","appTime","flag","registNo","chargeCode","payType:PayRefReason","payType");
			return jsonData;
		}else{//账户信息退票修改已处理、审核（未处理、已处理）
			queryVo.setHandleStatus(handleStatus);
			ResultPage<PrpLPayBankVo> page = accountQueryService.returnTticketInfoSearchByHandle(queryVo,start,length);
			String jsonData = ResponseUtils.toDataTableJson(page,"compensateNo","bankName:BankCode","errorMessage","accountId",
					"accountName","remark","accountNo","appTime","registNo","payeeId","chargeCode","payType:PayRefReason","payType");
			return jsonData;
		}
	}

	@RequestMapping(value = "/accountSearch.do", method = RequestMethod.POST)
	@ResponseBody
	public String search(@RequestParam("handleStatus") String handleStatus,
						@FormModel("prpLWfTaskQueryVo") PrpLWfTaskQueryVo queryVo,// 页面组装VO
						@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
						@RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception {
		queryVo.setCompensateNo(queryVo.getCompensateNo().replaceAll("\\s*",""));// 去掉所有空格符
		queryVo.setClaimNo(queryVo.getClaimNo().replaceAll("\\s*",""));// 去掉所有空格符
		//ComCode用saa_permitcompany.comcode
		List<SysCodeDictVo> sysCodeList=wfTaskQueryService.findAllPermitcompanyByUserCode(WebUserUtils.getUserCode());

		String  comCode = getDistinctPermitcomCode(sysCodeList,WebUserUtils.getComCode());
		queryVo.setComCode(comCode);
		
		queryVo.setUserCode(WebUserUtils.getUserCode());
		//查询账户信息修改未处理的
		if(!"1".equals(queryVo.getIncludeLower()) && "0".equals(handleStatus)){
			ResultPage<PrpLPayBankVo> page = accountQueryService.search(queryVo,start,length);
			String jsonData = ResponseUtils.toDataTableJson(page,"compensateNo","bankName:BankCode","errorMessage","serialNo",
					"accountName","remark","accountNo","appTime","flag","registNo","chargeCode","payType:PayRefReason","payType");
			return jsonData;
		}else{//账户信息修改已处理、审核（未处理、已处理）
			queryVo.setHandleStatus(handleStatus);
			ResultPage<PrpLPayBankVo> page = accountQueryService.searchByHandle(queryVo,start,length);
			String jsonData = ResponseUtils.toDataTableJson(page,"compensateNo","bankName:BankCode","errorMessage","serialNo",
					"accountName","remark","accountNo","appTime","registNo","payeeId","chargeCode","payType:PayRefReason","payType");
			return jsonData;
		}
	}

	private String getDistinctPermitcomCode(List<SysCodeDictVo> sysCodeList, String webUserComCode) {
		StringBuffer querySb = new StringBuffer();
		if (sysCodeList != null && sysCodeList.size() > 0) {
			String comCode = null;
			for (SysCodeDictVo sysCodeDictVo : sysCodeList) {
				comCode = sysCodeDictVo.getCodeCode();
				if (comCode.startsWith("0002")) {
					if (!querySb.toString().contains("0002")) {
						querySb.append("0002").append(",");
					}
				} else {
					if (!querySb.toString().contains(comCode.substring(0, 2))) {
						querySb.append(comCode, 0, 2).append(",");
					}
				}
			}
		} else {
			querySb.append(webUserComCode, 0, 2).append(",");
		}
		return querySb.toString();
	}
	
}
