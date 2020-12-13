package ins.sino.claimcar.regist.service;

import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.CodeConstants.KINDCODE;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.vo.PrpLDisasterVo;
import ins.sino.claimcar.claimjy.service.PrpLRegistToLossService;
import ins.sino.claimcar.claimjy.util.JyHttpUtil;
import ins.sino.claimcar.claimjy.vo.CarLossDetailItemVo;
import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;
import ins.sino.claimcar.claimjy.vo.JyResVo;
import ins.sino.claimcar.claimjy.vo.LossCoverVehicleVo;
import ins.sino.claimcar.claimjy.vo.LossInsuredItemVo;
import ins.sino.claimcar.claimjy.vo.LossPolicyItemVo;
import ins.sino.claimcar.claimjy.vo.LossReportingVo;
import ins.sino.claimcar.claimjy.vo.PrpLRegistToLossBodyVo;
import ins.sino.claimcar.claimjy.vo.PrpLRegistToLossVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPersonLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.text.SimpleDateFormat;
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
@Path(value = "prpLRegistToLossService")
public class PrpLRegistToLossServiceImpl implements PrpLRegistToLossService {

	public Logger LOGGER = LoggerFactory.getLogger(PrpLRegistToLossServiceImpl.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// 服务装载
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	RegistService registService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	private CheckHandleService checkHandleService;

	@Autowired
	ClaimInterfaceLogService claimInterfaceLogService;

	@Override
	public String sendRegistXmlData(String registNo) {
		PrpLRegistToLossVo reqVo = new PrpLRegistToLossVo();
		JyResVo resVo = new JyResVo();
		List<PrpLCMainVo> cMains = new ArrayList<PrpLCMainVo>();

		String url = "";
		String xmlToSend = "";
		String xmlReturn = "";
		String flag = "000";
		String createUser = "";
		String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
		seconds = StringUtils.isBlank(seconds) ? "20" : seconds;
		try{
			cMains = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
			if(cMains!=null&& !cMains.isEmpty()){
				reqVo = getPacket(cMains,registNo);
				createUser = cMains.get(0).getCreateUser();
			}

			xmlToSend = ClaimBaseCoder.objToXml(reqVo);
			LOGGER.info("报案请求接口提交send---------------------------"+xmlToSend);
			url = SpringProperties.getProperty("JY2_REGIST");
			xmlReturn = JyHttpUtil.sendData(xmlToSend,url,Integer.parseInt(seconds));
			LOGGER.info("报案请求接口接口提交return---------------------------"+xmlReturn);
			resVo = ClaimBaseCoder.xmlToObj(xmlReturn,JyResVo.class);
			LOGGER.info(resVo.getHead().getErrorMessage());
		}catch(Exception e){
			LOGGER.error("报案请求接口异常",e);
			flag = "returnFalse";// 失败了随便取，只要不是000就好
		}
		finally{
			ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
			logVo.setRegistNo(registNo);
			if("000".equals(flag)){
				// 成功
				logVo.setStatus("1");
				logVo.setErrorCode("true");
			}else{
				// 失败
				logVo.setStatus("0");
				logVo.setErrorCode("false");
			}
			logVo.setBusinessType(BusinessType.JY_Regist.name());
			logVo.setBusinessName(BusinessType.JY_Regist.getName());
			logVo.setRequestXml(xmlToSend);
			logVo.setResponseXml(xmlReturn);
			logVo.setRequestUrl(url);
			logVo.setCreateUser(createUser);
			logVo.setCreateUser(createUser);
			logVo.setCreateTime(new Date());
			logVo.setErrorCode(resVo.getHead().getResponseCode());
			logVo.setErrorMessage(resVo.getHead().getErrorMessage());
			try{
				claimInterfaceLogService.save(logVo);
			}catch(Exception e){
				LOGGER.error("日志保存失败");
				LOGGER.error(e+"");
			}
		}
		return resVo.getHead().getErrorMessage();
	}

	private PrpLRegistToLossVo getPacket(List<PrpLCMainVo> cMains,String registNo) {
		PrpLRegistToLossVo packet = new PrpLRegistToLossVo();
		packet.setJyReqHeadVo(getHead());
		packet.setPrpLRegistToLossBodyVo(getBody(cMains,registNo));
		return packet;
	}

	// HEAD
	private JyReqHeadVo getHead() {
		JyReqHeadVo head = new JyReqHeadVo();
		head.setUserCode("jy");
		head.setPassWord("jy");
		head.setRequestSourceCode("DHIC");
		head.setRequestSourceName("鼎和财产保险股份有限公司");
		head.setRequestType("008");
		head.setOperatingTime(DateUtils.dateToStr(DateUtils.now(),DateUtils.YToSec));
		return head;
	}

	// BODY
	private PrpLRegistToLossBodyVo getBody(List<PrpLCMainVo> cMains,String registNo) {
		PrpLRegistToLossBodyVo body = new PrpLRegistToLossBodyVo();
		body.setLossPolicy(getLossPolicy(cMains));
		body.setLossCoverVehicle(getLossCoverVehicle(cMains));
		body.setLossInsured(getLossInsured(cMains));
		body.setLossReporting(getLossReporting(cMains,registNo));
		body.setCarLossDetail(getCarLossDetail(registNo));
		return body;
	}

	// LossPolicy
	private List<LossPolicyItemVo> getLossPolicy(List<PrpLCMainVo> cMains) {
		List<LossPolicyItemVo> items = new ArrayList<LossPolicyItemVo>();
		// 获取PrpLCMain
		try{
			for(int i = 0; i<cMains.size(); i++ ){
				PrpLCMainVo cMain = cMains.get(i);
				List<PrpLCItemCarVo> cars = cMain.getPrpCItemCars();
				LossPolicyItemVo item = new LossPolicyItemVo();
				PrpLCItemCarVo car = cars.get(0);
				item.setId(cMain.getPolicyNo());// 主键,没有主键使用保单号
				item.setPolicyCode(cMain.getPolicyNo());// 保单号，与主键二选一
				item.setReportCode(cMain.getRegistNo());// 报案号
				item.setInsureBgnDate(sdf.format(cMain.getStartDate()));// 保险起期
				item.setInsureEndDate(sdf.format(cMain.getEndDate()));// 保险止期
				item.setVehicleCode(car.getModelCode());// 车型代码
				String vehicleName = CodeTranUtil.transCode("ModelCode",car.getModelCode());
				if(StringUtils.isEmpty(vehicleName)){
					item.setVehicleName("");
				}else{
					item.setVehicleName(vehicleName);
				}
//				item.setInsuredPerson(cMain.getInsuredName());// 被保险人姓名
				// 关闭推送被保险人姓名
				item.setInsuredPerson("");// 被保险人姓名
				item.setCompanyCode(cMain.getComCode());// 承保机构代码
				String comCodeName = CodeTranUtil.transCode("ComCode",cMain.getComCode());
				if(StringUtils.isEmpty(comCodeName)){
					item.setCompanyName("");
				}else{
					item.setCompanyName(comCodeName);// 承保机构名称
				}
				item.setTotalInsSum(cMain.getSumAmount()+"");// 总保额
				item.setRiskType(cMain.getClassCode());// 险类种别
				item.setRiskCode(cMain.getRiskCode());// 险种代码
				if(cMain.getClassCode().equals("11")){
					item.setRiskName("交强");// 险种名称
				}else if(cMain.getClassCode().equals("12")){
					item.setRiskName("商业");
				}
				item.setCustomerTypeCode("");// 客户类型代码 * 精友校验升级后为非必需字段
				item.setCustomerTypeName("");// 客户类型名称 * 精友校验升级后为非必需字段
				item.setSpecifyAssume("");// 特别约定
				// 关闭推送业务渠道代码
				item.setChannelCode("");
//				if(StringUtils.isEmpty(cMain.getBusinessChannel())){
//					item.setChannelCode("");// 业务渠道代码* 精友校验升级后为非必需字段
//				}else{
//					item.setChannelCode(cMain.getBusinessChannel());// 业务渠道代码* 精友校验升级后为非必需字段
//				}
				// String businessChannelName = CodeTranUtil.transCode("BusinessChannel",cMain.getBusinessChannel());
				item.setChannelName("");// 业务渠道名称* 精友校验升级后为非必需字段
				item.setBillDate(sdf.format(cMain.getInputDate()));// 出单日期
				item.setPolicyHoldDate(sdf.format(cMain.getOperateDate()));// 投保日期
//				item.setCarOwner(car.getCarOwner());// 车辆所有人
				// 关闭推送车辆所有人
				item.setCarOwner("");
//				PrpLRegistVo registVo = registService.findRegistByRegistNo(cMain.getRegistNo());
//				item.setIdentityNo(registVo.getReportorIdfNo());// 身份证号
				// 关闭推送身份证号
				item.setIdentityNo("");// 身份证号
//				item.setDriverName(registVo.getDriverName());// 驾驶员姓名
				// 关闭推送驾驶员姓名
				item.setDriverName("");// 驾驶员姓名

				if(cMain.getClassCode().equals("11")){
					item.setAccidentNum(registQueryService.findRiskInfoByRegistNo(cMain.getRegistNo(),"1")+"");// 历史出险次数
				}else if(cMain.getClassCode().equals("12")){
					item.setAccidentNum(registQueryService.findRiskInfoByRegistNo(cMain.getRegistNo(),"2")+"");
				}else{
					item.setAccidentNum("0");
				}
				items.add(item);
			}
		}catch(Exception e){
			LOGGER.error("getLossPolicy Exception"+e);
		}
		return items;
	}

	// LossCoverVehicle
	private LossCoverVehicleVo getLossCoverVehicle(List<PrpLCMainVo> cMains) {
		LossCoverVehicleVo lossCoverVehicle = new LossCoverVehicleVo();
		// 交强和商业对应同一辆车
		PrpLCMainVo cMain = new PrpLCMainVo();
		PrpLCItemCarVo car = new PrpLCItemCarVo();
		try{
			cMain = cMains.get(0); // 交强或者商业保单
			car = cMain.getPrpCItemCars().get(0);// 不论是交强还是商业，都是同一辆车
			lossCoverVehicle.setId(car.getPolicyNo());
			lossCoverVehicle.setRealPrice(car.getActualValue()+"");// 实际价值
			lossCoverVehicle.setVehiclePrice(car.getPurchasePrice()+"");// 新车购置价

			List<PrpLCItemKindVo> prpLCItemKindVos = cMain.getPrpCItemKinds();
			if( !prpLCItemKindVos.isEmpty()&&prpLCItemKindVos!=null){
				for(PrpLCItemKindVo cItemKindVo:prpLCItemKindVos){
					if(KINDCODE.KINDCODE_A.equals(cItemKindVo.getKindCode())){
						lossCoverVehicle.setInsuredAmount(cItemKindVo.getAmount()+"");// 承保保额;
					}
				}

			}

			if(car.getCountryNature().equals("A")){
				lossCoverVehicle.setIsImport("02");// 01 国产 B/02 进口 A
			}else if(car.getCountryNature().equals("B")){
				lossCoverVehicle.setIsImport("01");// 01 国产 B/02 进口 A
			}else{
				lossCoverVehicle.setIsImport("");// 01 国产 B/02 进口 A
			}

			lossCoverVehicle.setVehicleType(car.getCarKindCode());// 车辆种类代码
			String vehicleTypeName = CodeTranUtil.transCode("CarKindCode",car.getCarKindCode());
			lossCoverVehicle.setVehicleTypeName(vehicleTypeName);// 车辆种类名称
			lossCoverVehicle.setCarColor(car.getColorCode());// 车身颜色代码
			String carColorName = CodeTranUtil.transCode("ColorCode",car.getColorCode());
			lossCoverVehicle.setCarColorName(carColorName);// 车身颜色名称
			lossCoverVehicle.setEnrolDate(sdf.format(car.getEnrollDate()));// 初次登记年月日
			lossCoverVehicle.setUseProperty(car.getUseKindCode());// 使用性质
			// lossCoverVehicle.setDriverArea("北京");//具体以表格为准
			lossCoverVehicle.setSeat(car.getSeatCount()+"");// 座位
			if(StringUtils.isEmpty(car.getVinNo())){
				lossCoverVehicle.setVinNo("");
			}else{
				lossCoverVehicle.setVinNo(car.getVinNo());// 车架号
			}
			lossCoverVehicle.setEngineNo(car.getEngineNo());// 发动机号
			lossCoverVehicle.setVehicleModel(car.getBrandName());// 车辆厂牌型号
			lossCoverVehicle.setPlateNum(car.getLicenseNo());// 车牌号码
			lossCoverVehicle.setPower("");// 功率
			lossCoverVehicle.setDisplacement(car.getTonCount()+"");// 排量
			lossCoverVehicle.setTonnage(car.getExhaustScale()+"");// 吨位
			lossCoverVehicle.setPlateColor(car.getLicenseColorCode());// 车牌颜色
			lossCoverVehicle.setCreateTime(sdf.format(car.getCreateTime()));// 创建时间
			if(car.getMakeDate()==null){
				lossCoverVehicle.setMakeDate("");// 制造年月
			}else{
				lossCoverVehicle.setMakeDate(sdf.format(car.getMakeDate()));
			}
			lossCoverVehicle.setGuardAlarm("");// 防盗装置
			lossCoverVehicle.setExemptFlag("");// 免验标志
			lossCoverVehicle.setBelongProperty("");// 所属性质
		}catch(Exception e){
			LOGGER.error("getLossCoverVehicle Exception "+e);
		}
		return lossCoverVehicle;
	}

	// LossInsured
	private List<LossInsuredItemVo> getLossInsured(List<PrpLCMainVo> cMains) {
		List<LossInsuredItemVo> items = new ArrayList<LossInsuredItemVo>();
		try{
			for(int i = 0; i<cMains.size(); i++ ){
				PrpLCMainVo cMain = cMains.get(i);
				LossInsuredItemVo item = new LossInsuredItemVo();

				item.setId(cMain.getPolicyNo());
				item.setPolicyId(cMain.getPolicyNo());// 保单号
				item.setRiskCode(cMain.getRiskCode());// 险种代码
				if(cMain.getClassCode().equals("11")){
					item.setRiskName("交强");// 险种名称
				}else if(cMain.getClassCode().equals("12")){
					item.setRiskName("商业");
				}
				List<PrpLCItemKindVo> itemKinds = cMain.getPrpCItemKinds();
				if(itemKinds!=null&& !itemKinds.isEmpty()){
					item.setItemCode(itemKinds.get(0).getKindCode());// 险别代码 -
					item.setItemName(itemKinds.get(0).getKindName());// 险别名称 -
				}else{
					item.setItemCode("");// 险别代码 -
					item.setItemName("");// 险别名称 -
				}
				item.setInsuranceSuitYear("");// 险种适用年款
				item.setTotalInsSum(cMain.getSumAmount()+"");// 保险金额
				item.setTotalInsFee(cMain.getSumPremium()+"");// 保险费
				item.setInsuranceProperty("");// 险别属性
				item.setItemAttribute("");// 条款属性
				item.setNopayFlag("");// 投保不计免赔标志
				items.add(item);
			}
		}catch(Exception e){
			LOGGER.error("getLossInsured Exception"+e);
		}
		return items;
	}

	// LossReporting
	private LossReportingVo getLossReporting(List<PrpLCMainVo> cMains,String registNo) {
		LossReportingVo lossReporting = new LossReportingVo();
		PrpLRegistVo registVo = new PrpLRegistVo();
		PrpLDisasterVo disasterVo = new PrpLDisasterVo();
		PrpLRegistExtVo registExtVo = new PrpLRegistExtVo();
		try{
			registVo = registService.findRegistByRegistNo(registNo);;
			disasterVo = checkHandleService.findDisasterVoByRegistNo(registNo);

			registExtVo = registVo.getPrpLRegistExt();
			lossReporting.setId(cMains.get(0).getPolicyNo());//
			lossReporting.setReportCode(registNo);// 报案号
			lossReporting.setAccidentCauseCode(registVo.getDamageCode());// 出险原因代码
			String accidentCauseName = CodeTranUtil.transCode("DamageCode",registVo.getDamageCode());
			lossReporting.setAccidentCauseName(accidentCauseName);// 出险原因名称
			lossReporting.setAccidentDutyCode("");// 事故责任代码
			lossReporting.setAccidentDutyName("");// 事故责任名称
			lossReporting.setReportPersonName(registVo.getReportorName());// 报案人姓名
			lossReporting.setReportMoblePhone(registVo.getReportorPhone());// 报案人手机
			lossReporting.setReportTime(sdf.format(registVo.getReportTime()));// 报案时间
			lossReporting.setAccidentTime(sdf.format(registVo.getDamageTime()));// 出险时间
			lossReporting.setIsCurrentReport(registExtVo.getIsOnSitReport());// 现场报案
			lossReporting.setManageType("");// 处理方式
			lossReporting.setAccidentReasonCode("");// 事故原因代码
			lossReporting.setAccidentReasonName("");// 事故原因名称
			if(disasterVo==null){
				lossReporting.setHugeType("");// 巨灾类型
				lossReporting.setHugeTypeName("");// 巨灾类型名称
				lossReporting.setHugeCode("");// 巨灾代码
				lossReporting.setHugeName("");// 巨灾名称
			}else{
				lossReporting.setHugeType(disasterVo.getDisasterTypeCode());// 巨灾类型
				lossReporting.setHugeTypeName(disasterVo.getDisasterTypeName());// 巨灾类型名称
				lossReporting.setHugeCode(disasterVo.getDisasterCodeOne());// 巨灾代码
				lossReporting.setHugeName(disasterVo.getDisasterNameOne());// 巨灾名称
			}
			lossReporting.setInsuranceCompanyCode(cMains.get(0).getComCode());// 承保机构编码
			String comCodeName = CodeTranUtil.transCode("ComCode",cMains.get(0).getComCode());
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
		}catch(Exception e){
			LOGGER.error("getLossReporting Exception"+e);
		}
		return lossReporting;
	}

	// CarLossDetail
	private List<CarLossDetailItemVo> getCarLossDetail(String registNo) {
		List<CarLossDetailItemVo> items = new ArrayList<CarLossDetailItemVo>();
		PrpLRegistVo registVo = new PrpLRegistVo();
		List<PrpLRegistCarLossVo> prpLRegistCarLosses = new ArrayList<PrpLRegistCarLossVo>();
		try{
			registVo = registService.findRegistByRegistNo(registNo);;
			prpLRegistCarLosses = registVo.getPrpLRegistCarLosses();
			if(prpLRegistCarLosses!=null&& !prpLRegistCarLosses.isEmpty()){
				for(int i = 0; i<prpLRegistCarLosses.size(); i++ ){
					CarLossDetailItemVo item = new CarLossDetailItemVo();
					item.setThirdCarLossSerialNo(i+1+"");// 涉案车辆序号
					item.setThirdCarLossItemNo(i+1+"");// 损失项目编号
					item.setThirdCarLossLicenseNo(prpLRegistCarLosses.get(i).getLicenseNo());// 车牌号码
					// 以下字段报案无，传空字段
					item.setKindCode("");// 险别代码
					item.setKindName("");// 险别名称
					item.setLossPartCode("");// 损失部位代码
					item.setPartCode("");// 零件代码
					item.setPartName("");// 零件名称
					item.setLossPartCodeDesc("");// 损失部位描述
					items.add(item);
				}
			}
		}catch(Exception e){
			LOGGER.error("getCarLossDetail Exception"+e);
		}
		return items;
	}
}
