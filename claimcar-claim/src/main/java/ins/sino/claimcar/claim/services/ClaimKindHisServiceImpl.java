/******************************************************************************
* CREATETIME : 2016年6月3日 上午11:35:45
******************************************************************************/
package ins.sino.claimcar.claim.services;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.FeeType;
import ins.sino.claimcar.CodeConstants.ValidFlag;
import ins.sino.claimcar.claim.po.PrpLClaim;
import ins.sino.claimcar.claim.po.PrpLClaimKindHis;
import ins.sino.claimcar.claim.service.ClaimKindHisService;
import ins.sino.claimcar.claim.vo.PrpLClaimKindFeeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindHisVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.flow.constant.FlowNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * 立案轨迹数据处理
 * @author ★LiuPing
 * @CreateTime 2016年6月3日
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("claimKindHisService")
public class ClaimKindHisServiceImpl implements ClaimKindHisService {

	private Logger logger = LoggerFactory.getLogger(ClaimKindHisService.class);
	@Autowired
	private DatabaseDao databaseDao;
	

	// 查立案险别金融表
	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimKindHisService#findPrpLClaimKindHisByRegistNo(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public List<PrpLClaimKindHisVo> findPrpLClaimKindHisByRegistNo(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLClaimKindHis> prpLClaimKindHisList = databaseDao.findAll(PrpLClaimKindHis.class,queryRule);
		List<PrpLClaimKindHisVo> PrpLClaimKindHisVoList = new ArrayList<PrpLClaimKindHisVo>();
		PrpLClaimKindHisVoList = Beans.copyDepth().from(prpLClaimKindHisList).toList(PrpLClaimKindHisVo.class);
		return PrpLClaimKindHisVoList;
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimKindHisService#calcClaimKindHisByReOpen(java.lang.String)
	 * @param claimNo
	 */
	@Override
	public void calcClaimKindHisByReOpen(String claimNo) {
		logger.debug("重开赔案刷新立案历史轨迹------------>");
		List<PrpLClaimKindHis> claimKindHisPoList = findKindHisPoList(claimNo,"1");
		PrpLClaim claimPo = databaseDao.findByPK(PrpLClaim.class,claimNo);
		if(claimPo != null){
			claimPo.setEstiTimes((claimPo.getEstiTimes()!=null ? claimPo.getEstiTimes():1)+1);// 调整次数加一
		}
		
		// 复制对象，将所有金额都set为0
		for(PrpLClaimKindHis oldClaimKindHis:claimKindHisPoList){
			PrpLClaimKindHis newClaimKindHis = new PrpLClaimKindHis();
			Beans.copy().from(oldClaimKindHis).excludes("id").to(newClaimKindHis);
			if(claimPo != null){
				newClaimKindHis.setEstiTimes(claimPo.getEstiTimes());
			}else{
				newClaimKindHis.setEstiTimes(1);
			}
			newClaimKindHis.setAdjustReason(FlowNode.ReOpen.getName());
			newClaimKindHis.setDefLoss(BigDecimal.ZERO);
			newClaimKindHis.setDefLossChg(BigDecimal.ZERO);
			newClaimKindHis.setRescueFee(BigDecimal.ZERO);
			newClaimKindHis.setRescueFeeChg(BigDecimal.ZERO);
			newClaimKindHis.setClaimLoss(BigDecimal.ZERO);
			newClaimKindHis.setClaimLossChg(BigDecimal.ZERO);
			newClaimKindHis.setCreateTime(new Date());
			
			oldClaimKindHis.setValidFlag(ValidFlag.INVALID);// 将旧的这条数据改为无效
			
			databaseDao.save(PrpLClaimKindHis.class,newClaimKindHis);
		}
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimKindHisService#calcClaimKindHisByEndCase(java.lang.String)
	 * @param claimNo
	 */
	@Override
	public void calcClaimKindHisByEndCase(String claimNo) {
		logger.debug("结案刷新立案历史轨迹------------>");
		List<PrpLClaimKindHis> claimKindHisPoList = findKindHisPoList(claimNo,"1");
		PrpLClaim claimPo = databaseDao.findByPK(PrpLClaim.class,claimNo);
		claimPo.setEstiTimes(claimPo.getEstiTimes()+1);// 调整次数加一
		// 复制对象，将变化量等于赔款的负数
		for(PrpLClaimKindHis oldClaimKindHis:claimKindHisPoList){
			PrpLClaimKindHis newClaimKindHis = new PrpLClaimKindHis();
			Beans.copy().from(oldClaimKindHis).excludes("id").to(newClaimKindHis);
			newClaimKindHis.setAdjustReason(FlowNode.EndCas.getName());
			//估损金额不变，施救费不变，对应变化量不变
			//估计赔款不变，估计赔款变化量变成赔款的负值
			newClaimKindHis.setEstiTimes(claimPo.getEstiTimes());
			newClaimKindHis.setClaimLoss(oldClaimKindHis.getClaimLoss());
			newClaimKindHis.setClaimLossChg(oldClaimKindHis.getClaimLoss().negate());
			newClaimKindHis.setCreateTime(new Date());
			
			oldClaimKindHis.setValidFlag(ValidFlag.INVALID);// 将旧的这条数据改为无效
			
			databaseDao.save(PrpLClaimKindHis.class,newClaimKindHis);
		}
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimKindHisService#findAllKindHisVoList(java.lang.String)
	 * @param claimNo
	 * @return
	 */
	@Override
	public List<PrpLClaimKindHisVo> findAllKindHisVoList(String claimNo) {
		return findKindHisVoList(claimNo,null);
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimKindHisService#findKindHisVoList(java.lang.String, java.lang.String)
	 * @param claimNo
	 * @param validFlag
	 * @return
	 */
	@Override
	public List<PrpLClaimKindHisVo> findKindHisVoList(String claimNo,String validFlag) {
		List<PrpLClaimKindHisVo> kindHisVoList = null;
		List<PrpLClaimKindHis> claimKindHisPoList = findKindHisPoList(claimNo,validFlag);
		if(claimKindHisPoList!=null&& !claimKindHisPoList.isEmpty()){
			kindHisVoList = Beans.copyDepth().from(claimKindHisPoList).toList(PrpLClaimKindHisVo.class);
		}
		return kindHisVoList;
	}

	/**
	 * 查询立案估损轨迹信息Po
	 * @param claimNo
	 * @param validFlag 有效状态 1有效0-无效，null-全部
	 * @return
	 * @modified: ☆LiuPing(2016年6月3日 ): <br>
	 */
	private List<PrpLClaimKindHis> findKindHisPoList(String claimNo,String validFlag) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo",claimNo);
		if(validFlag!=null){
			queryRule.addEqual("validFlag",validFlag);
		}
		queryRule.addDescOrder("estiTimes").addAscOrder("kindCode");
		List<PrpLClaimKindHis> claimKindHisPoList = databaseDao.findAll(PrpLClaimKindHis.class,queryRule);
		return claimKindHisPoList;
	}
	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimKindHisService#saveClaimKindHis(ins.sino.claimcar.claim.vo.PrpLClaimVo)
	 * @param claimVo
	 */
	@Override
	public void saveClaimKindHis(PrpLClaimVo claimVo) {
		/* liuping 2016年6月3日 处理立案轨迹逻辑
		 有效的PrpLClaimKindHis为null，表示第一次立案 ，变化量=当前值，PrpLClaimKindHis的数据根据PrpLClaimKind和PrpLClaimFee 生成。
		 有效的PrpLClaimKindHis有数据，变化量需要计算，用当前prpLClaimVo的（PrpLClaimKind和PrpLClaimFee）-PrpLClaimKindHis
		 要注意：新的立案去掉了某个费用，轨迹表还是需要相应的处理
		 */
		// 最新的 PrpLClaimKindHisPo
		List<PrpLClaimKindHis> newKindHisPoList = new ArrayList<PrpLClaimKindHis>();

		String claimNo = claimVo.getClaimNo();

		// 查询当前立案有效的轨迹信息
		List<PrpLClaimKindHis> oldKindHisPoList = findKindHisPoList(claimNo,ValidFlag.VALID);

		// 先将旧的Kind信息放到MAP，// key=FeeType+KindCode+LossItemNo/FeeCode
		Map<String,PrpLClaimKindHis> oldKindHisPoMap = new HashMap<String,PrpLClaimKindHis>();

		if(oldKindHisPoList!=null && !oldKindHisPoList.isEmpty()){

			for(PrpLClaimKindHis oldKindHisPo:oldKindHisPoList){
				String key = null;
				if("F".equals(oldKindHisPo.getFeeType())){
					key = oldKindHisPo.getFeeType()+oldKindHisPo.getKindCode()+oldKindHisPo.getLossItemName();
				}else{
					if(CodeConstants.KINDCODE.KINDCODE_B.equals(oldKindHisPo.getKindCode())){
						key = oldKindHisPo.getFeeType()+oldKindHisPo.getKindCode()+oldKindHisPo.getLossItemName();
					}else{
						key = oldKindHisPo.getFeeType()+oldKindHisPo.getKindCode()+oldKindHisPo.getLossItemNo();
					}
				}
				oldKindHisPoMap.put(key,oldKindHisPo);
			}
		}
		
		// 根据 PrpLClaimKind 处理轨迹信息
		List<PrpLClaimKindVo> claimKindVoList = claimVo.getPrpLClaimKinds();
		if(claimKindVoList!=null && !claimKindVoList.isEmpty()){
			for(PrpLClaimKindVo claimKindVo:claimKindVoList){
				String key = null;
				if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimKindVo.getKindCode())){
					key = FeeType.PAY+claimKindVo.getKindCode()+claimKindVo.getLossItemName();
				}else{
					key = FeeType.PAY+claimKindVo.getKindCode()+claimKindVo.getLossItemNo();
				}
				PrpLClaimKindHis oldKindHisPo = oldKindHisPoMap.get(key);
				PrpLClaimKindHis newClaimKindHis = calcKindHisByClaimKind(claimVo,claimKindVo,oldKindHisPo);
				newKindHisPoList.add(newClaimKindHis);
				if(oldKindHisPo!=null){
					oldKindHisPo.setValidFlag(ValidFlag.INVALID);// 将旧的这条数据改为无效
					// FIXME oldKindHisPo是否需要执行update操作
					// databaseDao.update(PrpLClaimKindHis .class,oldKindHisPo);
					oldKindHisPoMap.remove(key);// 计算过的就清除掉
				}
			}
		}

		// 根据PrpLClaimFee处理轨迹信息
		List<PrpLClaimKindFeeVo> claimKindFeeVoList = claimVo.getPrpLClaimKindFees();
		if(claimKindFeeVoList!=null && !claimKindFeeVoList.isEmpty()){
			for(PrpLClaimKindFeeVo kindFeeVo:claimKindFeeVoList){
				String key = FeeType.FEE+kindFeeVo.getKindCode()+kindFeeVo.getFeeCode();
				PrpLClaimKindHis oldKindHisPo = oldKindHisPoMap.get(key);
				PrpLClaimKindHis newClaimKindHis = calcKindHisByFeeKind(claimVo,kindFeeVo,oldKindHisPo);
				newKindHisPoList.add(newClaimKindHis);
				if(oldKindHisPo!=null){
					oldKindHisPo.setValidFlag(ValidFlag.INVALID);// 将旧的这条数据改为无效
					// FIXME oldKindHisPo是否需要执行update操作
					oldKindHisPoMap.remove(key);// 计算过的就清除掉
				}
			}
		}

		if(oldKindHisPoMap.size()>0){// 旧的MAP中还有剩余，但是这次没计算到，说明是被删除了，需要减掉
			for(String key:oldKindHisPoMap.keySet()){
				PrpLClaimKindHis oldKindHisPo = oldKindHisPoMap.get(key);
				PrpLClaimKindHis newClaimKindHis = calcKindHisByKindHis(claimVo,oldKindHisPo);
				newKindHisPoList.add(newClaimKindHis);
				if(oldKindHisPo!=null){
					oldKindHisPo.setValidFlag(ValidFlag.INVALID);// 将旧的这条数据改为无效
					// FIXME oldKindHisPo是否需要执行update操作--Po.set 自己会执行update语句
				}
			}
		}
		oldKindHisPoMap.clear();

		databaseDao.saveAll(PrpLClaimKindHis.class,newKindHisPoList);
		logger.info("立案"+claimNo+"更新估损信息成功!，newKindHisPoList.size="+newKindHisPoList.size());
		/*不需要返回数据
		List<PrpLClaimKindHisVo> kindHisVoList = Beans.copyDepth().from(newKindHisPoList).toList(PrpLClaimKindHisVo.class);
		return kindHisVoList;
		*/
	}


