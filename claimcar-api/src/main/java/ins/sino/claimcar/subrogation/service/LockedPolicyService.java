package ins.sino.claimcar.subrogation.service;


import ins.sino.claimcar.subrogation.vo.PrpLLockedPolicyVo;

import java.util.List;

public interface LockedPolicyService {

	/**
	 * 保存锁定查询的结果
	 * 直接从返回报文解析后的vo,转换po 保存
	 * 先删除后保存
	 * @param lickedPolicyList
	 * @modified:
	 * ☆YangKun(2016年3月29日 上午11:24:30): <br>
	 */
	public abstract void saveLockedPolicy(List<PrpLLockedPolicyVo> resultList,
			String registNo, String claimSequenceNo);


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
	public abstract PrpLLockedPolicyVo findLockedPolicy(String registNo,
			String claimSequenceNo, String oppoentPolicyNo, String coverageType);

}