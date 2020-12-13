/******************************************************************************
 * CREATETIME : 2015年12月8日 上午10:19:01
 ******************************************************************************/
package ins.sino.claimcar.moblie.certify.service;
import ins.framework.lang.Springs;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.certify.vo.PrpLCertifyCodeVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyItemVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.certify.vo.CertifyBody;
import ins.sino.claimcar.moblie.certify.vo.CertifyDirect;
import ins.sino.claimcar.moblie.certify.vo.CertifyInfoReqVo;
import ins.sino.claimcar.moblie.certify.vo.CertifyInfoResVo;
import ins.sino.claimcar.moblie.certify.vo.CertifyItem;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 单证目录保存接口
 * 
 * <pre></pre>
 * @author ★zjd
 */
public class CertifyInfoServiceImpl implements ServiceInterface {
    private static Logger logger = LoggerFactory.getLogger(CertifyInfoServiceImpl.class);
    @Autowired
    CertifyService certifyService;
    
    @Override
    public Object service(String arg0,Object arg1) {
        init();
        XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
        stream.autodetectAnnotations(true);
        stream.setMode(XStream.NO_REFERENCES);
        stream.aliasSystemAttribute(null,"class");// 去掉 class属性
        stream.processAnnotations(CertifyInfoReqVo.class);
        CertifyInfoReqVo reqPacket =(CertifyInfoReqVo)arg1;
        String xml = stream.toXML(reqPacket);
        logger.info("单证目录保存接口接收报文: \n"+xml);
        Assert.notNull(reqPacket, " 请求信息为空  ");
        MobileCheckHead head = reqPacket.getHead();
        if (!"020".equals(head.getRequestType())|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())) {
            throw new IllegalArgumentException("请求头参数错误  ");
        }
        CertifyInfoResVo resVo = new CertifyInfoResVo();
//        String registNo = "";
        MobileCheckResponseHead responseHead = new MobileCheckResponseHead();
        try{
            CertifyBody body = reqPacket.getBody();
            //组织报文start
            List<CertifyItem> certifyItemList = body.getCertifyItemlist();
            checkCertifyItemInfo(certifyItemList);
            List<PrpLCertifyItemVo> prpLCertifyItemVoList = new ArrayList<PrpLCertifyItemVo>();
            for(CertifyItem certifyItem : certifyItemList){
                //主表
                PrpLCertifyItemVo vo = new PrpLCertifyItemVo();
                vo.setRegistNo(certifyItem.getRegistNo());
                vo.setCertifyTypeCode(certifyItem.getCertifyTypeCode());
                vo.setCertifyTypeName(certifyItem.getCertifytypeName());
                vo.setValidFlag("1");
                vo.setDirectFlag(certifyItem.getDirectFlag());
                vo.setCreateUser(certifyItem.getCreateUser());
                vo.setCreateTime(certifyItem.getCreateTime());
                vo.setUpdateTime(certifyItem.getCreateTime());
                vo.setUpdateUser(certifyItem.getCreateUser());
                
                List<CertifyDirect> certifyDirectList = certifyItem.getCertifyDirect();
                List<PrpLCertifyDirectVo> prpLCertifyDirectVoList = new ArrayList<PrpLCertifyDirectVo>();
                //子表
                if(certifyDirectList!=null && certifyDirectList.size() > 0){
                    for(CertifyDirect certifyDirect : certifyDirectList){
                        PrpLCertifyDirectVo prpLCertifyDirectVo = new PrpLCertifyDirectVo();
                        prpLCertifyDirectVo.setRegistNo(certifyDirect.getRegistNo());
                        prpLCertifyDirectVo.setLossItemCode(certifyDirect.getLossItemCode());
                        prpLCertifyDirectVo.setLossItemName(certifyDirect.getLossItemName());
                        prpLCertifyDirectVo.setTypeCode(certifyDirect.getTypeCode());
                        prpLCertifyDirectVo.setTypeName(certifyDirect.getTypeName());
                        prpLCertifyDirectVo.setCheckNode(certifyDirect.getCheckNode());
                        prpLCertifyDirectVo.setCheckUser(certifyDirect.getCheckUser());
                        prpLCertifyDirectVo.setCreateTime(certifyDirect.getCreateTime());
                        prpLCertifyDirectVo.setCreateUser(certifyDirect.getCheckUser());
                        prpLCertifyDirectVo.setUpdateTime(certifyDirect.getCreateTime());
                        prpLCertifyDirectVo.setUpdateUser(certifyDirect.getCheckUser());
                        prpLCertifyDirectVo.setValidFlag("1");
                        List<PrpLCertifyCodeVo> prpLCertifyCodeVoList = certifyService.findCertifyCodeVoByMustUpload(certifyDirect.getLossItemCode());
                        if(prpLCertifyCodeVoList != null && prpLCertifyCodeVoList.size() > 0 ){
                          prpLCertifyDirectVo.setMustUpload(prpLCertifyCodeVoList.get(0).getMustUpload()); 
                        }
                        prpLCertifyDirectVo.setPrpLCertifyItem(vo);
                        prpLCertifyDirectVoList.add(prpLCertifyDirectVo);
                    } 
                }
                vo.setPrpLCertifyDirects(prpLCertifyDirectVoList);
                
                prpLCertifyItemVoList.add(vo);
            }
            certifyService.savePrpLCertifyItem(prpLCertifyItemVoList);

            responseHead.setResponseType(head.getRequestType());
            responseHead.setResponseCode("YES");
            responseHead.setResponseMessage("Success");
            resVo.setHead(responseHead);
        }catch(Exception e){
            responseHead.setResponseType(head.getRequestType());
            responseHead.setResponseCode("NO");
            responseHead.setResponseMessage(e.getMessage());
            resVo.setHead(responseHead);
            logger.info("====================单证目录保存接口报错信息："+e.getMessage());
            e.printStackTrace();
        }
        
