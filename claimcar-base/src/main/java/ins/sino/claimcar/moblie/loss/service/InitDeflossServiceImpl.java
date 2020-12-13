package ins.sino.claimcar.moblie.loss.service;

import ins.framework.lang.Springs;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants.RepairFlag;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.vo.DeflossActionVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.mobilecheck.service.LockService;
import ins.sino.claimcar.moblie.loss.vo.CertainsTaskInfoVo;
import ins.sino.claimcar.moblie.loss.vo.DefLossAssistInfoVo;
import ins.sino.claimcar.moblie.loss.vo.DefLossFeeInfoVo;
import ins.sino.claimcar.moblie.loss.vo.DefLossManhourInfoVo;
import ins.sino.claimcar.moblie.loss.vo.DefLossOutRepairInfoVo;
import ins.sino.claimcar.moblie.loss.vo.DefLossPartInfoVo;
import ins.sino.claimcar.moblie.loss.vo.InitDeflossReqBody;
import ins.sino.claimcar.moblie.loss.vo.InitDeflossRequest;
import ins.sino.claimcar.moblie.loss.vo.InitDeflossResBody;
import ins.sino.claimcar.moblie.loss.vo.InitDeflossRespond;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;

/**
 * 定损信息初始化
 * @author wurh
 */
public class InitDeflossServiceImpl implements ServiceInterface{
	
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	DeflossHandleService deflossHandleService;
	@Autowired
	WfFlowService wfFlowService;
	@Autowired
	LossChargeService lossChargeService;
	@Autowired
	ScheduleTaskService scheduleTaskService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	SysUserService sysUserService;
    @Autowired
    LockService lockService;
	@Autowired
	ClaimTextService claimTextService;
	private static Logger logger = LoggerFactory.getLogger(InitDeflossServiceImpl.class);
	
	@Override
	public Object service(String arg0, Object arg1) {
		init();
		
		InitDeflossRespond resPacket = new InitDeflossRespond();
		MobileCheckResponseHead resHead = new MobileCheckResponseHead();
		String lockFlags = "1";
		InitDeflossReqBody requestBody = new InitDeflossReqBody();
		try{
			String reqXml = ClaimBaseCoder.objToXmlUtf(arg1);
			logger.info("=============定损信息初始化接口请求报文："+reqXml);
			InitDeflossRequest reqPacket = (InitDeflossRequest)arg1;
			Assert.notNull(reqPacket," 请求信息为空  ");
			MobileCheckHead head = reqPacket.getHead();
			if (!"009".equals(head.getRequestType())|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			requestBody = reqPacket.getBody();
			
			resHead.setResponseType("009");
			resHead.setResponseCode("YES");
			resHead.setResponseMessage("Success");
			
			Double flowTaskId = Double.valueOf(requestBody.getTaskId());
			
			// 加锁开始
			String registNo = requestBody.getRegistNo();
			Long lockYwMainId = Long.valueOf(requestBody.getTaskId());
			lockService.savePrplLockList(lockYwMainId, FlowNode.DLoss.name());
			lockFlags = "0";// 加锁
			logger.info("加锁"+new Date());
			
			SysUserVo userVo = new SysUserVo();
			userVo = sysUserService.findByUserCode(requestBody.getNextHandlerCode());
			
			InitDeflossResBody resBody = new InitDeflossResBody();
			PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.valueOf(flowTaskId));
			if(taskVo!=null && taskVo.getTaskId()!=null){
				if((taskVo.getHandlerUser()==null&&requestBody.getNextHandlerCode().equals(taskVo.getAssignUser()))||
						requestBody.getNextHandlerCode().equals(taskVo.getHandlerUser())){
					// 如果定损任务未接收，先接收
					if("0".equals(taskVo.getWorkStatus())){
						deflossHandleService.acceptDefloss(flowTaskId,registNo,userVo);
					}
					DeflossActionVo deflossVo = new DeflossActionVo();
					deflossVo = deflossHandleService.prepareAddDefLoss(flowTaskId,userVo,"0");
					
					// 组织返回数据
					organizeResBody(flowTaskId,resBody, deflossVo);
					resPacket.setHead(resHead);
					resPacket.setBody(resBody);
				}else{
					resHead.setResponseType("009");
					resHead.setResponseCode("NO");
					resHead.setResponseMessage("该员工没有处理案子的权限！");
					resPacket.setHead(resHead);
				}
			}else{
				resHead.setResponseType("009");
				resHead.setResponseCode("NO");
				resHead.setResponseMessage("没有查询到该定损任务！");
				resPacket.setHead(resHead);
			}
		}catch (Exception e){
			
			if(e.getMessage().contains("ORA-00001: unique constraint")){
				resHead.setResponseType("009");
				resHead.setResponseCode("NO");
				resHead.setResponseMessage("定损初始化接口数据不能重复提交"+e.getMessage());
				resPacket.setHead(resHead);
				logger.info("定损初始化接口Insert或Update数据时违反了完整性：\n",e);
        	}else{
        		resHead.setResponseType("009");
    			resHead.setResponseCode("NO");
    			resHead.setResponseMessage(e.getMessage());
    			resPacket.setHead(resHead);
				logger.info("定损初始化接口报错信息： ",e);
    			e.printStackTrace();
        	}
		}finally{
			// 删除加锁
			if("0".equals(lockFlags)){
				lockService.deletePrplLockListById(Long.valueOf(requestBody.getTaskId()), FlowNode.DLoss.name());
				logger.info("删除加锁"+new Date());
			}
			
		}
		
		String resXml = ClaimBaseCoder.objToXmlUtf(resPacket);
		logger.info("=============定损信息初始化接口返回报文："+resXml);
		return resPacket;
	}
	
