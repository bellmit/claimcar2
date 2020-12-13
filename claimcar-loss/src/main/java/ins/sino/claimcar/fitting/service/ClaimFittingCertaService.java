/******************************************************************************
* CREATETIME : 2015年12月19日 下午4:57:03
* FILE       : ins.sino.claimcar.fitting.service.ClaimFittingCertaService
******************************************************************************/
package ins.sino.claimcar.fitting.service;

import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.fitting.util.OperXML;
import ins.sino.claimcar.losscar.service.CaseCompService;
import ins.sino.claimcar.losscar.service.DeflossService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * 理赔系统与精友配件系统交互，定损处理类
 * @author ★yangkun
 */
@Service("claimFittingCertaService")
public class ClaimFittingCertaService {
	
	@Autowired
	PolicyViewService policyViewService;
	
	@Autowired
	ClaimFittingSaveService fittingSaveService;
	
	@Autowired
	DeflossService deflossService;
	@Autowired
	CaseCompService caseCompService;
	@Autowired
	ClaimFittingInterService fittingService;
	
	private Logger logger = LoggerFactory.getLogger(ClaimFittingCertaService.class);
	private HashMap<String,PrpLCItemKindVo> itemKindMap = new HashMap<String,PrpLCItemKindVo>();
	
	/**
	 * 
	 * 解析定损返回接口数据
	 * @throws Exception
	 */
	public String doTransData(String iData) throws SQLException,Exception {
		OperXML operXML = new OperXML();
		logger.info("\n------定损接收----------\n "+iData);
		try{
			operXML.parserFromXMLString(iData);
			
			PrpLDlossCarMainVo lossCarMainVo = new PrpLDlossCarMainVo(); 
			PrpLDlossCarInfoVo carInfoVo = new PrpLDlossCarInfoVo();
			
			this.decodeLossInfo(lossCarMainVo,carInfoVo,operXML);
			
			lossCarMainVo.setLossCarInfoVo(carInfoVo);
			
			String registNo = lossCarMainVo.getRegistNo();
			String licenseNo=carInfoVo.getLicenseNo();
			
//			UIWorkFlowAction uiworkflowaction = new UIWorkFlowAction();
//			StringBuffer sb=new StringBuffer();
//			sb.append(" 1=1 and registno='").append(registNo).append("'");
//			sb.append(" and ( (lossitemcode='").append(lossItemCode).append("'").append(" and nodetype in ('certa','agcta')) ");;
//			sb.append(" or nodetype='check' )");
//			sb.append(" and nodestatus in ('0','1','2','3','8') ");
//			
//			int swfcount = uiworkflowaction.getCount(sb.toString());
//			if( swfcount==0){ 
//					return "498";//最新节点操作类型不为空，说明已经操作，不能再接收数据。
//			}		
			
			
			
			PrpLDlossCarMainVo carMain = deflossService.findDeflossByPk(lossCarMainVo.getId());
			PrpLDlossCarInfoVo carInfo = deflossService.findDefCarInfoByPk(carMain.getCarId());
			carMain.setLossCarInfoVo(carInfo);
			List<PrpLDlossCarCompVo> fitlist=this.decodeCompInfo(operXML,lossCarMainVo,carMain);
			List<PrpLDlossCarRepairVo> repairlist=this.decodeLossRepairInfo(operXML, registNo, licenseNo);
			List<PrpLDlossCarMaterialVo> assistlist=this.decodeLossMaterialInfo(operXML, registNo, licenseNo);
			//this.decodeLossOtherInfo(operXML, registNo, licenseNo,repairlist);
			
//			List <PrpLDlossCarRepairVo> repairList = carMain.getPrpLDlossCarRepairs();
//			List <PrpLDlossCarRepairVo> outRepairList = new ArrayList<PrpLDlossCarRepairVo>();
//			BigDecimal sumLoss = lossCarMainVo.getSumLossFee();
//			sumLoss = sumLoss.add(DataUtils.NullToZero(carMain.getSumOutFee()));
//			lossCarMainVo.setSumLossFee(sumLoss);
//			if(repairList!=null && !repairList.isEmpty()){
//				for(PrpLDlossCarRepairVo repairVo : repairList){
//					if(CodeConstants.RepairFlag.OUTREPAIR.equals(repairVo.getRepairFlag())){
//						outRepairList.add(repairVo);
//					}
//				}
//			}
//			repairlist.addAll(outRepairList);
			lossCarMainVo.setPrpLDlossCarComps(fitlist);
			lossCarMainVo.setPrpLDlossCarMaterials(assistlist);
			lossCarMainVo.setPrpLDlossCarRepairs(repairlist);
			lossCarMainVo.setRegistNo(registNo);
			lossCarMainVo.setFlag(CodeConstants.JyFlag.INBACK);//精友交互标志
			fittingSaveService.saveCertaFittings(lossCarMainVo,carInfoVo);

			return "000";
		}catch(SQLException exception){
			logger.error("定损数据异常"+exception.getMessage() , exception);
			exception.printStackTrace();
			return "499";

		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			ex.printStackTrace();
			return "401";
		}
	}
	
