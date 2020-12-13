package ins.sino.claimcar.marketing.service;

import com.alibaba.dubbo.config.annotation.Service;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLClaimKindFeeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claimMarketing.service.ClaimMarketingService;
import ins.sino.claimcar.claimMarketing.vo.ClaimInfoVo;
import ins.sino.claimcar.claimMarketing.vo.ClaimMarketingDataVo;
import ins.sino.claimcar.claimMarketing.vo.ClaimMarketingQueryVo;
import ins.sino.claimcar.claimMarketing.vo.ClaimMarketingResponseDataVo;
import ins.sino.claimcar.commom.vo.StringUtils;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * description: ClaimMarketingServiceImpl 销管系统请求服务类
 * date: 2020/9/25 16:36
 * author: lk
 * version: 2.0
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("claimMarketingService")
public class ClaimMarketingServiceImpl implements ClaimMarketingService {
    private static Logger logger = LoggerFactory.getLogger(ClaimMarketingServiceImpl.class);

    @Autowired
    private CompensateService compensateService;

    @Autowired
    private PolicyViewService policyViewService;

    @Autowired
    private ClaimService claimService;

    @Autowired
    private RegistQueryService registQueryService;

    /**
     * 销管系统获取理赔信息接口
     *
     * @param queryVo 查询参数
     * @return
     */
    @Override
    public ClaimMarketingResponseDataVo getClaimInfo(ClaimMarketingQueryVo queryVo) {
        ClaimMarketingResponseDataVo responseDataVo = new ClaimMarketingResponseDataVo();
        ClaimMarketingDataVo claimMarketingDataVo = new ClaimMarketingDataVo();
        if (null == queryVo) {
            throw new IllegalArgumentException("接收查询参数为空");
        }

        String policyNo = queryVo.getPolicyNo();
        String sumExpiration = queryVo.getSumExpiration();
        //校验参数
        if (StringUtils.isBlank(policyNo)) {
            throw new IllegalArgumentException("保单号不能为空");
        }
        if (StringUtils.isBlank(sumExpiration)) {
            throw new IllegalArgumentException("满期保费不能为空");
        }
        try {
            List<PrpLRegistVo> prpLRegist = registQueryService.findPrpLRegistByPolicyNo(policyNo);
            //未决合计金额
            BigDecimal sumNoPay = new BigDecimal("0.00");
            //已决合计金额
            BigDecimal sumPayed = new BigDecimal("0.00");
            //年度
            String year = "";
            //保费收入
            String sumPremium = "";
            //已决件数
            Integer isDealNum = 0;
            //未决件数
            Integer outStandingNum = 0;
            if (null != prpLRegist && !prpLRegist.isEmpty()) {
                    List<ClaimInfoVo> claimInfoVos = new ArrayList<ClaimInfoVo>();
                    for (PrpLRegistVo prpLRegistVo : prpLRegist) {
                        String registNo = prpLRegistVo.getRegistNo();
                        List<PrpLClaimVo> prpLClaimVos = claimService.findprpLClaimVoListByRegistAndPolicyNo(registNo, policyNo, "1");
                        if (null != prpLClaimVos && !prpLClaimVos.isEmpty()) {
                            for (PrpLClaimVo prpLClaimVo : prpLClaimVos) {
                                //组装赔案详细信息
                                ClaimInfoVo claimInfoVo = new ClaimInfoVo();
                                //出险地点
                                String damageAddress = "";
                                if (null != prpLRegistVo.getDamageAddress()) {
                                    damageAddress = prpLRegistVo.getDamageAddress();
                                }
                                claimInfoVo.setDamageAddress(damageAddress);
                                //报案号
                                claimInfoVo.setRegistNo(registNo);
                                //立案号
                                String claimNo = "";
                                if (null != prpLClaimVo.getClaimNo()) {
                                    claimNo = prpLClaimVo.getClaimNo();
                                }
                                claimInfoVo.setClaimNo(claimNo);
                                //出险时间
                                Date damageTime = new Date();
                                if (null != prpLRegistVo.getDamageTime()) {
                                    damageTime = prpLRegistVo.getDamageTime();
                                }
                                claimInfoVo.setDamageTime(DateUtils.dateToStr(damageTime, DateUtils.YToSec));
                                PrpLCMainVo prpLCMainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(registNo, policyNo);
                                //保险起始日期
                                Date startDate = new Date();
                                //保险结束日期
                                Date endDate = new Date();
                                if (null != prpLCMainVo) {
                                    if (null != prpLCMainVo.getStartDate()) {
                                        startDate = prpLCMainVo.getStartDate();
                                        year = DateUtils.dateToStr(startDate, "yyyy");
                                    }
                                    if (null != prpLCMainVo.getEndDate()) {
                                        endDate = prpLCMainVo.getEndDate();
                                    }
                                    ;
                                    if (null != prpLCMainVo.getSumPremium()) {
                                        sumPremium = prpLCMainVo.getSumPremium().toString();
                                    }
                                }
                                claimInfoVo.setStartDate(DateUtils.dateToStr(startDate, DateUtils.YToSec));
                                claimInfoVo.setEndDate(DateUtils.dateToStr(endDate, DateUtils.YToSec));
                                //已决赔款
                                BigDecimal paidMount = new BigDecimal("0.00");
                                //未决赔款
                                BigDecimal noPayMount = new BigDecimal("0.00");
                                //赔付金额
                                BigDecimal payAmt = new BigDecimal(0.00);
                                List<PrpLCompensateVo> compensateList = compensateService.findCompensateByClaimno(claimNo, "N");
                                if (null != compensateList && !compensateList.isEmpty()) {
                                    isDealNum += 1;
                                    for (PrpLCompensateVo prpLCompensateVo : compensateList) {
                                        //赔款金额
                                        BigDecimal sumAmt = prpLCompensateVo.getSumAmt();
                                        if (null != sumAmt) {
                                            payAmt = payAmt.add(sumAmt);
                                        }
                                        //费用金额
                                        BigDecimal sumFee = prpLCompensateVo.getSumFee();
                                        if (null != sumFee) {
                                            payAmt = payAmt.add(sumFee);
                                        }
                                    }
                                    paidMount = paidMount.add(payAmt);
                                } else {
                                    outStandingNum += 1;
                                    //赔款
                                    if (null != prpLClaimVo.getSumClaim()) {
                                        noPayMount = noPayMount.add(prpLClaimVo.getSumClaim());
                                    }
                                    //费用
                                    List<PrpLClaimKindFeeVo> prpLClaimKindFees = prpLClaimVo.getPrpLClaimKindFees();
                                    if (null != prpLClaimKindFees && !prpLClaimKindFees.isEmpty()) {
                                        for (PrpLClaimKindFeeVo prpLClaimKindFee : prpLClaimKindFees) {
                                            BigDecimal payFee = prpLClaimKindFee.getPayFee();
                                            if (null != payFee) {
                                                noPayMount = noPayMount.add(payFee);
                                            }
                                        }
                                    }
                                }
                                claimInfoVo.setSumAmt(payAmt.toString());
                                //未决总金额
                                sumNoPay = sumNoPay.add(noPayMount);
                                //已决总金额
                                sumPayed = sumPayed.add(paidMount);

                                claimInfoVos.add(claimInfoVo);
                            }
                        }
                    }
                    //未决加已决总计金额
                    BigDecimal amt = sumNoPay.add(sumPayed);
                    if ("0".equals(sumExpiration)) {
                        claimMarketingDataVo.setExpirationDateRate("0");
                    } else {
                        claimMarketingDataVo.setExpirationDateRate(amt.divide(new BigDecimal(sumExpiration), 4, RoundingMode.HALF_UP).toString());
                    }
                    claimMarketingDataVo.setIsDealNum(isDealNum.toString());
                    claimMarketingDataVo.setOutStandingNum(outStandingNum.toString());
                    claimMarketingDataVo.setSumPremium(sumPremium);
                    claimMarketingDataVo.setYear(year);
                    claimMarketingDataVo.setSumOutStanding(sumNoPay.toString());
                    claimMarketingDataVo.setSumDealStanding(sumPayed.toString());
                    claimMarketingDataVo.setClaimInfoList(claimInfoVos);
            }


        } catch (Exception e) {
            throw new IllegalArgumentException("系统异常，查询失败", e);
        }
        responseDataVo.setMessage("查询成功");
        responseDataVo.setStatus("1");
        responseDataVo.setData(claimMarketingDataVo);
        return responseDataVo;
    }
}
