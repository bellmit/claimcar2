/******************************************************************************
* CREATETIME : 2016年3月30日 下午2:59:47
******************************************************************************/
package ins.sino.claimcar.subrogation.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.po.PrpLPlatLock;
import ins.sino.claimcar.subrogation.po.PrpLRecoveryOrPay;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLRecoveryOrPayVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月30日
 */

@Service("platLockService")
public class PlatLockService {

	@Autowired
	private DatabaseDao databaseDao;
	
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	private RegistService registService;
	/**
	 * 保存锁定信息
	 * @param platLockVo
	 * @modified:
	 * ☆YangKun(2016年3月30日 下午7:49:14): <br>
	 */
	
	public void savePlatLock(PrpLPlatLockVo platLockVo){
		PrpLPlatLock platLock = this.findPlatLock(platLockVo.getRegistNo(),platLockVo.getRecoveryCode());
		if(platLock !=null){
			Beans.copy().from(platLockVo).excludes("id").to(platLock);
			
			List<PrpLRecoveryOrPay> recoveryList = platLock.getPrpLRecoveryOrPays();
			for(PrpLRecoveryOrPayVo recoveryVo : platLockVo.getPrpLRecoveryOrPays()){
				if(recoveryVo.getId()==null){
					PrpLRecoveryOrPay recovery = Beans.copyDepth().from(recoveryVo).to(PrpLRecoveryOrPay.class);
					recovery.setPrpLPlatLock(platLock);
					recoveryList.add(recovery);
				}else{
					for(PrpLRecoveryOrPay recovery : recoveryList){
						if(recoveryVo.getId().equals(recovery.getId())){
							recoveryVo.setSerialNo(recovery.getSerialNo());
							Beans.copy().from(recoveryVo).to(recovery);
						}
					}
				}
			}
			
			databaseDao.update(PrpLPlatLock.class,platLock);
			
		}else{
			platLock = Beans.copyDepth().from(platLockVo).to(PrpLPlatLock.class);
			for(PrpLRecoveryOrPay recpvery : platLock.getPrpLRecoveryOrPays()){
				recpvery.setPrpLPlatLock(platLock);
			}
			
			databaseDao.save(PrpLPlatLock.class,platLock);
		}
		
		
	}
	
	/**
	 * 调用接口保存时 
	 * @param platLockVo
	 */
	
	public void firstSavePlatLock(PrpLPlatLockVo platLockVo){
		PrpLPlatLock platLock = this.findPlatLock(platLockVo.getRegistNo(),platLockVo.getRecoveryCode());
		if(platLock !=null){
			Beans.copy().from(platLockVo).excludes("id").to(platLock);
			
			List<PrpLRecoveryOrPay> recoveryList = platLock.getPrpLRecoveryOrPays();
			for(PrpLRecoveryOrPayVo recoveryVo : platLockVo.getPrpLRecoveryOrPays()){
				for(PrpLRecoveryOrPay recovery : recoveryList){
					if(recoveryVo.getRecoveryCode().equals(recovery.getRecoveryCode())){
						recoveryVo.setSerialNo(recovery.getSerialNo());
						Beans.copy().from(recoveryVo).excludes("id").to(recovery);
					}
				}
			}
			
			databaseDao.update(PrpLPlatLock.class,platLock);
			
		}else{
			
			platLock = Beans.copyDepth().from(platLockVo).to(PrpLPlatLock.class);
			for(PrpLRecoveryOrPay recpvery : platLock.getPrpLRecoveryOrPays()){
				recpvery.setPrpLPlatLock(platLock);
			}
			databaseDao.save(PrpLPlatLock.class,platLock);
		}
		
		platLockVo.setId(platLock.getId());
	}
	
	/**
	 * 查询 可以取消的锁定
	 * @param registNo
	 * @param recoveryCode
	 * @return
	 * @modified:
	 * ☆YangKun(2016年3月30日 下午7:48:54): <br>
	 */
	
	public List<PrpLPlatLockVo> findLockCancelList(String registNo,String recoveryCode){

		QueryRule queryRule = QueryRule.getInstance();
		if(StringUtils.isNotBlank(registNo)){
			queryRule.addEqual("registNo",registNo);
		}
		if(StringUtils.isNotBlank(recoveryCode)){
			queryRule.addEqual("recoveryCode",recoveryCode);
		}
		queryRule.addEqual("recoveryCodeStatus","1");//未审核
		
		List<PrpLPlatLock> lockList = databaseDao.findAll(PrpLPlatLock.class,queryRule);
		List<PrpLPlatLockVo> lockVoList = Beans.copyDepth().from(lockList).toList(PrpLPlatLockVo.class);
		
		return lockVoList;
	}

