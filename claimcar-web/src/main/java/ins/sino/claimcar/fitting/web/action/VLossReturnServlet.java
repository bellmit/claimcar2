package ins.sino.claimcar.fitting.web.action;

import ins.platform.utils.XstreamFactory;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claimjy.util.JaxbUtil;
import ins.sino.claimcar.claimjy.vo.DlossReqHeadVo;
import ins.sino.claimcar.claimjy.vo.JyResHeadVo;
import ins.sino.claimcar.claimjy.vo.pricereturn.JyPriceReturnResVo;
import ins.sino.claimcar.claimjy.vo.vlossreturn.JyVLossReturnAssistInfo;
import ins.sino.claimcar.claimjy.vo.vlossreturn.JyVLossReturnFitInfo;
import ins.sino.claimcar.claimjy.vo.vlossreturn.JyVLossReturnLossInfo;
import ins.sino.claimcar.claimjy.vo.vlossreturn.JyVLossReturnOutRepairInfo;
import ins.sino.claimcar.claimjy.vo.vlossreturn.JyVLossReturnRepairInfo;
import ins.sino.claimcar.claimjy.vo.vlossreturn.JyVLossReturnRepairSumInfo;
import ins.sino.claimcar.claimjy.vo.vlossreturn.JyVLossReturnReqBody;
import ins.sino.claimcar.claimjy.vo.vlossreturn.JyVLossReturnReqVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.losscar.vo.PrpLLossRepairSumInfoVo;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class VLossReturnServlet   extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(VLossReturnServlet.class);
	
	public void init() throws ServletException {
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
		BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		JyPriceReturnResVo resVo = new JyPriceReturnResVo();
		JyResHeadVo resHead = new JyResHeadVo();
		try{
			LossCarService lossCarService = (LossCarService) context.getBean(LossCarService.class);
			
			String strData  = receiveStream(request);
			log.info("\n------------------------???????????????????????????????????????\n "+strData);
			if(!StringUtils.isEmpty(strData)){
				JyVLossReturnReqVo reqVo = JaxbUtil.toObj(strData, JyVLossReturnReqVo.class);
				String str = checkValid(reqVo);
				if("".equals(str)){
					PrpLDlossCarMainVo lossCarMainVo = this.doTransData(reqVo);
					lossCarService.saveByJyDeflossCheck(lossCarMainVo, "CheckLoss");
					resHead.setResponseCode("000");
					resHead.setErrorMessage("??????");
				}else{
					resHead.setResponseCode("402");
					resHead.setErrorMessage(str);
				}
			}else{
				resHead.setResponseCode("402");
				resHead.setErrorMessage("???????????????");
			}
		}catch(Exception e){
			e.printStackTrace();
			resHead.setResponseCode("499");
			resHead.setErrorMessage(e.getMessage());
		}finally{
			resVo.setHead(resHead);
			String strReturn = XstreamFactory.objToXml(resVo);
			log.info("\n------------------------???????????????????????????????????????\n "+strReturn);
			output.write(strReturn.getBytes());
			output.close();
		}
		
	}
	
	/*
	 * ????????????
	 * */
	public String receiveStream(HttpServletRequest request) throws IOException{
		// ??????????????????
		java.io.InputStream in = request.getInputStream();
		InputStreamReader insreader = new InputStreamReader(in,"GBK");
		BufferedReader bin = new BufferedReader(insreader);
		
		String read = "";
		StringBuffer strBuffer = new StringBuffer();
		// ???????????????
		while(( read = bin.readLine() )!=null){
			strBuffer.append(read);
		}
		bin.close();
		insreader.close();
		in.close();
		
		return strBuffer.toString();
	}
	
	public String checkValid(JyVLossReturnReqVo reqVo){
		String str = "";
		DlossReqHeadVo head = reqVo.getHead();
		JyVLossReturnReqBody body = reqVo.getBody();
		JyVLossReturnLossInfo lossInfo = body.getLossInfo();
		List<JyVLossReturnFitInfo> fitInfoList = body.getFitInfoList();
		List<JyVLossReturnRepairInfo> repairInfoList = body.getRepairInfoList();
		List<JyVLossReturnRepairSumInfo> RepairSumInfoList = body.getRepairSumInfoList();
		List<JyVLossReturnOutRepairInfo> outRepairInfoList = body.getOutRepairInfoList();
		List<JyVLossReturnAssistInfo> assistIfoList = body.getAssistIfoList();
		
		if(!"006".equals(head.getRequestType())){
			str = str+"?????????????????????";
		}
		if(StringUtils.isEmpty(lossInfo.getLossNo())){
			str = str+"???????????????????????????";
		}
		if(StringUtils.isEmpty(lossInfo.getDmgVhclId())){
			str = str+"?????????????????????????????????";
		}
		if(StringUtils.isEmpty(lossInfo.getReportCode())){
			str = str+"???????????????????????????";
		}
		if(StringUtils.isEmpty(lossInfo.getAuditSalvageFee())){
			str = str+"???????????????????????????";
		}
		if(StringUtils.isEmpty(lossInfo.getAuditRemnantFee())){
			str = str+"???????????????????????????";
		}
		if(StringUtils.isEmpty(lossInfo.getAuditPartSum())){
			str = str+"?????????????????????????????????";
		}
		if(StringUtils.isEmpty(lossInfo.getAuditMateSum())){
			str = str+"?????????????????????????????????";
		}
		if(StringUtils.isEmpty(lossInfo.getTotalManageSum())){
			str = str+"????????????????????????????????????";
		}
		if(StringUtils.isEmpty(lossInfo.getSelfPaySum())){
			str = str+"???????????????????????????";
		}
		if(StringUtils.isEmpty(lossInfo.getOuterSum())){
			str = str+"???????????????????????????";
		}
		if(StringUtils.isEmpty(lossInfo.getDerogationSum())){
			str = str+"???????????????????????????";
		}
		if(StringUtils.isEmpty(lossInfo.getTotalSum())){
			str = str+"?????????????????????????????????";
		}
		if(StringUtils.isEmpty(lossInfo.getAuditRepiarSum())){
			str = str+"?????????????????????????????????";
		}
		
		if(fitInfoList!=null && fitInfoList.size()>0){
			for(JyVLossReturnFitInfo fitVo:fitInfoList){
				if(StringUtils.isEmpty(fitVo.getPartId())){
					str = str+"????????????????????????ID???????????????";
					break;
				}
				if(StringUtils.isEmpty(fitVo.getAuditMaterialFee())){
					str = str+"??????????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(fitVo.getAuditCount())){
					str = str+"???????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(fitVo.getApprPartSum())){
					str = str+"?????????????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(fitVo.getSelfPayPrice())){
					str = str+"???????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(fitVo.getApprRemainsPrice())){
					str = str+"?????????????????????";
					break;
				}
				if(StringUtils.isEmpty(fitVo.getManageRate())){
					str = str+"?????????????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(fitVo.getManageRate())){
					str = str+"??????????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(fitVo.getFitBackFlag())){
					str = str+"???????????????????????????";
					break;
				}
			}
		}
		
		if(repairInfoList!=null && repairInfoList.size()>0){
			for(JyVLossReturnRepairInfo jyRepairVo:repairInfoList){
				if(StringUtils.isEmpty(jyRepairVo.getRepairId())){
					str = str+"????????????????????????ID???????????????";
					break;
				}
				if(StringUtils.isEmpty(jyRepairVo.getAuditManpowerFee())){
					str = str+"??????????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(jyRepairVo.getApprHour())){
					str = str+"??????????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(jyRepairVo.getApprRepairSum())){
					str = str+"???????????????????????????";
					break;
				}
			}
		}
		
		if(RepairSumInfoList!=null && RepairSumInfoList.size()>0){
			for(JyVLossReturnRepairSumInfo jyRepairSumVo:RepairSumInfoList){
				if(StringUtils.isEmpty(jyRepairSumVo.getWorkTypeCode())){
					str = str+"???????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(jyRepairSumVo.getItemCount())){
					str = str+"???????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(jyRepairSumVo.getApprRepairSum())){
					str = str+"??????????????????????????????";
					break;
				}
			}
		}
		
		if(outRepairInfoList!=null && outRepairInfoList.size()>0){
			for(JyVLossReturnOutRepairInfo jyRepairVo:outRepairInfoList){
				if(StringUtils.isEmpty(jyRepairVo.getOuterId())){
					str = str+"????????????????????????ID???????????????";
					break;
				}
				if(StringUtils.isEmpty(jyRepairVo.getRepairHandaddFlag())){
					str = str+"??????????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(jyRepairVo.getEvalOuterPirce())){
					str = str+"???????????????????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(jyRepairVo.getDerogationPrice())){
					str = str+"???????????????????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(jyRepairVo.getRepairOuterSum())){
					str = str+"???????????????????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(jyRepairVo.getPartPrice())){
					str = str+"???????????????????????????";
					break;
				}
			}
		}
		
		if(assistIfoList!=null && assistIfoList.size()>0){
			for(JyVLossReturnAssistInfo assistVo:assistIfoList){
				if(StringUtils.isEmpty(assistVo.getAssistId())){
					str = str+"????????????ID???????????????";
					break;
				}
				if(StringUtils.isEmpty(assistVo.getAuditPrice())){
					str = str+"?????????????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(assistVo.getAuditCount())){
					str = str+"?????????????????????????????????";
					break;
				}
				if(StringUtils.isEmpty(assistVo.getApprMateSum())){
					str = str+"?????????????????????????????????";
					break;
				}
			}
		}
		
		
		return str;
	}
	
	public PrpLDlossCarMainVo doTransData(JyVLossReturnReqVo reqVo){
		PrpLDlossCarMainVo lossCarMainVo = new PrpLDlossCarMainVo(); 
		List<PrpLDlossCarCompVo> compList = new ArrayList<PrpLDlossCarCompVo>();
		List<PrpLDlossCarRepairVo> repairList = new ArrayList<PrpLDlossCarRepairVo>();
		List<PrpLDlossCarMaterialVo> materialList = new ArrayList<PrpLDlossCarMaterialVo>();
		List<PrpLLossRepairSumInfoVo> repairSumList = new ArrayList<PrpLLossRepairSumInfoVo>();
		JyVLossReturnReqBody body = reqVo.getBody();
		JyVLossReturnLossInfo lossInfo = body.getLossInfo();
		List<JyVLossReturnFitInfo> fitInfoList = body.getFitInfoList();
		List<JyVLossReturnRepairInfo> repairInfoList = body.getRepairInfoList();
		List<JyVLossReturnRepairSumInfo> RepairSumInfoList = body.getRepairSumInfoList();
		List<JyVLossReturnOutRepairInfo> outRepairInfoList = body.getOutRepairInfoList();
		List<JyVLossReturnAssistInfo> assistIfoList = body.getAssistIfoList();
		
		lossCarMainVo.setId(Long.valueOf(lossInfo.getDmgVhclId()));
		lossCarMainVo.setSumVeriRescueFee(new BigDecimal(lossInfo.getAuditSalvageFee()));//????????????
		lossCarMainVo.setSumVeriRemnant(new BigDecimal(lossInfo.getAuditRemnantFee()));
		lossCarMainVo.setSumVeriCompFee(new BigDecimal(lossInfo.getAuditPartSum()));
		lossCarMainVo.setSumVeriMatFee(new BigDecimal(lossInfo.getAuditMateSum()));
		lossCarMainVo.setSumVeriManage(new BigDecimal(lossInfo.getTotalManageSum()));
		lossCarMainVo.setSumVeriSelfpay(new BigDecimal(lossInfo.getSelfPaySum()));
		lossCarMainVo.setSumVeriOutFee(new BigDecimal(lossInfo.getOuterSum()));//????????????
		lossCarMainVo.setSumVeriRepairFee(new BigDecimal(lossInfo.getAuditRepiarSum()));//????????????
		lossCarMainVo.setSumVeriDerogation(new BigDecimal(lossInfo.getDerogationSum()));
		lossCarMainVo.setUnderWriteCode(lossInfo.getHandlerCode());
		lossCarMainVo.setRemark(lossInfo.getRemark());
		BigDecimal sumVeriLossFee = new BigDecimal(lossInfo.getTotalSum()).subtract(new BigDecimal(lossInfo.getAuditSalvageFee()));
		lossCarMainVo.setSumVeriLossFee(sumVeriLossFee);//????????????
		//??????????????????
		lossCarMainVo.setFlag(CodeConstants.JyFlag.INBACK);//??????????????????
		
		Boolean reCheckFlag =false;//????????????
		
		if(fitInfoList!=null && fitInfoList.size()>0){
			for(JyVLossReturnFitInfo fitVo:fitInfoList){
				PrpLDlossCarCompVo compVo = new PrpLDlossCarCompVo();
				compVo.setIndId(fitVo.getPartId());
				compVo.setVeriMaterFee(new BigDecimal(fitVo.getAuditMaterialFee()));
				compVo.setVeriQuantity(Integer.valueOf(fitVo.getAuditCount()));
				compVo.setVeriSelfPrice(new BigDecimal(fitVo.getSelfPayPrice()));
				compVo.setVeriRestFee(new BigDecimal(fitVo.getApprRemainsPrice()));
				compVo.setSumVeriLoss(NullToZero(fitVo.getApprPartSum()).subtract(compVo.getVeriRestFee()));
				compVo.setVeriManageRate(new BigDecimal(fitVo.getManageRate()));
				compVo.setVeriManageFee(new BigDecimal(fitVo.getManageRate()));
				compVo.setRemark(fitVo.getRemark());
				compVo.setRecycleFlag(fitVo.getFitBackFlag());
				compVo.setReCheckFlag(fitVo.getRecheckFlag());
				if("1".equals(fitVo.getRecheckFlag())){
					reCheckFlag = true;
				}
				compList.add(compVo);
			}
			lossCarMainVo.setPrpLDlossCarComps(compList);
		}
		if(reCheckFlag){
			lossCarMainVo.setReCheckFlag("1");
		}else{
			lossCarMainVo.setReCheckFlag("0");
		}
		
		if(repairInfoList!=null && repairInfoList.size()>0){
			for(JyVLossReturnRepairInfo jyRepairVo:repairInfoList){
				PrpLDlossCarRepairVo repairVo = new PrpLDlossCarRepairVo();
				repairVo.setRepairId(jyRepairVo.getRepairId());
				repairVo.setVeriManUnitPrice(new BigDecimal(jyRepairVo.getAuditManpowerFee()));
				repairVo.setVeriManHour(new BigDecimal(jyRepairVo.getApprHour()));
				repairVo.setVeriManHourFee(new BigDecimal(jyRepairVo.getApprRepairSum()));
				repairVo.setSumVeriLoss(new BigDecimal(jyRepairVo.getApprRepairSum()));
				repairVo.setVeriRemark(jyRepairVo.getRemark());	//???????????? 0-?????? 1-??????
				repairVo.setRepairFlag("0");
				repairList.add(repairVo);
			}
		}
		
		if(RepairSumInfoList!=null && RepairSumInfoList.size()>0){
			for(JyVLossReturnRepairSumInfo jyRepairSumVo:RepairSumInfoList){
				PrpLLossRepairSumInfoVo repairSumVo = new PrpLLossRepairSumInfoVo();
				repairSumVo.setWorkTypeCode(jyRepairSumVo.getWorkTypeCode());
				repairSumVo.setVeriItemCount(new BigDecimal(jyRepairSumVo.getItemCount()));
				repairSumVo.setApprRepairSum(new BigDecimal(jyRepairSumVo.getApprRepairSum()));
				repairSumList.add(repairSumVo);
			}
			lossCarMainVo.setPrpLLossRepairSumInfoVos(repairSumList);
		}
		
		//??????
		if(outRepairInfoList!=null && outRepairInfoList.size()>0){
			for(JyVLossReturnOutRepairInfo jyRepairVo:outRepairInfoList){
				PrpLDlossCarRepairVo repairVo = new PrpLDlossCarRepairVo();
				repairVo.setRepairId(jyRepairVo.getOuterId());
				repairVo.setSelfConfigFlag(jyRepairVo.getRepairHandaddFlag());
				repairVo.setSumVeriLoss(new BigDecimal(jyRepairVo.getRepairOuterSum()));
				repairVo.setVeriDerogationPrice(new BigDecimal(jyRepairVo.getDerogationPrice()));
				repairVo.setVeriMaterUnitPrice(new BigDecimal(jyRepairVo.getPartPrice()));
				if(!StringUtils.isEmpty(jyRepairVo.getPartAmount())){
					repairVo.setVeriMaterQuantity(new BigDecimal(jyRepairVo.getPartAmount()));
				}
				repairVo.setVeriManUnitPrice(new BigDecimal(jyRepairVo.getEvalOuterPirce()));
				if(!StringUtils.isEmpty(jyRepairVo.getOutItemAmount())){
					repairVo.setVeriManHour(new BigDecimal(jyRepairVo.getOutItemAmount()));
				}
				
				repairList.add(repairVo);
			}
		}
		lossCarMainVo.setPrpLDlossCarRepairs(repairList);
		
		if(assistIfoList!=null && assistIfoList.size()>0){
			for(JyVLossReturnAssistInfo assistVo:assistIfoList){
				PrpLDlossCarMaterialVo materialVo = new PrpLDlossCarMaterialVo();
				materialVo.setAssistId(assistVo.getAssistId());
				materialVo.setAuditLossPrice(new BigDecimal(assistVo.getAuditPrice()));
				materialVo.setAuditLossCount(new BigDecimal(assistVo.getAuditCount()).intValue());
				materialVo.setAuditLossMaterialFee(new BigDecimal(assistVo.getApprMateSum()));
				materialVo.setAuditLossRemark(assistVo.getRemark());
				materialList.add(materialVo);
			}
			lossCarMainVo.setPrpLDlossCarMaterials(materialList);
		}
		
		return lossCarMainVo;
	}
	private static BigDecimal NullToZero(String strNum) {
		if(strNum==null||strNum.equals("")){
			strNum = "0";
		}
		return new BigDecimal(strNum);
	}
}
