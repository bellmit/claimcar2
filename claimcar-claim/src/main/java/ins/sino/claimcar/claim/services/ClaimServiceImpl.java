package ins.sino.claimcar.claim.services;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.exception.BusinessException;
import ins.framework.utils.Beans;
import ins.platform.utils.DataUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.po.*;
import ins.sino.claimcar.claim.service.ClaimKindHisService;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimSummaryService;
import ins.sino.claimcar.claim.service.ConfigService;
import ins.sino.claimcar.claim.vo.*;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * @author CMQ
 *
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("claimService")
public class ClaimServiceImpl implements ClaimService {
	private static Logger logger = LoggerFactory.getLogger(ClaimServiceImpl.class);
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private ClaimTextService claimTextService;
	@Autowired
	private ClaimKindHisService claimKindHisService;

	@Autowired
	private ClaimSummaryService claimSummaryService;
	@Autowired
	private ConfigService configService;

	/*
	 * @see ins.sino.claimcar.claim.services.ClaimService#findByClaimNo(java.lang.String)
	 * @param claimNo
	 * @return
	 */
	@Override
	public PrpLClaimVo findByClaimNo(String claimNo) {
		PrpLClaimVo vo = null;
		Assert.notNull(claimNo,"id must have value.");
		PrpLClaim po = databaseDao.findByPK(PrpLClaim.class,claimNo);
		if(po != null){
			vo = new PrpLClaimVo();
			vo = Beans.copyDepth().from(po).to(PrpLClaimVo.class);
		}
		return vo;
	}

	/*
	 * @see ins.sino.claimcar.claim.services.ClaimService#findprpLClaimVoListByRegistAndPolicyNo(java.lang.String, java.lang.String, java.lang.String)
	 * @param registNo
	 * @param policyNo
	 * @param validFlag
	 * @return
	 */
	@Override
	public List<PrpLClaimVo> findprpLClaimVoListByRegistAndPolicyNo(String registNo,String policyNo,String validFlag){
		List<PrpLClaimVo> prpLClaimVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("policyNo",policyNo);
		if(validFlag != null){
			queryRule.addEqual("validFlag",validFlag);
		}
		List<PrpLClaim> prpLClaimList = databaseDao.findAll(PrpLClaim.class,queryRule);
		if(prpLClaimList != null && !prpLClaimList.isEmpty()){
			prpLClaimVoList = Beans.copyDepth().from(prpLClaimList).toList(PrpLClaimVo.class);
		}
		return prpLClaimVoList;
	}

	/*
	 * @see ins.sino.claimcar.claim.services.ClaimService#findClaimListByRegistNo(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public List<PrpLClaimVo> findClaimListByRegistNo(String registNo) {
		List<PrpLClaimVo> claimVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLClaim> claimList = databaseDao.findAll(PrpLClaim.class,queryRule);
		if(claimList!=null&&claimList.size()>0){
			claimVoList = new ArrayList<PrpLClaimVo>();
			claimVoList = Beans.copyDepth().from(claimList).toList(PrpLClaimVo.class);
		}
		return claimVoList;
	}


	/*
	 * @see ins.sino.claimcar.claim.services.ClaimService#saveClaim(ins.sino.claimcar.claim.vo.PrpLClaimVo)
	 * @param claimMainVo
	 * @return
	 */
	@Override
	public String saveClaim(PrpLClaimVo claimMainVo)  {
		PrpLClaim claimMainPo=new PrpLClaim();
		Beans.copy().from(claimMainVo).to(claimMainPo);

		List<PrpLClaimKind> prpLClaimKinds = new ArrayList<PrpLClaimKind>(0);
		List<PrpLClaimKindFee> prpLClaimKindFees = new ArrayList<PrpLClaimKindFee>(0);

		if(claimMainVo.getPrpLClaimKinds() != null){
			for(PrpLClaimKindVo prpLClaimKindVo:claimMainVo.getPrpLClaimKinds()){
				PrpLClaimKind kindPo=new PrpLClaimKind();
				Beans.copy().from(prpLClaimKindVo).excludeNull().to(kindPo);
				kindPo.setPrpLClaim(claimMainPo);
				prpLClaimKinds.add(kindPo);
			}
			claimMainPo.setPrpLClaimKinds(prpLClaimKinds);
		}

		if(claimMainVo.getPrpLClaimKindFees() != null){
			for(PrpLClaimKindFeeVo prpLClaimKindFeeVo : claimMainVo.getPrpLClaimKindFees()){
				PrpLClaimKindFee prpLClaimKindFee = new PrpLClaimKindFee();
				Beans.copy().from(prpLClaimKindFeeVo).excludeNull().to(prpLClaimKindFee);
				prpLClaimKindFee.setPrpLClaim(claimMainPo);
				prpLClaimKindFees.add(prpLClaimKindFee);
			}
			claimMainPo.setPrpLClaimKindFees(prpLClaimKindFees);
		}

		databaseDao.save(PrpLClaim.class, claimMainPo);

		//调用保存立案案件信息总览表的方法
		claimSummaryService.updateByClaim(claimMainVo);

		return claimMainPo.getClaimNo();
	}

