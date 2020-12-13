package ins.sino.claimcar.bankaccount.service;

import ins.platform.vo.SysUserVo;

import java.util.List;

import com.sinosoft.arch5service.dto.prpcar004.Prpcar004ResDto;
import com.sinosoft.arch5service.dto.prpcar005.AccountInfo005;


/**
 * 银行账号查询新增修改服务
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2015-3-28
 * @since (2015-3-28 下午02:13:55): <br>
 */
public interface BankAccountService {
	public List<AccountInfo005> searchAccount(String accountCode,String accountName,String userCode)throws Exception ;
	
	public Prpcar004ResDto SaveOrUpdateAccount(AccountInfo005 accountInfo005,SysUserVo userVo)throws Exception ;
}