	/**
	 * 查询
	 * @param registNo
	 * @param recoveryCode
	 * @return
	 * @modified:
	 * ☆YangKun(2016年3月30日 下午9:35:58): <br>
	 */
	
	public PrpLPlatLockVo findPlatLockVo(String registNo,String recoveryCode){
		PrpLPlatLock platLock = this.findPlatLock(registNo,recoveryCode);
		if(platLock ==null){
			return null;
		}
		PrpLPlatLockVo platLockVo =Beans.copyDepth().from(platLock).to(PrpLPlatLockVo.class);
		
		return platLockVo;
	}
	
	public PrpLPlatLock findPlatLock(String registNo,String recoveryCode){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("recoveryCode",recoveryCode);
		
		List<PrpLPlatLock> platLock = databaseDao.findAll(PrpLPlatLock.class,queryRule);
		
		if (platLock != null && !platLock.isEmpty()) {
			return platLock.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 查询 锁定信息
	 * @param registNo
	 * @param recoveryCode
	 * @return
	 * @modified:
	 * ☆YangKun(2016年3月30日 下午7:48:54): <br>
	 */
	
	public List<PrpLPlatLockVo> findPlatLockList(String registNo,String recoveryCode){

		QueryRule queryRule = QueryRule.getInstance();
		if(StringUtils.isNotBlank(registNo)){
			queryRule.addEqual("registNo",registNo);
		}
		if(StringUtils.isNotBlank(recoveryCode)){
			queryRule.addEqual("recoveryCode",recoveryCode);
		}
		
		List<PrpLPlatLock> lockList = databaseDao.findAll(PrpLPlatLock.class,queryRule);
		List<PrpLPlatLockVo> lockVoList = Beans.copyDepth().from(lockList).toList(PrpLPlatLockVo.class);
		
		return lockVoList;
	}
	
	/**
	 * 查询追偿或清算信息
	 * @param registNo
	 * @param recoveryCode
	 * @return
	 * @modified:
	 * ☆YangKun(2016年3月31日 下午8:22:16): <br>
	 */
	
	public List<PrpLRecoveryOrPayVo> findRecoveryList(String registNo,String recoveryCode){
		QueryRule queryRule = QueryRule.getInstance();
		if(StringUtils.isNotBlank(registNo)){
			queryRule.addEqual("registNo",registNo);
		}
		if(StringUtils.isNotBlank(recoveryCode)){
			queryRule.addEqual("recoveryCode",recoveryCode);
		}
		queryRule.addDescOrder("times");
		
		List<PrpLRecoveryOrPay> recoveryOrPayList = databaseDao.findAll(PrpLRecoveryOrPay.class,queryRule);
		List<PrpLRecoveryOrPayVo> recoveryVoList = Beans.copyDepth().from(recoveryOrPayList).toList(PrpLRecoveryOrPayVo.class);
		
		return recoveryVoList;
	}
	
	
	
	public PrpLPlatLockVo findPlatLockByRecoveryCode(String recoveryCode){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("recoveryCode",recoveryCode);
		
		PrpLPlatLock platLock = databaseDao.findUnique(PrpLPlatLock.class,queryRule);
		
		if(platLock ==null){
			return null;
		}
		PrpLPlatLockVo platLockVo =Beans.copyDepth().from(platLock).to(PrpLPlatLockVo.class);
		
		return platLockVo;
	}
	
	
	
	public List<PrpLPlatLockVo> findPrpLPlatLockVoByRegistNo(String registNo,String recoveryOrPayFlag) {
		List<PrpLPlatLockVo> prpLPlatLockVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		if(!StringUtils.isBlank(recoveryOrPayFlag)){
			queryRule.addEqual("recoveryOrPayFlag",recoveryOrPayFlag);
		}
		List<PrpLPlatLock> prpLPlatLockList = databaseDao.findAll(PrpLPlatLock.class,queryRule);
		if(prpLPlatLockList != null && !prpLPlatLockList.isEmpty()){
			prpLPlatLockVoList = Beans.copyDepth().from(prpLPlatLockList).toList(PrpLPlatLockVo.class);
		}
		return prpLPlatLockVoList;
	}
	
	public PrpLPlatLockVo findPrpLPlatLockVoByLicenseNo(String registNo,String policyNo,
	       String recoveryOrPayFlag,String oppoentLicensePlateNo){
		PrpLPlatLockVo prpLPlatLockVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("policyNo",policyNo);
		queryRule.addEqual("recoveryOrPayFlag",recoveryOrPayFlag);
		queryRule.addEqual("oppoentLicensePlateNo",oppoentLicensePlateNo);
		
		List<PrpLPlatLock> prpLPlatLock = databaseDao.findAll(PrpLPlatLock.class,queryRule);
		if(prpLPlatLock != null && prpLPlatLock.size() > 0){
			prpLPlatLockVo = Beans.copyDepth().from(prpLPlatLock.get(0)).to(PrpLPlatLockVo.class);
		}
		return prpLPlatLockVo;
	}
	
	public List<PrpLPlatLockVo> findPrpLPlatLockVoList(String registNo,String policyNo,String recoveryOrPayFlag) {
		List<PrpLPlatLockVo> prpLPlatLockVoList = null;
		List<PrpLPlatLock> prpLPlatLockList = this.findPrpLPlatLockList(registNo, policyNo, recoveryOrPayFlag);
		if(prpLPlatLockList != null && !prpLPlatLockList.isEmpty()){
			prpLPlatLockVoList = Beans.copyDepth().from(prpLPlatLockList).toList(PrpLPlatLockVo.class);
		}
		
		return prpLPlatLockVoList;
	}
	
	private List<PrpLPlatLock> findPrpLPlatLockList(String registNo,String policyNo,String recoveryOrPayFlag) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		if(!StringUtils.isBlank(policyNo)){
		    queryRule.addEqual("policyNo",policyNo);
		}
		if(!StringUtils.isBlank(recoveryOrPayFlag)){
			queryRule.addEqual("recoveryOrPayFlag",recoveryOrPayFlag);
		}
		queryRule.addNotEqual("recoveryCodeStatus","9");//不等于已注销
		
		List<PrpLPlatLock> prpLPlatLockList = databaseDao.findAll(PrpLPlatLock.class,queryRule);
		
		return prpLPlatLockList;
	}
	
	
	public PrpLPlatLockVo findPrpLPlatLockVoById(Long id) {
		PrpLPlatLockVo prpLPlatLockVo = null;
		PrpLPlatLock prpLPlatLock = databaseDao.findByPK(PrpLPlatLock.class,id);
		if(prpLPlatLock != null){
			prpLPlatLockVo = Beans.copyDepth().from(prpLPlatLock).to(PrpLPlatLockVo.class);
		}
		return prpLPlatLockVo;
	}
	
	/**
	 * 理算页面保存追偿信息
	 * @param prpLCompensate
	 * @param platLockVoList
	 */
	
	public void savePlatLockVoList(PrpLCompensateVo prpLCompensate,List<PrpLPlatLockVo> platLockVoList) {
		Map<Long,BigDecimal> payMap = new HashMap<Long, BigDecimal>();
		String comCode="22010028";
		
		String compensateNo = prpLCompensate.getCompensateNo();
		String registNo=prpLCompensate.getRegistNo();
		PrpLRegistVo registVo=registService.findRegistByRegistNo(registNo);
		if(registVo!=null){
			comCode=registVo.getComCode();
		}
		if(platLockVoList!=null && !platLockVoList.isEmpty()){
			List<PrpLPlatLock> oldPlatLockList = this.findPrpLPlatLockList(prpLCompensate.getRegistNo(),prpLCompensate.getPolicyNo(), null);
			
			for(PrpLPlatLockVo platLock : platLockVoList){
				payMap.put(platLock.getId(),platLock.getThisPaid());
			}
			
			int serialNo = 0;
			for(PrpLPlatLock oldPlatLock : oldPlatLockList){
				BigDecimal thisPad = payMap.get(oldPlatLock.getId());
				List<PrpLRecoveryOrPay> recoveryList = oldPlatLock.getPrpLRecoveryOrPays();
				BigDecimal sumPad = BigDecimal.ZERO;
				boolean saveFlag = false;
				if(recoveryList!=null && !recoveryList.isEmpty()){
					
					for(PrpLRecoveryOrPay recovery : recoveryList){
						if(StringUtils.isBlank(recovery.getCompensateNo()) || recovery.getCompensateNo().equals(compensateNo)){
							if(comCode.startsWith("22")){
								saveMatchAmount(prpLCompensate,recovery);
							}else{
								recovery.setRecoveryOrPayAmount(thisPad);
							}
							recovery.setCompensateNo(compensateNo);
							recovery.setUpdateTime(new Date());
							recovery.setSerialNo(++serialNo);
							saveFlag = true;
						}
						if(recovery.getRecoveryOrPayAmount()!=null){//yzy
						sumPad = sumPad.add(recovery.getRecoveryOrPayAmount());
						}
					}
				}
				
				if(!saveFlag){
					PrpLRecoveryOrPay recovery = recoveryList.get(0);
					PrpLRecoveryOrPay reRecoveryPo = Beans.copyDepth().from(recovery).to(PrpLRecoveryOrPay.class);
					reRecoveryPo.setId(null);
					reRecoveryPo.setCompensateNo(compensateNo);
					reRecoveryPo.setRecoveryOrPayAmount(thisPad);
					reRecoveryPo.setCreateTime(new Date());
					reRecoveryPo.setUpdateTime(null);
					recovery.setSerialNo(++serialNo);
					sumPad = sumPad.add(thisPad);
					recoveryList.add(reRecoveryPo);
				}
				oldPlatLock.setSumRealAmount(sumPad);
				
			}
			
			databaseDao.saveAll(PrpLPlatLock.class, oldPlatLockList);
		}
	
	}
	
	// 更新
	public void updateByPrplplatlock(PrpLPlatLock platLock) {
		databaseDao.update(PrpLPlatLock.class, platLock);
	}

	// 更新PrpLRecoveryOrPay
	public void updateByRecoveryOrPay(PrpLRecoveryOrPay recoveryOrPay) {
		databaseDao.update(PrpLRecoveryOrPay.class, recoveryOrPay);
	}
	
	
	public PrpLRecoveryOrPay findById(long id) {
		PrpLRecoveryOrPay recoveryOrPay = new PrpLRecoveryOrPay();
		recoveryOrPay = databaseDao.findByPK(PrpLRecoveryOrPay.class,id);
		return recoveryOrPay;
	}
	/**
	 * 根据PrpLPlatLock表ID查询PrpLRecoveryOrPayVo
	 * <pre></pre>
	 * @param lockId
	 * @return
	 * @modified:
	 * ☆WLL(2017年11月28日 上午10:01:31): <br>
	 */
	public PrpLRecoveryOrPayVo findRecOrPayByLockId(long lockId){
		PrpLRecoveryOrPayVo recoveryOrPay = null;
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("prpLPlatLock.id",lockId);
		List<PrpLRecoveryOrPay> prpLRecoveryOrPayList = databaseDao.findAll(PrpLRecoveryOrPay.class,qr);
		if(prpLRecoveryOrPayList != null && prpLRecoveryOrPayList.size() > 0){
			recoveryOrPay = Beans.copyDepth().from(prpLRecoveryOrPayList.get(0)).to(PrpLRecoveryOrPayVo.class);
		}
		return recoveryOrPay;
	}
	
	public PrpLPlatLock findByLPlatLockId(long id) {
		PrpLPlatLock lPlatLock = new PrpLPlatLock();
		lPlatLock = databaseDao.findByPK(PrpLPlatLock.class,id);
		return lPlatLock;
	}

	
	public void savePlatLockVoList(List<PrpLPlatLockVo> listLockList) {
		if (listLockList != null && !listLockList.isEmpty()) {
			for (PrpLPlatLockVo lockVo : listLockList) {
				Long lockId = lockVo.getId();
				if (lockId != null) {
					PrpLPlatLock platLock = databaseDao.findByPK(PrpLPlatLock.class,lockId);
					Beans.copy().from(lockVo).to(platLock);
					databaseDao.update(PrpLPlatLock.class, platLock);
				}
			}
		}
	}
	
	/**
	 * 将核定金额赋值给上海代位的追偿清付表的追偿或清付金额
	 * @param prpLCompensate
	 */
	public void saveMatchAmount(PrpLCompensateVo prpLCompensate,PrpLRecoveryOrPay recoverOrPay){
		List<PrpLLossItemVo> lossItemVos=null;
		List<PrpLPlatLock> PlatLockList = this.findPrpLPlatLockList(prpLCompensate.getRegistNo(),prpLCompensate.getPolicyNo(), null);
		if(prpLCompensate!=null){
			lossItemVos=prpLCompensate.getPrpLLossItems();
			if(lossItemVos!=null && lossItemVos.size()>0){
				for(PrpLLossItemVo temVo:lossItemVos){
					if(PlatLockList!=null && PlatLockList.size()>0){
						for(PrpLPlatLock lock:PlatLockList){
							if(temVo.getPayFlag().equals(lock.getRecoveryOrPayFlag()) && temVo.getItemName().equals(lock.getLicenseNo())){
								if(lock.getRecoveryCode().equals(recoverOrPay.getRecoveryCode())){
									recoverOrPay.setRecoveryOrPayAmount(temVo.getSumRealPay());
								}
							}
						}
					}
					
				}
			}
		}
	}
	
}

