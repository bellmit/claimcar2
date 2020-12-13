package ins.sino.claimcar.genilex;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.genilex.comResVo.SoapEnvelopeVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.List;





public interface PolicyInfoService {
  
    public SoapEnvelopeVo organizaForPolicyInfo(PrpLRegistVo vo,SysUserVo userVo,List<PrpLCMainVo> prpLCMainVo);

}
