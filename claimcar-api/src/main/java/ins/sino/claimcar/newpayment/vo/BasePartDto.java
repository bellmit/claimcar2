package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 收付回写车理赔公估费结算单号 数据实体
 *
 * @author maofengning
 * @date 2020/5/2 17:20
 */
public class BasePartDto implements Serializable {

    private static final long serialVersionUID = 2249711309022975173L;

    /** 结算单号 必录标识:Y 说明: */
    private String settleNo;
    /** 结算单类型 必录标识:N 说明:Y：预赔 C：实赔 Z：追偿 */
    private String certiType;
    /** 账号 必录标识:Y 说明: */
    private String accountCode;
    /** 操作类型 必录标识:Y 说明:1-生成结算单，0-注销结算单 */
    private String operateType;

    /** 公估费计算书明细 */
    private List<ItemVo> itemVo;

    public BasePartDto() {
    }

    public String getSettleNo() {
        return settleNo;
    }

    public void setSettleNo(String settleNo) {
        this.settleNo = settleNo;
    }

    public String getCertiType() {
        return certiType;
    }

    public void setCertiType(String certiType) {
        this.certiType = certiType;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public List<ItemVo> getItemVo() {
        return itemVo;
    }

    public void setItemVo(List<ItemVo> itemVo) {
        this.itemVo = itemVo;
    }
}
