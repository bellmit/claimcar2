package ins.sino.claimcar.invoice.services;

import ins.framework.lang.Springs;
import ins.platform.utils.DataUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.invoice.vo.BasePartReceiptTask;
import ins.sino.claimcar.invoice.vo.BodyReceiptTask;
import ins.sino.claimcar.invoice.vo.HeadRemoteRes;
import ins.sino.claimcar.invoice.vo.ReceiptTaskDto;
import ins.sino.claimcar.invoice.vo.ReceiptTaskResDto;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.pay.service.PadPayService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.alibaba.dubbo.common.utils.Assert;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

@WebService(serviceName="receiptCallBack")
@SOAPBinding
public class ReceiptTaskService  extends SpringBeanAutowiringSupport{
	
//	@Autowired
//	private CompensateTaskService compensateTaskService;
	@Autowired
	private CompensateService compensateService;
	@Autowired
	private AssessorService assessorService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	PadPayService padPayService;
	@Autowired
	PadPayPubService padPayPubService;
	private static Logger logger = LoggerFactory.getLogger(ReceiptTaskService.class);
	/* 
	 * 发票登记后回写费用信息
	 * 
	 * @ niuqiang
	 * 
	 */
	public String service(String xml) {
		logger.info("营改增接收报文: \n"+xml);
		ReceiptTaskResDto resVo = new ReceiptTaskResDto();
		HeadRemoteRes head = new HeadRemoteRes();
		head.setErrorMessage("成功");
		head.setResponseCode(true);
		resVo.setHead(head);
		
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		try{
			init();
			if(StringUtils.isBlank(xml)){
				throw new IllegalArgumentException("报文为空");
			}
			stream.processAnnotations(ReceiptTaskDto.class);
			ReceiptTaskDto basePartVo = (ReceiptTaskDto)stream.fromXML(xml);
			BodyReceiptTask tasks = basePartVo.getBody();
			this.invoiceWriteBack(tasks.getBaseParts());
		}
		catch(Exception e){
			head.setResponseCode(false);
			head.setErrorMessage("异常信息:  "+e.getMessage());
			logger.debug("营改增异常信息：\n");
			logger.info("营改增异常信息：",e);
		}
		stream.processAnnotations(ReceiptTaskResDto.class);
		System.out.println("营改增返回报文：\n"+stream.toXML(resVo));
		return stream.toXML(resVo);
	}
	
	
     private void init() {
		if(compensateService==null){
			compensateService = (CompensateService)Springs.getBean("compensateService");
		}
		if(assessorService==null){
			assessorService = (AssessorService)Springs.getBean("assessorService");
		}
		if(compensateTaskService==null){
			compensateTaskService = (CompensateTaskService)Springs.getBean(CompensateTaskService.class);
		}
		if(padPayService==null){
			padPayService = (PadPayService)Springs.getBean(PadPayService.class);
		}
		if(padPayPubService==null){
			padPayPubService = (PadPayPubService)Springs.getBean(PadPayPubService.class);
		}
	}


