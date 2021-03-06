package ins.sino.claimcar.newpayment.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.common.util.ConfigUtil;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.*;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.po.PrpLCompensate;
import ins.sino.claimcar.claim.po.PrpLPayment;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.ReOpenCaseService;
import ins.sino.claimcar.newpayment.vo.*;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.*;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.*;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.service.MsgModelService;
import ins.sino.claimcar.other.service.SendMsgService;
import ins.sino.claimcar.other.vo.*;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.pay.service.RecPayService;
import ins.sino.claimcar.recloss.service.RecLossService;
import ins.sino.claimcar.recloss.vo.PrpLRecLossDetailVo;
import ins.sino.claimcar.recloss.vo.PrpLRecLossVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.service.PlatLockService;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLRecoveryOrPayVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ????????????????????????????????????
 *
 * @author maofengning
 * @date 2020/5/6 18:45
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("claimToNewPaymentService")
public class ClaimToNewPaymentServiceImpl implements ClaimToNewPaymentService {

    private static Logger logger = LoggerFactory.getLogger(ClaimToNewPaymentServiceImpl.class);

    @Autowired
    private ManagerService managerService;
    @Autowired
    private CheckTaskService checkTaskService;
    @Autowired
    private PlatLockService platLockService;
    @Autowired
    ClaimTaskService claimService;
    @Autowired
    private PolicyViewService policyViewService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RegistQueryService registQueryService;
    @Autowired
    SendMsgService sendMsgService;
    @Autowired
    MsgModelService msgModelService;
    @Autowired
    private EndCasePubService endCasePubService;
    @Autowired
    private CodeTranService codeTranService;
    @Autowired
    private CompensateService compensateService;
    @Autowired
    AreaDictService areaDictService;
    @Autowired
    private AcheckService acheckService;
    @Autowired
    private SendToNewPaymentService sendToNewPaymentService;
    @Autowired
    private ClaimTaskService claimTaskService;
    @Autowired
    PadPayPubService padPayPubService;
    @Autowired
    ReOpenCaseService reOpenCaseService;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    RecPayService recPayService;
    @Autowired
    private RecLossService recLossService;
    @Autowired
    PadPayPubService padPayService;
    @Autowired
    private AssessorService assessorService;
    @Autowired
    private ClaimInterfaceLogService logService;
	@Autowired
	private CompensateTaskService compensateTaskService;
    /**
     * ???????????????
     *
     * @param compensateVo ?????????????????????????????????????????????
     * @throws Exception ?????????????????????
     */
    @Override
    public void compensateToNewPayment(PrpLCompensateVo compensateVo) throws Exception {
        if (compensateVo != null) {
        	// ???????????????????????????
			String hasPrepay = getYuPayFlagByClaim(compensateVo.getClaimNo());
            // ????????????????????? ???0 ????????????
            if (BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumPaidAmt())) != 0
					|| BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumPaidFee())) != 0 || "1".equals(hasPrepay)) {
				// ??????????????????
				Claim2NewPaymentDto claim2NewPaymentDto = packagePaymentInfos(compensateVo);
				if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null
						&& claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
					Gson gson = new Gson();
					String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/dataReception";
					String paymentJson = gson.toJson(claim2NewPaymentDto);
					logger.info("??????????????????????????????????????????" + paymentJson);
					ResponseDto responseDto;
					// ?????????????????????
					ClaimInterfaceLogVo logVo = null;
					try {
						logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_compe, newPaymentUrl,
								compensateVo.getCompensateNo(), compensateVo.getRegistNo());
					} catch (Exception e) {
						logger.info("??????????????????" + compensateVo.getCompensateNo() + " ??????????????????????????????????????????", e);
					}
					PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.PINGANTOPALTFORMFLAG);
					//???????????????????????????
					if(this.checkPingAnCase(compensateVo.getRegistNo()) && configValueVo != null && configValueVo.getConfigValue() != null && "0".equals(configValueVo.getConfigValue())){
						//??????
						logVo.setRegistNo(compensateVo.getRegistNo());
						logVo.setResponseXml("");    // ????????????
						logVo.setErrorMessage("??????????????????????????????");
						logVo.setErrorCode(false+"");
						logVo.setStatus("0");
						logVo.setCompensateNo(compensateVo.getCompensateNo());
						logService.save(logVo);
					}else{
						try {
							// ???????????????????????????
							responseDto = sendToNewPaymentService.callPaymentForClient(paymentJson,
									BusinessType.Payment_compe, compensateVo.getCompensateNo());
							logger.info("??????????????????{} ??????????????????????????????", compensateVo.getCompensateNo());
						} catch (Exception e) {
							logger.info("??????????????????" + compensateVo.getCompensateNo() + " ??????????????????????????????", e);
							if (logVo != null) {
								logVo.setRegistNo(compensateVo.getRegistNo());
								logVo.setStatus("0");
								logVo.setErrorCode(PaymentConstants.RESP_FAILED);
								logVo.setErrorMessage(e.getMessage());
								logService.save(logVo);
							}
							throw e;
						}
						// ????????????????????????????????????????????????
						String logStatus = "0";
						if (PaymentConstants.RESP_SUCCESS.equals(responseDto.getResponseCode())) {
							updateClaimStatus(compensateVo,responseDto.getPayRefNos());
							logStatus = "1";
						}
						if (logVo != null) {
							logVo.setRegistNo(compensateVo.getRegistNo());
							logVo.setStatus(logStatus);
							logVo.setErrorCode(responseDto.getResponseCode());
							logVo.setErrorMessage(responseDto.getErrorMessage());
							logService.save(logVo);
						}
					}
				}
			}
        } else {
            logger.info("??????????????????????????????????????????????????????");
        }

    }
	/**
	 * ???????????????
	 *
	 * @param compensateVo ?????????????????????????????????????????????
	 * @throws Exception ?????????????????????
	 */
	@Override
	public void compensateToNewPaymentPingAn(PrpLCompensateVo compensateVo,String registNo) throws Exception {
		if (compensateVo != null) {
			// ???????????????????????????
			String hasPrepay = getYuPayFlagByClaim(compensateVo.getClaimNo());
			// ????????????????????? ???0 ????????????
			if (BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumPaidAmt())) != 0
					|| BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumPaidFee())) != 0 || "1".equals(hasPrepay)) {
				// ??????????????????
				Claim2NewPaymentDto claim2NewPaymentDto = packagePaymentInfos(compensateVo);
				if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null
						&& claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
					String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/dataReception";
					String paymentJson = "";
					// ?????????????????????
					ClaimInterfaceLogVo logVo = null;
					try {
						logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_compe, newPaymentUrl,
								compensateVo.getCompensateNo(), registNo);
					} catch (Exception e) {
						logger.info("??????????????????" + compensateVo.getCompensateNo() + " ??????????????????????????????????????????", e);
					}
					//??????
					logVo.setClaimNo(compensateVo.getClaimNo());
					logVo.setCreateUser(compensateVo.getCreateUser());
					logVo.setComCode(compensateVo.getComCode());
					logVo.setRegistNo(registNo);
					logVo.setResponseXml("");    // ????????????
					logVo.setErrorMessage("??????????????????????????????????????????????????????????????????");
					logVo.setErrorCode(false+"");
					logVo.setStatus("0");
					logVo.setCompensateNo(compensateVo.getCompensateNo());
					logService.save(logVo);
				}
			}
		} else {
			logger.info("??????????????????????????????????????????????????????");
		}

	}
    /**
     * ???????????????
     *
     * @param compensateVo ???????????????
     * @throws Exception ????????????
     */
    @Override
    public void prePayToNewPayment(PrpLCompensateVo compensateVo) throws Exception {
		logger.info("????????????????????????????????????" + compensateVo.getCompensateNo());
        if (compensateVo != null) {
                // ????????????????????? ???0 ????????????
			logger.info("??????????????????????????????????????????????????????" + compensateVo.getCompensateNo());
			if (BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumAmt())) != 0) {
				logger.info("????????????????????????????????????" + compensateVo.getCompensateNo());
				// ??????????????????
				Claim2NewPaymentDto claim2NewPaymentDto = packagePrePayInfos(compensateVo);
				if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null
						&& claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
					Gson gson = new Gson();
					String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/dataReception";
					String paymentJson = gson.toJson(claim2NewPaymentDto);
					logger.info("???????????????????????????" + paymentJson);
					// ?????????????????????
					ClaimInterfaceLogVo logVo = null;
					try {
						logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_prePay, newPaymentUrl,
								compensateVo.getCompensateNo(), compensateVo.getRegistNo());
					} catch (Exception e) {
						logger.info("??????????????????" + compensateVo.getCompensateNo() + " ??????????????????????????????????????????", e);
					}
					ResponseDto responseDto;
					PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.PINGANTOPALTFORMFLAG);
					//???????????????????????????
					if(this.checkPingAnCase(compensateVo.getRegistNo()) && configValueVo != null && configValueVo.getConfigValue() != null && "0".equals(configValueVo.getConfigValue())){
						//??????
						logVo.setRegistNo(compensateVo.getRegistNo());
						logVo.setResponseXml("");    // ????????????
						logVo.setErrorMessage("??????????????????????????????");
						logVo.setErrorCode(false+"");
						logVo.setStatus("0");
						logVo.setCompensateNo(compensateVo.getCompensateNo());
						logService.save(logVo);
					}else{
						try {
							// ???????????????????????????
							responseDto = sendToNewPaymentService.callPaymentForClient(paymentJson,
									BusinessType.Payment_prePay, compensateVo.getCompensateNo());
							logger.info("??????????????????{} ??????????????????????????????????????????errorMessage??????{}", compensateVo.getCompensateNo(),
									responseDto.getErrorMessage());
						} catch (Exception e) {
							logger.info("??????????????????" + compensateVo.getCompensateNo() + " ????????????????????????", e);
							if (logVo != null) {
								logVo.setStatus("0");
								logVo.setErrorCode(PaymentConstants.RESP_FAILED);
								logVo.setErrorMessage(e.getMessage());
								logService.save(logVo);
							}
							throw e;
						}

						// ????????????????????????????????????????????????
						String logStatus = "0";
						if (PaymentConstants.RESP_SUCCESS.equals(responseDto.getResponseCode())) {
							updatePrePayStatus(compensateVo,responseDto.getPayRefNos());
							logStatus = "1";
						}
						if (logVo != null) {
							logVo.setRegistNo(compensateVo.getRegistNo());
							logVo.setStatus(logStatus);
							logVo.setErrorCode(responseDto.getResponseCode());
							logVo.setErrorMessage(responseDto.getErrorMessage());
							logService.save(logVo);
						}
					}
				}
			}
        } else {
            logger.info("????????????????????????????????????????????????");
        }
    }
	/**
	 * ?????????????????????????????????????????????
	 *
	 * @param compensateVo ???????????????
	 * @throws Exception ????????????
	 */
	@Override
	public void prePayToNewPaymentPingAn(PrpLCompensateVo compensateVo,String registNo) throws Exception {
		logger.info("????????????????????????????????????" + compensateVo.getCompensateNo());
		if (compensateVo != null) {
			// ????????????????????? ???0 ????????????
			logger.info("??????????????????????????????????????????????????????" + compensateVo.getCompensateNo());
			if (BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumAmt())) != 0) {
				logger.info("????????????????????????????????????" + compensateVo.getCompensateNo());
				// ??????????????????
				String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/dataReception";
				String paymentJson = "";
				// ?????????????????????
				ClaimInterfaceLogVo logVo = null;
				try {
					logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_prePay, newPaymentUrl,
							compensateVo.getCompensateNo(), registNo);
				} catch (Exception e) {
					logger.info("??????????????????" + compensateVo.getCompensateNo() + " ??????????????????????????????????????????", e);
				}
				//??????
				logVo.setRegistNo(registNo);
				logVo.setResponseXml("");    // ????????????
				logVo.setErrorMessage("?????????????????????????????????????????????????????????????????????");
				logVo.setErrorCode(false+"");
				logVo.setStatus("0");
				logVo.setCompensateNo(compensateVo.getCompensateNo());
				logService.save(logVo);
			}
		} else {
			logger.info("????????????????????????????????????????????????");
		}
	}
    /**
     * ???????????????
     *
     * @param padPayMainVo ???????????????
     * @throws Exception ????????????
     */
    @Override
    public void padPayToNewPayment(PrpLPadPayMainVo padPayMainVo) throws Exception {

		if (padPayMainVo != null) {
			// ??????????????????
			Claim2NewPaymentDto claim2NewPaymentDto = packagePadPayInfos(padPayMainVo);
			if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null && claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
				Gson gson = new Gson();
				String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/dataReception";
				String paymentJson = gson.toJson(claim2NewPaymentDto);
				logger.info("???????????????????????????" + paymentJson);
				// ?????????????????????
				ClaimInterfaceLogVo logVo = null;
				try {
					logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_padPay, newPaymentUrl,
							padPayMainVo.getCompensateNo(), padPayMainVo.getRegistNo());
				} catch (Exception e) {
					logger.info("??????????????????" + padPayMainVo.getCompensateNo() + " ??????????????????????????????????????????", e);
				}
				ResponseDto responseDto;
				PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.PINGANTOPALTFORMFLAG);
				//???????????????????????????
				if(this.checkPingAnCase(padPayMainVo.getRegistNo()) && configValueVo != null && configValueVo.getConfigValue() != null && "0".equals(configValueVo.getConfigValue())){
					//??????
					logVo.setResponseXml("");    // ????????????
					logVo.setErrorMessage("??????????????????????????????");
					logVo.setErrorCode(false+"");
					logVo.setStatus("0");
					logVo.setCompensateNo(padPayMainVo.getCompensateNo());
					logService.save(logVo);
				}else{
					try {
						// ???????????????????????????
						responseDto = sendToNewPaymentService.callPaymentForClient(paymentJson, BusinessType.Payment_padPay,
								padPayMainVo.getCompensateNo());
						logger.info("??????????????????{} ????????????????????????", padPayMainVo.getCompensateNo());
					} catch (Exception e) {
						logger.info("??????????????????" + padPayMainVo.getCompensateNo() + " ????????????????????????", e);
						if (logVo != null) {
							logVo.setStatus("0");
							logVo.setErrorCode(PaymentConstants.RESP_FAILED);
							logVo.setErrorMessage(e.getMessage());
							logService.save(logVo);
						}
						throw e;
					}
					// ????????????????????????????????????????????????
					String logStatus = "0";
					if (PaymentConstants.RESP_SUCCESS.equals(responseDto.getResponseCode())) {
						updatePadPayStatus(padPayMainVo,responseDto.getPayRefNos());
						logStatus = "1";
					}
					if (logVo != null) {
						logVo.setStatus(logStatus);
						logVo.setErrorCode(responseDto.getResponseCode());
						logVo.setErrorMessage(responseDto.getErrorMessage());
						logService.save(logVo);
					}
				}
			}
		} else {
            logger.info("????????????????????????????????????????????????");
        }
    }


	/**
	 * ?????????????????????????????????????????????
	 *
	 * @param padPayMainVo ???????????????
	 * @throws Exception ????????????
	 */
	@Override
	public void padPayToNewPaymentPingAn(PrpLPadPayMainVo padPayMainVo,String registNo) throws Exception {
		if (padPayMainVo != null) {
			String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/dataReception";
			String paymentJson = "";
			// ?????????????????????
			ClaimInterfaceLogVo logVo = null;
			try {
				logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_padPay, newPaymentUrl,
						padPayMainVo.getCompensateNo(), registNo);
			} catch (Exception e) {
				logger.info("??????????????????" + padPayMainVo.getCompensateNo() + " ??????????????????????????????????????????", e);
			}
			//??????
			logVo.setRegistNo(registNo);
			logVo.setResponseXml("");    // ????????????
			logVo.setErrorMessage("?????????????????????????????????????????????????????????????????????");
			logVo.setErrorCode(false+"");
			logVo.setStatus("0");
			logVo.setCompensateNo(padPayMainVo.getCompensateNo());
			logService.save(logVo);
		} else {
			logger.info("????????????????????????????????????????????????");
		}
	}
    /**
     * ???????????????
     *
     * @param prplReplevyMainVo ????????????
     * @throws Exception ???????????????????????????
     */
    @Override
    public void recPayToNewPayment(PrplReplevyMainVo prplReplevyMainVo) throws Exception {
        Claim2NewPaymentDto claim2NewPaymentDto = packageRelevyDatas(prplReplevyMainVo);
        if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null && claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
            Gson gson = new Gson();
            String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL")+"/dataReception";
            String paymentJson = gson.toJson(claim2NewPaymentDto);
            logger.info("????????????????????????????????????" + paymentJson);
            // ?????????????????????
            ClaimInterfaceLogVo logVo = null;
            try {
            	logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_recPay, newPaymentUrl, prplReplevyMainVo.getCompensateNo(), prplReplevyMainVo.getRegistNo());
            } catch (Exception e) {
                logger.info("??????????????????" + prplReplevyMainVo.getCompensateNo() + " ??????????????????????????????????????????", e);
            }
            ResponseDto responseDto; 
            try {
                // ???????????????????????????
            	responseDto = sendToNewPaymentService.callPaymentForClient(paymentJson, BusinessType.Payment_recPay, prplReplevyMainVo.getCompensateNo());
                logger.info("??????????????????{} ??????????????????????????????????????????errroMessage??????{}", prplReplevyMainVo.getCompensateNo(), responseDto.getErrorMessage());
            } catch (Exception e) {
            	logger.info("??????????????????" + prplReplevyMainVo.getCompensateNo() + " ????????????????????????", e);
            	if (logVo != null) {
            		logVo.setStatus("0");
                    logVo.setErrorCode(PaymentConstants.RESP_FAILED);
                    logVo.setErrorMessage(e.getMessage());
                    logService.save(logVo);
            	}
                throw e;
            }
            // ????????????????????????????????????????????????
			String logStatus = "0";
			if (PaymentConstants.RESP_SUCCESS.equals(responseDto.getResponseCode())) {
				logStatus = "1";
			}
            if (logVo != null) {
	            logVo.setStatus(logStatus);
	            logVo.setErrorCode(responseDto.getResponseCode());
	            logVo.setErrorMessage(responseDto.getErrorMessage());
	            logService.save(logVo);
            }
        }
    }

    /**
     * ?????????????????????
     *
     * @param recLossVo ????????????
     * @throws Exception ???????????????????????????
     */
    @Override
    public void recLossToNewPayment(PrpLRecLossVo recLossVo) throws Exception {
        Claim2NewPaymentDto claim2NewPaymentDto = packageRecLossDatas(recLossVo);
        if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null && claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
            Gson gson = new Gson();
            String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL")+"/dataReception";
            String paymentJson = gson.toJson(claim2NewPaymentDto);
            logger.info("??????????????????????????????????????????" + paymentJson);
            // ?????????????????????
            ClaimInterfaceLogVo logVo = null;
            try {
            	logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_recLoss, newPaymentUrl, recLossVo.getPrpLRecLossId(), recLossVo.getRegistNo());
            } catch (Exception e) {
                logger.info("????????????????????????" + recLossVo.getPrpLRecLossId() + " ????????????????????????????????????????????????", e);
            }
            ResponseDto responseDto;
            try {
                // ???????????????????????????
            	responseDto = sendToNewPaymentService.callPaymentForClient(paymentJson, BusinessType.Payment_recLoss, recLossVo.getPrpLRecLossId());
                logger.info("????????????????????????{} ????????????????????????????????????????????????errroMessage??????{}", recLossVo.getPrpLRecLossId(), responseDto.getErrorMessage());
            } catch (Exception e) {
            	logger.info("????????????????????????" + recLossVo.getPrpLRecLossId() + " ??????????????????????????????", e);
            	if (logVo != null) {
            		logVo.setStatus("0");
                    logVo.setErrorCode(PaymentConstants.RESP_FAILED);
                    logVo.setErrorMessage(e.getMessage());
                    logService.save(logVo);
            	}
                throw e;
            }
			// ????????????????????????????????????????????????
			String logStatus = "0";
			if (PaymentConstants.RESP_SUCCESS.equals(responseDto.getResponseCode())) {
				logStatus = "1";
			}
            if (logVo != null) {
	            logVo.setStatus(logStatus);
	            logVo.setErrorCode(responseDto.getResponseCode());
	            logVo.setErrorMessage(responseDto.getErrorMessage());
	            logService.save(logVo);
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param assessorMainVo ?????????????????????
     * @throws Exception ????????????????????????
     */
    @Override
    public void assessorToNewPayment(PrpLAssessorMainVo assessorMainVo, PrpLAssessorFeeVo assessorFeeVo) throws Exception {
        Claim2NewPaymentDto claim2NewPaymentDto = packageAssessorData(assessorMainVo, assessorFeeVo);
        if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null && claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
            Gson gson = new Gson();
            String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL")+"/dataReception";
            String paymentJson = gson.toJson(claim2NewPaymentDto);
            logger.info("???????????????????????????????????????" + paymentJson);
            // ?????????????????????
            ClaimInterfaceLogVo logVo = null;
            try {
            	logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_assessor, newPaymentUrl, assessorFeeVo.getCompensateNo(), assessorFeeVo.getRegistNo());
            } catch (Exception e) {
                logger.info("?????????????????????" + assessorFeeVo.getCompensateNo() + " ?????????????????????????????????????????????", e);
            }
            ResponseDto responseDto;
            try {
                // ???????????????????????????
            	responseDto = sendToNewPaymentService.callPaymentForClient(paymentJson, BusinessType.Payment_assessor, assessorFeeVo.getCompensateNo());
                logger.info("?????????????????????{} ?????????????????????????????????????????????errorMessage??????{}", assessorFeeVo.getCompensateNo(), responseDto.getErrorMessage());
            } catch (Exception e) {
            	logger.info("?????????????????????" + assessorFeeVo.getCompensateNo() + " ???????????????????????????", e);
            	if (logVo != null) {
            		logVo.setStatus("0");
                    logVo.setErrorCode(PaymentConstants.RESP_FAILED);
                    logVo.setErrorMessage(e.getMessage());
                    logService.save(logVo);
            	}
                throw e;
            }
			// ????????????????????????????????????????????????
			String logStatus = "0";
			if (PaymentConstants.RESP_SUCCESS.equals(responseDto.getResponseCode())) {
				logStatus = "1";
			}
            if (logVo != null) {
	            logVo.setStatus(logStatus);
	            logVo.setErrorCode(responseDto.getResponseCode());
	            logVo.setErrorMessage(responseDto.getErrorMessage());
	            logService.save(logVo);
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param checkMainVo ?????????????????????
     * @param checkFeeVo  ???????????????
     * @throws Exception ????????????
     */
    @Override
    public void checkFeeToNewPayment(PrpLAcheckMainVo checkMainVo, PrpLCheckFeeVo checkFeeVo) throws Exception {
		Claim2NewPaymentDto claim2NewPaymentDto = packageCheckFeeData(checkMainVo, checkFeeVo);
		if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null && claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
			Gson gson = new Gson();
			String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/dataReception";
			String paymentJson = gson.toJson(claim2NewPaymentDto);
			logger.info("???????????????????????????????????????" + paymentJson);
			// ?????????????????????
			ClaimInterfaceLogVo logVo = null;
			try {
				logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_checkFee, newPaymentUrl,
						checkFeeVo.getCompensateNo(), checkFeeVo.getRegistNo());
			} catch (Exception e) {
				logger.info("?????????????????????" + checkFeeVo.getCompensateNo() + " ?????????????????????????????????????????????", e);
			}
			ResponseDto responseDto = new ResponseDto();
			try {
				// ???????????????????????????
				responseDto = sendToNewPaymentService.callPaymentForClient(paymentJson, BusinessType.Payment_checkFee,
						checkFeeVo.getCompensateNo());
				logger.info("?????????????????????{} ?????????????????????????????????????????????errorMessage??????{}", checkFeeVo.getCompensateNo(), responseDto.getErrorMessage());
			} catch (Exception e) {
				logger.info("?????????????????????" + checkFeeVo.getCompensateNo() + " ???????????????????????????", e);
				if (logVo != null) {
					logVo.setStatus("0");
					logVo.setErrorCode(PaymentConstants.RESP_FAILED);
					logVo.setErrorMessage(e.getMessage());
					logService.save(logVo);
				}
				throw e;
			}
			// ????????????????????????????????????????????????
			String logStatus = "0";
			if (PaymentConstants.RESP_SUCCESS.equals(responseDto.getResponseCode())) {
				logStatus = "1";
			}
			if (logVo != null) {
				logVo.setStatus(logStatus);
				logVo.setErrorCode(responseDto.getResponseCode());
				logVo.setErrorMessage(responseDto.getErrorMessage());
				logService.save(logVo);
			}
		}
        
    }

    /**
     * ???????????????????????????
     *
     * @param compensateVo ???????????????
     * @return ???????????????????????????
     * @throws Exception ????????????
     */
    private Claim2NewPaymentDto packagePrePayInfos(PrpLCompensateVo compensateVo) throws Exception {
        Claim2NewPaymentDto claim2NewPaymentDto = new Claim2NewPaymentDto();
        List<PrpJlossPlanDto> jlossPlanDtos = new ArrayList<PrpJlossPlanDto>();
        // ?????????????????????????????????
        packagePrePayData(compensateVo, jlossPlanDtos);
        claim2NewPaymentDto.setPrpJlossPlanDtos(jlossPlanDtos);

        return claim2NewPaymentDto;
    }

    /**
     * ????????????????????????
     *
     * @param compensateVo  ???????????????
     * @param jlossPlanDtos ??????????????????
     */
    private void packagePrePayData(PrpLCompensateVo compensateVo, List<PrpJlossPlanDto> jlossPlanDtos) throws Exception {
        if (compensateVo != null) {
            // 1. ????????????????????????????????????0???????????????
            List<PrpLPrePayVo> prePayVos = compensateService.getPrePayVo(compensateVo.getCompensateNo(), null);
            boolean nonZero = isPrePayAndFeeNonZero(prePayVos);
            if (nonZero) {
                PrpLClaimVo prplClaimVo = claimService.findClaimVoByClaimNo(compensateVo.getClaimNo());
                PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(compensateVo.getRegistNo(), compensateVo.getPolicyNo());
                PrpLRegistVo registVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());

                // 2. ????????????????????????
                for (PrpLPrePayVo prePayVo : prePayVos) {
                    // ??????????????????0????????????
                    if (prePayVo.getPayAmt() != null && BigDecimal.ZERO.compareTo(prePayVo.getPayAmt()) != 0) {
                        PrpJlossPlanDto prpJlossPlanDto = new PrpJlossPlanDto();
                        prpJlossPlanDto.setPayeeId(prePayVo.getPayeeId() == null ? "" : prePayVo.getPayeeId().toString());
                        prpJlossPlanDto.setCertiType(PaymentConstants.CERTITYPE_Y);
                        prpJlossPlanDto.setCertiNo(compensateVo.getCompensateNo());
                        prpJlossPlanDto.setPolicyNo(compensateVo.getPolicyNo());
                        prpJlossPlanDto.setRegistNo(compensateVo.getRegistNo());
                        prpJlossPlanDto.setSerialNo(Integer.parseInt(prePayVo.getSerialNo()));
                        prpJlossPlanDto.setClaimNo(compensateVo.getClaimNo());
						String isSimpleCase = compensateVo.getPrpLCompensateExt() == null ? "0" : compensateVo.getPrpLCompensateExt().getIsFastReparation();
                        if (PaymentConstants.FEETYPE_P.equals(prePayVo.getFeeType())) {
							prpJlossPlanDto.setLossType(PaymentConstants.LOSSTYPE_0);
							prpJlossPlanDto.setPayRefReason(PaymentConstants.PAYREASON_P50);
							prpJlossPlanDto.setIsAutoPay(isSimpleCase == null ? "0" : isSimpleCase);
							prpJlossPlanDto.setIsSimpleCase(isSimpleCase == null ? "0" : isSimpleCase);
                        } else {
							prpJlossPlanDto.setLossType(PaymentConstants.LOSSTYPE_1);
                            SysCodeDictVo dictVo = codeTranService.findTransCodeDictVo("ChargeCode", prePayVo.getChargeCode());
                            prpJlossPlanDto.setPayRefReason(dictVo.getProperty1());
							prpJlossPlanDto.setIsAutoPay("0");
							prpJlossPlanDto.setIsSimpleCase("0");
                        }
                        prpJlossPlanDto.setClassCode(prplcMainVo == null ? "" : prplcMainVo.getClassCode());
                        prpJlossPlanDto.setRiskCode(compensateVo.getRiskCode());
                        prpJlossPlanDto.setAppliCode(prplcMainVo == null ? "" : prplcMainVo.getAppliCode());
                        prpJlossPlanDto.setAppliName(prplcMainVo == null ? "" : prplcMainVo.getAppliName());
                        prpJlossPlanDto.setInsuredCode(prplcMainVo == null ? "" : prplcMainVo.getInsuredCode());
                        prpJlossPlanDto.setInsuredName(prplcMainVo == null ? "" : prplcMainVo.getInsuredName());
                        prpJlossPlanDto.setStartDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getStartDate(), "yyyy-MM-dd"));
                        prpJlossPlanDto.setEndDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getEndDate(), "yyyy-MM-dd"));
                        prpJlossPlanDto.setUnderWriteDate(dateToString(compensateVo.getUnderwriteDate(), "yyyy-MM-dd"));
                        prpJlossPlanDto.setEndCaseDate(prplClaimVo == null ? "" : dateToString(prplClaimVo.getEndCaseTime(), "yyyy-MM-dd HH:mm:ss"));
                        prpJlossPlanDto.setCurrency(CodeConstants.Currency.CNY);
                        prpJlossPlanDto.setComCode(compensateVo.getComCode());
                        prpJlossPlanDto.setMakeCom(prplcMainVo == null ? registVo.getComCode() : prplcMainVo.getComCode());

                        prpJlossPlanDto.setHandler1Code(compensateVo.getCreateUser());
                        SysUserVo handler1 = sysUserService.findByUserCode(compensateVo.getCreateUser());
                        prpJlossPlanDto.setHandler1CodeName(handler1 == null ? "" : handler1.getUserName());
                        prpJlossPlanDto.setHandlerCode(compensateVo.getHandler1Code());
                        prpJlossPlanDto.setOperateCode(compensateVo.getHandler1Code());
                        prpJlossPlanDto.setOperateComCode(compensateVo.getComCode());
                        prpJlossPlanDto.setBusinessNature(prplcMainVo == null ? "" : prplcMainVo.getBusinessNature());
                        prpJlossPlanDto.setBusinessplate(prplcMainVo == null ? "" : prplcMainVo.getBusinessPlate());

                        if (prplcMainVo != null && prplcMainVo.getPrpCItemCars() != null) {
                            for (PrpLCItemCarVo itemCarVo : prplcMainVo.getPrpCItemCars()) {
                                if (compensateVo.getPolicyNo() != null && compensateVo.getPolicyNo().equals(itemCarVo.getPolicyNo())) {
                                    prpJlossPlanDto.setLicenseNo(itemCarVo.getLicenseNo());
                                    String carType = this.transCarType(itemCarVo);
                                    prpJlossPlanDto.setCarNatureCode(carType);
                                    prpJlossPlanDto.setUsenaturecode(itemCarVo.getUseNatureCode());
                                    break;
                                }
                            }
                        }

                        prpJlossPlanDto.setCoinsFlag(registVo == null ? "" : registVo.getIsGBFlag());
                        if (registVo != null) {
                            if (PaymentConstants.COINSFLAG_2.equals(registVo.getIsGBFlag())
                                    || PaymentConstants.COINSFLAG_4.equals(registVo.getIsGBFlag())) {
                                prpJlossPlanDto.setCoinsType(PaymentConstants.COINSTYPE_1);
                            }
                        }
                        prpJlossPlanDto.setPlanFee(prePayVo.getPayAmt());
                        prpJlossPlanDto.setSystemFlag(PaymentConstants.SYSTEMFLAG_CAR);
                        prpJlossPlanDto.setLocationFlag(prplcMainVo == null ? "D" : prplcMainVo.getNationFlag());
                        prpJlossPlanDto.setSplitRiskFlag(PaymentConstants.SPLITRISKFLAG_1);

                        // ????????????????????????
                        PrpLPayCustomVo custom = managerService.findPayCustomVoById(prePayVo.getPayeeId());
                        if (custom != null) {
                            prpJlossPlanDto.setPayObjectName(custom.getPayeeName());
                            prpJlossPlanDto.setBankCode(custom.getBankNo());
                            prpJlossPlanDto.setBankName(custom.getBankName());
                            prpJlossPlanDto.setBankType(custom.getBankType());
                            prpJlossPlanDto.setAccountCode(custom.getAccountNo());
                            prpJlossPlanDto.setAccountName(custom.getPayeeName());
                            prpJlossPlanDto.setAccountType(custom.getPublicAndPrivate());
                            prpJlossPlanDto.setCertificateType(custom.getCertifyType());
                            prpJlossPlanDto.setCertificateCode(custom.getCertifyNo());
                            prpJlossPlanDto.setPayeePhone(custom.getPayeeMobile());
                            prpJlossPlanDto.setAbstractContent(prePayVo.getSummary() == null ? "" : prePayVo.getSummary());
                            prpJlossPlanDto.setBankProvinceCode(custom.getProvinceCode() == null ? "" : custom.getProvinceCode().toString());
                            prpJlossPlanDto.setBankProvinceName(custom.getProvince());
                            prpJlossPlanDto.setBankCityCode(custom.getCityCode() == null ? "" : custom.getCityCode().toString());
                            prpJlossPlanDto.setBankCityName(custom.getCity());
                            prpJlossPlanDto.setUsage(StringUtils.isBlank(custom.getPurpose()) ? "???" : custom.getPurpose());
                            prpJlossPlanDto.setOtherRemark(custom.getRemark());
                            prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);
                        } else {
                            logger.info("????????????id???" + prePayVo.getPayeeId() + " ?????????????????????" + compensateVo.getCompensateNo() + " paycustom?????????????????????????????????");
                        }
                        prpJlossPlanDto.setPayeeCurrency("CNY");
                        prpJlossPlanDto.setMessageContent("???????????????");
                        prpJlossPlanDto.setPayReasonFlag(StringUtils.isBlank(prePayVo.getOtherFlag()) ? "0" : prePayVo.getOtherFlag());

                        prpJlossPlanDto.setIsExpress("0");
                        prpJlossPlanDto.setYuPayFlag("0");
						// ??????????????????
						PrpJlossPlanSubDto subDto = new PrpJlossPlanSubDto();
						subDto.setCertiType(PaymentConstants.CERTITYPE_Y);
						subDto.setCertiNo(compensateVo.getCompensateNo());
						subDto.setPolicyNo(compensateVo.getPolicyNo());
						subDto.setSerialNo(Integer.parseInt(prePayVo.getSerialNo()));
						subDto.setRiskCode(compensateVo.getRiskCode());
						if (PaymentConstants.FEETYPE_P.equals(prePayVo.getFeeType())) {
							subDto.setLossType(PaymentConstants.LOSSTYPE_0);
							subDto.setPayRefReason(PaymentConstants.PAYREASON_P50);
							subDto.setChargeCode(CodeConstants.VatFeeType.YPAY);
						} else {
							subDto.setChargeCode(CodeConstants.backvatFeeType(prePayVo.getChargeCode()));
							subDto.setLossType(PaymentConstants.LOSSTYPE_1);
							SysCodeDictVo dictVo = codeTranService.findTransCodeDictVo("ChargeCode", prePayVo.getChargeCode());
							subDto.setPayRefReason(dictVo == null ? "" : dictVo.getProperty1());
						}
						if (compensateVo.getRiskCode() != null && compensateVo.getRiskCode().length() > 1) {
							subDto.setClassCode(compensateVo.getRiskCode().substring(0, 2));
						}
						subDto.setKindCode(prePayVo.getKindCode());
						subDto.setPlanFee(prePayVo.getPayAmt());
						subDto.setIsTaxable("1");

						List<PrpJlossPlanSubDto> jlossPlanSubDtos = new ArrayList<PrpJlossPlanSubDto>();
						jlossPlanSubDtos.add(subDto);
                        prpJlossPlanDto.setPrpJlossPlanSubDtos(jlossPlanSubDtos);
                        jlossPlanDtos.add(prpJlossPlanDto);
                    }
                }
            }
        }
    }

    /**
     * ????????????????????????
     *
     * @param compensateVo ???????????????
     * @return ??????????????????
     * @throws Exception ????????????
     */
    private List<PrpJlossPlanSubDto> getPrePayDetails(PrpLCompensateVo compensateVo) throws Exception {

        List<PrpLPrePayVo> prePayVoList = compensateService.getPrePayVo(compensateVo.getCompensateNo(), null);
        List<PrpJlossPlanSubDto> subDtos = new ArrayList<PrpJlossPlanSubDto>();
        for (PrpLPrePayVo prePayVo : prePayVoList) {
            // ??????????????????
            PrpJlossPlanSubDto subDto = new PrpJlossPlanSubDto();
            subDto.setCertiType(PaymentConstants.CERTITYPE_Y);
            subDto.setCertiNo(compensateVo.getCompensateNo());
            subDto.setPolicyNo(compensateVo.getPolicyNo());
            subDto.setSerialNo(Integer.parseInt(prePayVo.getSerialNo()));
            subDto.setRiskCode(compensateVo.getRiskCode());
            if (PaymentConstants.FEETYPE_P.equals(prePayVo.getFeeType())) {
				subDto.setLossType(PaymentConstants.LOSSTYPE_0);
                subDto.setPayRefReason(PaymentConstants.PAYREASON_P50);
                subDto.setChargeCode(CodeConstants.VatFeeType.YPAY);
            } else {
				subDto.setChargeCode(CodeConstants.backvatFeeType(prePayVo.getChargeCode()));
				subDto.setLossType(PaymentConstants.LOSSTYPE_1);
                SysCodeDictVo dictVo = codeTranService.findTransCodeDictVo("ChargeCode", prePayVo.getChargeCode());
                subDto.setPayRefReason(dictVo == null ? "" : dictVo.getProperty1());
            }
            if (compensateVo.getRiskCode() != null && compensateVo.getRiskCode().length() > 1) {
                subDto.setClassCode(compensateVo.getRiskCode().substring(0, 2));
            }
            subDto.setKindCode(prePayVo.getKindCode());
            subDto.setPlanFee(prePayVo.getPayAmt());
            subDto.setIsTaxable("1");

            subDtos.add(subDto);
        }

        return subDtos;

    }

    /**
     * ????????????????????????????????????????????????0
     *
     * @param prePayVos ???????????? P-?????? F-??????
     * @return !0-true  0-false
     */
    private boolean isPrePayAndFeeNonZero(List<PrpLPrePayVo> prePayVos) {
        boolean nonZero = true;
        BigDecimal sumAmt = new BigDecimal(0);
        if (prePayVos != null && prePayVos.size() > 0) {
            for (PrpLPrePayVo prePayVo : prePayVos) {
                sumAmt = sumAmt.add(DataUtils.NullToZero(prePayVo.getPayAmt()));
            }
        }

        if (BigDecimal.ZERO.compareTo(DataUtils.NullToZero(sumAmt)) == 0) {
            nonZero = false;
        }

        return nonZero;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param compensateVo ?????????????????????
     * @throws Exception ??????????????????????????????
     */
    private void updatePrePayStatus(PrpLCompensateVo compensateVo,List<PayRefNoDto> payRefNoDtos) throws Exception {
        try {
            logger.info("????????????{} ??????????????????????????????????????????????????????...", compensateVo.getCompensateNo());

			Map<String,String> mapparam = new HashMap<String, String>();
			if(payRefNoDtos != null && payRefNoDtos.size()>0){
				for (PayRefNoDto payRefNoDto : payRefNoDtos) {
					String serialNo = payRefNoDto.getSerialNo();
					String settleNo = payRefNoDto.getPayRefNo();
					mapparam.put(serialNo,settleNo);
				}
			}
            List<PrpLPrePayVo> prePayVos = compensateService.getPrePayVo(compensateVo.getCompensateNo(), null);
            List<PrpLPrePayVo> prePayVoList = new ArrayList<PrpLPrePayVo>();
            if (prePayVos != null && prePayVos.size() > 0) {
                for (PrpLPrePayVo prePayVo : prePayVos) {
                    if (BigDecimal.ZERO.compareTo(prePayVo.getPayAmt()) != 0) {
						String settleNo = mapparam.get(prePayVo.getSerialNo());
						if(settleNo != null && settleNo != ""){
							prePayVo.setSettleNo(settleNo);
						}
                        prePayVo.setPayStatus("2");
                        // ??????????????????Y??????D??????P??????F??????
                        this.savePrplPayHis(compensateVo.getClaimNo(), prePayVo.getCompensateNo(), prePayVo.getId(), "2", "Y");
                    } else {
                        this.savePrplPayHis(compensateVo.getClaimNo(), prePayVo.getCompensateNo(), prePayVo.getId(), "0", "Y");
                    }
                }
                prePayVoList.addAll(prePayVos);
            }
            compensateService.saveOrUpdatePrePay(prePayVoList, compensateVo.getCompensateNo());
            logger.info("????????????{} ?????????????????????????????????????????????????????????", compensateVo.getCompensateNo());
        } catch (Exception e) {
            logger.info("????????????" + compensateVo.getCompensateNo() + " ?????????????????????????????????????????????????????????", e);
            throw e;
        }
    }

    /**
     * ???????????????????????????
     *
     * @param padPayMainVo ??????????????????
     * @return ???????????????????????????
     * @throws Exception ??????????????????
     */
    private Claim2NewPaymentDto packagePadPayInfos(PrpLPadPayMainVo padPayMainVo) throws Exception {
        List<PrpJlossPlanDto> jlossPlanDtos = new ArrayList<PrpJlossPlanDto>();
        List<PrpJlossPlanSubDto> jlossPlanSubDtos = new ArrayList<PrpJlossPlanSubDto>();
        BigDecimal costSum = getPadPayCostSum(padPayMainVo);
        Claim2NewPaymentDto claim2NewPaymentDto = new Claim2NewPaymentDto();
        if (costSum.compareTo(BigDecimal.ZERO) != 0) {

            PrpLClaimVo prplClaimVo = claimService.findClaimVoByClaimNo(padPayMainVo.getClaimNo());
            PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(padPayMainVo.getRegistNo(), padPayMainVo.getPolicyNo());
            PrpLRegistVo registVo = registQueryService.findByRegistNo(padPayMainVo.getRegistNo());

            // 2. ??????????????????
            for (PrpLPadPayPersonVo personVo : padPayMainVo.getPrpLPadPayPersons()) {
                PrpJlossPlanDto prpJlossPlanDto = new PrpJlossPlanDto();
                PrpJlossPlanSubDto prpJlossPlanSubDto = new PrpJlossPlanSubDto();
                prpJlossPlanDto.setPayeeId(personVo.getPayeeId() == null ? "" : personVo.getPayeeId().toString());
                prpJlossPlanDto.setCertiType(PaymentConstants.CERTITYPE_C);
                prpJlossPlanDto.setCertiNo(padPayMainVo.getCompensateNo());
                prpJlossPlanDto.setPolicyNo(padPayMainVo.getPolicyNo());
                prpJlossPlanDto.setRegistNo(padPayMainVo.getRegistNo());
                // ??????????????? ???????????????
                prpJlossPlanDto.setSerialNo(Integer.parseInt(personVo.getSerialNo()));
                prpJlossPlanDto.setClaimNo(padPayMainVo.getClaimNo());
                prpJlossPlanDto.setLossType(PaymentConstants.LOSSTYPE_0);
                prpJlossPlanDto.setPayRefReason(PaymentConstants.PAYREASON_P6K);
                prpJlossPlanDto.setClassCode(prplcMainVo == null ? "" : prplcMainVo.getClassCode());
                prpJlossPlanDto.setRiskCode(Risk.DQZ);
                prpJlossPlanDto.setAppliCode(prplcMainVo == null ? "" : prplcMainVo.getAppliCode());
                prpJlossPlanDto.setAppliName(prplcMainVo == null ? "" : prplcMainVo.getAppliName());
                prpJlossPlanDto.setInsuredCode(prplcMainVo == null ? "" : prplcMainVo.getInsuredCode());
                prpJlossPlanDto.setInsuredName(prplcMainVo == null ? "" : prplcMainVo.getInsuredName());
                prpJlossPlanDto.setStartDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getStartDate(), "yyyy-MM-dd"));
                prpJlossPlanDto.setEndDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getEndDate(), "yyyy-MM-dd"));
                prpJlossPlanDto.setUnderWriteDate(dateToString(padPayMainVo.getUnderwriteDate(), "yyyy-MM-dd"));
                prpJlossPlanDto.setEndCaseDate(dateToString(prplClaimVo.getEndCaseTime(), "yyyy-MM-dd HH:mm:ss"));
                prpJlossPlanDto.setCurrency(CodeConstants.Currency.CNY);
                prpJlossPlanDto.setComCode(padPayMainVo.getComCode());
                prpJlossPlanDto.setMakeCom(prplcMainVo == null ? registVo.getComCode() : prplcMainVo.getComCode());

                prpJlossPlanDto.setHandler1Code(padPayMainVo.getCreateUser());
                SysUserVo handler1 = sysUserService.findByUserCode(padPayMainVo.getCreateUser());
                prpJlossPlanDto.setHandler1CodeName(handler1 == null ? "" : handler1.getUserName());
                prpJlossPlanDto.setHandlerCode(padPayMainVo.getUpdateUser());
                prpJlossPlanDto.setOperateCode(padPayMainVo.getUpdateUser());
                prpJlossPlanDto.setOperateComCode(padPayMainVo.getComCode());
                prpJlossPlanDto.setBusinessNature(prplcMainVo == null ? "" : prplcMainVo.getBusinessNature());
                prpJlossPlanDto.setBusinessplate(prplcMainVo == null ? "" : prplcMainVo.getBusinessPlate());

                if (prplcMainVo != null && prplcMainVo.getPrpCItemCars() != null) {
                    for (PrpLCItemCarVo itemCarVo : prplcMainVo.getPrpCItemCars()) {
                        if (padPayMainVo.getPolicyNo() != null && padPayMainVo.getPolicyNo().equals(itemCarVo.getPolicyNo())) {
                            prpJlossPlanDto.setLicenseNo(itemCarVo.getLicenseNo());
							String carType = this.transCarType(itemCarVo);
                            prpJlossPlanDto.setCarNatureCode(carType);
                            prpJlossPlanDto.setUsenaturecode(itemCarVo.getUseNatureCode());
                            break;
                        }
                    }
                }

                prpJlossPlanDto.setCoinsFlag(registVo == null ? "" : registVo.getIsGBFlag());
                if (registVo != null) {
                    if (PaymentConstants.COINSFLAG_2.equals(registVo.getIsGBFlag())
                            || PaymentConstants.COINSFLAG_4.equals(registVo.getIsGBFlag())) {
                        prpJlossPlanDto.setCoinsType(PaymentConstants.COINSTYPE_1);
                    }
                }
                prpJlossPlanDto.setIsSimpleCase(padPayMainVo.getIsFastReparation());
                prpJlossPlanDto.setPlanFee(costSum);
                prpJlossPlanDto.setSystemFlag(PaymentConstants.SYSTEMFLAG_CAR);
                prpJlossPlanDto.setLocationFlag(prplcMainVo == null ? "D" : prplcMainVo.getNationFlag());
                prpJlossPlanDto.setSplitRiskFlag(PaymentConstants.SPLITRISKFLAG_1);

                // ????????????????????????
                PrpLPayCustomVo custom = managerService.findPayCustomVoById(personVo.getPayeeId());
                if (custom != null) {
                    prpJlossPlanDto.setPayObjectName(custom.getPayeeName());
                    prpJlossPlanDto.setBankCode(custom.getBankNo());
                    prpJlossPlanDto.setBankName(custom.getBankName());
                    prpJlossPlanDto.setBankType(custom.getBankType());
                    prpJlossPlanDto.setAccountCode(custom.getAccountNo());
                    prpJlossPlanDto.setAccountName(custom.getPayeeName());
                    prpJlossPlanDto.setAccountType(custom.getPublicAndPrivate());
                    prpJlossPlanDto.setCertificateType(custom.getCertifyType());
                    prpJlossPlanDto.setCertificateCode(custom.getCertifyNo());
                    prpJlossPlanDto.setPayeePhone(custom.getPayeeMobile());
                    prpJlossPlanDto.setAbstractContent(personVo.getSummary() == null ? "" : personVo.getSummary());
                    prpJlossPlanDto.setBankProvinceCode(custom.getProvinceCode() == null ? "" : custom.getProvinceCode().toString());
                    prpJlossPlanDto.setBankProvinceName(custom.getProvince());
                    prpJlossPlanDto.setBankCityCode(custom.getCityCode() == null ? "" : custom.getCityCode().toString());
                    prpJlossPlanDto.setBankCityName(custom.getCity());
                    prpJlossPlanDto.setUsage(StringUtils.isBlank(custom.getPurpose()) ? "???" : custom.getPurpose());
                    prpJlossPlanDto.setOtherRemark(custom.getRemark());
                    // ??????
                    String msg = padPayMsg(padPayMainVo, personVo, custom);
                    prpJlossPlanDto.setMessageContent(msg);
                    prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);
                } else {
                    logger.info("??????????????????id???" + personVo.getPayeeId() + " ?????????????????????" + padPayMainVo.getCompensateNo() + " paycustom?????????????????????????????????");
                }
                prpJlossPlanDto.setPayeeCurrency("CNY");

                prpJlossPlanDto.setPayReasonFlag(StringUtils.isBlank(personVo.getOtherFlag()) ? "0" : "1");
                prpJlossPlanDto.setIsAutoPay(padPayMainVo.getIsFastReparation());
                prpJlossPlanDto.setIsExpress("0");
                prpJlossPlanDto.setYuPayFlag("0");

                prpJlossPlanSubDto.setCertiType(PaymentConstants.CERTITYPE_C);
                prpJlossPlanSubDto.setCertiNo(padPayMainVo.getCompensateNo());
                prpJlossPlanSubDto.setPolicyNo(padPayMainVo.getPolicyNo());
                prpJlossPlanSubDto.setPayRefReason(PaymentConstants.PAYREASON_P6K);
                prpJlossPlanSubDto.setSerialNo(Integer.parseInt(personVo.getSerialNo()));
                prpJlossPlanSubDto.setLossType(PaymentConstants.LOSSTYPE_0);
                prpJlossPlanSubDto.setClassCode(PaymentConstants.CLASSCODE_11);
                prpJlossPlanSubDto.setRiskCode(Risk.DQZ);
                prpJlossPlanSubDto.setKindCode(CodeConstants.KINDCODE.KINDCODE_BZ);
                prpJlossPlanSubDto.setPlanFee(personVo.getCostSum());
                prpJlossPlanSubDto.setIsTaxable("1");
                prpJlossPlanSubDto.setChargeCode(CodeConstants.VatFeeType.DPAY);

                jlossPlanSubDtos.add(prpJlossPlanSubDto);
                prpJlossPlanDto.setPrpJlossPlanSubDtos(jlossPlanSubDtos);
                jlossPlanDtos.add(prpJlossPlanDto);
            }
            claim2NewPaymentDto.setPrpJlossPlanDtos(jlossPlanDtos);
        }
        return claim2NewPaymentDto;
    }

    /**
     * ?????????????????????
     *
     * @param padPayMainVo ??????????????????
     * @return !0-true  0-false
     */
    private BigDecimal getPadPayCostSum(PrpLPadPayMainVo padPayMainVo) {
        BigDecimal sumAmt = BigDecimal.ZERO;
        if (padPayMainVo != null && padPayMainVo.getPrpLPadPayPersons() != null && padPayMainVo.getPrpLPadPayPersons().size() > 0) {
            for (PrpLPadPayPersonVo padPayPersonVo : padPayMainVo.getPrpLPadPayPersons()) {
                sumAmt = sumAmt.add(DataUtils.NullToZero(padPayPersonVo.getCostSum()));
            }
        }

        return sumAmt;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param padPayMainVo ??????????????????
     * @throws Exception ??????????????????????????????
     */
    private void updatePadPayStatus(PrpLPadPayMainVo padPayMainVo,List<PayRefNoDto> payRefNoDtos) throws Exception {
        try {
            logger.info("????????????{} ??????????????????????????????????????????????????????...", padPayMainVo.getCompensateNo());
			Map<String,String> mapparam = new HashMap<String, String>();
			if(payRefNoDtos != null && payRefNoDtos.size()>0){
				for (PayRefNoDto payRefNoDto : payRefNoDtos) {
					String serialNo = payRefNoDto.getSerialNo();
					String settleNo = payRefNoDto.getPayRefNo();
					mapparam.put(serialNo,settleNo);
				}
			}
            if (padPayMainVo.getPrpLPadPayPersons() != null && padPayMainVo.getPrpLPadPayPersons().size() > 0) {
                for (PrpLPadPayPersonVo PadPayPersonVo : padPayMainVo.getPrpLPadPayPersons()) {
                    if (BigDecimal.ZERO.compareTo(PadPayPersonVo.getCostSum()) != 0) {
                        PadPayPersonVo.setPayStatus("2");
						String settleNo = mapparam.get(PadPayPersonVo.getSerialNo());
						if(settleNo != null && settleNo != ""){
							PadPayPersonVo.setSettleNo(settleNo);
						}
                        this.savePrplPayHis(padPayMainVo.getClaimNo(), padPayMainVo.getCompensateNo(), PadPayPersonVo.getId(), "2", "D");
                    } else {
                        this.savePrplPayHis(padPayMainVo.getClaimNo(), padPayMainVo.getCompensateNo(), PadPayPersonVo.getId(), "0", "D");
                    }
                }
            }
            padPayPubService.updatePadPay(padPayMainVo);
            logger.info("????????????{} ?????????????????????????????????????????????????????????", padPayMainVo.getCompensateNo());
        } catch (Exception e) {
            logger.info("????????????" + padPayMainVo.getCompensateNo() + " ??????????????????????????????????????????????????????!", e);
            throw e;
        }
    }

    /**
     * ??????????????????
     *
     * @param padPayMainVo   ??????????????????
     * @param padPayPersonVo ????????????????????????
     * @param customVo       ???????????????
     * @return ????????????
     */
    private String padPayMsg(PrpLPadPayMainVo padPayMainVo, PrpLPadPayPersonVo padPayPersonVo, PrpLPayCustomVo customVo) throws Exception {
        String message = "";
        PrpLRegistVo prplRegistVo = registQueryService.findByRegistNo(padPayMainVo.getRegistNo());
        List<PrpLCItemKindVo> carItemKinds = registQueryService.findCItemKindListByRegistNo(padPayMainVo.getRegistNo());
        SendMsgParamVo msgParamVo = new SendMsgParamVo();
        msgParamVo.setRegistNo(padPayMainVo.getRegistNo());
        msgParamVo.setLicenseNo(prplRegistVo.getPrpLRegistExt().getLicenseNo());
        msgParamVo.setDamageTime(prplRegistVo.getDamageTime());
        msgParamVo.setSumAmt(padPayPersonVo.getCostSum().toString());
        msgParamVo.setLinkerName(customVo.getPayeeName());
        // ????????????
        msgParamVo.setPrpCItemKinds(carItemKinds);
        SysMsgModelVo msgModelVo = this.findSysMsgModelVo(prplRegistVo, CodeConstants.CompensateFlag.padPay);
        if (msgModelVo != null) {
            message = sendMsgService.getMessage(msgModelVo.getContent(), msgParamVo);
        }

        return message;
    }

    /**
     * ??????????????????
     *
     * @param prplRegistVo   ????????????
     * @param compensateFlag ????????????
     * @return ??????????????????
     */
    private SysMsgModelVo findSysMsgModelVo(PrpLRegistVo prplRegistVo, String compensateFlag) {
        SysMsgModelVo msgModelVo = null;
        String systemNode = null;

        if (CodeConstants.CompensateFlag.prePay.equals(compensateFlag)) {
            systemNode = CodeConstants.SystemNode.prePay;
        } else if (CodeConstants.CompensateFlag.padPay.equals(compensateFlag)) {
            systemNode = CodeConstants.SystemNode.padPay;
        } else {
            systemNode = CodeConstants.SystemNode.endCase;
        }
        msgModelVo = sendMsgService.findmsgModelVo(CodeConstants.ModelType.payee, systemNode, prplRegistVo.getComCode(), CodeConstants.CaseType.normal);
        return msgModelVo;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param prplReplevyMainVo ??????????????????
     * @return ???????????????
     * @throws Exception ??????????????????
     */
    private Claim2NewPaymentDto packageRelevyDatas(PrplReplevyMainVo prplReplevyMainVo) throws Exception {
        Claim2NewPaymentDto claim2NewPaymentDto = new Claim2NewPaymentDto();
        List<PrpJlossPlanDto> jlossPlanDtos = new ArrayList<PrpJlossPlanDto>();
        if (prplReplevyMainVo != null) {
            // ??????????????????
			if (BigDecimal.ZERO.compareTo(prplReplevyMainVo.getSumRealReplevy()) < 0) {
				packageRelevyPayData(prplReplevyMainVo, jlossPlanDtos);
			}
            // ??????????????????
			if (BigDecimal.ZERO.compareTo(prplReplevyMainVo.getSumReplevyFee()) < 0) {
				packageRelevyFeeData(prplReplevyMainVo, jlossPlanDtos);
			}

            claim2NewPaymentDto.setPrpJlossPlanDtos(jlossPlanDtos);
        }
        return claim2NewPaymentDto;
    }

    /**
     * ?????????????????????????????????
     *
     * @param prplReplevyMainVo ??????????????????
     * @param jlossPlanDtos     ?????????????????????
     * @throws Exception ??????????????????
     */
    private void packageRelevyPayData(PrplReplevyMainVo prplReplevyMainVo, List<PrpJlossPlanDto> jlossPlanDtos) throws Exception {
        List<PrpJlossPlanSubDto> jlossPlanSubDtos = new ArrayList<PrpJlossPlanSubDto>();

        PrpLClaimVo prplClaimVo = claimService.findClaimVoByClaimNo(prplReplevyMainVo.getClaimNo());
        PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(prplReplevyMainVo.getRegistNo(), prplReplevyMainVo.getPolicyNo());
        PrpLRegistVo registVo = registQueryService.findByRegistNo(prplReplevyMainVo.getRegistNo());

        PrpJlossPlanDto prpJlossPlanDto = new PrpJlossPlanDto();
        prpJlossPlanDto.setCertiType(PaymentConstants.CERTITYPE_Z);
        prpJlossPlanDto.setCertiNo(prplReplevyMainVo.getCompensateNo());
        prpJlossPlanDto.setPolicyNo(prplReplevyMainVo.getPolicyNo());
        prpJlossPlanDto.setRegistNo(prplReplevyMainVo.getRegistNo());
        // ??????????????? ???????????????
        prpJlossPlanDto.setSerialNo(0);
        prpJlossPlanDto.setClaimNo(prplReplevyMainVo.getClaimNo());
        prpJlossPlanDto.setLossType(PaymentConstants.LOSSTYPE_0);
        prpJlossPlanDto.setPayRefReason(PaymentConstants.PAYREASON_P6C);
        prpJlossPlanDto.setClassCode(prplcMainVo == null ? "" : prplcMainVo.getClassCode());
        prpJlossPlanDto.setRiskCode(prplClaimVo.getRiskCode());
        prpJlossPlanDto.setAppliCode(prplcMainVo == null ? "" : prplcMainVo.getAppliCode());
        prpJlossPlanDto.setAppliName(prplcMainVo == null ? "" : prplcMainVo.getAppliName());
        prpJlossPlanDto.setInsuredCode(prplcMainVo == null ? "" : prplcMainVo.getInsuredCode());
        prpJlossPlanDto.setInsuredName(prplcMainVo == null ? "" : prplcMainVo.getInsuredName());
        prpJlossPlanDto.setStartDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getStartDate(), "yyyy-MM-dd"));
        prpJlossPlanDto.setEndDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getEndDate(), "yyyy-MM-dd"));
        prpJlossPlanDto.setUnderWriteDate(dateToString(new Date(), "yyyy-MM-dd"));
        prpJlossPlanDto.setEndCaseDate(dateToString(prplClaimVo.getEndCaseTime(), "yyyy-MM-dd HH:mm:ss"));
        prpJlossPlanDto.setCurrency(CodeConstants.Currency.CNY);
        prpJlossPlanDto.setComCode(prplReplevyMainVo.getComCode());
        prpJlossPlanDto.setMakeCom(prplcMainVo == null ? registVo.getComCode() : prplcMainVo.getComCode());

        prpJlossPlanDto.setHandler1Code(prplReplevyMainVo.getCreateUser());
        SysUserVo handler1 = sysUserService.findByUserCode(prplReplevyMainVo.getCreateUser());
        prpJlossPlanDto.setHandler1CodeName(handler1 == null ? "" : handler1.getUserName());
        prpJlossPlanDto.setHandlerCode(prplReplevyMainVo.getUpdateUser());
        prpJlossPlanDto.setOperateCode(prplReplevyMainVo.getUpdateUser());
        prpJlossPlanDto.setOperateComCode(prplReplevyMainVo.getComCode());
        prpJlossPlanDto.setBusinessNature(prplcMainVo == null ? "" : prplcMainVo.getBusinessNature());
        prpJlossPlanDto.setBusinessplate(prplcMainVo == null ? "" : prplcMainVo.getBusinessPlate());

        if (prplcMainVo != null && prplcMainVo.getPrpCItemCars() != null) {
            for (PrpLCItemCarVo itemCarVo : prplcMainVo.getPrpCItemCars()) {
                if (prplReplevyMainVo.getPolicyNo() != null && prplReplevyMainVo.getPolicyNo().equals(itemCarVo.getPolicyNo())) {
                    prpJlossPlanDto.setLicenseNo(itemCarVo.getLicenseNo());
					String carType = this.transCarType(itemCarVo);
                    prpJlossPlanDto.setCarNatureCode(carType);
                    prpJlossPlanDto.setUsenaturecode(itemCarVo.getUseNatureCode());
                    break;
                }
            }
        }

        prpJlossPlanDto.setCoinsFlag(registVo == null ? "" : registVo.getIsGBFlag());
        if (registVo != null) {
            if (PaymentConstants.COINSFLAG_2.equals(registVo.getIsGBFlag())
                    || PaymentConstants.COINSFLAG_4.equals(registVo.getIsGBFlag())) {
				String coinsCode = policyViewService.findCoinsCode(prplReplevyMainVo.getPolicyNo());
				List<PrpLCoinsVo> ywCoinsList = compensateTaskService.findPrpLCoins(prplReplevyMainVo.getPolicyNo());
				String coinsName = "";
				if (StringUtils.isNotBlank(coinsCode)) {
					for (PrpLCoinsVo coinsVo : ywCoinsList) {
						if (coinsCode.equals(coinsVo.getCoinsCode())) {
							coinsName = coinsVo.getCoinsName();
							break;
						}
					}
				}

                prpJlossPlanDto.setCoinsType(PaymentConstants.COINSTYPE_1);
                prpJlossPlanDto.setCoinsCode(coinsCode);
                prpJlossPlanDto.setCoinsName(coinsName);
            }
        }
        prpJlossPlanDto.setIsSimpleCase("0");
        prpJlossPlanDto.setPlanFee(BigDecimal.ZERO.compareTo(prplReplevyMainVo.getSumRealReplevy()) > 0 ? prplReplevyMainVo.getSumRealReplevy() : BigDecimal.ZERO.subtract(prplReplevyMainVo.getSumRealReplevy()));
        prpJlossPlanDto.setSystemFlag(PaymentConstants.SYSTEMFLAG_CAR);
        prpJlossPlanDto.setLocationFlag(prplcMainVo == null ? "D" : prplcMainVo.getNationFlag());
        prpJlossPlanDto.setSplitRiskFlag(PaymentConstants.SPLITRISKFLAG_1);
        prpJlossPlanDto.setPayeeCurrency("CNY");
        prpJlossPlanDto.setIsAutoPay("0");
        prpJlossPlanDto.setIsExpress("0");
        prpJlossPlanDto.setYuPayFlag("0");
        prpJlossPlanDto.setUsage("???");
        prpJlossPlanDto.setPayReasonFlag("0");
        prpJlossPlanDto.setVoucherNo2("???");
        prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);
        // ??????????????????
        for (PrplReplevyDetailVo detailVo : prplReplevyMainVo.getPrplReplevyDetails()) {
            if (PaymentConstants.FEETYPE_P.equals(detailVo.getLossCategory())) {
                PrpJlossPlanSubDto prpJlossPlanSubDto = new PrpJlossPlanSubDto();

                prpJlossPlanSubDto.setCertiType(PaymentConstants.CERTITYPE_Z);
                prpJlossPlanSubDto.setCertiNo(prplReplevyMainVo.getCompensateNo());
                prpJlossPlanSubDto.setPolicyNo(prplReplevyMainVo.getPolicyNo());
                prpJlossPlanSubDto.setPayRefReason(PaymentConstants.PAYREASON_P6C);
                prpJlossPlanSubDto.setSerialNo(0);
                prpJlossPlanSubDto.setLossType(PaymentConstants.LOSSTYPE_0);
                prpJlossPlanSubDto.setClassCode(prplcMainVo == null ? "" : prplcMainVo.getClassCode());
                prpJlossPlanSubDto.setRiskCode(prplClaimVo.getRiskCode());
                prpJlossPlanSubDto.setKindCode(detailVo.getKindCode());
                prpJlossPlanSubDto.setPlanFee(BigDecimal.ZERO.compareTo(detailVo.getRealReplevy()) > 0 ? detailVo.getRealReplevy() : BigDecimal.ZERO.subtract(detailVo.getRealReplevy()));
                prpJlossPlanSubDto.setIsTaxable("1");

                jlossPlanSubDtos.add(prpJlossPlanSubDto);
            }
        }
        prpJlossPlanDto.setPrpJlossPlanSubDtos(jlossPlanSubDtos);
        jlossPlanDtos.add(prpJlossPlanDto);
    }

    /**
     * ?????????????????????????????????
     *
     * @param prplReplevyMainVo ??????????????????
     * @param jlossPlanDtos     ???????????????????????????
     * @throws Exception ??????????????????
     */
    private void packageRelevyFeeData(PrplReplevyMainVo prplReplevyMainVo, List<PrpJlossPlanDto> jlossPlanDtos) throws Exception {
        List<PrpJlossPlanSubDto> jlossPlanSubDtos = new ArrayList<PrpJlossPlanSubDto>();

        PrpLClaimVo prplClaimVo = claimService.findClaimVoByClaimNo(prplReplevyMainVo.getClaimNo());
        PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(prplReplevyMainVo.getRegistNo(), prplReplevyMainVo.getPolicyNo());
        PrpLRegistVo registVo = registQueryService.findByRegistNo(prplReplevyMainVo.getRegistNo());

        PrpJlossPlanDto prpJlossPlanDto = new PrpJlossPlanDto();
        prpJlossPlanDto.setCertiType(PaymentConstants.CERTITYPE_Z);
        prpJlossPlanDto.setCertiNo(prplReplevyMainVo.getCompensateNo());
        prpJlossPlanDto.setPolicyNo(prplReplevyMainVo.getPolicyNo());
        prpJlossPlanDto.setRegistNo(prplReplevyMainVo.getRegistNo());
        // ??????????????? ???????????????
        prpJlossPlanDto.setSerialNo(0);
        prpJlossPlanDto.setClaimNo(prplReplevyMainVo.getClaimNo());
        prpJlossPlanDto.setLossType(PaymentConstants.LOSSTYPE_1);
        prpJlossPlanDto.setPayRefReason(PaymentConstants.PAYREASON_P6H);
        prpJlossPlanDto.setClassCode(prplcMainVo == null ? "" : prplcMainVo.getClassCode());
        prpJlossPlanDto.setRiskCode(prplClaimVo.getRiskCode());
        prpJlossPlanDto.setAppliCode(prplcMainVo == null ? "" : prplcMainVo.getAppliCode());
        prpJlossPlanDto.setAppliName(prplcMainVo == null ? "" : prplcMainVo.getAppliName());
        prpJlossPlanDto.setInsuredCode(prplcMainVo == null ? "" : prplcMainVo.getInsuredCode());
        prpJlossPlanDto.setInsuredName(prplcMainVo == null ? "" : prplcMainVo.getInsuredName());
        prpJlossPlanDto.setStartDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getStartDate(), "yyyy-MM-dd"));
        prpJlossPlanDto.setEndDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getEndDate(), "yyyy-MM-dd"));
        prpJlossPlanDto.setUnderWriteDate(dateToString(new Date(), "yyyy-MM-dd"));
        prpJlossPlanDto.setEndCaseDate(dateToString(prplClaimVo.getEndCaseTime(), "yyyy-MM-dd HH:mm:ss"));
        prpJlossPlanDto.setCurrency(CodeConstants.Currency.CNY);
        prpJlossPlanDto.setComCode(prplReplevyMainVo.getComCode());
        prpJlossPlanDto.setMakeCom(prplcMainVo == null ? registVo.getComCode() : prplcMainVo.getComCode());

        prpJlossPlanDto.setHandler1Code(prplReplevyMainVo.getCreateUser());
        SysUserVo handler1 = sysUserService.findByUserCode(prplReplevyMainVo.getCreateUser());
        prpJlossPlanDto.setHandler1CodeName(handler1 == null ? "" : handler1.getUserName());
        prpJlossPlanDto.setHandlerCode(prplReplevyMainVo.getUpdateUser());
        prpJlossPlanDto.setOperateCode(prplReplevyMainVo.getUpdateUser());
        prpJlossPlanDto.setOperateComCode(prplReplevyMainVo.getComCode());
        prpJlossPlanDto.setBusinessNature(prplcMainVo == null ? "" : prplcMainVo.getBusinessNature());
        prpJlossPlanDto.setBusinessplate(prplcMainVo == null ? "" : prplcMainVo.getBusinessPlate());

        if (prplcMainVo != null && prplcMainVo.getPrpCItemCars() != null) {
            for (PrpLCItemCarVo itemCarVo : prplcMainVo.getPrpCItemCars()) {
                if (prplReplevyMainVo.getPolicyNo() != null && prplReplevyMainVo.getPolicyNo().equals(itemCarVo.getPolicyNo())) {
                    prpJlossPlanDto.setLicenseNo(itemCarVo.getLicenseNo());
					String carType = this.transCarType(itemCarVo);
                    prpJlossPlanDto.setCarNatureCode(carType);
                    prpJlossPlanDto.setUsenaturecode(itemCarVo.getUseNatureCode());
                    break;
                }
            }
        }

        prpJlossPlanDto.setCoinsFlag(registVo == null ? "" : registVo.getIsGBFlag());
        if (registVo != null) {
            if (PaymentConstants.COINSFLAG_2.equals(registVo.getIsGBFlag())
                    || PaymentConstants.COINSFLAG_4.equals(registVo.getIsGBFlag())) {
				String coinsCode = policyViewService.findCoinsCode(prplReplevyMainVo.getPolicyNo());
				List<PrpLCoinsVo> ywCoinsList = compensateTaskService.findPrpLCoins(prplReplevyMainVo.getPolicyNo());
				String coinsName = "";
				if (StringUtils.isNotBlank(coinsCode)) {
					for (PrpLCoinsVo coinsVo : ywCoinsList) {
						if (coinsCode.equals(coinsVo.getCoinsCode())) {
							coinsName = coinsVo.getCoinsName();
							break;
						}
					}
				}

				prpJlossPlanDto.setCoinsType(PaymentConstants.COINSTYPE_1);
				prpJlossPlanDto.setCoinsCode(coinsCode);
				prpJlossPlanDto.setCoinsName(coinsName);
            }
        }
        prpJlossPlanDto.setIsSimpleCase("0");
        prpJlossPlanDto.setPlanFee(BigDecimal.ZERO.compareTo(prplReplevyMainVo.getSumReplevyFee()) > 0 ? prplReplevyMainVo.getSumReplevyFee() : BigDecimal.ZERO.subtract(prplReplevyMainVo.getSumReplevyFee()));
        prpJlossPlanDto.setSystemFlag(PaymentConstants.SYSTEMFLAG_CAR);
        prpJlossPlanDto.setLocationFlag(prplcMainVo == null ? "D" : prplcMainVo.getNationFlag());
        prpJlossPlanDto.setSplitRiskFlag(PaymentConstants.SPLITRISKFLAG_1);
        prpJlossPlanDto.setPayeeCurrency("CNY");
        prpJlossPlanDto.setIsAutoPay("0");
        prpJlossPlanDto.setIsExpress("0");
        prpJlossPlanDto.setYuPayFlag("0");
        prpJlossPlanDto.setUsage("???");
        prpJlossPlanDto.setPayReasonFlag("0");
        prpJlossPlanDto.setVoucherNo2("???");
        prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);
        // ??????????????????
        // ???????????????
        int i = 0;
        for (PrplReplevyDetailVo detailVo : prplReplevyMainVo.getPrplReplevyDetails()) {
            if (PaymentConstants.FEETYPE_F.equals(detailVo.getLossCategory())) {
                PrpJlossPlanSubDto prpJlossPlanSubDto = new PrpJlossPlanSubDto();

                prpJlossPlanSubDto.setCertiType(PaymentConstants.CERTITYPE_Z);
                prpJlossPlanSubDto.setCertiNo(prplReplevyMainVo.getCompensateNo());
                prpJlossPlanSubDto.setPolicyNo(prplReplevyMainVo.getPolicyNo());
                prpJlossPlanSubDto.setPayRefReason(PaymentConstants.PAYREASON_P6H);
                prpJlossPlanSubDto.setSerialNo(i);
                prpJlossPlanSubDto.setLossType(PaymentConstants.LOSSTYPE_1);
                prpJlossPlanSubDto.setClassCode(prplcMainVo == null ? "" : prplcMainVo.getClassCode());
                prpJlossPlanSubDto.setRiskCode(prplClaimVo.getRiskCode());
                prpJlossPlanSubDto.setKindCode(detailVo.getKindCode());
                prpJlossPlanSubDto.setPlanFee(BigDecimal.ZERO.compareTo(detailVo.getRealReplevy()) > 0 ? detailVo.getRealReplevy() : BigDecimal.ZERO.subtract(detailVo.getRealReplevy()));
                prpJlossPlanSubDto.setIsTaxable("1");

                jlossPlanSubDtos.add(prpJlossPlanSubDto);
            }
        }
        prpJlossPlanDto.setPrpJlossPlanSubDtos(jlossPlanSubDtos);
        jlossPlanDtos.add(prpJlossPlanDto);
    }

    /**
     * ?????????????????????????????????
     *
     * @param recLossVo ???????????????????????????
     * @return ??????????????????
     */
    private Claim2NewPaymentDto packageRecLossDatas(PrpLRecLossVo recLossVo) throws Exception {
        Claim2NewPaymentDto claim2NewPaymentDto = new Claim2NewPaymentDto();
        List<PrpJlossPlanDto> jlossPlanDtos = new ArrayList<PrpJlossPlanDto>();
        if (recLossVo != null && DataUtils.NullToZero(recLossVo.getRecLossFee()).compareTo(BigDecimal.ZERO)==1) {
            List<PrpJlossPlanSubDto> jlossPlanSubDtos = new ArrayList<PrpJlossPlanSubDto>();

            PrpLRegistVo registVo = registQueryService.findByRegistNo(recLossVo.getRegistNo());
            List<PrpLClaimVo> claimVos = claimTaskService.findClaimListByRegistNo(recLossVo.getRegistNo(), "1");
            PrpLClaimVo prplClaimVo = new PrpLClaimVo();
            if (claimVos != null) {
                if (claimVos.size() > 1) {
                    for (PrpLClaimVo claimVo : claimVos) {
                        if (PaymentConstants.CLASSCODE_12.equals(claimVo.getClassCode())) {
                            prplClaimVo = claimVo;
                            break;
                        }
                    }
                } else {
                    prplClaimVo = claimVos.get(0);
                }
            }
            PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(prplClaimVo.getRegistNo(), prplClaimVo.getPolicyNo());

            PrpJlossPlanDto prpJlossPlanDto = new PrpJlossPlanDto();
            prpJlossPlanDto.setCertiType(PaymentConstants.CERTITYPE_C);
            prpJlossPlanDto.setCertiNo(recLossVo.getPrpLRecLossId());
            prpJlossPlanDto.setPolicyNo(prplClaimVo.getPolicyNo());
            prpJlossPlanDto.setRegistNo(recLossVo.getRegistNo());
            // ??????????????? ???????????????
            prpJlossPlanDto.setSerialNo(0);
            prpJlossPlanDto.setClaimNo(prplClaimVo.getClaimNo());
            prpJlossPlanDto.setLossType(PaymentConstants.LOSSTYPE_0);
            prpJlossPlanDto.setPayRefReason(PaymentConstants.PAYREASON_P6J);
            prpJlossPlanDto.setClassCode(prplClaimVo.getClassCode());
            prpJlossPlanDto.setRiskCode(prplClaimVo.getRiskCode());
            prpJlossPlanDto.setAppliCode(prplcMainVo == null ? "" : prplcMainVo.getAppliCode());
            prpJlossPlanDto.setAppliName(prplcMainVo == null ? "" : prplcMainVo.getAppliName());
            prpJlossPlanDto.setInsuredCode(prplcMainVo == null ? "" : prplcMainVo.getInsuredCode());
            prpJlossPlanDto.setInsuredName(prplcMainVo == null ? "" : prplcMainVo.getInsuredName());
            prpJlossPlanDto.setStartDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getStartDate(), "yyyy-MM-dd"));
            prpJlossPlanDto.setEndDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getEndDate(), "yyyy-MM-dd"));
            prpJlossPlanDto.setUnderWriteDate(dateToString(new Date(), "yyyy-MM-dd"));
            prpJlossPlanDto.setEndCaseDate(dateToString(prplClaimVo.getEndCaseTime(), "yyyy-MM-dd HH:mm:ss"));
            prpJlossPlanDto.setCurrency(CodeConstants.Currency.CNY);
            prpJlossPlanDto.setComCode(prplClaimVo.getComCode());
            prpJlossPlanDto.setMakeCom(prplClaimVo.getComCode());

            prpJlossPlanDto.setHandler1Code(recLossVo.getCreateUser());
            SysUserVo handler1 = sysUserService.findByUserCode(recLossVo.getCreateUser());
            prpJlossPlanDto.setHandler1CodeName(handler1 == null ? "" : handler1.getUserName());
            prpJlossPlanDto.setHandlerCode(recLossVo.getUpdateUser());
            prpJlossPlanDto.setOperateCode(recLossVo.getUpdateUser());
            prpJlossPlanDto.setOperateComCode(prplClaimVo.getComCode());
            prpJlossPlanDto.setBusinessNature(prplcMainVo == null ? "" : prplcMainVo.getBusinessNature());
            prpJlossPlanDto.setBusinessplate(prplcMainVo == null ? "" : prplcMainVo.getBusinessPlate());

            if (prplcMainVo != null && prplcMainVo.getPrpCItemCars() != null) {
                for (PrpLCItemCarVo itemCarVo : prplcMainVo.getPrpCItemCars()) {
                    if (prplClaimVo.getPolicyNo() != null && prplClaimVo.getPolicyNo().equals(itemCarVo.getPolicyNo())) {
                        prpJlossPlanDto.setLicenseNo(itemCarVo.getLicenseNo());
						String carType = this.transCarType(itemCarVo);
                        prpJlossPlanDto.setCarNatureCode(carType);
                        prpJlossPlanDto.setUsenaturecode(itemCarVo.getUseNatureCode());
                        break;
                    }
                }
            }

            prpJlossPlanDto.setCoinsFlag(registVo == null ? "" : registVo.getIsGBFlag());
            if (registVo != null) {
                if (PaymentConstants.COINSFLAG_2.equals(registVo.getIsGBFlag())
                        || PaymentConstants.COINSFLAG_4.equals(registVo.getIsGBFlag())) {
                    prpJlossPlanDto.setCoinsType(PaymentConstants.COINSTYPE_1);
					String coinsCode = policyViewService.findCoinsCode(prplClaimVo.getPolicyNo());
					List<PrpLCoinsVo> ywCoinsList = compensateTaskService.findPrpLCoins(prplClaimVo.getPolicyNo());
					String coinsName = "";
					if (StringUtils.isNotBlank(coinsCode)) {
						for (PrpLCoinsVo coinsVo : ywCoinsList) {
							if (coinsCode.equals(coinsVo.getCoinsCode())) {
								coinsName = coinsVo.getCoinsName();
								break;
							}
						}
					}

					prpJlossPlanDto.setCoinsType(PaymentConstants.COINSTYPE_1);
					prpJlossPlanDto.setCoinsCode(coinsCode);
					prpJlossPlanDto.setCoinsName(coinsName);
                }
            }
            prpJlossPlanDto.setIsSimpleCase("0");
            prpJlossPlanDto.setPlanFee(BigDecimal.ZERO.compareTo(recLossVo.getRecLossFee()) > 0 ? recLossVo.getRecLossFee() : BigDecimal.ZERO.subtract(recLossVo.getRecLossFee()));
            prpJlossPlanDto.setSystemFlag(PaymentConstants.SYSTEMFLAG_CAR);
            prpJlossPlanDto.setLocationFlag(prplcMainVo == null ? "D" : prplcMainVo.getNationFlag());
            prpJlossPlanDto.setSplitRiskFlag(PaymentConstants.SPLITRISKFLAG_1);
            prpJlossPlanDto.setPayeeCurrency("CNY");
            prpJlossPlanDto.setIsAutoPay("0");
            prpJlossPlanDto.setIsExpress("0");
            prpJlossPlanDto.setYuPayFlag("0");
            prpJlossPlanDto.setUsage("???");
            prpJlossPlanDto.setPayReasonFlag("0");
            prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);
            int i = 0;
            for (PrpLRecLossDetailVo detailVo : recLossVo.getPrpLRecLossDetails()) {
                PrpJlossPlanSubDto prpJlossPlanSubDto = new PrpJlossPlanSubDto();

                prpJlossPlanSubDto.setCertiType(PaymentConstants.CERTITYPE_C);
                prpJlossPlanSubDto.setCertiNo(recLossVo.getPrpLRecLossId());
                prpJlossPlanSubDto.setPolicyNo(prplClaimVo.getPolicyNo());
                prpJlossPlanSubDto.setPayRefReason(PaymentConstants.PAYREASON_P6J);
                prpJlossPlanSubDto.setSerialNo(i);
                i++;
                prpJlossPlanSubDto.setLossType(PaymentConstants.LOSSTYPE_0);
                prpJlossPlanSubDto.setClassCode(prplcMainVo == null ? "" : prplcMainVo.getClassCode());
                prpJlossPlanSubDto.setRiskCode(prplClaimVo.getRiskCode());
                prpJlossPlanSubDto.setKindCode(detailVo.getKindCode());
                prpJlossPlanSubDto.setPlanFee(BigDecimal.ZERO.compareTo(detailVo.getRecLossFee()) > 0 ? detailVo.getRecLossFee() : BigDecimal.ZERO.subtract(detailVo.getRecLossFee()));
                prpJlossPlanSubDto.setIsTaxable("1");

                jlossPlanSubDtos.add(prpJlossPlanSubDto);
            }
            prpJlossPlanDto.setPrpJlossPlanSubDtos(jlossPlanSubDtos);
            jlossPlanDtos.add(prpJlossPlanDto);
            claim2NewPaymentDto.setPrpJlossPlanDtos(jlossPlanDtos);
        }

        return claim2NewPaymentDto;
    }

    /**
     * ??????????????????????????????
     *
     * @param assessorMainVo ?????????????????????
     * @param assessorFeeVo  ?????????????????????
     * @return ??????????????????????????????
     * @throws Exception ????????????
     */
    private Claim2NewPaymentDto packageAssessorData(PrpLAssessorMainVo assessorMainVo, PrpLAssessorFeeVo assessorFeeVo) throws Exception {
        Claim2NewPaymentDto claim2NewPaymentDto = new Claim2NewPaymentDto();
        List<PrpJlossPlanDto> jlossPlanDtos = new ArrayList<PrpJlossPlanDto>();
        List<PrpJlossPlanSubDto> jlossPlanSubDtos = new ArrayList<PrpJlossPlanSubDto>();

        PrpLClaimVo prplClaimVo = claimService.findClaimVoByClaimNo(assessorFeeVo.getClaimNo());
        PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(assessorFeeVo.getRegistNo(), assessorFeeVo.getPolicyNo());
        PrpLRegistVo registVo = registQueryService.findByRegistNo(assessorFeeVo.getRegistNo());

        PrpJlossPlanDto prpJlossPlanDto = new PrpJlossPlanDto();
        PrpJlossPlanSubDto prpJlossPlanSubDto = new PrpJlossPlanSubDto();
        prpJlossPlanDto.setCertiType(PaymentConstants.CERTITYPE_C);
        prpJlossPlanDto.setCertiNo(assessorFeeVo.getCompensateNo());
        prpJlossPlanDto.setPolicyNo(assessorFeeVo.getPolicyNo());
        prpJlossPlanDto.setRegistNo(assessorFeeVo.getRegistNo());
        // ???????????????????????? ???????????????
        prpJlossPlanDto.setSerialNo(0);
        prpJlossPlanDto.setClaimNo(assessorFeeVo.getClaimNo());
        prpJlossPlanDto.setLossType(PaymentConstants.LOSSTYPE_1);
        prpJlossPlanDto.setPayRefReason(PaymentConstants.PAYREASON_P67);
        prpJlossPlanDto.setClassCode(prplcMainVo == null ? "" : prplcMainVo.getClassCode());
        prpJlossPlanDto.setRiskCode(prplClaimVo.getRiskCode());
        prpJlossPlanDto.setAppliCode(prplcMainVo == null ? "" : prplcMainVo.getAppliCode());
        prpJlossPlanDto.setAppliName(prplcMainVo == null ? "" : prplcMainVo.getAppliName());
        prpJlossPlanDto.setInsuredCode(prplcMainVo == null ? "" : prplcMainVo.getInsuredCode());
        prpJlossPlanDto.setInsuredName(prplcMainVo == null ? "" : prplcMainVo.getInsuredName());
        prpJlossPlanDto.setStartDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getStartDate(), "yyyy-MM-dd"));
        prpJlossPlanDto.setEndDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getEndDate(), "yyyy-MM-dd"));
        prpJlossPlanDto.setUnderWriteDate(dateToString(assessorMainVo.getUnderWriteDate(), "yyyy-MM-dd"));
        prpJlossPlanDto.setEndCaseDate(dateToString(prplClaimVo.getEndCaseTime(), "yyyy-MM-dd HH:mm:ss"));
        prpJlossPlanDto.setCurrency(CodeConstants.Currency.CNY);
        prpJlossPlanDto.setComCode(assessorMainVo.getComCode());
        prpJlossPlanDto.setMakeCom(prplcMainVo == null ? registVo.getComCode() : prplcMainVo.getComCode());

        prpJlossPlanDto.setHandler1Code(assessorFeeVo.getCreateUser());
        SysUserVo handler1 = sysUserService.findByUserCode(assessorFeeVo.getCreateUser());
        prpJlossPlanDto.setHandler1CodeName(handler1 == null ? "" : handler1.getUserName());
        prpJlossPlanDto.setHandlerCode(assessorFeeVo.getUpdateUser());
        prpJlossPlanDto.setOperateCode(assessorFeeVo.getUpdateUser());
        prpJlossPlanDto.setOperateComCode(assessorMainVo.getComCode());
        prpJlossPlanDto.setBusinessNature(prplcMainVo == null ? "" : prplcMainVo.getBusinessNature());
        prpJlossPlanDto.setBusinessplate(prplcMainVo == null ? "" : prplcMainVo.getBusinessPlate());

        if (prplcMainVo != null && prplcMainVo.getPrpCItemCars() != null) {
            for (PrpLCItemCarVo itemCarVo : prplcMainVo.getPrpCItemCars()) {
                if (assessorFeeVo.getPolicyNo() != null && assessorFeeVo.getPolicyNo().equals(itemCarVo.getPolicyNo())) {
                    prpJlossPlanDto.setLicenseNo(itemCarVo.getLicenseNo());
					String carType = this.transCarType(itemCarVo);
                    prpJlossPlanDto.setCarNatureCode(carType);
                    prpJlossPlanDto.setUsenaturecode(itemCarVo.getUseNatureCode());
                    break;
                }
            }
        }

        prpJlossPlanDto.setCoinsFlag(registVo == null ? "" : registVo.getIsGBFlag());
        if (registVo != null) {
            if (PaymentConstants.COINSFLAG_2.equals(registVo.getIsGBFlag())
                    || PaymentConstants.COINSFLAG_4.equals(registVo.getIsGBFlag())) {
				String coinsCode = policyViewService.findCoinsCode(assessorFeeVo.getPolicyNo());
				List<PrpLCoinsVo> ywCoinsList = compensateTaskService.findPrpLCoins(assessorFeeVo.getPolicyNo());
				String coinsName = "";
				if (StringUtils.isNotBlank(coinsCode)) {
					for (PrpLCoinsVo coinsVo : ywCoinsList) {
						if (coinsCode.equals(coinsVo.getCoinsCode())) {
							coinsName = coinsVo.getCoinsName();
							break;
						}
					}
				}

				prpJlossPlanDto.setCoinsType(PaymentConstants.COINSTYPE_1);
				prpJlossPlanDto.setCoinsCode(coinsCode);
				prpJlossPlanDto.setCoinsName(coinsName);
            }
        }
        prpJlossPlanDto.setIsSimpleCase("0");

        prpJlossPlanDto.setPlanFee(assessorFeeVo.getAmount());
        prpJlossPlanDto.setSystemFlag(PaymentConstants.SYSTEMFLAG_CAR);
        prpJlossPlanDto.setLocationFlag(prplcMainVo == null ? "D" : prplcMainVo.getNationFlag());
        prpJlossPlanDto.setSplitRiskFlag(PaymentConstants.SPLITRISKFLAG_1);

        // ????????????????????????
        String comCode = assessorMainVo.getComCode();
        if (StringUtils.isBlank(comCode)) {
            comCode = ServiceUserUtils.getComCode();
        }
        PrpdIntermMainVo intermMainVo = managerService.findIntermById(assessorMainVo.getIntermId());
        if (intermMainVo == null) {
            intermMainVo = managerService.findIntermByCode(assessorMainVo.getIntermcode(), comCode);
        } else if (intermMainVo.getId() == null) {
            intermMainVo = managerService.findIntermByCode(assessorMainVo.getIntermcode(), comCode);
        }
        PrpdIntermBankVo bankVo = intermMainVo.getPrpdIntermBank();
        if (bankVo != null) {
            prpJlossPlanDto.setPayeeId(bankVo.getId().toString());
            prpJlossPlanDto.setPayObjectName(bankVo.getAccountName());
            prpJlossPlanDto.setBankCode(bankVo.getBankNumber());
            prpJlossPlanDto.setBankName(bankVo.getBankName());
            prpJlossPlanDto.setBankType(bankVo.getBankType());
            prpJlossPlanDto.setAccountCode(bankVo.getAccountNo());
            prpJlossPlanDto.setAccountName(bankVo.getAccountName());
            prpJlossPlanDto.setAccountType(StringUtils.isBlank(bankVo.getPublicAndPrivate()) ? "0" : bankVo.getPublicAndPrivate());
            prpJlossPlanDto.setCertificateType("99");
            prpJlossPlanDto.setCertificateCode(bankVo.getCertifyNo());
            prpJlossPlanDto.setPayeePhone(bankVo.getMobile());
            prpJlossPlanDto.setAbstractContent(assessorFeeVo.getRemark() == null ? "?????????" : assessorFeeVo.getRemark());
            prpJlossPlanDto.setBankProvinceCode(bankVo.getProvince() == null ? "" : bankVo.getProvince());
            prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);

            List<SysAreaDictVo> areas = areaDictService.findAreaCode(bankVo.getProvince());
            if (areas != null && areas.size() > 0) {
                prpJlossPlanDto.setBankProvinceName(areas.get(0).getAreaName());
            } else {
				ProvinceResponseDto responseDto = areaDictService.findProvinceDetailInfoFromBasicPlatform(bankVo.getProvince());
				if (responseDto != null && responseDto.getData() != null && responseDto.getData().size() > 0) {
					ProvinceDetailDto detailDto = responseDto.getData().get(0);
					prpJlossPlanDto.setBankProvinceName(detailDto.getProvinceName());
				}
			}
            prpJlossPlanDto.setBankCityCode(bankVo.getCity() == null ? "" : bankVo.getCity());
            areas = areaDictService.findAreaCode(bankVo.getCity());
            if (areas != null && areas.size() > 0) {
                prpJlossPlanDto.setBankCityName(areas.get(0).getAreaName());
            } else {
				CityResponseDto cityResponseDto = areaDictService.findCityDetailInfoFromBasicPlatform(bankVo.getProvince(), bankVo.getCity());
				if (cityResponseDto != null && cityResponseDto.getData() != null && cityResponseDto.getData().size() > 0) {
					CityDetailDto detailDto = cityResponseDto.getData().get(0);
					prpJlossPlanDto.setBankCityName(detailDto.getCityName());
				}
			}

            prpJlossPlanDto.setOtherRemark(bankVo.getRemark());
        } else {
            logger.info("?????????????????????IntermId???" + assessorMainVo.getIntermId() + " ????????????????????????" + assessorFeeVo.getCompensateNo() + " ????????????????????????????????????????????????????????????");
        }
        prpJlossPlanDto.setPayeeCurrency("CNY");
        prpJlossPlanDto.setMessageContent("??????????????????");
        prpJlossPlanDto.setPayReasonFlag("0");
        prpJlossPlanDto.setIsAutoPay("0");
        prpJlossPlanDto.setIsExpress("0");
        prpJlossPlanDto.setYuPayFlag("0");
        prpJlossPlanDto.setUsage("???");

        prpJlossPlanSubDto.setCertiType(PaymentConstants.CERTITYPE_C);
        prpJlossPlanSubDto.setCertiNo(assessorFeeVo.getCompensateNo());
        prpJlossPlanSubDto.setPolicyNo(assessorFeeVo.getPolicyNo());
        prpJlossPlanSubDto.setPayRefReason(PaymentConstants.PAYREASON_P67);
        prpJlossPlanSubDto.setSerialNo(0);
        prpJlossPlanSubDto.setLossType(PaymentConstants.LOSSTYPE_1);
        prpJlossPlanSubDto.setClassCode(CodeConstants.KINDCODE.KINDCODE_BZ.equals(assessorFeeVo.getKindCode()) ? "11" : "12");
        prpJlossPlanSubDto.setRiskCode(prplClaimVo.getRiskCode());
        prpJlossPlanSubDto.setKindCode(assessorFeeVo.getKindCode());
        prpJlossPlanSubDto.setPlanFee(assessorFeeVo.getAmount());
        prpJlossPlanSubDto.setChargeCode(CodeConstants.backvatFeeType("13"));
        prpJlossPlanSubDto.setIsTaxable("1");

        jlossPlanSubDtos.add(prpJlossPlanSubDto);
        prpJlossPlanDto.setPrpJlossPlanSubDtos(jlossPlanSubDtos);
        jlossPlanDtos.add(prpJlossPlanDto);
        claim2NewPaymentDto.setPrpJlossPlanDtos(jlossPlanDtos);

        return claim2NewPaymentDto;
    }

    /**
     * ??????????????????????????????
     *
     * @param checkMainVo ?????????????????????
     * @param checkFeeVo  ?????????????????????
     * @return ??????????????????????????????
     * @throws Exception ????????????
     */
    private Claim2NewPaymentDto packageCheckFeeData(PrpLAcheckMainVo checkMainVo, PrpLCheckFeeVo checkFeeVo) throws Exception {
        Claim2NewPaymentDto newPaymentDto = new Claim2NewPaymentDto();
        List<PrpJlossPlanDto> jlossPlanDtos = new ArrayList<PrpJlossPlanDto>();
        List<PrpJlossPlanSubDto> jlossPlanSubDtos = new ArrayList<PrpJlossPlanSubDto>();

        PrpLClaimVo prplClaimVo = claimService.findClaimVoByClaimNo(checkFeeVo.getClaimNo());
        PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(checkFeeVo.getRegistNo(), checkFeeVo.getPolicyNo());
        PrpLRegistVo registVo = registQueryService.findByRegistNo(checkFeeVo.getRegistNo());

        PrpJlossPlanDto prpJlossPlanDto = new PrpJlossPlanDto();
        PrpJlossPlanSubDto prpJlossPlanSubDto = new PrpJlossPlanSubDto();
        prpJlossPlanDto.setCertiType(PaymentConstants.CERTITYPE_C);
        prpJlossPlanDto.setCertiNo(checkFeeVo.getCompensateNo());
        prpJlossPlanDto.setPolicyNo(checkFeeVo.getPolicyNo());
        prpJlossPlanDto.setRegistNo(checkFeeVo.getRegistNo());
        // ???????????????????????? ???????????????
        prpJlossPlanDto.setSerialNo(0);
        prpJlossPlanDto.setClaimNo(checkFeeVo.getClaimNo());
        prpJlossPlanDto.setLossType(PaymentConstants.LOSSTYPE_1);
        prpJlossPlanDto.setPayRefReason(PaymentConstants.PAYREASON_P62);
        prpJlossPlanDto.setClassCode(prplcMainVo == null ? "" : prplcMainVo.getClassCode());
        prpJlossPlanDto.setRiskCode(prplClaimVo.getRiskCode());
        prpJlossPlanDto.setAppliCode(prplcMainVo == null ? "" : prplcMainVo.getAppliCode());
        prpJlossPlanDto.setAppliName(prplcMainVo == null ? "" : prplcMainVo.getAppliName());
        prpJlossPlanDto.setInsuredCode(prplcMainVo == null ? "" : prplcMainVo.getInsuredCode());
        prpJlossPlanDto.setInsuredName(prplcMainVo == null ? "" : prplcMainVo.getInsuredName());
        prpJlossPlanDto.setStartDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getStartDate(), "yyyy-MM-dd"));
        prpJlossPlanDto.setEndDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getEndDate(), "yyyy-MM-dd"));
        prpJlossPlanDto.setUnderWriteDate(dateToString(checkMainVo.getUnderWriteDate(), "yyyy-MM-dd"));
        prpJlossPlanDto.setEndCaseDate(dateToString(prplClaimVo.getEndCaseTime(), "yyyy-MM-dd HH:mm:ss"));
        prpJlossPlanDto.setCurrency(CodeConstants.Currency.CNY);
        prpJlossPlanDto.setComCode(checkMainVo.getComCode());
        prpJlossPlanDto.setMakeCom(prplcMainVo == null ? registVo.getComCode() : prplcMainVo.getComCode());

        prpJlossPlanDto.setHandler1Code(checkFeeVo.getCreateUser());
        SysUserVo handler1 = sysUserService.findByUserCode(checkFeeVo.getCreateUser());
        prpJlossPlanDto.setHandler1CodeName(handler1 == null ? "" : handler1.getUserName());
        prpJlossPlanDto.setHandlerCode(checkFeeVo.getUpdateUser());
        prpJlossPlanDto.setOperateCode(checkFeeVo.getUpdateUser());
        prpJlossPlanDto.setOperateComCode(checkMainVo.getComCode());
        prpJlossPlanDto.setBusinessNature(prplcMainVo == null ? "" : prplcMainVo.getBusinessNature());
        prpJlossPlanDto.setBusinessplate(prplcMainVo == null ? "" : prplcMainVo.getBusinessPlate());

        if (prplcMainVo != null && prplcMainVo.getPrpCItemCars() != null) {
            for (PrpLCItemCarVo itemCarVo : prplcMainVo.getPrpCItemCars()) {
                if (checkFeeVo.getPolicyNo() != null && checkFeeVo.getPolicyNo().equals(itemCarVo.getPolicyNo())) {
                    prpJlossPlanDto.setLicenseNo(itemCarVo.getLicenseNo());
					String carType = this.transCarType(itemCarVo);
                    prpJlossPlanDto.setCarNatureCode(carType);
                    prpJlossPlanDto.setUsenaturecode(itemCarVo.getUseNatureCode());
                    break;
                }
            }
        }

        prpJlossPlanDto.setCoinsFlag(registVo == null ? "" : registVo.getIsGBFlag());
        if (registVo != null) {
            if (PaymentConstants.COINSFLAG_2.equals(registVo.getIsGBFlag())
                    || PaymentConstants.COINSFLAG_4.equals(registVo.getIsGBFlag())) {
				String coinsCode = policyViewService.findCoinsCode(checkFeeVo.getPolicyNo());
				List<PrpLCoinsVo> ywCoinsList = compensateTaskService.findPrpLCoins(checkFeeVo.getPolicyNo());
				String coinsName = "";
				if (StringUtils.isNotBlank(coinsCode)) {
					for (PrpLCoinsVo coinsVo : ywCoinsList) {
						if (coinsCode.equals(coinsVo.getCoinsCode())) {
							coinsName = coinsVo.getCoinsName();
							break;
						}
					}
				}

				prpJlossPlanDto.setCoinsType(PaymentConstants.COINSTYPE_1);
				prpJlossPlanDto.setCoinsCode(coinsCode);
				prpJlossPlanDto.setCoinsName(coinsName);
            }
        }
        prpJlossPlanDto.setIsSimpleCase("0");

        prpJlossPlanDto.setPlanFee(checkFeeVo.getAmount());
        prpJlossPlanDto.setSystemFlag(PaymentConstants.SYSTEMFLAG_CAR);
        prpJlossPlanDto.setLocationFlag(prplcMainVo == null ? "D" : prplcMainVo.getNationFlag());
        prpJlossPlanDto.setSplitRiskFlag(PaymentConstants.SPLITRISKFLAG_1);

        PrpdCheckBankMainVo checkBankMainVo = managerService.findCheckById(checkMainVo.getCheckmId());
        if (checkBankMainVo != null && checkBankMainVo.getPrpdcheckBank() != null) {
            PrpdcheckBankVo checkBankVo = checkBankMainVo.getPrpdcheckBank();
            prpJlossPlanDto.setPayeeId(checkBankVo.getId().toString());
            prpJlossPlanDto.setPayObjectName(checkBankVo.getAccountName());
            prpJlossPlanDto.setBankCode(checkBankVo.getBankNumber());
            prpJlossPlanDto.setBankName(checkBankVo.getBankName());
            prpJlossPlanDto.setBankType(checkBankVo.getBankType());
            prpJlossPlanDto.setAccountCode(checkBankVo.getAccountNo());
            prpJlossPlanDto.setAccountName(checkBankVo.getAccountName());
            prpJlossPlanDto.setAccountType(StringUtils.isBlank(checkBankVo.getPublicAndPrivate()) ? "0" : checkBankVo.getPublicAndPrivate());
            prpJlossPlanDto.setCertificateType("99");
            prpJlossPlanDto.setCertificateCode(checkBankVo.getCertifyNo());
            prpJlossPlanDto.setPayeePhone(checkBankVo.getMobile());
            prpJlossPlanDto.setAbstractContent(checkFeeVo.getRemark() == null ? "?????????" : checkFeeVo.getRemark());
            prpJlossPlanDto.setBankProvinceCode(checkBankVo.getProvince() == null ? "" : checkBankVo.getProvince());
            prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);

			List<SysAreaDictVo> areas = areaDictService.findAreaCode(checkBankVo.getProvince());
			if (areas != null && areas.size() > 0) {
				prpJlossPlanDto.setBankProvinceName(areas.get(0).getAreaName());
			} else {
				ProvinceResponseDto responseDto = areaDictService.findProvinceDetailInfoFromBasicPlatform(checkBankVo.getProvince());
				if (responseDto != null && responseDto.getData() != null && responseDto.getData().size() > 0) {
					ProvinceDetailDto detailDto = responseDto.getData().get(0);
					prpJlossPlanDto.setBankProvinceName(detailDto.getProvinceName());
				}
			}
			prpJlossPlanDto.setBankCityCode(checkBankVo.getCity() == null ? "" : checkBankVo.getCity());
			areas = areaDictService.findAreaCode(checkBankVo.getCity());
			if (areas != null && areas.size() > 0) {
				prpJlossPlanDto.setBankCityName(areas.get(0).getAreaName());
			} else {
				CityResponseDto cityResponseDto = areaDictService.findCityDetailInfoFromBasicPlatform(checkBankVo.getProvince(), checkBankVo.getCity());
				if (cityResponseDto != null && cityResponseDto.getData() != null && cityResponseDto.getData().size() > 0) {
					CityDetailDto detailDto = cityResponseDto.getData().get(0);
					prpJlossPlanDto.setBankCityName(detailDto.getCityName());
				}
			}

            prpJlossPlanDto.setOtherRemark(checkBankVo.getRemark());
        } else {
            logger.info("?????????????????????????????????id???" + checkMainVo.getCheckmId() + " ????????????????????????" + checkFeeVo.getCompensateNo() + " ????????????????????????????????????????????????????????????");
        }

        prpJlossPlanDto.setPayeeCurrency("CNY");
        prpJlossPlanDto.setMessageContent("??????????????????");
        prpJlossPlanDto.setPayReasonFlag("0");
        prpJlossPlanDto.setIsAutoPay("0");
        prpJlossPlanDto.setIsExpress("0");
        prpJlossPlanDto.setYuPayFlag("0");
        prpJlossPlanDto.setUsage("???");

        prpJlossPlanSubDto.setCertiType(PaymentConstants.CERTITYPE_C);
        prpJlossPlanSubDto.setCertiNo(checkFeeVo.getCompensateNo());
        prpJlossPlanSubDto.setPolicyNo(checkFeeVo.getPolicyNo());
        prpJlossPlanSubDto.setPayRefReason(PaymentConstants.PAYREASON_P62);
        prpJlossPlanSubDto.setSerialNo(0);
        prpJlossPlanSubDto.setLossType(PaymentConstants.LOSSTYPE_1);
        prpJlossPlanSubDto.setClassCode(CodeConstants.KINDCODE.KINDCODE_BZ.equals(checkFeeVo.getKindCode()) ? "11" : "12");
        prpJlossPlanSubDto.setRiskCode(prplClaimVo.getRiskCode());
        prpJlossPlanSubDto.setKindCode(checkFeeVo.getKindCode());
        prpJlossPlanSubDto.setPlanFee(checkFeeVo.getAmount());
		prpJlossPlanSubDto.setChargeCode(CodeConstants.backvatFeeType("04"));
        prpJlossPlanSubDto.setIsTaxable("1");

        jlossPlanSubDtos.add(prpJlossPlanSubDto);
        prpJlossPlanDto.setPrpJlossPlanSubDtos(jlossPlanSubDtos);
        jlossPlanDtos.add(prpJlossPlanDto);
        newPaymentDto.setPrpJlossPlanDtos(jlossPlanDtos);

        return newPaymentDto;
    }

    /**
     * ???????????????????????????
     *
     * @param compensateVo ?????????
     * @return ?????????????????????
     */
    private Claim2NewPaymentDto packagePaymentInfos(PrpLCompensateVo compensateVo) throws Exception {
        Claim2NewPaymentDto claim2NewPaymentDto = new Claim2NewPaymentDto();
        List<PrpJlossPlanDto> jlossPlanDtos = new ArrayList<PrpJlossPlanDto>();
        // ??????????????????
        packageCompensateData(compensateVo, jlossPlanDtos);
        // ??????????????????
        packageChargeData(compensateVo, jlossPlanDtos);

        claim2NewPaymentDto.setPrpJlossPlanDtos(jlossPlanDtos);

        return claim2NewPaymentDto;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????
     *
     * @param prpJlossPlanDto ?????????????????????????????????
     * @param compensateVo    ?????????
     * @param paymentVo       ????????????
     */
    private void setPayRefReason(PrpJlossPlanDto prpJlossPlanDto, PrpLCompensateVo compensateVo, PrpLPaymentVo paymentVo) {
        String voucherNo2 = "";
        if (CodeConstants.PayFlagType.COMPENSATE_PAY.equals(paymentVo.getPayFlag())) {
            // ??????
            prpJlossPlanDto.setPayRefReason(PaymentConstants.PAYREASON_P60);
        } else if (CodeConstants.PayFlagType.CLEAR_PAY.equals(paymentVo.getPayFlag())) {
            // ??????
            prpJlossPlanDto.setPayRefReason(PaymentConstants.PAYREASON_P6D);
            // ???????????????
            if (StringUtils.isNotBlank(paymentVo.getItemId())) {
                voucherNo2 = getClearRecoveryCode(Integer.parseInt(paymentVo.getItemId()), compensateVo);
            }
            prpJlossPlanDto.setVoucherNo2(voucherNo2);
        } else if (CodeConstants.PayFlagType.INSTEAD_PAY.equals(paymentVo.getPayFlag())) {
            // ??????
            prpJlossPlanDto.setPayRefReason(PaymentConstants.PAYREASON_P6B);
            // ?????????????????????
            voucherNo2 = getPlatformRecoveryCode(compensateVo, CodeConstants.PayFlagType.INSTEAD_PAY);
            if (StringUtils.isBlank(voucherNo2)) {
                // ????????????????????? ???????????? ?????????platLockVo ????????????
                voucherNo2 = "???";
            }
            prpJlossPlanDto.setVoucherNo2(voucherNo2);
        }
    }

    /**
     * ?????????????????????
     *
     * @param compensateVo ?????????
     * @param payFlagType  ????????????
     * @return ?????????????????????
     */
    private String getPlatformRecoveryCode(PrpLCompensateVo compensateVo, String payFlagType) {
        String voucherNo2 = "";
        List<PrpLPlatLockVo> lockList = platLockService.findPrpLPlatLockVoList(compensateVo.getRegistNo(), compensateVo.getPolicyNo(), payFlagType);
        if (lockList != null && !lockList.isEmpty()) {
            for (PrpLPlatLockVo lock : lockList) {
                List<PrpLRecoveryOrPayVo> payVoList = lock.getPrpLRecoveryOrPays();
                if (payVoList != null && !payVoList.isEmpty()) {
                    for (PrpLRecoveryOrPayVo payVo : payVoList) {
                        if (payVo.getCompensateNo().equals(compensateVo.getCompensateNo())) {
                            voucherNo2 = payVo.getRecoveryCode();
                        }
                    }
                }
            }
        }
        return voucherNo2;
    }

    /**
     * ?????????????????????
     *
     * @param itemId
     * @param compensateVo
     * @return
     */
    private String getClearRecoveryCode(Integer itemId, PrpLCompensateVo compensateVo) {
        String voucherNo2 = "";
        PrpLCheckDutyVo dutyVo = checkTaskService.findCheckDuty(compensateVo.getRegistNo(), itemId);
        if (dutyVo != null) {
            PrpLPlatLockVo lockVo = platLockService.findPrpLPlatLockVoByLicenseNo(compensateVo.getRegistNo(),
                    compensateVo.getPolicyNo(), CodeConstants.PayFlagType.CLEAR_PAY, dutyVo.getLicenseNo());
            if (lockVo != null) {
                voucherNo2 = lockVo.getRecoveryCode();
            }
        }
        return voucherNo2;
    }

    private String dateToString(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    private void sendMessageContent(SysMsgModelVo msgModelVo, SendMsgParamVo msgParamVo, String message) {
        Date sendTime_7 = sendMsgService.getSendTime(msgModelVo.getTimeType());
        // ??????????????????
        Date trueSendTime = new Date();
        Date nowTime = new Date();
        Date sendTime1 = null;
        if (sendTime_7 != null) {
            // ????????????????????????
            sendTime1 = DateUtils.addMinutes(sendTime_7, -5);
            if (nowTime.getTime() < sendTime1.getTime()) {
                trueSendTime = sendTime1;
            }
        }

        PrpsmsMessageVo prpsmsMessageVo = null;
        if (msgParamVo != null) {
            prpsmsMessageVo = new PrpsmsMessageVo();
            prpsmsMessageVo.setBusinessNo(msgParamVo.getRegistNo());
            prpsmsMessageVo.setComCode(msgParamVo.getComCode());
            prpsmsMessageVo.setCreateTime(nowTime);
            prpsmsMessageVo.setPhoneCode(msgParamVo.getReportoMobile());
            prpsmsMessageVo.setSendNodecode(FlowNode.EndCas.toString());
            prpsmsMessageVo.setSendText(message);
            prpsmsMessageVo.setTruesendTime(trueSendTime);
            prpsmsMessageVo.setUserCode(msgParamVo.getUseCode());
            prpsmsMessageVo.setBackTime(nowTime);
            //???????????????????????????????????????????????????????????????????????????????????????????????????1
            prpsmsMessageVo.setStatus("0");
            msgModelService.saveorUpdatePrpSmsMessage(prpsmsMessageVo);
        }

    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param registVo     ????????????
     * @param compensateVo ???????????????
     * @param paymentVo    ????????????
     * @param cmainVo      ????????????
     * @param customVo     ???????????????
     * @return ??????????????????
     */
    private String handleMessageContent(PrpLRegistVo registVo, PrpLCompensateVo compensateVo, PrpLPaymentVo paymentVo, PrpLCMainVo cmainVo, PrpLPayCustomVo customVo) {
        String message = "";
        try {
            SysMsgModelVo msgModelVo = findSysMsgModelVo(registVo, CodeConstants.CompensateFlag.compensate);
            if (msgModelVo != null) {
                SendMsgParamVo msgParamVo = getMsgParamVo(registVo, paymentVo, customVo, cmainVo);
                message = sendMsgService.getMessage(msgModelVo.getContent(), msgParamVo);
                // ????????????
                sendMessageContent(msgModelVo, msgParamVo, message);
            }
        } catch (Exception e) {
            logger.info("????????????" + compensateVo.getRegistNo() + " ???????????????" + compensateVo.getCompensateNo() + " ???????????????????????????", e);
        }

        return message;
    }

    /**
     * ????????????????????????
     *
     * @param registVo    ????????????
     * @param paymentVo   ????????????
     * @param customVo    ???????????????
     * @param prpLCMainVo ????????????
     * @return ??????????????????
     */
    private SendMsgParamVo getMsgParamVo(PrpLRegistVo registVo, PrpLPaymentVo paymentVo, PrpLPayCustomVo customVo, PrpLCMainVo prpLCMainVo) {
        SendMsgParamVo msgParamVo = new SendMsgParamVo();
        if (prpLCMainVo != null) {
            List<PrpLCItemKindVo> itemKindVos = prpLCMainVo.getPrpCItemKinds();
            msgParamVo.setPrpCItemKinds(itemKindVos);
        }
        msgParamVo.setRegistNo(registVo.getRegistNo());
        msgParamVo.setLicenseNo(registVo.getPrpLRegistExt().getLicenseNo());
        msgParamVo.setDamageTime(registVo.getDamageTime());
        msgParamVo.setSumAmt(paymentVo.getSumRealPay() == null ? "" : paymentVo.getSumRealPay().toString());
        msgParamVo.setLinkerName(customVo.getPayeeName());
        msgParamVo.setComCode(registVo.getComCode());
        msgParamVo.setReportoMobile(registVo.getReportorPhone());
        msgParamVo.setUseCode(registVo.getCreateUser());
        return msgParamVo;
    }

    /**
     * ????????????????????????  0-??????????????? 1-????????????
     *
     * @param compensateVo ???????????????
     * @param prplcMainVo  ????????????
     * @return ?????????????????????
     */
    private String getIsAutoPay(PrpLCompensateVo compensateVo, PrpLCMainVo prplcMainVo) {
        // ?????????????????????????????????????????????????????????
        String isAutoPay = "1";
        Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("registNo", compensateVo.getRegistNo());
        queryMap.put("claimNo", compensateVo.getClaimNo());
        // ????????????
        queryMap.put("checkStatus", "6");
        // ????????????????????????????????? ???????????????
        List<PrpLReCaseVo> prpLReCaseVoList = endCasePubService.findReCaseVoListByqueryMap(queryMap);
        if (prpLReCaseVoList != null && prpLReCaseVoList.size() > 0) {
            isAutoPay = "0";
        }

        if ("0".equals(isAutoPay) || "2".equals(prplcMainVo.getCoinsFlag()) || "4".equals(prplcMainVo.getCoinsFlag())) {
            isAutoPay = "0";
        } else {
        	BigDecimal allSumRealPay = BigDecimal.ZERO;
        	for (PrpLPaymentVo paymentVo : compensateVo.getPrpLPayments()) {
				if (null != paymentVo.getSumRealPay()) {
					allSumRealPay = allSumRealPay.add(paymentVo.getSumRealPay());
				}
			}
        	boolean isMore5W = allSumRealPay.compareTo(new BigDecimal("50000.00")) > 0;
            isAutoPay = compensateVo.getPrpLCompensateExt() == null ? "0" : compensateVo.getPrpLCompensateExt().getIsAutoPay();
            if (isAutoPay == null) {
                isAutoPay = "0";
            } else {
            	if (isMore5W) {
					isAutoPay = "0";
				}
			}
        }

        return isAutoPay;
    }

    /**
     * ??????????????????
     *
     * @param compensateVo ?????????????????????????????????????????????
     * @throws Exception ????????????????????????
     */
    private void packageCompensateData(PrpLCompensateVo compensateVo, List<PrpJlossPlanDto> jlossPlanDtolist) throws Exception {
        if (compensateVo != null) {
                PrpLClaimVo prpLClaimVo = claimService.findClaimVoByClaimNo(compensateVo.getClaimNo());
                PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(compensateVo.getRegistNo(), compensateVo.getPolicyNo());
                PrpLRegistVo registVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());
                List<PrpLReCaseVo> reCaseVo = reOpenCaseService.findReCaseByClaimNo(compensateVo.getClaimNo());
                String repayType = reCaseVo != null && reCaseVo.size() > 0 ? "1" : "0";
				String isAutoPay = getIsAutoPay(compensateVo, prplcMainVo);

                // 2. ???????????????????????????????????????????????????????????????????????????????????????????????????
                for (PrpLPaymentVo paymentVo : compensateVo.getPrpLPayments()) {
                    // ??????????????????0????????????
                    if (paymentVo.getSumRealPay() != null && BigDecimal.ZERO.compareTo(paymentVo.getSumRealPay()) != 0) {
                        PrpJlossPlanDto prpJlossPlanDto = new PrpJlossPlanDto();
                        prpJlossPlanDto.setPayeeId(paymentVo.getPayeeId() == null ? "" : paymentVo.getPayeeId().toString());
                        prpJlossPlanDto.setCertiType(PaymentConstants.CERTITYPE_C);
                        prpJlossPlanDto.setCertiNo(compensateVo.getCompensateNo());
                        prpJlossPlanDto.setPolicyNo(compensateVo.getPolicyNo());
                        prpJlossPlanDto.setRegistNo(compensateVo.getRegistNo());
                        prpJlossPlanDto.setSerialNo(Integer.parseInt(paymentVo.getSerialNo()));
                        prpJlossPlanDto.setClaimNo(compensateVo.getClaimNo());
                        prpJlossPlanDto.setLossType(PaymentConstants.LOSSTYPE_0);
                        // ?????????????????????????????????????????????????????????
                        setPayRefReason(prpJlossPlanDto, compensateVo, paymentVo);
                        prpJlossPlanDto.setClassCode(prplcMainVo == null ? "" : prplcMainVo.getClassCode());
                        prpJlossPlanDto.setRiskCode(compensateVo.getRiskCode());
                        prpJlossPlanDto.setAppliCode(prplcMainVo == null ? "" : prplcMainVo.getAppliCode());
                        prpJlossPlanDto.setAppliName(prplcMainVo == null ? "" : prplcMainVo.getAppliName());
                        prpJlossPlanDto.setInsuredCode(prplcMainVo == null ? "" : prplcMainVo.getInsuredCode());
                        prpJlossPlanDto.setInsuredName(prplcMainVo == null ? "" : prplcMainVo.getInsuredName());
                        prpJlossPlanDto.setStartDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getStartDate(), "yyyy-MM-dd"));
                        prpJlossPlanDto.setEndDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getEndDate(), "yyyy-MM-dd"));
                        prpJlossPlanDto.setUnderWriteDate(dateToString(compensateVo.getUnderwriteDate(), "yyyy-MM-dd"));
                        prpJlossPlanDto.setEndCaseDate(prpLClaimVo == null ? "" : dateToString(prpLClaimVo.getEndCaseTime(), "yyyy-MM-dd HH:mm:ss"));
                        prpJlossPlanDto.setCurrency(CodeConstants.Currency.CNY);
                        prpJlossPlanDto.setComCode(compensateVo.getComCode());
                        prpJlossPlanDto.setMakeCom(prplcMainVo == null ? registVo.getComCode() : prplcMainVo.getComCode());

                        prpJlossPlanDto.setHandler1Code(compensateVo.getCreateUser());
                        SysUserVo handler1 = sysUserService.findByUserCode(compensateVo.getCreateUser());
                        prpJlossPlanDto.setHandler1CodeName(handler1 == null ? "" : handler1.getUserName());
                        prpJlossPlanDto.setHandlerCode(compensateVo.getHandler1Code());
                        prpJlossPlanDto.setOperateCode(compensateVo.getHandler1Code());
                        prpJlossPlanDto.setOperateComCode(compensateVo.getComCode());
                        prpJlossPlanDto.setBusinessNature(prplcMainVo == null ? "" : prplcMainVo.getBusinessNature());
                        prpJlossPlanDto.setBusinessplate(prplcMainVo == null ? "" : prplcMainVo.getBusinessPlate());

                        if (prplcMainVo != null && prplcMainVo.getPrpCItemCars() != null) {
                            for (PrpLCItemCarVo itemCarVo : prplcMainVo.getPrpCItemCars()) {
                                if (compensateVo.getPolicyNo() != null && compensateVo.getPolicyNo().equals(itemCarVo.getPolicyNo())) {
                                    prpJlossPlanDto.setLicenseNo(itemCarVo.getLicenseNo());
									String carType = this.transCarType(itemCarVo);
                                    prpJlossPlanDto.setCarNatureCode(carType);
                                    prpJlossPlanDto.setUsenaturecode(itemCarVo.getUseNatureCode());
                                    break;
                                }
                            }
                        }

                        prpJlossPlanDto.setCoinsFlag(registVo == null ? "" : registVo.getIsGBFlag());
                        if (registVo != null) {
                            if (PaymentConstants.COINSFLAG_2.equals(registVo.getIsGBFlag())
                                    || PaymentConstants.COINSFLAG_4.equals(registVo.getIsGBFlag())) {
								String coinsCode = policyViewService.findCoinsCode(compensateVo.getPolicyNo());
								List<PrpLCoinsVo> ywCoinsList = compensateTaskService.findPrpLCoins(compensateVo.getPolicyNo());
								String coinsName = "";
								if (StringUtils.isNotBlank(coinsCode)) {
									for (PrpLCoinsVo coinsVo : ywCoinsList) {
										if (coinsCode.equals(coinsVo.getCoinsCode())) {
											coinsName = coinsVo.getCoinsName();
											break;
										}
									}
								}

								prpJlossPlanDto.setCoinsType(PaymentConstants.COINSTYPE_1);
								prpJlossPlanDto.setCoinsCode(coinsCode);
								prpJlossPlanDto.setCoinsName(coinsName);
								prpJlossPlanDto.setPayObjectCode(coinsCode);
								prpJlossPlanDto.setPayObjectName(coinsName);
                            }
                        }
                        String isSimpleCase = compensateVo.getPrpLCompensateExt() == null ? "0" : compensateVo.getPrpLCompensateExt().getIsFastReparation();
                        prpJlossPlanDto.setIsSimpleCase(isSimpleCase == null ? "0" : isSimpleCase);
                        prpJlossPlanDto.setPlanFee(paymentVo.getSumRealPay());
                        prpJlossPlanDto.setSystemFlag(PaymentConstants.SYSTEMFLAG_CAR);
                        prpJlossPlanDto.setLocationFlag(prplcMainVo == null ? "D" : prplcMainVo.getNationFlag());
                        prpJlossPlanDto.setSplitRiskFlag(PaymentConstants.SPLITRISKFLAG_1);

                        // ????????????????????????
                        PrpLPayCustomVo custom = managerService.findPayCustomVoById(paymentVo.getPayeeId());
                        if (custom != null) {
                            prpJlossPlanDto.setPayObjectName(custom.getPayeeName());
                            prpJlossPlanDto.setBankCode(custom.getBankNo());
                            prpJlossPlanDto.setBankName(custom.getBankName());
                            prpJlossPlanDto.setBankType(custom.getBankType());
                            prpJlossPlanDto.setAccountCode(custom.getAccountNo());
                            prpJlossPlanDto.setAccountName(custom.getPayeeName());
                            prpJlossPlanDto.setAccountType(custom.getPublicAndPrivate());
                            prpJlossPlanDto.setCertificateType(custom.getCertifyType());
                            prpJlossPlanDto.setCertificateCode(custom.getCertifyNo());
                            prpJlossPlanDto.setPayeePhone(custom.getPayeeMobile());
                            prpJlossPlanDto.setAbstractContent(paymentVo.getSummary() == null ? "" : paymentVo.getSummary());
                            prpJlossPlanDto.setBankProvinceCode(custom.getProvinceCode() == null ? "" : custom.getProvinceCode().toString());
                            prpJlossPlanDto.setBankProvinceName(custom.getProvince());
                            prpJlossPlanDto.setBankCityCode(custom.getCityCode() == null ? "" : custom.getCityCode().toString());
                            prpJlossPlanDto.setBankCityName(custom.getCity());
                            prpJlossPlanDto.setUsage(StringUtils.isBlank(custom.getPurpose()) ? "???" : custom.getPurpose());
                            prpJlossPlanDto.setOtherRemark(custom.getRemark());
                            prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);
                        } else {
                            logger.info("????????????id???" + paymentVo.getPayeeId() + " ???????????????" + compensateVo.getCompensateNo() + " paycustom?????????????????????????????????");
                        }
                        prpJlossPlanDto.setPayeeCurrency("CNY");
                        prpJlossPlanDto.setMessageContent("???");
                        // ??????????????????
                        if ("1".equals(isAutoPay)) {
                            String message = handleMessageContent(registVo, compensateVo, paymentVo, prplcMainVo, custom);
                            prpJlossPlanDto.setMessageContent(message);
                        }
                        prpJlossPlanDto.setPayReasonFlag(StringUtils.isBlank(paymentVo.getOtherFlag()) ? "0" : "1");
                        prpJlossPlanDto.setIsAutoPay(isAutoPay);
                        prpJlossPlanDto.setIsExpress("0");
                        prpJlossPlanDto.setRepayType(repayType);
                        prpJlossPlanDto.setYuPayFlag(getYuPayFlagByClaim(compensateVo.getClaimNo()));

                        List<PrpJlossPlanSubDto> jlossPlanSubList = new ArrayList<PrpJlossPlanSubDto>();
                        jlossPlanSubList = getCompensateDetail(compensateVo, paymentVo);
                        prpJlossPlanDto.setPrpJlossPlanSubDtos(jlossPlanSubList);
                        jlossPlanDtolist.add(prpJlossPlanDto);
                    }
                }
        }
    }

    /**
     * ????????????????????????
     *
     * @param compensateVo     ???????????????
     * @param jlossPlanDtolist ????????????????????????
     */
    private void packageChargeData(PrpLCompensateVo compensateVo, List<PrpJlossPlanDto> jlossPlanDtolist) {
        if (compensateVo != null) {
                // ??????????????????
                List<PrpLChargeVo> chargeVos = compensateVo.getPrpLCharges();
                PrpLClaimVo prpLClaimVo = claimService.findClaimVoByClaimNo(compensateVo.getClaimNo());
                PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(compensateVo.getRegistNo(), compensateVo.getPolicyNo());
                PrpLRegistVo registVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());
                List<PrpLReCaseVo> reCaseVo = reOpenCaseService.findReCaseByClaimNo(compensateVo.getClaimNo());
                String repayType = reCaseVo != null && reCaseVo.size() > 0 ? "1" : "0";

                for (PrpLChargeVo chargeVo : chargeVos) {

                    PrpJlossPlanDto prpJlossPlanDto = new PrpJlossPlanDto();
                    PrpJlossPlanSubDto prpJlossPlanSubDto = new PrpJlossPlanSubDto();
                    prpJlossPlanDto.setPayeeId(chargeVo.getPayeeId() == null ? "" : chargeVo.getPayeeId().toString());
                    prpJlossPlanDto.setCertiType(PaymentConstants.CERTITYPE_C);
                    prpJlossPlanDto.setCertiNo(compensateVo.getCompensateNo());
                    prpJlossPlanDto.setPolicyNo(compensateVo.getPolicyNo());
                    prpJlossPlanDto.setRegistNo(compensateVo.getRegistNo());
                    prpJlossPlanDto.setSerialNo(Integer.parseInt(chargeVo.getSerialNo()));
                    prpJlossPlanDto.setClaimNo(compensateVo.getClaimNo());
                    prpJlossPlanDto.setLossType(PaymentConstants.LOSSTYPE_1);
                    SysCodeDictVo dictVo = codeTranService.findTransCodeDictVo("ChargeCode", chargeVo.getChargeCode());
                    prpJlossPlanDto.setPayRefReason(dictVo == null ? "" : dictVo.getProperty2());
                    prpJlossPlanDto.setClassCode(prplcMainVo == null ? "" : prplcMainVo.getClassCode());
                    prpJlossPlanDto.setRiskCode(compensateVo.getRiskCode());
                    prpJlossPlanDto.setAppliCode(prplcMainVo == null ? "" : prplcMainVo.getAppliCode());
                    prpJlossPlanDto.setAppliName(prplcMainVo == null ? "" : prplcMainVo.getAppliName());
                    prpJlossPlanDto.setInsuredCode(prplcMainVo == null ? "" : prplcMainVo.getInsuredCode());
                    prpJlossPlanDto.setInsuredName(prplcMainVo == null ? "" : prplcMainVo.getInsuredName());
                    prpJlossPlanDto.setStartDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getStartDate(), "yyyy-MM-dd"));
                    prpJlossPlanDto.setEndDate(prplcMainVo == null ? "" : dateToString(prplcMainVo.getEndDate(), "yyyy-MM-dd"));
                    prpJlossPlanDto.setUnderWriteDate(dateToString(compensateVo.getUnderwriteDate(), "yyyy-MM-dd"));
                    prpJlossPlanDto.setEndCaseDate(prpLClaimVo == null ? "" : dateToString(prpLClaimVo.getEndCaseTime(), "yyyy-MM-dd HH:mm:ss"));
                    prpJlossPlanDto.setCurrency(CodeConstants.Currency.CNY);
                    prpJlossPlanDto.setComCode(compensateVo.getComCode());
                    prpJlossPlanDto.setMakeCom(prplcMainVo == null ? compensateVo.getComCode() : prplcMainVo.getComCode());

                    prpJlossPlanDto.setHandler1Code(compensateVo.getCreateUser());
                    SysUserVo handler1 = sysUserService.findByUserCode(compensateVo.getCreateUser());
                    prpJlossPlanDto.setHandler1CodeName(handler1 == null ? "" : handler1.getUserName());
                    prpJlossPlanDto.setHandlerCode(compensateVo.getHandler1Code());
                    prpJlossPlanDto.setOperateCode(compensateVo.getHandler1Code());
                    prpJlossPlanDto.setOperateComCode(compensateVo.getComCode());
                    prpJlossPlanDto.setBusinessNature(prplcMainVo == null ? "" : prplcMainVo.getBusinessNature());
                    prpJlossPlanDto.setBusinessplate(prplcMainVo == null ? "" : prplcMainVo.getBusinessPlate());

                    if (prplcMainVo != null && prplcMainVo.getPrpCItemCars() != null) {
                        for (PrpLCItemCarVo itemCarVo : prplcMainVo.getPrpCItemCars()) {
                            if (compensateVo.getPolicyNo() != null && compensateVo.getPolicyNo().equals(itemCarVo.getPolicyNo())) {
                                prpJlossPlanDto.setLicenseNo(itemCarVo.getLicenseNo());
								String carType = this.transCarType(itemCarVo);
                                prpJlossPlanDto.setCarNatureCode(carType);
                                prpJlossPlanDto.setUsenaturecode(itemCarVo.getUseNatureCode());
                                break;
                            }
                        }
                    }

                    prpJlossPlanDto.setCoinsFlag(registVo == null ? "" : registVo.getIsGBFlag());
                    if (registVo != null) {
                        if (PaymentConstants.COINSFLAG_2.equals(registVo.getIsGBFlag())
                                || PaymentConstants.COINSFLAG_4.equals(registVo.getIsGBFlag())) {
							String coinsCode = policyViewService.findCoinsCode(compensateVo.getPolicyNo());
							List<PrpLCoinsVo> ywCoinsList = compensateTaskService.findPrpLCoins(compensateVo.getPolicyNo());
							String coinsName = "";
							if (StringUtils.isNotBlank(coinsCode)) {
								for (PrpLCoinsVo coinsVo : ywCoinsList) {
									if (coinsCode.equals(coinsVo.getCoinsCode())) {
										coinsName = coinsVo.getCoinsName();
										break;
									}
								}
							}

							prpJlossPlanDto.setCoinsType(PaymentConstants.COINSTYPE_1);
							prpJlossPlanDto.setCoinsCode(coinsCode);
							prpJlossPlanDto.setCoinsName(coinsName);
							prpJlossPlanDto.setPayObjectName(coinsName);
							prpJlossPlanDto.setPayObjectCode(coinsCode);
                        }
                    }
                    prpJlossPlanDto.setPlanFee(DataUtils.NullToZero(chargeVo.getFeeRealAmt()));
                    prpJlossPlanDto.setSystemFlag(PaymentConstants.SYSTEMFLAG_CAR);
                    prpJlossPlanDto.setLocationFlag(prplcMainVo == null ? "D" : prplcMainVo.getNationFlag());
                    prpJlossPlanDto.setSplitRiskFlag(PaymentConstants.SPLITRISKFLAG_1);
                    prpJlossPlanDto.setPayeeCurrency("CNY");

                    // ????????????????????????
                    PrpLPayCustomVo custom = managerService.findPayCustomVoById(chargeVo.getPayeeId());
                    if (custom != null) {
                        prpJlossPlanDto.setPayObjectName(custom.getPayeeName());
                        prpJlossPlanDto.setBankCode(custom.getBankNo());
                        prpJlossPlanDto.setBankName(custom.getBankName());
                        prpJlossPlanDto.setBankType(custom.getBankType());
                        prpJlossPlanDto.setAccountCode(custom.getAccountNo());
                        prpJlossPlanDto.setAccountName(custom.getPayeeName());
                        prpJlossPlanDto.setAccountType(custom.getPublicAndPrivate());
                        prpJlossPlanDto.setCertificateType(custom.getCertifyType());
                        prpJlossPlanDto.setCertificateCode(custom.getCertifyNo());
                        prpJlossPlanDto.setPayeePhone(custom.getPayeeMobile());
                        prpJlossPlanDto.setAbstractContent(chargeVo.getSummary() == null ? "" : chargeVo.getSummary());
                        prpJlossPlanDto.setBankProvinceCode(custom.getProvinceCode() == null ? "" : custom.getProvinceCode().toString());
                        prpJlossPlanDto.setBankProvinceName(custom.getProvince());
                        prpJlossPlanDto.setBankCityCode(custom.getCityCode() == null ? "" : custom.getCityCode().toString());
                        prpJlossPlanDto.setBankCityName(custom.getCity());
                        prpJlossPlanDto.setUsage(StringUtils.isBlank(custom.getPurpose()) ? "???" : custom.getPurpose());
                        prpJlossPlanDto.setOtherRemark(custom.getRemark());
                        prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);
                    } else {
                        logger.info("????????????id???" + chargeVo.getPayeeId() + " ???????????????" + compensateVo.getCompensateNo() + " paycustom?????????????????????????????????");
                    }

                    prpJlossPlanDto.setPayReasonFlag("0");
                    prpJlossPlanDto.setMessageContent("???");
                    // ??????????????????
                    prpJlossPlanDto.setMessageContent("????????????");
                    prpJlossPlanDto.setIsAutoPay("0");
					prpJlossPlanDto.setIsSimpleCase("0");
                    prpJlossPlanDto.setIsExpress("0");
                    prpJlossPlanDto.setRepayType(repayType);
                    prpJlossPlanDto.setYuPayFlag(getYuPayFlagByClaim(compensateVo.getClaimNo()));

                    prpJlossPlanSubDto.setCertiType(PaymentConstants.CERTITYPE_C);
                    prpJlossPlanSubDto.setCertiNo(compensateVo.getCompensateNo());
                    prpJlossPlanSubDto.setPolicyNo(compensateVo.getPolicyNo());
                    prpJlossPlanSubDto.setPayRefReason(dictVo == null ? "" : dictVo.getProperty2());
                    prpJlossPlanSubDto.setSerialNo(Integer.parseInt(chargeVo.getSerialNo()));
                    prpJlossPlanSubDto.setLossType(PaymentConstants.LOSSTYPE_1);
                    prpJlossPlanSubDto.setClassCode(prplcMainVo == null ? "" : prplcMainVo.getClassCode());
                    prpJlossPlanSubDto.setRiskCode(compensateVo.getRiskCode());
                    prpJlossPlanSubDto.setKindCode(chargeVo.getKindCode());
                    prpJlossPlanSubDto.setPlanFee(DataUtils.NullToZero(chargeVo.getFeeRealAmt()));
                    prpJlossPlanSubDto.setChargeCode(CodeConstants.backvatFeeType(chargeVo.getChargeCode()));
                    prpJlossPlanSubDto.setIsTaxable("1");

                    List<PrpJlossPlanSubDto> subDtos = new ArrayList<PrpJlossPlanSubDto>();
                    subDtos.add(prpJlossPlanSubDto);

                    prpJlossPlanDto.setPrpJlossPlanSubDtos(subDtos);
                    jlossPlanDtolist.add(prpJlossPlanDto);
                }
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param compensateVo ???????????????
     * @param paymentVo    ???????????????
     * @return ???????????????????????????????????????
     */
    private List<PrpJlossPlanSubDto> getCompensateDetail(PrpLCompensateVo compensateVo, PrpLPaymentVo paymentVo) {
        List<PrpLLossItemVo> lossItemVos = compensateVo.getPrpLLossItems();
        List<PrpLLossPropVo> lossPropVos = compensateVo.getPrpLLossProps();
        List<PrpLLossPersonVo> lossPersonVos = compensateVo.getPrpLLossPersons();

        Map<String, BigDecimal> kindPaidMap = new HashMap<String, BigDecimal>();
        // 1 ?????? 2??????
        Map<String, BigDecimal> dwPaidMap = new HashMap<String, BigDecimal>();
        // ?????????????????????????????????????????????
        BigDecimal allKindSumRealPay = BigDecimal.ZERO;
        // ????????????????????????
        if (lossItemVos != null && lossItemVos.size() > 0) {
            for (PrpLLossItemVo itemVo : lossItemVos) {
                if (BigDecimal.ZERO.compareTo(itemVo.getSumRealPay()) == 0) {
                    // ???????????????0?????????????????????
                    continue;
                }
                BigDecimal realPay = itemVo.getSumRealPay().subtract(DataUtils.NullToZero(itemVo.getOffPreAmt()));
                if (CodeConstants.PayFlagType.INSTEAD_PAY.equals(itemVo.getPayFlag())
                        || CodeConstants.PayFlagType.CLEAR_PAY.equals(itemVo.getPayFlag())) {
                    if (dwPaidMap.containsKey(itemVo.getPayFlag())) {
                        BigDecimal curdwSumPaid = dwPaidMap.get(itemVo.getPayFlag());
                        dwPaidMap.put(itemVo.getPayFlag(), curdwSumPaid.add(realPay));
                    } else {
                        dwPaidMap.put(itemVo.getPayFlag(), realPay);
                    }
                } else {
                    if (kindPaidMap.containsKey(itemVo.getKindCode())) {
                        BigDecimal curSumKindPaid = kindPaidMap.get(itemVo.getKindCode());
                        kindPaidMap.put(itemVo.getKindCode(), curSumKindPaid.add(realPay));
                    } else {
                        kindPaidMap.put(itemVo.getKindCode(), realPay);
                    }
                    allKindSumRealPay = allKindSumRealPay.add(realPay);
                }

            }
        }

        // ????????????????????????
        if (lossPropVos != null && lossPropVos.size() > 0) {
            for (PrpLLossPropVo lossPropVo : lossPropVos) {
                if (BigDecimal.ZERO.compareTo(lossPropVo.getSumRealPay()) == 0) {
                    continue;
                }
                BigDecimal realPay = lossPropVo.getSumRealPay().subtract(DataUtils.NullToZero(lossPropVo.getOffPreAmt()));
                allKindSumRealPay = allKindSumRealPay.add(realPay);
                if (kindPaidMap.containsKey(lossPropVo.getKindCode())) {
                    BigDecimal curSumKindPaid = kindPaidMap.get(lossPropVo.getKindCode());
                    kindPaidMap.put(lossPropVo.getKindCode(), curSumKindPaid.add(realPay));
                } else {
                    kindPaidMap.put(lossPropVo.getKindCode(), realPay);
                }
            }
        }

        // ????????????????????????
        if (lossPersonVos != null && lossPersonVos.size() > 0) {
            for (PrpLLossPersonVo lossPersonVo : lossPersonVos) {
                if (BigDecimal.ZERO.compareTo(lossPersonVo.getSumRealPay()) == 0) {
                    continue;
                }
                BigDecimal realPay = lossPersonVo.getSumRealPay().subtract(DataUtils.NullToZero(lossPersonVo.getOffPreAmt()));
                allKindSumRealPay = allKindSumRealPay.add(realPay);
                if (kindPaidMap.containsKey(lossPersonVo.getKindCode())) {
                    BigDecimal curSumKindPaid = kindPaidMap.get(lossPersonVo.getKindCode());
                    kindPaidMap.put(lossPersonVo.getKindCode(), curSumKindPaid.add(realPay));
                } else {
                    kindPaidMap.put(lossPersonVo.getKindCode(), realPay);
                }
            }
        }

        List<PrpJlossPlanSubDto> subDtos = new ArrayList<PrpJlossPlanSubDto>();
        int lastIndex = 1;
        BigDecimal addupKindCodeRealPay = BigDecimal.ZERO;
        for (Map.Entry<String, BigDecimal> entry : kindPaidMap.entrySet()) {
            // ??????????????????
            PrpJlossPlanSubDto subDto = new PrpJlossPlanSubDto();
            subDto.setCertiType(PaymentConstants.CERTITYPE_C);
            subDto.setCertiNo(compensateVo.getCompensateNo());
            subDto.setPolicyNo(compensateVo.getPolicyNo());
            subDto.setSerialNo(Integer.parseInt(paymentVo.getSerialNo()));
            subDto.setLossType(PaymentConstants.LOSSTYPE_0);
            subDto.setRiskCode(compensateVo.getRiskCode());
            subDto.setPayRefReason(PaymentConstants.PAYREASON_P60);
            subDto.setIsTaxable("1");
            if (compensateVo.getRiskCode() != null && compensateVo.getRiskCode().length() > 1) {
                subDto.setClassCode(compensateVo.getRiskCode().substring(0, 2));
            }

            String kindCode = entry.getKey();
            subDto.setKindCode(kindCode);
            BigDecimal sumRealPay = paymentVo.getSumRealPay();
            if (lastIndex != kindPaidMap.size()) {
                BigDecimal curkindCodeSumPay = entry.getValue();
                BigDecimal curKindCodeRealPay = sumRealPay.multiply(curkindCodeSumPay).divide(allKindSumRealPay, 2, BigDecimal.ROUND_HALF_UP);
                subDto.setPlanFee(curKindCodeRealPay);
                addupKindCodeRealPay = addupKindCodeRealPay.add(curKindCodeRealPay);
            } else {
                subDto.setPlanFee(sumRealPay.subtract(addupKindCodeRealPay));
            }
            subDto.setChargeCode(CodeConstants.VatFeeType.CPAY);

            subDtos.add(subDto);
            lastIndex++;
        }

        return subDtos;
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param compensateVo ???????????????
     */
    private void updateClaimStatus(PrpLCompensateVo compensateVo,List<PayRefNoDto> payRefNoDtos) throws Exception {
        // ??????????????????????????????PAYSTATUS?????????2-?????????????????????
		Map<String,String> mapparam = new HashMap<String, String>();
		if(payRefNoDtos != null && payRefNoDtos.size()>0){
			for (PayRefNoDto payRefNoDto : payRefNoDtos) {
				String serialNo = payRefNoDto.getSerialNo();
				String settleNo = payRefNoDto.getPayRefNo();
				mapparam.put(serialNo,settleNo);
			}
		}
        if (compensateVo.getPrpLPayments() != null && compensateVo.getPrpLPayments().size() > 0) {
            for (PrpLPaymentVo paymentVo : compensateVo.getPrpLPayments()) {
                if (BigDecimal.ZERO.compareTo(paymentVo.getSumRealPay()) != 0) {
                    paymentVo.setPayStatus("2");
                    String settleNo = mapparam.get(paymentVo.getSerialNo());
                    if(settleNo != null && settleNo != ""){
						paymentVo.setSettleNo(settleNo);
					}
                    compensateService.updatePrpLPaymentVo(paymentVo);
                    this.savePrplPayHis(compensateVo.getClaimNo(), compensateVo.getCompensateNo(), paymentVo.getId(), "2", "P");
                } else {
                    this.savePrplPayHis(compensateVo.getClaimNo(), compensateVo.getCompensateNo(), paymentVo.getId(), "0", "P");
                }
            }
        }
        if (compensateVo.getPrpLCharges() != null && compensateVo.getPrpLCharges().size() > 0) {
            for (PrpLChargeVo chargeVo : compensateVo.getPrpLCharges()) {
                if (BigDecimal.ZERO.compareTo(chargeVo.getFeeRealAmt()) < 0) {
                    chargeVo.setPayStatus("2");
                    compensateService.updatePrpLCharges(chargeVo);
                    this.savePrplPayHis(compensateVo.getClaimNo(), compensateVo.getCompensateNo(), chargeVo.getId(), "2", "F");
                } else {
                    this.savePrplPayHis(compensateVo.getClaimNo(), compensateVo.getCompensateNo(), chargeVo.getId(), "0", "F");
                }
            }
        }
    }

    private void savePrplPayHis(String claimNo, String compensateNo, Long id, String flags, String hisType) {
        //????????????????????????
        Date inputTime = new Date();
        compensateService.savePrplPayHis(claimNo, compensateNo, id, flags, hisType, inputTime);
    }

    /**
     * ?????????????????????????????????
     *
     * @param ClaimNo ?????????
     * @return ????????????
     */
    private String getYuPayFlagByClaim(String ClaimNo) {
        List<PrpLCompensateVo> comListVo = new ArrayList<PrpLCompensateVo>();
        boolean existsPrepay = false;
        if (!StringUtils.isBlank(ClaimNo)) {
            QueryRule qr = QueryRule.getInstance();
            qr.addEqual("claimNo", ClaimNo);
            // ????????????
            qr.addEqual("underwriteFlag", "1");
            qr.addEqual("compensateType", "Y");
            List<PrpLCompensate> compPoList = databaseDao.findAll(PrpLCompensate.class, qr);
            if (compPoList.size() > 0) {
                existsPrepay = true;
            }
        }

        List<PrpLReCaseVo> prpLReCaseVoList = reOpenCaseService.findReCaseByClaimNo(ClaimNo);
        boolean existReCase = false;
        if (prpLReCaseVoList != null && prpLReCaseVoList.size() > 0) {
            existReCase = true;
        }

        return (existsPrepay && !existReCase) ? "1" : "0";
    }

    /**
     * ?????????????????????????????????
     *
     * @param requestData  ?????????????????????
     * @param businessType ??????
     * @param url          ??????????????????
     */
    private ClaimInterfaceLogVo packageInterfaceLog(String requestData, BusinessType businessType, String url, String compensateNo, String registNo) throws Exception {
        ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo();

        if (BusinessType.Payment_compe.equals(businessType) || BusinessType.Payment_prePay.equals(businessType)) {
            PrpLCompensateVo compensateVo = compensateService.findCompByPK(compensateNo);
            claimInterfaceLogVo.setClaimNo(compensateVo == null ? "" : compensateVo.getClaimNo());
            claimInterfaceLogVo.setRegistNo(compensateVo == null ? "" : compensateVo.getRegistNo());
            claimInterfaceLogVo.setComCode(compensateVo == null ? "" : compensateVo.getComCode());
            claimInterfaceLogVo.setCreateUser(compensateVo == null ? "" : compensateVo.getHandler1Code());
        } else if (BusinessType.Payment_padPay.equals(businessType)) {
            PrpLPadPayMainVo padPayMainVo = padPayService.queryPadPay(registNo, compensateNo);
            claimInterfaceLogVo.setClaimNo(padPayMainVo == null ? "" : padPayMainVo.getClaimNo());
            claimInterfaceLogVo.setRegistNo(padPayMainVo == null ? "" : padPayMainVo.getRegistNo());
            claimInterfaceLogVo.setComCode(padPayMainVo == null ? "" : padPayMainVo.getComCode());
            claimInterfaceLogVo.setCreateUser(padPayMainVo == null ? "" : padPayMainVo.getUnderwriteUser());
        } else if (BusinessType.Payment_recPay.equals(businessType)) {
            PrplReplevyMainVo replevyMainVo = recPayService.findPrplReplevyMainVoByComPensateNo(compensateNo);
            claimInterfaceLogVo.setClaimNo(replevyMainVo == null ? "" : replevyMainVo.getClaimNo());
            claimInterfaceLogVo.setRegistNo(replevyMainVo == null ? "" : replevyMainVo.getRegistNo());
            claimInterfaceLogVo.setComCode(replevyMainVo == null ? "" : replevyMainVo.getComCode());
            claimInterfaceLogVo.setCreateUser(replevyMainVo == null ? "" : replevyMainVo.getHandlerCode());
        } else if (BusinessType.Payment_recLoss.equals(businessType)) {
            PrpLRecLossVo recLossVo = recLossService.findRecLossByRecLossId(compensateNo);
            List<PrpLClaimVo> claimVos = claimTaskService.findClaimListByRegistNo(recLossVo.getRegistNo(), "1");
            PrpLClaimVo prplClaimVo = new PrpLClaimVo();
            if (claimVos != null) {
                if (claimVos.size() > 1) {
                    for (PrpLClaimVo claimVo : claimVos) {
                        if (PaymentConstants.CLASSCODE_12.equals(claimVo.getClassCode())) {
                            prplClaimVo = claimVo;
                            break;
                        }
                    }
                } else {
                    prplClaimVo = claimVos.get(0);
                }
            }
            claimInterfaceLogVo.setClaimNo(prplClaimVo.getClaimNo());
            claimInterfaceLogVo.setRegistNo(recLossVo.getRegistNo());
            claimInterfaceLogVo.setComCode(prplClaimVo.getComCode());
            claimInterfaceLogVo.setCreateUser(recLossVo.getOperatorCode());
        } else if (BusinessType.Payment_assessor.equals(businessType)) {
            PrpLAssessorFeeVo assessorFeeVo = assessorService.findAssessorFeeVoByComp(compensateNo);
            PrpLAssessorMainVo assessorMainVo = assessorFeeVo.getAssessorMainVo();
            claimInterfaceLogVo.setClaimNo(assessorFeeVo.getClaimNo());
            claimInterfaceLogVo.setRegistNo(assessorFeeVo.getRegistNo());
            claimInterfaceLogVo.setComCode(assessorMainVo.getComCode());
            claimInterfaceLogVo.setCreateUser(assessorMainVo.getCreateUser());
        } else if (BusinessType.Payment_checkFee.equals(businessType)) {
            PrpLCheckFeeVo checkFeeVo = acheckService.findCheckFeeVoByComp(compensateNo);
            PrpLAcheckMainVo prplAcheckMainVo = acheckService.findAcheckMainVoByCompNo(compensateNo);
            claimInterfaceLogVo.setClaimNo(checkFeeVo.getClaimNo());
            claimInterfaceLogVo.setRegistNo(checkFeeVo.getRegistNo());
            claimInterfaceLogVo.setComCode(prplAcheckMainVo.getComCode());
            claimInterfaceLogVo.setCreateUser(prplAcheckMainVo.getCreateUser());
        }

        claimInterfaceLogVo.setOperateNode(getOperateNode(businessType));
        claimInterfaceLogVo.setCreateTime(new Date());
        claimInterfaceLogVo.setBusinessType(businessType.name());
        claimInterfaceLogVo.setBusinessName(businessType.getName());
        claimInterfaceLogVo.setRequestUrl(url);
        claimInterfaceLogVo.setRequestTime(new Date());
        claimInterfaceLogVo.setCompensateNo(compensateNo);
        claimInterfaceLogVo.setRequestXml(requestData);

        return claimInterfaceLogVo;
    }

    /**
     * ?????????????????????
     *
     * @param businessType ???????????????????????????
     * @return ??????????????????
     */
    private String getOperateNode(BusinessType businessType) {
        String node = "";
        if (BusinessType.Payment_prePay.equals(businessType)) {
            node = FlowNode.PrePay.name();
        } else if (BusinessType.Payment_compe.equals(businessType)) {
            //???????????????????????????????????????????????????????????????????????? ?????????VClaim???
            node = FlowNode.VClaim.name();
        } else if (BusinessType.Payment_padPay.equals(businessType)) {
            node = FlowNode.PadPay.name();
        } else if (BusinessType.Payment_recLoss.equals(businessType)) {
            node = FlowNode.RecLoss.name();
        } else if (BusinessType.Payment_recPay.equals(businessType)) {
            node = FlowNode.RecPay.name();
        } else {
            node = "";
        }
        return node;
    }

	/**
	 *
	 * @param payRefNos

	private void updateSettleNo(List<PayRefNoDto> payRefNos,PrpLCompensateVo compensateVo){
		List<PrpLPaymentVo> paymentList = compensateVo.getPrpLPayments();
		for (PayRefNoDto payRefNoDto : payRefNos) {
			String serialNo = payRefNoDto.getSerialNo();
			String compensateNo = payRefNoDto.getCertiNo();
			String settleNo = payRefNoDto.getPayRefNo();
			List<String> listHql = new ArrayList<String>();
			if(paymentList != null && paymentList.size()>0){
				for (PrpLPaymentVo payment : paymentList) {
					if(serialNo.equals(payment.getSerialNo())){
						String sql = "UPDATE prplpayment SET settleNo='"+settleNo+"' WHERE ID="+payment.getId();
						listHql.add(sql);
					}
				}
			}
			if(listHql != null && listHql.size()>0){
				for (String sql : listHql) {
					baseDaoService.executeSQL(sql);
				}
			}

		}

	}
	 */

	/**
	 * ???????????????????????????
	 * @param itemCarVo
	 * @return
	 */
	private String transCarType(PrpLCItemCarVo itemCarVo) {
		// ????????????
		String carKindCode = null;
		// ????????????
		String userKindCode = null;
		// ?????????????????????
		// 00-?????????????????????01-???????????????02-??????????????????03-???????????????04-??????????????????05-???????????????06-????????????07-????????????08-????????????09-??????
		String carNatureCode = null;
		if (itemCarVo != null) {
			carKindCode = itemCarVo.getCarKindCode();
			userKindCode = itemCarVo.getUseKindCode();
			// ????????????????????????????????? start
			if (carKindCode != null && carKindCode != "") {
				carKindCode = carKindCode.substring(0, 1);
				// A -??????, H -??????, M -?????????, J-?????????
				if ("A".equals(carKindCode)) {
					// 002-?????????
					if ("002".equals(userKindCode)) {
						carNatureCode = "02";
					} else {
						carNatureCode = "03";
					}
				} else if ("H".equals(carKindCode)) {
					// 002-?????????
					if ("002".equals(userKindCode)) {
						carNatureCode = "04";
					} else {
						carNatureCode = "05";
					}
				} else if ("M".equals(carKindCode)) {
					carNatureCode = "07";
				} else if ("J".equals(carKindCode)) {
					carNatureCode = "08";
				} else {
					carNatureCode = "00";
				}
			} else {
				carNatureCode = "00";
			}
		}
		return carNatureCode;
	}

	/**
	 * ????????????????????????
	 * @param registNo
	 * @return
	 */
	private boolean checkPingAnCase(String registNo){
		boolean resultFlag = false ;
		PrpLRegistVo registVO = registQueryService.findByRegistNo(registNo);
		if(registVO != null){
			if(registVO.getPaicReportNo() != null && registVO.getPaicReportNo() != ""){
				resultFlag = true;
			}
		}
		return resultFlag;
	}
}

