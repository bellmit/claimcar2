package ins.sino.claimcar.subrogationsh.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.DataUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.claim.po.PrpLCompensate;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.platform.service.PlatformReUploadService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.subrogation.platform.vo.AccountsInfoVo;
import ins.sino.claimcar.subrogation.po.PrpLPlatLock;
import ins.sino.claimcar.subrogation.po.PrpLRecoveryOrPay;
import ins.sino.claimcar.subrogation.sh.service.SubrogationSHHandleService;
import ins.sino.claimcar.subrogation.sh.vo.CopyInformationResultVo;
import ins.sino.claimcar.subrogation.sh.vo.SubrogationSHQueryVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.SubrogationQueryVo;
import ins.sino.claimcar.subrogationsh.platform.service.SubrogationSHToPlatformService;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 上海代位求偿处理dubbo服务类
 * @author ★Luwei
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("subrogationSHHandleServiceImpl")
public class SubrogationSHHandleServiceImpl implements SubrogationSHHandleService{

	@Autowired
	private DatabaseDao databaseDao;
	
	@Autowired
	private PlatformReUploadService platformLogService;
	
	@Autowired
	private SubrogationSHToPlatformService platformService;
	
	@Autowired
	private PolicyViewService policyViewService;
	
	@Autowired
	CodeTranService codeTranService;
	
	/**
	 * 代位求偿信息抄回接口信息查询
	 * @param registNo
	 * @param claimNo
	 * @throws Exception 
	 * @modified:
	 * ☆Luwei(2017年3月6日 下午7:01:56): <br>
	 */
	@Override
	public List<CopyInformationResultVo> sendCopyInformationToSubrogationSH(SubrogationSHQueryVo queryVo) throws Exception {
		// TODO Auto-generated method stub
		//这个提示信息也会出现在页面
		Assert.notNull(queryVo.getComCode(),"机构代码不能为空！");
		Assert.notNull(queryVo.getRegistNo(),"查询条件报案号不能为空！");
		Assert.notNull(queryVo.getClaimSeqNo(),"查询条件理赔编码不能为空！");
		//
		return platformService.sendCopyInformationToSubrogationSH(queryVo);
	}

