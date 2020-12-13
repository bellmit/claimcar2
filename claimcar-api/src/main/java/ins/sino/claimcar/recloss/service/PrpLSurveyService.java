package ins.sino.claimcar.recloss.service;


import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.recloss.vo.PrpLSurveyVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 调查任务Service
 * @author ly
 *
 */
public interface PrpLSurveyService {

	/**
	 * 根据报案号查询调查任务
	 * @param registNo
	 * @return
	 */
	public abstract List<PrpLSurveyVo> findSurveyByRegistNo(String registNo);
	
	/**
	 * 保存调查调查任务
	 * @param prpLSurveyVo
	 * @return
	 */
	public abstract void saveSurvey(PrpLSurveyVo prpLSurveyVo,SysUserVo sysUserVo,BigDecimal flowTaskId);
	
	/**
	 * 根据Id查询调查任务
	 * @param id
	 * @return
	 */
	public abstract PrpLSurveyVo findSurveyVo(Long id);

	/**
	 * 更新调查任务
	 * @param prpLSurveyVo
	 */
	public abstract void updateSurvey(PrpLSurveyVo prpLSurveyVo);

	
	/**
	 * 平安---保存调查调查任务
	 * @param prpLSurveyVo
	 * @return
	 */
	public abstract void pinganSaveSurvey(PrpLSurveyVo prpLSurveyVo,SysUserVo sysUserVo,BigDecimal flowTaskId);

}
