package ins.sino.claimcar.pinganunion.vo.padpay;

import java.io.Serializable;

/**
 * 平安联盟中心-请求参数类
 *
 * @author mfn
 * @date 2020/7/28 18:10
 */
public class UnionPadPayRequestParamDto implements Serializable {
    private static final long serialVersionUID = -6118934169002128696L;

    /**  垫付主键    是否非空：Y  编码：N */
    private String idClmAdvanceInfo;

    public String getIdClmAdvanceInfo() {
        return idClmAdvanceInfo;
    }

    public void setIdClmAdvanceInfo(String idClmAdvanceInfo) {
        this.idClmAdvanceInfo = idClmAdvanceInfo;
    }
}
