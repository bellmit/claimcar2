package ins.sino.claimcar.platform.service;

import java.lang.reflect.Method;
import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformTaskService;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;
import ins.sino.claimcar.carplatform.vo.PrpDReUploadConfigVo;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.platform.service.PlatformTaskUploadService;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("platformTaskUploadService")
public class PlatformTaskUploadServiceImpl implements PlatformTaskUploadService {

	private Logger logger = LoggerFactory.getLogger(PlatformTaskUploadServiceImpl.class);
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	VerifyClaimService verifyClaimService;
	@Autowired
	EndCasePubService endCasePubService;
	@Autowired
	CiClaimPlatformTaskService ciClaimPlatformTaskService;
	@Autowired
	BeanFactory factory;
	@Autowired
	CiClaimPlatformLogService ciClaimPlatformLogService;
	
	
	@Override
	public void uploadPlatform(CiClaimPlatformTaskVo platformTaskVo) {
		logger.info(this.getClass().getSimpleName()+"-"+platformTaskVo.getRequestName()+" is run at:"+new Date()+"-bussNo:"+platformTaskVo.getBussNo());
		try{
			//如果上传次数大于0则是补传，要更新LOG表的status
			if(platformTaskVo.getRedoTimes()>0){
				ciClaimPlatformLogService.platformLogUpdateByTaskId(platformTaskVo.getId(),CodeConstants.platformStatus.Cancel);
			}
		}catch(Exception e){
			logger.error(platformTaskVo.getRequestType()+"===更新CiClaimPlatformLog失败"+platformTaskVo.getBussNo(),e);
		}
		try{
			platformTaskVo.setStartDate(new Date());
			PrpDReUploadConfigVo reUploadConfigVo = ciClaimPlatformTaskService.findPrpDReUploadConfig(platformTaskVo.getRequestType());
			if(reUploadConfigVo!=null){
				String className = reUploadConfigVo.getClassName();// 取得类名
				String method = reUploadConfigVo.getMethodName();// 取得方法名
				String service = reUploadConfigVo.getServicerName();// 获得服务名称
				String[] paramTypes = reUploadConfigVo.getParamTypes().split(",");//获取参数类型
				String[] params = platformTaskVo.getTaskParams().split(",");//参数
				Class<?>[] paramTypeClasses = new Class<?>[paramTypes.length];
				Object[] paramObjs = new Object[params.length];
				
				for(int i = 0; i<paramTypes.length; i++ ){
					paramTypeClasses[i] = Class.forName(paramTypes[i]);
				}
				for(int i = 0; i<params.length; i++ ){
					if(CiClaimPlatformTaskVo.class.getName().equals(params[i])){
						paramObjs[i] = (Object)platformTaskVo;
					}else{
						paramObjs[i] = (Object)params[i];
					}
				}
				Class<?> cls = Class.forName(className);// 实例化一个class对象
				Object obj = factory.getBean(service,cls);// 获取对象
				Method setter = cls.getMethod(method,paramTypeClasses);// 获取执行方法
				setter.invoke(obj,paramObjs);// 执行方法
			}
		}catch(Exception e){
			logger.error(platformTaskVo.getBussNo()+platformTaskVo.getRequestType()+"自动上传平台失败！",e);
			platformTaskVo.setOperateStatus(CodeConstants.OperateStatus.OFF);
			platformTaskVo.setEndDate(new Date());
			platformTaskVo.setLastDate(new Date());
			platformTaskVo.setRedoTimes(platformTaskVo.getRedoTimes()+1);
			platformTaskVo.setRemark(e.getMessage());
			ciClaimPlatformTaskService.updatePlatformTask(platformTaskVo);
		}

	}
	
	@Override
	public void uploadPlatformByTaskId(Long taskId){
		CiClaimPlatformTaskVo platformTaskVo = ciClaimPlatformTaskService.findPlatformTaskByPK(taskId);
		this.uploadPlatform(platformTaskVo);
	}

}