	/**
	 *  解析lossInfo 车型 信息
	 * @throws Exception 
	 * @throws JDOMException 
	 **/
	public void decodeLossInfo(PrpLDlossCarMainVo lossCarMainVo,PrpLDlossCarInfoVo carInfoVo,OperXML operXML) throws JDOMException, Exception{
		Element LossRtnBodyitem = operXML.getElement("LossRtnBody");
		Element LossInfo_Element = operXML.getElement(LossRtnBodyitem,"LossInfo");
			
		String registNo=operXML.getKeyValue(LossInfo_Element,"CaseNo");
		String lossCarId=operXML.getKeyValue(LossInfo_Element,"LossNo");

		List<PrpLCItemKindVo> itemKinds =  policyViewService.findItemKinds(registNo,null);
		for(PrpLCItemKindVo itemKindVo : itemKinds){
			itemKindMap.put(itemKindVo.getKindCode(),itemKindVo);
		}
		
		lossCarMainVo.setRegistNo(registNo);
		lossCarMainVo.setId(Long.parseLong(lossCarId));
		//carloss.setLossItemCode(lossItemCode);

		lossCarMainVo.setSumCompFee(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"SumChgCompFee"))));//合计换件金额     	SumChgCompFee
		lossCarMainVo.setSumRepairFee(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"SumRepairFee"))));//合计修理金额	SumRepairFee
		lossCarMainVo.setSumMatFee(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"SumMaterialFee"))));//合计辅料费	SumMaterialFee
