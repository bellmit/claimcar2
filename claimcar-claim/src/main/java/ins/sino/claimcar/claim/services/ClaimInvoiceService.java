package ins.sino.claimcar.claim.services;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.po.PrpLClaim;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.sinosoft.webservice.client.InvoiceService.InvoiceInputService;
import com.sinosoft.webservice.client.InvoiceService.InvoiceInputServiceProxy;
import com.sinosoft.webservice.client.vo.InvoicReceiptTaskVo;
import com.sinosoft.webservice.client.vo.InvoiceInputInfo;
import com.sinosoft.webservice.client.vo.ReturnInfo;
import com.sinosoft.webservice.client.vo.VatInputTaxInvoiceVo;
import com.sinosoft.webservice.client.vo.VatInputTaxTaskVo;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

@Service("ClaimInvoiceServiceImpl")
public class ClaimInvoiceService {

	@Autowired
	private CompensateTaskService compensateTaskService;

	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	ClaimInterfaceLogService logService;
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	ManagerService managerService;
	@Autowired
	ClaimInterfaceLogService interfaceLogService;

	private Logger logger = LoggerFactory.getLogger(ClaimInvoiceService.class);
	private static final String INVOICEINPUTSERVICE="invoiceInputService";
	// private static final String invoiceInput_url = SpringProperties.getProperty("invoiceInput_url");

	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimInvoiceService#inputInvoiceInfo(com.sinosoft.webservice.client.vo.InvoiceInputInfo, java.lang.String, ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo)
	 * @param invoiceInfo
	 * @param url
	 * @param logVo
	 * @return
	 * @throws RemoteException
	 */
	public ReturnInfo inputInvoiceInfo(InvoiceInputInfo invoiceInfo,String url,ClaimInterfaceLogVo logVo) throws Exception {
		InvoiceInputService invoiceService = this.initProxyService(url);
		ReturnInfo returnInfo = new ReturnInfo();
		try {
			XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
			stream.autodetectAnnotations(true);
			stream.setMode(XStream.NO_REFERENCES);
			stream.aliasSystemAttribute(null,"class");// 去掉 class属性
			String requestXml = stream.toXML(invoiceInfo);
			logger.info("计算书号：" + logVo.getCompensateNo() + "，推送发票地址：" + url + "，送发票报文============\n" + requestXml);
			logVo.setRequestXml(requestXml); // 请求Vo翻译成xml
			logVo.setFlag(invoiceInfo.getInvoicReceiptTaskVo().getSerialNo()+"");
			returnInfo = invoiceService.inputInvoiceInfo(invoiceInfo);
			logVo.setResponseXml(stream.toXML(returnInfo));
			System.out.println(stream.toXML(returnInfo));
			logger.info("计算书号：" + logVo.getCompensateNo() + "，推送发票返回状态:" + returnInfo.isResponseCode());
			if(returnInfo.isResponseCode()){
				logVo.setStatus("1");
			}else{
				logVo.setStatus("0");
			}
			logVo.setErrorMessage(returnInfo.getErrorMessage());
			logVo.setErrorCode(returnInfo.isResponseCode()+"");
		} catch (Exception e) {
			logger.info("计算书号：" + logVo.getCompensateNo() + "送发票错误信息：", e);
			if (returnInfo.isResponseCode()) {
				logVo.setStatus("1");
			} else {
				logVo.setStatus("0");
			}
			logVo.setErrorMessage(returnInfo.getErrorMessage());
			logVo.setErrorCode(returnInfo.isResponseCode()+"");
			logger.info("计算书号：" + logVo.getCompensateNo() + " ErrorMessage:" + returnInfo.getErrorMessage());

		} finally {
			//保存日志
			try {
				logger.info("计算书号："+logVo.getCompensateNo()+"，推送VAT发票系统之后保存日志记录 start!");
				interfaceLogService.save(logVo);
				logger.info("计算书号："+logVo.getCompensateNo()+"，推送VAT发票系统之后保存日志记录 end!");
			} catch (Exception e) {
				logger.info("计算书号："+logVo.getCompensateNo()+"，推送VAT发票系统之后保存日志记录异常!", e);
				throw e;
			}
		}
		return returnInfo;
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimInvoiceService#inputInvoiceInfo(com.sinosoft.webservice.client.vo.InvoiceInputInfo, ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo)
	 * @param invoiceInfo
	 * @param logVo
	 * @return
	 * @throws Exception
	 */
	public ReturnInfo inputInvoiceInfo(InvoiceInputInfo invoiceInfo,ClaimInterfaceLogVo logVo) throws Exception {
		String invoiceInput_url = logVo.getRequestUrl();

		if(invoiceInput_url==null){
			invoiceInput_url = SpringProperties.getProperty("invoiceInput_url");
			System.out.println(invoiceInput_url);

			if(invoiceInput_url==null){
				logger.error("未配置进项税地址，不调用接口。");
				throw new Exception("未配置进项税服务地址。");
			}

		} // 10.236.0.127:8090
		logVo.setRequestUrl(invoiceInput_url);
		return inputInvoiceInfo(invoiceInfo,invoiceInput_url,logVo);
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimInvoiceService#getInvoiceInfoByCertiNo(java.lang.String, java.lang.String)
	 * @param certiNo
	 * @param certiType
	 * @return
	 * @throws Exception
	 */
	public List<VatInputTaxInvoiceVo> getInvoiceInfoByCertiNo(String certiNo,java.lang.String certiType) throws Exception {
		InvoiceInputService invoiceInputService = this.initProxyService();
		return invoiceInputService.getInvoiceInfoByCertiNo(certiNo,certiType);
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimInvoiceService#deleteInvoiceByCertiNo(java.lang.String, java.lang.String)
	 * @param certiNo
	 * @param certiType
	 * @throws Exception
	 */
	public void deleteInvoiceByCertiNo(String certiNo,String certiType) throws Exception {
		InvoiceInputService invoiceInputService = this.initProxyService();
		invoiceInputService.deleteInvoiceByCertiNo(certiNo,certiType);
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimInvoiceService#getTaskInfoByCertiNo(java.lang.String, java.lang.String)
	 * @param certiNo
	 * @param certiType
	 * @return
	 * @throws Exception
	 */
	public List<VatInputTaxTaskVo> getTaskInfoByCertiNo(String certiNo,String certiType) throws Exception {
		InvoiceInputService invoiceInputService = this.initProxyService();
		return invoiceInputService.getTaskInfoByCertiNo(certiNo,certiType);
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimInvoiceService#updateInvoiceStatus(java.lang.String, java.lang.String)
	 * @param certiNo
	 * @param certiType
	 * @throws Exception
	 */
	public void updateInvoiceStatus(String certiNo,String certiType) throws Exception {
		InvoiceInputService invoiceInputService = this.initProxyService();
		invoiceInputService.updateInvoiceStatus(certiNo,certiType);
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimInvoiceService#updateClaimInvoiceStatus(java.lang.String, java.math.BigDecimal)
	 * @param certiNo
	 * @param serialNo
	 * @throws Exception
	 */
	public void updateClaimInvoiceStatus(String certiNo,BigDecimal serialNo) throws Exception {
		InvoiceInputService invoiceInputService = this.initProxyService();
		invoiceInputService.updateClaimInvoiceStatus(certiNo,serialNo);
	}

	/**
	 * 初始化默认URL的webservice服务
	 * @return
	 * @modified: ☆qianxin(2016年8月16日 下午4:21:09): <br>
	 */
	private InvoiceInputService initProxyService() {
		// String url = AppConfig.get("sysconst.VAT_INPUT_WEBSERVICE_URL");
		String invoiceInput_url = SpringProperties.getProperty("invoiceInput_url");
		InvoiceInputService invoiceServiceProxy = new InvoiceInputServiceProxy(invoiceInput_url);
		return invoiceServiceProxy;
	}

	/**
	 * 初始化传入URL的webservice服务
	 * @param url
	 * @return
	 * @modified: ☆qianxin(2016年8月16日 下午4:21:41): <br>
	 */
	private InvoiceInputService initProxyService(String url) {
		InvoiceInputService invoiceServiceProxy = new InvoiceInputServiceProxy(url);
		return invoiceServiceProxy;
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimInvoiceService#pushCharge(java.lang.String)
	 * @param compensateNo 计算书号
	 * @param serialNoSend 需要送发票的费用的序列号，可为空，为空时送该计算书所有费用，不为空只送某一条费用
	 * @return
	 */
	public boolean pushCharge(String compensateNo,String serialNoSend) {
		ReturnInfo returnInfo = new ReturnInfo();
		try{
			PrpLCompensateVo compeVo = compensateTaskService.findPrpLCompensateVoByPK(compensateNo);
			String isWriteOff = compeVo.getPrpLCompensateExt().getWriteOffFlag();
			List<PrpLChargeVo> prpLchargeList = compeVo.getPrpLCharges();
			boolean flag = true;

			List<InvoiceInputInfo> claimInvoiceInfoList = new ArrayList<InvoiceInputInfo>();
			if(prpLchargeList!=null&&prpLchargeList.size()>0){
				Map<String,PrpLChargeVo> chargeMap = new HashMap<String,PrpLChargeVo>();
				// 合并相同的费用类型和收款人payeeid
				for(PrpLChargeVo chargeVo:compeVo.getPrpLCharges()){
					String key = chargeVo.getPayeeId()+chargeVo.getChargeCode();
					if(chargeMap.containsKey(key)){
						PrpLChargeVo chargeOldVo = chargeMap.get(key);
						BigDecimal sumFeeAmt = chargeVo.getFeeAmt().add(chargeOldVo.getFeeAmt());// 实付金额 （包含预付的）
						BigDecimal sumOffAmt = chargeVo.getOffAmt().add(chargeOldVo.getOffAmt());// 扣减金额
						chargeOldVo.setFeeAmt(sumFeeAmt);
						chargeOldVo.setOffAmt(sumOffAmt);
						Integer serialNo = Integer.parseInt(chargeVo.getSerialNo());
						Integer oldSerialNo = Integer.parseInt(chargeOldVo.getSerialNo());
						if(oldSerialNo>serialNo){// 取最小的serialno 保持送收付和送发票的serialno一致
							chargeOldVo.setSerialNo(chargeVo.getSerialNo());
						}

						chargeMap.put(key,chargeOldVo);
					}else{
						chargeMap.put(key,chargeVo);
					}
				}

				for(PrpLChargeVo prpLchargeDto:chargeMap.values()){
					InvoiceInputInfo claimInvoiceInfo = new InvoiceInputInfo();
					InvoicReceiptTaskVo invoicReceiptTaskVo = new InvoicReceiptTaskVo();
					String taskId = compensateNo;
					if(flag){
						invoicReceiptTaskVo.setTaskId(taskId);
						flag = false;
					}
					invoicReceiptTaskVo.setCertiNo(compensateNo);// 业务号
					invoicReceiptTaskVo.setSerialNo(new BigDecimal(prpLchargeDto.getSerialNo()));// 序列号
					invoicReceiptTaskVo.setCertiType("C");// 业务类型--默认费用
					invoicReceiptTaskVo.setUserCode(compeVo.getCreateUser());// 操作员
					invoicReceiptTaskVo.setComCode(compeVo.getComCode());// 归属机构

					// 重新组装费用类型 不匹配项 默认传 手续费
					String feeType = prpLchargeDto.getChargeCode();
					invoicReceiptTaskVo.setFeeType(CodeConstants.TransChargeMap.get(feeType));
					invoicReceiptTaskVo.setFeeName(codeTranService.transCode("ChargeCode",feeType));

					invoicReceiptTaskVo.setRiskCode(prpLchargeDto.getRiskCode());// 险种代码
					SysCodeDictVo sysVo1 = codeTranService.findTransCodeDictVo("CarRiskCode",prpLchargeDto.getRiskCode());
					invoicReceiptTaskVo.setRiskName(sysVo1.getCodeName());// 险种名称

					invoicReceiptTaskVo.setKindcode(prpLchargeDto.getKindCode());// 险别代码
					SysCodeDictVo sysVo2 = codeTranService.findTransCodeDictVo("KindCode",prpLchargeDto.getKindCode());
					invoicReceiptTaskVo.setKindName(sysVo2.getCodeName());// 险别名称

					invoicReceiptTaskVo.setCurrency(prpLchargeDto.getCurrency());// 币别

					invoicReceiptTaskVo.setInvoiceType(prpLchargeDto.getInvoiceType());// 发票类型
					// 費用金額 待定
					// 含税费用金额 ---改2016-12-22 加预付金额 减去扣减金额
					// 2018-04-28预付和理算分开送
					invoicReceiptTaskVo.setSumAmount(nullToZero(prpLchargeDto.getFeeAmt()).subtract(nullToZero(prpLchargeDto.getOffAmt())).subtract(
							nullToZero(prpLchargeDto.getOffPreAmt())));
					invoicReceiptTaskVo.setSumRegAmount(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记含税金额
					invoicReceiptTaskVo.setSumRegAmountNT(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记不含税金额
					invoicReceiptTaskVo.setSumRegVATAmount(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记增值税额
					invoicReceiptTaskVo.setSumRegVATRate(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记增值税率
					invoicReceiptTaskVo.setSystemCode("newClaim");
					if("0".equals(isWriteOff)){ // 案件冲销送红色 其他送蓝色
						invoicReceiptTaskVo.setInvoiceFlag("1");
					}else{
						invoicReceiptTaskVo.setInvoiceFlag("2");
						String invoiceType = invoicReceiptTaskVo.getInvoiceType();
						if("004".equals(invoiceType)){ // 冲销改变发票颜色
							invoicReceiptTaskVo.setInvoiceType("001");
						}
						if("007".equals(invoiceType)){
							invoicReceiptTaskVo.setInvoiceType("002");
						}
					}
					claimInvoiceInfo.setInvoicReceiptTaskVo(invoicReceiptTaskVo);
					claimInvoiceInfoList.add(claimInvoiceInfo);
				}
			}
			ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
			logVo.setRegistNo(compeVo.getRegistNo());
			logVo.setBusinessType(BusinessType.INVOICE_VAT.getCode());
			logVo.setBusinessName(BusinessType.INVOICE_VAT.getName());
			logVo.setServiceType(INVOICEINPUTSERVICE);
			logVo.setComCode(compeVo.getComCode());
			logVo.setClaimNo(compeVo.getClaimNo());
			logVo.setCompensateNo(compensateNo);
			logVo.setCreateUser(compeVo.getCreateUser());
			logVo.setRequestTime(new Date());
			logVo.setCreateTime(new Date());
			logVo.setOperateNode(CodeConstants.VClaimName.VC_ADOPT);
			String invoiceInput_url = SpringProperties.getProperty("invoiceInput_url");// invoiceInput_url
			logger.info("计算书号：" + compensateNo + "，送发票VAT接口地址invoiceInput_url：" + invoiceInput_url);
			logVo.setRequestUrl(invoiceInput_url);
			String policyCom = policyViewService.getPolicyComCode(compeVo.getRegistNo());
			PrpLConfigValueVo configValuePayToVatVo = ConfigUtil.findConfigByCode(CodeConstants.PAYTOVAT,policyCom);
            if(configValuePayToVatVo != null && CodeConstants.IsSingleAccident.YES.equals(configValuePayToVatVo.getConfigValue())){
            	//赔款
				claimInvoiceInfoList = pushReparation(claimInvoiceInfoList,compeVo);
            }
			for(InvoiceInputInfo claimInvoiceInfo:claimInvoiceInfoList){
				if ((!StringUtils.isBlank(serialNoSend)
						&& serialNoSend.equals(claimInvoiceInfo.getInvoicReceiptTaskVo().getSerialNo().toString()))
						|| StringUtils.isBlank(serialNoSend)) {
					// serialNoSend 需要送发票的费用的序列号，可为空，为空时送该计算书所有费用，不为空只送某一条费用
					try {
						if (claimInvoiceInfo.getInvoicReceiptTaskVo() != null
								&& !BigDecimal.ZERO.equals(claimInvoiceInfo.getInvoicReceiptTaskVo().getSumAmount())) {
							returnInfo = this.inputInvoiceInfo(claimInvoiceInfo, logVo);
						} else {
							logger.info("计算书号：" + compensateNo + "，进项税金额为0！系统时间：" + new Date());
							returnInfo.setResponseCode(false);
						}
					} catch (Exception e) {
						logger.info("营改增发送日志传送失败", e);
						logVo.setErrorMessage(e.getMessage());
						logService.save(logVo);
					}
				}
			}
		}catch(Exception e){
			logger.info("营改增发送必传数据不能为空：",e);
		}
		return returnInfo.isResponseCode();
	}
	/*
	 * @see ins.sino.claimcar.claim.services.ClaimInvoiceService#pushCharge(java.lang.String)
	 * @param compensateNo 计算书号
	 * @param serialNoSend 需要送发票的费用的序列号，可为空，为空时送该计算书所有费用，不为空只送某一条费用
	 * @return
	 */
	public boolean pingAnPushCharge(PrpLCompensateVo compeVo,String serialNoSend) {
		ReturnInfo returnInfo = new ReturnInfo();
		try{
			String compensateNo = compeVo.getCompensateNo();
			String isWriteOff = compeVo.getPrpLCompensateExt().getWriteOffFlag();
			List<PrpLChargeVo> prpLchargeList = compeVo.getPrpLCharges();
			boolean flag = true;

			List<InvoiceInputInfo> claimInvoiceInfoList = new ArrayList<InvoiceInputInfo>();
			if(prpLchargeList!=null&&prpLchargeList.size()>0){
				Map<String,PrpLChargeVo> chargeMap = new HashMap<String,PrpLChargeVo>();
				// 合并相同的费用类型和收款人payeeid
				for(PrpLChargeVo chargeVo:compeVo.getPrpLCharges()){
					String key = chargeVo.getPayeeId()+chargeVo.getChargeCode();
					if(chargeMap.containsKey(key)){
						PrpLChargeVo chargeOldVo = chargeMap.get(key);
						BigDecimal sumFeeAmt = chargeVo.getFeeAmt().add(chargeOldVo.getFeeAmt());// 实付金额 （包含预付的）
						BigDecimal sumOffAmt = chargeVo.getOffAmt().add(chargeOldVo.getOffAmt());// 扣减金额
						chargeOldVo.setFeeAmt(sumFeeAmt);
						chargeOldVo.setOffAmt(sumOffAmt);
						Integer serialNo = Integer.parseInt(chargeVo.getSerialNo());
						Integer oldSerialNo = Integer.parseInt(chargeOldVo.getSerialNo());
						if(oldSerialNo>serialNo){// 取最小的serialno 保持送收付和送发票的serialno一致
							chargeOldVo.setSerialNo(chargeVo.getSerialNo());
						}

						chargeMap.put(key,chargeOldVo);
					}else{
						chargeMap.put(key,chargeVo);
					}
				}

				for(PrpLChargeVo prpLchargeDto:chargeMap.values()){
					InvoiceInputInfo claimInvoiceInfo = new InvoiceInputInfo();
					InvoicReceiptTaskVo invoicReceiptTaskVo = new InvoicReceiptTaskVo();
					String taskId = compensateNo;
					if(flag){
						invoicReceiptTaskVo.setTaskId(taskId);
						flag = false;
					}
					invoicReceiptTaskVo.setCertiNo(compensateNo);// 业务号
					invoicReceiptTaskVo.setSerialNo(new BigDecimal(prpLchargeDto.getSerialNo()));// 序列号
					invoicReceiptTaskVo.setCertiType("C");// 业务类型--默认费用
					invoicReceiptTaskVo.setUserCode(compeVo.getCreateUser());// 操作员
					invoicReceiptTaskVo.setComCode(compeVo.getComCode());// 归属机构

					// 重新组装费用类型 不匹配项 默认传 手续费
					String feeType = prpLchargeDto.getChargeCode();
					invoicReceiptTaskVo.setFeeType(CodeConstants.TransChargeMap.get(feeType));
					invoicReceiptTaskVo.setFeeName(codeTranService.transCode("ChargeCode",feeType));

					invoicReceiptTaskVo.setRiskCode(prpLchargeDto.getRiskCode());// 险种代码
					SysCodeDictVo sysVo1 = codeTranService.findTransCodeDictVo("CarRiskCode",prpLchargeDto.getRiskCode());
					invoicReceiptTaskVo.setRiskName(sysVo1.getCodeName());// 险种名称

					invoicReceiptTaskVo.setKindcode(prpLchargeDto.getKindCode());// 险别代码
					SysCodeDictVo sysVo2 = codeTranService.findTransCodeDictVo("KindCode",prpLchargeDto.getKindCode());
					invoicReceiptTaskVo.setKindName(sysVo2.getCodeName());// 险别名称

					invoicReceiptTaskVo.setCurrency(prpLchargeDto.getCurrency());// 币别

					invoicReceiptTaskVo.setInvoiceType(prpLchargeDto.getInvoiceType());// 发票类型
					// 費用金額 待定
					// 含税费用金额 ---改2016-12-22 加预付金额 减去扣减金额
					// 2018-04-28预付和理算分开送
					invoicReceiptTaskVo.setSumAmount(nullToZero(prpLchargeDto.getFeeAmt()).subtract(nullToZero(prpLchargeDto.getOffAmt())).subtract(
							nullToZero(prpLchargeDto.getOffPreAmt())));
					invoicReceiptTaskVo.setSumRegAmount(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记含税金额
					invoicReceiptTaskVo.setSumRegAmountNT(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记不含税金额
					invoicReceiptTaskVo.setSumRegVATAmount(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记增值税额
					invoicReceiptTaskVo.setSumRegVATRate(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记增值税率
					invoicReceiptTaskVo.setSystemCode("newClaim");
					if("0".equals(isWriteOff)){ // 案件冲销送红色 其他送蓝色
						invoicReceiptTaskVo.setInvoiceFlag("1");
					}else{
						invoicReceiptTaskVo.setInvoiceFlag("2");
						String invoiceType = invoicReceiptTaskVo.getInvoiceType();
						if("004".equals(invoiceType)){ // 冲销改变发票颜色
							invoicReceiptTaskVo.setInvoiceType("001");
						}
						if("007".equals(invoiceType)){
							invoicReceiptTaskVo.setInvoiceType("002");
						}
					}
					claimInvoiceInfo.setInvoicReceiptTaskVo(invoicReceiptTaskVo);
					claimInvoiceInfoList.add(claimInvoiceInfo);
				}
			}
			ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
			logVo.setRegistNo(compeVo.getRegistNo());
			logVo.setBusinessType(BusinessType.INVOICE_VAT.getCode());
			logVo.setBusinessName(BusinessType.INVOICE_VAT.getName());
			logVo.setServiceType(INVOICEINPUTSERVICE);
			logVo.setComCode(compeVo.getComCode());
			logVo.setClaimNo(compeVo.getClaimNo());
			logVo.setCompensateNo(compensateNo);
			logVo.setCreateUser(compeVo.getCreateUser());
			logVo.setRequestTime(new Date());
			logVo.setCreateTime(new Date());
			logVo.setOperateNode(CodeConstants.VClaimName.VC_ADOPT);
			String invoiceInput_url = SpringProperties.getProperty("invoiceInput_url");// invoiceInput_url
			logger.info("计算书号：" + compensateNo + "，送发票VAT接口地址invoiceInput_url：" + invoiceInput_url);
			logVo.setRequestUrl(invoiceInput_url);
			String policyCom = policyViewService.getPolicyComCode(compeVo.getRegistNo());
			PrpLConfigValueVo configValuePayToVatVo = ConfigUtil.findConfigByCode(CodeConstants.PAYTOVAT,policyCom);
			if(configValuePayToVatVo != null && CodeConstants.IsSingleAccident.YES.equals(configValuePayToVatVo.getConfigValue())){
				//赔款
				claimInvoiceInfoList = pushReparation(claimInvoiceInfoList,compeVo);
			}
			for(InvoiceInputInfo claimInvoiceInfo:claimInvoiceInfoList){
				if ((!StringUtils.isBlank(serialNoSend)
						&& serialNoSend.equals(claimInvoiceInfo.getInvoicReceiptTaskVo().getSerialNo().toString()))
						|| StringUtils.isBlank(serialNoSend)) {
					// serialNoSend 需要送发票的费用的序列号，可为空，为空时送该计算书所有费用，不为空只送某一条费用
					try {
						if (claimInvoiceInfo.getInvoicReceiptTaskVo() != null
								&& !BigDecimal.ZERO.equals(claimInvoiceInfo.getInvoicReceiptTaskVo().getSumAmount())) {
							returnInfo = this.inputInvoiceInfo(claimInvoiceInfo, logVo);
						} else {
							logger.info("计算书号：" + compensateNo + "，进项税金额为0！系统时间：" + new Date());
							returnInfo.setResponseCode(false);
						}
					} catch (Exception e) {
						logger.info("营改增发送日志传送失败", e);
						logVo.setErrorMessage(e.getMessage());
						logService.save(logVo);
					}
				}
			}
		}catch(Exception e){
			logger.info("营改增发送必传数据不能为空：",e);
		}
		return returnInfo.isResponseCode();
	}
	public boolean pushPreCharge(String compensateNo,String serialNoSend) {
		ReturnInfo returnInfo = new ReturnInfo();
		try{
			PrpLCompensateVo compeVo = compensateTaskService.findPrpLCompensateVoByPK(compensateNo);
			String isWriteOff = compeVo.getPrpLCompensateExt().getWriteOffFlag();
			List<PrpLPrePayVo> prePayList = compensateTaskService.getPrePayVo(compensateNo,"F");
			boolean flag = true;
			List<InvoiceInputInfo> claimInvoiceInfoList = new ArrayList<InvoiceInputInfo>();
			if(prePayList!=null&&prePayList.size()>0){
				Map<String,PrpLPrePayVo> prePayMap = new HashMap<String,PrpLPrePayVo>();
				// 合并相同的费用类型和收款人payeeid
				for(PrpLPrePayVo prePayVo:prePayList){
					String key = prePayVo.getPayeeId()+prePayVo.getChargeCode();
					if(prePayMap.containsKey(key)){
						PrpLPrePayVo chargeOldVo = prePayMap.get(key);
						// prpLchargeDto.getFeeAmt().subtract(prpLchargeDto.getOffAmt()
						BigDecimal payAmt = prePayVo.getPayAmt().add(chargeOldVo.getPayAmt());
						chargeOldVo.setPayAmt(payAmt);
						Integer serialNo = Integer.parseInt(prePayVo.getSerialNo());
						Integer oldSerialNo = Integer.parseInt(chargeOldVo.getSerialNo());
						if(oldSerialNo>serialNo){// 取最小的serialno 保持送收付和送发票的serialno一致
							chargeOldVo.setSerialNo(prePayVo.getSerialNo());
						}

						prePayMap.put(key,chargeOldVo);
					}else{
						prePayMap.put(key,prePayVo);
					}
				}

				for(PrpLPrePayVo prpLPrePayDto:prePayMap.values()){
					InvoiceInputInfo claimInvoiceInfo = new InvoiceInputInfo();
					InvoicReceiptTaskVo invoicReceiptTaskVo = new InvoicReceiptTaskVo();
					String taskId = compensateNo;
					if(flag){
						invoicReceiptTaskVo.setTaskId(taskId);
						flag = false;
					}
					invoicReceiptTaskVo.setCertiNo(compensateNo);// 业务号
					// 重新组装费用类型 不匹配项 默认传 手续费
					String feeType = prpLPrePayDto.getChargeCode();
					invoicReceiptTaskVo.setFeeType(CodeConstants.TransChargeMap.get(feeType));
					invoicReceiptTaskVo.setFeeName(codeTranService.transCode("ChargeCode",feeType));
					invoicReceiptTaskVo = setInvoicReceiptTaskVo(invoicReceiptTaskVo,prpLPrePayDto,compeVo,isWriteOff);
					claimInvoiceInfo.setInvoicReceiptTaskVo(invoicReceiptTaskVo);
					claimInvoiceInfoList.add(claimInvoiceInfo);
				}
			}
			//赔款
			String policyCom = policyViewService.getPolicyComCode(compeVo.getRegistNo());
			PrpLConfigValueVo configValuePayToVatVo = ConfigUtil.findConfigByCode(CodeConstants.PAYTOVAT,policyCom);
            if(configValuePayToVatVo != null && CodeConstants.IsSingleAccident.YES.equals(configValuePayToVatVo.getConfigValue())){
				List<PrpLPrePayVo> prpLPrePayVoList = compensateTaskService.getPrePayVo(compeVo.getCompensateNo(),"P");
				//boolean flag = true;
				if(prpLPrePayVoList != null && prpLPrePayVoList.size() > 0){
					for(PrpLPrePayVo prpLPrePayDto : prpLPrePayVoList){
						PrpLPayCustomVo customVo = managerService.findPayCustomVoById(prpLPrePayDto.getPayeeId());
						if(CodeConstants.PayObjectKind.GARAGE.equals(customVo.getPayObjectKind())){//收款人类型为修理厂时，将赔款数据推送给VAT进项税系统，用于发票抵扣。预付赔款也可以进行发票抵扣。
							InvoiceInputInfo claimInvoiceInfo = new InvoiceInputInfo();
							InvoicReceiptTaskVo invoicReceiptTaskVo = new InvoicReceiptTaskVo();
							/*String taskId = compensateNo;
							if(flag){
								invoicReceiptTaskVo.setTaskId(taskId);
								flag = false;
							}*/
							invoicReceiptTaskVo.setCertiNo(compensateNo);// 业务号
							invoicReceiptTaskVo.setFeeType(CodeConstants.VatFeeType.YPAY);
							invoicReceiptTaskVo.setFeeName(CodeConstants.TransVatFeeTypeMap.get(CodeConstants.VatFeeType.YPAY));
							invoicReceiptTaskVo = setInvoicReceiptTaskVo(invoicReceiptTaskVo,prpLPrePayDto,compeVo,isWriteOff);
							claimInvoiceInfo.setInvoicReceiptTaskVo(invoicReceiptTaskVo);
							claimInvoiceInfoList.add(claimInvoiceInfo);
						}
					}
				}
            }
			
			ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
			logVo.setRegistNo(compeVo.getRegistNo());
			logVo.setBusinessType(BusinessType.INVOICE_VAT.getCode());
			logVo.setBusinessName(BusinessType.INVOICE_VAT.getName());
			logVo.setServiceType(INVOICEINPUTSERVICE);
			logVo.setComCode(compeVo.getComCode());
			logVo.setClaimNo(compeVo.getClaimNo());
			logVo.setCompensateNo(compensateNo);
			logVo.setCreateUser(compeVo.getCreateUser());
			logVo.setRequestTime(new Date());
			logVo.setCreateTime(new Date());
			logVo.setOperateNode(CodeConstants.VClaimName.VC_ADOPT);
			String invoiceInput_url = SpringProperties.getProperty("invoiceInput_url");// invoiceInput_url
			logger.info("invoiceInput_url"+invoiceInput_url);
			logVo.setRequestUrl(invoiceInput_url);

			for(InvoiceInputInfo claimInvoiceInfo:claimInvoiceInfoList){
				if(( !StringUtils.isBlank(serialNoSend)&&serialNoSend.equals(claimInvoiceInfo.getInvoicReceiptTaskVo().getSerialNo().toString()) )||StringUtils
						.isBlank(serialNoSend)){
					// serialNoSend 需要送发票的费用的序列号，可为空，为空时送该计算书所有费用，不为空只送某一条费用
					try{
						if(claimInvoiceInfo.getInvoicReceiptTaskVo()!=null&& !BigDecimal.ZERO.equals(claimInvoiceInfo.getInvoicReceiptTaskVo()
								.getSumAmount())){
							returnInfo = this.inputInvoiceInfo(claimInvoiceInfo,logVo);
						}else{
							logger.info("进项税金额为0！");
							returnInfo.setResponseCode(false);
						}
					}catch(Exception e){
						logger.error("营改增发送日志传送失败",e);
						logVo.setErrorMessage(e.getMessage());
						logService.save(logVo);
					}
				}
			}
		}catch(Exception e){
			logger.error("营改增发送必传数据不能为空：",e);
		}
		return returnInfo.isResponseCode();
	}

	public boolean pushAssessorFee(PrpLAssessorFeeVo assessorVo,SysUserVo userVo) {
		ReturnInfo returnInfo = new ReturnInfo();
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		String logFlag="0";
		try{
			List<PrpLCMainVo> prpLCmainList = policyViewService.findPrpLCMainVoListByRegistNo(assessorVo.getRegistNo());
			InvoiceInputInfo claimInvoiceInfo = new InvoiceInputInfo();
			InvoicReceiptTaskVo invoicReceiptTaskVo = new InvoicReceiptTaskVo();

			invoicReceiptTaskVo.setTaskId(assessorVo.getCompensateNo());
			invoicReceiptTaskVo.setCertiNo(assessorVo.getCompensateNo());// 业务号
			invoicReceiptTaskVo.setSerialNo(new BigDecimal(0));// 序列号
			invoicReceiptTaskVo.setCertiType("C");// 业务类型-
			invoicReceiptTaskVo.setUserCode(userVo.getUserCode());// 操作员
			if(prpLCmainList!=null && prpLCmainList.size()>0){
				invoicReceiptTaskVo.setComCode(prpLCmainList.get(0).getComCode());// 归属机构
			}

			invoicReceiptTaskVo.setFeeType("13");// 费用类型代码
			invoicReceiptTaskVo.setFeeName("公估费");// 费用名称
			if(StringUtils.isNotBlank(assessorVo.getClaimNo())){
				PrpLClaimVo claimVo = this.findClaimVo(assessorVo.getClaimNo());
				if(claimVo!=null){
					invoicReceiptTaskVo.setRiskCode(claimVo.getRiskCode());// 险种代码
					SysCodeDictVo sysVo1 = codeTranService.findTransCodeDictVo("CarRiskCode",claimVo.getRiskCode());
					invoicReceiptTaskVo.setRiskName(sysVo1.getCodeName());// 险种名称
				}
			}

			// invoicReceiptTaskVo.setRiskName("机动车辆保险特种车保险");//险种名
			invoicReceiptTaskVo.setKindcode(assessorVo.getKindCode());// 险别代码
			SysCodeDictVo sysVo2 = codeTranService.findTransCodeDictVo("KindCode",assessorVo.getKindCode());
			invoicReceiptTaskVo.setKindName(sysVo2.getCodeName());// 险别名称
			invoicReceiptTaskVo.setCurrency("CNY");// 币别
			invoicReceiptTaskVo.setInvoiceType("000");// 发票类型
			invoicReceiptTaskVo.setSumAmount(assessorVo.getAmount());
			invoicReceiptTaskVo.setSystemCode("newClaim");
			invoicReceiptTaskVo.setInvoiceFlag("1");
			claimInvoiceInfo.setInvoicReceiptTaskVo(invoicReceiptTaskVo);

			logVo.setRegistNo(assessorVo.getRegistNo());
			logVo.setBusinessType(BusinessType.AssessorFee_Invoice.name());
			logVo.setBusinessName(BusinessType.AssessorFee_Invoice.getName());
			logVo.setServiceType("invoiceInputService");
			logVo.setComCode(userVo.getComCode());
			logVo.setClaimNo(assessorVo.getClaimNo());
			logVo.setCompensateNo(assessorVo.getCompensateNo());
			logVo.setCreateUser(userVo.getUserCode());
			logVo.setRequestTime(new Date());
			logVo.setCreateTime(new Date());
			logVo.setOperateNode("公估审核通过");
			String invoiceInput_url = SpringProperties.getProperty("invoiceInput_url");// invoiceInput_url
			System.out.println(invoiceInput_url);
			logVo.setRequestUrl(invoiceInput_url);
			if( !BigDecimal.ZERO.equals(claimInvoiceInfo.getInvoicReceiptTaskVo().getSumAmount())){
				returnInfo = this.inputInvoiceInfo(claimInvoiceInfo,logVo);
				logFlag="1";
			}else{
				System.out.println("进项税金额为0！");
				logVo.setErrorCode("false");
				logVo.setErrorMessage("进项税金额为0！");
				returnInfo.setResponseCode(false);
			}
		}catch(Exception e){
			System.out.println("公估费送发票失败："+e.getMessage());
			logVo.setErrorMessage("公估费送发票失败："+e.getMessage());
		}
		finally{
			if("0".equals(logFlag)){
				logService.save(logVo);
			}
			
		}
		return returnInfo.isResponseCode();
	}
	//查勘费送发票
	public boolean pushCheckFee(PrpLCheckFeeVo checkFeeVo,SysUserVo userVo) {
		ReturnInfo returnInfo = new ReturnInfo();
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		String logFlag="0";
		try{
			List<PrpLCMainVo> prpLCmainList = policyViewService.findPrpLCMainVoListByRegistNo(checkFeeVo.getRegistNo());
			InvoiceInputInfo claimInvoiceInfo = new InvoiceInputInfo();
			InvoicReceiptTaskVo invoicReceiptTaskVo = new InvoicReceiptTaskVo();

			invoicReceiptTaskVo.setTaskId(checkFeeVo.getCompensateNo());
			invoicReceiptTaskVo.setCertiNo(checkFeeVo.getCompensateNo());// 业务号
			invoicReceiptTaskVo.setSerialNo(new BigDecimal(0));// 序列号
			invoicReceiptTaskVo.setCertiType("C");// 业务类型-
			invoicReceiptTaskVo.setUserCode(userVo.getUserCode());// 操作员
			if(prpLCmainList!=null && prpLCmainList.size()>0){
				invoicReceiptTaskVo.setComCode(prpLCmainList.get(0).getComCode());// 归属机构
			}

			invoicReceiptTaskVo.setFeeType("04");// 费用类型代码
			invoicReceiptTaskVo.setFeeName("查勘费");// 费用名称
			if(StringUtils.isNotBlank(checkFeeVo.getClaimNo())){
				PrpLClaimVo claimVo = this.findClaimVo(checkFeeVo.getClaimNo());
				if(claimVo!=null){
					invoicReceiptTaskVo.setRiskCode(claimVo.getRiskCode());// 险种代码
					SysCodeDictVo sysVo1 = codeTranService.findTransCodeDictVo("CarRiskCode",claimVo.getRiskCode());
					invoicReceiptTaskVo.setRiskName(sysVo1.getCodeName());// 险种名称
				}
			}

			// invoicReceiptTaskVo.setRiskName("机动车辆保险特种车保险");//险种名
			invoicReceiptTaskVo.setKindcode(checkFeeVo.getKindCode());// 险别代码
			SysCodeDictVo sysVo2 = codeTranService.findTransCodeDictVo("KindCode",checkFeeVo.getKindCode());
			invoicReceiptTaskVo.setKindName(sysVo2.getCodeName());// 险别名称
			invoicReceiptTaskVo.setCurrency("CNY");// 币别
			invoicReceiptTaskVo.setInvoiceType("000");// 发票类型
			invoicReceiptTaskVo.setSumAmount(checkFeeVo.getAmount());
			invoicReceiptTaskVo.setSystemCode("newClaim");
			invoicReceiptTaskVo.setInvoiceFlag("1");
			claimInvoiceInfo.setInvoicReceiptTaskVo(invoicReceiptTaskVo);

			logVo.setRegistNo(checkFeeVo.getRegistNo());
			logVo.setBusinessType(BusinessType.CheckFee_Invoice.name());
			logVo.setBusinessName(BusinessType.CheckFee_Invoice.getName());
			logVo.setServiceType("invoiceInputService");
			logVo.setComCode(userVo.getComCode());
			logVo.setClaimNo(checkFeeVo.getClaimNo());
			logVo.setCompensateNo(checkFeeVo.getCompensateNo());
			logVo.setCreateUser(userVo.getUserCode());
			logVo.setRequestTime(new Date());
			logVo.setCreateTime(new Date());
			logVo.setOperateNode("查勘审核通过");
			String invoiceInput_url = SpringProperties.getProperty("invoiceInput_url");// invoiceInput_url
			System.out.println(invoiceInput_url);
			logVo.setRequestUrl(invoiceInput_url);
			if( !BigDecimal.ZERO.equals(claimInvoiceInfo.getInvoicReceiptTaskVo().getSumAmount())){
				returnInfo = this.inputInvoiceInfo(claimInvoiceInfo,logVo);
				logFlag="1";
			}else{
				System.out.println("进项税金额为0！");
				logVo.setErrorCode("false");
				logVo.setErrorMessage("进项税金额为0！");
				returnInfo.setResponseCode(false);
			}
		}catch(Exception e){
			System.out.println("查勘费送发票失败："+e.getMessage());
			logVo.setErrorMessage("查勘费送发票失败："+e.getMessage());
		}
		finally{
			if("0".equals(logFlag)){
				logService.save(logVo);
			}
			
		}
		return returnInfo.isResponseCode();
	}

	
	public PrpLClaimVo findClaimVo(String claimNo) {
		Assert.notNull(claimNo,"registNo must have value.");
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("claimNo",claimNo);
		PrpLClaim po = databaseDao.findByPK(PrpLClaim.class,claimNo);
		PrpLClaimVo claimVo = Beans.copyDepth().from(po).to(PrpLClaimVo.class);
		return claimVo;
	}

	private BigDecimal nullToZero(BigDecimal value) {
		if(value==null){
			return BigDecimal.ZERO;
		}
		return value;
	}

	public List<InvoiceInputInfo> pushReparation(List<InvoiceInputInfo> claimInvoiceInfoList,PrpLCompensateVo compeVo) {
		
		String isWriteOff = compeVo.getPrpLCompensateExt().getWriteOffFlag();
		List<PrpLPaymentVo> prpLPaymentVoList = compeVo.getPrpLPayments();
		boolean flag = true;

		if(prpLPaymentVoList != null && prpLPaymentVoList.size() > 0){
			for(PrpLPaymentVo paymentVo : prpLPaymentVoList){
				PrpLPayCustomVo customVo = managerService.findPayCustomVoById(paymentVo.getPayeeId());
				//收款人类型为修理厂时，将赔款数据推送给VAT进项税系统，用于发票抵扣。预付赔款也可以进行发票抵扣。
				if(CodeConstants.PayObjectKind.GARAGE.equals(customVo.getPayObjectKind())){
					InvoiceInputInfo claimInvoiceInfo = new InvoiceInputInfo();
					InvoicReceiptTaskVo invoicReceiptTaskVo = new InvoicReceiptTaskVo();
					if(flag){
						invoicReceiptTaskVo.setTaskId(compeVo.getCompensateNo());
						flag = false;
					}
					invoicReceiptTaskVo.setCertiNo(compeVo.getCompensateNo());
					invoicReceiptTaskVo.setSerialNo(new BigDecimal(paymentVo.getSerialNo()));

					invoicReceiptTaskVo.setCertiType(CodeConstants.CertiType.COMPENSATE);//理算传C
					invoicReceiptTaskVo.setUserCode(compeVo.getCreateUser());// 操作员
					invoicReceiptTaskVo.setComCode(compeVo.getComCode());// 归属机构

					// 重新组装费用类型 不匹配项 默认传 手续费
					Map<String, String> feetypeMap = codeTranService.findFeeTypes();
					if (CodeConstants.PayFlagType.INSTEAD_PAY.equals(paymentVo.getPayFlag())) {
						invoicReceiptTaskVo.setFeeType(feetypeMap.get(CodeConstants.PayReason.INSTEAD_PAY_Res));
					} else {
						invoicReceiptTaskVo.setFeeType(feetypeMap.get(CodeConstants.PayReason.COMPENSATE_PAY_Res));
					}

					invoicReceiptTaskVo.setFeeName(CodeConstants.TransVatFeeTypeMap.get(CodeConstants.VatFeeType.CPAY));

					invoicReceiptTaskVo.setRiskCode(compeVo.getRiskCode());// 险种代码
					SysCodeDictVo sysVo1 = codeTranService.findTransCodeDictVo("CarRiskCode",compeVo.getRiskCode());
					invoicReceiptTaskVo.setRiskName(sysVo1.getCodeName());// 险种名称

					//待定
					/*invoicReceiptTaskVo.setKindcode(prpLchargeDto.getKindCode());// 险别代码
					SysCodeDictVo sysVo2 = codeTranService.findTransCodeDictVo("KindCode",prpLchargeDto.getKindCode());
					invoicReceiptTaskVo.setKindName(sysVo2.getCodeName());// 险别名称
	*/
					invoicReceiptTaskVo.setCurrency(CodeConstants.Currency.CNY);// 币别

					invoicReceiptTaskVo.setInvoiceType(CodeConstants.InvoiceType.STAYTICKET);// 发票类型

					invoicReceiptTaskVo.setSumAmount(nullToZero(paymentVo.getSumRealPay()));
					invoicReceiptTaskVo.setSumRegAmount(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记含税金额
					invoicReceiptTaskVo.setSumRegAmountNT(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记不含税金额
					invoicReceiptTaskVo.setSumRegVATAmount(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记增值税额
					invoicReceiptTaskVo.setSumRegVATRate(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记增值税率
					invoicReceiptTaskVo.setSystemCode(CodeConstants.SystemCode.NEWCLAIM);
					if(CodeConstants.IsSingleAccident.NOT.equals(isWriteOff)){ // 案件冲销送红色 其他送蓝色
						invoicReceiptTaskVo.setInvoiceFlag(CodeConstants.DeflossType.CarLoss);
					}else{
						invoicReceiptTaskVo.setInvoiceFlag(CodeConstants.DeflossType.PropLoss);
						String invoiceType = invoicReceiptTaskVo.getInvoiceType();
						if(CodeConstants.InvoiceType.SPECIALTICKET.equals(invoiceType)){ // 冲销改变发票颜色
							invoicReceiptTaskVo.setInvoiceType(CodeConstants.InvoiceType.REDSPECIALTICKET);
						}
						if(CodeConstants.InvoiceType.COMMONTICKET.equals(invoiceType)){
							invoicReceiptTaskVo.setInvoiceType(CodeConstants.InvoiceType.REDCOMMONTICKET);
						}
					}
					claimInvoiceInfo.setInvoicReceiptTaskVo(invoicReceiptTaskVo);
					claimInvoiceInfoList.add(claimInvoiceInfo);
				}
			}
		}
		return claimInvoiceInfoList;
	}
	
	public InvoicReceiptTaskVo setInvoicReceiptTaskVo(InvoicReceiptTaskVo invoicReceiptTaskVo,PrpLPrePayVo prpLPrePayDto,PrpLCompensateVo compeVo,String isWriteOff) {
		invoicReceiptTaskVo.setSerialNo(new BigDecimal(prpLPrePayDto.getSerialNo()));// 序列号
		invoicReceiptTaskVo.setCertiType(CodeConstants.CertiType.PREPAY);// 业务类型--默认费用
		invoicReceiptTaskVo.setUserCode(compeVo.getCreateUser());// 操作员
		invoicReceiptTaskVo.setComCode(compeVo.getComCode());// 归属机构


		invoicReceiptTaskVo.setRiskCode(prpLPrePayDto.getRiskCode());// 险种代码
		SysCodeDictVo sysVo1 = codeTranService.findTransCodeDictVo("CarRiskCode",prpLPrePayDto.getRiskCode());
		invoicReceiptTaskVo.setRiskName(sysVo1.getCodeName());// 险种名称

		invoicReceiptTaskVo.setKindcode(prpLPrePayDto.getKindCode());// 险别代码
		SysCodeDictVo sysVo2 = codeTranService.findTransCodeDictVo("KindCode",prpLPrePayDto.getKindCode());
		invoicReceiptTaskVo.setKindName(sysVo2.getCodeName());// 险别名称

		invoicReceiptTaskVo.setCurrency(prpLPrePayDto.getCurrency());// 币别
		if(StringUtils.isNotBlank(prpLPrePayDto.getInvoiceType())){
			invoicReceiptTaskVo.setInvoiceType(prpLPrePayDto.getInvoiceType());// 发票类型
		}else{
			invoicReceiptTaskVo.setInvoiceType(CodeConstants.InvoiceType.STAYTICKET);
		}

		
		invoicReceiptTaskVo.setSumAmount(prpLPrePayDto.getPayAmt());
		invoicReceiptTaskVo.setSumRegAmount(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记含税金额
		// invoicReceiptTaskVo.setSumNotRegAmount(new BigDecimal(prpLchargeDto.getChargeAmount()).setScale(2,BigDecimal.ROUND_HALF_UP));//未登记含税金额默认等于含税金额
		invoicReceiptTaskVo.setSumRegAmountNT(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记不含税金额
		invoicReceiptTaskVo.setSumRegVATAmount(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记增值税额
		invoicReceiptTaskVo.setSumRegVATRate(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记增值税率
		invoicReceiptTaskVo.setSystemCode(CodeConstants.SystemCode.NEWCLAIM);
		if(CodeConstants.IsSingleAccident.NOT.equals(isWriteOff)){ // 案件冲销送红色 其他送蓝色
			invoicReceiptTaskVo.setInvoiceFlag(CodeConstants.DeflossType.CarLoss);
		}else{
			invoicReceiptTaskVo.setInvoiceFlag(CodeConstants.DeflossType.PropLoss);
			String invoiceType = invoicReceiptTaskVo.getInvoiceType();
			if(CodeConstants.InvoiceType.SPECIALTICKET.equals(invoiceType)){ // 冲销改变发票颜色
				invoicReceiptTaskVo.setInvoiceType(CodeConstants.InvoiceType.REDSPECIALTICKET);
			}
			if(CodeConstants.InvoiceType.COMMONTICKET.equals(invoiceType)){
				invoicReceiptTaskVo.setInvoiceType(CodeConstants.InvoiceType.REDCOMMONTICKET);
			}
		}
		return invoicReceiptTaskVo;
		
	}
	
	
	
	//垫付
	public Boolean pushPadPay(PrpLPadPayMainVo padPayMainVo,String serialNoSend) {
		ReturnInfo returnInfo = new ReturnInfo();
		Boolean flag = true;
		String compensateNo = padPayMainVo.getCompensateNo();
		List<InvoiceInputInfo> claimInvoiceInfoList = new ArrayList<InvoiceInputInfo>();
		try{
			List<PrpLPadPayPersonVo> prpLPadPayPersonVos = padPayMainVo.getPrpLPadPayPersons();
			BigDecimal costSum =new BigDecimal(0);
			if(prpLPadPayPersonVos != null && prpLPadPayPersonVos.size() > 0){
				for(PrpLPadPayPersonVo padPayPersonVo : prpLPadPayPersonVos){
					costSum=costSum.add(padPayPersonVo.getCostSum());
				}
			}
			if(BigDecimal.ZERO.compareTo(DataUtils.NullToZero(costSum))==0){// 垫付赔款 为0 不送收付
			}else{
				if(prpLPadPayPersonVos != null && prpLPadPayPersonVos.size() > 0){
					for(PrpLPadPayPersonVo vo : prpLPadPayPersonVos){
						PrpLPayCustomVo customVo = managerService.findPayCustomVoById(vo.getPayeeId());
						if(CodeConstants.PayObjectKind.GARAGE.equals(customVo.getPayObjectKind())){//收款人类型为修理厂时，将赔款数据推送给VAT进项税系统，用于发票抵扣。预付赔款也可以进行发票抵扣。
							InvoiceInputInfo claimInvoiceInfo = new InvoiceInputInfo();
							InvoicReceiptTaskVo invoicReceiptTaskVo = new InvoicReceiptTaskVo();
							if(flag){
								invoicReceiptTaskVo.setTaskId(compensateNo);
								flag = false;
							}
							invoicReceiptTaskVo.setCertiNo(compensateNo);// 业务号
							invoicReceiptTaskVo.setFeeType(CodeConstants.VatFeeType.DPAY);
							invoicReceiptTaskVo.setFeeName(CodeConstants.TransVatFeeTypeMap.get(CodeConstants.VatFeeType.DPAY));



							invoicReceiptTaskVo.setSerialNo(new BigDecimal(vo.getSerialNo()));// 序列号
							invoicReceiptTaskVo.setCertiType(CodeConstants.CertiType.COMPENSATE);// 业务类型--默认费用
							invoicReceiptTaskVo.setUserCode(padPayMainVo.getCreateUser());// 操作员
							invoicReceiptTaskVo.setComCode(padPayMainVo.getComCode());// 归属机构


							invoicReceiptTaskVo.setRiskCode(vo.getRiskCode());// 险种代码
							SysCodeDictVo sysVo1 = codeTranService.findTransCodeDictVo("CarRiskCode",vo.getRiskCode());
							invoicReceiptTaskVo.setRiskName(sysVo1.getCodeName());// 险种名称

							/*invoicReceiptTaskVo.setKindcode(vo.getKindCode());// 险别代码
							SysCodeDictVo sysVo2 = codeTranService.findTransCodeDictVo("KindCode",vo.getKindCode());
							invoicReceiptTaskVo.setKindName(sysVo2.getCodeName());// 险别名称
	*/
							invoicReceiptTaskVo.setCurrency(CodeConstants.Currency.CNY);// 币别
							invoicReceiptTaskVo.setInvoiceType(CodeConstants.InvoiceType.STAYTICKET);

							
							invoicReceiptTaskVo.setSumAmount(vo.getCostSum());
							invoicReceiptTaskVo.setSumRegAmount(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记含税金额
							// invoicReceiptTaskVo.setSumNotRegAmount(new BigDecimal(prpLchargeDto.getChargeAmount()).setScale(2,BigDecimal.ROUND_HALF_UP));//未登记含税金额默认等于含税金额
							invoicReceiptTaskVo.setSumRegAmountNT(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记不含税金额
							invoicReceiptTaskVo.setSumRegVATAmount(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记增值税额
							invoicReceiptTaskVo.setSumRegVATRate(new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_HALF_UP));// 已登记增值税率
							invoicReceiptTaskVo.setSystemCode(CodeConstants.SystemCode.NEWCLAIM);
							invoicReceiptTaskVo.setInvoiceFlag(CodeConstants.DeflossType.CarLoss);
							
							claimInvoiceInfo.setInvoicReceiptTaskVo(invoicReceiptTaskVo);
							claimInvoiceInfoList.add(claimInvoiceInfo);
						}
					}
				}
			}
			ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
			logVo.setRegistNo(padPayMainVo.getRegistNo());
			logVo.setBusinessType(BusinessType.INVOICE_VAT.getCode());
			logVo.setBusinessName(BusinessType.INVOICE_VAT.getName());
			logVo.setServiceType(INVOICEINPUTSERVICE);
			logVo.setComCode(padPayMainVo.getComCode());
			logVo.setClaimNo(padPayMainVo.getClaimNo());
			logVo.setCompensateNo(compensateNo);
			logVo.setCreateUser(padPayMainVo.getCreateUser());
			logVo.setRequestTime(new Date());
			logVo.setCreateTime(new Date());
			logVo.setOperateNode(CodeConstants.VClaimName.VC_ADOPT);
			String invoiceInput_url = SpringProperties.getProperty("invoiceInput_url");// invoiceInput_url

			logVo.setRequestUrl(invoiceInput_url);

			for(InvoiceInputInfo claimInvoiceInfo:claimInvoiceInfoList){
				if(( !StringUtils.isBlank(serialNoSend)&&serialNoSend.equals(claimInvoiceInfo.getInvoicReceiptTaskVo().getSerialNo().toString()) )||StringUtils
						.isBlank(serialNoSend)){
					// serialNoSend 需要送发票的费用的序列号，可为空，为空时送该计算书所有费用，不为空只送某一条费用
					try{
						if(claimInvoiceInfo.getInvoicReceiptTaskVo()!=null&& !BigDecimal.ZERO.equals(claimInvoiceInfo.getInvoicReceiptTaskVo()
								.getSumAmount())){
							returnInfo = this.inputInvoiceInfo(claimInvoiceInfo,logVo);
						}else{
							logger.info("进项税金额为0！");
							returnInfo.setResponseCode(false);
						}
					}catch(Exception e){
						logger.error("营改增发送日志传送失败",e);
						logVo.setErrorMessage(e.getMessage());
						logService.save(logVo);
					}
				}
			}
		}catch(Exception e){
			logger.error("营改增发送必传数据不能为空：",e);
		}
			return returnInfo.isResponseCode();
		}
}
