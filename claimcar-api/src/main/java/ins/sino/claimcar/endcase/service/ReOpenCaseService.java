package ins.sino.claimcar.endcase.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseTextVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;

import java.math.BigDecimal;
import java.util.List;

public interface ReOpenCaseService {

	/**
	 * 根据报案号查询已结案案件
	 * @param registNo
	 * @return
	 */
	/*	public List<PrpLClaimVo> findClaimVoByRegistNo(String registNo){
			Date date=new Date();
			List<PrpLClaimVo> claimVos1 = claimTaskService.findClaimListByRegistNo(registNo);
			List<PrpLClaimVo> claimVos2 = new ArrayList<PrpLClaimVo>();
			for(PrpLClaimVo claimVo:claimVos1){
				if(claimVo.getEndCaserCode()!=null&&claimVo.getCaseNo()!=null&&
						claimVo.getEndCaseTime()!=null&&claimVo.getEndCaseTime().compareTo(date)<0){
					claimVos2.add(claimVo);
				}
			}
			return claimVos2;
		}*/
	public abstract List<PrpLEndCaseVo> findEndCaseByRegistNo(String registNo);

	/**
	 * 根据结案号查询最新结案的ReCaseVo
	 * @param endCase
	 * @return
	 */
	public abstract PrpLReCaseVo findReCaseVoByEndCaseNo(String endCaseNo);

	/**
	 * 根据立案号统计重开赔案次数
	 * @param claimNo
	 * @return
	 */
	public abstract Long countReCaseByClaimNo(String claimNo);

	public abstract Long saveOrUpdateReCase(PrpLReCaseVo prpLReCaseVo);

	/**
	 * 根据结案号查询结案案件
	 * @param endCaseNo
	 * @return
	 */
	public abstract PrpLEndCaseVo findEndCaseVoByEndCaseNo(String endCaseNo);

	/**
	 * 根据立案号查询一个结案案件
	 * @param claimNo
	 * @return
	 */
	public abstract PrpLEndCaseVo findEndCaseVoByClaimNo(String claimNo);

	/**
	 * 提交上级
	 * @param flowTaskId
	 */
	public abstract String superior(PrpLReCaseVo prpLReCaseVo,
			Double flowTaskId, String nextNode, SysUserVo userVo);

	/**
	 * 暂存
	 * @param prpLReCaseVo
	 * @param flowTaskId
	 */
	public abstract Long save(PrpLReCaseVo prpLReCaseVo, Double flowTaskId,SysUserVo userVo);

	/**
	 * 审核通过或者不通过
	 * @param flowTaskId
	 * @param auditStatus
	 * @param prpLReCaseVo
	 */
	public abstract void submit(Double flowTaskId, String auditStatus,
			PrpLReCaseVo prpLReCaseVo, SysUserVo userVo);

	/**
	 * 退回修改
	 * @param flowTaskId
	 * @param prpLReCaseVo
	 */
	public abstract void returnAndModify(Double flowTaskId,
			PrpLReCaseVo prpLReCaseVo,SysUserVo userVo);

	/**
	 * 根据立案号查询重开赔案案件
	 * @param claimNo
	 * @return
	 */
	public abstract List<PrpLReCaseVo> findReCaseByClaimNo(String claimNo);

	public abstract List<PrpLReCaseVo> findReCase(String registNo,
			String claimNo);

	//根据重开赔案原因代码返回重开原因
	public abstract String getOpinion(int reasonCode);

	//设置意见表
	public abstract List<PrpLReCaseTextVo> getReCaseTextList(
			String checkStatus, String checkOpinion,SysUserVo userVo);

	public abstract WfSimpleTaskVo setWfSimpleTaskVo(PrpLWfTaskVo wfTaskVo,
			String handlerIdKey, String registNo);

	/**
	 * 拼装WfTaskSubmitVo
	 * @param wfTaskVo
	 * @param handleIdKey
	 * @param taskInKey
	 * @return
	 */
	public abstract WfTaskSubmitVo setWfTaskSubmitVo(PrpLWfTaskVo wfTaskVo,
			String handleIdKey, String taskInKey,SysUserVo userVo);

	public abstract PrpLReCaseVo findReCaseByPk(Long reOpenId);

	public abstract String getTaskName(String saveType, String nextNode);

	public abstract String reOpenValid(String registNo);

	public abstract BigDecimal findTaskByCertify(String registNo,
			String checkId, String certifyId,String claimNo);

	//判断该案件是否重开过但审核不通过
	public abstract Boolean isFail(String endCaseNo);
	
	public List<PrpLReCaseTextVo> findReCaseTextByReCaseId(Long reCaseId);

	/**
	 * 重开赔案任务发起
	 * @param registNo
	 * @param claimNoArr
	 * @param handleStatus
	 * @param prpLReCaseVo
	 * @param userVo
	 * @throws Exception
	 */
	public void appSubmit(String registNo,String[] claimNoArr,String handleStatus,PrpLReCaseVo prpLReCaseVo,SysUserVo userVo)  throws Exception;
	/**
	 * 按照流程图任务id序号查询重开案件
	 * @param endCaseNo
	 * @param reOpenCount
	 * @return
	 */
	public PrpLReCaseVo findReCaseVoByEndCaseNoA(String endCaseNo,Integer reOpenCount);
	
	/**
	 * 平安--审核通过或者不通过
	 * @param flowTaskId
	 * @param auditStatus
	 * @param prpLReCaseVo
	 */
	public abstract void pinganSubmit(Double flowTaskId, String auditStatus,
			PrpLReCaseVo prpLReCaseVo, SysUserVo userVo);
}