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
 * 车理赔对接新收付接口实现
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
     * 理算送收付
     *
     * @param compensateVo 根据理算号获取的理算计算书信息
     * @throws Exception 理算送收付异常
     */
    @Override
    public void compensateToNewPayment(PrpLCompensateVo compensateVo) throws Exception {
        if (compensateVo != null) {
        	// 全额预付也要送收付
			String hasPrepay = getYuPayFlagByClaim(compensateVo.getClaimNo());
            // 实付金额和费用 为0 不送收付
            if (BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumPaidAmt())) != 0
					|| BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumPaidFee())) != 0 || "1".equals(hasPrepay)) {
				// 获取赔款数据
				Claim2NewPaymentDto claim2NewPaymentDto = packagePaymentInfos(compensateVo);
				if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null
						&& claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
					Gson gson = new Gson();
					String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/dataReception";
					String paymentJson = gson.toJson(claim2NewPaymentDto);
					logger.info("核赔通过推送到收付的数据为：" + paymentJson);
					ResponseDto responseDto;
					// 封装日志表数据
					ClaimInterfaceLogVo logVo = null;
					try {
						logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_compe, newPaymentUrl,
								compensateVo.getCompensateNo(), compensateVo.getRegistNo());
					} catch (Exception e) {
						logger.info("理算业务号：" + compensateVo.getCompensateNo() + " 核赔送收付日志数据封装异常！", e);
					}
					PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.PINGANTOPALTFORMFLAG);
					//平安案件是否送收付
					if(this.checkPingAnCase(compensateVo.getRegistNo()) && configValueVo != null && configValueVo.getConfigValue() != null && "0".equals(configValueVo.getConfigValue())){
						//不送
						logVo.setRegistNo(compensateVo.getRegistNo());
						logVo.setResponseXml("");    // 返回报文
						logVo.setErrorMessage("平安送收付开关未打开");
						logVo.setErrorCode(false+"");
						logVo.setStatus("0");
						logVo.setCompensateNo(compensateVo.getCompensateNo());
						logService.save(logVo);
					}else{
						try {
							// 推送理算数据给收付
							responseDto = sendToNewPaymentService.callPaymentForClient(paymentJson,
									BusinessType.Payment_compe, compensateVo.getCompensateNo());
							logger.info("理算业务号：{} 核赔通过送收付完成！", compensateVo.getCompensateNo());
						} catch (Exception e) {
							logger.info("理算业务号：" + compensateVo.getCompensateNo() + " 核赔通过送收付异常！", e);
							if (logVo != null) {
								logVo.setRegistNo(compensateVo.getRegistNo());
								logVo.setStatus("0");
								logVo.setErrorCode(PaymentConstants.RESP_FAILED);
								logVo.setErrorMessage(e.getMessage());
								logService.save(logVo);
							}
							throw e;
						}
						// 收付成功接收之后更新理赔数据状态
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
            logger.info("核赔通过送收付失败！计算书对象为空！");
        }

    }
	/**
	 * 理算送收付
	 *
	 * @param compensateVo 根据理算号获取的理算计算书信息
	 * @throws Exception 理算送收付异常
	 */
	@Override
	public void compensateToNewPaymentPingAn(PrpLCompensateVo compensateVo,String registNo) throws Exception {
		if (compensateVo != null) {
			// 全额预付也要送收付
			String hasPrepay = getYuPayFlagByClaim(compensateVo.getClaimNo());
			// 实付金额和费用 为0 不送收付
			if (BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumPaidAmt())) != 0
					|| BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumPaidFee())) != 0 || "1".equals(hasPrepay)) {
				// 获取赔款数据
				Claim2NewPaymentDto claim2NewPaymentDto = packagePaymentInfos(compensateVo);
				if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null
						&& claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
					String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/dataReception";
					String paymentJson = "";
					// 封装日志表数据
					ClaimInterfaceLogVo logVo = null;
					try {
						logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_compe, newPaymentUrl,
								compensateVo.getCompensateNo(), registNo);
					} catch (Exception e) {
						logger.info("理算业务号：" + compensateVo.getCompensateNo() + " 核赔送收付日志数据封装异常！", e);
					}
					//不送
					logVo.setClaimNo(compensateVo.getClaimNo());
					logVo.setCreateUser(compensateVo.getCreateUser());
					logVo.setComCode(compensateVo.getComCode());
					logVo.setRegistNo(registNo);
					logVo.setResponseXml("");    // 返回报文
					logVo.setErrorMessage("平安数据送收付第一次默认失败，请检查数据补送");
					logVo.setErrorCode(false+"");
					logVo.setStatus("0");
					logVo.setCompensateNo(compensateVo.getCompensateNo());
					logService.save(logVo);
				}
			}
		} else {
			logger.info("核赔通过送收付失败！计算书对象为空！");
		}

	}
    /**
     * 预付送收付
     *
     * @param compensateVo 预付计算书
     * @throws Exception 异常信息
     */
    @Override
    public void prePayToNewPayment(PrpLCompensateVo compensateVo) throws Exception {
		logger.info("预付送收付，计算书号为：" + compensateVo.getCompensateNo());
        if (compensateVo != null) {
                // 实付金额和费用 为0 不送收付
			logger.info("预付送收付进行金额判断，计算书号为：" + compensateVo.getCompensateNo());
			if (BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumAmt())) != 0) {
				logger.info("预付送收付，计算书号为：" + compensateVo.getCompensateNo());
				// 获取赔款数据
				Claim2NewPaymentDto claim2NewPaymentDto = packagePrePayInfos(compensateVo);
				if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null
						&& claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
					Gson gson = new Gson();
					String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/dataReception";
					String paymentJson = gson.toJson(claim2NewPaymentDto);
					logger.info("预付送收付数据为：" + paymentJson);
					// 封装日志表数据
					ClaimInterfaceLogVo logVo = null;
					try {
						logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_prePay, newPaymentUrl,
								compensateVo.getCompensateNo(), compensateVo.getRegistNo());
					} catch (Exception e) {
						logger.info("预付业务号：" + compensateVo.getCompensateNo() + " 预付送收付日志数据封装异常！", e);
					}
					ResponseDto responseDto;
					PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.PINGANTOPALTFORMFLAG);
					//平安案件是否送收付
					if(this.checkPingAnCase(compensateVo.getRegistNo()) && configValueVo != null && configValueVo.getConfigValue() != null && "0".equals(configValueVo.getConfigValue())){
						//不送
						logVo.setRegistNo(compensateVo.getRegistNo());
						logVo.setResponseXml("");    // 返回报文
						logVo.setErrorMessage("平安送收付开关未打开");
						logVo.setErrorCode(false+"");
						logVo.setStatus("0");
						logVo.setCompensateNo(compensateVo.getCompensateNo());
						logService.save(logVo);
					}else{
						try {
							// 推送预付数据给收付
							responseDto = sendToNewPaymentService.callPaymentForClient(paymentJson,
									BusinessType.Payment_prePay, compensateVo.getCompensateNo());
							logger.info("预付业务号：{} 预付送收付完成！收付响应数据errorMessage为：{}", compensateVo.getCompensateNo(),
									responseDto.getErrorMessage());
						} catch (Exception e) {
							logger.info("预付业务号：" + compensateVo.getCompensateNo() + " 预付送收付异常！", e);
							if (logVo != null) {
								logVo.setStatus("0");
								logVo.setErrorCode(PaymentConstants.RESP_FAILED);
								logVo.setErrorMessage(e.getMessage());
								logService.save(logVo);
							}
							throw e;
						}

						// 收付成功接收之后更新理赔数据状态
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
            logger.info("预付送收付失败！计算书对象为空！");
        }
    }
	/**
	 * 平安预付送收付，第一次默认失败
	 *
	 * @param compensateVo 预付计算书
	 * @throws Exception 异常信息
	 */
	@Override
	public void prePayToNewPaymentPingAn(PrpLCompensateVo compensateVo,String registNo) throws Exception {
		logger.info("预付送收付，计算书号为：" + compensateVo.getCompensateNo());
		if (compensateVo != null) {
			// 实付金额和费用 为0 不送收付
			logger.info("预付送收付进行金额判断，计算书号为：" + compensateVo.getCompensateNo());
			if (BigDecimal.ZERO.compareTo(DataUtils.NullToZero(compensateVo.getSumAmt())) != 0) {
				logger.info("预付送收付，计算书号为：" + compensateVo.getCompensateNo());
				// 获取赔款数据
				String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/dataReception";
				String paymentJson = "";
				// 封装日志表数据
				ClaimInterfaceLogVo logVo = null;
				try {
					logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_prePay, newPaymentUrl,
							compensateVo.getCompensateNo(), registNo);
				} catch (Exception e) {
					logger.info("预付业务号：" + compensateVo.getCompensateNo() + " 预付送收付日志数据封装异常！", e);
				}
				//不送
				logVo.setRegistNo(registNo);
				logVo.setResponseXml("");    // 返回报文
				logVo.setErrorMessage("平安送收付第一次默认失败，请检查数据后进行补送");
				logVo.setErrorCode(false+"");
				logVo.setStatus("0");
				logVo.setCompensateNo(compensateVo.getCompensateNo());
				logService.save(logVo);
			}
		} else {
			logger.info("预付送收付失败！计算书对象为空！");
		}
	}
    /**
     * 垫付送收付
     *
     * @param padPayMainVo 垫付计算书
     * @throws Exception 异常信息
     */
    @Override
    public void padPayToNewPayment(PrpLPadPayMainVo padPayMainVo) throws Exception {

		if (padPayMainVo != null) {
			// 获取赔款数据
			Claim2NewPaymentDto claim2NewPaymentDto = packagePadPayInfos(padPayMainVo);
			if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null && claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
				Gson gson = new Gson();
				String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/dataReception";
				String paymentJson = gson.toJson(claim2NewPaymentDto);
				logger.info("垫付送收付数据为：" + paymentJson);
				// 封装日志表数据
				ClaimInterfaceLogVo logVo = null;
				try {
					logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_padPay, newPaymentUrl,
							padPayMainVo.getCompensateNo(), padPayMainVo.getRegistNo());
				} catch (Exception e) {
					logger.info("垫付业务号：" + padPayMainVo.getCompensateNo() + " 垫付送收付日志数据封装异常！", e);
				}
				ResponseDto responseDto;
				PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.PINGANTOPALTFORMFLAG);
				//平安案件是否送收付
				if(this.checkPingAnCase(padPayMainVo.getRegistNo()) && configValueVo != null && configValueVo.getConfigValue() != null && "0".equals(configValueVo.getConfigValue())){
					//不送
					logVo.setResponseXml("");    // 返回报文
					logVo.setErrorMessage("平安送收付开关未打开");
					logVo.setErrorCode(false+"");
					logVo.setStatus("0");
					logVo.setCompensateNo(padPayMainVo.getCompensateNo());
					logService.save(logVo);
				}else{
					try {
						// 推送垫付数据给收付
						responseDto = sendToNewPaymentService.callPaymentForClient(paymentJson, BusinessType.Payment_padPay,
								padPayMainVo.getCompensateNo());
						logger.info("垫付业务号：{} 垫付送收付完成！", padPayMainVo.getCompensateNo());
					} catch (Exception e) {
						logger.info("垫付业务号：" + padPayMainVo.getCompensateNo() + " 垫付送收付异常！", e);
						if (logVo != null) {
							logVo.setStatus("0");
							logVo.setErrorCode(PaymentConstants.RESP_FAILED);
							logVo.setErrorMessage(e.getMessage());
							logService.save(logVo);
						}
						throw e;
					}
					// 收付成功接收之后更新理赔数据状态
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
            logger.info("垫付送收付失败！计算书对象为空！");
        }
    }


	/**
	 * 平安垫付送收付，默认第一次失败
	 *
	 * @param padPayMainVo 垫付计算书
	 * @throws Exception 异常信息
	 */
	@Override
	public void padPayToNewPaymentPingAn(PrpLPadPayMainVo padPayMainVo,String registNo) throws Exception {
		if (padPayMainVo != null) {
			String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/dataReception";
			String paymentJson = "";
			// 封装日志表数据
			ClaimInterfaceLogVo logVo = null;
			try {
				logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_padPay, newPaymentUrl,
						padPayMainVo.getCompensateNo(), registNo);
			} catch (Exception e) {
				logger.info("垫付业务号：" + padPayMainVo.getCompensateNo() + " 垫付送收付日志数据封装异常！", e);
			}
			//不送
			logVo.setRegistNo(registNo);
			logVo.setResponseXml("");    // 返回报文
			logVo.setErrorMessage("平安送收付第一次默认失败，请检查数据后进行补送");
			logVo.setErrorCode(false+"");
			logVo.setStatus("0");
			logVo.setCompensateNo(padPayMainVo.getCompensateNo());
			logService.save(logVo);
		} else {
			logger.info("垫付送收付失败！计算书对象为空！");
		}
	}
    /**
     * 追偿送收付
     *
     * @param prplReplevyMainVo 追偿信息
     * @throws Exception 追偿送收付异常信息
     */
    @Override
    public void recPayToNewPayment(PrplReplevyMainVo prplReplevyMainVo) throws Exception {
        Claim2NewPaymentDto claim2NewPaymentDto = packageRelevyDatas(prplReplevyMainVo);
        if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null && claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
            Gson gson = new Gson();
            String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL")+"/dataReception";
            String paymentJson = gson.toJson(claim2NewPaymentDto);
            logger.info("追偿推送到收付的数据为：" + paymentJson);
            // 封装日志表数据
            ClaimInterfaceLogVo logVo = null;
            try {
            	logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_recPay, newPaymentUrl, prplReplevyMainVo.getCompensateNo(), prplReplevyMainVo.getRegistNo());
            } catch (Exception e) {
                logger.info("追偿业务号：" + prplReplevyMainVo.getCompensateNo() + " 追偿送收付日志数据封装异常！", e);
            }
            ResponseDto responseDto; 
            try {
                // 推送追偿数据给收付
            	responseDto = sendToNewPaymentService.callPaymentForClient(paymentJson, BusinessType.Payment_recPay, prplReplevyMainVo.getCompensateNo());
                logger.info("追偿业务号：{} 追偿送收付完成！收付响应数据errroMessage为：{}", prplReplevyMainVo.getCompensateNo(), responseDto.getErrorMessage());
            } catch (Exception e) {
            	logger.info("追偿业务号：" + prplReplevyMainVo.getCompensateNo() + " 追偿送收付异常！", e);
            	if (logVo != null) {
            		logVo.setStatus("0");
                    logVo.setErrorCode(PaymentConstants.RESP_FAILED);
                    logVo.setErrorMessage(e.getMessage());
                    logService.save(logVo);
            	}
                throw e;
            }
            // 如果收付成功接收则认为送收付成功
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
     * 损余回收送收付
     *
     * @param recLossVo 损余信息
     * @throws Exception 损余回收送收付异常
     */
    @Override
    public void recLossToNewPayment(PrpLRecLossVo recLossVo) throws Exception {
        Claim2NewPaymentDto claim2NewPaymentDto = packageRecLossDatas(recLossVo);
        if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null && claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
            Gson gson = new Gson();
            String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL")+"/dataReception";
            String paymentJson = gson.toJson(claim2NewPaymentDto);
            logger.info("损余回收推送到收付的数据为：" + paymentJson);
            // 封装日志表数据
            ClaimInterfaceLogVo logVo = null;
            try {
            	logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_recLoss, newPaymentUrl, recLossVo.getPrpLRecLossId(), recLossVo.getRegistNo());
            } catch (Exception e) {
                logger.info("损余回收业务号：" + recLossVo.getPrpLRecLossId() + " 损余回收送收付日志数据封装异常！", e);
            }
            ResponseDto responseDto;
            try {
                // 推送追偿数据给收付
            	responseDto = sendToNewPaymentService.callPaymentForClient(paymentJson, BusinessType.Payment_recLoss, recLossVo.getPrpLRecLossId());
                logger.info("损余回收业务号：{} 损余回收送收付完成！收付响应数据errroMessage为：{}", recLossVo.getPrpLRecLossId(), responseDto.getErrorMessage());
            } catch (Exception e) {
            	logger.info("损余回收业务号：" + recLossVo.getPrpLRecLossId() + " 损余回收送收付异常！", e);
            	if (logVo != null) {
            		logVo.setStatus("0");
                    logVo.setErrorCode(PaymentConstants.RESP_FAILED);
                    logVo.setErrorMessage(e.getMessage());
                    logService.save(logVo);
            	}
                throw e;
            }
			// 如果收付成功接收则认为送收付成功
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
     * 公估费送收付
     *
     * @param assessorMainVo 公估费主表信息
     * @throws Exception 公估费送收付异常
     */
    @Override
    public void assessorToNewPayment(PrpLAssessorMainVo assessorMainVo, PrpLAssessorFeeVo assessorFeeVo) throws Exception {
        Claim2NewPaymentDto claim2NewPaymentDto = packageAssessorData(assessorMainVo, assessorFeeVo);
        if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null && claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
            Gson gson = new Gson();
            String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL")+"/dataReception";
            String paymentJson = gson.toJson(claim2NewPaymentDto);
            logger.info("公估费推送到收付的数据为：" + paymentJson);
            // 封装日志表数据
            ClaimInterfaceLogVo logVo = null;
            try {
            	logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_assessor, newPaymentUrl, assessorFeeVo.getCompensateNo(), assessorFeeVo.getRegistNo());
            } catch (Exception e) {
                logger.info("公估费业务号：" + assessorFeeVo.getCompensateNo() + " 公估费送收付日志数据封装异常！", e);
            }
            ResponseDto responseDto;
            try {
                // 推送追偿数据给收付
            	responseDto = sendToNewPaymentService.callPaymentForClient(paymentJson, BusinessType.Payment_assessor, assessorFeeVo.getCompensateNo());
                logger.info("公估费业务号：{} 公估费送收付完成！收付响应数据errorMessage为：{}", assessorFeeVo.getCompensateNo(), responseDto.getErrorMessage());
            } catch (Exception e) {
            	logger.info("公估费业务号：" + assessorFeeVo.getCompensateNo() + " 公估费送收付异常！", e);
            	if (logVo != null) {
            		logVo.setStatus("0");
                    logVo.setErrorCode(PaymentConstants.RESP_FAILED);
                    logVo.setErrorMessage(e.getMessage());
                    logService.save(logVo);
            	}
                throw e;
            }
			// 如果收付成功接收则认为送收付成功
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
     * 查勘费送收付
     *
     * @param checkMainVo 查勘费主表对象
     * @param checkFeeVo  查勘费明细
     * @throws Exception 异常信息
     */
    @Override
    public void checkFeeToNewPayment(PrpLAcheckMainVo checkMainVo, PrpLCheckFeeVo checkFeeVo) throws Exception {
		Claim2NewPaymentDto claim2NewPaymentDto = packageCheckFeeData(checkMainVo, checkFeeVo);
		if (claim2NewPaymentDto.getPrpJlossPlanDtos() != null && claim2NewPaymentDto.getPrpJlossPlanDtos().size() > 0) {
			Gson gson = new Gson();
			String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL") + "/dataReception";
			String paymentJson = gson.toJson(claim2NewPaymentDto);
			logger.info("查勘费推送到收付的数据为：" + paymentJson);
			// 封装日志表数据
			ClaimInterfaceLogVo logVo = null;
			try {
				logVo = packageInterfaceLog(paymentJson, BusinessType.Payment_checkFee, newPaymentUrl,
						checkFeeVo.getCompensateNo(), checkFeeVo.getRegistNo());
			} catch (Exception e) {
				logger.info("查勘费业务号：" + checkFeeVo.getCompensateNo() + " 查勘费送收付日志数据封装异常！", e);
			}
			ResponseDto responseDto = new ResponseDto();
			try {
				// 推送追偿数据给收付
				responseDto = sendToNewPaymentService.callPaymentForClient(paymentJson, BusinessType.Payment_checkFee,
						checkFeeVo.getCompensateNo());
				logger.info("查勘费业务号：{} 查勘费送收付完成！收付响应数据errorMessage为：{}", checkFeeVo.getCompensateNo(), responseDto.getErrorMessage());
			} catch (Exception e) {
				logger.info("查勘费业务号：" + checkFeeVo.getCompensateNo() + " 查勘费送收付异常！", e);
				if (logVo != null) {
					logVo.setStatus("0");
					logVo.setErrorCode(PaymentConstants.RESP_FAILED);
					logVo.setErrorMessage(e.getMessage());
					logService.save(logVo);
				}
				throw e;
			}
			// 如果收付成功接收则认为送收付成功
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
     * 封装预付送收付数据
     *
     * @param compensateVo 预付计算书
     * @return 预付送收付数据对象
     * @throws Exception 异常信息
     */
    private Claim2NewPaymentDto packagePrePayInfos(PrpLCompensateVo compensateVo) throws Exception {
        Claim2NewPaymentDto claim2NewPaymentDto = new Claim2NewPaymentDto();
        List<PrpJlossPlanDto> jlossPlanDtos = new ArrayList<PrpJlossPlanDto>();
        // 封装预付赔款和费用数据
        packagePrePayData(compensateVo, jlossPlanDtos);
        claim2NewPaymentDto.setPrpJlossPlanDtos(jlossPlanDtos);

        return claim2NewPaymentDto;
    }

    /**
     * 封装预付赔款数据
     *
     * @param compensateVo  预付计算书
     * @param jlossPlanDtos 送收付主对象
     */
    private void packagePrePayData(PrpLCompensateVo compensateVo, List<PrpJlossPlanDto> jlossPlanDtos) throws Exception {
        if (compensateVo != null) {
            // 1. 判断赔付金额（赔款），为0则不送收付
            List<PrpLPrePayVo> prePayVos = compensateService.getPrePayVo(compensateVo.getCompensateNo(), null);
            boolean nonZero = isPrePayAndFeeNonZero(prePayVos);
            if (nonZero) {
                PrpLClaimVo prplClaimVo = claimService.findClaimVoByClaimNo(compensateVo.getClaimNo());
                PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(compensateVo.getRegistNo(), compensateVo.getPolicyNo());
                PrpLRegistVo registVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());

                // 2. 封装预付赔款数据
                for (PrpLPrePayVo prePayVo : prePayVos) {
                    // 支付金额不为0的送收付
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

                        // 支付对象相关信息
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
                            prpJlossPlanDto.setUsage(StringUtils.isBlank(custom.getPurpose()) ? "无" : custom.getPurpose());
                            prpJlossPlanDto.setOtherRemark(custom.getRemark());
                            prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);
                        } else {
                            logger.info("支付对象id：" + prePayVo.getPayeeId() + " 预付计算书号：" + compensateVo.getCompensateNo() + " paycustom支付对象信息无法匹配！");
                        }
                        prpJlossPlanDto.setPayeeCurrency("CNY");
                        prpJlossPlanDto.setMessageContent("预付送收付");
                        prpJlossPlanDto.setPayReasonFlag(StringUtils.isBlank(prePayVo.getOtherFlag()) ? "0" : prePayVo.getOtherFlag());

                        prpJlossPlanDto.setIsExpress("0");
                        prpJlossPlanDto.setYuPayFlag("0");
						// 封装明细数据
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
     * 封装预付明细数据
     *
     * @param compensateVo 预付计算书
     * @return 明细数据集合
     * @throws Exception 封装异常
     */
    private List<PrpJlossPlanSubDto> getPrePayDetails(PrpLCompensateVo compensateVo) throws Exception {

        List<PrpLPrePayVo> prePayVoList = compensateService.getPrePayVo(compensateVo.getCompensateNo(), null);
        List<PrpJlossPlanSubDto> subDtos = new ArrayList<PrpJlossPlanSubDto>();
        for (PrpLPrePayVo prePayVo : prePayVoList) {
            // 封装明细数据
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
     * 判断预付相关赔款或费用金额是否为0
     *
     * @param prePayVos 预付数据 P-赔款 F-费用
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
     * 预付送收付成功更新预付数据状态
     *
     * @param compensateVo 预付计算书对象
     * @throws Exception 更新预付数据状态异常
     */
    private void updatePrePayStatus(PrpLCompensateVo compensateVo,List<PayRefNoDto> payRefNoDtos) throws Exception {
        try {
            logger.info("业务号：{} 预付送收付完成，更新预付数据状态开始...", compensateVo.getCompensateNo());

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
                        // 支付历史轨迹Y预付D垫付P赔款F费用
                        this.savePrplPayHis(compensateVo.getClaimNo(), prePayVo.getCompensateNo(), prePayVo.getId(), "2", "Y");
                    } else {
                        this.savePrplPayHis(compensateVo.getClaimNo(), prePayVo.getCompensateNo(), prePayVo.getId(), "0", "Y");
                    }
                }
                prePayVoList.addAll(prePayVos);
            }
            compensateService.saveOrUpdatePrePay(prePayVoList, compensateVo.getCompensateNo());
            logger.info("业务号：{} 预付送收付完成，更新预付数据状态完成！", compensateVo.getCompensateNo());
        } catch (Exception e) {
            logger.info("业务号：" + compensateVo.getCompensateNo() + " 预付送收付完成，更新预付数据状态异常！", e);
            throw e;
        }
    }

    /**
     * 封装垫付送收付数据
     *
     * @param padPayMainVo 垫付主表对象
     * @return 返回送收付数据对象
     * @throws Exception 数据封装异常
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

            // 2. 封装垫付数据
            for (PrpLPadPayPersonVo personVo : padPayMainVo.getPrpLPadPayPersons()) {
                PrpJlossPlanDto prpJlossPlanDto = new PrpJlossPlanDto();
                PrpJlossPlanSubDto prpJlossPlanSubDto = new PrpJlossPlanSubDto();
                prpJlossPlanDto.setPayeeId(personVo.getPayeeId() == null ? "" : personVo.getPayeeId().toString());
                prpJlossPlanDto.setCertiType(PaymentConstants.CERTITYPE_C);
                prpJlossPlanDto.setCertiNo(padPayMainVo.getCompensateNo());
                prpJlossPlanDto.setPolicyNo(padPayMainVo.getPolicyNo());
                prpJlossPlanDto.setRegistNo(padPayMainVo.getRegistNo());
                // 垫付无序号 给定默认值
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

                // 支付对象相关信息
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
                    prpJlossPlanDto.setUsage(StringUtils.isBlank(custom.getPurpose()) ? "无" : custom.getPurpose());
                    prpJlossPlanDto.setOtherRemark(custom.getRemark());
                    // 短信
                    String msg = padPayMsg(padPayMainVo, personVo, custom);
                    prpJlossPlanDto.setMessageContent(msg);
                    prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);
                } else {
                    logger.info("垫付支付对象id：" + personVo.getPayeeId() + " 垫付计算书号：" + padPayMainVo.getCompensateNo() + " paycustom支付对象信息无法匹配！");
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
     * 获取垫付总金额
     *
     * @param padPayMainVo 垫付主表数据
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
     * 垫付送收付完成更新垫付数据状态
     *
     * @param padPayMainVo 垫付主表数据
     * @throws Exception 更新垫付数据状态异常
     */
    private void updatePadPayStatus(PrpLPadPayMainVo padPayMainVo,List<PayRefNoDto> payRefNoDtos) throws Exception {
        try {
            logger.info("业务号：{} 垫付送收付完成，更新垫付数据状态开始...", padPayMainVo.getCompensateNo());
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
            logger.info("业务号：{} 垫付送收付完成，更新垫付数据状态完成！", padPayMainVo.getCompensateNo());
        } catch (Exception e) {
            logger.info("业务号：" + padPayMainVo.getCompensateNo() + " 垫付送收付完成，更新垫付数据状态异常!", e);
            throw e;
        }
    }

    /**
     * 垫付短信内容
     *
     * @param padPayMainVo   垫付主表对象
     * @param padPayPersonVo 垫付人伤明细对象
     * @param customVo       收款人信息
     * @return 短信内容
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
        // 主险金额
        msgParamVo.setPrpCItemKinds(carItemKinds);
        SysMsgModelVo msgModelVo = this.findSysMsgModelVo(prplRegistVo, CodeConstants.CompensateFlag.padPay);
        if (msgModelVo != null) {
            message = sendMsgService.getMessage(msgModelVo.getContent(), msgParamVo);
        }

        return message;
    }

    /**
     * 获取短信模板
     *
     * @param prplRegistVo   报案对象
     * @param compensateFlag 理算类型
     * @return 短信模板对象
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
     * 根据追偿主表数据封装送收付数据
     *
     * @param prplReplevyMainVo 追偿主表对象
     * @return 送收付对象
     * @throws Exception 数据封装异常
     */
    private Claim2NewPaymentDto packageRelevyDatas(PrplReplevyMainVo prplReplevyMainVo) throws Exception {
        Claim2NewPaymentDto claim2NewPaymentDto = new Claim2NewPaymentDto();
        List<PrpJlossPlanDto> jlossPlanDtos = new ArrayList<PrpJlossPlanDto>();
        if (prplReplevyMainVo != null) {
            // 封装赔款信息
			if (BigDecimal.ZERO.compareTo(prplReplevyMainVo.getSumRealReplevy()) < 0) {
				packageRelevyPayData(prplReplevyMainVo, jlossPlanDtos);
			}
            // 封装费用信息
			if (BigDecimal.ZERO.compareTo(prplReplevyMainVo.getSumReplevyFee()) < 0) {
				packageRelevyFeeData(prplReplevyMainVo, jlossPlanDtos);
			}

            claim2NewPaymentDto.setPrpJlossPlanDtos(jlossPlanDtos);
        }
        return claim2NewPaymentDto;
    }

    /**
     * 封装追偿赔款送收付数据
     *
     * @param prplReplevyMainVo 追偿主表数据
     * @param jlossPlanDtos     送收付数据集合
     * @throws Exception 数据封装异常
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
        // 追偿无序号 给定默认值
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
        prpJlossPlanDto.setUsage("无");
        prpJlossPlanDto.setPayReasonFlag("0");
        prpJlossPlanDto.setVoucherNo2("无");
        prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);
        // 追偿赔款数据
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
     * 封装追偿费用送收付数据
     *
     * @param prplReplevyMainVo 追偿主表数据
     * @param jlossPlanDtos     送收付数据对象集合
     * @throws Exception 数据封装异常
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
        // 追偿无序号 给定默认值
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
        prpJlossPlanDto.setUsage("无");
        prpJlossPlanDto.setPayReasonFlag("0");
        prpJlossPlanDto.setVoucherNo2("无");
        prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);
        // 追偿费用数据
        // 非严格序号
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
     * 封装损余回收送收付数据
     *
     * @param recLossVo 损余回收主对象数据
     * @return 数据回收异常
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
            // 追偿无序号 给定默认值
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
            prpJlossPlanDto.setUsage("无");
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
     * 封装公估费送收付数据
     *
     * @param assessorMainVo 公估费主表对象
     * @param assessorFeeVo  公估费明细数据
     * @return 公估费送收付数据对象
     * @throws Exception 异常信息
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
        // 公估费明细无序号 给定默认值
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

        // 支付对象相关信息
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
            prpJlossPlanDto.setAbstractContent(assessorFeeVo.getRemark() == null ? "公估费" : assessorFeeVo.getRemark());
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
            logger.info("公估费支付对象IntermId：" + assessorMainVo.getIntermId() + " 公估费计算书号：" + assessorFeeVo.getCompensateNo() + " 无法匹配到公估费支付对象的银行账号信息！");
        }
        prpJlossPlanDto.setPayeeCurrency("CNY");
        prpJlossPlanDto.setMessageContent("公估费送收付");
        prpJlossPlanDto.setPayReasonFlag("0");
        prpJlossPlanDto.setIsAutoPay("0");
        prpJlossPlanDto.setIsExpress("0");
        prpJlossPlanDto.setYuPayFlag("0");
        prpJlossPlanDto.setUsage("无");

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
     * 封装查勘费送收付数据
     *
     * @param checkMainVo 查勘费主表对象
     * @param checkFeeVo  查勘费明细数据
     * @return 查勘费送收付数据对象
     * @throws Exception 异常信息
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
        // 查勘费明细无序号 给定默认值
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
            prpJlossPlanDto.setAbstractContent(checkFeeVo.getRemark() == null ? "查勘费" : checkFeeVo.getRemark());
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
            logger.info("查勘费支付对象查勘机构id：" + checkMainVo.getCheckmId() + " 查勘费计算书号：" + checkFeeVo.getCompensateNo() + " 无法匹配到查勘费支付对象的银行账号信息！");
        }

        prpJlossPlanDto.setPayeeCurrency("CNY");
        prpJlossPlanDto.setMessageContent("查勘费送收付");
        prpJlossPlanDto.setPayReasonFlag("0");
        prpJlossPlanDto.setIsAutoPay("0");
        prpJlossPlanDto.setIsExpress("0");
        prpJlossPlanDto.setYuPayFlag("0");
        prpJlossPlanDto.setUsage("无");

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
     * 封装理算送收付信息
     *
     * @param compensateVo 计算书
     * @return 返回送收付信息
     */
    private Claim2NewPaymentDto packagePaymentInfos(PrpLCompensateVo compensateVo) throws Exception {
        Claim2NewPaymentDto claim2NewPaymentDto = new Claim2NewPaymentDto();
        List<PrpJlossPlanDto> jlossPlanDtos = new ArrayList<PrpJlossPlanDto>();
        // 封装赔款信息
        packageCompensateData(compensateVo, jlossPlanDtos);
        // 封装费用信息
        packageChargeData(compensateVo, jlossPlanDtos);

        claim2NewPaymentDto.setPrpJlossPlanDtos(jlossPlanDtos);

        return claim2NewPaymentDto;
    }

    /**
     * 设置收付原因，如果是清付或代付，还需设置清算码或结算码
     *
     * @param prpJlossPlanDto 理赔送收付数据封装对象
     * @param compensateVo    计算书
     * @param paymentVo       支付信息
     */
    private void setPayRefReason(PrpJlossPlanDto prpJlossPlanDto, PrpLCompensateVo compensateVo, PrpLPaymentVo paymentVo) {
        String voucherNo2 = "";
        if (CodeConstants.PayFlagType.COMPENSATE_PAY.equals(paymentVo.getPayFlag())) {
            // 自付
            prpJlossPlanDto.setPayRefReason(PaymentConstants.PAYREASON_P60);
        } else if (CodeConstants.PayFlagType.CLEAR_PAY.equals(paymentVo.getPayFlag())) {
            // 清付
            prpJlossPlanDto.setPayRefReason(PaymentConstants.PAYREASON_P6D);
            // 获取清算码
            if (StringUtils.isNotBlank(paymentVo.getItemId())) {
                voucherNo2 = getClearRecoveryCode(Integer.parseInt(paymentVo.getItemId()), compensateVo);
            }
            prpJlossPlanDto.setVoucherNo2(voucherNo2);
        } else if (CodeConstants.PayFlagType.INSTEAD_PAY.equals(paymentVo.getPayFlag())) {
            // 代付
            prpJlossPlanDto.setPayRefReason(PaymentConstants.PAYREASON_P6B);
            // 组织平台结算码
            voucherNo2 = getPlatformRecoveryCode(compensateVo, CodeConstants.PayFlagType.INSTEAD_PAY);
            if (StringUtils.isBlank(voucherNo2)) {
                // 非机动车代位时 无结算码 无锁定platLockVo 此时传无
                voucherNo2 = "无";
            }
            prpJlossPlanDto.setVoucherNo2(voucherNo2);
        }
    }

    /**
     * 组织平台结算码
     *
     * @param compensateVo 计算书
     * @param payFlagType  赔付类型
     * @return 返回平台结算码
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
     * 获取清算结算码
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
        // 真实发送时间
        Date trueSendTime = new Date();
        Date nowTime = new Date();
        Date sendTime1 = null;
        if (sendTime_7 != null) {
            // 短信平台发送时间
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
            //这时候保存的短信全部为推送失败，然后在收付回写报文中，再调整状态为1
            prpsmsMessageVo.setStatus("0");
            msgModelService.saveorUpdatePrpSmsMessage(prpsmsMessageVo);
        }

    }

    /**
     * 获取短信内容（同时保存短信记录）
     *
     * @param registVo     报案对象
     * @param compensateVo 计算书对象
     * @param paymentVo    支付对象
     * @param cmainVo      保单对象
     * @param customVo     收款人对象
     * @return 返回短信内容
     */
    private String handleMessageContent(PrpLRegistVo registVo, PrpLCompensateVo compensateVo, PrpLPaymentVo paymentVo, PrpLCMainVo cmainVo, PrpLPayCustomVo customVo) {
        String message = "";
        try {
            SysMsgModelVo msgModelVo = findSysMsgModelVo(registVo, CodeConstants.CompensateFlag.compensate);
            if (msgModelVo != null) {
                SendMsgParamVo msgParamVo = getMsgParamVo(registVo, paymentVo, customVo, cmainVo);
                message = sendMsgService.getMessage(msgModelVo.getContent(), msgParamVo);
                // 保存短信
                sendMessageContent(msgModelVo, msgParamVo, message);
            }
        } catch (Exception e) {
            logger.info("报案号：" + compensateVo.getRegistNo() + " 计算书号：" + compensateVo.getCompensateNo() + " 结案短信保存失败！", e);
        }

        return message;
    }

    /**
     * 获取短信内容参数
     *
     * @param registVo    报案数据
     * @param paymentVo   支付数据
     * @param customVo    收款人数据
     * @param prpLCMainVo 保单数据
     * @return 返回参数对象
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
     * 获取自动支付标识  0-不自动支付 1-自动支付
     *
     * @param compensateVo 计算书对象
     * @param prplcMainVo  保单对象
     * @return 返回送资金标识
     */
    private String getIsAutoPay(PrpLCompensateVo compensateVo, PrpLCMainVo prplcMainVo) {
        // 重开赔案不自动支付，从共从联不自动支付
        String isAutoPay = "1";
        Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("registNo", compensateVo.getRegistNo());
        queryMap.put("claimNo", compensateVo.getClaimNo());
        // 审核通过
        queryMap.put("checkStatus", "6");
        // 查找审核通过的重开赔案 立案号列表
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
     * 封装赔款数据
     *
     * @param compensateVo 根据理算号获取的理算计算书信息
     * @throws Exception 封装赔款信息异常
     */
    private void packageCompensateData(PrpLCompensateVo compensateVo, List<PrpJlossPlanDto> jlossPlanDtolist) throws Exception {
        if (compensateVo != null) {
                PrpLClaimVo prpLClaimVo = claimService.findClaimVoByClaimNo(compensateVo.getClaimNo());
                PrpLCMainVo prplcMainVo = policyViewService.findPolicyInfoByPaltform(compensateVo.getRegistNo(), compensateVo.getPolicyNo());
                PrpLRegistVo registVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());
                List<PrpLReCaseVo> reCaseVo = reOpenCaseService.findReCaseByClaimNo(compensateVo.getClaimNo());
                String repayType = reCaseVo != null && reCaseVo.size() > 0 ? "1" : "0";
				String isAutoPay = getIsAutoPay(compensateVo, prplcMainVo);

                // 2. 封装赔款支付信息（自付、清付、代付，是否送资金，送资金需发送短信）
                for (PrpLPaymentVo paymentVo : compensateVo.getPrpLPayments()) {
                    // 支付金额不为0的送收付
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
                        // 设置收付原因与清算码或结算码（如果有）
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

                        // 支付对象相关信息
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
                            prpJlossPlanDto.setUsage(StringUtils.isBlank(custom.getPurpose()) ? "无" : custom.getPurpose());
                            prpJlossPlanDto.setOtherRemark(custom.getRemark());
                            prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);
                        } else {
                            logger.info("支付对象id：" + paymentVo.getPayeeId() + " 计算书号：" + compensateVo.getCompensateNo() + " paycustom支付对象信息无法匹配！");
                        }
                        prpJlossPlanDto.setPayeeCurrency("CNY");
                        prpJlossPlanDto.setMessageContent("无");
                        // 是否自动支付
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
     * 封装理算费用数据
     *
     * @param compensateVo     理算计算书
     * @param jlossPlanDtolist 送收付主对象数据
     */
    private void packageChargeData(PrpLCompensateVo compensateVo, List<PrpJlossPlanDto> jlossPlanDtolist) {
        if (compensateVo != null) {
                // 理算费用信息
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

                    // 支付对象相关信息
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
                        prpJlossPlanDto.setUsage(StringUtils.isBlank(custom.getPurpose()) ? "无" : custom.getPurpose());
                        prpJlossPlanDto.setOtherRemark(custom.getRemark());
                        prpJlossPlanDto.setBankCardType(PaymentConstants.BANKCARDTYPE_2);
                    } else {
                        logger.info("支付对象id：" + chargeVo.getPayeeId() + " 计算书号：" + compensateVo.getCompensateNo() + " paycustom支付对象信息无法匹配！");
                    }

                    prpJlossPlanDto.setPayReasonFlag("0");
                    prpJlossPlanDto.setMessageContent("无");
                    // 是否自动支付
                    prpJlossPlanDto.setMessageContent("费用短信");
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
     * 获取明细数据（赔款和费用）
     *
     * @param compensateVo 赔款计算书
     * @param paymentVo    收款人信息
     * @return 返回所有赔款计算书明细数据
     */
    private List<PrpJlossPlanSubDto> getCompensateDetail(PrpLCompensateVo compensateVo, PrpLPaymentVo paymentVo) {
        List<PrpLLossItemVo> lossItemVos = compensateVo.getPrpLLossItems();
        List<PrpLLossPropVo> lossPropVos = compensateVo.getPrpLLossProps();
        List<PrpLLossPersonVo> lossPersonVos = compensateVo.getPrpLLossPersons();

        Map<String, BigDecimal> kindPaidMap = new HashMap<String, BigDecimal>();
        // 1 追偿 2代位
        Map<String, BigDecimal> dwPaidMap = new HashMap<String, BigDecimal>();
        // 该计算书下的所有险别损失总金额
        BigDecimal allKindSumRealPay = BigDecimal.ZERO;
        // 赔款险别明细数据
        if (lossItemVos != null && lossItemVos.size() > 0) {
            for (PrpLLossItemVo itemVo : lossItemVos) {
                if (BigDecimal.ZERO.compareTo(itemVo.getSumRealPay()) == 0) {
                    // 赔款金额为0时不组织该数据
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

        // 财产赔款险别明细
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

        // 人伤赔款险别明细
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
            // 封装明细数据
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
     * 收付成功接收之后更新理赔数据状态为送收付成功
     *
     * @param compensateVo 计算书对象
     */
    private void updateClaimStatus(PrpLCompensateVo compensateVo,List<PayRefNoDto> payRefNoDtos) throws Exception {
        // 送收付成功后回写理算PAYSTATUS字段为2-表示送收付成功
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
        //送收付取当前时间
        Date inputTime = new Date();
        compensateService.savePrplPayHis(claimNo, compensateNo, id, flags, hisType, inputTime);
    }

    /**
     * 获取立案号下的预付标识
     *
     * @param ClaimNo 立案号
     * @return 预付标识
     */
    private String getYuPayFlagByClaim(String ClaimNo) {
        List<PrpLCompensateVo> comListVo = new ArrayList<PrpLCompensateVo>();
        boolean existsPrepay = false;
        if (!StringUtils.isBlank(ClaimNo)) {
            QueryRule qr = QueryRule.getInstance();
            qr.addEqual("claimNo", ClaimNo);
            // 核赔通过
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
     * 封装推送新收付日志数据
     *
     * @param requestData  送收付请求数据
     * @param businessType 节点
     * @param url          收付接口地址
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
     * 送收付节点名称
     *
     * @param businessType 送收付时传入的类型
     * @return 返回节点名称
     */
    private String getOperateNode(BusinessType businessType) {
        String node = "";
        if (BusinessType.Payment_prePay.equals(businessType)) {
            node = FlowNode.PrePay.name();
        } else if (BusinessType.Payment_compe.equals(businessType)) {
            //理算完核赔通过后，才送收付，日志表里操作节点存成 核赔（VClaim）
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
	 * 对车辆类型进行转换
	 * @param itemCarVo
	 * @return
	 */
	private String transCarType(PrpLCItemCarVo itemCarVo) {
		// 车辆大类
		String carKindCode = null;
		// 车辆性质
		String userKindCode = null;
		// 交强险车辆类型
		// 00-其他险默认值，01-家庭用车，02-非营业客车，03-营业客车，04-非营业货车，05-营业货车，06-特种车，07-摩托车，08-拖拉机，09-挂车
		String carNatureCode = null;
		if (itemCarVo != null) {
			carKindCode = itemCarVo.getCarKindCode();
			userKindCode = itemCarVo.getUseKindCode();
			// 判断获取交强险车辆类型 start
			if (carKindCode != null && carKindCode != "") {
				carKindCode = carKindCode.substring(0, 1);
				// A -客车, H -货车, M -摩托车, J-拖拉机
				if ("A".equals(carKindCode)) {
					// 002-非营业
					if ("002".equals(userKindCode)) {
						carNatureCode = "02";
					} else {
						carNatureCode = "03";
					}
				} else if ("H".equals(carKindCode)) {
					// 002-非营业
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
	 * 判断是否平安案件
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

