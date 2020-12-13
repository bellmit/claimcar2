package ins.sino.claimcar.flow.service;

import ins.framework.common.ResultPage;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.vo.PrpCInsuredrelaVo;
import ins.sino.claimcar.claim.vo.PrpLFxqFavoreeVo;
import ins.sino.claimcar.claim.vo.PrpLPayFxqCustomVo;
import ins.sino.claimcar.manager.vo.AccBankNameVo;
import ins.sino.claimcar.manager.vo.PrpDFxqCustomerVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.manager.vo.PrplOldaccbankCodeVo;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;

import java.util.List;

public interface PayCustomService {

	/**
	 * 根据registNo查询PrpLPayCustomVos
	 * @param id
	 * @return
	 */
	public abstract List<PrpLPayCustomVo> findPayCustomVoByRegistNo(
			String registNo);

	/**
	 * 根据id查询PrpLPayCustomVo
	 * @param id
	 * @return
	 */
	public abstract PrpLPayCustomVo findPayCustomVoById(Long id);

	/**
	 * 根据报案号和收款人类型查询收款人
	 * @param registNo
	 * @param payObjectKind
	 * @return
	 * @modified:
	 * ☆XMSH(2016年7月26日 下午3:23:03): <br>
	 */
	public abstract PrpLPayCustomVo findPayCustomVo(String registNo,
			String payObjectKind);

	/**
	 * 保存或更新收款人信息
	 * @param payCustomVo
	 * @throws Exception 
	 */
	public abstract Long saveOrUpdatePayCustom(PrpLPayCustomVo payCustomVo,SysUserVo userVo)
			throws Exception;

	/**
	 * 调用核心增加或更新收款人接口
	 * @throws Exception 
	 * @modified:
	 * ☆XMSH(2016年7月27日 上午11:38:31): <br>
	 */
	public abstract String SaveOrUpdatePaymentAccount(PrpLPayCustomVo PayCustom,SysUserVo userVo)
			throws Exception;

	/**
	 * 当收款人类型为被保险人时，系统查询承保保单中的被保险人信息并返回
	 */
	public abstract PrpLCInsuredVo getCInsuredInfoByRegistNo(String registNo);

	/**
	 * 行号查询
	 * <pre></pre>
	 * @param accBankNameVo
	 * @return
	 * @modified:
	 * ☆WLL(2016年9月22日 上午11:25:03): <br>
	 */
	public ResultPage<AccBankNameVo> findBankNum(AccBankNameVo accBankNameVo,int start,int length);

	/**
	 * 通过联行号查询开户行信息（主要用于获取省市数据）
	 * <pre></pre>
	 * @param bankCode 联行号
	 * @return
	 * @date 2020-8-18 16:27:52
	 */
	public AccBankNameVo findBankByBankCode(String bankCode);

	/**
	 * 通过银行名称查询银行大类代码
	 * @param bankName
	 * @return
	 */
	public String findBankByBankName(String bankName);
	/**
	 * 账户信息修改
	 * @param payCustomVo
	 * @param userVo
	 * @throws Exception
	 */
	public void modPayCustomInfo(PrpLPayCustomVo payCustomVo,SysUserVo userVo,String compensateNo)
			throws Exception;
	
	/**
	 * 条件查询收款人信息
	 * <pre></pre>
	 * @param prpLPayCustomVo
	 * @param start
	 * @param length
	 * @return
	 * @modified:
	 * ☆WLL(2016年11月23日 上午11:58:56): <br>
	 */
	public ResultPage<PrpLPayCustomVo> findPrpLPayCustomByNameAccNo(PrpLPayCustomVo prpLPayCustomVo,int start, int length);
	/**
	 * 通过用户查询公估信息 ☆(2016年1月21日 下午6:00:27): <br>
	 */
	public PrpdIntermMainVo findIntermByUserCode(String userCode);
	
	public PrpLPayCustomVo adjustExistSamePayCusDifName(PrpLPayCustomVo payCustomVo);

	/**
	 * 更新prplpaycustom表status的字段
	 * @param prpLPayCustomVo
	 */
	public void updatePaycustom(PrpLPayCustomVo prpLPayCustomVo);
	/**
	 * 回写prplpaycustom表status的字段
	 * @param payIds
	 */
	public void setStatus(List<Long> payIds);

	
	/**
	 * 查询条件为账户名，账号，收款人类型
	 * @param prpLPayCustomVo
	 * @param start
	 * @param length
	 * @return
	 */
	public ResultPage<PrpLPayCustomVo> findPayCustomByKindNameAccNo(PrpLPayCustomVo prpLPayCustomVo,int start, int length);
	/**
	 * 根据flag-1有效-0无效查询PrplOldaccbankCode的有效数据
	 * @param flag
	 * @return
	 */
	public List<PrplOldaccbankCodeVo> findPrplOldaccbankCodeByFlag(String flag);
	/**
	 * 清除缓存
	 */
	public void clearCache();
    