	/*
	 * @see ins.sino.claimcar.claim.services.ClaimService#updateClaim(ins.sino.claimcar.claim.vo.PrpLClaimVo)
	 * @param claimMainVo
	 * @return
	 */
	@Override
	public String updateClaim(PrpLClaimVo claimMainVo){
		PrpLClaim claimMainPo = databaseDao.findByPK(PrpLClaim.class,claimMainVo.getClaimNo());
		Beans.copy().from(claimMainVo).to(claimMainPo);
		if(claimMainVo.getPrpLClaimKinds()!=null){

			if(claimMainPo.getPrpLClaimKinds()!=null&&claimMainPo.getPrpLClaimKinds().size()>0){
				for(PrpLClaimKindVo prpLClaimKindVo:claimMainVo.getPrpLClaimKinds()){
					if(prpLClaimKindVo.getId()==null){
						PrpLClaimKind claimKindAdd = new PrpLClaimKind();
						Beans.copy().from(prpLClaimKindVo).to(claimKindAdd);
						claimKindAdd.setPrpLClaim(claimMainPo);
						databaseDao.save(PrpLClaimKind.class,claimKindAdd);
					}else{
						for(PrpLClaimKind prpLClaimKind:claimMainPo.getPrpLClaimKinds()){
							if(prpLClaimKindVo.getId().equals(prpLClaimKind.getId())
									/*&&prpLClaimKindVo.getLossItemName().equals(prpLClaimKind.getLossItemName())*/){
								Beans.copy().from(prpLClaimKindVo).excludeNull().to(prpLClaimKind);
								continue;
							}
						}
					}
				}
			}else{
				List<PrpLClaimKind> claimKindPoList = new ArrayList<PrpLClaimKind>();
				for(PrpLClaimKindVo prpLClaimKindVo:claimMainVo.getPrpLClaimKinds()){
					PrpLClaimKind claimKindPo = new PrpLClaimKind();
					Beans.copy().from(prpLClaimKindVo).to(claimKindPo);
					claimKindPo.setPrpLClaim(claimMainPo);
					claimKindPoList.add(claimKindPo);

				}

				claimMainPo.setPrpLClaimKinds(claimKindPoList);
			}
		}

		if(claimMainVo.getPrpLClaimKindFees() != null){
			List<String> claimKindFeeKindList = new ArrayList<String>();
			for(PrpLClaimKindFee prpLClaimKindFee:claimMainPo.getPrpLClaimKindFees()){
				claimKindFeeKindList.add(prpLClaimKindFee.getKindCode()+prpLClaimKindFee.getFeeCode());
			}
			if(claimMainPo.getPrpLClaimKindFees()!=null&&claimMainPo.getPrpLClaimKindFees().size()>0){
				List<String> kindFeeCodeList = new ArrayList<String>();//变更的费用List
				for(PrpLClaimKindFeeVo prpLClaimKindFeeVo:claimMainVo.getPrpLClaimKindFees()){
					kindFeeCodeList.add(prpLClaimKindFeeVo.getKindCode()+prpLClaimKindFeeVo.getFeeCode());//变更新增费用
					for(PrpLClaimKindFee prpLClaimKindFee:claimMainPo.getPrpLClaimKindFees()){
						if(prpLClaimKindFeeVo.getId()!=null&&prpLClaimKindFeeVo.getId().equals(prpLClaimKindFee.getId())){
							Beans.copy().from(prpLClaimKindFeeVo).excludeNull().excludes("id").to(prpLClaimKindFee);
							continue;
						}
					}
					if( !claimKindFeeKindList.contains(prpLClaimKindFeeVo.getKindCode()+prpLClaimKindFeeVo.getFeeCode())){
						//如果没有该险别，新增
						PrpLClaimKindFee claimKindFeeAdd = new PrpLClaimKindFee();
						Beans.copy().from(prpLClaimKindFeeVo).to(claimKindFeeAdd);
						claimKindFeeAdd.setPrpLClaim(claimMainPo);
						databaseDao.save(PrpLClaimKindFee.class,claimKindFeeAdd);
					}
				}
				//如果变更后的费用记录中没有该记录，删除该条记录
				List<PrpLClaimKindFee> claimKindFeePoListTemp = new ArrayList<PrpLClaimKindFee>();
				for(PrpLClaimKindFee feeVo:claimMainPo.getPrpLClaimKindFees()){
					if(kindFeeCodeList.contains(feeVo.getKindCode()+feeVo.getFeeCode())){
						claimKindFeePoListTemp.add(feeVo);
					}else{
						databaseDao.deleteByObject(PrpLClaimKindFee.class,feeVo);
					}
				}
				claimMainPo.setPrpLClaimKindFees(claimKindFeePoListTemp);
			}else{
				List<PrpLClaimKindFee> claimKindFeePoList = new ArrayList<PrpLClaimKindFee>();
				for(PrpLClaimKindFeeVo prpLClaimKindFeeVo:claimMainVo.getPrpLClaimKindFees()){
					PrpLClaimKindFee claimKindFeePo = new PrpLClaimKindFee();
					Beans.copy().from(prpLClaimKindFeeVo).to(claimKindFeePo);
					claimKindFeePo.setPrpLClaim(claimMainPo);
					claimKindFeePoList.add(claimKindFeePo);

				}

				claimMainPo.setPrpLClaimKindFees(claimKindFeePoList);
			}

		}else{
			claimMainPo.setPrpLClaimKindFees(null);
		}


		databaseDao.update(PrpLClaim.class, claimMainPo);
		logger.debug("更新立案数据:"+claimMainPo.getClaimNo()+"claimKind记录数"+claimMainPo.getPrpLClaimKinds().size()
				+"claimKindFee记录条数"+claimMainPo.getPrpLClaimKindFees().size());
		return claimMainPo.getClaimNo();
	}

