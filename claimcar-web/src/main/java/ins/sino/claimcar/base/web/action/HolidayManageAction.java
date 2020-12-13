package ins.sino.claimcar.base.web.action;

import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.saa.vo.SaaGradeVo;
import ins.platform.saa.vo.SaaUserGradeVo;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.manager.vo.PrpLUserHolidayGradeVo;
import ins.sino.claimcar.manager.vo.PrpLUserHolidayVo;
import ins.sino.claimcar.other.service.HolidayManageService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.tools.web.Webserver;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/holidayManage")
public class HolidayManageAction {
	
	@Autowired
	HolidayManageService holidayManageService;
	
	/**
	 * 休假管理查询初始化
	 * @return
	 */
	@RequestMapping("/holidayManageList.do")
	@ResponseBody
	public ModelAndView holidayManageList() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("userCode", WebUserUtils.getUserCode());
		modelAndView.addObject("userName", WebUserUtils.getUserName());
		modelAndView.addObject("comCode", WebUserUtils.getComCode());
		modelAndView.setViewName("base/holidayManage/HolidayManageList");
		return modelAndView;
	}
	/**
	 * 休假审核初始化
	 * @return
	 */
	@RequestMapping("/holidayCheckList.do")
	@ResponseBody
	public ModelAndView holidayCheckList() {
		ModelAndView modelAndView = new ModelAndView();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		modelAndView.addObject("timeStart", startDate);
		modelAndView.addObject("timeEnd", endDate);
		modelAndView.addObject("flag", "check");
		modelAndView.setViewName("base/holidayManage/HolidayCheckList");
		return modelAndView;
	}
	
	/**
	 * 添加信息行
	 */
	@RequestMapping("/addGrade.ajax")
	@ResponseBody
	public ModelAndView addGrade(int size) {
		List<PrpLUserHolidayGradeVo> prpLUserHolidayGradeVos=new ArrayList<PrpLUserHolidayGradeVo>();
		PrpLUserHolidayGradeVo prpLUserHolidayGradeVo=new PrpLUserHolidayGradeVo();
		prpLUserHolidayGradeVos.add(prpLUserHolidayGradeVo);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("size", size);
		modelAndView.addObject("prpLUserHolidayGrades",prpLUserHolidayGradeVos);
		//modelAndView.addObject("gradeName",prpLUserHolidayGradeVo.getGradeName());
		//modelAndView.addObject("transferCode",prpLUserHolidayGradeVo.getTransferCode());
		modelAndView.setViewName("base/holidayManage/HolidayManageEdit_GradeTbody");
		return modelAndView;
	}
	

