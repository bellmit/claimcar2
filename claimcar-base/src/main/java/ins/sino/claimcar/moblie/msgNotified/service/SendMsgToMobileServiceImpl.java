package ins.sino.claimcar.moblie.msgNotified.service;

import freemarker.template.utility.StringUtil;
import ins.framework.service.CodeTranService;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.HttpClientHander;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WFMobileService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.MsgNotifiedBody;
import ins.sino.claimcar.flow.vo.NotifyMobileBody;
import ins.sino.claimcar.flow.vo.NotifyMobileItem;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.moblie.logUtil.QuickClaimLogUtil;
import ins.sino.claimcar.moblie.msgNotified.util.QuickClaimUtil;
import ins.sino.claimcar.moblie.msgNotified.vo.MsgNotifiedResPacket;
import ins.sino.claimcar.moblie.msgNotified.vo.SendMsgToMobiePacket;
import ins.sino.claimcar.moblie.msgNotified.vo.SendMsgToMobileHead;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 理赔系统通知终端接口（理赔请求快赔）
 * <pre></pre>
 * @author ★niuqiang
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("sendMsgToMobileService")
public class SendMsgToMobileServiceImpl implements SendMsgToMobileService {

    private static Logger logger = LoggerFactory.getLogger(SendMsgToMobileServiceImpl.class);
    
    @Autowired
    private CodeTranService codeTranService;
    @Autowired
    private WfMainService wfMainService;
    @Autowired
    private WFMobileService wFMobileService;
    @Autowired
    private WfFlowQueryService wfFlowQueryService;
    @Autowired
    private WfTaskHandleService wfTaskHandleService;
    @Autowired
    RegistQueryService registQueryService;
    @Autowired
    ManagerService managerService;
    
    public void sendMsgToMobile(NotifyMobileBody msgVo,PrpLWfTaskVo wfTakVo,String url)  {
    	if(StringUtils.isNotBlank(msgVo.getRegistNo())){
    		PrpLRegistVo registVo = registQueryService.findByRegistNo(msgVo.getRegistNo());
    		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
    			//从共，从联案件直接return
    			return ;
    		}	
    		// 如果是自助理赔案件,不送移动查勘
			if("1".equals(registVo.getSelfClaimFlag())){
				return;
			}
    	}
        MsgNotifiedResPacket resPacket =null;
        SendMsgToMobiePacket packet = new SendMsgToMobiePacket();
        SendMsgToMobileHead head  = new SendMsgToMobileHead();
        HttpClientHander httpClent = new HttpClientHander();
        head.setRequestType("017");
        head.setUser("mclaim_user");
        head.setPassWorld("mclaim_psd");
        packet.setHead(head);
        packet.setBody(msgVo);
        String registNo = msgVo.getRegistNo();
        String xmlToSend = ClaimBaseCoder.objToXmlUtf(packet);
        logger.info("理赔通知快赔发送信息send---------------------------"+xmlToSend);
        
