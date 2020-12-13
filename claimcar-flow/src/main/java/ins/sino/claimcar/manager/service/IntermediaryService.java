package ins.sino.claimcar.manager.service;

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
import ins.sino.claimcar.manager.po.PrpdIntermBank;
import ins.sino.claimcar.manager.po.PrpdIntermMain;
import ins.sino.claimcar.manager.po.PrpdIntermServer;
import ins.sino.claimcar.manager.po.PrpdIntermUser;
import ins.sino.claimcar.manager.po.PrpdcheckBank;
import ins.sino.claimcar.manager.po.PrpdcheckUser;
import ins.sino.claimcar.manager.vo.PrpdIntermBankVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermServerVo;
import ins.sino.claimcar.manager.vo.PrpdIntermUserVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinosoft.arch5service.dto.prpcar004.Prpcar004ResDto;
import com.sinosoft.arch5service.dto.prpcar005.AccountInfo005;


/**
 * @author lanlei
 *
 */
@Service("intermediaryService")
public class IntermediaryService {

	private Logger logger = LoggerFactory.getLogger(IntermediaryService.class);

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private BankAccountService bankAccountService;
	
	@Autowired
	BaseDaoService baseDaoService;
	

	/**
	 * 保存或修改公估主表和公估银行表信息
	 * 
	 * @param intermMainVo
	 * @param intermBankVo
	 * @param session
	 * @return
	 */
	public PrpdIntermMainVo saveOrUpdateInterm(PrpdIntermMainVo intermMainVo,
			PrpdIntermBankVo intermBankVo, List<PrpdIntermUserVo> intermUserVos,SysUserVo userVo) throws Exception {
		Date date = new Date();// 获取时间
		String user = ServiceUserUtils.getUserCode();// 获取用户
		Date crDate = new Date();
		String crUser = ServiceUserUtils.getUserCode();
		
		intermMainVo.setUpdateUser(user);
		intermMainVo.setUpdateTime(date);
		intermMainVo.setCreateTime(crDate);
		intermMainVo.setCreateUser(crUser);

		PrpdIntermMain intermMainPo = null;

		PrpdIntermBank intermBankPo = Beans.copyDepth().from(intermBankVo).to(PrpdIntermBank.class);
		intermBankPo.setUpdateUser(user);
		intermBankPo.setUpdateTime(date);
		intermBankPo.setCreateTime(crDate);
		intermBankPo.setCreateUser(crUser);
		// 对接收付
		String accountId = null;
		try{
			// accountId = this.SaveOrUpdateIntermBankAccount(intermBankPo,userVo);
		}catch(Exception e){
			logger.error("收付对接失败",e);
			throw new IllegalArgumentException("收付对接失败！");
		}
		intermBankPo.setAccountId(accountId);
		
		if (intermMainVo.getId() != null) {// 更新
			intermMainPo = databaseDao.findByPK(PrpdIntermMain.class, intermMainVo.getId());
			List<PrpdIntermUser> intermUserDatas = intermMainPo.getPrpdIntermUsers();
			List<PrpdIntermBank> banks=this.findPrpdIntermBankVosByIntermIdAndVaildFlag(String.valueOf(intermMainVo.getId()),"0");
			
			Beans.copy().from(intermMainVo).excludeEmpty().to(intermMainPo);
			List<PrpdIntermBank> intermBankList = new ArrayList<PrpdIntermBank>();
			// bank
			if(banks!=null && banks.size()>0){
				for(PrpdIntermBank bank:banks){
					bank.setPrpdIntermMain(intermMainPo);
					intermBankList.add(bank);
				}
			}
			intermBankPo.setPrpdIntermMain(intermMainPo);
			
			intermBankList.add(intermBankPo);
			intermMainPo.setPrpdIntermBanks(intermBankList);

//			List<PrpdIntermUser> intermUserPoList = new ArrayList<PrpdIntermUser>(); //
			// 判断执行的操作 保存 、 修改 、还是删除
			if (intermUserDatas != null && !intermUserDatas.isEmpty()) {
				for (Iterator it = intermUserDatas.iterator(); it.hasNext();) {
					PrpdIntermUser intermUserData = (PrpdIntermUser)it.next();
					Boolean flag = true;
					if (intermUserVos != null && !intermUserVos.isEmpty()) {
						for (PrpdIntermUserVo intermUserVo : intermUserVos) {
							if ((intermUserVo != null) && (intermUserVo.getId() != null)&&(intermUserData.getId().longValue() == intermUserVo.getId().longValue())) { // 页面存在id
								flag = false;
							}
						}
					}
					if (flag) {
						databaseDao.deleteByPK(PrpdIntermUser.class,intermUserData.getId());
						it.remove();
					}
				}
			}
			if (intermUserVos != null && !intermUserVos.isEmpty()) {
				for (PrpdIntermUserVo intermUserVo : intermUserVos) {
					if(intermUserVo != null && intermUserVo.getId() == null){
						//判断公估人员是否在查勘人员表和公估人员表存在
						QueryRule checkUserRule =  QueryRule.getInstance();
						checkUserRule.addEqual("userCode", intermUserVo.getUserCode());
						List<PrpdcheckUser> checkUserPoList = databaseDao.findAll(PrpdcheckUser.class, checkUserRule);
						if(checkUserPoList != null && !checkUserPoList.isEmpty()){
							 throw new IllegalArgumentException("公估人员  : "+intermUserVo.getUserCode()+"-"+intermUserVo.getUserName()+"<br/>已被维护在： "
							+checkUserPoList.get(0).getPrpdCheckBankMain().getCheckCode()+"-"+checkUserPoList.get(0).getPrpdCheckBankMain().getCheckName()+" 查勘机构下<br/>请核实！");
										
						}
						QueryRule rule = QueryRule.getInstance();
						rule.addEqual("userCode", intermUserVo.getUserCode());
						List<PrpdIntermUser>	userPoList = databaseDao.findAll(PrpdIntermUser.class, rule);
						if(userPoList != null && userPoList.size() > 0){
							 throw new IllegalArgumentException("公估人员  : "+intermUserVo.getUserCode()+"-"+intermUserVo.getUserName()+"<br/>已被维护在： "
						+userPoList.get(0).getPrpdIntermMain().getIntermCode()+"-"+userPoList.get(0).getPrpdIntermMain().getIntermName()+" 公估下<br/>请核实！");
						}
						PrpdIntermUser intermUserPo = new PrpdIntermUser();
						intermUserPo = Beans.copyDepth().from(intermUserVo).to(PrpdIntermUser.class);
						intermUserPo.setUpdateUser(user);
						intermUserPo.setUpdateTime(date);
						intermUserPo.setCreateUser(crUser);
						intermUserPo.setCreateTime(crDate);
						intermUserPo.setPrpdIntermMain(intermMainPo);
						intermUserDatas.add(intermUserPo);
					}
				}
			}
			databaseDao.update(PrpdIntermMain.class, intermMainPo);
		} else {// 新增

			intermMainPo = new PrpdIntermMain();
			intermMainPo = Beans.copyDepth().from(intermMainVo).to(PrpdIntermMain.class);
			// 主子表关联
			intermBankPo.setPrpdIntermMain(intermMainPo);

			List<PrpdIntermBank> intermBankList = new ArrayList<PrpdIntermBank>();
			intermBankList.add(intermBankPo);
			intermMainPo.setPrpdIntermBanks(intermBankList);

			// user
			List<PrpdIntermUser> intermUserPoList = new ArrayList<PrpdIntermUser>(); //
			if (intermUserVos != null && !intermUserVos.isEmpty()) {
				for (PrpdIntermUserVo intermUserVo : intermUserVos) {
					if(intermUserVo != null){
						//判断公估人员是否在查勘人员表和公估人员表存在
						QueryRule checkUserRule =  QueryRule.getInstance();
						checkUserRule.addEqual("userCode", intermUserVo.getUserCode());
						List<PrpdcheckUser> checkUserPoList = databaseDao.findAll(PrpdcheckUser.class, checkUserRule);
						if(checkUserPoList != null && !checkUserPoList.isEmpty()){
							 throw new IllegalArgumentException("公估人员  : "+intermUserVo.getUserCode()+"-"+intermUserVo.getUserName()+"<br/>已被维护在： "
							+checkUserPoList.get(0).getPrpdCheckBankMain().getCheckCode()+"-"+checkUserPoList.get(0).getPrpdCheckBankMain().getCheckName()+" 查勘机构下<br/>请核实！");
										
						}
						QueryRule rule = QueryRule.getInstance();
						rule.addEqual("userCode", intermUserVo.getUserCode());
						List<PrpdIntermUser> userPoList = databaseDao.findAll(PrpdIntermUser.class, rule);
						if(userPoList != null && userPoList.size() > 0){
							throw new IllegalArgumentException("公估人员  : "+intermUserVo.getUserCode()+"-"+intermUserVo.getUserName()+"<br/>已被维护在： "
									+userPoList.get(0).getPrpdIntermMain().getIntermCode()+"-"+userPoList.get(0).getPrpdIntermMain().getIntermName()+" 公估下<br/>请核实！");
						}
						PrpdIntermUser intermUserPo = new PrpdIntermUser();
						intermUserPo = Beans.copyDepth().from(intermUserVo).to(PrpdIntermUser.class);
						intermUserPo.setUpdateUser(user);
						intermUserPo.setUpdateTime(date);
						intermUserPo.setCreateUser(crUser);
						intermUserPo.setCreateTime(crDate);
						intermUserPo.setPrpdIntermMain(intermMainPo);
						intermUserPoList.add(intermUserPo);
					}
				}
			}
			intermMainPo.setPrpdIntermUsers(intermUserPoList);

			databaseDao.save(PrpdIntermMain.class, intermMainPo);
		}
		// 将获取的承保主表信息，复制到reIntermMainVo对象中,方便return
		PrpdIntermMainVo reIntermMainVo = Beans.copyDepth().from(intermMainPo).to(PrpdIntermMainVo.class);
		reIntermMainVo.setId(intermMainPo.getId());
		return reIntermMainVo;

	}
	
