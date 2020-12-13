package ins.sino.claimcar.realtimequery.web.action;

import java.util.ArrayList;
import java.util.List;

import ins.sino.claimcar.realtimequery.service.RealTimeQueryService;
import ins.sino.claimcar.realtimequery.vo.PrpLAntiFraudVo;
import ins.sino.claimcar.realtimequery.vo.PrpLBasicsInfoVo;
import ins.sino.claimcar.realtimequery.vo.PrpLCasualtyInforVo;
import ins.sino.claimcar.realtimequery.vo.PrpLDamageInfoVo;
import ins.sino.claimcar.realtimequery.vo.PrpLInjuredDetailsVo;
import ins.sino.claimcar.realtimequery.vo.PrpLPropertyLossVo;
import ins.sino.claimcar.realtimequery.vo.PrpLRealTimeQueryVo;
import ins.sino.claimcar.realtimequery.vo.PrpLVehicleInfoVo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/realTimeQueryAction")
public class RealTimeQueryAction {
	private static Logger logger = LoggerFactory.getLogger(RealTimeQueryAction.class);
	@Autowired
	RealTimeQueryService realTimeQueryService;
	
	/*
	 * 显示反欺诈平台信息
	 */
	@RequestMapping(value="/realTimeQueryView.do")
	public ModelAndView viewCheck(String registNo) {
		ModelAndView mav = new ModelAndView();
		List<PrpLRealTimeQueryVo> findPrpLRealTimeQueryVos = realTimeQueryService.findPrpLRealTimeQueryVo(registNo);
		if(findPrpLRealTimeQueryVos != null && findPrpLRealTimeQueryVos.size() > 0){
			//根据disType分类
			List<PrpLRealTimeQueryVo> realTimeQueryVehicle = new ArrayList<PrpLRealTimeQueryVo>();
			List<PrpLRealTimeQueryVo> realTimeQueryPerson = new ArrayList<PrpLRealTimeQueryVo>();
			List<PrpLRealTimeQueryVo> realTimeQueryReportPhone = new ArrayList<PrpLRealTimeQueryVo>();
			for (PrpLRealTimeQueryVo prpLRealTimeQueryVo : findPrpLRealTimeQueryVos) {
				if(StringUtils.isNotBlank(prpLRealTimeQueryVo.getDisType()) && prpLRealTimeQueryVo.getDisType().contains("1")){
					realTimeQueryVehicle.add(prpLRealTimeQueryVo);
				}else if(StringUtils.isNotBlank(prpLRealTimeQueryVo.getDisType()) && prpLRealTimeQueryVo.getDisType().contains("2")){
					realTimeQueryPerson.add(prpLRealTimeQueryVo);
				}else if(StringUtils.isNotBlank(prpLRealTimeQueryVo.getDisType()) && prpLRealTimeQueryVo.getDisType().contains("3")){
					realTimeQueryReportPhone.add(prpLRealTimeQueryVo);
				}
			}
			if(realTimeQueryVehicle != null && realTimeQueryVehicle.size() > 0){
				List<PrpLVehicleInfoVo> allVehicleList = new ArrayList<PrpLVehicleInfoVo>();
				for (PrpLRealTimeQueryVo prpLRealTimeQueryVo : realTimeQueryVehicle) {
					Long id = prpLRealTimeQueryVo.getId();
					List<PrpLVehicleInfoVo> prpLVehicleInfoVos = realTimeQueryService.findPrpLVehicleInfoVos(prpLRealTimeQueryVo.getReportNo(), id);
					if(prpLVehicleInfoVos != null && prpLVehicleInfoVos.size() > 0){
						for (PrpLVehicleInfoVo prpLVehicleInfoVo : prpLVehicleInfoVos) {
							prpLVehicleInfoVo.setChangeTime(prpLRealTimeQueryVo.getChangeTime());
							allVehicleList.add(prpLVehicleInfoVo);
						}
					}
				}
				mav.addObject("vehicleInfoVos", allVehicleList);
			}
			if(realTimeQueryPerson != null && realTimeQueryPerson.size() > 0){
				List<PrpLCasualtyInforVo> allPersonList = new ArrayList<PrpLCasualtyInforVo>();
				for (PrpLRealTimeQueryVo prpLRealTimeQueryVo : realTimeQueryPerson) {
					Long id = prpLRealTimeQueryVo.getId();
					List<PrpLCasualtyInforVo> prpLCasualtyInforVos = realTimeQueryService.findPrpLCasualtyInforVos(prpLRealTimeQueryVo.getReportNo(), id);
					if(prpLCasualtyInforVos != null && prpLCasualtyInforVos.size() > 0){
						for (PrpLCasualtyInforVo prpLCasualtyInforVo : prpLCasualtyInforVos) {
							prpLCasualtyInforVo.setChangeTime(prpLRealTimeQueryVo.getChangeTime());
							allPersonList.add(prpLCasualtyInforVo);
						}
					}
				}
				mav.addObject("perosonInfoVos", allPersonList);
			}
			if(realTimeQueryReportPhone != null && realTimeQueryReportPhone.size() > 0){
				List<PrpLAntiFraudVo> allReportPhoneList = new ArrayList<PrpLAntiFraudVo>();
				for (PrpLRealTimeQueryVo prpLRealTimeQueryVo : realTimeQueryReportPhone) {
					Long id = prpLRealTimeQueryVo.getId();
					List<PrpLAntiFraudVo> prpLAntiFraudVos = realTimeQueryService.findPrpLAntiFraudVos(prpLRealTimeQueryVo.getReportNo(), id);
					if(prpLAntiFraudVos != null && prpLAntiFraudVos.size() > 0){
						for (PrpLAntiFraudVo prpLAntiFraudVo : prpLAntiFraudVos) {
							prpLAntiFraudVo.setChangeTime(prpLRealTimeQueryVo.getChangeTime());
							allReportPhoneList.add(prpLAntiFraudVo);
						}
					}
				}
				mav.addObject("reportPhoneInfoVos", allReportPhoneList);
			}
			
		}
		mav.addObject("registNo", registNo);
		mav.setViewName("verifyClaim/realtimequery/RealTimeQueryEdit");
		return mav;
	}
	