    /**
     * 
     * 查询报案下的所有收款人
     * @param prpLPayCustomVo
     * @param start
     * @param length
     * @return
     * @modified:
     * ☆zhujunde(2017年6月21日 下午5:55:26): <br>
     */
    public ResultPage<PrpLPayCustomVo> findPayCustomByRegistNo(PrpLPayCustomVo prpLPayCustomVo,int start, int length);

    /**
     * 保存或更新收款人信息
     * @param payCustomVo
     * @throws Exception 
     */
    public abstract Long saveOrUpdatePayCustomById(PrpLPayCustomVo payCustomVo,SysUserVo userVo) throws Exception;
    
    public List<PrpDFxqCustomerVo> findFxqVo(String identifyCode ,String name);
	
	/**
	 * 
	 * 根据报案号和收款人类型查询收款人
	 * @param registNo
	 * @param payObjectKind
	 * @return
	 * @modified:
	 * ☆zhujunde(2017年6月14日 下午2:44:15): <br>
	 */
    public List<PrpLPayCustomVo> findPayCustomVoList(String registNo,
            String payObjectKind);
    
    /**
     * 
     * 根据收款人id查询反洗钱信息
     * @param payId
     * @return
     * @modified:
     * ☆zhujunde(2017年7月5日 下午5:14:58): <br>
     */
    public abstract List<PrpLPayFxqCustomVo> findPrpLPayFxqCustomVoByPayId(Long payId);
    /**
     * 通过立案号查询反洗钱表PrpLPayFxqCustom
     * @param claimNo
     * @return
     */
    public List<PrpLPayFxqCustomVo> findPrpLPayFxqCustomVoByclaimNo(String claimNo);
    
    
    /**
     * 通过立案号查询反洗钱表PrpLFxqFavoree
     * @param claimNo
     * @return
     */
    public List<PrpLFxqFavoreeVo> findPrpLFxqFavoreeVoByclaimNo(String claimNo);
    /**
     * 更新或保存PrpLFxqFavoree
     * @param vo
     */
    public void saveOrupdatePrpLPayFxqCustom(PrpLPayFxqCustomVo vo,SysUserVo userVo)throws Exception;
    
    /**
     * 更新或保存PrpLPayFxqCustom
     * @param vo
     */
    public void saveOrupdatePrpLFxqFavoreeCustom(PrpLFxqFavoreeVo vo,SysUserVo userVo)throws Exception;
    /**
     * 
     * 根据收款人id查询受益人信息
     * @param payId
     * @return
     * @modified:
     * ☆zhujunde(2017年7月5日 下午5:14:58): <br>
     */
    public abstract List<PrpLFxqFavoreeVo> findPrpLFxqFavoreeVoByPayId(Long payId);
    
   /**
    * 保存或者更新 ywuser.prpcinsuredrela根据授权办人
    * <pre></pre>
    * @param payCustomVo
    * @param prpCInsuredrelaVo
    * @param userVo
    * @modified:
    * ☆zhujunde(2017年7月14日 下午3:34:29): <br>
    */
    public void saveOrUpdateInsuredrelaByCustomVo(PrpLPayCustomVo payCustomVo,PrpCInsuredrelaVo prpCInsuredrelaVo,SysUserVo userVo);
    
   /**
    * 保存或者更新 ywuser.prpcinsuredrela根据负责人
    * <pre></pre>
    * @param prpLPayFxqCustoms
    * @param prpCInsuredrelaVos
    * @param userVo
    * @modified:
    * ☆zhujunde(2017年7月14日 下午3:34:17): <br>
    */
    public void saveOrUpdateInsuredrelaByFxqCustomVo(List<PrpCInsuredrelaVo> prpCInsuredrelaVos,SysUserVo userVo);
    
    /**
     * 保存或者更新 ywuser.prpcinsuredrela根据受益人
     * <pre></pre>
     * @param prpLFxqFavoreeVos
     * @param userVo
     * @modified:
     * ☆zhujunde(2017年7月17日 上午10:38:12): <br>
     */
    public void saveOrUpdateInsuredrelaByFxqFavoreeVo(List<PrpCInsuredrelaVo> prpCInsuredrelaVos,SysUserVo userVo);
    
    /**
     * 保存反洗钱数据Insuredrela
     * <pre></pre>
     * @param payCustomVo
     * @param userVo
     * @modified:
     * ☆zhujunde(2017年7月19日 上午10:35:15): <br>
     */
    public void installInsuredrela(PrpLPayCustomVo payCustomVo,SysUserVo userVo);
    //通过Id查询出对应的数据
    public List<PrpLPayCustomVo> findPrpLPayCustomVoByIds(List<Long> ids);
	/**
	 * <pre></pre>
	 * @param payeeName
	 * @param accountNo
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年3月13日 下午8:32:05): <br>
	 */
	public String  findByPayeeNameAndAccountNo(String payeeName,String accountNo,SysUserVo userVo) throws Exception;

	/**
	 *
	 * @param unitedName
	 * @param unitedCode
	 * @return
	 */
	public AccBankNameVo findBankInfoFromName(String unitedName,String unitedCode);
}