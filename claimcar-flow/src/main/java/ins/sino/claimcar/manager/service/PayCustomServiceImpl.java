package ins.sino.claimcar.manager.service;

import com.google.gson.Gson;
import ins.framework.cache.CacheManager;
import ins.framework.cache.CacheService;
import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.DateUtils;
import ins.platform.utils.ObjectUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.account.po.PrplPayaccRenewHis;
import ins.sino.claimcar.bankaccount.service.BankAccountService;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.po.PrpLPadPayPerson;
import ins.sino.claimcar.claim.po.PrpLPayment;
import ins.sino.claimcar.claim.po.PrpLPrePay;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpCInsuredrelaVo;
import ins.sino.claimcar.claim.vo.PrpLFxqFavoreeVo;
import ins.sino.claimcar.claim.vo.PrpLPayFxqCustomVo;
import ins.sino.claimcar.flow.po.PrpLPayFxqCustom;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.manager.po.AccBankName;
import ins.sino.claimcar.manager.po.PrpCInsuredrela;
import ins.sino.claimcar.manager.po.PrpDFxqCustomer;
import ins.sino.claimcar.manager.po.PrpLFxqFavoree;
import ins.sino.claimcar.manager.po.PrpLPayCustom;
import ins.sino.claimcar.manager.po.PrpdIntermMain;
import ins.sino.claimcar.manager.po.PrpdIntermUser;
import ins.sino.claimcar.manager.po.PrplOldaccbankCode;
import ins.sino.claimcar.manager.vo.*;
import ins.sino.claimcar.manager.vo.BankClassDetailDto;
import ins.sino.claimcar.manager.vo.BankClassRequestDto;
import ins.sino.claimcar.manager.vo.BankClassResponseDto;
import ins.sino.claimcar.manager.vo.OpenBankDetailDto;
import ins.sino.claimcar.manager.vo.OpenBankRequestDto;
import ins.sino.claimcar.manager.vo.OpenBankResponseDto;
import ins.sino.claimcar.other.vo.PrplPayaccRenewHisVo;
import ins.sino.claimcar.pinganUnion.enums.DealStatusEnum;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.sinosoft.arch5service.dto.prpcar004.Prpcar004ResDto;
import com.sinosoft.arch5service.dto.prpcar005.AccountInfo005;
import com.sinosoft.sff.interf.AccMainVo;
import com.sinosoft.sff.interf.AccRecAccountVo;
import com.sinosoft.sff.interf.ClaimReturnTicketPortBindingStub;
import com.sinosoft.sff.interf.ClaimReturnTicketServiceLocator;
import com.sinosoft.sff.interf.JPlanReturnVo;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * @author lanlei
 *
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("PayCustomService")
public class PayCustomServiceImpl implements PayCustomService {

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	BaseDaoService baseDaoService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private BankAccountService bankAccountService;
	@Autowired
	ClaimInterfaceLogService logService;
	@Autowired
	IntermediaryService intermediaryService;
    @Autowired
    InterfaceAsyncService interfaceAsyncService;
    @Autowired
    PrpLCMainService prpLCMainService;
	@Autowired
	CodeTranService codeTranService;
	
	private static CacheService childArcaCodeMap = CacheManager.getInstance("AreaCode_Map");
	
	private static final Logger logger = LoggerFactory.getLogger(PayCustomServiceImpl.class); 

	/* 
	 * @see ins.sino.claimcar.manager.service.PayCustomService#findPayCustomVoByRegistNo(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public List<PrpLPayCustomVo> findPayCustomVoByRegistNo(String registNo) {
		String rNo = registNo;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", rNo);
		List<PrpLPayCustom> payCustomPos = databaseDao.findAll(
				PrpLPayCustom.class, queryRule);
		List<PrpLPayCustomVo> payCustomVos = new ArrayList<PrpLPayCustomVo>();
		PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();
		for (PrpLPayCustom payCustom : payCustomPos) {
			// 复制到Vo对象中
			payCustomVo = Beans.copyDepth().from(payCustom)
					.to(PrpLPayCustomVo.class);
			payCustomVos.add(payCustomVo);
		}

		return payCustomVos;
	}
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
	@Override
	public ResultPage<PrpLPayCustomVo> findPrpLPayCustomByNameAccNo(PrpLPayCustomVo prpLPayCustomVo,int start, int length) {
		// 根据报案号取得保单号
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" select min(a.id), a.payObjectKind  ,a.payeeName ,a.certifyNo,a.bankOutlets, a.accountNo , a.registNo");
		sqlUtil.append(" from PrpLPayCustom a where 1=1 ");
		if(StringUtils.isNotBlank(prpLPayCustomVo.getRegistNo())){
			sqlUtil.append(" and  a.registNo= ? ");
			sqlUtil.addParamValue(prpLPayCustomVo.getRegistNo() );
		}else{
			logger.debug("案件无保单信息，收款人查询失败！");
		}
		
		if(StringUtils.isNotBlank(prpLPayCustomVo.getPayeeName())){
			sqlUtil.append(" and  a.payeeName like ? ");
			sqlUtil.addParamValue("%"+prpLPayCustomVo.getPayeeName()+"%" );
		}
		if(StringUtils.isNotBlank(prpLPayCustomVo.getAccountNo())){
			sqlUtil.append(" and  a.accountNo like ? ");
			sqlUtil.addParamValue("%"+prpLPayCustomVo.getAccountNo()+"%" );
		}
		
		sqlUtil.append(" group by a.payobjectkind  ,a.PAYEENAME ,a.BANKOUTLETS, a.ACCOUNTNO ,a.certifyno,a.registNo ");
		sqlUtil.append(" order by min(a.id) ");
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		System.out.println(sql);
//		Page page = databaseDao.findPageBySql(sql,start/length+1,length,values);
		Page<Object[]> page = new Page<Object[]>();
		try{
			page = baseDaoService.pagedSQLQuery(sql,start,length,values);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		List<PrpLPayCustomVo> prpLPayCustomVoList = new ArrayList<PrpLPayCustomVo>();
		if(page!=null&&page.getTotalCount()>0){
			for(Object[] obj : page.getResult()){
				PrpLPayCustomVo payCusVo = new PrpLPayCustomVo();
				payCusVo.setId(Long.valueOf(obj[0].toString()));
				payCusVo.setPayObjectKind(obj[1].toString());
				payCusVo.setPayeeName(obj[2].toString());
				payCusVo.setCertifyNo(obj[3].toString());
				payCusVo.setBankOutlets(obj[4].toString());
				payCusVo.setAccountNo(obj[5].toString());
				payCusVo.setRegistNo(obj[6].toString());
				
				prpLPayCustomVoList.add(payCusVo);
			}
		}

		// 返回结果ResultPage
		ResultPage<PrpLPayCustomVo> pageReturn = new ResultPage<PrpLPayCustomVo>(start,length,page.getTotalCount(),prpLPayCustomVoList);

		return pageReturn;
	}

	/* 
	 * @see ins.sino.claimcar.manager.service.PayCustomService#findPayCustomVoById(java.lang.Long)
	 * @param id
	 * @return
	 */
	@Override
	public PrpLPayCustomVo findPayCustomVoById(Long id) {
		QueryRule queryRule = QueryRule.getInstance();
		PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();
		if(id!=null){
			if(id <= 0){
				Long accID = -id;//accID = 0 表示收款人未知
				//旧理赔数据收款人信息迁移,id<0,payeeid存的是account表ID的负数，需要特殊处理
				if(accID.toString().length() < 6 && accID != 0){
					//旧理赔费用表多赔付到公估，payeeid小于7位，存在ywuser.prplpayobject
					SysCodeDictVo dictVo = codeTranService.findTransCodeDictVo("OldIntermCode",accID.toString());
					if(dictVo != null){
						payCustomVo.setPayeeName(dictVo.getCodeName());
						payCustomVo.setAccountNo("0");
						payCustomVo.setAccountId(accID.toString());
						payCustomVo.setBankName("0");
						payCustomVo.setCertifyNo("0");
						payCustomVo.setFlag("3");//旧理赔费用收款人信息设置特殊标识
					}
				}else if(accID != 0){
					SqlJoinUtils sqlUtil = new SqlJoinUtils();
					sqlUtil.append("select AccountName,AccountCode,AccountNo,BankCode,IdentifyNumber from AccRecAccount a");
					sqlUtil.append(" where a.accountNo = ? ");

					sqlUtil.addParamValue(accID+"");
					
					String sql = sqlUtil.getSql();
					Object[] values = sqlUtil.getParamValues();
					logger.debug("accRecQuerySql="+sql);
					
					List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
					
					for(int i = 0; i<objects.size(); i++ ){
						Object[] obj = objects.get(i);
						payCustomVo.setPayeeName(obj[0]==null ? "" : obj[0].toString());
						payCustomVo.setAccountNo(obj[1]==null ? "" : obj[1].toString());
						payCustomVo.setAccountId(obj[2]==null ? "" : obj[2].toString());
						payCustomVo.setBankName(obj[3]==null ? "" : obj[3].toString());
						payCustomVo.setCertifyNo(obj[4]==null ? "" : obj[4].toString());
						payCustomVo.setFlag("2");//旧理赔收款人信息设置特殊标识
					}
				}
				if(StringUtils.isBlank(payCustomVo.getAccountId())){
					//如果无法查询到正确收款人，默认赋值为未知
					payCustomVo.setPayeeName("未知收款人");
					payCustomVo.setAccountNo("0");
					payCustomVo.setAccountId(accID.toString());
					payCustomVo.setBankName("0");
					payCustomVo.setCertifyNo("0");
					payCustomVo.setFlag("2");//旧理赔收款人信息设置特殊标识
				}
				
			}else{
				queryRule.addEqual("id", id);
				List<PrpLPayCustom> payCustomPos = databaseDao.findAll(PrpLPayCustom.class, queryRule);
				if(payCustomPos!=null && payCustomPos.size()>0){
					PrpLPayCustom payCustomPo = payCustomPos.get(0);
					// 复制到Vo对象中
					payCustomVo = Beans.copyDepth().from(payCustomPo).to(PrpLPayCustomVo.class);
				}
			}
		}
		
		return payCustomVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.manager.service.PayCustomService#findPayCustomVo(java.lang.String, java.lang.String)
	 * @param registNo
	 * @param payObjectKind
	 * @return
	 */
	@Override
	public PrpLPayCustomVo findPayCustomVo(String registNo,String payObjectKind) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("payObjectKind", payObjectKind);
		List<PrpLPayCustom> payCustomPos = databaseDao.findAll(PrpLPayCustom.class, queryRule);
		PrpLPayCustomVo payCustomVo = null;
		if(payCustomPos!=null&&payCustomPos.size()>0){
			payCustomVo = new PrpLPayCustomVo();
			Beans.copy().from(payCustomPos.get(0)).to(payCustomVo);
		}
		return payCustomVo;
	}

