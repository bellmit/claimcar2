package ins.sino.claimcar.pinganunion.vo.payCallback;

import java.io.Serializable;

/**
 * description: UnionPayCallbackRequestListDto 平安联盟中心-支付结果回调列表  list
 * date: 2020/8/18 8:54
 * author: lk
 * version: 1.0
 */
public class UnionPayCallbackRequestListDto implements Serializable {

    private static final long serialVersionUID = 5324602638190462153L;
    /**
     * 支付主键    是否非空：Y  编码：N  备注：支付主键
     */
    private String idClmPaymentResult;
    /**
     * 通知单状态    是否非空：Y  编码：Y  备注：00-支付成功, 01-支付失败
     */
    private String noticeStatus;
    /**
     * 退回原因    是否非空：N  编码：N  备注：退回原因
     */
    private String backReason;
    /**
     * 支付成功时间    是否非空：CY  编码：N  备注：payDate、backDate不同时为空
     */
    private String payDate;
    /**
     * 退回时间    是否非空：CY  编码：N  备注：payDate、backDate不同时为空
     */
    private String backDate;

    public String getIdClmPaymentResult() {
        return idClmPaymentResult;
    }

    public void setIdClmPaymentResult(String idClmPaymentResult) {
        this.idClmPaymentResult = idClmPaymentResult;
    }

    public String getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(String noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    public String getBackReason() {
        return backReason;
    }

    public void setBackReason(String backReason) {
        this.backReason = backReason;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getBackDate() {
        return backDate;
    }

    public void setBackDate(String backDate) {
        this.backDate = backDate;
    }
}
