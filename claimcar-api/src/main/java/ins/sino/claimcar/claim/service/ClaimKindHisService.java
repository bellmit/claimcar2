package ins.sino.claimcar.claim.service;

import ins.sino.claimcar.claim.vo.PrpLClaimKindHisVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;

import java.util.List;

public interface ClaimKindHisService {

	// 查立案险别金融表
	public abstract List<PrpLClaimKindHisVo> findPrpLClaimKindHisByRegistNo(String registNo);

	/**
	 * 重开赔案刷新轨迹
	 * @param claimNo
	 * @return
	 * @modified: ☆LiuPing(2016年6月3日 ): <br>
	 */
	public abstract void calcClaimKindHisByReOpen(String claimNo);

	/**
	 * 结案刷新轨迹
	 * @param claimNo
	 * @return
	 * @modified: ☆LiuPing(2016年6月3日 ): <br>
	 */
	public abstract void calcClaimKindHisByEndCase(String claimNo);

	/**
	 * 查询所有立案轨迹信息
	 * @param claimNo
	 * @return
	 * @modified: ☆LiuPing(2016年6月3日 ): <br>
	 */
	public abstract List<PrpLClaimKindHisVo> findAllKindHisVoList(String claimNo);

	/**
	 * 查询立案估损轨迹信息
	 * @param claimNo
	 * @param validFlag 有效状态 1有效0-无效，null-全部
	 * @return
	 * @modified: ☆LiuPing(2016年6月3日 ): <br>
	 */
	public abstract List<PrpLClaimKindHisVo> findKindHisVoList(String claimNo,String validFlag);

	/**
	 * 保存立案轨迹表数据
	 * @param claimVo
	 * @return
	 * @modified: ☆LiuPing(2016年6月3日 ): <br>
	 */
	public abstract void saveClaimKindHis(PrpLClaimVo claimVo);

}
