package ins.sino.claimcar.flow.service;

import ins.sino.claimcar.flow.vo.MsgNotifiedBody;
import ins.sino.claimcar.flow.vo.MsgNotifiedResBody;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.PrplWhiteListVo;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 移动查勘操作工作流接口
 * <pre></pre>
 * @author ★niuqiang
 */
public interface WFMobileService {
    /**
     * 终端通知理赔接口 移动端任务接收或平级移交 理赔相应改变工作流状态
     * <pre></pre>
     * @return
     * @modified:
     * *牛强(2017年6月7日 上午9:44:54): <br>
     */
    public Map<String, MsgNotifiedResBody> mobileUpdateWFTaskIn(MsgNotifiedBody body);
    
    
    /**
     * 修改工作流理赔处理标识
     * <pre></pre>
     * @param flowTaskId
     * @modified:
     * *牛强(2017年6月9日 上午9:21:58): <br>
     */
    public PrpLWfTaskVo updateWfTaskInByTaskId(BigDecimal  flowTaskId);
    
    /**
     * 根据listType，userCode查询PRPlWhitelist
     * @param listType
     * @param userCode
     * @return
     */
    public PrplWhiteListVo findPrplWhiteListByOther(String  listType,String  userCode);
    
    /**
     * 是否是白名单
     * @param listType
     * @param userCode
     * @return
     */
    public Boolean findWhileListCase(String  listType,String  userCode);
  
}