	/*
	 * 车辆信息详情
	 */
	@RequestMapping(value="/vehicleInfoDetils.do")
	public ModelAndView vehicleInfoDetils(String registNo,Long upperId) {
		ModelAndView mav = new ModelAndView();
		List<PrpLVehicleInfoVo> prpLVehicleInfoVos = realTimeQueryService.findPrpLVehicleInfoVos(registNo, upperId);
		if(prpLVehicleInfoVos != null && prpLVehicleInfoVos.size() > 0){
			mav.addObject("vehicleInfoVos", prpLVehicleInfoVos);
		}
		
		List<PrpLAntiFraudVo> prpLAntiFraudVos = realTimeQueryService.findPrpLAntiFraudVos(registNo, upperId);
		if(prpLAntiFraudVos != null && prpLAntiFraudVos.size() > 0){
			for (PrpLAntiFraudVo prpLAntiFraudVo : prpLAntiFraudVos) {
				if(StringUtils.isNotBlank(prpLAntiFraudVo.getName())){
					prpLAntiFraudVo.setAntiFraudName(prpLAntiFraudVo.getName());
				}
				if(prpLAntiFraudVo.getTime() != null){
					prpLAntiFraudVo.setAntiFraudTime(prpLAntiFraudVo.getTime());
				}
			}
			mav.addObject("prpLAntiFraudVos", prpLAntiFraudVos);
		}

		List<PrpLBasicsInfoVo> prpLBasicsInfoVos = realTimeQueryService.findPrpLBasicsInfoVos(registNo, upperId);
		if(prpLBasicsInfoVos != null && prpLBasicsInfoVos.size() > 0){
			mav.addObject("prpLBasicsInfoVos", prpLBasicsInfoVos);
		}
		
		List<PrpLPropertyLossVo> prpLPropertyLossVos = realTimeQueryService.findPrpLPropertyLossVos(registNo, upperId);
		if(prpLPropertyLossVos != null && prpLPropertyLossVos.size() > 0){
			mav.addObject("prpLPropertyLossVos", prpLPropertyLossVos);
		}
		
		//mav.addObject("registNo", registNo);
		mav.setViewName("verifyClaim/realtimequery/VehicleInfoDetils");
		return mav;
	}
	
