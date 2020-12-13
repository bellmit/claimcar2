package ins.interf;

import ins.framework.factory.ExecutorFactory;
import ins.framework.lang.Springs;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformTaskService;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;

import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebService(targetNamespace="http://interf/platformTaskService/",serviceName="platformTaskService", endpointInterface = "ins.interf.PlatformTaskService")
public class PlatformTaskService extends SpringBeanAutowiringSupport{
	
	private static Logger logger = LoggerFactory.getLogger(PlatformTaskService.class);
	@Autowired
	CiClaimPlatformTaskService ciClaimPlatformTaskService;
	
	public void doJob(){
		init();
		logger.info(this.getClass().getSimpleName()+"-QueryPlatformTask is run at:"+new Date());
		List<CiClaimPlatformTaskVo> platformTaskVoList = ciClaimPlatformTaskService.findPlatformTaskList();
		ThreadPoolExecutor exec = ExecutorFactory.initConverterExecutor();
		LinkedBlockingQueue<CiClaimPlatformTaskVo> convertXmlQueue = new LinkedBlockingQueue<CiClaimPlatformTaskVo>();
		
		if(platformTaskVoList!=null && platformTaskVoList.size()>0){
			convertXmlQueue.addAll(platformTaskVoList);
		}
		try {
			exec.setRejectedExecutionHandler(new RejectedExecutionHandler() {
				@Override
				public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
					logger.info("Thread Rejected! Waiting for a while !!");
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						logger.error("The thread sleeps for two seconds",e);
					}
					executor.execute(r);
				}
			});
			//art alLet stl core threads initially
			exec.prestartAllCoreThreads();
			while (convertXmlQueue.size() != 0) {
				CiClaimPlatformTaskVo vo = convertXmlQueue.take();
				exec.execute(new SendPlatformThread(vo));
			}
		} catch (InterruptedException e) {
			logger.error("InterruptedException!", e);
		}
	}
	
	public void init() {
		if(ciClaimPlatformTaskService==null){
			ciClaimPlatformTaskService=Springs.getBean(CiClaimPlatformTaskService.class);
		}
	}

}
