/******************************************************************************
 * CREATETIME : 2016年9月22日 下午7:38:07
 ******************************************************************************/
package ins.sino.claimcar.base.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 理赔系统接口交互日志
 */
@Controller
@RequestMapping(value = "/interfaceLog")
public class InterfaceLogAction {

	@Autowired
	ClaimInterfaceLogService claimInterfaceLogService;
	
	// 日志查询初始化
	@RequestMapping(value = "/logQueryList.do")
	@ResponseBody
	public ModelAndView platformLogQuery() {
		ModelAndView mav = new ModelAndView();
		String comCode = WebUserUtils.getComCode();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		mav.addObject("requestTimeStart", startDate);
		mav.addObject("requestTimeEnd", endDate);
		mav.addObject("comCode",comCode);
		mav.setViewName("base/interfaceLog/LogQueryList");
		return mav;
	}
	
	/**
	 * 日志查询
	 */
	@RequestMapping(value = "/search.do")
	@ResponseBody
	public String searchPlatformLog(
			@FormModel(value = "interfaceLogQueryVo") ClaimInterfaceLogVo queryVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length)
			throws Exception {
		String userCode = WebUserUtils.getUserCode();
		queryVo.setUserCode(userCode);
		ResultPage<ClaimInterfaceLogVo> resultPage = claimInterfaceLogService.findLogForPage(queryVo,start,length);
		String jsonData = ResponseUtils.toDataTableJson(resultPage, "id", "comCode:ComCodeFull",  
				"registNo", "businessName", "serviceType", "operateNode:FlowNode", "claimNo",
				"errorMessage","compensateNo","status","errorCode","errorMessage","requestTime");
		return jsonData;
	}
	
	/**
	 * <pre></pre>
	 * @param request
	 * @return
	 * @modified:
	 * ☆Luwei(2016年10月24日 下午5:33:09): <br>
	 */
	@RequestMapping(value = "/interfaceReloadInit.do")
	public ModelAndView platformReloadInit(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		Long logId = Long.parseLong(request.getParameter("logId"));
		ClaimInterfaceLogVo logVo = claimInterfaceLogService.findLogByPK(logId);
//		String reqType = logVo.getRequestType();
		logVo.setOperateNode(logVo.getOperateNode());
		mav.addObject("logVo", logVo);
		
		String reqXml = logVo.getRequestXml();
		if(StringUtils.isBlank(reqXml)){
			reqXml = "<请求内容为空>";
		}
		mav.addObject("reqXml", reqXml.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		
		String resXml = logVo.getResponseXml();
		if(StringUtils.isBlank(resXml)){
			resXml = "<没有返回信息！>";
		}
		mav.addObject("resXml", resXml.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		mav.setViewName("base/interfaceLog/InterfaceLogReloadEdit");
		return mav;
	}
	
	/**
	 * <pre>接口补传</pre>
	 * @param logId
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆Luwei(2016年10月25日 上午11:46:36): <br>
	 */
	@RequestMapping(value = "/interfaceReload.do")
	@ResponseBody
	public AjaxResult platformReload(Long logId) throws Exception{
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo userVo = WebUserUtils.getUser();
		try {
			String result=claimInterfaceLogService.interfaceLogReupload(logId,userVo);
			ajaxResult.setStatusText(result);
			
			//补传成功后，把该条数据的状态改为 - 已补送，
			claimInterfaceLogService.changeInterfaceLog(logId);
		} catch (IllegalArgumentException e) {
			ajaxResult.setStatusText(e.toString());
		}
		return ajaxResult;
	}
}
