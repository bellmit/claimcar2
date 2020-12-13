package ins.sino.claimcar.account.service.spring;

import com.google.gson.Gson;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.account.po.AccRollBackAccount;
import ins.sino.claimcar.account.po.PrpDAccRollBackAccount;
import ins.sino.claimcar.account.po.PrpLPayBank;
import ins.sino.claimcar.account.po.PrpLPayBankHis;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.po.PrpLCharge;
import ins.sino.claimcar.claim.po.PrpLPadPayMain;
import ins.sino.claimcar.claim.po.PrpLPadPayPerson;
import ins.sino.claimcar.claim.po.PrpLPayment;
import ins.sino.claimcar.claim.po.PrpLPrePay;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.flow.po.PrpLCheckFee;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.manager.po.PrpLPayCustom;
import ins.sino.claimcar.manager.po.PrpdCheckBankMain;
import ins.sino.claimcar.manager.po.PrpdIntermBank;
import ins.sino.claimcar.manager.po.PrpdIntermMain;
import ins.sino.claimcar.manager.po.PrpdcheckBank;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdIntermBankVo;
import ins.sino.claimcar.manager.vo.PrpdcheckBankVo;
import ins.sino.claimcar.newpayment.vo.*;
import ins.sino.claimcar.other.po.PrpLAssessorFee;
import ins.sino.claimcar.other.service.AccountInfoService;
import ins.sino.claimcar.other.service.AccountQueryService;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpDAccRollBackAccountVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckmAuditVo;
import ins.sino.claimcar.other.vo.PrpLPayBankHisVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;
import ins.sino.claimcar.other.vo.PrplInterrmAuditVo;
import ins.sino.claimcar.other.vo.SendMsgParamVo;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
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
import com.sinosoft.sff.interf.AccMainVo;
import com.sinosoft.sff.interf.AccRecAccountVo;
import com.sinosoft.sff.interf.ClaimReturnTicketPortBindingStub;
import com.sinosoft.sff.interf.ClaimReturnTicketServiceLocator;
import com.sinosoft.sff.interf.JPlanReturnVo;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;


/**
 * 账号信息修改服务实现类
 * @author ★wu
 */
