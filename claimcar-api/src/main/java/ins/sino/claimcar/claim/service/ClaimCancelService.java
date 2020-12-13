package ins.sino.claimcar.claim.service;


import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.vo.PrpLcancelTraceVo;
import ins.sino.claimcar.claim.vo.PrpLrejectClaimTextVo;

import java.math.BigDecimal;
import java.util.List;

public interface ClaimCancelService {

	/** 交强险种前缀*/
	public static final String PREFIX_CI = "11";
	/** 商业险种前缀*/
	public static final String PREFIX_BI = "12";

	public abstract BigDecimal save(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo,String currentNode);

	public abstract BigDecimal saveTrace(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo,String currentNode);

	public abstract BigDecimal updates(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo,String currentNode);

	// 暂存
	public abstract BigDecimal zhanCun(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo);

	public abstract BigDecimal findId(String riskCode,String claimNo);

	// 更新申请原因
	public abstract BigDecimal savePrpLrejectClaimText(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo);

	public abstract List<PrpLrejectClaimTextVo> findById(BigDecimal id);

	//
	public abstract PrpLcancelTraceVo findByCancelTraceId(BigDecimal id);

	//
	public abstract PrpLrejectClaimTextVo findByCancelClaimTextId(BigDecimal id);

	// 更新
	public abstract void updateCancelTrace(PrpLcancelTraceVo prpLcancelTraceVo);

	// 公共按钮暂存处理更新
	public abstract void claimInitZhanC(PrpLcancelTraceVo prpLcancelTraceVo);

	public abstract PrpLcancelTraceVo findByClaimNo(String claimNo);

	// 再次申请更新状态位
	public abstract void updateCancelDTrace(PrpLcancelTraceVo prpLcancelTraceVo);

	// 发起
	public abstract BigDecimal saveF(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo);

	public abstract BigDecimal updatesF(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo);

	/**
	 * @param claimNo
	 * @param riskCode
	 * @modified: ☆Luwei(2016年6月8日 下午3:47:56): <br>
	 */
	public abstract List<PrpLcancelTraceVo> queryCancelTrace(String claimNo);

	/**
	 * 核赔回写
	 * @param cancelTraceVo
	 * @modified: ☆Luwei(2016年6月8日 下午3:48:07): <br>
	 */
	public abstract void vclaimBackWrite(List<PrpLcancelTraceVo> cancelTraceVos);

	// public List<PrpLrejectClaimTextVo> queryRejectClaimText(String claimNo){
	// List<PrpLrejectClaimTextVo> rejectClaimTextVo = null;
	// QueryRule qr = QueryRule.getInstance();
	// qr.addEqual("claimNo",claimNo);
	// List<PrpLrejectClaimText> rejectClaimPos =
	// databaseDao.findAll(PrpLrejectClaimText.class,qr);
	// if(rejectClaimPos!=null&&rejectClaimPos.size()>0){
	// rejectClaimTextVo = new ArrayList<PrpLrejectClaimTextVo>();
	// rejectClaimTextVo = Beans.copyDepth().from(rejectClaimPos).toList(PrpLrejectClaimTextVo.class);
	// }
	// return rejectClaimTextVo;
	// }
	//
	// public void rejectClaimBackWrite(List<PrpLrejectClaimTextVo> rejectClaimTextVos) {
	// for(PrpLrejectClaimTextVo rejectClaimTextVo : rejectClaimTextVos){
	// PrpLrejectClaimText rejectClaimText = databaseDao.findByPK
	// (PrpLrejectClaimText.class,rejectClaimTextVo.getId());
	// Beans.copy().from(rejectClaimTextVo).includeNull().to(rejectClaimText);
	// databaseDao.update(PrpLcancelTrace.class,rejectClaimText);
	// }
	// }
	public abstract String validCheckClaim(String registNo,String claimNo);

	//发起注销
	public abstract BigDecimal cancelUpdates(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo);

	/**
	 * 立案注销送平台
	 * @param prpLcancelTraceVo
	 * @modified: ☆LiuPing(2016年9月24日 ): <br>
	 */
	public abstract void sendToClaimPlatform(PrpLcancelTraceVo prpLcancelTraceVo);
	//查询立案注销的最新prpLcancelTraceVo
	public abstract PrpLcancelTraceVo findPrpLcancelTraceVo(String riskCode,String claimNo,String flags);

	/**
	 * 根据opinionCode类型查询PrpLrejectClaimTextVo
	 * <pre></pre>
	 * @param opinionCode
	 * @param claimNo
	 * @return
	 * @modified:
	 * ☆zhujunde(2018年6月26日 上午10:16:25): <br>
	 */
	public abstract List<PrpLrejectClaimTextVo> findByOpinionCode(String opinionCode,String claimNo);
}
