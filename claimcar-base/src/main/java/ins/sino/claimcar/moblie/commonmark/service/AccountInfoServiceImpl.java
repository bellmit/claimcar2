package ins.sino.claimcar.moblie.commonmark.service;
import ins.framework.lang.Springs;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.vo.SysAreaDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.mobile.check.vo.AccountInfoInitResVo;
import ins.sino.claimcar.mobile.check.vo.AccountInfoResBodyVo;
import ins.sino.claimcar.mobile.check.vo.AccountInfoSubmitReqBodyVo;
import ins.sino.claimcar.mobile.check.vo.AccountInfoSubmitReqVo;
import ins.sino.claimcar.mobile.check.vo.AccountInfoVo;
import ins.sino.claimcar.mobile.check.vo.AccountResVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.logUtil.QuickClaimLogUtil;

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
 * 收款人信息（快赔请求理赔）
 * <pre></pre>
 * @author ★zhujunde
 */
public class AccountInfoServiceImpl implements ServiceInterface {
    private static Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class);

    @Autowired
    private PayCustomService payCustomService;
    @Autowired
    AreaDictService areaDictService;
    
    @Override
    public Object service(String arg0,Object arg1) {
        init();
        XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
        stream.autodetectAnnotations(true);
        stream.setMode(XStream.NO_REFERENCES);
        stream.aliasSystemAttribute(null,"class");// 去掉 class属性
        stream.processAnnotations(AccountInfoSubmitReqVo.class);
        AccountInfoSubmitReqVo reqPacket =(AccountInfoSubmitReqVo)arg1;
        String xml = stream.toXML(reqPacket);
        logger.info("收款人信息（快赔请求理赔）接收报文: \n"+xml);
        Assert.notNull(reqPacket, " 请求信息为空  ");
        MobileCheckHead head = reqPacket.getHead();
        if (!"019".equals(head.getRequestType())|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())) {
            throw new IllegalArgumentException("请求头参数错误  ");
        }
        AccountInfoInitResVo resVo = new AccountInfoInitResVo();
        String registNo = "";
        MobileCheckResponseHead responseHead= new MobileCheckResponseHead();
        List<AccountResVo> accountResVoList = new ArrayList<AccountResVo>();
        try{
            AccountInfoSubmitReqBodyVo body = reqPacket.getBody();
            //组织报文start
            if(body.getAccountInfo()!=null && body.getAccountInfo().size() > 0){
                SysUserVo userVo = new SysUserVo();
                userVo.setComCode(body.getScheduleObjectId());
                userVo.setUserCode(body.getNextHandlerCode());
                userVo.setUserName(body.getNextHandlerName());
                userVo.setComName(body.getScheduleObjectName());
                List<AccountInfoVo>  accountInfoVoList = body.getAccountInfo();
                for(AccountInfoVo account : accountInfoVoList){
                    checkAccountInfo(account);//检查数据
                }
                for(AccountInfoVo account : accountInfoVoList){//保存或者更新收款人
                    saveAccountInfo(accountResVoList,account,userVo,body);
                }
            }
            else {
                throw new IllegalArgumentException("请求数据错误");
            }
            AccountInfoResBodyVo resBodyVo = new AccountInfoResBodyVo();
            resBodyVo.setAccountResVoList(accountResVoList);
            resVo.setBody(resBodyVo);
            responseHead.setResponseType(head.getRequestType());
            responseHead.setResponseCode("YES");
            responseHead.setResponseMessage("Success");
            resVo.setHead(responseHead);
        }catch(Exception e){
            responseHead.setResponseType(head.getRequestType());
            responseHead.setResponseCode("NO");
            responseHead.setResponseMessage(e.getMessage());
            resVo.setHead(responseHead);
            logger.info("收款人信息（快赔请求理赔）接口报错信息：");
            e.printStackTrace();
        }
        
        stream.processAnnotations(AccountInfoInitResVo.class);
        logger.info("收款人信息（快赔请求理赔）返回报文=========：\n"+stream.toXML(resVo));
        return resVo;
    }

    /**
     * 
     * <pre>校验body字段是否为空</pre>
     * @param reqBodyVo
     * @throws Exception 
     * @modified:
     * ☆zhujunde(2017年5月31日 下午3:42:15): <br>
     */
   public void checkAccountInfo(AccountInfoVo account) throws Exception{
       if(StringUtils.isBlank(account.getAccountSerialNo())){
           throw new IllegalArgumentException("移动终端收款人序号不能为空");
       }
       if(StringUtils.isBlank(account.getPayeeNature())){
           throw new IllegalArgumentException("收款人性质不能为空");
       }
       if(StringUtils.isBlank(account.getIdentifyType())){
           throw new IllegalArgumentException("证件类型不能为空");
       }
       if(StringUtils.isBlank(account.getPayeeType())){
           throw new IllegalArgumentException("收款人类型不能为空");
       }
       if(StringUtils.isBlank(account.getName())){
           throw new IllegalArgumentException("收款人户名不能为空");
       }
       if(StringUtils.isBlank(account.getPubandPrilogo())){
           throw new IllegalArgumentException("公私标志不能为空");
       }
       if(StringUtils.isBlank(account.getIdentifyNumber())){
           throw new IllegalArgumentException("证件号码不能为空");
       }
       if(StringUtils.isBlank(account.getAccountNo())){
           throw new IllegalArgumentException("收款人账号不能为空");
       }
       if(StringUtils.isBlank(account.getProvinceCode())){
           throw new IllegalArgumentException("收款人开户行归属地省代码不能为空");
       }
       if(StringUtils.isBlank(account.getCityCode())){
           throw new IllegalArgumentException("收款人开户行归属地市代码不能为空");
       }
       if(StringUtils.isBlank(account.getBankName())){
           throw new IllegalArgumentException("收款方开户行不能为空");
       }
       if(StringUtils.isBlank(account.getBranchName())){
           throw new IllegalArgumentException("收款人开户行分行名称不能为空");
       }
       if(StringUtils.isBlank(account.getBankCode())){
           throw new IllegalArgumentException("收款人银行行号不能为空");
       }
       if(StringUtils.isBlank(account.getTransferMode())){
           throw new IllegalArgumentException("转账汇款模式不能为空");
       }
       if(StringUtils.isBlank(account.getPhone())){
           throw new IllegalArgumentException("收款人手机号码不能为空");
       }
       if(StringUtils.isBlank(account.getDigest())){
           throw new IllegalArgumentException("摘要不能为空");
       }
   }
   
   /**
    * 
    * 保存收款人数据
    * @param accountInfo
    * @param account
    * @param userVo
    * @throws Exception
    * @modified:
    * ☆zhujunde(2017年7月11日 下午3:35:50): <br>
    */
   public void saveAccountInfo(List<AccountResVo> accountInfo,AccountInfoVo account,SysUserVo userVo,AccountInfoSubmitReqBodyVo body) throws Exception{
       PrpLPayCustomVo prpLPayCustomVo = new PrpLPayCustomVo();
       AccountResVo accountResVo = new AccountResVo();
       if(StringUtils.isNotBlank(account.getAccountId())){
           prpLPayCustomVo.setId(Long.valueOf(account.getAccountId())); 
       }
       prpLPayCustomVo.setRegistNo(body.getRegistNo());
       prpLPayCustomVo.setPayObjectType(account.getPayeeNature());
       prpLPayCustomVo.setCertifyType(account.getIdentifyType());
       prpLPayCustomVo.setPayObjectKind(account.getPayeeType());
       prpLPayCustomVo.setPayeeName(account.getName());
       prpLPayCustomVo.setPublicAndPrivate(account.getPubandPrilogo());
       prpLPayCustomVo.setCertifyNo(account.getIdentifyNumber());
       prpLPayCustomVo.setAccountNo(account.getAccountNo());
       prpLPayCustomVo.setProvinceCode(Long.valueOf(account.getProvinceCode()));
       List<SysAreaDictVo> provinceVo = areaDictService.findAreaCode(account.getProvinceCode());
       if(provinceVo!=null && provinceVo.size() > 0){
           prpLPayCustomVo.setProvince(provinceVo.get(0).getAreaName()); 
       }
       List<SysAreaDictVo> cityVo = areaDictService.findAreaCode(account.getCityCode());
       if(cityVo!=null && cityVo.size() > 0){
           prpLPayCustomVo.setCity(cityVo.get(0).getAreaName()); 
       }
       prpLPayCustomVo.setCityCode(Long.valueOf(account.getCityCode()));
       prpLPayCustomVo.setBankName(account.getBankName());
       prpLPayCustomVo.setBankOutlets(account.getBranchName());
       prpLPayCustomVo.setBankNo(account.getBankCode());
       prpLPayCustomVo.setPriorityFlag(account.getTransferMode());
       prpLPayCustomVo.setPayeeMobile(account.getPhone());
       //去除空格
       if(StringUtils.isNotBlank(account.getDigest())){
           prpLPayCustomVo.setSummary(account.getDigest().replaceAll("\\s*", ""));
       }
       prpLPayCustomVo.setAccountNo(account.getAccountNo().replaceAll("\\s*", ""));
       prpLPayCustomVo.setCertifyNo(account.getIdentifyNumber().replaceAll("\\s*", ""));
       prpLPayCustomVo.setPayeeName(account.getName().replaceAll("\\s*", ""));
      // 添加管控，如果银行账号相同的数据在系统中已存在，且户名不同，提示账户已存在户名不同，且不允许保存 硬管控
		PrpLPayCustomVo existPayCus = payCustomService.adjustExistSamePayCusDifName(prpLPayCustomVo);
		if(existPayCus!=null){
			throw new IllegalArgumentException("保存失败！该账号已存于案件"+existPayCus.getRegistNo()+"，且户名为"+existPayCus.getPayeeName()+"！");
		}
       Long id = payCustomService.saveOrUpdatePayCustom(prpLPayCustomVo,userVo);
       //设置返回的收款人
       accountResVo.setAccountId(String.valueOf(id));
       accountResVo.setAccountSerialNo(account.getAccountSerialNo());
       accountResVo.setRegistNo(body.getRegistNo());
       accountInfo.add(accountResVo);
   }
  /**
   * 
   * 服务初始化
   * @modified:
   * ☆zhujunde(2017年7月11日 下午2:58:17): <br>
   */
   private void init() {
       if(payCustomService == null){
           payCustomService = (PayCustomService)Springs.getBean(PayCustomService.class);
       }
       if(areaDictService == null){
           areaDictService = (AreaDictService)Springs.getBean(AreaDictService.class);
       }
       
   }
}
