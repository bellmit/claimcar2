package ins.sino.claimcar.pinganunion.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;

import com.lowagie.text.Paragraph;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.vo.PiccCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carplatform.util.StringUtil;
import ins.sino.claimcar.claim.po.PrpLPayCustom;
import ins.sino.claimcar.claim.po.PrpLPrePay;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.*;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.manager.vo.AccBankNameVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.other.service.AccountInfoService;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;
import ins.sino.claimcar.padpay.po.PrpLPadPayMain;
import ins.sino.claimcar.padpay.po.PrpLPadPayPerson;
import ins.sino.claimcar.pinganUnion.service.PingAnHandleService;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.pinganunion.vo.compensate.UnionCompensateAllParamsDto;
import ins.sino.claimcar.pinganunion.vo.payment.UnionPaymentResponseDataDto;
import ins.sino.claimcar.pinganunion.vo.payment.UnionPaymentResultDto;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;
import java.util.*;

/**
 * ??????????????????-????????????????????????-????????????
 *
 * @author mfn
 * @date 2020/8/7 11:51
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, group = "pingAnPaymentHandleService")
@Path("pingAnPaymentHandleService")
public class PingAnPaymentHandleServiceImpl implements PingAnHandleService {
    private static Logger logger = LoggerFactory.getLogger(PingAnPaymentHandleServiceImpl.class);
    @Autowired
    PingAnDictService pingAnDictService;
    @Autowired
    private PayCustomService payCustomService;
    @Autowired
	CompensateTaskService  compensateTaskService;
    @Autowired
	private CompensateService compensateService;
    @Autowired
	private DatabaseDao databaseDao;
    @Autowired
    private AreaDictService areaDictService;
    @Autowired
    private RegistService registService;
    @Autowired
    private VerifyClaimService verifyClaimService;
    @Autowired
    InterfaceAsyncService interfaceAsyncService;
    @Autowired
    private PolicyViewService policyViewService;
    @Autowired
    private ClaimInterfaceLogService logService;
    @Autowired
    private AccountInfoService accountInfoService;
    /**
     * ??????????????????????????????
     *
     * @param registNo ???????????????
     * @param paramObj ??????????????????json
     * @param respData ??????????????????json
     * @return
     */
    @Override
    public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
        logger.info("??????????????????-??????????????????????????????????????? {} ??????????????? {}", pingAnDataNoticeVo == null ? "" : pingAnDataNoticeVo.getParamObj(), respData);
        ResultBean resultBean = ResultBean.success();
        Gson gson = new Gson();
        PiccCodeDictVo codeDictVo;
        try {
            UnionPaymentResponseDataDto paymentResponseDataDto = gson.fromJson(respData, UnionPaymentResponseDataDto.class);
            List<UnionPaymentResultDto> paymentResultDtoList = paymentResponseDataDto.getPaymentResultList();
            
            // ????????????????????????????????????
            String paramObj = pingAnDataNoticeVo.getParamObj();
            logger.info(paramObj);
            UnionCompensateAllParamsDto allParamsDto = gson.fromJson(paramObj, UnionCompensateAllParamsDto.class);
            // ???????????????????????? 1-?????? 2-?????? 3-?????? 7-??????
            String claimType = allParamsDto.getClaimType();
            logger.info("??????????????????????????????????????????claimType???{}",claimType);
            if (paymentResultDtoList != null && paymentResultDtoList.size() > 0) {
                logger.info("???????????????????????????paymentResultDtoList????????????????????????claimType???{}",claimType);
                String certifyType = "99";
                Long payeeid = null;
                for (UnionPaymentResultDto paymentResultDto : paymentResultDtoList) {
                    String accountNo = paymentResultDto.getClientBankAccount();
                    payeeid = getPaycustomIdWithSameAccountNo(registNo, accountNo);
                    if (null == payeeid) {
                        PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();
                        payCustomVo.setRegistNo(registNo);
                        payCustomVo.setPaicclaimNo(paymentResultDto.getCaseNo());
                        payCustomVo.setCaseTimes(paymentResultDto.getCaseTimes());
                        // ????????????
                        codeDictVo = pingAnDictService.getDictData("certificateType", paymentResultDto.getClientCertificateType());
                        if (null != codeDictVo && codeDictVo.getDhCodeCode() != null) {
                            certifyType = codeDictVo.getDhCodeCode();
                        }
                        certifyType = this.transCertifyType(certifyType);
                        payCustomVo.setCertifyType(certifyType);
                        payCustomVo.setCertifyNo(paymentResultDto.getClientCertificateNo());
                        payCustomVo.setPayObjectType("0".equals(paymentResultDto.getBankAccountAttribute()) ? "2" : "1");
                        payCustomVo.setPayeeName(paymentResultDto.getClientName());
                        payCustomVo.setPayeeMobile(paymentResultDto.getClientMobile());
                        payCustomVo.setBankOutlets(paymentResultDto.getClientBankName());
                        payCustomVo.setAccountNo(accountNo);
                        payCustomVo.setBankNo(paymentResultDto.getClientBankCode());
                        // ????????????????????????????????????????????????
                        if (paymentResultDto.getClientBankCode() != null && paymentResultDto.getClientBankName() != null) {
                            AccBankNameVo accBankNameVo = payCustomService.findBankInfoFromName(paymentResultDto.getClientBankName(),paymentResultDto.getClientBankCode());
                            if (accBankNameVo != null) {
                                payCustomVo.setBankName(accBankNameVo.getBankCode());//?????????????????????
                                payCustomVo.setProvinceCode(Long.parseLong(accBankNameVo.getProvinceCode()));//?????????
                                payCustomVo.setProvince(accBankNameVo.getProvincial());//?????????
                                payCustomVo.setCityCode(Long.parseLong(accBankNameVo.getCityCode()));//?????????
                                payCustomVo.setCity(accBankNameVo.getCity());//?????????
                                payCustomVo.setBankType(accBankNameVo.getBankCode());
                            }
                        }
                        payCustomVo.setPriorityFlag("N");
                        payCustomVo.setPurpose("????????????????????????");
                        payCustomVo.setValidFlag("1");
                        payCustomVo.setFlag("0");
                        payCustomVo.setRemark("N");
                        payCustomVo.setCreateUser("AUTO");
                        payCustomVo.setCreateTime(new Date());
                        payCustomVo.setUpdateUser("AUTO");
                        payCustomVo.setUpdateTime(new Date());
                        String clientType = "99";
                        codeDictVo = pingAnDictService.getDictData("clientType", paymentResultDto.getClientType());
                        if (null != codeDictVo && codeDictVo.getDhCodeCode() != null) {
                            clientType = codeDictVo.getDhCodeCode();
                        }
                        payCustomVo.setPayObjectKind(clientType);
                        payCustomVo.setPublicAndPrivate("0".equals(paymentResultDto.getBankAccountAttribute()) ? "1" : "0");
                        payCustomVo.setIdClmPaymentResult(paymentResultDto.getIdClmPaymentResult());

                        PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
                        SysUserVo userVo = new SysUserVo();
                        userVo.setUserCode("AUTO");
                        userVo.setUserName("AUTO");
                        userVo.setComCode(registVo.getComCode());
                        // ???????????????????????????
                        payeeid = payCustomService.saveOrUpdatePayCustom(payCustomVo, userVo);
                    }



                    String oldIdClmPaymentResult = paymentResultDto.getIdClmPaymentResultOld();
                    // ?????????????????????id????????????id???????????????id
                    Map<String, Long> payeeidMap = new HashMap<String, Long>();
                    if (StringUtil.isNotBlank(oldIdClmPaymentResult)) {
                        Map<String, String> claimTypeAndCompensateNo = new HashMap<String, String>();
                        logger.info("????????????oldIdClmPaymentResult???????????????????????????oldIdClmPaymentResult???{}",oldIdClmPaymentResult);
                        // ?????????????????????????????????
                        Long oldpayeeid = null;
                        Long newpayeeid = null;
                        if("2".equals(claimType)){//??????
                            //???????????????????????????????????????payeeId,?????????????????????????????????
                            String policyNo = paymentResultDto.getPolicyNo();
                            String riskCode = "";
                            if (StringUtil.isNotBlank(policyNo) && !policyNo.startsWith("B")) {
                                policyNo = "B" + policyNo;
                                PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(registNo, policyNo);
                                riskCode = cMainVo.getRiskCode();
                            }
                            List<PrpLPrePayVo> prePayVoList = compensateService.queryLossPrePay(registNo,riskCode);
                            if(prePayVoList != null && prePayVoList.size()>0){
                                for (PrpLPrePayVo prepayVo :  prePayVoList) {
                                    //?????????????????????????????????payeeId
                                    if(oldIdClmPaymentResult.equals(prepayVo.getIdClmPaymentResult())){
                                        oldpayeeid = prepayVo.getPayeeId();
                                        //??????????????????KEY???????????????????????????????????????????????????MAP????????????????????????????????????
                                        if(!claimTypeAndCompensateNo.containsKey(claimType)){
                                            claimTypeAndCompensateNo.put(claimType,prepayVo.getCompensateNo());
                                        }
                                    }
                                }
                            }else{
                                throw new IllegalArgumentException("?????????????????????????????????????????????????????????????????????????????????");
                            }
                        }else if("3".equals(claimType)){//??????
                            if(paymentResultDto.getCaseTimes() != null ) {
                                List<PrpLPadPayMainVo> listPrpLPadPayMainVo = compensateService.findPadPayMainByRegistNo(registNo);
                                //????????????????????????????????????
                                if (listPrpLPadPayMainVo != null && listPrpLPadPayMainVo.size() > 0) {
                                    for (PrpLPadPayMainVo prpLPadPayMainVo : listPrpLPadPayMainVo) {
                                        //?????????????????????????????????????????????????????????????????????
                                        if (prpLPadPayMainVo.getCaseTimes() != null && prpLPadPayMainVo.getCaseTimes() == paymentResultDto.getCaseTimes()) {
                                            List<PrpLPadPayPersonVo> listPrpLPadPayPersonVo = prpLPadPayMainVo.getPrpLPadPayPersons();
                                            if(listPrpLPadPayPersonVo != null && listPrpLPadPayPersonVo.size()>0){
                                                for (PrpLPadPayPersonVo prpLPadPayPersonVo : listPrpLPadPayPersonVo) {
                                                    if(oldIdClmPaymentResult.equals(prpLPadPayPersonVo.getIdClmPaymentResult())){
                                                        oldpayeeid = prpLPadPayPersonVo.getPayeeId();
                                                        //??????????????????KEY???????????????????????????????????????????????????MAP????????????????????????????????????
                                                        if(!claimTypeAndCompensateNo.containsKey(claimType)){
                                                            claimTypeAndCompensateNo.put(claimType,prpLPadPayMainVo.getCompensateNo());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else{
                            List<PrpLCompensateVo> listCompensateVo=compensateService.findCompensatevosByRegistNo(registNo);
                            for (PrpLCompensateVo compensateVo : listCompensateVo) {
                                List<PrpLPaymentVo> prpLPaymentVoList=compensateVo.getPrpLPayments();
                                for (PrpLPaymentVo prpLPaymentVo : prpLPaymentVoList) {
                                    if(oldIdClmPaymentResult.equals(prpLPaymentVo.getIdClmPaymentResult())){
                                        oldpayeeid =  prpLPaymentVo.getPayeeId();
                                        //??????????????????KEY???????????????????????????????????????????????????MAP????????????????????????????????????
                                        if(!claimTypeAndCompensateNo.containsKey(claimType)){
                                            claimTypeAndCompensateNo.put(claimType,prpLPaymentVo.getCompensateNo());
                                        }
                                    }
                                }
                            }
                        }
                    	// ????????????????????????????????????
                    	List<PrpLPayCustomVo> paycustomVoList = payCustomService.findPayCustomVoByRegistNo(registNo);
                        String claimNo = "";
                   	    if (paycustomVoList != null) {
                    		for (PrpLPayCustomVo vo : paycustomVoList) {
                    			if ("1".equals(vo.getValidFlag()) && oldIdClmPaymentResult.equals(vo.getIdClmPaymentResult())) {
                                    oldpayeeid = vo.getId();
                    			    vo.setValidFlag("0");
                                    claimNo = vo.getClaimNo();
                    				payCustomService.updatePaycustom(vo);
                    			}
                    		}
                    	}
                        PrpLPayCustomVo payCustomVo = null;
                   	    if(null != oldpayeeid){
                            payCustomVo = new PrpLPayCustomVo();
                            payCustomVo.setRegistNo(registNo);
                            payCustomVo.setClaimNo(claimNo);
                            payCustomVo.setPaicclaimNo(paymentResultDto.getCaseNo());
                            payCustomVo.setCaseTimes(paymentResultDto.getCaseTimes());
                            // ????????????
                            codeDictVo = pingAnDictService.getDictData("certificateType", paymentResultDto.getClientCertificateType());
                            if (null != codeDictVo && codeDictVo.getDhCodeCode() != null) {
                                certifyType = codeDictVo.getDhCodeCode();
                            }
                            payCustomVo.setCertifyType(certifyType);
                            payCustomVo.setCertifyNo(paymentResultDto.getClientCertificateNo());
                            payCustomVo.setPayObjectType("0".equals(paymentResultDto.getBankAccountAttribute()) ? "2" : "1");
                            payCustomVo.setPayeeName(paymentResultDto.getClientName());
                            payCustomVo.setPayeeMobile(paymentResultDto.getClientMobile());
                            payCustomVo.setBankOutlets(paymentResultDto.getClientBankName());
                            payCustomVo.setAccountNo(accountNo);
                            payCustomVo.setBankNo(paymentResultDto.getClientBankCode());
                            // ????????????????????????????????????????????????
                            if (paymentResultDto.getClientBankCode() != null && paymentResultDto.getClientBankName() != null) {
                                AccBankNameVo accBankNameVo = payCustomService.findBankInfoFromName(paymentResultDto.getClientBankName(),paymentResultDto.getClientBankCode());
                                if (accBankNameVo != null) {
                                    payCustomVo.setBankName(accBankNameVo.getBankCode());//?????????????????????
                                    payCustomVo.setProvinceCode(Long.parseLong(accBankNameVo.getProvinceCode()));//?????????
                                    payCustomVo.setProvince(accBankNameVo.getProvincial());//?????????
                                    payCustomVo.setCityCode(Long.parseLong(accBankNameVo.getCityCode()));//?????????
                                    payCustomVo.setCity(accBankNameVo.getCity());//?????????
                                    payCustomVo.setBankType(accBankNameVo.getBankCode());
                                }
                            }
                            payCustomVo.setPriorityFlag("N");
                            payCustomVo.setPurpose("????????????????????????");
                            payCustomVo.setValidFlag("1");
                            payCustomVo.setFlag("0");
                            payCustomVo.setRemark("N");
                            payCustomVo.setCreateUser("AUTO");
                            payCustomVo.setCreateTime(new Date());
                            payCustomVo.setUpdateUser("AUTO");
                            payCustomVo.setUpdateTime(new Date());
                            String clientType = "99";
                            codeDictVo = pingAnDictService.getDictData("clientType", paymentResultDto.getClientType());
                            if (null != codeDictVo && codeDictVo.getDhCodeCode() != null) {
                                clientType = codeDictVo.getDhCodeCode();
                            }
                            payCustomVo.setPayObjectKind(clientType);
                            payCustomVo.setPublicAndPrivate("0".equals(paymentResultDto.getBankAccountAttribute()) ? "1" : "0");
                            payCustomVo.setIdClmPaymentResult(paymentResultDto.getIdClmPaymentResult());

                            PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
                            SysUserVo userVo = new SysUserVo();
                            userVo.setUserCode("AUTO");
                            userVo.setUserName("AUTO");
                            userVo.setComCode(registVo.getComCode());
                            newpayeeid = payCustomService.saveOrUpdatePayCustom(payCustomVo, userVo);//???????????????payeeid
                        }
                        //????????????payeeid?????????????????????????????????????????????
                        if(newpayeeid != null){
                            String serialNo = "";
                            String compensateNo = claimTypeAndCompensateNo.get(claimType);
                            if("2".equals(claimType)){
                                List<PrpLPrePayVo> prePayVoList = compensateService.queryPrePay(compensateNo);
                                if(prePayVoList != null && prePayVoList.size()>0){
                                    for (PrpLPrePayVo prpLPrePayVo : prePayVoList) {
                                        prpLPrePayVo.setPayeeId(newpayeeid);
                                        serialNo = prpLPrePayVo.getSerialNo();
                                    }
                                }
                            }else if("3".equals(claimType)){
                                List<PrpLPadPayMainVo> listPrpLPadPayMainVo = compensateService.findPadPayMainByRegistNo(registNo);
                                if(listPrpLPadPayMainVo != null && listPrpLPadPayMainVo.size()>0){
                                    for (PrpLPadPayMainVo prpLPadPayMainVo : listPrpLPadPayMainVo) {
                                        //???????????????????????????????????????payeeID
                                        if(compensateNo.equals(prpLPadPayMainVo.getCompensateNo())){
                                            List<PrpLPadPayPersonVo> listPrpLPadPayPersonVo = prpLPadPayMainVo.getPrpLPadPayPersons();
                                            if(listPrpLPadPayPersonVo != null && listPrpLPadPayPersonVo.size()>0){
                                                for (PrpLPadPayPersonVo prpLPadPayPersonVo : listPrpLPadPayPersonVo) {
                                                    if(oldIdClmPaymentResult.equals(prpLPadPayPersonVo.getIdClmPaymentResult()) && oldpayeeid.equals(prpLPadPayPersonVo.getPayeeId())){
                                                        prpLPadPayPersonVo.setPayeeId(newpayeeid);
                                                        serialNo = prpLPadPayPersonVo.getSerialNo();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }else{
                                PrpLCompensateVo compensateVo = compensateService.findCompByPK(compensateNo);
                                List<PrpLPaymentVo> prpLPaymentVoList = compensateVo.getPrpLPayments();
                                if(prpLPaymentVoList != null && prpLPaymentVoList.size()>0){
                                    for (PrpLPaymentVo prpLPaymentVo : prpLPaymentVoList) {
                                        if(oldpayeeid.equals(prpLPaymentVo.getPayeeId())){
                                            prpLPaymentVo.setPayeeId(newpayeeid);
                                            serialNo = prpLPaymentVo.getSerialNo();
                                        }
                                    }
                                }
                            }
                            if(payCustomVo != null){
                                PrpLPayBankVo payBankVo = new PrpLPayBankVo();
                                payBankVo.setRegistNo(registNo);
                                payBankVo.setClaimNo(claimNo);
                                payBankVo.setCompensateNo(compensateNo);
                                //payBankVo.setChargeCode();
                                payBankVo.setPayObjectType(payCustomVo.getPayObjectType());
                                //payBankVo.setPayee();
                                payBankVo.setPayeeIDNumber(payCustomVo.getCertifyNo());
                                payBankVo.setPayeeMobile(payCustomVo.getPayeeMobile());
                                payBankVo.setBankName(payCustomVo.getBankName());
                                payBankVo.setBankOutlets(payCustomVo.getBankOutlets());
                                payBankVo.setBankType(payCustomVo.getBankType());
                                payBankVo.setAccountName(payCustomVo.getPayeeName());
                                payBankVo.setAccountNo(payCustomVo.getAccountNo());
                                payBankVo.setBankNo(payCustomVo.getBankNo());
                                payBankVo.setPriorityFlag("N");//N??????  Y??????
                                payBankVo.setFlag("0");
                                payBankVo.setCreateUser("AUTO");
                                payBankVo.setCreateTime(new Date());
                                payBankVo.setUpdateUser("AUTO");
                                payBankVo.setUpdateTime(new Date());
                                //payBankVo.setInsuredName();
                                payBankVo.setAppTime(new Date());
                                payBankVo.setPayeeId(newpayeeid);
                                payBankVo.setIsVerify("1");
                                payBankVo.setVerifyUser("AUTO");
                                payBankVo.setVerifyDate(new Date());
                                payBankVo.setVerifyStatus("3");
                                payBankVo.setVerifyText("????????????????????????");
                                payBankVo.setVerifyHandle("1");
                                payBankVo.setPayObjectType(payCustomVo.getPayObjectType());
                                payBankVo.setPayObjectKind(payCustomVo.getPayObjectKind());
                                payBankVo.setProvinceCode(payCustomVo.getProvinceCode());
                                payBankVo.setProvince(payCustomVo.getProvince());
                                payBankVo.setCity(payCustomVo.getCity());
                                payBankVo.setCityCode(payCustomVo.getCityCode());
                                payBankVo.setPublicAndPrivate(payCustomVo.getPublicAndPrivate());
                                //payBankVo.setPayType(payCustomVo.getPayRefReason());?????????????????????????????????????????????
                                payBankVo.setSummary(payCustomVo.getSummary());
                                payBankVo.setCertifyType(payCustomVo.getCertifyType());
                                //??????????????????
                                accountInfoService.saveAccRbackAccountPingAn(payBankVo);
                            }
                            //
                            ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
                            logVo.setRegistNo(registNo);
                            logVo.setClaimNo(claimNo);
                            logVo.setCompensateNo(compensateNo);//????????????
                            // ?????????????????????
                            logVo.setStatus("0");
                            logVo.setErrorCode("false");
                            logVo.setErrorMessage("?????????????????????????????????????????????");
                            // ?????????????????????id????????????????????????????????????????????????????????????
                            logVo.setCreateTime(new Date());
                            logVo.setBusinessType(BusinessType.ModAccount.name());
                            logVo.setBusinessName(BusinessType.ModAccount.getName());
                            logVo.setRequestUrl("");
                            logVo.setRequestTime(new Date());
                            logVo.setRequestXml("");
                            logVo.setRemark(newpayeeid+"_"+serialNo);
                            logVo.setCreateUser("");
                            logVo.setComCode("");
                            logVo = logService.save(logVo);
                        }
                    } else {
                        // ?????????
                        try {
                            //????????????
                            logger.info("????????????claimType???????????????????????????claimType???{}",claimType);
                            if(StringUtil.isNotBlank(claimType)){
                                //??????
                                logger.info("??????????????????????????????????????????claimType???{}",claimType);
                                if("2".equals(claimType)){
                                    boolean flag = true;//??????????????????????????????????????????
                                    List<PrpLCompensateVo> listCompensateVo=compensateService.findCompensatevosByRegistNo(registNo);
                                    if(listCompensateVo != null && listCompensateVo.size()>0){
                                        for (PrpLCompensateVo compensateVoYP : listCompensateVo) {
                                            if(compensateVoYP.getCompensateType() != null && "Y".equals(compensateVoYP.getCompensateType())){
                                                //???????????????????????????????????????payeeId,?????????????????????????????????
                                                String policyNo = paymentResultDto.getPolicyNo();
                                                String riskCode = "";
                                                if(StringUtil.isNotBlank(policyNo) && !policyNo.startsWith("B")){
                                                    policyNo = "B"+policyNo;
                                                    PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(registNo,policyNo);
                                                    riskCode = cMainVo.getRiskCode();
                                                }
                                                List<PrpLPrePay> listPrpLPrePay = compensateService.findPrePayByRegistNoAndCaseTimes(compensateVoYP.getCompensateNo(),paymentResultDto.getCaseTimes());
                                                List<PrpLPrePayVo> prePayVos = new ArrayList<>();
                                                //????????????????????????????????????????????????????????????????????????
                                                if(listPrpLPrePay != null && listPrpLPrePay.size()>0){
                                                    for (PrpLPrePay prplPrePay : listPrpLPrePay) {
                                                        if(riskCode.equals(prplPrePay.getRiskCode())){
                                                            PrpLPrePayVo prePayVo = new PrpLPrePayVo();
                                                            prplPrePay.setPayeeId(payeeid);
                                                            prplPrePay.setIdClmPaymentResult(paymentResultDto.getIdClmPaymentResult());
                                                            Beans.copy().from(prplPrePay).to(prePayVo);
                                                            prePayVos.add(prePayVo);
                                                        }
                                                    }
                                                }
                                                if(prePayVos != null && prePayVos.size()>0){
                                                    flag = false;
                                                    logger.info("?????????????????????????????????????????????????????????????????????" + compensateVoYP.getCompensateNo());
                                                    interfaceAsyncService.prePayToNewPaymentPingAn(compensateVoYP,registNo);//???????????????
                                                    logger.info("?????????????????????????????????????????????????????????????????????" + compensateVoYP.getCompensateNo());
                                                }
                                            }
                                        }
                                        if(flag){
                                            logger.info("prePayVos??????????????????????????????{}",registNo);
                                            throw new IllegalArgumentException("???????????????????????????????????????????????????????????????????????????");
                                        }
                                    }else{
                                        throw new IllegalArgumentException("???????????????????????????????????????????????????????????????????????????");
                                    }

                                }else if("3".equals(claimType)){//??????
                                    //????????????????????????????????????payeeId,?????????????????????????????????
                                    if(paymentResultDto.getCaseTimes() != null ){
                                        List<PrpLPadPayMain> listPrpLPadPayMain = compensateService.findPadPayMainByRegistNoPA(registNo);
                                        //????????????????????????????????????
                                        if(listPrpLPadPayMain != null && listPrpLPadPayMain.size()>0){
                                            for (PrpLPadPayMain prpLPadPayMain : listPrpLPadPayMain) {
                                                //?????????????????????????????????????????????????????????????????????
                                                if(prpLPadPayMain.getCaseTimes()!=null && prpLPadPayMain.getCaseTimes()==paymentResultDto.getCaseTimes()){
                                                    List<PrpLPadPayPerson> listPrpLPadPayPerson = prpLPadPayMain.getPrpLPadPayPersons();
                                                    if(listPrpLPadPayPerson != null && listPrpLPadPayPerson.size()>0){
                                                        for (PrpLPadPayPerson prpLPadPayPerson : listPrpLPadPayPerson) {
                                                            prpLPadPayPerson.setPayeeId(payeeid);//??????payeeID
                                                            prpLPadPayPerson.setIdClmPaymentResult(paymentResultDto.getIdClmPaymentResult());//????????????????????????
                                                        }
                                                    }

                                                    PrpLPadPayMainVo prpLPadPayMainVo = Beans.copyDepth().from(prpLPadPayMain).to(PrpLPadPayMainVo.class);
                                                    logger.info("?????????????????????????????????????????????????????????????????????" + prpLPadPayMainVo.getCompensateNo());
                                                    interfaceAsyncService.padPayToNewPaymentPingAn(prpLPadPayMainVo,registNo);//???????????????
                                                    logger.info("?????????????????????????????????????????????????????????????????????" + prpLPadPayMainVo.getCompensateNo());
                                                }
                                            }
                                        }else{
                                            throw new IllegalArgumentException("???????????????????????????????????????????????????????????????????????????");
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.info("??????/??????????????????VAT?????????", e);
                            resultBean.fail("??????????????????-?????????????????????????????????????????????" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                logger.info("??????????????????-?????????????????????????????????????????????");
                resultBean.setSuccess(false);
                throw new IllegalArgumentException("??????????????????-?????????????????????????????????????????????");
            }
        } catch (Exception e) {
            logger.info("??????????????????-?????????????????????????????????????????????", e);
            resultBean.fail("??????????????????-?????????????????????????????????????????????" + e.getMessage());
            e.printStackTrace();
        }
        return resultBean;
    }

    /**
     * ???????????????????????????????????????id
     * @param registNo ?????????
     * @param accountNo ???????????????
     * @return ???????????????
     */
    public Long getPaycustomIdWithSameAccountNo(String registNo, String accountNo) {
        QueryRule queryrule = QueryRule.getInstance();
        queryrule.addEqual("registNo", registNo);
        queryrule.addEqual("accountNo", accountNo);
        queryrule.addEqual("validFlag", "1");

        List<PrpLPayCustom> payCusPoList = databaseDao.findAll(PrpLPayCustom.class, queryrule);
        if (payCusPoList != null && payCusPoList.size() > 0) {
            PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();
            Beans.copy().from(payCusPoList.get(0)).to(payCustomVo);
            return payCustomVo.getId();
        }

        return null;
    }

    /**
     * ??????????????????
     * @param oldCertifyType
     * @return
     */
    private String transCertifyType(String oldCertifyType){
        if(oldCertifyType != null && !"".equals(oldCertifyType)){
            String newCertifyType = "99";
            switch (oldCertifyType){
                case "1":
                    newCertifyType = "01";
                    break;
                case "7":
                    newCertifyType = "03";
                    break;
                case "41":
                    newCertifyType = "99";
                    break;
                case "3":
                    newCertifyType = "05";
                    break;
                case "43":
                    newCertifyType = "06";
                    break;
                case "553":
                    newCertifyType = "553";
                    break;
                case "550":
                    newCertifyType = "550";
                    break;
                case "5":
                    newCertifyType = "09";
                    break;
                default:
                    newCertifyType = "99";
                    break;
            }
            return newCertifyType;
        }else {
            return oldCertifyType;
        }
    }
}
