package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;

/**
 * @Description 保单险种信息
 * @Author liuys
 * @Date 2020/7/21 17:28
 */
public class PolicyPlanDTO implements Serializable {
    //险种码
    private String planCode;
    //险种名称
    private String planName;

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }
}