	/**
	 * 对接BankAccount
	 * <pre></pre>
	 * @param intermBank
	 * @param userVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆LuLiang(2016年9月30日 下午3:45:07): <br>
	 */
	private String SaveOrUpdateIntermBankAccount(PrpdIntermBank intermBank,SysUserVo userVo) throws Exception{
		List<AccountInfo005> accountInfoList = bankAccountService.searchAccount(intermBank.getAccountNo(),null,userVo.getUserCode());
		
		AccountInfo005 accountInfo005 = null;
		if(accountInfoList!=null&&accountInfoList.size()>0){//更新
			accountInfo005 = accountInfoList.get(0);
		}else{//新增
			accountInfo005 = new AccountInfo005();
			accountInfo005.setAccountCode(intermBank.getAccountNo());
			accountInfo005.setCurrency("CNY");
			accountInfo005.setAccountName(intermBank.getAccountName());
		}
		accountInfo005.setBankCode(intermBank.getBankName());
		accountInfo005.setFlag(intermBank.getBankNumber());//银联号
		accountInfo005.setIdentifyType("10");//组织机构类型
		accountInfo005.setIdentifyNumber(intermBank.getCertifyNo());//组织机构代码
		accountInfo005.setCity(intermBank.getCity());
		accountInfo005.setProvincial(intermBank.getProvince());
		accountInfo005.setNameOfBank(intermBank.getBankOutlets());
		accountInfo005.setTelephone(intermBank.getMobile()==null?"":intermBank.getMobile());
		accountInfo005.setClientType("4");//收款人类型为公估机构
		accountInfo005.setClientNo(intermBank.getId()==null?"":intermBank.getId().toString());
		accountInfo005.setClientName(intermBank.getAccountName());
		accountInfo005.setAccountName(intermBank.getAccountName());
		accountInfo005.setAccountType("1");
		accountInfo005.setDefaultFlag("1");
		accountInfo005.setCreateCode(intermBank.getCreateUser());
		accountInfo005.setCreateBranch(userVo.getComCode());
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
		accountInfo005.setCreateDate(dateFormater.format(intermBank.getCreateTime()));
		accountInfo005.setAccType("2"); //公估机构默认送公司账户
		accountInfo005.setValidStatus("1");
		Prpcar004ResDto dto = bankAccountService.SaveOrUpdateAccount(accountInfo005,userVo);
		return dto.getBody().getAccountInfo().getAccountNo();
	}