	//@Override
	/*public List<AccountsInfoVo> claimDWrecoveryQuery(SubrogationQueryVo queryVo) {*/
	@Override
	public ResultPage<PrpLPlatLockVo> claimDWrecoveryQuery(SubrogationQueryVo queryVo) {
		int start=0;
		int length=10;
	/*	QueryRule rule = QueryRule.getInstance();
		
		// 结算码
		if (StringUtils.isNotEmpty(queryVo.getRecoveryCode())) {
			rule.addEqual("recoveryCode",queryVo.getRecoveryCode());
		}
		// 结算码状态
		if (StringUtils.isNotEmpty(queryVo.getRecoveryCodeStatus())) {
			rule.addEqual("recoveryCodeStatus",queryVo.getRecoveryCodeStatus());
		}
		// 对方保险公司
		if (StringUtils.isNotEmpty(queryVo.getInsurerCode())) {
			rule.addEqual("oppoentInsurerCode",queryVo.getInsurerCode());
		}
		// 对方承保地区
		if (StringUtils.isNotEmpty(queryVo.getAreaCode())) {
			rule.addEqual("oppoentInsurerArea",queryVo.getAreaCode());
		}
		// 本方代付/清付标志
		if (StringUtils.isNotEmpty(queryVo.getRecoverStatus())) {
			rule.addEqual("recoveryOrPayFlag",queryVo.getRecoverStatus());
		}
		// 代付/清付险别
		if (StringUtils.isNotEmpty(queryVo.getCoverageType())) {
			rule.addEqual("recoveryOrPayType",queryVo.getCoverageType());
		}
		// 追偿金额
		String recoveryAmountMin = queryVo.getRecoveryAmountMin();
		String recoveryAmountMax = queryVo.getRecoveryAmountMax();
		if(StringUtils.isNotEmpty(recoveryAmountMin) && StringUtils.isNotEmpty(recoveryAmountMax)){
			rule.addBetweenIfExist("sumRecoveryAmount",recoveryAmountMin,recoveryAmountMax);
		}
		
		
		rule.addSql(" recoverorpaystatus is null or recoverorpaystatus = '1' ");
		
		// 清付金额
		String payAmountMin = queryVo.getPayAmountMin();
		String PayAmountMax = queryVo.getPayAmountMax();
		if(StringUtils.isNotEmpty(payAmountMin) && StringUtils.isNotEmpty(PayAmountMax)){
			rule.addBetweenIfExist("sumRealAmount",payAmountMin,PayAmountMax);
		}
        
		rule.addSql(" comCode is not null ");
		rule.addLike("comCode","22%");
		
		//rule.addLike("comCode","22%");
		
		// 结算起始时间
//		rule.addBetweenIfExist("startTime",queryVo.getPayStartTime(),queryVo.getPayEndTime());
		
		List<PrpLPlatLock> resultList = databaseDao.findAll(PrpLPlatLock.class,rule);
		*/
		
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		 sqlUtil.append(" FROM PrpLPlatLock lock WHERE 1=1 ");//清算中
		 /*sqlUtil.append(" AND lock.recoverOrPayStatus= ?");
		 sqlUtil.addParamValue("1");*/
		 sqlUtil.append(" AND lock.comCode like ?");	
		 sqlUtil.addParamValue("22%");
		 
		// 结算码
		if (StringUtils.isNotEmpty(queryVo.getRecoveryCode())) {
			sqlUtil.append(" AND lock.recoveryCode= ?");
			sqlUtil.addParamValue(queryVo.getRecoveryCode());
			}
		
		// 结算码状态
		if (StringUtils.isNotEmpty(queryVo.getRecoveryCodeStatus())) {
			sqlUtil.append(" AND lock.recoveryCodeStatus= ?");
			sqlUtil.addParamValue(queryVo.getRecoveryCodeStatus());
					
		}
		
		// 追偿方保险公司
		if (StringUtils.isNotEmpty(queryVo.getInsurerCode())) {
			sqlUtil.append(" AND lock.oppoentInsurerCode= ?");
			sqlUtil.addParamValue(queryVo.getInsurerCode());
		}
		// 对方承保地区
		if (StringUtils.isNotEmpty(queryVo.getAreaCode())) {
			 sqlUtil.append(" AND lock.oppoentInsurerArea= ?");
			 sqlUtil.addParamValue(queryVo.getAreaCode());
					
				}
		
		// 本方代付/清付标志
		if (StringUtils.isNotEmpty(queryVo.getRecoverStatus())) {
			sqlUtil.append(" AND lock.recoveryOrPayFlag= ?");
			sqlUtil.addParamValue(queryVo.getRecoverStatus());
					
		}
		
		// 代付/清付险别
		if (StringUtils.isNotEmpty(queryVo.getCoverageType())) {
			sqlUtil.append(" AND lock.recoveryOrPayType= ?");
			sqlUtil.addParamValue(queryVo.getCoverageType());
					
		}
		
		
		// 追偿金额
		String recoveryAmountMin = queryVo.getRecoveryAmountMin();
		String recoveryAmountMax = queryVo.getRecoveryAmountMax();
		 if(StringUtils.isNotEmpty(recoveryAmountMin) && StringUtils.isNotEmpty(recoveryAmountMax)){
					sqlUtil.append(" AND lock.sumRecoveryAmount >= ? and  lock.sumRecoveryAmount <= ? ");
					sqlUtil.addParamValue(new BigDecimal(recoveryAmountMin));
					sqlUtil.addParamValue(new BigDecimal(recoveryAmountMax));
					
		}
		 
		// 清付金额
		String payAmountMin = queryVo.getPayAmountMin();
		String PayAmountMax = queryVo.getPayAmountMax();
		if(StringUtils.isNotEmpty(payAmountMin) && StringUtils.isNotEmpty(PayAmountMax)){
				sqlUtil.append(" AND lock.sumRealAmount >= ? and  lock.sumRealAmount <= ? ");
				sqlUtil.addParamValue(new BigDecimal(payAmountMin));
				sqlUtil.addParamValue(new BigDecimal(PayAmountMax));
				
		}
			
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		
		List<PrpLPlatLockVo> resultVoList=new ArrayList<PrpLPlatLockVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			PrpLPlatLockVo LockVo = new PrpLPlatLockVo();
			List<Object[]> obj = page.getResult();

			PrpLPlatLock Lockpo = (PrpLPlatLock)obj.toArray()[i];
			
			Beans.copy().from(Lockpo).to(LockVo);
			LockVo.setRecoveryCodeStatus(codeTranService.transCode("RecoveryCodeStatus", LockVo.getRecoveryCodeStatus()));
			if(StringUtils.isNotBlank(LockVo.getOppoentInsurerCode()) && LockVo.getOppoentInsurerCode().endsWith("01")){
				 
				LockVo.setOppoentInsurerCode(codeTranService.transCode("DWInsurerCode",LockVo.getOppoentInsurerCode().substring(0,LockVo.getOppoentInsurerCode().length()-2)));
				
			 }else if(StringUtils.isNotBlank(LockVo.getOppoentInsurerCode())){
				 LockVo.setOppoentInsurerCode(codeTranService.transCode("DWInsurerCode",LockVo.getOppoentInsurerCode()));
			 }else{
				 
			 }  
			LockVo.setOppoentInsurerArea(codeTranService.transCode("DWInsurerArea", LockVo.getOppoentInsurerArea()));
			LockVo.setCoverageType(codeTranService.transCode("DWCoverageType", LockVo.getCoverageType()));  
			resultVoList.add(LockVo);
		}
		ResultPage<PrpLPlatLockVo> resultPage = new ResultPage<PrpLPlatLockVo> (start, length, page.getTotalCount(), resultVoList);
		
		
		
