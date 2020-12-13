/******************************************************************************
 * CREATETIME : 2015年12月8日 上午10:19:01
 ******************************************************************************/
package ins.sino.claimcar.moblie.service;

import ins.framework.lang.Springs;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.mobile.vo.FlowInfoInitReqBodyVo;
import ins.sino.claimcar.mobile.vo.FlowInfoInitReqVo;
import ins.sino.claimcar.mobile.vo.FlowInfoInitResBodyVo;
import ins.sino.claimcar.mobile.vo.FlowInfoInitResVo;
import ins.sino.claimcar.mobile.vo.PayInfoVo;
import ins.sino.claimcar.mobile.vo.TaskInfoVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.logUtil.QuickClaimLogUtil;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 移动查勘工作流查询实现类
 * 
 * <pre></pre>
 * @author ★zjd
 */
public class FlowInfoServiceImpl implements ServiceInterface {
	
	private static Logger logger = LoggerFactory.getLogger(FlowInfoServiceImpl.class);
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	private PayCustomService payCustomService;
    @Autowired
    private RegistQueryService registQueryService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    
	@Override
	public Object service(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		init();
		Map<String, String> nodeCode = new HashMap<String, String>();
		nodeCode.put(FlowNode.Sched.name(), FlowNode.Sched.getName());
		nodeCode.put(FlowNode.Chk.name(), FlowNode.Chk.getName());
		nodeCode.put(FlowNode.DLoss.name(), FlowNode.DLoss.getName());
		nodeCode.put(FlowNode.VLCar.name(), FlowNode.VLCar.getName());
		nodeCode.put(FlowNode.VPrice.name(), FlowNode.VPrice.getName());
		nodeCode.put(FlowNode.VLProp.name(), FlowNode.VLProp.getName());
		nodeCode.put(FlowNode.Compe.name(), FlowNode.Compe.getName());
		nodeCode.put(FlowNode.Certi.name(), FlowNode.Certi.getName());
		nodeCode.put(FlowNode.VClaim.name(), FlowNode.VClaim.getName());
		nodeCode.put(FlowNode.EndCas.name(), FlowNode.EndCas.getName());
		
		//返回的vo
		FlowInfoInitResVo resVo = new FlowInfoInitResVo();
		FlowInfoInitResBodyVo flowInfoInitResBodyVo = new FlowInfoInitResBodyVo();
		List<TaskInfoVo> taskInfoList = new ArrayList<TaskInfoVo>();
		List<PayInfoVo> payInfoList = new ArrayList<PayInfoVo>();
		MobileCheckResponseHead head = new MobileCheckResponseHead();
		MobileCheckHead mobileCheckHead = new MobileCheckHead();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		String registNo = "";
		try{
			FlowInfoInitReqVo flowInfoInitReqVo = (FlowInfoInitReqVo) arg1;
			stream.processAnnotations(FlowInfoInitReqVo.class);
			String xml = stream.toXML(flowInfoInitReqVo);
			logger.info("移动查勘工作流查询接收报文: \n"+xml);
			if (StringUtils.isBlank(xml)) {
				throw new IllegalArgumentException("报文为空");
			}
			stream.processAnnotations(FlowInfoInitReqVo.class);
			//FlowInfoInitReqVo flowInfoInitReqVo = (FlowInfoInitReqVo)stream.fromXML(xml);
			FlowInfoInitReqBodyVo flowInfoInitReqBodyVo = flowInfoInitReqVo.getBody();
			mobileCheckHead = flowInfoInitReqVo.getHead();
			if (!"013".equals(mobileCheckHead.getRequestType())|| !"claim_user".equals(mobileCheckHead.getUser())|| !"claim_psd".equals(mobileCheckHead.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			if(!StringUtils.isNotBlank(mobileCheckHead.getRequestType())){
				throw new IllegalArgumentException("请求类型不能为空");
			}
			if(!StringUtils.isNotBlank(mobileCheckHead.getRequestType())){
				throw new IllegalArgumentException("用户名不能为空");		
			}
			if(!StringUtils.isNotBlank(mobileCheckHead.getRequestType())){
				throw new IllegalArgumentException("密码不能为空");
			}
			if(!StringUtils.isNotBlank(flowInfoInitReqBodyVo.getRegistNo())){
				throw new IllegalArgumentException("报案号不能为空");
			}
			registNo = flowInfoInitReqBodyVo.getRegistNo();
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
			List<PrpLWfTaskVo> vos = wfFlowQueryService.findTaskVoForInAndOutByRegistNo(flowInfoInitReqBodyVo.getRegistNo());
			for(PrpLWfTaskVo vo:vos){
				TaskInfoVo taskInfoVo = new TaskInfoVo();
				
				if(nodeCode.containsKey(vo.getNodeCode()) || FlowNode.Chk.name().equals(vo.getSubNodeCode())){
					if(FlowNode.Chk.name().equals(vo.getSubNodeCode())){
						taskInfoVo.setClaimType(nodeCode.get(vo.getSubNodeCode()));
					}else{
						taskInfoVo.setClaimType(nodeCode.get(vo.getNodeCode()));
					}
					if(FlowNode.DLoss.name().equals(vo.getNodeCode())){
						String showInfoXML = vo.getShowInfoXML();
						if(StringUtils.isNotBlank(showInfoXML)){
							showInfoXML = showInfoXML.replace("{", "");
							showInfoXML = showInfoXML.replace("}", "");
							if(StringUtils.isNotBlank(showInfoXML)){
								showInfoXML = showInfoXML.replace("\"", "");
								String a[] =showInfoXML.split(",");
								if(a.length > 1){
									for(int j=0; j < a.length;j++){
										String serialNo[] = a[j].split(":");
										if(serialNo.length > 1){
											if("serialNo".equals(serialNo[0])){
												taskInfoVo.setIfObject(serialNo[1]);
											}
											
											/*if("license".equals(serialNo[0])){
												taskInfoVo.setLicenseNo(serialNo[1]);
											}*/
										}
									}
								}
							}
						}
					}
					taskInfoVo.setLossName(vo.getItemName());
					taskInfoVo.setHandlerCode(vo.getHandlerUser());
					String userName = CodeTranUtil.transCode("UserCode",vo.getHandlerUser());
					if(StringUtils.isNotBlank(userName) && userName.equals(vo.getHandlerUser())){
					    SysUserVo sysUserVo = scheduleTaskService.findPrpduserByUserCode(vo.getHandlerUser(),"");
    					taskInfoVo.setHandlerName(sysUserVo.getUserName());
					}else{
					    taskInfoVo.setHandlerName(userName);
					}
					if(vo.getTaskInTime()!=null){
						String taskInTime = DateUtils.dateToStr(vo.getTaskInTime(), DateUtils.YToSec);
						taskInfoVo.setCreateDate(taskInTime);
					}
					if(vo.getTaskOutTime()!=null){
						String taskOutnTime = DateUtils.dateToStr(vo.getTaskOutTime(), DateUtils.YToSec);
						taskInfoVo.setFinshDate(taskOutnTime);
					}
					//taskInfoVo.setLicenseNo("");
					if(FlowNode.VLoss.name().equals(vo.getNodeCode()) || FlowNode.VPrice.name().equals(vo.getNodeCode()) || 
							FlowNode.VClaim.name().equals(vo.getNodeCode())){
						if(vo.getMoney()!=null){
							taskInfoVo.setLossAmount(vo.getMoney().toString());
						}
					}
					
					taskInfoList.add(taskInfoVo);
				}
				
			}
			//payInfoList支付信息
			List<PrpLCompensateVo> prpLCompensateVoList = compensateTaskService.findCompByRegistNo(flowInfoInitReqBodyVo.getRegistNo());
			for(PrpLCompensateVo vo : prpLCompensateVoList){
				List<PrpLPaymentVo> prpLPaymentVoList = vo.getPrpLPayments();
				for(PrpLPaymentVo lPaymentVo :prpLPaymentVoList){
					PayInfoVo payInfoVo = new PayInfoVo();
					PrpLPayCustomVo prpLPayCustomVo = payCustomService.findPayCustomVoById(lPaymentVo.getPayeeId());
					payInfoVo.setAccountName(prpLPayCustomVo.getPayeeName());
					payInfoVo.setLossAmount(lPaymentVo.getSumRealPay().toString());
					if(lPaymentVo.getPayTime() != null){
						String payTime = DateUtils.dateToStr(lPaymentVo.getPayTime(), DateUtils.YToSec);
						payInfoVo.setPayDate(payTime);
					}
					if(vo.getRiskCode().substring(0, 2).equals("12")){
						payInfoVo.setLossName("商业险");
					}else{
						payInfoVo.setLossName("交强险");
					}
					payInfoList.add(payInfoVo);
				}
			}
			flowInfoInitResBodyVo.setRegistNo(flowInfoInitReqBodyVo.getRegistNo());
			if(prpLRegistVo!=null){
			    if(StringUtils.isNotBlank(prpLRegistVo.getPrpLRegistExt().getLicenseNo())){
			        flowInfoInitResBodyVo.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
			    }
			}
			flowInfoInitResBodyVo.setTaskInfoList(taskInfoList);
			flowInfoInitResBodyVo.setPayInfoList(payInfoList);
			head.setResponseType(mobileCheckHead.getRequestType());
			head.setResponseCode("YES");
			head.setResponseMessage("Success");
			resVo.setHead(head);
			resVo.setBody(flowInfoInitResBodyVo);
		}catch(Exception e){
			head.setResponseType(mobileCheckHead.getRequestType());
			head.setResponseCode("NO");
			head.setResponseMessage(e.getMessage());
			resVo.setHead(head);
			logger.info("移动查勘工作流查询异常信息：\n");
			e.printStackTrace();
		}
		stream.processAnnotations(FlowInfoInitResVo.class);
		logger.info("移动查勘工作流查询返回报文=========：\n"+stream.toXML(resVo));
		//return stream.toXML(resVo);
		return resVo;
	}
	private void init() {
		if(wfFlowQueryService==null){
			wfFlowQueryService = (WfFlowQueryService)Springs.getBean(WfFlowQueryService.class);
		}
		if(compensateTaskService==null){
			compensateTaskService = (CompensateTaskService)Springs.getBean(CompensateTaskService.class);
		}
		if(payCustomService==null){
			payCustomService = (PayCustomService)Springs.getBean(PayCustomService.class);
		}
		if(registQueryService==null){
		    registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
        }
		if(scheduleTaskService==null){
		    scheduleTaskService = (ScheduleTaskService)Springs.getBean(ScheduleTaskService.class);
        }
	}
	
}
