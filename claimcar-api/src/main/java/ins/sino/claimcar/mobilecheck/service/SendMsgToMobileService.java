package ins.sino.claimcar.mobilecheck.service;

import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.flow.vo.MsgNotifiedBody;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;


public interface SendMsgToMobileService {
    
    /**
     * 2.6.17理赔系统通知终端接口（理赔请求快赔） 发送通知消息
     * <pre></pre>
     * @param msgVo
     * @modified:
     * *牛强(2017年6月9日 上午8:47:24): <br>
     */
//    public void sendMsgToMobile(MsgNotifiedBody msgVo,PrpLWfTaskVo wfTakVo);
    
    /**
     * 为理赔通知终端接口组织报文
     * <pre></pre>
     * @param wfTakVo
     * @modified:
     * *牛强(2017年6月9日 上午8:47:29): <br>
     */
    public void packMsg(PrpLWfTaskVo wfTakVo,String url);
    
    /**
     * 通过报案号查找 PrpLWfMainVo
     * <pre></pre>
     * @param registNo
     * @return
     * @modified:
     * *牛强(2017年6月9日 上午8:49:36): <br>
     */
    public  PrpLWfMainVo  findPrpLWfMainVoByRegistNo(String registNo);

    
    public PrpLWfTaskVo changeToClaimAcceptStatus(BigDecimal flowTaskId);
    
    public PrpLWfTaskVo isMobileCaseAcceptInClaim(String registNo,BigDecimal flowTaskId);
    
    /**
     * 理赔通知移动端 补送
     * <pre></pre>
     * @param logVo
     * @return
     * @modified:
     * *牛强(2017年6月12日 下午4:18:01): <br>
     */
    public String sendMsgToMobile(ClaimInterfaceLogVo logVo,String url);
    
    /**
     * 判断是否为移动端案件
     * @param registVo
     * @param userCode
     * @return
     */
    public Boolean isMobileCase(PrpLRegistVo registVo,String userCode);
}
