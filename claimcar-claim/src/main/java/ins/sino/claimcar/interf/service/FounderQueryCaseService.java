package ins.sino.claimcar.interf.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ins.framework.lang.Springs;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.vo.SysUserVo;
import ins.sino.claim.founder.vo.FounderToClaimQueryRequestVo;
import ins.sino.claim.founder.vo.FounderToClaimQueryRespBody;
import ins.sino.claim.founder.vo.FounderToClaimQueryRespHead;
import ins.sino.claim.founder.vo.FounderToClaimQueryRespVo;
import ins.sino.claim.founder.vo.NodeInfo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 客服系统调用理赔系统--案件信息查询
 * @author j2eel
 *
 */
@WebService(serviceName="founderQueryCase")
public class FounderQueryCaseService extends SpringBeanAutowiringSupport{
	
//	@Autowired
	WfTaskHandleService wfTaskHandle;
//	@Autowired
	RegistQueryService registQueryService;
//	@Autowired
	CheckTaskService checkTaskService;
//	@Autowired
	ManagerService managerService;
//	@Autowired
	LossCarService lossCarService;
//	@Autowired
	PersTraceService persTraceService;
//	@Autowired
	PropTaskService propTaskService;
//	@Autowired
	private SysUserService sysUserService;
	
	
	private static Logger logger = LoggerFactory.getLogger(FounderQueryCaseService.class);
	
