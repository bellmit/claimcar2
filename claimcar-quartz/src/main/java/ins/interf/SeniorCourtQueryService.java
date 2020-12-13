package ins.interf;


import ins.framework.lang.Springs;
import ins.sino.claimcar.court.service.CourtService;
import ins.sino.claimcar.court.vo.PrpLCourtMessageVo;

import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebService(targetNamespace="http://interf/seniorCourtQueryService/",serviceName="seniorCourtQueryService")
public class SeniorCourtQueryService extends SpringBeanAutowiringSupport {

	private Logger logger = LoggerFactory.getLogger(SeniorCourtQueryService.class);
	
	@Autowired
	private CourtService courtService;
	
	public void doJob() {
		logger.info(this.getClass().getSimpleName()+"-QueryCourt is run at:"+new Date());
		init();
		try{
			String maxTimes = SpringProperties.getProperty("COURT_TIMES");	//最大执行次数
			List<PrpLCourtMessageVo> prpLCourtMessageVoList = courtService.searchPrpLCourtMessageVo("0", maxTimes);
			logger.info("===============高院查询的案件数量："+(prpLCourtMessageVoList!=null?prpLCourtMessageVoList.size():"0"));
			
			if(prpLCourtMessageVoList!=null && prpLCourtMessageVoList.size()>0){
				for(PrpLCourtMessageVo courtMsgVo:prpLCourtMessageVoList){
					try{
						courtService.seniorCourtQuery(courtMsgVo);
					}catch(Exception e){
						logger.error(courtMsgVo.getCaseNo()+"定时任务高院查询接口报错：",e);
					}
				}
			}
		}catch(Exception e){
			logger.error("定时任务高院查询接口报错：",e);
		}
	}
	
	public void init() {
		if(courtService==null){
			courtService=Springs.getBean(CourtService.class);
		}
	}
	
}
