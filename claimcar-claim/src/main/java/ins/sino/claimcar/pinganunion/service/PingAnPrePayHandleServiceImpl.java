package ins.sino.claimcar.pinganunion.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.vo.PiccCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.*;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.pay.service.PrePayHandleService;
import ins.sino.claimcar.pinganUnion.service.PingAnHandleService;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.pinganunion.vo.prepay.*;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.util.*;

/**
 * 平安联盟中心-预赔信息查询接口-业务处理
 *
 * @author mfn
 * @date 2020/7/21 15:04
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, group = "pingAnPrePayHandleService")
@Path("pingAnPrePayHandleService")
public class PingAnPrePayHandleServiceImpl implements PingAnHandleService {
    private static Logger logger = LoggerFactory.getLogger(PingAnPrePayHandleServiceImpl.class);
    @Autowired
    private ClaimService claimService;
    @Autowired
    private CheckTaskService checkTaskService;
    @Autowired
    CompensateService compensateService;
    @Autowired
    PrePayHandleService prePadHandleService;
    @Autowired
    WfTaskHandleService wfTaskHandleService;
    @Autowired
    PingAnDictService pingAnDictService;
    @Autowired
    private BillNoService billNoService;
    @Autowired
    PolicyViewService policyViewService;

    /**
     * 接口具体业务处理方法
     *
     * @param respData
     * @return
     */
    @Override
    public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
        logger.info("报案号：" + registNo + " 请求参数：" + pingAnDataNoticeVo.getParamObj() + " 平安返回的预付数据：" + respData);
        ResultBean resultBean = ResultBean.success();

        if (StringUtils.isNotBlank(respData)) {
            // 预付接口返回数据结果
            UnionPrePayResponseDataDto dataDto;
            // 预赔信息
            UnionPrePayInfo prePayInfo;
            // 预赔对象列表
            List<UnionPrePayChannelInfo> prePayChannelInfoList;
            // 预赔金额列表
            List<UnionPrePayMoney> prePayMoneyList;
            // 预赔费用列表
            List<UnionPrePayFeeItem> feeItemList;
            try {
                //Gson gson = new Gson();
                //dataDto = gson.fromJson(respData, UnionPrePayResponseDataDto.class);
                ObjectMapper mapper = new ObjectMapper();
                mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
                dataDto = mapper.readValue(respData, UnionPrePayResponseDataDto.class);

                if (dataDto != null) {
                    Date date = new Date();
                    prePayInfo = dataDto.getPrepayInfo();
                    String pinganReportNo = prePayInfo.getReportNo();
                    String isPersonPrepay = prePayInfo.getPrepayPerson();
                    String isCarOrPropPrepay = prePayInfo.getPrepayPropertyCar();
                    String chargeCode = "";
                    if ("Y".equals(isCarOrPropPrepay)) {
                        chargeCode = "01";
                    } else {
                        if ("Y".equals(isPersonPrepay)) {
                            chargeCode = "03";
                        }
                    }
                    prePayChannelInfoList = dataDto.getPrepayChannelInfoList();
                    prePayMoneyList = dataDto.getPrepayMoneyList();
                    feeItemList = dataDto.getFeeItemList();

                    Integer payTimes = prePayInfo.getCaseTimes() == null ? 0 : prePayInfo.getCaseTimes();

                    // 鼎和报案保单信息
                    List<PrpLCMainVo> prpLCMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);

                    BigDecimal compensatePayAmt = BigDecimal.ZERO;
                    Map<String, String> prepayNoMap = new HashMap<String, String>();
                    Map<String, String> policyNoMap = new HashMap<String, String>();
                    // 险种-理算对象
                    Map<String, PrpLCompensateVo> riskCompMap = new HashMap<String, PrpLCompensateVo>();
                    // 险种-预付主表对象集合
                    Map<String, List<PrpLPrePayVo>> prepayVosMap = new HashMap<String, List<PrpLPrePayVo>>();
                    int index=0;
                    // 赔款
                    for (UnionPrePayMoney prePayMoney : prePayMoneyList) {
                        if(prePayMoney.getDutyPayMoney().compareTo(BigDecimal.ZERO)<=0){
                            continue;
                        }
                        PrpLPrePayVo prePayVo = new PrpLPrePayVo();
                        prePayVo.setCreateTime(new Date());
                        prePayVo.setUpdateTime(new Date());
                        prePayVo.setCurrency(CodeConstants.Currency.CNY);
                        prePayVo.setPayStatus(CodeConstants.PayStatus.SENDPAY);
                        String prepayNo;
                        String policyNo = null;
                        String riskCode = null;
                        String kindCode = null;

                        PiccCodeDictVo codeDictVo = pingAnDictService.getDictData("planCode", prePayMoney.getPlanCode());
                        // 鼎和险种代码
                        if (null != codeDictVo) {
                            riskCode = codeDictVo.getDhCodeCode();
                        }
                        if (riskCode == null) {
                            throw new IllegalArgumentException("鼎和报案号：" + registNo + " 平安报案号："
                                    + pinganReportNo + "险种异常！险种代码：" + prePayMoney.getPlanCode());
                        }
                        if (null == prepayNoMap.get(riskCode)) {
                            prepayNo = billNoService.getPrePayNo(policyViewService.getPolicyComCode(registNo), riskCode);
                            prepayNoMap.put(riskCode, prepayNo);
                        } else {
                            prepayNo = prepayNoMap.get(riskCode);
                        }
                        for (PrpLCMainVo cmainVo : prpLCMainVoList) {
                            if (riskCode.equals(cmainVo.getRiskCode())) {
                                policyNo = cmainVo.getPolicyNo();
                                policyNoMap.put(riskCode, policyNo);
                                break;
                            }
                        }
                        codeDictVo = pingAnDictService.getDictData("dutyCode", prePayMoney.getDutyCode());
                        if (codeDictVo != null) {
                            kindCode = codeDictVo.getDhCodeCode();
                        }
                        prePayVo.setCompensateNo(prepayNo);
                        prePayVo.setPolicyNo(policyNo);
                        prePayVo.setRiskCode(riskCode);
                        prePayVo.setKindCode(kindCode);
                        prePayVo.setFeeType(CodeConstants.FeeType.PAY);
                        // TODO 需要进行代码转换 01-财产损失 02-死亡伤残 03-医疗费用
                        prePayVo.setChargeCode(chargeCode);
                        prePayVo.setChargeName("01".equals(chargeCode) ? "财产损失" : "医疗费用");
                        BigDecimal payAmt = prePayMoney.getDutyPayMoney() == null ? BigDecimal.ZERO : prePayMoney.getDutyPayMoney();
                        prePayVo.setPayAmt(payAmt);
                        compensatePayAmt = compensatePayAmt.add(payAmt);
                        prePayVo.setLossType("");
                        prePayVo.setLossName("");
                        prePayVo.setSerialNo(""+index);
                        index++;
                        prePayVo.setCaseTimes(payTimes);
                        if (null == prepayVosMap.get(riskCode)) {
                            List<PrpLPrePayVo> tmplist = new ArrayList<PrpLPrePayVo>();
                            tmplist.add(prePayVo);
                            prepayVosMap.put(riskCode, tmplist);
                        } else {
                            List<PrpLPrePayVo> tmplist = prepayVosMap.get(riskCode);
                            tmplist.add(prePayVo);
                            prepayVosMap.put(riskCode, tmplist);
                        }

                        //******************************理算主表数据 start **************************
                        if (null == riskCompMap.get(riskCode) && prepayVosMap.containsKey(riskCode)) {
                            PrpLCompensateVo compensateVo = new PrpLCompensateVo();
                            compensateVo.setCompensateNo(prepayNo);
                            List<PrpLClaimVo> claimVoList = claimService.findprpLClaimVoListByRegistAndPolicyNo(registNo, policyNo, "1");
                            PrpLClaimVo claimVo = new PrpLClaimVo();
                            if (claimVoList != null && claimVoList.size() > 0) {
                                for (PrpLClaimVo prpLClaimVo : claimVoList) {
                                    if (riskCode.equals(prpLClaimVo.getRiskCode())) {
                                        claimVo = prpLClaimVo;
                                        break;
                                    }
                                }
                            }
                            compensateVo.setClaimNo(claimVo.getClaimNo());
                            compensateVo.setRegistNo(registNo);
                            compensateVo.setPolicyNo(policyNo);
                            compensateVo.setRiskCode(riskCode);
                            compensateVo.setMakeCom(claimVo.getComCode());
                            compensateVo.setComCode(claimVo.getComCode());
                            compensateVo.setCaseType(CodeConstants.CompCaseType.NORMAL_CASE);
                            compensateVo.setCompensateType(CodeConstants.CompensateType.prepay_type);
                            PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo, 1);
                            if (null != checkDutyVo) {
                                compensateVo.setIndemnityDuty(checkDutyVo.getIndemnityDuty());
                                compensateVo.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate());
                            }
                            // 预付总金额 = 预付赔款 + 预付费用
                            compensateVo.setSumAmt(compensatePayAmt);
                            compensateVo.setAllLossFlag("0");
                            compensateVo.setRecoveryFlag("0");
                            compensateVo.setCreateUser("AUTO");
                            compensateVo.setUnderwriteFlag(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE);
                            compensateVo.setUnderwriteUser("AUTO");
                            compensateVo.setUnderwriteDate(date);
                            compensateVo.setCreateTime(date);
                            compensateVo.setUpdateTime(date);
                            compensateVo.setTimes(payTimes);
                            compensateVo.setCurrency(CodeConstants.Currency.CNY);
                            compensateVo.setDeductType(CodeConstants.DeductType.DEDUCT_PRE);
                            compensateVo.setLastModifyUser("AUTO");
                            compensateVo.setHandler1Code("AUTO");

                            PrpLCompensateExtVo compensateExtVo = new PrpLCompensateExtVo();
                            compensateExtVo.setRegistNo(registNo);
                            compensateExtVo.setPayBackState("1");
                            compensateExtVo.setCreateTime(date);
                            compensateExtVo.setCreateTime(date);
                            compensateExtVo.setWriteOffFlag("0");
                            compensateExtVo.setIsAutoPay("0");
                            compensateExtVo.setIsAutoPay("0");

                            if (compensatePayAmt.compareTo(BigDecimal.ZERO) > 0 && compensatePayAmt.compareTo(new BigDecimal("50000.00")) < 1) {
                                compensateExtVo.setIsFastReparation("1");
                                compensateExtVo.setIsAutoPay("1");
                            }
                            compensateExtVo.setIsCompDeduct("0");
                            compensateVo.setPrpLCompensateExt(compensateExtVo);

                            SysUserVo userVo = new SysUserVo();
                            userVo.setUserCode("AUTO");
                            userVo.setUserName("AUTO");
                            userVo.setComCode(claimVo.getComCode());
                            //******************************理算主表数据  end  **************************
                            riskCompMap.put(riskCode, compensateVo);
                            //如果compensateMap集合中存在对应险种得riskCode值，并且预赔mao集合中也存在对应得险种预赔数据
                            //则需要把金额重新更新到compensate集合中
                        } else if(riskCompMap.containsKey(riskCode) && prepayVosMap.containsKey(riskCode)){
                                PrpLCompensateVo compensateVo = new PrpLCompensateVo();
                                compensateVo = riskCompMap.get(riskCode);
                                compensateVo.setSumAmt(compensatePayAmt);
                                PrpLCompensateExtVo compensateExtVo = new PrpLCompensateExtVo();
                                compensateExtVo = compensateVo.getPrpLCompensateExt();
                                if (compensatePayAmt.compareTo(BigDecimal.ZERO) > 0 && compensatePayAmt.compareTo(new BigDecimal("50000.00")) < -1) {
                                    compensateExtVo.setIsFastReparation("1");
                                    compensateExtVo.setIsAutoPay("1");
                                }
                                compensateVo.setPrpLCompensateExt(compensateExtVo);
                                riskCompMap.put(riskCode, compensateVo);
                         }
                    }

                    // 费用
                    index=0;
                    for (UnionPrePayFeeItem prePayFeeItem : feeItemList) {
                        String prepayNo;
                        String policyNo = null;
                        String riskCode = null;
                        String kindCode = null;

                        PiccCodeDictVo codeDictVo = pingAnDictService.getDictData("planCode", prePayFeeItem.getPlanCode());
                        // 鼎和险种代码
                        if (null != codeDictVo) {
                            riskCode = codeDictVo.getDhCodeCode();
                        }
                        if (riskCode == null) {
                            throw new IllegalArgumentException("鼎和报案号：" + registNo + " 平安报案号："
                                    + pinganReportNo + "险种异常！险种代码：" + prePayFeeItem.getPlanCode());
                        }
                        if (null == riskCompMap.get(riskCode)) {
                            prepayNo = billNoService.getPrePayNo(policyViewService.getPolicyComCode(registNo), riskCode);
                        } else {
                            prepayNo = riskCompMap.get(riskCode).getCompensateNo();
                        }
                        if (null == policyNoMap.get(riskCode)) {
                            for (PrpLCMainVo cmainVo : prpLCMainVoList) {
                                if (riskCode.equals(cmainVo.getRiskCode())) {
                                    policyNo = cmainVo.getPolicyNo();
                                    policyNoMap.put(riskCode, policyNo);
                                    break;
                                }
                            }
                        } else {
                            policyNo = policyNoMap.get(riskCode);
                        }

                        codeDictVo = pingAnDictService.getDictData("dutyCode", prePayFeeItem.getDutyCode());
                        if (codeDictVo != null) {
                            kindCode = codeDictVo.getDhCodeCode();
                        }


                        // 仲裁费
                        if (prePayFeeItem.getArbitrateFee() != null && BigDecimal.ZERO.compareTo(prePayFeeItem.getArbitrateFee()) < 0) {
                            PrpLPrePayVo prePayVo = new PrpLPrePayVo();
                            prePayVo.setCompensateNo(prepayNo);
                            prePayVo.setCreateTime(new Date());
                            prePayVo.setUpdateTime(new Date());
                            prePayVo.setCurrency(CodeConstants.Currency.CNY);
                            prePayVo.setPayStatus(CodeConstants.PayStatus.SENDPAY);
                            prePayVo.setPolicyNo(policyNo);
                            prePayVo.setRiskCode(riskCode);
                            prePayVo.setKindCode(kindCode);
                            prePayVo.setCaseTimes(payTimes);
                            prePayVo.setFeeType(CodeConstants.FeeType.FEE);
                            prePayVo.setChargeCode("07");
                            prePayVo.setChargeName("仲裁费");
                            prePayVo.setSerialNo(""+index);
                            index++;
                            prePayVo.setPayAmt(prePayFeeItem.getArbitrateFee());
                            if (null == prepayVosMap.get(riskCode)) {
                                List<PrpLPrePayVo> tmplist = new ArrayList<PrpLPrePayVo>();
                                tmplist.add(prePayVo);
                                prepayVosMap.put(riskCode, tmplist);
                            } else {
                                List<PrpLPrePayVo> tmplist = prepayVosMap.get(riskCode);
                                tmplist.add(prePayVo);
                                prepayVosMap.put(riskCode, tmplist);
                            }
                            compensatePayAmt = compensatePayAmt.add(prePayFeeItem.getArbitrateFee());
                        }
                        // 诉讼费
                        if (prePayFeeItem.getLawsuitFee() != null && BigDecimal.ZERO.compareTo(prePayFeeItem.getLawsuitFee()) < 0) {
                            PrpLPrePayVo prePayVo = new PrpLPrePayVo();
                            prePayVo.setCompensateNo(prepayNo);
                            prePayVo.setCreateTime(new Date());
                            prePayVo.setUpdateTime(new Date());
                            prePayVo.setCurrency(CodeConstants.Currency.CNY);
                            prePayVo.setPayStatus(CodeConstants.PayStatus.SENDPAY);
                            prePayVo.setPolicyNo(policyNo);
                            prePayVo.setRiskCode(riskCode);
                            prePayVo.setKindCode(kindCode);
                            prePayVo.setCaseTimes(payTimes);
                            prePayVo.setFeeType(CodeConstants.FeeType.FEE);
                            prePayVo.setChargeCode("07");
                            prePayVo.setChargeName("诉讼费");
                            prePayVo.setSerialNo(""+index);
                            index++;
                            prePayVo.setPayAmt(prePayFeeItem.getLawsuitFee());
                            if (null == prepayVosMap.get(riskCode)) {
                                List<PrpLPrePayVo> tmplist = new ArrayList<PrpLPrePayVo>();
                                tmplist.add(prePayVo);
                                prepayVosMap.put(riskCode, tmplist);
                            } else {
                                List<PrpLPrePayVo> tmplist = prepayVosMap.get(riskCode);
                                tmplist.add(prePayVo);
                                prepayVosMap.put(riskCode, tmplist);
                            }
                            compensatePayAmt = compensatePayAmt.add(prePayFeeItem.getLawsuitFee());
                        }
                        // 律师费
                        if (prePayFeeItem.getLawyerFee() != null && BigDecimal.ZERO.compareTo(prePayFeeItem.getLawyerFee()) < 0) {
                            PrpLPrePayVo prePayVo = new PrpLPrePayVo();
                            prePayVo.setCompensateNo(prepayNo);
                            prePayVo.setCreateTime(new Date());
                            prePayVo.setUpdateTime(new Date());
                            prePayVo.setCurrency(CodeConstants.Currency.CNY);
                            prePayVo.setPayStatus(CodeConstants.PayStatus.SENDPAY);
                            prePayVo.setPolicyNo(policyNo);
                            prePayVo.setRiskCode(riskCode);
                            prePayVo.setKindCode(kindCode);
                            prePayVo.setCaseTimes(payTimes);
                            prePayVo.setFeeType(CodeConstants.FeeType.FEE);
                            prePayVo.setChargeCode("06");
                            prePayVo.setChargeName("律师费");
                            prePayVo.setSerialNo(""+index);
                            index++;
                            prePayVo.setPayAmt(prePayFeeItem.getLawyerFee());
                            if (null == prepayVosMap.get(riskCode)) {
                                List<PrpLPrePayVo> tmplist = new ArrayList<PrpLPrePayVo>();
                                tmplist.add(prePayVo);
                                prepayVosMap.put(riskCode, tmplist);
                            } else {
                                List<PrpLPrePayVo> tmplist = prepayVosMap.get(riskCode);
                                tmplist.add(prePayVo);
                                prepayVosMap.put(riskCode, tmplist);
                            }
                            compensatePayAmt = compensatePayAmt.add(prePayFeeItem.getLawyerFee());
                        }
                        // 检验费
                        if (prePayFeeItem.getCheckFee() != null && BigDecimal.ZERO.compareTo(prePayFeeItem.getCheckFee()) < 0) {
                            PrpLPrePayVo prePayVo = new PrpLPrePayVo();
                            prePayVo.setCompensateNo(prepayNo);
                            prePayVo.setCreateTime(new Date());
                            prePayVo.setUpdateTime(new Date());
                            prePayVo.setCurrency(CodeConstants.Currency.CNY);
                            prePayVo.setPayStatus(CodeConstants.PayStatus.SENDPAY);
                            prePayVo.setPolicyNo(policyNo);
                            prePayVo.setRiskCode(riskCode);
                            prePayVo.setKindCode(kindCode);
                            prePayVo.setCaseTimes(payTimes);
                            prePayVo.setFeeType(CodeConstants.FeeType.FEE);
                            prePayVo.setChargeCode("15");
                            prePayVo.setChargeName("检验费");
                            prePayVo.setSerialNo(""+index);
                            index++;
                            prePayVo.setPayAmt(prePayFeeItem.getCheckFee());
                            if (null == prepayVosMap.get(riskCode)) {
                                List<PrpLPrePayVo> tmplist = new ArrayList<PrpLPrePayVo>();
                                tmplist.add(prePayVo);
                                prepayVosMap.put(riskCode, tmplist);
                            } else {
                                List<PrpLPrePayVo> tmplist = prepayVosMap.get(riskCode);
                                tmplist.add(prePayVo);
                                prepayVosMap.put(riskCode, tmplist);
                            }
                            compensatePayAmt = compensatePayAmt.add(prePayFeeItem.getCheckFee());
                        }
                        // 执行费
                        if (prePayFeeItem.getExecuteFee() != null && BigDecimal.ZERO.compareTo(prePayFeeItem.getExecuteFee()) < 0) {
                            PrpLPrePayVo prePayVo = new PrpLPrePayVo();
                            prePayVo.setCompensateNo(prepayNo);
                            prePayVo.setCreateTime(new Date());
                            prePayVo.setUpdateTime(new Date());
                            prePayVo.setCurrency(CodeConstants.Currency.CNY);
                            prePayVo.setPayStatus(CodeConstants.PayStatus.SENDPAY);
                            prePayVo.setPolicyNo(policyNo);
                            prePayVo.setRiskCode(riskCode);
                            prePayVo.setKindCode(kindCode);
                            prePayVo.setCaseTimes(payTimes);
                            prePayVo.setFeeType(CodeConstants.FeeType.FEE);
                            prePayVo.setChargeCode("07");
                            prePayVo.setChargeName("执行费");
                            prePayVo.setSerialNo(""+index);
                            index++;
                            prePayVo.setPayAmt(prePayFeeItem.getExecuteFee());
                            if (null == prepayVosMap.get(riskCode)) {
                                List<PrpLPrePayVo> tmplist = new ArrayList<PrpLPrePayVo>();
                                tmplist.add(prePayVo);
                                prepayVosMap.put(riskCode, tmplist);
                            } else {
                                List<PrpLPrePayVo> tmplist = prepayVosMap.get(riskCode);
                                tmplist.add(prePayVo);
                                prepayVosMap.put(riskCode, tmplist);
                            }
                            compensatePayAmt = compensatePayAmt.add(prePayFeeItem.getExecuteFee());
                        }
                        // 公估费
                        if (prePayFeeItem.getEvaluationFee() != null && BigDecimal.ZERO.compareTo(prePayFeeItem.getEvaluationFee()) < 0) {
                            PrpLPrePayVo prePayVo = new PrpLPrePayVo();
                            prePayVo.setCompensateNo(prepayNo);
                            prePayVo.setCreateTime(new Date());
                            prePayVo.setUpdateTime(new Date());
                            prePayVo.setCurrency(CodeConstants.Currency.CNY);
                            prePayVo.setPayStatus(CodeConstants.PayStatus.SENDPAY);
                            prePayVo.setPolicyNo(policyNo);
                            prePayVo.setRiskCode(riskCode);
                            prePayVo.setKindCode(kindCode);
                            prePayVo.setCaseTimes(payTimes);
                            prePayVo.setFeeType(CodeConstants.FeeType.FEE);
                            prePayVo.setChargeCode("13");
                            prePayVo.setChargeName("公估费");
                            prePayVo.setSerialNo(""+index);
                            index++;
                            prePayVo.setPayAmt(prePayFeeItem.getEvaluationFee());
                            if (null == prepayVosMap.get(riskCode)) {
                                List<PrpLPrePayVo> tmplist = new ArrayList<PrpLPrePayVo>();
                                tmplist.add(prePayVo);
                                prepayVosMap.put(riskCode, tmplist);
                            } else {
                                List<PrpLPrePayVo> tmplist = prepayVosMap.get(riskCode);
                                tmplist.add(prePayVo);
                                prepayVosMap.put(riskCode, tmplist);
                            }
                            compensatePayAmt = compensatePayAmt.add(prePayFeeItem.getEvaluationFee());
                        }
                        // 前置调查费
                        if (prePayFeeItem.getPreInvestigateFee() != null && BigDecimal.ZERO.compareTo(prePayFeeItem.getPreInvestigateFee()) < 0) {
                            PrpLPrePayVo prePayVo = new PrpLPrePayVo();
                            prePayVo.setCompensateNo(prepayNo);
                            prePayVo.setCreateTime(new Date());
                            prePayVo.setUpdateTime(new Date());
                            prePayVo.setCurrency(CodeConstants.Currency.CNY);
                            prePayVo.setPayStatus(CodeConstants.PayStatus.SENDPAY);
                            prePayVo.setPolicyNo(policyNo);
                            prePayVo.setRiskCode(riskCode);
                            prePayVo.setKindCode(kindCode);
                            prePayVo.setCaseTimes(payTimes);
                            prePayVo.setFeeType(CodeConstants.FeeType.FEE);
                            prePayVo.setChargeCode("16");
                            prePayVo.setChargeName("前置调查费");
                            prePayVo.setSerialNo(""+index);
                            index++;
                            prePayVo.setPayAmt(prePayFeeItem.getPreInvestigateFee());
                            if (null == prepayVosMap.get(riskCode)) {
                                List<PrpLPrePayVo> tmplist = new ArrayList<PrpLPrePayVo>();
                                tmplist.add(prePayVo);
                                prepayVosMap.put(riskCode, tmplist);
                            } else {
                                List<PrpLPrePayVo> tmplist = prepayVosMap.get(riskCode);
                                tmplist.add(prePayVo);
                                prepayVosMap.put(riskCode, tmplist);
                            }
                            compensatePayAmt = compensatePayAmt.add(prePayFeeItem.getPreInvestigateFee());
                        }

                        //******************************理算主表数据 start **************************
                        //判断预赔得map集合中是否存在内容，如果存在则需要组装compensate数据
                        if (null == riskCompMap.get(riskCode) && prepayVosMap.containsKey(riskCode)) {
                            PrpLCompensateVo compensateVo = new PrpLCompensateVo();
                            compensateVo.setCompensateNo(prepayNo);
                            List<PrpLClaimVo> claimVoList = claimService.findprpLClaimVoListByRegistAndPolicyNo(registNo, policyNo, "1");
                            PrpLClaimVo claimVo = new PrpLClaimVo();
                            if (claimVoList != null && claimVoList.size() > 0) {
                                for (PrpLClaimVo prpLClaimVo : claimVoList) {
                                    if (riskCode.equals(prpLClaimVo.getRiskCode())) {
                                        claimVo = prpLClaimVo;
                                        break;
                                    }
                                }
                            }
                            compensateVo.setClaimNo(claimVo.getClaimNo());
                            compensateVo.setRegistNo(registNo);
                            compensateVo.setPolicyNo(policyNo);
                            compensateVo.setRiskCode(riskCode);
                            compensateVo.setMakeCom(claimVo.getComCode());
                            compensateVo.setComCode(claimVo.getComCode());
                            compensateVo.setCaseType(CodeConstants.CompCaseType.NORMAL_CASE);
                            compensateVo.setCompensateType(CodeConstants.CompensateType.prepay_type);
                            PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo, 1);
                            if (null != checkDutyVo) {
                                compensateVo.setIndemnityDuty(checkDutyVo.getIndemnityDuty());
                                compensateVo.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate());
                            }
                            // 预付总金额 = 预付赔款 + 预付费用
                            compensateVo.setSumAmt(compensatePayAmt);
                            compensateVo.setAllLossFlag("0");
                            compensateVo.setRecoveryFlag("0");
                            compensateVo.setCreateUser("AUTO");
                            compensateVo.setUnderwriteFlag(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE);
                            compensateVo.setUnderwriteUser("AUTO");
                            compensateVo.setUnderwriteDate(date);
                            compensateVo.setCreateTime(date);
                            compensateVo.setUpdateTime(date);
                            compensateVo.setTimes(payTimes);
                            compensateVo.setCurrency(CodeConstants.Currency.CNY);
                            compensateVo.setDeductType(CodeConstants.DeductType.DEDUCT_PRE);
                            compensateVo.setLastModifyUser("AUTO");
                            compensateVo.setHandler1Code("AUTO");

                            PrpLCompensateExtVo compensateExtVo = new PrpLCompensateExtVo();
                            compensateExtVo.setRegistNo(registNo);
                            compensateExtVo.setPayBackState("1");
                            compensateExtVo.setCreateTime(date);
                            compensateExtVo.setCreateTime(date);
                            compensateExtVo.setWriteOffFlag("0");

                            if (compensatePayAmt.compareTo(BigDecimal.ZERO) > 0 && compensatePayAmt.compareTo(new BigDecimal("50000.00")) < -1) {
                                compensateExtVo.setIsFastReparation("0");
                                compensateExtVo.setIsAutoPay("1");
                            }
                            compensateExtVo.setIsCompDeduct("0");
                            compensateVo.setPrpLCompensateExt(compensateExtVo);

                            //******************************理算主表数据  end  **************************
                            riskCompMap.put(riskCode, compensateVo);
                        //如果compensateMap集合中存在对应险种得riskCode值，并且预赔mao集合中也存在对应得险种预赔数据
                        //则需要把金额重新更新到compensate集合中
                        }else if(riskCompMap.containsKey(riskCode) && prepayVosMap.containsKey(riskCode)){
                            PrpLCompensateVo compensateVo = new PrpLCompensateVo();
                            compensateVo = riskCompMap.get(riskCode);
                            compensateVo.setSumAmt(compensatePayAmt);
                            PrpLCompensateExtVo compensateExtVo = new PrpLCompensateExtVo();
                            compensateExtVo = compensateVo.getPrpLCompensateExt();
                            if (compensatePayAmt.compareTo(BigDecimal.ZERO) > 0 && compensatePayAmt.compareTo(new BigDecimal("50000.00")) < -1) {
                                compensateExtVo.setIsFastReparation("0");
                                compensateExtVo.setIsAutoPay("1");
                            }
                            compensateVo.setPrpLCompensateExt(compensateExtVo);
                            riskCompMap.put(riskCode, compensateVo);
                        }

                    }
                    for (Map.Entry<String, PrpLCompensateVo> entry : riskCompMap.entrySet()) {
                        String riskCode = entry.getKey();
                        PrpLCompensateVo compensateVo = entry.getValue();
                        SysUserVo userVo = new SysUserVo();
                        userVo.setUserCode("AUTO");
                        userVo.setUserName("AUTO");
                        userVo.setComCode(compensateVo.getComCode());
                        List<PrpLPrePayVo> prePayPVos = prepayVosMap.get(riskCode);

                        compensateService.saveOrUpdateCompensateVo(compensateVo, userVo);
                        compensateService.saveOrUpdatePrePay(prePayPVos, compensateVo.getCompensateNo());

                        PrpLWfTaskVo prpLWfTaskVo = this.createPrePayTask(compensateVo);
                        // 提交预付任务会返回核赔工作流对象
                        prpLWfTaskVo = this.submitPrepayTask(prpLWfTaskVo, compensateVo, userVo);
                        this.submitVclaimTask(compensateVo, prpLWfTaskVo);
                    }

                } else {
                    resultBean.fail("平安联盟-预付查询接口数据结果为空！");
                }
            } catch (Exception e) {
                logger.info("平安联盟-预付查询接口数据结果处理异常！", e);
                resultBean.fail("平安联盟-预付查询接口数据结果处理异常！");
            }
        } else {
            resultBean.fail("平安联盟-预付查询接口数据结果为空！");
        }
        return resultBean;
    }

    /**
     * 生成预付节点
     */
    private PrpLWfTaskVo createPrePayTask(PrpLCompensateVo compensateVo) throws Exception {
        WfTaskSubmitVo submitVo = new WfTaskSubmitVo();

        try {
            if("1101".equals(compensateVo.getRiskCode())){
                submitVo.setCurrentNode(FlowNode.ClaimCI);
                submitVo.setNextNode(FlowNode.PrePayCI);
            }else{
                submitVo.setCurrentNode(FlowNode.ClaimBI);
                submitVo.setNextNode(FlowNode.PrePayBI);
            }
            List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.findPrpLWfTaskOutByRegistNo(compensateVo.getRegistNo());
            for (PrpLWfTaskVo tmpTaskVo : wfTaskVoList) {
                if (tmpTaskVo.getSubNodeCode().equals(submitVo.getCurrentNode().name())) {
                    submitVo.setFlowTaskId(tmpTaskVo.getTaskId());
                    submitVo.setFlowId(tmpTaskVo.getFlowId());
                    break;
                }
            }
            submitVo.setComCode(compensateVo.getComCode());
            submitVo.setTaskInUser("AUTO");
            submitVo.setTaskInKey(compensateVo.getClaimNo());
            submitVo.setHandleIdKey(compensateVo.getCompensateNo());
            submitVo.setAssignCom(compensateVo.getComCode());
            PrpLClaimVo claimVo = claimService.findByClaimNo(compensateVo.getClaimNo());
            return wfTaskHandleService.addPrePayTask(claimVo, submitVo);
        } catch (Exception e) {
            logger.info("报案号：{} 预付节点生成异常！{}", compensateVo.getRegistNo(), e);
            throw e;
        }
    }

    /**
     * 预付提交
     * @param prpLWfTaskVo
     * @param compensateVo
     * @param userVo
     * @throws Exception
     */
    private PrpLWfTaskVo submitPrepayTask(PrpLWfTaskVo prpLWfTaskVo, PrpLCompensateVo compensateVo, SysUserVo userVo) throws Exception {
        try {
            PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(prpLWfTaskVo.getTaskId().toString()));
            WfTaskSubmitVo submitVo = new WfTaskSubmitVo();

            submitVo.setFlowTaskId(wfTaskVo.getTaskId());
            submitVo.setTaskInKey(compensateVo.getCompensateNo());
            submitVo.setTaskInUser(userVo.getUserCode());
            submitVo.setFlowId(wfTaskVo.getFlowId());
            submitVo.setFlowTaskId(wfTaskVo.getTaskId());
            submitVo.setAssignUser("AUTO");
            submitVo.setHandleruser("AUTO");

            submitVo.setCurrentNode(FlowNode.valueOf(wfTaskVo.getSubNodeCode()));
            submitVo.setComCode(compensateVo.getComCode());
            submitVo.setNextNode(FlowNode.VClaim);

            PrpLWfTaskVo nextTaskVo = wfTaskHandleService.submitPrepay(compensateVo, submitVo);
            return nextTaskVo;
        } catch (Exception e) {
            logger.info("报案号：" + compensateVo.getRegistNo() + " 预付提交异常！{}", e);
            throw e;
        }
    }

    /**
     * 核赔任务提交
     * @throws Exception
     */
    private void submitVclaimTask(PrpLCompensateVo compensateVo, PrpLWfTaskVo wfTaskVo) throws Exception {
        try {
            WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
            submitVo.setFlowId(wfTaskVo.getFlowId());
            submitVo.setFlowTaskId(wfTaskVo.getTaskId());
            submitVo.setComCode(compensateVo.getComCode());
            submitVo.setTaskInUser("AUTO");
            submitVo.setTaskInKey(compensateVo.getCompensateNo());
            submitVo.setHandleIdKey(compensateVo.getClaimNo());
            submitVo.setAssignUser("AUTO");
            submitVo.setAssignCom(compensateVo.getComCode());
            submitVo.setCurrentNode(FlowNode.VClaim);
            submitVo.setNextNode(FlowNode.END);
            WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
            taskVo.setRegistNo(compensateVo.getRegistNo());
            taskVo.setHandlerIdKey(compensateVo.getCompensateNo());
            taskVo.setItemName("预付核赔");
            taskVo.setClaimNo(compensateVo.getClaimNo());
            wfTaskHandleService.submitSimpleTask(taskVo,submitVo);
        } catch (Exception e) {
            logger.info("报案号：{} 预付提交异常！{}", compensateVo.getRegistNo(), e);
            throw e;
        }

    }

}
