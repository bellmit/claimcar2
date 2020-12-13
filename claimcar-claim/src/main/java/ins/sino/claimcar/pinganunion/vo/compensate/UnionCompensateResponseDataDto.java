package ins.sino.claimcar.pinganunion.vo.compensate;

import java.io.Serializable;
import java.util.List;

/**
 * 平安联盟中心-理算查询接口-查询结果
 *
 * @author mfn
 * @date 2020/7/29 16:07
 */
public class UnionCompensateResponseDataDto implements Serializable {
    private static final long serialVersionUID = 215715300845280893L;

    /**  保单赔付信息    是否非空：N  编码：N */
    private List<UnionCompensatePolicyPayDto> policyPayDTOList;
    /**  责任赔付信息    是否非空：N  编码：N */
    private List<UnionCompensatePlanDutyPayDto> planDutyPayDTOList;
    /**  险种赔付信息    是否非空：N  编码：N */
    private List<UnionCompensatePlanPayDto> planPayDTOList;
    /**  保单批次赔付信息    是否非空：N  编码：N */
    private List<UnionCompensatePolicyBatchPayDto> policyBatchPayDTOList;
    /**  整案基本信息    是否非空：N  编码：N */
    private UnionCompensateWholeCaseBaseDto wholeCaseBaseDTO;

    public List<UnionCompensatePolicyPayDto> getPolicyPayDTOList() {
        return policyPayDTOList;
    }

    public void setPolicyPayDTOList(List<UnionCompensatePolicyPayDto> policyPayDTOList) {
        this.policyPayDTOList = policyPayDTOList;
    }

    public List<UnionCompensatePlanDutyPayDto> getPlanDutyPayDTOList() {
        return planDutyPayDTOList;
    }

    public void setPlanDutyPayDTOList(List<UnionCompensatePlanDutyPayDto> planDutyPayDTOList) {
        this.planDutyPayDTOList = planDutyPayDTOList;
    }

    public List<UnionCompensatePlanPayDto> getPlanPayDTOList() {
        return planPayDTOList;
    }

    public void setPlanPayDTOList(List<UnionCompensatePlanPayDto> planPayDTOList) {
        this.planPayDTOList = planPayDTOList;
    }

    public List<UnionCompensatePolicyBatchPayDto> getPolicyBatchPayDTOList() {
        return policyBatchPayDTOList;
    }

    public void setPolicyBatchPayDTOList(List<UnionCompensatePolicyBatchPayDto> policyBatchPayDTOList) {
        this.policyBatchPayDTOList = policyBatchPayDTOList;
    }

    public UnionCompensateWholeCaseBaseDto getWholeCaseBaseDTO() {
        return wholeCaseBaseDTO;
    }

    public void setWholeCaseBaseDTO(UnionCompensateWholeCaseBaseDto wholeCaseBaseDTO) {
        this.wholeCaseBaseDTO = wholeCaseBaseDTO;
    }
}
