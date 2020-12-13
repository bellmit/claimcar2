package ins.sino.claimcar.check.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.apc.annotation.AvoidRepeatableCommit;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.ObjectUtils;
import ins.sino.claimcar.carplatform.po.ClaimVinNoHis;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.UpdateVINService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
@RequestMapping("/updateVIN")
public class UpdateVINAction {

	private static Logger logger = LoggerFactory.getLogger(UpdateVINAction.class);
	
	@Autowired
	private UpdateVINService updateVINService;
	@Autowired
	private RegistService registService;
	@Autowired
	private ScheduleTaskService scheduleTaskService;
	@Autowired
	private CheckHandleService checkHandleService;
	@Autowired
	LossCarService lossCarService;
	
	@RequestMapping("/initQuery.do")
	@ResponseBody
	public ModelAndView initQuery() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("updateVIN/UpdateVINList");
		return modelAndView;
	}
	
	@RequestMapping(value = "/VINSearch.do", method = RequestMethod.POST)
	@ResponseBody
	public String search(@RequestParam("handleStatus") String handleStatus,
			@FormModel("prpLWfTaskQueryVo") PrpLWfTaskQueryVo queryVo,// 页面组装VO
			@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
			@RequestParam(value = "length", defaultValue = "10") Integer length) throws ParseException {
		
		queryVo.setRegistNo(queryVo.getRegistNo().replaceAll("\\s*",""));// 去掉所有空格符
		queryVo.setPolicyNo(queryVo.getPolicyNo().replaceAll("\\s*",""));
		queryVo.setComCode(WebUserUtils.getComCode());
		queryVo.setUserCode(WebUserUtils.getUserCode());
		queryVo.setHandleStatus(handleStatus);
		
		ResultPage<PrpLWfTaskQueryVo> page = updateVINService.search(queryVo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(page,"registNo","policyNo","insuredName","licenseNo",
				"operateTime","operateUser");
		return jsonData;
	}
	
	@RequestMapping("/VINEdit.do")
	@ResponseBody
	public ModelAndView VINEdit(String registNo,String handleStatus){
		
		ModelAndView modelAndView = new ModelAndView();
		List<PrpLDlossCarMainVo> resultList = new ArrayList<PrpLDlossCarMainVo>();
		PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
		List<PrpLScheduleDefLossVo> scheduleDefLossList = 
				scheduleTaskService.getScheduleDefLossByLossType(registNo, "1");
		
		if(scheduleDefLossList!=null && scheduleDefLossList.size()>0){
			for(PrpLScheduleDefLossVo scheduleDefLossVo:scheduleDefLossList){
				PrpLDlossCarMainVo resultVo = new PrpLDlossCarMainVo();
				PrpLCheckCarVo checkCarVo = 
						checkHandleService.findCarIdBySerialNoAndRegistNo(scheduleDefLossVo.getSerialNo(), registNo);
				List<PrpLDlossCarMainVo> lossCarList = 
						lossCarService.findLossCarMainBySerialNo(registNo, scheduleDefLossVo.getSerialNo());
				//如果查勘有数据就取查勘的数据，否则取定损
				if(checkCarVo!=null && checkCarVo.getCarid()!=null){
					resultVo.setRegistNo(registNo);
					resultVo.setDeflossCarType(scheduleDefLossVo.getSerialNo()==1 ? "标的车":"三者车");
					resultVo.setLicenseNo(checkCarVo.getPrpLCheckCarInfo().getLicenseNo());
					resultVo.setVinNo(checkCarVo.getPrpLCheckCarInfo().getVinNo());
					resultVo.setSerialNo(scheduleDefLossVo.getSerialNo());
					resultList.add(resultVo);
				}else if(lossCarList!=null && lossCarList.size()>0){
					PrpLDlossCarInfoVo carInfoVo = 
							lossCarService.findPrpLDlossCarInfoVoById(lossCarList.get(0).getCarId());
					if(carInfoVo!=null){
						resultVo.setRegistNo(registNo);
						resultVo.setDeflossCarType(scheduleDefLossVo.getSerialNo()==1 ? "标的车":"三者车");
						resultVo.setLicenseNo(carInfoVo.getLicenseNo());
						resultVo.setVinNo(carInfoVo.getVinNo());
						resultVo.setSerialNo(scheduleDefLossVo.getSerialNo());
						resultList.add(resultVo);
					}
				}
			}
		}
		
		modelAndView.addObject("PrpLDlossCarMainVoList",resultList);
		modelAndView.addObject("vinsSize",resultList.size());
		modelAndView.addObject("registVo",registVo);
		modelAndView.addObject("registExtVo",registVo.getPrpLRegistExt());
		modelAndView.addObject("handleStatus",handleStatus);
		modelAndView.setViewName("updateVIN/UpdateVINEdit");
		return modelAndView;
	}
	
	@RequestMapping(value = "/updateVIN.do")
	@ResponseBody
	public AjaxResult updateVIN(@FormModel("carList") List<PrpLDlossCarMainVo> carList) {
		AjaxResult ajaxResult = new AjaxResult();
		String oldVinNo = "UnKnown";
		try{
			if(carList!=null && carList.size()>0){
				String registNo = null;
				for(PrpLDlossCarMainVo carVo:carList){
					if(carVo != null){
						registNo = carVo.getRegistNo();
						PrpLCheckCarVo checkCarVo = 
								checkHandleService.findCarIdBySerialNoAndRegistNo(carVo.getSerialNo(), carVo.getRegistNo());
						List<PrpLDlossCarMainVo> lossCarList = 
								lossCarService.findLossCarMainBySerialNo(carVo.getRegistNo(), carVo.getSerialNo());
						
						if(checkCarVo!=null && checkCarVo.getPrpLCheckCarInfo()!=null && 
								checkCarVo.getPrpLCheckCarInfo().getCarid()!=null){
							PrpLCheckCarInfoVo prpLCheckCarInfo = checkCarVo.getPrpLCheckCarInfo();
							if( !ObjectUtils.isEmpty(prpLCheckCarInfo.getVinNo())){
								oldVinNo = prpLCheckCarInfo.getVinNo();
							}
							prpLCheckCarInfo.setVinNo(carVo.getVinNo());
							prpLCheckCarInfo.setFrameNo(carVo.getVinNo());
							checkHandleService.updatePrplCheckCarInfo(prpLCheckCarInfo);
						}
						
						if(lossCarList!=null && lossCarList.size()>0){
							for(PrpLDlossCarMainVo lossCarMainVo:lossCarList){
								PrpLDlossCarInfoVo carInfoVo = lossCarService.findPrpLDlossCarInfoVoById(lossCarMainVo.getCarId());
								if( !ObjectUtils.isEmpty(carInfoVo.getVinNo())){
									oldVinNo = carInfoVo.getVinNo();
								}
								carInfoVo.setVinNo(carVo.getVinNo());
								carInfoVo.setFrameNo(carVo.getVinNo());
								lossCarService.updateLossCarInfo(carInfoVo);
							}
						}
						ClaimVinNoHis claimvinNoHis = new ClaimVinNoHis();
						claimvinNoHis.setRegistNo(registNo);
						claimvinNoHis.setSerialNo(carVo.getSerialNo());
						claimvinNoHis.setLicenseNo(carVo.getLicenseNo());
						claimvinNoHis.setOldVinNo(oldVinNo);
						claimvinNoHis.setNewVinNo(carVo.getVinNo());
						claimvinNoHis.setUpdateUser(WebUserUtils.getUserCode());
						claimvinNoHis.setUpdateTime(new Date());
						updateVINService.saveClaimvinnohis(claimvinNoHis);
					}
				}
				PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
				if(registVo!=null && registVo.getRegistNo()!=null){
					registVo.setUpdateVINFlag("1");
					registVo.setUpdateVINTime(new Date());
					registVo.setUpdateVINUser(WebUserUtils.getUserName());
					registService.updatePrpLRegist(registVo);
				}
			}
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		}catch(Exception e){
			e.printStackTrace();
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_NO_CONTENT);
		}
		
		return ajaxResult;
	}

	@RequestMapping(value = "/updateVINByRegistNoAndSerialNo.do")
	@ResponseBody
	@AvoidRepeatableCommit
	public AjaxResult updateVIN(String registNo,Integer serialNo,String newVinNo,String licenseNo,String oldVinNo) {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			PrpLCheckCarVo checkCarVo = checkHandleService.findCarIdBySerialNoAndRegistNo(serialNo,registNo);
			List<PrpLDlossCarMainVo> lossCarList = lossCarService.findLossCarMainBySerialNo(registNo,serialNo);

			if(checkCarVo!=null&&checkCarVo.getPrpLCheckCarInfo()!=null&&checkCarVo.getPrpLCheckCarInfo().getCarid()!=null){
				PrpLCheckCarInfoVo prpLCheckCarInfo = checkCarVo.getPrpLCheckCarInfo();
				prpLCheckCarInfo.setVinNo(newVinNo);
				prpLCheckCarInfo.setFrameNo(newVinNo);
				checkHandleService.updatePrplCheckCarInfo(prpLCheckCarInfo);
			}

			if(lossCarList!=null&&lossCarList.size()>0){
				for(PrpLDlossCarMainVo lossCarMainVo:lossCarList){
					PrpLDlossCarInfoVo carInfoVo = lossCarService.findPrpLDlossCarInfoVoById(lossCarMainVo.getCarId());
					carInfoVo.setVinNo(newVinNo);
					carInfoVo.setFrameNo(newVinNo);
					lossCarService.updateLossCarInfo(carInfoVo);
				}
			}
			PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
			if(registVo!=null&&registVo.getRegistNo()!=null){
				registVo.setUpdateVINFlag("1");
				registVo.setUpdateVINTime(new Date());
				registVo.setUpdateVINUser(WebUserUtils.getUserName());
				registService.updatePrpLRegist(registVo);
			}
			ClaimVinNoHis claimvinNoHis = new ClaimVinNoHis();
			claimvinNoHis.setRegistNo(registNo);
			claimvinNoHis.setSerialNo(serialNo);
			claimvinNoHis.setLicenseNo(licenseNo);
			claimvinNoHis.setOldVinNo(oldVinNo);
			claimvinNoHis.setNewVinNo(newVinNo);
			claimvinNoHis.setUpdateUser(WebUserUtils.getUserCode());
			claimvinNoHis.setUpdateTime(new Date());
			updateVINService.saveClaimvinnohis(claimvinNoHis);
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
			ajaxResult.setStatusText("修改成功！");
		}catch(Exception e){
			logger.error("updateVINByRegistNoAndSerialNo error: ",e);
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_NO_CONTENT);
		}
		return ajaxResult;
	}
}