	/**
	 * 公估服务表新增和更新
	 * 
	 * @param reIntermMainVo
	 * @param intermServerVo
	 * @param session
	 */
	public void saveOrUpdateIntermServer(PrpdIntermMainVo reIntermMainVo,
			List<PrpdIntermServerVo> intermServerVos) {

		Date date = new Date();// 获取时间
		String user = ServiceUserUtils.getUserCode();// 获取用户
		Date crDate = new Date();
		String crUser = ServiceUserUtils.getUserCode();
		PrpdIntermMain intermMainPo = Beans.copyDepth().from(reIntermMainVo)
				.to(PrpdIntermMain.class);
		for (PrpdIntermServerVo intermServerVo : intermServerVos) {
			/*intermServerVo.setCreateTime(crDate);
			intermServerVo.setCreateUser(crUser);*/
			intermServerVo.setUpdateUser(user);
			intermServerVo.setUpdateTime(date);

			if (intermServerVo.getId() != null) {
				crDate = intermServerVo.getCreateTime();// 判断为更新则保留创建时间和用户更新update时间用户
				crUser = intermServerVo.getCreateUser();
				intermServerVo.setCreateTime(crDate);
				intermServerVo.setCreateUser(crUser);

				PrpdIntermServer intermServerPo = Beans.copyDepth()
						.from(intermServerVo).to(PrpdIntermServer.class);
				// 主子表
				intermServerPo.setPrpdIntermMain(intermMainPo);
				databaseDao.update(PrpdIntermServer.class, intermServerPo);
				System.out.println("更新ser");

			} else {
				PrpdIntermServer intermServerPo = Beans.copyDepth()
						.from(intermServerVo).to(PrpdIntermServer.class);
				// 主子表
				intermServerPo.setPrpdIntermMain(intermMainPo);

				databaseDao.save(PrpdIntermServer.class, intermServerPo);
				System.out.println("保存ser");
			}
		}
	}