		/**
	 * 根据最新的claimKindVo 计算本次立案的KindHis
	 * @param claimVo
	 * @param claimKindVo
	 * @param oldKindHisPo
	 * @return
	 * @modified: ☆LiuPing(2016年6月3日 ): <br>
	 */
	private PrpLClaimKindHis calcKindHisByClaimKind(PrpLClaimVo claimVo,PrpLClaimKindVo claimKindVo,PrpLClaimKindHis oldKindHisPo) {
		PrpLClaimKindHis newKindHisPo = new PrpLClaimKindHis();
		Beans.copy().from(claimKindVo).excludes("id").to(newKindHisPo);
		newKindHisPo.setClaimNo(claimVo.getClaimNo());
		newKindHisPo.setFeeType(FeeType.PAY);
		newKindHisPo.setEstiTimes(claimVo.getEstiTimes());
		newKindHisPo.setCreateTime(new Date());
		if(oldKindHisPo!=null){
			// 变化值等=当前最新的值-上次的值
			newKindHisPo.setDefLossChg(newKindHisPo.getDefLoss().subtract(oldKindHisPo.getDefLoss()));
			newKindHisPo.setRescueFeeChg(newKindHisPo.getRescueFee().subtract(oldKindHisPo.getRescueFee()));
			newKindHisPo.setClaimLossChg(newKindHisPo.getClaimLoss().subtract(oldKindHisPo.getClaimLoss()));
		}else{// 为空时，变化值=当前值
			newKindHisPo.setDefLossChg(newKindHisPo.getDefLoss());
			newKindHisPo.setRescueFeeChg(newKindHisPo.getRescueFee());
			newKindHisPo.setClaimLossChg(newKindHisPo.getClaimLoss());
		}

		return newKindHisPo;
	}