@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path(value = "accountInfoService")
public class AccountInfoServiceImpl implements AccountInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class); 
	
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	ManagerService managerService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	BaseDaoService baseDaoService;
	@Autowired
	CompensateTaskService  compensateTaskService;
	@Autowired
	PadPayPubService padPayPubService;
	@Autowired
	PayCustomService payCustomService;
	@Autowired
	ClaimInterfaceLogService logService;
	@Autowired
	RegistQueryService registQueryService;
	
	@Autowired
	AssessorService assessorService;
	
	@Autowired
	private AcheckService acheckService;
	
	
	@Autowired
	private AccountQueryService accountQueryService;
	
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.base.service.AccountInfoService#getPayBankVo(ins.sino.claimcar.claim.vo.PrpLCompensateVo)
	 */
	@Override
	public PrpLPayBankVo getPayBankVo(String compensateNo,PrpLPayCustomVo paycustomVo,String registNo,String payType){
		PrpLPayBankVo payBankVo = new PrpLPayBankVo();
		PrpLCMainVo cMainVo = new PrpLCMainVo();
		if(compensateNo.startsWith("D")){// 垫付计算书号
			PrpLPadPayMainVo padPayVo = padPayPubService.queryPadPay(registNo, compensateNo);
			cMainVo = policyViewService.getPolicyInfo(padPayVo.getRegistNo(), padPayVo.getPolicyNo());
			PrpLPadPayPersonVo padPayPersonVo = this.findPadPayPersonVo(padPayVo.getId(), paycustomVo.getId());
			if(padPayPersonVo != null){
				payBankVo.setSummary(padPayPersonVo.getSummary());
			}
			payBankVo.setCompensateNo(padPayVo.getCompensateNo());
			payBankVo.setPolicyNo(padPayVo.getPolicyNo());
			payBankVo.setClaimNo(padPayVo.getClaimNo());
			payBankVo.setRegistNo(padPayVo.getRegistNo());
			payBankVo.setLossType(paycustomVo.getFeeType());
			payBankVo.setPayeeId(paycustomVo.getId());
		}else{
			PrpLCompensateVo compensateVo = compensateTaskService.findPrpLCompensateVoByPK(compensateNo);
			cMainVo = policyViewService.getPolicyInfo(compensateVo.getRegistNo(), compensateVo.getPolicyNo());
			payBankVo.setCompensateNo(compensateVo.getCompensateNo());
			payBankVo.setPolicyNo(compensateVo.getPolicyNo());
			payBankVo.setClaimNo(compensateVo.getClaimNo());
			payBankVo.setRegistNo(compensateVo.getRegistNo());
			payBankVo.setLossType(paycustomVo.getFeeType());
			payBankVo.setPayeeId(paycustomVo.getId());
			
			if(compensateNo.startsWith("Y")){// 预付
				PrpLPrePayVo prePayVo = this.findPrePayByPayType(compensateNo, payType);
				if(prePayVo!=null && prePayVo.getSummary()!=null && !prePayVo.getSummary().isEmpty()){
					payBankVo.setSummary(prePayVo.getSummary());
				}
			}else if( !"P60".equals(payType)){// 理算费用
				PrpLChargeVo chargeVo = this.findChargeByPayType(compensateNo, payType);
				if(chargeVo!=null && chargeVo.getSummary()!=null && !chargeVo.getSummary().isEmpty()){
					payBankVo.setSummary(chargeVo.getSummary());
				}
			}else{// 理算赔款
				PrpLPaymentVo paymentVo = this.findPaymentVo(compensateNo, paycustomVo.getId());
				if(paymentVo != null){
					payBankVo.setSummary(paymentVo.getSummary());
				}
			}
		}
		
		payBankVo.setInsuredName(cMainVo.getInsuredName());
		payBankVo.setRemark(cMainVo.getInsuredCode());// 暂时存放被保险人代码
		return payBankVo;
	}
	
	/**
	 * 如果退票只修改账号，银行支行，手机号则自动审核通过；, 如果退票信息修改修改了收款人类型、收款人账户名，则必须通过审核。
	 * @param compensateNo
	 * @param payCustomVo
	 * @param payBankVo
	 * @param originalAccountNo
	 * @throws Exception
	 * @modified: ☆Luwei(2016年12月27日 下午5:19:55): <br>
	 */
	@Override
	public String saveAccountInfo(String compensateNo,PrpLPayCustomVo payCustomVo,
	            PrpLPayBankVo payBankVo,String originalAccountNo,SysUserVo userVo) throws Exception{

		String payObjectKind = payCustomVo.getPayObjectKind();// 收款人类型
		String payeeName = payCustomVo.getPayeeName();// 收款人户名
		PrpLPayCustomVo oldPaycustomVo = managerService.findPayCustomVoById(payCustomVo.getId());
		payCustomVo.setAccountId(oldPaycustomVo.getAccountId());
		this.setPayBank(payCustomVo, payBankVo, userVo,compensateNo);
		
		// 修改了收款人类型、收款人账户名
		boolean isUpdate = (StringUtils.isNoneBlank(payObjectKind) && !payObjectKind.equals(oldPaycustomVo.getPayObjectKind()))
		|| (StringUtils.isNoneBlank(payeeName) && !payeeName.equals(oldPaycustomVo.getPayeeName()));
		String handle = payBankVo.getVerifyHandle();// 审核退回的也是需要再次提交至审核(0-暂存，1-审核通过，2-退回，)
		if("2".equals(handle)||isUpdate){// 审核状态(0-未审核，1-正在审核，2-完成审核,3-自动审核)';
			// 需要审核
			payBankVo.setVerifyStatus("0");// 未审核
			this.saveAcc(payBankVo, userVo, originalAccountNo, oldPaycustomVo.getId());
		}else{
			// 自动审核通过
			payBankVo.setVerifyStatus("3");// 自动审核通过
			Beans.copy().from(payCustomVo).excludeNull().to(oldPaycustomVo);
			sendAccountToPayment(oldPaycustomVo,userVo,compensateNo,payBankVo);
		}
		
		return payBankVo.getVerifyStatus();
	}

	/**
	 * 保存退票信息并送收付
	 * @param compensateNo 计算书
	 * @param payCustomVo 修改后的收款人信息
	 * @param payBankVo 退票页面的退票银行信息
	 * @param userVo 处理人
	 * @return 审核状态
	 * @throws Exception 异常信息
	 */
	@Override
	public String saveRollbackAccountInfo(String compensateNo, PrpLPayCustomVo payCustomVo, PrpLPayBankVo payBankVo, SysUserVo userVo) throws Exception {
		// 收款人类型
		String payObjectKind = payCustomVo.getPayObjectKind();
		// 收款人户名
		String payeeName = payCustomVo.getPayeeName();
		PrpLPayCustomVo oldPaycustomVo = managerService.findPayCustomVoById(payCustomVo.getId());
		this.setPayBank(payCustomVo, payBankVo, userVo, compensateNo);

		// 修改了收款人类型、收款人账户名
		boolean isUpdate = (StringUtils.isNoneBlank(payObjectKind) && !payObjectKind.equals(oldPaycustomVo.getPayObjectKind()))
				|| (StringUtils.isNoneBlank(payeeName) && !payeeName.equals(oldPaycustomVo.getPayeeName()));
		// 审核退回的也是需要再次提交至审核(0-暂存，1-审核通过，2-退回，)
		String handle = payBankVo.getVerifyHandle();
		// 审核状态(0-未审核，1-正在审核，2-完成审核,3-自动审核);
		if ("2".equals(handle) || isUpdate) {
			// 需要审核
			// 未审核
			payBankVo.setVerifyStatus("0");
			saveAccRbackAccount(payBankVo, userVo, oldPaycustomVo.getId());
		} else {
			// 自动审核通过
			payBankVo.setVerifyStatus("3");
			Beans.copy().from(payCustomVo).excludeNull().to(oldPaycustomVo);
			refundToPayment(oldPaycustomVo, compensateNo, payBankVo, userVo);
		}

		return payBankVo.getVerifyStatus();
	}

	/**
	 * 保存或更新退票银行信息，更新退票信息
	 * @param payBankVo 退票银行信息
	 * @param userVo 处理人
	 * @param oldPayeeId 修改前的收款人信息id
	 * @throws Exception 异常信息
	 */
	private void saveAccRbackAccount(PrpLPayBankVo payBankVo, SysUserVo userVo, Long oldPayeeId) throws Exception {
		PrpLPayBank payBank = null;
		PrpLPayBankVo oldPayBank = findOldPayBank(payBankVo.getRegistNo(), payBankVo.getCompensateNo(), String.valueOf(oldPayeeId), payBankVo.getPayType());
		if (oldPayBank != null) {
			payBank = databaseDao.findByPK(PrpLPayBank.class, oldPayBank.getId());
			Beans.copy().from(payBankVo).excludeNull().to(payBank);
			setPayBankDateAndUser(payBankVo, payBank, userVo.getUserCode());
			databaseDao.update(PrpLPayBank.class, payBank);
		} else {
			payBank = new PrpLPayBank();
			Beans.copy().from(payBankVo).to(payBank);
			setPayBankDateAndUser(payBankVo, payBank, userVo.getUserCode());
			databaseDao.save(PrpLPayBank.class, payBank);
		}

		updateAccRollBackAccountStatus(payBankVo);
	}

	/**
	 * 将页面退票银行信息保存到数据库
	 * @param payBankVo 页面退票银行信息
	 * @param payBank 数据库退票银行信息对象
	 * @param userCode 处理人
	 */
	private void setPayBankDateAndUser(PrpLPayBankVo payBankVo, PrpLPayBank payBank, String userCode) {
		Date date = new Date();
		PrpDAccRollBackAccountVo vo = findRollBackBySerialNo(payBankVo.getCompensateNo(), payBankVo.getSerialNo(), payBankVo.getPayType());
		if (vo != null && vo.getRollBackTime() != null) {
			payBank.setAppTime(vo.getRollBackTime());
		}

		payBank.setCreateUser(userCode);
		payBank.setCreateTime(date);
		payBank.setUpdateUser(userCode);
		payBank.setUpdateTime(date);
	}

	/**
	 * 封装退票银行信息
	 * @param payCustomVo 收款人信息
	 * @param payBankVo 退票银行信息
	 * @param userVo 操作人
	 * @param compensateNo  计算书号
	 */
	public void setPayBank(PrpLPayCustomVo payCustomVo,PrpLPayBankVo payBankVo,SysUserVo userVo,String compensateNo){
		payBankVo.setIsVerify("1");
		payBankVo.setVerifyUser(userVo.getUserCode());
		payBankVo.setVerifyDate(new Date());
		payBankVo.setFlag("0");// 回写PrpDAccRollBackAccount的status字段
		payBankVo.setPayObjectKind(payCustomVo.getPayObjectKind());
		payBankVo.setPayObjectType(payCustomVo.getPayObjectType());
		// 身份证号/机构代码
		payBankVo.setPayeeIDNumber(payCustomVo.getCertifyNo());
		payBankVo.setPayee(payCustomVo.getPayeeName());
		payBankVo.setAccountNo(payCustomVo.getAccountNo());
		payBankVo.setBankType(payCustomVo.getBankType());
		payBankVo.setBankName(payCustomVo.getBankName());
		payBankVo.setAccountName(payCustomVo.getPayeeName());
		payBankVo.setBankNo(payCustomVo.getBankNo());
		payBankVo.setBankOutlets(payCustomVo.getBankOutlets());
		payBankVo.setProvinceCode(payCustomVo.getProvinceCode());
		payBankVo.setProvince(payCustomVo.getProvince());
		payBankVo.setCity(payCustomVo.getCity());
		payBankVo.setCityCode(payCustomVo.getCityCode());
		payBankVo.setPriorityFlag(payCustomVo.getPriorityFlag());
		payBankVo.setPayeeMobile(payCustomVo.getPayeeMobile());
		payBankVo.setPurpose(payCustomVo.getPurpose());
		payBankVo.setPublicAndPrivate(payCustomVo.getPublicAndPrivate());
		payBankVo.setSummary(payCustomVo.getSummary());
		payBankVo.setCertifyType(payCustomVo.getCertifyType());
		payBankVo.setRepairFactoryId(payCustomVo.getRepairFactoryCode());// 修理厂ID
		payBankVo.setCompensateNo(compensateNo);
		payBankVo.setAccountId(payCustomVo.getAccountId());
	}
	
	public void setPayCustom(PrpLPayCustomVo payCustomVo,PrpLPayBankVo payBankVo){
		payCustomVo.setPayObjectKind(payBankVo.getPayObjectKind());
		payCustomVo.setPayObjectType(payBankVo.getPayObjectType());
		payCustomVo.setCertifyNo(payBankVo.getPayeeIDNumber());
		payCustomVo.setPayeeName(payBankVo.getPayee());
		payCustomVo.setAccountNo(payBankVo.getAccountNo());
		payCustomVo.setBankType(payBankVo.getBankType());
		payCustomVo.setBankName(payBankVo.getBankName());
		payCustomVo.setBankNo(payBankVo.getBankNo());
		payCustomVo.setBankOutlets(payBankVo.getBankOutlets());
		payCustomVo.setProvinceCode(payBankVo.getProvinceCode());
		payCustomVo.setProvince(payBankVo.getProvince());
		payCustomVo.setCity(payBankVo.getCity());
		payCustomVo.setCityCode(payBankVo.getCityCode());
		payCustomVo.setPriorityFlag(payBankVo.getPriorityFlag());
		payCustomVo.setPayeeMobile(payBankVo.getPayeeMobile());
		payCustomVo.setPurpose(payBankVo.getPurpose());
		payCustomVo.setPublicAndPrivate(payBankVo.getPublicAndPrivate());
		payCustomVo.setSummary(payBankVo.getSummary());
		payCustomVo.setCertifyType(payBankVo.getCertifyType());
		payCustomVo.setRepairFactoryCode(payBankVo.getRepairFactoryId());
	}
	
	/**
	 * 账户信息修改审核提交
	 * @param payBankVo
	 * @param userVo
	 * @throws Exception
	 * @modified: ☆Luwei(2016年12月28日 上午9:49:01): <br>
	 */
	@Override
	public String accountSubmit(PrpLPayBankVo payBankVo,SysUserVo userVo,String originalAccountNo) throws Exception{
		String handle = payBankVo.getVerifyHandle();// (0-暂存，1-审核通过，2-退回)
		Long oldPayeeId = payBankVo.getPayeeId();
		PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(oldPayeeId);
		PrpLPayBankVo oldPayBank = this.findPayBankVoById(payBankVo.getId());
		this.setPayCustom(payCustomVo, oldPayBank);
		payBankVo.setCompensateNo(oldPayBank.getCompensateNo());
		Beans.copy().from(payBankVo).excludeNull().to(oldPayBank);
		oldPayBank.setVerifyDate(new Date());
		oldPayBank.setVerifyUser(userVo.getUserCode());
		if("1".equals(handle)){// 审核通过发送送收付报文
			oldPayBank.setVerifyStatus("2");// 审核状态(0-未审核，1-正在审核，2-完成审核,3-自动审核)
			oldPayBank.setFlag("0");
			// String returnPayeeId=sendAccountToPayment(payCustomVo,userVo,oldPayBank.getCompensateNo(),oldPayBank);
			String returnPayeeId = refundToPayment(payCustomVo,oldPayBank.getCompensateNo(),oldPayBank,userVo);
			return returnPayeeId;
		}else if("0".equals(handle)){// 暂存
			oldPayBank.setVerifyStatus("1");
			oldPayBank.setFlag("0");
			// 更新PrpLPayBankVo
			saveAccRbackAccount(oldPayBank, userVo, oldPayeeId);
			return String.valueOf(oldPayeeId);
		}else{
			oldPayBank.setVerifyStatus("2");
			oldPayBank.setFlag("2");
			// 更新PrpLPayBankVo
			saveAccRbackAccount(oldPayBank, userVo, oldPayeeId);
			return String.valueOf(oldPayeeId);
		}
		
	}
	
	@Override
	public void savePayBankHis(PrpLPayCustomVo paycustomVo){
		Date date = new Date();
		PrpLPayBankHis payBankHis = new PrpLPayBankHis();
		Beans.copy().from(paycustomVo).to(payBankHis);
		payBankHis.setPayeeId(paycustomVo.getId());
		payBankHis.setRemark(paycustomVo.getPayeeMobile());
		payBankHis.setCreateTime(date);
		payBankHis.setUpdateTime(date);
		databaseDao.save(PrpLPayBankHis.class, payBankHis);
	}
	
	/**
	 * 暂存或更新PrpLPayBankVo信息
	 */
	@Override
	public void saveAcc(PrpLPayBankVo payBankVo,SysUserVo userVo,String originalAccountNo,Long oldPayeeId) throws Exception{
		PrpLPayBank payBank = null;
		PrpLPayBankVo oldPayBank = findOldPayBank(payBankVo.getRegistNo(),payBankVo.getCompensateNo(),String.valueOf(oldPayeeId),payBankVo.getPayType());
		if(oldPayBank != null){
			payBank = databaseDao.findByPK(PrpLPayBank.class,oldPayBank.getId());
			Beans.copy().from(payBankVo).excludeNull().to(payBank);
			setPayBank(payBankVo,payBank,userVo.getUserCode(),originalAccountNo,"update");
			databaseDao.update(PrpLPayBank.class, payBank);
		}else{
			payBank = new PrpLPayBank();
			Beans.copy().from(payBankVo).to(payBank);
			setPayBank(payBankVo,payBank,userVo.getUserCode(),originalAccountNo,"save");
			databaseDao.save(PrpLPayBank.class, payBank);
		}

		this.writeAccRollBackAccount(payBankVo,originalAccountNo);
	}
	
	private void setPayBank(PrpLPayBankVo payBankVo,PrpLPayBank payBank,String userCode,String originalAccountNo,String flag){
		Date date = new Date();
		PrpDAccRollBackAccountVo vo = this.findAccRollBackAccount(payBankVo.getCompensateNo(), originalAccountNo,payBankVo.getPayType());
		if(vo!=null && vo.getRollBackTime()!=null){
			payBank.setAppTime(vo.getRollBackTime());
		}

		payBank.setCreateUser(userCode);
		payBank.setCreateTime(date);
		payBank.setUpdateUser(userCode);
		payBank.setUpdateTime(date);
	}
	
	@Override
	public void writeAccRollBackAccount(PrpLPayBankVo payBankVo,String originalAccountNo) throws Exception{
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("certiNo", payBankVo.getCompensateNo());
		qr.addEqual("accountId", originalAccountNo);
		qr.addEqual("payType", payBankVo.getPayType());
		List<PrpDAccRollBackAccount> rollBackAccount = databaseDao.findAll(PrpDAccRollBackAccount.class, qr);
		String verifyStatus = payBankVo.getVerifyStatus();
		if(rollBackAccount != null && !rollBackAccount.isEmpty()){
			for(PrpDAccRollBackAccount account : rollBackAccount){
				// 审核状态(0-未审核，1-正在审核，2-完成审核,3-自动审核)
				if(("2".equals(verifyStatus)||"3".equals(verifyStatus))&&
						payBankVo.getAccountId()!=null&&!"".equals(payBankVo.getAccountId())){
					account.setAccountId(payBankVo.getAccountId());
				}
				account.setStatus(payBankVo.getFlag());
				databaseDao.update(PrpDAccRollBackAccount.class,account);
			}
		}
	}

	@Override
	public void updateAccRollBackAccountStatus(PrpLPayBankVo payBankVo) throws Exception{
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("certiNo", payBankVo.getCompensateNo());
		qr.addEqual("serialNo", payBankVo.getSerialNo());
		qr.addEqual("payType", payBankVo.getPayType());
		List<PrpDAccRollBackAccount> rollBackAccount = databaseDao.findAll(PrpDAccRollBackAccount.class, qr);
		String verifyStatus = payBankVo.getVerifyStatus();
		if(rollBackAccount != null && !rollBackAccount.isEmpty()){
			for(PrpDAccRollBackAccount account : rollBackAccount){
				// 审核状态(0-未审核，1-正在审核，2-完成审核,3-自动审核)
				if(("2".equals(verifyStatus)||"3".equals(verifyStatus))&&
						payBankVo.getAccountId()!=null&&!"".equals(payBankVo.getAccountId())){
					account.setAccountId(payBankVo.getAccountId());
				}
				account.setStatus(payBankVo.getFlag());
				databaseDao.update(PrpDAccRollBackAccount.class,account);
			}
		}
	}
	
	public PrpDAccRollBackAccountVo findAccRollBackAccount(String certiNo,String accountId,String payType){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("certiNo", certiNo);
		qr.addEqual("accountId", accountId);
		qr.addEqual("payType", payType);
		qr.addDescOrder("rollBackTime");
		List<PrpDAccRollBackAccount> rollBackAccount = databaseDao.findAll(PrpDAccRollBackAccount.class, qr);
		PrpDAccRollBackAccountVo vo = new PrpDAccRollBackAccountVo();
		if(rollBackAccount != null && rollBackAccount.size()>0){
			Beans.copy().from(rollBackAccount.get(0)).to(vo);
		}
		return vo;
	}
	
	// 查找旧的PrpLPayBank数据
	@Override
	public PrpLPayBankVo findOldPayBank(String registNo,String compensateNo,String payeeId,String payType){
		PrpLPayBankVo payBank = null;
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		qr.addEqual("compensateNo", compensateNo);
		qr.addEqual("payType", payType);
		if(payeeId!=null && !"".equals(payeeId) && !"null".equals(payeeId)){
			qr.addEqual("payeeId", Long.valueOf(payeeId));
		}
		qr.addDescOrder("createTime");
		List<PrpLPayBank> oldPayBankList = databaseDao.findAll(PrpLPayBank.class,qr);
		if(oldPayBankList != null && !oldPayBankList.isEmpty()){
			payBank = Beans.copyDepth().from(oldPayBankList.get(0)).to(PrpLPayBankVo.class);
		}
		return payBank;
	}
	
	
	public AccRollBackAccount findAcc(String compensateNo){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("certiNo", compensateNo);
		qr.addEqual("status", "2");
		qr.addDescOrder("rollBackTime");
		List<AccRollBackAccount> accRollBackAccounts = databaseDao.findAll(AccRollBackAccount.class, qr);
		return accRollBackAccounts.get(0);
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.base.service.AccountInfoService#findPayBankHisVo(java.lang.Long)
	 */
	@Override
	public PrpLPayBankHisVo findPayBankHisVo(Long payeeId){
		PrpLPayBankHisVo prpLPayBankHisVo = new PrpLPayBankHisVo();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("payeeId", payeeId);
		qr.addDescOrder("createTime");
		List<PrpLPayBankHis> PayBankHisList=databaseDao.findAll(PrpLPayBankHis.class, qr);
		if(PayBankHisList != null && PayBankHisList.size() > 0){
			Beans.copy().from(PayBankHisList.get(0)).to(prpLPayBankHisVo);
		}
		return prpLPayBankHisVo;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.base.service.AccountInfoService#findRollBackAccount(java.lang.String)
	 */
	@Override
	public PrpDAccRollBackAccountVo findRollBackAccount(String certiNo,String accountId,String payType){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("certiNo", certiNo);
		qr.addEqual("accountId", accountId);
		qr.addEqual("payType", payType);
		qr.addDescOrder("rollBackTime");
		List<PrpDAccRollBackAccount> rollBackAccount = databaseDao.findAll(PrpDAccRollBackAccount.class, qr);
//		if(rollBackAccount==null||rollBackAccount.isEmpty()){
//			QueryRule query = QueryRule.getInstance();
//			query.addEqual("certiNo", certiNo);
//			query.addDescOrder("rollBackTime");
//			rollBackAccount = databaseDao.findAll(PrpDAccRollBackAccount.class, query);
//		}
		PrpDAccRollBackAccountVo rollBackAccountVo = new PrpDAccRollBackAccountVo();
		if(rollBackAccount != null && rollBackAccount.size() > 0){
			Beans.copy().from(rollBackAccount.get(0)).to(rollBackAccountVo);
		}
		return rollBackAccountVo;
	}
	
	@Override
	public PrpDAccRollBackAccountVo findRollBackByOldAccountId(String certiNo,String oldAccountId,String payType){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("certiNo", certiNo);
		qr.addEqual("serialNo", oldAccountId);
		qr.addEqual("payType", payType);
		qr.addDescOrder("rollBackTime");
		List<PrpDAccRollBackAccount> rollBackAccount = databaseDao.findAll(PrpDAccRollBackAccount.class, qr);
		PrpDAccRollBackAccountVo rollBackAccountVo = new PrpDAccRollBackAccountVo();
		if(rollBackAccount != null && rollBackAccount.size() > 0){
			Beans.copy().from(rollBackAccount.get(0)).to(rollBackAccountVo);
		}
		return rollBackAccountVo;
	}

	/**
	 * 查询退票信息
	 * @param certiNo 计算书号
	 * @param serialNo 支付对象序号
	 * @param payType 收款原因
	 * @return 退票账号信息
	 */
	@Override
	public PrpDAccRollBackAccountVo findRollBackBySerialNo(String certiNo, String serialNo, String payType) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("certiNo", certiNo);
		qr.addEqual("serialNo", serialNo);
		qr.addEqual("payType", payType);
		qr.addDescOrder("rollBackTime");
		List<PrpDAccRollBackAccount> rollBackAccount = databaseDao.findAll(PrpDAccRollBackAccount.class, qr);
		PrpDAccRollBackAccountVo rollBackAccountVo = new PrpDAccRollBackAccountVo();
		if (rollBackAccount != null && rollBackAccount.size() > 0) {
			Beans.copy().from(rollBackAccount.get(0)).to(rollBackAccountVo);
		}
		return rollBackAccountVo;
	}
	@Override
	public void saveAccRollBackAccount(PrpDAccRollBackAccountVo accRollBaccVo){
		PrpDAccRollBackAccount accRollBack = new PrpDAccRollBackAccount();
		Beans.copy().from(accRollBaccVo).to(accRollBack);
		String chargeCode = CodeConstants.TransPayTypeMap.get(accRollBack.getPayType());
		if(chargeCode!=null){
			accRollBack.setChargeCode(chargeCode);
		}
		accRollBack.setOldAccountId(accRollBack.getAccountId());
		databaseDao.save(PrpDAccRollBackAccount.class, accRollBack);
	}
	
	/**
	 * 账户信息修改成功后发送报文至收付
	 */
	@Override
	public String sendAccountToPayment(PrpLPayCustomVo payCustomVo,SysUserVo userVo,String compensateNo,PrpLPayBankVo payBankVo) throws Exception {
		String returnPayeeId = String.valueOf(payCustomVo.getId());
		if(payCustomVo.getId() == null){
			logger.info("====payCustomVo.getId()为空，操作失败:===="+payCustomVo.getId());
			return returnPayeeId;
		}
		
		// 调收付的接口
		String SVR_URL = SpringProperties.getProperty("PAYMENT_SVR_URL");
		if(SVR_URL==null || payCustomVo == null){
			logger.warn("未配置收付地址，不调用收付接口。");
			throw new Exception("未配置收付服务地址。或未查找到赔款人账号信息！");
		}
		SVR_URL = SVR_URL+"/service/ClaimReturnTicket";// 收付地址用同一个，调用不同的服务再拼接 改sys_config表的配置数据
		
		// 组织accMainVo
		AccMainVo accMainVo = setAccMainVo(payCustomVo,userVo,compensateNo,payBankVo);
		// 发送报文
		JPlanReturnVo jPlanReturnVo = sendXmlToPayment(payCustomVo,userVo,compensateNo,SVR_URL,accMainVo);
		if(jPlanReturnVo != null){
			// 更新accountID
			String accountId = jPlanReturnVo.getAccountNo();
			logger.info("====accountId:===="+accountId);
			//退票修改收款人账户信息保存
			logger.info("计算书号： " + compensateNo + " 收款人账户名： " + (payCustomVo.getPayeeName()) + " 收款人账号： " + (payCustomVo.getAccountNo() != null ? payCustomVo.getAccountNo() : null));

			returnPayeeId=this.saveOrUpdatePayCustom(payCustomVo, accountId, compensateNo, userVo,payBankVo);
		}
		return returnPayeeId;
	}
	
	/**
	 * <pre>
	 * 组装调用收付vo
	 * </pre>
	 * @param payCustomVo
	 * @param userVo
	 * @param compensateNo
	 * @return AccMainVo
	 * @modified: ☆Luwei(2016年12月27日 下午3:16:01): <br>
	 */
	private AccMainVo setAccMainVo(PrpLPayCustomVo payCustomVo,SysUserVo userVo,String compensateNo,PrpLPayBankVo payBankVo){
		AccMainVo accMainVo = new AccMainVo();
		Date date = new Date();
		List<AccRecAccountVo> accountVosList = new ArrayList<AccRecAccountVo>();
		AccRecAccountVo accountVo = new AccRecAccountVo();
		accMainVo.setCertiNo(compensateNo);
		accMainVo.setOperateComCode(userVo.getComCode());
		accMainVo.setModifyCode(userVo.getUserCode());
		accMainVo.setModifyTime(DateUtils.dateToStr(date, DateUtils.YToDay));
		accMainVo.setStatus("0");
		accountVo.setAccountCode(payCustomVo.getAccountNo());
		
		accountVo.setAccountNo(payCustomVo.getAccountId());// 原accountId
		
		accountVo.setBankCode(payCustomVo.getBankNo());
		accountVo.setCurrency("01");// 账户币种
		if(payCustomVo.getProvinceCode()!=null){
			accountVo.setProvincial(String.valueOf(payCustomVo.getProvinceCode()));
		}
		if(payCustomVo.getCityCode()!=null){
			accountVo.setCity(String.valueOf(payCustomVo.getCityCode()));
		}
		accountVo.setNameOfBank(payCustomVo.getBankOutlets());
		accountVo.setAccountName(payCustomVo.getPayeeName());
		accountVo.setTelephone(payCustomVo.getPayeeMobile());
		accountVo.setClientType(payCustomVo.getPayObjectKind());
		accountVo.setClientNo(payCustomVo.getId().toString());
		accountVo.setClientName(payCustomVo.getPayeeName());
		accountVo.setAccountType("1");
		accountVo.setDefaultFlag("0");
		accountVo.setCreateCode(payCustomVo.getCreateUser());
		accountVo.setCreateBranch(userVo.getComCode());
		accountVo.setValidStatus("1");
		accountVo.setFlag(payCustomVo.getBankNo());// 联行号
		accountVo.setIdentifyType(payCustomVo.getCertifyType());
		accountVo.setIdentifyNumber(payCustomVo.getCertifyNo());
		accountVo.setAccType(payCustomVo.getPayObjectType());
		accountVo.setActType("101");
		accountVo.setBankCode(payCustomVo.getBankName());
		accountVo.setCreateDate(date);
		accountVo.setAbstractcontent(payCustomVo.getSummary());// 摘要
		accountVo.setIsAutoPay(payBankVo.getIsAutoPay());
		accountVo.setPublicPrivateFlag(payCustomVo.getPublicAndPrivate());
		
		if(compensateNo.startsWith("P") || compensateNo.startsWith("G") || compensateNo.startsWith("J")){
			accMainVo.setCertiType("P");
			accountVo.setIsFastReparation("0");
		}else if(compensateNo.startsWith("Y")){
			accMainVo.setCertiType("Y");
		}else{
			accMainVo.setCertiType("C");
		}
		if(!compensateNo.startsWith("P") && !compensateNo.startsWith("G") && !compensateNo.startsWith("J")){
			// 是否快赔（0：否1：是）垫付预付理算
			// 垫付
			List<PrpLPaymentVo> prpLPaymentList = new ArrayList<PrpLPaymentVo>();
			if(compensateNo.startsWith("D")){
				QueryRule qr = QueryRule.getInstance();
				qr.addEqual("compensateNo", compensateNo);
				List<PrpLPadPayMain> list =  databaseDao.findAll(PrpLPadPayMain.class, qr);
				if(list!=null&&list.size()>0){
					QueryRule queryRule = QueryRule.getInstance();
					queryRule.addEqual("prpLPadPayMain.id", list.get(0).getId());
					List<PrpLPadPayPerson> personList =  databaseDao.findAll(PrpLPadPayPerson.class, queryRule);
					if(personList!=null&&personList.size()>0){
						for(PrpLPadPayPerson prpLPadPayPerson:personList){
						    if(prpLPadPayPerson.getPayeeId().equals(payCustomVo.getId())){
    							PrpLPaymentVo prpLPaymentVo = new PrpLPaymentVo();
    							prpLPaymentVo.setSumRealPay(prpLPadPayPerson.getCostSum());
    							prpLPaymentVo.setPayeeId(prpLPadPayPerson.getPayeeId());
    							prpLPaymentList.add(prpLPaymentVo);
						    }
						}
					}
				}
			
			}else if(compensateNo.startsWith("Y")){
				List<PrpLPrePayVo> prePayPVos = compensateTaskService.getPrePayVo(compensateNo,"P");
				if(prePayPVos!=null && prePayPVos.size()>0){
					for(PrpLPrePayVo vo : prePayPVos){
					    if(vo.getPayeeId().equals(payCustomVo.getId())){
    						PrpLPaymentVo prpLPaymentVo = new PrpLPaymentVo();
    						prpLPaymentVo.setSumRealPay(vo.getPayAmt());
    						prpLPaymentVo.setPayeeId(vo.getPayeeId());
    						prpLPaymentList.add(prpLPaymentVo);
					    }
					}
					
				}
			}else{
				prpLPaymentList = compensateTaskService.findPrpLPayment(payCustomVo, compensateNo);
			}
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(payCustomVo.getRegistNo());
			// 自动支付
			if(prpLPaymentList!=null && prpLPaymentList.size()>0){
				//List<SendMsgParamVo> msgParamVoList = compensateTaskService.getsendMsgParamVo(prpLRegistVo, prpLPaymentList);
			    List<SendMsgParamVo> msgParamVoList = compensateTaskService.getSendMsgParamVoByRefund(prpLRegistVo, prpLPaymentList,payCustomVo);
			    if(msgParamVoList!=null && msgParamVoList.size() > 0){
			    	if(compensateNo.startsWith("Y")){
						PrpLCompensateVo compVo = compensateTaskService.findCompByPK(compensateNo);
						// 2017-12-13 如立案号下存在预付冲销任务（不管是否核赔通过），预付送收付的自动支付标志为否。
						boolean isExistWriteOffFlag = compensateTaskService.isExistWriteOff(compVo.getClaimNo(),"Y");
						if(isExistWriteOffFlag){
							accountVo.setIsFastReparation("0");
						}else{
							accountVo.setIsFastReparation("1");
						}
					}else{
						accountVo.setIsFastReparation("1");
					}
				}else{
					accountVo.setIsFastReparation("0");
				}
			}else{
				accountVo.setIsFastReparation("0");
			}
		}

		
		accountVosList.add(accountVo);
		
		accMainVo.setAccountVo(accountVosList);
		accMainVo.setPaytype(payBankVo.getPayType());
		// ErrorType Modifytype
		// 6 6
		// 3 选择【资金系统支付】6
		// 3 选择【收付系统支付】3
		if(CodeConstants.ErrorType.back.equals(payBankVo.getErrorType())){
			accMainVo.setModifytype(CodeConstants.ErrorType.back);
		}else{
			if(CodeConstants.RadioValue.RADIO_YES.equals(payBankVo.getIsAutoPay())){
				accMainVo.setModifytype(CodeConstants.ErrorType.back);
			}else{
				accMainVo.setModifytype(CodeConstants.ErrorType.payFailure);
			}
		}
		
		return accMainVo;
	}
	
	/**
	 * <pre>
	 * 发送报文至收付系统
	 * </pre>
	 * @param SVR_URL
	 * @param accMainVo
	 * @return JPlanReturnVo
	 * @throws Exception
	 * @modified: ☆Luwei(2016年12月27日 下午3:32:07): <br>
	 */
	private JPlanReturnVo sendXmlToPayment(PrpLPayCustomVo payCustomVo,SysUserVo userVo,String compensateNo,
	                                       String SVR_URL,AccMainVo accMainVo) throws Exception{
		
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");
		String requestXml = stream.toXML(accMainVo);
		String xString = "";
		logger.info("退票请求收付的报文---->"+requestXml);
		JPlanReturnVo jPlanReturnVo = new JPlanReturnVo();
		
		try{
			ClaimReturnTicketServiceLocator retuService = new ClaimReturnTicketServiceLocator();
			ClaimReturnTicketPortBindingStub stub = new ClaimReturnTicketPortBindingStub(new java.net.URL(SVR_URL), retuService);
			xString = stub.transPoliceDataForXml(requestXml);
		}catch(Exception e){
			e.printStackTrace();
			jPlanReturnVo.setErrorMessage(e.getMessage());
		}
		
		stream.processAnnotations(JPlanReturnVo.class);
		
		logger.info("退票请求收付返回报文：--->"+xString);
		if(xString!=null && !"".equals(xString)){
			jPlanReturnVo = (JPlanReturnVo)stream.fromXML(xString);
		}
		
		// 保存日志
		saveClaimInterfaceLog(payCustomVo,userVo,compensateNo,jPlanReturnVo,SVR_URL,requestXml,xString);
		return jPlanReturnVo;
	}
	
	/**
	 * <pre>
	 * 保存日志
	 * </pre>
	 * @param payCustomVo,userVo,compensateNo,jPlanReturnVo
	 * @param SVR_URL,requestXml,xString
	 * @return ClaimInterfaceLogVo
	 * @modified: ☆Luwei(2016年12月27日 下午3:59:27): <br>
	 */
	private ClaimInterfaceLogVo saveClaimInterfaceLog(PrpLPayCustomVo payCustomVo,SysUserVo userVo,String compensateNo,
										JPlanReturnVo jPlanReturnVo,String SVR_URL,String requestXml,String xString) {
		Date date = new Date();
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		logVo.setRegistNo(payCustomVo.getRegistNo());
		logVo.setBusinessType(BusinessType.ModAccount.name());
		logVo.setBusinessName(BusinessType.ModAccount.getName());
		logVo.setComCode(userVo.getComCode());
		logVo.setCompensateNo(compensateNo);
		logVo.setRequestTime(date);
		logVo.setRequestUrl(SVR_URL);
		if(jPlanReturnVo!=null&&jPlanReturnVo.isResponseCode()){
			logVo.setErrorCode("true");
			logVo.setStatus("1");
		}else{
			logVo.setErrorCode("false");
			logVo.setStatus("0");
		}
		logVo.setErrorMessage(jPlanReturnVo==null ? "无返回信息！" : jPlanReturnVo.getErrorMessage());
		logVo.setCreateTime(date);
		logVo.setCreateUser(userVo.getUserCode());
		logVo.setRequestXml(requestXml);
		logVo.setResponseXml(xString);

		ClaimInterfaceLogVo returnLogVo = logService.save(logVo);
		return returnLogVo;
	}
	
	/**
	 * 公估费退票保存日志
	 * @param registNo
	 * @param userVo
	 * @param compensateNo
	 * @param jPlanReturnVo
	 * @param SVR_URL
	 * @param requestXml
	 * @param xString
	 * @return
	 */
	private ClaimInterfaceLogVo  saveClaimInterfaceLogOfAssessorFee (String registNo,SysUserVo userVo,String compensateNo,
			JPlanReturnVo jPlanReturnVo,String SVR_URL,String requestXml,String xString) {
		Date date = new Date();
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		logVo.setRegistNo(registNo);
		logVo.setBusinessType(BusinessType.AssessorFee_BackTicket.name());
		logVo.setBusinessName(BusinessType.AssessorFee_BackTicket.getName());
		logVo.setComCode(userVo.getComCode());
		logVo.setCompensateNo(compensateNo);
		logVo.setRequestTime(date);
		logVo.setRequestUrl(SVR_URL);
		if(jPlanReturnVo!=null&&jPlanReturnVo.isResponseCode()){
			logVo.setErrorCode("true");
			logVo.setStatus("1");
		}else{
			logVo.setErrorCode("false");
			logVo.setStatus("0");
		}
		logVo.setErrorMessage(jPlanReturnVo==null ? "无返回信息！" : jPlanReturnVo.getErrorMessage());
		logVo.setCreateTime(date);
		logVo.setCreateUser(userVo.getUserCode());
		logVo.setRequestXml(requestXml);
		logVo.setResponseXml(xString);
		ClaimInterfaceLogVo returnLogVo = logService.save(logVo);
		return returnLogVo;
	}
	
	/**
	 * 根据计算书号查询prpdAccRollBackAccount,按退票时间降序
	 * @param certiNo
	 */
	public PrpDAccRollBackAccountVo findPrpdAccByCertiNo(String certiNo){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("certiNo", certiNo);
		qr.addDescOrder("rollBackTime");
		List<PrpDAccRollBackAccount> rollBackAccount = databaseDao.findAll(PrpDAccRollBackAccount.class, qr);
		if(rollBackAccount != null && rollBackAccount.size() > 0){
			PrpDAccRollBackAccountVo rollBackVo = new PrpDAccRollBackAccountVo();
			Beans.copy().from(rollBackAccount.get(0)).to(rollBackVo);
			return rollBackVo;
		}else{
			return null;
		}
	}
	
	public String saveOrUpdatePayCustom(PrpLPayCustomVo payCustomVo,String accountId,String compensateNo,SysUserVo userVo,
			PrpLPayBankVo payBankVo) throws Exception {
		PrpLPayCustomVo rePayCustom = new PrpLPayCustomVo();
		String oldAccountId = payCustomVo.getAccountId();
		payCustomVo.setId(null);
		Long oldPayeeId = payBankVo.getPayeeId();
		String returnPayeeId = String.valueOf(payBankVo.getPayeeId());

		Date date = new Date();// 获取时间
		String userCode = userVo.getUserCode();// 获取用户

		payCustomVo.setBankType("test");
		payCustomVo.setValidFlag("1");

		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		// 如果是结算单退票，组装结算单对应的结算书信息
		if(compensateNo.startsWith("P") || compensateNo.startsWith("J") || compensateNo.startsWith("G")){
			PrpDAccRollBackAccountVo rollBackAccountVo = 
					this.findRollBackByOldAccountId(compensateNo, oldAccountId, payBankVo.getPayType());
			if(rollBackAccountVo != null){
				payBankVoList = this.organizePayBankVo(rollBackAccountVo);
				for(PrpLPayBankVo vo:payBankVoList){
					vo.setSummary(payBankVo.getSummary());
				}
			}
		}else{
			payBankVoList.add(payBankVo);
		}
		
		/*如果accountid改变了，就根据报案号和修改后的accountid查询prplpaycustom表，
		如果查到就更新，否则新增一条数据；然后返回payeeid，回写业务表。*/
		if(accountId!=null&&!"null".equals(accountId)&&!"".equals(accountId)&&
				!accountId.equals(payCustomVo.getAccountId())){
			for(PrpLPayBankVo prplpaybankVo:payBankVoList){
				PrpLPayCustom payCustomPo = new PrpLPayCustom();
				payCustomPo = this.findPayCustom(prplpaybankVo.getRegistNo(), accountId);
				if(payCustomPo == null || payCustomPo.getId() == null){
					Beans.copy().from(payCustomVo).excludeNull().to(payCustomPo);
					payCustomPo.setAccountId(accountId);
					payCustomPo.setCreateTime(date);
					payCustomPo.setCreateUser(userVo.getUserCode());
					payCustomPo.setUpdateTime(date);
					payCustomPo.setUpdateUser(userVo.getUserCode());
					databaseDao.save(PrpLPayCustom.class,payCustomPo);
					// 把旧paycustom的validflag设置为0
					this.updatePayCustom(oldPayeeId);
				}else{
					Beans.copy().from(payCustomVo).excludeNull().to(payCustomPo);
					payCustomPo.setAccountId(accountId);
					payCustomPo.setUpdateTime(date);
					payCustomPo.setUpdateUser(userVo.getUserCode());
					databaseDao.update(PrpLPayCustom.class, payCustomPo);
				}
				// 回写业务表的payeeId及收款人信息
				this.writePayeeId(prplpaybankVo, oldPayeeId, payCustomPo.getId(), userVo);
				payBankVo.setPayeeId(payCustomPo.getId());
				returnPayeeId = String.valueOf(payCustomPo.getId());
			}
			payBankVo.setAccountId(accountId);
			
		}else{
			for(PrpLPayBankVo prplpaybankVo:payBankVoList){
				PrpLPayCustom payCustomPo = new PrpLPayCustom();
				payCustomPo = databaseDao.findByPK(PrpLPayCustom.class,prplpaybankVo.getPayeeId());
				Beans.copy().from(payCustomVo).excludeNull().to(payCustomPo);
				payCustomPo.setUpdateUser(userCode);
				payCustomPo.setUpdateTime(date);
				databaseDao.update(PrpLPayCustom.class,payCustomPo);
				payBankVo.setAccountId(payCustomPo.getAccountId());
				// 回写业务表的收款人信息
				this.writePayeeId(prplpaybankVo, oldPayeeId, oldPayeeId, userVo);
			}
		}
		
		this.saveAcc(payBankVo, userVo, oldAccountId,oldPayeeId);
		return returnPayeeId;
	}
	
	/**
	 * 根据registNo和accountId查询PrpLPayCustom
	 * @param registNo
	 * @param accountId
	 * @return
	 */
	public PrpLPayCustom findPayCustom(String registNo,String accountId){
		PrpLPayCustom payCustomPo = new PrpLPayCustom();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		qr.addEqual("accountId", accountId);
		List<PrpLPayCustom> list = databaseDao.findAll(PrpLPayCustom.class, qr);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return payCustomPo;
	}
	
	public void writePayeeId(PrpLPayBankVo payBankVo,Long oldPayeeId,Long payeeId,SysUserVo userVo){
		Date date = new Date();
		QueryRule qr = QueryRule.getInstance();
		if(payBankVo.getCompensateNo().startsWith("Y")){
			qr.addEqual("compensateNo", payBankVo.getCompensateNo());
			qr.addEqual("payeeId", oldPayeeId);
			if(!PaymentConstants.PAYREASON_P50.equals(payBankVo.getPayType())){
				qr.addEqual("chargeCode", payBankVo.getChargeCode());
				qr.addEqual("feeType", "F");
			}else{
				qr.addEqual("feeType", "P");
			}
			List<PrpLPrePay> list =  databaseDao.findAll(PrpLPrePay.class, qr);
			if(list!=null&&list.size()>0){
				for(PrpLPrePay prpLPrePay:list){
					prpLPrePay.setPayeeId(payeeId);
					prpLPrePay.setUpdateTime(date);
					prpLPrePay.setPayeeName(payBankVo.getAccountName());
					prpLPrePay.setAccountNo(payBankVo.getAccountNo());
					prpLPrePay.setBankName(payBankVo.getBankName());
					prpLPrePay.setSummary(payBankVo.getSummary());
					databaseDao.update(PrpLPrePay.class,prpLPrePay);
				}
			}
		}else if(payBankVo.getCompensateNo().startsWith("D")){
			qr.addEqual("compensateNo", payBankVo.getCompensateNo());
			List<PrpLPadPayMain> list =  databaseDao.findAll(PrpLPadPayMain.class, qr);
			if(list!=null&&list.size()>0){
				QueryRule queryRule = QueryRule.getInstance();
				queryRule.addEqual("prpLPadPayMain.id", list.get(0).getId());
				queryRule.addEqual("payeeId", oldPayeeId);
				List<PrpLPadPayPerson> personList =  databaseDao.findAll(PrpLPadPayPerson.class, queryRule);
				if(personList!=null&&personList.size()>0){
					for(PrpLPadPayPerson prpLPadPayPerson:personList){
						PrpLPadPayPerson prpLPadPayPersonPo = databaseDao.findByPK(PrpLPadPayPerson.class, prpLPadPayPerson.getId());
						prpLPadPayPersonPo.setPayeeId(payeeId);
						prpLPadPayPersonPo.setPayeeName(payBankVo.getAccountName());
						prpLPadPayPersonPo.setAccountNo(payBankVo.getAccountNo());
						prpLPadPayPersonPo.setBankName(payBankVo.getBankName());
						prpLPadPayPersonPo.setSummary(payBankVo.getSummary());
						databaseDao.update(PrpLPadPayPerson.class, prpLPadPayPersonPo);
					}
				}
			}
		}else{
			compensateTaskService.modAccountWritePayeeId(payBankVo, oldPayeeId, payeeId, userVo);
		}
	}
	
	public PrpLPrePayVo findPrePayByPayType(String compensateNo,String payType){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("compensateNo", compensateNo);
		if(!"P50".equals(payType)){
			qr.addEqual("chargeCode", CodeConstants.TransPayTypeMap.get(payType));
			qr.addEqual("feeType", "F");
		}else{
			qr.addEqual("feeType", "P");
		}
		List<PrpLPrePay> list =  databaseDao.findAll(PrpLPrePay.class, qr);
		if(list!=null && list.size()>0){
			PrpLPrePayVo prePayVo = new PrpLPrePayVo();
			Beans.copy().from(list.get(0)).to(prePayVo);
			return prePayVo;
		}else{
			return null;
		}
	}
	
	public PrpLChargeVo findChargeByPayType(String compensateNo,String payType){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("prpLCompensate.compensateNo", compensateNo);
		qr.addEqual("chargeCode", CodeConstants.TransPayTypeMap.get(payType));
		List<PrpLCharge> list =  databaseDao.findAll(PrpLCharge.class, qr);
		if(list!=null && list.size()>0){
			PrpLChargeVo chargeVo = new PrpLChargeVo();
			Beans.copy().from(list.get(0)).to(chargeVo);
			return chargeVo;
		}else{
			return null;
		}
	}
	
	public void updatePrplPayCustom(PrpLPayCustomVo prpLPayCustomVo){
		PrpLPayCustom prpLPayCustom = databaseDao.findByPK(PrpLPayCustom.class, prpLPayCustomVo.getId());
		Beans.copy().from(prpLPayCustomVo).excludeEmpty().to(prpLPayCustom);
		databaseDao.update(PrpLPayCustom.class, prpLPayCustom);
	}

	
	/**
	 * 根据ID把validFlag设置为0
	 * @param payeeId
	 */
	public void updatePayCustom(Long payeeId){
		Date date = new Date();
		PrpLPayCustom prpLPayCustom = databaseDao.findByPK(PrpLPayCustom.class, payeeId);
		prpLPayCustom.setValidFlag("0");
		prpLPayCustom.setUpdateTime(date);
		databaseDao.update(PrpLPayCustom.class, prpLPayCustom);
	}

	
	@Override
	public PrpLPayCustomVo findPayCustomVo(String registNo,String accountId){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		qr.addEqual("accountId", accountId);
		qr.addDescOrder("createTime");
		List<PrpLPayCustom> PrpLPayCustomList = databaseDao.findAll(PrpLPayCustom.class, qr);
		PrpLPayCustomVo prpLPayCustomVo = new PrpLPayCustomVo();
		if(PrpLPayCustomList != null && PrpLPayCustomList.size() > 0){
			Beans.copy().from(PrpLPayCustomList.get(0)).to(prpLPayCustomVo);
		}
		return prpLPayCustomVo;
	}

	
	public PrpLPayBankVo findPayBankVoById(BigDecimal id){
		PrpLPayBank prpLPayBank = databaseDao.findByPK(PrpLPayBank.class, id);
		PrpLPayBankVo prpLPayBankVo = new PrpLPayBankVo();
		if(prpLPayBank!=null && prpLPayBank.getId()!=null){
			Beans.copy().from(prpLPayBank).to(prpLPayBankVo);
		}
		return prpLPayBankVo;
	}
	
	public PrpLPayBankVo findPayBank(String compensateNo,String accountId,String payType){
		PrpLPayBankVo payBank = null;
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("compensateNo", compensateNo);
		qr.addEqual("payType", payType);
		qr.addEqual("accountId", accountId);
		qr.addIn("verifyStatus", "2","3");
		qr.addDescOrder("createTime");
		List<PrpLPayBank> oldPayBankList = databaseDao.findAll(PrpLPayBank.class,qr);
		if(oldPayBankList != null && !oldPayBankList.isEmpty()){
			payBank = Beans.copyDepth().from(oldPayBankList.get(0)).to(PrpLPayBankVo.class);
		}
		return payBank;
	}
	public PrpLPayBankVo findPayBankNew(String compensateNo,Long payeeId,String payType){
		PrpLPayBankVo payBank = null;
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("compensateNo", compensateNo);
		qr.addEqual("payeeId", payeeId);
        qr.addEqual("payType", payType);
		//qr.addEqual("payeeId", payeeId);
		qr.addIn("verifyStatus", "2","3");
		qr.addDescOrder("createTime");
		List<PrpLPayBank> oldPayBankList = databaseDao.findAll(PrpLPayBank.class,qr);
		if(oldPayBankList != null && !oldPayBankList.isEmpty()){
			payBank = Beans.copyDepth().from(oldPayBankList.get(0)).to(PrpLPayBankVo.class);
		}
		return payBank;
	}
	@Override
	public String reUpload(String compensateNo,String xml,SysUserVo userVo) throws Exception{
		String payType="";
		String accountId="";
		String[] array_Paytype=xml.split("<paytype>");
		if(array_Paytype.length>1){
			payType=array_Paytype[1].split("</paytype>")[0];
		}else{
			throw new IllegalArgumentException("报文缺少信息！");
		}
		String[] array_AccountId=xml.split("<AccountNo>");
		if(array_AccountId.length>1){
			accountId=array_AccountId[1].split("</AccountNo>")[0];
		}else{
			throw new IllegalArgumentException("报文缺少信息！");
		}
		String returnMsg="";
		PrpLPayBankVo payBankVo = this.findPayBank(compensateNo, accountId, payType);
		if(payBankVo!=null&&payBankVo.getPayeeId()!=null){
			PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(payBankVo.getPayeeId());
			this.setPayCustom(payCustomVo, payBankVo);
			returnMsg=this.reSendAccountToPayment(payCustomVo, userVo, compensateNo, payBankVo);
			return returnMsg;
		}else{
			returnMsg = "补传失败！";
			return returnMsg;
		}
	}
	@Override
	public String reUploadNewPayment(String compensateNo,String xml,SysUserVo userVo,String payeeIdAndSerialNo) throws Exception{
		String payType="";
		String accountId="";
		if(payeeIdAndSerialNo != null && !"".equals(payeeIdAndSerialNo)){
			String returnMsg="";
            String[] splitStr = payeeIdAndSerialNo.split("_");
            if(splitStr.length<3){
                returnMsg = "补传失败！";
                return returnMsg;
            }
            String payeeId = splitStr[0];
            String serialNo = splitStr[1];
            payType = splitStr[2];
			PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(Long.valueOf(payeeId));
			PrpLPayBankVo payBankVo = this.findPayBankNew(compensateNo, Long.valueOf(payeeId),payType);
			if(payBankVo!=null&&payBankVo.getPayeeId()!=null){
                payBankVo.setSerialNo(serialNo);
				returnMsg=this.reSendAccountToNewPayment(payCustomVo, userVo, compensateNo, payBankVo);
				return returnMsg;
			}else{
				returnMsg = "补传失败！";
				return returnMsg;
			}
		}else{//针对历史数据的补传方法
			String[] array_Paytype=xml.split("<paytype>");
			if(array_Paytype.length>1){
				payType=array_Paytype[1].split("</paytype>")[0];
			}
			String[] array_AccountId=xml.split("<AccountNo>");
			if(array_AccountId.length>1){
				accountId=array_AccountId[1].split("</AccountNo>")[0];
			}
			String returnMsg="";
			PrpLPayBankVo payBankVo = this.findPayBank(compensateNo, accountId, payType);
			if(payBankVo!=null&&payBankVo.getPayeeId()!=null){
				PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(payBankVo.getPayeeId());
				this.setPayCustom(payCustomVo, payBankVo);
				returnMsg=this.reSendAccountToNewPayment(payCustomVo, userVo, compensateNo, payBankVo);
				return returnMsg;
			}else{
				returnMsg = "补传失败！";
				return returnMsg;
			}
		}
	}
	@Override
	public String reUploadOfAssessorFeeBackTickit(String compensateNo,String registNo, SysUserVo userVo) throws Exception {
		String returnMsg = "补传成功";
		PrplInterrmAuditVo  auditVo=assessorService.findPrplInterrmAuditVoByRegistNoAndBussNoAndStatus(registNo, compensateNo, "1");
		PrpdIntermBankVo bankVo=new PrpdIntermBankVo();
		PrpDAccRollBackAccountVo accuntVo=new PrpDAccRollBackAccountVo();
		if(auditVo!=null && auditVo.getId()!=null){
		accuntVo=accountQueryService.findRollBackAccountById(auditVo.getBackAccountId());
		accuntVo.setOldAccountId(auditVo.getIsAutoPay());
		this.setParamsForPrpdIntermBank(bankVo,auditVo);
		 returnMsg=this.reSendAssessFeeAccountToPayment(bankVo, userVo, registNo, accuntVo, String.valueOf(auditVo.getOldBankId()), auditVo.getIntermCode(), auditVo);
		}else{
			throw new IllegalArgumentException("补传失败，在公估费退票审核表没有找到对应的数据！");
		}
		
		
		return returnMsg;
	}
	
	
	public String reSendAccountToPayment(PrpLPayCustomVo payCustomVo,SysUserVo userVo,String compensateNo,PrpLPayBankVo payBankVo) throws Exception {
		String returnPayeeId = String.valueOf(payCustomVo.getId());
		if(payCustomVo.getId() == null){
			logger.info("====payCustomVo.getId()为空，操作失败:===="+payCustomVo.getId());
			return returnPayeeId;
		}
		
		// 调收付的接口
		String SVR_URL = SpringProperties.getProperty("PAYMENT_SVR_URL");
		if(SVR_URL==null || payCustomVo == null){
			logger.warn("未配置收付地址，不调用收付接口。");
			throw new Exception("未配置收付服务地址。或未查找到赔款人账号信息！");
		}
		SVR_URL = SVR_URL+"/service/ClaimReturnTicket";// 收付地址用同一个，调用不同的服务再拼接 改sys_config表的配置数据
		// 组织accMainVo
		AccMainVo accMainVo = setAccMainVo(payCustomVo,userVo,compensateNo,payBankVo);
		// 发送报文
		JPlanReturnVo jPlanReturnVo = sendXmlToPayment(payCustomVo,userVo,compensateNo,SVR_URL,accMainVo);
		String returnMsg = "补传成功！";
		if(jPlanReturnVo != null){
			// 更新accountID
			String accountId = jPlanReturnVo.getAccountNo();
			logger.info("====accountId:===="+accountId);
			// 添加管控，如果银行账号相同的数据在系统中已存在，且户名不同，提示账户已存在户名不同，且不允许保存 硬管控yzy
			PrpLPayCustomVo existPayCus = payCustomService.adjustExistSamePayCusDifName(payCustomVo);
			if(existPayCus!=null){
				throw new IllegalArgumentException("保存失败！该账号已存于案件"+existPayCus.getRegistNo()+"，且户名为"+existPayCus.getPayeeName()+"！");
			}
			returnPayeeId=this.saveOrUpdatePayCustom(payCustomVo, accountId, compensateNo, userVo,payBankVo);
			if(!jPlanReturnVo.isResponseCode()){
				returnMsg = "补传失败！"+jPlanReturnVo.getErrorMessage();
			}
		}
		return returnMsg;
	}
	public String reSendAccountToNewPayment(PrpLPayCustomVo payCustomVo,SysUserVo userVo,String compensateNo,PrpLPayBankVo payBankVo) throws Exception {
		String returnPayeeId = String.valueOf(payCustomVo.getId());
		if(payCustomVo.getId() == null){
			logger.info("====payCustomVo.getId()为空，操作失败:===="+payCustomVo.getId());
			return returnPayeeId;
		}
		// 新收付地址
		String newpaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL");
		String returnpayeeid = payBankVo.getPayeeId() == null ? "" : payBankVo.getPayeeId().toString();
		if (newpaymentUrl == null || payCustomVo == null) {
			logger.info("未配置收付地址或无法查找到收款人账号信息，调用收付接口失败！");
			throw new Exception("未配置收付地址或无法查找到收款人账号信息，调用收付接口失败！");
		}
		try {
			String reqJson = packageRefundData(payCustomVo,compensateNo,payBankVo);
			logger.info("退票推送到收付的数据为：" + reqJson);
			sendRefundToNewPayment(compensateNo, reqJson, payBankVo, userVo);
			logger.info("业务号：{} 退票送收付完成！", compensateNo);
		} catch (Exception e) {
			logger.info("业务号：" + compensateNo + " 退票送收付异常！", e);
			throw e;
		}
		return returnpayeeid;
	}
	// 公估费退票补传方法
    public String reSendAssessFeeAccountToPayment(PrpdIntermBankVo bankVo,SysUserVo userVo,String registNo,PrpDAccRollBackAccountVo accuntVo,String bankId,String intermCode,PrplInterrmAuditVo auditVo)throws Exception{
    	String returnPayeeId=bankId;
		if(StringUtils.isBlank(returnPayeeId)){
			logger.info("====PrpdIntermBankVo.getId()为空，操作失败:===="+bankId);
			return bankId;
		}
		
		// 调收付的接口
		String SVR_URL = SpringProperties.getProperty("PAYMENT_SVR_URL");
		if(SVR_URL==null || StringUtils.isBlank(bankId)){
			logger.warn("未配置收付地址，不调用收付接口。");
			throw new Exception("未配置收付服务地址。或未查找到银行账号信息！");
		}
		SVR_URL = SVR_URL+"/service/ClaimReturnTicket";// 收付地址用同一个，调用不同的服务再拼接 改sys_config表的配置数据
		AccMainVo accMainVo = setAssorAccMainVo(bankVo,userVo,accuntVo,bankId);
		// 发送报文
		JPlanReturnVo jPlanReturnVo = sendXmlToAssPayment(bankVo,userVo,accuntVo.getCertiNo(),registNo,SVR_URL,accMainVo);
		String returnMsg = "补传成功！";
		if(jPlanReturnVo != null){
			// 更新accountID
			String accountId = jPlanReturnVo.getAccountNo();
			if(!jPlanReturnVo.isResponseCode() || StringUtils.isBlank(accountId)){
				returnMsg = "补传失败！"+jPlanReturnVo.getErrorMessage();
			}else{
				logger.info("====accountId:===="+accountId);
				returnPayeeId=this.saveOrUpdatePrpdIntermBank(bankVo,accuntVo,accountId, userVo,bankId,intermCode,auditVo,"0");
				returnMsg = "补传成功！";
			}
			
			
		}
		return returnMsg;
    }
	
	@Override
	public String sendAssessFeeAccountToPayment(PrpdIntermBankVo bankVo,SysUserVo userVo,String registNo,PrpDAccRollBackAccountVo accuntVo,String bankId,String intermCode,PrplInterrmAuditVo auditVo)
			throws Exception {
		String returnPayeeId=bankId;
		if(StringUtils.isBlank(returnPayeeId)){
			logger.info("====PrpdIntermBankVo.getId()为空，操作失败:===="+bankId);
			return bankId;
		}
		
		// 调收付的接口
		String SVR_URL = SpringProperties.getProperty("PAYMENT_SVR_URL");
		if(SVR_URL==null || StringUtils.isBlank(bankId)){
			logger.warn("未配置收付地址，不调用收付接口。");
			throw new Exception("未配置收付服务地址。或未查找到银行账号信息！");
		}
		SVR_URL = SVR_URL+"/service/ClaimReturnTicket";// 收付地址用同一个，调用不同的服务再拼接 改sys_config表的配置数据
		AccMainVo accMainVo = setAssorAccMainVo(bankVo,userVo,accuntVo,bankId);
		// 发送报文
		JPlanReturnVo jPlanReturnVo = sendXmlToAssPayment(bankVo,userVo,accuntVo.getCertiNo(),registNo,SVR_URL,accMainVo);
		String returnMsg = "操作成功！";


		if(jPlanReturnVo != null){
			String accountId = jPlanReturnVo.getAccountNo();
			if(!jPlanReturnVo.isResponseCode() || StringUtils.isBlank(accountId)){
				// 回写公估费退票审核表
				assessorService.updateOrSaveOfPrplInterrmAudit(auditVo);
				if(accuntVo!=null){
					PrpDAccRollBackAccount backpo=new PrpDAccRollBackAccount();
					  Beans.copy().from(accuntVo).to(backpo);
					  backpo.setStatus("0");
					  backpo.setAuditFlag("1");
					  backpo.setInfoFlag("1");
					  databaseDao.update(PrpDAccRollBackAccount.class,backpo);
				}
				returnMsg = "操作失败！"+jPlanReturnVo.getErrorMessage();
				
				throw new IllegalArgumentException("请求退票接口失败，请到接口平台补传！"+jPlanReturnVo.getErrorMessage()+"accountId="+accountId);
			}else{
				logger.info("====accountId:===="+accountId);
				returnPayeeId=this.saveOrUpdatePrpdIntermBank(bankVo,accuntVo,accountId, userVo,bankId,intermCode,auditVo,"1");
				returnMsg = "操作成功！";
			}


		}
		return returnMsg;
	}

	/**
	 * 公估费退票推送至新收付
	 * @param bankVo
	 * @param userVo
	 * @param registNo
	 * @param accuntVo
	 * @param bankId
	 * @param intermCode
	 * @param auditVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public String sendAssessFeeAccountToNewPayment(PrpdIntermBankVo bankVo, SysUserVo userVo, String registNo, PrpDAccRollBackAccountVo accuntVo,
												   String bankId, String intermCode, PrplInterrmAuditVo auditVo) throws Exception {

		// 收付接口地址
		String newpaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL");
		if (newpaymentUrl == null || bankId == null) {
			logger.info("未配置收付地址或无法查找到收款人账号信息，调用收付接口失败！");
			throw new Exception("未配置收付地址或无法查找到收款人账号信息，调用收付接口失败！");
		}
		String returnMsg = "操作成功！";
		try {
			String refundToNewPaymentJson = packageAssessorFeeRefundData(accuntVo, bankVo);
			logger.info("公估费退票推送到收付的数据为：" + refundToNewPaymentJson);
			ResponseDto responseDto = sendAssessorOrCheckFeeRefundToNewPayment(accuntVo.getCertiNo(), refundToNewPaymentJson, registNo, userVo);
			logger.info("业务号：{} 公估费退票送收付完成！", accuntVo.getCertiNo());
			if (PaymentConstants.RESP_FAILED.equals(responseDto.getResponseCode())) {
				// 更新公估费退票审核数据
				assessorService.updateOrSaveOfPrplInterrmAudit(auditVo);
				// 更新退票任务数据状态
				PrpDAccRollBackAccount backpo = new PrpDAccRollBackAccount();
				Beans.copy().from(accuntVo).to(backpo);
				backpo.setStatus("0");
				backpo.setAuditFlag("1");
				backpo.setInfoFlag("1");
				databaseDao.update(PrpDAccRollBackAccount.class, backpo);
				returnMsg = "公估费退票失败！收付失败信息："+responseDto.getErrorMessage();
			} else {
				bankId = this.saveOrUpdatePrpdIntermBank(bankVo, accuntVo, bankId, userVo, bankId, intermCode, auditVo, "1");
				returnMsg = "操作成功！";
			}
		} catch (Exception e) {
			logger.info("业务号：" + accuntVo.getCertiNo() + " 公估费退票送收付异常！", e);
			throw e;
		}

		return returnMsg;
	}

	/**
	 * 公估费退票送收付组织报文
	 * @param bankVo
	 * @param userVo
	 * @param accuntVo
	 * @return
	 */
	private AccMainVo setAssorAccMainVo(PrpdIntermBankVo bankVo,SysUserVo userVo,PrpDAccRollBackAccountVo roaccuntVo,String bankId){
		AccMainVo accMainVo = new AccMainVo();
		Date date = new Date();
		List<AccRecAccountVo> accountVosList = new ArrayList<AccRecAccountVo>();
		AccRecAccountVo accountVo = new AccRecAccountVo();
		accMainVo.setCertiNo(roaccuntVo.getCertiNo());
		accMainVo.setOperateComCode(userVo.getComCode());
		accMainVo.setModifyCode(userVo.getUserCode());
		accMainVo.setModifyTime(DateUtils.dateToStr(date, DateUtils.YToDay));
//		accMainVo.setPaytype("c");
		accMainVo.setStatus("0");
		accountVo.setAccountCode(bankVo.getAccountNo());
		
		accountVo.setAccountNo(roaccuntVo.getAccountId());// 原accountId
		
		accountVo.setBankCode(bankVo.getBankName());
		accountVo.setCurrency("01");// 账户币种
		if(StringUtils.isNotBlank(bankVo.getCity())){
			accountVo.setProvincial(bankVo.getCity().substring(0,2)+"0000");
		}
		
		accountVo.setCity(bankVo.getCity());
		
		accountVo.setNameOfBank(bankVo.getBankOutlets());
		accountVo.setAccountName(bankVo.getAccountName());
		accountVo.setTelephone(bankVo.getMobile());
		accountVo.setClientType("1");// 客户类型i
		accountVo.setClientNo("1");
		accountVo.setClientName(bankVo.getAccountName());
		accountVo.setAccountType("1");
		accountVo.setDefaultFlag("0");
		if(StringUtils.isNotBlank(bankId)){
			// 旧的银行信息vo
			PrpdIntermBankVo intermbank1Vo=managerService.findPrpdIntermBankVoById(Long.valueOf(bankId));
			if(intermbank1Vo!=null){
				accountVo.setCreateCode(intermbank1Vo.getCreateUser());
			}
		}
		
		accountVo.setCreateBranch(userVo.getComCode());
		accountVo.setValidStatus("1");
		accountVo.setFlag(bankVo.getBankNumber());// 联行号
		accountVo.setIdentifyType("");
		accountVo.setIdentifyNumber("");
		accountVo.setAccType(CodeConstants.Acctype.company);// 账户类型i，公估费默认公司账号
		accountVo.setActType("101");
		accountVo.setBankCode(bankVo.getBankName());
		accountVo.setCreateDate(date);
		
		accountVo.setIsFastReparation("0");
		if(StringUtils.isBlank(bankVo.getRemark())){
			accountVo.setAbstractcontent("公估费");
		}else{
			accountVo.setAbstractcontent(bankVo.getRemark());
		}
		
		if(!"3".equals(roaccuntVo.getErrorType())){
			accountVo.setIsAutoPay(roaccuntVo.getIsAutoPay());
		}else{
			// 判断是否送资金
			if("1".equals(roaccuntVo.getOldAccountId())){
				accountVo.setIsAutoPay("1");
			}else if("0".equals(roaccuntVo.getOldAccountId())){
				accountVo.setIsAutoPay("0");
			}
		}
		// 是否对公对私
		if(StringUtils.isNotBlank(bankVo.getPublicAndPrivate())){
			accountVo.setPublicPrivateFlag(bankVo.getPublicAndPrivate());;
		}else{
			accountVo.setPublicPrivateFlag("0");
		}
		
		accountVosList.add(accountVo);
		
		accMainVo.setAccountVo(accountVosList);
		accMainVo.setPaytype(roaccuntVo.getPayType());
		if(roaccuntVo!=null && StringUtils.isNotBlank(roaccuntVo.getCertiNo())){
			if(roaccuntVo.getCertiNo().startsWith("P") || roaccuntVo.getCertiNo().startsWith("G") || roaccuntVo.getCertiNo().startsWith("J")){
				accMainVo.setCertiType("P");
			}else{
				accMainVo.setCertiType("C");
			}
		}
		// 资金失败 errortype-3，理算可以选择“资金系统支付”和“收付系统支付”按钮，如果选择“资金系统支付”modifytype-6；
		// 选择“收付系统支付”，modifytype-3。
		// （实际上选择“资金系统支付”很少，因为如果错了都会做退票，但是不排除这个情况）

		if(!"3".equals(roaccuntVo.getErrorType())){
			if("1".equals(roaccuntVo.getIsAutoPay())){
				accMainVo.setModifytype("6");
			}else if("0".equals(roaccuntVo.getIsAutoPay())){
				accMainVo.setModifytype("3");
			}else{
				accMainVo.setModifytype("6");
			}
		}else{
			if("1".equals(roaccuntVo.getOldAccountId())){
				accMainVo.setModifytype("6");
			}else if("0".equals(roaccuntVo.getOldAccountId())){
				accMainVo.setModifytype("3");
			}else{
				accMainVo.setModifytype("6");
			}
		}
		
		return accMainVo;
	}
	
	/**
	 * 公估费退票报文送收付
	 * @param bankVo
	 * @param userVo
	 * @param compensateNo
	 * @param registNo
	 * @param SVR_URL
	 * @param accMainVo
	 * @return
	 * @throws Exception
	 */
  private JPlanReturnVo sendXmlToAssPayment(PrpdIntermBankVo bankVo,SysUserVo userVo,String compensateNo,String registNo,
            String SVR_URL,AccMainVo accMainVo) throws Exception{

    XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
    stream.autodetectAnnotations(true);
    stream.setMode(XStream.NO_REFERENCES);
    stream.aliasSystemAttribute(null,"class");
    String requestXml = stream.toXML(accMainVo);
    String xString = "";
    JPlanReturnVo jPlanReturnVo = new JPlanReturnVo();

    try{
    	ClaimReturnTicketServiceLocator retuService = new ClaimReturnTicketServiceLocator();
        ClaimReturnTicketPortBindingStub stub = new ClaimReturnTicketPortBindingStub(new java.net.URL(SVR_URL), retuService);
			logger.info("退票请求收付的报文---->"+requestXml);
        xString = stub.transPoliceDataForXml(requestXml);
        stream.processAnnotations(JPlanReturnVo.class);
    }catch(Exception e){
			e.printStackTrace();
			jPlanReturnVo.setErrorMessage(e.getMessage());
		}

		logger.info("退票请求收付返回报文：--->"+xString);
    if(xString!=null && !"".equals(xString)){
    	jPlanReturnVo = (JPlanReturnVo)stream.fromXML(xString);
    }

		// 保存日志
     
     saveClaimInterfaceLogOfAssessorFee(registNo,userVo,compensateNo,jPlanReturnVo,SVR_URL,requestXml,xString);
      return jPlanReturnVo;
      
     }
  
	@Override
	public List<PrpLPayBankVo> organizePayBankVo(PrpDAccRollBackAccountVo rollBackAccountVo){
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		List<PrpLCompensateVo> compByPaymentList = compensateTaskService.findCompensateBySettleNo(rollBackAccountVo.getCertiNo(),"P");
		
		// 一个结算单只对应一个赔款类型
		// 理算赔款
		if(compByPaymentList!=null && compByPaymentList.size()>0){
			for(PrpLCompensateVo compensateVo:compByPaymentList){
				PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(compensateVo.getRegistNo(), compensateVo.getPolicyNo());
				List<PrpLPaymentVo> paymentVoList = this.findPaymentBySettleNo(rollBackAccountVo.getCertiNo(),compensateVo.getCompensateNo());
				if(paymentVoList!=null && paymentVoList.size()>0){
					for(PrpLPaymentVo paymentVo:paymentVoList){
						PrpLPayBankVo payBankVo = new PrpLPayBankVo();
						payBankVo.setSettleNo(rollBackAccountVo.getCertiNo());
						payBankVo.setPayType(rollBackAccountVo.getPayType());
						payBankVo.setIsAutoPay(rollBackAccountVo.getIsAutoPay());
						payBankVo.setErrorType(rollBackAccountVo.getErrorType());
						payBankVo.setErrorMessage(rollBackAccountVo.getErrorMessage());
						payBankVo.setPayeeId(paymentVo.getPayeeId());
						payBankVo.setCompensateNo(compensateVo.getCompensateNo());
						payBankVo.setPolicyNo(compensateVo.getPolicyNo());
						payBankVo.setClaimNo(compensateVo.getClaimNo());
						payBankVo.setvClaimTime(compensateVo.getUnderwriteDate());
						payBankVo.setInsuredCode(prpLCMainVo.getInsuredCode());
						payBankVo.setInsuredName(prpLCMainVo.getInsuredName());
						payBankVo.setRegistNo(compensateVo.getRegistNo());
						payBankVo.setSummary(paymentVo.getSummary());
						payBankVoList.add(payBankVo);
					}
				}
			}
		}else{
			// 理算费用
			List<PrpLCompensateVo> compByChargeList = compensateTaskService.findCompensateBySettleNo(rollBackAccountVo.getCertiNo(),"F");
			if(compByChargeList!=null && compByChargeList.size()>0){
				for(PrpLCompensateVo compensateVo:compByChargeList){
					PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(compensateVo.getRegistNo(), compensateVo.getPolicyNo());
					List<PrpLChargeVo> chargeVoList = this.findChargeBySettleNo(rollBackAccountVo.getCertiNo(),compensateVo.getCompensateNo());
					if(chargeVoList!=null && chargeVoList.size()>0){
						for(PrpLChargeVo chargeVo:chargeVoList){
							PrpLPayBankVo payBankVo = new PrpLPayBankVo();
							payBankVo.setSettleNo(rollBackAccountVo.getCertiNo());
							payBankVo.setPayType(rollBackAccountVo.getPayType());
							payBankVo.setIsAutoPay(rollBackAccountVo.getIsAutoPay());
							payBankVo.setErrorType(rollBackAccountVo.getErrorType());
							payBankVo.setErrorMessage(rollBackAccountVo.getErrorMessage());
							payBankVo.setPayeeId(chargeVo.getPayeeId());
							payBankVo.setCompensateNo(compensateVo.getCompensateNo());
							payBankVo.setPolicyNo(compensateVo.getPolicyNo());
							payBankVo.setClaimNo(compensateVo.getClaimNo());
							payBankVo.setvClaimTime(compensateVo.getUnderwriteDate());
							payBankVo.setInsuredCode(prpLCMainVo.getInsuredCode());
							payBankVo.setInsuredName(prpLCMainVo.getInsuredName());
							payBankVo.setRegistNo(compensateVo.getRegistNo());
							payBankVo.setChargeCode(chargeVo.getChargeCode());
							payBankVo.setSummary(chargeVo.getSummary());
							payBankVoList.add(payBankVo);
						}
					}
				}
			}else{
				// 预付
				List<PrpLPrePayVo> prePayVoList = this.findPrePayBySettleNo(rollBackAccountVo.getCertiNo());
				if(prePayVoList!=null && prePayVoList.size()>0){
					for(PrpLPrePayVo prePayVo:prePayVoList){
						PrpLCompensateVo compensateVo = compensateTaskService.findCompByPK(prePayVo.getCompensateNo());
						PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(compensateVo.getRegistNo(), compensateVo.getPolicyNo());
						PrpLPayBankVo payBankVo = new PrpLPayBankVo();
						payBankVo.setSettleNo(rollBackAccountVo.getCertiNo());
						payBankVo.setPayType(rollBackAccountVo.getPayType());
						payBankVo.setIsAutoPay(rollBackAccountVo.getIsAutoPay());
						payBankVo.setErrorType(rollBackAccountVo.getErrorType());
						payBankVo.setErrorMessage(rollBackAccountVo.getErrorMessage());
						payBankVo.setPayeeId(prePayVo.getPayeeId());
						payBankVo.setCompensateNo(compensateVo.getCompensateNo());
						payBankVo.setPolicyNo(compensateVo.getPolicyNo());
						payBankVo.setClaimNo(compensateVo.getClaimNo());
						payBankVo.setvClaimTime(compensateVo.getUnderwriteDate());
						payBankVo.setInsuredCode(prpLCMainVo.getInsuredCode());
						payBankVo.setInsuredName(prpLCMainVo.getInsuredName());
						payBankVo.setRegistNo(compensateVo.getRegistNo());
						payBankVo.setChargeCode(prePayVo.getChargeCode());
						payBankVo.setSummary(prePayVo.getSummary());
						payBankVoList.add(payBankVo);
					}
				}else{
					// 垫付
					List<PrpLPadPayMainVo> padPayMainList = this.findpadPayBySettleNo(rollBackAccountVo.getCertiNo());
					if(padPayMainList!=null && padPayMainList.size()>0){
						for(PrpLPadPayMainVo padPayMain:padPayMainList){
							List<PrpLPadPayPersonVo> padPayPersonList = 
									this.findPadPayPerson(padPayMain.getId(), rollBackAccountVo.getCertiNo());
							if(padPayPersonList!=null && padPayPersonList.size()>0){
								PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(padPayMain.getRegistNo(), padPayMain.getPolicyNo());
								for(PrpLPadPayPersonVo padPayPersonVo:padPayPersonList){
									PrpLPayBankVo payBankVo = new PrpLPayBankVo();
									payBankVo.setSettleNo(rollBackAccountVo.getCertiNo());
									payBankVo.setPayType(rollBackAccountVo.getPayType());
									payBankVo.setIsAutoPay(rollBackAccountVo.getIsAutoPay());
									payBankVo.setErrorType(rollBackAccountVo.getErrorType());
									payBankVo.setErrorMessage(rollBackAccountVo.getErrorMessage());
									payBankVo.setPayeeId(padPayPersonVo.getPayeeId());
									payBankVo.setCompensateNo(padPayMain.getCompensateNo());
									payBankVo.setPolicyNo(padPayMain.getPolicyNo());
									payBankVo.setClaimNo(padPayMain.getClaimNo());
									payBankVo.setvClaimTime(padPayMain.getUnderwriteDate());
									payBankVo.setInsuredCode(prpLCMainVo.getInsuredCode());
									payBankVo.setInsuredName(prpLCMainVo.getInsuredName());
									payBankVo.setRegistNo(padPayMain.getRegistNo());
									payBankVo.setSummary(padPayPersonVo.getSummary());
									payBankVoList.add(payBankVo);
								}
							}
						}
					}
				}
			}
		}
		
		return payBankVoList;
	}
	
	public List<PrpLPaymentVo> findPaymentBySettleNo(String settleNo,String compensateNo){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("settleNo", settleNo);
		qr.addEqual("prpLCompensate.compensateNo", compensateNo);
		List<PrpLPayment> paymentList = databaseDao.findAll(PrpLPayment.class, qr);
		List<PrpLPaymentVo> paymentVoList = new ArrayList<PrpLPaymentVo>();
		if(paymentList!=null && paymentList.size()>0){
			for(PrpLPayment payment:paymentList){
				PrpLPaymentVo paymentVo = new PrpLPaymentVo();
				Beans.copy().from(payment).to(paymentVo);
				paymentVoList.add(paymentVo);
			}
		}
		return paymentVoList;
	}
	
	public List<PrpLChargeVo> findChargeBySettleNo(String settleNo,String compensateNo){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("settleNo", settleNo);
		qr.addEqual("prpLCompensate.compensateNo", compensateNo);
		List<PrpLCharge> chargeList = databaseDao.findAll(PrpLCharge.class, qr);
		List<PrpLChargeVo> chargeVoList = new ArrayList<PrpLChargeVo>();
		if(chargeList!=null && chargeList.size()>0){
			for(PrpLCharge charge:chargeList){
				PrpLChargeVo chargeVo = new PrpLChargeVo();
				Beans.copy().from(charge).to(chargeVo);
				chargeVoList.add(chargeVo);
			}
		}
		return chargeVoList;
	}
	
	public List<PrpLPrePayVo> findPrePayBySettleNo(String settleNo){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("settleNo", settleNo);
		List<PrpLPrePay> prePayList = databaseDao.findAll(PrpLPrePay.class, qr);
		List<PrpLPrePayVo> prePayVoList = new ArrayList<PrpLPrePayVo>();
		if(prePayList!=null && prePayList.size()>0){
			prePayVoList = Beans.copyDepth().from(prePayList).toList(PrpLPrePayVo.class);
		}
		return prePayVoList;
	}
	
	public List<PrpLPadPayMainVo> findpadPayBySettleNo(String settleNo){
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLPadPayMain pad where 1=1 ");
		sqlUtil.append(" and exists(select 1 from PrpLPadPayPerson person where  ");
		sqlUtil.append(" pad.id=person.prpLPadPayMain.id and person.settleNo=?) ");
		sqlUtil.addParamValue(settleNo);
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<PrpLPadPayMain> padPayMainList = databaseDao.findAllByHql(PrpLPadPayMain.class, sql, values);
		List<PrpLPadPayMainVo> padPayMainVoList = new ArrayList<PrpLPadPayMainVo>();
		if(padPayMainList!=null && padPayMainList.size()>0){
			for(PrpLPadPayMain padPayMain:padPayMainList){
				PrpLPadPayMainVo padPayMainVo = new PrpLPadPayMainVo();
				Beans.copy().from(padPayMain).to(padPayMainVo);
				padPayMainVoList.add(padPayMainVo);
			}
		}
		return padPayMainVoList;
	}
	
	public List<PrpLPadPayPersonVo> findPadPayPerson(Long padPayMainId,String settleNo){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("settleNo", settleNo);
		qr.addEqual("prpLPadPayMain.id", padPayMainId);
		List<PrpLPadPayPerson> padPayPersonList = databaseDao.findAll(PrpLPadPayPerson.class, qr);
		List<PrpLPadPayPersonVo> padPayPersonVoList = new ArrayList<PrpLPadPayPersonVo>();
		if(padPayPersonList!=null && padPayPersonList.size()>0){
			for(PrpLPadPayPerson padPayPerson:padPayPersonList){
				PrpLPadPayPersonVo padPayPersonVo = new PrpLPadPayPersonVo();
				Beans.copy().from(padPayPerson).to(padPayPersonVo);
				padPayPersonVoList.add(padPayPersonVo);
			}
		}
		return padPayPersonVoList;
	}

    
   
	@SuppressWarnings("unused")
	public String saveOrUpdatePrpdIntermBank(PrpdIntermBankVo bankVo, PrpDAccRollBackAccountVo roAccountVo, String accountId, SysUserVo userVo, String bankId, String intermCode, PrplInterrmAuditVo
			auditVo, String index) throws Exception {

		// 旧的银行信息vo
		PrpdIntermBankVo intermbank1Vo = managerService.findPrpdIntermBankVoById(Long.valueOf(bankId));
		// 旧的银行信息po
		PrpdIntermBank prpdIntermBank1 = databaseDao.findByPK(PrpdIntermBank.class, intermbank1Vo.getId());
		// 是否更新的bankVo数据
		PrpdIntermBankVo intermbank2Vo = managerService.findPrpdIntermBankVosByIntermMainIdAndAccountNo(prpdIntermBank1.getPrpdIntermMain().getId(), bankVo.getAccountNo());

		// 旧数据中存在修改后的账号，则更新此条数据的VaildFlag这个字段
		PrpdIntermBank prpdIntermBank2 = new PrpdIntermBank();
		if (intermbank2Vo != null && intermbank2Vo.getId() != null) {
			prpdIntermBank2 = databaseDao.findByPK(PrpdIntermBank.class, intermbank2Vo.getId());
		}

		PrpdIntermMain prpdIntermMain = prpdIntermBank1.getPrpdIntermMain();
		// intermbank2Vo为空，则新增，如果intermbank2Vo不为空，则更新
		if (intermbank2Vo.getId() == null) {
			// 更新该公估机构下的银行基础数据,这将该条数据的VaildFlag=1，不是则VaildFlag=0
			bankVo.setVaildFlag("1");
			bankVo.setCreateTime(new Date());
			bankVo.setCreateUser(userVo.getUserCode());
			bankVo.setAccountId(accountId);
			bankVo.setBankType("test");
			PrpdIntermBank po = new PrpdIntermBank();
			Beans.copy().from(bankVo).to(po);
			po.setPrpdIntermMain(prpdIntermMain);
			po.setUpdateUser(userVo.getUserCode());
			po.setUpdateTime(new Date());
			databaseDao.save(PrpdIntermBank.class, po);
			if (prpdIntermBank1 != null) {
				prpdIntermBank1.setVaildFlag("0");
				prpdIntermBank1.setUpdateTime(new Date());
				prpdIntermBank1.setUpdateUser(userVo.getUserCode());
				databaseDao.update(PrpdIntermBank.class, prpdIntermBank1);

			}

			// 回写银行ID到PrpLAssessorFee中的BankId字段
			List<PrpLAssessorFeeVo> listvo = assessorService.findPrpLAssessorFeeVoByCompensateNoOrEndNo(roAccountVo.getCertiNo());
			if (listvo != null && listvo.size() > 0) {
				for (PrpLAssessorFeeVo vo : listvo) {
					PrpLAssessorFee feePo = databaseDao.findByPK(PrpLAssessorFee.class, vo.getId());
					feePo.setBankId(po.getId());
					feePo.setRemark(bankVo.getRemark());// 摘要
					feePo.setUpdateTime(new Date());
					feePo.setUpdateUser(userVo.getUserCode());
					databaseDao.update(PrpLAssessorFee.class, feePo);
				}
			}
		} else {
			prpdIntermBank1.setVaildFlag("0");
			// 先更新旧数据的VaildFlag=0,因为每个公估机构下，只能有一个有效的银行
			databaseDao.update(PrpdIntermBank.class, prpdIntermBank1);
			// 将该公估机构下，与本次修改的银行账号一致的账号数据，进行更新，并且将VaildFlag=1；
			bankVo.setUpdateUser(userVo.getUserCode());
			bankVo.setUpdateTime(new Date());
			Beans.copy().from(bankVo).excludeNull().to(prpdIntermBank2);
			prpdIntermBank2.setVaildFlag("1");

			databaseDao.update(PrpdIntermBank.class, prpdIntermBank2);

			// 回写银行ID到PrpLAssessorFee中的BankId字段
			List<PrpLAssessorFeeVo> listvo = assessorService.findPrpLAssessorFeeVoByCompensateNoOrEndNo(roAccountVo.getCertiNo());
			if (listvo != null && listvo.size() > 0) {
				for (PrpLAssessorFeeVo vo : listvo) {
					PrpLAssessorFee feePo = databaseDao.findByPK(PrpLAssessorFee.class, vo.getId());
					feePo.setBankId(prpdIntermBank2.getId());
					feePo.setRemark(bankVo.getRemark());// 摘要
					feePo.setUpdateTime(new Date());
					feePo.setUpdateUser(userVo.getUserCode());
					databaseDao.update(PrpLAssessorFee.class, feePo);
				}
			}
		}
		// 回写PrpDAccRollBackAccount表的accountId,status字段
		if (roAccountVo != null) {
			List<PrpDAccRollBackAccount> backAccountPos = new ArrayList<PrpDAccRollBackAccount>();
			List<PrpDAccRollBackAccountVo> backAccountVos = accountQueryService.findPrpDAccRollBackAccountVosByCertiNoAndPayTypeAndAccountId(roAccountVo.getCertiNo(), roAccountVo.getPayType(), roAccountVo.getAccountId());
			if (backAccountVos != null && backAccountVos.size() > 0) {
				for (PrpDAccRollBackAccountVo vo : backAccountVos) {
					vo.setAccountId(accountId);
					vo.setStatus("0");
					vo.setAuditFlag("1");
					vo.setInfoFlag("1");
				}
			}
			backAccountPos = Beans.copyDepth().from(backAccountVos).toList(PrpDAccRollBackAccount.class);
			databaseDao.saveAll(PrpDAccRollBackAccount.class, backAccountPos);

		}
		// 回写公估费退票审核表
		if (index.equals("1")) {
			assessorService.updateOrSaveOfPrplInterrmAudit(auditVo);
		}

		return "成功";
	}

	/**
	 * 给公估费银行表Vo赋值
	 * @param intermbankVo
	 * @param auditVo
	 */
	public void setParamsForPrpdIntermBank(PrpdIntermBankVo intermbankVo,PrplInterrmAuditVo auditVo){
		intermbankVo.setAccountName(auditVo.getAccountName());
		intermbankVo.setAccountNo(auditVo.getAccountCode());
		intermbankVo.setCertifyNo(auditVo.getCertifyNo());
		intermbankVo.setMobile(auditVo.getMobile());
		intermbankVo.setPublicAndPrivate(auditVo.getPublicAndprivate());
		intermbankVo.setCity(auditVo.getCity());
		intermbankVo.setBankName(auditVo.getBankName());
		intermbankVo.setBankOutlets(auditVo.getBankoutLets());
		intermbankVo.setBankNumber(auditVo.getBankNumber());
		intermbankVo.setRemark(auditVo.getRemark());
		
	}
	
	@Override
	public void updatePayBank(BigDecimal id){
		PrpLPayBank prplPayBank = databaseDao.findByPK(PrpLPayBank.class, id);
		if(prplPayBank!=null && prplPayBank.getId()!=null){
			prplPayBank.setVerifyStatus("2");
		}
		databaseDao.update(PrpLPayBank.class, prplPayBank);
	}
	
	/**
	 * 根据垫付主表ID和收款人表ID查垫付子表
	 * @param mainId
	 * @param payeeId
	 * @return
	 */
	public PrpLPadPayPersonVo findPadPayPersonVo(Long mainId,Long payeeId){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("prpLPadPayMain.id", mainId);
		qr.addEqual("payeeId", payeeId);
		List<PrpLPadPayPerson> padPayPersonList = databaseDao.findAll(PrpLPadPayPerson.class, qr);
		PrpLPadPayPersonVo padPayPersonVo = new PrpLPadPayPersonVo();
		if(padPayPersonList!=null && padPayPersonList.size()>0){
			Beans.copy().from(padPayPersonList.get(0)).to(padPayPersonVo);
		}
		return padPayPersonVo;
	}
	
	/**
	 * 根据计算书号和收款人ID查询理算赔款表
	 * @param compensateNo
	 * @param payeeId
	 * @return
	 */
	public PrpLPaymentVo findPaymentVo(String compensateNo,Long payeeId){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("prpLCompensate.compensateNo", compensateNo);
		qr.addEqual("payeeId", payeeId);
		List<PrpLPayment> paymentList = databaseDao.findAll(PrpLPayment.class, qr);
		PrpLPaymentVo paymentVo = new PrpLPaymentVo();
		if(paymentList!=null && paymentList.size()>0){
			Beans.copy().from(paymentList.get(0)).to(paymentVo);
		}
		return paymentVo;
	}

	/* 
	 * @see ins.sino.claimcar.other.service.AccountInfoService#sendCheckFeeAccountToPayment(ins.sino.claimcar.manager.vo.PrpdcheckBankVo, ins.platform.vo.SysUserVo, java.lang.String, ins.sino.claimcar.other.vo.PrpDAccRollBackAccountVo, java.lang.String, java.lang.String, ins.sino.claimcar.other.vo.PrpLCheckmAuditVo)
	 * @param checkbankVo
	 * @param userVo
	 * @param registNo
	 * @param backAccountVo
	 * @param bankId
	 * @param checkCode
	 * @param auditVo
	 * @return
	 */
	@Override
	public String sendCheckFeeAccountToPayment(PrpdcheckBankVo bankVo,SysUserVo userVo,String registNo,PrpDAccRollBackAccountVo accuntVo,
												String bankId,String checkCode,PrpLCheckmAuditVo auditVo) throws Exception {
		String returnPayeeId = bankId;
		if(StringUtils.isBlank(returnPayeeId)){
			logger.info("====PrpdcheckBankVo.getId()为空，操作失败:===="+bankId);
			return bankId;
		}
		
		// 调收付的接口
		String SVR_URL = SpringProperties.getProperty("PAYMENT_SVR_URL");
		if(SVR_URL==null || StringUtils.isBlank(bankId)){
			logger.error("未配置收付地址，不调用收付接口。");
			throw new Exception("未配置收付服务地址。或未查找到银行账号信息！");
		}
		SVR_URL = SVR_URL+"/service/ClaimReturnTicket";// 收付地址用同一个，调用不同的服务再拼接 改sys_config表的配置数据
		AccMainVo accMainVo = setCheckAccMainVo(bankVo,userVo,accuntVo,bankId);
		// 发送报文
		JPlanReturnVo jPlanReturnVo = sendXmlToCheckPayment(bankVo,userVo,accuntVo.getCertiNo(),registNo,SVR_URL,accMainVo);
		String returnMsg = "操作成功！";
		
		
		if(jPlanReturnVo != null){
			String accountId = jPlanReturnVo.getAccountNo();
			if(!jPlanReturnVo.isResponseCode() || StringUtils.isBlank(accountId)){
				// 回写查勘费退票审核表
				acheckService.updateOrSaveOfPrpLCheckmAudit(auditVo);
				if(accuntVo!=null){
					PrpDAccRollBackAccount backpo=new PrpDAccRollBackAccount();
					  Beans.copy().from(accuntVo).to(backpo);
					  backpo.setStatus("0");
					  backpo.setAuditFlag("1");
					  backpo.setInfoFlag("1");
					  databaseDao.update(PrpDAccRollBackAccount.class,backpo);
				}
				returnMsg = "操作失败！"+jPlanReturnVo.getErrorMessage();
				
				throw new IllegalArgumentException("请求退票接口失败，请到接口平台补传！"+jPlanReturnVo.getErrorMessage()+"accountId="+accountId);
			}else{
				logger.info("====accountId:===="+accountId);
				returnPayeeId=this.saveOrUpdatePrpdcheckBank(bankVo,accuntVo,accountId, userVo,bankId,checkCode,auditVo,"1");
				returnMsg = "操作成功！";
			}
			
			
		}
		return returnMsg;
	}

	/**
	 * 查勘费退票送新收付
	 * @param bankVo
	 * @param userVo
	 * @param registNo
	 * @param accuntVo
	 * @param bankId
	 * @param checkCode
	 * @param auditVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public String sendCheckFeeAccountToNewPayment(PrpdcheckBankVo bankVo,SysUserVo userVo,String registNo,PrpDAccRollBackAccountVo accuntVo,
											   String bankId,String checkCode,PrpLCheckmAuditVo auditVo) throws Exception {
		// 收付接口地址
		String newpaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL");
		if (newpaymentUrl == null || bankId == null) {
			logger.info("未配置收付地址或无法查找到收款人账号信息，调用收付接口失败！");
			throw new Exception("未配置收付地址或无法查找到收款人账号信息，调用收付接口失败！");
		}
		String returnMsg = "操作成功！";
		try {
			String refundToNewPaymentJson = packageCheckFeeRefundData(accuntVo, bankVo);
			logger.info("查勘费退票推送到收付的数据为：" + refundToNewPaymentJson);
			ResponseDto responseDto = sendAssessorOrCheckFeeRefundToNewPayment(accuntVo.getCertiNo(), refundToNewPaymentJson, registNo, userVo);
			logger.info("业务号：{} 查勘费退票送收付完成！", accuntVo.getCertiNo());
			if (PaymentConstants.RESP_FAILED.equals(responseDto.getResponseCode())) {
				// 更新查勘费退票审核数据
				acheckService.updateOrSaveOfPrpLCheckmAudit(auditVo);
				// 更新退票任务数据状态
				PrpDAccRollBackAccount backpo = new PrpDAccRollBackAccount();
				Beans.copy().from(accuntVo).to(backpo);
				backpo.setStatus("0");
				backpo.setAuditFlag("1");
				backpo.setInfoFlag("1");
				databaseDao.update(PrpDAccRollBackAccount.class, backpo);
				returnMsg = "查勘费退票失败！收付失败信息："+responseDto.getErrorMessage();
			} else {
				bankId = this.saveOrUpdatePrpdcheckBank(bankVo, accuntVo, bankId, userVo, bankId, checkCode, auditVo, "1");
				returnMsg = "操作成功！";
			}
		} catch (Exception e) {
			logger.info("业务号：" + accuntVo.getCertiNo() + " 查勘费退票送收付异常！", e);
			throw e;
		}

		return returnMsg;
	}

	/**
	 * 查勘费退票送收付数据封装
	 * @param rollBackAccountVo 退票信息
	 * @param bankVo 修改后的收款人信息
	 * @return 送收付报文
	 * @throws Exception 异常信息
	 */
	private String packageCheckFeeRefundData(PrpDAccRollBackAccountVo rollBackAccountVo, PrpdcheckBankVo bankVo) throws Exception {
		try {
			Gson gson = new Gson();
			List<PrpjPayForCapitalDetailDto> modifyPaymentInfos = new ArrayList<PrpjPayForCapitalDetailDto>();
			PrpjPayForCapitalDetailDto detailDto = new PrpjPayForCapitalDetailDto();
			detailDto.setCertiNo(rollBackAccountVo.getCertiNo());
			detailDto.setSerialNo(0);
			detailDto.setAccountName(bankVo.getAccountName());
			detailDto.setAccountCode(bankVo.getAccountNo());
			detailDto.setBankCode(bankVo.getBankNumber());
			detailDto.setBankName(bankVo.getBankName());
			detailDto.setPayeeCurrency("CNY");
			detailDto.setAccountType(bankVo.getPublicAndPrivate());
			detailDto.setPayeeId(rollBackAccountVo.getAccountId());
			modifyPaymentInfos.add(detailDto);

			return gson.toJson(modifyPaymentInfos);
		} catch (Exception e) {
			logger.info("业务号：" + rollBackAccountVo.getCertiNo() + "查勘费退票送收付数据封装异常！", e);
			throw e;
		}
	}

	/**
	 * <pre></pre>
	 * @param bankVo
	 * @param accuntVo
	 * @param accountId
	 * @param userVo
	 * @param bankId
	 * @param checkCode
	 * @param auditVo
	 * @param string
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆XiaoHuYao(2019年8月5日 下午3:14:50): <br>
	 */
	private String saveOrUpdatePrpdcheckBank(PrpdcheckBankVo bankVo, PrpDAccRollBackAccountVo roAccountVo, String accountId, SysUserVo userVo,
											 String bankId, String checkCode, PrpLCheckmAuditVo auditVo, String index) throws Exception {

		// 旧的银行信息vo
		PrpdcheckBankVo checkbankVo = managerService.findPrpdcheckBankVoById(Long.valueOf(bankId));
		// 旧的银行信息po
		PrpdcheckBank checkBankPo = databaseDao.findByPK(PrpdcheckBank.class, checkbankVo.getId());
		// 是否更新的bankVo数据
		PrpdcheckBankVo newCheckBankVo = managerService.findPrpdcheckBankVosByCheckMainIdAndAccountNo(checkBankPo.getPrpdCheckBankMain().getId(), bankVo.getAccountNo());

		// 旧数据中存在修改后的账号，则更新此条数据的VaildFlag这个字段
		PrpdcheckBank newCheckBank = new PrpdcheckBank();
		if (newCheckBankVo != null && newCheckBankVo.getId() != null) {
			newCheckBank = databaseDao.findByPK(PrpdcheckBank.class, newCheckBankVo.getId());
		}

		PrpdCheckBankMain prpdCheckBankMain = checkBankPo.getPrpdCheckBankMain();
		logger.info("prpdcheckmMain.getId()=" + prpdCheckBankMain.getId() + ",checkbank1Vo.getId()=" + checkbankVo.getId());

		if (newCheckBankVo.getId() == null) {
			// 更新该查勘机构下的银行基础数据,这将该条数据的VaildFlag=1，不是则VaildFlag=0
			bankVo.setVaildFlag(CodeConstants.ValidFlag.VALID);
			bankVo.setCreateTime(new Date());
			bankVo.setCreateUser(userVo.getUserCode());
			bankVo.setAccountId(accountId);
			bankVo.setBankType("test");
			PrpdcheckBank po = new PrpdcheckBank();
			Beans.copy().from(bankVo).to(po);
			po.setPrpdCheckBankMain(prpdCheckBankMain);
			po.setUpdateUser(userVo.getUserCode());
			po.setUpdateTime(new Date());
			databaseDao.save(PrpdcheckBank.class, po);
			if (checkBankPo != null) {
				checkBankPo.setVaildFlag(CodeConstants.ValidFlag.INVALID);
				checkBankPo.setUpdateTime(new Date());
				checkBankPo.setUpdateUser(userVo.getUserCode());
				databaseDao.update(PrpdcheckBank.class, checkBankPo);
			}

			// 回写银行ID到PrpLCheckFee中的BankId字段
			List<PrpLCheckFeeVo> listvo = acheckService.findPrpLCheckFeeVoByBussNo(roAccountVo.getCertiNo());
			if (listvo != null && listvo.size() > 0) {
				for (PrpLCheckFeeVo vo : listvo) {
					PrpLCheckFee feePo = databaseDao.findByPK(PrpLCheckFee.class, vo.getId());
					feePo.setBankId(po.getId());
					feePo.setRemark(bankVo.getRemark());// 摘要
					feePo.setUpdateTime(new Date());
					feePo.setUpdateUser(userVo.getUserCode());
					databaseDao.update(PrpLCheckFee.class, feePo);
				}
			}
		} else {
			checkBankPo.setVaildFlag(CodeConstants.ValidFlag.INVALID);
			// 先更新旧数据的VaildFlag=0,因为每个查勘机构下，只能有一个有效的银行
			databaseDao.update(PrpdcheckBank.class, checkBankPo);
			// 将该查勘机构下，与本次修改的银行账号一致的账号数据，进行更新，并且将VaildFlag=1；
			bankVo.setUpdateUser(userVo.getUserCode());
			bankVo.setUpdateTime(new Date());
			Beans.copy().from(bankVo).excludeNull().to(newCheckBank);
			newCheckBank.setVaildFlag(CodeConstants.ValidFlag.VALID);
			databaseDao.update(PrpdcheckBank.class, newCheckBank);

			// 回写银行ID到PrpLCheckFeeVo中的BankId字段
			List<PrpLCheckFeeVo> listvo = acheckService.findPrpLCheckFeeVoByBussNo(roAccountVo.getCertiNo());
			if (listvo != null && listvo.size() > 0) {
				for (PrpLCheckFeeVo vo : listvo) {
					PrpLCheckFee feePo = databaseDao.findByPK(PrpLCheckFee.class, vo.getId());
					feePo.setBankId(newCheckBank.getId());
					feePo.setRemark(bankVo.getRemark());// 摘要
					feePo.setUpdateTime(new Date());
					feePo.setUpdateUser(userVo.getUserCode());
					databaseDao.update(PrpLCheckFee.class, feePo);
				}
			}
		}
		// 回写PrpDAccRollBackAccount表的accountId,status字段
		if (roAccountVo != null) {
			//PrpDAccRollBackAccount backpo=new PrpDAccRollBackAccount();
			List<PrpDAccRollBackAccount> backAccountPos = new ArrayList<PrpDAccRollBackAccount>();
			List<PrpDAccRollBackAccountVo> backAccountVos = accountQueryService.findPrpDAccRollBackAccountVosByCertiNoAndPayTypeAndAccountId(roAccountVo.getCertiNo(), roAccountVo.getPayType(), roAccountVo.getAccountId());
			if (backAccountVos != null && backAccountVos.size() > 0) {
				for (PrpDAccRollBackAccountVo vo : backAccountVos) {
					vo.setAccountId(accountId);
					vo.setStatus(CodeConstants.CommonConst.FALSE);
					vo.setAuditFlag(CodeConstants.ValidFlag.VALID);
					vo.setInfoFlag(CodeConstants.ValidFlag.VALID);
				}
			}
			backAccountPos = Beans.copyDepth().from(backAccountVos).toList(PrpDAccRollBackAccount.class);
			databaseDao.saveAll(PrpDAccRollBackAccount.class, backAccountPos);

		}
		// 回写查勘费退票审核表
		if (index.equals(CodeConstants.CommonConst.TRUE)) {
			acheckService.updateOrSaveOfPrpLCheckmAudit(auditVo);
		}

		return "成功";
	}

	/**
	 * <pre></pre>
	 * @param bankVo
	 * @param userVo
	 * @param certiNo
	 * @param registNo
	 * @param sVR_URL
	 * @param accMainVo
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月5日 下午3:09:01): <br>
	 */
	private JPlanReturnVo sendXmlToCheckPayment(PrpdcheckBankVo bankVo,SysUserVo userVo,String compensateNo,String registNo,String SVR_URL,
												AccMainVo accMainVo) {
	    XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
	    stream.autodetectAnnotations(true);
	    stream.setMode(XStream.NO_REFERENCES);
	    stream.aliasSystemAttribute(null,"class");
	    String requestXml = stream.toXML(accMainVo);
	    String xString = "";
	    JPlanReturnVo jPlanReturnVo = new JPlanReturnVo();

	    try{
	    	ClaimReturnTicketServiceLocator retuService = new ClaimReturnTicketServiceLocator();
	        ClaimReturnTicketPortBindingStub stub = new ClaimReturnTicketPortBindingStub(new java.net.URL(SVR_URL), retuService);
			logger.info("退票请求收付的报文---->"+requestXml);
	        xString = stub.transPoliceDataForXml(requestXml);
	        stream.processAnnotations(JPlanReturnVo.class);
	    }catch(Exception e){
	    	logger.error("查勘费退票请求收付失败",e);
			jPlanReturnVo.setErrorMessage(e.getMessage());
		}
		logger.info("退票请求收付返回报文：--->"+xString);
	    if(xString!=null && !"".equals(xString)){
	    	jPlanReturnVo = (JPlanReturnVo)stream.fromXML(xString);
	    }

			// 保存日志
	     
	     saveClaimInterfaceLogOfCheckFee(registNo,userVo,compensateNo,jPlanReturnVo,SVR_URL,requestXml,xString);
	      return jPlanReturnVo;
	}

	/**
	 * <pre></pre>
	 * @param registNo
	 * @param userVo
	 * @param compensateNo
	 * @param jPlanReturnVo
	 * @param sVR_URL
	 * @param requestXml
	 * @param xString
	 * @return 
	 * @modified:
	 * ☆XiaoHuYao(2019年8月5日 下午4:57:39): <br>
	 */
	private ClaimInterfaceLogVo saveClaimInterfaceLogOfCheckFee(String registNo,SysUserVo userVo,String compensateNo,JPlanReturnVo jPlanReturnVo,String SVR_URL,
													String requestXml,String xString) {
		Date date = new Date();
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		logVo.setRegistNo(registNo);
		logVo.setBusinessType(BusinessType.CheckFee_BackTicket.name());
		logVo.setBusinessName(BusinessType.CheckFee_BackTicket.getName());
		logVo.setComCode(userVo.getComCode());
		logVo.setCompensateNo(compensateNo);
		logVo.setRequestTime(date);
		logVo.setRequestUrl(SVR_URL);
		if(jPlanReturnVo!=null&&jPlanReturnVo.isResponseCode()){
			logVo.setErrorCode("true");
			logVo.setStatus(CodeConstants.CommonConst.TRUE);
		}else{
			logVo.setErrorCode("false");
			logVo.setStatus(CodeConstants.CommonConst.FALSE);
		}
		logVo.setErrorMessage(jPlanReturnVo==null ? "无返回信息！" : jPlanReturnVo.getErrorMessage());
		logVo.setCreateTime(date);
		logVo.setCreateUser(userVo.getUserCode());
		logVo.setRequestXml(requestXml);
		logVo.setResponseXml(xString);
		ClaimInterfaceLogVo returnLogVo = logService.save(logVo);
		return returnLogVo;
		
	}

	/**
	 * <pre></pre>
	 * @param bankVo
	 * @param userVo
	 * @param accuntVo
	 * @param bankId
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月5日 下午3:07:00): <br>
	 */
	private AccMainVo setCheckAccMainVo(PrpdcheckBankVo bankVo,SysUserVo userVo,PrpDAccRollBackAccountVo roaccuntVo,String bankId) {

		AccMainVo accMainVo = new AccMainVo();
		Date date = new Date();
		List<AccRecAccountVo> accountVosList = new ArrayList<AccRecAccountVo>();
		AccRecAccountVo accountVo = new AccRecAccountVo();
		accMainVo.setCertiNo(roaccuntVo.getCertiNo());
		accMainVo.setOperateComCode(userVo.getComCode());
		accMainVo.setModifyCode(userVo.getUserCode());
		accMainVo.setModifyTime(DateUtils.dateToStr(date, DateUtils.YToDay));
//		accMainVo.setPaytype("c");
		accMainVo.setStatus(CodeConstants.CommonConst.FALSE);
		accountVo.setAccountCode(bankVo.getAccountNo());
		
		accountVo.setAccountNo(roaccuntVo.getAccountId());// 原accountId
		
		accountVo.setBankCode(bankVo.getBankName());
		accountVo.setCurrency("01");// 账户币种
		if(StringUtils.isNotBlank(bankVo.getCity())){
			accountVo.setProvincial(bankVo.getCity().substring(0,2)+"0000");
		}
		
		accountVo.setCity(bankVo.getCity());
		
		accountVo.setNameOfBank(bankVo.getBankOutlets());
		accountVo.setAccountName(bankVo.getAccountName());
		accountVo.setTelephone(bankVo.getMobile());
		accountVo.setClientType(CodeConstants.CommonConst.TRUE);// 客户类型i
		accountVo.setClientNo(CodeConstants.CommonConst.TRUE);
		accountVo.setClientName(bankVo.getAccountName());
		accountVo.setAccountType(CodeConstants.CommonConst.TRUE);
		accountVo.setDefaultFlag(CodeConstants.CommonConst.FALSE);
		if(StringUtils.isNotBlank(bankId)){
			// 旧的银行信息vo
			PrpdcheckBankVo checkbank1Vo=managerService.findPrpdcheckBankVoById(Long.valueOf(bankId));
			if(checkbank1Vo!=null){
				accountVo.setCreateCode(checkbank1Vo.getCreateUser());
			}
		}
		
		accountVo.setCreateBranch(userVo.getComCode());
		accountVo.setValidStatus(CodeConstants.CommonConst.TRUE);
		accountVo.setFlag(bankVo.getBankNumber());// 联行号
		accountVo.setIdentifyType("");
		accountVo.setIdentifyNumber("");
		accountVo.setAccType(CodeConstants.Acctype.company);// 账户类型i，查勘费默认公司账号
		accountVo.setActType("101");
		accountVo.setBankCode(bankVo.getBankName());
		accountVo.setCreateDate(date);
		
		accountVo.setIsFastReparation(CodeConstants.CommonConst.FALSE);
		if(StringUtils.isBlank(bankVo.getRemark())){
			accountVo.setAbstractcontent("查勘费");
		}else{
			accountVo.setAbstractcontent(bankVo.getRemark());
		}
		
		if(!"3".equals(roaccuntVo.getErrorType())){
			accountVo.setIsAutoPay(roaccuntVo.getIsAutoPay());
		}else{
			// 判断是否送资金
			if(CodeConstants.CommonConst.TRUE.equals(roaccuntVo.getOldAccountId())){
				accountVo.setIsAutoPay(CodeConstants.CommonConst.TRUE);
			}else if(CodeConstants.CommonConst.FALSE.equals(roaccuntVo.getOldAccountId())){
				accountVo.setIsAutoPay(CodeConstants.CommonConst.FALSE);
			}
		}
		// 是否对公对私
		if(StringUtils.isNotBlank(bankVo.getPublicAndPrivate())){
			accountVo.setPublicPrivateFlag(bankVo.getPublicAndPrivate());;
		}else{
			accountVo.setPublicPrivateFlag(CodeConstants.CommonConst.FALSE);
		}
		
		accountVosList.add(accountVo);
		
		accMainVo.setAccountVo(accountVosList);
		accMainVo.setPaytype(roaccuntVo.getPayType());
		if(roaccuntVo!=null && StringUtils.isNotBlank(roaccuntVo.getCertiNo())){
			if(roaccuntVo.getCertiNo().startsWith("P") || roaccuntVo.getCertiNo().startsWith("G") || roaccuntVo.getCertiNo().startsWith("J")){
				accMainVo.setCertiType("P");
			}else{
				accMainVo.setCertiType("C");
			}
		}
		// 资金失败 errortype-3，理算可以选择“资金系统支付”和“收付系统支付”按钮，如果选择“资金系统支付”modifytype-6；
		// 选择“收付系统支付”，modifytype-3。
		// （实际上选择“资金系统支付”很少，因为如果错了都会做退票，但是不排除这个情况）

		if(!"3".equals(roaccuntVo.getErrorType())){
			if(CodeConstants.CommonConst.TRUE.equals(roaccuntVo.getIsAutoPay())){
				accMainVo.setModifytype("6");
			}else if(CodeConstants.CommonConst.FALSE.equals(roaccuntVo.getIsAutoPay())){
				accMainVo.setModifytype("3");
			}else{
				accMainVo.setModifytype("6");
			}
		}else{
			if(CodeConstants.CommonConst.TRUE.equals(roaccuntVo.getOldAccountId())){
				accMainVo.setModifytype("6");
			}else if(CodeConstants.CommonConst.FALSE.equals(roaccuntVo.getOldAccountId())){
				accMainVo.setModifytype("3");
			}else{
				accMainVo.setModifytype("6");
			}
		}
		
		return accMainVo;
	
	}

	/* 
	 * @see ins.sino.claimcar.other.service.AccountInfoService#reUploadOfCheckFeeBackTickit(java.lang.String, java.lang.String, ins.platform.vo.SysUserVo)
	 * @param compensateNo
	 * @param registNo
	 * @param userVo
	 * @return
	 */
	@Override
	public String reUploadOfCheckFeeBackTickit(String compensateNo,String registNo,SysUserVo userVo) throws Exception {
		String returnMsg = "补传成功";
		PrpLCheckmAuditVo  auditVo=acheckService.findACheckmAuditByParams(registNo, compensateNo, CodeConstants.CommonConst.TRUE);
		PrpdcheckBankVo bankVo=new PrpdcheckBankVo();
		PrpDAccRollBackAccountVo accuntVo=new PrpDAccRollBackAccountVo();
		if(auditVo!=null && auditVo.getId()!=null){
		accuntVo=accountQueryService.findRollBackAccountById(auditVo.getBackAccountId());
		accuntVo.setOldAccountId(auditVo.getIsAutoPay());
		this.setParamsForPrpdcheckBank(bankVo,auditVo);
		 returnMsg=this.reSendCheckFeeToPayment(bankVo, userVo, registNo, accuntVo, String.valueOf(auditVo.getOldBankId()), auditVo.getCheckCode(), auditVo);
		}else{
			throw new IllegalArgumentException("补传失败，在查勘费退票审核表没有找到对应的数据！");
		}
		
		
		return returnMsg;
	}

	/**
	 * 补送查勘费到收付系统
	 * @param bankVo
	 * @param userVo
	 * @param registNo
	 * @param accuntVo
	 * @param valueOf
	 * @param checkCode
	 * @param auditVo
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆XiaoHuYao(2019年8月15日 下午3:59:06): <br>
	 */
	private String reSendCheckFeeToPayment(PrpdcheckBankVo bankVo,SysUserVo userVo,String registNo,PrpDAccRollBackAccountVo accuntVo,
													String bankId,String checkCode,PrpLCheckmAuditVo auditVo) throws Exception {
		String returnPayeeId=bankId;
		if(StringUtils.isBlank(returnPayeeId)){
			logger.error("操作失败，PrpdCheckBankVo.getId()为空");
			return bankId;
		}
		
		// 调收付的接口
		String SVR_URL = SpringProperties.getProperty("PAYMENT_SVR_URL");
		if(SVR_URL==null){
			logger.error("未配置收付地址，不调用收付接口。");
			throw new Exception("未配置收付服务地址。或未查找到银行账号信息！");
		}
		SVR_URL = SVR_URL+"/service/ClaimReturnTicket";// 收付地址用同一个，调用不同的服务再拼接 改sys_config表的配置数据
		AccMainVo accMainVo = setCheckAccMainVo(bankVo,userVo,accuntVo,bankId);
		// 发送报文
		JPlanReturnVo jPlanReturnVo = sendXmlToCheckPayment(bankVo,userVo,accuntVo.getCertiNo(),registNo,SVR_URL,accMainVo);
		String returnMsg = "补传成功！";
		if(jPlanReturnVo != null){
			// 更新accountID
			String accountId = jPlanReturnVo.getAccountNo();
			if(!jPlanReturnVo.isResponseCode() || StringUtils.isBlank(accountId)){
				returnMsg = "补传失败！"+jPlanReturnVo.getErrorMessage();
			}else{
				logger.info("====accountId:===="+accountId);
				returnPayeeId=this.saveOrUpdatePrpdcheckBank(bankVo,accuntVo,accountId, userVo,bankId,checkCode,auditVo,"0");
				returnMsg = "补传成功！";
			}
			
			
		}
		return returnMsg;
	}

	/**
	 * 组装查勘费银行信息
	 * @param bankVo
	 * @param auditVo
	 * @modified:
	 * ☆XiaoHuYao(2019年8月15日 下午3:57:59): <br>
	 */
	private void setParamsForPrpdcheckBank(PrpdcheckBankVo bankVo,PrpLCheckmAuditVo auditVo) {
		bankVo.setAccountName(auditVo.getAccountName());
		bankVo.setAccountNo(auditVo.getAccountCode());
		bankVo.setCertifyNo(auditVo.getCertifyNo());
		bankVo.setMobile(auditVo.getMobile());
		bankVo.setPublicAndPrivate(auditVo.getPublicAndprivate());
		bankVo.setCity(auditVo.getCity());
		bankVo.setBankName(auditVo.getBankName());
		bankVo.setBankOutlets(auditVo.getBankoutLets());
		bankVo.setBankNumber(auditVo.getBankNumber());
		bankVo.setRemark(auditVo.getRemark());
		
	}


	public String refundToPayment(PrpLPayCustomVo payCustomVo, String compensateNo, PrpLPayBankVo payBankVo, SysUserVo userVo) throws Exception {
		// 新收付地址
		String newpaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL");
		String returnpayeeid = payBankVo.getPayeeId() == null ? "" : payBankVo.getPayeeId().toString();
		if (newpaymentUrl == null || payCustomVo == null) {
			logger.info("未配置收付地址或无法查找到收款人账号信息，调用收付接口失败！");
			throw new Exception("未配置收付地址或无法查找到收款人账号信息，调用收付接口失败！");
		}
		try {
			returnpayeeid = updateRelatedData(payCustomVo, compensateNo, userVo, payBankVo);
			payCustomVo.setId(Long.valueOf(returnpayeeid));
			String refundToNewPaymentJson = packageRefundData(payCustomVo, compensateNo, payBankVo);
			logger.info("退票推送到收付的数据为：" + refundToNewPaymentJson);
			sendRefundToNewPayment(compensateNo, refundToNewPaymentJson, payBankVo, userVo);
			logger.info("业务号：{} 退票送收付完成！", compensateNo);
		} catch (Exception e) {
			logger.info("业务号：" + compensateNo + " 退票送收付异常！", e);
			throw e;
		}

		return returnpayeeid;
	}

	/**
	 * 送收付完成，更新收款人、退票、退票银行信息
	 * @param payCustomVo 修改后的收款人信息
	 * @param compensateNo 计算书号
	 * @param userVo 处理人
	 * @param payBankVo 退票银行信息
	 * @throws Exception 更新异常
	 */
	public String updateRelatedData(PrpLPayCustomVo payCustomVo, String compensateNo, SysUserVo userVo, PrpLPayBankVo payBankVo) throws Exception {
		PrpLPayCustomVo rePayCustom = new PrpLPayCustomVo();
		payCustomVo.setId(null);
		Long oldPayeeId = payBankVo.getPayeeId();
		String returnPayeeId = String.valueOf(payBankVo.getPayeeId());

		// 获取时间
		Date date = new Date();
		// 处理人
		String userCode = userVo.getUserCode();

		payCustomVo.setBankType("test");
		payCustomVo.setValidFlag("1");

		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		// 如果是结算单退票，组装结算单对应的结算书信息
		if (compensateNo.startsWith("P") || compensateNo.startsWith("G") || compensateNo.startsWith("J")) {
			PrpDAccRollBackAccountVo rollBackAccountVo = findRollBackBySerialNo(compensateNo, payBankVo.getSerialNo(), payBankVo.getPayType());
			if (rollBackAccountVo != null) {
				payBankVoList = this.organizePayBankVo(rollBackAccountVo);
				for (PrpLPayBankVo vo : payBankVoList) {
					vo.setSummary(payBankVo.getSummary());
				}
			}
		} else {
			payBankVoList.add(payBankVo);
		}

		/* 收款人名或卡号发生变化则新增一条记录，同时将历史记录置为无效  */
		PrpLPayCustomVo oldCustomVo = managerService.findPayCustomVoById(payBankVo.getPayeeId());
		if (!payCustomVo.getPayeeName().equals(oldCustomVo.getPayeeName()) || !payCustomVo.getAccountNo().equals(oldCustomVo.getAccountNo())) {
			for (PrpLPayBankVo prplpaybankVo : payBankVoList) {
				PrpLPayCustom payCustomPo = new PrpLPayCustom();
				Beans.copy().from(payCustomVo).excludeNull().to(payCustomPo);
				payCustomPo.setCreateTime(date);
				payCustomPo.setCreateUser(userVo.getUserCode());
				payCustomPo.setUpdateTime(date);
				payCustomPo.setUpdateUser(userVo.getUserCode());
				databaseDao.save(PrpLPayCustom.class, payCustomPo);
				// 把旧paycustom的validflag设置为0
				this.updatePayCustom(oldPayeeId);
				// 回写业务表的payeeId及收款人信息
				this.writePayeeId(prplpaybankVo, oldPayeeId, payCustomPo.getId(), userVo);
				payBankVo.setPayeeId(payCustomPo.getId());
				returnPayeeId = String.valueOf(payCustomPo.getId());
			}
		} else {
			for (PrpLPayBankVo prplpaybankVo : payBankVoList) {
				PrpLPayCustom payCustomPo = new PrpLPayCustom();
				payCustomPo = databaseDao.findByPK(PrpLPayCustom.class, prplpaybankVo.getPayeeId());
				Beans.copy().from(payCustomVo).excludeNull().to(payCustomPo);
				payCustomPo.setUpdateUser(userCode);
				payCustomPo.setUpdateTime(date);
				databaseDao.update(PrpLPayCustom.class, payCustomPo);
				// 回写业务表的收款人信息
				this.writePayeeId(prplpaybankVo, oldPayeeId, oldPayeeId, userVo);
			}
		}

		// 更新退票银行信息
		savePayBankInfo(payBankVo, userVo);

		return returnPayeeId;
	}

	private void savePayBankInfo(PrpLPayBankVo payBankVo,SysUserVo userVo) throws Exception{
		PrpLPayBank payBank = new PrpLPayBank();
		Beans.copy().from(payBankVo).to(payBank);
		Date date = new Date();
		PrpDAccRollBackAccountVo vo = findRollBackBySerialNo(payBankVo.getCompensateNo(), payBankVo.getSerialNo(), payBankVo.getPayType());
		if (vo != null && vo.getRollBackTime() != null) {
			payBank.setAppTime(vo.getRollBackTime());
		}

		payBank.setCreateUser(userVo.getUserCode());
		payBank.setCreateTime(date);
		payBank.setUpdateUser(userVo.getUserCode());
		payBank.setUpdateTime(date);
		databaseDao.save(PrpLPayBank.class, payBank);
	}

	/**
	 * 退票送收付数据封装
	 * @param payCustomVo 修改后的收款人信息
	 * @param compensateNo 计算书号
	 * @return 送收付数据字符串
	 * @throws Exception 数据封装异常信息
	 */
	private String packageRefundData(PrpLPayCustomVo payCustomVo, String compensateNo, PrpLPayBankVo payBankVo) throws Exception {
		try {
			Gson gson = new Gson();
			PrpDAccRollBackAccountVo vo = findRollBackBySerialNo(payBankVo.getCompensateNo(), payBankVo.getSerialNo(), payBankVo.getPayType());
			List<PrpjPayForCapitalDetailDto> modifyPaymentInfos = new ArrayList<PrpjPayForCapitalDetailDto>();
			PrpjPayForCapitalDetailDto detailDto = new PrpjPayForCapitalDetailDto();
			detailDto.setCertiNo(compensateNo);
			detailDto.setSerialNo(Integer.parseInt(payBankVo.getSerialNo()));
			detailDto.setAccountName(payBankVo.getAccountName());
			detailDto.setAccountCode(payBankVo.getAccountNo());
			detailDto.setBankCode(payBankVo.getBankNo());
			detailDto.setBankName(payBankVo.getBankName());
			detailDto.setPayeeCurrency("CNY");
			detailDto.setAccountType(payCustomVo.getPublicAndPrivate());
			detailDto.setIsAutoPay(StringUtils.isBlank(payBankVo.getIsAutoPay()) ? "0" : payBankVo.getIsAutoPay());
			String IsFastReparation = getIsFastReparation(compensateNo, payCustomVo);
			detailDto.setIsSimpleCase(IsFastReparation);
			detailDto.setPayeeId(payCustomVo.getId() == null ? "" : payCustomVo.getId().toString());
			modifyPaymentInfos.add(detailDto);
			try {
				logger.info("退票送收付 payBankVo 详细信息为： " + gson.toJson(payBankVo));
			} catch (Exception e) {
				logger.info("退票送收付 payBankVo 详细信息转换异常！", e);
			}

			return gson.toJson(modifyPaymentInfos);
		} catch (Exception e) {
			logger.info("计算书号：" + compensateNo + "退票送收付数据封装异常！", e);
			throw e;
		}
	}
	/**
	 * 获取是否快赔标识
	 * @param compensateNo 业务号
	 * @return 返回是否快赔标识
	 */
	private String getIsFastReparation(String compensateNo, PrpLPayCustomVo payCustomVo) {
		String isFastReparation = "0";
		try {
			if (!compensateNo.startsWith("P") && !compensateNo.startsWith("G") && !compensateNo.startsWith("J")) {
				List<PrpLPaymentVo> prpLPaymentList = new ArrayList<PrpLPaymentVo>();
				if (compensateNo.startsWith("D")) {
					QueryRule qr = QueryRule.getInstance();
					qr.addEqual("compensateNo", compensateNo);
					List<PrpLPadPayMain> list = databaseDao.findAll(PrpLPadPayMain.class, qr);
					if (list != null && list.size() > 0) {
						QueryRule queryRule = QueryRule.getInstance();
						queryRule.addEqual("prpLPadPayMain.id", list.get(0).getId());
						List<PrpLPadPayPerson> personList = databaseDao.findAll(PrpLPadPayPerson.class, queryRule);
						if (personList != null && personList.size() > 0) {
							for (PrpLPadPayPerson prpLPadPayPerson : personList) {
								if (prpLPadPayPerson.getPayeeId().equals(payCustomVo.getId())) {
									PrpLPaymentVo prpLPaymentVo = new PrpLPaymentVo();
									prpLPaymentVo.setSumRealPay(prpLPadPayPerson.getCostSum());
									prpLPaymentVo.setPayeeId(prpLPadPayPerson.getPayeeId());
									prpLPaymentList.add(prpLPaymentVo);
								}
							}
						}
					}
				} else if (compensateNo.startsWith("Y")) {
					List<PrpLPrePayVo> prePayPVos = compensateTaskService.getPrePayVo(compensateNo, "P");
					if (prePayPVos != null && prePayPVos.size() > 0) {
						for (PrpLPrePayVo vo : prePayPVos) {
							if (vo.getPayeeId().equals(payCustomVo.getId())) {
								PrpLPaymentVo prpLPaymentVo = new PrpLPaymentVo();
								prpLPaymentVo.setSumRealPay(vo.getPayAmt());
								prpLPaymentVo.setPayeeId(vo.getPayeeId());
								prpLPaymentList.add(prpLPaymentVo);
							}
						}
					}
				} else {
					prpLPaymentList = compensateTaskService.findPrpLPayment(payCustomVo, compensateNo);
				}
				PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(payCustomVo.getRegistNo());
				// 自动支付
				if (prpLPaymentList != null && prpLPaymentList.size() > 0) {
					List<SendMsgParamVo> msgParamVoList = compensateTaskService.getSendMsgParamVoByRefund(prpLRegistVo, prpLPaymentList, payCustomVo);
					if (msgParamVoList != null && msgParamVoList.size() > 0) {
						if (compensateNo.startsWith("Y")) {
							PrpLCompensateVo compVo = compensateTaskService.findCompByPK(compensateNo);
							// 2017-12-13 如立案号下存在预付冲销任务（不管是否核赔通过），预付送收付的自动支付标志为否。
							boolean isExistWriteOffFlag = compensateTaskService.isExistWriteOff(compVo.getClaimNo(), "Y");
							if (isExistWriteOffFlag) {
								isFastReparation = "0";
							} else {
								isFastReparation = "1";
							}
						} else {
							isFastReparation = "1";
						}
					} else {
						isFastReparation = "0";
					}
				} else {
					isFastReparation = "0";
				}
			}
		} catch (Exception e) {
			logger.info("业务号：" + compensateNo + " 获取快赔标识异常！", e);
		}

		return isFastReparation;
	}

	/**
	 * 公估费退票送收付
	 * @param rollBackAccountVo 退票信息
	 * @param bankVo 修改后的收款人信息
	 * @return 送收付报文
	 * @throws Exception 异常信息
	 */
	private String packageAssessorFeeRefundData(PrpDAccRollBackAccountVo rollBackAccountVo, PrpdIntermBankVo bankVo) throws Exception {
		try {
			Gson gson = new Gson();
			List<PrpjPayForCapitalDetailDto> modifyPaymentInfos = new ArrayList<PrpjPayForCapitalDetailDto>();
			PrpjPayForCapitalDetailDto detailDto = new PrpjPayForCapitalDetailDto();
			detailDto.setCertiNo(rollBackAccountVo.getCertiNo());
			detailDto.setSerialNo(0);
			detailDto.setAccountName(bankVo.getAccountName());
			detailDto.setAccountCode(bankVo.getAccountNo());
			detailDto.setBankCode(bankVo.getBankNumber());
			detailDto.setBankName(bankVo.getBankName());
			detailDto.setPayeeCurrency("CNY");
			detailDto.setAccountType(bankVo.getPublicAndPrivate());
			detailDto.setPayeeId(rollBackAccountVo.getAccountId());
			modifyPaymentInfos.add(detailDto);

			return gson.toJson(modifyPaymentInfos);
		} catch (Exception e) {
			logger.info("业务号：" + rollBackAccountVo.getCertiNo() + "退票送收付数据封装异常！", e);
			throw e;
		}
	}

	/**
	 * 公估费退票送收付数据封装、推送及日志记录
	 * @param certiNo 业务号
	 * @param requestData 推送收付报文
	 * @param registNo 报案号
	 * @param userVo 操作人员信息
	 * @throws Exception 数据封装或推送异常
	 */
	private ResponseDto sendAssessorOrCheckFeeRefundToNewPayment(String certiNo, String requestData, String registNo, SysUserVo userVo) throws Exception {
		// 新收付地址
		String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL");
		if (newPaymentUrl == null || newPaymentUrl.trim().length() == 0) {
			logger.info("业务号：{} 未配置收付地址，公估费或查勘费退票推送收付失败！推送数据为：{}", certiNo, requestData);
			throw new Exception("未配置收付服务地址!");
		}
		newPaymentUrl = newPaymentUrl + "/payInformationModification";
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		// 封装日志表数据
		logVo = packageAssessorInterfaceLog(requestData, newPaymentUrl, userVo, registNo, certiNo);
		// 先保存理赔数据
		logVo.setStatus("1");
		logVo.setErrorCode("true");
		logVo.setErrorMessage("收付处理成功！");
		// 保存之后会生成id，下面根据送收付的实际情况来更新日志数据
		logVo = logService.save(logVo);

		String responseData = "";
		try {
			responseData = sendToNewPayment(requestData, certiNo, newPaymentUrl);
			logger.info(" 业务号：{}" + " 公估费或查勘费退票送收付返回数据为：{}", certiNo, responseData);
		} catch (Exception e) {
			logger.info("业务号：" + certiNo + " 公估费或查勘费退票推送收付失败！", e);
			logVo.setStatus("0");
			logVo.setErrorCode("false");
			logVo.setErrorMessage(e.getMessage());
			logService.updateLog(logVo);
			throw e;
		}
		Gson gson = new Gson();
		ResponseDto respDto = gson.fromJson(responseData, ResponseDto.class);
		if (PaymentConstants.RESP_SUCCESS.equals(respDto.getResponseCode())) {
			logVo.setStatus("1");
			logVo.setErrorMessage(respDto.getErrorMessage());
		} else {
			logVo.setStatus("0");
			logVo.setErrorMessage("公估费或查勘费退票送收付 收付处理失败信息：" + respDto.getErrorMessage());
		}
		logVo.setResponseXml(responseData);
		logService.updateLog(logVo);

		return respDto;
	}

	private void sendRefundToNewPayment(String compensateNo, String requestData, PrpLPayBankVo payBankVo, SysUserVo userVo) throws Exception {
		logger.info("业务号：{}" + "  退票推送收付数据为：{}", compensateNo, requestData);

		String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL");
		if (newPaymentUrl == null || newPaymentUrl.trim().length() == 0) {
			logger.info("业务号：{} 未配置收付地址，推送失败！推送数据为：{}", compensateNo, requestData);
			throw new Exception("未配置收付服务地址!");
		}
		newPaymentUrl = newPaymentUrl + "/payInformationModification";
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		// 封装日志表数据
		logVo = packageInterfaceLog(requestData, newPaymentUrl, payBankVo, userVo);
		// 先保存理赔数据
		if(payBankVo.getPayeeId() != null && payBankVo.getSerialNo() != null && payBankVo.getPayType() != null){
			logVo.setRemark(String.valueOf(payBankVo.getPayeeId())+"_"+payBankVo.getSerialNo()+"_"+payBankVo.getPayType());
		}
		logVo.setStatus("1");
		logVo.setErrorCode("true");
		logVo.setErrorMessage("收付处理成功！");
		// 保存之后会生成id，下面根据送收付的实际情况来更新日志数据
		logVo = logService.save(logVo);
		
		String responseData = "";
		try {
			responseData = sendToNewPayment(requestData, compensateNo, newPaymentUrl);

			logger.info(" 业务号：{}" + "  退票送收付返回数据为：{}", compensateNo, responseData);
		} catch (Exception e) {
			logger.info("业务号：" + compensateNo + " 退票推送收付失败！", e);
			logVo.setStatus("0");
			logVo.setErrorCode("false");
            if(payBankVo.getPayeeId() != null && payBankVo.getSerialNo() != null && payBankVo.getPayType() != null){
                logVo.setRemark(String.valueOf(payBankVo.getPayeeId())+"_"+payBankVo.getSerialNo()+"_"+payBankVo.getPayType());
            }
			logVo.setErrorMessage(e.getMessage());
			logService.updateLog(logVo);
			throw e;
		}
		Gson gson = new Gson();
		ResponseDto respDto = gson.fromJson(responseData, ResponseDto.class);
		if (PaymentConstants.RESP_SUCCESS.equals(respDto.getResponseCode())) {
			logVo.setStatus("1");
			logVo.setErrorMessage(respDto.getErrorMessage());
		} else {
			logVo.setStatus("0");
			logVo.setErrorMessage("退票送收付 收付处理失败信息：" + respDto.getErrorMessage());
		}
		logVo.setResponseXml(responseData);
		logService.updateLog(logVo);
	}

	/**
	 * 推送理赔支付数据到收付系统
	 * @param sendData 发送报文
	 * @param certiNo 业务号
	 * @param newPaymentUrl  新收付地址
	 * @return 收付响应数据
	 * @throws Exception 送收付异常
	 */
	private String sendToNewPayment(String sendData, String certiNo, String newPaymentUrl) throws Exception {
		if (sendData == null || sendData.trim().length() == 0) {
			logger.info("推送收付数据为空，推送失败！推送地址为：{}", newPaymentUrl);
			throw new Exception("推送收付数据为空!");
		}

		BufferedReader bfreader = null;
		HttpURLConnection httpURLConnection = null;
		OutputStream outputStream = null;
		DataOutputStream out = null;
		StringBuilder buffer = new StringBuilder();

		try {
			URL url = new URL(newPaymentUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("POST");
			// post方式不能使用缓存
			httpURLConnection.setUseCaches(false);
			// 配置本次连接的Content-Type，配置为text/xml
			httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			// 维持长连接
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setConnectTimeout(20 * 1000);
			httpURLConnection.setReadTimeout(20 * 1000);
			httpURLConnection.setAllowUserInteraction(true);
			httpURLConnection.connect();
		} catch (Exception ex) {
			logger.info("业务号：{} 连接收付地址失败！", certiNo, ex);
			throw new Exception("连接收付地址失败，请稍候再试！", ex);
		}
		try {
			outputStream = httpURLConnection.getOutputStream();
			out = new DataOutputStream(outputStream);
			out.write(sendData.getBytes("utf-8"));
			out.flush();
			bfreader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
			String strLine = "";
			while ((strLine = bfreader.readLine()) != null) {
				buffer.append(strLine);
			}
		} catch (Exception e) {
			logger.info("业务号：{} 读取收付接口返回数据失败！", certiNo, e);
			throw new Exception("读取收付接口返回数据失败", e);
		} finally {
			try {
				if (bfreader != null) {
					bfreader.close();
				}

				if (out != null) {
					out.close();
				}

				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				logger.info("退票送收付流关闭异常！", e);
				e.printStackTrace();
			}

		}
		return buffer.toString();
	}

	private ClaimInterfaceLogVo packageInterfaceLog(String requestData, String url, PrpLPayBankVo payBankVo, SysUserVo userVo) throws Exception {
		ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo();
		claimInterfaceLogVo.setRegistNo(payBankVo.getRegistNo());
		claimInterfaceLogVo.setCreateTime(new Date());
		claimInterfaceLogVo.setBusinessType(BusinessType.ModAccount.name());
		claimInterfaceLogVo.setBusinessName(BusinessType.ModAccount.getName());
		claimInterfaceLogVo.setRequestUrl(url);
		claimInterfaceLogVo.setRequestTime(new Date());
		claimInterfaceLogVo.setCompensateNo(payBankVo.getCompensateNo());
		claimInterfaceLogVo.setRequestXml(requestData);
		claimInterfaceLogVo.setCreateUser(userVo.getUserCode());
		claimInterfaceLogVo.setComCode(userVo.getComCode());

		return claimInterfaceLogVo;
	}

	/**
	 * 公估费退票送收付日志表数据封装
	 * @param requestData 送收付数据
	 * @param url 新收付地址
	 * @param userVo 操作人信息
	 * @param registNo 报案号
	 * @return 日志表数据对象
	 * @throws Exception 数据封装异常
	 */
	private ClaimInterfaceLogVo packageAssessorInterfaceLog(String requestData, String url, SysUserVo userVo, String registNo, String certiNo) throws Exception {
		ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo();
		claimInterfaceLogVo.setRegistNo(registNo);
		claimInterfaceLogVo.setCreateTime(new Date());
		claimInterfaceLogVo.setBusinessType(BusinessType.AssessorFee_BackTicket.name());
		claimInterfaceLogVo.setBusinessName(BusinessType.AssessorFee_BackTicket.getName());
		claimInterfaceLogVo.setRequestUrl(url);
		claimInterfaceLogVo.setRequestTime(new Date());
		claimInterfaceLogVo.setCompensateNo(certiNo);
		claimInterfaceLogVo.setRequestXml(requestData);
		claimInterfaceLogVo.setCreateUser(userVo.getUserCode());
		claimInterfaceLogVo.setComCode(userVo.getComCode());

		return claimInterfaceLogVo;
	}
	/**
	 * 平安保存或更新退票银行信息，更新退票信息
	 * @param payBankVo 退票银行信息
	 * @throws Exception 异常信息
	 */
	@Override
	public  void saveAccRbackAccountPingAn(PrpLPayBankVo payBankVo) throws Exception {
		PrpLPayBank payBank = new PrpLPayBank();
		Beans.copy().from(payBankVo).to(payBank);
		databaseDao.save(PrpLPayBank.class, payBank);
	}
}
