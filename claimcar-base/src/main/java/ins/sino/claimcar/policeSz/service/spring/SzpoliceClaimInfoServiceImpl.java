package ins.sino.claimcar.policeSz.service.spring;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tc.qrpay.openapi.sdk.OpenApiClient;
import com.tc.qrpay.openapi.sdk.protocol.OpenApiRsp;
import com.tc.qrpay.openapi.sdk.util.SdkException;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.base.po.PrpLCompensate;
import ins.sino.claimcar.base.po.PrpLPayment;
import ins.sino.claimcar.carinterface.po.ClaimInterfaceLog;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.platform.service.PlatformReUploadService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.trafficplatform.service.SzpoliceCaseService;
import ins.sino.claimcar.trafficplatform.service.SzpoliceClaimInfoService;
import ins.sino.claimcar.trafficplatform.vo.AccidentResInfo;
import ins.sino.claimcar.trafficplatform.vo.ClaimInfoVo;
import ins.sino.claimcar.trafficplatform.vo.PayDataVo;
import ins.sino.claimcar.trafficplatform.vo.PlateDataVo;
@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"} , timeout = 1000*60*10)
@Path("szpoliceClaimInfoService")
public class SzpoliceClaimInfoServiceImpl implements SzpoliceClaimInfoService{
	
	private static Logger logger = LoggerFactory.getLogger(SzpoliceClaimInfoServiceImpl.class);
	
