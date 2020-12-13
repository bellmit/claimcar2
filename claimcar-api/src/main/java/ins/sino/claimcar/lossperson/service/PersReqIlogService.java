package ins.sino.claimcar.lossperson.service;

import java.util.List;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.SubmitNextVo;
import ins.sino.claimcar.mobile.check.vo.PhotoInfo;

/**
 * 人伤核损请求ILOG接口
 * @author WURH
 *
 */
public interface PersReqIlogService {

	/**
	 * 查勘提交人伤核损请求ILOG接口
	 * @param checkVo
	 * @param userVo
	 * @param ruleNode
	 * @param taskVo
	 * @return
	 */
	public LIlogRuleResVo reqIlogByChe(PrpLCheckVo checkVo,SysUserVo userVo,String ruleNode,PrpLWfTaskVo taskVo,
			List<PhotoInfo> photoInfoList);
	
	/**
	 * operateType等于1是自动，2是人工权限
	 * @param traceMainVo
	 * @param taskVo
	 * @param submitNextVo
	 * @param userVo
	 * @param operateType
	 * @param existHeadOffice 
	 * @return
	 */
	public LIlogRuleResVo reqIlogByPers(PrpLDlossPersTraceMainVo traceMainVo,PrpLWfTaskVo taskVo,SubmitNextVo submitNextVo,
			SysUserVo userVo,String operateType, String existHeadOffice);
	

	/**
	 * 旧理赔案子请求ilog
	 * <pre></pre>
	 * @param traceMainVo
	 * @param policyComCode
	 * @param operateType
	 * @return
	 * @modified:
	 * ☆zhujunde(2019年2月25日 下午5:44:01): <br>
	 */
	public LIlogRuleResVo reqIlogByOldPers(PrpLDlossPersTraceMainVo traceMainVo,String policyComCode,String operateType);
}
