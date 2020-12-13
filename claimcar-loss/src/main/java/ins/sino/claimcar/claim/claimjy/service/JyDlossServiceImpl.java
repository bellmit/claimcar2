package ins.sino.claimcar.claim.claimjy.service;

import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.CarType;
import ins.sino.claimcar.CodeConstants.KINDCODE;
import ins.sino.claimcar.CodeConstants.LossParty;
import ins.sino.claimcar.carplatform.util.CodeConvertTool;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.vo.PrpLDisasterVo;
import ins.sino.claimcar.claimjy.service.JyDlossService;
import ins.sino.claimcar.claimjy.util.JyHttpUtil;
import ins.sino.claimcar.claimjy.vo.DlossAskEvalLossInfo;
import ins.sino.claimcar.claimjy.vo.DlossAskReqBodyVo;
import ins.sino.claimcar.claimjy.vo.DlossAskReqInfo;
import ins.sino.claimcar.claimjy.vo.DlossAskReqVo;
import ins.sino.claimcar.claimjy.vo.ItemCover;
import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;
import ins.sino.claimcar.claimjy.vo.JyResVo;
import ins.sino.claimcar.claimjy.vo.LossCoverVehicleVo;
import ins.sino.claimcar.claimjy.vo.LossInsuredItemVo;
import ins.sino.claimcar.claimjy.vo.LossPolicyItemVo;
import ins.sino.claimcar.claimjy.vo.LossReportingVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.losscar.service.DeflossService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.ClaimFittingVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLJingYouLogVo;
import ins.sino.claimcar.losscar.vo.SendVo;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPersonLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "jyDlossService")
public class JyDlossServiceImpl implements JyDlossService {	
	private static Logger LOGGER = LoggerFactory.getLogger(JyDlossServiceImpl.class);
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private RegistService registService;
	@Autowired
	private PrpLCMainService prpLCMainService;
	@Autowired
	DeflossService deflossService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	CheckHandleService checkHandleService;

