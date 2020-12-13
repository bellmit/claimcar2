/******************************************************************************
* CREATETIME : 2015年12月19日 下午4:57:24
* FILE       : ins.sino.claimcar.fitting.service.ClaimFittingCheckPriceService
******************************************************************************/
package ins.sino.claimcar.fitting.service;

import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.fitting.util.OperXML;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;

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
 * 理赔系统与精友配件系统交互，核价处理类
 * @author ★yangkun
 */
@Service("claimFittingCheckPriceService")
public class ClaimFittingCheckPriceService {

	@Autowired
	ClaimFittingSaveService fittingSaveService;
	@Autowired
	ClaimFittingInterService fittingService;

	private Logger logger = LoggerFactory.getLogger(ClaimFittingCheckPriceService.class);
	
	/**
	 * 解析核价返回接口数据
	 * @throws Exception
	 */
	public String doTransData(String iData) throws Exception {

		OperXML operXML = new OperXML();
		logger.info("\n------核价接收----------\n "+iData);
		try{
			operXML.parserFromXMLString(iData);
			
			PrpLDlossCarMainVo lossCarMainVo = new PrpLDlossCarMainVo(); 
			this.decodeLossInfo(operXML,lossCarMainVo);
			//fittingService.saveJingYouLog(lossCarMainVo,iData,"verifyPrice");
			List<PrpLDlossCarCompVo> fitlist=this.decodeFitInfo(operXML);
			List<PrpLDlossCarMaterialVo> assistlist=this.decodeLossAssistInfo(operXML);
			
			lossCarMainVo.setPrpLDlossCarComps(fitlist);
			lossCarMainVo.setPrpLDlossCarMaterials(assistlist);
			lossCarMainVo.setFlag(CodeConstants.JyFlag.INBACK);//精友交互标志
			
			fittingSaveService.saveCheckPriceFittings(lossCarMainVo);
			

			return "000";
		}catch(SQLException exception){
			logger.error("错误:",exception);
			return "499";

		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("错误:",ex);
			return "401";
		}
	}
	
	/**
	 *  解析lossInfo 车型 信息
	 * @throws Exception 
	 * @throws JDOMException 
	 **/
	public void decodeLossInfo(OperXML operXML,PrpLDlossCarMainVo carloss) throws JDOMException, Exception{
		Element LossRtnBodyitem = operXML.getElement("LossRtnBody");
		Element LossInfo_Element = operXML.getElement(LossRtnBodyitem,"LossInfo");
		
		String registNo=operXML.getKeyValue(LossInfo_Element,"CaseNo");
		String LossNo=operXML.getKeyValue(LossInfo_Element,"LossNo");

		carloss.setRegistNo(registNo);
		carloss.setId(Long.parseLong(LossNo));
		carloss.setSumVeripCompFee(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"AuditSumChgCompFee"))));  //核价合计换件金额   AuditSumChgCompFee
		carloss.setSumVeripMatFee(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"AuditSumMaterialFee")))); //核价合计辅料金额	AuditSumMaterialFee
//		carloss.setAuditSumLossFee(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"AuditSumRepairFee"))));     //核价合计修理金额	AuditSumRepairFee
//		carloss.setAuditSumOtherFee(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"AuditSumOtherFee"))));    //核价合计其它金额	AuditSumOtherFee
		carloss.setSumVeripRemnant(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"AuditSumRemnant"))));     //核价合计残值金额	AuditSumRemnant
		carloss.setSumVeripLoss(new BigDecimal(NullToZero(operXML.getKeyValue(LossInfo_Element,"AuditSumLossFee"))));     //核价总金额	    AuditSumLossFee
		carloss.setRemark(operXML.getKeyValue(LossInfo_Element,"AuditRemark"));

	}
	
	/**
	 *  解析配件信息 LossFitInfo
	 * @throws Exception 
	 * @throws JDOMException 
	 * */
	public  List<PrpLDlossCarCompVo> decodeFitInfo(OperXML operXML) throws JDOMException, Exception{
		    List<PrpLDlossCarCompVo> fitlist=new ArrayList<PrpLDlossCarCompVo>();
	
			Element LossRtnBodyitem = operXML.getElement("LossRtnBody");
			Element LossRtnBody_LossFitInfoitem = operXML.getElement(LossRtnBodyitem,"LossFitInfo");
			
			int count = operXML.getCountByTag(LossRtnBody_LossFitInfoitem,"Item");
			for(int i = 0; i<count; i++ ){
				Element itemnu = operXML.getElement(LossRtnBody_LossFitInfoitem,"Item",i);
				PrpLDlossCarCompVo prpLcomponentDto=new PrpLDlossCarCompVo();
				prpLcomponentDto.setIndId(operXML.getKeyValue(itemnu,"PartId"));//定损系统零件唯一ID	
				prpLcomponentDto.setVeripMaterFee(new BigDecimal(NullToZero(operXML.getKeyValue(itemnu,"AuditPrice"))));// 核价价格
				prpLcomponentDto.setVeripRestFee(new BigDecimal(( NullToZero( operXML.getKeyValue(itemnu,"AuditRemnant")) )));// 核价残值
				prpLcomponentDto.setMaterialQuantity(new BigDecimal( NullToZero( operXML.getKeyValue(itemnu,"AuditCount"))));//核价数量
				prpLcomponentDto.setSumCheckLoss(new BigDecimal(( NullToZero( operXML.getKeyValue(itemnu,"AuditSumPrice")) )));//核价小计
				prpLcomponentDto.setVeripRemark(operXML.getKeyValue(itemnu,"AuditRemark")); //备注
				prpLcomponentDto.setAuditCount(new BigDecimal( NullToZero( operXML.getKeyValue(itemnu,"AuditCount"))));//核价数量
				
				fitlist.add(prpLcomponentDto);
			}
			return fitlist;
	}
	
	
	/**
	 *  解析LossAssistInfo 辅料信息
	 * @throws Exception 
	 * @throws JDOMException 
	 **/
	public List<PrpLDlossCarMaterialVo> decodeLossAssistInfo(OperXML operXML) throws JDOMException, Exception{
		List<PrpLDlossCarMaterialVo> assistList=new ArrayList<PrpLDlossCarMaterialVo>();
		
		Element LossRtnBodyitem = operXML.getElement("LossRtnBody");
		Element LossRtnBody_LossRepairInfoitem = operXML.getElement(LossRtnBodyitem,"LossAssistInfo");
		
		int count = operXML.getCountByTag(LossRtnBody_LossRepairInfoitem,"Item");
		for(int i = 0; i<count; i++ ){
			Element itemnu = operXML.getElement(LossRtnBody_LossRepairInfoitem,"Item",i);
			PrpLDlossCarMaterialVo assist=new PrpLDlossCarMaterialVo();
			assist.setAssistId(operXML.getKeyValue(itemnu,"AssistId"));	//定损系统辅料唯一ID AssistId
			assist.setAuditPrice(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"AuditPrice"))));//单价	AuditPrice
			assist.setAuditMaterialFee(new BigDecimal(NullToZero( operXML.getKeyValue(itemnu,"AuditMaterialFee"))));//金额	MaterialFee
			assist.setAuditRemark(operXML.getKeyValue(itemnu,"AuditRemark"));//核价说明
			assist.setAuditCount(new Double( NullToZero(operXML.getKeyValue(itemnu,"AuditCount"))).intValue());//核价数量
			assistList.add(assist);
		}
		return assistList;
	}
	
	
	
	private static String NullToZero(String strNum) {
		if(strNum==null||strNum.equals("")){
			return "0";
		}
		return strNum;
	}
	
	

}
