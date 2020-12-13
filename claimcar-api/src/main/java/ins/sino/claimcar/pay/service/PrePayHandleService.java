package ins.sino.claimcar.pay.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.claim.vo.SubmitNextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;

import java.util.List;

public interface PrePayHandleService {

	/**
	 * 预付任务发起
	 * @param claimNoArr
	 * @throws Exception 
	 */
	public abstract void prePayTaskApply(String[] claimNoArr,SysUserVo userVo) throws Exception;

	/**
	 * 预付保存或提交
	 * @param compensateVo
	 * @param prePayPVos
	 * @param prePayFVos
	 * @param submitNextVo
	 * @return
	 * @throws Exception
	 */
	public String saveOrSubmit(PrpLCompensateVo compensateVo,List<PrpLPrePayVo> prePayPVos,List<PrpLPrePayVo> prePayFVos,SubmitNextVo submitNextVo,SysUserVo userVo
		) throws Exception;

			

	
	/**
	 * 预付冲销
	 * @param compVo
	 * @param submitNextVo
	 * @throws Exception
	 */
	public abstract void submitPrePayWriteOff(PrpLCompensateVo compVo,SubmitNextVo submitNextVo,SysUserVo userVo,List<PrpLPrePayVo> prePayPVos,
												List<PrpLPrePayVo> prePayFVos) throws Exception;
	
	/**
	 * 预付提交后台验证
	 * @return
	 */
	public List<String> nameback(String payList,String registNo,String feeType) throws Exception;

	public PrpLWfTaskVo findTaskIn(String registNo, String handleIdKey,FlowNode nextNode) throws Exception;
	
	/**
	 * 预付提交后台校验收款人账户
	 * @param prpPrePayVos
	 * @return
	 */
	public boolean saveBeforeCheck(List<PrpLPrePayVo> prpPrePayVos);
	

	/**
	 * <pre></pre>
	 * @param oldCompVo
	 * @param prePayFVos
	 * @param prePayPVos
	 * @param userVo
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年3月13日 下午4:28:24): <br>
	 */
	public PrpLCompensateVo updatePrePayWf(PrpLCompensateVo oldCompVo,List<PrpLPrePayVo> prePayFVos,List<PrpLPrePayVo> prePayPVos,SysUserVo userVo);
}