	@Override
	public String dlossAskService(ClaimFittingVo claimFittingVo, SysUserVo sysUserVo) throws Exception {
		
		DlossAskReqVo reqVo = new DlossAskReqVo();
		JyResVo resVo = new JyResVo();
		String url = "";
		try {
			this.initRealUrl(claimFittingVo);
			reqVo = this.orgnaizeInfo(claimFittingVo,sysUserVo);
			String xmlToSend = "";
			String xmlReturn = "";
			xmlToSend = ClaimBaseCoder.objToXmlUtf(reqVo);
			url = SpringProperties.getProperty("JY2_DLOSS");
			LOGGER.info("定损请求接口提交send---------------------------"+xmlToSend);
			claimFittingVo.setRequestXml(xmlToSend);
			xmlReturn = JyHttpUtil.sendData(xmlToSend, url, 200);
			claimFittingVo.setResponseXml(xmlReturn);
			LOGGER.info("定损请求接口接口提交return---------------------------"+xmlReturn);
			resVo = ClaimBaseCoder.xmlToObj(xmlReturn, JyResVo.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("定损请求接口异常",e);
			throw e;
		}
		return resVo.getBody().getUrl();
	}
	
	private DlossAskReqVo orgnaizeInfo(ClaimFittingVo claimFittingVo, SysUserVo sysUserVo){
		String dmgVhclId = claimFittingVo.getLossCarId();
		String userCode = sysUserVo.getUserCode();
		String userName = sysUserVo.getUserName();
		String comCode = sysUserVo.getComCode();
		
		DlossAskReqVo dlossAskReqVo = new DlossAskReqVo();
		JyReqHeadVo jyReqHeadVo = new JyReqHeadVo();
		DlossAskReqBodyVo dlossAskReqBodyVo = new DlossAskReqBodyVo();
		PrpLDlossCarMainVo lossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(dmgVhclId));
		String registNo = lossCarMainVo.getRegistNo();
		PrpLDlossCarInfoVo lossCarInfoVo = lossCarService.findPrpLDlossCarInfoVoById(lossCarMainVo.getCarId());
		List<PrpLCMainVo> prpLCMainVos = prpLCMainService.findPrpLCMainsByRegistNo(registNo);

		jyReqHeadVo.setUserCode("jy");
		jyReqHeadVo.setPassWord("jy");
		jyReqHeadVo.setRequestSourceCode("DHIC");
		jyReqHeadVo.setRequestSourceName("鼎和财产保险股份有限公司");
		jyReqHeadVo.setRequestType("001");
		jyReqHeadVo.setOperatingTime(DateUtils.dateToStr(DateUtils.now(), DateUtils.YToSec));
		
		DlossAskReqInfo dlossAskReqInfo = new DlossAskReqInfo();
		dlossAskReqInfo.setIfNewLossFlag("1");// 是否包含保单、报案信息
		// 核损标志
		if(CodeConstants.VeriFlag.BACK.equals(lossCarMainVo.getUnderWriteFlag())
			|| FlowNode.DLCarMod.name().equals(claimFittingVo.getNodeCode())){
			dlossAskReqInfo.setAuditLossFlag("1");// 0-正常定损 1-核损退回进定损 核价传-0
		}
		dlossAskReqInfo.setReturnURL(claimFittingVo.getReturnURL());
		dlossAskReqInfo.setRefreshURL(claimFittingVo.getRefreshURL());
		dlossAskReqBodyVo.setDlossAskReqInfo(dlossAskReqInfo);
		
		DlossAskEvalLossInfo dlossAskEvalLossInfo = new DlossAskEvalLossInfo();
		
		dlossAskEvalLossInfo.setDmgVhclId(dmgVhclId);
		dlossAskEvalLossInfo.setLossNo(dmgVhclId);// 业务表ID，追加取追加的ID
		dlossAskEvalLossInfo.setReportCode(registNo);
		dlossAskEvalLossInfo.setPlateNo(lossCarMainVo.getLicenseNo());// 车牌号码
		dlossAskEvalLossInfo.setEngineNo(lossCarInfoVo.getEngineNo());// 发动机号
		dlossAskEvalLossInfo.setVinNo(lossCarInfoVo.getVinNo());// VIN码
		if("1".equals(lossCarMainVo.getDeflossCarType())){
			dlossAskEvalLossInfo.setMarkColor(prpLCMainVos.get(0).getPrpCItemCars().get(0).getLicenseColorCode());// 牌照颜色
			dlossAskEvalLossInfo.setInsureVehicleCode(lossCarInfoVo.getModelCode());// 承保的车型编码
			dlossAskEvalLossInfo.setInsureVehicle(lossCarInfoVo.getModelName());// 承保车型名称
			dlossAskEvalLossInfo.setIsSubjectVehicle("1");
	        if(prpLCMainVos.get(0).getPrpCItemCars().get(0).getEnrollDate() != null){
				dlossAskEvalLossInfo.setEnrolDate(DateUtils.dateToStr(prpLCMainVos.get(0).getPrpCItemCars().get(0).getEnrollDate(),DateUtils.YToDay)); // 初登日期
	        }
		}
		String branchComCode = "";
		if(comCode.startsWith("00")){
			branchComCode = comCode.substring(0, 4)+"0000";
		}else{
			branchComCode = comCode.substring(0, 2)+"000000";
		}
		dlossAskEvalLossInfo.setComCode(branchComCode);// 定损员所属分公司代码
		dlossAskEvalLossInfo.setCompany(CodeTranUtil.transCode("ComCodeFull",branchComCode));// 定损员所属分公司名称
		dlossAskEvalLossInfo.setBranchComCode(comCode);// 定损员所属中支代码
		dlossAskEvalLossInfo.setBranchComName(CodeTranUtil.transCode("ComCodeFull",comCode));// 定损员所属中支名称
		dlossAskEvalLossInfo.setEvalHandlerCode(userCode);// 定损员代码
		dlossAskEvalLossInfo.setEvalHandlerName(userName);// 定损员名称
//		dlossAskEvalLossInfo.setDriverName(lossCarInfoVo.getDriveName());// 驾驶员
		dlossAskEvalLossInfo.setDriverName("");
		dlossAskEvalLossInfo.setEvalRemark(lossCarMainVo.getRemark());// 定损单备注
		//添加车辆种类
		String platformCarKind = null;
		// 如果是标的车，直接取承保的车辆种类
		if(LossParty.TARGET.equals(lossCarMainVo.getDeflossCarType())) {
			PrpLCItemCarVo ciItemCarVo = registQueryService.findCItemCarByRegistNo(lossCarMainVo.getRegistNo());
			//如果是特种车或者是挂车，则要取承保的车辆种类进行字典转译
			if(CarType.trailer.equals(ciItemCarVo.getCarType())|| CarType.specialVehicle.equals(ciItemCarVo.getCarType())) { 
				platformCarKind = CodeConvertTool
						.getVehicleCategory(ciItemCarVo.getCarType(),ciItemCarVo.getExhaustScale(),ciItemCarVo.getTonCount());
				if(StringUtils.isBlank(platformCarKind)) {
					platformCarKind = CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1();
				}
			} else {
				platformCarKind = CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1();// 车辆种类
			}
		} else {// 如果是三者车，取定损的车辆种类
			platformCarKind = lossCarInfoVo.getPlatformCarKindCode();
		}
		dlossAskEvalLossInfo.setCarKindCode(platformCarKind);// 车辆种类
		
		dlossAskReqBodyVo.setDlossAskEvalLossInfo(dlossAskEvalLossInfo);
		
		
		
		List<ItemCover> itemCovers = new ArrayList<ItemCover>();
		
		if(prpLCMainVos!=null&&prpLCMainVos.size()>0){
			for(PrpLCMainVo cmain:prpLCMainVos){
				List<PrpLCItemKindVo> prpLCItemKindVos = cmain.getPrpCItemKinds();
				if(prpLCItemKindVos!=null&&prpLCItemKindVos.size()>0){
					for (PrpLCItemKindVo prpLCItemKindVo : prpLCItemKindVos) {
						ItemCover itemCover = new ItemCover();
						itemCover.setItemCover(prpLCItemKindVo.getKindName());
						itemCover.setItemCoverCode(prpLCItemKindVo.getKindCode());
						itemCovers.add(itemCover);
					}
				}
			}
		}
		
		dlossAskReqBodyVo.setItemCovers(itemCovers);
		
		this.organizePolicyInfo(prpLCMainVos,dlossAskReqBodyVo);// 组织保单信息
		this.organizeLossCoverVehicle(prpLCMainVos.get(0),dlossAskReqBodyVo);// 组织承保车辆信息
		this.organizeLossInsured(prpLCMainVos,dlossAskReqBodyVo);// 组织承保险别信息
		
		PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);;
		PrpLDisasterVo disasterVo = checkHandleService.findDisasterVoByRegistNo(registNo);
		this.organizeLossReporting(registVo,disasterVo,prpLCMainVos,dlossAskReqBodyVo);// 组织报案信息
		