	/*
	 * @see ins.sino.claimcar.claim.services.ClaimService#findClaimKindVoListByRegistNo(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public List<PrpLClaimKindVo> findClaimKindVoListByRegistNo(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLClaimKind> claimKindPos = databaseDao.findAll(
				PrpLClaimKind.class, queryRule);
		List<PrpLClaimKindVo> claimKindVos = new ArrayList<PrpLClaimKindVo>();
		claimKindVos = Beans.copyDepth().from(claimKindPos)
				.toList(PrpLClaimKindVo.class);
		return claimKindVos;
	}

	/*
	 * @see ins.sino.claimcar.claim.services.ClaimService#findClaimVoByRegistNoAndPolicyNo(java.lang.String, java.lang.String)
	 * @param registNo
	 * @param policyNo
	 * @return
	 */
	@Override
	public PrpLClaimVo findClaimVoByRegistNoAndPolicyNo(String registNo,String policyNo){
		PrpLClaimVo prpLClaimVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("policyNo",policyNo);
		List<PrpLClaim> prpLClaimList = databaseDao.findAll(PrpLClaim.class,queryRule);
		if(prpLClaimList!=null&& !prpLClaimList.isEmpty()){
			prpLClaimVo = new PrpLClaimVo();
			PrpLClaim po = prpLClaimList.get(0);
			prpLClaimVo = Beans.copyDepth().from(po).to(PrpLClaimVo.class);
		}
		return prpLClaimVo;
	}