	/**
	 * 查询公估信息
	 * 
	 * @param intermMainVo
	 * @param start
	 * @param length
	 * @return
	 */
	public Page<PrpdIntermMainVo> find(PrpdIntermMainVo intermMainVo,
			int start, int length) {
		
		// 定义参数list，ps：执行查询时需要转换成object数组
		List<Object> paramValues = new ArrayList<Object>();
		// hql查询语句
		StringBuffer queryString = new StringBuffer("from PrpdIntermMain pf where 1=1 ");
		if (StringUtils.isNotBlank(intermMainVo.getIntermCode())) {
			queryString.append(" AND pf.intermCode = ? ");
			paramValues.add(intermMainVo.getIntermCode());
		}
		if (StringUtils.isNotBlank(intermMainVo.getIntermName())) {
			queryString.append(" AND pf.intermName like ? ");
			paramValues.add("%" + intermMainVo.getIntermName() + "%");
		}
		if(StringUtils.isNotBlank(intermMainVo.getComCode())){
			queryString.append(" AND pf.comCode = ? ");
			paramValues.add(intermMainVo.getComCode());
		}		
		queryString.append(" Order By pf.id ");
		
		// 执行查询
		Page page = databaseDao.findPageByHql(queryString.toString(), (start/ length + 1), length, paramValues.toArray());
		
		Page<PrpdIntermMainVo> pageReturn = this.assemblyPolicyInfo(page);

		return pageReturn;
	}

