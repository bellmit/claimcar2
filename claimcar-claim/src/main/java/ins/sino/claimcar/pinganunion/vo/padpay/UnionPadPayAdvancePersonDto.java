package ins.sino.claimcar.pinganunion.vo.padpay;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 垫付信息查询接口-人伤信息
 *
 * @author mfn
 * @date 2020/7/22 11:20
 */
public class UnionPadPayAdvancePersonDto implements Serializable {
    private static final long serialVersionUID = 5336475716784845893L;

    /**  通道号    是否非空：N  编码：N */
    private String idClmChannelProcess;
    /**  伤者姓名    是否非空：N  编码：N */
    private String personName;
    /**  伤者电话    是否非空：N  编码：N */
    private String personPhone;
    /**  我司审核后垫付医疗费用    是否非空：N  编码：N */
    private BigDecimal treatSum;

    public String getIdClmChannelProcess() {
        return idClmChannelProcess;
    }

    public void setIdClmChannelProcess(String idClmChannelProcess) {
        this.idClmChannelProcess = idClmChannelProcess;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonPhone() {
        return personPhone;
    }

    public void setPersonPhone(String personPhone) {
        this.personPhone = personPhone;
    }

    public BigDecimal getTreatSum() {
        return treatSum;
    }

    public void setTreatSum(BigDecimal treatSum) {
        this.treatSum = treatSum;
    }
}
