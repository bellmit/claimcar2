package ins.sino.claimcar.fitting.web.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ins.framework.lang.Springs;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.RepairFlag;
import ins.sino.claimcar.claimjy.util.JaxbUtil;
import ins.sino.claimcar.claimjy.vo.CollisionParts;
import ins.sino.claimcar.claimjy.vo.DlossReceiveBodyVo;
import ins.sino.claimcar.claimjy.vo.DlossReceiveReqVo;
import ins.sino.claimcar.claimjy.vo.DlossReceiveResVo;
import ins.sino.claimcar.claimjy.vo.DlossReqHeadVo;
import ins.sino.claimcar.claimjy.vo.DlossRules;
import ins.sino.claimcar.claimjy.vo.JyResHeadVo;
import ins.sino.claimcar.claimjy.vo.LossAssistInfoItem;
import ins.sino.claimcar.claimjy.vo.LossFitInfoItem;
import ins.sino.claimcar.claimjy.vo.LossOuterRepairInfoItem;
import ins.sino.claimcar.claimjy.vo.LossRepairInfoItem;
import ins.sino.claimcar.claimjy.vo.LossRepairSumInfoItem;
import ins.sino.claimcar.claimjy.vo.ReceiveEvalLossInfo;
import ins.sino.claimcar.claimjy.vo.VehicleInfo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.losscar.vo.PrpLLossRepairSumInfoVo;
import ins.sino.claimcar.losscar.vo.PrplLossRuleVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.xstream.XStreamException;

