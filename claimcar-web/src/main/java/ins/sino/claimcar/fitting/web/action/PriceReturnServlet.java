package ins.sino.claimcar.fitting.web.action;

import ins.platform.utils.XstreamFactory;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claimjy.util.JaxbUtil;
import ins.sino.claimcar.claimjy.vo.DlossReqHeadVo;
import ins.sino.claimcar.claimjy.vo.JyResHeadVo;
import ins.sino.claimcar.claimjy.vo.pricereturn.JyPriceReturnReqAssistInfo;
import ins.sino.claimcar.claimjy.vo.pricereturn.JyPriceReturnReqBody;
import ins.sino.claimcar.claimjy.vo.pricereturn.JyPriceReturnReqFitInfo;
import ins.sino.claimcar.claimjy.vo.pricereturn.JyPriceReturnReqLossInfo;
import ins.sino.claimcar.claimjy.vo.pricereturn.JyPriceReturnReqVo;
import ins.sino.claimcar.claimjy.vo.pricereturn.JyPriceReturnResVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;

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

public class PriceReturnServlet  extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(PriceReturnServlet.class);
	
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
			log.info("\n------------------------精友核价返回接口请求报文：\n "+strData);
			if(!StringUtils.isEmpty(strData)){
				JyPriceReturnReqVo reqVo = JaxbUtil.toObj(strData, JyPriceReturnReqVo.class);
				String str = checkValid(reqVo);
				if("".equals(str)){
					PrpLDlossCarMainVo mainVo = lossCarService.findLossCarMainById(Long.valueOf(reqVo.getBody().getLossInfo().getDmgVhclId()));
					PrpLDlossCarMainVo lossCarMainVo = this.doTransData(reqVo,mainVo);
					lossCarService.saveByJyDeflossCheck(lossCarMainVo, "CheckPrice");
					resHead.setResponseCode("000");
					resHead.setErrorMessage("成功");
				}else{
					resHead.setResponseCode("402");
					resHead.setErrorMessage(str);
				}
			}else{
				resHead.setResponseCode("402");
				resHead.setErrorMessage("数据为空！");
			}
		}catch(Exception e){
			e.printStackTrace();
			resHead.setResponseCode("499");
			resHead.setErrorMessage(e.getMessage());
		}finally{
			resVo.setHead(resHead);
			String strReturn = XstreamFactory.objToXml(resVo);
			output.write(strReturn.getBytes());
			output.close();
		}
		
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
		doPost(request, response);
	}
	
	/*
	 * 数据接收
	 * */
	public String receiveStream(HttpServletRequest request) throws IOException{
		// 获取接收对象
		java.io.InputStream in = request.getInputStream();
		InputStreamReader insreader = new InputStreamReader(in,"GBK");
		BufferedReader bin = new BufferedReader(insreader);
		
		String read = "";
		StringBuffer strBuffer = new StringBuffer();
		// 转换数据流
		while(( read = bin.readLine() )!=null){
			strBuffer.append(read);
		}
		bin.close();
		insreader.close();
		in.close();
		
		return strBuffer.toString();
	}
	
	public String checkValid(JyPriceReturnReqVo reqVo){
		String str = "";
		DlossReqHeadVo head = reqVo.getHead();
		JyPriceReturnReqBody body = reqVo.getBody();
		JyPriceReturnReqLossInfo lossInfo = body.getLossInfo();
		List<JyPriceReturnReqFitInfo> fitInfoList = body.getFitInfoList();
		List<JyPriceReturnReqAssistInfo> assistInfoList = body.getAssistInfoList();
		
		if(!"004".equals(head.getRequestType())){
			str = str+"请求类型错误；";
		}
		if(StringUtils.isEmpty(lossInfo.getLossNo())){
			str = str+"定损单号不能为空；";
		}
		if(StringUtils.isEmpty(lossInfo.getDmgVhclId())){
			str = str+"车损标的主键不能为空；";
		}
		if(StringUtils.isEmpty(lossInfo.getReportCode())){
			str = str+"事故号码不能为空；";
		}
		if(StringUtils.isEmpty(lossInfo.getAuditPartSum())){
			str = str+"核价换件合计不能为空；";
		}
		if(StringUtils.isEmpty(lossInfo.getAuditMateSum())){
			str = str+"核价辅料合计不能为空；";
		}
		if(StringUtils.isEmpty(lossInfo.getSelfPaySum())){
			str = str+"自付合计不能为空；";
		}
		if(StringUtils.isEmpty(lossInfo.getDerogationSum())){
			str = str+"减损合计不能为空；";
		}
		if(StringUtils.isEmpty(lossInfo.getTotalSum())){
			str = str+"核损合计不能为空；";
		}
		if(fitInfoList!=null && fitInfoList.size()>0){
			for(JyPriceReturnReqFitInfo fitVo:fitInfoList){
				if(StringUtils.isEmpty(fitVo.getPartId())){
					str = str+"定损系统零件唯一ID不能为空；";
					break;
				}
				if(StringUtils.isEmpty(fitVo.getAuditDiscountFee())){
					str = str+"核价折前价格不能为空；";
					break;
				}
				if(StringUtils.isEmpty(fitVo.getAuditDiscount())){
					str = str+"核价折扣不能为空；";
					break;
				}
				if(StringUtils.isEmpty(fitVo.getAuditMaterialFee())){
					str = str+"核价材料费不能为空；";
					break;
				}
				if(StringUtils.isEmpty(fitVo.getManageRate())){
					str = str+"核价管理费比率不能为空；";
					break;
				}
				if(StringUtils.isEmpty(fitVo.getManageFee())){
					str = str+"核价管理费不能为空；";
					break;
				}
				if(StringUtils.isEmpty(fitVo.getEstiPartSum())){
					str = str+"核价换件小计不能为空；";
					break;
				}
			}
		}
		if(assistInfoList!=null && assistInfoList.size()>0){
			for(JyPriceReturnReqAssistInfo assistVo:assistInfoList){
				if(StringUtils.isEmpty(assistVo.getAssistId())){
					str = str+"辅料明细主键不能为空；";
					break;
				}
				if(StringUtils.isEmpty(assistVo.getCount())){
					str = str+"辅料数量不能为空；";
					break;
				}
				if(StringUtils.isEmpty(assistVo.getMaterialFee())){
					str = str+"辅料价格不能为空；";
					break;
				}
				if(StringUtils.isEmpty(assistVo.getEvalMateSum())){
					str = str+"辅料合计不能为空；";
					break;
				}
				if(StringUtils.isEmpty(assistVo.getSelfConfigFlag())){
					str = str+"自定义辅料标记不能为空；";
					break;
				}
			}
		}
		return str;
	}
	
	public PrpLDlossCarMainVo doTransData(JyPriceReturnReqVo reqVo,PrpLDlossCarMainVo mainVo){
		PrpLDlossCarMainVo lossCarMainVo = new PrpLDlossCarMainVo(); 
		List<PrpLDlossCarCompVo> compList = new ArrayList<PrpLDlossCarCompVo>();
		List<PrpLDlossCarMaterialVo> materialList = new ArrayList<PrpLDlossCarMaterialVo>();
		JyPriceReturnReqBody body = reqVo.getBody();
		JyPriceReturnReqLossInfo lossInfo = body.getLossInfo();
		List<JyPriceReturnReqFitInfo> fitInfoList = body.getFitInfoList();
		List<JyPriceReturnReqAssistInfo> assistInfoList = body.getAssistInfoList();
		
		lossCarMainVo.setId(Long.valueOf(lossInfo.getDmgVhclId()));
//		lossCarMainVo.setRegistNo(lossInfo.getReportCode());
		if(lossInfo.getTotalManageSum() != null){
			lossCarMainVo.setSumVeriPManage(new BigDecimal(lossInfo.getTotalManageSum()));
		}
		lossCarMainVo.setVeripHandlerCode(lossInfo.getHandlerCode());
		lossCarMainVo.setSumVeripCompFee(new BigDecimal(lossInfo.getAuditPartSum()));
		lossCarMainVo.setSumVeripMatFee(new BigDecimal(lossInfo.getAuditMateSum()));
		lossCarMainVo.setSumVeripSelfpay(new BigDecimal(lossInfo.getSelfPaySum()));
		lossCarMainVo.setSumVeripDerogation(new BigDecimal(lossInfo.getDerogationSum()));
		lossCarMainVo.setSumVeripRemnant(mainVo.getSumRemnant());	//定损系统不改残值
		BigDecimal sumVeripLoss = (new BigDecimal(lossInfo.getAuditPartSum()).add(new BigDecimal(lossInfo.getAuditMateSum())).subtract(mainVo.getSumRemnant())).add(NullToZero(lossInfo.getTotalManageSum()));
		lossCarMainVo.setSumVeripLoss(sumVeripLoss);
		lossCarMainVo.setRemark(lossInfo.getRemark());
		//精友交互标志
		lossCarMainVo.setFlag(CodeConstants.JyFlag.INBACK);//精友交互标志
		
		if(fitInfoList!=null && fitInfoList.size()>0){
			for(JyPriceReturnReqFitInfo fitVo:fitInfoList){
				PrpLDlossCarCompVo compVo = new PrpLDlossCarCompVo();
				compVo.setIndId(fitVo.getPartId());
				compVo.setAuditDiscountFee(new BigDecimal(fitVo.getAuditDiscountFee()));
				compVo.setVeripMaterFee(new BigDecimal(fitVo.getAuditMaterialFee()));
				compVo.setVeripManageRate(new BigDecimal(fitVo.getManageRate()));
				compVo.setVeripManageFee(new BigDecimal(fitVo.getManageFee()));
				compVo.setVeripRemark(fitVo.getRemark());
				if(fitVo.getEvalPartSumFirst()!=null){
					compVo.setFirstMaterialFee(new BigDecimal(fitVo.getEvalPartSumFirst()));
				}
				for(PrpLDlossCarCompVo vo:mainVo.getPrpLDlossCarComps()){
					if(fitVo.getPartId().equals(vo.getIndId())){
						compVo.setVeripRestFee(vo.getRestFee());
						compVo.setAuditCount(new BigDecimal(vo.getQuantity()));
					}
				}
				compVo.setSumCheckLoss(NullToZero(fitVo.getEstiPartSum()).subtract(compVo.getVeripRestFee()));
				compList.add(compVo);
			}
			lossCarMainVo.setPrpLDlossCarComps(compList);
		}
		if(assistInfoList!=null && assistInfoList.size()>0){
			for(JyPriceReturnReqAssistInfo assistVo:assistInfoList){
				PrpLDlossCarMaterialVo material = new PrpLDlossCarMaterialVo();
				material.setAssistId(assistVo.getAssistId());
				material.setMaterialName(assistVo.getItemName());
				material.setAuditCount(new BigDecimal(assistVo.getCount()).intValue());
				material.setAuditPrice(new BigDecimal(assistVo.getMaterialFee()));
				material.setAuditMaterialFee(new BigDecimal(assistVo.getEvalMateSum()));
				material.setSelfConfigFlag(assistVo.getSelfConfigFlag());
				material.setKindCode(assistVo.getItemCoverCode());
				material.setAuditRemark(assistVo.getRemark());
				materialList.add(material);
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
