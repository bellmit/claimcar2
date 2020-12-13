package ins.interf;

import ins.framework.lang.Springs;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformTaskService;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;
import ins.sino.claimcar.platform.service.PlatformTaskUploadService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class SendPlatformThread implements Runnable {

	 private static Logger logger = LoggerFactory.getLogger(SendPlatformThread.class);
	 
	 @Autowired
	 CiClaimPlatformTaskService ciClaimPlatformTaskService;
	 @Autowired
	 PlatformTaskUploadService platformTaskUploadService;
	 
	 private CiClaimPlatformTaskVo platformTaskVo;
	 
	 public SendPlatformThread(CiClaimPlatformTaskVo platformTaskVo){
		 this.platformTaskVo = platformTaskVo;
	 }
	 

	@Override
	public void run() {
		init();
		//先把运行状态更新为ON
		platformTaskVo.setOperateStatus(CodeConstants.OperateStatus.ON);
		ciClaimPlatformTaskService.updatePlatformTask(platformTaskVo);
		platformTaskUploadService.uploadPlatform(platformTaskVo);
	}
	
	public void init(){
		if(ciClaimPlatformTaskService == null){
			ciClaimPlatformTaskService = Springs.getBean(CiClaimPlatformTaskService.class);
		}
		if(platformTaskUploadService == null){
			platformTaskUploadService = Springs.getBean(PlatformTaskUploadService.class);
		}
	}
	 
}
