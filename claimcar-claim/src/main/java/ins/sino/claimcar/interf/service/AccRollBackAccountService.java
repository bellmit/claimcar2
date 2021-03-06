package ins.sino.claimcar.interf.service;



import ins.framework.lang.Springs;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.carplatform.util.StringUtil;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.interf.vo.AccRollBackVo;
import ins.sino.claimcar.interf.vo.PayReturnVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.other.service.AccountInfoService;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpDAccRollBackAccountVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.pay.service.PadPayService;

import java.math.BigDecimal;
import java.util.*;

import javax.jws.WebService;

import ins.sino.claimcar.pinganUnion.service.PingAnPayCallBackService;
import ins.sino.claimcar.pinganunion.vo.payCallback.UnionPayCallbackRequestListDto;
import ins.sino.claimcar.pinganunion.vo.payCallback.UnionPayCallbackRequestParamDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.alibaba.dubbo.common.utils.Assert;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;


@WebService(serviceName="accRollBackAccount")
public class AccRollBackAccountService extends SpringBeanAutowiringSupport {

	private AccountInfoService accountInfoService;
	private CompensateTaskService compensateTaskService;
	private CompensateService compensateService;
	private PadPayService padPayService;
	private ManagerService managerService;
	private CodeTranService codeTranService;
	private AcheckService acheckService;
	private AssessorService assessorService;
	@Autowired
	private PingAnPayCallBackService pingAnPayCallBackService;
	private static Logger logger = LoggerFactory.getLogger(AccRollBackAccountService.class);
	
	public String saveAccRollBackAccountForXml(String xml) {
		this.init();
		logger.info("????????????????????? "+xml);
		AccRollBackVo accRollBackVo = new AccRollBackVo();
		PayReturnVo payReturnVo = new PayReturnVo();
		payReturnVo.setErrorMessage("??????");
		payReturnVo.setResponseCode(true);
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// ?????? class??????
		try{
			stream.processAnnotations(AccRollBackVo.class);
			accRollBackVo = (AccRollBackVo)stream.fromXML(xml);
			this.saveAccRollBackAccount(accRollBackVo);
		}
		catch(Exception e){
			payReturnVo.setResponseCode(false);
			payReturnVo.setErrorMessage(e.getMessage());
			logger.error("??????????????????", e);
		}
		stream.processAnnotations(PayReturnVo.class);
		return stream.toXML(payReturnVo);
	}
	
