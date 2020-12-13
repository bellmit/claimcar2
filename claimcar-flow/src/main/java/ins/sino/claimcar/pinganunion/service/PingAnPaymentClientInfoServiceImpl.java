package ins.sino.claimcar.pinganunion.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLFxqFavoreeVo;
import ins.sino.claimcar.claim.vo.PrpLPayFxqCustomVo;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.pinganUnion.service.PingAnHandleService;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import javax.ws.rs.Path;

import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.vo.PiccCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.pinganUnion.enums.PingAnCodeTypeEnum;
import ins.sino.claimcar.pinganunion.vo.paymentclient.PingAnPaymentClientDTO;
/**
 * 
 * @Description: 平安联盟-反洗钱信息查询接口业务数据处理入口
 * @author: zhubin
 * @date: 2020年8月3日 上午9:34:04
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},group="pingAnPaymentClientInfoService")
@Path("pingAnPaymentClientInfoService")
public class PingAnPaymentClientInfoServiceImpl implements PingAnHandleService {

	 private static Logger logger = LoggerFactory.getLogger(PingAnPaymentClientInfoServiceImpl.class);

    @Autowired
    private PayCustomService payCustomService;
    
    @Autowired
    private PingAnDictService pingAnDictService;    
    
    @Autowired
    private ClaimService claimService; 
    
	@Override
	public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo,
			String respData) {
		logger.info("平安联盟-反洗钱信息查询接口业务数据处理入口--respData={}", respData);

        ResultBean resultBean = ResultBean.success();
        try {
        	//解析json字符串
            List<PingAnPaymentClientDTO> paymentClientDTOList = JSON.parseArray(respData, PingAnPaymentClientDTO.class);//保单先结信息
            //基本校验
            checkData(registNo, paymentClientDTOList);
            
            //数据保存
            savePaymentClientInfo(registNo,paymentClientDTOList);
        }catch (Exception e){
        	logger.error("平安联盟-反洗钱信息查询接口业务数据处理报错：registNo={},error={}", registNo, ExceptionUtils.getStackTrace(e));
            resultBean = resultBean.fail(e.getMessage());
        }

        return resultBean;
	}

    /**
     * 校验数据是否合法
     */
    private void checkData(String registNo,List<PingAnPaymentClientDTO> paymentClientDTOList) {
        if (paymentClientDTOList == null || paymentClientDTOList.size() == 0){
            throw new IllegalArgumentException("反洗钱信息paymentClientDTOList为空");
        }
        
        if (StringUtils.isBlank(registNo)){
            throw new IllegalArgumentException("报案号reportNo为空");
        }
        
    }
    
    private void savePaymentClientInfo(String registNo,List<PingAnPaymentClientDTO> paymentClientDTOList){
    	PrpLPayFxqCustomVo payFxqCustomVo = null;
	 	PrpLFxqFavoreeVo lFxqFavoreeVo = null;
	 	String claimNo = "";
	 	//创建默认用户
        SysUserVo userVo = new SysUserVo();
        userVo.setUserCode("AUTO");
        userVo.setUserName("AUTO");
   	 	List<PrpLClaimVo> claimVos = claimService.findClaimListByRegistNo(registNo,"1");
	 	if(claimVos != null && claimVos.size() > 0){
	 		for (PrpLClaimVo claimVo : claimVos) {
	 			claimNo = claimVo.getClaimNo();
	 			for (PingAnPaymentClientDTO paymentClientDTO : paymentClientDTOList) {
	   	 			payFxqCustomVo = new PrpLPayFxqCustomVo();
	   	 			
	   	 			//payFxqCustomVo.setId(new BigDecimal(""));
	   	 			payFxqCustomVo.setClaimNo(claimNo);
	   	 			payFxqCustomVo.setIsConsistent("1");    
	   	 			payFxqCustomVo.setPayAccountNo("");
	   	 	
	   	   	 		lFxqFavoreeVo = new PrpLFxqFavoreeVo();
	   	   	 		//受益人客户类型  
					PiccCodeDictVo dictData = pingAnDictService.getDictData(PingAnCodeTypeEnum.KHXXLX.getCodeType(), paymentClientDTO.getInfoAttribute());
					if(StringUtils.isNotEmpty(dictData.getDhCodeCode())){
						lFxqFavoreeVo.setCustomerType(dictData.getDhCodeCode()); 
					}else{
						//默认个人
						lFxqFavoreeVo.setCustomerType("1"); 
					}
			   	   	//lFxqFavoreeVo.setId(0L);                      
			   	   	lFxqFavoreeVo.setClaimNo(claimNo);                  
			   	   	lFxqFavoreeVo.setFavoreelBusinessArea(paymentClientDTO.getBusinessScope());
			   	   	lFxqFavoreeVo.setFavoreelBusinessCode(""); 
			   	   	lFxqFavoreeVo.setFavoreelRevenueRegistNo("");
			   	   	lFxqFavoreeVo.setFavoreelInsureRelation("1");    //默认本人  
			   	   	lFxqFavoreeVo.setFavoreeName(paymentClientDTO.getClientName());           
			   	   	lFxqFavoreeVo.setFavoreenPhone(paymentClientDTO.getContact());           
			   	   	lFxqFavoreeVo.setFavoreenSex(paymentClientDTO.getSex());                
			   	   	lFxqFavoreeVo.setFavoreenNatioNality(paymentClientDTO.getCountry());   
			   	   	lFxqFavoreeVo.setFavoreenProfession(paymentClientDTO.getCarrer());
			   	   	//证件类型  
					PiccCodeDictVo dictDataIdenType = pingAnDictService.getDictData(PingAnCodeTypeEnum.ZJLX.getCodeType(), paymentClientDTO.getClientCertificateType());
					if(StringUtils.isNotEmpty(dictDataIdenType.getDhCodeCode())){
						lFxqFavoreeVo.setFavoreeCertifyType(dictDataIdenType.getDhCodeCode()); 
					}else{
						//默认统一社会信用代码
						lFxqFavoreeVo.setFavoreeCertifyType("00"); 
					}
			   	   	lFxqFavoreeVo.setFavoreeIdentifyCode(paymentClientDTO.getClientCertificateNo());      
			   	   	lFxqFavoreeVo.setFavoreeCertifyStartDate(paymentClientDTO.getValidBeginDate()); 
			   	   	lFxqFavoreeVo.setFavoreeCertifyEndDate(paymentClientDTO.getValidEndDate());
			   	    //住址类型: 默认居住地
			   	   	lFxqFavoreeVo.setFavoreenAdressType("1");
			   	   	lFxqFavoreeVo.setFlag("1");
			   	   	lFxqFavoreeVo.setFavoreeAdress(paymentClientDTO.getAddress());
	   	   	 		try {
		   	   	 		payCustomService.saveOrupdatePrpLFxqFavoreeCustom(lFxqFavoreeVo, userVo);
		   				payCustomService.saveOrupdatePrpLPayFxqCustom(payFxqCustomVo, userVo);
					} catch (Exception e) {
						throw new IllegalArgumentException("反洗钱信息保存失败！");
					}
				}
			}
	 	}else{
	 		throw new IllegalArgumentException("反洗钱信息保存失败！");
	 	}
    }
    
}
