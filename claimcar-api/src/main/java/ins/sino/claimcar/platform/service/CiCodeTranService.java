package ins.sino.claimcar.platform.service;

import ins.sino.claimcar.platform.vo.transKindCodeVo.SysCicodeconvertVo;


public interface CiCodeTranService {
    
    public String findTranCodeName(String codeType,String codeCode);
    
    public SysCicodeconvertVo findTranCodeDictVo(String codeType,String codeCode);
    
    public String findTranCodeCode(String codeType,String codeCode);
}
