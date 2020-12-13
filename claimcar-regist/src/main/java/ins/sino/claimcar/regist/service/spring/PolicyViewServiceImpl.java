/******************************************************************************
 * CREATETIME : 2015年12月1日 下午6:46:52
 ******************************************************************************/
package ins.sino.claimcar.regist.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.exception.BusinessException;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.regist.po.PrpCItemCar;
import ins.sino.claimcar.regist.po.PrpCMain;
import ins.sino.claimcar.regist.po.PrpCMainSub;
import ins.sino.claimcar.regist.po.PrpLCItemCar;
import ins.sino.claimcar.regist.po.PrpLCItemKind;
import ins.sino.claimcar.regist.po.PrpLCMain;
import ins.sino.claimcar.regist.po.PrpLTmpCMain;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PolicyEndorseInfoVo;
import ins.sino.claimcar.regist.vo.PolicyInfoVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2015年12月1日
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path("policyViewService")
public class PolicyViewServiceImpl implements PolicyViewService {
	
	private static final Log logger = LogFactory.getLog(PolicyViewServiceImpl.class);
	
	@Autowired
	DatabaseDao databaseDao;
	
	@Autowired
	BaseDaoService baseDaoService;
	

	@Override
	public List<PrpLCMainVo> getPolicyAllInfo(String registNo) {
		// 创建返回对象列表
		List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
		//HQL
		StringBuffer queryString = new StringBuffer("from PrpLCMain t where t.registNo = ? and t.validFlag= ? ");
		// 查询参数
		Object[] paramValues = {registNo, "1"};
		// 执行查询
		List<PrpLCMain> prpLCMainList =  databaseDao.findAllByHql(PrpLCMain.class, queryString.toString(), paramValues);
		for(int i = 0;i < prpLCMainList.size();i ++){
			PrpLCMainVo prpLCMainVo = new PrpLCMainVo();
			PrpLCMain prpLCMain = prpLCMainList.get(i);
			// po转vo
			prpLCMainVo = Beans.copyDepth().from(prpLCMain).to(PrpLCMainVo.class);
			if(!"1101".equals(prpLCMainVo.getRiskCode())){//商业险
				for(PrpLCItemKindVo prpLCItemKindVo:prpLCMainVo.getPrpCItemKinds()){
					prpLCItemKindVo.setNoDutyFlag("0");
					if(!"Y".equals(prpLCItemKindVo.getCalculateFlag())){
						if(!prpLCItemKindVo.getKindCode().endsWith("M") && 
								prpLCItemKindVo.getFlag().length() > 4 && 
								"1".equals(prpLCItemKindVo.getFlag().substring(4,5))){
							prpLCItemKindVo.setNoDutyFlag("1");
						}
					}else{//主险
						if(prpLCItemKindVo.getFlag().length() > 4 && 
								"1".equals(prpLCItemKindVo.getFlag().substring(4,5))){
							prpLCItemKindVo.setNoDutyFlag("1");
						}
					}
				}
			}
			prpLCMainVoList.add(prpLCMainVo);
			
		}
				
		return prpLCMainVoList;
	}
	
	
	/**
	 * <pre>查询批单纪录</pre>
	 * @param registNo
	 * @modified:
	 * ☆Luwei(2016年9月6日 下午6:15:46): <br>
	 */
	@Override
	public List<PolicyEndorseInfoVo> findPolicyEndorseInfo(String registNo) {
		// SQL
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("SELECT * FROM prpphead t WHERE 1=1 ");
		sqlUtil.append("AND  policyNo in (select c.policyNo from prplcmain c where c.registNo = ?)");
		sqlUtil.addParamValue(registNo);
		
		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		
		// 对象转换
		List<PolicyEndorseInfoVo> infoVoList = new ArrayList<PolicyEndorseInfoVo>();
		for(int i = 0; i<objects.size(); i++ ){

			Object[] obj = objects.get(i);

			PolicyEndorseInfoVo resultVo = new PolicyEndorseInfoVo();
			
			resultVo.setSerialNo(i+1);
			resultVo.setEndorseNo(obj[0]==null ? "" : obj[0].toString());
			resultVo.setPolicyNo(obj[1]==null ? "" : obj[1].toString());
			resultVo.setEndorseTimes(obj[5]==null ? "" : obj[5].toString());
			
			infoVoList.add(resultVo);
		}
		
		return infoVoList;
	}
	
	@Override
	public List<PolicyEndorseInfoVo> findPrpPheadAndPrpPmainInfo(String policyNo) {
		// SQL
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
//select a.endorseno,a.endordate,b.chgamount,b.chgpremium from ywuser.PrpPhead a,ywuser.PrpPmain b where a.endorseno=b.endorseno and a.endortype!='56'		
		sqlUtil.append("SELECT t.endorseno,t.endordate,b.chgamount,b.chgpremium FROM prpphead t ,PrpPmain b WHERE 1=1 ");
		sqlUtil.append("AND t.endorseno=b.endorseno AND t.endortype!='56' AND t.policyNo=? ");
		sqlUtil.addParamValue(policyNo);
		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		// 对象转换
		List<PolicyEndorseInfoVo> infoVoList = new ArrayList<PolicyEndorseInfoVo>();
		for(int i = 0; i<objects.size(); i++ ){
			Object[] obj = objects.get(i);
			PolicyEndorseInfoVo resultVo = new PolicyEndorseInfoVo(); 
			resultVo.setEndorseNo(obj[0]==null ? "" : obj[0].toString());
			resultVo.setEndorDate(obj[1]==null ? "" : obj[1].toString());
			resultVo.setChgAmount(obj[2]==null ? "" : obj[2].toString());
			resultVo.setChgPremium(obj[3]==null ? "" : obj[3].toString());
			infoVoList.add(resultVo);
		}
		
		return infoVoList;
	}
	