	/* 
	 * @see ins.sino.claimcar.manager.service.PayCustomService#saveOrUpdatePayCustom(ins.sino.claimcar.manager.vo.PrpLPayCustomVo)
	 * @param payCustomVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public Long saveOrUpdatePayCustom(PrpLPayCustomVo payCustomVo,SysUserVo userVo) throws Exception {
		PrpLPayCustomVo rePayCustom = new PrpLPayCustomVo();
		// 收款人只在修改了账号和姓名时校验是否和收付交互，其他字段的修改不校验
		if(payCustomVo.getId()!=null){
			PrpLPayCustom payCusOld = databaseDao.findByPK(PrpLPayCustom.class,payCustomVo.getId());
			if(payCusOld!=null
					&&payCusOld.getPayeeName().equals(payCustomVo.getPayeeName())
					&&payCusOld.getAccountNo().equals(payCustomVo.getAccountNo())){
				logger.info(payCustomVo.getId()+"收款人账号及名称未改动，不校验是否与收付交互");
			}else{
				// 控制 收款人如果已经送过收付 或 已经支付 不允许修改
				if(!"Y".equals(payCustomVo.getRemark())){
					// 如果账号信息修改是在反洗钱环节，修改不管控
					boolean callPayFlag = this.adjustExistCallPayment(payCustomVo.getId());
					if(callPayFlag){
						throw new IllegalArgumentException("保存失败！该账号已和收付交互，不允许修改！");
					}
				}else{
					payCustomVo.setRemark(null);
				}
			}
		}
		// 添加控制，收款人信息维护需控制同一个案子的不能维护相同账号的收款人信息
		List<PrpLPayCustomVo> cusVoList = this.findPayCustomByRegistNoAndAccount(payCustomVo.getRegistNo(),payCustomVo.getAccountNo());
		if(cusVoList!=null&&cusVoList.size()>0){
			for(PrpLPayCustomVo cusVo:cusVoList){
				if(!cusVo.getId().equals(payCustomVo.getId())){
					throw new IllegalArgumentException("保存失败！同一案件下相同银行账号"+payCustomVo.getAccountNo()+"只能维护一条记录！");
				}
			}
		}
		
		Date date = new Date();// 获取时间
		String userCode = userVo.getUserCode();// 获取用户

		payCustomVo.setBankType(payCustomVo.getBankName());
		payCustomVo.setValidFlag("1");

		PrpLPayCustom payCustomPo = new PrpLPayCustom();
		// 一个报案号下只保存一条被保险人的收款信息记录，因此在保存的时候进行控制
		if(payCustomVo.getId()!=null){// 更新
            payCustomPo = databaseDao.findByPK(PrpLPayCustom.class,payCustomVo.getId());
            Beans.copy().excludeNull().from(payCustomVo).to(payCustomPo);
            List<PrpLPayFxqCustomVo> prpLPayFxqCustomVos = payCustomVo.getPrpLPayFxqCustomVos();
            List<PrpLFxqFavoreeVo> prpLFxqFavoreeVos = payCustomVo.getPrpLFxqFavoreeVos();
            List<PrpLPayFxqCustom> prpLPayFxqCustoms = new ArrayList<PrpLPayFxqCustom>();
            List<PrpLFxqFavoree> prpLFxqFavorees = new ArrayList<PrpLFxqFavoree>();
           
            if(prpLPayFxqCustomVos != null && prpLPayFxqCustomVos.size() > 0 ){
                for(PrpLPayFxqCustomVo prpLPayFxqCustomVo : prpLPayFxqCustomVos){
                    //prpLPayFxqCustomVo.setPrpLPayCustomVo(payCustomVo);
                    prpLPayFxqCustomVo.setCreateuser(userCode);
                    prpLPayFxqCustomVo.setCreatetime(date);
                    prpLPayFxqCustomVo.setUpdateuser(userCode);
                    prpLPayFxqCustomVo.setUpdatetime(date);
                    PrpLPayFxqCustom prpLPayFxqCustom = Beans.copyDepth().from(prpLPayFxqCustomVo).to(PrpLPayFxqCustom.class);
                    prpLPayFxqCustom.setPrpLPayCustom(payCustomPo);
                    prpLPayFxqCustoms.add(prpLPayFxqCustom);
                    
                }
            }
            if(prpLFxqFavoreeVos != null && prpLFxqFavoreeVos.size() > 0 ){
                for(PrpLFxqFavoreeVo prpLFxqFavoreeVo : prpLFxqFavoreeVos){
                    //prpLPayFxqCustomVo.setPrpLPayCustomVo(payCustomVo);
                    prpLFxqFavoreeVo.setCreateUser(userCode);
                    prpLFxqFavoreeVo.setCreateTime(date);
                    prpLFxqFavoreeVo.setUpdateUser(userCode);
                    prpLFxqFavoreeVo.setUpdateTime(date);
                    PrpLFxqFavoree prpLFxqFavoree = Beans.copyDepth().from(prpLFxqFavoreeVo).to(PrpLFxqFavoree.class);
                    prpLFxqFavoree.setPrpLPayCustom(payCustomPo);
                    prpLFxqFavorees.add(prpLFxqFavoree);
                    
                }
            }
            
            payCustomPo.setPrpLPayFxqCustoms(prpLPayFxqCustoms);
            payCustomPo.setPrpLFxqFavorees(prpLFxqFavorees);
            payCustomPo.setCreateTime(payCustomPo.getCreateTime());
            payCustomPo.setUpdateUser(userVo.getUserCode());
            payCustomPo.setUpdateTime(new Date());
        }else{//新增
            // 将vo拷贝到po
            payCustomPo = Beans.copyDepth().from(payCustomVo).to(PrpLPayCustom.class);
            //Beans.copy().from(payCustomVo).to(payCustomPo);
            payCustomPo.setCreateUser(userVo.getUserCode());
            payCustomPo.setCreateTime(new Date());
            payCustomPo.setUpdateUser(userVo.getUserCode());
            payCustomPo.setUpdateTime(new Date());
        }
        Beans.copy().from(payCustomPo).to(rePayCustom);
        PrplPayaccRenewHisVo payaccRenewHisVo = null;
        try {
        	logger.info("即将保存收款人信息，报案号：" + rePayCustom.getRegistNo() +" 收款人名称：" + rePayCustom.getPayeeName() + " 收款人账号：" + rePayCustom.getAccountNo());
        	// String accountId = SaveOrUpdatePaymentAccount(rePayCustom,userVo);
        	// payCustomPo.setAccountId(accountId);
        	databaseDao.save(PrpLPayCustom.class,payCustomPo);
        	logger.info("保存收款人信息结束，报案号：" + rePayCustom.getRegistNo() +" 收款人名称：" + rePayCustom.getPayeeName() + " 收款人账号：" + rePayCustom.getAccountNo());
		} catch (Exception e) {
			logger.info("保存收款人信息异常！报案号：" + rePayCustom.getRegistNo()+" 收款人名称：" + rePayCustom.getPayeeName() + " 收款人账号：" + rePayCustom.getAccountNo(), e);
			throw e;
		}
		if(payaccRenewHisVo != null){
			// 保存银行修改轨迹
			payaccRenewHisVo.setAccountNo(payCustomPo.getAccountNo());
			payaccRenewHisVo.setRegistNo(payCustomPo.getRegistNo());
			payaccRenewHisVo.setPayeeId(payCustomPo.getId());
			PrplPayaccRenewHis prplpayAccRenewHis = new PrplPayaccRenewHis();
			Beans.copy().from(payaccRenewHisVo).to(prplpayAccRenewHis);
            databaseDao.save(PrplPayaccRenewHis.class,prplpayAccRenewHis);
		}
	    //保存prpCInsuredrela以便提数异步
	    List<PrpLPayFxqCustomVo> payFxqCustomVoList = findPrpLPayFxqCustomVoByPayId(payCustomPo.getId());
	    List<PrpLFxqFavoreeVo> fxqFavoreeVoList = findPrpLFxqFavoreeVoByPayId(payCustomPo.getId());
		PrpLPayCustomVo prpLPayCustomVo = new PrpLPayCustomVo();
		prpLPayCustomVo = Beans.copyDepth().from(payCustomPo).to(PrpLPayCustomVo.class);
		if(payFxqCustomVoList != null && payFxqCustomVoList.size() > 0){
		    prpLPayCustomVo.setPrpLPayFxqCustomVos(payFxqCustomVoList);
		}
		if(fxqFavoreeVoList != null && fxqFavoreeVoList.size() > 0){
            prpLPayCustomVo.setPrpLFxqFavoreeVos(fxqFavoreeVoList);
            interfaceAsyncService.installInsuredrela(prpLPayCustomVo,userVo);
        }
		
		
		return payCustomPo.getId();
	}
	
	private boolean adjustExistCallPayment(Long id){
		if(id!=null){
			QueryRule qr = QueryRule.getInstance();
			qr.addEqual("payeeId",id);
			List<String> payStatusList = new ArrayList<String>();
			payStatusList.add("1");
			payStatusList.add("2");
			payStatusList.add("3");
			qr.addIn("payStatus",payStatusList);
			// 查询理算赔款表
			List<PrpLPayment> PrpLPaymentList = databaseDao.findAll(PrpLPayment.class,qr);
			if(PrpLPaymentList!=null&&PrpLPaymentList.size()>0){
				return true;
			}else{
				// 查询预付赔款表
				List<PrpLPrePay> PrpLPrePayList = databaseDao.findAll(PrpLPrePay.class,qr);
				if(PrpLPrePayList!=null&&PrpLPrePayList.size()>0){
					return true;
				}else{
					// 查询垫付赔款表
					List<PrpLPadPayPerson> PrpLPadPayPersonList = databaseDao.findAll(PrpLPadPayPerson.class,qr);
					if(PrpLPadPayPersonList!=null&&PrpLPadPayPersonList.size()>0){
						return true;
					}else{
						return false;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断是否存在账号相同户名不同的数据 存在返回true
	 * <pre></pre>
	 * @param payCustomVo
	 * @return
	 * @modified:
	 * ☆WLL(2016年12月29日 下午4:57:18): <br>
	 */
	@Override
	public PrpLPayCustomVo adjustExistSamePayCusDifName(PrpLPayCustomVo payCustomVo){
		PrpLPayCustomVo rePayCus = null;
		if(!"0".equals(payCustomVo.getValidFlag())){
			// 已支付和正在退票状态 赔款费用为零和赔款金额为零 的收款人数据不作为比对数据
			List<PrpLPayCustomVo> PayCusVoList = this.getPayCustomByAccount(payCustomVo.getAccountNo());
			if(PayCusVoList != null && PayCusVoList.size()>0){
				for(PrpLPayCustomVo payCus : PayCusVoList){
					if(!payCustomVo.getPayeeName().equals(payCus.getPayeeName().replaceAll("\\s*", ""))
							&&!payCus.getId().equals(payCustomVo.getId())){
						rePayCus = payCus;// 存在账号相同户名不同的数据  且不是当前正在修改的这一条
						break;
					}
				}
			}
		}
		return rePayCus;// 不存在账号相同户名不同的数据
	}
	/**
	 *  根据账号查询收款人信息（排除掉无效和退票中的记录）
	 * <pre></pre>
	 * @param accountNo
	 * @return
	 * @modified:
	 * ☆WLL(2016年12月29日 下午4:48:09): <br>
	 */
	public List<PrpLPayCustomVo> getPayCustomByAccount(String accountNo){
		List<PrpLPayCustomVo> rePayCusVoList = null;
		
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLPayCustom a ");
		sqlUtil.append(" where a.validFlag != ? and a.accountNo = ? ");// 排除掉退票后无效的数据
		sqlUtil.addParamValue("0");
		sqlUtil.addParamValue(accountNo);
		sqlUtil.append(" and not exists (select 1 from PrpLPayment b where (b.payStatus in (?,?) or b.sumRealPay = 0) and b.payeeId = a.id) ");// 排除掉已支付和已退票 赔款金额为零的数据
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("3");
		sqlUtil.append(" and not exists (select 1 from PrpLCharge c where (c.payStatus in (?,?) or c.feeRealAmt = 0) and c.payeeId = a.id) ");// 排除掉已支付和已退票 赔款费用为零的数据
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("3");
		sqlUtil.append(" and not exists (select 1 from PrpLPrePay d where d.payStatus in (?,?) and d.payeeId = a.id) ");
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("3");
		sqlUtil.append(" and not exists (select 1 from PrpLPadPayPerson e where e.payStatus in (?,?) and e.payeeId = a.id) ");
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("3");
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.info(sql);
		List<PrpLPayCustom>  payCus = databaseDao.findAllByHql(PrpLPayCustom.class,sql,values);
		if(payCus != null && payCus.size()>0){
			rePayCusVoList = Beans.copyDepth().from(payCus).toList(PrpLPayCustomVo.class);
		}
		return rePayCusVoList;
	}
	
