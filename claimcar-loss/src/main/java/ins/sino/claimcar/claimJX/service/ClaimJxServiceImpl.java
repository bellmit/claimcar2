package ins.sino.claimcar.claimJX.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DataUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.RepairFactoryService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.genilex.comResVo.SoapEnvelopeVo;
import ins.sino.claimcar.genilex.dlossReqVo.BodyVo;
import ins.sino.claimcar.genilex.dlossReqVo.ClaimMainVo;
import ins.sino.claimcar.genilex.dlossReqVo.DataPacketVo;
import ins.sino.claimcar.genilex.dlossReqVo.EntitiesVo;
import ins.sino.claimcar.genilex.dlossReqVo.EvalPersonDiagnosisVo;
import ins.sino.claimcar.genilex.dlossReqVo.EvalPersonHospitalVo;
import ins.sino.claimcar.genilex.dlossReqVo.EvalPersonInjuryVo;
import ins.sino.claimcar.genilex.dlossReqVo.EvalPersonVo;
import ins.sino.claimcar.genilex.dlossReqVo.EvalProtectVo;
import ins.sino.claimcar.genilex.dlossReqVo.EvalVehicleChangePartVo;
import ins.sino.claimcar.genilex.dlossReqVo.EvalVehicleLossPartVo;
import ins.sino.claimcar.genilex.dlossReqVo.EvalVehicleMaterialVo;
import ins.sino.claimcar.genilex.dlossReqVo.EvalVehicleRepairVo;
import ins.sino.claimcar.genilex.dlossReqVo.EvalVehicleVo;
import ins.sino.claimcar.genilex.dlossReqVo.EvalVo;
import ins.sino.claimcar.genilex.dlossReqVo.PersonLossFeeVo;
import ins.sino.claimcar.genilex.dlossReqVo.SoapenVo;
import ins.sino.claimcar.genilex.dlossService.ClaimJxService;
import ins.sino.claimcar.genilex.vo.common.AccountInfo;
import ins.sino.claimcar.genilex.vo.common.FraudRequest;
import ins.sino.claimcar.genilex.vo.common.Requestor;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersExtVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropFeeVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path("claimJxService")
public class ClaimJxServiceImpl implements ClaimJxService{
	private static Logger logger = LoggerFactory.getLogger(ClaimJxServiceImpl.class);
	@Autowired
	LossCarService lossCarService;
	@Autowired
	PersTraceDubboService persTraceDubboService;
	@Autowired
	PersTraceService persTraceService;
	@Autowired
	DeflossHandleService deflossHandleService;
	@Autowired
	private PropTaskService propTaskService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private ClaimService claimService;

	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	CiClaimPlatformLogService platformLogService;
	@Autowired
    private ClaimInterfaceLogService interfaceLogService;
	@Autowired
	RepairFactoryService repairFactoryService;
	@Override
	public void sendDlossInfor(String registNo,String taskId, SysUserVo userVo){
		PrpLRegistVo registVo=registQueryService.findByRegistNo(registNo);//报案信息
		SoapenVo soapenVo=new SoapenVo();//精励联讯主信息
		soapenVo=soapenVoforParams(registNo,userVo,soapenVo,taskId);//组装接口报文信息
		String reqXml=ClaimBaseCoder.objToXml(soapenVo);//请求xml
	    logger.info("精励联讯发送信息send---------------------------"+reqXml);
	    String url= SpringProperties.getProperty("GENILEX_URL");
	    SoapEnvelopeVo soapEnvelopeVo = new SoapEnvelopeVo();
        String responseXml = "";
        ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
        Date date = new Date();
	    try {
	    	XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
            stream.autodetectAnnotations(true);
            stream.setMode(XStream.NO_REFERENCES);
            stream.aliasSystemAttribute(null,"class");// 去掉 class属性
			String resXml=requestPlatform(reqXml,url);
			logger.info("精励联讯返回送信息return---------------------------"+resXml);
			soapEnvelopeVo=ClaimBaseCoder.xmlToObj(resXml,SoapEnvelopeVo.class);
			if("0041".equals(soapEnvelopeVo.getBodyVo().getDataPacketVo().getProductResponseVo().getFraudResponseVo().getProductResultVo().getResultCode())){
                logVo.setStatus("1");
                logVo.setErrorCode("true");
            }else {
                logVo.setStatus("0");
                logVo.setErrorCode(soapEnvelopeVo.getBodyVo().getDataPacketVo().getProductResponseVo().getFraudResponseVo().getProductResultVo().getResultCode());
                logVo.setErrorMessage(soapEnvelopeVo.getBodyVo().getDataPacketVo().getProductResponseVo().getFraudResponseVo().getProductResultVo().getResultDesc());
            }
            responseXml = stream.toXML(soapEnvelopeVo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("精励联讯请求报错---------------------------"+e.getMessage()+e);
			logVo.setStatus("0");
            logVo.setErrorCode("false");
            logVo.setErrorMessage(e.getMessage()+e);
		}finally{
			logVo.setBusinessType(BusinessType.GENILEX_Dloss.name());
            logVo.setBusinessName(BusinessType.GENILEX_Dloss.getName());
            logVo.setFlag(taskId);
            logVo.setRegistNo(registVo.getRegistNo());
            logVo.setOperateNode(FlowNode.DLoss.name());
            logVo.setComCode(userVo.getComCode());
            logVo.setRequestTime(date);
            logVo.setRequestUrl(url);
            logVo.setCreateUser(userVo.getUserCode());
            logVo.setCreateTime(date);
            logVo.setRequestXml(reqXml);
            logVo.setResponseXml(responseXml);
            interfaceLogService.save(logVo);
            
		}
	    
	}
	
	
	private SoapenVo soapenVoforParams(String registNo,SysUserVo userVo,SoapenVo soapenVo,String taskId){
		PrpLRegistVo prplregist=registQueryService.findByRegistNo(registNo);//报案信息
		List<CiClaimPlatformLogVo> logVos=platformLogService.findLogByBussNoAndRequestName("报案登记 ",registNo);//平台信息
		PrpLCheckVo prplcheckVo=checkTaskService.findCheckVoByRegistNo(registNo);//查勘信息
		List<PrpLClaimVo> prpLClaimVos=claimService.findClaimListByRegistNo(registNo);
		List<PrpLCMainVo>  prplcmainVos=policyViewService.findPrpLCMainVoListByRegistNo(registNo);
		List<PrpLDlossCarMainVo> lossCars=lossCarService.findLossCarMainByRegistNo(registNo);//定损车辆信息
		List<PrpLDlossPersTraceMainVo> persTraces=persTraceDubboService.findPersTraceMainVoList(registNo);//定损人伤主表
		List<PrpLdlossPropMainVo> propTasks=propTaskService.findPropMainListByRegistNo(registNo);//财产信息
		String dlossAll="1";//所有定损是否都已完成,1都已完成，0未全部完成
		Boolean dlossCarflag=wfTaskHandleService.existTaskInByNodeCode(registNo,FlowNode.DLoss.toString());
		Boolean dlossPropflag1=wfTaskHandleService.existTaskInBySubNodeCode(registNo,FlowNode.PLFirst.toString());//人伤首次跟踪
		Boolean dlossPropflag2=wfTaskHandleService.existTaskInBySubNodeCode(registNo,FlowNode.PLNext.toString());//人伤后续跟踪
		if(dlossCarflag || dlossPropflag1 || dlossPropflag2){
			dlossAll="0";
		}
		BodyVo bodyReqVo=new BodyVo();//体信息
		DataPacketVo dataPacketVo=new DataPacketVo();
		Requestor requestor=new Requestor();
		AccountInfo accountInfo =new AccountInfo();//银行账户信息
		dataPacketVo.setPacketType("REQUEST");
		requestor.setReference(taskId);//交易唯一标识，报案号拼接时间
		requestor.setTimestamp(DateFormatStringXs(new Date()));//交易日期
		requestor.setLineOfBusiness("PM");//产品线
		requestor.setPointOfRequest("POC");//业务环节
		requestor.setTransactionType("C");//交易类型
		requestor.setEchoEntities("false");//是否传回实体
		requestor.setEchoProductRequests("false");//是否回传产品请求
		accountInfo.setCryptType("04");//密码加密类型，默认不加密
        String userName = SpringProperties.getProperty("GENILEX_USERNAME");
        String userPswd = SpringProperties.getProperty("GENILEX_USERPSWD");
        accountInfo.setUserName(userName);
        accountInfo.setUserPswd(userPswd);
		requestor.setAccountInfo(accountInfo);
		FraudRequest fraudRequest = new FraudRequest();
        fraudRequest.setProductRequestId("03");
        fraudRequest.setReportNo(registNo);
        fraudRequest.setScoredType("01");
        fraudRequest.setServiceType("C003");
        List<FraudRequest> fraudRequests = new ArrayList<FraudRequest>();
        fraudRequests.add(fraudRequest);
		EntitiesVo entitiesVo=new EntitiesVo();//定核损信息列表
		List<EvalVo> Evalslist=new ArrayList<EvalVo>();
		EvalVo evalVo=new EvalVo();//定核损信息
		evalVo.setReportNo(registNo);//报案号
		if("1".equals(dlossAll)){
			evalVo.setIsCompleteEval("true");//定损是否全部已完成
		}else{
			evalVo.setIsCompleteEval("false");
		}
		List<EvalVehicleVo> evalVehicleVos=new ArrayList<EvalVehicleVo>();
		List<PrpLWfTaskVo> dlossInwftaskVos=wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.DLoss.toString());//车辆定损in表数据
		List<PrpLWfTaskVo> dlossOutwftaskVos=wfFlowQueryService.findTaskVoForOutByNodeCode(registNo, FlowNode.DLoss.toString());//车辆定损out表数据
		if(lossCars!=null && lossCars.size()>0){
			for(PrpLDlossCarMainVo carMainVo:lossCars){
				PrpLDlossCarInfoVo CarInfoVo=lossCarService.findPrpLDlossCarInfoVoById(carMainVo.getCarId());
				EvalVehicleVo evalVehicleVo=new EvalVehicleVo();
				evalVehicleVo.setEvalID(carMainVo.getId()+"");//定损主表Id
				evalVehicleVo.setLicensePlateNo(carMainVo.getLicenseNo());//损失车辆号牌号码
				evalVehicleVo.setLicensePlateType(CarInfoVo.getLicenseType());//损失车辆号牌种类代码
				evalVehicleVo.setEngineNo(CarInfoVo.getEngineNo());//损失车辆发动机号
				evalVehicleVo.setVin(CarInfoVo.getVinNo());//损失车辆VIN码
				evalVehicleVo.setModel(CarInfoVo.getModelCode());//车辆型号
				evalVehicleVo.setDriverName(CarInfoVo.getDriveName());//驾驶人员姓名
				evalVehicleVo.setCertiType(CarInfoVo.getIdentifyType());//出险驾驶员证件类型
				evalVehicleVo.setCertiCode(CarInfoVo.getIdentifyNo());//出险驾驶员证件号码
				evalVehicleVo.setDriverLicenseNo(CarInfoVo.getDrivingLicenseNo());//出险车辆驾驶证号码
				evalVehicleVo.setLossDriverRelationTele(prplregist.getInsuredPhone());//出险驾驶员联系电话
				if(carMainVo.getSumVeriLossFee() != null){
					evalVehicleVo.setUnderDefLoss(carMainVo.getSumVeriLossFee()+"");//核损金额
				}else{
					evalVehicleVo.setUnderDefLoss("");//核损金额
				}
				String flag="0";//如果该定损车辆在in表中就为1
				if(dlossInwftaskVos!=null && dlossInwftaskVos.size()>0){
					for(PrpLWfTaskVo vo:dlossInwftaskVos){
						if(StringUtils.isNotBlank(vo.getItemName()) && vo.getItemName().contains(CarInfoVo.getLicenseNo()) && "DLCar".equals(vo.getSubNodeCode())){
							evalVehicleVo.setTaskStatus(vo.getWorkStatus());//任务状态
							flag="1";
							break;
						}
					}
				}
				if("0".equals(flag)){
					if(dlossOutwftaskVos!=null && dlossOutwftaskVos.size()>0){
						for(PrpLWfTaskVo vo:dlossOutwftaskVos){
							if(StringUtils.isNotBlank(vo.getItemName()) && vo.getItemName().contains(CarInfoVo.getLicenseNo()) && "DLCar".equals(vo.getSubNodeCode())){
								evalVehicleVo.setTaskStatus(vo.getWorkStatus());//任务状态
								break;
							}
						}
					}
				}
				evalVehicleVo.setLossType("0");//损失类型
				if("1".equals(carMainVo.getDeflossCarType())){
					evalVehicleVo.setLossDetailType("01");//损失类型细分--标的
				}else{
					evalVehicleVo.setLossDetailType("04");//损失类型细分--三者
				}
				evalVehicleVo.setEstimateCode(carMainVo.getHandlerCode());//定损人员代码
				evalVehicleVo.setUnderWriteCode(carMainVo.getUnderWriteCode());//核损人员代码
				evalVehicleVo.setEstimateStartTime(DateFormatStringXs(carMainVo.getDeflossDate()));//定损时间
				evalVehicleVo.setUnderEndTime(DateFormatStringXs(carMainVo.getUnderWriteEndDate()));//核损完成时间
				evalVehicleVo.setEstimateAddr(carMainVo.getDefSite());//定损地点  
				if(carMainVo.getSumRemnant() != null){
					evalVehicleVo.setRemnant(carMainVo.getSumRemnant()+"");//残值回收预估金额
				}else{
					evalVehicleVo.setRemnant("");//残值回收预估金额
				}
				evalVehicleVo.setTotalManHour("");//配件总工时
				String isChangeOrRepair="0";//是否换件或修理  0否，1是
				List<PrpLDlossCarCompVo> carCompVos=carMainVo.getPrpLDlossCarComps();//换件
				List<PrpLDlossCarRepairVo> carRepairVos=carMainVo.getPrpLDlossCarRepairs();//修理
				if((carCompVos!=null && carCompVos.size()>0) || (carRepairVos!=null && carRepairVos.size()>0)){
					isChangeOrRepair="1";
				}
				evalVehicleVo.setIsChangeOrRepair(isChangeOrRepair);//是否修理或更换配件
				PrpLRepairFactoryVo repairFactoryVo=null;
				if(carMainVo!=null && StringUtils.isNotBlank(carMainVo.getRepairFactoryCode())){
					repairFactoryVo=repairFactoryService.findFactoryById(carMainVo.getRepairFactoryCode());
					if(repairFactoryVo==null){//解决历史数据问题
						repairFactoryVo=repairFactoryService.findFactoryByCode(carMainVo.getRepairFactoryCode(), carMainVo.getRepairFactoryType());
					}
				   
				}
				if(repairFactoryVo!=null){
					evalVehicleVo.setRepairFactoryCode(repairFactoryVo.getFactoryCode());//修理厂代码
				}else{
					evalVehicleVo.setRepairFactoryCode(carMainVo.getRepairFactoryCode());
				}
				evalVehicleVo.setRepairFactoryName(carMainVo.getRepairFactoryName());//修理机构名称/定损修理厂
				evalVehicleVo.setRepairFactoryType(carMainVo.getRepairFactoryType());//修理机构类型/定损修理厂
				evalVehicleVo.setRepairFactoryCertiCode("");//修理机构组织机构代码
				evalVehicleVo.setIsSingleAccident(prplcheckVo.getSingleAccidentFlag());//是否单车事故
				evalVehicleVo.setIsAllFlag("");//是否按全损或包干修复定损
				evalVehicleVo.setIsAllLossFlag(carMainVo.getIsTotalLoss());//是否定损全损
				evalVehicleVo.setIsAllRepairFlag("");//是否按包干修复定损
				if(CarInfoVo.getActualValue() != null){
					evalVehicleVo.setActualamt(CarInfoVo.getActualValue()+"");//实际价值
				}else{
					evalVehicleVo.setActualamt("");//实际价值
				}
				
				evalVehicleVo.setSumamt("");//全损或包干修复定损金额合计
				evalVehicleVo.setDoSalvageQuoted("");//是否进行残值报价
				if(carMainVo.getSumLossFee() != null){
					evalVehicleVo.setNormalamt(carMainVo.getSumLossFee()+"");//正常定损金额
				}else{
					evalVehicleVo.setNormalamt("");//正常定损金额
				}
				evalVehicleVo.setSalvageQuotedAmt("");//残值报价金额
				evalVehicleVo.setDoAuction("");//是否进行拍卖处理
				evalVehicleVo.setAuctionAmt("");//拍卖金额
				evalVehicleVo.setFittingsDataProvider("");//定型来源
				evalVehicleVo.setCarOwnerName(CarInfoVo.getCarOwner());//车主姓名
				evalVehicleVo.setBrandCode(CarInfoVo.getBrandCode());//厂牌车型代码
				evalVehicleVo.setBrandName(CarInfoVo.getBrandName());//厂牌车型名称
				evalVehicleVo.setFrameNo(CarInfoVo.getFrameNo());//车架号
				evalVehicleVo.setJyCarId("");//精友临时定损车辆信息表主键
				evalVehicleVo.setJyBrandId("");//精友配件平台车型主键
				evalVehicleVo.setVehicleCategory(CarInfoVo.getLossCarKindCode());//车辆种类代码
				evalVehicleVo.setThirdcompanycode("");//第三方保险公司代码
				evalVehicleVo.setThirdPolicyNo("");//第三方保单号码
				String BFlag="0";//是否包含商业险，0不包含，1包含
				String CFlag="0";//是否包含交强险，0不包含，1包含
				if(prplcmainVos!=null && prplcmainVos.size()>0){
					for(PrpLCMainVo vo:prplcmainVos){
						if("1101".equals(vo.getRiskCode())){
							CFlag="1";
							evalVehicleVo.setThirdIaPolicyNo(vo.getPolicyNo());//强制险保单号
							evalVehicleVo.setThirdIaInsurerCode(CarInfoVo.getCiInsureComCode());//交强险承保公司
							evalVehicleVo.setThirdIaInsurerArea(CarInfoVo.getCiInsurerArea());//交强险承保地区

						}else{
							BFlag="1";
							evalVehicleVo.setThirdCaPolicyNo(vo.getPolicyNo());//商业险保单号
							evalVehicleVo.setThirdCaInsurerCode(CarInfoVo.getBiInsureComCode());//商业险承保公司
							evalVehicleVo.setThirdCaInsurerArea(CarInfoVo.getBiInsurerArea());//商业险承保地区
						}
					}
				}
				if("0".equals(BFlag)){
					evalVehicleVo.setThirdCaPolicyNo("");//商业险保单号
					evalVehicleVo.setThirdCaInsurerCode("");//商业险承保公司
					evalVehicleVo.setThirdCaInsurerArea("");//商业险承保地区
				}
				if("0".equals(CFlag)){
					evalVehicleVo.setThirdIaPolicyNo("");//强制险保单号
					evalVehicleVo.setThirdIaInsurerCode("");//交强险承保公司
					evalVehicleVo.setThirdIaInsurerArea("");//交强险承保地区

				}
				evalVehicleVo.setServicingInvoiceCode("");//维修发票号码
				evalVehicleVo.setInvoiceAmornt("");//维修发票金额  
				evalVehicleVo.setDerogationType("");//减损类型
				evalVehicleVo.setDerogationAmt("");//减损金额
				evalVehicleVo.setRemark("");//备注
				evalVehicleVo.setCreateBy(carMainVo.getCreateUser());//创建者
				evalVehicleVo.setCreateTime(DateFormatStringXs(carMainVo.getCreateTime()));//创建日期
				evalVehicleVo.setUpdateBy(carMainVo.getUpdateUser());//更新者
				evalVehicleVo.setUpdateTime(DateFormatStringXs(carMainVo.getUpdateTime()));//更新时间
				List<EvalVehicleChangePartVo> evalVehicleChangePartVos =new ArrayList<EvalVehicleChangePartVo>();//车辆更换配件明细列表
				List<PrpLDlossCarCompVo> carCompListVos=carMainVo.getPrpLDlossCarComps();//车辆更换配件明细列表
				if(carCompListVos!=null && carCompListVos.size()>0){
					for(PrpLDlossCarCompVo compVo:carCompListVos){
						EvalVehicleChangePartVo partVo=new EvalVehicleChangePartVo();
						partVo.setEvalID(carMainVo.getId()+"");//定核损ID
						partVo.setHandAddpartFlag("2");//配件平台手工添加标志
						partVo.setItemCode(compVo.getCompCode());//精友零件编码
						partVo.setItemName(compVo.getCompName());//配件名称
						partVo.setFactoryPartCode(compVo.getOriginalId());//原厂零件号
						partVo.setPartGroupCode(compVo.getPartGroupCode());//配件分组编码
						partVo.setPartGroupName(compVo.getPartGroupName());//配件分组名称
						if(compVo.getQuantity() != null ){
							partVo.setEvalPartPrice((DataUtils.NullToZero(compVo.getSumDefLoss()).intValue()/compVo.getQuantity())+"");//定损单价金额
						}else{
							partVo.setEvalPartPrice("");
						}
						partVo.setEvalPartAmount(compVo.getQuantity()+"");//定损配件数量
						if(compVo.getSumDefLoss() != null){
							partVo.setEvalPartSum(compVo.getSumDefLoss()+"");//定损金额
							partVo.setEvalPartSumFirst(compVo.getSumDefLoss()+"");//首次定损金额
						}else{
							partVo.setEvalPartSum("");//定损金额
							partVo.setEvalPartSumFirst("");//首次定损金额
						}
						if(compVo.getAuditCount() != null ){
							partVo.setEstiPartPrice((DataUtils.NullToZero(compVo.getSumCheckLoss()).intValue()/DataUtils.NullToZero(compVo.getAuditCount()).intValue())+"");//核价单价金额
						}else{
							partVo.setEstiPartPrice("");
						}
						if(compVo.getSumCheckLoss() != null){
							partVo.setEstiPartSum(compVo.getSumCheckLoss()+"");//核价金额
						}else{
							partVo.setEstiPartSum("");//核价金额
						}
						if(compVo.getVeripRestFee() != null){
							partVo.setEstiRemainsPrice(compVo.getVeripRestFee()+"");//核价残值
						}else{
							partVo.setEstiRemainsPrice("");//核价残值
						}
						if(compVo.getVeriQuantity() != null ){
							partVo.setApprPartPrice((DataUtils.NullToZero(compVo.getSumVeriLoss()).intValue()/compVo.getVeriQuantity())+"");//核损单价金额	
							partVo.setApprPartAmount(compVo.getVeriQuantity()+"");//核损配件数量
						}else{
							partVo.setApprPartPrice("");
							partVo.setApprPartAmount("");//核损配件数量
						}
						if(compVo.getSumVeriLoss() != null){
							partVo.setApprPartSum(compVo.getSumVeriLoss()+"");//核损金额
						}else{
							partVo.setApprPartSum("");//核损金额
						}
						if(compVo.getNativeMarketPrice() != null){
							partVo.setLocalPrice(compVo.getNativeMarketPrice()+"");//本地价格
						}else{
							partVo.setLocalPrice("");//本地价格
						}
						if(compVo.getSysMatchPrice() != null){
							partVo.setSystemPrice(compVo.getSysMatchPrice()+"");//系统价格
						}else{
							partVo.setSystemPrice("");//系统价格
						}
						if(compVo.getNativeMarketPrice() != null){
							partVo.setAreaPrice(compVo.getNativeMarketPrice()+"");//区域价格
						}else{
							partVo.setAreaPrice("");//区域价格
						}
						partVo.setResurveyFlag(compVo.getReCheckFlag());//复勘标志
						partVo.setSelfPayPrice((DataUtils.NullToZero(compVo.getSumDefLoss()).intValue()-DataUtils.NullToZero(compVo.getRestFee()).intValue())+"");//自付
						if(compVo.getRestFee() != null){
							partVo.setRemainsPrice(compVo.getRestFee()+"");//残值
						}else{
							partVo.setRemainsPrice("");//区域价格
						}
						partVo.setResurveyPrice("");//复勘价
						partVo.setEvalFloatRatio("");//管理费率
						partVo.setFitBackFlag(compVo.getRecycleFlag());//旧件回收标志
						partVo.setRecycleType("");//回收类型
						partVo.setOutsideRadio("1");
						partVo.setOutsideRepairName(compVo.getRepairFactoryName());//专修厂名称
						partVo.setOutsideRepairCode(compVo.getRepairFactoryCode());//专修厂代码
						if(compVo.getNative4SPrice() != null){
							partVo.setOutsidePrice(compVo.getNative4SPrice()+"");//专修价格
						}else{
							partVo.setOutsidePrice("");//专修价格
						}
						partVo.setIsSubjoin("");//是否增补
						partVo.setCreateBy(carMainVo.getCreateUser());//创建者
						partVo.setCreatetime(DateFormatStringXs(carMainVo.getCreateTime()));//创建日期
						partVo.setUpdateby(carMainVo.getUpdateUser());//更新者
						partVo.setUpdatetime(DateFormatStringXs(carMainVo.getUpdateTime())+"");//更新日期
						evalVehicleChangePartVos.add(partVo);

					}
					
				}
				evalVehicleVo.setEvalVehicleChangeParts(evalVehicleChangePartVos);
				
				//车辆维修明细列表
				List<EvalVehicleRepairVo> EvalVehicleRepairs=new ArrayList<EvalVehicleRepairVo>();
				List<PrpLDlossCarRepairVo> repairVos= carMainVo.getPrpLDlossCarRepairs();
                if(repairVos!=null && repairVos.size()>0){
                	for(PrpLDlossCarRepairVo vo:repairVos){
                		EvalVehicleRepairVo repairVo=new EvalVehicleRepairVo();
                		repairVo.setEvalID(carMainVo.getId()+"");//定核损ID
                		repairVo.setItemCode(vo.getCompCode());//项目编码
                		repairVo.setItemName(vo.getCompName());//项目名称
                		repairVo.setRepairName(vo.getRepairName());//修理项目名称
                		repairVo.setRepairCode(vo.getRepairCode());//修理编码
                		repairVo.setEvalManHour(vo.getManHour()+"");//定损工时
                		if(vo.getManHourFee() != null){
                			repairVo.setEvalHourSum(vo.getManHourFee()+"");//定损工时金额
                			repairVo.setEstiHourSum(vo.getManHourFee()+"");//核价工时金额
                		}else{
                			repairVo.setEvalHourSum("");//定损工时金额
                			repairVo.setEstiHourSum("");//核价工时金额
                		}
                		if(vo.getSumDefLoss() != null){
                			repairVo.setEstiRepairSum(vo.getSumDefLoss()+"");//定损金额
                			repairVo.setEvalHourSumFirst(vo.getSumDefLoss()+"");//首次定损工时金额
                			repairVo.setEstiRepairSum(vo.getSumDefLoss()+"");//核价金额
                		}else{
                			repairVo.setEstiRepairSum("");//定损金额
                			repairVo.setEvalHourSumFirst("");//首次定损工时金额
                			repairVo.setEstiRepairSum("");//核价金额
                		}
                		repairVo.setEstiManHour(vo.getManHour()+"");//核价工时
                		if(vo.getVeriManHourFee() != null){
                			repairVo.setApprHourSum(vo.getVeriManHourFee()+"");//核损工时金额
                		}else{
                			repairVo.setApprHourSum("");//核损工时金额
                		}
                		if(vo.getSumVeriLoss() != null){
                			repairVo.setApprRepairSum(vo.getSumVeriLoss()+"");//核损金额
                		}else{
                			repairVo.setApprRepairSum("");//核损金额
                		}
                		repairVo.setRepairlocalPrice("");//本地价格
                		repairVo.setReferenceHour("");//参考工时
                		repairVo.setReferencePrice("");//参考工时费
                		repairVo.setEvalFloatRatio("");//管理费率
                		repairVo.setIsFullPaint("");//是否是全车喷漆
                		repairVo.setIsSubjoin("");//是否增补
                		repairVo.setCreateBy(carMainVo.getCreateUser());//创建者
                		repairVo.setCreateTime(DateFormatStringXs(carMainVo.getCreateTime()));//创建日期
                		repairVo.setUpdateBy(carMainVo.getUpdateUser());//更新者
                		repairVo.setUpdateTime(DateFormatStringXs(carMainVo.getUpdateTime())+"");//更新日期
                			
                		EvalVehicleRepairs.add(repairVo);
                	}
                }
				
                evalVehicleVo.setEvalVehicleRepairs(EvalVehicleRepairs);
                //暂时不传
                List<EvalVehicleLossPartVo> evalVehicleLossParts=new ArrayList<EvalVehicleLossPartVo>();//车辆损失部位列表
                
                List<EvalVehicleMaterialVo> evalVehicleMaterials=new ArrayList<EvalVehicleMaterialVo>();//车辆维修辅料费列表
                List<PrpLDlossCarMaterialVo> materialVos=carMainVo.getPrpLDlossCarMaterials();
                  if(materialVos!=null && materialVos.size()>0){
                	  for(PrpLDlossCarMaterialVo vo:materialVos){
                		  EvalVehicleMaterialVo materialVo=new EvalVehicleMaterialVo();
                		  materialVo.setEvalID(carMainVo.getId()+"");//定核损ID
                		  materialVo.setItemCode(vo.getId()+"");//项目编码
                		  materialVo.setItemName("");//项目名称
                		  materialVo.setMateTypeName(vo.getMaterialName());//辅料项目类型名称
                		  materialVo.setMateTypeName("");//辅料项目类型编码
            			  if(vo.getUnitPrice() != null){
            				  materialVo.setEvalUnitPrice(vo.getUnitPrice()+"");//定损单价
                		  }else{
                			  materialVo.setEvalUnitPrice("");//定损单价
                		  }
            			  if(vo.getMaterialFee() != null){
            				  materialVo.setEvalMateSum(vo.getMaterialFee()+"");//定损金额
                		  }else{
                			  materialVo.setEvalMateSum("");//定损金额
                		  }
            			  if(vo.getAssisCount() != null){
            				  materialVo.setEvalMateAmount(vo.getAssisCount()+"");//定损数量
                		  }else{
                			  materialVo.setEvalMateAmount("");//定损数量
                		  }
                		  if(vo.getAuditPrice() != null){
                			  materialVo.setEstiUnitPrice(vo.getAuditPrice()+"");//核价单价
                		  }else{
                			  materialVo.setEstiUnitPrice("");//核价单价
                		  }
                		  if(vo.getAuditPrice() != null){
                			  materialVo.setEstiUnitPrice(vo.getAuditPrice()+"");//核价单价
                		  }else{
                			  materialVo.setEstiUnitPrice("");//核价单价
                		  }
                		  if(vo.getAuditMaterialFee() != null){
                			  materialVo.setEstiMateSum(vo.getAuditMaterialFee()+"");//核价金额
                		  }else{
                			  materialVo.setEstiMateSum("");//核价金额
                		  }
                		  if(vo.getAuditLossPrice() != null){
                			  materialVo.setApprUnitPrice(vo.getAuditLossPrice()+"");//核损单价
                		  }else{
                			  materialVo.setApprUnitPrice("");//核损单价
                		  }
                		  if(vo.getAuditLossMaterialFee() != null){
                			  materialVo.setApprMateSum(vo.getAuditLossMaterialFee()+"");//核损金额
                		  }else{
                			  materialVo.setApprMateSum("");//核损金额
                		  }
                		  materialVo.setReferencePrice("");//参考价格
                		  if(vo.getAuditCount() != null){
                			  materialVo.setEstiMateAmount(vo.getAuditCount()+"");//核价数量
                		  }else{
                			  materialVo.setEstiMateAmount("");//核价数量
                		  }
                		  materialVo.setEstiFloatRatio("");//核价费率
                		  materialVo.setIsSubjoin("");//是否增补
                		  materialVo.setCreateBy(carMainVo.getCreateUser());//创建者
                		  materialVo.setCreateTime(DateFormatStringXs(carMainVo.getCreateTime()));//创建日期
                		  materialVo.setUpdateBy(carMainVo.getUpdateUser());//更新者
                		  materialVo.setUpdateTime(DateFormatStringXs(carMainVo.getUpdateTime())+"");//更新日期
                		  evalVehicleMaterials.add(materialVo);
                	  }
                  }

                  evalVehicleVo.setEvalVehicleMaterials(evalVehicleMaterials);
                  
                  evalVehicleVos.add(evalVehicleVo);

			}
		}
			evalVo.setEvalVehicles(evalVehicleVos);
		
			List<EvalProtectVo> evalProtects=new ArrayList<EvalProtectVo>();//财产信息列表
            if(propTasks!=null && propTasks.size()>0){
            	for(PrpLdlossPropMainVo propMianVo:propTasks){
            	  List<PrpLdlossPropFeeVo> propFeevos=propMianVo.getPrpLdlossPropFees();
            	  if(propFeevos!=null && propFeevos.size()>0){
            		  for(PrpLdlossPropFeeVo vo:propFeevos){
            			  EvalProtectVo  evalProtectVo = new EvalProtectVo();
            			  evalProtectVo.setEvalID(propMianVo.getId()+"");//定核损ID
            			  evalProtectVo.setItemCode(vo.getId()+"");//项目编码
            			  evalProtectVo.setItemName(vo.getLossItemName());//项目名称	
            			  evalProtectVo.setLossDesc(vo.getLossItemName());//损失描述
            			  evalProtectVo.setLossNum(vo.getLossQuantity()+"");//损失数量
            			  if(vo.getSumDefloss() != null){
            				  evalProtectVo.setLossAmt(vo.getSumDefloss()+"");//损失金额
                			  evalProtectVo.setEvalAmt(vo.getSumDefloss()+"");//定损金额	
                			  evalProtectVo.setEstiAmt(vo.getSumDefloss()+"");//核价金额
                		  }else{
                			  evalProtectVo.setLossAmt("");//损失金额
                			  evalProtectVo.setEvalAmt("");//定损金额	
                			  evalProtectVo.setEstiAmt("");//核价金额
                		  }
            			  if(vo.getSumVeriLoss() != null){
            				  evalProtectVo.setApprAmt(vo.getSumVeriLoss()+"");//核损金额
                		  }else{
                			  evalProtectVo.setApprAmt("");//核损金额
                		  }
            			  if(vo.getRecyclePrice() != null){
            				  evalProtectVo.setRemainsPrice(vo.getRecyclePrice()+"");//残值
                		  }else{
                			  evalProtectVo.setRemainsPrice("");//残值
                		  }
            			  evalProtectVo.setRecoveryFlag(vo.getRecycleFlag());//是否需回收标志		
            			  evalProtectVo.setOwner("");//所有人/管理人		
            			  evalProtectVo.setTaskStatus("");//任务状态
            			  evalProtectVo.setLossType("2");//损失类型
            			  if("0".equals(propMianVo.getLossType())){
            				  evalProtectVo.setLossDetailType("07");//车外--损失类型细分
            			  }else if("1".equals(propMianVo.getLossType())){
            				  evalProtectVo.setLossDetailType("05");//标的
            			  }else{
            				  evalProtectVo.setLossDetailType("06");//三者
            			  }
            			  if("1".equals(propMianVo.getLossType())){
            				  evalProtectVo.setProtectProperty("1"); //本车- 财产属性
            			  }else{
            				  evalProtectVo.setProtectProperty("2"); //车外 -财产属性
            			  }
            			  evalProtectVo.setEstimateCode(propMianVo.getHandlerCode());//	定损人员代码		
            			  evalProtectVo.setUnderWriteCode(propMianVo.getUnderWriteCode());//核损人员代码
            			  evalProtectVo.setEstimateAddr("");//定损地点		
            			  evalProtectVo.setRemark(propMianVo.getDefrescuereMark());//备注
            			  evalProtectVo.setCreateBy(propMianVo.getCreateUser());//创建者
            			  evalProtectVo.setCreateTime(DateFormatStringXs(propMianVo.getCreateTime()));//创建日期
            			  evalProtectVo.setUpdateBy(propMianVo.getUpdateUser());//更新者
            			  evalProtectVo.setUpdateTime(DateFormatStringXs(propMianVo.getUpdateTime()));//更新日期
            			  evalProtects.add(evalProtectVo);
            		  }
            	  }
            		

            	}
            }
            evalVo.setEvalProtects(evalProtects);//财产损失情况列表
            
            //人员伤亡情况列表
            List<EvalPersonVo> evalPersons=new ArrayList<EvalPersonVo>();
            
            if(persTraces!=null && persTraces.size()>0){
            	for(PrpLDlossPersTraceMainVo personVo:persTraces){
            		int index=0;
            		List<PrpLDlossPersTraceVo> persTraceVos=personVo.getPrpLDlossPersTraces();
            		if(persTraceVos!=null && persTraceVos.size()>0){
            			for(PrpLDlossPersTraceVo persTraceVo:persTraceVos){
            				index++;
            				BigDecimal medicUnderDefLoss=new BigDecimal("0");
            				List<PrpLDlossPersTraceFeeVo> traceFeeVos=persTraceVo.getPrpLDlossPersTraceFees();
            				//PrpLDlossPersTraceFeeVo prpLDlossPersTraceFeeVo=new PrpLDlossPersTraceFeeVo();
            				if(traceFeeVos!=null && traceFeeVos.size()>0){
            					for(PrpLDlossPersTraceFeeVo feeVo:traceFeeVos){
            						medicUnderDefLoss.add(feeVo.getDefloss());
            					}
            				}
            				PrpLDlossPersInjuredVo injureVo=persTraceVo.getPrpLDlossPersInjured();
            				PrpLDlossPersHospitalVo prpLDlossPersHospitalVo=new PrpLDlossPersHospitalVo();
            				List<PrpLDlossPersHospitalVo> hospitalVos=injureVo.getPrpLDlossPersHospitals();
            				List<PrpLDlossPersExtVo> prpLDlossPersExtVos= injureVo.getPrpLDlossPersExts();
            				PrpLDlossPersExtVo prpLDlossPersExtVo=new PrpLDlossPersExtVo();
            				if(prpLDlossPersExtVos!=null && prpLDlossPersExtVos.size()>0){
            					prpLDlossPersExtVo=prpLDlossPersExtVos.get(0);
            				}
            				if(hospitalVos!=null && hospitalVos.size()>0){
            					prpLDlossPersHospitalVo=hospitalVos.get(0);
            				}
            				EvalPersonVo evalPersonVo=new EvalPersonVo();
                    		evalPersonVo.setEvalID(personVo.getId()+"");//定核损ID
                    		evalPersonVo.setPersonID(index+"");//伤亡人员编号
                    		evalPersonVo.setTaskStatus("");//任务状态	
                    		evalPersonVo.setLossType("1");//损失类型	
                    		if("1".equals(injureVo.getLossItemType())){
                    	      evalPersonVo.setLossDetailType("10");//损失类型细分	
                    		}else if("2".equals(injureVo.getLossItemType())){
                    		  evalPersonVo.setLossDetailType("13");//损失类型细分
                    		}else{
                    		  evalPersonVo.setLossDetailType("12");//损失类型细分
                    		}
                    		evalPersonVo.setPersonName(injureVo.getPersonName());//伤亡人员姓名
                    		evalPersonVo.setCertiCode(injureVo.getCertiCode());//伤亡人员证件号码
                    		evalPersonVo.setCertiType(injureVo.getCertiType());	//伤亡人员证件类型
                    		if("1".equals(injureVo.getLossItemType())){
                    			evalPersonVo.setPersonProperty("2");//人员属性
                    		}else{
                    			evalPersonVo.setPersonProperty("1");//人员属性
                    		}
                    		evalPersonVo.setMedicalType(injureVo.getTreatSituation());//伤亡人员医疗类型
                    		evalPersonVo.setAddmissionTime(DateFormatStringXs(prpLDlossPersHospitalVo.getInHospitalDate()));//入院时间		
                    		evalPersonVo.setInjuryType(injureVo.getWoundCode());//伤情类别		
                    		evalPersonVo.setInjuryLevel(prpLDlossPersExtVo.getWoundGrade());//伤残程度
                    		evalPersonVo.setMedicUnderDefLoss(medicUnderDefLoss+"");//医疗审核金额
                    		evalPersonVo.setMedicEstimateName(personVo.getOperatorName());//人伤跟踪人员姓名		
                    		evalPersonVo.setMedicEstimateCode(personVo.getOperatorCode());//人伤跟踪人员代码		
                    		evalPersonVo.setMedicUnderWriteCode(personVo.getUnderwriteCode());//医疗审核人员姓名
                    		evalPersonVo.setMedicUnderWriteName(personVo.getUnderwriteName());//医疗审核人员代码
                    		evalPersonVo.setEstimateStartTime(DateFormatStringXs(personVo.getCreateTime()));//人伤跟踪开始时间	
                    		evalPersonVo.setUnderEndTime(DateFormatStringXs(personVo.getUndwrtFeeEndDate()));//	医疗审核完成时间		
                    		evalPersonVo.setEstimateAddr("");//医疗审核地点
                    		if("1".equals(injureVo.getLossItemType())){
                    			evalPersonVo.setInjuredTypeCode("02");//伤者类型
                    		}else if("2".equals(injureVo.getLossItemType())){
                    			evalPersonVo.setInjuredTypeCode("04");
                    		}else{
                    			evalPersonVo.setInjuredTypeCode("03");
                    		}
                    		evalPersonVo.setInjuredGenderCode(injureVo.getPersonSex());//伤者性别
                    		evalPersonVo.setInjuredAge(injureVo.getPersonAge()+"");//伤者年龄
                    		evalPersonVo.setResidentProvince("");//户口所在省
                    		evalPersonVo.setResidentCity("");//户口所在市
                    		evalPersonVo.setResidentType(injureVo.getDomicile());//户口类型		
                    		evalPersonVo.setLocalWorkTime("");//本地工作时间		
                    		evalPersonVo.setTradeName(injureVo.getTicName());//所在行业			
                    		evalPersonVo.setInjuredWorkplace("");//伤者工作单位		
                    		evalPersonVo.setDutyName("");//职务
                    		evalPersonVo.setHasFixedIncome("");//有无固定收入
                    		if("01".equals(injureVo.getWoundCode())){
                    			evalPersonVo.setFatalityInd("0");//伤情判断
                    		}else if("03".equals(injureVo.getWoundCode())){
                    			evalPersonVo.setFatalityInd("2");//伤情判断
                    		}else{
                    			evalPersonVo.setFatalityInd("1");
                    		}
                    		evalPersonVo.setDiagnoseDesc("");//初步诊断	    
                    		evalPersonVo.setEquipmentUsedDesc("");//医疗措施		
                    		evalPersonVo.setInHospitalDate(DateFormatStringXs(prpLDlossPersHospitalVo.getInHospitalDate()));//就诊时间			        
                    		evalPersonVo.setHospitalName(prpLDlossPersHospitalVo.getHospitalName());//就诊医院	
                    		evalPersonVo.setBedNo("");//科室床位
                    		evalPersonVo.setPrincipalDoctorName("");//主治医生			
                    		evalPersonVo.setDoctorName("");//访谈医生		
                    		evalPersonVo.setDoctorTel("");//访谈医生联系电话
                    		evalPersonVo.setNeedParamedicInd("");//是否需要护理人员			
                    		evalPersonVo.setParamedicNumber("");//护理人员数
                    		evalPersonVo.setParamedicIdentity("");//护理人员身份		
                    		evalPersonVo.setDisabledInd("");//会否伤残	
                    		evalPersonVo.setFatalityTypeCode("");		      
                    		evalPersonVo.setFamilyCase("");//家庭情况	
                    		evalPersonVo.setFosterDesc("");//抚养义务 		 
                    		evalPersonVo.setOtherCase("");//其它情况			  
                    		evalPersonVo.setEstimateCode(personVo.getOperatorCode());//定损人员代码		 
                    		evalPersonVo.setUnderWriteCode(personVo.getUnderwriteCode());//核损人员代码		  
                    		evalPersonVo.setRemark("");//备注
                    		evalPersonVo.setCreateBy(personVo.getCreateUser());//创建者
                    		evalPersonVo.setCreateTime(DateFormatStringXs(personVo.getCreateTime()));//创建日期
                    		evalPersonVo.setUpdateBy(personVo.getUpdateUser());	//更新者		
                    		evalPersonVo.setUpdateTime(DateFormatStringXs(personVo.getUpdateTime()));//	更新日期
                    		//人员伤亡费用明细列表
                    		List<PersonLossFeeVo> personLossFees=new ArrayList<PersonLossFeeVo>();
                    		if(traceFeeVos!=null && traceFeeVos.size()>0){
                    			for(PrpLDlossPersTraceFeeVo feeVo:traceFeeVos){
                    				PersonLossFeeVo personLossFeeVo=new PersonLossFeeVo();
                    				personLossFeeVo.setEvalID(personVo.getId()+"");//定核损ID
                    				personLossFeeVo.setPersonID(injureVo.getId()+"");//伤亡人员编号
                    				personLossFeeVo.setFeeType(feeVo.getFeeTypeCode());//损失赔偿类型明细代码
                    				if(feeVo.getDefloss() != null){
                    					personLossFeeVo.setUnderDefLoss(feeVo.getDefloss()+"");//核损金额
                    				}else{
                    					personLossFeeVo.setUnderDefLoss("");//核损金额
                    				}
                    				personLossFeeVo.setExpenseSort("");//费用类别
                    				personLossFeeVo.setCurrencyCode("CNY");//币种	
                    				if(feeVo.getDefloss() != null){
                    					personLossFeeVo.setItemBillAmt(feeVo.getDefloss()+"");//单据金额
                    				}else{
                    					personLossFeeVo.setItemBillAmt("");//单据金额
                    				}
                    				personLossFeeVo.setRemark("");//备注
                    				personLossFeeVo.setCreateBy(feeVo.getCreateUser());//创建者
                    				personLossFeeVo.setCreateTime(DateFormatStringXs(feeVo.getCreateTime()));//创建日期
                    				personLossFeeVo.setUpdateBy(feeVo.getUpdateUser());//更新者
                    				personLossFeeVo.setUpdateTime(DateFormatStringXs(feeVo.getUpdateTime()));//更新日期
                    				personLossFees.add(personLossFeeVo);
                    			   }
                    		}
                    		evalPersonVo.setPersonLossFees(personLossFees);
                    		//受伤情况列表
                    		int serriso=0;
                    		List<EvalPersonInjuryVo> evalPersonInjurys=new ArrayList<EvalPersonInjuryVo>();
                    		if(prpLDlossPersExtVos!=null && prpLDlossPersExtVos.size()>0){
                    			for(PrpLDlossPersExtVo persExtVo:prpLDlossPersExtVos){
                    				serriso++;
                    				EvalPersonInjuryVo evalPersonInjuryVo=new EvalPersonInjuryVo();
                    				evalPersonInjuryVo.setEvalID(personVo.getId()+"");//定核损ID
                    				evalPersonInjuryVo.setPersonID(injureVo.getId()+"");//伤亡人员编号
                    				evalPersonInjuryVo.setSerialNo(serriso+"");//顺序号
                    				evalPersonInjuryVo.setInjuryPart(persExtVo.getInjuredPart());//受伤部位
                    				evalPersonInjuryVo.setCreateBy(persExtVo.getCreateUser());//创建者
                    				evalPersonInjuryVo.setCreateTime(DateFormatStringXs(persExtVo.getCreateTime()));//创建日期
                    				evalPersonInjuryVo.setUpdateBy(persExtVo.getUpdateUser());//更新者
                    				evalPersonInjuryVo.setUpdateTime(DateFormatStringXs(persExtVo.getUpdateTime()));//更新日期
                    				evalPersonInjurys.add(evalPersonInjuryVo);
                    			}
                    		}
                    		evalPersonVo.setEvalPersonInjurys(evalPersonInjurys);
                    		//医疗机构列表列表
                    		int hospitalSerivalNo=0;
                    		List<EvalPersonHospitalVo> evalPersonHospitals=new ArrayList<EvalPersonHospitalVo>();
                              if(hospitalVos!=null && hospitalVos.size()>0){
                            	  for(PrpLDlossPersHospitalVo hospitalVo:hospitalVos){
                            		  hospitalSerivalNo++;
                            		  EvalPersonHospitalVo evalPersonHospitalVo=new EvalPersonHospitalVo();
                            		  evalPersonHospitalVo.setEvalID(personVo.getId()+"");//定核损ID
                            		  evalPersonHospitalVo.setPersonID(injureVo.getId()+"");//伤亡人员编号
                            		  evalPersonHospitalVo.setSerialNo(hospitalSerivalNo+"");//顺序号
                            		  evalPersonHospitalVo.setHospitalName(hospitalVo.getHospitalName());//治疗机构名称
                            		  evalPersonHospitalVo.setCreateBy(hospitalVo.getCreateUser());//创建者
                            		  evalPersonHospitalVo.setCreateTime(DateFormatStringXs(hospitalVo.getCreateTime()));//创建日期
                            		  evalPersonHospitalVo.setUpdateBy(hospitalVo.getUpdateUser());//更新者
                            		  evalPersonHospitalVo.setUpdateTime(DateFormatStringXs(hospitalVo.getUpdateTime()));//更新日期
                            		  evalPersonHospitals.add(evalPersonHospitalVo);
                                   }
                              }
                              evalPersonVo.setEvalPersonHospitals(evalPersonHospitals);
                               //伤者诊断信息列表
                              List<EvalPersonDiagnosisVo> evalPersonDiagnosiss=new ArrayList<EvalPersonDiagnosisVo>();
                              if(prpLDlossPersExtVos!=null && prpLDlossPersExtVos.size()>0){
                            	  for(PrpLDlossPersExtVo persExtVo:prpLDlossPersExtVos){
                            		  EvalPersonDiagnosisVo evalPersonDiagnosisVo=new EvalPersonDiagnosisVo();
                            		  evalPersonDiagnosisVo.setEvalID(personVo.getId()+"");//定核损ID
                            		  evalPersonDiagnosisVo.setDiagnosisid(persExtVo.getId()+"");//伤情诊断ID
                            		  evalPersonDiagnosisVo.setPersonNo(injureVo.getId()+"");//人员序号
                            		  evalPersonDiagnosisVo.setDiagnosisName(persExtVo.getInjuredDiag());//诊断名称
                            		  evalPersonDiagnosisVo.setSupplementaryDiagnosis("");//补充诊断
                            		  evalPersonDiagnosisVo.setInjurySite(persExtVo.getInjuredPart());//损伤部位
                            		  evalPersonDiagnosisVo.setMeasure(persExtVo.getTreatWay());//治疗措施
                            		  evalPersonDiagnosisVo.setCreateBy(persExtVo.getCreateUser());//创建者
                            		  evalPersonDiagnosisVo.setCreateTime(DateFormatStringXs(persExtVo.getCreateTime()));//创建日期
                            		  evalPersonDiagnosisVo.setUpdateBy(persExtVo.getUpdateUser());//更新者
                            		  evalPersonDiagnosisVo.setUpdateTime(DateFormatStringXs(persExtVo.getUpdateTime()));//更新日期
                            		  evalPersonDiagnosiss.add(evalPersonDiagnosisVo);

                            	  }
                              }
                              evalPersonVo.setEvalPersonDiagnosiss(evalPersonDiagnosiss);
                              
                              evalPersons.add(evalPersonVo);
            			}
            		}
            	}
            }
            evalVo.setEvalPersons(evalPersons);
    		Evalslist.add(evalVo);
			//赔案主档信息列表
    		List<ClaimMainVo> claimMains=new ArrayList<ClaimMainVo>();
    		  if(prpLClaimVos!=null && prpLClaimVos.size()>0){
    			for(PrpLClaimVo prpLClaimVo:prpLClaimVos){
    				ClaimMainVo ClaimMainVo=new ClaimMainVo();
    				ClaimMainVo.setReportNo(prpLClaimVo.getRegistNo());//报案号
    				ClaimMainVo.setClaimNo(prpLClaimVo.getClaimNo());//赔案号
    				String ci="0";
    				if(logVos!=null && logVos.size()>0){
    					for(CiClaimPlatformLogVo vo:logVos){
    						if("1101".equals(prpLClaimVo.getRiskCode())){
    							if(vo.getRequestName().contains("交强")){
    								ci="1";
    								ClaimMainVo.setClaimSequenceNo(vo.getClaimSeqNo());//交强险平台赔案编号
    							}
    						}
    					}
    				}
    				if("0".equals(ci)){
    					ClaimMainVo.setClaimSequenceNo("");//交强险平台赔案编号
    				}
    				ClaimMainVo.setClaimTimes("");//赔付次数					
    				ClaimMainVo.setClaimStatus("3");//赔案主状态					
    				ClaimMainVo.setDepartmentCode(prpLClaimVo.getComCode());//机构编码				
    				ClaimMainVo.setPolicyNo(prpLClaimVo.getPolicyNo());//保单号			
    				ClaimMainVo.setEdrPrjNo("");//批改期次				
    				ClaimMainVo.setPlanCode(prpLClaimVo.getRiskCode());//险种编码			
    				ClaimMainVo.setLossTime(DateFormatStringXs(prpLClaimVo.getDamageTime()));//出险时间				
    				ClaimMainVo.setReportTime(DateFormatStringXs(prpLClaimVo.getReportTime()));//报案时间		
    				ClaimMainVo.setCatastropheLossInd(prplcheckVo.getMajorCaseFlag());//重大赔案标志				
    				ClaimMainVo.setDepositInd("");//垫付标					
    				ClaimMainVo.setRegisterInd("1");//立案标志					
    				ClaimMainVo.setDocumentCompletedInd("");//	资料齐全标志			
    				ClaimMainVo.setPrepayInd("");//预付标志					
    				ClaimMainVo.setGettingbackInd(prpLClaimVo.getIsSubRogation());//追偿标志					
    				ClaimMainVo.setLitigationInd("");//诉讼标志				
    				ClaimMainVo.setPaymentInd("");//赔付标志				
    				ClaimMainVo.setxEpaFlag("");//小额赔案标识					
    						
    				ClaimMainVo.setReportCurrencyCode("");//报案币种				
    				ClaimMainVo.setTotalDepositAmt("");//垫付总金额					
    				ClaimMainVo.setDepositCurrencyCode("CNY");//垫付币种		
    				if(prpLClaimVo.getSumDefLoss() != null){
    					ClaimMainVo.setRegisterAmt(prpLClaimVo.getSumDefLoss()+"");//立案金额
    					ClaimMainVo.setReportAmt(prpLClaimVo.getSumDefLoss()+"");//报案金额		
    				}else{
    					ClaimMainVo.setRegisterAmt("");//立案金额
    					ClaimMainVo.setReportAmt("");//报案金额		
    				}	
    				ClaimMainVo.setRegisterCurrencyCode("CNY");//立案币种					
    				ClaimMainVo.setTotalVerifiedAmt("");//总核损金额					
 				    ClaimMainVo.setVerifiedCurrencyCode("CNY");//总核损币种					
 				    ClaimMainVo.setTotalPrepayAmt("");//预付总金额					
 				    ClaimMainVo.setPrepayCurrencyCode("CNY");//	预付币种				
 				    ClaimMainVo.setTotalPaymentAmt("");//赔款总金额					
 				    ClaimMainVo.setPaymentCurrencyCode("CNY");//赔款币种					
 				    ClaimMainVo.setRegisterBy(prpLClaimVo.getCreateUser());//立案人					
 				    ClaimMainVo.setRegisterTime(DateFormatStringXs(prpLClaimVo.getClaimTime()));//立案时间					
    				ClaimMainVo.setEndcaseBy("");//结案人					
 				    ClaimMainVo.setEndcaseDepartmentCode("");//结案受理机构					
 				    ClaimMainVo.setEndcaseTime("");//结案日期				
                    ClaimMainVo.setEndcasePigeonholeNo("");//结案归档号				
                    ClaimMainVo.setEndcaseDesc("");//结案说明					
                    ClaimMainVo.setRevokecaseInd("");//撤案标志					
                    ClaimMainVo.setRevokecaseBy("");//撤案人					
                    ClaimMainVo.setRevokecaseTime("");//撤案时间	
                    ClaimMainVo.setRevokecaseDesc("");//撤案意见
                    ClaimMainVo.setHugeLossCode("0");//是否巨灾					
                    ClaimMainVo.setTotalVerifiedTime("");//总核损时间					
                    ClaimMainVo.setCollidedThirdInd(prplcheckVo.getIsClaimSelf());//互碰自赔标识					
                    ClaimMainVo.setActiontype("");//操作方式				
                    ClaimMainVo.setSubrogationFlag(prplcheckVo.getIsSubRogation());//代位求偿标识					
                    ClaimMainVo.setRemark("");//备注					
                    ClaimMainVo.setCreateBy(prpLClaimVo.getCreateUser());//创建者				
                    ClaimMainVo.setCreateTime(DateFormatStringXs(prpLClaimVo.getCreateTime()));//创建日期
                    ClaimMainVo.setUpdateby(prpLClaimVo.getUpdateUser());//更新者
                    ClaimMainVo.setUpdateTime(DateFormatStringXs(prpLClaimVo.getUpdateTime()));//更新日期					
 					
                    claimMains.add(ClaimMainVo);
    			}
    		}
    		  dataPacketVo.setRequestorVo(requestor);
    		  dataPacketVo.setEntitiesVo(entitiesVo);
    		  entitiesVo.setEvalVos(Evalslist);
    		  entitiesVo.setClaimMains(claimMains);
    		  dataPacketVo.setEntitiesVo(entitiesVo);
    		  dataPacketVo.setFraudRequests(fraudRequests);
    		  bodyReqVo.setDataPacketVo(dataPacketVo);
    		  soapenVo.setSoapenvHeader("");
    		  soapenVo.setBodyVo(bodyReqVo);
		return soapenVo;
	}
	
