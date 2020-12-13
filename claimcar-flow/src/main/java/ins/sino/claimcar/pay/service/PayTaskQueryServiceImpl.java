package ins.sino.claimcar.pay.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.CodeConstants.FeeType;
import ins.sino.claimcar.claim.po.PrpLClaim;
import ins.sino.claimcar.claim.po.PrpLCompensate;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.flow.po.PrpLWfTaskQuery;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("padPayService")
public class PayTaskQueryServiceImpl implements PayTaskQueryService {
	
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private CompensateTaskService compensateTaskService;
	
	@Autowired
	BaseDaoService baseDaoService;
	private static Logger logger = LoggerFactory.getLogger(PayTaskQueryServiceImpl.class);

	
	/* 
	 * @see ins.sino.claimcar.pay.service.PayTaskQueryService#find(ins.sino.claimcar.claim.vo.PrpLClaimVo, ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo, int, int)
	 * @param prpLClaimVo
	 * @param prplwftaskqueryvo
	 * @param start
	 * @param length
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@Override
	public ResultPage<WfTaskQueryResultVo> find(PrpLClaimVo prpLClaimVo, PrpLWfTaskQueryVo prplwftaskqueryvo,
			int start, int length) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLWfTaskQuery query,PrpLClaim claim,PrpLRegist reg where 1=1");
		sqlUtil.append(" and query.registNo=claim.registNo and claim.registNo=reg.registNo ");
		sqlUtil.append(" and claim.claimTime is not null ");
		sqlUtil.append(" and claim.endCaseTime is null ");
		sqlUtil.append(" and claim.riskCode = ? ");
		sqlUtil.addParamValue("1101");
		sqlUtil.append(" and reg.isGBFlag not in(?,?) ");
		sqlUtil.addParamValue("2");
		sqlUtil.addParamValue("4");
		
//		sqlUtil.andReverse(prpLClaimVo,"query",7,"registNo","policyNo","claimNo");
		sqlUtil.andReverse(prpLClaimVo,"query",7,"registNo","policyNo");
		sqlUtil.andLike2(prpLClaimVo,"claim","claimNo");
		//sqlUtil.andReverse(prplwftaskqueryvo,"query",7);
		//sqlUtil.andLike2(prplwftaskqueryvo,"query","licenseNo");
//		sqlUtil.andReverse(prplwftaskqueryvo,"query",7,"licenseNo");
		sqlUtil.andTopComSql("query","comCodePly",prplwftaskqueryvo.getComCode());
		sqlUtil.andEquals(prpLClaimVo,"claim","mercyFlag","riskCode");
		sqlUtil.andLike2(prplwftaskqueryvo,"query","insuredName","licenseNo");
		
		// 排序
		sqlUtil.append(" Order By claim.claimTime,query.damageTime");

		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);

		// 对象转换
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){

			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
			Object[] obj = page.getResult().get(i);

			PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[0];
			Beans.copy().from(wfTaskQuery).to(resultVo);

			PrpLClaim prpLClaim = (PrpLClaim)obj[1];
				Beans.copy().from(prpLClaim).excludeNull().to(resultVo);
		
			resultVo.setComCode(resultVo.getComCodePly());
			resultVo.setClaimNo(prpLClaim.getClaimNo());
			resultVoList.add(resultVo);
		}

		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo> (start, length, page.getTotalCount(), resultVoList);
		return resultPage;

	}
	
	
	
	
	/* 
	 * @see ins.sino.claimcar.pay.service.PayTaskQueryService#findByPre(ins.sino.claimcar.claim.vo.PrpLClaimVo, ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo, int, int)
	 * @param prpLClaimVo
	 * @param prplwftaskqueryvo
	 * @param start
	 * @param length
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@Override
	public ResultPage<WfTaskQueryResultVo> findByPre(PrpLClaimVo prpLClaimVo, PrpLWfTaskQueryVo prplwftaskqueryvo,
			int start, int length) throws Exception {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		
		sqlUtil.append(" select query.registNo,query.mercyFlag,query.insuredName,claim.createUser,to_char(wmsys.wm_concat(claim.claimNo)),to_char(wmsys.wm_concat(claim.claimTime)) a,to_char(wmsys.wm_concat(query.damageTime)) b FROM PrpLWfTaskQuery query,PrpLClaim claim,PrpLRegist reg where 1=1 ");
		sqlUtil.append(" and query.registNo=claim.registNo and claim.registNo=reg.registNo ");
		sqlUtil.append(" and claim.claimTime is not null ");
		sqlUtil.append(" and claim.endCaseTime is null ");
		sqlUtil.append(" and reg.isGBFlag not in('2','4') ");
		
		sqlUtil.andReverse(prpLClaimVo,"query",7,"registNo","policyNo");
		sqlUtil.andReverse(prplwftaskqueryvo,"query",7,"licenseNo");
		sqlUtil.andTopComSql("query","comCodePly",prplwftaskqueryvo.getComCode());
		sqlUtil.andEquals(prpLClaimVo,"claim","mercyFlag","riskCode");
		sqlUtil.andLike(prpLClaimVo,"claim","claimNo");
		sqlUtil.andLike2(prplwftaskqueryvo,"query","insuredName");
//		sqlUtil.andLike(prplwftaskqueryvo,"comCode","claimNo");
		
		
		sqlUtil.append(" group by query.registNo,query.mercyFlag,query.insuredName,claim.createUser ");
		// 排序
		sqlUtil.append(" Order By a desc,b desc ");
		String sql = sqlUtil.getSql();
		System.out.println(sql);
		Object[] values = sqlUtil.getParamValues();
		logger.debug(ArrayUtils.toString(values));
		System.out.println(ArrayUtils.toString(values));
		
		Page<Object[]> page = baseDaoService.pagedSQLQuery(sql,start,length,values);
	//	Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);

		// 对象转换
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){

			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
			Object[] obj = page.getResult().get(i);
//
//			PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[0];
//			Beans.copy().from(wfTaskQuery).to(resultVo);
//
//			PrpLClaim prpLClaim = (PrpLClaim)obj[1];
//				Beans.copy().from(prpLClaim).excludeNull().to(resultVo);
		
//			resultVo.setComCode(resultVo.getComCodePly());
//			resultVo.setClaimNo(prpLClaim.getClaimNo());
			resultVo.setRegistNo(obj[0]==null ? "" : obj[0].toString());
			resultVo.setMercyFlag(obj[1]==null ? "" : obj[1].toString());
			resultVo.setInsuredName(obj[2]==null ? "" : obj[2].toString());
			resultVo.setCreateUser(obj[3]==null ? "" : obj[3].toString());
			resultVoList.add(resultVo);
		}

		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo> (start, length, page.getTotalCount(), resultVoList);
		return resultPage;

	}
	private boolean  addTimeControl(PrpLWfTaskQueryVo taskQueryVo){
		String registNo = taskQueryVo.getRegistNo(); //报案号
		String policyNo = taskQueryVo.getPolicyNo(); //保单号
		String licenseNo = taskQueryVo.getLicenseNo(); //标的车牌号
		String claimNo = taskQueryVo.getClaimNo(); //立案号
		String compensateNo = taskQueryVo.getCompensateNo();//预付号
		//如果报案号、保单号、标的车牌号、立案号符合添加忽略出险时间查询
		if((StringUtils.isNotBlank(registNo) && registNo.trim().length() >= 4)
			|| (StringUtils.isNotBlank(policyNo) && policyNo.trim().length()>=4) 
			|| (StringUtils.isNotBlank(licenseNo) && licenseNo.trim().length() >= 4)
			|| (StringUtils.isNotBlank(claimNo) && claimNo.trim().length()>= 21)
			|| (StringUtils.isNotBlank(compensateNo) && compensateNo.trim().length() >= 21)){
			return false;
		}	
		return true;
	}
	private  boolean validate(PrpLWfTaskQueryVo taskQueryVo){
		String registNo = taskQueryVo.getRegistNo(); //报案号
		String policyNo = taskQueryVo.getPolicyNo(); //保单号
		String licenseNo = taskQueryVo.getLicenseNo(); //标的车牌号
		String insuredName = taskQueryVo.getInsuredName(); //被保险人
		String claimNo = taskQueryVo.getClaimNo(); //立案号
		String compensateNo = taskQueryVo.getCompensateNo();//预付号
		if(((StringUtils.isBlank(registNo) ||registNo.length() < 4)
				&& (StringUtils.isBlank(policyNo) || policyNo.length() < 4)
				&& (StringUtils.isBlank(compensateNo) ||  compensateNo.length() < 21)
				&& (StringUtils.isBlank(claimNo) || claimNo.length() < 21)
				&& (StringUtils.isBlank(licenseNo) || licenseNo.length() < 4)
				&& (StringUtils.isBlank(insuredName) || insuredName.length() < 2))
				&& (taskQueryVo.getUnderwriteDateStart() == null || taskQueryVo.getUnderwriteDateEnd() == null ||(DateUtils.compareDays(taskQueryVo.getUnderwriteDateEnd(),taskQueryVo.getUnderwriteDateStart())>30))){
			return false;
		}
		return true;
	}
	
	/* 
	 * @see ins.sino.claimcar.pay.service.PayTaskQueryService#findByPreWriteOff(ins.sino.claimcar.claim.vo.PrpLCompensateVo, ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo, int, int)
	 * @param prpLCompensateVo
	 * @param prplwftaskqueryvo
	 * @param start
	 * @param length
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@Override
	public ResultPage<WfTaskQueryResultVo> findByPreWriteOff(PrpLCompensateVo prpLCompensateVo, PrpLWfTaskQueryVo prplwftaskqueryvo,
			int start, int length) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(!validate(prplwftaskqueryvo)){
			throw new IllegalArgumentException("核赔通过时间查询范围最大不能超过30天");
		}
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLWfTaskQuery query,PrpLCompensate compensate,PrpLCompensateExt ext where 1=1 ");
		sqlUtil.append(" and query.registNo=compensate.registNo");
		sqlUtil.append(" and ext.compensateNo=compensate.compensateNo");
		sqlUtil.append(" and (ext.oppoCompensateNo is null or"
				+ " exists ( select 1 from PrpLCompensateExt t, PrpLCompensate b where t.oppoCompensateNo = compensate.compensateNo"
				+ " and t.compensateNo = b.compensateNo and b.underwriteFlag= ? group by t.oppoCompensateNo having sum(abs(b.sumAmt)) < compensate.sumAmt))");//未发起过冲销
		sqlUtil.addParamValue("1");
		sqlUtil.append(" and compensate.underwriteFlag = ? ");//核赔通过
		sqlUtil.addParamValue("1");
		sqlUtil.append(" and compensate.compensateType = ? ");
		sqlUtil.addParamValue("Y");
	//	sqlUtil.append(" and compensate.sumAmt > 0 ");//排除冲销
		sqlUtil.append(" and (ext.isCompDeduct is null or ext.isCompDeduct = ?)"); //未被理算扣减
		sqlUtil.addParamValue("0");
		
		sqlUtil.andReverse(prplwftaskqueryvo,"query",7,"registNo","licenseNo");
		sqlUtil.andTopComSql("query","comCodePly",prplwftaskqueryvo.getComCode());
///		sqlUtil.andComSql("query","comCodePly",prpLCompensateVo.getComCode());
		sqlUtil.andEquals(prpLCompensateVo,"compensate","riskCode");
		if(StringUtils.isNotBlank(prpLCompensateVo.getCompensateNo())){
			if(prpLCompensateVo.getCompensateNo().replaceAll("\\s*", "").length() >= 21){
				sqlUtil.andEquals(prpLCompensateVo,"compensate","compensateNo");
			} else{
				sqlUtil.andLike(prpLCompensateVo,"compensate","compensateNo");
			}
		}
		if(StringUtils.isNotBlank(prpLCompensateVo.getClaimNo())){
			if(prpLCompensateVo.getClaimNo().replaceAll("\\s*", "").length() >= 21){
				sqlUtil.andEquals(prpLCompensateVo,"compensate","claimNo");
			} else{
				sqlUtil.andLike(prpLCompensateVo,"compensate","claimNo");
			}
		}
		sqlUtil.andEquals(prplwftaskqueryvo,"query","mercyFlag");
		sqlUtil.andLike2(prplwftaskqueryvo,"query","insuredName");
		prplwftaskqueryvo.setCompensateNo(prpLCompensateVo.getCompensateNo());
		prplwftaskqueryvo.setClaimNo(prpLCompensateVo.getClaimNo());
		if(addTimeControl(prplwftaskqueryvo)){
			//日期条件
			sqlUtil.andDate(prplwftaskqueryvo,"compensate","underwriteDate");
		}
		
		if("1".equals(prplwftaskqueryvo.getPrePayType())){//交强预付
			sqlUtil.append(" and compensate.riskCode = ? ");
			sqlUtil.addParamValue("1101");
		}else if("2".equals(prplwftaskqueryvo.getPrePayType())){//商业预付
			sqlUtil.append(" and compensate.riskCode <> ? ");
			sqlUtil.addParamValue("1101");
		}
		
		String policyNo = prplwftaskqueryvo.getPolicyNo();
		if(StringUtils.isNotBlank(policyNo)&&policyNo.length()>2){
			String policyNoRev = StringUtils.reverse(policyNo.toString()).trim();
			sqlUtil.append("AND (query.policyNoRev LIKE ? Or query.policyNoLink LIKE ? ) ");
			sqlUtil.addParamValue(policyNoRev+"%");
			sqlUtil.addParamValue(policyNo+"%");
		}
		
		// 排序
		sqlUtil.append(" Order By compensate.underwriteDate desc,query.damageTime");

		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		logger.info("预付冲销查询sql =" + sql);
		
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);



		// 对象转换
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){

			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
			Object[] obj = page.getResult().get(i);

			PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[0];
			Beans.copy().from(wfTaskQuery).to(resultVo);

			PrpLCompensate prpLCompensate = (PrpLCompensate)obj[1];
			Beans.copy().from(prpLCompensate).excludeNull().to(resultVo);
			
			List<PrpLPrePayVo> prePayVoList = compensateTaskService.findPrePayList(prpLCompensate.getCompensateNo());
			Double sumPay = 0d;//赔款费用总计
			Double sumFee = 0d;//费用总计
			if(prePayVoList != null && prePayVoList.size()>0){
				for(PrpLPrePayVo prePayVo : prePayVoList){
					if(FeeType.PAY.equals(prePayVo.getFeeType())){
						sumPay += prePayVo.getPayAmt().doubleValue();
					}else{
						sumFee += prePayVo.getPayAmt().doubleValue();
					}
				}
				
			}
			resultVo.setSumPay(sumPay);
			resultVo.setSumFee(sumFee);
			resultVo.setComCode(resultVo.getComCodePly());
			resultVo.setClaimNo(prpLCompensate.getClaimNo());
			resultVo.setUnderwriteDate(prpLCompensate.getUnderwriteDate());
			resultVo.setCompensateNo(prpLCompensate.getCompensateNo());
			resultVoList.add(resultVo);
		}

		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo> (start, length, page.getTotalCount(), resultVoList);
		return resultPage;

	}

}