//		lossCarMainVo.setSumOutFee(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"SumOtherFee"))));//合计其它金额	SumOtherFee
		lossCarMainVo.setSumRemnant(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"SumRemnant"))));//合计残值金额	SumRemnant
		BigDecimal sumLoss = new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"SumLossFee")));
		
		lossCarMainVo.setSumLossFee(sumLoss);//定损合计金额 +外修金额	SumLossFee
		logger.debug("报案号"+registNo+"残值金额 SumRemnant = "+operXML.getKeyValue(LossInfo_Element,"SumRemnant"));
		logger.debug("合计金额 SumLossFee = "+operXML.getKeyValue(LossInfo_Element,"SumLossFee"));
		logger.debug(lossCarMainVo.getSumRemnant().toString());
		logger.debug(lossCarMainVo.getSumLossFee().toString());
		
		carInfoVo.setLicenseNo(operXML.getKeyValue(LossInfo_Element,"PlateNo"));//车牌号码	PlateNo
		carInfoVo.setVehKindCode(operXML.getKeyValue(LossInfo_Element,"VehKindCode"));//定损车辆种类代码	VehKindCode
		carInfoVo.setVehKindName(operXML.getKeyValue(LossInfo_Element,"VehKindName"));//定损车辆种类名称	VehKindName
		carInfoVo.setModelCode(operXML.getKeyValue(LossInfo_Element,"VehCertainCode"));//定损车型编码	VehCertainCode
		carInfoVo.setModelName(operXML.getKeyValue(LossInfo_Element,"VehCertainName"));//定损车型名称	VehCertainName
		carInfoVo.setGroupName(operXML.getKeyValue(LossInfo_Element,"VehGroupName"));//定损车组名称	VehGroupName
		carInfoVo.setGroupCode(operXML.getKeyValue(LossInfo_Element,"VehGroupCode"));//定损车组代码	VehGroupCode
		carInfoVo.setSeriCode(operXML.getKeyValue(LossInfo_Element,"VehSeriCode"));//定损车系编码	VehSeriCode
		carInfoVo.setSeriName(operXML.getKeyValue(LossInfo_Element,"VehSeriName"));//定损车系名称	VehSeriName
		carInfoVo.setYearType(operXML.getKeyValue(LossInfo_Element,"VehYearType"));//年款	VehYearType
		carInfoVo.setFactoryCode(operXML.getKeyValue(LossInfo_Element,"VehFactoryCode"));//厂家编码	VehFactoryCode
		carInfoVo.setFactoryName(operXML.getKeyValue(LossInfo_Element,"VehFactoryName"));//厂家名称	VehFactoryName
		carInfoVo.setBrandCode(operXML.getKeyValue(LossInfo_Element,"VehBrandCode"));//品牌编码	VehBrandCode
		carInfoVo.setBrandName(operXML.getKeyValue(LossInfo_Element,"VehBrandName"));//品牌名称	VehBrandName
		carInfoVo.setSelfConfigFlag(operXML.getKeyValue(LossInfo_Element,"SelfConfigFlag"));//自定义车型标志	SelfConfigFlag
	}
	
	/**
	 *  解析配件信息 LossFitInfo
	 *  lizy
	 *  2012-12-04
	 * @throws Exception 
	 * @throws JDOMException 
	 * */
	public List<PrpLDlossCarCompVo> decodeCompInfo(OperXML operXML,PrpLDlossCarMainVo lossCarMainVo,PrpLDlossCarMainVo carMain) throws JDOMException, Exception{
		List<PrpLDlossCarCompVo> fitlist=new ArrayList<PrpLDlossCarCompVo>();
			
		Element LossRtnBodyitem = operXML.getElement("LossRtnBody");
		Element LossRtnBody_LossFitInfoitem = operXML.getElement(LossRtnBodyitem,"LossFitInfo");
		
		int count = operXML.getCountByTag(LossRtnBody_LossFitInfoitem,"Item");
		//查询获得车架号
		//PrpLDlossCarMainVo carMain = deflossService.findDeflossByPk(lossCarMainVo.getId());
		PrpLDlossCarInfoVo carInfo = carMain.getLossCarInfoVo();
		lossCarMainVo.getLossCarInfoVo().setId(carMain.getCarId());
		Boolean recycleFlag =false;//损余回收标志
		
		String kindCode_X ="";//涉水险
		if(itemKindMap.containsKey(CodeConstants.KINDCODE.KINDCODE_X1)){
			kindCode_X = CodeConstants.KINDCODE.KINDCODE_X1;
		}else if(itemKindMap.containsKey(CodeConstants.KINDCODE.KINDCODE_X2)){
			kindCode_X = CodeConstants.KINDCODE.KINDCODE_X2;
		}
		
		for(int i = 0; i<count; i++ ){
			Element itemnu = operXML.getElement(LossRtnBody_LossFitInfoitem,"Item",i);
			PrpLDlossCarCompVo carCompVo=new PrpLDlossCarCompVo();
			carCompVo.setIndId(operXML.getKeyValue(itemnu,"PartId"));//定损系统零件唯一ID
			carCompVo.setSerialNo(Integer.parseInt(( NullToZero(operXML.getKeyValue(itemnu,"SerialNo")))));//鼎和估损单唯一序号
			carCompVo.setOriginalId(operXML.getKeyValue(itemnu,"OriginalId"));//零配件原厂编码
			
			carCompVo.setCompCode(operXML.getKeyValue(itemnu,"PartStandardCode"));// 配件标准代码
			if( carCompVo.getCompCode()==null || "".equals(carCompVo.getCompCode())){
				carCompVo.setCompCode("00");//自定义配件
			}
			carCompVo.setCompName(operXML.getKeyValue(itemnu,"PartStandard"));// 配件标准名称
			
			carCompVo.setMaterialFee(new BigDecimal(NullToZero(operXML.getKeyValue(itemnu,"LossPrice"))));// 定损报价
			carCompVo.setRepairFactoryFee(new BigDecimal(NullToZero(operXML.getKeyValue(itemnu,"RepairSitePrice"))));// 修理厂
			carCompVo.setQuantity(new Double(NullToZero(operXML.getKeyValue(itemnu,"LossCount"))).intValue());//换件数量
			carCompVo.setSumDefLoss(new BigDecimal(NullToZero(operXML.getKeyValue(itemnu,"SumPrice"))));// 换件金额小计 352.44  467.8
			carCompVo.setOriginalName(operXML.getKeyValue(itemnu,"OriginalName"));//零配件原厂名称	OriginalName	VARCHAR(100)
			carCompVo.setPartGroupCode(operXML.getKeyValue(itemnu,"PartGroupCode"));//配件部位代码	PartGroupCode	VARCHAR(60)
			carCompVo.setPartGroupName(operXML.getKeyValue(itemnu,"PartGroupName"));//配件部位名称	PartGroupName	VARCHAR(60)
			carCompVo.setSelfConfigFlag(operXML.getKeyValue(itemnu,"SelfConfigFlag"));//自定义配件标记	SelfConfigFlag	VARCHAR (2)
			
			carCompVo.setDirectSupplyFlag(operXML.getKeyValue(itemnu, "DirectSupplyFlag"));  //解析配件直供标志
			
			// 2010.4.27 在获取到配件信息后，保存到数据库，已便如果用户不保存下次打开页面时还有配件信息
			String compSetCode = operXML.getKeyValue(itemnu,"ChgCompSetCode");
			if(lossCarMainVo.getRepairFactoryType() == null 
				|| !compSetCode.equals(lossCarMainVo.getRepairFactoryType()) ){
				lossCarMainVo.setRepairFactoryType(compSetCode);
			}
			carCompVo.setPriceType(compSetCode);//价格方案
			carCompVo.setSys4SPrice(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"RefPrice1"))));// 价格方案系统价格 系统专修价
			carCompVo.setSysMarketPrice(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"RefPrice2"))));// 价格方案本地价格 系统市场价
			carCompVo.setSysMatchPrice(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"RefPrice3"))));//系统副厂价
			carCompVo.setChgRefPrice(new BigDecimal(NullToZero(operXML.getKeyValue(itemnu, "ChgRefPrice")))); //价格方案系统价格
			carCompVo.setChgLocPrice(new BigDecimal(NullToZero(operXML.getKeyValue(itemnu, "ChgLocPrice")))); //价格方案本地价格
			
			carCompVo.setNative4SPrice(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"LocPrice1"))));//本地专修价
			carCompVo.setNativeMarketPrice(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"LocPrice2"))));//本地市场价
			carCompVo.setNativeMatchPrice(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"LocPrice3"))));//本地副厂价
			carCompVo.setRestFee(new BigDecimal(( NullToZero(operXML.getKeyValue(itemnu,"Remnant")) )));// 残值
