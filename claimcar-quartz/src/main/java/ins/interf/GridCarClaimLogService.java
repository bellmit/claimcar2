package ins.interf;

import java.util.Date;

import ins.framework.lang.Springs;
import ins.sino.powerGridCarClaimLog.service.PowerGridCarClaimLogService;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebService(targetNamespace="http://interf/gridCarClaimLogService/",serviceName="gridCarClaimLogService",endpointInterface = "ins.interf.GridCarClaimLogService")
public class GridCarClaimLogService extends SpringBeanAutowiringSupport{
	
	private static Logger logger = LoggerFactory.getLogger(GridCarClaimLogService.class);
	@Autowired
	private PowerGridCarClaimLogService powerGridCarClaimLogService;
	
	/**
	 * 广东电网理赔信息同步
	 * @throws Exception 
	 * @author CSVC
	 */
	public void doClaimInfoSynchronous() {
		init();
		logger.info(this.getClass().getSimpleName()+"-doClaimInfoSynchronous is run at:"+new Date());
		try {
			powerGridCarClaimLogService.doClaimInfoSynchronous();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void init() {
		if(powerGridCarClaimLogService==null){
			powerGridCarClaimLogService = (PowerGridCarClaimLogService)Springs.getBean(PowerGridCarClaimLogService.class);
        }
	}
}
