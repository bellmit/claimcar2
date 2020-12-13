package ins.interf;

import ins.framework.lang.Springs;
import ins.sino.claimcar.trafficplatform.service.SzpoliceCaseService;
import ins.sino.claimcar.trafficplatform.service.SzpoliceClaimInfoService;
import ins.sino.claimcar.trafficplatform.service.SzpoliceRegistService;

import java.util.Date;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;


@WebService(targetNamespace="http://interf/claimToSzPoliceInfoService/",serviceName="claimToSzPoliceInfoService",endpointInterface = "ins.interf.ClaimToSzPoliceInfoService")
public class ClaimToSzPoliceInfoService extends SpringBeanAutowiringSupport{

	private static Logger logger = LoggerFactory.getLogger(ClaimToSzPoliceInfoService.class);
	
	@Autowired
	private SzpoliceCaseService szpoliceCaseService;
	@Autowired
	private SzpoliceClaimInfoService szpoliceClaimInfoService;
	@Autowired
	private SzpoliceRegistService szpoliceRegistService;
	
	public void accidentInfoDownLoad() {
		init();
		logger.info(this.getClass().getSimpleName()+"-ClaimToPoliceInfoService-accidentInfoDownLoad is run at:"+new Date());
		szpoliceCaseService.accidentInfoDownLoad();

	}
	
	public void doClaim() {
		init();
		logger.info(this.getClass().getSimpleName()+"-ClaimToPoliceInfoService is run at:"+new Date());
		szpoliceClaimInfoService.settleClaimUpload();
	}

	public void init() {
        if(szpoliceCaseService==null){
        	szpoliceCaseService = (SzpoliceCaseService)Springs.getBean(SzpoliceCaseService.class);
        }
        if(szpoliceClaimInfoService==null){
        	szpoliceClaimInfoService = (SzpoliceClaimInfoService)Springs.getBean(SzpoliceClaimInfoService.class);
        }
		if(szpoliceRegistService==null){
			szpoliceRegistService = (SzpoliceRegistService)Springs.getBean(SzpoliceRegistService.class);
		}
    }

	public void reportCaseUpload() {
		init();
		logger.info(this.getClass().getSimpleName()+"-szpoliceRegistService reportCaseUpload is run at:"+new Date());
		szpoliceRegistService.reportCaseUpload("quartz");
	}

	public void dispatchInfoDownload() {
		init();
		logger.info(this.getClass().getSimpleName()+"-szpoliceRegistService dispatchInfoDownload is run at:"+new Date());
		szpoliceCaseService.dispatchInfoDownload();
	}
	public void warningInstanceDownload() {
		init();
		logger.info(this.getClass().getSimpleName()+"-warningInstanceDownload is run at:"+new Date());
		szpoliceCaseService.warningInstanceDownload();
	}
}
