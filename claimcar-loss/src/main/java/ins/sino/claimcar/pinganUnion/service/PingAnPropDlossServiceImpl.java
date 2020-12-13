package ins.sino.claimcar.pinganUnion.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.lossprop.po.PrpLdlossPropFee;
import ins.sino.claimcar.lossprop.po.PrpLdlossPropMain;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.pinganVo.PropertyLossDTO;
import ins.sino.claimcar.pinganVo.PropertyLossDetailDTO;
import ins.sino.claimcar.pinganVo.PropertyObjectDTO;
import ins.sino.claimcar.pinganVo.RespPropDlossDataVo;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},group="pingAnPropDlossService")
@Path("pingAnPropDlossService")//财定损
public class PingAnPropDlossServiceImpl implements PingAnHandleService{
	private static Logger logger = LoggerFactory.getLogger(PingAnPropDlossServiceImpl.class);
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	RegistService registService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	private PropLossService propLossService;
	@Override
	public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
		logger.info("财产定损接口信息报文--------------------------------》{}",respData);
		ResultBean resultBean=ResultBean.success();
		Gson gson = new Gson();
		String comcode="";
		PrpLdlossPropMain prpLdlossPropMain=new PrpLdlossPropMain();
		JSONObject jsonObject=JSON.parseObject(pingAnDataNoticeVo.getParamObj());
		try{
			if(StringUtils.isBlank(registNo)){
				throw new IllegalAccessException("------报案号为空-----");
			}
			PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(registNo);
			comcode=prpLRegistVo.getComCode();
			RespPropDlossDataVo respPropDlossDataVo=gson.fromJson(respData,RespPropDlossDataVo.class);
			PrpLdlossPropMainVo losspropMainVo=propLossService.findVoByRegistNo(registNo);
			PrpLdlossPropMainVo propMainVo=propLossService.findVoByRegistNoAndPaid(registNo,pingAnDataNoticeVo.getId());
			if(respPropDlossDataVo!=null && propMainVo==null){
				PropertyObjectDTO propertyObjectDTO=respPropDlossDataVo.getPropertyObjectDTO();
				PropertyLossDTO propertyLossDTO=respPropDlossDataVo.getPropertyLossDTO();
				List<PropertyLossDetailDTO> propLossDetails=respPropDlossDataVo.getPropertyLossDetailDTOList();
				List<PrpLdlossPropFee> prpLdlossPropFees = new ArrayList<PrpLdlossPropFee>(0);
				prpLdlossPropMain.setRegistNo(registNo);
				prpLdlossPropMain.setMercyFlag("0");
				prpLdlossPropMain.setPaId(pingAnDataNoticeVo.getId());
				prpLdlossPropMain.setLossType("0");//定损类型
				prpLdlossPropMain.setInterMediaryFlag("0");//定损类别
				prpLdlossPropMain.setHandlerName("AUTO");//处理人
				prpLdlossPropMain.setDefLossDate(StringtoDate(propertyLossDTO.getLossCompleteDate()));//定损完成时间
				prpLdlossPropMain.setSumDefloss(StringUtils.isNotBlank(propertyLossDTO.getTotalAmount())?new BigDecimal(propertyLossDTO.getTotalAmount()):new BigDecimal(0));//物损定损总金额
				prpLdlossPropMain.setUnderWriteName("AUTO");
				prpLdlossPropMain.setSumVeriLoss(StringUtils.isNotBlank(propertyLossDTO.getTotalAmount())?new BigDecimal(propertyLossDTO.getTotalAmount()):new BigDecimal(0));//物核损总金额
				prpLdlossPropMain.setHandlerCode("AUTO");
				prpLdlossPropMain.setRiskCode(prpLRegistVo.getRiskCode());
				prpLdlossPropMain.setUnderWriteCode("AUTO");
				prpLdlossPropMain.setUnderWriteCom(comcode);
				prpLdlossPropMain.setUnderWriteEndDate(StringtoDate(propertyLossDTO.getLossCompleteDate()));
				prpLdlossPropMain.setUnderWriteFlag("1");
				prpLdlossPropMain.setComCode(comcode);
				prpLdlossPropMain.setMakeCom(comcode);
				prpLdlossPropMain.setValidFlag("1");
				prpLdlossPropMain.setCreateUser("AUTO");
				if("02".equals(propertyObjectDTO.getLossType())){//标的物损
					prpLdlossPropMain.setSerialNo(1);
					prpLdlossPropMain.setLicense(propertyObjectDTO.getPropertyName());
				}else if("05".equals(propertyObjectDTO.getLossType())){
					prpLdlossPropMain.setSerialNo(0);//地面物损
					prpLdlossPropMain.setLicense(propertyObjectDTO.getPropertyName());
				}else{
					prpLdlossPropMain.setSerialNo(3);//三者物损
					prpLdlossPropMain.setLicense(propertyObjectDTO.getPropertyName());
				}
				if(StringUtils.isBlank(propertyObjectDTO.getPropertyName())){
					prpLdlossPropMain.setLicense("默认");
				}
				prpLdlossPropMain.setCreateTime(new Date());
				if(propLossDetails!=null && propLossDetails.size()>0){
					for(PropertyLossDetailDTO propertyLossDetailDTO:propLossDetails){
						PrpLdlossPropFee prpLdlossPropFee=new PrpLdlossPropFee();
						prpLdlossPropFee.setPrpLdlossPropMain(prpLdlossPropMain);
						prpLdlossPropFee.setRegistNo(registNo);//报案号
						prpLdlossPropFee.setLossItemName(propertyLossDetailDTO.getLossName());//损失名称
						prpLdlossPropFee.setLossSpeciesCode("99");//物损种类
						prpLdlossPropFee.setFeeTypeCode("99");//费用名称
						prpLdlossPropFee.setLossQuantity(StringUtils.isNotBlank(propertyLossDetailDTO.getLossCount())?new BigDecimal(propertyLossDetailDTO.getLossCount()):new BigDecimal(0));//定损损失数量
						prpLdlossPropFee.setUnitPrice(StringUtils.isNotBlank(propertyLossDetailDTO.getLossUnit())?new BigDecimal(propertyLossDetailDTO.getLossUnit()):new BigDecimal(0));//定损损失单价
						prpLdlossPropFee.setRecyclePrice(StringUtils.isNotBlank(propertyLossDetailDTO.getLossRemnant())?new BigDecimal(propertyLossDetailDTO.getLossRemnant()):new BigDecimal(0));//定损残值
						prpLdlossPropFee.setSumDefloss(StringUtils.isNotBlank(propertyLossDetailDTO.getLossAmount())?new BigDecimal(propertyLossDetailDTO.getLossAmount()).subtract(prpLdlossPropFee.getRecyclePrice()):new BigDecimal(0));//定损金额
						prpLdlossPropFee.setVeriLossQuantity(StringUtils.isNotBlank(propertyLossDetailDTO.getLossCount())?new BigDecimal(propertyLossDetailDTO.getLossCount()):new BigDecimal(0));//损失核损数量
						prpLdlossPropFee.setVeriUnitPrice(StringUtils.isNotBlank(propertyLossDetailDTO.getLossUnit())?new BigDecimal(propertyLossDetailDTO.getLossUnit()):new BigDecimal(0));//核损损失单价
						prpLdlossPropFee.setVeriRecylePrice(StringUtils.isNotBlank(propertyLossDetailDTO.getLossRemnant())?new BigDecimal(propertyLossDetailDTO.getLossRemnant()):new BigDecimal(0));//核损残值
						prpLdlossPropFee.setSumVeriLoss(StringUtils.isNotBlank(propertyLossDetailDTO.getLossAmount())?new BigDecimal(propertyLossDetailDTO.getLossAmount()).subtract(prpLdlossPropFee.getVeriRecylePrice()):new BigDecimal(0));//核损金额
						prpLdlossPropFee.setCreateUser("AUTO");
						prpLdlossPropFee.setCreateTime(new Date());//创建时间
						prpLdlossPropFees.add(prpLdlossPropFee);
					}
				}
				prpLdlossPropMain.setPrpLdlossPropFees(prpLdlossPropFees);
				databaseDao.save(PrpLdlossPropMain.class, prpLdlossPropMain);
			}
			List<PrpLWfTaskVo> prpLWfTaskVos=wfFlowQueryService.findPrpWfTaskVoForIn(registNo, FlowNode.DLoss.name(), FlowNode.DLProp.name());
			PrpLdlossPropMainVo prpLdlossPropMainVo=new PrpLdlossPropMainVo();
			if(prpLdlossPropMain!=null && prpLdlossPropMain.getId()!=null){
				prpLdlossPropMainVo=Beans.copyDepth().from(prpLdlossPropMain).to(PrpLdlossPropMainVo.class);

			}else{
				prpLdlossPropMainVo=propMainVo;
			}
			//第一个财损任务
			if(prpLWfTaskVos!=null && prpLWfTaskVos.size()>0 && losspropMainVo==null){
				WfTaskSubmitVo taskSubmitVo = new WfTaskSubmitVo();
				prpLWfTaskVos.get(0).setHandlerIdKey(prpLdlossPropMainVo.getId().toString());
				wfTaskHandleService.updateTaskIn(prpLWfTaskVos.get(0));
				taskSubmitVo.setCurrentNode(FlowNode.valueOf(prpLWfTaskVos.get(0).getSubNodeCode()));
				taskSubmitVo.setFlowId(prpLWfTaskVos.get(0).getFlowId());
				taskSubmitVo.setFlowTaskId(prpLWfTaskVos.get(0).getTaskId());
				taskSubmitVo.setNextNode(FlowNode.VLProp_LV0);
				taskSubmitVo.setHandleruser("AUTO");
				taskSubmitVo.setAssignUser("AUTO");
				taskSubmitVo.setComCode(comcode);
				taskSubmitVo.setTaskInUser("AUTO");
				taskSubmitVo.setTaskInKey(prpLWfTaskVos.get(0).getTaskInKey());
				List<PrpLWfTaskVo> prpLWfTaskinVos=wfTaskHandleService.submitLossProp(prpLdlossPropMainVo,taskSubmitVo);
				if(prpLWfTaskinVos!=null && prpLWfTaskinVos.size()>0){
					for(PrpLWfTaskVo taskVo:prpLWfTaskinVos){
						if("VLoss".equals(taskVo.getNodeCode())){
							WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
							submitVo.setCurrentNode(FlowNode.VLProp_LV0);
							submitVo.setAssignCom(comcode);
							submitVo.setFlowId(taskVo.getFlowId());
							submitVo.setFlowTaskId(taskVo.getTaskId());
							submitVo.setNextNode(FlowNode.valueOf(FlowNode.END.name()));
							submitVo.setHandleruser("AUTO");
							submitVo.setTaskInKey(taskVo.getTaskInKey());
							submitVo.setTaskInUser("AUTO");
							submitVo.setComCode(comcode);
							List<PrpLWfTaskVo> wtaskVoList = wfTaskHandleService.submitLossProp(prpLdlossPropMainVo,submitVo);
							break;
						}
					}
				}
			}else if(losspropMainVo!=null && propMainVo==null){//后续财损新任务任务
				List<PrpLWfTaskVo> prpLWfTaskoutVos=wfFlowQueryService.findTaskVoForOutBySubNodeCode(registNo,FlowNode.VLProp_LV0.name());
				if(prpLWfTaskoutVos!=null && prpLWfTaskoutVos.size()>0){
					PrpLWfTaskVo prpLWfTaskVo=new PrpLWfTaskVo();
					prpLWfTaskVo.setNodeCode(FlowNode.DLoss.name());
					prpLWfTaskVo.setTaskName(FlowNode.DLProp.getName());
					prpLWfTaskVo.setRegistNo(registNo);
					prpLWfTaskVo.setFlowId(prpLWfTaskoutVos.get(0).getFlowId());
					prpLWfTaskVo.setSubNodeCode(FlowNode.DLProp.name());
					prpLWfTaskVo.setUpperTaskId(prpLWfTaskoutVos.get(0).getTaskId());
					prpLWfTaskVo.setComCode(comcode);
					prpLWfTaskVo.setHandlerStatus(HandlerStatus.INIT);// 未处理
					prpLWfTaskVo.setWorkStatus(WorkStatus.INIT);// 未处理
					prpLWfTaskVo.setTaskInTime(new Date());
					prpLWfTaskVo.setTaskInUser("AUTO");
					prpLWfTaskVo.setTaskInKey(registNo);
					prpLWfTaskVo.setTaskInNode(FlowNode.Chk.name());
					prpLWfTaskVo.setAssignCom(comcode);
					prpLWfTaskVo.setAssignUser("AUTO");
					prpLWfTaskVo.setHandlerUser("AUTO");
					prpLWfTaskVo.setHandlerCom(comcode);
					prpLWfTaskVo.setHandlerTime(new Date());
					prpLWfTaskVo.setItemName(prpLdlossPropMainVo.getLicense());
					prpLWfTaskVo.setTaskOutUser("AUTO");
					prpLWfTaskVo.setTaskOutTime(new Date());
	                prpLWfTaskVo.setAcceptOffTime(null);
	                prpLWfTaskVo.setBussTag(prpLWfTaskVo.getBussTag());
	                prpLWfTaskVo.setShowInfoXML(prpLWfTaskVo.getShowInfoXML());
	                prpLWfTaskVo.setHandlerIdKey(prpLdlossPropMainVo.getId().toString());
	                PrpLWfTaskVo taskInVo=wfTaskHandleService.saveTaskIn(prpLWfTaskVo);
	                if(taskInVo.getTaskId()!=null){
	                	WfTaskSubmitVo taskSubmitVo = new WfTaskSubmitVo();
	    				taskSubmitVo.setCurrentNode(FlowNode.valueOf(taskInVo.getSubNodeCode()));
	    				taskSubmitVo.setFlowId(taskInVo.getFlowId());
	    				taskSubmitVo.setFlowTaskId(taskInVo.getTaskId());
	    				taskSubmitVo.setNextNode(FlowNode.VLProp_LV0);
	    				taskSubmitVo.setHandleruser("AUTO");
	    				taskSubmitVo.setAssignUser("AUTO");
	    				taskSubmitVo.setComCode(comcode);
	    				taskSubmitVo.setTaskInUser("AUTO");
	    				taskSubmitVo.setTaskInKey(prpLWfTaskoutVos.get(0).getTaskInKey());
	    				List<PrpLWfTaskVo> prpLWfTaskinVos=wfTaskHandleService.submitLossProp(prpLdlossPropMainVo,taskSubmitVo);
	    				if(prpLWfTaskinVos!=null && prpLWfTaskinVos.size()>0){
	    					for(PrpLWfTaskVo taskVo:prpLWfTaskinVos){
	    						if("VLoss".equals(taskVo.getNodeCode())){
	    							WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
	    							submitVo.setCurrentNode(FlowNode.VLProp_LV0);
	    							submitVo.setAssignCom(comcode);
	    							submitVo.setFlowId(taskVo.getFlowId());
	    							submitVo.setFlowTaskId(taskVo.getTaskId());
	    							submitVo.setNextNode(FlowNode.valueOf(FlowNode.END.name()));
	    							submitVo.setHandleruser("AUTO");
	    							submitVo.setTaskInKey(taskVo.getTaskInKey());
	    							submitVo.setTaskInUser("AUTO");
	    							submitVo.setComCode(comcode);
	    							List<PrpLWfTaskVo> wtaskVoList = wfTaskHandleService.submitLossProp(prpLdlossPropMainVo,submitVo);
	    							break;
	    						}
	    					}
	    				}
	                }
				}
				
			}
				
		}catch(Exception e){
			e.printStackTrace();
			logger.info("平安联盟财定损报错：{}",e);
			resultBean.fail("平安联盟财定损报错："+e.getMessage());
		}
		return resultBean;
	}
	 /**
 	 * 时间转换方法
 	 *  String 类型转换类型Date
 	 * @param strDate
 	 * @return
 	 * @throws ParseException
 	 */
 	private static Date StringtoDate(String strDate){
 		Date str=null;
 		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 		  if(StringUtils.isNotBlank(strDate)){
 			  try {
 				str=format.parse(strDate);
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 		}
 		return str;
 	}
}
