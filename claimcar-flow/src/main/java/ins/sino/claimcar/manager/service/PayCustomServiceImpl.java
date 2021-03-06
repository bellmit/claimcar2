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
			// ?????????Vo?????????
			payCustomVo = Beans.copyDepth().from(payCustom)
					.to(PrpLPayCustomVo.class);
			payCustomVos.add(payCustomVo);
		}

		return payCustomVos;
	}
	/**
	 * ???????????????????????????
	 * <pre></pre>
	 * @param prpLPayCustomVo
	 * @param start
	 * @param length
	 * @return
	 * @modified:
	 * ???WLL(2016???11???23??? ??????11:58:56): <br>
	 */
	@Override
	public ResultPage<PrpLPayCustomVo> findPrpLPayCustomByNameAccNo(PrpLPayCustomVo prpLPayCustomVo,int start, int length) {
		// ??????????????????????????????
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" select min(a.id), a.payObjectKind  ,a.payeeName ,a.certifyNo,a.bankOutlets, a.accountNo , a.registNo");
		sqlUtil.append(" from PrpLPayCustom a where 1=1 ");
		if(StringUtils.isNotBlank(prpLPayCustomVo.getRegistNo())){
			sqlUtil.append(" and  a.registNo= ? ");
			sqlUtil.addParamValue(prpLPayCustomVo.getRegistNo() );
		}else{
			logger.debug("????????????????????????????????????????????????");
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

		// ????????????ResultPage
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
				Long accID = -id;//accID = 0 ?????????????????????
				//????????????????????????????????????,id<0,payeeid?????????account???ID??????????????????????????????
				if(accID.toString().length() < 6 && accID != 0){
					//???????????????????????????????????????payeeid??????7????????????ywuser.prplpayobject
					SysCodeDictVo dictVo = codeTranService.findTransCodeDictVo("OldIntermCode",accID.toString());
					if(dictVo != null){
						payCustomVo.setPayeeName(dictVo.getCodeName());
						payCustomVo.setAccountNo("0");
						payCustomVo.setAccountId(accID.toString());
						payCustomVo.setBankName("0");
						payCustomVo.setCertifyNo("0");
						payCustomVo.setFlag("3");//????????????????????????????????????????????????
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
						payCustomVo.setFlag("2");//??????????????????????????????????????????
					}
				}
				if(StringUtils.isBlank(payCustomVo.getAccountId())){
					//????????????????????????????????????????????????????????????
					payCustomVo.setPayeeName("???????????????");
					payCustomVo.setAccountNo("0");
					payCustomVo.setAccountId(accID.toString());
					payCustomVo.setBankName("0");
					payCustomVo.setCertifyNo("0");
					payCustomVo.setFlag("2");//??????????????????????????????????????????
				}
				
			}else{
				queryRule.addEqual("id", id);
				List<PrpLPayCustom> payCustomPos = databaseDao.findAll(PrpLPayCustom.class, queryRule);
				if(payCustomPos!=null && payCustomPos.size()>0){
					PrpLPayCustom payCustomPo = payCustomPos.get(0);
					// ?????????Vo?????????
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
		// ??????????????????????????????????????????????????????????????????????????????????????????????????????
		if(payCustomVo.getId()!=null){
			PrpLPayCustom payCusOld = databaseDao.findByPK(PrpLPayCustom.class,payCustomVo.getId());
			if(payCusOld!=null
					&&payCusOld.getPayeeName().equals(payCustomVo.getPayeeName())
					&&payCusOld.getAccountNo().equals(payCustomVo.getAccountNo())){
				logger.info(payCustomVo.getId()+"??????????????????????????????????????????????????????????????????");
			}else{
				// ?????? ????????????????????????????????? ??? ???????????? ???????????????
				if(!"Y".equals(payCustomVo.getRemark())){
					// ???????????????????????????????????????????????????????????????
					boolean callPayFlag = this.adjustExistCallPayment(payCustomVo.getId());
					if(callPayFlag){
						throw new IllegalArgumentException("???????????????????????????????????????????????????????????????");
					}
				}else{
					payCustomVo.setRemark(null);
				}
			}
		}
		// ?????????????????????????????????????????????????????????????????????????????????????????????????????????
		List<PrpLPayCustomVo> cusVoList = this.findPayCustomByRegistNoAndAccount(payCustomVo.getRegistNo(),payCustomVo.getAccountNo());
		if(cusVoList!=null&&cusVoList.size()>0){
			for(PrpLPayCustomVo cusVo:cusVoList){
				if(!cusVo.getId().equals(payCustomVo.getId())){
					throw new IllegalArgumentException("????????????????????????????????????????????????"+payCustomVo.getAccountNo()+"???????????????????????????");
				}
			}
		}
		
		Date date = new Date();// ????????????
		String userCode = userVo.getUserCode();// ????????????

		payCustomVo.setBankType(payCustomVo.getBankName());
		payCustomVo.setValidFlag("1");

		PrpLPayCustom payCustomPo = new PrpLPayCustom();
		// ?????????????????????????????????????????????????????????????????????????????????????????????????????????
		if(payCustomVo.getId()!=null){// ??????
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
        }else{//??????
            // ???vo?????????po
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
        	logger.info("??????????????????????????????????????????" + rePayCustom.getRegistNo() +" ??????????????????" + rePayCustom.getPayeeName() + " ??????????????????" + rePayCustom.getAccountNo());
        	// String accountId = SaveOrUpdatePaymentAccount(rePayCustom,userVo);
        	// payCustomPo.setAccountId(accountId);
        	databaseDao.save(PrpLPayCustom.class,payCustomPo);
        	logger.info("??????????????????????????????????????????" + rePayCustom.getRegistNo() +" ??????????????????" + rePayCustom.getPayeeName() + " ??????????????????" + rePayCustom.getAccountNo());
		} catch (Exception e) {
			logger.info("??????????????????????????????????????????" + rePayCustom.getRegistNo()+" ??????????????????" + rePayCustom.getPayeeName() + " ??????????????????" + rePayCustom.getAccountNo(), e);
			throw e;
		}
		if(payaccRenewHisVo != null){
			// ????????????????????????
			payaccRenewHisVo.setAccountNo(payCustomPo.getAccountNo());
			payaccRenewHisVo.setRegistNo(payCustomPo.getRegistNo());
			payaccRenewHisVo.setPayeeId(payCustomPo.getId());
			PrplPayaccRenewHis prplpayAccRenewHis = new PrplPayaccRenewHis();
			Beans.copy().from(payaccRenewHisVo).to(prplpayAccRenewHis);
            databaseDao.save(PrplPayaccRenewHis.class,prplpayAccRenewHis);
		}
	    //??????prpCInsuredrela??????????????????
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
			// ?????????????????????
			List<PrpLPayment> PrpLPaymentList = databaseDao.findAll(PrpLPayment.class,qr);
			if(PrpLPaymentList!=null&&PrpLPaymentList.size()>0){
				return true;
			}else{
				// ?????????????????????
				List<PrpLPrePay> PrpLPrePayList = databaseDao.findAll(PrpLPrePay.class,qr);
				if(PrpLPrePayList!=null&&PrpLPrePayList.size()>0){
					return true;
				}else{
					// ?????????????????????
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
	 * ??????????????????????????????????????????????????? ????????????true
	 * <pre></pre>
	 * @param payCustomVo
	 * @return
	 * @modified:
	 * ???WLL(2016???12???29??? ??????4:57:18): <br>
	 */
	@Override
	public PrpLPayCustomVo adjustExistSamePayCusDifName(PrpLPayCustomVo payCustomVo){
		PrpLPayCustomVo rePayCus = null;
		if(!"0".equals(payCustomVo.getValidFlag())){
			// ?????????????????????????????? ??????????????????????????????????????? ???????????????????????????????????????
			List<PrpLPayCustomVo> PayCusVoList = this.getPayCustomByAccount(payCustomVo.getAccountNo());
			if(PayCusVoList != null && PayCusVoList.size()>0){
				for(PrpLPayCustomVo payCus : PayCusVoList){
					if(!payCustomVo.getPayeeName().equals(payCus.getPayeeName().replaceAll("\\s*", ""))
							&&!payCus.getId().equals(payCustomVo.getId())){
						rePayCus = payCus;// ???????????????????????????????????????  ???????????????????????????????????????
						break;
					}
				}
			}
		}
		return rePayCus;// ??????????????????????????????????????????
	}
	/**
	 *  ???????????????????????????????????????????????????????????????????????????
	 * <pre></pre>
	 * @param accountNo
	 * @return
	 * @modified:
	 * ???WLL(2016???12???29??? ??????4:48:09): <br>
	 */
	public List<PrpLPayCustomVo> getPayCustomByAccount(String accountNo){
		List<PrpLPayCustomVo> rePayCusVoList = null;
		
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLPayCustom a ");
		sqlUtil.append(" where a.validFlag != ? and a.accountNo = ? ");// ?????????????????????????????????
		sqlUtil.addParamValue("0");
		sqlUtil.addParamValue(accountNo);
		sqlUtil.append(" and not exists (select 1 from PrpLPayment b where (b.payStatus in (?,?) or b.sumRealPay = 0) and b.payeeId = a.id) ");// ?????????????????????????????? ???????????????????????????
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("3");
		sqlUtil.append(" and not exists (select 1 from PrpLCharge c where (c.payStatus in (?,?) or c.feeRealAmt = 0) and c.payeeId = a.id) ");// ?????????????????????????????? ???????????????????????????
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
		// ????????????????????????  ?????????????????? ???????????????  ??????
		List<AccountInfo005> accountInfoList = bankAccountService.searchAccount(PayCustom.getAccountNo(),null,userVo.getUserCode());
		// ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		AccountInfo005 accountInfo005 = new AccountInfo005();
		accountInfo005.setAccountCode(PayCustom.getAccountNo());
		accountInfo005.setCurrency("CNY");
		accountInfo005.setAccountName(PayCustom.getPayeeName());

		if(accountInfoList!=null&&accountInfoList.size()>0){//??????
			accountInfoList.removeAll(Collections.singletonList(null));
			for(AccountInfo005 accountInfo : accountInfoList){
				if(accountInfo.getAccountCode().equals(PayCustom.getAccountNo())){
					accountInfo005 = accountInfo;
					break;
				} 
			}
		}

		accountInfo005.setBankCode(PayCustom.getBankName());
		accountInfo005.setFlag(PayCustom.getBankNo());//?????????
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
			if(accountInfoList!=null&&accountInfoList.size()>0){//??????
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
		 * (cMainVo.getRiskCode().equals("1101")) {// ????????????????????????????????? cMain =
		 * cMainVo; } }
		 */
		List<PrpLCInsuredVo> cInsuredVos = cMain.getPrpCInsureds();
		PrpLCInsuredVo cInsured = new PrpLCInsuredVo();
		for (PrpLCInsuredVo cInsuredVo : cInsuredVos) {
			if(cInsuredVo.getInsuredFlag().equals("1")){// ?????????????????????????????????????????????
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
		
		// ????????????list???ps?????????????????????????????????object??????
		List<Object> paramValues = new ArrayList<Object>();
		// hql????????????
		StringBuffer queryString = new StringBuffer("from AccBankName pf where 1=1 ");
		queryString.append(" AND pf.validStatus = ? ");//????????????????????????
		paramValues.add("1");
		if(!isNum){
			if (StringUtils.isNotBlank(accBankNameVo.getProvincial())) {
				queryString.append(" AND pf.provincial = ? ");
				paramValues.add(accBankNameVo.getProvincial());
			}
			if(StringUtils.isNotBlank(accBankNameVo.getCity())){
				queryString.append(" AND pf.city = ? ");
				if(accBankNameVo.getCity().equals("?????????")){
					paramValues.add(accBankNameVo.getProvincial());
				}else{
					paramValues.add(accBankNameVo.getCity());
				}
			}
			if(!"????????????".equals(accBankNameVo.getBelongBank())){
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
			// ???????????????????????????????????????????????????
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
	 * ????????????????????????????????????????????????????????????????????????
	 * @param bankCode ?????????
	 * @return ???????????????
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
	 * ??????????????????????????????????????????
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
		Date date = new Date();// ????????????
		PrpLPayCustom prpLPayCustomPo=databaseDao.findByPK(PrpLPayCustom.class, payCustomVo.getId());
		Beans.copy().excludeNull().from(payCustomVo).to(prpLPayCustomPo);
		//??????????????????
		
		String SVR_URL = SpringProperties.getProperty("PAYMENT_SVR_URL");
		if(SVR_URL==null){
			logger.warn("????????????????????????????????????????????????");
			throw new Exception("??????????????????????????????");
		}
		SVR_URL = SVR_URL +"/service/ClaimReturnTicket";//?????????????????????????????????????????????????????????  ???sys_config??????????????????
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
		accountVo.setCurrency("01");//????????????
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
		logger.info("???????????????--->"+xString);
		JPlanReturnVo jPlanReturnVo = (JPlanReturnVo)stream.fromXML(xString);
		String accountId = jPlanReturnVo.getAccountNo();
		// ??????accountID
		System.out.println(accountId+accountId!=null&&accountId!="null"&&accountId.length()>0);
		if(accountId!=null&&!"null".equals(accountId)&&accountId.length()>0){
			prpLPayCustomPo.setAccountId(accountId);
		}
		//????????????
		 ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		 logVo.setRegistNo(prpLPayCustomPo.getRegistNo());
		 logVo.setBusinessType(BusinessType.ModAccount.name());
		 logVo.setBusinessName(BusinessType.ModAccount.getName());
		 logVo.setComCode(userVo.getComCode());
		 logVo.setCompensateNo(compensateNo);
		 logVo.setRequestTime(date);
		 logVo.setRequestUrl(SVR_URL);
		 if("??????".equals(jPlanReturnVo.getErrorMessage())){
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
	 * ?????????????????????????????? ???(2016???1???21??? ??????6:00:27): <br>
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
	 * ??????ID??????PrpLPayCustom???
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
		//???????????????????????????????????????
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
			logger.debug("????????????????????????????????????????????????");
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
		//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		String cacheKey = "bankcode_"+flag;
		List<PrplOldaccbankCodeVo> 	listVo1 = (List<PrplOldaccbankCodeVo>)childArcaCodeMap.getCache(cacheKey);
          if(listVo1==null||listVo1.size()<1){
			// ?????????????????????????????????????????????
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
			//?????????????????????????????????????????????
		   if( !ObjectUtils.isEmpty(listVo)){
			childArcaCodeMap.putCache(cacheKey,listVo);
		    }
		   return listVo;
         }else{
            return listVo1;
         }
	}
          
	/**
	 * ????????????????????????????????????????????????
	 * @param responseDto ????????????????????????
	 * @date 2020???7???4??? 14:48:24
	 * @return ????????????
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
	 * ???????????????????????????????????????????????????
	 * @param responseDto ????????????????????????
	 * @date 2020-7-6 11:30:08
	 * @return ????????????
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
	 * ????????????????????????????????????????????????
	 * @date 2020???7???4??? 14:47:13
	 * @return ?????????????????????????????????
	 */
	private BankClassResponseDto findBankKindsFromBasicPlatform() {

		BankClassResponseDto responseDto = new BankClassResponseDto();
		BankClassRequestDto requestDto = new BankClassRequestDto();
		String responseString = "";
		Gson gson = new Gson();
		String platformUrl = SpringProperties.getProperty("basicdataUrl");
		if (platformUrl == null) {
			logger.info("????????????????????????????????????????????????");
			return responseDto;
		}
		// ????????????????????????
		platformUrl = platformUrl + "/bank/kinds";
		try {
			String requestString = gson.toJson(requestDto);
			logger.info("?????????????????????????????????????????????????????????" + requestString);
			responseString = getBasicPlatformData(requestString, platformUrl);
			if (StringUtils.isBlank(responseString)) {
				logger.info("requestString: " + requestString + "?????????????????????????????????????????????");
				return responseDto;
			}
			logger.info("??????????????????????????????????????????" + responseString);
		} catch (Exception e) {
			logger.info("???????????????????????????????????????????????????", e);
			return responseDto;
		}

		try {
			responseDto = gson.fromJson(responseString, BankClassResponseDto.class);
		} catch (Exception e) {
			logger.info("???????????????????????????????????????????????????????????????????????????????????????" + responseString, e);
			return responseDto;
		}

		return responseDto;
	}

	/**
	 * ????????????????????????????????????
	 * @date 2020???7???4??? 14:47:13
	 * @return ?????????????????????????????????
	 */
	private OpenBankResponseDto findBankInfoFromBasicPlatform(AccBankNameVo accBankNameVo) {
		OpenBankResponseDto responseDto = new OpenBankResponseDto();
		OpenBankRequestDto requestDto = new OpenBankRequestDto();

		String platformUrl = SpringProperties.getProperty("basicdataUrl");
		if (platformUrl == null) {
			logger.info("????????????????????????????????????????????????");
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
					// ????????????????????????????????????????????????
					requestDto.setUnitedCode(accBankNameVo.getBankName());
				} else {
					requestDto.setUnitedName(accBankNameVo.getBankName());
				}
			}
		}

		String responseString = "";
		Gson gson = new Gson();

		// ???????????????????????????
		platformUrl = platformUrl + "/bank/openBankInfo";
		try {
			String requestString = gson.toJson(requestDto);
			logger.info("??????????????????????????????????????????????????????" + requestString);
			responseString = getBasicPlatformData(requestString, platformUrl);
			if (StringUtils.isBlank(responseString)) {
				logger.info("requestString: " + requestString + "??????????????????????????????????????????");
				return responseDto;
			}
			logger.info("???????????????????????????????????????" + responseString);
		} catch (Exception e) {
			logger.info("????????????????????????????????????????????????", e);
			return responseDto;
		}

		try {
			responseDto = gson.fromJson(responseString, OpenBankResponseDto.class);
		} catch (Exception e) {
			logger.info("???????????????????????????????????????????????????????????????????????????????????????" + responseString, e);
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
			logger.info("????????????????????????????????????????????????");
			return responseDto;
		}
		// ???????????????????????????
		platformUrl = platformUrl + "/bank/getBankInfoByName";
		String responseString = "";
		String requestString = "{\"unitedName\":\""+unitedName+"\"}";
		Gson gson = new Gson();
		try {
			logger.info("??????????????????????????????????????????????????????" + requestString);
			responseString = getBasicPlatformData(requestString, platformUrl);
			if (StringUtils.isBlank(responseString)) {
				logger.info("requestString: " + requestString + "??????????????????????????????????????????");
				return responseDto;
			}
			logger.info("???????????????????????????????????????" + responseString);
		} catch (Exception e) {
			logger.info("????????????????????????????????????????????????", e);
			return responseDto;
		}

		try {
			responseDto = gson.fromJson(responseString, OpenBankResponseDto.class);
		} catch (Exception e) {
			logger.info("???????????????????????????????????????????????????????????????????????????????????????" + responseString, e);
			return responseDto;
		}
		return responseDto;

	}
	/**
	 * ????????????????????????
	 * @param requestParams ????????????
	 * @param platformUrl ????????????
	 * @return ??????????????????
	 * @throws Exception ??????????????????
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
			// post????????????????????????
			httpURLConnection.setUseCaches(false);
			// ?????????????????????Content-Type????????????text/xml
			httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			// ???????????????
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setConnectTimeout(20 * 1000);
			httpURLConnection.setReadTimeout(20 * 1000);
			httpURLConnection.setAllowUserInteraction(true);
			httpURLConnection.connect();
		} catch (Exception ex) {
			throw new Exception("???????????????????????????????????????????????????", ex);
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
			throw new Exception("????????????????????????????????????", e);
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
	 * ????????????????????????????????????????????????????????????
	 * <pre></pre>
	 * @param registNo
	 * @param accountNo ????????????
	 * @return
	 * @modified:
	 * ???WLL(2017???6???13??? ??????5:53:50): <br>
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
                // ?????????Vo?????????
                payCustomVo = Beans.copyDepth().from(payCustom).to(PrpLPayCustomVo.class);
                payCustomVos.add(payCustomVo);
            }
        }
        return payCustomVos;
    }

    /**
     *??????????????????????????????????????????????????????
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
        //??????????????????????????????????????????
        
        
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

        Date date = new Date();// ????????????
        String userCode = userVo.getUserCode();// ????????????

        payCustomVo.setBankType("test");
        payCustomVo.setValidFlag("1");

        PrpLPayCustom payCustomPo = new PrpLPayCustom();
        if(payCustomVo.getId()!=null){//??????
            payCustomPo = databaseDao.findByPK(PrpLPayCustom.class,payCustomVo.getId());
            Beans.copy().from(payCustomVo).excludeNull().to(payCustomPo);
            payCustomPo.setCreateUser(payCustomPo.getCreateUser());
            payCustomPo.setCreateTime(payCustomPo.getCreateTime());
            payCustomPo.setUpdateUser(userCode);
            payCustomPo.setUpdateTime(date);
            databaseDao.update(PrpLPayCustom.class,payCustomPo);
        }else{//??????
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
    public void saveOrUpdateInsuredrelaByCustomVo(PrpLPayCustomVo payCustomVo,PrpCInsuredrelaVo prpCInsuredrelaVo,SysUserVo userVo) {//ADDRESS???????????????id
        Date date = new Date();// ????????????
        String userCode = userVo.getUserCode();
        if(payCustomVo.getId()!=null){
            QueryRule qr = QueryRule.getInstance();
            qr.addEqual("address",payCustomVo.getId().toString());
            List<PrpCInsuredrela> insuredrelaPoList = databaseDao.findAll(PrpCInsuredrela.class,qr);
            if(insuredrelaPoList != null && insuredrelaPoList.size() > 0){//??????update
                PrpCInsuredrela prpCInsuredrela = insuredrelaPoList.get(0);
                Beans.copy().from(prpCInsuredrelaVo).excludeNull().to(prpCInsuredrela);
                prpCInsuredrela.setOperatorCode(userCode);
                prpCInsuredrela.setOperateTime(date);
                databaseDao.update(PrpCInsuredrela.class,prpCInsuredrela);
            }else{//??????save
                PrpCInsuredrela prpCInsuredrela = new PrpCInsuredrela();
                Beans.copy().from(prpCInsuredrelaVo).to(prpCInsuredrela);
                prpCInsuredrela.setOperatorCode(userCode);
                prpCInsuredrela.setOperateTime(date);
                prpCInsuredrela.setAddress(payCustomVo.getId().toString());
                databaseDao.save(PrpCInsuredrela.class,prpCInsuredrela);
            }
        }else{//??????save
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
     * ???????????????PrpCInsuredrela
     * <pre></pre>
     * @param prpCInsuredrelaVos
     * @param userVo
     * @modified:
     * ???zhujunde(2017???7???18??? ??????4:42:50): <br>
     */
    public void saveOrUpdateInsuredrela(List<PrpCInsuredrelaVo> prpCInsuredrelaVos,SysUserVo userVo){
        for(PrpCInsuredrelaVo vo : prpCInsuredrelaVos){
            Date date = new Date();// ????????????
            String userCode = userVo.getUserCode();
            if(StringUtils.isNotBlank(vo.getAddress())){
                QueryRule qr = QueryRule.getInstance();
                qr.addEqual("address",vo.getAddress());
                List<PrpCInsuredrela> insuredrelaPoList = databaseDao.findAll(PrpCInsuredrela.class,qr);
                if(insuredrelaPoList != null && insuredrelaPoList.size() > 0){//??????update
                    PrpCInsuredrela prpCInsuredrela = insuredrelaPoList.get(0);
                    Beans.copy().from(vo).excludeNull().to(prpCInsuredrela);
                    prpCInsuredrela.setOperatorCode(userCode);
                    prpCInsuredrela.setOperateTime(date);
                    databaseDao.update(PrpCInsuredrela.class,prpCInsuredrela);
                }else{//??????save
                    PrpCInsuredrela prpCInsuredrela = new PrpCInsuredrela();
                    Beans.copy().from(vo).to(prpCInsuredrela);
                    prpCInsuredrela.setOperatorCode(userCode);
                    prpCInsuredrela.setOperateTime(date);
                    prpCInsuredrela.setAddress(vo.getAddress());
                    databaseDao.save(PrpCInsuredrela.class,prpCInsuredrela);
                }
            }else{//??????save
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
        if("2".equals(insuredType)){//??????  
            PrpCInsuredrelaVo prpCInsuredrelaVo = new PrpCInsuredrelaVo();
            prpCInsuredrelaVo.setCertiNo(prpLFxqFavoreeVos.get(0).getCompensateNo());
            prpCInsuredrelaVo.setCertiType("C");
            //????????????
            prpCInsuredrelaVo.setCustomerKind("2");
            prpCInsuredrelaVo.setCustomerType("4");
            prpCInsuredrelaVo.setName(payCustomVo.getAuthorityName());
            prpCInsuredrelaVo.setNatioNality(payCustomVo.getNationality());
            prpCInsuredrelaVo.setIdentifyType(payCustomVo.getAuthorityCertifyType());
            prpCInsuredrelaVo.setIdentifyNumber(payCustomVo.getAuthorityNo());
            prpCInsuredrelaVo.setValidDate(payCustomVo.getAuthorityStartDate());
            prpCInsuredrelaVo.setPhone(payCustomVo.getAuthorityPhone());
            saveOrUpdateInsuredrelaByCustomVo(payCustomVo,prpCInsuredrelaVo,userVo);
            //?????????
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
                    /*insuredrelaVo.setOwnType(payFxqCustomVo.getLegalType());//??????????????????      
                    insuredrelaVo.setGender(payFxqCustomVo.getPersonSex());
                    insuredrelaVo.setRevenueRegistNo("");
                    insuredrelaVo.setBusinessCode("");
                    insuredrelaVo.setBusinessArea("");
                    insuredrelaVo.setOccupAtionCode(payFxqCustomVo.getProfession());//??????????????????         
                    insuredrelaVo.setAddrType("");
                    insuredrelaVo.setOperatorCode(payFxqCustomVo.getCreateuser());
                    insuredrelaVo.setOperateTime(payFxqCustomVo.getCreatetime());*/
                    prpCInsuredrelaVos.add(insuredrelaVo);
                }
                saveOrUpdateInsuredrelaByFxqCustomVo(prpCInsuredrelaVos,userVo);
            }
            //????????? 
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
					bankNameVo.setBankCode(openBankDetailDto.getBankCode());//??????????????????
					bankNameVo.setBankName(openBankDetailDto.getBankName());//??????????????????
					bankNameVo.setCity(openBankDetailDto.getCityName());//?????????
					bankNameVo.setCityCode(openBankDetailDto.getCityCode());//?????????
					bankNameVo.setProvincial(openBankDetailDto.getProvinceName());//?????????
					bankNameVo.setProvinceCode(openBankDetailDto.getProvinceCode());//?????????
					bankNameVo.setF01(openBankDetailDto.getUnitedCode());//???????????????
					bankNameVo.setF02(openBankDetailDto.getUnitedName());//???????????????
					break;
				}
			}
		}
		return bankNameVo;

	}
    
}
