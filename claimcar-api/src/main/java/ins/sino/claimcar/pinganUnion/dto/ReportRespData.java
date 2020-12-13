package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 案件基本信息查询接口响应对象
 * @Author liuys
 * @Date 2020/8/13 14:27
 */
public class ReportRespData implements Serializable {
    //案件基本信息
    private ReportInfoDTO reportInfoDTO;
    //案件事故信息
    private ReportAccidentDTO reportAccidentDTO;
    //整案基本信息
    private WholeCaseBaseDTO wholeCaseBaseDTO;
    //保单案件信息
    private List<CaseBaseDTO> caseBaseDTOList;
    //保单信息
    private List<PolicyInfoDTO> policyInfoDTOList;

    public ReportInfoDTO getReportInfoDTO() {
        return reportInfoDTO;
    }

    public void setReportInfoDTO(ReportInfoDTO reportInfoDTO) {
        this.reportInfoDTO = reportInfoDTO;
    }

    public ReportAccidentDTO getReportAccidentDTO() {
        return reportAccidentDTO;
    }

    public void setReportAccidentDTO(ReportAccidentDTO reportAccidentDTO) {
        this.reportAccidentDTO = reportAccidentDTO;
    }

    public WholeCaseBaseDTO getWholeCaseBaseDTO() {
        return wholeCaseBaseDTO;
    }

    public void setWholeCaseBaseDTO(WholeCaseBaseDTO wholeCaseBaseDTO) {
        this.wholeCaseBaseDTO = wholeCaseBaseDTO;
    }

    public List<CaseBaseDTO> getCaseBaseDTOList() {
        return caseBaseDTOList;
    }

    public void setCaseBaseDTOList(List<CaseBaseDTO> caseBaseDTOList) {
        this.caseBaseDTOList = caseBaseDTOList;
    }

    public List<PolicyInfoDTO> getPolicyInfoDTOList() {
        return policyInfoDTOList;
    }

    public void setPolicyInfoDTOList(List<PolicyInfoDTO> policyInfoDTOList) {
        this.policyInfoDTOList = policyInfoDTOList;
    }
}