	/**
	 * 接口组装数据
	 * @param requestXML
	 * @param urlStr
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	private String requestPlatform(String requestXML,String urlStr) throws Exception {
		long t1 = System.currentTimeMillis();
		String responseXml="";
		  StringBuffer buffer = new StringBuffer();    
	        try {    
	        	//获取超时时间
				String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
				if(StringUtils.isBlank(seconds)){
					seconds = "20";
				}
	            URL url = new URL(urlStr);
	            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
	            httpUrlConn.setDoOutput(true);    
	            httpUrlConn.setDoInput(true);    
	            httpUrlConn.setUseCaches(false);    
	            // 设置请求方式（GET/POST）    
	            httpUrlConn.setRequestMethod("POST"); 
	            httpUrlConn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
	            httpUrlConn.setConnectTimeout(Integer.valueOf(seconds) * 1000);
		        
	            httpUrlConn.connect();    
	    
	            String outputStr =requestXML;
	            			
	            OutputStream outputStream = httpUrlConn.getOutputStream();  		        
	            // 当有数据需要提交时    
	            if (null != outputStr) {    
	                // 注意编码格式，防止中文乱码    outputStream.write
	                outputStream.write(outputStr.getBytes("utf-8"));    
	            }    
	    
	            // 将返回的输入流转换成字符串    
	            InputStream inputStream = httpUrlConn.getInputStream();    
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");    
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
	    
	            String str = null;    
	            while ((str = bufferedReader.readLine()) != null) {
	            	
	                buffer.append(str);    
	            } 
	            if (buffer.length() < 1) {
					throw new Exception("精励联讯返回数据失败");
				}
	            bufferedReader.close();    
	            inputStreamReader.close();    
	            // 释放资源  
	            outputStream.flush();
	            outputStream.close();
	            inputStream.close();    
	            inputStream = null;    
	            httpUrlConn.disconnect(); 
	            System.out.println(buffer);
	            responseXml=buffer.toString();
	            
	        } catch (ConnectException ce) {
	        	throw new Exception("与精励联讯连接失败，请稍候再试", ce);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	throw new Exception("读取精励联讯返回数据失败", e);
	        	
	        } finally {
				logger.warn("接口({})调用耗时{}ms", urlStr, System.currentTimeMillis() - t1);
			}    
	        return responseXml;
	}
	
	
	/**
	 * 时间转换方法
	 * Date 类型转换 String类型
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private String DateFormatStringXs(Date strDate){
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		return str;
	}
	
	/**
	 * 时间转换方法
	 * Date 类型转换 String类型
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private String DateFormatString(Date strDate){
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		return str;
	}
	
	
	 private void saveGenilexInterfaceLog(SysUserVo userVo,PrpLRegistVo registVo,SoapenVo reqVo,SoapEnvelopeVo resVo,String url,String taskId){     
	        ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
	        String requestXml = "";
	        String responseXml = "";
	        try{
	        	logVo.setBusinessType(BusinessType.GENILEX_Dloss.name());
	            logVo.setBusinessName(BusinessType.GENILEX_Dloss.getName());
	            logVo.setFlag(taskId);
	            XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
	            stream.autodetectAnnotations(true);
	            stream.setMode(XStream.NO_REFERENCES);
	            stream.aliasSystemAttribute(null,"class");// 去掉 class属性
	            requestXml = stream.toXML(reqVo);
	            responseXml = stream.toXML(resVo);
	            if("0041".equals(resVo.getBodyVo().getDataPacketVo().getProductResponseVo().getFraudResponseVo().getProductResultVo().getResultCode())){
	                logVo.setStatus("1");
	                logVo.setErrorCode("true");
	            }else {
	                logVo.setStatus("0");
	                logVo.setErrorCode(resVo.getBodyVo().getDataPacketVo().getProductResponseVo().getFraudResponseVo().getProductResultVo().getResultCode());
	                logVo.setErrorMessage(resVo.getBodyVo().getDataPacketVo().getProductResponseVo().getFraudResponseVo().getProductResultVo().getResultDesc());
	            }
	        }
	        catch(Exception e){
	            logVo.setStatus("0");
	            logVo.setErrorCode("false");
	            e.printStackTrace();
	        }
	        finally{
	            logVo.setRegistNo(registVo.getRegistNo());
	            logVo.setOperateNode(FlowNode.DLoss.name());
	            logVo.setComCode(userVo.getComCode());
	            Date date = new Date();
	            logVo.setRequestTime(date);
	            logVo.setRequestUrl(url);
	            logVo.setCreateUser(userVo.getUserCode());
	            logVo.setCreateTime(date);
	            logVo.setRequestXml(requestXml);
	            logVo.setResponseXml(responseXml);
	            interfaceLogService.save(logVo);
	        }
	       
	    }
	
}
