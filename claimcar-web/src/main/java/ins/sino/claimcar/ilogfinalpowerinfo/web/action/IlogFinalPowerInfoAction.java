package ins.sino.claimcar.ilogfinalpowerinfo.web.action;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.support.sequence.SeqGenerator;
import ins.framework.lang.Springs;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.sino.claimcar.ilog.rule.service.IlogRuleService;
import ins.sino.claimcar.ilogFinalpowerInfo.vo.IlogFinalPowerInfoVo;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/ilogfinalpower")
public class IlogFinalPowerInfoAction {

	private static Logger logger = LoggerFactory.getLogger(IlogFinalPowerInfoAction.class);
	
	@Autowired
	IlogRuleService ilogRuleService; 
	
	
	/**
	 * 根据id查询
	 * @param ILOGFinalPowerInfo
	 * @param start
	 * @param length
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/ilogFinalPowerInfoList.do")
	@ResponseBody
	public String ilogFinalPowerInfoList(
			@FormModel("ILOGFinalPowerInfo") IlogFinalPowerInfoVo ILOGFinalPowerInfo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length){
		
		System.out.println("ilogRuleService注入： " + ilogRuleService);
		Page page = ilogRuleService.findIlogFinalPowerForPage(ILOGFinalPowerInfo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(page, "id", "userCode", "userName", "gradePower", "powerLevel",
				           "gradeAmount", "branchComcode", "subSidiaryComcode","inputTime","updateTime","remark");
		
		logger.info(jsonData);
		return jsonData;
	}
	
	
	@RequestMapping("/ilogFinalPowerInfoShow.do")
	public String ilogFinalPowerInfoShow(HttpServletRequest request) throws ParseException {
		
		return "ilogFinalPowerInfo/ilogFinalPowerInfoShow";
	}
	
	/**
	 * 添加之前生成id
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/ilogFinalPowerInfoBeforeAdd.do")
	public ModelAndView ilogFinalPowerInfoBeforeAdd(HttpServletRequest request) throws Exception{
		
		ModelAndView mv = new ModelAndView();
		DateFormat myformat = new SimpleDateFormat("yyyy");
		String date = myformat.format(new Date());
		SeqGenerator seqGenerator = (SeqGenerator)Springs.getBean("seqGenerator");
		String key = seqGenerator.generateSequence(date, 4);
		
		Long id =Long.parseLong( date+key );
		IlogFinalPowerInfoVo info = new IlogFinalPowerInfoVo();
		info.setId(id);
		mv.addObject("ILOGFinalPowerInfoVo" , info);
		mv.setViewName("ilogFinalPowerInfo/ilogFinalPowerInfoAdd");
		return mv;
	}
	
	/**
	 * 	如果不存在兜底人员权限，则添加，如果存在则对兜底信息进行更新
	 * @param ilogInfo
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/ilogFinalPowerInfoAdd.do")
	@ResponseBody
	public AjaxResult ilogFinalPowerInfoAdd( HttpServletRequest req,
			@FormModel("ILOGFinalPowerInfoVo") IlogFinalPowerInfoVo ilogInfo) throws Exception{
		
		AjaxResult ajaxResult = new AjaxResult();
      
		ilogInfo.setGradePower(req.getParameter("gradePower"));
		ilogInfo.setPowerLevel(req.getParameter("powerLevel"));	
		
		try {
			if (ilogInfo != null && ilogInfo.getUserCode() != null) {
				String userCode = ilogInfo.getUserCode();
				IlogFinalPowerInfoVo ilogFinalPowerInfo = ilogRuleService.findByUserCode(userCode);
				if (ilogFinalPowerInfo == null) {
					ilogRuleService.addIlog(ilogInfo);
				} else {
					ilogInfo.setId(ilogFinalPowerInfo.getId());
					ilogRuleService.ilogFinalUpdate(ilogInfo);
				}
			} else {
				logger.info("要添加的ilog兜底信息为空！");
			}
			ajaxResult.setStatus(HttpStatus.SC_OK);
		} catch (Exception e) {
			logger.info("添加ilog信息失败！", e);
		}
		return ajaxResult;
	}
	
	/**
	 * 	校验工号是否已经存在兜底信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/whetherExistFinalInfo.do")
	@ResponseBody
	public AjaxResult whetherExistFinalInfo(String userCode) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		IlogFinalPowerInfoVo finalInfo = ilogRuleService.findByUserCode(userCode);
		if (finalInfo != null && finalInfo.getUserCode() != null) {
			ajaxResult.setData("1");
		} else {
			ajaxResult.setData("0");
		}
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}

	
	/**
	 * 	更新
	 * @param ilogfinal
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/ilogFinalPowerInfoSave.do" )
	@ResponseBody
	public AjaxResult updateIlog( HttpServletRequest req, 
			@FormModel("ILOGFinalPowerInfoVo")IlogFinalPowerInfoVo ilogfinal)throws Exception{
		
		AjaxResult ajaxResult = new AjaxResult();
		IlogFinalPowerInfoVo info = new IlogFinalPowerInfoVo();
		Long id = ilogfinal.getId();
		if (ilogfinal.getId() != 0) {
			info = ilogRuleService.findById(id);
		}
		try {
			ilogfinal.setGradePower(req.getParameter("gradePower"));
			ilogfinal.setPowerLevel(req.getParameter("powerLevel"));
			ilogfinal.setInputTime(info.getInputTime());
			ilogRuleService.ilogFinalUpdate(ilogfinal);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setDatas(resultMap);
		} catch (Exception e) {
			logger.debug("保存ilog信息失败！" + e.getMessage());
		}
		return ajaxResult;
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/ilogFinalPowerInfoDel.do")
	@ResponseBody
	public AjaxResult deleteIlog(Long id)throws Exception{
		
		AjaxResult ajaxResult = new AjaxResult();
		try{
			if(id != 0){
				ilogRuleService.deleteilog(id);
			}
			ajaxResult.setStatus(HttpStatus.SC_OK);			
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			logger.debug("删除id为： " + id + "失败！"+e.getMessage());
		}		
		return ajaxResult;
	}
	
	/**
	 * 跳转到更新页面
	 * @param id
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/ilogFinalPowerInfoupdate.do")
	public ModelAndView ilogFinalPowerInfoUpdate( Long id ) throws Exception{
		
		ModelAndView mv = new ModelAndView();
		IlogFinalPowerInfoVo ilogInfoVo = ilogRuleService.findById(id);	
		
		mv.addObject("ILOGFinalPowerInfoVo", ilogInfoVo);
		mv.setViewName("ilogFinalPowerInfo/ilogFinalPowerInfoUpdate");
		
		return mv;
	}
	
	/**
	 * 跳转到删除页面
	 * @param id
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/ilogFinalPowerInfodel.do")
	public ModelAndView ilogFinalPowerInfoDel( Long id ) throws Exception{
		
		ModelAndView mv = new ModelAndView();
		IlogFinalPowerInfoVo ilogInfoVo = ilogRuleService.findById(id);		
		
		mv.addObject("ILOGFinalPowerInfoVo", ilogInfoVo);
		mv.setViewName("ilogFinalPowerInfo/ilogFinalPowerInfoDelete");
		
		return mv;
	}
	
	
}
