package ins.sino.claimcar.regist.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.regist.service.RegistAddService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.Date;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/registAdd")
public class RegistAddAction {
	private static Logger logger = LoggerFactory.getLogger(RegistEditAction.class);
	@Autowired
	RegistAddService registAddService;
	
	/**
	 * 报案补登查询界面初始化
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆WLL(2017年2月14日 下午5:54:24): <br>
	 */
	@RequestMapping(value = "/registAddQueryList.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initPage() {
		ModelAndView mav = new ModelAndView();
		
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		mav.addObject("taskInTimeStart", startDate);
		mav.addObject("taskInTimeEnd", endDate);
		mav.addObject("reportTimeStart", startDate);
		mav.addObject("reportTimeEnd", endDate);
		
		// 根据请求路径的FlowNode节点名 进入查询界面
		mav.setViewName("regist/registQuery/RegisAddQueryList");

		return mav;
	}
	/**
	 */
	@RequestMapping(value = "/registAddSearch.do", method = RequestMethod.POST)
	@ResponseBody
	public String search(@FormModel("registVo") PrpLRegistVo prpLRegistVo,
	                     @RequestParam(value = "start", defaultValue = "0") Integer start,
						 @RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception {
		ResultPage<PrpLRegistVo> page = null;
		SysUserVo userVo = WebUserUtils.getUser();

		page = registAddService.findRegistAddForPage(prpLRegistVo,start,length,userVo);

		String jsonData = ResponseUtils.toDataTableJson(page,"registNo","policyNo","damageAddress","damageTime","reportTime",
				"createUser","prpLRegistCarLosses.licenseNo","prpLRegistExt.insuredName");
		return jsonData;
	}
	/**
	 * 判断案件是否符合补登报案条件
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆WLL(2017年2月20日 下午3:13:39): <br>
	 */
	@RequestMapping("/isRegistAdd.do")
	@ResponseBody
	public AjaxResult isRegistAdd(@FormModel("registNo") String registNo){
		AjaxResult ajaxResult = new AjaxResult();
		try {
			String isRegistAdd = registAddService.isRegistAdd(registNo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(isRegistAdd);
		} catch (Exception e) {
			e.printStackTrace();
			ajaxResult.setStatusText(e.getMessage());
		}
		return ajaxResult;
	}

}