		dlossAskReqVo.setJyReqHeadVo(jyReqHeadVo);
		dlossAskReqVo.setDlossAskReqBodyVo(dlossAskReqBodyVo);
		return dlossAskReqVo;
	}

	// 组织保单信息
	private void organizePolicyInfo(List<PrpLCMainVo> prpLCMainVos,DlossAskReqBodyVo dlossAskReqBodyVo) {
		List<LossPolicyItemVo> lossPolicy = new ArrayList<LossPolicyItemVo>();
		for (PrpLCMainVo prpLCMainVo : prpLCMainVos) {
			List<PrpLCItemCarVo> cars = prpLCMainVo.getPrpCItemCars();
			LossPolicyItemVo item = new LossPolicyItemVo();
			PrpLCItemCarVo car = cars.get(0);
			item.setId(prpLCMainVo.getPolicyNo());// 主键,没有主键使用保单号
			item.setPolicyCode(prpLCMainVo.getPolicyNo());// 保单号，与主键二选一
			item.setReportCode(prpLCMainVo.getRegistNo());// 报案号
			item.setInsureBgnDate(DateUtils.dateToStr(prpLCMainVo.getStartDate(),DateUtils.YToSec));// 保险起期
			item.setInsureEndDate(DateUtils.dateToStr(prpLCMainVo.getEndDate(),DateUtils.YToSec));// 保险止期
			item.setVehicleCode(car.getModelCode());// 车型代码
			String vehicleName = CodeTranUtil.transCode("ModelCode",car.getModelCode());
			if(StringUtils.isEmpty(vehicleName)){
				item.setVehicleName("");
			}else{
				item.setVehicleName(vehicleName);
			}
//			item.setInsuredPerson(prpLCMainVo.getInsuredName());// 被保险人姓名
			// 关闭推送被保险人姓名
			item.setInsuredPerson("");// 被保险人姓名
			item.setCompanyCode(prpLCMainVo.getComCode());// 承保机构代码
			String comCodeName = CodeTranUtil.transCode("ComCode",prpLCMainVo.getComCode());
			if(StringUtils.isEmpty(comCodeName)){
				item.setCompanyName("");
			}else{
				item.setCompanyName(comCodeName);// 承保机构名称
			}
			item.setTotalInsSum(prpLCMainVo.getSumAmount()+"");// 总保额
			item.setRiskType(prpLCMainVo.getClassCode());// 险类种别
			item.setRiskCode(prpLCMainVo.getRiskCode());// 险种代码
			if(prpLCMainVo.getClassCode().equals("11")){
				item.setRiskName("交强");// 险种名称
			}else if(prpLCMainVo.getClassCode().equals("12")){
				item.setRiskName("商业");
			}
			item.setCustomerTypeCode("");// 客户类型代码 * 精友校验升级后为非必需字段
			item.setCustomerTypeName("");// 客户类型名称 * 精友校验升级后为非必需字段
			item.setSpecifyAssume("");// 特别约定
			// 关闭推送业务渠道代码
			item.setChannelCode("");
//			if(StringUtils.isEmpty(prpLCMainVo.getBusinessChannel())){
//				item.setChannelCode("");// 业务渠道代码* 精友校验升级后为非必需字段
//			}else{
//				item.setChannelCode(prpLCMainVo.getBusinessChannel());// 业务渠道代码* 精友校验升级后为非必需字段
//			}
			item.setChannelName("");// 业务渠道名称* 精友校验升级后为非必需字段
			item.setBillDate(DateUtils.dateToStr(prpLCMainVo.getInputDate(),DateUtils.YToSec));// 出单日期
			item.setPolicyHoldDate(DateUtils.dateToStr(prpLCMainVo.getOperateDate(),DateUtils.YToSec));// 投保日期
			// 关闭推送车辆所有人
			item.setCarOwner("");
//			item.setCarOwner(car.getCarOwner());// 车辆所有人
			PrpLRegistVo registVo = registService.findRegistByRegistNo(prpLCMainVo.getRegistNo());
			// 关闭推送身份证号
			item.setIdentityNo("");// 身份证号
//			item.setIdentityNo(registVo.getReportorIdfNo());// 身份证号
			// 关闭推送驾驶员姓名
			item.setDriverName("");// 驾驶员姓名
//			item.setDriverName(registVo.getDriverName());// 驾驶员姓名

			if(prpLCMainVo.getClassCode().equals("11")){
				item.setAccidentNum(registQueryService.findRiskInfoByRegistNo(prpLCMainVo.getRegistNo(),"1")+"");// 历史出险次数
			}else if(prpLCMainVo.getClassCode().equals("12")){
				item.setAccidentNum(registQueryService.findRiskInfoByRegistNo(prpLCMainVo.getRegistNo(),"2")+"");
			}else{
				item.setAccidentNum("0");
			}
			lossPolicy.add(item);
		}
		dlossAskReqBodyVo.setLossPolicy(lossPolicy);
	}
	
	// 承保车辆信息
	private void organizeLossCoverVehicle(PrpLCMainVo prpLCMainVo,DlossAskReqBodyVo dlossAskReqBodyVo) {
		LossCoverVehicleVo lossCoverVehicle = new LossCoverVehicleVo();
		PrpLCItemCarVo car = prpLCMainVo.getPrpCItemCars().get(0);// 不论是交强还是商业，都是同一辆车
		List<PrpLCItemKindVo> prpLCItemKindVos = prpLCMainVo.getPrpCItemKinds();
		lossCoverVehicle.setId(car.getPolicyNo());
		lossCoverVehicle.setRealPrice(car.getActualValue()+"");// 实际价值
		lossCoverVehicle.setVehiclePrice(car.getPurchasePrice()+"");// 新车购置价
		for (PrpLCItemKindVo prpLCItemKindVo : prpLCItemKindVos) {
			if(KINDCODE.KINDCODE_A.equals(prpLCItemKindVo.getKindCode())){
				lossCoverVehicle.setInsuredAmount(String.valueOf(prpLCItemKindVo.getAmount()));// 车损险承保保额
			}
		}
		if(car.getCountryNature().equals("A")){
			lossCoverVehicle.setIsImport("02");// 01 国产 B/02 进口 A
		}else if(car.getCountryNature().equals("B")){
			lossCoverVehicle.setIsImport("01");// 01 国产 B/02 进口 A
		}
		lossCoverVehicle.setVehicleType(car.getCarKindCode());// 车辆种类代码
		String vehicleTypeName = CodeTranUtil.transCode("CarKindCode",car.getCarKindCode());
		lossCoverVehicle.setVehicleTypeName(vehicleTypeName);// 车辆种类名称
		lossCoverVehicle.setCarColor(car.getColorCode());// 车身颜色代码
		String carColorName = CodeTranUtil.transCode("ColorCode",car.getColorCode());
		lossCoverVehicle.setCarColorName(carColorName);// 车身颜色名称
		lossCoverVehicle.setEnrolDate(DateUtils.dateToStr(car.getEnrollDate(),DateUtils.YToSec));// 初次登记年月日
		lossCoverVehicle.setUseProperty(car.getUseKindCode());// 使用性质
		lossCoverVehicle.setSeat(car.getSeatCount()+"");// 座位
		lossCoverVehicle.setVinNo(car.getVinNo());// 车架号
		lossCoverVehicle.setEngineNo(car.getEngineNo());// 发动机号
		lossCoverVehicle.setVehicleModel(car.getBrandName());// 车辆厂牌型号
		lossCoverVehicle.setPlateNum(car.getLicenseNo());// 车牌号码
		lossCoverVehicle.setPower("");// 功率
		lossCoverVehicle.setDisplacement(car.getTonCount()+"");// 排量
		lossCoverVehicle.setTonnage(car.getExhaustScale()+"");// 吨位
		lossCoverVehicle.setPlateColor(car.getLicenseColorCode());// 车牌颜色
		lossCoverVehicle.setCreateTime(DateUtils.dateToStr(car.getCreateTime(),DateUtils.YToSec));// 创建时间
		lossCoverVehicle.setMakeDate(DateUtils.dateToStr(car.getMakeDate(),DateUtils.YToSec));// 制造年月
		lossCoverVehicle.setGuardAlarm("");// 防盗装置
		lossCoverVehicle.setExemptFlag("");// 免验标志
		lossCoverVehicle.setBelongProperty("");// 所属性质
		dlossAskReqBodyVo.setLossCoverVehicle(lossCoverVehicle);
	}
	
	// 组织承保险别信息
	private void organizeLossInsured(List<PrpLCMainVo> prpLCMainVos,DlossAskReqBodyVo dlossAskReqBodyVo) {
		List<LossInsuredItemVo> items = new ArrayList<LossInsuredItemVo>();
		for (PrpLCMainVo prpLCMainVo : prpLCMainVos) {
			LossInsuredItemVo item = new LossInsuredItemVo();
			item.setId(prpLCMainVo.getPolicyNo());
			item.setPolicyId(prpLCMainVo.getPolicyNo());// 保单号
			item.setRiskCode(prpLCMainVo.getRiskCode());// 险种代码
			if(prpLCMainVo.getClassCode().equals("11")){
				item.setRiskName("交强");// 险种名称
			}else if(prpLCMainVo.getClassCode().equals("12")){
				item.setRiskName("商业");
			}
			List<PrpLCItemKindVo> itemKinds = prpLCMainVo.getPrpCItemKinds();
			if(itemKinds!=null&& !itemKinds.isEmpty()){
				item.setItemCode(itemKinds.get(0).getKindCode());// 险别代码 -
				item.setItemName(itemKinds.get(0).getKindName());// 险别名称 -
			}else{
				item.setItemCode("");// 险别代码 -
				item.setItemName("");// 险别名称 -
			}
			item.setInsuranceSuitYear("");// 险种适用年款
			item.setTotalInsSum(prpLCMainVo.getSumAmount()+"");// 保险金额
			item.setTotalInsFee(prpLCMainVo.getSumPremium()+"");// 保险费
			item.setInsuranceProperty("");// 险别属性
			item.setItemAttribute("");// 条款属性
			item.setNopayFlag("");// 投保不计免赔标志
			items.add(item);
		}
		dlossAskReqBodyVo.setLossInsured(items);
	}
	
	// 组织报案信息
	private void organizeLossReporting(PrpLRegistVo registVo,PrpLDisasterVo disasterVo, List<PrpLCMainVo> prpLCMainVos,DlossAskReqBodyVo dlossAskReqBodyVo) {
		LossReportingVo lossReporting = new LossReportingVo();
		PrpLRegistExtVo registExtVo = registVo.getPrpLRegistExt();
		PrpLCMainVo prpLCMainVo = prpLCMainVos.get(0);
		lossReporting.setId(prpLCMainVo.getPolicyNo());
		lossReporting.setReportCode(prpLCMainVo.getRegistNo());// 报案号
		lossReporting.setAccidentCauseCode(registVo.getDamageCode());// 出险原因代码
		String accidentCauseName = CodeTranUtil.transCode("DamageCode",registVo.getDamageCode());
		lossReporting.setAccidentCauseName(accidentCauseName);// 出险原因名称
		lossReporting.setAccidentDutyCode("");// 事故责任代码
		lossReporting.setAccidentDutyName("");// 事故责任名称
		lossReporting.setReportPersonName(registVo.getReportorName());// 报案人姓名
		lossReporting.setReportMoblePhone(registVo.getReportorPhone());// 报案人手机
		lossReporting.setReportTime(DateUtils.dateToStr(registVo.getReportTime(),DateUtils.YToSec));// 报案时间
		lossReporting.setAccidentTime(DateUtils.dateToStr(registVo.getDamageTime(),DateUtils.YToSec));// 出险时间
		lossReporting.setIsCurrentReport(registExtVo.getIsOnSitReport());// 现场报案
		lossReporting.setManageType("");// 处理方式
		lossReporting.setAccidentReasonCode("");// 事故原因代码
		lossReporting.setAccidentReasonName("");// 事故原因名称
		if(disasterVo!=null){
			lossReporting.setHugeType(disasterVo.getDisasterTypeCode());// 巨灾类型
			lossReporting.setHugeTypeName(disasterVo.getDisasterTypeName());// 巨灾类型名称
			lossReporting.setHugeCode(disasterVo.getDisasterCodeOne());// 巨灾代码
			lossReporting.setHugeName(disasterVo.getDisasterNameOne());// 巨灾名称
		}else {
			lossReporting.setHugeType("");// 巨灾类型
			lossReporting.setHugeTypeName("");// 巨灾类型名称
			lossReporting.setHugeCode("");// 巨灾代码
			lossReporting.setHugeName("");// 巨灾名称
		}
		lossReporting.setInsuranceCompanyCode(prpLCMainVo.getComCode());// 承保机构编码
		String comCodeName = CodeTranUtil.transCode("ComCode",prpLCMainVo.getComCode());
		lossReporting.setInsuranceCompanyName(comCodeName);// 承保机构名称
		lossReporting.setReportType("");// 报案类型
		lossReporting.setAccidentPlace(registVo.getDamageAddress());// 出险地点
		registExtVo.setDangerRemark(registExtVo.getDangerRemark().replaceAll(registVo.getDriverName(), "***"));
		lossReporting.setAccidentCourse(registExtVo.getDangerRemark());// 出险经过
		lossReporting.setCarLossFlag(registExtVo.getIsCarLoss());// 是否有车损
		lossReporting.setInjureLossFlag(registExtVo.getIsPersonLoss());// 是否有人伤
		lossReporting.setCargoLossFlag(registExtVo.getIsPropLoss());// 是否有物损
		lossReporting.setThiefLossFlag("否");// 是否有盗窃 --默认否
		List<PrpLRegistPersonLossVo> prpLRegistPersonLosses = registVo.getPrpLRegistPersonLosses();
		int InjureNum = 0;
		if( !prpLRegistPersonLosses.isEmpty()&&prpLRegistPersonLosses!=null){
			for(int i = 0; i<prpLRegistPersonLosses.size(); i++ ){
				InjureNum += prpLRegistPersonLosses.get(i).getInjuredcount()+prpLRegistPersonLosses.get(i).getDeathcount();
			}
		}
		lossReporting.setInjureNum(InjureNum+"");// 死伤人数
		String damageAreaName = CodeTranUtil.transCode("DamageAreaCode",registVo.getDamageAreaCode());
		lossReporting.setAccidentArea(damageAreaName);// 出险区域
		lossReporting.setAccidentWeather("");// 出险天气 --不传
		dlossAskReqBodyVo.setLossReporting(lossReporting);
	}
	
	/**
	 * 对封装地址进行内外网封装
	 **/
	public void initRealUrl(ClaimFittingVo claimFittingVo){
		SendVo sendVo = claimFittingVo.getSendVo();
		String claimUrl = "http://" + sendVo.getServerName()
				+ ":"+ sendVo.getServerPort()
				+ sendVo.getContextPath();
		// 暂时这么取 理赔内网地址 ，之后配置到fram的app文件中
		String claimUrl_In = SpringProperties.getProperty("CLAIM_URL_IN");
		claimFittingVo.setReturnURL(claimUrl_In+"/jyDlossReceive");
		claimFittingVo.setRefreshURL(claimUrl+"/reloadFittings?operateType=certa");
		
	}
	
}
