/******************************************************************************
* CREATETIME : 2016年9月27日 上午10:30:44
******************************************************************************/
package ins.sino.claimcar.lossperson.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossperson.vo.SubmitNextVo;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author ★XMSH
 */
public interface PersTraceHandleService {
	
	/**
	 * 接收人伤任务
	 * @param flowTaskId
	 * @param registNo
	 * @param userVo
	 * @return
	 * @modified:
	 * ☆XMSH(2016年9月27日 上午11:06:51): <br>
	 */
	public Long acceptPersTraceTask(String flowTaskId,String registNo,SysUserVo userVo) throws Exception;
	
	/**
	 * 暂存人伤信息
	 * @param persTraceVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年9月27日 上午11:13:22): <br>
	 */
	public Long saveCasualtyInfo(PrpLDlossPersTraceVo persTraceVo,SysUserVo userVo) throws Exception;
	
	/**
	 * 注销或激活人员
	 * @param id
	 * @param validFlag，1-激活，0-注销
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年9月27日 下午2:27:56): <br>
	 */
	public void ActiveOrCancelPersTrace(String id,String validFlag) throws Exception;
	
	/**
	 * 保存或提交人伤跟踪
	 * @param prpLDlossPersTraceMainVo
	 * @param prpLCheckDutyVo
	 * @param prpLDlossChargeVos
	 * @param prpLClaimTextVo
	 * @param submitNextVo
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年9月27日 上午11:24:30): <br>
	 */
	public PrpLClaimTextVo saveOrSubmitPLEdit(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo,
											PrpLCheckDutyVo prpLCheckDutyVo,List<PrpLDlossChargeVo> prpLDlossChargeVos,
											PrpLClaimTextVo prpLClaimTextVo,SubmitNextVo submitNextVo,SysUserVo userVo) throws Exception;
	/**
	 * 保存或提交大案审核
	 * @param prpLDlossPersTraceMainVo
	 * @param submitNextVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年9月27日 下午12:11:55): <br>
	 */
	public String saveOrSubmitPLBig(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo,SubmitNextVo submitNextVo,SysUserVo userVo) throws Exception;
	
	/**
	 * 保存或提交人伤审核环节
	 * @param prpLDlossPersTraceMainVo
	 * @param prpLDlossPersTraceVos
	 * @param prpLDlossChargeVos
	 * @param prpLClaimTextVo
	 * @param submitNextVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年9月27日 下午2:38:40): <br>
	 */
	public String saveOrSubmitPLVerify(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo,
										List<PrpLDlossPersTraceVo> prpLDlossPersTraceVos,
										List<PrpLDlossChargeVo> prpLDlossChargeVos,PrpLClaimTextVo prpLClaimTextVo,
										SubmitNextVo submitNextVo,SysUserVo userVo) throws Exception;
	/**
	 * 提交费用审核修改
	 * @param persTraceMainId
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年9月27日 下午2:51:47): <br>
	 */
	public void submitPLChargeAdjust(Long persTraceMainId) throws Exception;
	
	/**
	 * 查勘提交，人伤费用自动审核
	 * @param taskVo
	 * @param userVo
	 */
	public void autoPersSubmitForChe(PrpLWfTaskVo taskVo,PrpLCheckVo checkVo,SysUserVo userVo);
	
	/**
	 * 组织提交信息
	 * @param persTraceMain
	 * @param nextVo
	 * @param userVo
	 * @param caseProcessType
	 * @param existHeadOffice 
	 * @return
	 */
	public SubmitNextVo organizNextVo(PrpLDlossPersTraceMainVo persTraceMain,SubmitNextVo nextVo,
			SysUserVo userVo,String caseProcessType, String existHeadOffice);
	
	/**
	 * 人伤跟踪注销
	 * <pre></pre>
	 * @param persTraceMainVo
	 * @param flowTaskId
	 * @param userVo
	 * @modified:
	 * ☆zhujunde(2018年5月28日 上午11:22:06): <br>
	 */
	public void cancelPerson(PrpLDlossPersTraceMainVo persTraceMainVo, BigDecimal flowTaskId,SysUserVo userVo);
	/**
	 * 人伤生成查勘费任务
	 * @param persTraceMainVo
	 * @param userVo
	 */
	public void saveAcheckFeeTask(PrpLDlossPersTraceMainVo persTraceMainVo,SysUserVo userVo);
}
