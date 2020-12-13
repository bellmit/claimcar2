package ins.sino.claimcar.bankaccount.service.spring;

import ins.platform.utils.XstreamFactory;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.bankaccount.service.BankAccountService;
import ins.sino.claimcar.bankaccount.utils.SysConnection;

import java.util.Calendar;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.sinosoft.arch5service.dto.HeadRemote;
import com.sinosoft.arch5service.dto.prpcar004.AccountInfo004;
import com.sinosoft.arch5service.dto.prpcar004.BodyPrpcar004;
import com.sinosoft.arch5service.dto.prpcar004.Prpcar004Dto;
import com.sinosoft.arch5service.dto.prpcar004.Prpcar004ResDto;
import com.sinosoft.arch5service.dto.prpcar005.AccountInfo005;
import com.sinosoft.arch5service.dto.prpcar005.BasePartPrpcar005;
import com.sinosoft.arch5service.dto.prpcar005.BodyPrpcar005;
import com.sinosoft.arch5service.dto.prpcar005.Prpcar005Dto;
import com.sinosoft.arch5service.dto.prpcar005.Prpcar005ResDto;
import com.sinosoft.arch5service.util.PasswordDefineUtil;




/**
 * 银行账号查询新增修改服务实现
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2015-3-28
 * @since (2015-3-28 下午02:36:53): <br>
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("bankAccountService")
public class BankAccountServiceImpl implements BankAccountService {
	private static final Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);
	
//	@Autowired
//	private ClaimInterfaceLogService logService;
	
	@Override
	public List<AccountInfo005> searchAccount(String accountCode,String accountName,String userCode) throws Exception {
		Prpcar005Dto dto = new Prpcar005Dto();
		HeadRemote head = new HeadRemote();
		head.setUserCode(userCode);
		head.setRequestType("Prpcar005");
		head.setPassword(PasswordDefineUtil.generatePassword(userCode));
		BodyPrpcar005 body = new BodyPrpcar005();
		BasePartPrpcar005 basePart = new BasePartPrpcar005();
		basePart.setAccountCode(accountCode);
		basePart.setAccountName(accountName);
		body.setBasePart(basePart);
		dto.setHead(head);
		dto.setBody(body);
		
		String requestXML = XstreamFactory.objToXml(dto);
		logger.info("银行账号查询请求："+requestXML);
		String returnXml = SysConnection.requestApiweb(requestXML);
		logger.info("提银行账号查询返回："+returnXml);
		Prpcar005ResDto resDto = XstreamFactory.xmlToObj(returnXml,Prpcar005ResDto.class);
		
//		ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); //填写日志表
//		claimInterfaceLogVo.setRequestXml(requestXML);  //发送报文
//		claimInterfaceLogVo.setRequestTime(new Date());
//		claimInterfaceLogVo.setBusinessType("Account_Query");
//		claimInterfaceLogVo.setBusinessName("银行信息查询");
//		
//		claimInterfaceLogVo.setResponseXml(returnXml);    // 返回报文
//		claimInterfaceLogVo.setErrorMessage(resDto.getHead().getErrorMessage());
//	//	claimInterfaceLogVo.setErrorCode(resDto.getHead().ge+"");
//		if(resDto.getHead().isResponseCode()){
//			claimInterfaceLogVo.setStatus("1");
//		}else{
//			claimInterfaceLogVo.setStatus("0");
//		}
//		claimInterfaceLogVo.setCompensateNo(accountCode);
//		logService.save(claimInterfaceLogVo);
		
		if( !resDto.getHead().isResponseCode()){
			throw new Exception(resDto.getHead().getErrorMessage());
		}
		List<AccountInfo005> accountInfos = resDto.getBody().getAccountInfos();
		return accountInfos;
	}
	
	@Override
	public Prpcar004ResDto SaveOrUpdateAccount(AccountInfo005 accountInfo005,SysUserVo userVo)throws Exception{
		//SysUserVo user = ServiceUserUtils .getUserCode();
		Prpcar004Dto dto = new Prpcar004Dto();
		HeadRemote head = new HeadRemote();
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
		String date=sdf.format(Calendar.getInstance().getTime());
		
		head.setUserCode(userVo.getUserCode());
		head.setRequestType("Prpcar004");
		head.setPassword(PasswordDefineUtil.generatePassword(userVo.getUserCode()));
		BodyPrpcar004 body = new BodyPrpcar004();
		AccountInfo004 accountInfo004 = new AccountInfo004();
		accountInfo004.setAccountNo(accountInfo005.getAccountNo());
		accountInfo004.setAccountCode(accountInfo005.getAccountCode());
		accountInfo004.setMnemonicCode(accountInfo005.getMnemonicCode());
		accountInfo004.setBankCode(accountInfo005.getBankCode());
		accountInfo004.setCurrency(accountInfo005.getCurrency());
		accountInfo004.setProvincial(accountInfo005.getProvincial());
		accountInfo004.setCity(accountInfo005.getCity());
		accountInfo004.setNameOfBank(accountInfo005.getNameOfBank());
		accountInfo004.setAccountName(accountInfo005.getAccountName());
		accountInfo004.setTelephone(accountInfo005.getTelephone());
		accountInfo004.setClientType(accountInfo005.getClientType());
		accountInfo004.setClientNo(accountInfo005.getClientNo());
		accountInfo004.setClientName(accountInfo005.getClientName());
		accountInfo004.setAccountType(accountInfo005.getAccountType());
		accountInfo004.setDefaultFlag(accountInfo005.getDefaultFlag());
		accountInfo004.setCreateCode(userVo.getUserCode());
		accountInfo004.setCreateBranch(userVo.getComCode());
		accountInfo004.setValidStatus(accountInfo005.getValidStatus());
		accountInfo004.setFlag(accountInfo005.getFlag());
		accountInfo004.setRemark(accountInfo005.getRemark());
		accountInfo004.setIdentifyType(accountInfo005.getIdentifyType());
		accountInfo004.setIdentifyNumber(accountInfo005.getIdentifyNumber());
		accountInfo004.setPayeeID(accountInfo005.getPayeeID());
		accountInfo004.setActType(accountInfo005.getActType());
		accountInfo004.setToAddress(accountInfo005.getToAddress());
		accountInfo004.setAccType(accountInfo005.getAccType());
		accountInfo004.setCreateDate(date);
		body.setAccountInfo(accountInfo004);
		dto.setHead(head);
		dto.setBody(body);
		
		String requestXML = XstreamFactory.objToXml(dto);
		logger.info("银行账号查询请求："+requestXML);
		String returnXml = SysConnection.requestApiweb(requestXML);
		logger.info("提银行账号查询返回："+returnXml);
		Prpcar004ResDto resDto = XstreamFactory.xmlToObj(returnXml,Prpcar004ResDto.class);
//		ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); //填写日志表
//		claimInterfaceLogVo.setRequestXml(requestXML);  //发送报文
//		claimInterfaceLogVo.setRequestTime(new Date());
//		claimInterfaceLogVo.setBusinessType("Account_Update");
//		claimInterfaceLogVo.setBusinessName("银行信息更新");
//		claimInterfaceLogVo.setResponseXml(returnXml);    // 返回报文
//		claimInterfaceLogVo.setErrorMessage(resDto.getHead().getErrorMessage());
//	//	claimInterfaceLogVo.setErrorCode(resDto.getHead().ge+"");
//		if(resDto.getHead().isResponseCode()){
//			claimInterfaceLogVo.setStatus("1");
//		}else{
//			claimInterfaceLogVo.setStatus("0");
//		}
//		claimInterfaceLogVo.setCompensateNo(accountInfo005.getAccountNo());
//		logService.save(claimInterfaceLogVo);
		
		if( !resDto.getHead().isResponseCode()){
			throw new Exception(resDto.getHead().getErrorMessage());
		}
		return resDto;
	}

}
