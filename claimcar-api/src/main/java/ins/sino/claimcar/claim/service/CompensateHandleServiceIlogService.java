package ins.sino.claimcar.claim.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;

import java.math.BigDecimal;





public interface CompensateHandleServiceIlogService {
    /**
     * 组织赔对外服务接口数据
     * <pre></pre>
     * @param compensateId
     * @param userVo
     * @param isSubmitHeadOffice 
     * @return
     * @modified:
     * ☆zhujunde(2018年1月4日 下午3:17:09): <br>
     */
    public LIlogRuleResVo organizaForCompensate(String compensateId,String operateType,String callNode,BigDecimal taskId,String triggerNode,SysUserVo userVo,PrpLWfTaskVo wfTaskVo, String isSubmitHeadOffice) throws Exception;
    
    
    public LIlogRuleResVo organizaForPrePay(PrpLWfTaskVo prpLWfTaskVo,String operateType,String callNode,String triggerNode,SysUserVo userVo,String isSubmitHeadOffice) throws Exception;
    
    public LIlogRuleResVo organizaForPadPay(PrpLPadPayMainVo padPayVo,String operateType,String callNode,BigDecimal taskId,String triggerNode,SysUserVo userVo,String isSubmitHeadOffice) throws Exception;
    
    public LIlogRuleResVo organizaForClaimCancelJuPei(PrpLClaimVo prpLClaimVo,BigDecimal adjustmentAmount,String operateType,String callNode,BigDecimal taskId,String triggerNode,SysUserVo userVo) throws Exception;

    /**
     * 旧理赔案件调用ilog获取级别（理算）
     * <pre></pre>
     * @param compensateId
     * @param operateType
     * @param callNode
     * @return
     * @throws Exception
     * @modified:
     * ☆zhujunde(2019年2月25日 下午9:27:24): <br>
     */
    public LIlogRuleResVo organizaForOldCompensate(String compensateId,String operateType,String callNode) throws Exception;
    
    /**
     * 旧理赔案件调用ilog获取级别（预付）
     * <pre></pre>
     * @param prpLWfTaskVo
     * @param operateType
     * @return
     * @throws Exception
     * @modified:
     * ☆zhujunde(2019年2月25日 下午9:27:55): <br>
     */
    public LIlogRuleResVo organizaForOldPrePay(PrpLWfTaskVo prpLWfTaskVo,String operateType) throws Exception;


}
