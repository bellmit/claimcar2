/******************************************************************************
* CREATETIME : 2016年3月29日 上午11:16:28
******************************************************************************/
package ins.sino.claimcar.subrogation.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.subrogation.po.PrpLLockedNotify;
import ins.sino.claimcar.subrogation.po.PrpLLockedPolicy;
import ins.sino.claimcar.subrogation.po.PrpLLockedThirdParty;
import ins.sino.claimcar.subrogation.vo.PrpLLockedPolicyVo;

import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月29日
 */

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("lockedPolicyService")
public class LockedPolicyServiceImpl implements LockedPolicyService {

	@Autowired
	private DatabaseDao databaseDao;
	
	/**
	 * 保存锁定查询的结果
	 * 直接从返回报文解析后的vo,转换po 保存
	 * 先删除后保存
	 * @param lickedPolicyList
	 * @modified:
	 * ☆YangKun(2016年3月29日 上午11:24:30): <br>
	 */
	@Override
	public void saveLockedPolicy(List<PrpLLockedPolicyVo> resultList,String registNo,String claimSequenceNo){
		
		//删除已有数据
		List<PrpLLockedPolicy> lockedPolicyPoList = this.findLockedPolicyByClaimSequenceNo(claimSequenceNo,registNo);
		if(lockedPolicyPoList!=null && !lockedPolicyPoList.isEmpty()){
			databaseDao.deleteAll(PrpLLockedPolicy.class,lockedPolicyPoList);
		}
		
		List<PrpLLockedPolicy> lockedPolicyList = Beans.copyDepth().from(resultList).toList(PrpLLockedPolicy.class);
		if(lockedPolicyList!=null && !lockedPolicyList.isEmpty()){
			for(PrpLLockedPolicy lockedPolicy : lockedPolicyList){
				List<PrpLLockedNotify> lockedNotifyList = lockedPolicy.getPrplLockedNotifies();
				for(PrpLLockedNotify lockedNotify : lockedNotifyList){
					lockedNotify.setClaimSequenceNo(claimSequenceNo);
					lockedNotify.setPrplLockedPolicy(lockedPolicy);
					
					List<PrpLLockedThirdParty> lockedThirdPartyList = lockedNotify.getPrplLockedThirdParties();
					for(PrpLLockedThirdParty lockedThirdParty : lockedThirdPartyList){
						lockedThirdParty.setPrplLockedNotify(lockedNotify);
					}
				}
			}
			databaseDao.saveAll(PrpLLockedPolicy.class,lockedPolicyList);
		}
		
		
	}
	
	/**
	 * 理赔编码和报案号查询
	 * @param claimSequenceNo
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆YangKun(2016年3月30日 下午2:58:33): <br>
	 */
	public List<PrpLLockedPolicy> findLockedPolicyByClaimSequenceNo(String claimSequenceNo,String registNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimSequenceNo",claimSequenceNo);
		queryRule.addEqual("registNo",registNo);
		
		List<PrpLLockedPolicy> lockedPolicyList = databaseDao.findAll(PrpLLockedPolicy.class,queryRule);
		
		return lockedPolicyList;
	}

	/**
	 * 理赔编码和报案号 对方保单号和对方保单类型查询
	 * @param registNo
	 * @param claimSequenceNo
	 * @param oppoentPolicyNo
	 * @param coverageType
	 * @return
	 * @modified:
	 * ☆YangKun(2016年3月30日 下午2:58:58): <br>
	 */
	@Override
	public PrpLLockedPolicyVo findLockedPolicy(String registNo,String claimSequenceNo,String oppoentPolicyNo,String coverageType) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimSequenceNo",claimSequenceNo);
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("policyNo",oppoentPolicyNo);
		queryRule.addEqual("coverageType",coverageType);
		
		PrpLLockedPolicy lockedPolicy = databaseDao.findUnique(PrpLLockedPolicy.class,queryRule);
		if(lockedPolicy  == null){
			return null;
		}
		PrpLLockedPolicyVo lockedPolicyVo = Beans.copyDepth().from(lockedPolicy).to(PrpLLockedPolicyVo.class);
		
		return lockedPolicyVo;
		
	}
}