	/**
	 * 根据最新的kindFeeVo 计算本次立案的KindHis
	 * @param claimVo
	 * @param kindFeeVo
	 * @param oldKindHisPo
	 * @return
	 * @modified: ☆LiuPing(2016年6月3日 ): <br>
	 */
	private PrpLClaimKindHis calcKindHisByFeeKind(PrpLClaimVo claimVo,PrpLClaimKindFeeVo kindFeeVo,PrpLClaimKindHis oldKindHisPo) {
		PrpLClaimKindHis newKindHisPo = new PrpLClaimKindHis();
		Beans.copy().from(kindFeeVo).excludes("id").to(newKindHisPo);
		newKindHisPo.setFeeType(FeeType.FEE);
		newKindHisPo.setClaimNo(claimVo.getClaimNo());
		newKindHisPo.setEstiTimes(claimVo.getEstiTimes());
		newKindHisPo.setLossItemName(kindFeeVo.getFeeCode());
		newKindHisPo.setClaimLoss(kindFeeVo.getPayFee());// PayFee 放到了历史的ClaimLoss里面
		newKindHisPo.setCreateTime(new Date());
		newKindHisPo.setDefLoss(kindFeeVo.getPayFee());
		if(oldKindHisPo!=null){
			// 变化值等=当前最新的值-上次的值
			newKindHisPo.setClaimLossChg(newKindHisPo.getClaimLoss().subtract(oldKindHisPo.getClaimLoss()));
			newKindHisPo.setDefLossChg(newKindHisPo.getDefLoss().subtract(oldKindHisPo.getDefLoss()));
		}else{// 为空时，变化值=当前值
			newKindHisPo.setClaimLossChg(newKindHisPo.getClaimLoss());
			newKindHisPo.setDefLossChg(newKindHisPo.getDefLoss());
		}

		return newKindHisPo;
	}

