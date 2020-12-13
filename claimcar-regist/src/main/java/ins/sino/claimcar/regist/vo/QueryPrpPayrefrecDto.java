package ins.sino.claimcar.regist.vo;

import java.io.Serializable;

/**
 * 保单实收请求实体
 *
 * @author maofengning
 * @date 2020/7/8 15:22
 */
public class QueryPrpPayrefrecDto implements Serializable {

    private static final long serialVersionUID = -1622708230924768309L;

    /** 业务类型 P-保单 是否必录：Y */
    private String certiType;
    /** 归属机构 是否必录：N */
    private String comCode;
    /** 业务号 是否必录：Y */
    private String certiNo;

    public String getCertiType() {
        return certiType;
    }

    public void setCertiType(String certiType) {
        this.certiType = certiType;
    }

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    public String getCertiNo() {
        return certiNo;
    }

    public void setCertiNo(String certiNo) {
        this.certiNo = certiNo;
    }
}
