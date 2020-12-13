package ins.sino.claimcar.flow.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.flow.po.PrpLWfMain;
import ins.sino.claimcar.flow.po.PrpLWfTaskIn;
import ins.sino.claimcar.flow.po.PrpLWfTaskOut;
import ins.sino.claimcar.flow.service.WFMobileService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.MsgNotifiedBody;
import ins.sino.claimcar.flow.vo.MsgNotifiedResBody;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.PrplWhiteListVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.manager.po.PrpdIntermBank;
import ins.sino.claimcar.manager.po.PrplWhiteList;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "wFMobileService")
public class WFMobileServiceImpl implements WFMobileService {

    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
	WfTaskHandleService wfTaskHandleService;
    @Autowired
    SysUserService sysUserService;
    @Override
    public Map<String, MsgNotifiedResBody> mobileUpdateWFTaskIn(MsgNotifiedBody body) {
        /*QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",body.getRegistNo());
        List<PrpLWfMain> mainList = databaseDao.findAll(PrpLWfMain.class,queryRule);
        if(null != mainList && mainList.size() ==1){
            if(!"1".equals(mainList.get(0).getIsMobileCase())){
                throw new IllegalArgumentException(" 此案件非移动端案件 ");
            }
        }else{
            throw new IllegalArgumentException(" 错误案件！请检查报案号 ");
        }*/
        Map<String, MsgNotifiedResBody> returnMap = new HashMap<String, MsgNotifiedResBody>();
        MsgNotifiedResBody resBody = new MsgNotifiedResBody();
        String returnMsg= "";
        PrpLWfTaskIn taskIn = databaseDao.findByPK(PrpLWfTaskIn.class, new BigDecimal(body.getTaskId()));
        resBody.setRegistNo(body.getRegistNo());
        resBody.setTaskId(body.getTaskId());
        if(taskIn != null){
        	if("10".equals(body.getType())){
        		 taskIn.setIsMobileAccept("0"); //PC端处理
        	}else if("1".equals(body.getType())||"2".equals(body.getType())||"3".equals(body.getType())){
        		taskIn.setIsMobileAccept("1"); //移动端处理
        	}
      		 databaseDao.update(PrpLWfTaskIn.class, taskIn);
      		 if("7".equals(body.getType())){
      			 WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
                   submitVo.setFlowTaskId(new BigDecimal(body.getTaskId()));
                   submitVo.setTaskInUser(body.getHandlerCode());
                   submitVo.setAssignUser(body.getNextHandlerCode());
                   SysUserVo userVo = sysUserService.findByUserCode(body.getNextHandlerCode());
                   if(userVo!=null){
                   	submitVo.setAssignCom(userVo.getComCode());
                   }
                   PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.handOverTask(submitVo, body.getHandoverTaskReason());
                   if(prpLWfTaskVo!=null && prpLWfTaskVo.getTaskId()!=null){
                	resBody.setNewTaskId(prpLWfTaskVo.getTaskId().toString());
                   	returnMsg="操作成功";
                   }else{
                   	returnMsg="任务移交失败！";
                   }
      		 }else{
      			returnMsg="操作成功";
      			resBody.setNewTaskId("");  //没有移交任务，不会产生新的taskId
      		 }
      	 }else{
      		 returnMsg="没有查到任务！";
      	 }
   	 	returnMap.put(returnMsg, resBody);
   		return returnMap;
    }
    
    @Override
    public PrpLWfTaskVo updateWfTaskInByTaskId(BigDecimal flowTaskId) {
        PrpLWfTaskVo taskVo = null;
        PrpLWfTaskIn in = databaseDao.findByPK(PrpLWfTaskIn.class,flowTaskId);
        if(in != null){
            taskVo = Beans.copyDepth().from(in).to(PrpLWfTaskVo.class);
            in.setIsMobileAccept("0"); //理赔处理
            databaseDao.update(PrpLWfTaskIn.class,in);
        }else{
        	taskVo = wfTaskHandleService.queryTask(Double.valueOf(flowTaskId.toString()));
        }
        return taskVo;
    }

	@Override
	public PrplWhiteListVo findPrplWhiteListByOther(String listType, String userCode) {		
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("listType",listType);
		queryRule.addEqual("userCode",userCode);
		PrplWhiteListVo prplWhiteListVo = null;
		List<PrplWhiteList>  prplWhiteListPos = databaseDao.findAll(PrplWhiteList.class,queryRule);
		if(prplWhiteListPos != null && prplWhiteListPos.size() > 0){
			PrplWhiteList prplWhiteList = prplWhiteListPos.get(0);
			prplWhiteListVo = Beans.copyDepth().from(prplWhiteList).to(PrplWhiteListVo.class);
		}
		return prplWhiteListVo;
	}

	@Override
	public Boolean findWhileListCase(String listType, String userCode) {
        PrplWhiteListVo prplWhiteListVo = this.findPrplWhiteListByOther(listType, userCode);
		if(prplWhiteListVo != null){//移动端案件，不送民太安车童接口
			return true;
		}
		return false;
    }
}
