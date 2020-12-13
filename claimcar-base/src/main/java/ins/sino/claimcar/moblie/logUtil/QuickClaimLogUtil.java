package ins.sino.claimcar.moblie.logUtil;

import ins.framework.lang.Springs;
import ins.platform.utils.ClaimBaseCoder;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.moblie.msgNotified.vo.MsgNotifiedPacket;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class QuickClaimLogUtil {

    private Logger logger = LoggerFactory.getLogger(QuickClaimLogUtil.class);
    @Autowired
    ClaimInterfaceLogService interfaceLogService;
    public  void logUtil(Object reqObj,Object resObj,ClaimInterfaceLogVo logVo) {
       // ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
        try{
            String reqXml = ClaimBaseCoder.objToXmlUtf(reqObj);
            String resXml = ClaimBaseCoder.objToXmlUtf(resObj);
            logger.info("===============移动查勘 ===========");
            logger.info(reqXml);
            logger.info(resXml);
            //找到请求类型 
            Field head = reqObj.getClass().getDeclaredField("head");
            head.setAccessible(true);
            Object headValue = head.get(reqObj);
            Field requestType = headValue.getClass().getDeclaredField("requestType");
            requestType.setAccessible(true);
            String requestTypeValue = (String)requestType.get(headValue);
            //找到返回代码
            String resCodeValue = "";
            if(resObj != null){
                Field resHead = resObj.getClass().getDeclaredField("head");
                resHead.setAccessible(true);
                Object resHeadValue = resHead.get(resObj);
                Field resCodeType = resHeadValue.getClass().getDeclaredField("responseCode");
                resCodeType.setAccessible(true);
                 resCodeValue = (String)resCodeType.get(resHeadValue);
            }
          
            boolean flag = true; //请求类型
            for(MobileRequestType reqType :MobileRequestType.values()){
                if( reqType.getCode().equals(requestTypeValue)){
                    logVo.setBusinessType(reqType.name());
                    logVo.setBusinessName(reqType.getName());
                    flag = false;
                }
            }
            if(flag){
                logVo.setBusinessType("mobile");
                logVo.setBusinessName("移动查勘请求类型错误");
            }
            if(StringUtils.isNotBlank(logVo.getRegistNo())){
                logVo.setRegistNo(logVo.getRegistNo());
            }else{
                logVo.setRegistNo(requestTypeValue);
            }
         //   logVo.setServiceType("mobile");
            logVo.setRequestXml(reqXml);
            logVo.setResponseXml(resXml);
            logVo.setCreateTime(new Date());
            logVo.setCreateUser("mobile");
          //  logVo.setCompensateNo(logVo.getRegistNo());
            logVo.setRequestTime(new Date());
            if("YES".equals(resCodeValue) || "OK".equals(resCodeValue)){
                logVo.setStatus("1");
            }else{
                logVo.setStatus("0");
            }
            

        }catch(Exception e1){
            e1.printStackTrace();
        }finally{
            if(interfaceLogService == null){
                interfaceLogService = (ClaimInterfaceLogService)Springs.getBean(ClaimInterfaceLogService.class);
            }
            interfaceLogService.save(logVo);
        }

    }
    public static void main(String args[]){
        MsgNotifiedPacket pascet = new MsgNotifiedPacket();
        MobileCheckHead head = new MobileCheckHead();
        head.setPassWord("111");
        head.setUser("111");
        head.setRequestType("016");
        pascet.setHead(head);
        QuickClaimLogUtil quickClaimLogUtil = new QuickClaimLogUtil();
        ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
        logVo.setRegistNo("12345678987654321");
        quickClaimLogUtil.logUtil(pascet,pascet,logVo);
    }
}