//			carCompVo.setKindCode(operXML.getKeyValue(itemnu,"InsureTermCode"));// 险别代码 
//			carCompVo.setKindName(operXML.getKeyValue(itemnu,"InsureTerm"));// 险别名称
			carCompVo.setRemark(operXML.getKeyValue(itemnu,"Remark")); //备注
			String wadFlag = operXML.getKeyValue(itemnu,"Wading");// 涉水标识 0否 1是
			if("1".equals(wadFlag) && StringUtils.isNotBlank(kindCode_X)){
				carCompVo.setWadFlag(wadFlag);
				carCompVo.setKindCode(kindCode_X);// 险别代码 
				carCompVo.setKindName(itemKindMap.get(kindCode_X).getKindName());// 险别名称
				
			}else{
				carCompVo.setWadFlag("0");
			}
			
		
			carCompVo.setRegistNo(lossCarMainVo.getRegistNo());//报案号
//				carCompVo.setLossItemCode(lossItemCode);//损失项目号
			if(StringUtils.isNotBlank(carCompVo.getKindCode())){
				carCompVo.setRiskCode(itemKindMap.get(carCompVo.getKindCode()).getRiskCode());
				carCompVo.setPolicyNo(itemKindMap.get(carCompVo.getKindCode()).getPolicyNo());
				carCompVo.setItemKindNo(itemKindMap.get(carCompVo.getKindCode()).getItemKindNo());
			}
			carCompVo.setLicenseNo(carInfo.getLicenseNo());//车牌
			carCompVo.setRecycleFlag(operXML.getKeyValue(itemnu,"IfRemain"));//是否损余
			if("1".equals(carCompVo.getRecycleFlag())){
				recycleFlag = true;
			}
			
			carCompVo.setFlag(operXML.getKeyValue(itemnu,"Status"));//新增或修改标志 A新增 U修改
			
			Long replaceNum =caseCompService.countComp(carCompVo.getCompCode(),carInfo.getFrameNo());
			carCompVo.setReplaceNum(replaceNum.intValue());
			
			fitlist.add(carCompVo);
		}
		
		if(recycleFlag){
			lossCarMainVo.setRecycleFlag("1");
		}else{
			lossCarMainVo.setRecycleFlag("0");
		}
		
		return fitlist;
	}
	
	/**
	 *  解析lossRepairInfo 工时信息
	 * @throws Exception 
	 * @throws JDOMException 
	 **/
	public List<PrpLDlossCarRepairVo> decodeLossRepairInfo(OperXML operXML,String registNo ,String lcnNo) throws JDOMException, Exception{
		List<PrpLDlossCarRepairVo> repairList=new ArrayList<PrpLDlossCarRepairVo>();
		
		Element LossRtnBodyitem = operXML.getElement("LossRtnBody");
		Element LossRtnBody_LossRepairInfoitem = operXML.getElement(LossRtnBodyitem,"LossRepairInfo");
		
		int count = operXML.getCountByTag(LossRtnBody_LossRepairInfoitem,"Item");
		for(int i = 0; i<count; i++ ){
			Element itemnu = operXML.getElement(LossRtnBody_LossRepairInfoitem,"Item",i);
			PrpLDlossCarRepairVo repair=new PrpLDlossCarRepairVo();
			repair.setRepairId(operXML.getKeyValue(itemnu,"RepairId"));	//定损系统修理唯一ID RepairId
			repair.setSerialNo(Integer.parseInt(( NullToZero(operXML.getKeyValue(itemnu,"SerialNo")))));//增量id
			repair.setSumDefLoss(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"RepairFee"))));//修理金额 
			repair.setManHourFee(repair.getSumDefLoss());
			repair.setManHourUnitPrice(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"UnitManHourPrice"))));//工时费率 
			repair.setManHour(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"ManHour"))));//维修工时 
			repair.setKindCode(operXML.getKeyValue(itemnu,"InsureTermCode"));// 险别代码
			repair.setKindName(operXML.getKeyValue(itemnu,"InsureTerm"));// 险别名称
			repair.setRemark(operXML.getKeyValue(itemnu,"Remark")); //备注
			repair.setSelfConfigFlag(operXML.getKeyValue(itemnu,"SelfConfigFlag"));//自定义修理件标记 
			repair.setRegistNo(registNo);//报案号