	public String founderQueryCaseForXml(String xml){
		
		this.init();
		logger.info("客服查询案件信息请求报文：" + xml);
		FounderToClaimQueryRequestVo requestVo = new FounderToClaimQueryRequestVo();
		FounderToClaimQueryRespVo resPacket = new FounderToClaimQueryRespVo();
		FounderToClaimQueryRespHead resHead = new FounderToClaimQueryRespHead();
		FounderToClaimQueryRespBody respBody = new FounderToClaimQueryRespBody();
		List<NodeInfo> infoList = new ArrayList<NodeInfo>();
		SimpleDateFormat fat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性

		try {
			stream.processAnnotations(FounderToClaimQueryRequestVo.class);
			requestVo = (FounderToClaimQueryRequestVo) stream.fromXML(xml);
			this.checkFounderQueryCase(requestVo);   //校验报文信息
		} catch (Exception e) {
			resHead.setSystemCode("Claim");
			resHead.setResponseType(true);
			resHead.setErrorMessage(e.getMessage());
			resPacket.setHead(resHead);
			logger.info("客服查询案件信息请求报文异常！ 报文内容：" + xml +"\n详细信息：",e);
		}
		
		//组织返回数据
		String registNo = requestVo.getBody().getRegistNo();
		//查询taskin表
		List<PrpLWfTaskVo> taskinList = wfTaskHandle.findPrpLWfTaskInByRegistNo(registNo);
		if(taskinList != null && taskinList.size() > 0){
			for(PrpLWfTaskVo vo : taskinList){
				NodeInfo info = new NodeInfo();
				info.setFlowId(vo.getFlowId());
				info.setRegistNo(vo.getRegistNo());
				info.setTaskId((vo.getTaskId() == null ? "" : vo.getTaskId().toString()));
				info.setUpperTaskId(vo.getUpperTaskId() == null ? "" : vo.getUpperTaskId().toString());
				info.setNodeCode(vo.getNodeCode());
				info.setSubNodeCode(vo.getSubNodeCode());
				info.setTaskName(vo.getTaskName());
				info.setItemName(vo.getItemName());
				info.setComCode(vo.getComCode());
				info.setRiskCode(vo.getRiskCode());
				info.setHandlerStatus(vo.getHandlerStatus());
				info.setTaskInTime(fat.format(vo.getTaskInTime()));
				SysUserVo userVo = new SysUserVo();
				if(StringUtils.isNotBlank(vo.getHandlerUser())){
					//报案节点为：ANYONE 或 工号
					if(StringUtils.isNotBlank(vo.getNodeCode()) && FlowNode.Regis.name().equals(vo.getNodeCode()) &&
							vo.getHandlerUser().equals("ANYONE")){
						userVo = sysUserService.findByUserCode(vo.getTaskInUser());
					}else{
						userVo = sysUserService.findByUserCode(vo.getHandlerUser());
					}
					if(userVo != null && !userVo.equals("")){
						info.setHandlerUser(userVo.getUserName());
					}
					//当为：AUTO，AutoClaim，AutoVClaim 不用翻译
					if(vo.getHandlerUser().equals("AutoVClaim") || vo.getHandlerUser().equals("AUTO") || 
							                    vo.getHandlerUser().equals("AutoClaim")){
						info.setHandlerUser(vo.getHandlerUser());
					}
				}

				info.setHandlerCom(vo.getHandlerCom());
				info.setTaskInNode(vo.getTaskInNode());
				info.setTaskOutNode(vo.getTaskOutNode());
				
				if(StringUtils.isNotBlank(vo.getNodeCode())){
					//报案时间
					if(FlowNode.Regis.name().equals(vo.getNodeCode())){
						PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
						if(registVo.getReportTime() != null){
							info.setReportTime(fat.format(registVo.getReportTime()));
						}
					}
					
					//查勘人类型
					if(FlowNode.Check.name().equals(vo.getNodeCode()) && !FlowNode.ChkRe.name().equals(vo.getSubNodeCode())){
						if(vo.getHandlerIdKey() != null && !vo.getHandlerIdKey().isEmpty()){
							PrpLCheckTaskVo checkVo = checkTaskService.findCheckTaskVoById(Long.decode(vo.getHandlerIdKey()));
							if(checkVo != null && !checkVo.equals("")){
								String checkCode = checkVo.getCheckerCode();
								if(checkCode != null && !checkCode.isEmpty()){
									String checkerType = checkPersonType(checkCode);
									info.setHandlerType(checkerType);
								}
							}
						}
					}
					if( StringUtils.isNotBlank(vo.getSubNodeCode())){
						//车辆定损人类型,修理厂
						if(FlowNode.DLoss.name().equals(vo.getNodeCode()) && 
								FlowNode.DLCar.name().equals(vo.getSubNodeCode()) ){
							if(StringUtils.isNotBlank(vo.getHandlerIdKey())){
								PrpLDlossCarMainVo carMain = new PrpLDlossCarMainVo();
								carMain = lossCarService.findLossCarMainbyId(Long.decode(vo.getHandlerIdKey()));
								if(carMain != null && !carMain.equals("")){
									//修理厂
									info.setRepairFactoryName(carMain.getRepairFactoryName());
									//定损人员类型
									if(StringUtils.isNotBlank(carMain.getHandlerCode())){
										String losser = checkPersonType(carMain.getHandlerCode());
										info.setHandlerType(losser);
									}
								}
							}
						}
						
						//财产定损人类型
						if( FlowNode.DLoss.name().equals(vo.getNodeCode()) &&
								FlowNode.DLProp.name().equals(vo.getSubNodeCode())){
							boolean flag = false;
							if(StringUtils.isNotBlank(vo.getHandlerIdKey())){
								PrpLdlossPropMainVo propMain = propTaskService.findPropMainVoById(Long.decode(vo.getHandlerIdKey()));
								if(propMain != null && !propMain.equals("") && propMain.getRegistNo().equals(vo.getRegistNo())){
									if(StringUtils.isNotBlank(propMain.getHandlerCode())){
										String losserType = checkPersonType(propMain.getHandlerCode());
										info.setHandlerType(losserType);
										flag = true;
									}
								}
								//使用报案号查询财产定损信息
								if(!flag){
									List<PrpLdlossPropMainVo> propMainList = propTaskService.findPropMainListByRegistNo(registNo);
									if(propMainList != null && propMainList.size() > 0){
										for(PrpLdlossPropMainVo voo : propMainList){
											if(StringUtils.isNotBlank(voo.getLicense()) && StringUtils.isNotBlank(vo.getItemName()) && 
													StringUtils.isNotBlank(voo.getHandlerCode())&& voo.getLicense().equals(vo.getItemName())){
												String losserType = this.checkPersonType(voo.getHandlerCode());
												info.setHandlerType(losserType);
											}
										}
									}
								}
							}
						}
						
						//人伤追踪人类型
						if( FlowNode.PLoss.name().equals(vo.getNodeCode()) && 
								(FlowNode.PLFirst.name().equals(vo.getSubNodeCode()) || 
										FlowNode.PLNext.name().equals(vo.getSubNodeCode()))){
							if(StringUtils.isNotBlank(vo.getHandlerIdKey())){
								PrpLDlossPersTraceMainVo persTraceMain =  persTraceService.findPersTraceMainVobyId(Long.decode(vo.getHandlerIdKey()));
								if(persTraceMain != null && !persTraceMain.equals("")){
									String intermediaryFlag = persTraceMain.getIntermediaryFlag();
									if(StringUtils.isNotBlank(intermediaryFlag)){
										if(intermediaryFlag.equals("1")){
										    info.setHandlerType("2");
									    }else if(intermediaryFlag.equals("0")){
										    info.setHandlerType("1");
									    }
									}
								}
							}
						}
					}
				}
				
				infoList.add(info);
			}
		}
		
		//查询taskout表
		List<PrpLWfTaskVo> taskoutList = wfTaskHandle.findPrpLWfTaskOutByRegistNo(registNo);
		if(taskoutList != null && taskoutList.size() > 0){
			for(PrpLWfTaskVo vo : taskoutList){
				NodeInfo info = new NodeInfo();
				info.setFlowId(vo.getFlowId());
				info.setRegistNo(vo.getRegistNo());
				info.setTaskId((vo.getTaskId() == null ? "" : vo.getTaskId().toString()));
				info.setUpperTaskId(vo.getUpperTaskId() == null ? "" : vo.getUpperTaskId().toString());
				info.setNodeCode(vo.getNodeCode());
				info.setSubNodeCode(vo.getSubNodeCode());
				info.setTaskName(vo.getTaskName());
				info.setItemName(vo.getItemName());
				info.setComCode(vo.getComCode());
				info.setRiskCode(vo.getRiskCode());
				info.setHandlerStatus(vo.getHandlerStatus());
				info.setTaskInTime(fat.format(vo.getTaskInTime()));
				SysUserVo userVo = new SysUserVo();
				if(StringUtils.isNotBlank(vo.getHandlerUser())){
					//报案节点为：ANYONE 或 工号
					if(StringUtils.isNotBlank(vo.getNodeCode()) && FlowNode.Regis.name().equals(vo.getNodeCode()) &&
							vo.getHandlerUser().equals("ANYONE")){
						userVo = sysUserService.findByUserCode(vo.getTaskInUser());
					}else{
						userVo = sysUserService.findByUserCode(vo.getHandlerUser());
					}
					if(userVo != null && !userVo.equals("")){
						info.setHandlerUser(userVo.getUserName());
					}
					//当为：AUTO，AutoClaim，AutoVClaim 不用翻译
					if(vo.getHandlerUser().equals("AutoVClaim") || vo.getHandlerUser().equals("AUTO") || 
							                    vo.getHandlerUser().equals("AutoClaim")){
						info.setHandlerUser(vo.getHandlerUser());
					}
				}

				info.setHandlerCom(vo.getHandlerCom());
				info.setTaskInNode(vo.getTaskInNode());
				info.setTaskOutNode(vo.getTaskOutNode());
				
				if(StringUtils.isNotBlank(vo.getNodeCode())){
					//报案时间
					if(FlowNode.Regis.name().equals(vo.getNodeCode())){
						PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
						if(registVo.getReportTime() != null){
							info.setReportTime(fat.format(registVo.getReportTime()));
						}
					}
					
					//查勘人类型
					if(FlowNode.Check.name().equals(vo.getNodeCode()) && !FlowNode.ChkRe.name().equals(vo.getSubNodeCode())){
						if(vo.getHandlerIdKey() != null && !vo.getHandlerIdKey().isEmpty()){
							PrpLCheckTaskVo checkVo = checkTaskService.findCheckTaskVoById(Long.decode(vo.getHandlerIdKey()));
							if(checkVo != null && !checkVo.equals("")){
								String checkCode = checkVo.getCheckerCode();
								if(checkCode != null && !checkCode.isEmpty()){
									String checkerType = checkPersonType(checkCode);
									info.setHandlerType(checkerType);
								}
							}
						}
					}
					if( StringUtils.isNotBlank(vo.getSubNodeCode())){
						//车辆定损人类型,修理厂
						if(FlowNode.DLoss.name().equals(vo.getNodeCode()) && 
								FlowNode.DLCar.name().equals(vo.getSubNodeCode()) ){
							if(StringUtils.isNotBlank(vo.getHandlerIdKey())){
								PrpLDlossCarMainVo carMain = new PrpLDlossCarMainVo();
								carMain = lossCarService.findLossCarMainbyId(Long.decode(vo.getHandlerIdKey()));
								if(carMain != null && !carMain.equals("")){
									//修理厂
									info.setRepairFactoryName(carMain.getRepairFactoryName());
									//定损人员类型
									if(StringUtils.isNotBlank(carMain.getHandlerCode())){
										String losser = checkPersonType(carMain.getHandlerCode());
										info.setHandlerType(losser);
									}
								}
							}
						}
						
						//财产定损人类型
						if( FlowNode.DLoss.name().equals(vo.getNodeCode()) &&
								FlowNode.DLProp.name().equals(vo.getSubNodeCode())){
							boolean flag = false;
							if(StringUtils.isNotBlank(vo.getHandlerIdKey())){
								PrpLdlossPropMainVo propMain = propTaskService.findPropMainVoById(Long.decode(vo.getHandlerIdKey()));
								if(propMain != null && !propMain.equals("") && propMain.getRegistNo().equals(vo.getRegistNo())){
									if(StringUtils.isNotBlank(propMain.getHandlerCode())){
										String losserType = checkPersonType(propMain.getHandlerCode());
										info.setHandlerType(losserType);
										flag = true;
									}
								}
								//使用报案号查询财产定损信息
								if(!flag){
									List<PrpLdlossPropMainVo> propMainList = propTaskService.findPropMainListByRegistNo(registNo);
									if(propMainList != null && propMainList.size() > 0){
										for(PrpLdlossPropMainVo voo : propMainList){
											if(StringUtils.isNotBlank(voo.getLicense()) && StringUtils.isNotBlank(vo.getItemName()) && 
													StringUtils.isNotBlank(voo.getHandlerCode())&& voo.getLicense().equals(vo.getItemName())){
												String losserType = this.checkPersonType(voo.getHandlerCode());
												info.setHandlerType(losserType);
											}
										}
									}
								}
							}
						}
						
						//人伤追踪人类型
						if( FlowNode.PLoss.name().equals(vo.getNodeCode()) && 
								(FlowNode.PLFirst.name().equals(vo.getSubNodeCode()) || 
										FlowNode.PLNext.name().equals(vo.getSubNodeCode()))){
							if(StringUtils.isNotBlank(vo.getHandlerIdKey())){
								PrpLDlossPersTraceMainVo persTraceMain =  persTraceService.findPersTraceMainVobyId(Long.decode(vo.getHandlerIdKey()));
								if(persTraceMain != null && !persTraceMain.equals("")){
									String intermediaryFlag = persTraceMain.getIntermediaryFlag();
									if(StringUtils.isNotBlank(intermediaryFlag)){
										if(intermediaryFlag.equals("1")){
										    info.setHandlerType("2");
									    }else if(intermediaryFlag.equals("0")){
										    info.setHandlerType("1");
									    }
									}
								}
							}
						}
					}
				}
				
				infoList.add(info);
			}
		}
		
		respBody.setNodeInfoList(infoList);
		resPacket.setBody(respBody);
		
		resHead.setSystemCode("Claim");
		resHead.setResponseType(true);
		resHead.setErrorMessage("成功");
		resPacket.setHead(resHead);

		String headXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
		stream.processAnnotations(FounderToClaimQueryRespVo.class);
		String responseXml = headXml + stream.toXML(resPacket);
		logger.info("客服查询案件信息返回报文：" + responseXml);
		return responseXml;
	}
	