	public void organizeResBody(Double flowTaskId,InitDeflossResBody resBody,DeflossActionVo deflossVo){
		
		CertainsTaskInfoVo certainsTaskInfoVo = new CertainsTaskInfoVo();
		PrpLDlossCarMainVo lossCarMainVo = deflossVo.getLossCarMainVo();
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.valueOf(flowTaskId));
		PrpLDlossCarInfoVo carInfoVo = deflossVo.getCarInfoVo();
		
		// 组织定损主信息
		certainsTaskInfoVo.setRegistNo(taskVo.getRegistNo());// 报案号
		if(lossCarMainVo.getId()!=null){
			certainsTaskInfoVo.setCertainsId(lossCarMainVo.getId().toString());// 理赔定损ID
		}
		certainsTaskInfoVo.setTaskId(taskVo.getTaskId().toString());// 任务id
		certainsTaskInfoVo.setNodeType("DLCar");// 调度节点
		// 标的序号 是否标的
		if("1".equals(lossCarMainVo.getDeflossCarType())){
			certainsTaskInfoVo.setItemNo("1");
			certainsTaskInfoVo.setIsObject("1");
		}else{
			certainsTaskInfoVo.setItemNo("2");
			certainsTaskInfoVo.setIsObject("0");
		}
		certainsTaskInfoVo.setItemNoName(taskVo.getItemName());// 标的名称
		if(taskVo.getHandlerUser()==null || taskVo.getHandlerUser().isEmpty()){
			certainsTaskInfoVo.setNextHandlerCode(taskVo.getAssignUser());// 处理人代码
			String assign = CodeTranUtil.transCode("UserCode", taskVo.getAssignUser());
			if(assign.equals(taskVo.getAssignUser())){
				SysUserVo sysUserVo = scheduleTaskService.findPrpduserByUserCode(taskVo.getAssignUser(),"");
				if(sysUserVo!=null && sysUserVo.getUserName()!=null){
					assign = sysUserVo.getUserName();
				}
			}
			certainsTaskInfoVo.setNextHandlerName(assign);// 处理人名称
			certainsTaskInfoVo.setScheduleObjectId(taskVo.getAssignCom());// 处理人员归属机构编码
			certainsTaskInfoVo.setScheduleObjectName(CodeTranUtil.transCode("ComCodeFull",taskVo.getAssignCom()));// 处理人员归属机构名称
		}else{
			certainsTaskInfoVo.setNextHandlerCode(taskVo.getHandlerUser());// 处理人代码
			String assignUser = CodeTranUtil.transCode("UserCode", taskVo.getHandlerUser());
			if(assignUser.equals(taskVo.getHandlerUser())){
				SysUserVo sysUserVo = scheduleTaskService.findPrpduserByUserCode(taskVo.getHandlerUser(),"");
				if(sysUserVo!=null && sysUserVo.getUserName()!=null){
					assignUser = sysUserVo.getUserName();
				}
			}
			certainsTaskInfoVo.setNextHandlerName(assignUser);// 处理人名称
			certainsTaskInfoVo.setScheduleObjectId(taskVo.getHandlerCom());// 处理人员归属机构编码
			certainsTaskInfoVo.setScheduleObjectName(CodeTranUtil.transCode("ComCodeFull",taskVo.getHandlerCom()));// 处理人员归属机构名称
		}
		certainsTaskInfoVo.setLicenseNo(carInfoVo.getLicenseNo());// 号牌号码
		certainsTaskInfoVo.setLicenseType(carInfoVo.getLicenseType());// 号牌种类
		certainsTaskInfoVo.setVehBrandCode(carInfoVo.getBrandCode());// 定损品牌编码
		certainsTaskInfoVo.setBrandName(carInfoVo.getBrandName());// 厂牌型号
		certainsTaskInfoVo.setFrameNo(carInfoVo.getFrameNo());// 车架号
		certainsTaskInfoVo.setVin(carInfoVo.getVinNo());// VIN码
		certainsTaskInfoVo.setEngineNo(carInfoVo.getEngineNo());// 发动机号
		certainsTaskInfoVo.setVehicleModleCode(carInfoVo.getModelCode());// 车型编码
		certainsTaskInfoVo.setVehicleModleName(carInfoVo.getModelName());// 车型名称
		certainsTaskInfoVo.setCarKindCode(carInfoVo.getPlatformCarKindCode());// 车辆种类
		certainsTaskInfoVo.setOwern(carInfoVo.getCarOwner());// 车主
		// 初登日期
		if(carInfoVo.getEnrollDate()!=null){
			certainsTaskInfoVo.setEnrollDate(DateUtils.dateToStr(carInfoVo.getEnrollDate(), DateUtils.YToDay));
		}
		certainsTaskInfoVo.setAssessIdentifyNo(lossCarMainVo.getHandlerIdNo());// 定损员身份证号
		certainsTaskInfoVo.setAssessPlace(lossCarMainVo.getDefSite());// 定损地点
		certainsTaskInfoVo.setDriveName(carInfoVo.getDriveName());// 驾驶人姓名
		certainsTaskInfoVo.setDrivingLicenseNo(carInfoVo.getDrivingLicenseNo());// 驾照号码
		certainsTaskInfoVo.setIdentifyType(carInfoVo.getIdentifyType());// 驾驶人证件类型
		certainsTaskInfoVo.setIdentifyNo(carInfoVo.getIdentifyNo());// 驾驶人证件号码
		certainsTaskInfoVo.setStIpolicyNo(carInfoVo.getCiPolicyNo());// 交强险保单号
		certainsTaskInfoVo.setStInsurCom(carInfoVo.getCiInsureComCode());// 交强承保机构
		certainsTaskInfoVo.setIndemnityDuty(lossCarMainVo.getIndemnityDuty());// 事故责任
		// 事故责任比例
		if(lossCarMainVo.getIndemnityDutyRate() != null){
			certainsTaskInfoVo.setIndemnityDutyRate(lossCarMainVo.getIndemnityDutyRate().toString());
		}
		certainsTaskInfoVo.setCiDutyFlag(lossCarMainVo.getCiDutyFlag());// 交强责任
		certainsTaskInfoVo.setLossOpinion("");// 定损意见
		certainsTaskInfoVo.setExigenceGree(lossCarMainVo.getMercyFlag());// 案件紧急程度
		certainsTaskInfoVo.setExcessType(lossCarMainVo.getIsClaimSelf());// 是否互碰自赔
		certainsTaskInfoVo.setLossFeeType(lossCarMainVo.getLossFeeType());// 损失类别
		certainsTaskInfoVo.setVehGroupCode(carInfoVo.getGroupCode());// 车组编码
		certainsTaskInfoVo.setGroupName(carInfoVo.getGroupName());// 车组名称
		certainsTaskInfoVo.setSelfConfigFlag(carInfoVo.getSelfConfigFlag());// 自定义车型标志
		certainsTaskInfoVo.setSalvageFee(valueOf(lossCarMainVo.getSumRescueFee()));// 定损施救费用
		certainsTaskInfoVo.setRemnantFee(valueOf(lossCarMainVo.getSumRemnant()));// 定损折扣残值
		certainsTaskInfoVo.setManageFee(valueOf(lossCarMainVo.getSumManageFee()));// 定损管理费合计
		certainsTaskInfoVo.setEvalPartSum(valueOf(lossCarMainVo.getSumCompFee()));// 定损换件合计
		certainsTaskInfoVo.setEvalRePairSum(valueOf(lossCarMainVo.getSumRepairFee()));// 定损工时合计
		certainsTaskInfoVo.setEvalMateSum(valueOf(lossCarMainVo.getSumManageFee()));// 定损辅料合计
		certainsTaskInfoVo.setSelfPaySum(valueOf(lossCarMainVo.getSumSelfPayFee()));// 自付合计
		certainsTaskInfoVo.setOuterSum(valueOf(lossCarMainVo.getSumOutFee()));// 定损外修合计
		certainsTaskInfoVo.setDerogationSum(valueOf(lossCarMainVo.getSumDerogationFee()));// 减损合计
		certainsTaskInfoVo.setSumLossAmount(valueOf(lossCarMainVo.getSumLossFee()));// 定损合计
		certainsTaskInfoVo.setHandlerCode(lossCarMainVo.getHandlerCode());// 定损员代码
		certainsTaskInfoVo.setRemark(lossCarMainVo.getRemark());// 备注
		certainsTaskInfoVo.setPriceType(lossCarMainVo.getRepairType());// 价格类型
		certainsTaskInfoVo.setRepairFacId("");// 修理厂ID
		certainsTaskInfoVo.setRepairFacCode(lossCarMainVo.getRepairFactoryType());// 修理厂编码
		certainsTaskInfoVo.setRepairFacName(lossCarMainVo.getRepairFactoryName());// 修理厂名称
		certainsTaskInfoVo.setRepairFacType(lossCarMainVo.getRepairFactoryType());// 修理厂类型
		certainsTaskInfoVo.setRepairPhone(lossCarMainVo.getFactoryMobile());// 修理厂手机
		certainsTaskInfoVo.setMixCode("");// 修理厂组织机构代码
		certainsTaskInfoVo.setPartDiscountPercent("");// 换件折扣
		certainsTaskInfoVo.setRepairDiscountPercent("");// 工时折扣
		certainsTaskInfoVo.setSalvageFeeRemark("");// 施救费备注
		certainsTaskInfoVo.setSelfEstiFlag("0");// 自核价标记 是否自核价 1=是 0=否 传0
		certainsTaskInfoVo.setEvalTypeCode("01");// 定损方式 默认修复定损
		certainsTaskInfoVo.setInsuranceCode("");// 险别代码
		certainsTaskInfoVo.setInsuranceName("");// 险别名称
		certainsTaskInfoVo.setVirtualValue(valueOf(lossCarMainVo.getActualValue()));// 总施救财产实际价值
		certainsTaskInfoVo.setVerSumFitsFee(valueOf(lossCarMainVo.getSumVeriCompFee()));// 核损换金额合计
		certainsTaskInfoVo.setVerAccessorFee(valueOf(lossCarMainVo.getSumVeriMatFee()));// 核损辅料金额合计
		certainsTaskInfoVo.setVerSumRepairFee(valueOf(lossCarMainVo.getSumVeriRepairFee()));// 核损维修金额（工时费）合计
		certainsTaskInfoVo.setVerSumSalvageFee(valueOf(lossCarMainVo.getSumVeriRemnant()));// 核损残值合计
		certainsTaskInfoVo.setSumVeriAmount(valueOf(lossCarMainVo.getSumVeriLossFee()));// 核损金额合计
		certainsTaskInfoVo.setSumFeeAmount(valueOf(lossCarMainVo.getSumChargeFee()));// 费用合计
		certainsTaskInfoVo.setVehKindCode(carInfoVo.getVehKindCode());// 定损车辆种类代码
		certainsTaskInfoVo.setVehKindName(carInfoVo.getVehKindName());// 定损车辆种类名称
		certainsTaskInfoVo.setVehCertainCode(carInfoVo.getModelCode());// 定损车型编码
		certainsTaskInfoVo.setVehCertainName(carInfoVo.getModelName());// 定损车系编码
		certainsTaskInfoVo.setVehSeriCode(carInfoVo.getSeriCode());// 定损车系名称
		certainsTaskInfoVo.setVehSeriName(carInfoVo.getSeriName());// 定损车系名称
		certainsTaskInfoVo.setVehYearType(carInfoVo.getYearType());// 年款
		certainsTaskInfoVo.setVehFactoryCode(carInfoVo.getFactoryCode());// 厂家编码
		certainsTaskInfoVo.setVehFactoryName(carInfoVo.getFactoryName());// 厂家名称
		certainsTaskInfoVo.setFroceExclusions(lossCarMainVo.getIsCInotpayFlag());// 是否交强拒赔
		certainsTaskInfoVo.setBusinessExclusions(lossCarMainVo.getIsBInotpayFlag());// 是否商业拒赔
		certainsTaskInfoVo.setExclusionsReason(lossCarMainVo.getNotpayCause());// 拒赔原因
		certainsTaskInfoVo.setDocumentsComplete(lossCarMainVo.getDirectFlag());// 是否单证齐全
		certainsTaskInfoVo.setIsspecialOperation(lossCarMainVo.getIsSpecialcarFlag());// 是否有特种车资格证
		certainsTaskInfoVo.setIsbusinessQualification(lossCarMainVo.getIsBusinesscarFlag());// 是否营业车资格证
		certainsTaskInfoVo.setIsnoResponsibility(lossCarMainVo.getIsNodutypayFlag());// 是否无责代赔
		if("5".equals(lossCarMainVo.getNotpayCause())){
			certainsTaskInfoVo.setOtherexcReason(lossCarMainVo.getOtherNotpayCause());// 拒赔其它原因
		}
		//不管是不是标的车提交，还是是否同步，有就传，没有默认否
		certainsTaskInfoVo.setIsGlassBroken(StringUtils.isNotBlank(lossCarMainVo.getIsGlassBroken())?lossCarMainVo.getIsGlassBroken():"0");   //是否玻璃单独破碎,空默认否
		certainsTaskInfoVo.setIsNotFindThird(StringUtils.isNotBlank(lossCarMainVo.getIsNotFindThird())?lossCarMainVo.getIsNotFindThird():"0");  //是否属于无法找到第三方,空默认否