/**
	 * 查询
	 * @param prpLUserHolidayVo
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping("/holidayManageFind.do")
	@ResponseBody
	public String search(
			@FormModel(value="prpLUserHolidayVo")PrpLUserHolidayVo prpLUserHolidayVo,
			@RequestParam(value="start",defaultValue="0")Integer start,
			@RequestParam(value="length",defaultValue="10")Integer length){
		List<PrpLUserHolidayVo> list = holidayManageService
				.findAllHolidayManage(prpLUserHolidayVo,start,length);
		String jsonData = ResponseUtils.toDataTableJson(list, "userCode",
				"userName", "comCode:ComCode", "prpLUserHolidayGrade.gradeName",
				"mobileNo","checkStatus:HolidayCheckStatus","id","cancelTime","status");
		return jsonData;
	}
	
	
	/**
	 * 休假管理处理初始化
	 * @param request
	 * @return
	 */
	@RequestMapping("/holidayManageEdit.do")
	@ResponseBody
	public ModelAndView holidayManageEdit(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String hid = request.getParameter("hid");
		//String flag=request.getParameter("flag");
		if(hid!=null){
			Long id=null;
		    id = Long.parseLong(hid);
			PrpLUserHolidayVo prpLUserHolidayVo = holidayManageService
					.findPrpLUserHolidayVoByPK(id);
			modelAndView.addObject("prpLUserHolidayVo", prpLUserHolidayVo);
			modelAndView.addObject("prpLUserHolidayGrades",prpLUserHolidayVo.getPrpLUserHolidayGrades());
		}
		String userCode=WebUserUtils.getUserCode();
		String userName=WebUserUtils.getUserName();
		String comCode=WebUserUtils.getComCode();
		modelAndView.addObject("userCode", userCode);
		modelAndView.addObject("userName",userName);
		modelAndView.addObject("comCode",comCode);
		modelAndView.setViewName("base/holidayManage/HolidayManageEdit");
		return modelAndView;
	}
	/**
	 * 休假新增初始化
	 * @param 
	 * @return
	 */
	@RequestMapping("/holidayManageAddEdit.do")
	@ResponseBody
	public ModelAndView holidayManageAddEdit() {
		ModelAndView modelAndView = new ModelAndView();
		SysUserVo userVo = WebUserUtils.getUser();
		Map<String,Map> userMap = new HashMap<String,Map>();
		userMap = holidayManageService.organizeMap(userVo);
		
		modelAndView.addObject("userMap", userMap);
		modelAndView.addObject("userCode", userVo.getUserCode());
		modelAndView.addObject("userName",userVo.getUserName());
		modelAndView.addObject("comCode",userVo.getComCode());
		modelAndView.addObject("flag", "add");
		//判断该员工是否已有申请未审核
		modelAndView.setViewName("base/holidayManage/HolidayAddEdit");
		return modelAndView;
	}
	/**
	 * 保存或更新
	 * @param flag
	 * @param prpLUserHolidayVo
	 * @return
	 */
	@RequestMapping("/saveholidayManage.do")
	@ResponseBody
	public AjaxResult saveholidayManage(
			@FormModel(value = "prpLUserHolidayVo") PrpLUserHolidayVo prpLUserHolidayVo) {
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo userVo = WebUserUtils.getUser();
		holidayManageService.saveOrUpdateHolidayManage(prpLUserHolidayVo,userVo);
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	/**
	 * 休假管理审核初始化
	 * @param request
	 * @return
	 */
	@RequestMapping("/holidayManageEdit_Check.do")
	@ResponseBody
	public ModelAndView holidayManageEdit_Check(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String hid = request.getParameter("hid");
		String userCode=WebUserUtils.getUserCode();
		String userName=WebUserUtils.getUserName();
		String comCode=WebUserUtils.getComCode();
		modelAndView.addObject("userCode", userCode);
		modelAndView.addObject("userName",userName);
		modelAndView.addObject("comCode",comCode);
	    Long id=null;
		id = Long.parseLong(hid);
		PrpLUserHolidayVo prpLUserHolidayVo = holidayManageService
				.findPrpLUserHolidayVoByPK(id);
		modelAndView.addObject("prpLUserHolidayVo", prpLUserHolidayVo);
		modelAndView.addObject("prpLUserHolidayGrades",prpLUserHolidayVo.getPrpLUserHolidayGrades());
		if(prpLUserHolidayVo.getCheckStatus().equals("2")){
			Boolean bl = false;
			if(!prpLUserHolidayVo.getUserCode().equals(userCode)){
				bl = true;
			}
			modelAndView.addObject("bl", bl);
			modelAndView.setViewName("base/holidayManage/HolidayCheckEdit");
		}else{
			modelAndView.setViewName("base/holidayManage/HolidayManageView");
		}
		return modelAndView;
		
	}
	
	@RequestMapping("/holidayManageFindCheck.do")
	@ResponseBody
	public String search_Check(
			@RequestParam(value="handleStatus")String handleStatus,
			@RequestParam(value="timeEnd")Date timeEnd,
			@RequestParam(value="timeStart")Date timeStart,
			@FormModel(value="prpLUserHolidayVo")PrpLUserHolidayVo prpLUserHolidayVo,
			@RequestParam(value="start",defaultValue="0")Integer start,
			@RequestParam(value="length",defaultValue="10")Integer length){
		String userCode = WebUserUtils.getUserCode();
		List<PrpLUserHolidayVo> list= holidayManageService
				.findAllHolidayManageByHql(prpLUserHolidayVo,handleStatus,timeEnd,timeStart,start,length,userCode);
		String jsonData = ResponseUtils.toDataTableJson(list, "userCode",
				"userName", "comCode", "prpLUserHolidayGrade.gradeName",
				"mobileNo","checkStatus:HolidayCheckStatus","id","checkerName","checkStatus","cancelTime");
		
		return jsonData;
	}
	/**
	 * 判断是员工是否已有申请未审核
	 * @return
	 */
	@RequestMapping("/judge.do")
	@ResponseBody
	public AjaxResult judge(){
		AjaxResult ar = new AjaxResult();
		String userCode=WebUserUtils.getUserCode();
		Date date = new Date();
		List<PrpLUserHolidayVo> list=holidayManageService.findAllHolidayManageByUserCode(userCode);
		String flag="t";
		for(int i=0;i<list.size();i++){
			if(list.get(i).getCheckStatus().equals("2")&&date.getTime()<list.get(i).getEndDate().getTime()){
				flag="f";
				break;
			}
		}
		ar.setData(flag);
		ar.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ar;
	}
	
	@RequestMapping("/cancelInit.do")
	@ResponseBody
	public ModelAndView cancelInit(HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView();
		String hid = request.getParameter("hid");
		PrpLUserHolidayVo prpLUserHolidayVo = holidayManageService
				.findPrpLUserHolidayVoByPK(Long.valueOf(hid));
		modelAndView.addObject("prpLUserHolidayVo",prpLUserHolidayVo);
		modelAndView.setViewName("base/holidayManage/CancelHoliday");
		return modelAndView;
	}

	@RequestMapping("/cancelHoliday.do")
	@ResponseBody
	public AjaxResult cancelHoliday(@RequestParam(value="holidayId")String holidayId){
		AjaxResult result = new AjaxResult();
		SysUserVo userVo = WebUserUtils.getUser();
		holidayManageService.cancelHoliday(holidayId,userVo);
		result.setStatus(org.apache.http.HttpStatus.SC_OK);
		return result;
	}
}