//			repair.setLossItemCode(lossItemCode);//损失项目号
			if(StringUtils.isNotBlank(repair.getKindCode())){
				repair.setRiskCode(itemKindMap.get(repair.getKindCode()).getRiskCode());
				repair.setPolicyNo(itemKindMap.get(repair.getKindCode()).getPolicyNo());
			}
			repair.setLicenseNo(lcnNo);//车牌
			repair.setCompCode(operXML.getKeyValue(itemnu,"RepairItemCode"));//维修项代码	
			if( repair.getCompCode()==null || "".equals(repair.getCompCode())){//自定义工时处理
				repair.setCompCode("00");
			}
			repair.setCompName(operXML.getKeyValue(itemnu,"RepairItemName"));//维修项名称	
			repair.setRepairCode(operXML.getKeyValue(itemnu,"RepairCode"));  //工种代码	
			repair.setRepairName(operXML.getKeyValue(itemnu,"RepairName"));  //工种名称	
			repair.setLevelCode(Integer.parseInt(NullToZero( operXML.getKeyValue(itemnu,"LevelCode"))));//修理项目受损程度编号	
			repair.setLevelName(operXML.getKeyValue(itemnu,"LevelName"));//修理项目受损程度名称
			if(StringUtils.isNotBlank(repair.getKindCode())){
				repair.setItemKindNo(itemKindMap.get(repair.getKindCode()).getItemKindNo());
			}
			repair.setRepairFlag(CodeConstants.RepairFlag.INNERREPAIR); //0-内修  1-外修
		
			repairList.add(repair);
		}
		return repairList;
	}
	
	/**
	 *  解析LossAssistInfo 辅料信息
	 * @throws Exception 
	 * @throws JDOMException 
	 **/
	public List<PrpLDlossCarMaterialVo> decodeLossMaterialInfo(OperXML operXML,String registNo ,String lcnNo) throws JDOMException, Exception{
		List<PrpLDlossCarMaterialVo> assistList=new ArrayList<PrpLDlossCarMaterialVo>();
		
		Element LossRtnBodyitem = operXML.getElement("LossRtnBody");
		Element LossRtnBody_LossRepairInfoitem = operXML.getElement(LossRtnBodyitem,"LossAssistInfo");
		
		int count = operXML.getCountByTag(LossRtnBody_LossRepairInfoitem,"Item");
		for(int i = 0; i<count; i++ ){
			Element itemnu = operXML.getElement(LossRtnBody_LossRepairInfoitem,"Item",i);
			PrpLDlossCarMaterialVo material = new PrpLDlossCarMaterialVo();
			
			material.setAssistId(operXML.getKeyValue(itemnu,"AssistId"));	//定损系统辅料唯一ID AssistId
			material.setSerialNo(Integer.parseInt((NullToZero(operXML.getKeyValue(itemnu,"SerialNo")))));//增量序列号码 SerialNo
			material.setMaterialName(operXML.getKeyValue(itemnu,"MaterialName"));//名称	MaterialName
			material.setUnitPrice(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"UnitPrice"))));//单价	UnitPrice
			material.setAssisCount(new Double(NullToZero(operXML.getKeyValue(itemnu,"Count"))).intValue());//数量	Count
			material.setMaterialFee(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"MaterialFee"))));//金额	MaterialFee
				
			material.setKindCode(operXML.getKeyValue(itemnu,"InsureTermCode"));// 险别代码
			material.setKindName(operXML.getKeyValue(itemnu,"InsureTerm"));// 险别名称
			material.setRemark(operXML.getKeyValue(itemnu,"Remark")); //备注
			material.setSelfConfigFlag(operXML.getKeyValue(itemnu,"SelfConfigFlag"));//自定义修理件标记 SelfConfigFlag
			material.setRegistNo(registNo);//报案号
			material.setLicenseNo(lcnNo);//车牌
			if(StringUtils.isNotBlank(material.getKindCode())){
				material.setRiskCode(itemKindMap.get(material.getKindCode()).getRiskCode());
				material.setPolicyNo(itemKindMap.get(material.getKindCode()).getPolicyNo());
				material.setItemKindNo(itemKindMap.get(material.getKindCode()).getItemKindNo());
			}
			material.setFlag(operXML.getKeyValue(itemnu,"Status"));//新增或修改标志 A新增 U修改
			assistList.add(material);
		}
		return assistList;
	}
	
	/**
	 *  解析LossOtherInfo 其它信息
	 * @throws Exception 
	 * @throws JDOMException 
	 **/
	public void decodeLossOtherInfo(OperXML operXML,String registNo,String lcnNo,List<PrpLDlossCarRepairVo> repairlist) throws JDOMException, Exception{
		Element LossRtnBodyitem = operXML.getElement("LossRtnBody");
		Element LossRtnBody_LossRepairInfoitem = operXML.getElement(LossRtnBodyitem,"LossOtherInfo");
		
		int count = operXML.getCountByTag(LossRtnBody_LossRepairInfoitem,"Item");
		for(int i = 0; i<count; i++ ){
			Element itemnu = operXML.getElement(LossRtnBody_LossRepairInfoitem,"Item",i);
			PrpLDlossCarRepairVo outRepair=new PrpLDlossCarRepairVo();
			outRepair.setRepairId(operXML.getKeyValue(itemnu,"OtherFeeId"));	//定损系统其他项目唯一ID OtherFeeId
			outRepair.setSerialNo(Integer.parseInt(( NullToZero(operXML.getKeyValue(itemnu,"SerialNo")))));//增量序列号码 SerialNo
			outRepair.setCompName(operXML.getKeyValue(itemnu,"OtherFeeName"));//其他费用项目名称	OtherFeeName
			outRepair.setSumDefLoss(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"OtherFee"))));//金额	OtherFee
				
			outRepair.setKindCode(operXML.getKeyValue(itemnu,"InsureTermCode"));// 险别代码
			outRepair.setKindName(operXML.getKeyValue(itemnu,"InsureTerm"));// 险别名称
			outRepair.setRemark(operXML.getKeyValue(itemnu,"Remark")); //备注
			outRepair.setRegistNo(registNo);//报案号
			outRepair.setLicenseNo(lcnNo);//车牌
//			outRepair.setLossItemCode(lossItemCode);//损失项目号
			if(StringUtils.isNotBlank(outRepair.getKindCode())){
				outRepair.setRiskCode(itemKindMap.get(outRepair.getKindCode()).getRiskCode());
				outRepair.setPolicyNo(itemKindMap.get(outRepair.getKindCode()).getPolicyNo());
				outRepair.setItemKindNo(itemKindMap.get(outRepair.getKindCode()).getItemKindNo());
			}
//			outRepair.setVeriSumLoss(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"AuditLossAmount"))));//金额	AuditLossAmount
//			outRepair.setVeriRemark(operXML.getKeyValue(itemnu,"AuditLossRemark"));//核损说明	
			outRepair.setRepairFlag(CodeConstants.RepairFlag.OUTREPAIR); //0-内修  1-外修
			
			repairlist.add(outRepair);
		}
	}
	
	private static String NullToZero(String strNum) {
		if(strNum==null||strNum.equals("")){
			return "0";
		}
		return strNum;

	}
	
}
