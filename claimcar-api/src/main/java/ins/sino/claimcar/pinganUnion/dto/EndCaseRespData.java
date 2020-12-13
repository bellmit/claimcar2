package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;

/**
 * @Description 结案信息查询接口响应对象
 * @Author liuys
 * @Date 2020/8/13 15:19
 */
public class EndCaseRespData implements Serializable {
    //整案基本信息
    private WholeCaseBaseDTO wholeCaseBaseDTO;
    //零结注销信息
    private ZeroCancelApplyDTO zeroCancelApplyDTO;

    public WholeCaseBaseDTO getWholeCaseBaseDTO() {
        return wholeCaseBaseDTO;
    }

    public void setWholeCaseBaseDTO(WholeCaseBaseDTO wholeCaseBaseDTO) {
        this.wholeCaseBaseDTO = wholeCaseBaseDTO;
    }

    public ZeroCancelApplyDTO getZeroCancelApplyDTO() {
        return zeroCancelApplyDTO;
    }

    public void setZeroCancelApplyDTO(ZeroCancelApplyDTO zeroCancelApplyDTO) {
        this.zeroCancelApplyDTO = zeroCancelApplyDTO;
    }
}