	/*
	 * 人员信息详情
	 */
	@RequestMapping(value="/personInfoDetils.do")
	public ModelAndView personInfoDetils(String registNo,Long upperId) {
		ModelAndView mav = new ModelAndView();
		List<PrpLVehicleInfoVo> prpLVehicleInfoVos = realTimeQueryService.findPrpLVehicleInfoVos(registNo, upperId);
		if(prpLVehicleInfoVos != null && prpLVehicleInfoVos.size() > 0){
			mav.addObject("vehicleInfoVos", prpLVehicleInfoVos);
		}
		
		List<PrpLCasualtyInforVo> prpLCasualtyInforVos = realTimeQueryService.findPrpLCasualtyInforVos(registNo, upperId);
		if(prpLCasualtyInforVos != null && prpLCasualtyInforVos.size() > 0){
			mav.addObject("prpLCasualtyInforVos", prpLCasualtyInforVos);
		}
		
		List<PrpLAntiFraudVo> prpLAntiFraudVos = realTimeQueryService.findPrpLAntiFraudVos(registNo, upperId);
		if(prpLAntiFraudVos != null && prpLAntiFraudVos.size() > 0){
			for (PrpLAntiFraudVo prpLAntiFraudVo : prpLAntiFraudVos) {
				if(StringUtils.isNotBlank(prpLAntiFraudVo.getName())){
					prpLAntiFraudVo.setAntiFraudName(prpLAntiFraudVo.getName());
				}
				if(prpLAntiFraudVo.getTime() != null){
					prpLAntiFraudVo.setAntiFraudTime(prpLAntiFraudVo.getTime());
				}
			}
			mav.addObject("prpLAntiFraudVos", prpLAntiFraudVos);
		}

		List<PrpLBasicsInfoVo> prpLBasicsInfoVos = realTimeQueryService.findPrpLBasicsInfoVos(registNo, upperId);
		if(prpLBasicsInfoVos != null && prpLBasicsInfoVos.size() > 0){
			mav.addObject("prpLBasicsInfoVos", prpLBasicsInfoVos);
		}
		
		List<PrpLPropertyLossVo> prpLPropertyLossVos = realTimeQueryService.findPrpLPropertyLossVos(registNo, upperId);
		if(prpLPropertyLossVos != null && prpLPropertyLossVos.size() > 0){
			mav.addObject("prpLPropertyLossVos", prpLPropertyLossVos);
		}
		
		//mav.addObject("registNo", registNo);
		mav.setViewName("verifyClaim/realtimequery/PersonInfoDetils");
		return mav;
	}
	
	/*
	 * 报案电话信息详情
	 */
	@RequestMapping(value="/reportPhoneInfoDetils.do")
	public ModelAndView reportPhoneInfoDetils(String registNo,Long upperId) {
		ModelAndView mav = new ModelAndView();
		
		List<PrpLAntiFraudVo> prpLAntiFraudVos = realTimeQueryService.findPrpLAntiFraudVos(registNo, upperId);
		if(prpLAntiFraudVos != null && prpLAntiFraudVos.size() > 0){
			for (PrpLAntiFraudVo prpLAntiFraudVo : prpLAntiFraudVos) {
				if(StringUtils.isNotBlank(prpLAntiFraudVo.getName())){
					prpLAntiFraudVo.setAntiFraudName(prpLAntiFraudVo.getName());
				}
				if(prpLAntiFraudVo.getTime() != null){
					prpLAntiFraudVo.setAntiFraudTime(prpLAntiFraudVo.getTime());
				}
			}
			mav.addObject("prpLAntiFraudVos", prpLAntiFraudVos);
		}
		
		List<PrpLBasicsInfoVo> prpLBasicsInfoVos = realTimeQueryService.findPrpLBasicsInfoVos(registNo, upperId);
		if(prpLBasicsInfoVos != null && prpLBasicsInfoVos.size() > 0){
			mav.addObject("prpLBasicsInfoVos", prpLBasicsInfoVos);
		}
		
		List<PrpLPropertyLossVo> prpLPropertyLossVos = realTimeQueryService.findPrpLPropertyLossVos(registNo, upperId);
		if(prpLPropertyLossVos != null && prpLPropertyLossVos.size() > 0){
			mav.addObject("prpLPropertyLossVos", prpLPropertyLossVos);
		}
		
		//mav.addObject("registNo", registNo);
		mav.setViewName("verifyClaim/realtimequery/ReportPhoneInfoDetils");
		return mav;
	}
	
	/*
	 * 伤亡详情
	 */
	@RequestMapping(value="/showInjuredDetails.do")
	public ModelAndView showInjuredDetails(Long upperId) {
		ModelAndView mav = new ModelAndView();
		
		List<PrpLInjuredDetailsVo> prpLInjuredDetailsVos = realTimeQueryService.findPrpLInjuredDetailsVos(upperId);
		if(prpLInjuredDetailsVos != null && prpLInjuredDetailsVos.size() > 0){
			mav.addObject("prpLInjuredDetailsVos", prpLInjuredDetailsVos);
		}
		
		mav.setViewName("verifyClaim/realtimequery/InjuredDetails");
		return mav;
	}
	
	/*
	 * 车损详情
	 */
	@RequestMapping(value="/showDamageInfoDetails.do")
	public ModelAndView showDamageInfoDetails(Long upperId) {
		ModelAndView mav = new ModelAndView();
		
		List<PrpLDamageInfoVo> prpLDamageInfoVos = realTimeQueryService.findPrpLDamageInfoVos(upperId);
		if(prpLDamageInfoVos != null && prpLDamageInfoVos.size() > 0){
			mav.addObject("prpLDamageInfoVos", prpLDamageInfoVos);
		}
		
		mav.setViewName("verifyClaim/realtimequery/DamageDetails");
		return mav;
	}
}
