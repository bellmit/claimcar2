package ins.sino.claimcar.claimMarketing.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * description: ClaimInfoVo
 * date: 2020/9/25 15:19
 * author: lk
 * version: 1.0
 */
public class ClaimInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 赔案号
     */
    private String claimNo;

    /**
     * 报案号
     */
    private String registNo;

    /**
     * 保险起始日期
     */
    private String startDate;

    /**
     * 保险结束日期
     */
    private String endDate;

    /**
     * 出险时间
     */
    private String damageTime;

    /**
     * 出险地点
     */
    private String damageAddress;

    /**
     * 赔付金额
     */
    private String sumAmt;

    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public String getRegistNo() {
        return registNo;
    }

    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDamageTime() {
        return damageTime;
    }

    public void setDamageTime(String damageTime) {
        this.damageTime = damageTime;
    }

    public String getDamageAddress() {
        return damageAddress;
    }

    public void setDamageAddress(String damageAddress) {
        this.damageAddress = damageAddress;
    }

    public String getSumAmt() {
        return sumAmt;
    }

    public void setSumAmt(String sumAmt) {
        this.sumAmt = sumAmt;
    }
}
