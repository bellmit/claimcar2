/******************************************************************************
* CREATETIME : 2015年12月19日 下午4:57:43
* FILE       : ins.sino.claimcar.fitting.service.ClaimFittingCheckLossService
******************************************************************************/
package ins.sino.claimcar.fitting.service;

import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.fitting.util.OperXML;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <pre></pre>
 * @author ★yangkun
 */
@Service("claimFittingCheckLossService")
public class ClaimFittingCheckLossService {

	@Autowired
	ClaimFittingSaveService fittingSaveService;
	@Autowired
	ClaimFittingInterService fittingService;

	private Logger logger = LoggerFactory.getLogger(ClaimFittingCheckLossService.class);

	/**
	 * parse XML
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param iData
	 *            String
	 * @throws Exception
	 */
	public String doTransData(String iData) throws Exception {

		OperXML operXML = new OperXML();
		logger.info("\n------核损接收----------\n "+iData);
		try{
			operXML.parserFromXMLString(iData);
			
			PrpLDlossCarMainVo lossCarMainVo = new PrpLDlossCarMainVo(); 
			this.decodeLossInfo(operXML,lossCarMainVo);
			//保存日志
			//fittingService.saveJingYouLog(lossCarMainVo,iData,"verifyLoss");
			List<PrpLDlossCarCompVo> compList=this.decodeCompInfo(operXML,lossCarMainVo);
			List<PrpLDlossCarMaterialVo> materialList=this.decodeLossMaterialInfo(operXML);
			List<PrpLDlossCarRepairVo> repairList=this.decodeLossRepairInfo(operXML);
//			this.decodeLossOutRepair(operXML, repairList);
			
			lossCarMainVo.setPrpLDlossCarComps(compList);
			lossCarMainVo.setPrpLDlossCarMaterials(materialList);
			lossCarMainVo.setPrpLDlossCarRepairs(repairList);
			lossCarMainVo.setFlag(CodeConstants.JyFlag.INBACK);//精友交互标志
			
			fittingSaveService.saveCheckLossFittings(lossCarMainVo);
			

			return "000";
		}catch(SQLException exception){
			//exception.printStackTrace();
			logger.error(exception.getMessage(),exception);
			return "499";

		}catch(Exception ex){
			logger.error(ex.getMessage(),ex);
			ex.printStackTrace();
			return "401";
		}
	
	}
	
	
	/**
	 *  解析lossInfo 车型 信息
	 * @throws Exception 
	 * @throws JDOMException 
	 **/
	public void decodeLossInfo(OperXML operXML,PrpLDlossCarMainVo carloss) throws JDOMException, Exception{
		//operXML.parserFromXMLString(iData);
		Element LossRtnBodyitem = operXML.getElement("LossRtnBody");
		Element LossInfo_Element = operXML.getElement(LossRtnBodyitem,"LossInfo");
		
		String registNo=operXML.getKeyValue(LossInfo_Element,"CaseNo");
		String LossNo=operXML.getKeyValue(LossInfo_Element,"LossNo");

		carloss.setRegistNo(registNo);
		carloss.setId(Long.parseLong(LossNo));
		
		carloss.setSumVeriCompFee(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"AuditLossSumChgCompFee"))));  //核损合计换件金额   AuditSumChgCompFee
		carloss.setSumVeriRepairFee(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"AuditLossSumRepairFee"))));     //核损合计修理金额	AuditSumRepairFee
		carloss.setSumVeriMatFee(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"AuditLossSumMaterialFee")))); //核损合计辅料金额	AuditSumMaterialFee
//		carloss.setSumVeriOutFee(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"AuditLossSumOtherFee"))));    //核损合计其它金额	AuditSumOtherFee
		carloss.setSumVeriRemnant(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"AuditLossSumRemnant"))));     //核损合计残值金额	AuditSumRemnant
		
		BigDecimal sumVeriLoss =new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"AuditLossSumLossFee")));