	public void invoiceWriteBack(List<BasePartReceiptTask> writeBackList ){
	   for(BasePartReceiptTask writeBackVo :writeBackList){
		   Assert.notNull(writeBackVo.getBusinessNo(),"业务号为空");
		   Assert.notNull(writeBackVo.getSerialNo(),"序号为空");
		   Assert.notNull(writeBackVo.getInvoicetype(),"发票类型为空");
		   Assert.notNull(writeBackVo.getTaxRate(),"税率为空");
		   Assert.notNull(writeBackVo.getSumAmountNT(),"不含金额税为空");
		   Assert.notNull(writeBackVo.getSumAmountTax(),"总税额为空");
		   Assert.notNull(writeBackVo.getFeeType(),"总税额为空");
	   }
	  
	   for(BasePartReceiptTask writeBackVo :writeBackList){
		   if("13".equals(writeBackVo.getFeeType())){  // 公估费写回
			   if(assessorService == null){
				   throw new IllegalArgumentException("公估费写会错误：调不到服务");
			   }
			   PrpLAssessorFeeVo feeVo = assessorService.findAssessorFeeVoByComp(writeBackVo.getBusinessNo());
			   if(feeVo ==null){
				   //throw new IllegalArgumentException("计算书号错误");
			   }else{
			       feeVo.setInvoiceType(writeBackVo.getInvoicetype());
	               feeVo.setAddTaxRate(String.valueOf(writeBackVo.getTaxRate()));
	               feeVo.setAddTaxValue(String.valueOf(writeBackVo.getSumAmountTax()));
	               feeVo.setNoTaxValue(String.valueOf(writeBackVo.getSumAmountNT()));
	               assessorService.updateAssessorFee(feeVo);
			   }
			 
		   }else{  //理算费用写回，预付写回
			   
			   if(writeBackVo.getBusinessNo().startsWith("Y")){
				   if(CodeConstants.VatFeeType.YPAY.equals(writeBackVo.getFeeType())){//赔款
					   List<PrpLPrePayVo> prePayVoList = compensateTaskService.getPrePayVo(writeBackVo.getBusinessNo(),"P");
					   List<PrpLPrePayVo> prePayNewVoList = new ArrayList<PrpLPrePayVo>();
					   BigDecimal sumChargeFee = BigDecimal.ZERO;
					   for(PrpLPrePayVo prepayVo:prePayVoList){
						   if (prepayVo.getSerialNo().equals(writeBackVo.getSerialNo()+"")){
							   sumChargeFee = sumChargeFee.add(prepayVo.getPayAmt());
							   prePayNewVoList.add(prepayVo);
						   }
					   }
					   updatePrpLPrePay(writeBackVo,prePayNewVoList,sumChargeFee);
				   }else{
					   List<PrpLPrePayVo> prePayVoList = compensateService.queryPrePay(writeBackVo.getBusinessNo());
					   List<PrpLPrePayVo> prePayNewVoList = new ArrayList<PrpLPrePayVo>();
					   BigDecimal sumChargeFee = BigDecimal.ZERO;
					   for(PrpLPrePayVo prepayVo:prePayVoList){
						   if (prepayVo.getSerialNo().equals(writeBackVo.getSerialNo()+"")){
							   for(PrpLPrePayVo prepayOldVo:prePayVoList){
								   if(prepayVo.getChargeCode().equals(prepayOldVo.getChargeCode()) && 
										   prepayVo.getPayeeId().equals(prepayOldVo.getPayeeId())){
									   sumChargeFee = sumChargeFee.add(prepayOldVo.getPayAmt());
									   prePayNewVoList.add(prepayOldVo);
								   }
							   }
						   }
					   }
					   updatePrpLPrePay(writeBackVo,prePayNewVoList,sumChargeFee);
				   }
				 
			   }else if(writeBackVo.getBusinessNo().startsWith("D")){
				   if(CodeConstants.VatFeeType.DPAY.equals(writeBackVo.getFeeType())){//赔款
					   PrpLPadPayMainVo padPayMainVo = padPayService.findPadPayMainByCompNo(writeBackVo.getBusinessNo());
					   List<PrpLPadPayPersonVo> prpLPadPayPersonVos = padPayMainVo.getPrpLPadPayPersons();
					   List<PrpLPadPayPersonVo> prpLPadPayPersonNewVos = new ArrayList<PrpLPadPayPersonVo>();
						for (PrpLPadPayPersonVo vo : prpLPadPayPersonVos) {
							if (vo.getSerialNo().equals(writeBackVo.getSerialNo() + "")) {
								prpLPadPayPersonNewVos.add(vo);
							}
						}
						int i = 1;// 序号
						BigDecimal sumAmountNT = new BigDecimal(writeBackVo.getSumAmountTax());// 不含税金额
						BigDecimal sumNoTax = new BigDecimal(writeBackVo.getSumAmountNT());// 税额
						double addTax = 0d;
						double addNoTax = 0d;
						
					   for(PrpLPadPayPersonVo padPayPersonVo: prpLPadPayPersonNewVos){
						   
						   padPayPersonVo.setInvoiceType(writeBackVo.getInvoicetype());
						   padPayPersonVo.setAddTaxRate(String.valueOf(writeBackVo.getTaxRate()));
							if (i == prpLPadPayPersonNewVos.size()) {
								padPayPersonVo.setAddTaxValue(String.valueOf(new BigDecimal(writeBackVo.getSumAmountTax()).subtract(new BigDecimal(addTax)).setScale(2, BigDecimal.ROUND_HALF_UP)));
								padPayPersonVo.setNoTaxValue(String.valueOf(new BigDecimal(writeBackVo.getSumAmountNT()).subtract(new BigDecimal(addNoTax)).setScale(2, BigDecimal.ROUND_HALF_UP)));
							} else {
								BigDecimal rate = new BigDecimal("1.0000");
								double taxValue = sumAmountNT.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								double noTaxValue = sumNoTax.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								padPayPersonVo.setAddTaxValue(String.valueOf(taxValue));
								padPayPersonVo.setNoTaxValue(String.valueOf(noTaxValue));
								addTax = addTax + taxValue;
								addNoTax = addNoTax + noTaxValue;
							}
							i++;
							// 写入数据库
							//更新垫付
							padPayPubService.updatePadPay(padPayPersonVo);
					   }
				   }
			   }else{
				   if(CodeConstants.VatFeeType.CPAY.equals(writeBackVo.getFeeType())){//赔款
					// 写入charge 表  根据计算书号  和 序号 serialNo  更新PrplCharge
						PrpLCompensateVo compensateVo = compensateService.findCompByPK(writeBackVo.getBusinessNo());
						if (compensateVo == null) {
							throw new IllegalArgumentException("计算书号错误");
						}
						List<PrpLPaymentVo> paymentVoList = compensateVo.getPrpLPayments();
						List<PrpLPaymentVo> prpLPaymentNewVo = new ArrayList<PrpLPaymentVo>();

						//统计相同的费用类型和收款人的费用总额
						BigDecimal sumSumRealPay = BigDecimal.ZERO;
						for (PrpLPaymentVo paymentVo : paymentVoList) {
							if (paymentVo.getSerialNo().equals(writeBackVo.getSerialNo() + "")) {
								sumSumRealPay = sumSumRealPay.add(DataUtils.NullToZero(paymentVo.getSumRealPay()));
								prpLPaymentNewVo.add(paymentVo);
							}
						}
						int i = 1;// 序号
						BigDecimal sumAmountNT = new BigDecimal(writeBackVo.getSumAmountTax());// 不含税金额
						BigDecimal sumNoTax = new BigDecimal(writeBackVo.getSumAmountNT());// 税额
						double addTax = 0d;
						double addNoTax = 0d;
						for (PrpLPaymentVo prpLPaymentVo : prpLPaymentNewVo) {
							prpLPaymentVo.setInvoiceType(writeBackVo.getInvoicetype());
							prpLPaymentVo.setAddTaxRate(String.valueOf(writeBackVo.getTaxRate()));
							if (i == prpLPaymentNewVo.size()) {
								prpLPaymentVo.setAddTaxValue(String.valueOf(new BigDecimal(writeBackVo.getSumAmountTax()).subtract(new BigDecimal(addTax)).setScale(2, BigDecimal.ROUND_HALF_UP)));
								prpLPaymentVo.setNoTaxValue(String.valueOf(new BigDecimal(writeBackVo.getSumAmountNT()).subtract(new BigDecimal(addNoTax)).setScale(2, BigDecimal.ROUND_HALF_UP)));
							} else {
								BigDecimal sumRealPay = prpLPaymentVo.getSumRealPay();
								BigDecimal rate = sumRealPay.divide(sumSumRealPay, 4,BigDecimal.ROUND_HALF_UP);
								double taxValue = sumAmountNT.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								double noTaxValue = sumNoTax.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								logger.info(""+taxValue);
								prpLPaymentVo.setAddTaxValue(String.valueOf(taxValue));
								prpLPaymentVo.setNoTaxValue(String.valueOf(noTaxValue));
								addTax = addTax + taxValue;
								addNoTax = addNoTax + noTaxValue;
							}
							i++;
							// 写入数据库
							compensateService.updatePrpLPaymentVo(prpLPaymentVo);
						}
						
				   }else{
						// 写入charge 表  根据计算书号  和 序号 serialNo  更新PrplCharge
						PrpLCompensateVo compensateVo = compensateService.findCompByPK(writeBackVo.getBusinessNo());
						if (compensateVo == null) {
							throw new IllegalArgumentException("计算书号错误");
						}
						List<PrpLChargeVo> chargeList = compensateVo.getPrpLCharges();
						List<PrpLChargeVo> chargeNewList = new ArrayList<PrpLChargeVo>();

						//统计相同的费用类型和收款人的费用总额
						BigDecimal sumChargeFee = BigDecimal.ZERO;
						for (PrpLChargeVo chargeVo : chargeList) {
							if (chargeVo.getSerialNo().equals(writeBackVo.getSerialNo() + "")) {
								for (PrpLChargeVo chargeOldVo : chargeList) {
									if(chargeOldVo.getChargeCode().equals(chargeVo.getChargeCode())&&chargeOldVo.getPayeeId().equals(
											chargeVo.getPayeeId())){
										sumChargeFee = sumChargeFee.add(DataUtils.NullToZero(chargeOldVo.getFeeAmt()))
												.subtract(DataUtils.NullToZero(chargeOldVo.getOffAmt()))
												.subtract(DataUtils.NullToZero(chargeOldVo.getOffPreAmt()));
										chargeNewList.add(chargeOldVo);
									}
								}
							}
						}
						int i = 1;// 序号
						BigDecimal sumAmountNT = new BigDecimal(writeBackVo.getSumAmountTax());// 不含税金额
						BigDecimal sumNoTax = new BigDecimal(writeBackVo.getSumAmountNT());// 税额
						double addTax = 0d;
						double addNoTax = 0d;
						for (PrpLChargeVo chargeVo : chargeNewList) {
							chargeVo.setInvoiceType(writeBackVo.getInvoicetype());
							chargeVo.setAddTaxRate(String.valueOf(writeBackVo.getTaxRate()));
							if (i == chargeNewList.size()) {
								chargeVo.setAddTaxValue(String.valueOf(new BigDecimal(writeBackVo.getSumAmountTax()).subtract(new BigDecimal(addTax)).setScale(2, BigDecimal.ROUND_HALF_UP)));
								chargeVo.setNoTaxValue(String.valueOf(new BigDecimal(writeBackVo.getSumAmountNT()).subtract(new BigDecimal(addNoTax)).setScale(2, BigDecimal.ROUND_HALF_UP)));
							} else {
								BigDecimal chargeFee = chargeVo.getFeeAmt().subtract(chargeVo.getOffAmt()).subtract(DataUtils.NullToZero(chargeVo.getOffPreAmt()));
								BigDecimal rate = chargeFee.divide(sumChargeFee, 4,BigDecimal.ROUND_HALF_UP);
								double taxValue = sumAmountNT.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								double noTaxValue = sumNoTax.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								logger.info(""+taxValue);
								chargeVo.setAddTaxValue(String.valueOf(taxValue));
								chargeVo.setNoTaxValue(String.valueOf(noTaxValue));
								addTax = addTax + taxValue;
								addNoTax = addNoTax + noTaxValue;
							}
							i++;
							// 写入数据库
							compensateService.updatePrpLCharges(chargeVo);
						}
				   }
			   }
		   }
	   }
	   
	   
   }
	public void updatePrpLPrePay(BasePartReceiptTask writeBackVo,List<PrpLPrePayVo> prePayNewVoList,BigDecimal sumChargeFee){
		 int i = 1;// 序号
		   BigDecimal sumAmountNT = new BigDecimal(writeBackVo.getSumAmountTax());// 不含税金额
		   BigDecimal sumNoTax = new BigDecimal(writeBackVo.getSumAmountNT());// 税额
		   double addTax = 0d;
		   double addNoTax = 0d;
		   for(PrpLPrePayVo prepayVo:prePayNewVoList){
			   prepayVo.setInvoiceType(writeBackVo.getInvoicetype());
			   prepayVo.setAddTaxRate(String.valueOf(writeBackVo.getTaxRate()));
			   if (i == prePayNewVoList.size()) {
				   prepayVo.setAddTaxValue(String.valueOf(new BigDecimal(writeBackVo.getSumAmountTax()).subtract(new BigDecimal(addTax)).setScale(2, BigDecimal.ROUND_HALF_UP)));
				   prepayVo.setNoTaxValue(String.valueOf(new BigDecimal(writeBackVo.getSumAmountNT()).subtract(new BigDecimal(addNoTax)).setScale(2, BigDecimal.ROUND_HALF_UP)));
				}else {
					BigDecimal chargeFee = prepayVo.getPayAmt();
					BigDecimal rate = chargeFee.divide(sumChargeFee, 4,BigDecimal.ROUND_HALF_UP);
					double taxValue = sumAmountNT.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					double noTaxValue = sumNoTax.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					System.out.println(taxValue);
					prepayVo.setAddTaxValue(String.valueOf(taxValue));
					prepayVo.setNoTaxValue(String.valueOf(noTaxValue));
					addTax = addTax + taxValue;
					addNoTax = addNoTax + noTaxValue;
				}
				i++;
				compensateService.updatePrpLPrePay(prepayVo);
		   }
	}

}