	@Override
	public List<PrpLCItemKindVo> findItemKinds(String registNo,String calculateFlag) {
		// TODO Auto-generated method stub
		List<PrpLCItemKindVo> itemKindVoList = new ArrayList<PrpLCItemKindVo>(); 
		QueryRule queryRule = QueryRule.getInstance();
		if(StringUtils.isNotBlank(calculateFlag)){
			queryRule.addEqual("calculateFlag",calculateFlag);
		}
		queryRule.addEqual("registNo",registNo);
		queryRule.addDescOrder("calculateFlag");
		//queryRule.addEqual("validFlag","1");
		
		List<PrpLCItemKind> itemKindList = databaseDao.findAll(PrpLCItemKind.class,queryRule);
		for(PrpLCItemKind itemKind : itemKindList){
			PrpLCItemKindVo itemKindVo = Beans.copyDepth().from(itemKind).to(PrpLCItemKindVo.class);
			itemKindVoList.add(itemKindVo);
		}
		return itemKindVoList;
	}
	
	public PrpLCItemKindVo findItemKindByKindCode(String registNo,String kindCode){
		PrpLCItemKind itemKind=new PrpLCItemKind();
		QueryRule queryRule = QueryRule.getInstance();
		
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("kindCode", kindCode);
		itemKind=databaseDao.findUnique(PrpLCItemKind.class,queryRule);
		if(itemKind ==null){
			return null;
		}
		PrpLCItemKindVo itemKindVo = Beans.copyDepth().from(itemKind).to(PrpLCItemKindVo.class);
		return itemKindVo;
	}

	/**
	 * 根据报案号和保单号取数据
	 * 
	 * <pre></pre>
	 * @param registNo 报案号
	 * @param policyNo 保单号
	 * @return
	 * @modified: ☆ZhouYanBin(2016年3月17日 上午11:54:38): <br>
	 */
	@Override
	public PrpLCMainVo getPrpLCMainByRegistNoAndPolicyNo(String registNo,String policyNo) {
		// 取有效立案信息
		if(registNo==null||policyNo==null){
			return null;
		}

		PrpLCMainVo prpLCMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("policyNo",policyNo);
		queryRule.addEqual("validFlag","1");

		PrpLCMain prpLCMain = databaseDao.findUnique(PrpLCMain.class,queryRule);
		if (prpLCMain != null) {
			prpLCMainVo = Beans.copyDepth().from(prpLCMain).to(PrpLCMainVo.class);
		}
		return prpLCMainVo;
	}

	/**
	 * 根据报案号和保单号取险别信息
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @param policyNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年3月23日 下午3:32:44): <br>
	 */
	@Override
	public List<PrpLCItemKindVo> findItemKindVoListByRegistNoAndPolicyNo(String registNo,String policyNo) {
		List<PrpLCItemKindVo> prpLCItemKindVoList = new ArrayList<PrpLCItemKindVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		if(StringUtils.isNotBlank(policyNo)){
			queryRule.addEqual("policyNo",policyNo);
		}
		List<PrpLCItemKind> prpLCItemKindList = databaseDao.findAll(PrpLCItemKind.class,queryRule);
		if(prpLCItemKindList!=null&& !prpLCItemKindList.isEmpty()){
			for(PrpLCItemKind prpLCItemKind:prpLCItemKindList){
				PrpLCItemKindVo prpLCItemKindVo = Beans.copyDepth().from(prpLCItemKind).to(PrpLCItemKindVo.class);
				prpLCItemKindVoList.add(prpLCItemKindVo);
			}
		}
		return prpLCItemKindVoList;
	}

	@Override
	public List<PrpLCMainVo> findPrpLCMainVoListByRegistNo(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLCMain> prpLCMainList = databaseDao.findAll(PrpLCMain.class, queryRule);
		List<PrpLCMainVo> prpLCMainVoList = null;
		if(prpLCMainList != null){
			prpLCMainVoList = new ArrayList<PrpLCMainVo>();
			for(PrpLCMain prpLCMain:prpLCMainList){
				PrpLCMainVo prpLCMainVo = Beans.copyDepth().from(prpLCMain).to(PrpLCMainVo.class);
				prpLCMainVoList.add(prpLCMainVo);
			}
		}
		return prpLCMainVoList;
	}

	@Override
	public PrpLCMainVo getPolicyInfo(String registNo,String policyNo) {
		PrpLCMainVo prpLCMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("policyNo",policyNo);
		queryRule.addEqual("validFlag","1");
		List<PrpLCMain> prpLCMain = databaseDao.findAll(PrpLCMain.class,queryRule);
		if(prpLCMain!=null&&prpLCMain.size()>0){
			prpLCMainVo = Beans.copyDepth().from(prpLCMain.get(0)).to(PrpLCMainVo.class);
		}
		return prpLCMainVo;
	}
	
	@Override
	public PrpLCMainVo findPolicyInfoByPaltform(String registNo,String policyNo) {
		PrpLCMainVo prpLCMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("policyNo",policyNo);
		List<PrpLCMain> prpLCMain = databaseDao.findAll(PrpLCMain.class,queryRule);
		if(prpLCMain!=null&&prpLCMain.size()>0){
			prpLCMainVo = new PrpLCMainVo();
			prpLCMainVo = Beans.copyDepth().from(prpLCMain.get(0)).to(PrpLCMainVo.class);
		}
		return prpLCMainVo;
	}
	
	//根据立案号跟报案号查询报案信息
	public PrpLCMainVo getRegistNoInfo(String registNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		//queryRule.addEqual("claimNo",claimNo);
		
		PrpLCMain prpLCMain = databaseDao.findAll(PrpLCMain.class,queryRule).get(0);
		PrpLCMainVo prpLCMainVo = new PrpLCMainVo();
		if(prpLCMain!=null){
			prpLCMainVo = Beans.copyDepth().from(prpLCMain).to(PrpLCMainVo.class);
		}
		
		return prpLCMainVo;
	}


