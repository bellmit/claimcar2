package ins.sino.claimcar.flow.service;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.flow.vo.AMLVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;


public interface AMLService {
    /**
     * 
     * <pre>反洗钱信息列表查询</pre>
     * @param amlVo
     * @param start
     * @param length
     * @return
     * @modified:
     * ☆LinYi(2017年7月5日 下午2:53:44): <br>
     */
    public ResultPage<PrpLPayCustomVo> findAMLList(PrpLWfTaskQueryVo prpLWfTaskQueryVo,int start,int length) ;
    
    /**
     * 
     * <pre>反洗钱打印信息</pre>
     * @param mainId
     * @param registNo
     * @param claimNo
     * @return
     * @modified:
     * ☆LinYi(2017年7月6日 下午4:32:57): <br>
     */
    public AMLVo findAMLInfo(Long mainId,String registNo,String claimNo,String signFlag);

}
