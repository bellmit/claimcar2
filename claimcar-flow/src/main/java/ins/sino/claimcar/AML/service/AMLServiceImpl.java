package ins.sino.claimcar.AML.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.utils.Beans;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLFxqFavoreeVo;
import ins.sino.claimcar.claim.vo.PrpLPayFxqCustomVo;
import ins.sino.claimcar.flow.service.AMLService;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.vo.AMLVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.manager.po.PrpLPayCustom;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.regist.service.PrpLCMainService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" })
@Path("AMLService")
public class AMLServiceImpl implements AMLService {
    
    @Autowired
    DatabaseDao databaseDao;
    @Autowired
    PrpLCMainService prpLCMainService;
    @Autowired
    PayCustomService payCustomService;
    @Autowired
    ClaimTaskService claimTaskService;
    @Override
    public ResultPage<PrpLPayCustomVo> findAMLList(PrpLWfTaskQueryVo prpLWfTaskQueryVo,int start,int length) {
        
        SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();
        sqlJoinUtils.append(" FROM PrpLPayCustom payCustom WHERE 1=1 ");
        
        if(StringUtils.isNotBlank(prpLWfTaskQueryVo.getRegistNo())){
            sqlJoinUtils.append(" AND payCustom.registNo like ? ");
            sqlJoinUtils.addParamValue("%"+prpLWfTaskQueryVo.getRegistNo()+"%");
        }
        if(StringUtils.isNotBlank(prpLWfTaskQueryVo.getPayeeName())){
            sqlJoinUtils.append(" AND payCustom.payeeName like ? ");
            sqlJoinUtils.addParamValue("%"+prpLWfTaskQueryVo.getPayeeName()+"%");
        }
        if(StringUtils.isNotBlank(prpLWfTaskQueryVo.getAccountNo())){
            sqlJoinUtils.append(" AND payCustom.accountNo = ? ");
            sqlJoinUtils.addParamValue(prpLWfTaskQueryVo.getAccountNo());
        }
        
        String sql = sqlJoinUtils.getSql();
        Object[] values = sqlJoinUtils.getParamValues();
        
        Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
        List<PrpLPayCustomVo>  resultList = new ArrayList<PrpLPayCustomVo>();
        
        
        for(int i = 0; i<page.getResult().size(); i++ ){
            PrpLPayCustomVo prpLPayCustomVo = new PrpLPayCustomVo(); 
            Object object = page.getResult().get(i);           
            PrpLPayCustom prpLPayCustom = (PrpLPayCustom)object;
            Beans.copy().from(prpLPayCustom).to(prpLPayCustomVo);
            List<PrpLClaimVo> claimVoList= claimTaskService.findClaimListByRegistNo(prpLPayCustomVo.getRegistNo());
            if(claimVoList!=null && claimVoList.size()>0){
            	if(claimVoList.size()==1){
            		for(PrpLClaimVo vo:claimVoList){
            			prpLPayCustomVo.setClaimNo(vo.getClaimNo());
                	}
            	}else{
            		for(PrpLClaimVo vo:claimVoList){
            			if(!"1101".equals(vo.getRiskCode())){
            				prpLPayCustomVo.setClaimNo(vo.getClaimNo());
            			}
                	}
            	}
            	
            }
            resultList.add(prpLPayCustomVo);
        }
        
        ResultPage<PrpLPayCustomVo> resultPage = new ResultPage<PrpLPayCustomVo>(start,length,page.getTotalCount(),resultList);
        return resultPage;
    }

    
    @Override
    public AMLVo findAMLInfo(Long mainId,String registNo,String claimNo,String signFlag) {
        
        AMLVo amlVo = new AMLVo();
        String sign="3";//1-反洗钱旧数据，2-反洗钱新数据
        PrpLPayCustomVo prpLPayCustomVo = new PrpLPayCustomVo();
        PrpLPayFxqCustomVo prpLPayFxqCustomVo = new PrpLPayFxqCustomVo();
        PrpLFxqFavoreeVo prpLFxqFavoreeVo = new PrpLFxqFavoreeVo();
        List<PrpLPayFxqCustomVo> prpLPayFxqCustomVoList=new ArrayList<PrpLPayFxqCustomVo>();
        List<PrpLFxqFavoreeVo> prpLFxqFavoreeVoList=new ArrayList<PrpLFxqFavoreeVo>();
        if(StringUtils.isNotBlank(claimNo)){
        	prpLPayFxqCustomVoList = payCustomService.findPrpLPayFxqCustomVoByclaimNo(claimNo);
        	if(prpLPayFxqCustomVoList!=null && prpLPayFxqCustomVoList.size()>0){
                prpLPayFxqCustomVo = prpLPayFxqCustomVoList.get(0);
            }
        	
        	 prpLFxqFavoreeVoList = payCustomService.findPrpLFxqFavoreeVoByclaimNo(claimNo);
            if(prpLFxqFavoreeVoList!=null && prpLFxqFavoreeVoList.size()>0){
                prpLFxqFavoreeVo = prpLFxqFavoreeVoList.get(0);
               if("SY".equals(signFlag)){
            	   amlVo.setFxqSeeflag("SY");
            	   amlVo.setFavoreeVo(prpLFxqFavoreeVo);
            	   
            	  }
            }
            if((prpLPayFxqCustomVoList==null && prpLFxqFavoreeVoList==null) || (prpLPayFxqCustomVoList.size()==0 && prpLFxqFavoreeVoList.size()==0)){
            	 if(mainId!=null){
                 	prpLPayCustomVo = payCustomService.findPayCustomVoById(mainId);
                     prpLPayFxqCustomVoList = payCustomService.findPrpLPayFxqCustomVoByPayId(mainId);
                     if(prpLPayFxqCustomVoList!=null && prpLPayFxqCustomVoList.size()>0){
                         prpLPayFxqCustomVo = prpLPayFxqCustomVoList.get(0);
                     }
                      prpLFxqFavoreeVoList = payCustomService.findPrpLFxqFavoreeVoByPayId(mainId);
                     if(prpLFxqFavoreeVoList!=null && prpLFxqFavoreeVoList.size()>0){
                         prpLFxqFavoreeVo = prpLFxqFavoreeVoList.get(0);
                     }
                     sign="1";
                 }
            }else{
            	
            	sign="2";
            }
        }else{
        	 if(mainId!=null){
             	prpLPayCustomVo = payCustomService.findPayCustomVoById(mainId);
                prpLPayFxqCustomVoList = payCustomService.findPrpLPayFxqCustomVoByPayId(mainId);
                 if(prpLPayFxqCustomVoList!=null && prpLPayFxqCustomVoList.size()>0){
                     prpLPayFxqCustomVo = prpLPayFxqCustomVoList.get(0);
                 }
                 prpLFxqFavoreeVoList = payCustomService.findPrpLFxqFavoreeVoByPayId(mainId);
                 if(prpLFxqFavoreeVoList!=null && prpLFxqFavoreeVoList.size()>0){
                     prpLFxqFavoreeVo = prpLFxqFavoreeVoList.get(0);
                 }
                 sign="1";
             }
        }
       
        
        
        
        AMLVo amlVoTemp = prpLCMainService.findAMLInfoByRegistNo(registNo);
        amlVo.setClaimNo(claimNo);
        //被保险人信息
        amlVo.setInsuredName(amlVoTemp.getInsuredName());
        amlVo.setSex(amlVoTemp.getSex());
        amlVo.setEducationCode(amlVoTemp.getEducationCode());//国籍
        amlVo.setIdentifyType(amlVoTemp.getIdentifyType());
        amlVo.setIdentifyNumber(amlVoTemp.getIdentifyNumber());
        amlVo.setCertifyDate(amlVoTemp.getCertifyDate());//证件有效期
        amlVo.setProfession(amlVoTemp.getProfession());
        amlVo.setMobile(amlVoTemp.getMobile());
        amlVo.setAdressType("");//TODO 被保险人地址类型
        amlVo.setAdress(amlVoTemp.getAdress());
        amlVo.setBusinessCode(amlVoTemp.getBusinessCode());
        amlVo.setRevenueRegistNo(amlVoTemp.getRevenueRegistNo());
        amlVo.setInsureRelation(amlVoTemp.getInsureRelation());
        amlVo.setPayAccountNo(prpLPayFxqCustomVo.getPayAccountNo());//缴费
        amlVo.setAcconutNo(prpLPayCustomVo.getAccountNo());//收款
        amlVo.setIsConsistent(prpLPayFxqCustomVo.getIsConsistent());//账号一致性
        amlVo.setInsuredType(amlVoTemp.getInsuredType());//自然人法人标志      
        //法定代表人
       
        amlVo.setFxqCustomId(prpLPayFxqCustomVo.getId());
        amlVo.setLegalPerson(prpLPayFxqCustomVo.getLegalPerson());
        amlVo.setLegalPhone(prpLPayFxqCustomVo.getLegalPhone());
        amlVo.setLegalIdentifyType(prpLPayFxqCustomVo.getLegalCertifyType());
        amlVo.setLegalIdentifyCode(prpLPayFxqCustomVo.getLegalIdentifyCode());
        amlVo.setLegalCertifyStartDate(prpLPayFxqCustomVo.getLegalCertifyStartDate());
        amlVo.setLegalCertifyEndDate(prpLPayFxqCustomVo.getLegalCertifyEndDate());
        //授权办理人
        if("1".equals(sign)){
        	amlVo.setAuthorityName(prpLPayCustomVo.getAuthorityName());
            amlVo.setAuthorityPhone(prpLPayCustomVo.getAuthorityPhone());
            amlVo.setAuthorityCertifyType(prpLPayCustomVo.getAuthorityCertifyType());
            amlVo.setAuthorityNo(prpLPayCustomVo.getAuthorityNo());
            amlVo.setAuthorityStartDate(prpLPayCustomVo.getAuthorityStartDate());
            amlVo.setAuthorityEndDate(prpLPayCustomVo.getAuthorityEndDate());
        }else if("2".equals(sign)){
        	amlVo.setAuthorityName(prpLPayFxqCustomVo.getAuthorityName());
            amlVo.setAuthorityPhone(prpLPayFxqCustomVo.getAuthorityPhone());
            amlVo.setAuthorityCertifyType(prpLPayFxqCustomVo.getAuthorityCertifyType());
            amlVo.setAuthorityNo(prpLPayFxqCustomVo.getAuthorityNo());
            amlVo.setAuthorityStartDate(prpLPayFxqCustomVo.getAuthorityStartDate());
            amlVo.setAuthorityEndDate(prpLPayFxqCustomVo.getAuthorityEndDate());
        }
        
        
        amlVo.setCustomerType(prpLFxqFavoreeVo.getCustomerType());//受益人类型
        amlVo.setFavoreeName(prpLFxqFavoreeVo.getFavoreeName());//受益人名字名称
        amlVo.setFavoreeAdress(prpLFxqFavoreeVo.getFavoreeAdress());//受益人地址
        amlVo.setFavoreeCertifyStartDate(prpLFxqFavoreeVo.getFavoreeCertifyStartDate());
        amlVo.setFavoreeCertifyEndDate(prpLFxqFavoreeVo.getFavoreeCertifyEndDate());
        //受益人自然人信息  
        if(prpLFxqFavoreeVo.getId()!=null){
        	amlVo.setFxqFavoreeId(new BigDecimal(prpLFxqFavoreeVo.getId()));
        }
        amlVo.setFavoreenSex(prpLFxqFavoreeVo.getFavoreenSex());
        amlVo.setFavoreenNatioNality(prpLFxqFavoreeVo.getFavoreenNatioNality());
        amlVo.setFavoreeCertifyType(prpLFxqFavoreeVo.getFavoreeCertifyType());
        amlVo.setFavoreeIdentifyCode(prpLFxqFavoreeVo.getFavoreeIdentifyCode());
        amlVo.setFavoreenProfession(prpLFxqFavoreeVo.getFavoreenProfession());
        amlVo.setFavoreenPhone(prpLFxqFavoreeVo.getFavoreenPhone());
        amlVo.setFavoreenAdressType(prpLFxqFavoreeVo.getFavoreenAdressType());
        
        //受益人法人
        amlVo.setFavoreelBusinessArea(prpLFxqFavoreeVo.getFavoreelBusinessArea());
        amlVo.setFavoreelRevenueRegistNo(prpLFxqFavoreeVo.getFavoreelRevenueRegistNo());
        amlVo.setFavoreelBusinessCode(prpLFxqFavoreeVo.getFavoreelBusinessCode());
        
        amlVo.setFavoreelInsureRelation(prpLFxqFavoreeVo.getFavoreelInsureRelation());
        return amlVo;
    }
}
