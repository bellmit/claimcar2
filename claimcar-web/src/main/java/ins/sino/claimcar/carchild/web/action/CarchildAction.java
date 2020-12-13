package ins.sino.claimcar.carchild.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.support.ObjectToMapCallback;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carchild.service.CarchildService;
import ins.sino.claimcar.carchild.vo.PrplcarchildregistcancleVo;
import ins.sino.claimcar.flow.util.TaskQueryUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/carchild")
public class CarchildAction {
	@Autowired
	CarchildService carchildService;
	
	@RequestMapping("carchildList.do")
	public ModelAndView carchildQuery(){
		ModelAndView mv=new ModelAndView();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
	
		mv.addObject("taskInTimeStart",startDate);
		mv.addObject("taskInTimeEnd",endDate);
		mv.setViewName("carChild/carChildCaseCancleList");
		return mv;
	}
	
	
	/**
	 * 车童/民太安案件注销查询方法
	 * @param handleStatus
	 * @param queryVo
	 * @param start
	 * @param length
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/carchildCaseCancleSearch.do", method = RequestMethod.POST)
	@ResponseBody
	public String search(@RequestParam("handleStatus") String handleStatus,
						@FormModel("prpLWfTaskQueryVo") PrpLWfTaskQueryVo queryVo,// 页面组装VO
						@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
						@RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception {
		
		queryVo.setComCode(WebUserUtils.getComCode());
		queryVo.setUserCode(WebUserUtils.getUserCode());
	
		
			ResultPage<PrplcarchildregistcancleVo> page = carchildService.search(queryVo,start,length,handleStatus);
			String jsonData = ResponseUtils.toDataTableJson(page,searchCallBack(),"registNo","bipolicyNo","cipolicyNo",
					"flagLog","licenseNo","cancleDate","userCode","reason","handleDate","handleUser","examineRusult","requestSource","id");
			return jsonData;
		
	}
	
	
	private ObjectToMapCallback searchCallBack() {
		ObjectToMapCallback callBack = new ObjectToMapCallback() {

			public Map<String, Object> toMap(Object object) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				PrplcarchildregistcancleVo resultVo = (PrplcarchildregistcancleVo) object;
				String registNo = resultVo.getRegistNo();
				String policyNo = resultVo.getBipolicyNo();
				String policyNoLink = resultVo.getCipolicyNo();
				
				StringBuffer regNoUrl = new StringBuffer();
				regNoUrl.append(TaskQueryUtils.getTaskHandUrl("Regis",
						"Regis"));
				regNoUrl.append("?registNo=").append(registNo);
				StringBuffer regHtml = new StringBuffer();
				
					regHtml.append("<a onClick=openTaskEditWin('报案处理','"
							+ regNoUrl.toString() + "') >");
			
				regHtml.append(registNo);
				regHtml.append("</a>");
				dataMap.put("registNoHtml", regHtml.toString());
				String plyNoUrl = TaskQueryUtils.getTaskHandUrl("PolicyView",
						null);
				plyNoUrl += "?registNo=" + registNo;

				StringBuffer plyNoHtml = new StringBuffer();
				plyNoHtml.append("<a href='").append(plyNoUrl)
						.append("'   target='_blank' >");
				plyNoHtml.append(policyNo);
				if (StringUtils.isNotBlank(policyNoLink)) {
					plyNoHtml.append("<br>");
					plyNoHtml.append(policyNoLink);
				}
				plyNoHtml.append("</a>");
				dataMap.put("policyNoHtml", plyNoHtml.toString());

				return dataMap;
			}
		};
		return callBack;
	}
	
	@RequestMapping(value = "/caseCancleNopass.do")
	@ResponseBody
	public AjaxResult caseCancleNopass(Long id){
		AjaxResult ajax=new AjaxResult();
		String sign="0";
		SysUserVo userVo=WebUserUtils.getUser();
		PrplcarchildregistcancleVo cancleVo=carchildService.findPrplcarchildregistcancleVoById(id);
		if(cancleVo!=null && cancleVo.getId()!=null){
			sign=carchildService.sendCaseInfoToCarchild(cancleVo.getRegistNo(),cancleVo.getRequestSource(),userVo);
			if(sign.equals("1")){
				cancleVo.setHandleDate(new Date());
				cancleVo.setHandleUser(userVo.getUserName());
				cancleVo.setExamineRusult("0");
				cancleVo.setStatus("1");
				carchildService.updatePrplcarchildregistcancle(cancleVo);
			}
		}
		ajax.setData(sign);
		ajax.setStatus(HttpStatus.SC_OK);
		return ajax;
	}
}
