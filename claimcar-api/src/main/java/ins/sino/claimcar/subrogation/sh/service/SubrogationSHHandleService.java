package ins.sino.claimcar.subrogation.sh.service;

import java.util.List;



import ins.framework.common.ResultPage;
import ins.sino.claimcar.subrogation.platform.vo.AccountsInfoVo;
import ins.sino.claimcar.subrogation.sh.vo.CopyInformationResultVo;
import ins.sino.claimcar.subrogation.sh.vo.SubrogationSHQueryVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.SubrogationQueryVo;

/**
 * 上海代位求偿处理dubbo接口类
 * @author ★Luwei
 */
public interface SubrogationSHHandleService {

	/**
	 * 代位求偿信息抄回接口信息查询
	 * @param registNo
	 * @param claimNo
	 * @modified:
	 * ☆Luwei(2017年3月6日 下午7:01:56): <br>
	 */
	public abstract List<CopyInformationResultVo> sendCopyInformationToSubrogationSH(SubrogationSHQueryVo queryVo) throws Exception;
	
	/**
	 * 上海平台的 结算查询 --》待结算金额确认
	 * @param queryVo
	 * @return
	 * @modified:
	 * ☆Luwei(2017年3月10日 下午2:48:23): <br>
	 */
	public abstract ResultPage<PrpLPlatLockVo> claimDWrecoveryQuery(SubrogationQueryVo queryVo);
	
	/**
	 * 清算查询结果跳转显示查询
	 * @param recoveryCode
	 * @return
	 * @modified:
	 * ☆Luwei(2017年3月13日 下午2:27:35): <br>
	 */
	public abstract List<AccountsInfoVo> findRecoveryByCode(String recoveryCode);
	
	/**
	 * 清算码查询锁定
	 * @param recoveryCode
	 * @return
	 */
	public PrpLPlatLockVo findPlatLockByRecoveryCode(String recoveryCode);
		
	
}
