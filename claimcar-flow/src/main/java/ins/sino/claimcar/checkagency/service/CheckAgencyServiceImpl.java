package ins.sino.claimcar.checkagency.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.bankaccount.service.BankAccountService;
import ins.sino.claimcar.manager.po.PrpdCheckBankMain;
import ins.sino.claimcar.manager.po.PrpdIntermBank;
import ins.sino.claimcar.manager.po.PrpdIntermUser;
import ins.sino.claimcar.manager.po.PrpdcheckBank;
import ins.sino.claimcar.manager.po.PrpdcheckServer;
import ins.sino.claimcar.manager.po.PrpdcheckUser;
import ins.sino.claimcar.manager.service.IntermediaryService;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.manager.vo.PrpdcheckBankVo;
import ins.sino.claimcar.manager.vo.PrpdcheckServerVo;
import ins.sino.claimcar.manager.vo.PrpdcheckUserVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.sinosoft.arch5service.dto.prpcar004.Prpcar004ResDto;
import com.sinosoft.arch5service.dto.prpcar005.AccountInfo005;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})

@Path("checkAgencyService")
public class CheckAgencyServiceImpl implements CheckAgencyService  {
	private Logger logger = LoggerFactory.getLogger(CheckAgencyServiceImpl.class);
	
	@Autowired
	private DatabaseDao databaseDao;
	
	@Autowired 
	private IntermediaryService intermediaryService; 
	@Autowired
	private BankAccountService bankAccountService;
	
	@Autowired
	private BaseDaoService baseDaoService;
	
	@Override
	public PrpdCheckBankMainVo findCheckBankByCode(String checkCode,String comCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("checkCode", checkCode);
		if(StringUtils.isBlank(comCode)){
			comCode = ServiceUserUtils.getComCode();
		}
		if(comCode.startsWith("00")){//??????
			comCode = comCode.substring(0, 4)+"0000";
		}else{
			comCode = comCode.substring(0, 2)+"000000";
		}
		queryRule.addEqual("comCode", comCode);

		List<PrpdCheckBankMain> checkList = databaseDao.findAll(
				PrpdCheckBankMain.class, queryRule);
		if (checkList == null || checkList.size() == 0) {
			return null;
		}
		PrpdCheckBankMainVo checkBankVo = Beans.copyDepth().from(checkList.get(0))
				.to(PrpdCheckBankMainVo.class);

		return checkBankVo;
	}

	@Override
	public PrpdCheckBankMainVo findCheckBankByComCode(String comCode) {
		PrpdCheckBankMainVo checkBankMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("comCode", comCode);
		queryRule.addEqual("validStatus", "1");
		List<PrpdCheckBankMain> checkBankList = databaseDao.findAll(
				PrpdCheckBankMain.class, queryRule);
		if (checkBankList != null && checkBankList.size() > 0) {
			checkBankMainVo = new PrpdCheckBankMainVo();
			checkBankMainVo = Beans.copyDepth().from(checkBankList.get(0)).to(PrpdCheckBankMainVo.class);
		}
		return checkBankMainVo;
	}

	@Override
	public PrpdCheckBankMainVo findCheckBankById(Long id) {
		PrpdCheckBankMain prpdCheckBankMain = databaseDao.findByPK(PrpdCheckBankMain.class,id);
		
		PrpdCheckBankMainVo vo =new PrpdCheckBankMainVo();
		if(prpdCheckBankMain != null){
			vo=Beans.copyDepth().from(prpdCheckBankMain).to(PrpdCheckBankMainVo.class);
		}
		return vo;
	}

	@Override
	public List<PrpdCheckBankMainVo> findCheckBankListByComCode(String comCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("comCode", comCode);
		List<PrpdCheckBankMainVo> checkBankVoList = new ArrayList<PrpdCheckBankMainVo>();
		List<PrpdCheckBankMain> checkBankList = databaseDao.findAll(
				PrpdCheckBankMain.class, queryRule);
		if (checkBankList != null && checkBankList.size() > 0) {
			for (PrpdCheckBankMain prpdCheckBankMain : checkBankList) {
				PrpdCheckBankMainVo checkBankVo = Beans.copyDepth().from(prpdCheckBankMain).to(PrpdCheckBankMainVo.class);
				checkBankVoList.add(checkBankVo);
			}
		}
		return checkBankVoList;
	}