	/**
	 * 这里计算的是: 上次立案有这种数据，本次立案没有了，表示本次立案删掉了这些费用，需要在历史表里面也增加一个负的数据
	 * @param claimVo
	 * @param oldKindHisPo
	 * @return
	 * @modified: ☆LiuPing(2016年6月3日 ): <br>
	 */
	private PrpLClaimKindHis calcKindHisByKindHis(PrpLClaimVo claimVo,PrpLClaimKindHis oldKindHisPo) {
		PrpLClaimKindHis newKindHisPo = new PrpLClaimKindHis();
		Beans.copy().from(oldKindHisPo).excludes("id").to(newKindHisPo);
		newKindHisPo.setEstiTimes(claimVo.getEstiTimes());
		newKindHisPo.setCreateTime(new Date());

		// Java API:negate() ,返回 BigDecimal，其值为 (-this)，其标度为 this.scale()。
		newKindHisPo.setClaimLoss(BigDecimal.ZERO);
		newKindHisPo.setDefLoss(BigDecimal.ZERO);
		newKindHisPo.setRescueFee(BigDecimal.ZERO);
		newKindHisPo.setDefLossChg(oldKindHisPo.getDefLoss().negate());
		if(oldKindHisPo.getRescueFee()!=null){
			newKindHisPo.setRescueFeeChg(oldKindHisPo.getRescueFee().negate());
		}
		newKindHisPo.setClaimLossChg(oldKindHisPo.getClaimLoss().negate());

		return newKindHisPo;
	}

}