        stream.processAnnotations(CertifyInfoResVo.class);
        logger.info("单证目录保存接口返回报文=========：\n"+stream.toXML(resVo));
        return resVo;
    }

    /**
     * 
     * 检查字段是否为空
     * @param certifyItemList
     * @modified:
     * ☆zhujunde(2017年7月13日 上午11:38:56): <br>
     */
   public void checkCertifyItemInfo(List<CertifyItem> certifyItemList){
       for(CertifyItem certifyItem : certifyItemList){
           if(StringUtils.isBlank(certifyItem.getRegistNo())){
               throw new IllegalArgumentException("报案号不能为空");
           }
           if(StringUtils.isBlank(certifyItem.getCertifyTypeCode())){
               throw new IllegalArgumentException("单证分类代码不能为空");
           }
           if(StringUtils.isBlank(certifyItem.getCertifytypeName())){
               throw new IllegalArgumentException("单证分类名称不能为空");
           }
           /*if(StringUtils.isBlank(certifyItem.getDirectFlag())){
               throw new IllegalArgumentException("收集齐全标志不能为空");
           }*/
           if(StringUtils.isBlank(certifyItem.getCreateUser())){
               throw new IllegalArgumentException("上传人员不能为空");
           }
           if(certifyItem.getCreateTime()==null){
               throw new IllegalArgumentException("上传时间不能为空");
           }
       }

   }
   
   public void checkCertifyDirectInfo(List<CertifyDirect> certifyDirectList){
       for(CertifyDirect certifyDirect : certifyDirectList){
           if(StringUtils.isBlank(certifyDirect.getRegistNo())){
               throw new IllegalArgumentException("报案号不能为空");
           }
           if(StringUtils.isBlank(certifyDirect.getLossItemCode())){
               throw new IllegalArgumentException("单证代码不能为空");
           }
           if(StringUtils.isBlank(certifyDirect.getLossItemName())){
               throw new IllegalArgumentException("单证名称不能为空");
           }
           if(StringUtils.isBlank(certifyDirect.getTypeCode())){
               throw new IllegalArgumentException("单证分类代码不能为空");
           }
           if(StringUtils.isBlank(certifyDirect.getTypeName())){
               throw new IllegalArgumentException("单证分类名称不能为空");
           }
           if(StringUtils.isBlank(certifyDirect.getCheckNode())){
               throw new IllegalArgumentException("勾选节点不能为空");
           }
           if(StringUtils.isBlank(certifyDirect.getCheckUser())){
               throw new IllegalArgumentException("勾选人员代码不能为空");
           }
           if(certifyDirect.getCreateTime()==null){
               throw new IllegalArgumentException("上传时间不能为空");
           }
       }
   }
   /**
    * 
    * 服务初始化
    * @modified:
    * ☆zhujunde(2017年5月31日 下午3:22:57): <br>
    */
   private void init() {
       if(certifyService == null){
           certifyService = (CertifyService)Springs.getBean(CertifyService.class);
       }
   }
}