//		sumVeriLoss = sumVeriLoss.add(DataUtils.NullToZero(carloss.getSumVeriOutFee()));
		carloss.setSumVeriLossFee(sumVeriLoss);     //核损总金额(精友)+外修金额	    AuditSumLossFee
		carloss.setRemark(operXML.getKeyValue(LossInfo_Element,"AuditLossRemark"));

	}
	
	/**
	 *  解析配件信息 LossFitInfo
	 *  lizy
	 *  2012-12-04
	 * @throws Exception 
	 * @throws JDOMException 
	 * */
	public  List<PrpLDlossCarCompVo> decodeCompInfo(OperXML operXML,PrpLDlossCarMainVo lossCarMainVo) throws JDOMException, Exception{
		    List<PrpLDlossCarCompVo> fitlist=new ArrayList<PrpLDlossCarCompVo>();
	
			Element LossRtnBodyitem = operXML.getElement("LossRtnBody");
			Element LossRtnBody_LossFitInfoitem = operXML.getElement(LossRtnBodyitem,"LossFitInfo");
			Boolean reCheckFlag =false;//复检标志
			Boolean recycleFlag =false;//损余回收标志
			int count = operXML.getCountByTag(LossRtnBody_LossFitInfoitem,"Item");
			for(int i = 0; i<count; i++ ){
				Element itemnu = operXML.getElement(LossRtnBody_LossFitInfoitem,"Item",i);
				PrpLDlossCarCompVo compDto=new PrpLDlossCarCompVo();
				
				compDto.setIndId(operXML.getKeyValue(itemnu,"PartId"));//定损系统零件唯一ID	
				compDto.setVeriMaterFee(new BigDecimal(NullToZero(operXML.getKeyValue(itemnu,"AuditLossPrice"))));// 核损价格
				compDto.setVeriRestFee(new BigDecimal(( NullToZero( operXML.getKeyValue(itemnu,"AuditLossRemnant")) )));// 核损残值
				compDto.setVeriQuantity(new Double( NullToZero(operXML.getKeyValue(itemnu,"AuditLossCount"))).intValue());//核损换件数量
//				prpLcomponentDto.setVeriQuantity(new Double( NullToZero(operXML.getKeyValue(itemnu,"AuditLossCount"))).intValue());//核损换件数量
				
				compDto.setVeriRemark(operXML.getKeyValue(itemnu,"AuditLossRemark")); //备注
				compDto.setSumVeriLoss(new BigDecimal(( NullToZero( operXML.getKeyValue(itemnu,"AuditLossSumPrice")) )));// 核损小计
				compDto.setReCheckFlag(operXML.getKeyValue(itemnu,"Recheck"));
				if("1".equals(compDto.getReCheckFlag())){
					reCheckFlag = true;
				}
				
				compDto.setRecycleFlag(operXML.getKeyValue(itemnu,"IfRemain"));//是否损余
				if("1".equals(compDto.getRecycleFlag())){
					recycleFlag = true;
				}
				
				fitlist.add(compDto);
			}
			if(reCheckFlag){
				lossCarMainVo.setReCheckFlag("1");
			}else{
				lossCarMainVo.setReCheckFlag("0");
			}
			//核损也能选择损余标识
			if(recycleFlag){
				lossCarMainVo.setRecycleFlag("1");
			}else{
				lossCarMainVo.setRecycleFlag("0");
			}
			
			return fitlist;
	}
	
	/**
	 *  解析lossRepairInfo 工时信息
	 * 
	 * @throws Exception 
	 * @throws JDOMException 
	 **/
	public List<PrpLDlossCarRepairVo> decodeLossRepairInfo(OperXML operXML) throws JDOMException, Exception{
		List<PrpLDlossCarRepairVo> repairList=new ArrayList<PrpLDlossCarRepairVo>();
		
		Element LossRtnBodyitem = operXML.getElement("LossRtnBody");
		Element LossRtnBody_LossRepairInfoitem = operXML.getElement(LossRtnBodyitem,"LossRepairInfo");
		
		int count = operXML.getCountByTag(LossRtnBody_LossRepairInfoitem,"Item");
		for(int i = 0; i<count; i++ ){
			Element itemnu = operXML.getElement(LossRtnBody_LossRepairInfoitem,"Item",i);
			PrpLDlossCarRepairVo repair=new PrpLDlossCarRepairVo();
			repair.setRepairId(operXML.getKeyValue(itemnu,"RepairId"));	//定损系统修理唯一ID RepairId
			repair.setSumVeriLoss(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"AuditLossPrice"))));//修理金额 AuditRepairFee
			repair.setVeriManHourFee(repair.getSumVeriLoss());
			repair.setVeriManUnitPrice(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"AuditLossManHourPrice"))));//工时费率 AuditManHourPrice
			repair.setVeriManHour(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"AuditLossManHour")))); //AuditLossManHour
			repair.setVeriRemark(operXML.getKeyValue(itemnu,"AuditLossRemark"));
			repair.setRepairFlag(CodeConstants.RepairFlag.INNERREPAIR);
			
			repairList.add(repair);
		}
		
		return repairList;
	}
	
	/**
	 *  解析LossAssistInfo 辅料信息
	 * @throws Exception 
	 * @throws JDOMException 
	 **/
	public List<PrpLDlossCarMaterialVo> decodeLossMaterialInfo(OperXML operXML) throws JDOMException, Exception{
		List<PrpLDlossCarMaterialVo> assistList=new ArrayList<PrpLDlossCarMaterialVo>();
		
		Element LossRtnBodyitem = operXML.getElement("LossRtnBody");
		Element LossRtnBody_LossRepairInfoitem = operXML.getElement(LossRtnBodyitem,"LossAssistInfo");
		
		int count = operXML.getCountByTag(LossRtnBody_LossRepairInfoitem,"Item");
		for(int i = 0; i<count; i++ ){
			Element itemnu = operXML.getElement(LossRtnBody_LossRepairInfoitem,"Item",i);
			PrpLDlossCarMaterialVo assist=new PrpLDlossCarMaterialVo();
			
			assist.setAssistId(operXML.getKeyValue(itemnu,"AssistId"));	//定损系统辅料唯一ID AssistId
			assist.setAuditLossPrice(new BigDecimal(NullToZero(operXML.getKeyValue(itemnu,"AuditLossPrice"))));//单价	AuditLossPrice
			assist.setAuditLossMaterialFee(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"AuditLossMaterialFee"))));//金额	AuditLossMaterialFee
			assist.setAuditLossRemark(operXML.getKeyValue(itemnu,"AuditLossRemark"));//核损备注
			assist.setAuditLossCount(new Double( NullToZero(operXML.getKeyValue(itemnu,"AuditLossCount"))).intValue());//核损数量
			assistList.add(assist);
		}
		return assistList;
	}
	
	/**
	 *  解析LossOtherInfo 其它信息
	 * @throws Exception 
	 * @throws JDOMException 
	 **/
	public void decodeLossOutRepair(OperXML operXML,List<PrpLDlossCarRepairVo> repairlist) throws JDOMException, Exception{
		
		Element LossRtnBodyitem = operXML.getElement("LossRtnBody");
		Element LossRtnBody_LossRepairInfoitem = operXML.getElement(LossRtnBodyitem,"LossOtherInfo");
		int count = operXML.getCountByTag(LossRtnBody_LossRepairInfoitem,"Item");
		
		for(int i = 0; i<count; i++ ){
			Element itemnu = operXML.getElement(LossRtnBody_LossRepairInfoitem,"Item",i);
			PrpLDlossCarRepairVo outRepair=new PrpLDlossCarRepairVo();
			outRepair.setRepairId(operXML.getKeyValue(itemnu,"OtherFeeId"));	//定损系统其他项目唯一ID OtherFeeId
			outRepair.setSumVeriLoss(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"AuditLossAmount"))));//金额	AuditLossAmount
			outRepair.setVeriRemark(operXML.getKeyValue(itemnu,"AuditLossRemark"));//核损说明		
			
			outRepair.setRepairFlag(CodeConstants.RepairFlag.OUTREPAIR);
			
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
