package ins.sino.claimcar.claim.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.vo.PrpLcancelTraceVo;
import ins.sino.claimcar.claim.vo.PrpLrejectClaimTextVo;

import java.math.BigDecimal;
import java.util.List;

public interface ClaimCancelRecoverService {

	public abstract BigDecimal save(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo,String currentNode);

	public abstract BigDecimal updates(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo,String currentNode);

	//恢复暂存
	public abstract BigDecimal zhanCun(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo);

	public abstract BigDecimal findId(String riskCode,String claimNo);

	//更新申请原因
	public abstract BigDecimal savePrpLrejectClaimText(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo);

	public abstract List<PrpLrejectClaimTextVo> findById(BigDecimal id);

	//
	public abstract PrpLcancelTraceVo findByCancelTraceId(BigDecimal id);

	// 更新
	public abstract void updateCancelTrace(PrpLcancelTraceVo prpLcancelTraceVo);

	//公共按钮暂存处理更新
	public abstract void claimInitZhanC(PrpLcancelTraceVo prpLcancelTraceVo);

	public abstract PrpLcancelTraceVo findByClaimNo(String claimNo);

}
