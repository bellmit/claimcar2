package ins.sino.claimcar.genilex;

import java.util.List;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.genilex.comResVo.SoapEnvelopeVo;
import ins.sino.claimcar.genilex.vo.common.PrpLFraudScoreVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;





public interface ReportInfoService {
  
    public SoapEnvelopeVo organizaForReport(PrpLRegistVo vo,SysUserVo userVo,PrpLCMainVo prpLCMainVo);
    
    /**
     * 通过报案号查询评分信息表下该报案号最新一条数据信息
     * @param registNo
     * @param scoreNode
     * @return
     */
    public PrpLFraudScoreVo findPrpLFraudScoreVoByRegistNo(String registNo,String scoreNode);
    
    /**
     * 通过报案号查询评分信息表下数据列表
     * @param registNo
     * @return
     */
    public List<PrpLFraudScoreVo> findPrpLFraudScoreVoListByRegistNo(String registNo);
    
    

    /**
     * 通过主键Id查询评分信息表
     * @param fraudScoreID
     * @return
     */
    public PrpLFraudScoreVo findPrpLFraudScoreById(Long fraudScoreID);
}