		//如果该定损节点是核损退回的，则需要传核损退回原因
		PrpLWfTaskVo oldTaskVo = wfTaskHandleService.queryTask(taskVo.getUpperTaskId().doubleValue());
		if(oldTaskVo != null) {
			if( FlowNode.VLoss.name().equals(oldTaskVo.getNodeCode()) && oldTaskVo.getSubNodeCode().contains("VLCar")) {
				Long perstraceMainId = Long.valueOf(oldTaskVo.getHandlerIdKey());
				String registNo = oldTaskVo.getRegistNo();
				List<PrpLClaimTextVo> claimTextList = claimTextService.findClaimTextByregistNoAndbussTaskId(registNo, perstraceMainId);
				
				if(claimTextList != null && claimTextList.size() > 0) {
					for(PrpLClaimTextVo vo : claimTextList) {
						if(StringUtils.isNotBlank(vo.getNodeCode()) && vo.getNodeCode().contains("VLCar")) {
							certainsTaskInfoVo.setRemark2(vo.getDescription());
							break;
						}
					}
				}
			}
		}
		
		// 换件信息
		List<DefLossPartInfoVo> defLossPartInfoVos = new ArrayList<DefLossPartInfoVo>();
		for(PrpLDlossCarCompVo compVo:lossCarMainVo.getPrpLDlossCarComps()){
			DefLossPartInfoVo partInfoVo = new DefLossPartInfoVo();
			partInfoVo.setPartId(compVo.getIndId());// 定损明细主键
			partInfoVo.setPartCode(compVo.getCompCode());// 项目代码
			partInfoVo.setItemName(compVo.getCompName());// 项目名称
			partInfoVo.setSysGuidePrice(valueOf(compVo.getSys4SPrice()));// 系统4S店价
			partInfoVo.setSysMarketPrice(valueOf(compVo.getSysMarketPrice()));// 系统区域市场价
			partInfoVo.setLocalGuidePrice(valueOf(compVo.getRepairFactoryFee()));// 本地区域原厂价
			partInfoVo.setLocalMarketPrice(valueOf(compVo.getNativeMarketPrice()));// 本地区域市场价
			partInfoVo.setLocalApplicablePrice("");// 本地区域适用价
			partInfoVo.setLocalPrice(valueOf(compVo.getMaterialFee()));// 本地价
			partInfoVo.setCount(valueOf(compVo.getQuantity()));// 数量
			partInfoVo.setMaterialFee(valueOf(compVo.getMaterialFee()));// 定损材料费（定损单价）
			partInfoVo.setSelfConfigFlag(compVo.getSelfConfigFlag());// 自定义配件标记
			partInfoVo.setItemCoverCode(compVo.getKindCode());// 险种代码
			partInfoVo.setRemark(compVo.getRemark());// 备注
			partInfoVo.setChgCompSetCode(compVo.getPriceType());// 参考价格类型编码
			partInfoVo.setFitBackFlag(compVo.getRecycleFlag());// 回收标志
			partInfoVo.setRemainsPrice(valueOf(compVo.getRestFee()));// 残值
			partInfoVo.setDetectedFlag("");// 待检测标志
			partInfoVo.setDirectSupplyFlag("");// 直供测标志
			partInfoVo.setDirectSupplier("");// 直供商
			partInfoVo.setManageSingleRate(valueOf(compVo.getManageFeeRate()));// 管理费率
			partInfoVo.setManageSingleFee(valueOf(compVo.getManageSingleFee()));// 管理费
			partInfoVo.setEvalPartSum(valueOf(compVo.getSumDefLoss()));// 换件合计
			partInfoVo.setRecyclePartFlag(compVo.getRecyclePartFlag());// 回收方式
			partInfoVo.setSelfPayRate(valueOf(compVo.getSelfPayRate()));// 自付比例
			partInfoVo.setLossFee2(valueOf(compVo.getSumVeriLoss()));// 核损价格
			partInfoVo.setRemark2(compVo.getVeriRemark());// 核损备注
			defLossPartInfoVos.add(partInfoVo);
		}
		certainsTaskInfoVo.setDefLossPartInfoVos(defLossPartInfoVos);
		