		//return this.setAccountsInfoVo(resultList);
		return resultPage;
	}

	@Override
	public List<AccountsInfoVo> findRecoveryByCode(String recoveryCode) {
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("recoveryCode",recoveryCode);
		List<PrpLPlatLock> resultList = databaseDao.findAll(PrpLPlatLock.class,rule);
		return this.setAccountsInfoVo(resultList);
	}
	
	private List<AccountsInfoVo> setAccountsInfoVo(List<PrpLPlatLock> resultList) {
		List<AccountsInfoVo> infoVoList = new ArrayList<AccountsInfoVo>();
		String address = "";
		if(resultList!=null&& !resultList.isEmpty()){
			List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(resultList.get(0).getRegistNo());
			if(cMainVoList!=null&& !cMainVoList.isEmpty()){
				address = cMainVoList.get(0).getAppliAddress();
			}
			for(PrpLPlatLock result:resultList){
				List<PrpLRecoveryOrPay> recoveryOrPayList = result.getPrpLRecoveryOrPays();
				if(recoveryOrPayList!=null&& !recoveryOrPayList.isEmpty()){
					for(PrpLRecoveryOrPay recoveryOrPay:recoveryOrPayList){
						AccountsInfoVo infoVo = new AccountsInfoVo();
						infoVo.setAccountsNo(recoveryOrPay.getRecoveryCode());// 结算码
						infoVo.setAccountsNoStatus(result.getRecoveryCodeStatus());// 结算码状态
						infoVo.setAccountsStartDate(result.getLossTime());// 结算起始时间

						if("1".equals(result.getRecoveryOrPayFlag())){// 追偿
							infoVo.setRecoverCompanyCode("DHIC");
							infoVo.setRecoverAreaCode(address);// 追偿方承保地区
							infoVo.setCompensateComCode(result.getOppoentInsurerCode());// 责任对方保险公司
							infoVo.setCompensationAreaCode(result.getOppoentInsurerArea());// 责任对方承保地区
						}else if("2".equals(result.getRecoveryOrPayFlag())){// 清付
							infoVo.setRecoverCompanyCode(result.getOppoentInsurerCode());// 追偿方保险公司
							infoVo.setRecoverAreaCode(result.getOppoentInsurerArea());// 追偿方承保地区
							infoVo.setCompensateComCode("DHIC");// 责任对方保险公司
							infoVo.setCompensationAreaCode(address);// 责任对方承保地区
						}

						// result.getCoverageType()
						infoVo.setCoverageCode(result.getRecoveryOrPayType());
						double amount = DataUtils.NullToZero(recoveryOrPay.getRecoveryOrPayAmount()).doubleValue();
						infoVo.setRecoverAmount(amount);// 追偿金额
						double cAmount = DataUtils.NullToZero(result.getSumRealAmount()).doubleValue();
						infoVo.setCompensateAmount(cAmount);
						infoVo.setLastCompensateAmount(cAmount);
						infoVo.setAccountAmount(cAmount);

						infoVoList.add(infoVo);
					}
				}
			}
		}
		return infoVoList;
	}

	@Override
	public PrpLPlatLockVo findPlatLockByRecoveryCode(String recoveryCode) {
	
			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addEqual("recoveryCode",recoveryCode);
			
			PrpLPlatLock platLock = databaseDao.findUnique(PrpLPlatLock.class,queryRule);
			
			if(platLock ==null){
				return null;
			}
			PrpLPlatLockVo platLockVo =Beans.copyDepth().from(platLock).to(PrpLPlatLockVo.class);
			
			return platLockVo;
		
	}
	
	//
}
