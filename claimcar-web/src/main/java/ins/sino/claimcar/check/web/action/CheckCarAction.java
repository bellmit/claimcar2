/******************************************************************************
 * CREATETIME : 2015年12月4日 下午4:49:32
 ******************************************************************************/
package ins.sino.claimcar.check.web.action;

import freemarker.core.ParseException;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.ScheduleStatus;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.history.CarInfoHistoryResBasePartDataVo;
import ins.sino.claimcar.check.vo.history.CarInfoHistoryResBodyVo;
import ins.sino.claimcar.commom.vo.StringUtils;
import ins.sino.claimcar.platform.service.CheckToPlatformService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/** CheckCarAction
 * @author ★Luwei
 */
@Controller
@RequestMapping(value = "/checkcar")
public class CheckCarAction {
	
	private Logger logger = LoggerFactory.getLogger(CheckCarAction.class);

	// 服务装载
	@Autowired
	CheckHandleService checkHandleService;

	@Autowired
	CheckTaskService checkTaskService;
	
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	CheckToPlatformService checkToPlatformService;
	
	@Autowired 
	RegistQueryService RegistQueryService;

	/**
	 * 车损处理(车辆处理初始化)
	 * @param mainCarId
	 * @return
	 * @modified: ☆Luwei <br>
	 */
	@RequestMapping(value = "/viewCarLossEdit.do/{checkCarId}/{nodeCode}/{status}")
	@ResponseBody
	public ModelAndView CheckCarLossEdit(@PathVariable(value = "checkCarId") Long carId,
	        @PathVariable(value = "nodeCode") String nodeCode,
	        @PathVariable(value = "status") String status) throws ParseException {
		PrpLCheckCarVo checkCarVo = checkHandleService.initPrpLCheckCar(carId);
		PrpLCItemCarVo prpLCItemCarVo = RegistQueryService.findCItemCarByRegistNo(checkCarVo.getRegistNo());
		PrpLRegistVo prpLRegistVo = RegistQueryService.findByRegistNo(checkCarVo.getRegistNo());
		String comCode = prpLRegistVo.getComCode();
		//初始化duty
		PrpLCheckDutyVo checkDutyVo=checkTaskService.findCheckDuty
				(checkCarVo.getRegistNo(),checkCarVo.getSerialNo());
		Map<String,String> carKindMap = new HashMap<String,String>();
		String registNo = checkCarVo.getRegistNo();
		List<PrpLCItemKindVo> cIemKindVoList = checkHandleService.findItemKind(registNo,null);
		for(PrpLCItemKindVo cIemKindVo:cIemKindVoList){
			String kindCode = cIemKindVo.getKindCode();
			String kindName = cIemKindVo.getKindName();
			//新增A1险别
			if("A".equals(kindCode)||"F".equals(kindCode)||"G".equals(kindCode)||"L".equals(kindCode)
					||"Z".equals(kindCode) || "A1".equals(kindCode)){
				carKindMap.put(kindCode,kindName);
			}
 		}
//		carKindMap.put("BZ","机动车交通事故责任强制险");
		// 处理结果集
		ModelAndView mav = new ModelAndView();
		//脱敏处理
		if("3".equals(status)){
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
			if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){//开关
				checkCarVo.getPrpLCheckDriver().setIdentifyNumber(DataUtils .replacePrivacy(checkCarVo.getPrpLCheckDriver().getIdentifyNumber()));
				checkCarVo.getPrpLCheckDriver().setDrivingLicenseNo((DataUtils .replacePrivacy(checkCarVo.getPrpLCheckDriver().getDrivingLicenseNo())));
				checkCarVo.getPrpLCheckCarInfo().setPhone(DataUtils .replacePrivacy(checkCarVo.getPrpLCheckCarInfo().getPhone()));
				checkCarVo.getPrpLCheckDriver().setLinkPhoneNumber((DataUtils .replacePrivacy(checkCarVo.getPrpLCheckDriver().getLinkPhoneNumber())));
			}
		}
		mav.addObject("comCode",comCode);
		mav.addObject("checkCarVo",checkCarVo);
		mav.addObject("checkDutyVo",checkDutyVo);
//		carKindMap.put("","");
		mav.addObject("carKindMap",carKindMap);
		mav.addObject("nodeCode",nodeCode);
		mav.addObject("status",status);
		mav.addObject("prpLCItemCarVo",prpLCItemCarVo);
		if(StringUtils.isNotBlank(carKindMap.get("A"))){
            mav.addObject("kindCode_A","A");
        }
		mav.setViewName("check/checkEdit/CheckCarLossEdit");
		return mav;
	}

	/**
	 * Ajax保存车损信息
	 * @param PrpLCheckCarVo
	 * @return registNo
	 */
	@RequestMapping(value = "/saveCheckCar.do")
	@ResponseBody
	public AjaxResult saveCheckCar(@FormModel("checkCarVo") PrpLCheckCarVo prpLcheckCarVo,
	        @FormModel("checkDutyVo") PrpLCheckDutyVo checkDutyVo)throws ParseException {
		AjaxResult ajaxResult = new AjaxResult();
		String userCode = WebUserUtils.getUserCode();
		// 1.参数校验
		Long carId = prpLcheckCarVo.getCarid();
		try{
			// 保存车损
			if(prpLcheckCarVo.getCarid() == null){
				carId = checkHandleService.savePrpLCheckCar(prpLcheckCarVo,checkDutyVo,userCode,"0");
			}else{
				carId = checkHandleService.updatePrpLCheckCar(prpLcheckCarVo,checkDutyVo,"0");
			}
			PrpLCheckCarVo checkCarVo = checkHandleService.findCheckCarById(carId);
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("carId",carId);
			resultMap.put("serialNo",checkCarVo.getSerialNo());
			resultMap.put("mainCarDuty",checkDutyVo.getIndemnityDuty());
			resultMap.put("mainCarDutyRate",checkDutyVo.getIndemnityDutyRate());
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setDatas(resultMap);
		}
		catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			logger.debug("保存车损信息失败！"+e.getMessage());
		}
		return ajaxResult;
	}

	/**
	 * 新增三者车初始化
	 * @param registNo
	 * @return
	 * @modified: ☆Luwei <br>
	 */
	@RequestMapping(value = "/viewThirdCarAdd.do/{registNo}/{nodeCode}")
	@ResponseBody
	public ModelAndView AddThirdCarEdit(@PathVariable(value = "registNo") String registNo,
	        @PathVariable(value="nodeCode") String nodeCode) throws ParseException {
		ModelAndView mav = new ModelAndView();
		PrpLCheckCarVo checkCarVo=checkHandleService.initAddThirdCar(registNo);
		mav.addObject("checkCarVo",checkCarVo);
		mav.addObject("nodeCode",nodeCode);
		mav.addObject("addCarFlag","addCarFlag");
		mav.addObject("status","2");
		mav.setViewName("check/checkEdit/CheckCarLossEdit");
		return mav;
	}

	/**
	 * 后台校验，，车牌号和车架号不能与已有的相同
	 */
	@RequestMapping(value="/carSaveValid.do")
	@ResponseBody
	public AjaxResult saveValid(int seriNo,String registNo,String licenseNo,String frameNo,
	    String vinNo,Long carId,String idNoType,String idNo,Date checkTime,String licenseType,String drivingLicenseNo,String carKind){
		AjaxResult ajaxResult = new AjaxResult();
		try{
			if(carId==null) carId = 0L;
			String msgData = checkHandleService.saveValidCar
			(seriNo,registNo,licenseNo,frameNo,vinNo,carId,idNoType,idNo,checkTime,licenseType,drivingLicenseNo,carKind);
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(msgData);
		}
		catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			logger.debug("校验失败！"+e.getMessage());
		}
		return ajaxResult;
	}
	
	/**
	 * <pre>删除查勘新增的三者车校验</pre>
	 * @return
	 * @modified:
	 * ☆Luwei(2016年9月5日 下午5:06:34): <br>
	 */
	@RequestMapping(value = "/delThirdCarValid.do")
	@ResponseBody
	public AjaxResult delThirdCarValid(Long thCarId){
		AjaxResult ajaxResult = new AjaxResult();
		PrpLCheckCarVo checkCarVo = checkHandleService.findCheckCarById(thCarId);
		PrpLScheduleDefLossVo defLossVo = scheduleService.findCarDefLossBySerialNo
				(checkCarVo.getRegistNo(),checkCarVo.getSerialNo());
		if(checkCarVo.getScheduleitem()==null||
				(defLossVo!=null&&ScheduleStatus.SCHEDULED_CANCEL.equals(defLossVo.getScheduleStatus()))){
			//调度注销
			ajaxResult.setData(true);
		}else{
			ajaxResult.setData(false);
		}
//		System.out.println(checkCarVo.getScheduleitem()==null);
		return ajaxResult;
	}
	
	/**
	 * 删除查勘新增的三者车
	 * @param thCarId
	 * @return AjaxResult
	 * @throws ParseException
	 */
	@RequestMapping(value = "/dropThirdCar")
	@ResponseBody
	public AjaxResult dropThirdCar(Long thCarId) throws ParseException {
		AjaxResult ajaxResult = new AjaxResult();
		PrpLCheckCarVo checkCarVo = checkHandleService.findCheckCarById(thCarId);
		try{
			checkHandleService.deleteThirdCar(thCarId);
			//删除checkDuty
			if(checkCarVo!=null){
				checkTaskService.deleteCheckDutyByCheckId(checkCarVo.getRegistNo(),checkCarVo.getSerialNo());
			}
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}
		catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			logger.debug("删除查勘新增的三者车失败！"+e.getMessage());
		}
		return ajaxResult;
	}
	
	/** 刷新车辆Tr
	 * @throws ParseException
	 */
	@RequestMapping(value = "/loadReplaceCarTr.ajax")
	public ModelAndView loadReplaceCarTr(Long carId,Integer carTr_index,
	        String saveType,String nodeCode,String status)  {
		ModelAndView mav = new ModelAndView();
		PrpLCheckCarVo checkCarVo = checkHandleService.findCheckCarById(carId);
		if(checkCarVo.getSerialNo()==1){
			mav.addObject("checkMainCarVo",checkCarVo);
			mav.addObject("nodeCode",nodeCode);
			mav.addObject("status",status);
			mav.setViewName("check/checkEdit/CheckMainCar_Tr");
		}else{
			mav.addObject("checkThirdCarVo",checkCarVo);
			mav.addObject("inputIdx",carTr_index);
			mav.addObject("nodeCode",nodeCode);
			mav.addObject("statusFlag",Long.parseLong(status));
			mav.setViewName("check/checkEdit/CheckThirdCarItem");
		}
		return mav;
	}
	
	/** 刷新财产、人伤的损失方
	 */
	@RequestMapping(value = "/refreshPropKey.ajax")
	@ResponseBody
	public AjaxResult refreshPropKey(String registNo) throws ParseException {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			Map<String,String> caoNoMap = checkHandleService.getCarLossParty(registNo);
			ajaxResult.setData(caoNoMap);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}
		catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			logger.debug("刷新财产、人伤的损失方失败！"+e.getMessage());
		}
		return ajaxResult;
	}
	
	//三者车历史出险
	@RequestMapping(value = "/CheckThirdInfo.ajax")
	public ModelAndView CheckThirdInfo(Long carId) {
		List<CarInfoHistoryResBasePartDataVo> list = null;
		ModelAndView mv = new ModelAndView();
		String comCode = WebUserUtils.getComCode();
		// comCode = "1100000000";
		CarInfoHistoryResBodyVo resListVo = checkToPlatformService.thirdCarInfoQuery(carId,comCode);
		if(resListVo!=null){
			list = resListVo.getThirdCarDataListVo();
		}
		mv.addObject("resVo",list);
		mv.setViewName("check/checkEdit/HistoryCheckThirdCar");
		return mv;
	}
	
}
