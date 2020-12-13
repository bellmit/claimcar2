package ins.sino.claimcar.common.web.action;

import java.util.Date;
import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.SystemFunctionService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
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
@RequestMapping("/systemFunction")
public class SystemFunctionAtion {
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private SystemFunctionService systemFunctionService;
	
	
	private static Logger logger = LoggerFactory.getLogger(SystemFunctionAtion.class);
	
	@RequestMapping("/systemFunctionList.do")
	public ModelAndView systemFunctionQuery(){
		ModelAndView mv=new ModelAndView();
		Date  reportTimeEnd= new Date();
		Date reportTimeStart = DateUtils.addDays(reportTimeEnd, -15);
		mv.addObject("reportTimeStart",reportTimeStart);
		mv.addObject("reportTimeEnd",reportTimeEnd);
		mv.setViewName("systemFunction/caseunlockQueryList");
		return mv;
	}
	
	/**
	 * 案件解锁查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆yzy(2018年9月06日 上午11:33:30): <br>
	 */
	@RequestMapping(value = "/search.do", method = RequestMethod.POST)
	@ResponseBody
	public String search(@FormModel("prpLWfTaskQueryVo")PrpLWfTaskQueryVo taskQueryVo,@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
			@RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception {
		
		ResultPage<PrpLRegistVo> resultPage = systemFunctionService.findRegistPage(taskQueryVo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(resultPage,"registNo","comCode","reportTime","remark");
		logger.debug("assessorsQuery.jsonData="+jsonData);

		return jsonData;
	}
	
	@RequestMapping(value = "/updateStatus.ajax", method = RequestMethod.POST)
	@ResponseBody
   public  AjaxResult updateStatus(String registNo){
		 AjaxResult ajax=new  AjaxResult();
		 try {
			systemFunctionService.updateIsQuickCase(registNo);
			ajax.setData("1");
		} catch (Exception e) {
			ajax.setData("0");
			ajax.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		 return ajax;
	}
}
