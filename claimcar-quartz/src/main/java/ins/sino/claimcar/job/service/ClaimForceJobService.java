package ins.sino.claimcar.job.service;

import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "claimForceJobService")
public class ClaimForceJobService {
	private static Logger logger = LoggerFactory.getLogger(ClaimForceJobService.class);
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private PolicyViewService policyViewService;
	
	public void doJob() {
		logger.info(this.getClass().getSimpleName()+"-doSomething is run at:"+new Date());
		
		
		// 1、查询距离报案时间48小时尚未立案的案子(强制立案标准)
		Calendar endcalendar = Calendar.getInstance();
		endcalendar.set(Calendar.DAY_OF_YEAR, endcalendar.get(Calendar.DAY_OF_YEAR) - 2);
		// 2、查询15天以内的案子(防止以前有强制立案失败的案子，算是补传操作)
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 15);
		
		
		List<PrpLRegistVo> prpLRegistVoList = registQueryService.findPrpLRegistVosByTime(calendar.getTime(),endcalendar.getTime());
		if(prpLRegistVoList == null || prpLRegistVoList.isEmpty()){
			return;
		}
		
		
		Map<List<PrpLCMainVo>,String> prpLCMainMap = new HashMap<List<PrpLCMainVo>,String>();
		for(PrpLRegistVo prpLRegistVo:prpLRegistVoList){
			List<PrpLCMainVo> prpLCMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(prpLRegistVo.getRegistNo());
			if(prpLCMainVoList != null && !prpLCMainVoList.isEmpty()){
				prpLCMainMap.put(prpLCMainVoList,prpLRegistVo.getFlowId());
			}
			logger.debug("强制立案执行报案号："+prpLRegistVo.getRegistNo());
		}
		
		for(Entry<List<PrpLCMainVo>,String> entry:prpLCMainMap.entrySet()){
			List<PrpLCMainVo> prpLCMainVoList = entry.getKey();
			String flowId = entry.getValue();
			for(PrpLCMainVo prpLCMainVo:prpLCMainVoList){
					try{
						// 获取当前时间
						Calendar now = Calendar.getInstance();
						String time = String.valueOf(now.get(Calendar.HOUR_OF_DAY));
						logger.debug("强制立案执行到现在是几点："+time);

						// 3、防止以后业务量大，定时服务拉慢数据库，强制立案的时间从凌晨一点到早上八点钟，八点以后如果强制立案未完成，也强制退出，留在下次扫描
						if ("8".equals(time)) {
							return;
						}
						claimTaskService.claimForceJob(prpLCMainVo.getRegistNo(),prpLCMainVo.getPolicyNo(),flowId);
					}catch(Exception e){
						logger.error("强制立案失败，报案号:"+prpLCMainVo.getRegistNo()+",保单号:"+prpLCMainVo.getPolicyNo());
					}
			}
			
		}
		
	}
	
}