		// 修理信息
		List<DefLossManhourInfoVo> defLossManhourInfoVos = new ArrayList<DefLossManhourInfoVo>();// 内修
		List<DefLossOutRepairInfoVo> defLossOutRepairInfoVos = new ArrayList<DefLossOutRepairInfoVo>();// 外修
		for(PrpLDlossCarRepairVo repairVo:lossCarMainVo.getPrpLDlossCarRepairs()){
			if(RepairFlag.INNERREPAIR.equals(repairVo.getRepairFlag())){
				DefLossManhourInfoVo manhourInfo = new DefLossManhourInfoVo();
				manhourInfo.setRepairId(repairVo.getRepairId());//
				manhourInfo.setRepairModeCode(repairVo.getCompCode());// 修理方式代码
				manhourInfo.setItemName(repairVo.getCompName());// 项目名称
				manhourInfo.setManpowerFee(valueOf(repairVo.getSumDefLoss()));// 定损人工费（工时定损费用）
				manhourInfo.setManpowerRefFee("");// 工时参考价格
				manhourInfo.setSelfConfigFlag(repairVo.getSelfConfigFlag());// 自定义修理标记
				manhourInfo.setItemCoverCode(repairVo.getKindCode());// 险种代码
				manhourInfo.setRemark(repairVo.getRemark());// 备注
				manhourInfo.setEvalHour(valueOf(repairVo.getManHour()));// 工时数
				manhourInfo.setRepairUnitPrice(valueOf(repairVo.getManHourUnitPrice()));// 工时单价
				manhourInfo.setRepairLevelCode(valueOf(repairVo.getLevelCode()));// 损失程度代码
				manhourInfo.setRepairLevelName(repairVo.getLevelName());// 损失程度名称
				manhourInfo.setLossFee2(valueOf(repairVo.getSumVeriLoss()));// 核损修理费用
				manhourInfo.setRemark2(repairVo.getVeriRemark());// 核损备注
				defLossManhourInfoVos.add(manhourInfo);
			}else {
				DefLossOutRepairInfoVo outRepairInfo = new DefLossOutRepairInfoVo();
				outRepairInfo.setOuterId(repairVo.getRepairId());
				outRepairInfo.setOuterName(repairVo.getCompName());// 外修项目名称
				outRepairInfo.setReferencePrice("");// 参考工时费
				outRepairInfo.setRepairHandaddFlag(repairVo.getSelfConfigFlag());// 自定义标记
				outRepairInfo.setEvalOuterPirce(valueOf(repairVo.getManHourFee()));// 外修项目单价
				outRepairInfo.setDerogationPrice(valueOf(repairVo.getDerogationPrice()));// 外修项目减损金额
				outRepairInfo.setDerogationItemName(repairVo.getRepairCode());// 配件项目名称
				outRepairInfo.setDerogationItemCode(repairVo.getRepairName());// 配件零件号
				outRepairInfo.setDerogationPriceType("");// 配件价格类型
				outRepairInfo.setPartPrice(valueOf(repairVo.getMaterialFee()));// 配件金额
				outRepairInfo.setRepairFactoryId(repairVo.getRepairFactoryCode());// 外修修理厂ID
				outRepairInfo.setRepairFactoryName(repairVo.getRepairFactoryName());// 外修修理厂名称
				outRepairInfo.setRepairFactoryCode(repairVo.getRepairFactoryCode());// 外修修理厂代码
				outRepairInfo.setItemCoverCode(repairVo.getKindCode());// 险种代码
				outRepairInfo.setRemark(repairVo.getRemark());// 备注
				outRepairInfo.setPartAmount(repairVo.getMaterialQuantity() == null ? "" : repairVo.getMaterialQuantity().toString());// 外修配件数量
				outRepairInfo.setOutItemAmount(valueOf(repairVo.getManHour()));// 外修数量
				outRepairInfo.setRepairOuterSum(valueOf(repairVo.getSumDefLoss()));// 外修费用小计金额
				outRepairInfo.setReferencePartPrice("");// 外修配件参考价
			}
		}
		certainsTaskInfoVo.setDefLossManhourInfoVos(defLossManhourInfoVos);
		certainsTaskInfoVo.setDefLossOutRepairInfoVos(defLossOutRepairInfoVos);
		