	@Override
	public List<PrpdCheckBankMainVo> findCheckBankListByHql(String comCode) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" FROM PrpdCheckBankMain prpMain where 1=1");
		if (StringUtils.isNotBlank(comCode)) {
			sqlUtil.append(" and prpMain.comCode like ?");
			 //comCode = comCode.substring(0, 2);
				if(comCode.startsWith("00")){
					comCode = comCode.substring(0, 4);
				}else{
					comCode = comCode.substring(0, 2);
				}
			sqlUtil.addParamValue((comCode.equals("0000")?"":comCode) + "%");
		}
		sqlUtil.append(" and prpMain.validStatus = ? ");
		sqlUtil.addParamValue("1");
		
		sqlUtil.append(" order by checkCode  ");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<PrpdCheckBankMain> checkBankList = databaseDao.findAllByHql(
				PrpdCheckBankMain.class, sql, values);
		List<PrpdCheckBankMainVo> checkBankVoList = new ArrayList<PrpdCheckBankMainVo>();
		if (checkBankList != null && checkBankList.size() > 0) {
			for (PrpdCheckBankMain prpdCheckBankMain : checkBankList) {
				PrpdCheckBankMainVo checkBankVo = Beans.copyDepth()
						.from(prpdCheckBankMain).to(PrpdCheckBankMainVo.class);
				checkBankVoList.add(checkBankVo);
			}
		}
		return checkBankVoList;
	}

	
	@Override
	public List<PrpdCheckBankMainVo> findCheckBankListByCaseType(String comCode,String caseType) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		List<PrpdCheckBankMainVo> checkMainVoList = new ArrayList<PrpdCheckBankMainVo>();
		
		sqlUtil.append(" select distinct m.id,m.checkCode,m.checkName  from PrpdCheckBankMain m  where ");
		if("0".equals(caseType) || "1".equals(caseType)){
			sqlUtil.append(" m.comcode like ?  ");
		}else{
			sqlUtil.append(" m.comcode not like ?  ");
			
		}
		if(comCode.startsWith("00")){
			comCode = comCode.substring(0, 4);
		}else{
			comCode = comCode.substring(0, 2);
		}
		sqlUtil.addParamValue(comCode+"%");
		List<Object[]> objList = baseDaoService.getAllBySql(sqlUtil.getSql(), sqlUtil.getParamValues());
		if(objList != null && objList.size() > 0){
			for(Object[] obj : objList){
				PrpdCheckBankMainVo checkBankMainVo = new PrpdCheckBankMainVo();
				checkBankMainVo.setId(Long.valueOf(obj[0].toString()));
				checkBankMainVo.setCheckCode((String)obj[1]);
				checkBankMainVo.setCheckName((String)obj[2]);
				checkMainVoList.add(checkBankMainVo);
			}
		}
		return checkMainVoList;
	}
	
	@Override
	public PrpdCheckBankMainVo findCheckBankByUserCode(String userCode) {
		PrpdCheckBankMainVo checkBankVo = null;
		
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode",userCode);
		queryRule.addDescOrder("updateTime");
		List<PrpdcheckUser> checkUserList = databaseDao.findAll(PrpdcheckUser.class,queryRule);
		if(checkUserList!=null && !checkUserList.isEmpty()){
			PrpdcheckUser checkuser = checkUserList.get(0);
			PrpdCheckBankMain checkBankMainPo = checkuser.getPrpdCheckBankMain();
			if(checkBankMainPo!=null && "1".equals(checkBankMainPo.getValidStatus())){
				checkBankVo = Beans.copyDepth().from(checkBankMainPo).to(PrpdCheckBankMainVo.class);
			}
			
		}
		
		return checkBankVo;
	}

	
	@Override
	public PrpdCheckBankMainVo findcheckById(String id) {
		PrpdCheckBankMain prpdCheckBankMain = databaseDao.findByPK(PrpdCheckBankMain.class,Long.valueOf(id));
		PrpdCheckBankMainVo vo =new PrpdCheckBankMainVo();
		if(prpdCheckBankMain != null){
			vo=Beans.copyDepth().from(prpdCheckBankMain).to(PrpdCheckBankMainVo.class);
		}
		return vo;
	}

	@Override
	public PrpdcheckBankVo findPrpdcheckBankVoById(Long id) {
		PrpdcheckBank prpdcheckBank = databaseDao.findByPK(PrpdcheckBank.class, id);
		
		PrpdcheckBankVo vo =new PrpdcheckBankVo();
		if(prpdcheckBank != null){
			Beans.copy().from(prpdcheckBank).to(vo);
			vo.setCheckMianId(prpdcheckBank.getPrpdCheckBankMain().getId());
		}
		return vo;
	}

	@Override
	public Map<String,String> findUserCode(String comCode,String userInfo,String gradeId) {
		return intermediaryService.findUserCode(comCode,userInfo,gradeId);
	}

	/* 
	 * @see ins.sino.claimcar.checkagency.service.CheckAgencyService#saveOrUpdateCheck(ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo, ins.sino.claimcar.manager.vo.PrpdcheckBankVo, java.util.List, ins.platform.vo.SysUserVo)
	 * @param checkMainVo
	 * @param checkBankVo
	 * @param checkUserVos
	 * @param userVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public PrpdCheckBankMainVo saveOrUpdateCheck(PrpdCheckBankMainVo checkMainVo,PrpdcheckBankVo checkBankVo,List<PrpdcheckUserVo> checkUserVos,
													SysUserVo userVo) throws Exception {
		Date date = new Date();// ????????????
		String user = ServiceUserUtils.getUserCode();// ????????????
		Date crDate = new Date();
		String crUser = ServiceUserUtils.getUserCode();
		
		checkMainVo.setUpdateUser(user);
		checkMainVo.setUpdateTime(date);
		checkMainVo.setCreateTime(crDate);
		checkMainVo.setCreateUser(crUser);

		PrpdCheckBankMain checkBankMainPo = null;

		PrpdcheckBank checkBankPo = Beans.copyDepth().from(checkBankVo)
				.to(PrpdcheckBank.class);
		checkBankPo.setUpdateUser(user);
		checkBankPo.setUpdateTime(date);
		checkBankPo.setBankType("test");
		checkBankPo.setCreateTime(crDate);
		checkBankPo.setCreateUser(crUser);
		// ????????????
		String accountId = null;
//		try{
//			accountId = this.SaveOrUpdateCheckBankAccount(checkBankPo,userVo);
//		}catch(Exception e){
//			logger.error("??????????????????",e);
//			throw new IllegalArgumentException("?????????????????????");
//		}
		checkBankPo.setAccountId(accountId);
		
		if (checkMainVo.getId() != null) {// ??????
			checkBankMainPo = databaseDao.findByPK(PrpdCheckBankMain.class,
					checkMainVo.getId());
			List<PrpdcheckUser> checkUserDatas = checkBankMainPo.getPrpdcheckUsers();
			List<PrpdcheckBank> banks=this.findPrpdcheckBankByParams(String.valueOf(checkMainVo.getId()),"0");
			
			Beans.copy().from(checkMainVo).excludeEmpty().to(checkBankMainPo);
			List<PrpdcheckBank> checkBankList = new ArrayList<PrpdcheckBank>();
			// bank
			if(banks!=null && banks.size()>0){
				for(PrpdcheckBank bank:banks){
					bank.setPrpdCheckBankMain(checkBankMainPo);
					checkBankList.add(bank);
				}
			}
			checkBankPo.setPrpdCheckBankMain(checkBankMainPo);
			
			checkBankList.add(checkBankPo);
			checkBankMainPo.setPrpdcheckBanks(checkBankList);


			// ????????????????????? ?????? ??? ?????? ???????????????
			if (checkUserDatas != null && !checkUserDatas.isEmpty()) {
				for (Iterator it = checkUserDatas.iterator(); it.hasNext();) {
					PrpdcheckUser checkUserData = (PrpdcheckUser)it.next();
					Boolean flag = true;
					if (checkUserDatas != null && !checkUserDatas.isEmpty()) {
						for (PrpdcheckUserVo checkUserVo : checkUserVos) {
							if ((checkUserVo != null) 
									&& (checkUserVo.getId() != null)
									&&(checkUserData.getId().longValue() == checkUserVo.getId().longValue())) { // ????????????id
								flag = false;
							}
						}
					}
					if (flag) {
						databaseDao.deleteByPK(PrpdcheckUser.class,checkUserData.getId());
						it.remove();
					}
				}
			}
			if (checkUserVos != null && !checkUserVos.isEmpty()) {
				for (PrpdcheckUserVo checkUserVo : checkUserVos) {
					if(checkUserVo != null && checkUserVo.getId() == null){
						//??????????????????????????????????????????????????????????????????
						QueryRule intermUserRule =  QueryRule.getInstance();
						intermUserRule.addEqual("userCode", checkUserVo.getUserCode());
						List<PrpdIntermUser> intermUserPoList = databaseDao.findAll(PrpdIntermUser.class, intermUserRule);
						if(intermUserPoList != null && !intermUserPoList.isEmpty()){
							 throw new IllegalArgumentException("????????????  : "+checkUserVo.getUserCode()+"-"+checkUserVo.getUserName()+"<br/>?????????????????? "
							+intermUserPoList.get(0).getPrpdIntermMain().getIntermCode()+"-"+intermUserPoList.get(0).getPrpdIntermMain().getIntermName()+" ?????????<br/>????????????");
										
						}
						QueryRule rule = QueryRule.getInstance();
						rule.addEqual("userCode", checkUserVo.getUserCode());
						List<PrpdcheckUser>	userPoList = databaseDao.findAll(PrpdcheckUser.class, rule);
						if(userPoList != null && userPoList.size() > 0){
							 throw new IllegalArgumentException("????????????  : "+checkUserVo.getUserCode()+"-"+checkUserVo.getUserName()+"<br/>?????????????????? "
						+userPoList.get(0).getPrpdCheckBankMain().getCheckCode()+"-"+userPoList.get(0).getPrpdCheckBankMain().getCheckName()+" ?????????<br/>????????????");
						}
						
						PrpdcheckUser checkUserPo = new PrpdcheckUser();
						checkUserPo = Beans.copyDepth().from(checkUserVo).to(PrpdcheckUser.class);
						checkUserPo.setUpdateUser(user);
						checkUserPo.setUpdateTime(date);
						checkUserPo.setCreateUser(crUser);
						checkUserPo.setCreateTime(crDate);
						checkUserPo.setPrpdCheckBankMain(checkBankMainPo);
						checkUserDatas.add(checkUserPo);
					}
				}
			}
			databaseDao.update(PrpdCheckBankMain.class, checkBankMainPo);
		} else {// ??????

			checkBankMainPo = new PrpdCheckBankMain();
			checkBankMainPo = Beans.copyDepth().from(checkMainVo)
					.to(PrpdCheckBankMain.class);
			// ???????????????
			checkBankPo.setPrpdCheckBankMain(checkBankMainPo);

			List<PrpdcheckBank> checkBankList = new ArrayList<PrpdcheckBank>();
			checkBankList.add(checkBankPo);
			checkBankMainPo.setPrpdcheckBanks(checkBankList);

			// user
			List<PrpdcheckUser> checkUserPoList = new ArrayList<PrpdcheckUser>(); //
			if (checkUserVos != null && !checkUserVos.isEmpty()) {
				for (PrpdcheckUserVo checkUserVo : checkUserVos) {
					if(checkUserVo != null){
						//??????????????????????????????????????????????????????????????????
						QueryRule intermUserRule =  QueryRule.getInstance();
						intermUserRule.addEqual("userCode", checkUserVo.getUserCode());
						List<PrpdIntermUser> intermUserPoList = databaseDao.findAll(PrpdIntermUser.class, intermUserRule);
						if(intermUserPoList != null && !intermUserPoList.isEmpty()){
							 throw new IllegalArgumentException("????????????  : "+checkUserVo.getUserCode()+"-"+checkUserVo.getUserName()+"<br/>?????????????????? "
							+intermUserPoList.get(0).getPrpdIntermMain().getIntermCode()+"-"+intermUserPoList.get(0).getPrpdIntermMain().getIntermName()+" ?????????<br/>????????????");
										
						}
						QueryRule rule = QueryRule.getInstance();
						rule.addEqual("userCode", checkUserVo.getUserCode());
						List<PrpdcheckUser>	userPoList = databaseDao.findAll(PrpdcheckUser.class, rule);
						if(userPoList != null && userPoList.size() > 0){
							throw new IllegalArgumentException("????????????  : "+checkUserVo.getUserCode()+"-"+checkUserVo.getUserName()+"<br/>?????????????????? "
									+userPoList.get(0).getPrpdCheckBankMain().getCheckCode()+"-"+userPoList.get(0).getPrpdCheckBankMain().getCheckName()+" ?????????<br/>????????????");
						}
						PrpdcheckUser checkUserPo = new PrpdcheckUser();
						checkUserPo = Beans.copyDepth().from(checkUserVo)
								.to(PrpdcheckUser.class);
						checkUserPo.setUpdateUser(user);
						checkUserPo.setUpdateTime(date);
						checkUserPo.setCreateUser(crUser);
						checkUserPo.setCreateTime(crDate);
						checkUserPo.setPrpdCheckBankMain(checkBankMainPo);
						checkUserPoList.add(checkUserPo);
					}
				}
			}
			checkBankMainPo.setPrpdcheckUsers(checkUserPoList);

			databaseDao.save(PrpdCheckBankMain.class, checkBankMainPo);
		}
		// ??????????????????????????????????????????recheckMainVo?????????,??????return
		PrpdCheckBankMainVo recheckMainVo = Beans.copyDepth().from(checkBankMainPo)
				.to(PrpdCheckBankMainVo.class);
		recheckMainVo.setId(checkBankMainPo.getId());
		return recheckMainVo;

	}

	/**
	 * <pre></pre>
	 * @param checkBankPo
	 * @param userVo
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ???XiaoHuYao(2019???8???2??? ??????10:27:43): <br>
	 */
	private String SaveOrUpdateCheckBankAccount(PrpdcheckBank checkBankPo,SysUserVo userVo) throws Exception {
	List<AccountInfo005> accountInfoList = bankAccountService.searchAccount(checkBankPo.getAccountNo(),null,userVo.getUserCode());
		
		AccountInfo005 accountInfo005 = null;
		if(accountInfoList!=null&&accountInfoList.size()>0){//??????
			accountInfo005 = accountInfoList.get(0);
		}else{//??????
			accountInfo005 = new AccountInfo005();
			accountInfo005.setAccountCode(checkBankPo.getAccountNo());
			accountInfo005.setCurrency("CNY");
			accountInfo005.setAccountName(checkBankPo.getAccountName());
		}
		accountInfo005.setBankCode(checkBankPo.getBankName());
		accountInfo005.setFlag(checkBankPo.getBankNumber());//?????????
		accountInfo005.setIdentifyType("10");//??????????????????
		accountInfo005.setIdentifyNumber(checkBankPo.getCertifyNo());//??????????????????
		accountInfo005.setCity(checkBankPo.getCity());
		accountInfo005.setProvincial(checkBankPo.getProvince());
		accountInfo005.setNameOfBank(checkBankPo.getBankOutlets());
		accountInfo005.setTelephone(checkBankPo.getMobile()==null?"":checkBankPo.getMobile());
		accountInfo005.setClientType("4");//??????????????????????????????
		accountInfo005.setClientNo(checkBankPo.getId()==null?"":checkBankPo.getId().toString());
		accountInfo005.setClientName(checkBankPo.getAccountName());
		accountInfo005.setAccountName(checkBankPo.getAccountName());
		accountInfo005.setAccountType("1");
		accountInfo005.setDefaultFlag("1");
		accountInfo005.setCreateCode(checkBankPo.getCreateUser());
		accountInfo005.setCreateBranch(userVo.getComCode());
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
		accountInfo005.setCreateDate(dateFormater.format(checkBankPo.getCreateTime()));
		accountInfo005.setAccType("2"); //?????????????????????????????????
		accountInfo005.setValidStatus("1");
		Prpcar004ResDto dto = bankAccountService.SaveOrUpdateAccount(accountInfo005,userVo);
		return dto.getBody().getAccountInfo().getAccountNo();
	}

	/**
	 * ??????????????????id?????????????????????????????????
	 * <pre></pre>
	 * @param valueOf
	 * @param string
	 * @return
	 * @modified:
	 * ???XiaoHuYao(2019???8???2??? ??????9:27:03): <br>
	 */
	private List<PrpdcheckBank> findPrpdcheckBankByParams(String checkMainId,String validFlag) {
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("vaildFlag",validFlag);
		queryRule.addEqual("prpdCheckBankMain.id",Long.valueOf(checkMainId));
		List<PrpdcheckBank>  banks=databaseDao.findAll(PrpdcheckBank.class,queryRule);
		return banks;
	}

	/* 
	 * @see ins.sino.claimcar.checkagency.service.CheckAgencyService#saveOrUpdateCheckServer(ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo, java.util.List)
	 * @param recheckMainVo
	 * @param checkServerVos
	 */
	@Override
	public void saveOrUpdateCheckServer(PrpdCheckBankMainVo recheckMainVo,List<PrpdcheckServerVo> checkServerVos) {
		Date date = new Date();// ????????????
		String user = ServiceUserUtils.getUserCode();// ????????????
		Date crDate = new Date();
		String crUser = ServiceUserUtils.getUserCode();
		PrpdCheckBankMain checkBankMainPo = Beans.copyDepth().from(recheckMainVo)
				.to(PrpdCheckBankMain.class);
		for (PrpdcheckServerVo checkServerVo : checkServerVos) {
			/*intermServerVo.setCreateTime(crDate);
			intermServerVo.setCreateUser(crUser);*/
			checkServerVo.setUpdateUser(user);
			checkServerVo.setUpdateTime(date);

			if (checkServerVo.getId() != null) {
				crDate = checkServerVo.getCreateTime();// ???????????????????????????????????????????????????update????????????
				crUser = checkServerVo.getCreateUser();
				checkServerVo.setCreateTime(crDate);
				checkServerVo.setCreateUser(crUser);

				PrpdcheckServer checkServerPo = Beans.copyDepth()
						.from(checkServerVo).to(PrpdcheckServer.class);
				// ?????????
				checkServerPo.setPrpdCheckBankMain(checkBankMainPo);
				databaseDao.update(PrpdcheckServer.class, checkServerPo);

			} else {
				PrpdcheckServer checkServerPo = Beans.copyDepth()
						.from(checkServerVo).to(PrpdcheckServer.class);
				// ?????????
				checkServerPo.setPrpdCheckBankMain(checkBankMainPo);

				databaseDao.save(PrpdcheckServer.class, checkServerPo);
			}
		}
		
	}

	@Override
	public Page<PrpdCheckBankMainVo> find(PrpdCheckBankMainVo checkBankMainVo,int start,int length) {
		// ????????????list???ps?????????????????????????????????object??????
		List<Object> paramValues = new ArrayList<Object>();
		// hql????????????
		StringBuffer queryString = new StringBuffer("from PrpdCheckBankMain pf where 1=1 ");
		if (StringUtils.isNotBlank(checkBankMainVo.getCheckCode())) {
			queryString.append(" AND pf.checkCode = ? ");
			paramValues.add(checkBankMainVo.getCheckCode());
		}
		if (StringUtils.isNotBlank(checkBankMainVo.getCheckName())) {
			queryString.append(" AND pf.checkName like ? ");
			paramValues.add("%" + checkBankMainVo.getCheckName() + "%");
		}
		if(StringUtils.isNotBlank(checkBankMainVo.getComCode())){
			queryString.append(" AND pf.comCode = ? ");
			paramValues.add(checkBankMainVo.getComCode());
		}		
		queryString.append(" Order By pf.id ");
		
		// ????????????
		Page page = databaseDao.findPageByHql(queryString.toString(), (start/ length + 1), length, paramValues.toArray());
		
		Page<PrpdCheckBankMainVo> pageReturn = this.assemblyPolicyInfo(page);

		return pageReturn;
	}
	
	/**
	 * ??????page??????
	 * 
	 * @param page
	 * @param prpdIntermMainVo
	 * @return
	 */
	private Page assemblyPolicyInfo(Page page) {
		for (int i = 0; i < page.getResult().size(); i++) {
			PrpdCheckBankMainVo plyVo = new PrpdCheckBankMainVo();
			PrpdCheckBankMain pm =  (PrpdCheckBankMain)page.getResult().get(i);
			plyVo = Beans.copyDepth().from(pm).to(PrpdCheckBankMainVo.class);
			page.getResult().set(i,plyVo);
		}
		return page;
	}

	@Override
	public String existcheckCode(String checkCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("checkCode", checkCode);
		queryRule.addEqual("validStatus", "1");
		List<PrpdCheckBankMain> checkBankList = null;
		checkBankList = databaseDao.findAll(
				PrpdCheckBankMain.class, queryRule);
		if(checkBankList != null && checkBankList.size() > 0){
			return "1";
		}else{
			return "0";
		}
	}



	/* 
	 * @see ins.sino.claimcar.checkagency.service.CheckAgencyService#findPrpdcheckBankVosBycheckId(java.lang.String)
	 * @param itermMianId
	 * @return
	 */
	@Override
	public List<PrpdcheckBankVo> findPrpdcheckBankVosBycheckId(String checkMainId) {
		QueryRule query=QueryRule.getInstance();
		query.addEqual("prpdCheckBankMain.id",Long.valueOf(checkMainId));
		query.addDescOrder("createTime");
		List<PrpdcheckBank> bankPos=databaseDao.findAll(PrpdcheckBank.class,query);
		List<PrpdcheckBankVo> bankVos=new ArrayList<PrpdcheckBankVo>();
		if(bankPos!=null && bankPos.size()>0){
			for(PrpdcheckBank po :bankPos){
				PrpdcheckBankVo vo=new PrpdcheckBankVo();
				Beans.copy().from(po).to(vo);
				bankVos.add(vo);
			}
		}
		return bankVos;
	}

	@Override
	public PrpdcheckBankVo findPrpdcheckBankVosBycheckMainIdAndAccountNo(Long checkMainId,String accountNo) {
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("accountNo",accountNo);
		queryRule.addEqual("prpdCheckBankMain.id",checkMainId);
		List<PrpdcheckBank>  banks=databaseDao.findAll(PrpdcheckBank.class,queryRule);
		PrpdcheckBankVo vo=new PrpdcheckBankVo();
		if(banks!=null && banks.size()>0 ){
			for(PrpdcheckBank po:banks){
			  Beans.copy().from(po).to(vo);
						break;
						
			}
		}
		return vo;
	}

	/* 
	 * @see ins.sino.claimcar.checkagency.service.CheckAgencyService#existAccountAtIntermBank(java.lang.String)
	 * @param accountNo
	 * @return
	 */
	@Override
	public String existAccountAtIntermBank(String accountNo) {
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("vaildFlag",CodeConstants.CommonConst.TRUE);
		queryRule.addEqual("accountNo",accountNo);
		List<PrpdIntermBank>  banks=databaseDao.findAll(PrpdIntermBank.class,queryRule);
		if(banks != null && !banks.isEmpty()){
			return "???????????????????????????"+ banks.get(0).getPrpdIntermMain().getIntermName() +"??????????????????";
		}
		return null;
	}



	
	
}
