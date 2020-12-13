package ins.sino.claimcar.job.service;

import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "claimAvgFloatService")
public class ClaimAvgFloatService {
	
	private static Logger logger = LoggerFactory.getLogger(ClaimForceJobService.class);
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private PolicyViewService policyViewService;
	
	
	public void doFloatCarProp(){
		
		logger.info(this.getClass().getSimpleName()+"-doClaimAvgFloat is run at:"+new Date());
		
		// 1.获取需要案均上浮的报案号
		
		// 查询距离今天超过15天的案件
		Calendar endcalendar = Calendar.getInstance();
		endcalendar.set(Calendar.DAY_OF_YEAR, endcalendar.get(Calendar.DAY_OF_YEAR) - 15);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 30);

		List<PrpLRegistVo> prpLRegistVoList = registQueryService.findPrpLRegistVoListByReportTime(calendar.getTime(),endcalendar.getTime());
		
		if(prpLRegistVoList == null || prpLRegistVoList.isEmpty()){
			return;
		}
		
		// 根据报案号查询车辆和财产的定核损主表，状态为未通过核损，或者没有定核损主表的案件，执行案均上浮
		List<PrpLRegistVo> registVoList = new ArrayList<PrpLRegistVo>();
		for(PrpLRegistVo registVo : prpLRegistVoList){
			boolean lossState = claimTaskService.adjustPropLossState(registVo.getRegistNo());
			if(lossState){
				registVoList.add(registVo);
			}
		}
		// 2.执行15天案均上浮
		if(registVoList == null || registVoList.isEmpty()){
			return;
		}
		for(PrpLRegistVo registVo:registVoList){
			logger.debug("案均上浮执行报案号："+registVo.getRegistNo());
			claimTaskService.updateClaimFeeForFifteen(registVo);
			logger.debug("案均上浮执行成功");
		}
		
	}
	
	public void doFloatPers(){
		
		logger.info(this.getClass().getSimpleName()+"-doClaimAvgFloatPers is run at:"+new Date());
		// 1.获取需要30天上浮人伤估损的案件
		Calendar endcalendar = Calendar.getInstance();
		endcalendar.set(Calendar.DAY_OF_YEAR, endcalendar.get(Calendar.DAY_OF_YEAR) - 30);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 45);
		
		List<PrpLRegistVo> prpLRegistVoList = registQueryService.findPrpLRegistVoListByReportTime(calendar.getTime(),endcalendar.getTime());
		
		if(prpLRegistVoList == null || prpLRegistVoList.isEmpty()){
			return;
		}
		// 查询查勘人伤表，报案超过30天且未通过核损，或者没有定损主表的，执行案均上浮
		List<PrpLRegistVo> registVoList = new ArrayList<PrpLRegistVo>();
		for(PrpLRegistVo registVo : prpLRegistVoList){
			boolean lossState = claimTaskService.adjustPersLossState(registVo.getRegistNo());
			if(lossState){
				registVoList.add(registVo);
			}
		}
		// 2.执行30天案均上浮
		if(registVoList == null || registVoList.isEmpty()){
			return;
		}
		for(PrpLRegistVo registVo:registVoList){
			logger.debug("案均上浮人伤执行报案号："+registVo.getRegistNo());
			claimTaskService.updateClaimFeeForThirty(registVo);
			logger.debug("案均上浮人伤执行成功");
		}
		
	}
	

}
