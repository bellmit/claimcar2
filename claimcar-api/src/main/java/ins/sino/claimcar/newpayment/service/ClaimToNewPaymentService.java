package ins.sino.claimcar.newpayment.service;

import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrplReplevyMainVo;
import ins.sino.claimcar.other.vo.PrpLAcheckMainVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.recloss.vo.PrpLRecLossVo;

/**
 * 车理赔对接新收付接口
 *
 * @author maofengning
 * @date 2020/5/6 18:34
 */
public interface ClaimToNewPaymentService {
    /**
     * 预付送收付
     *
     * @param compensateVo 预付计算书
     * @throws Exception 异常信息
     */
    void prePayToNewPayment(PrpLCompensateVo compensateVo) throws Exception;
    /**
     * 平安预付送收付，第一次默认失败
     *
     * @param compensateVo 预付计算书
     * @throws Exception 异常信息
     */
    void prePayToNewPaymentPingAn(PrpLCompensateVo compensateVo,String registNo) throws Exception;

    /**
     * 垫付送收付
     *
     * @param padPayMainVo 垫付计算书
     * @throws Exception 异常信息
     */
    void padPayToNewPayment(PrpLPadPayMainVo padPayMainVo) throws Exception;
    /**
     * 平安垫付送收付，第一次默认失败
     *
     * @param padPayMainVo 垫付计算书
     * @throws Exception 异常信息
     */
    void padPayToNewPaymentPingAn(PrpLPadPayMainVo padPayMainVo,String registNo) throws Exception;
    /**
     * 理算送收付
     *
     * @param compensateVo 理算计算书
     * @throws Exception 异常信息
     */
    void compensateToNewPayment(PrpLCompensateVo compensateVo) throws Exception;

    /**
     * 平安第一次送收付默认失败
     * @param compensateVo
     * @param registNo
     * @throws Exception
     */
    void compensateToNewPaymentPingAn(PrpLCompensateVo compensateVo,String registNo)throws Exception;

    /**
     * 追偿送收付
     *
     * @param prplReplevyMainVo 追偿信息
     * @throws Exception 异常信息
     */
    void recPayToNewPayment(PrplReplevyMainVo prplReplevyMainVo) throws Exception;

    /**
     * 损余回收送收付
     *
     * @param recLossVo 损余信息
     * @throws Exception 异常信息
     */
    void recLossToNewPayment(PrpLRecLossVo recLossVo) throws Exception;

    /**
     * 公估费送收付
     *
     * @param assessorMainVo 公估费主表信息
     * @param assessorFeeVo 公估费明细
     * @throws Exception 异常信息
     */
    void assessorToNewPayment(PrpLAssessorMainVo assessorMainVo, PrpLAssessorFeeVo assessorFeeVo) throws Exception;

    /**
     * 查勘费送收付
     * @param checkMainVo 查勘费主表对象
     * @param checkFeeVo 查勘费明细
     * @throws Exception 异常信息
     */
    void checkFeeToNewPayment(PrpLAcheckMainVo checkMainVo, PrpLCheckFeeVo checkFeeVo) throws Exception;
}