        url = url+"claimInformTerminal/claimInformTerm.do?xml=";
        try{
            String xmlReturn = QuickClaimUtil.requestPlatform(xmlToSend, url);
            logger.info("理赔通知快赔返回送信息return---------------------------"+xmlReturn);
             resPacket =  ClaimBaseCoder.xmlToObj(xmlReturn,MsgNotifiedResPacket.class);
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            QuickClaimLogUtil util = new QuickClaimLogUtil();
            ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
            logVo.setRegistNo(registNo);
            logVo.setOperateNode(wfTakVo.getMobileOperateType()); //移动查勘操作类型
            if(wfTakVo.getTaskId()!=null && !"".equals(wfTakVo.getTaskId())){
            	logVo.setServiceType(wfTakVo.getTaskId().toString().split("\\.")[0]); //prplwftaskin 表taskId
            }
            util.logUtil(packet,resPacket,logVo);
        }
    }
    @Override
    public void packMsg(PrpLWfTaskVo wfTakVo,String url) {
    	NotifyMobileBody body = new NotifyMobileBody();
    	List<NotifyMobileItem> taskList = new ArrayList<NotifyMobileItem>();
    	body.setRegistNo(wfTakVo.getRegistNo());
    	body.setType(wfTakVo.getWorkStatus());
    	if("9".equals(wfTakVo.getWorkStatus())){
    		if(CodeConstants.MobileOperationType.LOSSCANCLE.equals(wfTakVo.getMobileOperateType())){//定损注销
    			body.setCancelType("3");
    			NotifyMobileItem item = new NotifyMobileItem();
        		if(wfTakVo.getTaskId() != null){
        			item.setTaskId(wfTakVo.getTaskId().toString().split("\\.")[0]);
                }
        		item.setNodeType(getNodeCode(wfTakVo.getSubNodeCode()));
        		item.setItemNo(wfTakVo.getMobileNo());
        		item.setItemName(wfTakVo.getMobileName());
        		item.setCheckStatus(wfTakVo.getHandlerStatus());
        		item.setHandlerCode(wfTakVo.getHandlerUser());
        	    String userName = codeTranService.transCode("UserCode",wfTakVo.getHandlerUser());
        	    item.setHandlerName(userName);
        	    item.setNextHandlerCode(wfTakVo.getHandlerUser());
        	    item.setNextHandlerName(userName);
        	    taskList.add(item);
    		}else{
    			if(CodeConstants.MobileOperationType.CLAIMCANCLE.equals(wfTakVo.getMobileOperateType())){//立案注销
        			body.setCancelType("2");
        			
        		}else{//报案注销
        			body.setCancelType("1");
        		}
    			List<PrpLWfTaskVo> taskVoList = wfFlowQueryService.findCancelByRegistNo(wfTakVo.getRegistNo());
    			for(PrpLWfTaskVo taskVo:taskVoList){
    				NotifyMobileItem item = new NotifyMobileItem();
    				item.setTaskId(taskVo.getTaskId().toString().split("\\.")[0]);
    				item.setNodeType(getNodeCode(taskVo.getSubNodeCode()));
    	    		item.setCheckStatus(taskVo.getHandlerStatus());
    	    	    taskList.add(item);
    			}
    		}
    	}else{
    		NotifyMobileItem item = new NotifyMobileItem();
    		if(wfTakVo.getTaskId() != null){
    			item.setTaskId(wfTakVo.getTaskId().toString().split("\\.")[0]);
            }
    		if(wfTakVo.getOriginalTaskId() != null){
    			item.setOriginalTaskId(wfTakVo.getOriginalTaskId().toString());
    		}
    		item.setNodeType(getNodeCode(wfTakVo.getSubNodeCode()));
    		item.setItemNo(wfTakVo.getMobileNo());
    		item.setItemName(wfTakVo.getMobileName());
    		item.setCheckStatus(wfTakVo.getHandlerStatus());
    		item.setHandlerCode(wfTakVo.getHandlerUser());
    	    String userName = codeTranService.transCode("UserCode",wfTakVo.getHandlerUser());
    	    item.setHandlerName(userName);
    	    item.setNextHandlerCode(wfTakVo.getHandlerUser());
    	    item.setNextHandlerName(userName);
    	    taskList.add(item);
    	}
    	body.setTaskList(taskList);
    	this.sendMsgToMobile(body,wfTakVo,url);
    }
    
    public  static void main(String args[]){
        MsgNotifiedBody body = new MsgNotifiedBody();
        body.setRegistNo("4300200201612070020389");
        body.setType("0");
        body.setNodeType("Check");
        body.setItemNo("1");
        body.setItemName("标的车");
        body.setCheckStatus("0");
        body.setHandlerCode("30020060");
        body.setHandlerName("30020060");
        body.setNextHandlerCode("30020060");
        body.setNextHandlerName("30020060");
        SendMsgToMobileServiceImpl impl = new SendMsgToMobileServiceImpl();
        //impl.sendMsgToMobile(body);
    }
    
    @Override
    public PrpLWfMainVo findPrpLWfMainVoByRegistNo(String registNo) {
        return wfMainService.findPrpLWfMainVoByRegistNo(registNo);
    }
    @Override
    public PrpLWfTaskVo changeToClaimAcceptStatus(BigDecimal flowTaskId) {
        return wFMobileService.updateWfTaskInByTaskId(flowTaskId);
    }
    @Override
    public PrpLWfTaskVo isMobileCaseAcceptInClaim(String registNo,BigDecimal flowTaskId) {
        PrpLWfTaskVo wfTaskVo = null;
        PrpLWfMainVo wfMainVo =  this.findPrpLWfMainVoByRegistNo(registNo);
        PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.valueOf(flowTaskId.toString()));
        PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
        if("1".equals(wfMainVo.getIsMobileCase()) || isMobileCase(registVo,taskVo.getAssignUser())){ //移动端案件
            wfTaskVo = this.changeToClaimAcceptStatus(flowTaskId);
        }
        return wfTaskVo;
    }
    
    /**
     * 因为改派移交不回写prplwfmain表IsMobileCase，所以要单独判断是否移动端案件
     * 如果已生成查勘任务，以最新查勘任务的处理人员来判断
     * @param registVo
     * @param userCode
     * @return
     */
    @Override
    public Boolean isMobileCase(PrpLRegistVo registVo,String userCode){
    	Boolean isMobileCase;
    	boolean isFireOrRobber = false;  //出险原因是否为 盗抢、自燃
	    boolean isAssessorAcceapt = false;  //是否为公估案件
    	if("DM04".equals(registVo.getDamageCode()) || "DM09".equals(registVo.getDamageCode())){
	        isFireOrRobber = true;  //出险原因为盗抢或者自燃
	    }
    	
    	List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(registVo.getRegistNo(), FlowNode.Chk.toString());
		if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
			//流入时间降序排
			Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
			@Override
			public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
					return o2.getTaskInTime().compareTo(o1.getTaskInTime());
				}
			});
			userCode = prpLWfTaskVoList.get(0).getAssignUser();
		}else if(userCode==null || StringUtils.isEmpty(userCode)){
			return false;
		}
		
    	//是否为公估人员
    	PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(userCode);
    	if(intermMainVo != null){
	        isAssessorAcceapt = true;
	    }
    	if(!isFireOrRobber && !isAssessorAcceapt){
	        isMobileCase =true;
	    }else{
	    	isMobileCase =false;
	    }
    	return isMobileCase;
    }
    
    @Override
    public String sendMsgToMobile(ClaimInterfaceLogVo logVo,String url) {
        Double taskId = Double.valueOf(logVo.getServiceType());
        PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(taskId);
        wfTaskVo.setMobileNo(logVo.getClaimNo());
        wfTaskVo.setMobileName(logVo.getCompensateNo());
        wfTaskVo.setMobileOperateType(logVo.getOperateNode());
        if(StringUtils.isNotBlank(logVo.getOperateNode())){
            if(logVo.getOperateNode().contains("接收")){
                wfTaskVo.setHandlerStatus("0");
                wfTaskVo.setWorkStatus("0");
            }else if(logVo.getOperateNode().contains("暂存")){
                wfTaskVo.setHandlerStatus("2");
                wfTaskVo.setWorkStatus("2");
            }else if(logVo.getOperateNode().contains("提交")){
                wfTaskVo.setHandlerStatus("3");
                wfTaskVo.setWorkStatus("3");
            }else if(logVo.getOperateNode().contains("退回")){
                //退回案件要传被退回定损的taskId
           	 	PrpLWfTaskVo upWfTaskVo = wfTaskHandleService.queryTask(Double.valueOf(wfTaskVo.getUpperTaskId().toString()));
           	 	while(!FlowNode.DLoss.name().equals(upWfTaskVo.getNodeCode())){
           	 		upWfTaskVo = wfTaskHandleService.queryTask(Double.valueOf(upWfTaskVo.getUpperTaskId().toString()));
           	 	}
           	 	wfTaskVo.setOriginalTaskId(upWfTaskVo.getTaskId());
                wfTaskVo.setHandlerStatus("0");
                wfTaskVo.setWorkStatus("6");
            }else if(logVo.getOperateNode().contains("注销")){
                wfTaskVo.setHandlerStatus("9");
                wfTaskVo.setWorkStatus("9");
            }
        }
      
        this.packMsg(wfTaskVo,url);
        return "";
    }

    public String getNodeCode(String nodeCode){
    	if(FlowNode.Chk.name().equals(nodeCode)){
    		nodeCode = FlowNode.Check.name();
    	}
    	return nodeCode;
    }
}