	private void  saveAccRollBackAccount(AccRollBackVo accRollBackVo)throws Exception{
		Assert.notNull(adjustContent(accRollBackVo.getCertiNo()),"???????????????");
		Assert.notNull(adjustContent(accRollBackVo.getAccountId()),"????????????ID??????");
		Assert.notNull(adjustContent(accRollBackVo.getStatus()),"???????????????");
		PrpDAccRollBackAccountVo accRollBaccVo = new PrpDAccRollBackAccountVo();
		Beans.copy().from(accRollBackVo).to(accRollBaccVo);
		accRollBaccVo.setRollBackTime(DateUtils.strToDate(accRollBackVo.getRollBackTime()));
		accountInfoService.saveAccRollBackAccount(accRollBaccVo);
		//???????????????
		if("P62".equals(accRollBaccVo.getPayType()) && StringUtils.isNotBlank(accRollBaccVo.getCertiNo())){
			List<PrpLCheckFeeVo> checkFees=acheckService.findPrpLCheckFeeVoByBussNo(accRollBaccVo.getCertiNo());
			if(checkFees!=null && checkFees.size()>0){
				for(PrpLCheckFeeVo checkFeeVo:checkFees){
					checkFeeVo.setTaskStatus("2");
					acheckService.updateCheckFee(checkFeeVo);
				}
			}
		}
		//???????????????
		if("P67".equals(accRollBaccVo.getPayType()) && StringUtils.isNotBlank(accRollBaccVo.getCertiNo())){
			List<PrpLAssessorFeeVo> asseorFeeVos=assessorService.findPrpLAssessorFeeVoByCompensateNoOrEndNo(accRollBaccVo.getCertiNo());
			if(asseorFeeVos!=null && asseorFeeVos.size()>0){
				for(PrpLAssessorFeeVo asseorFeeVo:asseorFeeVos){
					asseorFeeVo.setTaskStatus("2");
					assessorService.updateAssessorFee(asseorFeeVo);
				}
			}
		}
		
		//?????????????????????????????????????????????
	/*	if("P60".equals(accRollBaccVo.getPayType())){
		//??????
		PrpLCompensateVo compensateVo = compensateTaskService.queryCompByPK(accRollBaccVo.getCertiNo());
		for(PrpLPaymentVo paymentVo : compensateVo.getPrpLPayments()){
				paymentVo.setPayStatus("1");
				//????????????
				this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), paymentVo.getId(),"1");
			}
		}*/
		
		//????????????
		
		if("P".equals(accRollBaccVo.getCertiNo().substring(0,1))){
		    if("P6K".equals(accRollBaccVo.getPayType())){//??????
		        List<PrpLPadPayMainVo> padPayVoList = padPayService.findPadPayMainBySettleNo(accRollBaccVo.getCertiNo());
		        /*if(prpLPadPayPersonVoList != null && prpLPadPayPersonVoList.size() > 0){
		            padPayService.findPadPayMainById(prpLPadPayPersonVoList.get(0).getPrpLPadPayMain().getId());
		        }*/
		        //??????????????????
		        if(padPayVoList != null && padPayVoList.size() > 0){
		            logger.info("??????=============================1");
		            for(PrpLPadPayMainVo vo : padPayVoList){
		                PrpLPadPayMainVo padPayVo = vo;
		                Map<String,Long> payIdMap = new HashMap<String, Long>();
						//????????????????????????Map
						Map<String, Object> callBackDataMap = new HashMap<String, Object>();
						//??????????????????????????????
						List<UnionPayCallbackRequestListDto> callbackRequestListDtos = new ArrayList<UnionPayCallbackRequestListDto>();
	                    List<PrpLPayCustomVo> customList = managerService.findPayCustomVoByRegistNo(padPayVo.getRegistNo());
	                    if(customList!=null && !customList.isEmpty()){
	                        for(PrpLPayCustomVo customVo : customList){
	                            payIdMap.put(customVo.getAccountId(), customVo.getId());
	                        }
	                    }
	                    if(padPayVo!=null){
	                        //????????????
	                        for(PrpLPadPayPersonVo PadPayPersonVo:padPayVo.getPrpLPadPayPersons()){
	                            //?????????????????????id ??????????????????
	                            Long payeeid = payIdMap.get(accRollBaccVo.getAccountId());
	                            if(PadPayPersonVo.getPayeeId().equals(payeeid)){
	                                if(BigDecimal.ZERO.compareTo(PadPayPersonVo.getCostSum())==-1){
	                                    PadPayPersonVo.setPayStatus("3");
	                                    this.savePrplPayHis(padPayVo.getClaimNo(),padPayVo.getCompensateNo(), PadPayPersonVo.getId(),"3","D",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
	                                    logger.info("??????=============================2");
	                                }else{
	                                    logger.info("??????=============================4");
	                                    this.savePrplPayHis(padPayVo.getClaimNo(),padPayVo.getCompensateNo(), PadPayPersonVo.getId(),"0","D",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
	                                }
	                                //????????????????????????
									String idClmPaymentResult = PadPayPersonVo.getIdClmPaymentResult();
	                                if (StringUtils.isNotBlank(idClmPaymentResult)) {
										addPingAnCallbackData(accRollBackVo, callbackRequestListDtos, idClmPaymentResult);
									}
								}
	                        }
							if (!callbackRequestListDtos.isEmpty()) {
								UnionPayCallbackRequestParamDto unionPayCallbackRequestParamDto = new UnionPayCallbackRequestParamDto();
								unionPayCallbackRequestParamDto.setList(callbackRequestListDtos);
								callBackDataMap.put(padPayVo.getRegistNo(), unionPayCallbackRequestParamDto);
								//????????????????????????
								logger.info("????????????????????????????????????????????? ????????????" + new Date());
								pingAnPayCallBackService.payCallBackDataBuild(callBackDataMap);
								logger.info("????????????????????????????????????????????? ????????????" + new Date());
							}
	                        logger.info("??????=============================3");
	                        padPayService.save(padPayVo, null, null);
	                    }
		            }
		        }

		    }else{
		        List<PrpLPrePayVo> prePayVoList = compensateTaskService.findPrpLPrePayVoBySettleNo(accRollBaccVo.getCertiNo());
		        if(prePayVoList != null && prePayVoList.size() > 0){//??????
                   // List<PrpLPrePayVo> prePayVoList = compensateTaskService.queryPrePay(accRollBaccVo.getCertiNo());
                    for(PrpLPrePayVo prePayFVo:prePayVoList){
						//????????????????????????Map
						Map<String, Object> callBackDataMap = new HashMap<String, Object>();
						//??????????????????????????????
						List<UnionPayCallbackRequestListDto> callbackRequestListDtos = new ArrayList<UnionPayCallbackRequestListDto>();
                        PrpLCompensateVo compensateVo = compensateTaskService.findPrpLCompensateVoByPK(prePayFVo.getCompensateNo());
                        Map<String,Long> payIdMap = new HashMap<String, Long>();
                        List<PrpLPayCustomVo> customList = managerService.findPayCustomVoByRegistNo(compensateVo.getRegistNo());
                        if(customList!=null && !customList.isEmpty()){
                            for(PrpLPayCustomVo customVo : customList){
                                payIdMap.put(customVo.getAccountId(), customVo.getId());
                            }
                        }
                        logger.info("??????=============================1");
                        //?????????????????????id ??????????????????
                        Long payeeid = payIdMap.get(accRollBaccVo.getAccountId());
                        if("P".equals(prePayFVo.getFeeType())){
                            if(prePayFVo.getPayeeId().equals(payeeid)){
                                if(BigDecimal.ZERO.compareTo(prePayFVo.getPayAmt())==-1){
                                    prePayFVo.setPayStatus("3");
                                    this.savePrplPayHis(compensateVo.getClaimNo(),prePayFVo.getCompensateNo(), prePayFVo.getId(),"3","Y",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
                                    compensateService.updatePrpLPrePay(prePayFVo,"3");
                                    logger.info("??????=============================2");
                                }else{
                                    logger.info("??????=============================3");
                                    this.savePrplPayHis(compensateVo.getClaimNo(),prePayFVo.getCompensateNo(), prePayFVo.getId(),"0","Y",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
                                }
                                //????????????????????????
								String idClmPaymentResult = prePayFVo.getIdClmPaymentResult();
								if (StringUtils.isNotBlank(idClmPaymentResult)) {
									addPingAnCallbackData(accRollBackVo, callbackRequestListDtos, idClmPaymentResult);
								}
							}
                        }else{
                            Map<String,SysCodeDictVo> dictTransMap = codeTranService.findCodeDictTransMap("ChargeCode", "");
                            SysCodeDictVo sysCodeDictVo = dictTransMap.get(prePayFVo.getChargeCode());
                            if(prePayFVo.getPayeeId().equals(payeeid) && accRollBaccVo.getPayType().equals(sysCodeDictVo.getProperty1())){
                                if(BigDecimal.ZERO.compareTo(prePayFVo.getPayAmt())==-1){
                                    logger.info("??????=============================4");
                                    prePayFVo.setPayStatus("3");
                                    this.savePrplPayHis(compensateVo.getClaimNo(),prePayFVo.getCompensateNo(), prePayFVo.getId(),"3","Y",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
                                    compensateService.updatePrpLPrePay(prePayFVo,"3");
                                }else{
                                    logger.info("??????=============================5");
                                    this.savePrplPayHis(compensateVo.getClaimNo(),prePayFVo.getCompensateNo(), prePayFVo.getId(),"0","Y",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
                                }
								//????????????????????????
								String idClmPaymentResult = prePayFVo.getIdClmPaymentResult();
								if (StringUtils.isNotBlank(idClmPaymentResult)) {
									addPingAnCallbackData(accRollBackVo, callbackRequestListDtos, idClmPaymentResult);
								}
                            }
                        }
						if (!callbackRequestListDtos.isEmpty()) {
							UnionPayCallbackRequestParamDto unionPayCallbackRequestParamDto = new UnionPayCallbackRequestParamDto();
							unionPayCallbackRequestParamDto.setList(callbackRequestListDtos);
							callBackDataMap.put(compensateVo.getRegistNo(), unionPayCallbackRequestParamDto);
							//????????????????????????
							logger.info("????????????????????????????????????????????? ????????????" + new Date());
							pingAnPayCallBackService.payCallBackDataBuild(callBackDataMap);
							logger.info("????????????????????????????????????????????? ????????????" + new Date());
						}
                    }
		        }else{
		            List<PrpLPaymentVo> prpLPaymentVoList = compensateTaskService.findPrpLPaymentVoBySettleNo(accRollBaccVo.getCertiNo());
		            List<PrpLChargeVo> prpLChargeVoList = compensateTaskService.findPrpLChargeVoBySettleNo(accRollBaccVo.getCertiNo());

		            PrpLCompensateVo compensateVo = new PrpLCompensateVo();
		            Map<String,PrpLCompensateVo> compensateVoMap = new HashMap<String,PrpLCompensateVo>();//???????????????????????????
		            if(prpLPaymentVoList!=null && prpLPaymentVoList.size()>0){
		                List<PrpLCompensateVo> prpLCompensateVoList = compensateTaskService.findCompensateBySettleNo(accRollBaccVo.getCertiNo(),"P");
                        if(prpLCompensateVoList!=null && prpLCompensateVoList.size() >0){
                            for(PrpLCompensateVo vo : prpLCompensateVoList){
                                if(!compensateVoMap.containsKey(vo.getCompensateNo())){
                                    compensateVoMap.put(vo.getCompensateNo(),vo);
                                }
                            }
                            //compensateVo = prpLCompensateVoList.get(0);
                        }
	                   /* List<PrpLPayCustomVo> customList = managerService.findPayCustomVoByRegistNo(compensateVo.getRegistNo());
	                    if(customList!=null && !customList.isEmpty()){
	                        for(PrpLPayCustomVo customVo : customList){
	                            payIdMap.put(customVo.getAccountId(), customVo.getId());
	                        }
	                    }*/
		            }
		            if(prpLChargeVoList!=null && prpLChargeVoList.size()>0){
		                List<PrpLCompensateVo> prpLCompensateVoList = compensateTaskService.findCompensateBySettleNo(accRollBaccVo.getCertiNo(),"F");
		                if(prpLCompensateVoList!=null && prpLCompensateVoList.size() >0){
		                    for(PrpLCompensateVo vo : prpLCompensateVoList){
                                if(!compensateVoMap.containsKey(vo.getCompensateNo())){
                                    compensateVoMap.put(vo.getCompensateNo(),vo);
                                }
                            }
		                    //compensateVo = prpLCompensateVoList.get(0);
		                }

		            }

		            if(!compensateVoMap.isEmpty()){
		                Set<String> keSet=compensateVoMap.keySet();
		                for (Iterator<String> iterator = keSet.iterator(); iterator.hasNext();) {
							//????????????????????????Map
							Map<String, Object> callBackDataMap = new HashMap<String, Object>();
							//??????????????????????????????
							List<UnionPayCallbackRequestListDto> callbackRequestListDtos = new ArrayList<UnionPayCallbackRequestListDto>();
		                    String compensateNo = iterator.next();
		                    compensateVo = compensateVoMap.get(compensateNo);
		                    Map<String,Long> payIdMap = new HashMap<String, Long>();
		                    List<PrpLPayCustomVo> customList = managerService.findPayCustomVoByRegistNo(compensateVo.getRegistNo());
                            if(customList!=null && !customList.isEmpty()){
                                for(PrpLPayCustomVo customVo : customList){
                                    payIdMap.put(customVo.getAccountId(), customVo.getId());
                                }
                            }

		                    boolean payFlags = false;
		                    boolean feeFlags = false;
		                    if(compensateVo.getPrpLPayments() != null && compensateVo.getPrpLPayments().size() >0){
		                        payFlags = true;
		                    }
		                    if(compensateVo.getPrpLCharges() != null && compensateVo.getPrpLCharges().size() >0){
		                        feeFlags = true;
		                    }
		                    logger.info("??????=========??????1========??????======="+payFlags+feeFlags);
		                    if(payFlags || feeFlags){
		                        //?????????????????????
		                        if("P60".equals(accRollBaccVo.getPayType())||"P6B".equals(accRollBaccVo.getPayType())
		                                ||"P6D".equals(accRollBaccVo.getPayType())){//??????
		                            for(PrpLPaymentVo paymentVo : compensateVo.getPrpLPayments()){
		                                //?????????????????????id ??????????????????
		                                Long payeeid = payIdMap.get(accRollBaccVo.getAccountId());

		                                if(paymentVo.getPayeeId().equals(payeeid)){
		                                    if(BigDecimal.ZERO.compareTo(paymentVo.getSumRealPay())==-1){
		                                        logger.info("??????=========??????2========??????=======");
		                                        paymentVo.setPayStatus("3");
		                                        compensateService.updatePrpLPaymentVo(paymentVo);
		                                        this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), paymentVo.getId(),"3","P",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
		                                    }else{
		                                        logger.info("??????=========??????3========??????=======");
		                                        this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), paymentVo.getId(),"0","P",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
		                                    }
		                                    //????????????????????????
											String idClmPaymentResult = paymentVo.getIdClmPaymentResult();
		                                    if (StringUtils.isNotBlank(idClmPaymentResult)) {
												addPingAnCallbackData(accRollBackVo, callbackRequestListDtos, idClmPaymentResult);
											}
		                                }
		                            }
		                        }else{
		                            for(PrpLChargeVo chargeVo:compensateVo.getPrpLCharges()){
		                                //?????????????????????id ??????????????????
		                                Map<String,SysCodeDictVo> dictTransMap = codeTranService.findCodeDictTransMap("ChargeCode", "");
		                                SysCodeDictVo sysCodeDictVo = dictTransMap.get(chargeVo.getChargeCode());
		                                Long payeeid = payIdMap.get(accRollBaccVo.getAccountId());
		                                if(chargeVo.getPayeeId().equals(payeeid) && accRollBaccVo.getPayType().equals(sysCodeDictVo.getProperty2())){
		                                    if(BigDecimal.ZERO.compareTo(chargeVo.getFeeRealAmt())==-1){
		                                        logger.info("??????=========??????4========??????=======");
		                                        chargeVo.setPayStatus("3");
		                                        compensateService.updatePrpLCharges(chargeVo);
		                                        this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), chargeVo.getId(),"3","F",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
		                                    }else{
		                                        logger.info("??????=========??????5========??????=======");
		                                        this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), chargeVo.getId(),"0","F",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
		                                    }
											//????????????????????????
											String idClmPaymentResult = chargeVo.getIdClmPaymentResult();
											if (StringUtils.isNotBlank(idClmPaymentResult)) {
												addPingAnCallbackData(accRollBackVo, callbackRequestListDtos, idClmPaymentResult);
											}
		                                }
		                            }
		                        }
								if (!callbackRequestListDtos.isEmpty()) {
									UnionPayCallbackRequestParamDto unionPayCallbackRequestParamDto = new UnionPayCallbackRequestParamDto();
									unionPayCallbackRequestParamDto.setList(callbackRequestListDtos);
									callBackDataMap.put(compensateVo.getRegistNo(), unionPayCallbackRequestParamDto);
									//????????????????????????
									logger.info("??????/????????????????????????????????????????????? ????????????" + new Date());
									pingAnPayCallBackService.payCallBackDataBuild(callBackDataMap);
									logger.info("??????/????????????????????????????????????????????? ????????????" + new Date());
								}
		                        logger.info("??????=========??????6========??????=======");
		                        compensateTaskService.paymentWriteBackCompVo(compensateVo);
		                    }
		                }
		            }
		        }
		    }
		}else{
		    Map<String,Long> payIdMap = new HashMap<String, Long>();
    		if(accRollBaccVo.getCertiNo().startsWith("D")){//??????????????????
    			PrpLPadPayMainVo padPayVo = padPayService.findPadPayMainByCompNo(accRollBaccVo.getCertiNo());
    			List<PrpLPayCustomVo> customList = managerService.findPayCustomVoByRegistNo(padPayVo.getRegistNo());
    			if(customList!=null && !customList.isEmpty()){
    				for(PrpLPayCustomVo customVo : customList){
    					payIdMap.put(customVo.getAccountId(), customVo.getId());
    				}
    			}
    			if(padPayVo!=null){
					//????????????????????????Map
					Map<String, Object> callBackDataMap = new HashMap<String, Object>();
					//??????????????????????????????
					List<UnionPayCallbackRequestListDto> callbackRequestListDtos = new ArrayList<UnionPayCallbackRequestListDto>();
    				//????????????
    				for(PrpLPadPayPersonVo PadPayPersonVo:padPayVo.getPrpLPadPayPersons()){
    					//?????????????????????id ??????????????????
    					Long payeeid = payIdMap.get(accRollBaccVo.getAccountId());
    					if(PadPayPersonVo.getPayeeId().equals(payeeid)){
    						if(BigDecimal.ZERO.compareTo(PadPayPersonVo.getCostSum())==-1){
    							PadPayPersonVo.setPayStatus("3");
    							this.savePrplPayHis(padPayVo.getClaimNo(),padPayVo.getCompensateNo(), PadPayPersonVo.getId(),"3","D",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
    						}else{
    							this.savePrplPayHis(padPayVo.getClaimNo(),padPayVo.getCompensateNo(), PadPayPersonVo.getId(),"0","D",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
    						}
    						//????????????????????????
							String idClmPaymentResult = PadPayPersonVo.getIdClmPaymentResult();
							if (StringUtils.isNotBlank(idClmPaymentResult)) {
								addPingAnCallbackData(accRollBackVo, callbackRequestListDtos, idClmPaymentResult);
							}
    					}
    				}
					if (!callbackRequestListDtos.isEmpty()) {
						UnionPayCallbackRequestParamDto unionPayCallbackRequestParamDto = new UnionPayCallbackRequestParamDto();
						unionPayCallbackRequestParamDto.setList(callbackRequestListDtos);
						callBackDataMap.put(padPayVo.getRegistNo(), unionPayCallbackRequestParamDto);
						//????????????????????????
						logger.info("????????????????????????????????????????????? ????????????" + new Date());
						pingAnPayCallBackService.payCallBackDataBuild(callBackDataMap);
						logger.info("????????????????????????????????????????????? ????????????" + new Date());
					}
    				padPayService.save(padPayVo, null, null);
    			}
    		}else{
    			PrpLCompensateVo compensateVo = compensateTaskService.findPrpLCompensateVoByPK(accRollBaccVo.getCertiNo());
    			List<PrpLPayCustomVo> customList = managerService.findPayCustomVoByRegistNo(compensateVo.getRegistNo());
    			if(customList!=null && !customList.isEmpty()){
    				for(PrpLPayCustomVo customVo : customList){
    					payIdMap.put(customVo.getAccountId(), customVo.getId());
    				}
    			}
    			boolean payFlags = false;
    			boolean feeFlags = false;
    			if(compensateVo.getPrpLPayments() != null && compensateVo.getPrpLPayments().size() >0){
    			    payFlags = true;
    			}
    			if(compensateVo.getPrpLCharges() != null && compensateVo.getPrpLCharges().size() >0){
    			    feeFlags = true;
                }
    			logger.info("??????=========??????========??????======="+payFlags+feeFlags);
    			if((!accRollBaccVo.getCertiNo().startsWith("Y"))&&(payFlags || feeFlags)){
					//????????????????????????Map
					Map<String, Object> callBackDataMap = new HashMap<String, Object>();
					//??????????????????????????????
					List<UnionPayCallbackRequestListDto> callbackRequestListDtos = new ArrayList<UnionPayCallbackRequestListDto>();
    				//?????????????????????
    				if("P60".equals(accRollBaccVo.getPayType())||"P6B".equals(accRollBaccVo.getPayType())
    						||"P6D".equals(accRollBaccVo.getPayType())){//??????
    					for(PrpLPaymentVo paymentVo : compensateVo.getPrpLPayments()){
    						//?????????????????????id ??????????????????
    						Long payeeid = payIdMap.get(accRollBaccVo.getAccountId());
    					
    						if(paymentVo.getPayeeId().equals(payeeid)){
    							if(BigDecimal.ZERO.compareTo(paymentVo.getSumRealPay())==-1){
    								paymentVo.setPayStatus("3");
    								compensateService.updatePrpLPaymentVo(paymentVo);
    								this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), paymentVo.getId(),"3","P",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
    							}else{
    								this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), paymentVo.getId(),"0","P",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
    							}
    							//????????????????????????
								String idClmPaymentResult = paymentVo.getIdClmPaymentResult();
    							if (StringUtils.isNotBlank(idClmPaymentResult)) {
									addPingAnCallbackData(accRollBackVo, callbackRequestListDtos, idClmPaymentResult);
								}
    						}
    					}
    				}else{
    					for(PrpLChargeVo chargeVo:compensateVo.getPrpLCharges()){
    						//?????????????????????id ??????????????????
    						Map<String,SysCodeDictVo> dictTransMap = codeTranService.findCodeDictTransMap("ChargeCode", "");
    						SysCodeDictVo sysCodeDictVo = dictTransMap.get(chargeVo.getChargeCode());
    						Long payeeid = payIdMap.get(accRollBaccVo.getAccountId());
    						if(chargeVo.getPayeeId().equals(payeeid) && accRollBaccVo.getPayType().equals(sysCodeDictVo.getProperty2())){
    							if(BigDecimal.ZERO.compareTo(chargeVo.getFeeRealAmt())==-1){
    								chargeVo.setPayStatus("3");
    								compensateService.updatePrpLCharges(chargeVo);
    								this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), chargeVo.getId(),"3","F",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
    							}else{
    								this.savePrplPayHis(compensateVo.getClaimNo(),compensateVo.getCompensateNo(), chargeVo.getId(),"0","F",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
    							}
    							//????????????????????????
								String idClmPaymentResult = chargeVo.getIdClmPaymentResult();
								if (StringUtils.isNotBlank(idClmPaymentResult)) {
									addPingAnCallbackData(accRollBackVo, callbackRequestListDtos, idClmPaymentResult);
								}

    						}
    					}
    				}
					if (!callbackRequestListDtos.isEmpty()) {
						UnionPayCallbackRequestParamDto unionPayCallbackRequestParamDto = new UnionPayCallbackRequestParamDto();
						unionPayCallbackRequestParamDto.setList(callbackRequestListDtos);
						callBackDataMap.put(compensateVo.getRegistNo(), unionPayCallbackRequestParamDto);
						//????????????????????????
						logger.info("??????????????????????????????????????????????????? ????????????" + new Date());
						pingAnPayCallBackService.payCallBackDataBuild(callBackDataMap);
						logger.info("??????????????????????????????????????????????????? ????????????" + new Date());
					}
    				compensateTaskService.paymentWriteBackCompVo(compensateVo);
    			}else{//??????
    			    logger.info("??????=============================");
					//????????????????????????Map
					Map<String, Object> callBackDataMap = new HashMap<String, Object>();
					//??????????????????????????????
					List<UnionPayCallbackRequestListDto> callbackRequestListDtos = new ArrayList<UnionPayCallbackRequestListDto>();
    				List<PrpLPrePayVo> prePayVoList = compensateTaskService.queryPrePay(accRollBaccVo.getCertiNo());
    				for(PrpLPrePayVo prePayFVo:prePayVoList){
    					//?????????????????????id ??????????????????
    					Long payeeid = payIdMap.get(accRollBaccVo.getAccountId());
    					if("P".equals(prePayFVo.getFeeType())){
    						if(prePayFVo.getPayeeId().equals(payeeid)){
    							if(BigDecimal.ZERO.compareTo(prePayFVo.getPayAmt())==-1){
    								prePayFVo.setPayStatus("3");
    								this.savePrplPayHis(compensateVo.getClaimNo(),prePayFVo.getCompensateNo(), prePayFVo.getId(),"3","Y",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
    							}else{
    								this.savePrplPayHis(compensateVo.getClaimNo(),prePayFVo.getCompensateNo(), prePayFVo.getId(),"0","Y",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
    							}
								String idClmPaymentResult = prePayFVo.getIdClmPaymentResult();
								if (StringUtils.isNotBlank(idClmPaymentResult)) {
									addPingAnCallbackData(accRollBackVo, callbackRequestListDtos, idClmPaymentResult);
								}
							}
    					}else{
    						Map<String,SysCodeDictVo> dictTransMap = codeTranService.findCodeDictTransMap("ChargeCode", "");
    						SysCodeDictVo sysCodeDictVo = dictTransMap.get(prePayFVo.getChargeCode());
    						if(prePayFVo.getPayeeId().equals(payeeid) && accRollBaccVo.getPayType().equals(sysCodeDictVo.getProperty1())){
    							if(BigDecimal.ZERO.compareTo(prePayFVo.getPayAmt())==-1){
    								prePayFVo.setPayStatus("3");
    								this.savePrplPayHis(compensateVo.getClaimNo(),prePayFVo.getCompensateNo(), prePayFVo.getId(),"3","Y",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
    							}else{
    								this.savePrplPayHis(compensateVo.getClaimNo(),prePayFVo.getCompensateNo(), prePayFVo.getId(),"0","Y",DateUtils.strToDate(accRollBackVo.getRollBackTime()));
    							}
    							//????????????????????????
								String idClmPaymentResult = prePayFVo.getIdClmPaymentResult();
								if (StringUtils.isNotBlank(idClmPaymentResult)) {
									addPingAnCallbackData(accRollBackVo, callbackRequestListDtos, idClmPaymentResult);
								}
    						}
    					}
    				}
					if (!callbackRequestListDtos.isEmpty()) {
						UnionPayCallbackRequestParamDto unionPayCallbackRequestParamDto = new UnionPayCallbackRequestParamDto();
						unionPayCallbackRequestParamDto.setList(callbackRequestListDtos);
						callBackDataMap.put(compensateVo.getRegistNo(), unionPayCallbackRequestParamDto);
						//????????????????????????
						logger.info("????????????????????????????????????????????????????????? ????????????" + new Date());
						pingAnPayCallBackService.payCallBackDataBuild(callBackDataMap);
						logger.info("????????????????????????????????????????????????????????? ????????????" + new Date());
					}
    				compensateService.writeBackPay(prePayVoList, accRollBaccVo.getCertiNo());
    			}
    		}
        }
	}

	private void addPingAnCallbackData(AccRollBackVo accRollBackVo, List<UnionPayCallbackRequestListDto> callbackRequestListDtos, String idClmPaymentResult) {
		UnionPayCallbackRequestListDto piccCallBakcData = new UnionPayCallbackRequestListDto();
		piccCallBakcData.setBackDate(accRollBackVo.getRollBackTime());
		piccCallBakcData.setIdClmPaymentResult(idClmPaymentResult);
		piccCallBakcData.setBackReason(accRollBackVo.getErrorMessage());
		piccCallBakcData.setNoticeStatus("01");
		callbackRequestListDtos.add(piccCallBakcData);
	}

	private String adjustContent(String content){
		String reContent = null;
		if(content!=null&&!"".equals(content)&&!content.isEmpty()){
			reContent = content;
		}
		return reContent;
	}

	/**
	 * webserive ????????? service???????????????
	 */
	private void init() {
		if(accountInfoService==null){
			accountInfoService=(AccountInfoService)Springs.getBean(AccountInfoService.class);
		}
		if(compensateTaskService==null){
			compensateTaskService=(CompensateTaskService)Springs.getBean(CompensateTaskService.class);
		}
		if(compensateService==null){
			compensateService=(CompensateService)Springs.getBean(CompensateService.class);
		}
		if(padPayService==null){
			padPayService=(PadPayService)Springs.getBean(PadPayService.class);
		}
		if(managerService==null){
			managerService=(ManagerService)Springs.getBean(ManagerService.class);
		}
		if(codeTranService==null){
			codeTranService=(CodeTranService)Springs.getBean(CodeTranService.class);
		}
		if(acheckService==null){
			acheckService=(AcheckService)Springs.getBean(AcheckService.class);
		}
		if(assessorService==null){
			assessorService=(AssessorService)Springs.getBean(AssessorService.class);
		}
		if (pingAnPayCallBackService == null) {
			pingAnPayCallBackService = (PingAnPayCallBackService) Springs.getBean(PingAnPayCallBackService.class);
		}
	}

	public void savePrplPayHis(String claimNo,String compensateNo,Long id,String flags,String hisType,Date inputTime) {
		compensateService.savePrplPayHis(claimNo,compensateNo, id,flags,hisType,inputTime);
	}
}