public class JyDlossReceiveServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(JyDlossReceiveServlet.class);
	private HashMap<String,PrpLCItemKindVo> itemKindMap = new HashMap<String,PrpLCItemKindVo>();
	
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	LossCarService lossCarService;

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.initService();
		DlossReceiveReqVo dlossReceiveReqVo = new DlossReceiveReqVo();
		DlossReqHeadVo dlossReqHeadVo = null;
		DlossReceiveBodyVo jyDlossReceiveBodyVo = null;
		try {
			dlossReceiveReqVo = this.requestData(req);			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("精友定损提交报文解析异常！", e);
			this.responseData(resp,"401");
			return;
		}
		dlossReqHeadVo = dlossReceiveReqVo.getDlossReqHeadVo();
		jyDlossReceiveBodyVo = dlossReceiveReqVo.getDlossReceiveBodyVo();	
		
		if(dlossReqHeadVo==null||jyDlossReceiveBodyVo==null){
			this.responseData(resp,"402");
			return;
		}
		if(!"009".equals(dlossReqHeadVo.getRequestType())){
			this.responseData(resp,"403");
			return;
		}
		if((!"jy".equals(dlossReqHeadVo.getUserCode()) && !"jy".equals(dlossReqHeadVo.getPassWord()))){
			this.responseData(resp,"498");
			return;
		}
		
		try {
			PrpLDlossCarMainVo prpLDlossCarMainVo = this.organizeInfo(dlossReceiveReqVo);
			//保存数据
			lossCarService.saveJyDeflossInfo(prpLDlossCarMainVo, prpLDlossCarMainVo.getLossCarInfoVo());
		} catch (SQLException e) {
			logger.info("精友定损完成返回接口数据保存异常", e);
			this.responseData(resp,"405");
			e.printStackTrace();
			return;
		} catch (Exception e) {
			logger.info("精友定损完成返回接口数据保存异常", e);
			this.responseData(resp,"499");
			e.printStackTrace();
			return;
		}
		this.responseData(resp,"000");
		return;
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	private DlossReceiveReqVo requestData(HttpServletRequest httpServletRequest) throws IOException,XStreamException, DocumentException{
		DlossReceiveReqVo dlossReceiveReqVo = new DlossReceiveReqVo();
		String xmlData="";
		InputStreamReader inputStreamReader = new InputStreamReader(httpServletRequest.getInputStream(),"GBK");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		
		String read = "";
		StringBuffer stringBuffer = new StringBuffer();
		// 转换数据流
		while(( read = bufferedReader.readLine() )!=null){
			stringBuffer.append(read);
		}
		inputStreamReader.close();
		bufferedReader.close();
		xmlData = stringBuffer.toString();
        logger.info("精友定损完成返回接口接收xml：\n"+xmlData);
        dlossReceiveReqVo = JaxbUtil.toObj(xmlData, DlossReceiveReqVo.class);
		return dlossReceiveReqVo;
	}
	
	private void responseData(HttpServletResponse httpServletResponse,String responseCode) throws IOException{
		DlossReceiveResVo jyDlossResVo = new DlossReceiveResVo();
		JyResHeadVo jyResHeadVo = new JyResHeadVo();
		Writer out = new OutputStreamWriter(httpServletResponse.getOutputStream(), "GBK");
		
		String resultXml = "";
		
		jyResHeadVo.setResponseCode(responseCode);
		jyResHeadVo.setErrorMessage(this.getMsgByCode(responseCode));
		jyDlossResVo.setJyResHeadVo(jyResHeadVo);
		resultXml = ClaimBaseCoder.objToXml(jyDlossResVo);
		logger.info("精友定损完成返回接口返回xml：\n"+resultXml);
		out.write(resultXml);
		out.flush();
		out.close();
	}
	
	private PrpLDlossCarMainVo organizeInfo(DlossReceiveReqVo receiveReqVo){
		PrpLDlossCarMainVo lossCarMainVo = new PrpLDlossCarMainVo();
		PrpLDlossCarInfoVo lossCarInfoVo = new PrpLDlossCarInfoVo();
		DlossReceiveBodyVo dlossReceiveBodyVo = receiveReqVo.getDlossReceiveBodyVo();
		ReceiveEvalLossInfo receiveEvalLossInfo = dlossReceiveBodyVo.getReceiveEvalLossInfo();
		VehicleInfo vehicleInfo = dlossReceiveBodyVo.getVehicleInfo();//车辆信息
		List<CollisionParts> collisionPartses = dlossReceiveBodyVo.getCollisionParts();//损失部位
		List<LossFitInfoItem> lossFitInfoItems = dlossReceiveBodyVo.getLossFitInfoItemItems();//配件
		List<LossRepairInfoItem> lossRepairInfoItems = dlossReceiveBodyVo.getLossRepairInfoItems();//修理
		List<LossOuterRepairInfoItem> lossOuterRepairInfoItems = dlossReceiveBodyVo.getLossOuterRepairInfoItems();//外修
		List<LossAssistInfoItem> lossAssistInfoItems = dlossReceiveBodyVo.getLossAssistInfoItems();//辅料
		List<LossRepairSumInfoItem> lossRepairSumInfoItems = dlossReceiveBodyVo.getLossRepairSumInfoItems();
		DlossRules dlossRules = dlossReceiveBodyVo.getDlossRules();//风险规则
		
		List<PrpLCItemKindVo> itemKinds =  policyViewService.findItemKinds(receiveEvalLossInfo.getReportCode(),null);
		for(PrpLCItemKindVo itemKindVo : itemKinds){
			itemKindMap.put(itemKindVo.getKindCode(),itemKindVo);
		}
			
		lossCarMainVo.setRegistNo(receiveEvalLossInfo.getReportCode());
		lossCarMainVo.setId(Long.parseLong(receiveEvalLossInfo.getDmgVhclId()));
		lossCarMainVo.setSumCompFee(NullToZero(receiveEvalLossInfo.getEvalPartSum()));//合计换件金额
		lossCarMainVo.setSumRepairFee(NullToZero(receiveEvalLossInfo.getEvalRepairSum()));//合计修理金额
		lossCarMainVo.setSumMatFee(NullToZero(receiveEvalLossInfo.getEvalMateSum()));//合计辅料费
		lossCarMainVo.setSumOutFee(NullToZero(receiveEvalLossInfo.getOuterSum()));//合计外修金额
		lossCarMainVo.setSumRemnant(NullToZero(receiveEvalLossInfo.getRemnantFee()));//合计残值金额
		lossCarMainVo.setSumManageFee(NullToZero(receiveEvalLossInfo.getManageFee()));//合计管理费
		BigDecimal sumLossFee = NullToZero(receiveEvalLossInfo.getSumLossAmount()).subtract(NullToZero(receiveEvalLossInfo.getSalvageFee()));
		lossCarMainVo.setSumLossFee(sumLossFee);
		lossCarMainVo.setSumRescueFee(NullToZero(receiveEvalLossInfo.getSalvageFee()));//施救费用 
		lossCarMainVo.setRepairType(receiveEvalLossInfo.getPriceType());//价格类型
		lossCarMainVo.setFlag("1");//精友标志
		StringBuffer lossPart = new StringBuffer();//损失部位
		if(collisionPartses!=null){
			for (int i = 0; i < collisionPartses.size(); i++) {	
				if(i == 0){
					lossPart.append(collisionPartses.get(i).getCollisionWay());
				}else {
					lossPart.append("," + collisionPartses.get(i).getCollisionWay());
				}
			}
		}
		logger.info("精友报文解析中，报案号：" + receiveEvalLossInfo.getReportCode() + " 修理厂ID：" + receiveEvalLossInfo.getRepairFacID()
				+ " 修理厂名称：" + receiveEvalLossInfo.getRepairFacName() + " 修理厂类型：" + receiveEvalLossInfo.getRepairFacType()
				+ " 修理厂电话：" + receiveEvalLossInfo.getRepairFacPhone());
		lossCarMainVo.setLossPart(lossPart.toString());//损失部位
		lossCarMainVo.setRepairFactoryCode(receiveEvalLossInfo.getRepairFacID());//修理厂Id
		lossCarMainVo.setRepairFactoryName(receiveEvalLossInfo.getRepairFacName());//修理厂名称
		lossCarMainVo.setRepairFactoryType(receiveEvalLossInfo.getRepairFacType());//修理厂类型
		lossCarMainVo.setFactoryMobile(receiveEvalLossInfo.getRepairFacPhone());//修理厂手机RepairFacPhone
		lossCarInfoVo.setLicenseNo(receiveEvalLossInfo.getPlateNo());//车牌号码	
		lossCarInfoVo.setEngineNo(receiveEvalLossInfo.getEngineNo());//发动机号
		lossCarInfoVo.setVinNo(receiveEvalLossInfo.getVinNo());//VIN码
		lossCarInfoVo.setFrameNo(receiveEvalLossInfo.getVinNo());//车架号
		lossCarInfoVo.setVehKindCode(vehicleInfo.getVehicleType());//定损车辆种类代码
		lossCarInfoVo.setVehKindName(this.getVehKindName(vehicleInfo.getVehicleType()));//定损车辆种类名称	VehKindName
		lossCarInfoVo.setModelCode(receiveEvalLossInfo.getVehCertainCode());//定损车型编码
		lossCarInfoVo.setModelName(receiveEvalLossInfo.getVehCertainName());//定损车型名称
		lossCarInfoVo.setGroupCode(receiveEvalLossInfo.getVehGroupCode());//定损车组代码
		lossCarInfoVo.setGroupName(receiveEvalLossInfo.getGroupName());//定损车组名称
		lossCarInfoVo.setSeriCode("");//定损车系编码
		lossCarInfoVo.setSeriName("");//定损车系名称
		lossCarInfoVo.setYearType("");//年款
		lossCarInfoVo.setFactoryCode("");//厂家编码
		lossCarInfoVo.setFactoryName("");//厂家名称	
		lossCarInfoVo.setBrandCode(receiveEvalLossInfo.getVehBrandCode());//品牌编码
		lossCarInfoVo.setBrandName(receiveEvalLossInfo.getBrandName());//品牌名称
		lossCarInfoVo.setSelfConfigFlag(receiveEvalLossInfo.getSelfConfigFlag());//自定义车型标志
		
		lossCarMainVo.setLossCarInfoVo(lossCarInfoVo);
		
		PrpLDlossCarMainVo carMain = lossCarService.findLossCarMainById(lossCarMainVo.getId());
		lossCarMainVo.getLossCarInfoVo().setId(carMain.getCarId());
		
		this.organizeCompInfo(lossFitInfoItems, lossCarMainVo);//组织配件信息
		this.organizeRepairInfo(lossRepairInfoItems, lossOuterRepairInfoItems, lossCarMainVo);//组织修理信息
		this.organizeLossMaterialInfo(lossAssistInfoItems, lossCarMainVo);//组织辅料信息
		this.organizeRepairSumInfo(lossRepairSumInfoItems, lossCarMainVo);//组织工种合计的修理费用
		this.organizeRule(dlossRules, lossCarMainVo);//组织风险规则信息
		return lossCarMainVo;
	}
	
	//配件信息
	private void organizeCompInfo(List<LossFitInfoItem> lossFitInfoItems,PrpLDlossCarMainVo lossCarMainVo) {
		List<PrpLDlossCarCompVo> fitlist=new ArrayList<PrpLDlossCarCompVo>();
		
		String kindCode_X ="";//涉水险
		if(itemKindMap.containsKey(CodeConstants.KINDCODE.KINDCODE_X1)){
			kindCode_X = CodeConstants.KINDCODE.KINDCODE_X1;
		}else if(itemKindMap.containsKey(CodeConstants.KINDCODE.KINDCODE_X2)){
			kindCode_X = CodeConstants.KINDCODE.KINDCODE_X2;
		}
		if(lossFitInfoItems!=null){
			int serialNo = 1;
			for (LossFitInfoItem lossFitInfoItem : lossFitInfoItems) {
				PrpLDlossCarCompVo carCompVo=new PrpLDlossCarCompVo();	
				carCompVo.setIndId(lossFitInfoItem.getPartId());//定损系统零件唯一ID
				carCompVo.setSerialNo(serialNo);//鼎和估损单唯一序号
				carCompVo.setOriginalId(lossFitInfoItem.getPartCode());//零配件原厂编码
				carCompVo.setOriginalName(lossFitInfoItem.getItemName());//零配件原厂名称
				carCompVo.setCompCode(lossFitInfoItem.getPartCode());// 配件标准代码
				carCompVo.setCompName(lossFitInfoItem.getItemName());// 配件标准名称
				carCompVo.setPartGroupCode("");//配件部位代码
				carCompVo.setPartGroupName("");//配件部位名称
				
				carCompVo.setDirectSupplyFlag(lossFitInfoItem.getDirectSupplyFlag());  //解析配件直供标志
				
				
				carCompVo.setRepairFactoryFee(NullToZero(lossFitInfoItem.getLocalMarketPrice()));//本地市场原厂价
				carCompVo.setMaterialFee(NullToZero(lossFitInfoItem.getMaterialFee()));// 材料费
				carCompVo.setQuantity(Integer.valueOf(lossFitInfoItem.getCount()));//换件数量
				carCompVo.setSelfConfigFlag(lossFitInfoItem.getSelfConfigFlag());//自定义配件标记		
				carCompVo.setPriceType(lossFitInfoItem.getChgCompSetCode());//价格方案
				carCompVo.setSys4SPrice(NullToZero(lossFitInfoItem.getSysGuidePrice()));//系统4S店价
				carCompVo.setNative4SPrice(NullToZero(lossFitInfoItem.getLocalGuidePrice()));//本地4S店价
				carCompVo.setSysMarketPrice(NullToZero(lossFitInfoItem.getSysMarketPrice()));//系统市场价
				carCompVo.setNativeMarketPrice(NullToZero(lossFitInfoItem.getLocalMarketPrice()));//本地市场价
				carCompVo.setSysMatchPrice(null);//系统副厂价
				carCompVo.setNativeMatchPrice(null);//本地副厂价
				if("1".equals(lossFitInfoItem.getChgCompSetCode())){
					carCompVo.setChgRefPrice(carCompVo.getSys4SPrice());//系统4S店价
				}else {
					carCompVo.setChgRefPrice(carCompVo.getSysMarketPrice());//系统市场价
				}
				carCompVo.setChgLocPrice(null);//价格方案本地价格	
				carCompVo.setRestFee(NullToZero(lossFitInfoItem.getRemainsPrice()));//残值
				carCompVo.setSumDefLoss(NullToZero(lossFitInfoItem.getEvalPartSum()).subtract(NullToZero(lossFitInfoItem.getRemainsPrice())));//定损金额
				carCompVo.setRemark(lossFitInfoItem.getRemark());//备注
				carCompVo.setKindCode(lossFitInfoItem.getItemCoverCode());//险别代码 
				carCompVo.setKindName(CodeTranUtil.transCode("RiskCode", lossFitInfoItem.getItemCoverCode()));//险别名称
				String wadFlag = lossFitInfoItem.getIfWading();//涉水标识 0否 1是
				if("1".equals(wadFlag) && StringUtils.isNotBlank(kindCode_X)){
					carCompVo.setWadFlag(wadFlag);
					carCompVo.setKindCode(kindCode_X);// 险别代码 
					carCompVo.setKindName(itemKindMap.get(kindCode_X).getKindName());// 险别名称	
				}else{
					carCompVo.setWadFlag("0");
				}
				carCompVo.setRegistNo(lossCarMainVo.getRegistNo());//报案号
				if(StringUtils.isNotBlank(carCompVo.getKindCode())){
					carCompVo.setRiskCode(itemKindMap.get(carCompVo.getKindCode()).getRiskCode());
					carCompVo.setPolicyNo(itemKindMap.get(carCompVo.getKindCode()).getPolicyNo());
					carCompVo.setItemKindNo(itemKindMap.get(carCompVo.getKindCode()).getItemKindNo());
				}
				carCompVo.setLicenseNo(lossCarMainVo.getLossCarInfoVo().getLicenseNo());//车牌
				carCompVo.setRecycleFlag(lossFitInfoItem.getFitBackFlag());//回收标志
				if("1".equals(carCompVo.getRecycleFlag())){
					lossCarMainVo.setRecycleFlag("1");
				}else {
					lossCarMainVo.setRecycleFlag("0");
				}
				carCompVo.setManageSingleRate(NullToZero(lossFitInfoItem.getManageSingleRate()));//管理费率
				carCompVo.setManageSingleFee(NullToZero(lossFitInfoItem.getManageSingleFee()));//管理费
				carCompVo.setSelfPayRate(NullToZero(lossFitInfoItem.getSelfPayRate()));//自付比例
				carCompVo.setFirstMaterialFee(NullToZero(lossFitInfoItem.getEvalPartSumFirst()));//首次定损金额
				fitlist.add(carCompVo);
				serialNo ++;
			}
			lossCarMainVo.setPrpLDlossCarComps(fitlist);
		}
	}
	
	//修理信息
	private void organizeRepairInfo(List<LossRepairInfoItem> lossRepairInfoItems,List<LossOuterRepairInfoItem> lossOuterRepairInfoItems,PrpLDlossCarMainVo lossCarMainVo) {
		List<PrpLDlossCarRepairVo> repairList = new ArrayList<PrpLDlossCarRepairVo>();
		//内修
		int serialNo = 1;
		if(lossRepairInfoItems!=null){
			for (LossRepairInfoItem lossRepairInfoItem : lossRepairInfoItems) {
				PrpLDlossCarRepairVo repair = new PrpLDlossCarRepairVo();	
				repair.setRepairId(lossRepairInfoItem.getRepairId());//定损系统修理唯一ID RepairId
				repair.setSerialNo(serialNo);//增量id
				repair.setSumDefLoss(NullToZero(lossRepairInfoItem.getManpowerFee()));//修理金额 
				repair.setManHourFee(repair.getSumDefLoss());//工时费
				repair.setManHourUnitPrice(NullToZero(lossRepairInfoItem.getRepairUnitPrice()));//工时单价 
				repair.setManHour(NullToZero(lossRepairInfoItem.getEvalHour()));//维修工时 
				repair.setKindCode(lossRepairInfoItem.getItemCoverCode());//险别代码
				repair.setKindName(CodeTranUtil.transCode("RiskCode", lossRepairInfoItem.getItemCoverCode()));//险别名称
				repair.setRemark(lossRepairInfoItem.getRemark()); //备注
				repair.setSelfConfigFlag(lossRepairInfoItem.getSelfConfigFlag());//自定义修理件标记 
				repair.setRegistNo(lossCarMainVo.getRegistNo());//报案号
				if(StringUtils.isNotBlank(repair.getKindCode())){
					repair.setRiskCode(itemKindMap.get(repair.getKindCode()).getRiskCode());
					repair.setPolicyNo(itemKindMap.get(repair.getKindCode()).getPolicyNo());
					repair.setItemKindNo(itemKindMap.get(repair.getKindCode()).getItemKindNo());
				}
				repair.setLicenseNo(lossCarMainVo.getLicenseNo());//车牌
				repair.setCompCode(lossRepairInfoItem.getRepairModeCode());//维修项代码
				repair.setCompName(lossRepairInfoItem.getItemName());//维修项名称	
				repair.setRepairCode(lossRepairInfoItem.getRepairModeCode()); //工种代码
				repair.setRepairType(lossRepairInfoItem.getRepairModeCode()); //工种代码
				repair.setRepairName(this.getRepairName(lossRepairInfoItem.getRepairModeCode()));//工种名称
				if(StringUtils.isNotBlank(lossRepairInfoItem.getRepairLevelCode())){
					repair.setLevelCode(Integer.valueOf(lossRepairInfoItem.getRepairLevelCode()));//损失程度代码
				}	
				repair.setLevelName(lossRepairInfoItem.getRepairLevelName());//损失程度名称
				repair.setRepairFlag(RepairFlag.INNERREPAIR); //0-内修  1-外修
				repairList.add(repair);
				serialNo ++;
			}
		}
		
		//外修
		if(lossOuterRepairInfoItems!=null){
			for (LossOuterRepairInfoItem lossOuterRepairInfoItem : lossOuterRepairInfoItems) {
				PrpLDlossCarRepairVo outRepair = new PrpLDlossCarRepairVo();
				outRepair.setRepairId(lossOuterRepairInfoItem.getOuterId());//定损系统其他项目唯一ID OtherFeeId
				outRepair.setSerialNo(serialNo);//增量序列号码 SerialNo
				outRepair.setCompName(lossOuterRepairInfoItem.getOuterName());//外修项目名称
				outRepair.setMaterialFee(NullToZero(lossOuterRepairInfoItem.getPartPrice()));//配件金额
				outRepair.setMaterialQuantity(new BigDecimal(lossOuterRepairInfoItem.getPartAmount()));//外修配件数量
				outRepair.setRepairCode("");//工种编码 外修无
				outRepair.setRepairName("");//工种名称 外修无
				outRepair.setManHour(NullToZero(lossOuterRepairInfoItem.getOutItemAmount()));//外修数量
				outRepair.setManHourFee(NullToZero(lossOuterRepairInfoItem.getEvalOuterPirce()));//外修项目单价
				outRepair.setSumDefLoss(NullToZero(lossOuterRepairInfoItem.getRepairOuterSum()));//定损金额
				outRepair.setRepairType(""); //修理方式 外修无
				outRepair.setRepairFactoryCode(lossOuterRepairInfoItem.getRepairFactoryId());//外修修理厂Id
				outRepair.setRepairFactoryName(lossOuterRepairInfoItem.getRepairFactoryName());//外修修理厂名称
				outRepair.setSelfConfigFlag(lossOuterRepairInfoItem.getRepairHandaddFlag());//自定义标记
				outRepair.setKindCode(lossOuterRepairInfoItem.getItemCoverCode());//险别代码
				outRepair.setKindName(CodeTranUtil.transCode("RiskCode", lossOuterRepairInfoItem.getItemCoverCode()));//险别名称
				if(StringUtils.isNotBlank(outRepair.getKindCode())){
					outRepair.setRiskCode(itemKindMap.get(outRepair.getKindCode()).getRiskCode());
					outRepair.setPolicyNo(itemKindMap.get(outRepair.getKindCode()).getPolicyNo());
					outRepair.setItemKindNo(itemKindMap.get(outRepair.getKindCode()).getItemKindNo());
				}
				outRepair.setRemark(lossOuterRepairInfoItem.getRemark()); //备注
				outRepair.setRegistNo(lossCarMainVo.getRegistNo());//报案号
				outRepair.setLicenseNo(lossCarMainVo.getLicenseNo());//车牌
				outRepair.setRepairFlag(RepairFlag.OUTREPAIR); //0-内修  1-外修
				repairList.add(outRepair);
				serialNo ++;
			}
		}
		lossCarMainVo.setPrpLDlossCarRepairs(repairList);
	}
	
	//辅料
	private void organizeLossMaterialInfo(List<LossAssistInfoItem> lossAssistInfoItems,PrpLDlossCarMainVo lossCarMainVo) {
		List<PrpLDlossCarMaterialVo> assistList=new ArrayList<PrpLDlossCarMaterialVo>();
		if(lossAssistInfoItems!=null){
			int serialNo = 1;
			for (LossAssistInfoItem lossAssistInfoItem : lossAssistInfoItems) {
				PrpLDlossCarMaterialVo material = new PrpLDlossCarMaterialVo();		
				material.setAssistId(lossAssistInfoItem.getAssistId());//定损系统辅料唯一ID AssistId
				material.setSerialNo(serialNo);//增量序列号码 SerialNo
				material.setMaterialName(lossAssistInfoItem.getItemName());//辅料名称
				material.setUnitPrice(NullToZero(lossAssistInfoItem.getMaterialFee()));//辅料价格
				material.setAssisCount(NullToZero(lossAssistInfoItem.getCount()).intValue());//数量
				material.setMaterialFee(NullToZero(lossAssistInfoItem.getEvalMateSum()));//金额
				material.setKindCode(lossAssistInfoItem.getItemCoverCode());// 险别代码
				material.setKindName(CodeTranUtil.transCode("RiskCode", lossAssistInfoItem.getItemCoverCode()));// 险别名称
				if(StringUtils.isNotBlank(material.getKindCode())){
					material.setRiskCode(itemKindMap.get(material.getKindCode()).getRiskCode());
					material.setPolicyNo(itemKindMap.get(material.getKindCode()).getPolicyNo());
					material.setItemKindNo(itemKindMap.get(material.getKindCode()).getItemKindNo());
				}
				material.setRemark(lossAssistInfoItem.getRemark());//备注
				material.setSelfConfigFlag(lossAssistInfoItem.getSelfConfigFlag());//自定义辅料标记
				material.setRegistNo(lossCarMainVo.getRegistNo());//报案号
				material.setLicenseNo(lossCarMainVo.getLicenseNo());//车牌
				assistList.add(material);
				serialNo ++;
			}
		}
		lossCarMainVo.setPrpLDlossCarMaterials(assistList);
	}
	
	//工种合计的修理费用
	private void organizeRepairSumInfo(List<LossRepairSumInfoItem> lossRepairSumInfoItems, PrpLDlossCarMainVo lossCarMainVo){
		List<PrpLLossRepairSumInfoVo> prpLLossRepairSumInfoVos = new ArrayList<PrpLLossRepairSumInfoVo>();
		prpLLossRepairSumInfoVos = Beans.copyDepth().from(lossRepairSumInfoItems).toList(PrpLLossRepairSumInfoVo.class);
		lossCarMainVo.setPrpLLossRepairSumInfoVos(prpLLossRepairSumInfoVos);
	}
	
	//风险规则信息
	private void organizeRule(DlossRules dlossRules, PrpLDlossCarMainVo lossCarMainVo){
		PrplLossRuleVo prplLossRuleVo = new PrplLossRuleVo();
		if(dlossRules!=null){
			prplLossRuleVo = Beans.copyDepth().from(dlossRules).to(PrplLossRuleVo.class);
			lossCarMainVo.setPrplLossRuleVo(prplLossRuleVo);
		}
	}
	
	private void initService(){
		if(policyViewService == null){
			policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
		}
		if(lossCarService == null){
			lossCarService = (LossCarService)Springs.getBean(LossCarService.class);
		}
	}
	
	private String getMsgByCode(String code){
		String msg="";
		if( "000".equals(code)){
			msg = "成功";
		}
		if( "400".equals(code)){
			msg = "系统认证错误";
		}
		if( "401".equals(code)){
			msg = "数据包格式错误";
		}
		if( "402".equals(code)){
			msg = "数据完整性错误";
		}
		if( "403".equals(code)){
			msg = "请求类型错误";
		}
		if( "405".equals(code)){
			msg = "理赔系统因内部权限等各种原因，无法保存本次数据";
		}
		if( "408".equals(code)){
			msg = "安全验证错误";
		}
		if( "499".equals(code)){
			msg = "其它异常错误";
		}
		return msg;
	}
	
	private String getVehKindName(String vehKindCode){
		String vehKindName = "";
		if("A".equals(vehKindCode)){
			vehKindName = "客车类";
		}
		if("B".equals(vehKindCode)){
			vehKindName = "货车类";
		}
		if("C".equals(vehKindCode)){
			vehKindName = "轿车类";
		}
		if("D".equals(vehKindCode)){
			vehKindName = "其他车";
		}
		if("E".equals(vehKindCode)){
			vehKindName = "摩托";
		}
		if("F".equals(vehKindCode)){
			vehKindName = "挖掘机";
		}
		if("G".equals(vehKindCode)){
			vehKindName = "起重机";
		}
		return vehKindName;
	}
	
	private String getRepairName(String repairCode){
		String repairName = "";
		if("01".equals(repairCode)){
			repairName = "喷漆项目";
		}
		if("03".equals(repairCode)){
			repairName = "钣金项目";
		}
		if("04".equals(repairCode)){
			repairName = "电工项目";
		}
		if("05".equals(repairCode)){
			repairName = "机修项目";
		}
		if("06".equals(repairCode)){
			repairName = "拆装项目";
		}
		if("09".equals(repairCode)){
			repairName = "其他";
		}
		return repairName;
	}
	
	private static BigDecimal NullToZero(String strNum) {
		if(strNum==null||strNum.equals("")){
			strNum = "0";
		}
		return new BigDecimal(strNum);
	}
}
