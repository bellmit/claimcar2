package ins.sino.claimcar.claimMarketing.vo;

import java.io.Serializable;

/**
 * description: ClaimMarketingQueryVo
 * date: 2020/9/25 14:50
 * author: lk
 * version: 1.0
 */
public class ClaimMarketingQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 保单号
     */
    private String policyNo;
    /**
     * 满期保费，用于计算满期赔付率
     */
    private String sumExpiration;

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getSumExpiration() {
        return sumExpiration;
    }

    public void setSumExpiration(String sumExpiration) {
        this.sumExpiration = sumExpiration;
    }



}