	/**
	 * 组装page数据
	 * 
	 * @param page
	 * @param prpdIntermMainVo
	 * @return
	 */
	private Page assemblyPolicyInfo(Page page) {

		for (int i = 0; i < page.getResult().size(); i++) {
			PrpdIntermMainVo plyVo = new PrpdIntermMainVo();
			PrpdIntermMain pm =  (PrpdIntermMain)page.getResult().get(i);
			plyVo = Beans.copyDepth().from(pm).to(PrpdIntermMainVo.class);
			page.getResult().set(i,plyVo);
		}
		return page;
	}

	/**
	 * 根据主键查询IntermMainVo
	 * 
	 * @param id
	 * @return
	 */
	public PrpdIntermMainVo findIntermById(String id) {
		long intermID = Long.parseLong(id);
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("id", intermID);
		List<PrpdIntermMain> intermMainPos = databaseDao.findAll(
				PrpdIntermMain.class, queryRule);
		// 获取当前序号下的保单相关对象数组
		PrpdIntermMainVo intermMainVo = new PrpdIntermMainVo();
		PrpdIntermMain intermMainPo = intermMainPos.get(0);
		// 复制到Vo对象中
		intermMainVo = Beans.copyDepth().from(intermMainPo).to(PrpdIntermMainVo.class);
		return intermMainVo;
	}

	/**
	 * 
	 * 通过公估代码查询公估信息 ☆yangkun(2016年1月21日 下午6:00:27): <br>
	 */
	public PrpdIntermMainVo findIntermByCode(String intermCode,String comCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("intermCode", intermCode);
		if(StringUtils.isBlank(comCode)){
			comCode = ServiceUserUtils.getComCode();
		}
		if(comCode.startsWith("00")){//深分
			comCode = comCode.substring(0, 4)+"0000";
		}else{
			comCode = comCode.substring(0, 2)+"000000";
		}
		queryRule.addEqual("comCode", comCode);

		List<PrpdIntermMain> intermList = databaseDao.findAll(
				PrpdIntermMain.class, queryRule);
		if (intermList == null || intermList.size() == 0) {
			return null;
		}
		PrpdIntermMainVo intermVo = Beans.copyDepth().from(intermList.get(0))
				.to(PrpdIntermMainVo.class);

		return intermVo;
	}

	/* 通过公估代码查询公估信息的list */
	public List<PrpdIntermMainVo> findIntermListByCode(String comCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("comCode", comCode);
		List<PrpdIntermMainVo> intermVoList = new ArrayList<PrpdIntermMainVo>();
		List<PrpdIntermMain> intermList = databaseDao.findAll(
				PrpdIntermMain.class, queryRule);
		if (intermList != null && intermList.size() > 0) {
			for (PrpdIntermMain prpdIntermMain : intermList) {
				PrpdIntermMainVo intermVo = Beans.copyDepth()
						.from(prpdIntermMain).to(PrpdIntermMainVo.class);
				intermVoList.add(intermVo);
			}
		}
		return intermVoList;
	}