	/* 
	 * @see ins.sino.claimcar.manager.service.PayCustomService#SaveOrUpdatePaymentAccount(ins.sino.claimcar.manager.vo.PrpLPayCustomVo)
	 * @param PayCustom
	 * @return
	 * @throws Exception
	 */
	@Override
	public String SaveOrUpdatePaymentAccount(PrpLPayCustomVo PayCustom,SysUserVo userVo) throws Exception{
		// 接口查询账户信息  不传输账户名 只传输账号  查询
		List<AccountInfo005> accountInfoList = bankAccountService.searchAccount(PayCustom.getAccountNo(),null,userVo.getUserCode());
		// 不管查询结果中是否有匹配值，先初始化，如果有匹配值则用匹配值替换，如果没有则新增处理
		AccountInfo005 accountInfo005 = new AccountInfo005();
		accountInfo005.setAccountCode(PayCustom.getAccountNo());
		accountInfo005.setCurrency("CNY");
		accountInfo005.setAccountName(PayCustom.getPayeeName());

		if(accountInfoList!=null&&accountInfoList.size()>0){//更新
			accountInfoList.removeAll(Collections.singletonList(null));
			for(AccountInfo005 accountInfo : accountInfoList){
				if(accountInfo.getAccountCode().equals(PayCustom.getAccountNo())){
					accountInfo005 = accountInfo;
					break;
				} 
			}
		}

		accountInfo005.setBankCode(PayCustom.getBankName());
		accountInfo005.setFlag(PayCustom.getBankNo());//联行号
		accountInfo005.setIdentifyType(PayCustom.getCertifyType());
		accountInfo005.setIdentifyNumber(PayCustom.getCertifyNo());
		accountInfo005.setCity(PayCustom.getCityCode().toString());
		accountInfo005.setProvincial(PayCustom.getProvinceCode().toString());
		accountInfo005.setNameOfBank(PayCustom.getBankOutlets());
		accountInfo005.setTelephone(PayCustom.getPayeeMobile()==null?"":PayCustom.getPayeeMobile().toString());
		accountInfo005.setClientType(PayCustom.getPayObjectKind());
		accountInfo005.setClientNo(PayCustom.getId()==null?"":PayCustom.getId().toString());
		accountInfo005.setClientName(PayCustom.getPayeeName());
		accountInfo005.setAccountName(PayCustom.getPayeeName());
		accountInfo005.setAccountType("1");
		accountInfo005.setDefaultFlag("1");
		accountInfo005.setCreateCode(PayCustom.getCreateUser());
		accountInfo005.setCreateBranch(userVo.getComCode());
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
		accountInfo005.setCreateDate(dateFormater.format(PayCustom.getCreateTime()));
		accountInfo005.setValidStatus("1");
		if(StringUtils.isNotBlank(PayCustom.getPayObjectType())){
			accountInfo005.setAccType(PayCustom.getPayObjectType());
		}
		Prpcar004ResDto dto = bankAccountService.SaveOrUpdateAccount(accountInfo005,userVo);
		return dto.getBody().getAccountInfo().getAccountNo();
	}
	/* 
	 * @see ins.sino.claimcar.claim.service.CompensateTaskService#findByPayeeNameAndAccountNo(java.lang.String, java.lang.String)
	 * @param payeeName
	 * @param accountNo
	 * @return
	 */
	private AccountInfo005 getAccountInfo005(String payeeName,String accountNo,SysUserVo userVo) throws Exception{
		AccountInfo005 accountInfo005 = null;
		if(StringUtils.isNotBlank(accountNo)){
			List<AccountInfo005> accountInfoList = bankAccountService.searchAccount(accountNo,null,userVo.getUserCode());
			if(accountInfoList!=null&&accountInfoList.size()>0){//更新
				accountInfoList.removeAll(Collections.singletonList(null));
				for(AccountInfo005 accountInfo : accountInfoList){
					if(accountInfo.getAccountCode().equals(accountNo) && !payeeName.equals(accountInfo.getAccountName())){
						accountInfo005 = accountInfo;
						break;
					}
				}
			}
			
		}
		return accountInfo005;
	}
	@Override
	public String findByPayeeNameAndAccountNo(String payeeName,String accountNo,SysUserVo userVo) throws Exception {
		AccountInfo005 accountInfo005 = getAccountInfo005(payeeName,accountNo,userVo);
		// return (accountInfo005 == null ? null:accountInfo005.getAccountName());
		return null;
	}
	/* 
	 * @see ins.sino.claimcar.manager.service.PayCustomService#getCInsuredInfoByRegistNo(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public PrpLCInsuredVo getCInsuredInfoByRegistNo(String registNo) {
		List<PrpLCMainVo> cMainVos = policyViewService
				.getPolicyAllInfo(registNo);
		PrpLCMainVo cMain = new PrpLCMainVo();
		if (cMainVos.size() > 0) {
			cMain = cMainVos.get(0);
		}
		/*
		 * for (PrpLCMainVo cMainVo : cMainVos) { if
		 * (cMainVo.getRiskCode().equals("1101")) {// 取商业险的被保险人信息 cMain =
		 * cMainVo; } }
		 */
		List<PrpLCInsuredVo> cInsuredVos = cMain.getPrpCInsureds();
		PrpLCInsuredVo cInsured = new PrpLCInsuredVo();
		for (PrpLCInsuredVo cInsuredVo : cInsuredVos) {
			if(cInsuredVo.getInsuredFlag().equals("1")){// 判断该表信息是否是被保险人信息
				cInsured = cInsuredVo;
			}
		}
		return cInsured;

	}
	
	/* 
	 * @see ins.sino.claimcar.manager.service.PayCustomService#findBankNum(ins.sino.claimcar.manager.vo.AccBankNameVo, int, int)
	 * @param accBankNameVo
	 * @param start
	 * @param length
	 * @return
	 */
	@Override
	public ResultPage<AccBankNameVo> findBankNum(AccBankNameVo accBankNameVo,int start,int length){
		/*
		String bankName = accBankNameVo.getBankName();
		boolean isNum = bankName.matches("[0-9]+");
		
		// 定义参数list，ps：执行查询时需要转换成object数组
		List<Object> paramValues = new ArrayList<Object>();
		// hql查询语句
		StringBuffer queryString = new StringBuffer("from AccBankName pf where 1=1 ");
		queryString.append(" AND pf.validStatus = ? ");//只查询有效开户行
		paramValues.add("1");
		if(!isNum){
			if (StringUtils.isNotBlank(accBankNameVo.getProvincial())) {
				queryString.append(" AND pf.provincial = ? ");
				paramValues.add(accBankNameVo.getProvincial());
			}
			if(StringUtils.isNotBlank(accBankNameVo.getCity())){
				queryString.append(" AND pf.city = ? ");
				if(accBankNameVo.getCity().equals("市辖区")){
					paramValues.add(accBankNameVo.getProvincial());
				}else{
					paramValues.add(accBankNameVo.getCity());
				}
			}
			if(!"其他银行".equals(accBankNameVo.getBelongBank())){
				if (StringUtils.isNotBlank(accBankNameVo.getBelongBank())) {
					queryString.append(" AND pf.belongBank like ? ");
					paramValues.add("%" + accBankNameVo.getBelongBank() + "%");
				}
			}
			if (StringUtils.isNotBlank(accBankNameVo.getBankName())) {
				queryString.append(" AND pf.bankName like ? ");
				paramValues.add("%" + accBankNameVo.getBankName() + "%");
			}
		}else{
			// 当查询条件为行号时，只精确查询行号
			if (StringUtils.isNotBlank(accBankNameVo.getBankName())) {
				queryString.append(" AND pf.bankCode = ? ");
				paramValues.add(accBankNameVo.getBankName());
			}
		}
		queryString.append(" Order By pf.id ");
		
		Page resultPage = databaseDao.findPageByHql(queryString.toString(), (start/ length + 1), length, paramValues.toArray());
		
		OpenBankResponseDto responseDto = findBankInfoFromBasicPlatform(accBankNameVo);
		List<AccBankNameVo> accBankNameList = transBasicPlatformDataToAccBankNameData(responseDto);
		for(Object obj:resultPage.getResult()){
			AccBankNameVo accVo = new AccBankNameVo();
			AccBankName accPo = (AccBankName)obj;
			Beans.copy().from(accPo).to(accVo);
			accBankNameList.add(accVo);
		}
		
		ResultPage<AccBankNameVo> rePage = new ResultPage<AccBankNameVo>( start, length, resultPage.getTotalCount(), accBankNameList);
		*/
		OpenBankResponseDto responseDto = findBankInfoFromBasicPlatform(accBankNameVo);
		List<AccBankNameVo> accBankNameList = transBasicPlatformDataToAccBankNameData(responseDto, accBankNameVo);
		ResultPage<AccBankNameVo> rePage = new ResultPage<AccBankNameVo>( start, length, accBankNameList.size(), accBankNameList);
		
		return rePage;
		
	}

	/**
	 * 通过联行号查询开户行信息（主要用于获取省市数据）
	 * @param bankCode 联行号
	 * @return 开户行信息
	 * @date 2020-8-18 16:27:52
	 */
	@Override
	public AccBankNameVo findBankByBankCode(String bankCode) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("bankCode", bankCode);
		AccBankName accBankName = databaseDao.findUnique(AccBankName.class, qr);
		if (accBankName == null) {
			return null;
		}

		return Beans.copyDepth().from(accBankName).to(AccBankNameVo.class);
	}

	/**
	 * 通过银行名称查询银行大类代码
	 * @param bankName
	 * @return
	 */
	@Override
	public String findBankByBankName(String bankName) {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append("select t.bankCode from ywuser.accbankcode t where t.bankname = ?");
		sqlUtil.addParamValue(bankName);
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.info(sql);
		List<String> listBanName = baseDaoService.getAllBySql(String.class,sql,values);
		if (listBanName != null && listBanName.size()>0) {
			return listBanName.get(0);
		}
		return null;
	}

	@Override
	public void modPayCustomInfo(PrpLPayCustomVo payCustomVo,SysUserVo userVo,String compensateNo) throws Exception {
		Date date = new Date();// 获取时间
		PrpLPayCustom prpLPayCustomPo=databaseDao.findByPK(PrpLPayCustom.class, payCustomVo.getId());
		Beans.copy().excludeNull().from(payCustomVo).to(prpLPayCustomPo);
		//调收付的接口
		
		String SVR_URL = SpringProperties.getProperty("PAYMENT_SVR_URL");
		if(SVR_URL==null){
			logger.warn("未配置收付地址，不调用收付接口。");
			throw new Exception("未配置收付服务地址。");
		}
		SVR_URL = SVR_URL +"/service/ClaimReturnTicket";//收付地址用同一个，调用不同的服务再拼接  改sys_config表的配置数据
//		SVR_URL = "http://10.236.0.127:8080/payment/service/ClaimReturnTicket";
		ClaimReturnTicketServiceLocator retuService = new ClaimReturnTicketServiceLocator();
		ClaimReturnTicketPortBindingStub stub = new ClaimReturnTicketPortBindingStub(new java.net.URL(SVR_URL), retuService);
		
		AccRecAccountVo accountVo=new AccRecAccountVo();
		List<AccRecAccountVo> accountVosList=new ArrayList<AccRecAccountVo>();
		AccMainVo accMainVo=new AccMainVo();
		accMainVo.setCertiNo(compensateNo);
		accMainVo.setOperateComCode(userVo.getComCode());
		accMainVo.setModifyCode(userVo.getUserCode());
		accMainVo.setModifyTime(DateUtils.dateToStr(date, DateUtils.YToDay));
//		accMainVo.setPaytype("c");
		accMainVo.setStatus("0");
		
		accountVo.setAccountCode(prpLPayCustomPo.getAccountNo());
		accountVo.setAccountNo(prpLPayCustomPo.getAccountId());
		accountVo.setBankCode(prpLPayCustomPo.getBankNo());
		accountVo.setCurrency("01");//账户币种
		if(prpLPayCustomPo.getProvinceCode()!=null){
			accountVo.setProvincial(String.valueOf(prpLPayCustomPo.getProvinceCode()));
		}
		if(prpLPayCustomPo.getCityCode()!=null){
			accountVo.setCity(String.valueOf(prpLPayCustomPo.getCityCode()));
		}
		accountVo.setNameOfBank(prpLPayCustomPo.getBankOutlets());
		accountVo.setAccountName(prpLPayCustomPo.getPayeeName());
		accountVo.setTelephone(prpLPayCustomPo.getPayeeMobile());
		accountVo.setClientType(prpLPayCustomPo.getPayObjectKind());
		accountVo.setClientNo(prpLPayCustomPo.getId().toString());
		accountVo.setClientName(prpLPayCustomPo.getPayeeName());
		accountVo.setAccountType("1");
		accountVo.setDefaultFlag("0");
		accountVo.setCreateCode(prpLPayCustomPo.getCreateUser());
		accountVo.setCreateBranch(userVo.getComCode());
		accountVo.setValidStatus("1");
//		accountVo.setFlag("1");
		accountVo.setIdentifyType(prpLPayCustomPo.getCertifyType());
		accountVo.setIdentifyNumber(prpLPayCustomPo.getCertifyNo());
		accountVo.setAccType(prpLPayCustomPo.getPayObjectType());
		accountVo.setActType("101");
		accountVo.setBankCode(prpLPayCustomPo.getBankName());
		accountVo.setCreateDate(date);
		accountVosList.add(accountVo);
		accMainVo.setAccountVo(accountVosList);
		accMainVo.setPaytype("P60");
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");
		String xml=null;
		xml=stream.toXML(accMainVo);
		logger.info(xml);
		String xString=stub.transPoliceDataForXml(xml);
		stream.processAnnotations(JPlanReturnVo.class);
		logger.info("返回报文：--->"+xString);
		JPlanReturnVo jPlanReturnVo = (JPlanReturnVo)stream.fromXML(xString);
		String accountId = jPlanReturnVo.getAccountNo();
		// 更新accountID
		System.out.println(accountId+accountId!=null&&accountId!="null"&&accountId.length()>0);
		if(accountId!=null&&!"null".equals(accountId)&&accountId.length()>0){
			prpLPayCustomPo.setAccountId(accountId);
		}
		//保存日志
		 ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		 logVo.setRegistNo(prpLPayCustomPo.getRegistNo());
		 logVo.setBusinessType(BusinessType.ModAccount.name());
		 logVo.setBusinessName(BusinessType.ModAccount.getName());
		 logVo.setComCode(userVo.getComCode());
		 logVo.setCompensateNo(compensateNo);
		 logVo.setRequestTime(date);
		 logVo.setRequestUrl(SVR_URL);
		 if("成功".equals(jPlanReturnVo.getErrorMessage())){
			 logVo.setErrorCode("true");
			 logVo.setStatus("1");
		 }else{
			 logVo.setErrorCode("false");
			 logVo.setStatus("0");
		 }
		 logVo.setErrorMessage(jPlanReturnVo.getErrorMessage());
		 logVo.setCreateTime(date);
		 logVo.setCreateUser(userVo.getUserCode());
		 logVo.setRequestXml(xml);
		 logVo.setResponseXml(xString);
		 logService.save(logVo);
	}
	
	/**
	 * 通过用户查询公估信息 ☆(2016年1月21日 下午6:00:27): <br>
	 */
	public PrpdIntermMainVo findIntermByUserCode(String userCode){
		PrpdIntermMainVo intermVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode",userCode);
		queryRule.addDescOrder("updateTime");
		List<PrpdIntermUser> intermUserList = databaseDao.findAll(PrpdIntermUser.class,queryRule);
		if(intermUserList!=null && !intermUserList.isEmpty()){
			PrpdIntermUser intermuser = intermUserList.get(0);
			PrpdIntermMain intermMainPo = intermuser.getPrpdIntermMain();
			intermVo = Beans.copyDepth().from(intermMainPo).to(PrpdIntermMainVo.class);
		}
		return intermVo;
	}

	
	@Override
	public void updatePaycustom(PrpLPayCustomVo prpLPayCustomVo) {
		PrpLPayCustom prpLPayCustom=new PrpLPayCustom();
		Beans.copy().from(prpLPayCustomVo).to(prpLPayCustom);
		databaseDao.update(PrpLPayCustom.class, prpLPayCustom);
		
	}
	/**
	 * 根据ID查询PrpLPayCustom表
	 */
	@Override
	public void setStatus(List<Long> payIds) {
		QueryRule rule=QueryRule.getInstance();
		rule.addIn("id", payIds);
		List<PrpLPayCustom>	lists=databaseDao.findAll(PrpLPayCustom.class, rule);
		if(lists!=null && lists.size()>0){
			for(PrpLPayCustom pay:lists){
				if("1".equals(pay.getStatus())){
					pay.setStatus("0");
				}
			}
		}
		
	}
	
	
	@Override
	public ResultPage<PrpLPayCustomVo> findPayCustomByKindNameAccNo(PrpLPayCustomVo prpLPayCustomVo,int start, int length){
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLPayCustom payCustom where 1=1 ");
		//查询该保单的所有收款人信息
		List<PrpLCMainVo> policyNoList = policyViewService.findPrpLCMainVoListByRegistNo(prpLPayCustomVo.getRegistNo());
		if(policyNoList!=null&&policyNoList.size()>0){
			sqlUtil.append(" and payCustom.registNo in (select registNo from PrplCMain cMain where  ");
			if(policyNoList.size()==1){
				sqlUtil.append(" cMain.policyNo = ?) ");
				sqlUtil.addParamValue(policyNoList.get(0).getPolicyNo());
			}
			if(policyNoList.size()==2){
				sqlUtil.append(" cMain.policyNo in( ? , ? )) ");
				sqlUtil.addParamValue(policyNoList.get(0).getPolicyNo());
				sqlUtil.addParamValue(policyNoList.get(1).getPolicyNo());
			}
		}else{
			logger.debug("案件无保单信息，收款人查询失败！");
		}
		if(StringUtils.isNotBlank(prpLPayCustomVo.getAccountNo())){
			sqlUtil.append(" and  payCustom.accountNo like ? ");
			sqlUtil.addParamValue("%"+prpLPayCustomVo.getAccountNo()+"%"  );
		}
		if(StringUtils.isNotBlank(prpLPayCustomVo.getPayeeName())){
			sqlUtil.append(" and  payCustom.payeeName like ? ");
			sqlUtil.addParamValue("%"+prpLPayCustomVo.getPayeeName()+"%"  );
		}
		
		sqlUtil.append(" Order By payCustom.createTime desc");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		List<PrpLPayCustomVo> payCustomVoList = new ArrayList<PrpLPayCustomVo>();
		for(int i=0;i<page.getResult().size();i++){
			Object obj = page.getResult().get(i);
			PrpLPayCustom payCustom = (PrpLPayCustom)obj;
			PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();
			Beans.copy().from(payCustom).to(payCustomVo);
			payCustomVoList.add(payCustomVo);
		}
		ResultPage<PrpLPayCustomVo> resultPage = new ResultPage<PrpLPayCustomVo>(start, length, page.getTotalCount(), payCustomVoList);
		return resultPage;
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<PrplOldaccbankCodeVo> findPrplOldaccbankCodeByFlag(String flag) {
		QueryRule queryrule=QueryRule.getInstance();
		queryrule.addEqual("flag",flag);
		List<PrplOldaccbankCodeVo> listVo=new ArrayList<PrplOldaccbankCodeVo>();
		//当缓存不为空时，则从缓存中拿数据，如果是新维护的银行账号，记住要去清一下缓存，方可查出新数据
		String cacheKey = "bankcode_"+flag;
		List<PrplOldaccbankCodeVo> 	listVo1 = (List<PrplOldaccbankCodeVo>)childArcaCodeMap.getCache(cacheKey);
          if(listVo1==null||listVo1.size()<1){
			// 从基础平台数据获取银行大类信息
			// List<PrplOldaccbankCode> listPo = databaseDao.findAll(PrplOldaccbankCode.class, queryrule);
			BankClassResponseDto bankClassResponseDto = findBankKindsFromBasicPlatform();
			List<PrplOldaccbankCode> listPo = transBasicPlatformDataToLocalSystemData(bankClassResponseDto);
			if (listPo.size() > 0) {
					for(PrplOldaccbankCode po :listPo){
						PrplOldaccbankCodeVo vo=new PrplOldaccbankCodeVo();
						Beans.copy().from(po).to(vo);
						listVo.add(vo);
					}
				}
			//当缓存为空时，则将数据存入缓存
		   if( !ObjectUtils.isEmpty(listVo)){
			childArcaCodeMap.putCache(cacheKey,listVo);
		    }
		   return listVo;
         }else{
            return listVo1;
         }
	}
          
	/**
	 * 将基础平台数据对象转换为系统对象
	 * @param responseDto 基础平台数据对象
	 * @date 2020年7月4日 14:48:24
	 * @return 系统对象
	 */
	private List<PrplOldaccbankCode> transBasicPlatformDataToLocalSystemData(BankClassResponseDto responseDto) {
		List<PrplOldaccbankCode> bankList = new ArrayList<PrplOldaccbankCode>();
   
		if (responseDto != null && responseDto.getData() != null) {
			for (BankClassDetailDto detailDto : responseDto.getData()) {
				PrplOldaccbankCode bankVo = new PrplOldaccbankCode();
				bankVo.setBankCode(detailDto.getBankCode());
				bankVo.setBankName(detailDto.getBankName());
				bankList.add(bankVo);
			}
		}

		return bankList;
	}

	/**
	 * 将基础平台开户行数据转换为系统对象
	 * @param responseDto 基础平台数据对象
	 * @date 2020-7-6 11:30:08
	 * @return 系统对象
	 */
	private List<AccBankNameVo> transBasicPlatformDataToAccBankNameData(OpenBankResponseDto responseDto, AccBankNameVo accBankNameVo) {
		List<AccBankNameVo> accBankNameList = new ArrayList<AccBankNameVo>();

		if (responseDto != null && responseDto.getData() != null) {
			for (OpenBankDetailDto detailDto : responseDto.getData()) {
				AccBankNameVo bankVo = new AccBankNameVo();
				bankVo.setBankCode(detailDto.getUnitedCode());
				bankVo.setBankName(detailDto.getUnitedName());
				bankVo.setProvinceCode(detailDto.getProvinceCode());
				bankVo.setProvincial(detailDto.getProvinceName());
				bankVo.setCityCode(detailDto.getCityCode());
				bankVo.setCity(detailDto.getCityName());
				bankVo.setBelongBank(accBankNameVo == null ? "" : accBankNameVo.getBelongBank());

				accBankNameList.add(bankVo);
			}
		}

		return accBankNameList;
	}

	/**
	 * 从基础平台获取开户行银行大类信息
	 * @date 2020年7月4日 14:47:13
	 * @return 基础平台数据转换为对象
	 */
	private BankClassResponseDto findBankKindsFromBasicPlatform() {

		BankClassResponseDto responseDto = new BankClassResponseDto();
		BankClassRequestDto requestDto = new BankClassRequestDto();
		String responseString = "";
		Gson gson = new Gson();
		String platformUrl = SpringProperties.getProperty("basicdataUrl");
		if (platformUrl == null) {
			logger.info("基础平台地址为空，无法获取数据！");
			return responseDto;
		}
		// 查询所有省级信息
		platformUrl = platformUrl + "/bank/kinds";
		try {
			String requestString = gson.toJson(requestDto);
			logger.info("获取基础平台银行大类数据，请求参数为：" + requestString);
			responseString = getBasicPlatformData(requestString, platformUrl);
			if (StringUtils.isBlank(responseString)) {
				logger.info("requestString: " + requestString + "基础平台银行大类数据获取为空！");
				return responseDto;
			}
			logger.info("基础平台返回银行大类数据为：" + responseString);
		} catch (Exception e) {
			logger.info("理赔获取基础平台银行大类数据异常！", e);
			return responseDto;
		}

		try {
			responseDto = gson.fromJson(responseString, BankClassResponseDto.class);
		} catch (Exception e) {
			logger.info("理赔解析基础平台银行大类数据异常！基础平台银行大类数据为：" + responseString, e);
			return responseDto;
		}

		return responseDto;
	}

	/**
	 * 从基础平台获取开户行信息
	 * @date 2020年7月4日 14:47:13
	 * @return 基础平台数据转换为对象
	 */
	private OpenBankResponseDto findBankInfoFromBasicPlatform(AccBankNameVo accBankNameVo) {
		OpenBankResponseDto responseDto = new OpenBankResponseDto();
		OpenBankRequestDto requestDto = new OpenBankRequestDto();

		String platformUrl = SpringProperties.getProperty("basicdataUrl");
		if (platformUrl == null) {
			logger.info("基础平台地址为空，无法获取数据！");
			return responseDto;
		}
		if (accBankNameVo != null && accBankNameVo.getBankCode() != null && accBankNameVo.getProvinceCode() != null) {
			requestDto.setBankCode(accBankNameVo.getBankCode());
			requestDto.setProvinceCode(accBankNameVo.getProvinceCode());
			if (accBankNameVo.getCityCode() != null) {
				requestDto.setCityCode(accBankNameVo.getCityCode());
			}
			if (accBankNameVo.getBankName() != null) {
				if (accBankNameVo.getBankName().matches("[0-9]+")) {
					// 如果全数字，则作为联行号进行查询
					requestDto.setUnitedCode(accBankNameVo.getBankName());
				} else {
					requestDto.setUnitedName(accBankNameVo.getBankName());
				}
			}
		}

		String responseString = "";
		Gson gson = new Gson();

		// 条件查询开户行接口
		platformUrl = platformUrl + "/bank/openBankInfo";
		try {
			String requestString = gson.toJson(requestDto);
			logger.info("获取基础平台开户行数据，请求参数为：" + requestString);
			responseString = getBasicPlatformData(requestString, platformUrl);
			if (StringUtils.isBlank(responseString)) {
				logger.info("requestString: " + requestString + "基础平台开户行数据获取为空！");
				return responseDto;
			}
			logger.info("基础平台返回开户行数据为：" + responseString);
		} catch (Exception e) {
			logger.info("理赔获取基础平台开户行数据异常！", e);
			return responseDto;
		}

		try {
			responseDto = gson.fromJson(responseString, OpenBankResponseDto.class);
		} catch (Exception e) {
			logger.info("理赔解析基础平台银行大类数据异常！基础平台银行大类数据为：" + responseString, e);
			return responseDto;
		}

		return responseDto;
	}

	/**
	 *
	 * @param unitedName
	 * @return
	 */
	private OpenBankResponseDto findBankInfoFromUnitedName(String unitedName){
		OpenBankResponseDto responseDto = new OpenBankResponseDto();
		String platformUrl = SpringProperties.getProperty("basicdataUrl");
		if (platformUrl == null) {
			logger.info("基础平台地址为空，无法获取数据！");
			return responseDto;
		}
		// 条件查询开户行接口
		platformUrl = platformUrl + "/bank/getBankInfoByName";
		String responseString = "";
		String requestString = "{\"unitedName\":\""+unitedName+"\"}";
		Gson gson = new Gson();
		try {
			logger.info("获取基础平台开户行数据，请求参数为：" + requestString);
			responseString = getBasicPlatformData(requestString, platformUrl);
			if (StringUtils.isBlank(responseString)) {
				logger.info("requestString: " + requestString + "基础平台开户行数据获取为空！");
				return responseDto;
			}
			logger.info("基础平台返回开户行数据为：" + responseString);
		} catch (Exception e) {
			logger.info("理赔获取基础平台开户行数据异常！", e);
			return responseDto;
		}

		try {
			responseDto = gson.fromJson(responseString, OpenBankResponseDto.class);
		} catch (Exception e) {
			logger.info("理赔解析基础平台银行大类数据异常！基础平台银行大类数据为：" + responseString, e);
			return responseDto;
		}
		return responseDto;

	}
	/**
	 * 获取基础平台数据
	 * @param requestParams 请求参数
	 * @param platformUrl 平台地址
	 * @return 基础平台数据
	 * @throws Exception 数据获取异常
	 */
	private String getBasicPlatformData(String requestParams, String platformUrl) throws Exception {
		BufferedReader bfreader = null;
		HttpURLConnection httpURLConnection = null;
		OutputStream outputStream = null;
		DataOutputStream out = null;
		StringBuilder buffer = new StringBuilder();

		try {
			URL url = new URL(platformUrl);
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
			throw new Exception("请求基础平台服务失败，请稍候再试！", ex);
		}
		try {
			outputStream = httpURLConnection.getOutputStream();
			out = new DataOutputStream(outputStream);
			out.write(requestParams.getBytes("utf-8"));
			out.flush();
			bfreader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
			String strLine = "";
			while ((strLine = bfreader.readLine()) != null) {
				buffer.append(strLine);
			}
		} catch (Exception e) {
			throw new Exception("读取基础平台返回数据失败", e);
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
				e.printStackTrace();
			}
		}
		return buffer.toString();
	}


	/**
	 * 根据报案号和银行账号查询案件下收款人记录
	 * <pre></pre>
	 * @param registNo
	 * @param accountNo 银行账号
	 * @return
	 * @modified:
	 * ☆WLL(2017年6月13日 下午5:53:50): <br>
	 */
	public List<PrpLPayCustomVo> findPayCustomByRegistNoAndAccount(String registNo,String accountNo){
		List<PrpLPayCustomVo> payCusList = null;
		
		QueryRule queryrule=QueryRule.getInstance();
		queryrule.addEqual("registNo",registNo);
		queryrule.addEqual("accountNo",accountNo);
		
		List<PrpLPayCustom> payCusPoList = databaseDao.findAll(PrpLPayCustom.class,queryrule);
		if(payCusPoList!=null&&payCusPoList.size()>0){
			payCusList = Beans.copyDepth().from(payCusPoList).toList(PrpLPayCustomVo.class);
		}
		
		return payCusList;
	}
	
	@Override
	public void clearCache(){
		childArcaCodeMap.clearAllCacheManager();
	}
	
    @Override
    public List<PrpLPayCustomVo> findPayCustomVoList(String registNo,String payObjectKind) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo", registNo);
        queryRule.addEqual("payObjectKind", payObjectKind);
        List<PrpLPayCustom> payCustomPos = databaseDao.findAll(PrpLPayCustom.class, queryRule);
        List<PrpLPayCustomVo> payCustomVos = new ArrayList<PrpLPayCustomVo>();
        if(payCustomPos != null && payCustomPos.size() > 0){
            for (PrpLPayCustom payCustom : payCustomPos) {
                PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();
                // 复制到Vo对象中
                payCustomVo = Beans.copyDepth().from(payCustom).to(PrpLPayCustomVo.class);
                payCustomVos.add(payCustomVo);
            }
        }
        return payCustomVos;
    }

    /**
     *根据身份证和姓名查询反洗钱公共信息表
     */
    @Override
    public List<PrpDFxqCustomerVo> findFxqVo(String identifyCode ,String name){
        List<PrpDFxqCustomerVo> fxqVo = null;
        QueryRule qr = QueryRule.getInstance();
        qr.addEqual("id.identifyCode",identifyCode);
        qr.addEqual("name",name);
        
        List<PrpDFxqCustomer> fxqPoList = databaseDao.findAll(PrpDFxqCustomer.class,qr);
        if(fxqPoList!=null&&fxqPoList.size()>0){
            fxqVo = Beans.copyDepth().from(fxqPoList).toList(PrpDFxqCustomerVo.class);
        }
        return fxqVo;
    }
    @Override
    public ResultPage<PrpLPayCustomVo> findPayCustomByRegistNo(PrpLPayCustomVo prpLPayCustomVo,int start,int length) {
        SqlJoinUtils sqlUtil=new SqlJoinUtils();
        sqlUtil.append(" from PrpLPayCustom payCustom where 1=1 ");
        //查询该报案下的所有收款人信息
        
        
        if(StringUtils.isNotBlank(prpLPayCustomVo.getRegistNo())){
            sqlUtil.append(" and  payCustom.registNo = ? ");
            sqlUtil.addParamValue(prpLPayCustomVo.getRegistNo());
        }
        if(StringUtils.isNotBlank(prpLPayCustomVo.getAccountNo())){
            sqlUtil.append(" and  payCustom.accountNo like ? ");
            sqlUtil.addParamValue("%"+prpLPayCustomVo.getAccountNo()+"%"  );
        }
        if(StringUtils.isNotBlank(prpLPayCustomVo.getPayeeName())){
            sqlUtil.append(" and  payCustom.payeeName like ? ");
            sqlUtil.addParamValue("%"+prpLPayCustomVo.getPayeeName()+"%"  );
        }
        
        sqlUtil.append(" Order By payCustom.createTime desc");
        String sql = sqlUtil.getSql();
        Object[] values = sqlUtil.getParamValues();
        Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
        List<PrpLPayCustomVo> payCustomVoList = new ArrayList<PrpLPayCustomVo>();
        for(int i=0;i<page.getResult().size();i++){
            Object obj = page.getResult().get(i);
            PrpLPayCustom payCustom = (PrpLPayCustom)obj;
            PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();
            Beans.copy().from(payCustom).to(payCustomVo);
            payCustomVoList.add(payCustomVo);
        }
        ResultPage<PrpLPayCustomVo> resultPage = new ResultPage<PrpLPayCustomVo>(start, length, page.getTotalCount(), payCustomVoList);
        return resultPage;
    
    }
    @Override
    public Long saveOrUpdatePayCustomById(PrpLPayCustomVo payCustomVo,SysUserVo userVo)  throws Exception {

        Date date = new Date();// 获取时间
        String userCode = userVo.getUserCode();// 获取用户

        payCustomVo.setBankType("test");
        payCustomVo.setValidFlag("1");

        PrpLPayCustom payCustomPo = new PrpLPayCustom();
        if(payCustomVo.getId()!=null){//更新
            payCustomPo = databaseDao.findByPK(PrpLPayCustom.class,payCustomVo.getId());
            Beans.copy().from(payCustomVo).excludeNull().to(payCustomPo);
            payCustomPo.setCreateUser(payCustomPo.getCreateUser());
            payCustomPo.setCreateTime(payCustomPo.getCreateTime());
            payCustomPo.setUpdateUser(userCode);
            payCustomPo.setUpdateTime(date);
            databaseDao.update(PrpLPayCustom.class,payCustomPo);
        }else{//新增
            Beans.copy().from(payCustomVo).to(payCustomPo);
            payCustomPo.setCreateUser(userCode);
            payCustomPo.setCreateTime(date);
            payCustomPo.setUpdateUser(userCode);
            payCustomPo.setUpdateTime(date);
            databaseDao.save(PrpLPayCustom.class,payCustomPo);
        }
        
        return payCustomPo.getId();
    }
    @Override
    public List<PrpLPayFxqCustomVo> findPrpLPayFxqCustomVoByPayId(Long payId) {
        List<PrpLPayFxqCustomVo> fxqCustomVo = new ArrayList<PrpLPayFxqCustomVo>();
        QueryRule qr = QueryRule.getInstance();
        qr.addEqual("prpLPayCustom.id",payId);
        
        List<PrpLPayFxqCustom> fxqCustomPoList = databaseDao.findAll(PrpLPayFxqCustom.class,qr);
        if(fxqCustomPoList != null && fxqCustomPoList.size() > 0){
            fxqCustomVo = Beans.copyDepth().from(fxqCustomPoList).toList(PrpLPayFxqCustomVo.class);
        }
        return fxqCustomVo;
    }
    @Override
    public List<PrpLFxqFavoreeVo> findPrpLFxqFavoreeVoByPayId(Long payId) {
        List<PrpLFxqFavoreeVo> fxqFavoreeVo = new ArrayList<PrpLFxqFavoreeVo>();
        QueryRule qr = QueryRule.getInstance();
        qr.addEqual("prpLPayCustom.id",payId);
        List<PrpLFxqFavoree> fxqFavoreePoList = databaseDao.findAll(PrpLFxqFavoree.class,qr);
        if(fxqFavoreePoList != null && fxqFavoreePoList.size() > 0){
            fxqFavoreeVo = Beans.copyDepth().from(fxqFavoreePoList).toList(PrpLFxqFavoreeVo.class);
        }
        return fxqFavoreeVo;
    }

    @Override
    public void saveOrUpdateInsuredrelaByCustomVo(PrpLPayCustomVo payCustomVo,PrpCInsuredrelaVo prpCInsuredrelaVo,SysUserVo userVo) {//ADDRESS用这个来存id
        Date date = new Date();// 获取时间
        String userCode = userVo.getUserCode();
        if(payCustomVo.getId()!=null){
            QueryRule qr = QueryRule.getInstance();
            qr.addEqual("address",payCustomVo.getId().toString());
            List<PrpCInsuredrela> insuredrelaPoList = databaseDao.findAll(PrpCInsuredrela.class,qr);
            if(insuredrelaPoList != null && insuredrelaPoList.size() > 0){//执行update
                PrpCInsuredrela prpCInsuredrela = insuredrelaPoList.get(0);
                Beans.copy().from(prpCInsuredrelaVo).excludeNull().to(prpCInsuredrela);
                prpCInsuredrela.setOperatorCode(userCode);
                prpCInsuredrela.setOperateTime(date);
                databaseDao.update(PrpCInsuredrela.class,prpCInsuredrela);
            }else{//执行save
                PrpCInsuredrela prpCInsuredrela = new PrpCInsuredrela();
                Beans.copy().from(prpCInsuredrelaVo).to(prpCInsuredrela);
                prpCInsuredrela.setOperatorCode(userCode);
                prpCInsuredrela.setOperateTime(date);
                prpCInsuredrela.setAddress(payCustomVo.getId().toString());
                databaseDao.save(PrpCInsuredrela.class,prpCInsuredrela);
            }
        }else{//执行save
            PrpCInsuredrela prpCInsuredrela = new PrpCInsuredrela();
            Beans.copy().from(prpCInsuredrelaVo).to(prpCInsuredrela);
            prpCInsuredrela.setOperatorCode(userCode);
            prpCInsuredrela.setOperateTime(date);
            databaseDao.save(PrpCInsuredrela.class,prpCInsuredrela);
        }
    }
    @Override
    public void saveOrUpdateInsuredrelaByFxqCustomVo(List<PrpCInsuredrelaVo> prpCInsuredrelaVos,SysUserVo userVo) {
        saveOrUpdateInsuredrela(prpCInsuredrelaVos,userVo);
    }
    @Override
    public void saveOrUpdateInsuredrelaByFxqFavoreeVo(List<PrpCInsuredrelaVo> prpCInsuredrelaVos,SysUserVo userVo) {
        saveOrUpdateInsuredrela(prpCInsuredrelaVos,userVo);
    }
    
    /**
     * 保存或更新PrpCInsuredrela
     * <pre></pre>
     * @param prpCInsuredrelaVos
     * @param userVo
     * @modified:
     * ☆zhujunde(2017年7月18日 下午4:42:50): <br>
     */
    public void saveOrUpdateInsuredrela(List<PrpCInsuredrelaVo> prpCInsuredrelaVos,SysUserVo userVo){
        for(PrpCInsuredrelaVo vo : prpCInsuredrelaVos){
            Date date = new Date();// 获取时间
            String userCode = userVo.getUserCode();
            if(StringUtils.isNotBlank(vo.getAddress())){
                QueryRule qr = QueryRule.getInstance();
                qr.addEqual("address",vo.getAddress());
                List<PrpCInsuredrela> insuredrelaPoList = databaseDao.findAll(PrpCInsuredrela.class,qr);
                if(insuredrelaPoList != null && insuredrelaPoList.size() > 0){//执行update
                    PrpCInsuredrela prpCInsuredrela = insuredrelaPoList.get(0);
                    Beans.copy().from(vo).excludeNull().to(prpCInsuredrela);
                    prpCInsuredrela.setOperatorCode(userCode);
                    prpCInsuredrela.setOperateTime(date);
                    databaseDao.update(PrpCInsuredrela.class,prpCInsuredrela);
                }else{//执行save
                    PrpCInsuredrela prpCInsuredrela = new PrpCInsuredrela();
                    Beans.copy().from(vo).to(prpCInsuredrela);
                    prpCInsuredrela.setOperatorCode(userCode);
                    prpCInsuredrela.setOperateTime(date);
                    prpCInsuredrela.setAddress(vo.getAddress());
                    databaseDao.save(PrpCInsuredrela.class,prpCInsuredrela);
                }
            }else{//执行save
                PrpCInsuredrela prpCInsuredrela = new PrpCInsuredrela();
                Beans.copy().from(vo).to(prpCInsuredrela);
                prpCInsuredrela.setOperatorCode(userCode);
                prpCInsuredrela.setOperateTime(date);
                prpCInsuredrela.setAddress(vo.getAddress());
                databaseDao.save(PrpCInsuredrela.class,prpCInsuredrela);
            }
        }
    }
    
    public void installInsuredrela(PrpLPayCustomVo payCustomVo,SysUserVo userVo){
        
        List<PrpLFxqFavoreeVo> prpLFxqFavoreeVos = new ArrayList<PrpLFxqFavoreeVo>();
        if(payCustomVo.getPrpLFxqFavoreeVos() != null && payCustomVo.getPrpLFxqFavoreeVos().size() > 0){
            prpLFxqFavoreeVos = payCustomVo.getPrpLFxqFavoreeVos();
        }
        String insuredType = new String();
        List<PrpLCMainVo> prpLCMainList = prpLCMainService.findPrpLCMainsByRegistNo(payCustomVo.getRegistNo());
        PrpLCMainVo cMain = prpLCMainList.get(0);
        List<PrpLCInsuredVo> PrpLCInsuredList = cMain.getPrpCInsureds();
        if(prpLCMainList != null && prpLCMainList.size() > 0 ){
            for(PrpLCInsuredVo cInsured:PrpLCInsuredList){
                if("1".equals(cInsured.getInsuredFlag())){
                    insuredType = cInsured.getInsuredType();
                }
            }
        }
        if("2".equals(insuredType)){//机构  
            PrpCInsuredrelaVo prpCInsuredrelaVo = new PrpCInsuredrelaVo();
            prpCInsuredrelaVo.setCertiNo(prpLFxqFavoreeVos.get(0).getCompensateNo());
            prpCInsuredrelaVo.setCertiType("C");
            //授权办人
            prpCInsuredrelaVo.setCustomerKind("2");
            prpCInsuredrelaVo.setCustomerType("4");
            prpCInsuredrelaVo.setName(payCustomVo.getAuthorityName());
            prpCInsuredrelaVo.setNatioNality(payCustomVo.getNationality());
            prpCInsuredrelaVo.setIdentifyType(payCustomVo.getAuthorityCertifyType());
            prpCInsuredrelaVo.setIdentifyNumber(payCustomVo.getAuthorityNo());
            prpCInsuredrelaVo.setValidDate(payCustomVo.getAuthorityStartDate());
            prpCInsuredrelaVo.setPhone(payCustomVo.getAuthorityPhone());
            saveOrUpdateInsuredrelaByCustomVo(payCustomVo,prpCInsuredrelaVo,userVo);
            //负责人
            if(payCustomVo.getPrpLPayFxqCustomVos() != null && payCustomVo.getPrpLPayFxqCustomVos().size() > 0){
                List<PrpLPayFxqCustomVo> prpLPayFxqCustomVos = payCustomVo.getPrpLPayFxqCustomVos();
                List<PrpCInsuredrelaVo> prpCInsuredrelaVos = new ArrayList<PrpCInsuredrelaVo>();
                for(PrpLPayFxqCustomVo payFxqCustomVo : prpLPayFxqCustomVos){
                    PrpCInsuredrelaVo insuredrelaVo = new PrpCInsuredrelaVo();
                    if(payFxqCustomVo.getId()!=null){
                        insuredrelaVo.setAddress(payFxqCustomVo.getId().toString());
                    }
                    insuredrelaVo.setCertiType("C");
                    insuredrelaVo.setCertiNo(prpLFxqFavoreeVos.get(0).getCompensateNo());
                    insuredrelaVo.setCustomerKind("1");
                    insuredrelaVo.setCustomerType("4");
                          
                    insuredrelaVo.setName(payFxqCustomVo.getLegalPerson());         
                    insuredrelaVo.setIdentifyType(payFxqCustomVo.getLegalCertifyType());
                    insuredrelaVo.setIdentifyNumber(payFxqCustomVo.getLegalIdentifyCode());
                    insuredrelaVo.setValidDate(payFxqCustomVo.getLegalCertifyEndDate());
                    insuredrelaVo.setPhone(payFxqCustomVo.getLegalPhone());
                    /*insuredrelaVo.setOwnType(payFxqCustomVo.getLegalType());//传负责人类型      
                    insuredrelaVo.setGender(payFxqCustomVo.getPersonSex());
                    insuredrelaVo.setRevenueRegistNo("");
                    insuredrelaVo.setBusinessCode("");
                    insuredrelaVo.setBusinessArea("");
                    insuredrelaVo.setOccupAtionCode(payFxqCustomVo.getProfession());//职业类型待定         
                    insuredrelaVo.setAddrType("");
                    insuredrelaVo.setOperatorCode(payFxqCustomVo.getCreateuser());
                    insuredrelaVo.setOperateTime(payFxqCustomVo.getCreatetime());*/
                    prpCInsuredrelaVos.add(insuredrelaVo);
                }
                saveOrUpdateInsuredrelaByFxqCustomVo(prpCInsuredrelaVos,userVo);
            }
            //受益人 
            List<PrpCInsuredrelaVo> prpCInsuredrelaVos = new ArrayList<PrpCInsuredrelaVo>();
            for(PrpLFxqFavoreeVo fxqFavoreeVo : prpLFxqFavoreeVos){
                PrpCInsuredrelaVo insuredrelaVo = new PrpCInsuredrelaVo();
                if(fxqFavoreeVo.getId()!=null){
                    insuredrelaVo.setAddress(fxqFavoreeVo.getId().toString());
                }
                insuredrelaVo.setCertiType("C");
                insuredrelaVo.setCertiNo(fxqFavoreeVo.getCompensateNo());
                insuredrelaVo.setCustomerKind("6");
                insuredrelaVo.setCustomerType("4");
                //insuredrelaVo.setOwnType(fxqFavoreeVo.getFavoreelInsureRelation());
                insuredrelaVo.setName(fxqFavoreeVo.getFavoreeName());
                insuredrelaVo.setIdentifyType(fxqFavoreeVo.getFavoreeCertifyType());
                insuredrelaVo.setIdentifyNumber(fxqFavoreeVo.getFavoreeIdentifyCode());
                insuredrelaVo.setValidDate(fxqFavoreeVo.getFavoreeCertifyStartDate());
                insuredrelaVo.setRevenueRegistNo(fxqFavoreeVo.getFavoreelRevenueRegistNo());
                insuredrelaVo.setBusinessCode(fxqFavoreeVo.getFavoreelBusinessCode());
                insuredrelaVo.setBusinessArea(fxqFavoreeVo.getFavoreelBusinessArea());
                insuredrelaVo.setAddrType(fxqFavoreeVo.getFavoreenAdressType());
                prpCInsuredrelaVos.add(insuredrelaVo);
            }
            saveOrUpdateInsuredrelaByFxqFavoreeVo(prpCInsuredrelaVos,userVo);
        }else{
            List<PrpCInsuredrelaVo> prpCInsuredrelaVos = new ArrayList<PrpCInsuredrelaVo>();
            for(PrpLFxqFavoreeVo fxqFavoreeVo : prpLFxqFavoreeVos){
                PrpCInsuredrelaVo insuredrelaVo = new PrpCInsuredrelaVo();
                if(fxqFavoreeVo.getId()!=null){
                    insuredrelaVo.setAddress(fxqFavoreeVo.getId().toString());
                }
                insuredrelaVo.setCertiType("C");
                insuredrelaVo.setCertiNo(fxqFavoreeVo.getCompensateNo());
                insuredrelaVo.setCustomerKind("6");
                insuredrelaVo.setCustomerType("4");
                //insuredrelaVo.setOwnType(fxqFavoreeVo.getFavoreelInsureRelation());
                insuredrelaVo.setName(fxqFavoreeVo.getFavoreeName());
                insuredrelaVo.setGender(fxqFavoreeVo.getFavoreenSex());
                insuredrelaVo.setNatioNality(fxqFavoreeVo.getFavoreenNatioNality());
                insuredrelaVo.setIdentifyType(fxqFavoreeVo.getFavoreeCertifyType());
                insuredrelaVo.setIdentifyNumber(fxqFavoreeVo.getFavoreeIdentifyCode());
                insuredrelaVo.setValidDate(fxqFavoreeVo.getFavoreeCertifyStartDate());
                insuredrelaVo.setPhone(fxqFavoreeVo.getFavoreenPhone());
                insuredrelaVo.setAddrType(fxqFavoreeVo.getFavoreenAdressType());
                prpCInsuredrelaVos.add(insuredrelaVo);
            }
            saveOrUpdateInsuredrelaByFxqFavoreeVo(prpCInsuredrelaVos,userVo);
        }
    }
	@Override
	public List<PrpLPayFxqCustomVo> findPrpLPayFxqCustomVoByclaimNo(String claimNo) {
		List<PrpLPayFxqCustomVo> fxqCustomVo = new ArrayList<PrpLPayFxqCustomVo>();
        QueryRule qr = QueryRule.getInstance();
        qr.addEqual("claimNo",claimNo);
        
        List<PrpLPayFxqCustom> fxqCustomPoList = databaseDao.findAll(PrpLPayFxqCustom.class,qr);
        if(fxqCustomPoList != null && fxqCustomPoList.size() > 0){
            fxqCustomVo = Beans.copyDepth().from(fxqCustomPoList).toList(PrpLPayFxqCustomVo.class);
        }
        return fxqCustomVo;
		
	}
	@Override
	public List<PrpLFxqFavoreeVo> findPrpLFxqFavoreeVoByclaimNo(String claimNo) {
		List<PrpLFxqFavoreeVo> fxqCustomVo = new ArrayList<PrpLFxqFavoreeVo>();
        QueryRule qr = QueryRule.getInstance();
        qr.addEqual("claimNo",claimNo);
        
        List<PrpLFxqFavoree> fxqCustomPoList = databaseDao.findAll(PrpLFxqFavoree.class,qr);
        if(fxqCustomPoList != null && fxqCustomPoList.size() > 0){
            fxqCustomVo = Beans.copyDepth().from(fxqCustomPoList).toList(PrpLFxqFavoreeVo.class);
        }
        return fxqCustomVo;
	}
	@Override
	public void saveOrupdatePrpLPayFxqCustom(PrpLPayFxqCustomVo vo,SysUserVo userVo)throws Exception {
		PrpLPayFxqCustom po=new PrpLPayFxqCustom();
		if(vo!=null && vo.getId()!=null){
			vo.setUpdatetime(new Date());
			if(userVo!=null){
				vo.setUpdateuser(userVo.getUserCode());	
			}
			Beans.copy().from(vo).to(po);
			databaseDao.update(PrpLPayFxqCustom.class,po);
		}else if(vo!=null && vo.getId()==null){
			
			if(userVo!=null){
				vo.setCreateuser(userVo.getUserCode());
			}
			vo.setCreatetime(new Date());
			   Beans.copy().from(vo).to(po);
			databaseDao.save(PrpLPayFxqCustom.class,po);
		}
		
	}
	@Override
	public void saveOrupdatePrpLFxqFavoreeCustom(PrpLFxqFavoreeVo vo,SysUserVo userVo)throws Exception {
		PrpLFxqFavoree po=new PrpLFxqFavoree();
		if(vo!=null && vo.getId()!=null){
			if(userVo!=null){
				vo.setUpdateUser(userVo.getUserCode());
			}
			vo.setUpdateTime(new Date());;
			Beans.copy().from(vo).to(po);
			databaseDao.update(PrpLFxqFavoree.class,po);
		}else if(vo!=null && vo.getId()==null){
			if(userVo!=null){
				vo.setCreateUser(userVo.getUserCode());
			}
			vo.setCreateTime(new Date());
			Beans.copy().from(vo).to(po);
			databaseDao.save(PrpLFxqFavoree.class,po);
		}
		
	}
	@Override
	public List<PrpLPayCustomVo> findPrpLPayCustomVoByIds(List<Long> ids) {
		List<PrpLPayCustomVo> volist=new ArrayList<PrpLPayCustomVo>();;
		QueryRule rule=QueryRule.getInstance();
		rule.addIn("id",ids);
		List<PrpLPayCustom> polist=databaseDao.findAll(PrpLPayCustom.class,rule);
		if(polist!=null && polist.size()>0){
			 volist=Beans.copyDepth().from(polist).toList(PrpLPayCustomVo.class);
		}
		return volist;
	}

	/**
	 *
	 * @param unitedName
	 * @param unitedCode
	 * @return
	 */
	@Override
	public AccBankNameVo findBankInfoFromName(String unitedName,String unitedCode){
		AccBankNameVo bankNameVo= new AccBankNameVo();
		OpenBankResponseDto bankDto = findBankInfoFromUnitedName(unitedName);
		List<OpenBankDetailDto> listOpenBankDetail = bankDto.getData();
		if(listOpenBankDetail != null && listOpenBankDetail.size()>0){
			for (OpenBankDetailDto openBankDetailDto : listOpenBankDetail) {
				if(unitedCode.equals(openBankDetailDto.getUnitedCode())){
					bankNameVo.setBankCode(openBankDetailDto.getBankCode());//银行大类代码
					bankNameVo.setBankName(openBankDetailDto.getBankName());//银行大类名称
					bankNameVo.setCity(openBankDetailDto.getCityName());//市名称
					bankNameVo.setCityCode(openBankDetailDto.getCityCode());//市代码
					bankNameVo.setProvincial(openBankDetailDto.getProvinceName());//省名称
					bankNameVo.setProvinceCode(openBankDetailDto.getProvinceCode());//省代码
					bankNameVo.setF01(openBankDetailDto.getUnitedCode());//联行号代码
					bankNameVo.setF02(openBankDetailDto.getUnitedName());//联行号名称
					break;
				}
			}
		}
		return bankNameVo;

	}
    
}