	/*
	 * @see ins.sino.claimcar.claim.services.ClaimService#findPrpLClaimSummaryByRegistNo(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public List<PrpLClaimSummaryVo> findPrpLClaimSummaryByRegistNo(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLClaimSummary> PrpLClaimSummaryList = databaseDao.findAll(PrpLClaimSummary.class,queryRule);
		List<PrpLClaimSummaryVo> PrpLClaimSummaryVoList = new ArrayList<PrpLClaimSummaryVo>();
		PrpLClaimSummaryVoList = Beans.copyDepth().from(PrpLClaimSummaryList).toList(PrpLClaimSummaryVo.class);
		return PrpLClaimSummaryVoList;
	}

	/*
	 * @see ins.sino.claimcar.claim.services.ClaimService#findPrpLClaimSummaryVoByRegistNo(java.lang.String)
	 * @param claimNo
	 * @return
	 */
	@Override
	public PrpLClaimSummaryVo findPrpLClaimSummaryVoByRegistNo(String claimNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo",claimNo);
		PrpLClaimSummary prpLClaimSummary = databaseDao.findByPK(PrpLClaimSummary.class, queryRule);
		PrpLClaimSummaryVo prpLClaimSummaryVo = new PrpLClaimSummaryVo();
		prpLClaimSummaryVo = Beans.copyDepth().from(prpLClaimSummary).to(PrpLClaimSummaryVo.class);
		return prpLClaimSummaryVo;
	}
	/*
	 * @see ins.sino.claimcar.claim.services.ClaimService#cancleClaimByClaimNo(java.lang.String, java.lang.String)
	 * @param claimNo
	 * @param validFlag
	 */
	@Override
	@SuppressWarnings("unused")
	public void cancleClaimByClaimNo(String claimNo,String validFlag,SysUserVo userVo){
		logger.info("立案号claimNo={}进入立案注销方法回写标志位validFlag",claimNo);
		PrpLClaim claimMainPo = databaseDao.findByPK(PrpLClaim.class,claimNo);
		System.out.println("----"+claimMainPo.getClaimNo()+"==="+claimMainPo.getValidFlag());
		String validFlags = claimMainPo.getValidFlag();
		/*if(claimMainPo == null || !"1".equals(claimMainPo.getValidFlag())){*/
		//if( (validFlags.equals("1")||claimMainPo == null)){
		if( claimMainPo == null){
			logger.debug("立案不存在（立案无效）,无法立案注销，立案号:" + claimNo);
			throw new BusinessException("立案不存在（立案无效）,无法立案注销，立案号:" + claimNo,false);
		}


		if(claimMainPo.getPrpLClaimKinds() != null){
			for(PrpLClaimKind prpLClaimKind:claimMainPo.getPrpLClaimKinds()){
				prpLClaimKind.setValidFlag(validFlag);
				prpLClaimKind.setUpdateTime(new Date());
				prpLClaimKind.setUpdateUser(userVo.getUserCode());
			}
		}

		if(claimMainPo.getPrpLClaimKindFees() != null){
			for(PrpLClaimKindFee prpLClaimKindFee : claimMainPo.getPrpLClaimKindFees()){
				prpLClaimKindFee.setValidFlag(validFlag);
				prpLClaimKindFee.setUpdateTime(new Date());
				prpLClaimKindFee.setUpdateUser(userVo.getUserCode());
			}
		}
		claimMainPo.setCancelCode(validFlag);
		claimMainPo.setCancelCom(userVo.getComCode());
		if("1".equals(validFlag)){
			claimMainPo.setCancelTime(null);
		}else{
			claimMainPo.setCancelTime(new Date());
		}

		claimMainPo.setUpdateTime(new Date());
		claimMainPo.setUpdateUser(userVo.getUserCode());
		//claimMainPo.setValidFlag("0");
		claimMainPo.setValidFlag(validFlag);
		logger.info("立案号claimNo={}结束立案注销方法回写标志位validFlag",claimNo);
	}
	/*
	 * @see ins.sino.claimcar.claim.services.ClaimService#saveClaimModifity(ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo, java.util.List, java.util.List)
	 * @param claimTextVo
	 * @param claimKindVoList
	 * @param claimKindFeeVoList
	 */
	@Override
	public void saveClaimModifity(PrpLClaimTextVo claimTextVo,List<PrpLClaimKindVo> claimKindVoList,List<PrpLClaimKindFeeVo> claimKindFeeVoList){
		if(claimTextVo!=null){
			claimTextService.saveOrUpdte(claimTextVo);
		}
		PrpLClaim claimPo = databaseDao.findByPK(PrpLClaim.class,claimTextVo.getBussNo());

		if(claimKindVoList!=null&&claimKindVoList.size()>0){
			for(PrpLClaimKindVo claimKindVoNew:claimKindVoList){
				for(PrpLClaimKind claimKindOld:claimPo.getPrpLClaimKinds()){
					if(claimKindOld.getId().equals(claimKindVoNew.getId())){
						//只允許修改 估计赔款  调整原因
						Beans.copy().from(claimKindVoNew).excludeNull().to(claimKindOld);
					}
				}
			}
		}
		if(claimKindFeeVoList!=null&&claimKindFeeVoList.size()>0){
			for(PrpLClaimKindFeeVo claimKindFeeVoNew:claimKindFeeVoList){
				for(PrpLClaimKindFee claimKindFeeOld:claimPo.getPrpLClaimKindFees()){
					if(claimKindFeeOld.getId().equals(claimKindFeeVoNew.getId())){
						//只允許修改 费用金额，调整原因
						Beans.copy().from(claimKindFeeVoNew).excludeNull().to(claimKindFeeOld);
					}
				}
			}
		}

		//修改主表的立案估损金额
		getSumLossForPrplClaim(claimPo);

		claimPo.setEstiTimes(claimPo.getEstiTimes()+1);//调整次数加一
		databaseDao.update(PrpLClaim.class,claimPo);
		// liuping 2016年6月3日 保存立案信息后，计算并保存立案轨迹信息
		PrpLClaimVo claimVo = Beans.copyDepth().from(claimPo).to(PrpLClaimVo.class);

		claimKindHisService.saveClaimKindHis(claimVo);

	}
	/**
	 * 汇总估计赔款金额赋值到主表
	 * <pre></pre>
	 * @param claimVo
	 * @modified:
	 * ☆WLL(2016年7月20日 上午9:39:47): <br>
	 */
	private void getSumLossForPrplClaim(PrpLClaim claimPo){
		if(claimPo.getPrpLClaimKinds()!=null&&claimPo.getPrpLClaimKinds().size()>0){
			BigDecimal sumClaim = BigDecimal.ZERO;
			BigDecimal sumLoss = BigDecimal.ZERO;
			for(PrpLClaimKind claimKind:claimPo.getPrpLClaimKinds()){
				sumClaim = sumClaim.add(DataUtils.NullToZero(claimKind.getClaimLoss()));
				sumLoss = sumLoss.add(DataUtils.NullToZero(claimKind.getDefLoss()));
				//包括施救费
				sumClaim = sumClaim.add(DataUtils.NullToZero(claimKind.getRescueFee()));
				sumLoss = sumLoss.add(DataUtils.NullToZero(claimKind.getRescueFee()));
			}
			claimPo.setSumClaim(sumClaim);
			claimPo.setSumDefLoss(sumLoss);
		}
	}