		// 定损辅料
		List<DefLossAssistInfoVo> defLossAssistInfoVos = new ArrayList<DefLossAssistInfoVo>();
		if(lossCarMainVo.getPrpLDlossCarMaterials() != null){
			for(PrpLDlossCarMaterialVo materialVo:lossCarMainVo.getPrpLDlossCarMaterials()){
				DefLossAssistInfoVo assistVo = new DefLossAssistInfoVo();
				assistVo.setAssistId(materialVo.getAssistId());// 辅料明细主键
				assistVo.setItemName(materialVo.getMaterialName());// 辅料名称
				assistVo.setCount(valueOf(materialVo.getAssisCount()));// 数量
				assistVo.setMaterialFee(valueOf(materialVo.getUnitPrice()));// 定损材料费
				assistVo.setEvalMateSum(valueOf(materialVo.getMaterialFee()));// 辅料合计
				assistVo.setSelfConfigFlag(materialVo.getSelfConfigFlag());// 自定义辅料标记
				assistVo.setItemCoverCode(materialVo.getKindCode());// 险种代码
				assistVo.setRemark(materialVo.getRemark());// 备注
				defLossAssistInfoVos.add(assistVo);
			}
			certainsTaskInfoVo.setDefLossAssistInfoVo(defLossAssistInfoVos);
		}
		
