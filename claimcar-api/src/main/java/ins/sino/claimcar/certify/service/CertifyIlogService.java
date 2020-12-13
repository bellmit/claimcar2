package ins.sino.claimcar.certify.service;


import java.math.BigDecimal;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;

public interface CertifyIlogService {
	/**
	 * 发送生成单证提交信息判断是否自动理算
	 * @param registNo
	 * @param userVo
	 * @param taskId
	 * @return
	 */
      public LIlogRuleResVo sendAutoCertifyRule(String registNo,SysUserVo userVo,BigDecimal taskId,String codeNode);
      /**
       * 自动单证
       * @param registNo
       */
      public WfTaskSubmitVo autoCertify(String registNo,SysUserVo userVo);
     /**
      * 判断是否为最后一个核损
      * @param registNo
      * @return
      */
      public boolean validAllVLossPass(String registNo);
}
