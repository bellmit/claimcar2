/******************************************************************************
 * CREATETIME : 2016年8月9日 下午4:41:09
 ******************************************************************************/
package ins.interf;


import ins.framework.lang.Springs;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants.JobStatus;
import ins.sino.claimcar.CodeConstants.JobType;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.other.service.QuartzLogService;
import ins.sino.claimcar.other.vo.PrplQuartzLogVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jws.WebService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * 
 * <pre></pre>
 * @author ★zhujunde
 */

@WebService(targetNamespace="http://interf/claimForceJobService/",serviceName="claimForceJobService",endpointInterface = "ins.interf.ClaimForceJobService")
public class ClaimForceJobService extends SpringBeanAutowiringSupport {
    @Autowired
    private ClaimTaskService claimTaskService;
    @Autowired
    private RegistQueryService registQueryService;
    @Autowired
    private PolicyViewService policyViewService;
    @Autowired
    private QuartzLogService quartzLogService;
	private static Logger logger = LoggerFactory.getLogger(ClaimForceJobService.class);
	
	public void doJob() {
	    init();
        logger.info(this.getClass().getSimpleName()+"-强制立案 is run at:"+new Date());
        
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setUserCode("AUTO");
        
        // 1、查询距离报案时间48小时尚未立案的案子(强制立案标准)
        Calendar endcalendar = Calendar.getInstance();
        endcalendar.set(Calendar.DAY_OF_YEAR, endcalendar.get(Calendar.DAY_OF_YEAR) - 2);
        // 2、查询15天以内的案子(防止以前有强制立案失败的案子，算是补传操作)
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 15);
        
        List<PrpLRegistVo> prpLRegistVoList = new ArrayList<PrpLRegistVo>();
        
        prpLRegistVoList = registQueryService.findPrpLRegistVosByTime(calendar.getTime(),endcalendar.getTime());
        
        //查询所有曾经失败一次的强制立案案件
        List<PrplQuartzLogVo> prplQuartzLogVos =  quartzLogService.findClaimForceFirstFail();
        if(prplQuartzLogVos!=null){
            for(PrplQuartzLogVo prplQuartzLogVo:prplQuartzLogVos){
                PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prplQuartzLogVo.getRegistNo());
                prpLRegistVoList.add(prpLRegistVo);
            }
        }
        
        if(prpLRegistVoList.isEmpty()){
            return;
        }
        
        
        
        Map<List<PrpLCMainVo>,String> prpLCMainMap = new HashMap<List<PrpLCMainVo>,String>();
        for(PrpLRegistVo prpLRegistVo:prpLRegistVoList){
            List<PrpLCMainVo> prpLCMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(prpLRegistVo.getRegistNo());
            if(prpLCMainVoList != null && !prpLCMainVoList.isEmpty()){
                prpLCMainMap.put(prpLCMainVoList,prpLRegistVo.getFlowId());
            }
        }
        
        for(Entry<List<PrpLCMainVo>,String> entry:prpLCMainMap.entrySet()){
            List<PrpLCMainVo> prpLCMainVoList = entry.getKey();
            String flowId = entry.getValue();
            for(PrpLCMainVo prpLCMainVo:prpLCMainVoList){
                String registNo = prpLCMainVo.getRegistNo();
                    try{
                        // 获取当前时间
                        Calendar now = Calendar.getInstance();
                        String time = String.valueOf(now.get(Calendar.HOUR_OF_DAY));
                        logger.info("{}强制立案执行到现在是几点：{}", registNo, time);

                        // 3、防止以后业务量大，定时服务拉慢数据库，强制立案的时间从凌晨一点到早上八点钟，八点以后如果强制立案未完成，也强制退出，留在下次扫描
                        if ("8".equals(time)) {
                            return;
                        }
                        //强制立案
                        claimTaskService.claimForceJob(registNo,prpLCMainVo.getPolicyNo(),flowId);
                        
                        //查询定时任务日志表
                        PrplQuartzLogVo prplQuartzLogVo = quartzLogService.findQuartzLogByRegistNoAndJobType(registNo,JobType.CLAIMFORCE);
                        if(prplQuartzLogVo!=null){
                            prplQuartzLogVo.setStatus(JobStatus.SUCCEED);
                            //更新状态
                            quartzLogService.saveOrUpdateQuartzLog(prplQuartzLogVo,sysUserVo);
                        }
                        
                    }catch(Exception e){
                        logger.info("强制立案失败，报案号:"+registNo+",保单号:"+prpLCMainVo.getPolicyNo());
                        logger.info("forceClaim 强制立案失败{}", registNo, e);
                        //保存定时任务日志表
                        this.saveClaimForceFailLog(e,registNo,sysUserVo);
                    }
            }
            
        }
        
    }

    private void init() {
        if(claimTaskService == null){
            claimTaskService = (ClaimTaskService)Springs.getBean(ClaimTaskService.class);
        }
        if(registQueryService == null){
            registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
        }
        if(policyViewService == null){
            policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
        }
        if(quartzLogService == null){
            quartzLogService = (QuartzLogService)Springs.getBean(QuartzLogService.class);
        }
    }

    /**
     * 保存强制立案失败日志
     * <pre></pre>
     * @param exception
     * @param registNo
     * @modified:
     * ☆LinYi(2018年3月22日 下午3:01:38): <br>
     */
    private void saveClaimForceFailLog(Exception exception,String registNo,SysUserVo sysUserVo){
        PrplQuartzLogVo prplQuartzLogVo = null;
        //查询定时任务日志表-判断是否存在记录
        prplQuartzLogVo = quartzLogService.findQuartzLogByRegistNoAndJobType(registNo,JobType.CLAIMFORCE);
        if(prplQuartzLogVo==null){
            //第一次
            prplQuartzLogVo = new PrplQuartzLogVo();
            prplQuartzLogVo.setRegistNo(registNo);
            prplQuartzLogVo.setStatus(JobStatus.FIRST); 
        }else {
          //第二次
            prplQuartzLogVo.setStatus(JobStatus.SECEND);
        }
        prplQuartzLogVo.setJobType(JobType.CLAIMFORCE);
        prplQuartzLogVo.setFailReason(this.getExceptionStackMsg(exception));
        
        quartzLogService.saveOrUpdateQuartzLog(prplQuartzLogVo,sysUserVo);
    }
    
    
    /**
     * 获取堆栈信息
     * <pre></pre>
     * @param exception
     * @return
     * @modified:
     * ☆LinYi(2018年3月22日 下午2:49:43): <br>
     */
    private String getExceptionStackMsg(Exception exception){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        exception.printStackTrace(pw);
        pw.flush();
        sw.flush();
        String errMsg=sw.toString();
        if(StringUtils.isNotBlank(errMsg)){
        	return errMsg.substring(0,errMsg.length()>2000?2000:errMsg.length());
        }else{
        	return "无异常信息";
        }
        
    }
}
