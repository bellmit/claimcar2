package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 立案信息查询接口响应对象
 * @Author liuys
 * @Date 2020/8/13 15:11
 */
public class RegisterRespData implements Serializable {
    //整案基本信息
    private WholeCaseBaseDTO wholeCaseBaseDTO;
    //推修信息
    private List<RecommendRepairDTO> recommendRepairDTOList;

    public WholeCaseBaseDTO getWholeCaseBaseDTO() {
        return wholeCaseBaseDTO;
    }

    public void setWholeCaseBaseDTO(WholeCaseBaseDTO wholeCaseBaseDTO) {
        this.wholeCaseBaseDTO = wholeCaseBaseDTO;
    }

    public List<RecommendRepairDTO> getRecommendRepairDTOList() {
        return recommendRepairDTOList;
    }

    public void setRecommendRepairDTOList(List<RecommendRepairDTO> recommendRepairDTOList) {
        this.recommendRepairDTOList = recommendRepairDTOList;
    }
}
