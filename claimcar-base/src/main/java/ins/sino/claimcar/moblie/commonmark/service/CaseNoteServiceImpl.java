/******************************************************************************
 * CREATETIME : 2015年12月8日 上午10:19:01
 ******************************************************************************/
package ins.sino.claimcar.moblie.commonmark.service;
import ins.framework.lang.Springs;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.flow.service.SysMsgContentService;
import ins.sino.claimcar.manager.vo.SysMsgContentVo;
import ins.sino.claimcar.mobile.vo.CaseNoteReqBodyVo;
import ins.sino.claimcar.mobile.vo.CaseNoteReqVo;
import ins.sino.claimcar.mobile.vo.CaseNoteResVo;
import ins.sino.claimcar.mobile.vo.RemarkInfo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.logUtil.QuickClaimLogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 移动查勘案件备注实现类
 * 
 * <pre></pre>
 * @author ★zjd
 */
public class CaseNoteServiceImpl implements ServiceInterface {
    private static Logger logger = LoggerFactory.getLogger(CaseNoteServiceImpl.class);

    private SysMsgContentService sysMsgContentService;
    
    @Override
    public Object service(String arg0,Object arg1) {
        init();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
        stream.autodetectAnnotations(true);
        stream.setMode(XStream.NO_REFERENCES);
        stream.aliasSystemAttribute(null,"class");// 去掉 class属性
        stream.processAnnotations(CaseNoteReqVo.class);
        CaseNoteReqVo reqPacket =(CaseNoteReqVo)arg1;
        String xml = stream.toXML(reqPacket);
        logger.info("===============案件备注接收报文: \n"+xml);
        Assert.notNull(reqPacket, " 请求信息为空  ");
        MobileCheckHead head = reqPacket.getHead();
        if (!"018".equals(head.getRequestType())|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())) {
            throw new IllegalArgumentException("请求头参数错误  ");
        }
        CaseNoteResVo resVo = new CaseNoteResVo();
        String registNo = "";
        MobileCheckResponseHead responseHead= new MobileCheckResponseHead();
        try{
            CaseNoteReqBodyVo body = reqPacket.getBody();
            //组织报文start
            SysMsgContentVo sysMsgContentVo = new SysMsgContentVo();
            if(body.getRemark()!=null){
                RemarkInfo remark = body.getRemark();
                checkRemarkInfo(remark);
                sysMsgContentVo.setBussNo(remark.getRegistNo());
                registNo = remark.getRegistNo();
                sysMsgContentVo.setNodeCode(remark.getMessageNode());
                sysMsgContentVo.setMsgType(remark.getRemarkType());
                sysMsgContentVo.setMsgContents(remark.getMessageDesc());
                sysMsgContentVo.setCreateUser(remark.getHandlerName());
                try{
                    sysMsgContentVo.setCreateDate(formatter.parse(remark.getInputDate()));
                }
                catch(ParseException e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else {
                throw new IllegalArgumentException("请求数据错误");
            }
            
            sysMsgContentService.saveSysMsg(sysMsgContentVo);
            
            
            responseHead.setResponseType(head.getRequestType());
            responseHead.setResponseCode("YES");
            responseHead.setResponseMessage("Success");
            resVo.setHead(responseHead);
        }catch(Exception e){
            responseHead.setResponseType(head.getRequestType());
            responseHead.setResponseCode("NO");
            responseHead.setResponseMessage(e.getMessage());
            resVo.setHead(responseHead);
            e.printStackTrace();
        }
        
        stream.processAnnotations(CaseNoteResVo.class);
        logger.info("案件备注返回报文=========：\n"+stream.toXML(resVo));
        return resVo;
    }

    /**
     * 
     * <pre>校验body字段是否为空</pre>
     * @param reqBodyVo
     * @modified:
     * ☆zhujunde(2017年5月31日 下午3:42:15): <br>
     */
   public void checkRemarkInfo(RemarkInfo remark){
       if(StringUtils.isBlank(remark.getRegistNo())){
           throw new IllegalArgumentException("报案号不能为空");
       }
       if(StringUtils.isBlank(remark.getRemarkType())){
           throw new IllegalArgumentException("备注类型不能为空");
       }
       if(StringUtils.isBlank(remark.getHandlerName())){
           throw new IllegalArgumentException("操作员姓名不能为空");
       }
       if(StringUtils.isBlank(remark.getMessageNode())){
           throw new IllegalArgumentException("留言节点不能为空");
       }
       if(StringUtils.isBlank(remark.getInputDate())){
           throw new IllegalArgumentException("填写时间不能为空");
       }
       if(StringUtils.isBlank(remark.getMessageDesc())){
           throw new IllegalArgumentException("留言内容不能为空");
       }
   }
   
   /**
    * 
    * 服务初始化
    * @modified:
    * ☆zhujunde(2017年5月31日 下午3:22:57): <br>
    */
   private void init() {
       if(sysMsgContentService == null){
           sysMsgContentService = (SysMsgContentService)Springs.getBean(SysMsgContentService.class);
       }
   }
}