	/**
	 * 判断保单是否包含此险种代码
	 * <pre></pre>
	 * @param prpLCMainVo 
	 * @param kindCode 险种代码
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月14日 上午9:13:07): <br>
	 */
	@Override
	public boolean isInsuredKindCode(PrpLCMainVo prpLCMainVo,String kindCode) {
		boolean flag = false;
		if(prpLCMainVo == null || prpLCMainVo.getPrpCItemKinds() == null){
			return false;
		}
		
		for(PrpLCItemKindVo prpLCItemKindVo:prpLCMainVo.getPrpCItemKinds()){
			if(kindCode.equals(prpLCItemKindVo.getKindCode().trim())){
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 该险别是否承保了不计免赔险
	 * <pre></pre>
	 * @param registNo 案件号
	 * @param kindCode 险别代码
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月14日 下午6:04:11): <br>
	 */
	@Override
	public boolean isMKindCode(String registNo,String kindCode) {
		boolean flag = false;
		List<PrpLCItemKindVo> prpLCItemKindVoList = this.findItemKindVoListByRegistNoAndPolicyNo(registNo,null);
		if(prpLCItemKindVoList == null || prpLCItemKindVoList.isEmpty()){
			logger.error("案件没有对应的险别信息，报案号：" + registNo);
			throw new BusinessException("案件没有对应的险别信息，报案号：" + registNo,false);
		}
		
		//是否是新条款险别
		boolean isNewClauseCode = false;
		for(PrpLCItemKindVo prpLCItemKindVo:prpLCItemKindVoList){
			if(!CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLCItemKindVo.getKindCode())){
				isNewClauseCode = CodeConstants.ISNEWCLAUSECODE_MAP.get(prpLCItemKindVo.getRiskCode());
				break;
			}
		}
		
		if(isNewClauseCode){
			for(PrpLCItemKindVo prpLCItemKindVo:prpLCItemKindVoList){
				//存在不计免赔的险别直接break；
				if((kindCode+"M").equals(prpLCItemKindVo.getKindCode().trim())){
					flag = true;
					break;
				}
			}
		}else{
			for(PrpLCItemKindVo prpLCItemKindVo:prpLCItemKindVoList){
				//flag位第五位是1 
				if(kindCode.equals(prpLCItemKindVo.getKindCode()) && "1".equals(prpLCItemKindVo.getFlag().charAt(4)+"")){
					flag =  true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 查询承保车辆信息
	 * <pre></pre>
	 * @param registNo
	 * @param policyNo
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月18日 下午3:10:51): <br>
	 */
	@Override
	public PrpLCItemCarVo findItemCarByRegistNoAndPolicyNo(String registNo,String policyNo) {
		PrpLCItemCarVo prpLCItemCarVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("policyNo",policyNo);
		
		PrpLCItemCar prpLCItemCar = databaseDao.findUnique(PrpLCItemCar.class,queryRule);
		
		if(prpLCItemCar != null){
			prpLCItemCarVo = Beans.copyDepth().from(prpLCItemCar).to(PrpLCItemCarVo.class);
		}
		return prpLCItemCarVo;
	}


	/**
	 * 判断案件是否承保费改以后的保单
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月29日 上午9:10:43): <br>
	 */
	@Override
	public boolean isNewClauseCode(String registNo) {
		List<PrpLCItemKindVo> prpLCItemKindVoList = this.findItemKindVoListByRegistNoAndPolicyNo(registNo,null);
		if(prpLCItemKindVoList == null || prpLCItemKindVoList.isEmpty()){
			logger.error("案件没有对应的险别信息，报案号：" + registNo);
			throw new BusinessException("案件没有对应的险别信息，报案号：" + registNo,false);
		}
		
		//是否是新条款险别
		boolean isNewClauseCode = false;
		for(PrpLCItemKindVo prpLCItemKindVo:prpLCItemKindVoList){
			if(!CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLCItemKindVo.getKindCode())){
				isNewClauseCode = CodeConstants.ISNEWCLAUSECODE_MAP.get(prpLCItemKindVo.getRiskCode());
				break;
			}
		}
		return isNewClauseCode;
	}

	/**
	 * 判断案件是否承保费改以后的保单
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月29日 上午9:10:43): <br>
	 */
	@Override
	public boolean isNew2020ClauseCode(String registNo) {
		//是否是2020新条款险别
		boolean isNewClauseCode = false;

		List<PrpLCItemKindVo> prpLCItemKindVoList = this.findItemKindVoListByRegistNoAndPolicyNo(registNo,null);
		if(!CollectionUtils.isEmpty(prpLCItemKindVoList)){
			for(PrpLCItemKindVo prpLCItemKindVo:prpLCItemKindVoList){
				if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLCItemKindVo.getKindCode())){
					if (prpLCItemKindVo.getAmount() != null && prpLCItemKindVo.getAmount().compareTo(new BigDecimal(122000)) == 1){
						isNewClauseCode = true;
						break;
					}
				}else {
					isNewClauseCode = CodeConstants.ISNEWCLAUSECODE2020_MAP.get(prpLCItemKindVo.getRiskCode());
					break;
				}
			}
		}
		return isNewClauseCode;
	}

	//查看临时保单
	@Override
	public List<PrpLCMainVo> getPolicyForPrpLTmpCMain(String registNo) {
		// 创建返回对象列表
		List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
		//HQL
		StringBuffer queryString = new StringBuffer("from PrpLTmpCMain t where t.registNo = ?");
		// 查询参数
		Object[] paramValues = {registNo};
		// 执行查询
		List<PrpLTmpCMain> prpLCMainList =  databaseDao.findAllByHql(PrpLTmpCMain.class, queryString.toString(), paramValues);
		for(int i = 0;i < prpLCMainList.size();i ++){
			PrpLCMainVo prpLCMainVo = new PrpLCMainVo();
			PrpLTmpCMain prpLCMain = prpLCMainList.get(i);
			// po转vo
			prpLCMainVo = Beans.copyDepth().from(prpLCMain).to(PrpLCMainVo.class);
			prpLCMainVoList.add(prpLCMainVo);
		}
				
		return prpLCMainVoList;
	}

	@Override
	public String findValidNo(String registNo, String classType) {
		String validNo = "";
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("validFlag","1");
//		if(StringUtils.isNotBlank(classType)){
//			queryRule.addEqual("classCode",classType);
//		}
		List<PrpLCMain> cMainList = 
		databaseDao.findAll(PrpLCMain.class,queryRule);
		if(cMainList!=null&&cMainList.size()>0){
			for(PrpLCMain cMain:cMainList){
				boolean equals = Risk.DQZ.equals(cMain.getRiskCode());
				if("11".equals(classType)&&equals){
					validNo = cMain.getValidNo();
				}
				if("12".equals(classType)&&!equals){
					validNo = cMain.getValidNo();
				}
			}
//			validNo = cMainList.get(0).getValidNo();
		}
		return validNo;
	}

	@Override
	public String findPolicyComCode(String registNo, String policyType) {
		if(StringUtils.isEmpty(registNo)||StringUtils.isEmpty(policyType)){
			return null;
		}
		String comCode = "";
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
//		queryRule.addEqual("classCode",policyType);
		queryRule.addEqual("validFlag","1");
		List<PrpLCMain> cMainList = databaseDao.findAll(PrpLCMain.class,queryRule);
		if(cMainList!=null&&cMainList.size()>0){
			for(PrpLCMain cMain:cMainList){
				boolean equals = Risk.DQZ.equals(cMain.getRiskCode());
				if("11".equals(policyType)&&equals){
					comCode = cMain.getComCode();
				}
				if("12".equals(policyType)&&!equals){
					comCode = cMain.getComCode();
				}
			}
		}
		return comCode;
	}

	@Override
	public int findRelatedPolicyNo(String policyNo) {
		return this.findByPolicyNo(policyNo);
	}
	
	/**
	 * <pre></pre>
	 * @param prpCMainVo
	 * @param request
	 * @return
	 * @modified:
	 * ☆ZhangYu(2015年12月1日 上午10:35:14): <br>
	 */
	public Page<PolicyInfoVo> findPrpCMainForPage(PolicyInfoVo policyInfoVo,int start,int pageSize) {
		
		//定义参数list，ps：执行查询时需要转换成object数组
		List<Object> paramValues = new ArrayList<Object>();
		
		//hql查询语句
		StringBuffer queryString = new StringBuffer("from PrpCMain pm, PrpCItemCar pc, PrpCInsured pi"
				+ " WHERE pm.policyNo = pc.id.policyNo AND pm.policyNo = pi.id.policyNo AND pi.id.serialNo = 1");

			//根据页面查询条件的单选，拼接语句的查询条件
			switch (Integer.valueOf(policyInfoVo.getCheckFlag())) {
			case 1://保单号查询，支持精准或后4~7为查询
				if (!policyInfoVo.getPolicyNo().isEmpty()) {
					if(policyInfoVo.getPolicyNo().trim().length() <= 7 && policyInfoVo.getPolicyNo().trim().length() >= 4){
						queryString.append(" AND reverse(pm.policyNo) LIKE ?");
						paramValues.add(reverse(policyInfoVo.getPolicyNo()) +"%");
					} else {
						queryString.append(" AND pm.policyNo = ?");
						paramValues.add(policyInfoVo.getPolicyNo().trim());
					}
				}
				break;
			case 2://车牌号查询，支持精准或后4~7为查询
				if (!policyInfoVo.getLicenseNo().trim().isEmpty()) {
					if (policyInfoVo.getLicenseNo().trim().length() >= 4 && policyInfoVo.getLicenseNo().trim().length() < 7) {
						queryString.append(" AND reverse(pc.licenseNo) LIKE ?");
						paramValues.add(reverse(policyInfoVo.getLicenseNo()) +"%");
					} else {
						queryString.append(" AND pc.licenseNo = ?");
						paramValues.add(policyInfoVo.getLicenseNo().trim());
					}
				}
				break;
			case 3://被保险人查询
				if (!policyInfoVo.getInsuredName().trim().isEmpty()) {
					queryString.append(" AND pm.insuredName LIKE ?");
					paramValues.add(policyInfoVo.getInsuredName().trim() + "%");
				}
				break;
			case 4://发动机号查询，支持精准或后4~7为查询
				if (!policyInfoVo.getEngineNo().trim().isEmpty()) {
					if (policyInfoVo.getEngineNo().trim().length() >= 4 && 
							policyInfoVo.getEngineNo().trim().length() <= 7) {
						queryString.append(" AND reverse(pc.engineNo) LIKE ?");
						paramValues.add(reverse(policyInfoVo.getEngineNo()) +"%");
					} else {
						queryString.append(" AND pc.engineNo = ?");
						paramValues.add(policyInfoVo.getEngineNo().trim());
					}
				}
				break;
			case 5://车架号查询，支持精准或后4~7为查询
				if (!policyInfoVo.getFrameNo().trim().isEmpty()) {
					if (policyInfoVo.getFrameNo().trim().length() >= 4 &&
							policyInfoVo.getFrameNo().trim().length() <= 7) {
						queryString.append(" AND reverse(pc.frameNo) LIKE ?");
						paramValues.add(reverse(policyInfoVo.getFrameNo()) + "%");
					} else {
						queryString.append(" AND pc.frameNo = ?");
						paramValues.add(policyInfoVo.getFrameNo().trim());
					}
					if(policyInfoVo.getComCode()!=null){
						queryString.append(" and pm.comCode like ? ");
						paramValues.add(policyInfoVo.getComCode().substring(0, 2)+"%");
					}
					
				}
				break;
			case 6://vin号（车架号）查询，支持精准或后4~7为查询//改成跟车架号一样
			/*	if (!policyInfoVo.getVinNo().isEmpty()) {
					if (policyInfoVo.getVinNo().length() >= 4 && 
							policyInfoVo.getVinNo().length() <= 7) {
						queryString.append(" AND pc.vinNo LIKE ?");
						paramValues.add("%" + policyInfoVo.getVinNo());
					} else {
						queryString.append(" AND pc.vinNo = ?");
						paramValues.add(policyInfoVo.getVinNo());
					}
				}
				break;*/
				if (!policyInfoVo.getVinNo().trim().isEmpty()) {
					if (policyInfoVo.getVinNo().trim().length() >= 4 &&
							policyInfoVo.getVinNo().trim().length() <= 7) {
						queryString.append(" AND reverse(pc.frameNo) LIKE ?");
						paramValues.add(reverse(policyInfoVo.getVinNo()) + "%");
					} else {
						queryString.append(" AND pc.frameNo = ?");
						paramValues.add(policyInfoVo.getVinNo().trim());
					}
				}
				break;
			case 7://被保险人身份证号查询
				if (!policyInfoVo.getInsuredIdNo().trim().isEmpty()) {
					queryString.append(" AND pi.identifyNumber = ?");
					paramValues.add(policyInfoVo.getInsuredIdNo().trim());
				}
				break;
			}
//			//查询二级机构
//			queryString.append(" AND pc.otherNature like pm.comcode?%");
//			paramValues.add(policyInfoVo.getLicenseColor());		
		//车牌底色查询，非必填项
		//if( !policyInfoVo.getLicenseColor().isEmpty()){
			if(!StringUtils.isBlank(policyInfoVo.getLicenseColor())){

			queryString.append(" AND pc.licenseColorCode = ?");
			paramValues.add(policyInfoVo.getLicenseColor());
		}
		//被保险人身份证号查询，非必填项
		/*if( !policyInfoVo.getInsuredIdNo().isEmpty()){
			queryString.append(" AND pi.identifyNumber = ?");
			paramValues.add(policyInfoVo.getInsuredIdNo());
		}*/
		if("1".equals(policyInfoVo.getOnlyValid())){
			queryString.append(" AND (pm.underWriteFlag = ? ");
			paramValues.add("1");
			queryString.append(" or  pm.underWriteFlag = ? )");
			paramValues.add("3");
			queryString.append(" AND (pm.endDate > ? ");
			paramValues.add(policyInfoVo.getDamageTime());
			queryString.append(" or pm.endDate = ?)");
			paramValues.add(policyInfoVo.getDamageTime());
			}
		queryString.append(" Order By pm.startDate Desc ");
		
		//执行查询
		Page page = databaseDao.findPageByHql(queryString.toString(),( start/pageSize+1 ),pageSize,paramValues.toArray());
		
		//对查询结果进行数据处理
		Page pageReturn = assemblyPolicyInfo(page,policyInfoVo);
	    
	    //返回结果ResultPage
		return pageReturn;
	}

	private Page assemblyPolicyInfo(Page page,PolicyInfoVo policyInfoVo) {
		
		//循环查询结果集合
		for(int i = 0; i<page.getResult().size(); i++ ){

			// 获取当前序号下的保单相关对象数组
			Object[] obj = (Object[])page.getResult().get(i);

			if(obj[0]!=null&&obj[1]!=null){

				PolicyInfoVo plyVo = new PolicyInfoVo();

				// 将获取的承保主表信息，复制到PolicyInfoVo对象中
				PrpCMain pm = (PrpCMain)obj[0];
				Beans.copy().from(pm).to(plyVo);

				// 将获取的标的车表信息，复制到PolicyInfoVo对象中
				PrpCItemCar pc = (PrpCItemCar)obj[1];
				Beans.copy().from(pc).to(plyVo);

				String relatedPolicyNo = getRelatedPolicyNo(plyVo.getPolicyNo());
				plyVo.setRelatedPolicyNo(relatedPolicyNo);

				// 当保单为1-核保通过，3-自动核保通过，判定保单有效，之后通过出险时间进行判断
				if(( "1".equals(plyVo.getUnderWriteFlag())||"3".equals(plyVo.getUnderWriteFlag()) )){
					
				/*	 DateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd 23:00:00");
					 String dateString = formatter.format(plyVo.getEndDate());
					try {
						Date endDate = formatter.parse(dateString);
						plyVo.setEndDate(endDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
					if("1".equals(plyVo.getOthFlag().substring(2, 3))){
						plyVo.setValidFlag("3");// 已退保
					}else if("1".equals(plyVo.getOthFlag().substring(5, 6))){
						plyVo.setValidFlag("4");// 已终保
					}else if(policyInfoVo.getDamageTime().getTime()<plyVo.getStartDate().getTime()){
						plyVo.setValidFlag("2");// 未起保
					}else if(( plyVo.getEndDate().getTime() > policyInfoVo.getDamageTime().getTime() )||( policyInfoVo.getDamageTime().getTime()==plyVo
							.getEndDate().getTime() )){
						plyVo.setValidFlag("1");// 有效
					}else{
						plyVo.setValidFlag("0");// 无效
					}
				}else{// UnderWriteFlag不是1或3时直接判定保单无效
					plyVo.setValidFlag("0");
				}
				String riskCode = pm.getRiskCode();
				if(riskCode.equals("1101")){
					plyVo.setRiskType("交强");
				}else{
					plyVo.setRiskType("商业");
				}

				// 仅查询有效保单
				if("1".equals(policyInfoVo.getOnlyValid())&& !"1".equals(plyVo.getValidFlag())&& !"3".equals(plyVo.getValidFlag())){
					page.getResult().remove(i);
					i-- ;
				}else{
					page.getResult().set(i,plyVo);
				}
			}
		}
		return page;
	}

	/**
	 * 查找一个保单号的关联保单号
	 * @param policyNo
	 * @return
	 * @modified: ☆LiuPing(2016年1月7日 下午3:08:29): <br>
	 */
	public String getRelatedPolicyNo(String policyNo) {
		String mainSubSql = "FROM PrpCMainSub ps WHERE ps.id.mainpolicyno = ? or ps.id.policyNo = ? ";
		List<Object> paramValues = new ArrayList<Object>();
		paramValues.add(policyNo);
		List<PrpCMainSub> prpCMainSubList = databaseDao.findAllByHql(PrpCMainSub.class,mainSubSql,policyNo,policyNo);

		if(prpCMainSubList!=null&&prpCMainSubList.size()>0){
			PrpCMainSub cmainSub = prpCMainSubList.get(0);// 只有一条记录
			if(policyNo.equals(cmainSub.getId().getPolicyNo())){
				return cmainSub.getId().getMainpolicyno();
			}else{
				return cmainSub.getId().getPolicyNo();
			}
		}
		return null;
	}

	public List<PrpLCMainVo> findPrpcMainByPolicyNos(List<String> policyNoList) {
		List<PrpLCMainVo> voList = new ArrayList<PrpLCMainVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("policyNo", policyNoList);
		List<PrpCMain> list = databaseDao.findAll(PrpCMain.class,queryRule);
		
		//迭代保单信息，深度拷贝无法将子表主键中的policyNo复制到抄单表，所以单独处理
		for (PrpCMain prpCMain : list) {
			//将保单信息拷贝到抄单表中
			PrpLCMainVo prpCMainVo = Beans.copyDepth().from(prpCMain).to(PrpLCMainVo.class);
			
			//单独处理保单号
			if (prpCMainVo.getPrpCInsureds().size() > 0) {
				for (int i = 0; i < prpCMainVo.getPrpCInsureds().size(); i ++) {
					prpCMainVo.getPrpCInsureds().get(i).setPolicyNo(prpCMain.getPrpCInsureds().get(i).getId().getPolicyNo());
				}
			}
			//单独处理保单号和序号
			if (prpCMainVo.getPrpCItemCars().size() > 0) {
				for (int i = 0; i < prpCMainVo.getPrpCItemCars().size(); i ++) {
					prpCMainVo.getPrpCItemCars().get(i).setPolicyNo(prpCMain.getPrpCItemCars().get(i).getId().getPolicyNo());
					prpCMainVo.getPrpCItemCars().get(i).setItemNo(prpCMain.getPrpCItemCars().get(i).getId().getItemNo());
				}
			}
			//单独处理保单号和序号
			if (prpCMainVo.getPrpCItemKinds().size() > 0) {
				for (int i = 0; i < prpCMainVo.getPrpCItemKinds().size(); i ++) {
					PrpLCItemKindVo prpLCItemKindVo = prpCMainVo.getPrpCItemKinds().get(i);
					prpLCItemKindVo.setPolicyNo(prpCMain.getPrpCItemKinds().get(i).getId().getPolicyNo());
					prpLCItemKindVo.setItemKindNo(prpCMain.getPrpCItemKinds().get(i).getId().getItemKindNo());
					if(!"1101".equals(prpCMainVo.getRiskCode())){//商业险
						prpLCItemKindVo.setNoDutyFlag("0");
						if(!"Y".equals(prpLCItemKindVo.getCalculateFlag())){
							if(!prpLCItemKindVo.getKindCode().endsWith("M") && 
									prpLCItemKindVo.getFlag().length() > 4 && 
									"1".equals(prpLCItemKindVo.getFlag().substring(4,5))){
								prpLCItemKindVo.setNoDutyFlag("1");
							}
						}else{//主险
							if(prpLCItemKindVo.getFlag().length() > 4 && 
									"1".equals(prpLCItemKindVo.getFlag().substring(4,5))){
								prpLCItemKindVo.setNoDutyFlag("1");
							}
						}
					}
				}
			}
			voList.add(prpCMainVo);
		}
		return voList;
	}

	/**
	 * 查询一个保单的关联保单号，不包括当前保单号
	 * @param policyInfoVo 包含了保单号，出险日期
	 * @return
	 * @modified: ☆LiuPing(2016年1月7日 下午3:05:26): <br>
	 */
	public Page<PolicyInfoVo> findRelatedPolicy(PolicyInfoVo policyInfoVo) {
		String policyNo = policyInfoVo.getPolicyNo();// 当前保单号
		String relatePlyNo = policyInfoVo.getRelatedPolicyNo();// 关联的保单号

		if(StringUtils.isBlank(relatePlyNo)){
			relatePlyNo = getRelatedPolicyNo(policyNo);
		}

		String queryString = "from PrpCMain pm, PrpCItemCar pc WHERE pm.policyNo = pc.id.policyNo  AND pm.policyNo = ? ";
		// 执行查询
		Page page = databaseDao.findPageByHql(queryString,1,3,relatePlyNo);

		// 对查询结果进行数据处理
		Page pageReturn = assemblyPolicyInfo(page,policyInfoVo);
		// 返回结果ResultPage
		return pageReturn;
	}
	
	public int findByPolicyNo(String policyNo) {
		String relatePlyNo = getRelatedPolicyNo(policyNo);// 关联的保单号
		String queryString = "from PrpCMain pm, PrpCItemCar pc WHERE pm.policyNo = pc.id.policyNo  AND pm.policyNo = ? ";
		// 执行查询
		Page<Object[]> page = databaseDao.findPageByHql(queryString,1,3,relatePlyNo);
		return page.getResult().size();
	}
	
	public List<PolicyInfoVo> findPrpCMainForPages(PolicyInfoVo policyInfo,int start,int pageSize) {
		Page<PolicyInfoVo> page = findPrpCMainForPage(policyInfo, start, pageSize);
		/*String mainSubSql = "FROM from PrpCMain pm, PrpCItemCar pc WHERE pm like ? and pc.frameNo = ? ";
		List<Object> paramValues = new ArrayList<Object>();
		paramValues.add(policyNo);
		paramValues.add(policyNo);*/
		//List<PrpCMainSub> prpCMainSubList = databaseDao.findAllByHql(PrpCMainSub.class,mainSubSql,policyNo,policyNo);
		//循环查询结果集合
		
		List<PolicyInfoVo> policyInfoVos = new ArrayList<PolicyInfoVo>();
		List<PolicyInfoVo> policyInfoVoList = new ArrayList<PolicyInfoVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			PolicyInfoVo policyInfoVo = page.getResult().get(i);
			QueryRule queryRule = QueryRule.getInstance();
			//hql查询语句
			StringBuffer queryString = new StringBuffer("from PrpCMain pm, PrpCItemCar pc"
					+ " WHERE pm.policyNo = pc.id.policyNo");
			//定义参数list，ps：执行查询时需要转换成object数组
			List<Object> paramValues = new ArrayList<Object>();
			queryString.append(" and pm.comCode like ? ");
			paramValues.add(policyInfoVo.getComCode().substring(0, 2)+"%");
			
			queryString.append("and pc.frameNo = ? ");
			paramValues.add(policyInfoVo.getFrameNo());
			
			if (!policyInfoVo.getPolicyNo().isEmpty()) {
				queryString.append("and pc.id.policyNo = ?");
				paramValues.add(policyInfoVo.getPolicyNo());
			}
			
//			String queryString = "FROM from PrpCMain pm, PrpCItemCar pc WHERE pm.comcode like ? and pc.frameNo = ? ";
		/*	List<Object> paramValues = new ArrayList<Object>();
			paramValues.add(policyInfoVo.getComCode().substring(0, 2)+"%");
			paramValues.add(policyInfoVo.getFrameNo());
			if (!policyInfoVo.getPolicyNo().isEmpty()) {
				queryString =queryString+"and pm.policyNo = ?";
				paramValues.add(policyInfoVo.getPolicyNo());
			}*/
			
		//	PrpLCMainVo
			
			/*if (!policyInfoVo.getPolicyNo().isEmpty()) {
			queryRule.addEqual("id.policyNo", policyInfoVo.getPolicyNo());
			}
			queryRule.addEqual("frameNo", policyInfoVo.getFrameNo());
			queryRule.addLike("otherNature",policyInfoVo.getComCode().substring(0, 2)+"%");*/
			//执行查询
			Page page1 = databaseDao.findPageByHql(queryString.toString(),( start/pageSize+1 ),pageSize,paramValues.toArray());
			
			//List<PrpCItemCar> list = databaseDao.findAllByHql(PrpCItemCar.class, queryString, paramValues.toString());
		
			//System.out.println(policyInfoVo.getComCode().substring(0, 2)+"%"+"ppppppp"+policyInfoVo.getPolicyNo()+"kkk"+policyInfoVo.getFrameNo());
			//List<PrpCItemCar> list = databaseDao.findAll(PrpCItemCar.class,queryRule);
			//对查询结果进行数据处理
			Page pageReturn = assemblyPolicyInfo(page1,policyInfo);
			/*for(PrpCItemCar po:list){
				policyInfoVo.setPolicyNo(po.getPrpCMain().getPolicyNo());
				policyInfoVos.add(policyInfoVo);
			}*/
			if(pageReturn.getTotalCount()>0){
				for(int j=0;j<pageReturn.getTotalCount();j++){
					policyInfoVos.add((PolicyInfoVo) pageReturn.getResult().get(j));
				}
			}
		}
		policyInfoVoList.addAll(policyInfoVos);
		return  policyInfoVoList;
		
	}
	
	private String reverse(String str){
		if(str!=null){
			return new StringBuilder(str.trim()).reverse().toString();
		}
		
		return null ;
	}


	@Override
	public String getPolicyComCode(String registNo) {
		String comCode = "";
		List<PrpLCMainVo> cMainVoList = getPolicyAllInfo(registNo);
		if(cMainVoList != null && cMainVoList.size() >0 ){
			if(cMainVoList.size() > 1){
				for(PrpLCMainVo cMainVo : cMainVoList){
					String tempComCode = cMainVo.getComCode();
					if(!Risk.DQZ.equals(tempComCode)){
						comCode = tempComCode;
					}
				}
			}else{
				comCode = cMainVoList.get(0).getComCode();
			}
		}
		return comCode;
	}



	@Override
	public PrpLCMainVo getRegistNoAndRiskCodeInfo(String registNo,
			String riskCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("riskCode",riskCode);
		List<PrpLCMain> prpLCMainList = databaseDao.findAll(PrpLCMain.class,queryRule);
		PrpLCMainVo prpLCMainVo = new PrpLCMainVo();
		if(prpLCMainList!=null && prpLCMainList.size()>0 ){
			prpLCMainVo = Beans.copyDepth().from(prpLCMainList.get(0)).to(PrpLCMainVo.class);
		}
		return prpLCMainVo;
	}


	@Override
	public PrpLCMainVo getPolicyInfoByType(String registNo, String type) {
		PrpLCMainVo cMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
//		queryRule.addEqual("policyNo",policyNo);
		queryRule.addEqual("validFlag","1");
		List<PrpLCMain> prpLCMain = databaseDao.findAll(PrpLCMain.class,queryRule);
		if(prpLCMain != null && prpLCMain.size() > 0){
			for(PrpLCMain cmain : prpLCMain){
				if("1".equals(type) && Risk.DQZ.equals(cmain.getRiskCode())){
					cMainVo = Beans.copyDepth().from(cmain).to(PrpLCMainVo.class);
				}else if("2".equals(type) && !Risk.DQZ.equals(cmain.getRiskCode())){
					cMainVo = Beans.copyDepth().from(cmain).to(PrpLCMainVo.class);
				}
			}
		}
		return cMainVo;
	}


	@Override
	public List<PrpLCItemKindVo> findItemKindVos(String policyNo,String registNo,String kindcode) {
		QueryRule queryRule =QueryRule.getInstance();
		if(policyNo!=null){
			queryRule.addEqual("policyNo",policyNo);
		}
		queryRule.addEqual("registNo",registNo);
	    queryRule.addEqual("kindCode",kindcode);
		List<PrpLCItemKind> prpLCItemKinds=databaseDao.findAll(PrpLCItemKind.class,queryRule);
		List<PrpLCItemKindVo> prpLCItemKindVos=new ArrayList<PrpLCItemKindVo>();
		if(prpLCItemKinds!=null && prpLCItemKinds.size()>0){
			for(PrpLCItemKind prpLCItemKind:prpLCItemKinds){
				PrpLCItemKindVo prpLCItemKindVo=new PrpLCItemKindVo();
				Beans.copy().from(prpLCItemKind).to(prpLCItemKindVo);
				prpLCItemKindVos.add(prpLCItemKindVo);
				break;
			}
		}
		return prpLCItemKindVos;
	}

	@Override
	public String findCoinsCode(String policyNo){
		String coinsCode = null;
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append("SELECT A.COINSCODE,A.COINSNAME,A.COINSRATE FROM YWUSER.PRPCCOINS A WHERE POLICYNO = ? AND CHIEFFLAG = ?  ");
		sqlUtil.addParamValue(policyNo);
		sqlUtil.addParamValue("1");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		
		if(objects!=null && objects.size()>0){
			for(int i = 0; i<objects.size(); i++ ){
				Object[] obj = objects.get(i);
				coinsCode = obj[0]!=null ? obj[0].toString():null;
				break;
			}
		}
		return coinsCode;
	}
	@Override
	public List<PrpLCItemCarVo> findPrpcItemcar(String licenseNo,
			String engineNo, String vin) {
		List<PrpLCItemCarVo> prpLCItemCarVos = new ArrayList<PrpLCItemCarVo>();
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" FROM PrpCItemCar itemCar ");
		sqlUtil.append(" WHERE 1=1 ");
		if(StringUtils.isNotBlank(licenseNo)){
			sqlUtil.append(" AND( itemCar.licenseNo = ? ");
			sqlUtil.addParamValue(licenseNo);
		}
		if(StringUtils.isNotBlank(engineNo)){
			sqlUtil.append(" OR itemCar.engineNo = ? ");
			sqlUtil.addParamValue(engineNo);
		}
		if(StringUtils.isNotBlank(vin)){
			sqlUtil.append(" OR itemCar.vinNo = ? ");
			sqlUtil.addParamValue(vin);
			sqlUtil.append(" OR itemCar.frameNo = ? )");
			sqlUtil.addParamValue(vin);
		}
		String sql = sqlUtil.getSql();
		Object[] params = sqlUtil.getParamValues();
		List<PrpCItemCar> prpCItemCars = new ArrayList<PrpCItemCar>();
		prpCItemCars = databaseDao.findAllByHql(PrpCItemCar.class,sql,params);
		for (PrpCItemCar prpCItemCar : prpCItemCars) {
			PrpLCItemCarVo prpLCItemCarVo = new PrpLCItemCarVo();
			Beans.copy().from(prpCItemCar).to(prpLCItemCarVo);
			prpLCItemCarVo.setPolicyNo(prpCItemCar.getId().getPolicyNo());
			prpLCItemCarVos.add(prpLCItemCarVo);
		}
		return prpLCItemCarVos;
	}


	@Override
	public List<PrpLCItemCarVo> findPrpcItemcarByRegistNo(String registNo) {
		PrpLCItemCarVo prpLCItemCarVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		
		List<PrpLCItemCar> prpLCItemCars = databaseDao.findAll(PrpLCItemCar.class,queryRule);
		List<PrpLCItemCarVo> prpLCItemCarVoList = new ArrayList<PrpLCItemCarVo>();
		if(!prpLCItemCars.isEmpty() && prpLCItemCars.size()>0){
			for(PrpLCItemCar prpLCItemCar : prpLCItemCars){
				prpLCItemCarVo = Beans.copyDepth().from(prpLCItemCar).to(PrpLCItemCarVo.class);
				prpLCItemCarVoList.add(prpLCItemCarVo);
			}
		}
		return prpLCItemCarVoList;
	}


	@Override
	public List<Map<String,String>> getCaseInfo(String registNo) {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append("SELECT A.POLICYNO,A.REGISTNO,B.LICENSENO,B.INSUREDNAME,A.REPORTORNAME,A.REPORTORPHONE, ");
		sqlUtil.append(" 	A.LINKERNAME,A.LINKERMOBILE,to_char(A.REPORTTIME,'yyyy-mm-dd hh24:mi:ss') REPORTTIME, ");
		sqlUtil.append(" 	B.DANGERREMARK,to_char(A.DAMAGETIME,'yyyy-mm-dd hh24:mi:ss') DAMAGETIME,A.DAMAGEADDRESS, ");
		sqlUtil.append(" 	B.ISONSITREPORT,A.DAMAGECODE ,'1' SUBCERTITYPE, A.SELFCLAIMFLAG ");
		sqlUtil.append(" FROM CLAIMUSER.PRPLREGIST A LEFT JOIN CLAIMUSER.PRPLREGISTEXT B ON A.REGISTNO = B.REGISTNO WHERE A.REGISTNO = ? ");
		sqlUtil.addParamValue(registNo);
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(int i = 0; i<objects.size(); i++ ){

			Object[] obj = objects.get(i);
			Map<String,String> map= new HashMap<String,String>();
			map.put("PolicyNo", obj[0]==null ? "" : obj[0].toString());
			map.put("ClmNo", obj[1]==null ? "" : obj[1].toString());
			map.put("LicenseNo", obj[2]==null ? "" : obj[2].toString());
			map.put("InsuredName", obj[3]==null ? "" : obj[3].toString());
			map.put("ReporterName", obj[4]==null ? "" : obj[4].toString());
			map.put("ReporterPhone", obj[5]==null ? "" : obj[5].toString());
			map.put("LinkerName", obj[6]==null ? "" : obj[6].toString());
			map.put("LinkerPhone", obj[7]==null ? "" : obj[7].toString());
			map.put("ReportTime", obj[8]==null ? "" : obj[8].toString());
			map.put("AccidentCourse", obj[9]==null ? "" : obj[9].toString());
			map.put("DamageTime", obj[10]==null ? "" : obj[10].toString());
			map.put("ExamineAddress", obj[11]==null ? "" : obj[11].toString());
			map.put("FirstSiteFlag", obj[12]==null ? "" : obj[12].toString());
			map.put("DamageCode", obj[13]==null ? "" : obj[13].toString());
			map.put("SubCertiType", obj[14]==null ? "" : obj[14].toString());
			map.put("SelfClaimFlag", obj[15]==null ? "" : obj[15].toString());
			
			list.add(map);
		}
		return list;
	}
	
}
