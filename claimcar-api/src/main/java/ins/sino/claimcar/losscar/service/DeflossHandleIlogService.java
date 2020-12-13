package ins.sino.claimcar.losscar.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.SubmitNextVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;

import java.math.BigDecimal;

public interface DeflossHandleIlogService {
    
  /**
   * 组织核价报文数据
   * <pre></pre>
   * @param lossCarMainVo
   * @param operateType
 * @param isSubmitHeadOffice 
   * @return
   * @modified:
   * ☆zhujunde(2017年12月11日 下午4:47:19): <br>
   */
    public LIlogRuleResVo organizaVprice(PrpLDlossCarMainVo lossCarMainVo,String operateType,SysUserVo userVo,BigDecimal taskId,String triggerNode,SubmitNextVo nextVo, String isSubmitHeadOffice)throws Exception;
    
    /**
     * 整理财产核损推送ILOG数据
     * <pre></pre>
     * @param lossCarMainVo
     * @param operateType
     * @param userVo
     * @param isSubmitHeadOffice 
     * @return
     * @modified:
     * ☆LuoWenShuang(2018年1月5日 下午5:04:40): <br>
     */
    public LIlogRuleResVo organizaVProperty(PrpLdlossPropMainVo lossPropMainVo,String operateType,SysUserVo userVo,BigDecimal taskId,String triggerNode, String isSubmitHeadOffice)throws Exception;

    /**
     * 整理车辆核损推送ILOG数据
     * <pre></pre>
     * @param lossPropMainVo
     * @param operateType
     * @param userVo
     * @param isSubmitHeadOffice 
     * @return
     * @modified:
     * ☆LuoWenShuang(2018年1月5日 下午5:01:41): <br>
     */
    public LIlogRuleResVo organizaVehicleLoss(PrpLDlossCarMainVo lossCarMainVo,String operateType,SysUserVo userVo,BigDecimal taskId,String triggerNode, String isSubmitHeadOffice)throws Exception;


    /**
     * 旧理赔案子请求ilog
     * <pre></pre>
     * @param lossCarMainVo
     * @param operateType
     * @return
     * @throws Exception
     * @modified:
     * ☆zhujunde(2019年2月13日 下午6:07:46): <br>
     */
    public LIlogRuleResVo organizaOldVehicleLoss(PrpLDlossCarMainVo lossCarMainVo,String operateType)throws Exception;

    /**
     * 旧理赔案子请求ilog
     * <pre></pre>
     * @param lossCarMainVo
     * @param operateType
     * @return
     * @throws Exception
     * @modified:
     * ☆zhujunde(2019年2月21日 下午7:16:08): <br>
     */
    public LIlogRuleResVo organizaOldOrganizaVprice(PrpLDlossCarMainVo lossCarMainVo,String operateType)throws Exception;
    
    /**
     * 旧理赔案子请求ilog
     * <pre></pre>
     * @param lossPropMainVo
     * @param operateType
     * @return
     * @throws Exception
     * @modified:
     * ☆zhujunde(2019年2月21日 下午7:46:46): <br>
     */
    public LIlogRuleResVo organizaOldVProperty(PrpLdlossPropMainVo lossPropMainVo,String operateType)throws Exception;
}