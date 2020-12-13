package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 未决变动信息查询接口响应对象
 * @Author liuys
 * @Date 2020/8/13 15:20
 */
public class EstimateRespData implements Serializable {
    private List<DutyEstimateDTO> dutyEstimateList;
    private List<ChannelEstimateDTO> channelEstimateList;

    public List<DutyEstimateDTO> getDutyEstimateList() {
        return dutyEstimateList;
    }

    public void setDutyEstimateList(List<DutyEstimateDTO> dutyEstimateList) {
        this.dutyEstimateList = dutyEstimateList;
    }

    public List<ChannelEstimateDTO> getChannelEstimateList() {
        return channelEstimateList;
    }

    public void setChannelEstimateList(List<ChannelEstimateDTO> channelEstimateList) {
        this.channelEstimateList = channelEstimateList;
    }
}