	@Autowired
	private SzpoliceCaseService szpoliceCaseService;
	@Autowired
	ClaimInterfaceLogService interfaceLogService;
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	PayCustomService payCustomService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	PlatformReUploadService reUploadService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	PadPayService padPayService;
	@SuppressWarnings({"static-access","rawtypes"})
	@Override
	public void settleClaimUpload() {
		long t1 = System.currentTimeMillis();
		String gateway = SpringProperties.getProperty("SZWARN_URL");
		String appId = SpringProperties.getProperty("SZWARN_APPID");
		String appKey = SpringProperties.getProperty("SZWARN_APPKEY");
		String method = SpringProperties.getProperty("SZWARN_METHOD_CLAIM");
		boolean isDataEncrypt = true;
		boolean isSign = true;
		List<ClaimInfoVo> claimInfoVos = forParams();
		if(claimInfoVos!=null && claimInfoVos.size()>0){
			for(ClaimInfoVo vo:claimInfoVos){
				PrpLRegistVo registVo = registQueryService.findByRegistNo(vo.getWarningNo());
				if(registVo!=null){
					if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
						continue;
					}
				}
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				JSONObject json = new JSONObject();
				json.put("insuranceCode",appId);
				json.put("claimNo",vo.getClaimNo());
				json.put("policyNo",vo.getPolicyNo());
				json.put("insuranceType",vo.getInsuranceType());
				json.put("accidentNo",vo.getAccidentNo());
				json.put("warningNo",vo.getWarningNo());
				json.put("payAmout",vo.getPayAmout());
				JSONArray palateArray = new JSONArray();
				palateArray.addAll(vo.getPlateData());
				json.put("plateData",palateArray);
				JSONArray payArray = new JSONArray();
				payArray.addAll(vo.getPayData());
				json.put("payData",payArray);
				OpenApiClient open = new OpenApiClient(gateway,appId,appKey,isDataEncrypt,isSign);
				OpenApiRsp responseData = new OpenApiRsp();
				try{
					responseData = open.execute(method,json.toString());
					if(responseData!=null){
						if("0000".equals(responseData.getReturnCode())){
							logVo.setStatus("1");
							logVo.setRequestXml(json.toJSONString());
							logVo.setResponseXml(JSON.toJSON(responseData).toString());
						}else{
							logVo.setStatus("0");
							logVo.setErrorMessage(responseData.getReturnMsg());
							logVo.setRequestXml(json.toJSONString());
							logVo.setResponseXml(JSON.toJSON(responseData).toString());
						}
					}else{
						logVo.setStatus("0");
						logVo.setErrorMessage("与深圳警保平台连接失败!");
						logVo.setRequestXml(json.toJSONString());
					}
				}catch(SdkException e){
					logVo.setStatus("0");
					logVo.setErrorMessage(e.getMessage());
					logVo.setRequestXml(json.toJSONString());
					logVo.setResponseXml(responseData!=null?JSON.toJSON(responseData).toString():"");
					logger.error("错误信息=========》",e);
				}finally{
					logVo.setClaimNo(vo.getClaimNo1());// 立案号
					logVo.setRegistNo(vo.getWarningNo());// 报案号
					if("1101".equals(vo.getClaimNo1().substring(11,15))){
						logVo.setBusinessType(BusinessType.SZClaim_CI.name());
						logVo.setBusinessName(BusinessType.SZClaim_CI.getName());
					}else{
						logVo.setBusinessType(BusinessType.SZClaim_BI.name());
						logVo.setBusinessName(BusinessType.SZClaim_BI.getName());
					}
					logVo.setCreateTime(new Date());
					logVo.setRequestTime(new Date());
					logVo.setRequestUrl(gateway);
					logVo.setCreateUser("0000000000");
					logVo.setComCode("00020000");
					interfaceLogService.save(logVo);
					logger.info("接口({})调用耗时{}ms"+( System.currentTimeMillis()-t1 ));
				}

			}
		}
		// 补送前一日失败的深圳警保理赔信息
		Map<String,Long> map = szpoliceCaseService.findClaimInterfaceLog();
		if(map!=null&&map.size()>0){
			for(Entry<String,Long> entry:map.entrySet()){
				settleClaimReUpload(entry.getKey(),"1",entry.getValue());
			}

		}

	}

	/**
	 * 查询出一天之中所有结案信息,组装深圳警保理赔信息上传请求接口信息
	 * @return
	 * @throws ParseException
	 */
	private List<ClaimInfoVo> forParams() {
		List<ClaimInfoVo> infoVos = new ArrayList<ClaimInfoVo>();
		try{
			Date nowDate = new Date();
			Calendar dar = Calendar.getInstance();
			dar.setTime(nowDate);
			dar.add(Calendar.DATE, -1);// 前一天时间
			Date Bdate = dar.getTime();
			SqlJoinUtils sqlUtils = new SqlJoinUtils();
			sqlUtils.append(" FROM PrpLCompensate compensate,PrpLPayment pay where compensate.compensateType= ? and compensate.underwriteFlag= ? ");
			sqlUtils.addParamValue("N");
			sqlUtils.addParamValue("1");
			sqlUtils.append(" and pay.prpLCompensate.compensateNo=compensate.compensateNo and pay.payTime >= ? ");
			sqlUtils.addParamValue(Bdate);
			sqlUtils.append(" and exists(select 1 from PrpLClaim claim where claim.claimNo=compensate.claimNo and claim.caseNo is not null )");
			sqlUtils.append(" and compensate.comCode like ? ");
			sqlUtils.addParamValue("0002%");
			Object[] params = sqlUtils.getParamValues();
			String sql = sqlUtils.getSql();
			List<PrpLCompensate> compenlists1=new ArrayList<PrpLCompensate>();
			List<PrpLPayment> prplpayments=new ArrayList<PrpLPayment>();
			Set<String> compensateNoSet=new HashSet<String>();
			List<Object[]> list = databaseDao.findAllByHql(sql, params);
			if(list!=null && list.size()>0){
				for(Object[] obj:list){
					PrpLCompensate prpLCompensate=(PrpLCompensate)obj[0];
					PrpLPayment prpLPayment=(PrpLPayment)obj[1];
					compenlists1.add(prpLCompensate);
					prplpayments.add(prpLPayment);
				}
			List<PrpLCompensate> compenlists=new ArrayList<PrpLCompensate>();
			if(compenlists1!=null && compenlists1.size()>0){
				for(PrpLCompensate po:compenlists1){
					if(compensateNoSet.add(po.getCompensateNo())){
						compenlists.add(po);
					}
				}
			}
			
			List<String> claimNoList = new ArrayList<String>();
			Set<String> claimNoSet = new HashSet<String>();
			Map<String,String> RegistNomap = new HashMap<String,String>();
			Map<String,String> riskcodemap = new HashMap<String,String>();
			Map<String,String> comCodemap = new HashMap<String,String>();
			if(compenlists!=null && compenlists.size()>0){
				for(PrpLCompensate comVo:compenlists){
					if(claimNoSet.add(comVo.getClaimNo())){// 立案号去重
						claimNoList.add(comVo.getClaimNo());
						RegistNomap.put(comVo.getClaimNo(),comVo.getRegistNo());
						riskcodemap.put(comVo.getClaimNo(),comVo.getRiskCode());
						comCodemap.put(comVo.getClaimNo(),comVo.getComCode());
					}
				}
			}
			if(claimNoList!=null && claimNoList.size()>0){
				for(String claimNo:claimNoList){
					BigDecimal sumAmt = new BigDecimal("0.00");
					ClaimInfoVo infoVo = new ClaimInfoVo();
					List<PlateDataVo> plateDatas = new ArrayList<PlateDataVo>();// 车牌信息 Y
					List<PayDataVo> payDatas = new ArrayList<PayDataVo>();// 赔款信息 Y
					infoVo.setInsuranceCode("DHIC");
					String flag = "0";//0表示不需查旧理赔，1表示需要查旧理赔
					if(RegistNomap.get(claimNo).length()==21){
						flag = "1";
					}
					String reqType_regist = Risk.DQZ.equals(riskcodemap.get(claimNo))? RequestType.RegistInfoCI.getCode():RequestType.RegistInfoBI.getCode();
					CiClaimPlatformLogVo registLog = reUploadService.findPlatformLog(reqType_regist,RegistNomap.get(claimNo), "1", flag);
					if(registLog!=null){
						infoVo.setClaimNo(registLog.getClaimSeqNo());//理赔编码
					}
					infoVo.setClaimNo1(claimNo);//立案号
					if(compenlists!=null&&compenlists.size()>0){
						for(PrpLCompensate comVo:compenlists){
							if(claimNo.equals(comVo.getClaimNo())){
								infoVo.setPolicyNo(comVo.getPolicyNo());
								if("1101".equals(comVo.getRiskCode())){
									infoVo.setInsuranceType("1");
								}else{
									infoVo.setInsuranceType("2");
								}

							}

						}
					}
					AccidentResInfo accidentVo = szpoliceCaseService.findAccidentResInfoByRegistNo(RegistNomap.get(claimNo));
					if(accidentVo!=null){
						infoVo.setAccidentNo(accidentVo.getAccidentNo());
					}
					infoVo.setWarningNo(RegistNomap.get(claimNo));
					infoVo=commonShare(RegistNomap,claimNo,prplpayments,infoVo);
					infoVo.setPayAmout(sumAmt.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
					List<PrpLDlossCarMainVo> losscarMainVos = lossCarService.findLossCarMainByRegistNo(RegistNomap.get(claimNo));

					if(losscarMainVos!=null&&losscarMainVos.size()>0){
						for(PrpLDlossCarMainVo mainVo:losscarMainVos){
							PrpLDlossCarInfoVo carInfoVo = lossCarService.findDefCarInfoByPk(mainVo.getCarId());
							PrpLCheckCarVo checkCarVo = checkTaskService.findByCheckId(mainVo.getCheckCarId());
							PlateDataVo plateDataVo = new PlateDataVo();
							if("25".equals(carInfoVo.getLicenseType())){
								plateDataVo.setPlateType("99");
							}else{
								plateDataVo.setPlateType(carInfoVo.getLicenseType());
							}
							plateDataVo.setPlateNo(carInfoVo.getLicenseNo());
							plateDataVo.setDriverName(carInfoVo.getDriveName());
							if(checkCarVo!=null){
								plateDataVo.setDriverPhone(checkCarVo.getPrpLCheckDriver().getLinkPhoneNumber());
							}else{
								PrpLCheckCarVo prplcheckcarVo = checkTaskService.findCheckCarBySerialNo(RegistNomap.get(claimNo),1);
								if(prplcheckcarVo!=null){
									plateDataVo.setDriverPhone(prplcheckcarVo.getPrpLCheckDriver().getLinkPhoneNumber());
								}
							}

							plateDataVo.setGarage(mainVo.getRepairFactoryName());
							plateDataVo.setFixedAmount(mainVo.getSumLossFee().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
							List<PrpLCheckDutyVo> checkdutys = checkTaskService.findCheckDutyByRegistNo(RegistNomap.get(claimNo));
							if(checkdutys!=null&&checkdutys.size()>0){
								for(PrpLCheckDutyVo vo:checkdutys){
									if(mainVo.getLicenseNo().equals(vo.getLicenseNo())) 
							          if("0".equals(vo.getIndemnityDuty())){
										plateDataVo.setDutyProportion("100");
									}else if("1".equals(vo.getIndemnityDuty())){
										plateDataVo.setDutyProportion("70");
									}else if("2".equals(vo.getIndemnityDuty())){
										plateDataVo.setDutyProportion("50");
									}else if("3".equals(vo.getIndemnityDuty())){
										plateDataVo.setDutyProportion("30");
									}else if("4".equals(vo.getIndemnityDuty())){
										plateDataVo.setDutyProportion("0");
									}
								}
							}
							if(StringUtils.isBlank(plateDataVo.getDutyProportion())){
								plateDataVo.setDutyProportion("50");// 如果没有责任比例，则默认同责
							}
							plateDatas.add(plateDataVo);

						}
					}

					// 同一立案号下的所有赔款信息
					if(prplpayments!=null&&prplpayments.size()>0){
						for(PrpLPayment paypent:prplpayments){
							if(claimNo.equals(paypent.getPrpLCompensate().getClaimNo())){
								PayDataVo paydataVo = new PayDataVo();
								PrpLPayCustomVo prplcustVo = payCustomService.findPayCustomVoById(paypent.getPayeeId());
								paydataVo.setPayNumber(paypent.getPrpLCompensate().getCompensateNo());
								paydataVo.setBankName(prplcustVo.getBankOutlets());
								paydataVo.setPayeeNo(prplcustVo.getAccountNo());
								if("10".equals(prplcustVo.getCertifyType())){
									paydataVo.setCertificateType("71");
								}else if("11".equals(prplcustVo.getCertifyType())){
									paydataVo.setCertificateType("99");
								}else if("550".equals(prplcustVo.getCertifyType())){
									paydataVo.setCertificateType("99");
								}else if("553".equals(prplcustVo.getCertifyType())){
									paydataVo.setCertificateType("99");
								}else{
									paydataVo.setCertificateType(prplcustVo.getCertifyType());
								}
								paydataVo.setCertificateNo(prplcustVo.getCertifyNo());
								paydataVo.setPayeePhone(prplcustVo.getPayeeMobile());
								paydataVo.setPayUnitAcount(paypent.getSumRealPay().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
								paydataVo.setPayTime(DateFormatString(paypent.getPayTime()));
								paydataVo.setPayDesc("已支付");
								payDatas.add(paydataVo);
							}
						}
					}
					infoVo.setPlateData(plateDatas);// 案件车辆信息
					infoVo.setPayData(payDatas);// 案件收款人信息
					infoVos.add(infoVo);
				}
			}
		}
		}catch(Exception e){
			logger.error("深圳警保理赔信息上传错误信息===>",e);
		}

		return infoVos;
	}

	/**
	 * 深圳警保，补送赋值
	 * @param claimNo
	 * @return
	 */
	private List<ClaimInfoVo> reforParams(String claimNo) {
		List<ClaimInfoVo> infoVos = new ArrayList<ClaimInfoVo>();
		try{
			SqlJoinUtils sqlUtils = new SqlJoinUtils();
			sqlUtils.append(" FROM PrpLCompensate compensate,PrpLPayment pay where compensate.compensateType= ? and compensate.underwriteFlag= ? and compensate.claimNo= ? ");
			sqlUtils.addParamValue("N");
			sqlUtils.addParamValue("1");
			sqlUtils.addParamValue(claimNo);
			sqlUtils.append(" and pay.prpLCompensate.compensateNo=compensate.compensateNo ");
			Object[] params = sqlUtils.getParamValues();
			String sql = sqlUtils.getSql();
			List<PrpLCompensate> compenlists1=new ArrayList<PrpLCompensate>();
			List<PrpLPayment> prplpayments=new ArrayList<PrpLPayment>();
			Set<String> compensateNoSet=new HashSet<String>();
			List<Object[]> list = databaseDao.findAllByHql(sql, params);
			if(list!=null && list.size()>0){
				for(Object[] obj:list){
					PrpLCompensate prpLCompensate=(PrpLCompensate)obj[0];
					PrpLPayment prpLPayment=(PrpLPayment)obj[1];
					compenlists1.add(prpLCompensate);
					prplpayments.add(prpLPayment);
				}
			List<PrpLCompensate> compenlists=new ArrayList<PrpLCompensate>();
			if(compenlists1!=null && compenlists1.size()>0){
				for(PrpLCompensate po:compenlists1){
					if(compensateNoSet.add(po.getCompensateNo())){
						compenlists.add(po);
					}
				}
			}
			
			Map<String,String> RegistNomap = new HashMap<String,String>();
			Map<String,String> riskcodemap = new HashMap<String,String>();
            Map<String,String> comCodemap = new HashMap<String,String>();
			if(compenlists!=null&&compenlists.size()>0){
				for(PrpLCompensate comVo:compenlists){
					RegistNomap.put(comVo.getClaimNo(),comVo.getRegistNo());
					riskcodemap.put(comVo.getClaimNo(),comVo.getRiskCode());
					comCodemap.put(comVo.getClaimNo(),comVo.getComCode());
				}
			}
			ClaimInfoVo infoVo = new ClaimInfoVo();
			infoVo.setInsuranceCode("DHIC");
			String flag = "0";//0表示不需查旧理赔，1表示需要查旧理赔
			if(StringUtils.isNotBlank(RegistNomap.get(claimNo)) && RegistNomap.get(claimNo).length()==21){
				flag = "1";
			}
			String reqType_regist = Risk.DQZ.equals(riskcodemap.get(claimNo))? RequestType.RegistInfoCI.getCode():RequestType.RegistInfoBI.getCode();
			CiClaimPlatformLogVo registLog = reUploadService.findPlatformLog(reqType_regist,RegistNomap.get(claimNo), "1", flag);
			if(registLog!=null){
				infoVo.setClaimNo(registLog.getClaimSeqNo());//理赔编码
			}
			infoVo.setClaimNo1(claimNo);//立案号
			if(compenlists!=null&&compenlists.size()>0){
				for(PrpLCompensate comVo:compenlists){
					if(claimNo.equals(comVo.getClaimNo())){
						infoVo.setPolicyNo(comVo.getPolicyNo());
						if("1101".equals(comVo.getRiskCode())){
							infoVo.setInsuranceType("1");
						}else{
							infoVo.setInsuranceType("2");
						}

					}

				}
			}
			AccidentResInfo accidentVo = szpoliceCaseService.findAccidentResInfoByRegistNo(RegistNomap.get(claimNo));
			if(accidentVo!=null){
				infoVo.setAccidentNo(accidentVo.getAccidentNo());
			}
			infoVo.setWarningNo(RegistNomap.get(claimNo));
			infoVo=commonShare(RegistNomap,claimNo,prplpayments,infoVo);
			infoVos.add(infoVo);
		 }
		}catch(Exception e){
			logger.error("深圳警保理赔信息上传错误信息===>",e);
		}

		return infoVos;
	}

	/**
	 * 深圳警保批量补送
	 * <pre></pre>
	 * @param claimNo
	 * @return
	 * @modified:
	 * ☆yzy(2019年1月29日 下午7:48:00): <br>
	 */
	private List<ClaimInfoVo> pLReforParams(String claimNo){
		
		List<ClaimInfoVo> infoVos = new ArrayList<ClaimInfoVo>();
		try{
			SqlJoinUtils sqlUtils = new SqlJoinUtils();
			sqlUtils.append(" FROM PrpLCompensate compensate,PrpLPayment pay where compensate.compensateType= ? and compensate.underwriteFlag= ? and compensate.claimNo= ? ");
			sqlUtils.addParamValue("N");
			sqlUtils.addParamValue("1");
			sqlUtils.addParamValue(claimNo);
			sqlUtils.append(" and pay.prpLCompensate.compensateNo=compensate.compensateNo ");
			Object[] params = sqlUtils.getParamValues();
			String sql = sqlUtils.getSql();
			List<PrpLCompensate> compenlists1=new ArrayList<PrpLCompensate>();
			List<PrpLPayment> prplpayments=new ArrayList<PrpLPayment>();
			Set<String> compensateNoSet=new HashSet<String>();
			List<Object[]> list = databaseDao.findAllByHql(sql, params);
			if(list!=null && list.size()>0){
				for(Object[] obj:list){
					PrpLCompensate prpLCompensate=(PrpLCompensate)obj[0];
					PrpLPayment prpLPayment=(PrpLPayment)obj[1];
					compenlists1.add(prpLCompensate);
					prplpayments.add(prpLPayment);
				}
			List<PrpLCompensate> compenlists=new ArrayList<PrpLCompensate>();
			if(compenlists1!=null && compenlists1.size()>0){
				for(PrpLCompensate po:compenlists1){
					if(compensateNoSet.add(po.getCompensateNo())){
						compenlists.add(po);
					}
				}
			}
			Map<String,String> RegistNomap = new HashMap<String,String>();
			Map<String,String> riskcodemap = new HashMap<String,String>();
            Map<String,String> comCodemap = new HashMap<String,String>();
			if(compenlists!=null&&compenlists.size()>0){
				for(PrpLCompensate comVo:compenlists){
					RegistNomap.put(comVo.getClaimNo(),comVo.getRegistNo());
					riskcodemap.put(comVo.getClaimNo(),comVo.getRiskCode());
					comCodemap.put(comVo.getClaimNo(),comVo.getComCode());
				}
			}
			ClaimInfoVo infoVo = new ClaimInfoVo();
			infoVo.setInsuranceCode("DHIC");
			String flag = "0";//0表示不需查旧理赔，1表示需要查旧理赔
			if(RegistNomap.get(claimNo).length()==21){
				flag = "1";
			}
			String reqType_regist = Risk.DQZ.equals(riskcodemap.get(claimNo))? RequestType.RegistInfoCI.getCode():RequestType.RegistInfoBI.getCode();
			CiClaimPlatformLogVo registLog = reUploadService.findPlatformLog(reqType_regist,RegistNomap.get(claimNo), "1", flag);
			if(registLog!=null){
				infoVo.setClaimNo(registLog.getClaimSeqNo());//理赔编码
			}
			infoVo.setClaimNo1(claimNo);//立案号
			if(compenlists!=null&&compenlists.size()>0){
				for(PrpLCompensate comVo:compenlists){
					if(claimNo.equals(comVo.getClaimNo())){
						infoVo.setPolicyNo(comVo.getPolicyNo());
						if("1101".equals(comVo.getRiskCode())){
							infoVo.setInsuranceType("1");
						}else{
							infoVo.setInsuranceType("2");
						}

					}

				}
			}
			AccidentResInfo accidentVo = szpoliceCaseService.findAccidentResInfoByRegistNo(RegistNomap.get(claimNo));
			if(accidentVo!=null){
				infoVo.setAccidentNo(accidentVo.getAccidentNo());
			}
			infoVo.setWarningNo(RegistNomap.get(claimNo));
			infoVo=commonShare(RegistNomap,claimNo,prplpayments,infoVo);
			infoVos.add(infoVo);
		 }
		}catch(Exception e){
			logger.error("深圳警保理赔信息上传错误信息===>",e);
		}
		return infoVos;
	}
	
	/**
	 * 时间转换方法 Date 类型转换 String类型
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private String DateFormatString(Date strDate) {
		String str = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(strDate!=null){
			str = format.format(strDate);
		}
		return str;
	}

	@SuppressWarnings("static-access")
	@Override
	public void settleClaimReUpload(String claimNo,String sign,Long logId) {
		long t1 = System.currentTimeMillis();
		String gateway = SpringProperties.getProperty("SZWARN_URL");
		String appId = SpringProperties.getProperty("SZWARN_APPID");
		String appKey = SpringProperties.getProperty("SZWARN_APPKEY");
		String method = SpringProperties.getProperty("SZWARN_METHOD_CLAIM");
		boolean isDataEncrypt = true;
		boolean isSign = true;
		List<ClaimInfoVo> claimInfoVos = reforParams(claimNo);
		if(claimInfoVos!=null&&claimInfoVos.size()>0){
			for(ClaimInfoVo vo:claimInfoVos){
				PrpLRegistVo registVo = registQueryService.findByRegistNo(vo.getWarningNo());
				if(registVo!=null){
					if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
						continue;
					}
				}
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				JSONObject json = new JSONObject();
				json.put("insuranceCode",appId);
				json.put("claimNo",vo.getClaimNo());
				json.put("policyNo",vo.getPolicyNo());
				json.put("insuranceType",vo.getInsuranceType());
				json.put("accidentNo",vo.getAccidentNo());
				json.put("warningNo",vo.getWarningNo());
				json.put("payAmout",vo.getPayAmout());
				JSONArray palateArray = new JSONArray();
				palateArray.addAll(vo.getPlateData());
				json.put("plateData",palateArray);
				JSONArray payArray = new JSONArray();
				payArray.addAll(vo.getPayData());
				json.put("payData",payArray);
				OpenApiClient open = new OpenApiClient(gateway,appId,appKey,isDataEncrypt,isSign);
				OpenApiRsp responseData = new OpenApiRsp();
				try{
					responseData = open.execute(method,json.toString());
					if(responseData!=null){
						if("0000".equals(responseData.getReturnCode())){
							logVo.setStatus("1");
							logVo.setRequestXml(json.toJSONString());
							logVo.setResponseXml(JSON.toJSON(responseData).toString());
						}else{
							logVo.setStatus("0");
							logVo.setErrorMessage(responseData.getReturnMsg());
							logVo.setRequestXml(json.toJSONString());
							logVo.setResponseXml(JSON.toJSON(responseData).toString());
						}
					}else{
						logVo.setStatus("0");
						logVo.setErrorMessage("与深圳警保平台连接失败!");
						logVo.setRequestXml(json.toJSONString());
					}
				}catch(SdkException e){
					logVo.setStatus("0");
					logVo.setErrorMessage(e.getMessage());
					logVo.setRequestXml(json.toJSONString());
					logVo.setResponseXml(responseData!=null?JSON.toJSON(responseData).toString():"");
					logger.error("错误信息======》",e);
				}finally{
					logVo.setClaimNo(claimNo);// 立案号
					logVo.setRegistNo(vo.getWarningNo());// 报案号
					if("1101".equals(claimNo.substring(11,15))){
						logVo.setBusinessType(BusinessType.SZClaim_CI.name());
						logVo.setBusinessName(BusinessType.SZClaim_CI.getName());
					}else{
						logVo.setBusinessType(BusinessType.SZClaim_BI.name());
						logVo.setBusinessName(BusinessType.SZClaim_BI.getName());
					}
					logVo.setCreateTime(new Date());
					logVo.setRequestTime(new Date());
					logVo.setRequestUrl(gateway);
					logVo.setCreateUser("0000000000");
					logVo.setComCode("00020000");
					interfaceLogService.save(logVo);
					if("1".equals(sign)){
						ClaimInterfaceLog logpo = databaseDao.findByPK(ClaimInterfaceLog.class,logId);
						logpo.setStatus("2");
					}
					logger.info("接口({})调用耗时{}ms"+( System.currentTimeMillis()-t1 ));
				}

			}
		}

	}

	@Override
	public String settleClaimPLReUpload(String claimNo) {
		String gateway = SpringProperties.getProperty("SZWARN_URL");
		String appId = SpringProperties.getProperty("SZWARN_APPID");
		String appKey = SpringProperties.getProperty("SZWARN_APPKEY");
		String method = SpringProperties.getProperty("SZWARN_METHOD_CLAIM");
		String status="0";
			long t1 = System.currentTimeMillis();
			boolean isDataEncrypt = true;
			boolean isSign = true;
			List<ClaimInfoVo> claimInfoVos = pLReforParams(claimNo);
			if(claimInfoVos!=null&&claimInfoVos.size()>0){
				for(ClaimInfoVo vo:claimInfoVos){ 
					PrpLRegistVo registVo = registQueryService.findByRegistNo(vo.getWarningNo());
					if(registVo!=null){
						if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
							continue;
						}
					}
					ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
					JSONObject json = new JSONObject();
					json.put("insuranceCode",appId);
					json.put("claimNo",vo.getClaimNo());
					json.put("policyNo",vo.getPolicyNo());
					json.put("insuranceType",vo.getInsuranceType());
					json.put("accidentNo",vo.getAccidentNo());
					json.put("warningNo",vo.getWarningNo());
					json.put("payAmout",vo.getPayAmout());
					JSONArray palateArray = new JSONArray();
					palateArray.addAll(vo.getPlateData());
					json.put("plateData",palateArray);
					JSONArray payArray = new JSONArray();
					payArray.addAll(vo.getPayData());
					json.put("payData",payArray);
					OpenApiClient open = new OpenApiClient(gateway,appId,appKey,isDataEncrypt,isSign);
					OpenApiRsp responseData = new OpenApiRsp();
					try{
						responseData = open.execute(method,json.toString());
						if(responseData!=null){
							if("0000".equals(responseData.getReturnCode())){
								logVo.setStatus("1");
								logVo.setRequestXml(json.toJSONString());
								logVo.setResponseXml(JSON.toJSON(responseData).toString());
							}else{
								logVo.setStatus("0");
								logVo.setErrorMessage(responseData.getReturnMsg());
								logVo.setRequestXml(json.toJSONString());
								logVo.setResponseXml(JSON.toJSON(responseData).toString());
							}
						}
					}catch(SdkException e){
						logVo.setStatus("0");
						logVo.setErrorMessage(e.getMessage());
						logVo.setRequestXml(json.toJSONString());
						logVo.setResponseXml(JSON.toJSON(responseData).toString());
						e.printStackTrace();
					}finally{
						logVo.setClaimNo(claimNo);// 立案号
						logVo.setRegistNo(vo.getWarningNo());// 报案号
						if("1101".equals(claimNo.substring(11,15))){
							logVo.setBusinessType(BusinessType.SZClaim_CI.name());
							logVo.setBusinessName(BusinessType.SZClaim_CI.getName());
						}else{
							logVo.setBusinessType(BusinessType.SZClaim_BI.name());
							logVo.setBusinessName(BusinessType.SZClaim_BI.getName());
						}
						logVo.setCreateTime(new Date());
						logVo.setRequestTime(new Date());
						logVo.setRequestUrl(gateway);
						logVo.setCreateUser("0000000000");
						logVo.setComCode("00020000");
						interfaceLogService.save(logVo);
						status=logVo.getStatus();
						logger.info("接口({})调用耗时{}ms"+( System.currentTimeMillis()-t1 ));
					}

				}
			}
		return status;
	}
	/**
	 * 共用代码逻辑
	 * <pre></pre>
	 * @param RegistNomap
	 * @param claimNo
	 * @param prplpayments
	 * @param infoVo
	 * @return
	 * @modified:
	 * ☆yzy(2019年2月28日 下午5:21:46): <br>
	 */
	private ClaimInfoVo commonShare(Map<String, String> RegistNomap,String claimNo,List<PrpLPayment> prplpayments,ClaimInfoVo infoVo){
		BigDecimal sumAmt = new BigDecimal("0.00");
		List<PrpLDlossCarMainVo> losscarMainVos = lossCarService.findLossCarMainByRegistNo(RegistNomap.get(claimNo));
		List<PlateDataVo> plateDatas = new ArrayList<PlateDataVo>();// 车牌信息 Y
		List<PayDataVo> payDatas = new ArrayList<PayDataVo>();// 赔款信息 Y
        
		//定损追加的车辆只送一次，两个把金额相加
		List<PrpLDlossCarMainVo> losscarMainVosdis=new ArrayList<PrpLDlossCarMainVo>();
		Set<Integer> set =new HashSet<Integer>();
		if(losscarMainVos!=null  && losscarMainVos.size()>0){
        	for(PrpLDlossCarMainVo mainVo:losscarMainVos){
        		if(set.add(mainVo.getSerialNo()) && !"DLCarAdd".equals(mainVo.getFlowFlag())){
        			   losscarMainVosdis.add(mainVo);
        		}else{//将同一辆车的定损与定损追加金额相加
        			if(losscarMainVosdis!=null && losscarMainVosdis.size()>0){
        				for(PrpLDlossCarMainVo carmainVo:losscarMainVosdis){
        					if(carmainVo.getSerialNo()==mainVo.getSerialNo()){
        					  carmainVo.setSumLossFee(carmainVo.getSumLossFee().add(mainVo.getSumLossFee()));
        					}
        				}
        			}
        		}
        	}
        }
		if(losscarMainVosdis!=null && losscarMainVosdis.size()>0){
			for(PrpLDlossCarMainVo mainVo:losscarMainVosdis){
				PrpLDlossCarInfoVo carInfoVo = lossCarService.findDefCarInfoByPk(mainVo.getCarId());
				PrpLCheckCarVo checkCarVo = checkTaskService.findByCheckId(mainVo.getCheckCarId());
				PlateDataVo plateDataVo = new PlateDataVo();
				if("25".equals(carInfoVo.getLicenseType())){
					plateDataVo.setPlateType("99");
				}else{
					plateDataVo.setPlateType(carInfoVo.getLicenseType());
				}
				plateDataVo.setPlateNo(carInfoVo.getLicenseNo());
				plateDataVo.setDriverName(carInfoVo.getDriveName());
				if(checkCarVo!=null){
					plateDataVo.setDriverPhone(checkCarVo.getPrpLCheckDriver().getLinkPhoneNumber());
				}else{
					PrpLCheckCarVo prplcheckcarVo = checkTaskService.findCheckCarBySerialNo(RegistNomap.get(claimNo),1);
					if(prplcheckcarVo!=null){
						plateDataVo.setDriverPhone(prplcheckcarVo.getPrpLCheckDriver().getLinkPhoneNumber());
					}
				}

				plateDataVo.setGarage(mainVo.getRepairFactoryName());
				plateDataVo.setFixedAmount(mainVo.getSumLossFee().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
				List<PrpLCheckDutyVo> checkdutys = checkTaskService.findCheckDutyByRegistNo(RegistNomap.get(claimNo));
				if(checkdutys!=null&&checkdutys.size()>0){
					for(PrpLCheckDutyVo vo:checkdutys){
					  if(mainVo.getLicenseNo().equals(vo.getLicenseNo())){ 
						if("0".equals(vo.getIndemnityDuty())){
							plateDataVo.setDutyProportion("100");
						}else if("1".equals(vo.getIndemnityDuty())){
							plateDataVo.setDutyProportion("70");
						}else if("2".equals(vo.getIndemnityDuty())){
							plateDataVo.setDutyProportion("50");
						}else if("3".equals(vo.getIndemnityDuty())){
							plateDataVo.setDutyProportion("30");
						}else if("4".equals(vo.getIndemnityDuty())){
							plateDataVo.setDutyProportion("0");
						}
					   }
					}
				}
				if(StringUtils.isBlank(plateDataVo.getDutyProportion())){
					plateDataVo.setDutyProportion("50");// 如果没有责任比例，则默认同责
				}
				plateDatas.add(plateDataVo);

			}
		}

		// 同一立案号下的所有赔款信息
		if(prplpayments!=null&&prplpayments.size()>0){
			for(PrpLPayment paypent:prplpayments){
				if(claimNo.equals(paypent.getPrpLCompensate().getClaimNo())){
					if(CodeConstants.IsSingleAccident.YES.equals(paypent.getPayStatus())){
						PayDataVo paydataVo = new PayDataVo();
						PrpLPayCustomVo prplcustVo = payCustomService.findPayCustomVoById(paypent.getPayeeId());
						paydataVo.setPayNumber(paypent.getPrpLCompensate().getCompensateNo());
						paydataVo.setBankName(prplcustVo.getBankOutlets());
						paydataVo.setPayeeNo(prplcustVo.getAccountNo());
						paydataVo.setPayeeName(prplcustVo.getPayeeName());
						if("10".equals(prplcustVo.getCertifyType())){
							paydataVo.setCertificateType("71");
						}else if("11".equals(prplcustVo.getCertifyType())){
							paydataVo.setCertificateType("99");
						}else if("550".equals(prplcustVo.getCertifyType())){
							paydataVo.setCertificateType("99");
						}else if("553".equals(prplcustVo.getCertifyType())){
							paydataVo.setCertificateType("99");
						}else{
							paydataVo.setCertificateType(prplcustVo.getCertifyType());
						}
						paydataVo.setCertificateNo(prplcustVo.getCertifyNo());
						paydataVo.setPayeePhone(prplcustVo.getPayeeMobile());
						paydataVo.setPayUnitAcount(paypent.getSumRealPay().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
						sumAmt = sumAmt.add(paypent.getSumRealPay().setScale(2,BigDecimal.ROUND_HALF_UP));
						paydataVo.setPayTime(DateFormatString(paypent.getPayTime()));
						paydataVo.setPayDesc("已支付");
						payDatas.add(paydataVo);
					}
				}
			}
		}
		
		//预付赔款
		List<PrpLCompensateVo> compensateVos = compensateTaskService.findCompListByClaimNo(claimNo,"Y");
		for(PrpLCompensateVo prpLCompensateVo : compensateVos){
			List<PrpLPrePayVo> prePayPVos = compensateTaskService.getPrePayVo(prpLCompensateVo.getCompensateNo(),"P");
			if(prePayPVos != null && prePayPVos.size() > 0){
				for(PrpLPrePayVo vo : prePayPVos){
					if(CodeConstants.IsSingleAccident.YES.equals(vo.getPayStatus())){
						PayDataVo paydataVo = new PayDataVo();
						PrpLPayCustomVo prplcustVo = payCustomService.findPayCustomVoById(vo.getPayeeId());
						paydataVo.setPayNumber(vo.getCompensateNo());
						paydataVo.setBankName(prplcustVo.getBankOutlets());
						paydataVo.setPayeeNo(prplcustVo.getAccountNo());
						paydataVo.setPayeeName(prplcustVo.getPayeeName());
						if("10".equals(prplcustVo.getCertifyType())){
							paydataVo.setCertificateType("71");
						}else if("11".equals(prplcustVo.getCertifyType())){
							paydataVo.setCertificateType("99");
						}else if("550".equals(prplcustVo.getCertifyType())){
							paydataVo.setCertificateType("99");
						}else if("553".equals(prplcustVo.getCertifyType())){
							paydataVo.setCertificateType("99");
						}else{
							paydataVo.setCertificateType(prplcustVo.getCertifyType());
						}
						paydataVo.setCertificateNo(prplcustVo.getCertifyNo());
						paydataVo.setPayeePhone(prplcustVo.getPayeeMobile());
						paydataVo.setPayUnitAcount(vo.getPayAmt().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
						sumAmt = sumAmt.add(vo.getPayAmt().setScale(2,BigDecimal.ROUND_HALF_UP));
						paydataVo.setPayTime(DateFormatString(vo.getPayTime()));
						paydataVo.setPayDesc("已支付");
						payDatas.add(paydataVo);
					}
				}
			}
		}

		//垫付赔款
		PrpLPadPayMainVo padPayVo = padPayService.getPadPayInfoByClaimNo(claimNo);
		if(padPayVo != null){
			List<PrpLPadPayPersonVo> prpLPadPayPersons = padPayVo.getPrpLPadPayPersons();
			if(prpLPadPayPersons != null && prpLPadPayPersons.size() > 0){
				for(PrpLPadPayPersonVo padPayPersonVo : prpLPadPayPersons){
					if(CodeConstants.IsSingleAccident.YES.equals(padPayPersonVo.getPayStatus())){
						PayDataVo paydataVo = new PayDataVo();
						PrpLPayCustomVo prplcustVo = payCustomService.findPayCustomVoById(padPayPersonVo.getPayeeId());
						paydataVo.setPayNumber(padPayVo.getCompensateNo());
						paydataVo.setBankName(prplcustVo.getBankOutlets());
						paydataVo.setPayeeNo(prplcustVo.getAccountNo());
						paydataVo.setPayeeName(prplcustVo.getPayeeName());
						if("10".equals(prplcustVo.getCertifyType())){
							paydataVo.setCertificateType("71");
						}else if("11".equals(prplcustVo.getCertifyType())){
							paydataVo.setCertificateType("99");
						}else if("550".equals(prplcustVo.getCertifyType())){
							paydataVo.setCertificateType("99");
						}else if("553".equals(prplcustVo.getCertifyType())){
							paydataVo.setCertificateType("99");
						}else{
							paydataVo.setCertificateType(prplcustVo.getCertifyType());
						}
						paydataVo.setCertificateNo(prplcustVo.getCertifyNo());
						paydataVo.setPayeePhone(prplcustVo.getPayeeMobile());
						paydataVo.setPayUnitAcount(padPayPersonVo.getCostSum().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
						sumAmt = sumAmt.add(padPayPersonVo.getCostSum().setScale(2,BigDecimal.ROUND_HALF_UP));
						paydataVo.setPayTime(DateFormatString(padPayPersonVo.getPayTime()));
						paydataVo.setPayDesc("已支付");
						payDatas.add(paydataVo);
					}
				}
			}
		}
		//赔款总金额汇总
		infoVo.setPayAmout(sumAmt.toString());
		infoVo.setPlateData(plateDatas);// 案件车辆信息
		infoVo.setPayData(payDatas);// 案件收款人信息
		return infoVo;
	}
}