		// 定损费用
		List<DefLossFeeInfoVo> defLossfeeInfoVos = new ArrayList<DefLossFeeInfoVo>();
		List<PrpLDlossChargeVo> lossChargeVoList = lossChargeService.findLossChargeVos(lossCarMainVo.getId(), FlowNode.DLCar.name());
		if(lossChargeVoList!=null && lossChargeVoList.size()>0){
			for(PrpLDlossChargeVo chargeVo:lossChargeVoList){
				DefLossFeeInfoVo lossFeeInfo = new DefLossFeeInfoVo();
				lossFeeInfo.setSerialNo(valueOf(chargeVo.getSerialNo()));
				lossFeeInfo.setKindCode(chargeVo.getKindCode());
				lossFeeInfo.setFeeType(chargeVo.getChargeCode());
				lossFeeInfo.setFeeAmount(chargeVo.getChargeFee().doubleValue());
				lossFeeInfo.setPayee(chargeVo.getReceiver());		
				defLossfeeInfoVos.add(lossFeeInfo);
			}
			certainsTaskInfoVo.setDefLossfeeInfoVos(defLossfeeInfoVos);
		}
		
		resBody.setCertainsTaskInfoVo(certainsTaskInfoVo);
	}
	
	private void init(){
		if(wfTaskHandleService==null){
			wfTaskHandleService = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
		}
		if(deflossHandleService==null){
			deflossHandleService = (DeflossHandleService)Springs.getBean(DeflossHandleService.class);
		}
		if(wfFlowService==null){
			wfFlowService = (WfFlowService)Springs.getBean(WfFlowService.class);
		}
		if(lossChargeService==null){
			lossChargeService = (LossChargeService)Springs.getBean(LossChargeService.class);
		}
		if(scheduleTaskService==null){
			scheduleTaskService = (ScheduleTaskService)Springs.getBean(ScheduleTaskService.class);
		}
		if(policyViewService==null){
			policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
		}
		if(sysUserService==null){
			sysUserService = (SysUserService)Springs.getBean(SysUserService.class);
		}
		if(lockService==null){
			lockService = (LockService)Springs.getBean(LockService.class);
		}
		if(claimTextService == null) {
			claimTextService = (ClaimTextService)Springs.getBean(ClaimTextService.class);
		}
	}
	
    public String valueOf(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }
}
