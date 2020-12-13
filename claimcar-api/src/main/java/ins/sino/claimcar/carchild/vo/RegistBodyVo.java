package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class RegistBodyVo implements Serializable{

    /**  */
    private static final long serialVersionUID = -7831400413768899519L;
    
    @XStreamAlias("REPORTINFO")
    private ReportInfoVo reportInfo;//报案信息
    
    @XStreamAlias("SCHEDULEITEMLIST")
    private List<ScheduleItemVo> scheduleItems;//调度信息
    
    @XStreamAlias("POLICYLIST")
    private List<PolicyInfoVo> policyInfos;//保单信息

    @XStreamAlias("HISCLAIMINFO")
    private HisclaimInfoVo hisclaimInfo;//历史赔案基础信息
    
    public ReportInfoVo getReportInfo() {
        return reportInfo;
    }

    
    public void setReportInfo(ReportInfoVo reportInfo) {
        this.reportInfo = reportInfo;
    }

    
    public List<ScheduleItemVo> getScheduleItems() {
        return scheduleItems;
    }

    
    public void setScheduleItems(List<ScheduleItemVo> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }

    
    public List<PolicyInfoVo> getPolicyInfos() {
        return policyInfos;
    }

    
    public void setPolicyInfos(List<PolicyInfoVo> policyInfos) {
        this.policyInfos = policyInfos;
    }


    
    public HisclaimInfoVo getHisclaimInfo() {
        return hisclaimInfo;
    }


    
    public void setHisclaimInfo(HisclaimInfoVo hisclaimInfo) {
        this.hisclaimInfo = hisclaimInfo;
    }
    
}