	/* 通过公估代码模糊查询公估信息的list */
	public List<PrpdIntermMainVo> findIntermListByHql(String comCode) {
	
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" FROM PrpdIntermMain prpMain where 1=1");
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
		sqlUtil.append(" order by intermCode  ");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<PrpdIntermMain> intermList = databaseDao.findAllByHql(
				PrpdIntermMain.class, sql, values);
		List<PrpdIntermMainVo> intermVoList = new ArrayList<PrpdIntermMainVo>();
		if (intermList != null && intermList.size() > 0) {
			for (PrpdIntermMain prpdIntermMain : intermList) {
				PrpdIntermMainVo intermVo = Beans.copyDepth()
						.from(prpdIntermMain).to(PrpdIntermMainVo.class);
				intermVoList.add(intermVo);
			}
		}
		return intermVoList;
	}
	
	public List<PrpdIntermMainVo> findIntermListByComCodes(List<String> comCodes) {
		
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" FROM PrpdIntermMain prpMain where 1=1 and (");
		if(comCodes!=null && comCodes.size()>0){
			for(int i=0;i<comCodes.size();i++){
				if(i==comCodes.size()-1){
					sqlUtil.append(" prpMain.comCode like ? )");
				}else{
					sqlUtil.append(" prpMain.comCode like ? or ");
				}
				sqlUtil.addParamValue(comCodes.get(i) + "%");
			}
		}
		sqlUtil.append(" order by intermCode  ");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<PrpdIntermMain> intermList = databaseDao.findAllByHql(
				PrpdIntermMain.class, sql, values);
		List<PrpdIntermMainVo> intermVoList = new ArrayList<PrpdIntermMainVo>();
		if (intermList != null && intermList.size() > 0) {
			for (PrpdIntermMain prpdIntermMain : intermList) {
				PrpdIntermMainVo intermVo = Beans.copyDepth()
						.from(prpdIntermMain).to(PrpdIntermMainVo.class);
				intermVoList.add(intermVo);
			}
		}
		return intermVoList;
	}
	
	/**
	 * caseType=0是正常案件，1我方代查勘，2代我方查勘
	 * @param comCode
	 * @param caseType
	 * @return
	 */
	public List<PrpdIntermMainVo> findIntermListByCaseType(String comCode,String caseType){
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		List<PrpdIntermMainVo> intermMainVoList = new ArrayList<PrpdIntermMainVo>();
		
		sqlUtil.append(" select distinct m.id,m.intermCode,m.intermname  from PrpdIntermMain m  where ");
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
				PrpdIntermMainVo intermMainVo = new PrpdIntermMainVo();
				intermMainVo.setId(Long.valueOf(obj[0].toString()));
				intermMainVo.setIntermCode((String)obj[1]);
				intermMainVo.setIntermName((String)obj[2]);
				intermMainVoList.add(intermMainVo);
			}
		}
		return intermMainVoList;
	}

	/**
	 * 通过机构代码查询公估信息 ☆(2016年1月21日 下午6:00:27): <br>
	 */
	public PrpdIntermMainVo findIntermByComCode(String comCode) {
		PrpdIntermMainVo intermVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("comCode", comCode);
		queryRule.addEqual("validStatus", "1");
		List<PrpdIntermMain> intermList = databaseDao.findAll(
				PrpdIntermMain.class, queryRule);
		if (intermList != null && intermList.size() > 0) {
			intermVo = new PrpdIntermMainVo();
			intermVo = Beans.copyDepth().from(intermList.get(0))
					.to(PrpdIntermMainVo.class);
		}
		return intermVo;
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
	
	public Map<String, String> findUserCode(String comCode, String userInfo,
			String gradeId) {

		Map<String, String> resultMap = new HashMap<String, String>();
		StringBuffer querySb = new StringBuffer();
		List<String> paramValues = new ArrayList<String>();
		querySb.append(" SELECT userCode,userName FROM ywuser.PrpDuser WHERE 1=1 and VALIDSTATUS=?");
		paramValues.add("1");
		if (StringUtils.isNotBlank(userInfo)) {
			querySb.append("  AND (userCode like ? OR userName like ?)");
			paramValues.add("%" + userInfo + "%");
			paramValues.add("%" + userInfo + "%");
		}
		if (StringUtils.isNotBlank(comCode)) {
			querySb.append("  AND comCode like ? ");
			paramValues.add(comCode + "%");
		}
		if (StringUtils.isNotBlank(gradeId)) {
			querySb.append("  AND userCode in (SELECT usercode FROM saa_usergrade WHERE gradeid = ?)");
			paramValues.add(gradeId);
		}
		List<Object[]> objList = baseDaoService.getAllBySql(querySb.toString(), paramValues.toArray());

		
//		List<Object[]> objList = databaseDao.findRangeBySql(querySb.toString(),
//				0, 3000, paramValues.toArray());

		for (Object[] result : objList) {
			if(result[1]!=null){
				resultMap.put((String) result[0], (String) result[1]);
			}
			
		}
		return resultMap;
	}
	
	public String existIntermCode(String intermCode){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("intermCode", intermCode);
		queryRule.addEqual("validStatus", "1");
		List<PrpdIntermMain> intermList = null;
		intermList = databaseDao.findAll(
				PrpdIntermMain.class, queryRule);
		if(intermList != null && intermList.size() > 0){
			return "1";
		}else{
			return "0";
		}
	}
	
	public PrpdIntermBankVo findPrpdIntermBankVoById(Long id){
		PrpdIntermBank prpdIntermBank = databaseDao.findByPK(PrpdIntermBank.class, id);
		
		PrpdIntermBankVo vo =new PrpdIntermBankVo();
		if(prpdIntermBank != null){
			Beans.copy().from(prpdIntermBank).to(vo);
			vo.setIntermMianId(prpdIntermBank.getPrpdIntermMain().getId());
		}
		return vo;
	}
	
	public List<PrpdIntermBankVo> findPrpdIntermBankVosByIntermId(String itermMianId) {
		QueryRule query=QueryRule.getInstance();
		query.addEqual("prpdIntermMain.id",Long.valueOf(itermMianId));
		query.addDescOrder("createTime");
		List<PrpdIntermBank> bankPos=databaseDao.findAll(PrpdIntermBank.class,query);
		List<PrpdIntermBankVo> bankVos=new ArrayList<PrpdIntermBankVo>();
		if(bankPos!=null && bankPos.size()>0){
			for(PrpdIntermBank po :bankPos){
				PrpdIntermBankVo vo=new PrpdIntermBankVo();
				Beans.copy().from(po).to(vo);
				bankVos.add(vo);
			}
		}
		return bankVos;
	}
	
	/**
	 * 查本公估机构下是否有相同的银行账号
	 * @param intermCode
	 * @param accountNo
	 * @return
	 */
	public PrpdIntermBankVo findPrpdIntermBankVosByIntermMainIdAndAccountNo(Long intermMainId,String accountNo) {
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("accountNo",accountNo);
		queryRule.addEqual("prpdIntermMain.id",intermMainId);
		List<PrpdIntermBank>  banks=databaseDao.findAll(PrpdIntermBank.class,queryRule);
		PrpdIntermBankVo vo=new PrpdIntermBankVo();
		if(banks!=null && banks.size()>0 ){
			for(PrpdIntermBank po:banks){
			  Beans.copy().from(po).to(vo);
						break;
						
			}
		}
		return vo;
	}
	
	public PrpdIntermMainVo findIntermById(Long id){
		PrpdIntermMain prpdIntermMain = databaseDao.findByPK(PrpdIntermMain.class,id);
		
		PrpdIntermMainVo vo =new PrpdIntermMainVo();
		if(prpdIntermMain != null){
			vo=Beans.copyDepth().from(prpdIntermMain).to(PrpdIntermMainVo.class);
		}
		return vo;
	}
	
	public List<PrpdIntermBank> findPrpdIntermBankVosByIntermIdAndVaildFlag(String itermMianId, String vaildFlag){
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("vaildFlag",vaildFlag);
		queryRule.addEqual("prpdIntermMain.id",Long.valueOf(itermMianId));
		List<PrpdIntermBank>  banks=databaseDao.findAll(PrpdIntermBank.class,queryRule);
		
		
		return banks;
	}
	

	public String existAccountAtCheckmBank(String accountNo) {
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("vaildFlag",CodeConstants.CommonConst.TRUE);
		queryRule.addEqual("accountNo",accountNo);
		List<PrpdcheckBank>  banks=databaseDao.findAll(PrpdcheckBank.class,queryRule);
		if(banks != null && !banks.isEmpty()){
			return "该银行账号已经存在"+ banks.get(0).getPrpdCheckBankMain().getCheckName() +"查勘机构下。";
		}
		return null;
	}
}