	/*
	 * @see ins.sino.claimcar.claim.services.ClaimService#findClaimListByRegistNo(java.lang.String, java.lang.String)
	 * @param registNo
	 * @param validFlag
	 * @return
	 */
	@Override
	public List<PrpLClaimVo> findClaimListByRegistNo(String registNo,String validFlag) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("validFlag",validFlag);
		List<PrpLClaim> prpLClaimList = databaseDao.findAll(PrpLClaim.class,queryRule);
		List<PrpLClaimVo> prpLClaimVoList = new ArrayList<PrpLClaimVo>();
		if(prpLClaimList!=null){
			prpLClaimVoList = Beans.copyDepth().from(prpLClaimList).toList(PrpLClaimVo.class);
		}else{
			return null;
		}
		return prpLClaimVoList;
	}

	/*
	 * @see ins.sino.claimcar.claim.services.ClaimService#adjustCaseHadEnd(java.lang.String)
	 * @param claimNo
	 * @return
	 */
	@Override
	public String adjustCaseHadEnd(String claimNo) {
		PrpLClaimVo claimVo = this.findByClaimNo(claimNo);
		String caseStatus = "noEnd";
		if(claimVo!=null){
			if(claimVo.getEndCaseTime()!=null){
				// 结案时间不为空则已结案
				caseStatus = "end";
			}
		}
		return caseStatus;
	}


	@Override
	public ResultPage<PrpLClaimVo> findCancelAppTaskQuery(PrpLWfTaskQueryVo taskQueryVo) throws Exception {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();

		sqlUtil.append(" FROM PrpLClaim prpLClaim ");
		sqlUtil.append(" where  1=1  AND prpLClaim.validFlag !=?");
		sqlUtil.addParamValue("0");

		if(StringUtils.isNotBlank(taskQueryVo.getClaimNo())){
			sqlUtil.append(" and (prpLClaim.claimNo = ? ) ");
			sqlUtil.addParamValue(taskQueryVo.getClaimNo());
		}
		if(StringUtils.isNotBlank(taskQueryVo.getRegistNo())){
			sqlUtil.append(" and (prpLClaim.registNo = ? ) ");
			sqlUtil.addParamValue(taskQueryVo.getRegistNo());
		}
		if(StringUtils.isNotBlank(taskQueryVo.getPolicyNo())){
			sqlUtil.append(" and (prpLClaim.policyNo = ? ) ");
			sqlUtil.addParamValue(taskQueryVo.getPolicyNo());
		}



		/*if(StringUtils.isNotBlank(taskQueryVo.getLicenseNo())){
			sqlUtil.append(" and (prpLClaim.licenseNo = ? ) ");
			sqlUtil.addParamValue(taskQueryVo.getLicenseNo());
		}*/

		//开始=======================
	/*	sqlUtil.append("  and ( (exists(select 1 from PrpLcancelTrace  a where a.claimNo = prpLClaim.claimNo) ) ");

		sqlUtil.append("  or (not exists(select 1 from PrpLcancelTrace  a where a.claimNo = prpLClaim.claimNo and a.flags=? and a.textType=? order by inputTime Desc)) ) ");
		sqlUtil.addParamValue("4");
		sqlUtil.addParamValue("02 ");*/


		//结束====================
		/*sqlUtil.append("  and (((not exists(select 1 from PrpLWfTaskIn  a where a.claimNo = prpLClaim.claimNo and a.subNodeCode=? )  and "
				+ "not exists(select 1 from PrpLWfTaskOut  b where b.claimNo = prpLClaim.claimNo and b.subNodeCode=? ) ) ");
		sqlUtil.addParamValue(FlowNode.CancelApp.toString());
		sqlUtil.addParamValue(FlowNode.CancelApp.toString());


		sqlUtil.append(" or ((exists(select 1 from PrpLWfTaskOut  d ,PrpLcancelTrace c where d.claimNo = prpLClaim.claimNo and prpLClaim.claimNo=c.claimNo"
				+ " and d.subNodeCode=? and c.flags=?  ) )and (not exists(select 1 from PrpLWfTaskIn  f where f.claimNo = prpLClaim.claimNo and f.subNodeCode=? ))  )))");
		sqlUtil.addParamValue(FlowNode.ReCanApp.toString());
		sqlUtil.addParamValue("4");
		sqlUtil.addParamValue(FlowNode.CancelApp.toString());*/
		Calendar c = Calendar.getInstance();
		c.setTime(taskQueryVo.getTaskInTimeEnd());   //设置当前日期
		c.add(Calendar.DATE, 1); //日期加1天
		Date taskInTimeEnd = new Date();
		taskInTimeEnd = c.getTime();
		sqlUtil.append(" and (prpLClaim.claimTime >= ? and prpLClaim.claimTime<?) ");
		sqlUtil.addParamValue(taskQueryVo.getTaskInTimeStart());
		sqlUtil.addParamValue(taskInTimeEnd);

		/*sqlUtil.append(" and (prpLClaim.claimTime BETWEEN ? and ? ) ");
		sqlUtil.addParamValue(taskQueryVo.getTaskInTimeStart());
		sqlUtil.addParamValue(taskQueryVo.getTaskInTimeEnd());*/
		//sqlUtil.andDate(taskQueryVo,"prpLClaim","taskInTime");

		//sqlUtil.andDate(taskQueryVo,"task","taskInTime");
		// 开始记录数
		int start = taskQueryVo.getStart();
		// 查询记录数量
		int length = taskQueryVo.getLength();
		System.out.println(start+"--321321--"+length);
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.debug("taskQueryVo.getNodeCode()========"+taskQueryVo.getNodeCode());
		logger.debug("taskQueryVo.getSubNodeCode()========"+taskQueryVo.getSubNodeCode());
		System.out.println("taskQrySql="+sql);
		System.out.println("ParamValues="+ArrayUtils.toString(values));/**/
		List<PrpLClaim> page = databaseDao.findAllByHql(PrpLClaim.class,sql,values);
		//Page<PrpLClaim> page = databaseDao.findPageByHql(PrpLClaim.class,sql,start/length+1,length,values);

		// 对象转换
		List<PrpLClaimVo> resultVoList=new ArrayList<PrpLClaimVo>();
		List<PrpLClaimVo> resultVoListResult=new ArrayList<PrpLClaimVo>();
		List<PrpLClaimVo> resultVoList1=new ArrayList<PrpLClaimVo>();
		for(int i = 0; i<page.size(); i++ ){

			PrpLClaimVo resultVo=new PrpLClaimVo();
			/*Object[] obj = page.getResult().get(i);

			PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[0];
			Beans.copy().from(wfTaskQuery).to(resultVo);

			PrpLClaim prpLClaim = (PrpLClaim)obj[0];*/
			Beans.copy().from(page.get(i)).excludeNull().to(resultVo);
			QueryRule queryRule1=QueryRule.getInstance();
			queryRule1.addEqual("claimNo", page.get(i).getClaimNo());
			queryRule1.addDescOrder("inputTime");
			List<PrpLcancelTrace> prpLcancelTraceList= databaseDao.findAll(PrpLcancelTrace.class,queryRule1);

			if(prpLcancelTraceList==null ||prpLcancelTraceList.size()==0){
				QueryRule queryRule = QueryRule.getInstance();
				queryRule.addEqual("policyNo",page.get(i).getPolicyNo());
				queryRule.addEqual("registNo",page.get(i).getRegistNo());
				PrpLCMain prpLCMain = databaseDao.findUnique(PrpLCMain.class, queryRule);
				resultVo.setInsuredName(prpLCMain.getInsuredName());
				resultVo.setStartDate(prpLCMain.getStartDate());
				resultVo.setEndDate(prpLCMain.getEndDate());
				resultVo.setRegistNo(prpLCMain.getRegistNo());
				resultVoList.add(resultVo);
			}else{
				if(prpLcancelTraceList.get(0).getFlags()!=null){
					if("4".equals(prpLcancelTraceList.get(0).getFlags().trim()) || "11".equals(prpLcancelTraceList.get(0).getFlags().trim())){
						QueryRule queryRule = QueryRule.getInstance();
						queryRule.addEqual("policyNo",page.get(i).getPolicyNo());
						queryRule.addEqual("registNo",page.get(i).getRegistNo());
						PrpLCMain prpLCMain = databaseDao.findUnique(PrpLCMain.class, queryRule);
						resultVo.setInsuredName(prpLCMain.getInsuredName());
						resultVo.setStartDate(prpLCMain.getStartDate());
						resultVo.setEndDate(prpLCMain.getEndDate());
						resultVo.setRegistNo(prpLCMain.getRegistNo());
						resultVoList.add(resultVo);
					}
				}
			}

		}
		resultVoListResult.addAll(resultVoList);

		int nowPage = start/length;//当前页数的前一页
		int end = 0;
		if(nowPage == resultVoListResult.size()/length){
			end = resultVoListResult.size()/length*length+resultVoListResult.size()%length;
		}else{
			end = start+length;
		}
		//System.out.println(end+"---------"+resultVoListResult.size());
		for(;start < end;start++){
			resultVoList1.add(resultVoListResult.get(start));
		}
		//System.out.println("----yyyy-----"+resultVoList1.size()+"======"+start+"kk"+length);
		ResultPage<PrpLClaimVo> resultPage = new ResultPage<PrpLClaimVo> (start, length,resultVoListResult.size(), resultVoList1);
		return resultPage;

	}

	@Override
	public ResultPage<PrpLClaimVo> findRecanAppTaskQuery(
			PrpLWfTaskQueryVo taskQueryVo) throws Exception {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		// 权限控制的表放到前面
		sqlUtil.append(" FROM PrpLClaim prpLClaim ");
		sqlUtil.append(" where  1=1  AND prpLClaim.validFlag =?");
		sqlUtil.addParamValue("0");

		if(StringUtils.isNotBlank(taskQueryVo.getClaimNo())){
			sqlUtil.append(" and (prpLClaim.claimNo = ? ) ");
			sqlUtil.addParamValue(taskQueryVo.getClaimNo());
		}

		if(StringUtils.isNotBlank(taskQueryVo.getRegistNo())){
			sqlUtil.append(" and (prpLClaim.registNo = ? ) ");
			sqlUtil.addParamValue(taskQueryVo.getRegistNo());
		}
		if(StringUtils.isNotBlank(taskQueryVo.getPolicyNo())){
			sqlUtil.append(" and (prpLClaim.policyNo = ? ) ");
			sqlUtil.addParamValue(taskQueryVo.getPolicyNo());
		}
		/*sqlUtil.append("  and (((not exists(select 1 from PrpLWfTaskIn  a where a.claimNo = prpLClaim.claimNo and a.subNodeCode=? )  and "
				+ "not exists(select 1 from PrpLWfTaskOut  b where b.claimNo = prpLClaim.claimNo and b.subNodeCode=? ) ) ");
		sqlUtil.addParamValue(FlowNode.ReCanApp.toString());
		sqlUtil.addParamValue(FlowNode.ReCanApp.toString());


		sqlUtil.append(" or ((exists(select 1 from PrpLWfTaskOut  d ,PrpLcancelTrace c where d.claimNo = prpLClaim.claimNo and prpLClaim.claimNo=c.claimNo"
				+ " and d.subNodeCode=? and c.flags =?  ) )and (not exists(select 1 from PrpLWfTaskIn  f where f.claimNo = prpLClaim.claimNo and f.subNodeCode=? ))  )))");
		sqlUtil.addParamValue(FlowNode.CancelApp.toString());
		sqlUtil.addParamValue("5");
		sqlUtil.addParamValue(FlowNode.ReCanApp.toString());
		sqlUtil.append(" and (prpLClaim.claimTime >= ? and prpLClaim.claimTime< ?) ");
		sqlUtil.addParamValue(taskQueryVo.getTaskInTimeStart());
		sqlUtil.addParamValue(taskQueryVo.getTaskInTimeEnd());*/
		Calendar c = Calendar.getInstance();
		c.setTime(taskQueryVo.getTaskInTimeEnd());   //设置当前日期
		c.add(Calendar.DATE, 1); //日期加1天
		Date taskInTimeEnd = new Date();
		taskInTimeEnd = c.getTime();
		sqlUtil.append(" and (prpLClaim.claimTime >= ? and prpLClaim.claimTime<?) ");
		sqlUtil.addParamValue(taskQueryVo.getTaskInTimeStart());
		sqlUtil.addParamValue(taskInTimeEnd);
		// 开始记录数
		int start = taskQueryVo.getStart();
		// 查询记录数量
		int length = taskQueryVo.getLength();
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		System.out.println("taskQrySql="+sql);
		System.out.println("ParamValues="+ArrayUtils.toString(values));/**/
		List<PrpLClaim> page = databaseDao.findAllByHql(PrpLClaim.class,sql,values);
		//Page<PrpLClaim> page = databaseDao.findPageByHql(PrpLClaim.class,sql,start/length+1,length,values);
		// 对象转换
		List<PrpLClaimVo> resultVoList=new ArrayList<PrpLClaimVo>();
		List<PrpLClaimVo> resultVoListResult=new ArrayList<PrpLClaimVo>();
		List<PrpLClaimVo> resultVoList1=new ArrayList<PrpLClaimVo>();
		for(int i = 0; i<page.size(); i++ ){
			PrpLClaimVo resultVo=new PrpLClaimVo();
			Beans.copy().from(page.get(i)).excludeNull().to(resultVo);
			/*QueryRule queryRule = QueryRule.getInstance();
			queryRule.addEqual("policyNo",page.getResult().get(i).getPolicyNo());
			queryRule.addEqual("registNo",page.getResult().get(i).getRegistNo());
			PrpLCMain prpLCMain = databaseDao.findUnique(PrpLCMain.class, queryRule);
			resultVo.setInsuredName(prpLCMain.getInsuredName());
			resultVo.setStartDate(prpLCMain.getStartDate());
			resultVo.setEndDate(prpLCMain.getEndDate());
			resultVoList.add(resultVo);*/
			QueryRule queryRule1=QueryRule.getInstance();
			queryRule1.addEqual("claimNo", page.get(i).getClaimNo());
			queryRule1.addDescOrder("inputTime");
			List<PrpLcancelTrace> prpLcancelTraceList= databaseDao.findAll(PrpLcancelTrace.class,queryRule1);

			if(prpLcancelTraceList==null ||prpLcancelTraceList.size()==0){
				QueryRule queryRule = QueryRule.getInstance();
				queryRule.addEqual("policyNo",page.get(i).getPolicyNo());
				queryRule.addEqual("registNo",page.get(i).getRegistNo());
				PrpLCMain prpLCMain = databaseDao.findUnique(PrpLCMain.class, queryRule);
				resultVo.setInsuredName(prpLCMain.getInsuredName());
				resultVo.setStartDate(prpLCMain.getStartDate());
				resultVo.setEndDate(prpLCMain.getEndDate());
				resultVo.setRegistNo(prpLCMain.getRegistNo());
				resultVoList.add(resultVo);
			}else{
				if(prpLcancelTraceList.get(0).getFlags()!=null){
					if("5".equals(prpLcancelTraceList.get(0).getFlags().trim())){
						QueryRule queryRule = QueryRule.getInstance();
						queryRule.addEqual("policyNo",page.get(i).getPolicyNo());
						queryRule.addEqual("registNo",page.get(i).getRegistNo());
						PrpLCMain prpLCMain = databaseDao.findUnique(PrpLCMain.class, queryRule);
						resultVo.setInsuredName(prpLCMain.getInsuredName());
						resultVo.setStartDate(prpLCMain.getStartDate());
						resultVo.setEndDate(prpLCMain.getEndDate());
						resultVo.setRegistNo(prpLCMain.getRegistNo());
						resultVoList.add(resultVo);
					}
				}
			}
		}
		resultVoListResult.addAll(resultVoList);
		int nowPage = start/length;//当前页数的前一页
		int end = 0;
		if(nowPage == resultVoListResult.size()/length){
			end = resultVoListResult.size()/length*length+resultVoListResult.size()%length;
		}else{
			end = start+length;
		}
		for(;start < end;start++){
			resultVoList1.add(resultVoListResult.get(start));
		}
		ResultPage<PrpLClaimVo> resultPage = new ResultPage<PrpLClaimVo> (start, length,resultVoListResult.size(), resultVoList1);
		return resultPage;
	}

	@Override
	public List<PrpLClaimVo> findSumClaimByPolicyNo(String policyNo)throws Exception {
		// TODO Auto-generated method stub
		List<PrpLClaimVo> claimVoList =null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("policyNo", policyNo);
		queryRule.addIsNull("endCaseTime");
		queryRule.addIsNull("cancelTime");
		List<PrpLClaim> claimList = databaseDao.findAll(PrpLClaim.class, queryRule);

		if(claimList!=null && !claimList.isEmpty()){
			claimVoList = new ArrayList<PrpLClaimVo>();
			claimVoList = Beans.copyDepth().from(claimList).toList(PrpLClaimVo.class);
		}

		return claimVoList;
	}

	/**
	 * 获得人伤刷新的最新一次立案轨迹
	 * <pre></pre>
	 * @modified:
	 * ☆WLL(2017年4月7日 下午3:29:54): <br>
	 */
	@Override
	public List<PrpLClaimKindHisVo> findPLossKindHisNewest(String registNo){
		List<PrpLClaimKindHisVo> claimKindHisVoList = new ArrayList<PrpLClaimKindHisVo>();
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addLike("adjustReason","人伤%");
		queryRule.addDescOrder("estiTimes");
		List<PrpLClaimKindHis> claimKindHisPoList = databaseDao.findAll(PrpLClaimKindHis.class, queryRule);

		if(claimKindHisPoList!=null && !claimKindHisPoList.isEmpty()){
			// 获取人伤最新调整次数
			Integer estiTimesNew = claimKindHisPoList.get(0).getEstiTimes();
			for(PrpLClaimKindHis claimKindHisPo : claimKindHisPoList){
				// 获取该调整次数下的所有轨迹记录
				if(estiTimesNew == claimKindHisPo.getEstiTimes()){
					PrpLClaimKindHisVo hisVo = new PrpLClaimKindHisVo();
					Beans.copy().from(claimKindHisPo).to(hisVo);
					claimKindHisVoList.add(hisVo);
				}
			}
		}

		return claimKindHisVoList;

	}

	@Override
	public List<PrpLClaimVo> findPrpLClaimVosByPolicyNo(String policyNo) {
		// TODO Auto-generated method stub
		List<PrpLClaimVo> claimVoList =null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("policyNo", policyNo);
		List<PrpLClaim> claimList = databaseDao.findAll(PrpLClaim.class, queryRule);

		if(claimList!=null && !claimList.isEmpty()){
			claimVoList = new ArrayList<PrpLClaimVo>();
			claimVoList = Beans.copyDepth().from(claimList).toList(PrpLClaimVo.class);
		}

		return claimVoList;
	}

	@Override
	public Boolean findNotCaseByRegistNo(String registNo) {
		Boolean notCase = false;//默认结案
		if(StringUtils.isNotBlank(registNo)){
			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addEqual("registNo",registNo);
			//queryRule.addIsNull("caseNo");
			List<PrpLClaim> claimList = databaseDao.findAll(PrpLClaim.class,queryRule);
			if(claimList != null && claimList.size() > 0){
				for(PrpLClaim lClaim : claimList){
					if(StringUtils.isEmpty(lClaim.getCaseNo())){
						notCase = true;//未结案
						break;
					}
				}
			}else{
				notCase = true;//未结案
			}
		}else{
			notCase = true;//未结案
		}
		return notCase;
	}

	@Override
	public PrpLClaimVo findClaimVoByClassCode(String registNo,String classCode){
		PrpLClaimVo claimVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("classCode",classCode);
		List<PrpLClaim> claimList = databaseDao.findAll(PrpLClaim.class,queryRule);
		if(claimList!=null&&claimList.size()>0){
			claimVo = new PrpLClaimVo();
			claimVo = Beans.copyDepth().from(claimList.get(0)).to(PrpLClaimVo.class);
		}
		return claimVo;
	}

	@Override
	public PrplcodeDictVo findPrplcodeDictByCodeAndcodeType(String codeType,String codeCode) {
		PrplcodeDictVo prplcodeDictVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("codeType",codeType);
		queryRule.addEqual("wcode",codeCode);
		List<PrplcodeDict> dictList = databaseDao.findAll(PrplcodeDict.class,queryRule);
		if(dictList!=null && dictList.size()>0){
			prplcodeDictVo = new PrplcodeDictVo();
			prplcodeDictVo = Beans.copyDepth().from(dictList.get(0)).to(PrplcodeDictVo.class);
		}
		return prplcodeDictVo;
		
	}

	@Override
	public List<PrpLDLimitVo> findPrpLDLimitList(String ciindemDuty, String registNo){
		return configService.findPrpLDLimitList(ciindemDuty,registNo);
	}
}