	//校验报文
	private void checkFounderQueryCase(FounderToClaimQueryRequestVo requestVo) throws Exception{
		Assert.notNull(adjustContent(requestVo.getBody().getRegistNo()), "报案号为空！");
		
	}	
	
	private String adjustContent(String content){
		String reContent = null;
		if(content!=null&&!"".equals(content)&&!content.isEmpty()){
			reContent = content;
		}
		return reContent;
	}

	//判断人员类型（司内：1，公估：2）
	private String checkPersonType(String userCode){
		PrpdIntermMainVo intermVo = new PrpdIntermMainVo();
		intermVo = managerService.findIntermByUserCode(userCode);
		if(intermVo == null || intermVo.equals("")){
			return "1";
		}else{
            return "2";
		}
	}
	private void init(){
		if(wfTaskHandle == null){
			wfTaskHandle = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
		}
		
		if(registQueryService == null){
			registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
		}
		
		if(checkTaskService == null){
			checkTaskService = (CheckTaskService)Springs.getBean(CheckTaskService.class);
		}
		
		if(managerService == null){
			managerService = (ManagerService)Springs.getBean(ManagerService.class);
		}
		
		if(lossCarService == null){
			lossCarService = (LossCarService)Springs.getBean(LossCarService.class);
		}
		
		if(persTraceService == null){
			persTraceService = (PersTraceService)Springs.getBean(PersTraceService.class);
		}
		
		if(propTaskService == null){
			propTaskService = (PropTaskService)Springs.getBean(PropTaskService.class);
		}
		
		if(sysUserService == null){
			sysUserService = (SysUserService)Springs.getBean(SysUserService.class);
		}
	}

}
